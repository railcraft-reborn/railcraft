package mods.railcraft.world.item.enchantment;

import mods.railcraft.api.item.Crowbar;
import mods.railcraft.world.item.SpikeMaulItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public final class RailcraftEnchantmentCategories {

  public static final EnchantmentCategory RAILWAY_TOOL =
      EnchantmentCategory.create("railway_tool",
          RailcraftEnchantmentCategories::isRailwayTool);

  private RailcraftEnchantmentCategories() {}

  private static boolean isRailwayTool(Item item) {
    return item instanceof Crowbar || item instanceof SpikeMaulItem;
  }
}
