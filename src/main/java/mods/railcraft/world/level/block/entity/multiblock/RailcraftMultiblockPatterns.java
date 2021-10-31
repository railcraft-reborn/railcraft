package mods.railcraft.world.level.block.entity.multiblock;

import com.google.common.collect.Lists;

import java.util.ArrayList;

import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.advancements.criterion.BlockPredicate;
import net.minecraft.advancements.criterion.NBTPredicate;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.Blocks;

public class RailcraftMultiblockPatterns {

  private static final BlockPredicate airPredicate = new BlockPredicate(null,
      Blocks.AIR, StatePropertiesPredicate.ANY, NBTPredicate.ANY);

  public static final MultiblockPattern COKE_OVEN = cokeOven();



  private static final MultiblockPattern cokeOven() {
    final BlockPredicate cokeBrickPredicate = new BlockPredicate(null,
        RailcraftBlocks.COKE_OVEN_BRICKS.get(), StatePropertiesPredicate.ANY, NBTPredicate.ANY);

    final ArrayList<ArrayList<BlockPredicate>> arry1 =
        Lists.newArrayList(
          Lists.newArrayList(
            cokeBrickPredicate, cokeBrickPredicate, cokeBrickPredicate
          ),
          Lists.newArrayList(
            cokeBrickPredicate, cokeBrickPredicate, cokeBrickPredicate
          ),
          Lists.newArrayList(
            cokeBrickPredicate, cokeBrickPredicate, cokeBrickPredicate
          )
        );

    final MultiblockPattern cokeOven =
        new MultiblockPattern.Builder(-1, -1)
          .pattern(arry1)
          .pattern(
            Lists.newArrayList(
              Lists.newArrayList(
                cokeBrickPredicate, cokeBrickPredicate, cokeBrickPredicate
              ),
              Lists.newArrayList(
                cokeBrickPredicate, airPredicate, cokeBrickPredicate
              ),
              Lists.newArrayList(
                cokeBrickPredicate, cokeBrickPredicate, cokeBrickPredicate
              )
            )
          )
          .pattern(arry1)
          .build();

    return cokeOven;
  }
}
