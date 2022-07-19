package mods.railcraft.world.module;

import mods.railcraft.util.container.ContainerMapper;
import mods.railcraft.util.container.manipulator.ContainerManipulator;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.crafting.BlastFurnaceRecipe;
import mods.railcraft.world.item.crafting.RailcraftRecipeTypes;
import mods.railcraft.world.level.block.entity.BlastFurnaceBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;

public class BlastFurnaceModule extends CookingModule<BlastFurnaceRecipe, BlastFurnaceBlockEntity> {

  private static final Map<IForgeRegistry<Item>, Integer> vanillaFuel =
    Map.of(getForgeRegistry(Items.CHARCOAL), 1600);

  public static final int SLOT_INPUT = 0;
  public static final int SLOT_FUEL = 1;
  public static final int SLOT_OUTPUT = 2;
  public static final int SLOT_SLAG = 3;
  private static final int FUEL_PER_TICK = 5;
  private final ContainerMapper fuelContainer = ContainerMapper.make(this, SLOT_FUEL, 1);
  private final ContainerMapper outputContainer =
      new ContainerMapper(this, SLOT_OUTPUT, 1).ignoreItemChecks();
  private final ContainerMapper slagContainer =
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

  public BlastFurnaceModule(BlastFurnaceBlockEntity provider) {
    super(provider, 4, SLOT_INPUT);
  }

  public ContainerManipulator<?> getFuelContainer() {
    return this.fuelContainer;
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
    return RailcraftRecipeTypes.BLASTING.get();
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

    if (!this.outputContainer.canFit(nextResult)) {
      return false;
    }

    ItemStack nextSlag = RailcraftItems.SLAG.get().getDefaultInstance();
    nextSlag.setCount(this.recipe.getSlagOutput());

    if (!this.slagContainer.canFit(nextSlag)) {
      return false;
    }

    this.outputContainer.addStack(nextResult);
    this.slagContainer.addStack(nextSlag);
    this.removeItem(SLOT_INPUT, 1);

    this.setProgress(0);
    return true;
  }

  public boolean needsFuel() {
    return this.getItem(SLOT_FUEL).getCount() < 8;
  }

  public int getItemBurnTime(ItemStack itemStack) {
    return vanillaFuel.getOrDefault(getForgeRegistry(itemStack.getItem()),
        itemStack.getBurnTime(RailcraftRecipeTypes.BLASTING.get()));
  }

  private static IForgeRegistry<Item> getForgeRegistry(Item item) {
    return RegistryManager.ACTIVE.getRegistry(ForgeRegistries.ITEMS.getKey(item));
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
  public boolean canPlaceItem(int slot, ItemStack itemStack) {
    return super.canPlaceItem(slot, itemStack) && switch (slot) {
      case SLOT_INPUT -> true;
      case SLOT_FUEL -> this.isFuel(itemStack);
      default -> false;
    };
  }

  public boolean isFuel(ItemStack itemStack) {
    return this.getItemBurnTime(itemStack) > 0;
  }

  public LazyOptional<IItemHandler> getItemHandler() {
    return this.itemHandler;
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
