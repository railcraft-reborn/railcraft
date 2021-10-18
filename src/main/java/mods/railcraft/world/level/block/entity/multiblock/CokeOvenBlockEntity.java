package mods.railcraft.world.level.block.entity.multiblock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import javax.annotation.Nullable;

import mods.railcraft.world.item.crafting.CokeOvenMenu;
import mods.railcraft.world.item.crafting.CokeOvenRecipe;
import mods.railcraft.world.item.crafting.RailcraftRecipeTypes;
import mods.railcraft.world.level.block.CokeOvenBricksBlock;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class CokeOvenBlockEntity extends MultiblockEntity<CokeOvenBlockEntity>
    implements INamedContainerProvider, IRecipeHolder, IFluidTank, IInventory {

  private static final ITextComponent MENU_TITLE =
      new TranslationTextComponent("container.coke_oven");

  private static final int FLUID_STORAGE_MAX = 10000;

  // internal inventory, 3 total. 0 IN, 1 OUT
  protected NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);
  protected FluidStack fluid = FluidStack.EMPTY;

  private int recipieRequiredTime = 0;
  private int currentTick = 0;

  // 0 - required time, 1 - current tick, 2 - fluid amount
  protected final IIntArray dataAccess = new IIntArray() {
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

  public CokeOvenBlockEntity() {
    super(RailcraftBlockEntityTypes.COKE_OVEN.get());
  }

  public CokeOvenBlockEntity(TileEntityType<?> tileEntityType) {
    super(tileEntityType);
  }

  @Override
  public boolean isMultiblockPatternValid(BlockPos normal) {
    return this.getPatternEntities(normal) != null;
  }

  @Override
  @Nullable
  public Collection<CokeOvenBlockEntity> getPatternEntities(BlockPos normal, boolean ignoreChecks) {
    int box = 2; // 3-1
    BlockPos originPos = this.worldPosition.offset(0, -1, 0).offset(
        (normal.getX() == 0) ? -1 : 0,
        0,
        (normal.getZ() == 0) ? -1 : 0);

    int i = 0;
    Collection<CokeOvenBlockEntity> out = new ArrayList<>(0);

    for (BlockPos blockpos : BlockPos.betweenClosed(
        // 3 - 1
        originPos, originPos.offset(
          (normal.getX() == 0) ? box : normal.getX() * box,
          2,
          (normal.getZ() == 0) ? box : normal.getZ() * box))) {
      i++;

      BlockState theState = this.getLevel().getBlockState(blockpos);
      Block theBlock = theState.getBlock();

      if ((i == 14) && !ignoreChecks) {
        if (!theBlock.is(Blocks.AIR)) {
          return null;
        }
        continue;
      }
      if (!ignoreChecks && (i != 14
          && (!theBlock.is(RailcraftBlocks.COKE_OVEN_BRICKS.get())
              || !theBlock.hasTileEntity(theState)))) {
        return null;
      }

      TileEntity te = this.getLevel().getBlockEntity(blockpos);
      if (!(te instanceof CokeOvenBlockEntity)) { // this is important though
        if (ignoreChecks) { // probably air
          continue;
        }
        return null;
      }
      out.add((CokeOvenBlockEntity) te);
    }

    return out;
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
  public Container createMenu(int containerProvider,
      PlayerInventory playerInventory, PlayerEntity player) {
    if (!this.isFormed()) {
      return null;
    }
    CokeOvenBlockEntity parent = this.getParent();
    if (parent == this) {
      return new CokeOvenMenu(containerProvider, playerInventory, this, this.dataAccess);
    }

    if (parent == null) {
      return null;
    }
    return parent.createMenu(containerProvider, playerInventory, player);
  }

  @Override
  protected void load() {
    super.load();
    if (this.isParent() && !this.getLevel().isClientSide()) {
      this.level.setBlock(this.worldPosition,
          this.level.getBlockState(this.worldPosition)
              .setValue(CokeOvenBricksBlock.PARENT, true), 3);
    }
  }

  @Override
  public void load(BlockState blockState, CompoundNBT data) {
    super.load(blockState, data);
    this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
    ItemStackHelper.loadAllItems(data, this.items);
    this.recipieRequiredTime = data.getInt("recipieRequiredTime");
    this.currentTick = data.getInt("currentTick");
    this.fluid = FluidStack.loadFluidStackFromNBT(data);
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    ItemStackHelper.saveAllItems(data, this.items);
    data.putInt("recipieRequiredTime", this.recipieRequiredTime);
    data.putInt("currentTick", this.currentTick);
    this.fluid.writeToNBT(data);
    return data;
  }


  // TODO particles
  @Override
  public void tick() {
    super.tick();

    if (this.level.isClientSide()) {
      return;
    }

    BlockState parentBrick = this.level.getBlockState(this.worldPosition);

    if (!this.isFormed()) {
      if (parentBrick.getValue(CokeOvenBricksBlock.PARENT)) {
        this.level.setBlock(this.worldPosition,
            parentBrick.setValue(CokeOvenBricksBlock.PARENT, false), 3);
      }

      if (parentBrick.getValue(CokeOvenBricksBlock.LIT)) {
        this.level.setBlock(this.worldPosition,
            parentBrick.setValue(CokeOvenBricksBlock.LIT, false), 3);
      }
      return;
    }

    if (this.isParent()) {
      // isParent status
      if (!parentBrick.getValue(CokeOvenBricksBlock.PARENT)) {
        this.level.setBlock(this.worldPosition,
            parentBrick.setValue(CokeOvenBricksBlock.PARENT, true), 3);
        // this.setChanged();
      }

      // only set if needed, burn stat
      if (this.recipieRequiredTime <= 0
          && parentBrick.getValue(CokeOvenBricksBlock.LIT)) {
        this.level.setBlock(this.worldPosition,
            parentBrick.setValue(CokeOvenBricksBlock.LIT, false), 3);
        // this.setChanged();
      }
      if (this.recipieRequiredTime > 0
          && !parentBrick.getValue(CokeOvenBricksBlock.LIT)) {
        this.level.setBlock(this.worldPosition,
            parentBrick.setValue(CokeOvenBricksBlock.LIT, true), 3);
        // this.setChanged();
      }
    }

    ItemStack itemstack = this.items.get(0);
    if (itemstack.isEmpty()) {
      this.recipieRequiredTime = 0;
      return;
    }

    Optional<CokeOvenRecipe> irecipe = this.level.getServer()
        .getRecipeManager()
        .getRecipeFor(RailcraftRecipeTypes.COKE_OVEN_COOKING, this, this.level);

    if (!irecipe.isPresent()) {
      this.recipieRequiredTime = 0;
      return;
    }
    CokeOvenRecipe cokeRecipe = irecipe.get();
    if (!this.canWork(cokeRecipe)) {
      this.recipieRequiredTime = 0;
      return;
    }
    if (this.currentTick >= this.recipieRequiredTime) {
      if (recipieRequiredTime != 0) {
        this.bake(cokeRecipe);
      }
      this.recipieRequiredTime = this.getTickCost();
      this.currentTick = 0;
      return;
    }
    this.currentTick++;
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
    return ItemStackHelper.removeItem(this.items, index, count);
  }

  @Override
  public ItemStack removeItemNoUpdate(int index) {
    return ItemStackHelper.takeItem(this.items, index);
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
      this.recipieRequiredTime = 0; //this.getTotalCookTime();
      this.currentTick = 0;
      this.setChanged();
    }
  }

  @Override
  public boolean stillValid(PlayerEntity playerEntity) {
    if (!this.isFormed() || this.isRemoved()) {
      return false;
    }
    return playerEntity.distanceToSqr(
      (double)this.worldPosition.getX() + 0.5D,
      (double)this.worldPosition.getY() + 0.5D,
      (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
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
  public void setRecipeUsed(IRecipe<?> recipe) {
    // unused
  }

  @Override
  @Nullable
  public IRecipe<?> getRecipeUsed() {
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
      return; //do not run deletion clientside.
    }
    InventoryHelper.dropContents(this.getLevel(), this.getBlockPos(), this.items);
    super.setRemoved(); // at this point, the block itself is null
  }

  @Override
  public ITextComponent getDisplayName() {
    return MENU_TITLE;
  }

}
