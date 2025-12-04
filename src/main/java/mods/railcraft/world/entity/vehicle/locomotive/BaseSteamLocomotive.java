package mods.railcraft.world.entity.vehicle.locomotive;

import mods.railcraft.RailcraftConfig;
import mods.railcraft.api.carts.FluidTransferHandler;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.particle.RailcraftParticleTypes;
import mods.railcraft.season.Seasons;
import mods.railcraft.sounds.RailcraftSoundEvents;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.util.container.ContainerMapper;
import mods.railcraft.util.fluids.FluidTools;
import mods.railcraft.util.fluids.FluidTools.ProcessType;
import mods.railcraft.world.level.material.StandardTank;
import mods.railcraft.world.level.material.TankManager;
import mods.railcraft.world.level.material.steam.SteamBoiler;
import mods.railcraft.world.level.material.steam.SteamConstants;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public abstract class BaseSteamLocomotive extends Locomotive implements FluidTransferHandler {

  protected static final int SLOT_WATER_INPUT = 0;
  protected static final int SLOT_WATER_PROCESSING = 1;
  protected static final int SLOT_WATER_OUTPUT = 2;

  private static final EntityDataAccessor<Boolean> SMOKE =
      SynchedEntityData.defineId(BaseSteamLocomotive.class, EntityDataSerializers.BOOLEAN);
  private static final EntityDataAccessor<Boolean> STEAM =
      SynchedEntityData.defineId(BaseSteamLocomotive.class, EntityDataSerializers.BOOLEAN);

  private static final byte TICKS_PER_BOILER_CYCLE = 2;
  private static final int FUEL_PER_REQUEST = 3;

  protected final StandardTank waterTank =
      StandardTank.ofBuckets(6)
          .fillProcessor((originalState) -> this.boiler.checkFill(originalState, this::explode))
          .filter(FluidTags.WATER);

  protected final StandardTank steamTank =
      StandardTank.ofBuckets(16)
          .filter(RailcraftTags.Fluids.STEAM)
          .disableExtract()
          .disableInsert();

  private final SteamBoiler boiler = new SteamBoiler(this.waterTank, this.steamTank)
      .setEfficiencyModifier(RailcraftConfig.SERVER.fuelPerSteamMultiplier.get())
      .setTicksPerCycle(TICKS_PER_BOILER_CYCLE);

  protected final ContainerMapper invWaterContainers =
      ContainerMapper.make(this, SLOT_WATER_INPUT, 3).ignoreItemChecks();

  private final TankManager tankManager = new TankManager(this.waterTank, this.steamTank);

  private int fluidProcessingTimer = 0;

  private FluidTools.ProcessState processState = FluidTools.ProcessState.RESET;

  protected BaseSteamLocomotive(EntityType<?> type, Level level) {
    super(type, level);
  }

  protected BaseSteamLocomotive(ItemStack itemStack, EntityType<?> type, Level level,
      double x, double y, double z) {
    super(itemStack, type, level, x, y, z);
  }

  @Override
  public Speed getMaxReverseSpeed() {
    return Speed.SLOWEST;
  }

  @Override
  protected void defineSynchedData(SynchedEntityData.Builder builder) {
    super.defineSynchedData(builder);
    builder.define(SMOKE, false);
    builder.define(STEAM, false);
  }

  @Override
  public boolean isAllowedMode(Mode mode) {
    return this.waterTank.isEmpty() && mode == Mode.SHUTDOWN || super.isAllowedMode(mode);
  }

  @Override
  public SoundEvent getWhistleSound() {
    return RailcraftSoundEvents.STEAM_WHISTLE.get();
  }

  @Override
  public InteractionResult interact(Player player, InteractionHand hand) {
    return FluidTools.interactWithFluidHandler(player, hand, this.getTankManager())
        ? InteractionResult.SUCCESS
        : super.interact(player, hand);
  }

  public TankManager getTankManager() {
    return this.tankManager;
  }

  @Override
  protected void serverTick(ServerLevel level) {
    super.serverTick(level);
    if (this.waterTank.isEmpty()) {
      this.setMode(Mode.SHUTDOWN);
    }
    this.setSteaming(this.steamTank.getFluidAmount() > 0);

    if (this.steamTank.getRemainingSpace() >= SteamConstants.STEAM_PER_UNIT_WATER
        || this.isShutdown()) {
      this.boiler.tick(level, 1);

      this.setSmoking(this.boiler.isBurning());

      // TODO: make venting a toggleable thing (why autodump while train has no coal??)
      if (!this.boiler.isBurning()) {
        this.ventSteam();
      }
    }

    if (++this.fluidProcessingTimer >= FluidTools.BUCKET_FILL_TIME) {
      this.fluidProcessingTimer = 0;
      this.processState = FluidTools.processContainer(this.invWaterContainers,
          this.waterTank, ProcessType.DRAIN_ONLY, this.processState);
    }
  }

  @Override
  protected void clientTick(Level level) {
    super.clientTick(level);
    // future information: renderYaw FACES at -x when at 0deg
    double rads = Math.toRadians(renderYaw);
    if (this.isSmoking()) {
      float offset = 0.4f;

      var x = this.getX() - Math.cos(rads) * offset;
      var y = this.getY() + 1.5;
      var z = this.getZ() - Math.sin(rads) * offset;

      SimpleParticleType particle;
      if (Seasons.isHalloween() && this.random.nextInt(4) == 0) { // 20%?
        particle = RailcraftParticleTypes.PUMPKIN.get();
      } else {
        // smog, obviously.
        particle = ParticleTypes.CAMPFIRE_COSY_SMOKE;
      }
      level.addParticle(particle, x, y, z, 0, 0.02, 0);
    }
    // steam spawns ON the engine itself, spreading left or right
    // as the pistons are on the train's sides
    if (this.isSteaming()) {
      float offset = 0.5f;
      double ninetyDeg = Math.toRadians(90) + Math.toRadians(this.random.nextInt(10)); // 10* bias
      double steamAngularSpeed = 0.01;
      double yCoord = this.getY() + 0.15;

      var vx = steamAngularSpeed * Math.cos(rads - ninetyDeg);
      var vz = steamAngularSpeed * Math.sin(rads - ninetyDeg);

      level.addParticle(RailcraftParticleTypes.STEAM.get(),
          this.getX() - Math.cos(rads + ninetyDeg) * offset, yCoord,
          this.getZ() - Math.sin(rads + ninetyDeg) * offset, vx,
          0.02 + (this.random.nextDouble() * 0.01), vz);

      level.addParticle(RailcraftParticleTypes.STEAM.get(),
          this.getX() - Math.cos(rads - ninetyDeg) * offset, yCoord,
          this.getZ() - Math.sin(rads - ninetyDeg) * offset, vx,
          0.02 + (this.random.nextDouble() * 0.01), vz);
    }
  }

  public boolean isSmoking() {
    return this.entityData.get(SMOKE);
  }

  private void setSmoking(boolean smoke) {
    this.entityData.set(SMOKE, smoke);
  }

  public boolean isSteaming() {
    return this.entityData.get(STEAM);
  }

  private void setSteaming(boolean steam) {
    this.entityData.set(STEAM, steam);
  }

  private void ventSteam() {
    try (var tx = Transaction.openRoot()) {
      var resource = this.steamTank.getResource(0);
      if (!resource.isEmpty()) {
        this.steamTank.internalExtract(resource, 4, tx);
        tx.commit();
      }
    }
  }

  public SteamBoiler boiler() {
    return this.boiler;
  }

  @Override
  public int retrieveFuel() {
    try (var tx = Transaction.openRoot()) {
      var steam = this.steamTank.getFluidStack();
      if (steam.isEmpty()) {
        return 0;
      }
      if (steam.getAmount() >= this.steamTank.getCapacity() / 2) {
        this.steamTank.internalExtract(FluidResource.of(steam), SteamConstants.STEAM_PER_UNIT_WATER, tx);
        tx.commit();
        return FUEL_PER_REQUEST;
      }
      return 0;
    }
  }

  @Override
  protected void addAdditionalSaveData(ValueOutput valueOutput) {
    super.addAdditionalSaveData(valueOutput);
    valueOutput.putChild(CompoundTagKeys.TANK_MANAGER, this.tankManager);
    valueOutput.putChild(CompoundTagKeys.BOILER, this.boiler);
    valueOutput.store(CompoundTagKeys.PROCESS_STATE, FluidTools.ProcessState.CODEC, this.processState);
  }

  @Override
  protected void readAdditionalSaveData(ValueInput valueInput) {
    super.readAdditionalSaveData(valueInput);
    valueInput.readChild(CompoundTagKeys.TANK_MANAGER, this.tankManager);
    valueInput.readChild(CompoundTagKeys.BOILER, this.boiler);
    this.processState = valueInput.read(CompoundTagKeys.PROCESS_STATE, FluidTools.ProcessState.CODEC)
        .orElse(FluidTools.ProcessState.RESET);
  }

  public boolean isSafeToFill() {
    return !this.boiler.isSuperHeated() || !this.waterTank.isEmpty();
  }

  @Override
  public boolean canPassFluidRequests(FluidStack fluid) {
    return fluid.is(FluidTags.WATER);
  }

  @Override
  public boolean canAcceptPushedFluid(RollingStock requester, FluidStack fluid) {
    return fluid.is(FluidTags.WATER);
  }

  @Override
  public boolean canProvidePulledFluid(RollingStock requester, FluidStack fluid) {
    return false;
  }
}
