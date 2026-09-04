package mods.railcraft.world.module;

import mods.railcraft.api.container.manipulator.ContainerManipulator;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.util.container.ContainerMapper;
import mods.railcraft.util.container.SlotFilteredResourceHandler;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.crafting.BlastFurnaceRecipe;
import mods.railcraft.world.item.crafting.RailcraftRecipeTypes;
import mods.railcraft.world.level.block.entity.BlastFurnaceBlockEntity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;

public class BlastFurnaceModule extends CookingModule<BlastFurnaceRecipe, BlastFurnaceBlockEntity> {

  public static final int SLOT_INPUT = 0;
  public static final int SLOT_FUEL = 1;
  public static final int SLOT_OUTPUT = 2;
  public static final int SLOT_SLAG = 3;
  private static final int FUEL_PER_TICK = 5;
  private final ContainerMapper fuelContainer, outputContainer, slagContainer;

  private final ResourceHandler<ItemResource> itemHandler;

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
    fuelContainer = ContainerMapper.make(this, SLOT_FUEL, 1);
    outputContainer = ContainerMapper.make(this, SLOT_OUTPUT, 1).ignoreItemChecks();
    slagContainer = ContainerMapper.make(this, SLOT_SLAG, 1).ignoreItemChecks();

    itemHandler = new SlotFilteredResourceHandler<>(VanillaContainerWrapper.of(this),
        index -> index == SLOT_INPUT || index == SLOT_FUEL,
        index -> index != SLOT_INPUT && index != SLOT_FUEL);
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
    var output = this.recipe.assemble(null);

    if (!this.outputContainer.canFit(output)) {
      return false;
    }

    ItemStack nextSlag = RailcraftItems.SLAG.get().getDefaultInstance();
    nextSlag.setCount(recipe.getSlagOutput());

    if (!this.slagContainer.canFit(nextSlag)) {
      return false;
    }

    this.outputContainer.insert(output);
    this.slagContainer.insert(nextSlag);
    this.removeItem(SLOT_INPUT, 1);

    this.setProgress(0);
    return true;
  }

  private int getItemBurnTime(ItemStack itemStack) {
    return itemStack.getBurnTime(null, this.provider.level().fuelValues());
  }

  private void loadFuel() {
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
    var craftRemainder = fuel.getCraftingRemainder();
    fuel.shrink(1);
    this.setItem(SLOT_FUEL, fuel.isEmpty() ? craftRemainder.create() : fuel);
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
      case SLOT_FUEL -> this.isFuel(itemStack);
      case SLOT_OUTPUT, SLOT_SLAG -> true;
      default -> false;
    } && super.canPlaceItem(slot, itemStack);
  }

  public boolean isFuel(ItemStack itemStack) {
    return this.getItemBurnTime(itemStack) > 0;
  }

  public ResourceHandler<ItemResource> getItemHandler() {
    return itemHandler;
  }

  @Override
  public void serialize(ValueOutput valueOutput) {
    super.serialize(valueOutput);
    valueOutput.putInt(CompoundTagKeys.BURN_TIME, this.burnTime);
    valueOutput.putInt(CompoundTagKeys.CURRENT_ITEM_BURN_TIME, this.currentItemBurnTime);
  }

  @Override
  public void deserialize(ValueInput valueInput) {
    super.deserialize(valueInput);
    this.burnTime = valueInput.getIntOr(CompoundTagKeys.BURN_TIME, 0);
    this.currentItemBurnTime = valueInput.getIntOr(CompoundTagKeys.CURRENT_ITEM_BURN_TIME, 0);
  }

  @Override
  public void writeToBuf(RegistryFriendlyByteBuf out) {
    super.writeToBuf(out);
    out.writeVarInt(this.burnTime);
  }

  @Override
  public void readFromBuf(RegistryFriendlyByteBuf in) {
    super.readFromBuf(in);
    this.setBurnTime(in.readVarInt());
  }
}
