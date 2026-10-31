package mods.railcraft.world.level.block.entity;

import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.sounds.RailcraftSoundEvents;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.world.inventory.SteamEngineMenu;
import mods.railcraft.world.level.block.SteamEngineBlock;
import mods.railcraft.world.level.material.StandardTank;
import mods.railcraft.world.level.material.TankManager;
import mods.railcraft.world.level.material.steam.SteamConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class SteamEngineBlockEntity extends RailcraftBlockEntity implements MenuProvider {

  private static final int STEAM_VENT_RATE = 5;
  private static final int OVERHEAT_ENERGY_LOSS = 50;

  protected final StandardTank steamTank;
  protected final TankManager tankManager = new TankManager();
  protected final EnergyStorage energyStorage;

  private final IEnergyStorage outputEnergyStorage = new IEnergyStorage() {
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
      return 0;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
      return energyStorage.extractEnergy(Math.min(maxExtract, getTier().getMaxExtract()), simulate);
    }

    @Override
    public int getEnergyStored() {
      return energyStorage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
      return energyStorage.getMaxEnergyStored();
    }

    @Override
    public boolean canExtract() {
      return true;
    }

    @Override
    public boolean canReceive() {
      return false;
    }
  };

  private float pistonProgress;
  private int pistonStage;
  private boolean powered;
  private boolean active;
  private boolean needsInit = true;
  private EnergyStage energyStage = EnergyStage.BLUE;
  private int steamUsed;
  private float currentOutput;

  public SteamEngineBlockEntity(BlockPos blockPos, BlockState blockState) {
    this(RailcraftBlockEntityTypes.STEAM_ENGINE.get(), blockPos, blockState, 8);
  }

  protected SteamEngineBlockEntity(BlockEntityType<?> type, BlockPos blockPos,
      BlockState blockState, int steamBuckets) {
    super(type, blockPos, blockState);
    var tier = getTier(blockState);
    this.energyStorage = new EnergyStorage(tier.getCapacity(), tier.getCapacity(),
        tier.getMaxExtract());
    this.steamTank = StandardTank.ofBuckets(steamBuckets)
        .filter(RailcraftTags.Fluids.STEAM)
        .disableDrain();
    this.steamTank.changeCallback(this::setChanged);
    this.tankManager.add(this.steamTank);
  }

  private static SteamEngineBlock.Tier getTier(BlockState blockState) {
    return blockState.getBlock() instanceof SteamEngineBlock engine
        ? engine.getTier()
        : SteamEngineBlock.Tier.COMMERCIAL;
  }

  public SteamEngineBlock.Tier getTier() {
    return getTier(this.getBlockState());
  }

  public Direction getFacing() {
    var blockState = this.getBlockState();
    return blockState.hasProperty(SteamEngineBlock.FACING)
        ? blockState.getValue(SteamEngineBlock.FACING)
        : Direction.UP;
  }

  public StandardTank getSteamTank() {
    return this.steamTank;
  }

  public TankManager getTankManager() {
    return this.tankManager;
  }

  public IEnergyStorage getEnergyStorage() {
    return this.energyStorage;
  }

  public float getCurrentOutput() {
    return this.currentOutput;
  }

  public void setCurrentOutput(float currentOutput) {
    this.currentOutput = currentOutput;
  }

  public float getPistonProgress() {
    return this.pistonProgress;
  }

  public boolean isActive() {
    return this.active;
  }

  public boolean isPowered() {
    return this.powered;
  }

  public EnergyStage getEnergyStage() {
    return this.energyStage;
  }

  public boolean isOverheated() {
    return this.energyStage == EnergyStage.OVERHEAT;
  }

  public void resetOverheat() {
    this.energyStage = this.computeEnergyStage();
    this.syncToClient();
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      SteamEngineBlockEntity engine) {
    engine.serverTick();
  }

  public static void clientTick(Level level, BlockPos blockPos, BlockState blockState,
      SteamEngineBlockEntity engine) {
    engine.clientTick();
  }

  protected void serverTick() {
    if (this.needsInit) {
      this.needsInit = false;
      this.checkPower();
    }

    if (!this.powered && this.energyStorage.getEnergyStored() > 1) {
      this.energyStorage.extractEnergy(1, false);
    }

    this.updateEnergyStage();

    if (this.isOverheated()) {
      this.overheat();
    } else if (this.pistonStage != 0) {
      this.pistonProgress += this.getServerPistonSpeed();
      if (this.pistonProgress > 0.5F && this.pistonStage == 1) {
        this.pistonStage = 2;
        this.pushEnergy();
      } else if (this.pistonProgress >= 1) {
        this.pistonProgress = 0;
        this.pistonStage = 0;
      }
    } else if (this.powered) {
      this.setActive(this.getEnergyReceiver() != null
          && this.energyStorage.getEnergyStored() > 0);
      if (this.active) {
        this.pistonStage = 1;
      }
    } else {
      this.setActive(false);
    }

    this.burn();
  }

  protected void clientTick() {
    if (this.pistonStage != 0) {
      this.pistonProgress += this.energyStage.getPistonSpeed();
      if (this.pistonProgress > 0.5F && this.pistonStage == 1) {
        this.pistonStage = 2;
        this.playSound(0.5F);
      } else if (this.pistonProgress >= 1) {
        this.pistonStage = 0;
        this.pistonProgress = 0;
        this.playSound(1.0F);
      }
    } else if (this.active) {
      this.pistonStage = 1;
    }
  }

  private void playSound(float pitch) {
    this.level.playLocalSound(this.getBlockPos(), RailcraftSoundEvents.STEAM_BURST.get(),
        SoundSource.BLOCKS, 0.15F,
        (float) (pitch + this.level.getRandom().nextGaussian() * 0.1), false);
  }

  @Nullable
  private IEnergyStorage getEnergyReceiver() {
    var facing = this.getFacing();
    var storage = this.level.getCapability(Capabilities.EnergyStorage.BLOCK,
        this.getBlockPos().relative(facing), facing.getOpposite());
    return storage != null && storage.canReceive() ? storage : null;
  }

  private void pushEnergy() {
    var receiver = this.getEnergyReceiver();
    if (receiver == null) {
      return;
    }
    int available = this.energyStorage.extractEnergy(this.getTier().getMaxExtract(), true);
    if (available > 0) {
      int accepted = receiver.receiveEnergy(available, false);
      this.energyStorage.extractEnergy(accepted, false);
    }
  }

  private double getServerPistonSpeed() {
    return Math.max(0.16 * this.getEnergyLevel(), 0.01);
  }

  private void setActive(boolean active) {
    if (this.active != active) {
      this.active = active;
      this.syncToClient();
    }
  }

  public void checkPower() {
    var powered = this.level.hasNeighborSignal(this.getBlockPos());
    if (this.powered != powered) {
      this.powered = powered;
      this.setChanged();
      this.syncToClient();
    }
  }

  private double getEnergyLevel() {
    return (double) this.energyStorage.getEnergyStored()
        / (double) this.energyStorage.getMaxEnergyStored();
  }

  private EnergyStage computeEnergyStage() {
    var energyLevel = this.getEnergyLevel();
    if (energyLevel < 0.2) {
      return EnergyStage.BLUE;
    } else if (energyLevel < 0.4) {
      return EnergyStage.GREEN;
    } else if (energyLevel < 0.6) {
      return EnergyStage.YELLOW;
    } else if (energyLevel < 0.8) {
      return EnergyStage.ORANGE;
    } else if (energyLevel < 1) {
      return EnergyStage.RED;
    }
    return EnergyStage.OVERHEAT;
  }

  private void updateEnergyStage() {
    // Once overheated the engine stays like that until it is reset with a crowbar
    if (this.isOverheated()) {
      return;
    }
    var newStage = this.computeEnergyStage();
    if (this.energyStage != newStage) {
      this.energyStage = newStage;
      this.syncToClient();
    }
  }

  protected void overheat() {
    this.energyStorage.extractEnergy(OVERHEAT_ENERGY_LOSS, false);
    this.ventSteam();
  }

  protected void ventSteam() {
    this.steamTank.internalDrain(STEAM_VENT_RATE, IFluidHandler.FluidAction.EXECUTE);
  }

  protected void burn() {
    int output = 0;
    if (!this.isOverheated()) {
      var steamUsedPerTick = this.getTier().getSteamUsedPerTick();
      if (this.powered && this.steamTank.getFluidAmount()
          >= this.steamTank.getCapacity() / 2 - SteamConstants.STEAM_PER_UNIT_WATER) {
        this.steamUsed += this.steamTank
            .internalDrain(steamUsedPerTick - 1, IFluidHandler.FluidAction.EXECUTE)
            .getAmount();
      }
      this.steamUsed += this.steamTank.internalDrain(1, IFluidHandler.FluidAction.EXECUTE)
          .getAmount();

      if (this.powered) {
        if (this.steamUsed >= steamUsedPerTick) {
          this.steamUsed -= steamUsedPerTick;
          output = this.getTier().getOutput();
          this.energyStorage.receiveEnergy(output, false);
          this.setChanged();
        }
      } else {
        this.steamUsed = 0;
        this.ventSteam();
      }
    }
    this.currentOutput = (this.currentOutput * 74 + output) / 75.0F;
  }

  @Nullable
  public IFluidHandler getFluidCap(@Nullable Direction side) {
    return side == this.getFacing() ? null : this.tankManager;
  }

  @Nullable
  public IEnergyStorage getEnergyCap(@Nullable Direction side) {
    return side == null || side == this.getFacing() ? this.outputEnergyStorage : null;
  }

  @Override
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return new SteamEngineMenu(id, inventory, this);
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.saveAdditional(tag, provider);
    tag.put(CompoundTagKeys.TANK_MANAGER, this.tankManager.serializeNBT(provider));
    tag.put(CompoundTagKeys.ENERGY, this.energyStorage.serializeNBT(provider));
    tag.putBoolean(CompoundTagKeys.POWERED, this.powered);
    tag.putString(CompoundTagKeys.ENERGY_STAGE, this.energyStage.getSerializedName());
    tag.putFloat(CompoundTagKeys.CURRENT_OUTPUT, this.currentOutput);
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.loadAdditional(tag, provider);
    this.tankManager.deserializeNBT(provider,
        tag.getList(CompoundTagKeys.TANK_MANAGER, Tag.TAG_COMPOUND));
    if (tag.contains(CompoundTagKeys.ENERGY)) {
      this.energyStorage.deserializeNBT(provider, tag.get(CompoundTagKeys.ENERGY));
    }
    this.powered = tag.getBoolean(CompoundTagKeys.POWERED);
    this.energyStage = EnergyStage.fromName(tag.getString(CompoundTagKeys.ENERGY_STAGE));
    this.currentOutput = tag.getFloat(CompoundTagKeys.CURRENT_OUTPUT);
  }

  @Override
  public void writeToBuf(RegistryFriendlyByteBuf out) {
    super.writeToBuf(out);
    out.writeEnum(this.energyStage);
    out.writeBoolean(this.active);
    out.writeBoolean(this.powered);
  }

  @Override
  public void readFromBuf(RegistryFriendlyByteBuf in) {
    super.readFromBuf(in);
    this.energyStage = in.readEnum(EnergyStage.class);
    this.active = in.readBoolean();
    this.powered = in.readBoolean();
  }

  public enum EnergyStage implements StringRepresentable {

    BLUE("blue", 0.01, 1),
    GREEN("green", 0.02, 2),
    YELLOW("yellow", 0.04, 3),
    ORANGE("orange", 0.08, 4),
    RED("red", 0.16, 5),
    OVERHEAT("overheat", 0.0, 8);

    private final String name;
    private final double pistonSpeed;
    private final int particleRate;

    EnergyStage(String name, double pistonSpeed, int particleRate) {
      this.name = name;
      this.pistonSpeed = pistonSpeed;
      this.particleRate = particleRate;
    }

    public double getPistonSpeed() {
      return this.pistonSpeed;
    }

    public int getParticleRate() {
      return this.particleRate;
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }

    public static EnergyStage fromName(String name) {
      for (var stage : values()) {
        if (stage.name.equals(name)) {
          return stage;
        }
      }
      return BLUE;
    }
  }
}
