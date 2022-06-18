package mods.railcraft.world.module;

import javax.annotation.Nonnull;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.util.container.ContainerMapper;
import mods.railcraft.world.level.block.entity.steamboiler.SteamBoilerBlockEntity;
import mods.railcraft.world.level.block.steamboiler.FireboxBlock;
import mods.railcraft.world.level.material.fluid.FluidTools;
import mods.railcraft.world.level.material.fluid.FluidTools.ProcessType;
import mods.railcraft.world.level.material.fluid.TankManager;
import mods.railcraft.world.level.material.fluid.steam.SteamBoiler;
import mods.railcraft.world.level.material.fluid.tank.FilteredTank;
import mods.railcraft.world.level.material.fluid.tank.StandardTank;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public abstract class SteamBoilerModule<T extends SteamBoilerBlockEntity>
    extends ContainerModule<T> {

  public static final int TANK_WATER = 0;
  public static final int TANK_STEAM = 1;

  public static final int SLOT_LIQUID_INPUT = 0;
  public static final int SLOT_LIQUID_PROCESSING = 1;
  public static final int SLOT_LIQUID_OUTPUT = 2;

  protected final SteamBoiler boiler;

  private final LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() -> this.tankManager);
  private final LazyOptional<IItemHandler> itemHandler =
      LazyOptional.of(() -> new InvWrapper(this) {
        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
          if (slot != SLOT_LIQUID_OUTPUT)
            return ItemStack.EMPTY;
          return super.extractItem(slot, amount, simulate);
        }
      });

  protected final TankManager tankManager = new TankManager();

  private final FilteredTank waterTank = new FilteredTank(4 * FluidTools.BUCKET_VOLUME) {
    @Override
    public int internalFill(FluidStack resource, FluidAction action) {
      return super.internalFill(SteamBoilerModule.this.checkFill(resource), action);
    }
  }.setFilterFluid(() -> Fluids.WATER);

  private final FilteredTank steamTank = new FilteredTank(16 * FluidTools.BUCKET_VOLUME)
      .setFilterTag(RailcraftTags.Fluids.STEAM);

  protected final ContainerMapper fluidContainer =
      ContainerMapper.make(this.container, SLOT_LIQUID_INPUT, 3).ignoreItemChecks();

  private FluidTools.ProcessState waterProcessState = FluidTools.ProcessState.RESET;
  private int processTicks;

  private boolean explode;

  public SteamBoilerModule(T provider, int containerSize) {
    super(provider, containerSize);

    this.waterTank.setChangeListener(provider::setChanged);
    this.steamTank.setChangeListener(provider::setChanged);

    this.tankManager.add(this.waterTank);
    this.tankManager.add(this.steamTank);

    this.waterTank.disableDrain();
    this.steamTank.disableFill();

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

  public LazyOptional<IFluidHandler> getFluidHandler() {
    return this.fluidHandler;
  }

  public LazyOptional<IItemHandler> getItemHandler() {
    return this.itemHandler;
  }

  public void update(SteamBoilerBlockEntity.Metadata metadata) {
    int capacity = metadata.tanks() * FluidTools.BUCKET_VOLUME;
    this.tankManager.setCapacity(TANK_STEAM, capacity * metadata.steamCapacityPerTank());
    this.tankManager.setCapacity(TANK_WATER, capacity * 4);
    this.boiler.setMaxTemperature(metadata.maxTemperature());
    this.boiler.setTicksPerCycle(metadata.ticksPerCycle());
  }

  private FluidStack checkFill(FluidStack fluidStack) {
    return this.boiler.checkFill(fluidStack, () -> this.explode = true);
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
          Explosion.BlockInteraction.BREAK);
      this.explode = false;
      return;
    }

    this.boiler.tick(metadata.tanks());

    if (this.processTicks++ >= FluidTools.BUCKET_FILL_TIME) {
      this.processTicks = 0;
      this.processFluidContainers();
    }
  }

  protected void processFluidContainers() {
    this.waterProcessState = FluidTools.processContainer(this.fluidContainer, this.waterTank,
        ProcessType.DRAIN_ONLY, this.waterProcessState);
  }

  @Override
  public CompoundTag serializeNBT() {
    var tag = super.serializeNBT();
    tag.put("tankManager", this.tankManager.serializeNBT());
    tag.put("boiler", this.boiler.serializeNBT());
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundTag data) {
    super.deserializeNBT(data);
    this.tankManager.deserializeNBT(data.getList("tankManager", Tag.TAG_COMPOUND));
    this.boiler.deserializeNBT(data.getCompound("boiler"));
  }
}
