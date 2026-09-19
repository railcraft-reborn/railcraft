package mods.railcraft.world.module;

import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.util.container.ContainerMapper;
import mods.railcraft.util.container.SlotFilteredResourceHandler;
import mods.railcraft.util.fluids.FluidTools;
import mods.railcraft.util.fluids.FluidTools.ProcessType;
import mods.railcraft.world.level.block.entity.steamboiler.SteamBoilerBlockEntity;
import mods.railcraft.world.level.block.steamboiler.FireboxBlock;
import mods.railcraft.world.level.material.StandardTank;
import mods.railcraft.world.level.material.TankManager;
import mods.railcraft.world.level.material.steam.SteamBoiler;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;

public abstract class SteamBoilerModule<T extends SteamBoilerBlockEntity>
    extends ContainerModule<T> {

  public static final int TANK_WATER = 0;
  public static final int TANK_STEAM = 1;

  public static final int SLOT_LIQUID_INPUT = 0;
  public static final int SLOT_LIQUID_PROCESSING = 1;
  public static final int SLOT_LIQUID_OUTPUT = 2;

  protected final SteamBoiler boiler;

  private final ResourceHandler<ItemResource> itemHandler =
      new SlotFilteredResourceHandler<>(VanillaContainerWrapper.of(this),
          index -> index == SLOT_LIQUID_INPUT,
          index -> index == SLOT_LIQUID_OUTPUT);

  protected final TankManager tankManager = new TankManager();

  private final StandardTank waterTank =
      StandardTank.ofBuckets(4)
          .fillProcessor(this::checkFill)
          .filter(Fluids.WATER);

  private final StandardTank steamTank = StandardTank.ofBuckets(16)
      .filter(RailcraftTags.Fluids.STEAM);

  protected final ContainerMapper fluidContainer =
      ContainerMapper.make(this.container, SLOT_LIQUID_INPUT, 3).ignoreItemChecks();

  private FluidTools.ProcessState processState = FluidTools.ProcessState.RESET;
  private int processTicks;

  private boolean explode;

  public SteamBoilerModule(T provider, int containerSize) {
    super(provider, containerSize);

    this.waterTank.changeCallback(provider::setChanged);
    this.steamTank.changeCallback(provider::setChanged);

    this.tankManager.add(this.waterTank);
    this.tankManager.add(this.steamTank);

    this.waterTank.disableExtract();
    this.steamTank.disableInsert();

    this.boiler = new SteamBoiler(this.waterTank, this.steamTank);
    this.boiler.setChangeListener(provider::syncToClient);
  }

  public StandardTank getWaterTank() {
    return this.waterTank;
  }

  public StandardTank getSteamTank() {
    return this.steamTank;
  }

  public TankManager getTankManager() {
    return this.tankManager;
  }

  public ResourceHandler<ItemResource> getItemHandler() {
    return this.itemHandler;
  }

  public void update(SteamBoilerBlockEntity.Metadata metadata) {
    int capacity = metadata.tanks() * FluidType.BUCKET_VOLUME;
    this.tankManager.setCapacity(TANK_STEAM, capacity * metadata.steamCapacityPerTank());
    this.tankManager.setCapacity(TANK_WATER, capacity * 4);
    this.boiler.setMaxTemperature(metadata.maxTemperature());
    this.boiler.setTicksPerCycle(metadata.ticksPerCycle());
  }

  private void checkFill(FluidStack waterOriginalState) {
    this.boiler.checkFill(waterOriginalState, () -> this.explode = true);
  }

  public SteamBoiler getBoiler() {
    return this.boiler;
  }

  @Override
  public void serverTick() {
    var burning = this.boiler.isBurning();
    if (!this.provider.isMaster()) {
      this.boiler.reduceHeat(1);

      burning = this.provider.getMembership()
          .flatMap(membership -> membership.master().getModule(SteamBoilerModule.class))
          .map(module -> module.getBoiler().isBurning())
          .orElse(false);
    }

    var level = this.provider.getLevel();
    var blockPos = this.provider.getBlockPos();

    var blockState = this.provider.getBlockState();
    if (blockState.getBlock() instanceof FireboxBlock
        && burning != blockState.getValue(FireboxBlock.LIT)) {
      level.setBlockAndUpdate(blockPos, blockState.setValue(FireboxBlock.LIT, burning));
    }

    if (!this.provider.isMaster()) {
      return;
    }

    var metadata = this.provider.getCurrentPattern().get().getMetadata();
    if (this.explode) {
      level.explode(null, blockPos.getX(), blockPos.getY(),
          blockPos.getZ(), 5.0F + 0.1F * metadata.tanks(),
          Level.ExplosionInteraction.TNT);
      this.explode = false;
      return;
    }

    this.boiler.tick(level, metadata.tanks());

    if (this.processTicks++ >= FluidTools.BUCKET_FILL_TIME) {
      this.processTicks = 0;
      this.processFluidContainers();
    }
  }

  protected void processFluidContainers() {
    this.processState = FluidTools.processContainer(this.fluidContainer, this.waterTank,
        ProcessType.DRAIN_ONLY, this.processState);
  }

  @Override
  public void serialize(ValueOutput valueOutput) {
    super.serialize(valueOutput);
    valueOutput.putChild(CompoundTagKeys.TANK_MANAGER, this.tankManager);
    valueOutput.putChild(CompoundTagKeys.BOILER, this.boiler);
    valueOutput.store(CompoundTagKeys.PROCESS_STATE, FluidTools.ProcessState.CODEC, this.processState);
  }

  @Override
  public void deserialize(ValueInput valueInput) {
    super.deserialize(valueInput);
    valueInput.readChild(CompoundTagKeys.TANK_MANAGER, this.tankManager);
    valueInput.readChild(CompoundTagKeys.BOILER, this.boiler);
    this.processState = valueInput.read(CompoundTagKeys.PROCESS_STATE, FluidTools.ProcessState.CODEC)
        .orElse(FluidTools.ProcessState.RESET);
  }
}
