package mods.railcraft.world.level.block.entity.multiblock;

import java.util.Optional;
import javax.annotation.Nullable;
import mods.railcraft.world.item.crafting.CokeOvenMenu;
import mods.railcraft.world.item.crafting.CokeOvenRecipe;
import mods.railcraft.world.item.crafting.RailcraftRecipeTypes;
import mods.railcraft.world.level.block.CokeOvenBricksBlock;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.material.fluid.FluidTools;
import mods.railcraft.world.level.material.fluid.RailcraftFluids;
import mods.railcraft.world.level.material.fluid.TankManager;
import mods.railcraft.world.level.material.fluid.tank.FilteredTank;
import mods.railcraft.world.level.material.fluid.tank.StandardTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class CokeOvenBlockEntity extends MultiblockBlockEntity<CokeOvenBlockEntity>
    implements RecipeHolder, Container {

  private static final Component MENU_TITLE =
      new TranslatableComponent("container.coke_oven");

  private static final int FLUID_STORAGE_MAX = 10000;

  // internal inventory, 3 total. 0 IN, 1 OUT
  protected NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);

  private int recipieRequiredTime = 0;
  private int currentTick = 0;

  // Is this performant enough? \/
  protected final StandardTank fluidTank = new FilteredTank(FluidTools.BUCKET_VOLUME * 6)
      .setFilterFluid(RailcraftFluids.CREOSOTE)
      .disableFill();
  // same with this
  private LazyOptional<TankManager> tankManager =
      LazyOptional.of(() -> new TankManager(this.fluidTank));

  @Nullable
  private Recipe<?> cachedRecipie = null;

  // 0 - required time, 1 - current tick
  protected final ContainerData dataAccess = new ContainerData() {
    public int get(int key) {
      switch (key) {
        case 0:
          return CokeOvenBlockEntity.this.recipieRequiredTime;
        case 1:
          return CokeOvenBlockEntity.this.currentTick;
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
        default:
          break;
      }

    }

    public int getCount() {
      return 2;
    }
  };

  public CokeOvenBlockEntity(BlockPos blockPos, BlockState blockState) {
    this(RailcraftBlockEntityTypes.COKE_OVEN.get(), blockPos, blockState);
  }

  public CokeOvenBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
    super(type, blockPos, blockState, RailcraftMultiblockPatterns.COKE_OVEN);
  }

  public TankManager getTankManager() {
    return this.tankManager.orElseThrow(() -> new IllegalStateException("Expected tank manager"));
  }

  @Override
  public boolean tryToMakeParent(Direction facingDir) {
    boolean returnValue = super.tryToMakeParent(facingDir);
    // tank nonexistant? make new one.
    if (returnValue && this.tankManager.equals(LazyOptional.empty())) {
      this.tankManager = LazyOptional.of(() -> new TankManager(this.fluidTank));
    }
    return returnValue;
  }

  @Override
  public void delink() {
    super.delink();
    if (!this.getLevel().isClientSide()) {
      BlockState state = this.level.getBlockState(this.worldPosition);
      state.setValue(CokeOvenBricksBlock.PARENT, false);
      state.setValue(CokeOvenBricksBlock.LIT, false);
      this.level.setBlock(this.worldPosition, state, 3);

      // this.fluidTank = LazyOptional.empty(); // remove the fluidtank ref
      this.tankManager = LazyOptional.empty(); // ditto
    }
  }

  /// does not exist on TE's
  public InteractionResult interact(Player player, InteractionHand hand) {
    if (!this.isFormed()) {
      return InteractionResult.PASS;
    }
    return FluidTools.interactWithFluidHandler(player, hand, this.getTankManager())
        ? InteractionResult.SUCCESS
        : InteractionResult.PASS;
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
    if (this.getLevel().isClientSide()) {
      return;
    }
    if (this.isParent()) {
      this.level.setBlock(this.worldPosition,
          this.level.getBlockState(this.worldPosition)
              .setValue(CokeOvenBricksBlock.PARENT, true),
          3);
      return;
    }
    // we set it during actual load (actual without the '"', thanks 1.18!)
    this.tankManager = LazyOptional.of(() -> this.getParent().getTankManager());
    // this.fluidTank = LazyOptional.empty();
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
    ContainerHelper.loadAllItems(tag, this.items);
    this.recipieRequiredTime = tag.getInt("recipieRequiredTime");
    this.currentTick = tag.getInt("currentTick");

    if (tag.contains("tankManager")) {
      this.getTankManager().deserializeNBT(tag.getList("tankManager", Tag.TAG_COMPOUND));
    }
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    ContainerHelper.saveAllItems(tag, this.items);
    tag.putInt("recipieRequiredTime", this.recipieRequiredTime);
    tag.putInt("currentTick", this.currentTick);

    if (this.isParent()) {
      tag.put("tankManager", this.getTankManager().serializeNBT());
    }
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
    if (CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY == capability) {
      return this.tankManager.cast();
    }
    return super.getCapability(capability, facing);
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
      blockEntity.setRecipeUsed(null);
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
      blockEntity.setRecipeUsed(null);
      return;
    }

    @Nullable
    Recipe<?> recipe = blockEntity.getRecipeUsed();

    if (recipe == null) {
      blockEntity.recipieRequiredTime = 0;
      // check if it exists, else L
      Optional<CokeOvenRecipe> irecipe = level.getServer()
          .getRecipeManager()
          .getRecipeFor(RailcraftRecipeTypes.COKE_OVEN_COOKING, blockEntity, level);

      if (!irecipe.isPresent()) {
        blockEntity.setRecipeUsed(null);
        return;
      }
      blockEntity.setRecipeUsed(irecipe.get());
    }

    CokeOvenRecipe cokeRecipe = CokeOvenRecipe.class.cast(recipe);

    if (!blockEntity.canWork(cokeRecipe)) {
      blockEntity.recipieRequiredTime = 0;
      return;
    }

    if (blockEntity.currentTick >= blockEntity.recipieRequiredTime) {
      if (blockEntity.recipieRequiredTime != 0) {
        blockEntity.setRecipeUsed(null);
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
  public void setRecipeUsed(@Nullable Recipe<?> recipe) {
    this.cachedRecipie = recipe;
  }

  @Override
  @Nullable
  public Recipe<?> getRecipeUsed() {
    return this.cachedRecipie;
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
    if (!resultSlot.sameItem(resultItemStack) && !this.fluidTank.isFluidValid(resultFluidStack)) {
      return false;
    }
    // respect stacks (and fluid count.)
    if ((resultSlot.getCount() + resultItemStack.getCount() <= this.getMaxStackSize())
        && resultSlot.getCount() + resultItemStack.getCount() <= resultSlot.getMaxStackSize()
        && this.fluidTank.getFluidAmount() + resultFluidStack.getAmount() <= FLUID_STORAGE_MAX
    ) {
      return true;
    }
    return (resultSlot.getCount() + resultItemStack.getCount() <= resultItemStack.getMaxStackSize())
        && this.fluidTank.getFluidAmount() + resultFluidStack.getAmount() <= FLUID_STORAGE_MAX;
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

    this.fluidTank.internalFill(resultFluidStack.copy(), FluidAction.EXECUTE);

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
