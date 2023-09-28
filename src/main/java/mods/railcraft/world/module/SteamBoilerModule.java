package mods.railcraft.world.module;

import org.jetbrains.annotations.NotNull;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.util.FluidTools;
import mods.railcraft.util.FluidTools.ProcessType;
import mods.railcraft.util.container.ContainerMapper;
import mods.railcraft.world.level.block.entity.steamboiler.SteamBoilerBlockEntity;
import mods.railcraft.world.level.block.steamboiler.FireboxBlock;
import mods.railcraft.world.level.material.StandardTank;
import mods.railcraft.world.level.material.TankManager;
import mods.railcraft.world.level.material.steam.SteamBoiler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
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
  private final LazyOptional<
      IItemHandler> itemHandler = LazyOptional.of(() -> new InvWrapper(this) {
        @NotNull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
          if (slot != SLOT_LIQUID_OUTPUT)
            return ItemStack.EMPTY;
          return super.extractItem(slot, amount, simulate);
        }
      });

  protected final TankManager tankManager = new TankManager();

  private final StandardTank waterTank =
      StandardTank.ofBuckets(4)
          .fillProcessor(this::checkFill)
          .filter(Fluids.WATER);

  private final StandardTank steamTank = StandardTank.ofBuckets(16)
      .filter(RailcraftTags.Fluids.STEAM);

  protected final ContainerMapper fluidContainer =
      ContainerMapper.make(this.container, SLOT_LIQUID_INPUT, 3).ignoreItemChecks();

  private FluidTools.ProcessState waterProcessState = FluidTools.ProcessState.RESET;
  private int processTicks;

  private boolean explode;

  public SteamBoilerModule(T provider, int containerSize) {
    super(provider, containerSize);

    this.waterTank.changeCallback(provider::setChanged);
    this.steamTank.changeCallback(provider::setChanged);

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
    int capacity = metadata.tanks() * FluidType.BUCKET_VOLUME;
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
          Level.ExplosionInteraction.TNT);
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
    tag.putString("processState", this.waterProcessState.getSerializedName());
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundTag tag) {
    super.deserializeNBT(tag);
    this.tankManager.deserializeNBT(tag.getList("tankManager", Tag.TAG_COMPOUND));
    this.boiler.deserializeNBT(tag.getCompound("boiler"));
    this.waterProcessState = FluidTools.ProcessState.getByName(tag.getString("processState"))
        .orElse(FluidTools.ProcessState.RESET);
  }
}
