package mods.railcraft.world.item;

import mods.railcraft.tags.RailcraftTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ToolMaterial;

public class RailcraftToolMaterial {

  public static final ToolMaterial STEEL = new ToolMaterial(BlockTags.INCORRECT_FOR_WOODEN_TOOL,
      500, 7, 2.5F, 9, RailcraftTags.Items.STEEL_INGOT);
  public static final ToolMaterial BRONZE = new ToolMaterial(BlockTags.INCORRECT_FOR_IRON_TOOL,
      500, 7, 2.5F, 13, RailcraftTags.Items.BRONZE_INGOT);
}
