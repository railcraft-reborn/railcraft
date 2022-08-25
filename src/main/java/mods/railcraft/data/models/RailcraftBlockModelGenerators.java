package mods.railcraft.data.models;

import com.google.gson.JsonElement;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import mods.railcraft.Railcraft;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.data.models.blockstates.BlockStateGenerator;
import net.minecraft.data.models.blockstates.Condition;
import net.minecraft.data.models.blockstates.MultiPartGenerator;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class RailcraftBlockModelGenerators {

  private final Consumer<BlockStateGenerator> blockStateOutput;
  private final BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput;
  private final Consumer<Item> skippedAutoModelsOutput;

  public RailcraftBlockModelGenerators(Consumer<BlockStateGenerator> blockStateOutput,
      BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput,
      Consumer<Item> skippedAutoModelsOutput) {
    this.blockStateOutput = blockStateOutput;
    this.modelOutput = modelOutput;
    this.skippedAutoModelsOutput = skippedAutoModelsOutput;
  }

  public void run() {
    this.skipAutoItemBlock(RailcraftBlocks.FORCE_TRACK_EMITTER.get());
    this.skipAutoItemBlock(RailcraftBlocks.SIGNAL_CAPACITOR_BOX.get());
    this.skipAutoItemBlock(RailcraftBlocks.SIGNAL_CONTROLLER_BOX.get());
    this.skipAutoItemBlock(RailcraftBlocks.SIGNAL_INTERLOCK_BOX.get());
    this.skipAutoItemBlock(RailcraftBlocks.SIGNAL_RECEIVER_BOX.get());
    this.skipAutoItemBlock(RailcraftBlocks.SIGNAL_SEQUENCER_BOX.get());
    this.skipAutoItemBlock(RailcraftBlocks.ANALOG_SIGNAL_CONTROLLER_BOX.get());
    this.skipAutoItemBlock(RailcraftBlocks.SIGNAL_BLOCK_RELAY_BOX.get());
    this.skipAutoItemBlock(RailcraftBlocks.BLOCK_SIGNAL.get());
    this.skipAutoItemBlock(RailcraftBlocks.DISTANT_SIGNAL.get());
    this.skipAutoItemBlock(RailcraftBlocks.TOKEN_SIGNAL.get());
    this.skipAutoItemBlock(RailcraftBlocks.DUAL_BLOCK_SIGNAL.get());
    this.skipAutoItemBlock(RailcraftBlocks.DUAL_DISTANT_SIGNAL.get());
    this.skipAutoItemBlock(RailcraftBlocks.DUAL_TOKEN_SIGNAL.get());
    this.skipAutoItemBlock(RailcraftBlocks.SWITCH_TRACK_LEVER.get());
    this.skipAutoItemBlock(RailcraftBlocks.SWITCH_TRACK_MOTOR.get());

    this.createAbandonedTracks(
        RailcraftBlocks.ABANDONED_TRACK.get(),
        RailcraftBlocks.ABANDONED_LOCKING_TRACK.get(),
        RailcraftBlocks.ABANDONED_BUFFER_STOP_TRACK.get(),
        RailcraftBlocks.ABANDONED_ACTIVATOR_TRACK.get(),
        RailcraftBlocks.ABANDONED_BOOSTER_TRACK.get(),
        RailcraftBlocks.ABANDONED_CONTROL_TRACK.get(),
        RailcraftBlocks.ABANDONED_GATED_TRACK.get(),
        RailcraftBlocks.ABANDONED_DETECTOR_TRACK.get(),
        RailcraftBlocks.ABANDONED_COUPLER_TRACK.get(),
        RailcraftBlocks.ABANDONED_EMBARKING_TRACK.get(),
        RailcraftBlocks.ABANDONED_DISEMBARKING_TRACK.get(),
        RailcraftBlocks.ABANDONED_TURNOUT_TRACK.get(),
        RailcraftBlocks.ABANDONED_WYE_TRACK.get(),
        RailcraftBlocks.ABANDONED_JUNCTION_TRACK.get(),
        RailcraftBlocks.ABANDONED_LAUNCHER_TRACK.get(),
        RailcraftBlocks.ABANDONED_ONE_WAY_TRACK.get(),
        RailcraftBlocks.ABANDONED_LOCOMOTIVE_TRACK.get());
    this.createTracks(
        RailcraftBlocks.ELECTRIC_TRACK.get(),
        RailcraftBlocks.ELECTRIC_LOCKING_TRACK.get(),
        RailcraftBlocks.ELECTRIC_BUFFER_STOP_TRACK.get(),
        RailcraftBlocks.ELECTRIC_ACTIVATOR_TRACK.get(),
        RailcraftBlocks.ELECTRIC_BOOSTER_TRACK.get(),
        RailcraftBlocks.ELECTRIC_CONTROL_TRACK.get(),
        RailcraftBlocks.ELECTRIC_GATED_TRACK.get(),
        RailcraftBlocks.ELECTRIC_DETECTOR_TRACK.get(),
        RailcraftBlocks.ELECTRIC_COUPLER_TRACK.get(),
        RailcraftBlocks.ELECTRIC_EMBARKING_TRACK.get(),
        RailcraftBlocks.ELECTRIC_DISEMBARKING_TRACK.get(),
        RailcraftBlocks.ELECTRIC_TURNOUT_TRACK.get(),
        RailcraftBlocks.ELECTRIC_WYE_TRACK.get(),
        RailcraftBlocks.ELECTRIC_JUNCTION_TRACK.get(),
        RailcraftBlocks.ELECTRIC_LAUNCHER_TRACK.get(),
        RailcraftBlocks.ELECTRIC_ONE_WAY_TRACK.get(),
        RailcraftBlocks.ELECTRIC_LOCOMOTIVE_TRACK.get());
    this.createOutfittedTracks(Blocks.RAIL,
        RailcraftBlocks.IRON_LOCKING_TRACK.get(),
        RailcraftBlocks.IRON_BUFFER_STOP_TRACK.get(),
        RailcraftBlocks.IRON_ACTIVATOR_TRACK.get(),
        RailcraftBlocks.IRON_BOOSTER_TRACK.get(),
        RailcraftBlocks.IRON_CONTROL_TRACK.get(),
        RailcraftBlocks.IRON_GATED_TRACK.get(),
        RailcraftBlocks.IRON_DETECTOR_TRACK.get(),
        RailcraftBlocks.IRON_COUPLER_TRACK.get(),
        RailcraftBlocks.IRON_EMBARKING_TRACK.get(),
        RailcraftBlocks.IRON_DISEMBARKING_TRACK.get(),
        RailcraftBlocks.IRON_TURNOUT_TRACK.get(),
        RailcraftBlocks.IRON_WYE_TRACK.get(),
        RailcraftBlocks.IRON_JUNCTION_TRACK.get(),
        RailcraftBlocks.IRON_LAUNCHER_TRACK.get(),
        RailcraftBlocks.IRON_ONE_WAY_TRACK.get(),
        RailcraftBlocks.IRON_LOCOMOTIVE_TRACK.get());
    this.createTracks(
        RailcraftBlocks.REINFORCED_TRACK.get(),
        RailcraftBlocks.REINFORCED_LOCKING_TRACK.get(),
        RailcraftBlocks.REINFORCED_BUFFER_STOP_TRACK.get(),
        RailcraftBlocks.REINFORCED_ACTIVATOR_TRACK.get(),
        RailcraftBlocks.REINFORCED_BOOSTER_TRACK.get(),
        RailcraftBlocks.REINFORCED_CONTROL_TRACK.get(),
        RailcraftBlocks.REINFORCED_GATED_TRACK.get(),
        RailcraftBlocks.REINFORCED_DETECTOR_TRACK.get(),
        RailcraftBlocks.REINFORCED_COUPLER_TRACK.get(),
        RailcraftBlocks.REINFORCED_EMBARKING_TRACK.get(),
        RailcraftBlocks.REINFORCED_DISEMBARKING_TRACK.get(),
        RailcraftBlocks.REINFORCED_TURNOUT_TRACK.get(),
        RailcraftBlocks.REINFORCED_WYE_TRACK.get(),
        RailcraftBlocks.REINFORCED_JUNCTION_TRACK.get(),
        RailcraftBlocks.REINFORCED_LAUNCHER_TRACK.get(),
        RailcraftBlocks.REINFORCED_ONE_WAY_TRACK.get(),
        RailcraftBlocks.REINFORCED_LOCOMOTIVE_TRACK.get());
    this.createTracks(
        RailcraftBlocks.STRAP_IRON_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_LOCKING_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_BUFFER_STOP_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_ACTIVATOR_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_BOOSTER_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_CONTROL_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_GATED_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_DETECTOR_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_COUPLER_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_EMBARKING_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_DISEMBARKING_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_TURNOUT_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_WYE_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_JUNCTION_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_LAUNCHER_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_ONE_WAY_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_LOCOMOTIVE_TRACK.get());
  }

  private void skipAutoItemBlock(Block block) {
    this.skippedAutoModelsOutput.accept(block.asItem());
  }

  private void createSimpleFlatItemModel(Item item) {
    ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item),
        TextureMapping.layer0(item), this.modelOutput);
  }


  private ResourceLocation createVariant(String name,
      ModelTemplate model, Function<ResourceLocation, TextureMapping> textureFactory) {
    return model.create(
        new ResourceLocation(Railcraft.ID, "block/" + name + model.suffix.orElse("")),
        textureFactory.apply(new ResourceLocation(Railcraft.ID, "block/" + name)),
        this.modelOutput);
  }

  private void createAbandonedTracks(Block block, Block lockingTrackBlock,
      Block bufferStopTrackBlock, Block activatorTrackBlock, Block boosterTrackBlock,
      Block controlTrackBlock, Block gatedTrackBlock, Block detectorTrackBlock,
      Block couplerTrackBlock, Block embarkingTrackBlock, Block disembarkingTrackBlock,
      Block turnoutTrackBlock, Block wyeTrackBlock, Block junctionTrackBlock,
      Block launcherTrackBlock, Block oneWayTrackBlock, Block locomotiveTrackBlock) {
    this.createOutfittedTracks(block, lockingTrackBlock, bufferStopTrackBlock, activatorTrackBlock,
        boosterTrackBlock, controlTrackBlock, gatedTrackBlock, detectorTrackBlock,
        couplerTrackBlock, embarkingTrackBlock, disembarkingTrackBlock, turnoutTrackBlock,
        wyeTrackBlock, junctionTrackBlock, launcherTrackBlock, oneWayTrackBlock,
        locomotiveTrackBlock);
  }

  private void createTracks(Block block, Block lockingTrackBlock, Block bufferStopTrackBlock,
      Block activatorTrackBlock, Block boosterTrackBlock, Block controlTrackBlock,
      Block gatedTrackBlock, Block detectorTrackBlock, Block couplerTrackBlock,
      Block embarkingTrackBlock, Block disembarkingTrackBlock, Block turnoutTrackBlock,
      Block wyeTrackBlock, Block junctionTrackBlock, Block launcherTrackBlock,
      Block oneWayTrackBlock, Block locomotiveTrackBlock) {
    this.createOutfittedTracks(block, lockingTrackBlock, bufferStopTrackBlock, activatorTrackBlock,
        boosterTrackBlock, controlTrackBlock, gatedTrackBlock, detectorTrackBlock,
        couplerTrackBlock, embarkingTrackBlock, disembarkingTrackBlock, turnoutTrackBlock,
        wyeTrackBlock, junctionTrackBlock, launcherTrackBlock, oneWayTrackBlock,
        locomotiveTrackBlock);
  }

  private void createOutfittedTracks(Block block, Block lockingTrackBlock,
      Block bufferStopTrackBlock, Block activatorTrackBlock, Block boosterTrackBlock,
      Block controlTrackBlock, Block gatedTrackBlock, Block detectorTrackBlock,
      Block couplerTrackBlock, Block embarkingTrackBlock, Block disembarkingTrackBlock,
      Block turnoutTrackBlock, Block wyeTrackBlock, Block junctionTrackBlock,
      Block launcherTrackBlock, Block oneWayTrackBlock, Block locomotiveTrackBlock) {
    var outfittedTrackModels = this.createOutfittedTrackModelSet(block);
  }

  private ResourceLocation createPassiveRail(String name) {
    return this.createVariant(name, ModelTemplates.RAIL_FLAT, TextureMapping::rail);
  }

  private ResourceLocation createActiveRail(String name) {
    return this.createVariant(name + "_on", ModelTemplates.RAIL_FLAT, TextureMapping::rail);
  }

  private StraightTrackModelSet createOutfittedTrackModelSet(Block block) {
    return this.createTrackModelSet(ForgeRegistries.BLOCKS.getKey(block).getPath() + "_outfitted");
  }

  private StraightTrackModelSet createTrackModelSet(String name) {
    return new StraightTrackModelSet(this.createPassiveRail(name),
        this.createVariant(name, ModelTemplates.RAIL_RAISED_NE, TextureMapping::rail),
        this.createVariant(name, ModelTemplates.RAIL_RAISED_SW, TextureMapping::rail));
  }

  private StraightTrackModelSet createActiveTrackModelSet(String name) {
    return new StraightTrackModelSet(this.createActiveRail(name),
        this.createVariant(name + "_on",
            ModelTemplates.RAIL_RAISED_NE, TextureMapping::rail),
        this.createVariant(name + "_on",
            ModelTemplates.RAIL_RAISED_SW, TextureMapping::rail));
  }

  private record StraightTrackModelSet(
      ResourceLocation flatModel,
      ResourceLocation raisedNorthEastModel,
      ResourceLocation raisedSouthWestModel) {

    private void apply(MultiPartGenerator generator, Property<RailShape> shapeProperty,
        boolean includeRaised, boolean reversed) {
      this.apply(generator, shapeProperty, includeRaised, reversed, null);
    }

    private static <T extends Comparable<T>> Condition andWith(Property<T> property, T value,
        @Nullable Condition optionalCondition) {
      return and(Condition.condition().term(property, value), optionalCondition);
    }

    private static Condition and(Condition condition, @Nullable Condition optionalCondition) {
      return optionalCondition == null
          ? condition
          : Condition.and(condition, optionalCondition);
    }

    private void apply(MultiPartGenerator generator, Property<RailShape> shapeProperty,
        boolean includeRaised, boolean reversed,
        @Nullable Condition condition) {
      generator
          .with(
              andWith(shapeProperty, RailShape.NORTH_SOUTH, condition),
              Variant.variant()
                  .with(VariantProperties.MODEL, this.flatModel)
                  .with(VariantProperties.Y_ROT,
                      reversed ? VariantProperties.Rotation.R180 : VariantProperties.Rotation.R0))
          .with(
              andWith(shapeProperty, RailShape.EAST_WEST, condition),
              Variant.variant()
                  .with(VariantProperties.MODEL, this.flatModel)
                  .with(VariantProperties.Y_ROT,
                      reversed ? VariantProperties.Rotation.R270 : VariantProperties.Rotation.R90));

      if (includeRaised) {
        generator.with(
            andWith(shapeProperty, RailShape.ASCENDING_NORTH, condition),
            Variant.variant()
                .with(VariantProperties.MODEL,
                    reversed ? this.raisedSouthWestModel : this.raisedNorthEastModel)
                .with(VariantProperties.Y_ROT,
                    reversed ? VariantProperties.Rotation.R180 : VariantProperties.Rotation.R0))
            .with(
                andWith(shapeProperty, RailShape.ASCENDING_SOUTH, condition),
                Variant.variant()
                    .with(VariantProperties.MODEL,
                        reversed ? this.raisedNorthEastModel : this.raisedSouthWestModel)
                    .with(VariantProperties.Y_ROT,
                        reversed ? VariantProperties.Rotation.R180 : VariantProperties.Rotation.R0))
            .with(
                andWith(shapeProperty, RailShape.ASCENDING_EAST, condition),
                Variant.variant()
                    .with(VariantProperties.MODEL,
                        reversed ? this.raisedSouthWestModel : this.raisedNorthEastModel)
                    .with(VariantProperties.Y_ROT,
                        reversed ? VariantProperties.Rotation.R270
                            : VariantProperties.Rotation.R90))
            .with(
                andWith(shapeProperty, RailShape.ASCENDING_WEST, condition),
                Variant.variant()
                    .with(VariantProperties.MODEL,
                        reversed ? this.raisedNorthEastModel : this.raisedSouthWestModel)
                    .with(VariantProperties.Y_ROT,
                        reversed ? VariantProperties.Rotation.R270
                            : VariantProperties.Rotation.R90));
      }
    }
  }
}
