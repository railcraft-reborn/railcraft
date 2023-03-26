package mods.railcraft.world.module;

import java.util.function.IntSupplier;
import mods.railcraft.util.container.ContainerMapper;
import mods.railcraft.world.item.crafting.RailcraftRecipeTypes;
import mods.railcraft.world.level.block.entity.SteamOvenBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class SteamOvenModule extends CookingModule<BlastingRecipe, SteamOvenBlockEntity> {

  public static final int SLOT_INPUT = 0;
  public static final int SLOT_OUTPUT = 1;
  private static final int FUEL_PER_TICK = 5;
  private final ContainerMapper outputContainer;

  private LazyOptional<IItemHandler> itemHandler;

  /**
   * The number of ticks that the furnace will keep burning
   */
  private int burnTime;
  /**
   * The number of ticks that a fresh copy of the currently-burning item would keep the furnace
   * burning for
   */
  private int currentItemBurnTime;

  public SteamOvenModule(SteamOvenBlockEntity provider) {
    super(provider, 2, SLOT_INPUT);
    outputContainer = ContainerMapper.make(this, SLOT_OUTPUT, 1).ignoreItemChecks();

    itemHandler = LazyOptional.of(() -> new InvWrapper(this) {
      /*@Override
      @NotNull
      public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot == SLOT_INPUT || slot == SLOT_FUEL) {
          return ItemStack.EMPTY;
        }
        return super.extractItem(slot, amount, simulate);
      }*/
    });
  }

  public int getCurrentItemBurnTime() {
    return this.currentItemBurnTime;
  }

  public void setCurrentItemBurnTime(int currentItemBurnTime) {
    this.currentItemBurnTime = currentItemBurnTime;
  }

  public int getBurnTime() {
    return this.burnTime;
  }

  @Override
  public void serverTick() {
    super.serverTick();
    this.setBurnTime(this.burnTime - FUEL_PER_TICK);
  }

  @Override
  protected RecipeType<BlastingRecipe> getRecipeType() {
    return RecipeType.BLASTING;
  }

  @Override
  protected boolean doProcessStep() {
    this.loadFuel();
    return isBurning();
  }

  @Override
  protected boolean craftAndPush() {
    var output = this.recipe.getResultItem();

    if (!this.outputContainer.canFit(output)) {
      return false;
    }

    this.outputContainer.insert(output);
    this.removeItem(SLOT_INPUT, 1);

    this.setProgress(0);
    return true;
  }

  public int getItemBurnTime(ItemStack itemStack) {
    return getVanillaBurnTimeOr(itemStack,
        () -> itemStack.getBurnTime(RailcraftRecipeTypes.BLASTING.get()));
  }

  private static int getVanillaBurnTimeOr(ItemStack itemStack, IntSupplier defaultTime) {
    if (itemStack.is(Items.CHARCOAL)) {
      return 1600;
    }
    return defaultTime.getAsInt();
  }

  private void loadFuel() {

  }

  public void setBurnTime(int burnTime) {
    burnTime = Math.max(0, burnTime);
    boolean wasBurning = isBurning();
    this.burnTime = burnTime;
    if (wasBurning != this.isBurning()) {
      this.provider.syncToClient();
    }
  }

  public boolean isBurning() {
    return this.burnTime > 0;
  }

  public int getBurnProgressScaled(int scale) {
    if (this.burnTime <= 0 || this.currentItemBurnTime <= 0) {
      return 0;
    }
    return Mth.clamp(this.burnTime * scale / this.currentItemBurnTime, 0, scale);
  }

  @Override
  public boolean canPlaceItem(int slot, ItemStack itemStack) {
    return switch (slot) {
      case SLOT_INPUT -> this.getRecipeFor(itemStack).isPresent();
      case SLOT_OUTPUT -> true;
      default -> false;
    } && super.canPlaceItem(slot, itemStack);
  }

  public LazyOptional<IItemHandler> getItemHandler() {
    return itemHandler;
  }

  public void invalidItemHandler() {
    itemHandler.invalidate();
  }

  @Override
  public CompoundTag serializeNBT() {
    var tag = super.serializeNBT();
    tag.putInt("burnTime", this.burnTime);
    tag.putInt("currentItemBurnTime", this.currentItemBurnTime);
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundTag tag) {
    super.deserializeNBT(tag);
    this.burnTime = tag.getInt("burnTime");
    this.currentItemBurnTime = tag.getInt("currentItemBurnTime");
  }

  @Override
  public void writeToBuf(FriendlyByteBuf out) {
    super.writeToBuf(out);
    out.writeVarInt(this.burnTime);
  }

  @Override
  public void readFromBuf(FriendlyByteBuf in) {
    super.readFromBuf(in);
    this.setBurnTime(in.readVarInt());
  }
}
