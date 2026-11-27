package mods.railcraft.world.level.levelgen.structure;

import java.util.Optional;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class GeodeStructure extends Structure {

  public GeodeStructure(StructureSettings settings) {
    super(settings);
  }

  @Override
  protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
    int x = context.chunkPos().getBlockX(9);
    int z = context.chunkPos().getBlockZ(9);

    int seaLevel = context.chunkGenerator().getSeaLevel();
    var biomesHolder = context.biomeResolver().getBiomesWithin(x, seaLevel, z, 29);
    for (var holder : biomesHolder) {
      if (!holder.is(BiomeTags.IS_DEEP_OCEAN)) {
        return Optional.empty();
      }
    }
    return onTopOfChunkCenter(context, Heightmap.Types.OCEAN_FLOOR_WG, (structurePiecesBuilder) -> {
      generatePieces(structurePiecesBuilder, context);
    });
  }

  private void generatePieces(StructurePiecesBuilder builder, GenerationContext context) {
    var chunkPos = context.chunkPos();
    var generator = context.chunkGenerator();
    var heightAccessor = context.heightAccessor();
    var random = context.random();
    var state = context.randomState();

    int x = chunkPos.getBlockX(random.nextInt(16));
    int z = chunkPos.getBlockZ(random.nextInt(16));
    int y = generator.getBaseHeight(x, z, Heightmap.Types.OCEAN_FLOOR, heightAccessor, state);
    builder.addPiece(new GeodeStructurePiece(x, y - 6, z));
  }

  @Override
  public StructureType<?> type() {
    return RailcraftStructureTypes.GEODE.get();
  }
}
