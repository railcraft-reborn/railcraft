package mods.railcraft.world.level.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class CokeOvenBlock extends Block {

  public CokeOvenBlock(Properties properties) {
    super(properties
        .harvestTool(ToolType.PICKAXE));
  }

}
