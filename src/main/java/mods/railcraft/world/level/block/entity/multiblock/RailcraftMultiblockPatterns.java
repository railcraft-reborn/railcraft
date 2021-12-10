package mods.railcraft.world.level.block.entity.multiblock;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Set;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.world.level.block.Blocks;

public class RailcraftMultiblockPatterns {

  private static final BlockPredicate AIR_PREDICATE = new BlockPredicate(null,
      Set.of(Blocks.AIR), StatePropertiesPredicate.ANY, NbtPredicate.ANY);

  public static final MultiblockPattern COKE_OVEN = cokeOven();

  private static final MultiblockPattern cokeOven() {
    final BlockPredicate cokeBrickPredicate = new BlockPredicate(null,
        Set.of(RailcraftBlocks.COKE_OVEN_BRICKS.get()), StatePropertiesPredicate.ANY,
        NbtPredicate.ANY);

    final List<List<BlockPredicate>> arry1 =
        Lists.newArrayList(
            Lists.newArrayList(
                cokeBrickPredicate, cokeBrickPredicate, cokeBrickPredicate),
            Lists.newArrayList(
                cokeBrickPredicate, cokeBrickPredicate, cokeBrickPredicate),
            Lists.newArrayList(
                cokeBrickPredicate, cokeBrickPredicate, cokeBrickPredicate));

    final MultiblockPattern cokeOven =
        new MultiblockPattern.Builder(-1, -1)
            .pattern(arry1)
            .pattern(
                Lists.newArrayList(
                    Lists.newArrayList(
                        cokeBrickPredicate, cokeBrickPredicate, cokeBrickPredicate),
                    Lists.newArrayList(
                        cokeBrickPredicate, AIR_PREDICATE, cokeBrickPredicate),
                    Lists.newArrayList(
                        cokeBrickPredicate, cokeBrickPredicate, cokeBrickPredicate)))
            .pattern(arry1)
            .build();

    return cokeOven;
  }
}
