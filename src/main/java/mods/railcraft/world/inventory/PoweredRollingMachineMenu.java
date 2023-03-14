package mods.railcraft.world.inventory;

import mods.railcraft.gui.widget.AnalogGaugeWidget;
import mods.railcraft.gui.widget.ChargeNetworkLevelIndicator;
import mods.railcraft.world.level.block.entity.PoweredRollingMachineBlockEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PoweredRollingMachineMenu extends ManualRollingMachineMenu {

  private final AnalogGaugeWidget energyWidget;

  public PoweredRollingMachineMenu(int id, Inventory inventory,
      PoweredRollingMachineBlockEntity blockEntity) {
    super(RailcraftMenuTypes.POWERED_ROLLING_MACHINE.get(), id, inventory, blockEntity, 93, 17);

    var chargeIndicator =
        new ChargeNetworkLevelIndicator(blockEntity.getLevel(), blockEntity.getBlockPos());
    this.energyWidget = new AnalogGaugeWidget(chargeIndicator, 87, 54, 28, 14, 99, 65);
    this.addWidget(energyWidget);
  }

  @Override
  public void removed(Player player) {
    if (player instanceof ServerPlayer serverPlayer) {
      var itemstack = this.getCarried();
      if (!itemstack.isEmpty()) {
        if (player.isAlive() && !serverPlayer.hasDisconnected()) {
          player.getInventory().placeItemBackInInventory(itemstack);
        } else {
          player.drop(itemstack, false);
        }
        this.setCarried(ItemStack.EMPTY);
      }
    }
  }

  public AnalogGaugeWidget getEnergyWidget() {
    return this.energyWidget;
  }
}
