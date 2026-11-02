package mods.railcraft.world.level.block.entity;

import org.jspecify.annotations.Nullable;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.util.container.AdvancedContainer;
import mods.railcraft.util.container.ContainerMapper;
import mods.railcraft.util.container.SlotFilteredResourceHandler;
import mods.railcraft.util.fluids.FluidTools;
import mods.railcraft.world.inventory.HobbyistSteamEngineMenu;
import mods.railcraft.world.level.material.StandardTank;
import mods.railcraft.world.level.material.steam.SolidFuelProvider;
import mods.railcraft.world.level.material.steam.SteamBoiler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;

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
        case SLOT_FUEL -> isFuel(itemStack);
        case SLOT_LIQUID_INPUT -> FluidUtil.getFirstStackContained(itemStack).is(FluidTags.WATER);
        default -> false;
      };
    }
  }.listener(this);

  private final ContainerMapper fluidContainer =
      ContainerMapper.make(this.container, SLOT_LIQUID_INPUT, 3).ignoreItemChecks();

  private final ResourceHandler<ItemResource> itemHandler =
      new SlotFilteredResourceHandler<>(VanillaContainerWrapper.of(this.container),
          index -> index == SLOT_FUEL || index == SLOT_LIQUID_INPUT,
          index -> index == SLOT_LIQUID_OUTPUT);

  private FluidTools.ProcessState processState = FluidTools.ProcessState.RESET;
  private int processTicks;
  private boolean explode;

  public HobbyistSteamEngineBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.HOBBYIST_STEAM_ENGINE.get(), blockPos, blockState, 4);
    this.waterTank = StandardTank.ofBuckets(4)
        .fillProcessor(this::checkFill)
        .filter(Fluids.WATER)
        .disableExtract();
    this.waterTank.changeCallback(this::setChanged);
    this.tankManager.add(this.waterTank);

    this.boiler = new SteamBoiler(this.waterTank, this.steamTank)
        .setTicksPerCycle(TICKS_PER_BOILER_CYCLE)
        .setEfficiencyModifier(FUEL_PER_CONVERSION_MULTIPLIER);
    this.boiler.setFuelProvider(new SolidFuelProvider(this.container, SLOT_FUEL) {
      @Override
      public float consumeFuel(Level level) {
        return isOverheated() || !isPowered() ? 0 : super.consumeFuel(level);
      }
    });
  }

  private boolean isFuel(ItemStack itemStack) {
    return this.level != null && itemStack.getBurnTime(null, this.level.fuelValues()) > 0;
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
  public ResourceHandler<ItemResource> getItemCap(@Nullable Direction side) {
    return side == this.getFacing() ? null : this.itemHandler;
  }

  private void checkFill(FluidStack waterOriginalState) {
    this.boiler.checkFill(waterOriginalState, () -> this.explode = true);
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
    this.boiler.tick(this.level, 1);

    if (this.processTicks++ >= FluidTools.BUCKET_FILL_TIME) {
      this.processTicks = 0;
      this.processState = FluidTools.processContainer(this.fluidContainer, this.waterTank,
          FluidTools.ProcessType.DRAIN_ONLY, this.processState);
    }
  }

  @Override
  public void preRemoveSideEffects(BlockPos pos, BlockState state) {
    super.preRemoveSideEffects(pos, state);
    Containers.dropContents(this.level, pos, this.container);
  }

  @Override
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return new HobbyistSteamEngineMenu(id, inventory, this);
  }

  @Override
  protected void saveAdditional(ValueOutput output) {
    super.saveAdditional(output);
    output.putChild(CompoundTagKeys.CONTAINER, this.container);
    output.putChild(CompoundTagKeys.BOILER, this.boiler);
    output.store(CompoundTagKeys.PROCESS_STATE, FluidTools.ProcessState.CODEC, this.processState);
  }

  @Override
  protected void loadAdditional(ValueInput input) {
    super.loadAdditional(input);
    input.readChild(CompoundTagKeys.CONTAINER, this.container);
    input.readChild(CompoundTagKeys.BOILER, this.boiler);
    this.processState = input.read(CompoundTagKeys.PROCESS_STATE, FluidTools.ProcessState.CODEC)
        .orElse(FluidTools.ProcessState.RESET);
  }
}
