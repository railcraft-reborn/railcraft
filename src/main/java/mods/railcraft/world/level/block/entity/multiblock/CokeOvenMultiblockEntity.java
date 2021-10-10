package mods.railcraft.world.level.block.entity.multiblock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import javax.annotation.Nullable;

import mods.railcraft.world.item.crafting.CokeOvenMenu;
import mods.railcraft.world.item.crafting.CokeOvenRecipe;
import mods.railcraft.world.item.crafting.RailcraftRecipeTypes;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CokeOvenMultiblockEntity extends MultiblockEntity<CokeOvenMultiblockEntity>
    implements INamedContainerProvider, IRecipeHolder, ITickableTileEntity, IFluidTank, IInventory {

  public static final Logger MULTIBLOCK_LOGGER =
      LogManager.getLogger("Railcraft/MultiblockEntity/CokeOvenMultiblockEntity");
  private static final ITextComponent MENU_TITLE =
      new TranslationTextComponent("container.manual_rolling_machine");

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
          return CokeOvenMultiblockEntity.this.recipieRequiredTime;
        case 1:
          return CokeOvenMultiblockEntity.this.currentTick;
        case 2:
          return CokeOvenMultiblockEntity.this.fluid.getAmount();
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

  public CokeOvenMultiblockEntity() {
    super(RailcraftBlockEntityTypes.COKE_OVEN.get());
  }

  public CokeOvenMultiblockEntity(TileEntityType<?> tileEntityType) {
    super(tileEntityType);
  }

  @Override
  public boolean isMultiblockPatternValid(BlockPos normal) {
    return this.getPatternEntities(normal) != null;
  }

  @Override
  public @Nullable Collection<CokeOvenMultiblockEntity> getPatternEntities(BlockPos normal) {
    int box = 2; // 3-1
    BlockPos originPos = this.worldPosition.offset(0, -1, 0).offset(
        (normal.getX() == 0) ? -1 : 0,
        0,
        (normal.getZ() == 0) ? -1 : 0);
    int i = 0;
    Collection<CokeOvenMultiblockEntity> out = new ArrayList<CokeOvenMultiblockEntity>(0);

    for (BlockPos blockpos : BlockPos.betweenClosed(
        // 3 - 1
        originPos, originPos.offset(
          (normal.getX() == 0) ? box : normal.getX() * box,
          2,
          (normal.getZ() == 0) ? box : normal.getZ() * box))) {
      i++;

      @Nullable BlockState theState;
      try {
        theState = this.getLevel().getBlockState(blockpos);
      } catch (Exception e) {
        MULTIBLOCK_LOGGER.info("getPatternEntities - " + e.getMessage());
        this.delink();
        return null;
      }

      Block theBlock = theState.getBlock();

      if (i == 14) {
        if (!theBlock.is(Blocks.AIR)) {
          MULTIBLOCK_LOGGER.info("NOT AIR");
          return null;
        }
        continue;
      }
      if (i != 14
          && (!theBlock.is(RailcraftBlocks.COKE_OVEN_BLOCK.get())
              || !theBlock.hasTileEntity(theState))) {
        MULTIBLOCK_LOGGER.info("NOT COKE OVEN BRICK OR HAS NO TE");
        return null;
      }

      TileEntity te = this.getLevel().getBlockEntity(blockpos);
      if (!(te instanceof CokeOvenMultiblockEntity)) {
        MULTIBLOCK_LOGGER.info("WRONG TE TYPE, TYPE GOT: " + te.toString());
        return null;
      }
      out.add((CokeOvenMultiblockEntity) te);
    }

    return out;
  }

  @Override
  public Container createMenu(int containerProvider,
      PlayerInventory playerInventory, PlayerEntity player) {
    if (!this.isFormed()) {
      return null;
    }
    CokeOvenMultiblockEntity parent = this.getParent();
    if (parent == this) {
      return new CokeOvenMenu(containerProvider, playerInventory, this, this.dataAccess);
    }

    if (parent == null) {
      return null;
    }
    return parent.createMenu(containerProvider, playerInventory, player);
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
    if (this.level.isClientSide() || !this.isFormed()) {
      return;
    }
    // TODO neighbour checking for multiblock.

    ItemStack itemstack = this.items.get(0);
    if (itemstack.isEmpty()) {
      this.recipieRequiredTime = 0;
      return;
    }

    Optional<CokeOvenRecipe> irecipe = this.level.getRecipeManager()
        .getRecipeFor(RailcraftRecipeTypes.COKEING, this, this.level);

    if (!irecipe.isPresent()) {
      this.recipieRequiredTime = 0;
      return;
    }
    CokeOvenRecipe cokeRecipe = irecipe.get();
    if (this.canWork(cokeRecipe)) {
      this.recipieRequiredTime = 0;
      return;
    }
    if (this.currentTick >= this.recipieRequiredTime) {
      this.currentTick = 0;
      this.recipieRequiredTime = this.getTickCost();
      this.bake(cokeRecipe);
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
    if (!this.isFormed()) {
      return false;
    }
    if (this.level.getBlockEntity(this.worldPosition) != this) {
      return false;
    } else {
      return playerEntity.distanceToSqr(
      (double)this.worldPosition.getX() + 0.5D,
      (double)this.worldPosition.getY() + 0.5D,
      (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
    }
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
    return this.level.getRecipeManager()
    .getRecipeFor(RailcraftRecipeTypes.COKEING, this, this.level)
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
  public ITextComponent getDisplayName() {
    return MENU_TITLE;
  }

}
