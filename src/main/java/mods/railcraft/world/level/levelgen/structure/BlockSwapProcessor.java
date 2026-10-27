package mods.railcraft.world.level.levelgen.structure;

import java.util.Map;
import org.jetbrains.annotations.Nullable;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

/**
 * Swaps blocks of a structure for other ones, keeping their properties, so stairs keep facing the
 * same way.
 */
public class BlockSwapProcessor extends StructureProcessor {

  public static final MapCodec<BlockSwapProcessor> CODEC =
      Codec.unboundedMap(BuiltInRegistries.BLOCK.byNameCodec(),
              BuiltInRegistries.BLOCK.byNameCodec())
          .fieldOf("swaps")
          .xmap(BlockSwapProcessor::new, processor -> processor.swaps);

  private final Map<Block, Block> swaps;

  public BlockSwapProcessor(Map<Block, Block> swaps) {
    this.swaps = swaps;
  }

  @Nullable
  @Override
  public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos offset,
      BlockPos pos, StructureTemplate.StructureBlockInfo blockInfo,
      StructureTemplate.StructureBlockInfo relativeBlockInfo, StructurePlaceSettings settings) {
    var block = this.swaps.get(relativeBlockInfo.state().getBlock());
    if (block == null) {
      return relativeBlockInfo;
    }
    return new StructureTemplate.StructureBlockInfo(relativeBlockInfo.pos(),
        block.withPropertiesOf(relativeBlockInfo.state()), relativeBlockInfo.nbt());
  }

  @Override
  protected StructureProcessorType<?> getType() {
    return RailcraftStructureProcessorTypes.BLOCK_SWAP.get();
  }
}
