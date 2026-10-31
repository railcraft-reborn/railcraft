package mods.railcraft.world.level.block.entity;

import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.util.container.AdvancedContainer;
import mods.railcraft.util.container.ContainerMapper;
import mods.railcraft.util.fluids.FluidTools;
import mods.railcraft.world.inventory.HobbyistSteamEngineMenu;
import mods.railcraft.world.level.material.StandardTank;
import mods.railcraft.world.level.material.steam.SolidFuelProvider;
import mods.railcraft.world.level.material.steam.SteamBoiler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

public class HobbyistSteamEngineBlockEntity extends SteamEngineBlockEntity {

  public static final int SLOT_FUEL = 0;
  public static final int SLOT_LIQUID_INPUT = 1;
  public static final int SLOT_LIQUID_PROCESSING = 2;
  public static final int SLOT_LIQUID_OUTPUT = 3;

  private static final int TICKS_PER_BOILER_CYCLE = 20;
  private static final double FUEL_PER_CONVERSION_MULTIPLIER = 1.25;
  private static final float EXPLOSION_POWER = 2.0F;

  private final StandardTank waterTank;
  private final SteamBoiler boiler;
  private final AdvancedContainer container = new AdvancedContainer(4) {
    @Override
    public boolean canPlaceItem(int slot, ItemStack itemStack) {
      return switch (slot) {
        case SLOT_FUEL -> itemStack.getBurnTime(null) > 0;
        case SLOT_LIQUID_INPUT -> FluidUtil.getFluidContained(itemStack)
            .map(fluid -> fluid.is(FluidTags.WATER))
            .orElse(false);
        default -> false;
      };
    }
  }.listener(this);

  private final ContainerMapper fluidContainer =
      ContainerMapper.make(this.container, SLOT_LIQUID_INPUT, 3).ignoreItemChecks();

  private final IItemHandler itemHandler = new InvWrapper(this.container) {
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
      return slot == SLOT_LIQUID_OUTPUT
          ? super.extractItem(slot, amount, simulate)
          : ItemStack.EMPTY;
    }
  };

  private FluidTools.ProcessState processState = FluidTools.ProcessState.RESET;
  private int processTicks;
  private boolean explode;

  public HobbyistSteamEngineBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.HOBBYIST_STEAM_ENGINE.get(), blockPos, blockState, 4);
    this.waterTank = StandardTank.ofBuckets(4)
        .fillProcessor(this::checkFill)
        .filter(Fluids.WATER)
        .disableDrain();
    this.waterTank.changeCallback(this::setChanged);
    this.tankManager.add(this.waterTank);

    this.boiler = new SteamBoiler(this.waterTank, this.steamTank)
        .setTicksPerCycle(TICKS_PER_BOILER_CYCLE)
        .setEfficiencyModifier(FUEL_PER_CONVERSION_MULTIPLIER)
        .setFuelProvider(new SolidFuelProvider(this.container, SLOT_FUEL) {
          @Override
          public float consumeFuel() {
            return isOverheated() || !isPowered() ? 0 : super.consumeFuel();
          }
        });
  }

  public StandardTank getWaterTank() {
    return this.waterTank;
  }

  public SteamBoiler getBoiler() {
    return this.boiler;
  }

  public Container getContainer() {
    return this.container;
  }

  @Nullable
  public IItemHandler getItemCap(@Nullable Direction side) {
    return side == this.getFacing() ? null : this.itemHandler;
  }

  private FluidStack checkFill(FluidStack fluidStack) {
    return this.boiler.checkFill(fluidStack, () -> this.explode = true);
  }

  @Override
  protected void serverTick() {
    if (this.explode) {
      this.explode = false;
      var pos = this.getBlockPos();
      this.level.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
          EXPLOSION_POWER, Level.ExplosionInteraction.BLOCK);
      return;
    }
    super.serverTick();
  }

  @Override
  protected void burn() {
    super.burn();
    this.boiler.tick(1);

    if (this.processTicks++ >= FluidTools.BUCKET_FILL_TIME) {
      this.processTicks = 0;
      this.processState = FluidTools.processContainer(this.fluidContainer, this.waterTank,
          FluidTools.ProcessType.DRAIN_ONLY, this.processState);
    }
  }

  @Override
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return new HobbyistSteamEngineMenu(id, inventory, this);
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.saveAdditional(tag, provider);
    tag.put(CompoundTagKeys.CONTAINER, this.container.createTag(provider));
    tag.put(CompoundTagKeys.BOILER, this.boiler.serializeNBT(provider));
    tag.putString(CompoundTagKeys.PROCESS_STATE, this.processState.getSerializedName());
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.loadAdditional(tag, provider);
    this.container.fromTag(tag.getList(CompoundTagKeys.CONTAINER, Tag.TAG_COMPOUND), provider);
    if (tag.contains(CompoundTagKeys.BOILER, Tag.TAG_COMPOUND)) {
      this.boiler.deserializeNBT(provider, tag.getCompound(CompoundTagKeys.BOILER));
    }
    this.processState = FluidTools.ProcessState.fromTag(tag);
  }
}
