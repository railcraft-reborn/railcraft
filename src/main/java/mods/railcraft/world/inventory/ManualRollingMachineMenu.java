package mods.railcraft.world.inventory;

import mods.railcraft.util.container.AdvancedContainer;
import mods.railcraft.world.inventory.slot.RollingResultSlot;
import mods.railcraft.world.inventory.slot.UnmodifiableSlot;
import mods.railcraft.world.item.component.RailcraftDataComponents;
import mods.railcraft.world.level.block.entity.ManualRollingMachineBlockEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

public class ManualRollingMachineMenu extends RailcraftMenu {

  private final ManualRollingMachineBlockEntity blockEntity;
  private final ManualRollingMachineBlockEntity.RollingCraftingContainer craftMatrix;
  private final ResultContainer craftResult;
  private final AdvancedContainer resultSlot;

  protected ManualRollingMachineMenu(MenuType<?> type, int id, Inventory inventory,
      ManualRollingMachineBlockEntity blockEntity, int xs, int xy) {
    super(type, id, inventory.player, blockEntity::isStillValid);
    this.blockEntity = blockEntity;
    this.craftMatrix = blockEntity.getCraftMatrix(this);
    this.resultSlot = blockEntity.getInvResult();

    this.craftResult = new ResultContainer() {
      @Override
      public void setItem(int index, ItemStack stack) {
        super.setItem(index, stack);
        stack.set(RailcraftDataComponents.CLICK_TO_CRAFT, true);
      }
    };
    this.addSlot(new SlotRollingMachine(this.craftResult, 0, xs, xy));
    this.addSlot(new RollingResultSlot(this.getPlayer(), this.craftMatrix, this.resultSlot, 0,
        124, 35));

    for (int y = 0; y < 3; y++) {
      for (int x = 0; x < 3; x++) {
        this.addSlot(new Slot(this.craftMatrix, x + y * 3, 30 + (x * 18), 17 + (y * 18)));
      }
    }

    this.addInventorySlots(inventory);
    this.addDataSlot(new SimpleDataSlot(this.blockEntity::getProgress, this.blockEntity::setProgress));
    this.addDataSlot(new SimpleDataSlot(this.blockEntity::getProcessTime, this.blockEntity::setProcessTime));
    this.slotsChanged(this.craftMatrix);
  }

  public ManualRollingMachineMenu(int id, Inventory inventory,
      ManualRollingMachineBlockEntity blockEntity) {
    this(RailcraftMenuTypes.MANUAL_ROLLING_MACHINE.get(), id, inventory, blockEntity, 93, 27);
  }

  @Override
  public void slotsChanged(Container container) {
    if (this.blockEntity.level() instanceof ServerLevel level) {
      this.craftResult.setItem(0, this.blockEntity.getRecipe(level)
          .map(RecipeHolder::value)
          .map(r -> r.assemble(this.craftMatrix.asCraftInput()))
          .orElse(ItemStack.EMPTY));
    }
  }

  public float rollingProgress() {
    int requiredTime = this.blockEntity.getProcessTime();
    int currentTick = this.blockEntity.getProgress();
    return Mth.clamp((float) currentTick / (float) requiredTime, 0, 1);
  }

  @Override
  public void removed(Player player) {
    super.removed(player);
    this.clearContainer(player, this.craftMatrix);
    this.clearContainer(player, this.resultSlot);
  }

  private class SlotRollingMachine extends UnmodifiableSlot {

    SlotRollingMachine(Container contents, int id, int x, int y) {
      super(contents, id, x, y);
    }

    @Override
    public void onTake(Player player, ItemStack stack) {
      super.onTake(player, stack);
      blockEntity.setUseLast();
    }
  }
}
