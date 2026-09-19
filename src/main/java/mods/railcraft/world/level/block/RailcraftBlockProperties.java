package mods.railcraft.world.level.block;

import java.util.function.ToIntFunction;
import java.util.function.UnaryOperator;
import mods.railcraft.world.level.block.track.TrackConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class RailcraftBlockProperties {

  public static UnaryOperator<BlockBehaviour.Properties> detector() {
    return __ -> BlockBehaviour.Properties.of()
        .mapColor(MapColor.STONE)
        .strength(2.0F, 4.5F)
        .requiresCorrectToolForDrops()
        .sound(SoundType.METAL);
  }

  public static UnaryOperator<BlockBehaviour.Properties> steamBoilerTank() {
    return __ -> BlockBehaviour.Properties.of()
        .mapColor(MapColor.METAL)
        .strength(5.0F, 6.0F)
        .requiresCorrectToolForDrops()
        .noOcclusion()
        .sound(SoundType.METAL);
  }

  public static UnaryOperator<BlockBehaviour.Properties> fueledFirebox() {
    return __ -> BlockBehaviour.Properties.of()
        .mapColor(MapColor.STONE)
        .strength(3.5F)
        .requiresCorrectToolForDrops()
        .lightLevel(litBlockEmission(13))
        .sound(SoundType.METAL);
  }

  public static UnaryOperator<BlockBehaviour.Properties> battery() {
    return __ -> BlockBehaviour.Properties.of()
        .mapColor(MapColor.METAL)
        .strength(3F)
        .randomTicks()
        .requiresCorrectToolForDrops()
        .sound(SoundType.METAL);
  }

  public static UnaryOperator<BlockBehaviour.Properties> steelAnvil() {
    return __ -> BlockBehaviour.Properties.of()
        .mapColor(MapColor.METAL)
        .pushReaction(PushReaction.BLOCK)
        .requiresCorrectToolForDrops()
        .strength(5.0F, 2000.0F)
        .sound(SoundType.ANVIL);
  }

  public static BlockBehaviour.Properties ingotBlock(BlockBehaviour.Properties properties) {
    return properties
        .mapColor(MapColor.METAL)
        .strength(5.0F, 15.0F)
        .sound(SoundType.METAL)
        .requiresCorrectToolForDrops();
  }

  public static BlockBehaviour.Properties oreInGroundStone(BlockBehaviour.Properties properties) {
    return properties
        .mapColor(MapColor.STONE)
        .instrument(NoteBlockInstrument.BASEDRUM)
        .strength(3.0F, 3.0F)
        .requiresCorrectToolForDrops();
  }

  public static BlockBehaviour.Properties oreInGroundDeepslate(BlockBehaviour.Properties properties) {
    return properties
        .mapColor(MapColor.DEEPSLATE)
        .instrument(NoteBlockInstrument.BASEDRUM)
        .strength(4.5F, 3.0F)
        .sound(SoundType.DEEPSLATE)
        .requiresCorrectToolForDrops();
  }

  public static UnaryOperator<BlockBehaviour.Properties> fluidManipulator() {
    return __ -> BlockBehaviour.Properties.of()
        .mapColor(MapColor.STONE)
        .strength(3.5F)
        .requiresCorrectToolForDrops()
        .sound(SoundType.STONE)
        .noOcclusion();
  }

  public static UnaryOperator<BlockBehaviour.Properties> manipulator() {
    return __ -> BlockBehaviour.Properties.of()
        .mapColor(MapColor.STONE)
        .strength(3.5F)
        .requiresCorrectToolForDrops()
        .sound(SoundType.STONE);
  }

  public static UnaryOperator<BlockBehaviour.Properties> railSupportBlocks() {
    return __ -> BlockBehaviour.Properties.of()
        .strength(8.0F, 50.0F)
        .sound(SoundType.METAL)
        .noOcclusion();
  }

  public static BlockBehaviour.Properties standardTrack(BlockBehaviour.Properties properties) {
    return properties
        .noCollision()
        .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
        .sound(SoundType.METAL);
  }

  public static BlockBehaviour.Properties electricTrack(BlockBehaviour.Properties properties) {
    return standardTrack(properties).randomTicks();
  }

  public static BlockBehaviour.Properties reinforcedTrack(BlockBehaviour.Properties properties) {
    return standardTrack(properties)
        .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE);
  }

  public static UnaryOperator<BlockBehaviour.Properties> worldSpike() {
    return __ -> BlockBehaviour.Properties.ofFullCopy(Blocks.OBSIDIAN)
        .sound(SoundType.STONE);
  }

  public static BlockBehaviour.Properties strengthenedGlass() {
    return BlockBehaviour.Properties.of()
        .instrument(NoteBlockInstrument.HAT)
        .sound(SoundType.GLASS)
        .noOcclusion()
        .strength(1.0F, 5.0F)
        .requiresCorrectToolForDrops()
        .isValidSpawn(RailcraftBlockProperties::never)
        .isRedstoneConductor(RailcraftBlockProperties::never)
        .isSuffocating(RailcraftBlockProperties::never)
        .isViewBlocking(RailcraftBlockProperties::never);
  }

  public static BlockBehaviour.Properties ironTank() {
    return BlockBehaviour.Properties.of()
        .mapColor(MapColor.METAL)
        .sound(SoundType.METAL)
        .noOcclusion()
        .isValidSpawn(RailcraftBlockProperties::never)
        .strength(1.0F, 5.0F)
        .requiresCorrectToolForDrops()
        .explosionResistance(12);
  }

  public static BlockBehaviour.Properties ironTankGauge() {
    return ironTank()
        .instrument(NoteBlockInstrument.HAT)
        .mapColor(MapColor.NONE)
        .sound(SoundType.GLASS)
        .isRedstoneConductor(RailcraftBlockProperties::never)
        .isSuffocating(RailcraftBlockProperties::never)
        .isViewBlocking(RailcraftBlockProperties::never)
        .lightLevel(LightBlock.LIGHT_EMISSION);
  }

  public static BlockBehaviour.Properties steelTank() {
    return BlockBehaviour.Properties.of()
        .mapColor(MapColor.METAL)
        .sound(SoundType.METAL)
        .noOcclusion()
        .isValidSpawn(RailcraftBlockProperties::never)
        .strength(1.5F, 6.0F)
        .requiresCorrectToolForDrops()
        .explosionResistance(15);
  }

  public static BlockBehaviour.Properties steelTankGauge() {
    return steelTank()
        .instrument(NoteBlockInstrument.HAT)
        .mapColor(MapColor.NONE)
        .sound(SoundType.GLASS)
        .isRedstoneConductor(RailcraftBlockProperties::never)
        .isSuffocating(RailcraftBlockProperties::never)
        .isViewBlocking(RailcraftBlockProperties::never)
        .lightLevel(LightBlock.LIGHT_EMISSION);
  }

  public static BlockBehaviour.Properties post() {
    return BlockBehaviour.Properties.of()
        .mapColor(MapColor.METAL)
        .strength(2.0F, 3.0F)
        .requiresCorrectToolForDrops()
        .sound(SoundType.METAL);
  }

  private static ToIntFunction<BlockState> litBlockEmission(int light) {
    return blockState -> blockState.getValue(BlockStateProperties.LIT) ? light : 0;
  }

  private static Boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos,
      EntityType<?> entityType) {
    return false;
  }

  private static boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
    return false;
  }
}
