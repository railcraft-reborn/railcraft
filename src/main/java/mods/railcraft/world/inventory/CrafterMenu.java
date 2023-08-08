package mods.railcraft.world.inventory;

import mods.railcraft.world.module.CrafterModule;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;

public class CrafterMenu extends RailcraftMenu {

  protected final CrafterModule<?> module;

  public CrafterMenu(MenuType<?> type, int id, Player player, CrafterModule<?> module) {
    super(type, id, player, module::stillValid);
    this.module = module;

    this.addDataSlot(new SimpleDataSlot(this.module::getProgress, this.module::setProgress));
    this.addDataSlot(new SimpleDataSlot(this.module::getDuration, this.module::setDuration));
  }
}
