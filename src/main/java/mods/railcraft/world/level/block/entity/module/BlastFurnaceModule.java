package mods.railcraft.world.level.block.entity.module;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import mods.railcraft.util.container.wrappers.ContainerMapper;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.crafting.BlastFurnaceRecipe;
import mods.railcraft.world.item.crafting.RailcraftRecipeTypes;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.registries.IRegistryDelegate;

public class BlastFurnaceModule extends CookingModule<BlastFurnaceRecipe>
    implements ICapabilityProvider {

  private static final Map<IRegistryDelegate<Item>, Integer> vanillaFuel =
      Map.of(Items.CHARCOAL.delegate, 1600);

  public static final int SLOT_INPUT = 0;
  public static final int SLOT_FUEL = 1;
  public static final int SLOT_OUTPUT = 2;
  public static final int SLOT_SLAG = 3;
  private static final int FUEL_PER_TICK = 5;
  public final ContainerMapper invFuel = ContainerMapper.make(this, SLOT_FUEL, 1);
  private final ContainerMapper invOutput =
      new ContainerMapper(this, SLOT_OUTPUT, 1).ignoreItemChecks();
  private final ContainerMapper invSlag =
      new ContainerMapper(this, SLOT_SLAG, 1).ignoreItemChecks();

  private final LazyOptional<IItemHandler> itemHandler =
      LazyOptional.of(() -> new InvWrapper(this) {
        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
          if (slot != SLOT_OUTPUT && slot != SLOT_SLAG) {
            return ItemStack.EMPTY;
          }
          return super.extractItem(slot, amount, simulate);
        }
      });

  /**
   * The number of ticks that the furnace will keep burning
   */
  private int burnTime;
  /**
   * The number of ticks that a fresh copy of the currently-burning item would keep the furnace
   * burning for
   */
  private int currentItemBurnTime;

  public BlastFurnaceModule(ModuleProvider provider) {
    super(provider, 4, SLOT_INPUT);
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
  protected RecipeType<BlastFurnaceRecipe> getRecipeType() {
    return RailcraftRecipeTypes.BLASTING;
  }

  @Override
  protected boolean doProcessStep() {
    this.loadFuel();
    return isBurning();
  }

  @Override
  protected boolean craftAndPush() {
    Objects.requireNonNull(this.recipe);
    var nextResult = this.recipe.getResultItem();

    if (!this.invOutput.canFit(nextResult)) {
      return false;
    }

    ItemStack nextSlag = RailcraftItems.SLAG.get().getDefaultInstance();
    nextSlag.setCount(this.recipe.getSlagOutput());

    if (!this.invSlag.canFit(nextSlag)) {
      return false;
    }

    this.invOutput.addStack(nextResult);
    this.invSlag.addStack(nextSlag);
    this.removeItem(SLOT_INPUT, 1);

    this.setProgress(0);
    return true;
  }

  public boolean needsFuel() {
    return this.getItem(SLOT_FUEL).getCount() < 8;
  }

  public int getItemBurnTime(ItemStack itemStack) {
    return vanillaFuel.getOrDefault(itemStack.getItem().delegate,
        itemStack.getBurnTime(RailcraftRecipeTypes.BLASTING));
  }

  void loadFuel() {
    ItemStack fuel;
    if (this.burnTime > FUEL_PER_TICK * 2 || (fuel = this.getItem(SLOT_FUEL)).isEmpty()) {
      return;
    }
    int itemBurnTime = this.getItemBurnTime(fuel);

    if (itemBurnTime <= 0) {
      return;
    }
    this.currentItemBurnTime = itemBurnTime + this.burnTime;
    this.setBurnTime(this.currentItemBurnTime);
    fuel.shrink(1);
    this.setItem(SLOT_FUEL, fuel.isEmpty() ? ItemStack.EMPTY : fuel);
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
  public boolean canPlaceItem(int slot, ItemStack stack) {
    if (!super.canPlaceItem(slot, stack)) {
      return false;
    }
    switch (slot) {
      case SLOT_OUTPUT:
      case SLOT_SLAG:
        return false;
      case SLOT_FUEL:
        return this.getItemBurnTime(stack) > 0;
      case SLOT_INPUT:
        return true;
    }
    return false;
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> capability,
      @Nullable Direction direction) {
    return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
        ? this.itemHandler.cast()
        : LazyOptional.empty();
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
