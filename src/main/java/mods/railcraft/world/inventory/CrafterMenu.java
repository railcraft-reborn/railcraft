package mods.railcraft.world.inventory;

import mods.railcraft.world.level.block.entity.module.CrafterModule;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;

/**
 * Created by CovertJaguar on 1/11/2019 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class CrafterMenu extends RailcraftMenu {

  protected final CrafterModule logic;

  public CrafterMenu(MenuType<?> type, int id, Player player, CrafterModule logic) {
    super(type, id, player, logic::stillValid);
    this.logic = logic;

    this.addDataSlot(new SimpleDataSlot(this.logic::getProgress, this.logic::setProgress));
    this.addDataSlot(new SimpleDataSlot(this.logic::getDuration, this.logic::setDuration));
  }
}
