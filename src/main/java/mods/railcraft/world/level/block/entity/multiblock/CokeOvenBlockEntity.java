package mods.railcraft.world.level.block.entity.multiblock;

import java.util.Optional;
import javax.annotation.Nullable;
import mods.railcraft.world.item.crafting.CokeOvenMenu;
import mods.railcraft.world.item.crafting.CokeOvenRecipe;
import mods.railcraft.world.item.crafting.RailcraftRecipeTypes;
import mods.railcraft.world.level.block.CokeOvenBricksBlock;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class CokeOvenBlockEntity extends MultiblockBlockEntity<CokeOvenBlockEntity>
    implements RecipeHolder, IFluidTank, Container {

  private static final Component MENU_TITLE =
      new TranslatableComponent("container.coke_oven");

  private static final int FLUID_STORAGE_MAX = 10000;

  // internal inventory, 3 total. 0 IN, 1 OUT
  protected NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);
  protected FluidStack fluid = FluidStack.EMPTY;

  private int recipieRequiredTime = 0;
  private int currentTick = 0;

  // 0 - required time, 1 - current tick, 2 - fluid amount
  protected final ContainerData dataAccess = new ContainerData() {
    public int get(int key) {
      switch (key) {
        case 0:
          return CokeOvenBlockEntity.this.recipieRequiredTime;
        case 1:
          return CokeOvenBlockEntity.this.currentTick;
        case 2:
          return CokeOvenBlockEntity.this.fluid.getAmount();
        default:
          return 0;
      }
    }

    public void set(int key, int value) {
      switch (key) {
        case 0:
          // unsettable
          break;
        case 1:
          // unsettable
          break;
        case 2:
          // unsettable
          break;
        default:
          break;
      }

    }

    public int getCount() {
      return 3;
    }
  };

  public CokeOvenBlockEntity(BlockPos blockPos, BlockState blockState) {
    this(RailcraftBlockEntityTypes.COKE_OVEN.get(), blockPos, blockState);
  }

  public CokeOvenBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
    super(type, blockPos, blockState, RailcraftMultiblockPatterns.COKE_OVEN);
  }

  @Override
  public void delink() {
    super.delink();
    if (!this.getLevel().isClientSide()) {
      BlockState state = this.level.getBlockState(this.worldPosition);
      state.setValue(CokeOvenBricksBlock.PARENT, false);
      state.setValue(CokeOvenBricksBlock.LIT, false);
      this.level.setBlock(this.worldPosition, state, 3);
    }
  }

  @Override
  public AbstractContainerMenu createMenu(int containerProvider, Inventory inventory,
      Player player) {
    if (!this.isFormed()) {
      return null;
    }
    CokeOvenBlockEntity parent = this.getParent();
    if (parent == this) {
      return new CokeOvenMenu(containerProvider, inventory, this, this.dataAccess);
    }

    if (parent == null) {
      return null;
    }
    return parent.createMenu(containerProvider, inventory, player);
  }

  @Override
  public void onLoad() {
    super.onLoad();
    if (this.isParent() && !this.getLevel().isClientSide()) {
      this.level.setBlock(this.worldPosition,
          this.level.getBlockState(this.worldPosition)
              .setValue(CokeOvenBricksBlock.PARENT, true),
          3);
    }
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
    ContainerHelper.loadAllItems(tag, this.items);
    this.recipieRequiredTime = tag.getInt("recipieRequiredTime");
    this.currentTick = tag.getInt("currentTick");
    this.fluid = FluidStack.loadFluidStackFromNBT(tag);
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    ContainerHelper.saveAllItems(tag, this.items);
    tag.putInt("recipieRequiredTime", this.recipieRequiredTime);
    tag.putInt("currentTick", this.currentTick);
    this.fluid.writeToNBT(tag);
  }

  // TODO particles
  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      CokeOvenBlockEntity blockEntity) {
    if (!blockEntity.isFormed()) {
      if (blockState.getValue(CokeOvenBricksBlock.PARENT)) {
        level.setBlock(blockPos,
            blockState.setValue(CokeOvenBricksBlock.PARENT, false), 3);
      }

      if (blockState.getValue(CokeOvenBricksBlock.LIT)) {
        level.setBlock(blockPos,
            blockState.setValue(CokeOvenBricksBlock.LIT, false), 3);
      }
      return;
    }

    if (blockEntity.isParent()) {
      // isParent status
      if (!blockState.getValue(CokeOvenBricksBlock.PARENT)) {
        level.setBlock(blockPos,
            blockState.setValue(CokeOvenBricksBlock.PARENT, true), 3);
        // this.setChanged();
      }

      // only set if needed, burn stat
      if (blockEntity.recipieRequiredTime <= 0
          && blockState.getValue(CokeOvenBricksBlock.LIT)) {
        level.setBlock(blockPos,
            blockState.setValue(CokeOvenBricksBlock.LIT, false), 3);
        // this.setChanged();
      }
      if (blockEntity.recipieRequiredTime > 0
          && !blockState.getValue(CokeOvenBricksBlock.LIT)) {
        level.setBlock(blockPos,
            blockState.setValue(CokeOvenBricksBlock.LIT, true), 3);
        // this.setChanged();
      }
    }

    ItemStack itemstack = blockEntity.items.get(0);
    if (itemstack.isEmpty()) {
      blockEntity.recipieRequiredTime = 0;
      return;
    }

    Optional<CokeOvenRecipe> irecipe = level.getServer()
        .getRecipeManager()
        .getRecipeFor(RailcraftRecipeTypes.COKE_OVEN_COOKING, blockEntity, level);

    if (!irecipe.isPresent()) {
      blockEntity.recipieRequiredTime = 0;
      return;
    }
    CokeOvenRecipe cokeRecipe = irecipe.get();
    if (!blockEntity.canWork(cokeRecipe)) {
      blockEntity.recipieRequiredTime = 0;
      return;
    }
    if (blockEntity.currentTick >= blockEntity.recipieRequiredTime) {
      if (blockEntity.recipieRequiredTime != 0) {
        blockEntity.bake(cokeRecipe);
      }
      blockEntity.recipieRequiredTime = blockEntity.getTickCost();
      blockEntity.currentTick = 0;
      return;
    }
    blockEntity.currentTick++;
  }

  @Override
  public int getContainerSize() {
    return this.items.size();
  }

  @Override
  public boolean isEmpty() {
    for (ItemStack itemstack : this.items) {
      if (!itemstack.isEmpty()) {
        return false;
      }
    }
    return true;
  }

  @Override
  public ItemStack getItem(int index) {
    return this.items.get(index);
  }

  @Override
  public ItemStack removeItem(int index, int count) {
    return ContainerHelper.removeItem(this.items, index, count);
  }

  @Override
  public ItemStack removeItemNoUpdate(int index) {
    return ContainerHelper.takeItem(this.items, index);
  }

  @Override
  public void setItem(int slotID, ItemStack itemStack) {
    ItemStack itemstack = this.items.get(slotID);
    // not empty, same item, and tag matches
    boolean isValid = !itemStack.isEmpty() && itemStack.sameItem(itemstack)
        && ItemStack.tagMatches(itemStack, itemstack);
    this.items.set(slotID, itemStack);
    if (itemStack.getCount() > this.getMaxStackSize()) {
      itemStack.setCount(this.getMaxStackSize());
    }
    // slot 0 is input
    if (slotID == 0 && !isValid) {
      this.recipieRequiredTime = 0; // this.getTotalCookTime();
      this.currentTick = 0;
      this.setChanged();
    }
  }

  @Override
  public boolean stillValid(Player playerEntity) {
    if (!this.isFormed() || this.isRemoved()) {
      return false;
    }
    return playerEntity.distanceToSqr(
        (double) this.worldPosition.getX() + 0.5D,
        (double) this.worldPosition.getY() + 0.5D,
        (double) this.worldPosition.getZ() + 0.5D) <= 64.0D;
  }

  @Override
  public boolean canPlaceItem(int index, ItemStack itemStack) {
    if (index == 1) { // 1 is OUTPUT, 0 is INPUT
      return false;
    }
    return true;
  }

  @Override
  public void clearContent() {
    this.items.clear();
  }

  @Override
  public void setRecipeUsed(Recipe<?> recipe) {
    // unused
  }

  @Override
  @Nullable
  public Recipe<?> getRecipeUsed() {
    // unused
    return null;
  }

  @Override
  public FluidStack getFluid() {
    return this.fluid;
  }

  @Override
  public int getFluidAmount() {
    return this.fluid.getAmount();
  }

  @Override
  public int getCapacity() {
    return FLUID_STORAGE_MAX;
  }

  @Override
  public boolean isFluidValid(FluidStack stack) {
    return false;
  }

  @Override
  public int fill(FluidStack resource, FluidAction action) {
    return 0;
  }

  @Override
  public FluidStack drain(int maxDrain, FluidAction action) {
    int drained = maxDrain;
    if (fluid.getAmount() < drained) {
      drained = fluid.getAmount();
    }
    FluidStack stack = new FluidStack(fluid, drained);
    if (action.execute() && drained > 0) {
      fluid.shrink(drained);
    }
    return stack;
  }

  @Override
  public FluidStack drain(FluidStack resource, FluidAction action) {
    if (resource.isEmpty() || !resource.isFluidEqual(fluid)) {
      return FluidStack.EMPTY;
    }
    return drain(resource.getAmount(), action);
  }

  protected boolean canWork(CokeOvenRecipe recipe) {
    ItemStack resultItemStack = recipe.getResultItem();
    FluidStack resultFluidStack = recipe.getResultFluid();
    if (resultItemStack.isEmpty()) {
      return false;
    }
    ItemStack resultSlot = this.items.get(1);
    // empty? we can proceede to coke
    if (resultSlot.isEmpty()) {
      return true;
    }
    if (!resultSlot.sameItem(resultItemStack) && !this.fluid.isFluidEqual(resultFluidStack)) {
      return false;
    }
    // respect stacks (and fluid count.)
    if ((resultSlot.getCount() + resultItemStack.getCount() <= this.getMaxStackSize())
        && resultSlot.getCount() + resultItemStack.getCount() <= resultSlot.getMaxStackSize()
        && this.fluid.getAmount() + resultFluidStack.getAmount() <= FLUID_STORAGE_MAX) {
      return true;
    }
    return (resultSlot.getCount() + resultItemStack.getCount() <= resultItemStack.getMaxStackSize())
        && (this.fluid.getAmount() + resultFluidStack.getAmount() <= FLUID_STORAGE_MAX);
  }

  protected int getTickCost() {
    return this.level.getServer()
        .getRecipeManager()
        .getRecipeFor(RailcraftRecipeTypes.COKE_OVEN_COOKING, this, this.level)
        .map(CokeOvenRecipe::getTickCost).orElse(2400); // 2min base for coal coke
  }

  private void bake(CokeOvenRecipe cokeOvenRecipe) {
    if (!this.canWork(cokeOvenRecipe)) {
      return;
    }
    ItemStack resultItemStack = cokeOvenRecipe.getResultItem();
    FluidStack resultFluidStack = cokeOvenRecipe.getResultFluid();

    ItemStack resultStack = this.items.get(1);

    if (resultStack.isEmpty()) {
      this.items.set(1, resultItemStack.copy());
    } else if (resultStack.sameItem(resultItemStack)) {
      resultStack.grow(resultItemStack.getCount());
    }

    if (this.fluid.isEmpty()) {
      this.fluid = resultFluidStack.copy();
    } else if (this.fluid.isFluidEqual(resultFluidStack)) {
      this.fluid.grow(resultFluidStack.getAmount());
    }


    if (!this.level.isClientSide) {
      this.setRecipeUsed(cokeOvenRecipe);
    }

    ItemStack inputStack = this.items.get(0);
    inputStack.shrink(1);
  }

  @Override
  public void setRemoved() {
    if (this.getLevel().isClientSide()) {
      super.setRemoved();
      return; // do not run deletion clientside.
    }
    Containers.dropContents(this.getLevel(), this.getBlockPos(), this.items);
    super.setRemoved(); // at this point, the block itself is null
  }

  @Override
  public Component getDisplayName() {
    return MENU_TITLE;
  }
}
