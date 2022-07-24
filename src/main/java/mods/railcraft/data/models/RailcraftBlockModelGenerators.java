package mods.railcraft.data.models;

import java.util.EnumMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import com.google.gson.JsonElement;
import mods.railcraft.Railcraft;
import mods.railcraft.world.level.block.AbstractStrengthenedGlassBlock;
import mods.railcraft.world.level.block.FurnaceMultiblockBlock;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.SteamTurbineBlock;
import mods.railcraft.world.level.block.entity.track.CouplerTrackBlockEntity;
import mods.railcraft.world.level.block.manipulator.AdvancedItemLoaderBlock;
import mods.railcraft.world.level.block.post.Column;
import mods.railcraft.world.level.block.post.Connection;
import mods.railcraft.world.level.block.post.PostBlock;
import mods.railcraft.world.level.block.steamboiler.FireboxBlock;
import mods.railcraft.world.level.block.steamboiler.SteamBoilerTankBlock;
import mods.railcraft.world.level.block.track.AbandonedTrackBlock;
import mods.railcraft.world.level.block.track.ElevatorTrackBlock;
import mods.railcraft.world.level.block.track.ForceTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.ControlTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.CouplerTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.DetectorTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.DisembarkingTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.GatedTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.LockingMode;
import mods.railcraft.world.level.block.track.outfitted.LockingTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.OutfittedTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.PoweredOutfittedTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.ReversibleOutfittedTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.SwitchTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.TurnoutTrackBlock;
import net.minecraft.core.Direction;
import net.minecraft.data.models.blockstates.BlockStateGenerator;
import net.minecraft.data.models.blockstates.Condition;
import net.minecraft.data.models.blockstates.MultiPartGenerator;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.blockstates.VariantProperties.Rotation;
import net.minecraft.data.models.model.DelegatedModel;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.data.models.model.TexturedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraftforge.registries.ForgeRegistries;

public class RailcraftBlockModelGenerators {

  private final Consumer<BlockStateGenerator> blockStateOutput;
  private final BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput;
  private final Consumer<Item> skippedAutoModelsOutput;

  private final ResourceLocation lockingTrackLockdownModel;
  private final ResourceLocation lockingTrackTrainLockdownModel;
  private final ResourceLocation lockingTrackHoldingModel;
  private final ResourceLocation lockingTrackTrainHoldingModel;
  private final ResourceLocation lockingTrackBoardingModel;
  private final ResourceLocation lockingTrackBoardingReversedModel;
  private final ResourceLocation lockingTrackTrainBoardingModel;
  private final ResourceLocation lockingTrackTrainBoardingReversedModel;
  private final ResourceLocation lockingTrackActiveLockdownModel;
  private final ResourceLocation lockingTrackActiveTrainLockdownModel;
  private final ResourceLocation lockingTrackActiveHoldingModel;
  private final ResourceLocation lockingTrackActiveTrainHoldingModel;
  private final ResourceLocation lockingTrackActiveBoardingModel;
  private final ResourceLocation lockingTrackActiveBoardingReversedModel;
  private final ResourceLocation lockingTrackActiveTrainBoardingModel;
  private final ResourceLocation lockingTrackActiveTrainBoardingReversedModel;

  private final StraightTrackModelSet transitionTrackModels;
  private final StraightTrackModelSet activeTransitionTrackModels;
  private final StraightTrackModelSet activatorTrackModels;
  private final StraightTrackModelSet activeActivatorTrackModels;
  private final StraightTrackModelSet boosterTrackModels;
  private final StraightTrackModelSet activeBoosterTrackModels;
  private final StraightTrackModelSet controlTrackModels;
  private final StraightTrackModelSet detectorTrackModels;
  private final StraightTrackModelSet activeDetectorTrackModels;
  private final StraightTrackModelSet travelDetectorTrackModels;
  private final StraightTrackModelSet activeTravelDetectorTrackModels;

  private final StraightTrackModelSet couplerTrackCoupler;
  private final StraightTrackModelSet activeCouplerTrackCoupler;
  private final StraightTrackModelSet couplerTrackDecoupler;
  private final StraightTrackModelSet activeCouplerTrackDecoupler;
  private final StraightTrackModelSet couplerTrackAutoCoupler;
  private final StraightTrackModelSet activeCouplerTrackAutoCoupler;

  private final StraightTrackModelSet embarkingTrack;
  private final StraightTrackModelSet activeEmbarkingTrack;

  private final StraightTrackModelSet disembarkingTrackLeft;
  private final StraightTrackModelSet activeDisembarkingTrackLeft;
  private final StraightTrackModelSet disembarkingTrackRight;
  private final StraightTrackModelSet activeDisembarkingTrackRight;

  private final StraightTrackModelSet launcherTrackModels;
  private final StraightTrackModelSet activeLauncherTrackModels;

  public RailcraftBlockModelGenerators(Consumer<BlockStateGenerator> blockStateOutput,
      BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput,
      Consumer<Item> skippedAutoModelsOutput) {
    this.blockStateOutput = blockStateOutput;
    this.modelOutput = modelOutput;
    this.skippedAutoModelsOutput = skippedAutoModelsOutput;

    this.lockingTrackLockdownModel =
        this.createPassiveRail("locking_track_lockdown");
    this.lockingTrackTrainLockdownModel =
        this.createPassiveRail("locking_track_train_lockdown");
    this.lockingTrackHoldingModel =
        this.createPassiveRail("locking_track_holding");
    this.lockingTrackTrainHoldingModel =
        this.createPassiveRail("locking_track_train_holding");
    this.lockingTrackBoardingModel =
        this.createPassiveRail("locking_track_boarding");
    this.lockingTrackBoardingReversedModel =
        this.createPassiveRail("locking_track_boarding_reversed");
    this.lockingTrackTrainBoardingModel =
        this.createPassiveRail("locking_track_train_boarding");
    this.lockingTrackTrainBoardingReversedModel =
        this.createPassiveRail("locking_track_train_boarding_reversed");
    this.lockingTrackActiveLockdownModel =
        this.createActiveRail("locking_track_lockdown");
    this.lockingTrackActiveTrainLockdownModel =
        this.createActiveRail("locking_track_train_lockdown");
    this.lockingTrackActiveHoldingModel =
        this.createActiveRail("locking_track_holding");
    this.lockingTrackActiveTrainHoldingModel =
        this.createActiveRail("locking_track_train_holding");
    this.lockingTrackActiveBoardingModel =
        this.createActiveRail("locking_track_boarding");
    this.lockingTrackActiveBoardingReversedModel =
        this.createActiveRail("locking_track_boarding_reversed");
    this.lockingTrackActiveTrainBoardingModel =
        this.createActiveRail("locking_track_train_boarding");
    this.lockingTrackActiveTrainBoardingReversedModel =
        this.createActiveRail("locking_track_train_boarding_reversed");

    this.transitionTrackModels = this.createTrackModelSet("transition_track");
    this.activeTransitionTrackModels = this.createActiveTrackModelSet("transition_track");

    this.activatorTrackModels = this.createTrackModelSet("activator_track");
    this.activeActivatorTrackModels = this.createActiveTrackModelSet("activator_track");

    this.boosterTrackModels = this.createTrackModelSet("booster_track");
    this.activeBoosterTrackModels = this.createActiveTrackModelSet("booster_track");

    this.controlTrackModels = this.createTrackModelSet("control_track");

    this.detectorTrackModels = this.createTrackModelSet("detector_track");
    this.activeDetectorTrackModels = this.createActiveTrackModelSet("detector_track");
    this.travelDetectorTrackModels = this.createTrackModelSet("detector_track_travel");
    this.activeTravelDetectorTrackModels = this.createActiveTrackModelSet("detector_track_travel");

    this.couplerTrackCoupler = this.createTrackModelSet("coupler_track_coupler");
    this.activeCouplerTrackCoupler = this.createActiveTrackModelSet("coupler_track_coupler");
    this.couplerTrackDecoupler = this.createTrackModelSet("coupler_track_decoupler");
    this.activeCouplerTrackDecoupler = this.createActiveTrackModelSet("coupler_track_decoupler");
    this.couplerTrackAutoCoupler = this.createTrackModelSet("coupler_track_auto_coupler");
    this.activeCouplerTrackAutoCoupler =
        this.createActiveTrackModelSet("coupler_track_auto_coupler");

    this.embarkingTrack = this.createTrackModelSet("embarking_track");
    this.activeEmbarkingTrack = this.createActiveTrackModelSet("embarking_track");

    this.disembarkingTrackLeft = this.createTrackModelSet("disembarking_track_left");
    this.activeDisembarkingTrackLeft = this.createActiveTrackModelSet("disembarking_track_left");
    this.disembarkingTrackRight = this.createTrackModelSet("disembarking_track_right");
    this.activeDisembarkingTrackRight = this.createActiveTrackModelSet("disembarking_track_right");

    this.launcherTrackModels = this.createTrackModelSet("launcher_track");
    this.activeLauncherTrackModels = this.createActiveTrackModelSet("launcher_track");
  }

  public void run() {
    this.skipAutoItemBlock(RailcraftBlocks.FORCE_TRACK_EMITTER.get());
    this.skipAutoItemBlock(RailcraftBlocks.SIGNAL_CAPACITOR_BOX.get());
    this.skipAutoItemBlock(RailcraftBlocks.SIGNAL_CONTROLLER_BOX.get());
    this.skipAutoItemBlock(RailcraftBlocks.SIGNAL_INTERLOCK_BOX.get());
    this.skipAutoItemBlock(RailcraftBlocks.SIGNAL_RECEIVER_BOX.get());
    this.skipAutoItemBlock(RailcraftBlocks.SIGNAL_SEQUENCER_BOX.get());
    this.skipAutoItemBlock(RailcraftBlocks.ANALOG_SIGNAL_CONTROLLER_BOX.get());
    this.skipAutoItemBlock(RailcraftBlocks.BLOCK_SIGNAL_RELAY_BOX.get());
    this.skipAutoItemBlock(RailcraftBlocks.BLOCK_SIGNAL.get());
    this.skipAutoItemBlock(RailcraftBlocks.DISTANT_SIGNAL.get());
    this.skipAutoItemBlock(RailcraftBlocks.TOKEN_SIGNAL.get());
    this.skipAutoItemBlock(RailcraftBlocks.DUAL_BLOCK_SIGNAL.get());
    this.skipAutoItemBlock(RailcraftBlocks.DUAL_DISTANT_SIGNAL.get());
    this.skipAutoItemBlock(RailcraftBlocks.DUAL_TOKEN_SIGNAL.get());
    this.skipAutoItemBlock(RailcraftBlocks.SWITCH_TRACK_LEVER.get());
    this.skipAutoItemBlock(RailcraftBlocks.SWITCH_TRACK_MOTOR.get());

    this.createSteamBoilerTank(RailcraftBlocks.LOW_PRESSURE_STEAM_BOILER_TANK.get());
    this.createSteamBoilerTank(RailcraftBlocks.HIGH_PRESSURE_STEAM_BOILER_TANK.get());

    this.createFirebox(RailcraftBlocks.SOLID_FUELED_FIREBOX.get());
    this.createFirebox(RailcraftBlocks.FLUID_FUELED_FIREBOX.get());

    this.createSteamTurbine(RailcraftBlocks.STEAM_TURBINE.get());

    this.createTrivialBlock(RailcraftBlocks.MANUAL_ROLLING_MACHINE.get(),
        TexturedModel.CUBE_TOP_BOTTOM);

    this.createTrivialBlock(RailcraftBlocks.CREOSOTE.get());

    this.createTrivialCube(RailcraftBlocks.STEEL_BLOCK.get());

    this.createSteelAnvil(RailcraftBlocks.STEEL_ANVIL.get());
    this.createSteelAnvil(RailcraftBlocks.CHIPPED_STEEL_ANVIL.get());
    this.createSteelAnvil(RailcraftBlocks.DAMAGED_STEEL_ANVIL.get());

    this.createFurnaceMultiblockBricks(RailcraftBlocks.COKE_OVEN_BRICKS.get());
    this.createFurnaceMultiblockBricks(RailcraftBlocks.BLAST_FURNACE_BRICKS.get());
    this.createFeedStation();

    this.createStrengthenedGlass(RailcraftBlocks.WHITE_STRENGTHENED_GLASS.get());
    this.createStrengthenedGlass(RailcraftBlocks.ORANGE_STRENGTHENED_GLASS.get());
    this.createStrengthenedGlass(RailcraftBlocks.MAGENTA_STRENGTHENED_GLASS.get());
    this.createStrengthenedGlass(RailcraftBlocks.LIGHT_BLUE_STRENGTHENED_GLASS.get());
    this.createStrengthenedGlass(RailcraftBlocks.YELLOW_STRENGTHENED_GLASS.get());
    this.createStrengthenedGlass(RailcraftBlocks.LIME_STRENGTHENED_GLASS.get());
    this.createStrengthenedGlass(RailcraftBlocks.PINK_STRENGTHENED_GLASS.get());
    this.createStrengthenedGlass(RailcraftBlocks.GRAY_STRENGTHENED_GLASS.get());
    this.createStrengthenedGlass(RailcraftBlocks.LIGHT_GRAY_STRENGTHENED_GLASS.get());
    this.createStrengthenedGlass(RailcraftBlocks.CYAN_STRENGTHENED_GLASS.get());
    this.createStrengthenedGlass(RailcraftBlocks.PURPLE_STRENGTHENED_GLASS.get());
    this.createStrengthenedGlass(RailcraftBlocks.BLUE_STRENGTHENED_GLASS.get());
    this.createStrengthenedGlass(RailcraftBlocks.BROWN_STRENGTHENED_GLASS.get());
    this.createStrengthenedGlass(RailcraftBlocks.GREEN_STRENGTHENED_GLASS.get());
    this.createStrengthenedGlass(RailcraftBlocks.RED_STRENGTHENED_GLASS.get());
    this.createStrengthenedGlass(RailcraftBlocks.BLACK_STRENGTHENED_GLASS.get());

    this.createStrengthenedGlass(RailcraftBlocks.WHITE_IRON_TANK_GAUGE.get());
    this.createStrengthenedGlass(RailcraftBlocks.ORANGE_IRON_TANK_GAUGE.get());
    this.createStrengthenedGlass(RailcraftBlocks.MAGENTA_IRON_TANK_GAUGE.get());
    this.createStrengthenedGlass(RailcraftBlocks.LIGHT_BLUE_IRON_TANK_GAUGE.get());
    this.createStrengthenedGlass(RailcraftBlocks.YELLOW_IRON_TANK_GAUGE.get());
    this.createStrengthenedGlass(RailcraftBlocks.LIME_IRON_TANK_GAUGE.get());
    this.createStrengthenedGlass(RailcraftBlocks.PINK_IRON_TANK_GAUGE.get());
    this.createStrengthenedGlass(RailcraftBlocks.GRAY_IRON_TANK_GAUGE.get());
    this.createStrengthenedGlass(RailcraftBlocks.LIGHT_GRAY_IRON_TANK_GAUGE.get());
    this.createStrengthenedGlass(RailcraftBlocks.CYAN_IRON_TANK_GAUGE.get());
    this.createStrengthenedGlass(RailcraftBlocks.PURPLE_IRON_TANK_GAUGE.get());
    this.createStrengthenedGlass(RailcraftBlocks.BLUE_IRON_TANK_GAUGE.get());
    this.createStrengthenedGlass(RailcraftBlocks.BROWN_IRON_TANK_GAUGE.get());
    this.createStrengthenedGlass(RailcraftBlocks.GREEN_IRON_TANK_GAUGE.get());
    this.createStrengthenedGlass(RailcraftBlocks.RED_IRON_TANK_GAUGE.get());
    this.createStrengthenedGlass(RailcraftBlocks.BLACK_IRON_TANK_GAUGE.get());

    this.createTankValve(RailcraftBlocks.WHITE_IRON_TANK_VALVE.get(),
        RailcraftBlocks.WHITE_IRON_TANK_WALL.get());
    this.createTankValve(RailcraftBlocks.ORANGE_IRON_TANK_VALVE.get(),
        RailcraftBlocks.ORANGE_IRON_TANK_WALL.get());
    this.createTankValve(RailcraftBlocks.MAGENTA_IRON_TANK_VALVE.get(),
        RailcraftBlocks.MAGENTA_IRON_TANK_WALL.get());
    this.createTankValve(RailcraftBlocks.LIGHT_BLUE_IRON_TANK_VALVE.get(),
        RailcraftBlocks.LIGHT_BLUE_IRON_TANK_WALL.get());
    this.createTankValve(RailcraftBlocks.YELLOW_IRON_TANK_VALVE.get(),
        RailcraftBlocks.YELLOW_IRON_TANK_WALL.get());
    this.createTankValve(RailcraftBlocks.LIME_IRON_TANK_VALVE.get(),
        RailcraftBlocks.LIME_IRON_TANK_WALL.get());
    this.createTankValve(RailcraftBlocks.PINK_IRON_TANK_VALVE.get(),
        RailcraftBlocks.PINK_IRON_TANK_WALL.get());
    this.createTankValve(RailcraftBlocks.GRAY_IRON_TANK_VALVE.get(),
        RailcraftBlocks.GRAY_IRON_TANK_WALL.get());
    this.createTankValve(RailcraftBlocks.LIGHT_GRAY_IRON_TANK_VALVE.get(),
        RailcraftBlocks.LIGHT_GRAY_IRON_TANK_WALL.get());
    this.createTankValve(RailcraftBlocks.CYAN_IRON_TANK_VALVE.get(),
        RailcraftBlocks.CYAN_IRON_TANK_WALL.get());
    this.createTankValve(RailcraftBlocks.PURPLE_IRON_TANK_VALVE.get(),
        RailcraftBlocks.PURPLE_IRON_TANK_WALL.get());
    this.createTankValve(RailcraftBlocks.BLUE_IRON_TANK_VALVE.get(),
        RailcraftBlocks.BLUE_IRON_TANK_WALL.get());
    this.createTankValve(RailcraftBlocks.BROWN_IRON_TANK_VALVE.get(),
        RailcraftBlocks.BROWN_IRON_TANK_WALL.get());
    this.createTankValve(RailcraftBlocks.GREEN_IRON_TANK_VALVE.get(),
        RailcraftBlocks.GREEN_IRON_TANK_WALL.get());
    this.createTankValve(RailcraftBlocks.RED_IRON_TANK_VALVE.get(),
        RailcraftBlocks.RED_IRON_TANK_WALL.get());
    this.createTankValve(RailcraftBlocks.BLACK_IRON_TANK_VALVE.get(),
        RailcraftBlocks.BLACK_IRON_TANK_WALL.get());

    this.createTankWall(RailcraftBlocks.WHITE_IRON_TANK_WALL.get());
    this.createTankWall(RailcraftBlocks.ORANGE_IRON_TANK_WALL.get());
    this.createTankWall(RailcraftBlocks.MAGENTA_IRON_TANK_WALL.get());
    this.createTankWall(RailcraftBlocks.LIGHT_BLUE_IRON_TANK_WALL.get());
    this.createTankWall(RailcraftBlocks.YELLOW_IRON_TANK_WALL.get());
    this.createTankWall(RailcraftBlocks.LIME_IRON_TANK_WALL.get());
    this.createTankWall(RailcraftBlocks.PINK_IRON_TANK_WALL.get());
    this.createTankWall(RailcraftBlocks.GRAY_IRON_TANK_WALL.get());
    this.createTankWall(RailcraftBlocks.LIGHT_GRAY_IRON_TANK_WALL.get());
    this.createTankWall(RailcraftBlocks.CYAN_IRON_TANK_WALL.get());
    this.createTankWall(RailcraftBlocks.PURPLE_IRON_TANK_WALL.get());
    this.createTankWall(RailcraftBlocks.BLUE_IRON_TANK_WALL.get());
    this.createTankWall(RailcraftBlocks.BROWN_IRON_TANK_WALL.get());
    this.createTankWall(RailcraftBlocks.GREEN_IRON_TANK_WALL.get());
    this.createTankWall(RailcraftBlocks.RED_IRON_TANK_WALL.get());
    this.createTankWall(RailcraftBlocks.BLACK_IRON_TANK_WALL.get());

    this.createStrengthenedGlass(RailcraftBlocks.WHITE_STEEL_TANK_GAUGE.get());
    this.createStrengthenedGlass(RailcraftBlocks.ORANGE_STEEL_TANK_GAUGE.get());
    this.createStrengthenedGlass(RailcraftBlocks.MAGENTA_STEEL_TANK_GAUGE.get());
    this.createStrengthenedGlass(RailcraftBlocks.LIGHT_BLUE_STEEL_TANK_GAUGE.get());
    this.createStrengthenedGlass(RailcraftBlocks.YELLOW_STEEL_TANK_GAUGE.get());
    this.createStrengthenedGlass(RailcraftBlocks.LIME_STEEL_TANK_GAUGE.get());
    this.createStrengthenedGlass(RailcraftBlocks.PINK_STEEL_TANK_GAUGE.get());
    this.createStrengthenedGlass(RailcraftBlocks.GRAY_STEEL_TANK_GAUGE.get());
    this.createStrengthenedGlass(RailcraftBlocks.LIGHT_GRAY_STEEL_TANK_GAUGE.get());
    this.createStrengthenedGlass(RailcraftBlocks.CYAN_STEEL_TANK_GAUGE.get());
    this.createStrengthenedGlass(RailcraftBlocks.PURPLE_STEEL_TANK_GAUGE.get());
    this.createStrengthenedGlass(RailcraftBlocks.BLUE_STEEL_TANK_GAUGE.get());
    this.createStrengthenedGlass(RailcraftBlocks.BROWN_STEEL_TANK_GAUGE.get());
    this.createStrengthenedGlass(RailcraftBlocks.GREEN_STEEL_TANK_GAUGE.get());
    this.createStrengthenedGlass(RailcraftBlocks.RED_STEEL_TANK_GAUGE.get());
    this.createStrengthenedGlass(RailcraftBlocks.BLACK_STEEL_TANK_GAUGE.get());

    this.createTankValve(RailcraftBlocks.WHITE_STEEL_TANK_VALVE.get(),
        RailcraftBlocks.WHITE_STEEL_TANK_WALL.get());
    this.createTankValve(RailcraftBlocks.ORANGE_STEEL_TANK_VALVE.get(),
        RailcraftBlocks.ORANGE_STEEL_TANK_WALL.get());
    this.createTankValve(RailcraftBlocks.MAGENTA_STEEL_TANK_VALVE.get(),
        RailcraftBlocks.MAGENTA_STEEL_TANK_WALL.get());
    this.createTankValve(RailcraftBlocks.LIGHT_BLUE_STEEL_TANK_VALVE.get(),
        RailcraftBlocks.LIGHT_BLUE_STEEL_TANK_WALL.get());
    this.createTankValve(RailcraftBlocks.YELLOW_STEEL_TANK_VALVE.get(),
        RailcraftBlocks.YELLOW_STEEL_TANK_WALL.get());
    this.createTankValve(RailcraftBlocks.LIME_STEEL_TANK_VALVE.get(),
        RailcraftBlocks.LIME_STEEL_TANK_WALL.get());
    this.createTankValve(RailcraftBlocks.PINK_STEEL_TANK_VALVE.get(),
        RailcraftBlocks.PINK_STEEL_TANK_WALL.get());
    this.createTankValve(RailcraftBlocks.GRAY_STEEL_TANK_VALVE.get(),
        RailcraftBlocks.GRAY_STEEL_TANK_WALL.get());
    this.createTankValve(RailcraftBlocks.LIGHT_GRAY_STEEL_TANK_VALVE.get(),
        RailcraftBlocks.LIGHT_GRAY_STEEL_TANK_WALL.get());
    this.createTankValve(RailcraftBlocks.CYAN_STEEL_TANK_VALVE.get(),
        RailcraftBlocks.CYAN_STEEL_TANK_WALL.get());
    this.createTankValve(RailcraftBlocks.PURPLE_STEEL_TANK_VALVE.get(),
        RailcraftBlocks.PURPLE_STEEL_TANK_WALL.get());
    this.createTankValve(RailcraftBlocks.BLUE_STEEL_TANK_VALVE.get(),
        RailcraftBlocks.BLUE_STEEL_TANK_WALL.get());
    this.createTankValve(RailcraftBlocks.BROWN_STEEL_TANK_VALVE.get(),
        RailcraftBlocks.BROWN_STEEL_TANK_WALL.get());
    this.createTankValve(RailcraftBlocks.GREEN_STEEL_TANK_VALVE.get(),
        RailcraftBlocks.GREEN_STEEL_TANK_WALL.get());
    this.createTankValve(RailcraftBlocks.RED_STEEL_TANK_VALVE.get(),
        RailcraftBlocks.RED_STEEL_TANK_WALL.get());
    this.createTankValve(RailcraftBlocks.BLACK_STEEL_TANK_VALVE.get(),
        RailcraftBlocks.BLACK_STEEL_TANK_WALL.get());

    this.createTankWall(RailcraftBlocks.WHITE_STEEL_TANK_WALL.get());
    this.createTankWall(RailcraftBlocks.ORANGE_STEEL_TANK_WALL.get());
    this.createTankWall(RailcraftBlocks.MAGENTA_STEEL_TANK_WALL.get());
    this.createTankWall(RailcraftBlocks.LIGHT_BLUE_STEEL_TANK_WALL.get());
    this.createTankWall(RailcraftBlocks.YELLOW_STEEL_TANK_WALL.get());
    this.createTankWall(RailcraftBlocks.LIME_STEEL_TANK_WALL.get());
    this.createTankWall(RailcraftBlocks.PINK_STEEL_TANK_WALL.get());
    this.createTankWall(RailcraftBlocks.GRAY_STEEL_TANK_WALL.get());
    this.createTankWall(RailcraftBlocks.LIGHT_GRAY_STEEL_TANK_WALL.get());
    this.createTankWall(RailcraftBlocks.CYAN_STEEL_TANK_WALL.get());
    this.createTankWall(RailcraftBlocks.PURPLE_STEEL_TANK_WALL.get());
    this.createTankWall(RailcraftBlocks.BLUE_STEEL_TANK_WALL.get());
    this.createTankWall(RailcraftBlocks.BROWN_STEEL_TANK_WALL.get());
    this.createTankWall(RailcraftBlocks.GREEN_STEEL_TANK_WALL.get());
    this.createTankWall(RailcraftBlocks.RED_STEEL_TANK_WALL.get());
    this.createTankWall(RailcraftBlocks.BLACK_STEEL_TANK_WALL.get());

    this.createPost(RailcraftBlocks.BLACK_POST.get());
    this.createPost(RailcraftBlocks.RED_POST.get());
    this.createPost(RailcraftBlocks.GREEN_POST.get());
    this.createPost(RailcraftBlocks.BROWN_POST.get());
    this.createPost(RailcraftBlocks.BLUE_POST.get());
    this.createPost(RailcraftBlocks.PURPLE_POST.get());
    this.createPost(RailcraftBlocks.CYAN_POST.get());
    this.createPost(RailcraftBlocks.LIGHT_GRAY_POST.get());
    this.createPost(RailcraftBlocks.GRAY_POST.get());
    this.createPost(RailcraftBlocks.PINK_POST.get());
    this.createPost(RailcraftBlocks.LIME_POST.get());
    this.createPost(RailcraftBlocks.YELLOW_POST.get());
    this.createPost(RailcraftBlocks.LIGHT_BLUE_POST.get());
    this.createPost(RailcraftBlocks.MAGENTA_POST.get());
    this.createPost(RailcraftBlocks.ORANGE_POST.get());
    this.createPost(RailcraftBlocks.WHITE_POST.get());

    this.createFluidManipulator(RailcraftBlocks.FLUID_LOADER.get());
    this.createFluidManipulator(RailcraftBlocks.FLUID_UNLOADER.get());
    this.createManipulator(RailcraftBlocks.ITEM_LOADER.get());
    this.createManipulator(RailcraftBlocks.ITEM_UNLOADER.get());
    this.createDirectionalManipulator(RailcraftBlocks.ADVANCED_ITEM_LOADER.get(),
        AdvancedItemLoaderBlock.FACING);
    this.createDirectionalManipulator(RailcraftBlocks.ADVANCED_ITEM_UNLOADER.get(),
        AdvancedItemLoaderBlock.FACING);

    this.createElevatorTrack(RailcraftBlocks.ELEVATOR_TRACK.get());

    this.createForceTrack(RailcraftBlocks.FORCE_TRACK.get());

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
        RailcraftBlocks.ABANDONED_LAUNCHER_TRACK.get());
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
        RailcraftBlocks.ELECTRIC_LAUNCHER_TRACK.get());
    this.createHighSpeedTracks(
        RailcraftBlocks.HIGH_SPEED_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_TRANSITION_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_LOCKING_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ACTIVATOR_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_BOOSTER_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_DETECTOR_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_TURNOUT_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_WYE_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_JUNCTION_TRACK.get());
    this.createHighSpeedTracks(
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRANSITION_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCKING_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_BOOSTER_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_DETECTOR_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_TURNOUT_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_WYE_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_JUNCTION_TRACK.get());
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
        RailcraftBlocks.IRON_LAUNCHER_TRACK.get());
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
        RailcraftBlocks.REINFORCED_LAUNCHER_TRACK.get());
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
        RailcraftBlocks.STRAP_IRON_LAUNCHER_TRACK.get());
  }

  private void skipAutoItemBlock(Block block) {
    this.skippedAutoModelsOutput.accept(block.asItem());
  }

  private void createTrivialCube(Block block) {
    this.createTrivialBlock(block, TexturedModel.CUBE);
  }

  private void createTrivialBlock(Block block, TexturedModel.Provider textureFactory) {
    this.blockStateOutput.accept(
        createSimpleBlock(block, textureFactory.create(block, this.modelOutput)));
  }

  private void createTrivialBlock(Block block) {
    this.createTrivialBlock(block, block);
  }

  private void createTrivialBlock(Block block, Block block2) {
    this.blockStateOutput.accept(createSimpleBlock(block,
        ModelLocationUtils.getModelLocation(block2)));
  }

  private static MultiVariantGenerator createSimpleBlock(Block block,
      ResourceLocation modelLocation) {
    return MultiVariantGenerator.multiVariant(block,
        Variant.variant().with(VariantProperties.MODEL, modelLocation));
  }

  private void delegateItemModel(Block block, ResourceLocation modelLocation) {
    this.modelOutput.accept(ModelLocationUtils.getModelLocation(block.asItem()),
        new DelegatedModel(modelLocation));
  }

  private void createSimpleFlatItemModel(Block block) {
    Item item = block.asItem();
    if (item != Items.AIR) {
      ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item),
          TextureMapping.layer0(block), this.modelOutput);
    }
  }

  private void createSimpleFlatItemModel(Item item) {
    ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item),
        TextureMapping.layer0(item), this.modelOutput);
  }

  private void createSimpleFlatItemModel(Block block, String textureSuffix) {
    Item item = block.asItem();
    ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item),
        TextureMapping.layer0(TextureMapping.getBlockTexture(block, textureSuffix)),
        this.modelOutput);
  }

  private ResourceLocation createSuffixedVariant(Block block, String suffix,
      ModelTemplate model, Function<ResourceLocation, TextureMapping> textureFactory) {
    return model.createWithSuffix(block, suffix,
        textureFactory.apply(TextureMapping.getBlockTexture(block, suffix)),
        this.modelOutput);
  }

  private ResourceLocation createVariant(String name,
      ModelTemplate model, Function<ResourceLocation, TextureMapping> textureFactory) {
    return model.create(
        new ResourceLocation(Railcraft.ID, "block/" + name + model.suffix.orElse("")),
        textureFactory.apply(new ResourceLocation(Railcraft.ID, "block/" + name)),
        this.modelOutput);
  }

  private void createTankValve(Block block, Block wallBlock) {
    var verticalModel = ModelTemplates.CUBE.create(block,
        new TextureMapping()
            .put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(wallBlock, "_top"))
            .put(TextureSlot.NORTH, TextureMapping.getBlockTexture(wallBlock, "_side"))
            .put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(wallBlock, "_side"))
            .put(TextureSlot.EAST, TextureMapping.getBlockTexture(wallBlock, "_side"))
            .put(TextureSlot.WEST, TextureMapping.getBlockTexture(wallBlock, "_side"))
            .put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"))
            .put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_top")),
        this.modelOutput);
    var horizontalModel = ModelTemplates.CUBE.createWithSuffix(block, "_horizontal",
        new TextureMapping()
            .put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(wallBlock, "_top"))
            .put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front"))
            .put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_front"))
            .put(TextureSlot.EAST, TextureMapping.getBlockTexture(wallBlock, "_side"))
            .put(TextureSlot.WEST, TextureMapping.getBlockTexture(wallBlock, "_side"))
            .put(TextureSlot.UP, TextureMapping.getBlockTexture(wallBlock, "_top"))
            .put(TextureSlot.DOWN, TextureMapping.getBlockTexture(wallBlock, "_top")),
        this.modelOutput);
    this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
        .with(PropertyDispatch.property(BlockStateProperties.AXIS)
            .select(Direction.Axis.Y,
                Variant.variant().with(VariantProperties.MODEL, verticalModel))
            .select(Direction.Axis.Z,
                Variant.variant().with(VariantProperties.MODEL, horizontalModel))
            .select(Direction.Axis.X,
                Variant.variant().with(VariantProperties.MODEL, horizontalModel)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))));
  }

  private void createTankWall(Block block) {
    this.blockStateOutput.accept(
        createSimpleBlock(block, TexturedModel.COLUMN.create(block, this.modelOutput)));
  }

  private void createStrengthenedGlass(Block block) {
    var endTexture = TextureMapping.getBlockTexture(block, "_top");
    var singleModel =
        ModelTemplates.CUBE_ALL.createWithSuffix(block, "_single", TextureMapping.cube(endTexture),
            this.modelOutput);
    var topModel = ModelTemplates.CUBE_COLUMN.createWithSuffix(block, "_top", new TextureMapping()
        .put(TextureSlot.END, endTexture)
        .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side_top")),
        this.modelOutput);
    var centerModel = ModelTemplates.CUBE_COLUMN.createWithSuffix(block, "_center",
        new TextureMapping()
            .put(TextureSlot.END, endTexture)
            .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side_center")),
        this.modelOutput);
    var bottomModel = ModelTemplates.CUBE_COLUMN.createWithSuffix(block, "_bottom",
        new TextureMapping()
            .put(TextureSlot.END, endTexture)
            .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side_bottom")),
        this.modelOutput);
    this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
        .with(PropertyDispatch.property(AbstractStrengthenedGlassBlock.TYPE)
            .select(AbstractStrengthenedGlassBlock.Type.SINGLE,
                Variant.variant().with(VariantProperties.MODEL, singleModel))
            .select(AbstractStrengthenedGlassBlock.Type.TOP,
                Variant.variant().with(VariantProperties.MODEL, topModel))
            .select(AbstractStrengthenedGlassBlock.Type.CENTER,
                Variant.variant().with(VariantProperties.MODEL, centerModel))
            .select(AbstractStrengthenedGlassBlock.Type.BOTTOM,
                Variant.variant().with(VariantProperties.MODEL, bottomModel))));
    this.delegateItemModel(block, singleModel);
  }

  private void createSteelAnvil(Block block) {
    var model = RailcraftTexturedModel.STEEL_ANVIL.create(block, this.modelOutput);
    this.blockStateOutput.accept(
        createSimpleBlock(block, model).with(createHorizontalFacingDispatchAlt()));
  }

  private void createForceTrack(Block block) {
    var model = RailcraftModelTemplates.FORCE_TRACK.create(
        block, TextureMapping.rail(block), this.modelOutput);
    this.createSimpleFlatItemModel(block);
    this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
        .with(PropertyDispatch.property(ForceTrackBlock.SHAPE)
            .select(RailShape.NORTH_SOUTH,
                Variant.variant().with(VariantProperties.MODEL, model))
            .select(RailShape.EAST_WEST,
                Variant.variant().with(VariantProperties.MODEL, model)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))));
  }

  private void createFurnaceMultiblockBricks(Block block) {
    var bricksModel = TexturedModel.CUBE.create(block, this.modelOutput);
    var windowModel =
        ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(block, "_window",
            new TextureMapping()
                .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_window"))
                .put(TextureSlot.TOP, TextureMapping.getBlockTexture(block))
                .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(block)),
            this.modelOutput);
    var litWindowModel =
        ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(block, "_window_lit",
            new TextureMapping()
                .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_window_lit"))
                .put(TextureSlot.TOP, TextureMapping.getBlockTexture(block))
                .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(block)),
            this.modelOutput);

    this.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(block).with(
            PropertyDispatch.properties(FurnaceMultiblockBlock.LIT, FurnaceMultiblockBlock.WINDOW)
                .select(false, false, Variant.variant()
                    .with(VariantProperties.MODEL, bricksModel))
                .select(true, false, Variant.variant()
                    .with(VariantProperties.MODEL, bricksModel))
                .select(false, true, Variant.variant()
                    .with(VariantProperties.MODEL, windowModel))
                .select(true, true, Variant.variant()
                    .with(VariantProperties.MODEL, litWindowModel))));
  }

  private void createFeedStation() {
    var block = RailcraftBlocks.FEED_STATION.get();
    var mapping = TextureMapping.column(TextureMapping.getBlockTexture(block, "_side"),
        TextureMapping.getBlockTexture(block, "_top"));
    var model = ModelTemplates.CUBE_COLUMN.create(block, mapping, this.modelOutput);
    this.blockStateOutput.accept(createSimpleBlock(block, model));
  }

  private static PropertyDispatch createHorizontalFacingDispatchAlt() {
    return PropertyDispatch.property(BlockStateProperties.HORIZONTAL_FACING)
        .select(Direction.SOUTH, Variant.variant())
        .select(Direction.WEST,
            Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .select(Direction.NORTH,
            Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
        .select(Direction.EAST,
            Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270));
  }

  private void createElevatorTrack(Block block) {
    var model =
        RailcraftModelTemplates.ELEVATOR_TRACK.create(block, TextureMapping.defaultTexture(block),
            this.modelOutput);
    var activeModel =
        this.createSuffixedVariant(block, "_on", RailcraftModelTemplates.ELEVATOR_TRACK,
            TextureMapping::defaultTexture);

    this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
        .with(PropertyDispatch
            .properties(ElevatorTrackBlock.POWERED, ElevatorTrackBlock.FACING)
            .generate((powered, facing) -> {
              VariantProperties.Rotation yRot = VariantProperties.Rotation.R0;
              switch (facing) {
                case SOUTH:
                  yRot = VariantProperties.Rotation.R180;
                  break;
                case EAST:
                  yRot = VariantProperties.Rotation.R90;
                  break;
                case WEST:
                  yRot = VariantProperties.Rotation.R270;
                  break;
                default:
                  break;
              }
              return Variant.variant()
                  .with(VariantProperties.MODEL, powered ? activeModel : model)
                  .with(VariantProperties.Y_ROT, yRot);
            })));

    this.createSimpleFlatItemModel(block);
  }

  private void createFirebox(Block block) {
    var endTexture = TextureMapping.getBlockTexture(block, "_end");
    var model = ModelTemplates.CUBE_COLUMN.create(block,
        new TextureMapping()
            .put(TextureSlot.END, endTexture)
            .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side")),
        this.modelOutput);

    var litModel = ModelTemplates.CUBE_COLUMN.createWithSuffix(block, "_lit",
        new TextureMapping()
            .put(TextureSlot.END, endTexture)
            .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side_lit")),
        this.modelOutput);

    this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
        .with(PropertyDispatch.property(FireboxBlock.LIT)
            .select(false, Variant.variant()
                .with(VariantProperties.MODEL, model))
            .select(true, Variant.variant()
                .with(VariantProperties.MODEL, litModel))));
  }

  private void createSteamBoilerTank(Block block) {
    var textureMapping = new TextureMapping()
        .put(TextureSlot.END, TextureMapping.getBlockTexture(block, "_end"))
        .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side"));

    var model =
        RailcraftModelTemplates.STEAM_BOILER_TANK.create(block, textureMapping, this.modelOutput);

    var allModel = ModelTemplates.CUBE_COLUMN.createWithSuffix(block, "_all", textureMapping,
        this.modelOutput);

    var northEastModel =
        RailcraftModelTemplates.STEAM_BOILER_TANK_NE.create(block, textureMapping,
            this.modelOutput);
    var northEastWestModel =
        RailcraftModelTemplates.STEAM_BOILER_TANK_NEW.create(block, textureMapping,
            this.modelOutput);
    var northSouthEastModel =
        RailcraftModelTemplates.STEAM_BOILER_TANK_NSE.create(block, textureMapping,
            this.modelOutput);
    var northSouthWestModel =
        RailcraftModelTemplates.STEAM_BOILER_TANK_NSW.create(block, textureMapping,
            this.modelOutput);
    var northWestModel =
        RailcraftModelTemplates.STEAM_BOILER_TANK_NW.create(block, textureMapping,
            this.modelOutput);
    var southEastModel =
        RailcraftModelTemplates.STEAM_BOILER_TANK_SE.create(block, textureMapping,
            this.modelOutput);
    var southEastWestModel =
        RailcraftModelTemplates.STEAM_BOILER_TANK_SEW.create(block, textureMapping,
            this.modelOutput);
    var southWestModel =
        RailcraftModelTemplates.STEAM_BOILER_TANK_SW.create(block, textureMapping,
            this.modelOutput);

    this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
        .with(PropertyDispatch.property(SteamBoilerTankBlock.CONNECTION_TYPE)
            .select(SteamBoilerTankBlock.ConnectionType.NONE, Variant.variant()
                .with(VariantProperties.MODEL, model))
            .select(SteamBoilerTankBlock.ConnectionType.ALL, Variant.variant()
                .with(VariantProperties.MODEL, allModel))
            .select(SteamBoilerTankBlock.ConnectionType.NORTH_EAST, Variant.variant()
                .with(VariantProperties.MODEL, northEastModel))
            .select(SteamBoilerTankBlock.ConnectionType.SOUTH_EAST, Variant.variant()
                .with(VariantProperties.MODEL, southEastModel))
            .select(SteamBoilerTankBlock.ConnectionType.SOUTH_WEST, Variant.variant()
                .with(VariantProperties.MODEL, southWestModel))
            .select(SteamBoilerTankBlock.ConnectionType.NORTH_WEST, Variant.variant()
                .with(VariantProperties.MODEL, northWestModel))
            .select(SteamBoilerTankBlock.ConnectionType.NORTH_SOUTH_EAST, Variant.variant()
                .with(VariantProperties.MODEL, northSouthEastModel))
            .select(SteamBoilerTankBlock.ConnectionType.SOUTH_EAST_WEST, Variant.variant()
                .with(VariantProperties.MODEL, southEastWestModel))
            .select(SteamBoilerTankBlock.ConnectionType.NORTH_EAST_WEST, Variant.variant()
                .with(VariantProperties.MODEL, northEastWestModel))
            .select(SteamBoilerTankBlock.ConnectionType.NORTH_SOUTH_WEST, Variant.variant()
                .with(VariantProperties.MODEL, northSouthWestModel))));
  }

  private void createSteamTurbine(Block block) {
    var sideTexture = TextureMapping.getBlockTexture(block, "_side");
    this.delegateItemModel(block,
        this.createSteamTurbineModel(block, sideTexture, "_inventory", false));

    var noneVariant = Variant.variant()
        .with(VariantProperties.MODEL,
            ModelTemplates.CUBE_ALL.createWithSuffix(block, "_side",
                new TextureMapping().put(TextureSlot.ALL, sideTexture),
                this.modelOutput));

    var models =
        new EnumMap<SteamTurbineBlock.Type, ResourceLocation>(SteamTurbineBlock.Type.class);
    for (var type : SteamTurbineBlock.Type.values()) {
      if (type == SteamTurbineBlock.Type.NONE) {
        continue;
      }
      models.put(type,
          this.createSteamTurbineModel(block, sideTexture, "_" + type.getSerializedName(),
              type != SteamTurbineBlock.Type.WINDOW));
    }

    this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
        .with(PropertyDispatch.properties(SteamTurbineBlock.TYPE, SteamTurbineBlock.ROTATED)
            .generate((type, rotated) -> type == SteamTurbineBlock.Type.NONE
                ? noneVariant
                : Variant.variant()
                    .with(VariantProperties.MODEL, models.get(type))
                    .with(VariantProperties.Y_ROT, rotated
                        ? VariantProperties.Rotation.R90
                        : VariantProperties.Rotation.R0))));
  }

  private ResourceLocation createSteamTurbineModel(Block block, ResourceLocation sideTexture,
      String suffix, boolean rotated) {
    var frontTexture = TextureMapping.getBlockTexture(block, suffix);
    return RailcraftModelTemplates.MIRRORED_CUBE.createWithOverride(block, suffix,
        new TextureMapping()
            .put(TextureSlot.PARTICLE, sideTexture)
            .put(TextureSlot.NORTH, rotated ? sideTexture : frontTexture)
            .put(TextureSlot.SOUTH, rotated ? sideTexture : frontTexture)
            .put(TextureSlot.EAST, rotated ? frontTexture : sideTexture)
            .put(TextureSlot.WEST, rotated ? frontTexture : sideTexture)
            .put(TextureSlot.UP, sideTexture)
            .put(TextureSlot.DOWN, sideTexture),
        this.modelOutput);
  }

  private void createFluidManipulator(Block block) {
    this.createManipulator(block);
    var model =
        ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(block, "_inventory",
            new TextureMapping()
                .put(TextureSlot.SIDE,
                    new ResourceLocation(Railcraft.ID, "block/fluid_manipulator_side_inventory"))
                .put(TextureSlot.TOP, TextureMapping.getBlockTexture(block, "_top"))
                .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(block, "_bottom")),
            this.modelOutput);
    this.delegateItemModel(block, model);
  }

  private void createManipulator(Block block) {
    var model =
        ModelTemplates.CUBE_BOTTOM_TOP.create(block, TextureMapping.cubeBottomTop(block),
            this.modelOutput);
    this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block,
        Variant.variant()
            .with(VariantProperties.MODEL, model)));
  }

  private void createDirectionalManipulator(Block block, DirectionProperty facingProperty) {
    var horizontalModel =
        ModelTemplates.CUBE_ORIENTABLE.create(block, TextureMapping.orientableCubeOnlyTop(block),
            this.modelOutput);
    var upModel =
        ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(block, "_up",
            new TextureMapping()
                .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side"))
                .put(TextureSlot.TOP, TextureMapping.getBlockTexture(block, "_front"))
                .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(block, "_top")),
            this.modelOutput);
    var downModel =
        ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(block, "_down",
            new TextureMapping()
                .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side"))
                .put(TextureSlot.TOP, TextureMapping.getBlockTexture(block, "_top"))
                .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(block, "_front")),
            this.modelOutput);
    this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
        .with(PropertyDispatch.property(facingProperty)
            .generate(facing -> {
              VariantProperties.Rotation yRot = VariantProperties.Rotation.R0;
              VariantProperties.Rotation xRot = VariantProperties.Rotation.R0;
              switch (facing) {
                case SOUTH:
                  yRot = VariantProperties.Rotation.R180;
                  break;
                case EAST:
                  yRot = VariantProperties.Rotation.R90;
                  break;
                case WEST:
                  yRot = VariantProperties.Rotation.R270;
                  break;
                case UP:
                  return Variant.variant()
                      .with(VariantProperties.MODEL, upModel);
                case DOWN:
                  return Variant.variant()
                      .with(VariantProperties.MODEL, downModel);
                default:
                  break;
              }
              return Variant.variant()
                  .with(VariantProperties.MODEL, horizontalModel)
                  .with(VariantProperties.Y_ROT, yRot)
                  .with(VariantProperties.X_ROT, xRot);
            })));
  }

  private void createPost(Block block) {
    var textures = TextureMapping.defaultTexture(block);
    var fullColumnModel = RailcraftModelTemplates.POST_COLUMN.create(
        block, textures, this.modelOutput);
    var doubleConnectionModel = RailcraftModelTemplates.POST_DOUBLE_CONNECTION.create(
        block, textures, this.modelOutput);
    var topColumnModel = RailcraftModelTemplates.POST_TOP_COLUMN.create(
        block, textures, this.modelOutput);
    var middleColumnModel = RailcraftModelTemplates.POST_SMALL_COLUMN.create(
        block, textures, this.modelOutput);
    var platformModel = RailcraftModelTemplates.POST_PLATFORM.create(
        block, textures, this.modelOutput);
    var singleConnectionModel = RailcraftModelTemplates.POST_SINGLE_CONNECTION.create(
        block, textures, this.modelOutput);
    var inventoryModel =
        RailcraftModelTemplates.POST_INVENTORY.create(block, textures, this.modelOutput);
    this.delegateItemModel(block, inventoryModel);
    this.blockStateOutput.accept(
        MultiPartGenerator.multiPart(block)
            .with(
                Condition.condition()
                    .term(PostBlock.COLUMN, Column.PLATFORM),
                Variant.variant()
                    .with(VariantProperties.MODEL, platformModel))
            .with(
                Condition.condition()
                    .term(PostBlock.COLUMN, Column.TOP),
                Variant.variant()
                    .with(VariantProperties.MODEL, topColumnModel))
            .with(
                Condition.condition()
                    .term(PostBlock.COLUMN, Column.SMALL),
                Variant.variant()
                    .with(VariantProperties.MODEL, middleColumnModel))
            .with(
                Condition.condition()
                    .term(PostBlock.COLUMN, Column.FULL),
                Variant.variant()
                    .with(VariantProperties.MODEL, fullColumnModel))
            .with(
                Condition.condition()
                    .term(PostBlock.NORTH, Connection.SINGLE),
                Variant.variant()
                    .with(VariantProperties.MODEL, singleConnectionModel)
                    .with(VariantProperties.UV_LOCK, true))
            .with(
                Condition.condition()
                    .term(PostBlock.NORTH, Connection.DOUBLE),
                Variant.variant()
                    .with(VariantProperties.MODEL, doubleConnectionModel)
                    .with(VariantProperties.UV_LOCK, true))
            .with(
                Condition.condition()
                    .term(PostBlock.SOUTH, Connection.SINGLE),
                Variant.variant()
                    .with(VariantProperties.MODEL, singleConnectionModel)
                    .with(VariantProperties.Y_ROT, Rotation.R180)
                    .with(VariantProperties.UV_LOCK, true))
            .with(
                Condition.condition()
                    .term(PostBlock.SOUTH, Connection.DOUBLE),
                Variant.variant()
                    .with(VariantProperties.MODEL, doubleConnectionModel)
                    .with(VariantProperties.Y_ROT, Rotation.R180)
                    .with(VariantProperties.UV_LOCK, true))
            .with(
                Condition.condition()
                    .term(PostBlock.EAST, Connection.SINGLE),
                Variant.variant()
                    .with(VariantProperties.MODEL, singleConnectionModel)
                    .with(VariantProperties.Y_ROT, Rotation.R90)
                    .with(VariantProperties.UV_LOCK, true))
            .with(
                Condition.condition()
                    .term(PostBlock.EAST, Connection.DOUBLE),
                Variant.variant()
                    .with(VariantProperties.MODEL, doubleConnectionModel)
                    .with(VariantProperties.Y_ROT, Rotation.R90)
                    .with(VariantProperties.UV_LOCK, true))
            .with(
                Condition.condition()
                    .term(PostBlock.WEST, Connection.SINGLE),
                Variant.variant()
                    .with(VariantProperties.MODEL, singleConnectionModel)
                    .with(VariantProperties.Y_ROT, Rotation.R270)
                    .with(VariantProperties.UV_LOCK, true))
            .with(
                Condition.condition()
                    .term(PostBlock.WEST, Connection.DOUBLE),
                Variant.variant()
                    .with(VariantProperties.MODEL, doubleConnectionModel)
                    .with(VariantProperties.Y_ROT, Rotation.R270)
                    .with(VariantProperties.UV_LOCK, true)));
  }

  private void createAbandonedTracks(Block block, Block lockingTrackBlock,
      Block bufferStopTrackBlock, Block activatorTrackBlock, Block boosterTrackBlock,
      Block controlTrackBlock, Block gatedTrackBlock, Block detectorTrackBlock,
      Block couplerTrackBlock, Block embarkingTrackBlock, Block disembarkingTrackBlock,
      Block turnoutTrackBlock, Block wyeTrackBlock, Block junctionTrackBlock,
      Block launcherTrackBlock) {
    this.createAbandonedFlexTrack(block);
    this.createOutfittedTracks(block, lockingTrackBlock, bufferStopTrackBlock, activatorTrackBlock,
        boosterTrackBlock, controlTrackBlock, gatedTrackBlock, detectorTrackBlock,
        couplerTrackBlock, embarkingTrackBlock, disembarkingTrackBlock, turnoutTrackBlock,
        wyeTrackBlock, junctionTrackBlock, launcherTrackBlock);
  }

  private void createAbandonedFlexTrack(Block block) {
    var texture0 =
        TextureMapping.rail(TextureMapping.getBlockTexture(block, "_0"));
    var texture1 =
        TextureMapping.rail(TextureMapping.getBlockTexture(block, "_1"));
    var cornerTextures =
        TextureMapping.rail(TextureMapping.getBlockTexture(block, "_corner"));
    var flatModel0 =
        ModelTemplates.RAIL_FLAT.createWithSuffix(block, "_0", texture0, this.modelOutput);
    var flatModel1 =
        ModelTemplates.RAIL_FLAT.createWithSuffix(block, "_1", texture1, this.modelOutput);
    var cornerModel =
        ModelTemplates.RAIL_CURVED.create(block, cornerTextures, this.modelOutput);
    var raisedNorthEastModel =
        ModelTemplates.RAIL_RAISED_NE.create(block, texture0, this.modelOutput);
    var raisedSouthWestModel =
        ModelTemplates.RAIL_RAISED_SW.create(block, texture0, this.modelOutput);

    this.createSimpleFlatItemModel(block, "_0");

    this.blockStateOutput.accept(MultiPartGenerator.multiPart(block)
        .with(
            Condition.condition()
                .term(AbandonedTrackBlock.GRASS, true),
            Variant.variant()
                .with(VariantProperties.MODEL, new ResourceLocation("block/grass")))
        .with(
            Condition.condition()
                .term(BlockStateProperties.RAIL_SHAPE, RailShape.NORTH_SOUTH),
            Variant.variant().with(VariantProperties.MODEL, flatModel0),
            Variant.variant().with(VariantProperties.MODEL, flatModel1))
        .with(
            Condition.condition()
                .term(BlockStateProperties.RAIL_SHAPE, RailShape.EAST_WEST),
            Variant.variant()
                .with(VariantProperties.MODEL, flatModel0)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90),
            Variant.variant()
                .with(VariantProperties.MODEL, flatModel1)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(BlockStateProperties.RAIL_SHAPE, RailShape.ASCENDING_EAST),
            Variant.variant()
                .with(VariantProperties.MODEL, raisedNorthEastModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(BlockStateProperties.RAIL_SHAPE, RailShape.ASCENDING_WEST),
            Variant.variant()
                .with(VariantProperties.MODEL, raisedSouthWestModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(BlockStateProperties.RAIL_SHAPE, RailShape.ASCENDING_NORTH),
            Variant.variant()
                .with(VariantProperties.MODEL, raisedNorthEastModel))
        .with(
            Condition.condition()
                .term(BlockStateProperties.RAIL_SHAPE, RailShape.ASCENDING_SOUTH),
            Variant.variant()
                .with(VariantProperties.MODEL, raisedSouthWestModel))
        .with(
            Condition.condition()
                .term(BlockStateProperties.RAIL_SHAPE, RailShape.SOUTH_EAST),
            Variant.variant()
                .with(VariantProperties.MODEL, cornerModel))
        .with(
            Condition.condition()
                .term(BlockStateProperties.RAIL_SHAPE, RailShape.SOUTH_WEST),
            Variant.variant()
                .with(VariantProperties.MODEL, cornerModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(BlockStateProperties.RAIL_SHAPE, RailShape.NORTH_WEST),
            Variant.variant()
                .with(VariantProperties.MODEL, cornerModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
        .with(
            Condition.condition()
                .term(BlockStateProperties.RAIL_SHAPE, RailShape.NORTH_EAST),
            Variant.variant()
                .with(VariantProperties.MODEL, cornerModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)));
  }

  private void createTracks(Block block, Block lockingTrackBlock, Block bufferStopTrackBlock,
      Block activatorTrackBlock, Block boosterTrackBlock, Block controlTrackBlock,
      Block gatedTrackBlock, Block detectorTrackBlock, Block couplerTrackBlock,
      Block embarkingTrackBlock, Block disembarkingTrackBlock, Block turnoutTrackBlock,
      Block wyeTrackBlock, Block junctionTrackBlock, Block launcherTrackBlock) {
    this.createFlexTrack(block);
    this.createOutfittedTracks(block, lockingTrackBlock, bufferStopTrackBlock, activatorTrackBlock,
        boosterTrackBlock, controlTrackBlock, gatedTrackBlock, detectorTrackBlock,
        couplerTrackBlock, embarkingTrackBlock, disembarkingTrackBlock, turnoutTrackBlock,
        wyeTrackBlock, junctionTrackBlock, launcherTrackBlock);
  }

  private void createOutfittedTracks(Block block, Block lockingTrackBlock,
      Block bufferStopTrackBlock, Block activatorTrackBlock, Block boosterTrackBlock,
      Block controlTrackBlock, Block gatedTrackBlock, Block detectorTrackBlock,
      Block couplerTrackBlock, Block embarkingTrackBlock, Block disembarkingTrackBlock,
      Block turnoutTrackBlock, Block wyeTrackBlock, Block junctionTrackBlock,
      Block launcherTrackBlock) {
    var outfittedTrackModels = this.createOutfittedTrackModelSet(block);
    this.createLockingTrack(lockingTrackBlock, outfittedTrackModels.getFlatModel());
    this.createBufferStopTrack(bufferStopTrackBlock, outfittedTrackModels.getFlatModel());
    this.createActiveOutfittedTrack(activatorTrackBlock, true, outfittedTrackModels,
        this.activatorTrackModels, this.activeActivatorTrackModels);
    this.createActiveOutfittedTrack(boosterTrackBlock, true, outfittedTrackModels,
        this.boosterTrackModels, this.activeBoosterTrackModels);
    this.createControlTrack(controlTrackBlock, outfittedTrackModels);
    this.createGatedTrack(gatedTrackBlock, outfittedTrackModels);
    this.createDetectorTrack(detectorTrackBlock, outfittedTrackModels);
    this.createCouplerTrack(couplerTrackBlock, outfittedTrackModels);
    this.createEmbarkingTrack(embarkingTrackBlock, outfittedTrackModels);
    this.createDisembarkingTrack(disembarkingTrackBlock, outfittedTrackModels);
    this.createTurnoutTrack(turnoutTrackBlock);
    this.createWyeTrack(wyeTrackBlock);
    this.createJunctionTrack(junctionTrackBlock);
    this.createActiveOutfittedTrack(launcherTrackBlock, false, outfittedTrackModels,
        this.launcherTrackModels, this.activeLauncherTrackModels);
  }

  private void createHighSpeedTracks(Block block, Block transitionTrackBlock,
      Block lockingTrackBlock, Block activatorTrackBlock, Block boosterTrackBlock,
      Block detectorTrackBlock, Block turnoutTrackBlock, Block wyeTrackBlock,
      Block junctionTrackBlock) {
    this.createFlexTrack(block);
    var outfittedTrackModels = this.createOutfittedTrackModelSet(block);
    this.createTransitionTrack(transitionTrackBlock, outfittedTrackModels);
    this.createLockingTrack(lockingTrackBlock, outfittedTrackModels.getFlatModel());
    this.createActiveOutfittedTrack(activatorTrackBlock, true, outfittedTrackModels,
        this.activatorTrackModels, this.activeActivatorTrackModels);
    this.createActiveOutfittedTrack(boosterTrackBlock, true, outfittedTrackModels,
        this.boosterTrackModels, this.activeBoosterTrackModels);
    this.createDetectorTrack(detectorTrackBlock, outfittedTrackModels);
    this.createTurnoutTrack(turnoutTrackBlock);
    this.createWyeTrack(wyeTrackBlock);
    this.createJunctionTrack(junctionTrackBlock);
  }

  private void createFlexTrack(Block block) {
    var textures = TextureMapping.rail(block);
    var cornerTextures = TextureMapping.rail(TextureMapping.getBlockTexture(block, "_corner"));
    var flatModel = ModelTemplates.RAIL_FLAT.create(block, textures, this.modelOutput);
    var cornerModel = ModelTemplates.RAIL_CURVED.create(block, cornerTextures, this.modelOutput);
    var raisedNorthEastModel =
        ModelTemplates.RAIL_RAISED_NE.create(block, textures, this.modelOutput);
    var raisedSouthWestModel =
        ModelTemplates.RAIL_RAISED_SW.create(block, textures, this.modelOutput);

    this.createSimpleFlatItemModel(block);

    this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
        .with(PropertyDispatch.property(BlockStateProperties.RAIL_SHAPE)
            .select(RailShape.NORTH_SOUTH, Variant.variant()
                .with(VariantProperties.MODEL, flatModel))
            .select(RailShape.EAST_WEST, Variant.variant()
                .with(VariantProperties.MODEL, flatModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
            .select(RailShape.ASCENDING_EAST, Variant.variant()
                .with(VariantProperties.MODEL, raisedNorthEastModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
            .select(RailShape.ASCENDING_WEST, Variant.variant()
                .with(VariantProperties.MODEL, raisedSouthWestModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
            .select(RailShape.ASCENDING_NORTH, Variant.variant()
                .with(VariantProperties.MODEL, raisedNorthEastModel))
            .select(RailShape.ASCENDING_SOUTH, Variant.variant()
                .with(VariantProperties.MODEL, raisedSouthWestModel))
            .select(RailShape.SOUTH_EAST, Variant.variant()
                .with(VariantProperties.MODEL, cornerModel))
            .select(RailShape.SOUTH_WEST, Variant.variant()
                .with(VariantProperties.MODEL, cornerModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
            .select(RailShape.NORTH_WEST, Variant.variant()
                .with(VariantProperties.MODEL, cornerModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
            .select(RailShape.NORTH_EAST, Variant.variant()
                .with(VariantProperties.MODEL, cornerModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))));
  }

  private void createTurnoutTrack(Block block) {
    var northModel = this.createSuffixedVariant(block, "_north",
        ModelTemplates.RAIL_FLAT, TextureMapping::rail);
    var northSwitchedModel = this.createSuffixedVariant(block, "_north_switched",
        ModelTemplates.RAIL_FLAT, TextureMapping::rail);
    var southModel = this.createSuffixedVariant(block, "_south",
        ModelTemplates.RAIL_FLAT, TextureMapping::rail);
    var southSwitchedModel = this.createSuffixedVariant(block, "_south_switched",
        ModelTemplates.RAIL_FLAT, TextureMapping::rail);

    this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
        .with(PropertyDispatch.properties(SwitchTrackBlock.SHAPE,
            ReversibleOutfittedTrackBlock.REVERSED, TurnoutTrackBlock.MIRRORED,
            SwitchTrackBlock.SWITCHED)
            .select(RailShape.NORTH_SOUTH, false, false, false, Variant.variant() // North
                .with(VariantProperties.MODEL, northModel))
            .select(RailShape.NORTH_SOUTH, false, false, true, Variant.variant() // North
                .with(VariantProperties.MODEL, northSwitchedModel))
            .select(RailShape.NORTH_SOUTH, true, false, false, Variant.variant() // South
                .with(VariantProperties.MODEL, northModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
            .select(RailShape.NORTH_SOUTH, true, false, true, Variant.variant() // South
                .with(VariantProperties.MODEL, northSwitchedModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
            .select(RailShape.NORTH_SOUTH, false, true, false, Variant.variant() // North
                .with(VariantProperties.MODEL, southModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
            .select(RailShape.NORTH_SOUTH, false, true, true, Variant.variant() // North
                .with(VariantProperties.MODEL, southSwitchedModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
            .select(RailShape.NORTH_SOUTH, true, true, false, Variant.variant() // South
                .with(VariantProperties.MODEL, southModel))
            .select(RailShape.NORTH_SOUTH, true, true, true, Variant.variant() // South
                .with(VariantProperties.MODEL, southSwitchedModel))
            .select(RailShape.EAST_WEST, false, false, false, Variant.variant() // East
                .with(VariantProperties.MODEL, northModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
            .select(RailShape.EAST_WEST, false, false, true, Variant.variant() // East
                .with(VariantProperties.MODEL, northSwitchedModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
            .select(RailShape.EAST_WEST, true, false, false, Variant.variant() // West
                .with(VariantProperties.MODEL, northModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
            .select(RailShape.EAST_WEST, true, false, true, Variant.variant() // West
                .with(VariantProperties.MODEL, northSwitchedModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
            .select(RailShape.EAST_WEST, false, true, false, Variant.variant() // East
                .with(VariantProperties.MODEL, southModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
            .select(RailShape.EAST_WEST, false, true, true, Variant.variant() // East
                .with(VariantProperties.MODEL, southSwitchedModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
            .select(RailShape.EAST_WEST, true, true, false, Variant.variant() // West
                .with(VariantProperties.MODEL, southModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
            .select(RailShape.EAST_WEST, true, true, true, Variant.variant() // West
                .with(VariantProperties.MODEL, southSwitchedModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))));

    this.createSimpleFlatItemModel(block, "_north");
  }

  private void createWyeTrack(Block block) {
    var eastModel = this.createSuffixedVariant(block, "_east",
        ModelTemplates.RAIL_FLAT, TextureMapping::rail);
    var eastSwitchedModel = this.createSuffixedVariant(block, "_east_switched",
        ModelTemplates.RAIL_FLAT, TextureMapping::rail);
    var westModel = this.createSuffixedVariant(block, "_west",
        ModelTemplates.RAIL_FLAT, TextureMapping::rail);
    var westSwitchedModel = this.createSuffixedVariant(block, "_west_switched",
        ModelTemplates.RAIL_FLAT, TextureMapping::rail);

    this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
        .with(PropertyDispatch.properties(SwitchTrackBlock.SHAPE,
            ReversibleOutfittedTrackBlock.REVERSED, SwitchTrackBlock.SWITCHED)
            .generate((railShape, reversed, switched) -> {
              Direction facing = ReversibleOutfittedTrackBlock.getDirection(railShape, reversed);
              switch (facing) {
                case NORTH:
                  return Variant.variant()
                      .with(VariantProperties.MODEL, switched ? eastSwitchedModel : eastModel)
                      .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90);
                case SOUTH:
                  return Variant.variant()
                      .with(VariantProperties.MODEL, switched ? westSwitchedModel : westModel)
                      .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90);
                case EAST:
                  return Variant.variant()
                      .with(VariantProperties.MODEL, switched ? westSwitchedModel : westModel);
                case WEST:
                  return Variant.variant()
                      .with(VariantProperties.MODEL, switched ? eastSwitchedModel : eastModel);
                default:
                  throw new UnsupportedOperationException();
              }
            })));

    this.createSimpleFlatItemModel(block, "_east");
  }

  private void createJunctionTrack(Block block) {
    this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block, Variant.variant()
        .with(VariantProperties.MODEL, this.createPassiveRail(ForgeRegistries.BLOCKS.getKey(block).getPath()))));
    this.createSimpleFlatItemModel(block);
  }

  private void createLockingTrack(Block block, ResourceLocation trackModel) {
    var generator = MultiPartGenerator.multiPart(block)
        .with(
            Condition.condition()
                .term(LockingTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            Variant.variant()
                .with(VariantProperties.MODEL, trackModel))
        .with(
            Condition.condition()
                .term(LockingTrackBlock.SHAPE, RailShape.EAST_WEST),
            Variant.variant()
                .with(VariantProperties.MODEL, trackModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90));

    this.addLockingMode(LockingMode.LOCKDOWN, this.lockingTrackLockdownModel,
        this.lockingTrackActiveLockdownModel,
        generator);
    this.addLockingMode(LockingMode.TRAIN_LOCKDOWN, this.lockingTrackTrainLockdownModel,
        this.lockingTrackActiveTrainLockdownModel, generator);
    this.addLockingMode(LockingMode.HOLDING, this.lockingTrackHoldingModel,
        this.lockingTrackActiveHoldingModel,
        generator);
    this.addLockingMode(LockingMode.TRAIN_HOLDING, this.lockingTrackTrainHoldingModel,
        this.lockingTrackActiveTrainHoldingModel, generator);
    this.addLockingMode(LockingMode.BOARDING, this.lockingTrackBoardingModel,
        this.lockingTrackActiveBoardingModel,
        generator);
    this.addLockingMode(LockingMode.BOARDING_REVERSED, this.lockingTrackBoardingReversedModel,
        this.lockingTrackActiveBoardingReversedModel, generator);
    this.addLockingMode(LockingMode.TRAIN_BOARDING, this.lockingTrackTrainBoardingModel,
        this.lockingTrackActiveTrainBoardingModel, generator);
    this.addLockingMode(LockingMode.TRAIN_BOARDING_REVERSED,
        this.lockingTrackTrainBoardingReversedModel,
        this.lockingTrackActiveTrainBoardingReversedModel, generator);

    this.blockStateOutput.accept(generator);

    this.createSimpleFlatItemModel(block.asItem());
  }

  private void addLockingMode(LockingMode lockingMode, ResourceLocation model,
      ResourceLocation poweredModel, MultiPartGenerator blockState) {
    blockState
        .with(
            Condition.condition()
                .term(LockingTrackBlock.LOCKING_MODE, lockingMode)
                .term(LockingTrackBlock.POWERED, false)
                .term(LockingTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            Variant.variant()
                .with(VariantProperties.MODEL, model))
        .with(
            Condition.condition()
                .term(LockingTrackBlock.LOCKING_MODE, lockingMode)
                .term(LockingTrackBlock.POWERED, true)
                .term(LockingTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            Variant.variant()
                .with(VariantProperties.MODEL, poweredModel))
        .with(
            Condition.condition()
                .term(LockingTrackBlock.LOCKING_MODE, lockingMode)
                .term(LockingTrackBlock.POWERED, false)
                .term(LockingTrackBlock.SHAPE, RailShape.EAST_WEST),
            Variant.variant()
                .with(VariantProperties.MODEL, model)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(LockingTrackBlock.LOCKING_MODE, lockingMode)
                .term(LockingTrackBlock.POWERED, true)
                .term(LockingTrackBlock.SHAPE, RailShape.EAST_WEST),
            Variant.variant()
                .with(VariantProperties.MODEL, poweredModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90));
  }

  private void createBufferStopTrack(Block block, ResourceLocation trackModel) {
    this.blockStateOutput.accept(MultiPartGenerator.multiPart(block)
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH)
                .term(ReversibleOutfittedTrackBlock.REVERSED, false), // North
            Variant.variant()
                .with(VariantProperties.MODEL, RailcraftModelTemplates.BUFFER_STOP))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH)
                .term(ReversibleOutfittedTrackBlock.REVERSED, true), // South
            Variant.variant()
                .with(VariantProperties.MODEL, RailcraftModelTemplates.BUFFER_STOP)
                .with(VariantProperties.Y_ROT, Rotation.R180))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST)
                .term(ReversibleOutfittedTrackBlock.REVERSED, false), // East
            Variant.variant()
                .with(VariantProperties.MODEL, RailcraftModelTemplates.BUFFER_STOP)
                .with(VariantProperties.Y_ROT, Rotation.R90))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST)
                .term(ReversibleOutfittedTrackBlock.REVERSED, true), // West
            Variant.variant()
                .with(VariantProperties.MODEL, RailcraftModelTemplates.BUFFER_STOP)
                .with(VariantProperties.Y_ROT, Rotation.R270))
        .with(
            Condition.condition()
                .term(LockingTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            Variant.variant()
                .with(VariantProperties.MODEL, trackModel))
        .with(
            Condition.condition()
                .term(LockingTrackBlock.SHAPE, RailShape.EAST_WEST),
            Variant.variant()
                .with(VariantProperties.MODEL, trackModel)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)));

    this.createSimpleFlatItemModel(block.asItem());
  }

  private void createActiveOutfittedTrack(Block block, boolean allowedOnSlopes,
      StraightTrackModelSet trackModels,
      StraightTrackModelSet trackKitModels,
      StraightTrackModelSet activeTrackKitModels) {
    var generator = MultiPartGenerator.multiPart(block);
    trackModels.apply(generator, OutfittedTrackBlock.SHAPE, allowedOnSlopes, false);
    trackKitModels.apply(generator, OutfittedTrackBlock.SHAPE, allowedOnSlopes, false,
        condition -> condition.term(PoweredOutfittedTrackBlock.POWERED, false));
    activeTrackKitModels.apply(generator, OutfittedTrackBlock.SHAPE, allowedOnSlopes, false,
        condition -> condition.term(PoweredOutfittedTrackBlock.POWERED, true));
    this.blockStateOutput.accept(generator);
    this.createSimpleFlatItemModel(block.asItem());
  }

  private void createControlTrack(Block block, StraightTrackModelSet trackModels) {
    var generator = MultiPartGenerator.multiPart(block);
    trackModels.apply(generator, OutfittedTrackBlock.SHAPE, true, false);
    this.blockStateOutput.accept(generator
        .with(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, false)
                .term(ControlTrackBlock.REVERSED, false)
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            Variant.variant()
                .with(VariantProperties.MODEL, this.controlTrackModels.getFlatModel()))
        .with(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, false)
                .term(ControlTrackBlock.REVERSED, false)
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST),
            Variant.variant()
                .with(VariantProperties.MODEL, this.controlTrackModels.getFlatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.or(
                Condition.condition()
                    .term(PoweredOutfittedTrackBlock.POWERED, true)
                    .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
                Condition.condition()
                    .term(ControlTrackBlock.REVERSED, true)
                    .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH)),
            Variant.variant()
                .with(VariantProperties.MODEL, this.controlTrackModels.getFlatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
        .with(
            Condition.or(
                Condition.condition()
                    .term(PoweredOutfittedTrackBlock.POWERED, true)
                    .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST),
                Condition.condition()
                    .term(ControlTrackBlock.REVERSED, true)
                    .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST)),
            Variant.variant()
                .with(VariantProperties.MODEL, this.controlTrackModels.getFlatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
        .with(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, false)
                .term(ControlTrackBlock.REVERSED, false)
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_NORTH),
            Variant.variant()
                .with(VariantProperties.MODEL, this.controlTrackModels.getRaisedNorthEastModel()))
        .with(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, false)
                .term(ControlTrackBlock.REVERSED, false)
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_SOUTH),
            Variant.variant()
                .with(VariantProperties.MODEL, this.controlTrackModels.getRaisedSouthWestModel()))
        .with(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, false)
                .term(ControlTrackBlock.REVERSED, false)
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_EAST),
            Variant.variant()
                .with(VariantProperties.MODEL, this.controlTrackModels.getRaisedNorthEastModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, false)
                .term(ControlTrackBlock.REVERSED, false)
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_WEST),
            Variant.variant()
                .with(VariantProperties.MODEL, this.controlTrackModels.getRaisedSouthWestModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.or(
                Condition.condition()
                    .term(PoweredOutfittedTrackBlock.POWERED, true)
                    .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_NORTH),
                Condition.condition()
                    .term(ControlTrackBlock.REVERSED, true)
                    .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_NORTH)),
            Variant.variant()
                .with(VariantProperties.MODEL, this.controlTrackModels.getRaisedSouthWestModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
        .with(
            Condition.or(
                Condition.condition()
                    .term(PoweredOutfittedTrackBlock.POWERED, true)
                    .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_SOUTH),
                Condition.condition()
                    .term(ControlTrackBlock.REVERSED, true)
                    .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_SOUTH)),
            Variant.variant()
                .with(VariantProperties.MODEL, this.controlTrackModels.getRaisedNorthEastModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
        .with(
            Condition.or(
                Condition.condition()
                    .term(PoweredOutfittedTrackBlock.POWERED, true)
                    .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_EAST),
                Condition.condition()
                    .term(ControlTrackBlock.REVERSED, true)
                    .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_EAST)),
            Variant.variant()
                .with(VariantProperties.MODEL, this.controlTrackModels.getRaisedSouthWestModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
        .with(
            Condition.or(
                Condition.condition()
                    .term(PoweredOutfittedTrackBlock.POWERED, true)
                    .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_WEST),
                Condition.condition()
                    .term(ControlTrackBlock.REVERSED, true)
                    .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_WEST)),
            Variant.variant()
                .with(VariantProperties.MODEL, this.controlTrackModels.getRaisedNorthEastModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)));

    this.createSimpleFlatItemModel(block.asItem());
  }

  private void createTransitionTrack(Block block, StraightTrackModelSet trackModels) {
    var generator = MultiPartGenerator.multiPart(block);
    trackModels.apply(generator, OutfittedTrackBlock.SHAPE, true, false);
    this.addTransitionVariants(generator, false, this.transitionTrackModels);
    this.addTransitionVariants(generator, true, this.activeTransitionTrackModels);
    this.blockStateOutput.accept(generator);
    this.createSimpleFlatItemModel(block.asItem());
  }

  private void addTransitionVariants(MultiPartGenerator blockState, boolean powered,
      StraightTrackModelSet trackKitModels) {
    blockState
        .with(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, powered)
                .term(ReversibleOutfittedTrackBlock.REVERSED, false)
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            Variant.variant()
                .with(VariantProperties.MODEL, trackKitModels.getFlatModel()))
        .with(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, powered)
                .term(ReversibleOutfittedTrackBlock.REVERSED, false)
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST),
            Variant.variant()
                .with(VariantProperties.MODEL, trackKitModels.getFlatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, powered)
                .term(ReversibleOutfittedTrackBlock.REVERSED, true)
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            Variant.variant()
                .with(VariantProperties.MODEL, trackKitModels.getFlatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
        .with(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, powered)
                .term(ReversibleOutfittedTrackBlock.REVERSED, true)
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST),
            Variant.variant()
                .with(VariantProperties.MODEL, trackKitModels.getFlatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
        .with(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, powered)
                .term(ReversibleOutfittedTrackBlock.REVERSED, false)
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_NORTH),
            Variant.variant()
                .with(VariantProperties.MODEL, trackKitModels.getRaisedNorthEastModel()))
        .with(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, powered)
                .term(ReversibleOutfittedTrackBlock.REVERSED, false)
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_SOUTH),
            Variant.variant()
                .with(VariantProperties.MODEL, trackKitModels.getRaisedSouthWestModel()))
        .with(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, powered)
                .term(ReversibleOutfittedTrackBlock.REVERSED, false)
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_EAST),
            Variant.variant()
                .with(VariantProperties.MODEL, trackKitModels.getRaisedNorthEastModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, powered)
                .term(ReversibleOutfittedTrackBlock.REVERSED, false)
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_WEST),
            Variant.variant()
                .with(VariantProperties.MODEL, trackKitModels.getRaisedSouthWestModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, powered)
                .term(ReversibleOutfittedTrackBlock.REVERSED, true)
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_NORTH),
            Variant.variant()
                .with(VariantProperties.MODEL, trackKitModels.getRaisedSouthWestModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
        .with(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, powered)
                .term(ReversibleOutfittedTrackBlock.REVERSED, true)
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_SOUTH),
            Variant.variant()
                .with(VariantProperties.MODEL, trackKitModels.getRaisedNorthEastModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
        .with(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, powered)
                .term(ReversibleOutfittedTrackBlock.REVERSED, true)
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_EAST),
            Variant.variant()
                .with(VariantProperties.MODEL, trackKitModels.getRaisedSouthWestModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
        .with(
            Condition.condition()
                .term(PoweredOutfittedTrackBlock.POWERED, powered)
                .term(ReversibleOutfittedTrackBlock.REVERSED, true)
                .term(OutfittedTrackBlock.SHAPE, RailShape.ASCENDING_WEST),
            Variant.variant()
                .with(VariantProperties.MODEL, trackKitModels.getRaisedNorthEastModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270));
  }

  private void createGatedTrack(Block block, StraightTrackModelSet trackModels) {
    var closedGateModel =
        ModelLocationUtils.getModelLocation(Blocks.OAK_FENCE_GATE);
    var openGateModel =
        ModelLocationUtils.getModelLocation(Blocks.OAK_FENCE_GATE, "_open");
    var closedWallGateModel =
        ModelLocationUtils.getModelLocation(Blocks.OAK_FENCE_GATE, "_wall");
    var openWallGateModel =
        ModelLocationUtils.getModelLocation(Blocks.OAK_FENCE_GATE, "_wall_open");

    var generator = MultiPartGenerator.multiPart(block)
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH),
            Variant.variant()
                .with(VariantProperties.MODEL, trackModels.getFlatModel()))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST),
            Variant.variant()
                .with(VariantProperties.MODEL, trackModels.getFlatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(GatedTrackBlock.ONE_WAY, true)
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH)
                .term(ReversibleOutfittedTrackBlock.REVERSED, false),
            Variant.variant()
                .with(VariantProperties.MODEL, this.controlTrackModels.getFlatModel()))
        .with(
            Condition.condition()
                .term(GatedTrackBlock.ONE_WAY, true)
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH)
                .term(ReversibleOutfittedTrackBlock.REVERSED, true),
            Variant.variant()
                .with(VariantProperties.MODEL, this.controlTrackModels.getFlatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
        .with(
            Condition.condition()
                .term(GatedTrackBlock.ONE_WAY, true)
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST)
                .term(ReversibleOutfittedTrackBlock.REVERSED, false),
            Variant.variant()
                .with(VariantProperties.MODEL, this.controlTrackModels.getFlatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
        .with(
            Condition.condition()
                .term(GatedTrackBlock.ONE_WAY, true)
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST)
                .term(ReversibleOutfittedTrackBlock.REVERSED, true),
            Variant.variant()
                .with(VariantProperties.MODEL, this.controlTrackModels.getFlatModel())
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270));

    this.addGateVariants(generator, false, false, closedGateModel);
    this.addGateVariants(generator, true, false, openGateModel);
    this.addGateVariants(generator, false, true, closedWallGateModel);
    this.addGateVariants(generator, true, true, openWallGateModel);

    this.blockStateOutput.accept(generator);

    this.createSimpleFlatItemModel(block.asItem());
  }

  private void addGateVariants(MultiPartGenerator blockState, boolean open, boolean inWall,
      ResourceLocation model) {
    blockState
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH)
                .term(ReversibleOutfittedTrackBlock.REVERSED, false) // North
                .term(GatedTrackBlock.OPEN, open)
                .term(GatedTrackBlock.IN_WALL, inWall),
            Variant.variant()
                .with(VariantProperties.MODEL, model)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH)
                .term(ReversibleOutfittedTrackBlock.REVERSED, true) // South
                .term(GatedTrackBlock.OPEN, open)
                .term(GatedTrackBlock.IN_WALL, inWall),
            Variant.variant()
                .with(VariantProperties.MODEL, model))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST)
                .term(ReversibleOutfittedTrackBlock.REVERSED, false) // East
                .term(GatedTrackBlock.OPEN, open)
                .term(GatedTrackBlock.IN_WALL, inWall),
            Variant.variant()
                .with(VariantProperties.MODEL, model)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
        .with(
            Condition.condition()
                .term(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST)
                .term(ReversibleOutfittedTrackBlock.REVERSED, true) // West
                .term(GatedTrackBlock.OPEN, open)
                .term(GatedTrackBlock.IN_WALL, inWall),
            Variant.variant()
                .with(VariantProperties.MODEL, model)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90));
  }

  private void createDetectorTrack(Block block, StraightTrackModelSet trackModels) {
    var generator = MultiPartGenerator.multiPart(block);
    trackModels.apply(generator, DetectorTrackBlock.SHAPE, true, false);
    this.detectorTrackModels.apply(generator, DetectorTrackBlock.SHAPE, true, false,
        condition -> condition
            .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.BI_DIRECTIONAL)
            .term(DetectorTrackBlock.POWERED, false));
    this.activeDetectorTrackModels.apply(generator, DetectorTrackBlock.SHAPE, true, false,
        condition -> condition
            .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.BI_DIRECTIONAL)
            .term(DetectorTrackBlock.POWERED, true));
    this.travelDetectorTrackModels.apply(generator, DetectorTrackBlock.SHAPE, true, false,
        condition -> condition
            .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.TRAVEL)
            .term(DetectorTrackBlock.POWERED, false));
    this.activeTravelDetectorTrackModels.apply(generator, DetectorTrackBlock.SHAPE, true, false,
        condition -> condition
            .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.TRAVEL)
            .term(DetectorTrackBlock.POWERED, true));
    this.travelDetectorTrackModels.apply(generator, DetectorTrackBlock.SHAPE, true, true,
        condition -> condition
            .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.TRAVEL_REVERSED)
            .term(DetectorTrackBlock.POWERED, false));
    this.activeTravelDetectorTrackModels.apply(generator, DetectorTrackBlock.SHAPE, true, true,
        condition -> condition
            .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.TRAVEL_REVERSED)
            .term(DetectorTrackBlock.POWERED, true));
    this.blockStateOutput.accept(generator);
    this.createSimpleFlatItemModel(block.asItem());
  }

  private void createCouplerTrack(Block block, StraightTrackModelSet trackModels) {
    var generator = MultiPartGenerator.multiPart(block);
    trackModels.apply(generator, CouplerTrackBlock.SHAPE, true, false);
    this.couplerTrackCoupler.apply(generator, CouplerTrackBlock.SHAPE, true, false,
        condition -> condition
            .term(CouplerTrackBlock.MODE, CouplerTrackBlockEntity.Mode.COUPLER)
            .term(CouplerTrackBlock.POWERED, false));
    this.activeCouplerTrackCoupler.apply(generator, CouplerTrackBlock.SHAPE, true, false,
        condition -> condition
            .term(CouplerTrackBlock.MODE, CouplerTrackBlockEntity.Mode.COUPLER)
            .term(CouplerTrackBlock.POWERED, true));
    this.couplerTrackDecoupler.apply(generator, CouplerTrackBlock.SHAPE, true, false,
        condition -> condition
            .term(CouplerTrackBlock.MODE, CouplerTrackBlockEntity.Mode.DECOUPLER)
            .term(CouplerTrackBlock.POWERED, false));
    this.activeCouplerTrackDecoupler.apply(generator, CouplerTrackBlock.SHAPE, true, false,
        condition -> condition
            .term(CouplerTrackBlock.MODE, CouplerTrackBlockEntity.Mode.DECOUPLER)
            .term(CouplerTrackBlock.POWERED, true));
    this.couplerTrackAutoCoupler.apply(generator, CouplerTrackBlock.SHAPE, true, false,
        condition -> condition
            .term(CouplerTrackBlock.MODE, CouplerTrackBlockEntity.Mode.AUTO_COUPLER)
            .term(CouplerTrackBlock.POWERED, false));
    this.activeCouplerTrackAutoCoupler.apply(generator, CouplerTrackBlock.SHAPE, true, false,
        condition -> condition
            .term(CouplerTrackBlock.MODE, CouplerTrackBlockEntity.Mode.AUTO_COUPLER)
            .term(CouplerTrackBlock.POWERED, true));
    this.blockStateOutput.accept(generator);
    this.createSimpleFlatItemModel(block.asItem());
  }

  private void createEmbarkingTrack(Block block, StraightTrackModelSet trackModels) {
    var generator = MultiPartGenerator.multiPart(block);
    trackModels.apply(generator, CouplerTrackBlock.SHAPE, true, false);
    this.embarkingTrack.apply(generator, CouplerTrackBlock.SHAPE, true, false,
        condition -> condition.term(PoweredOutfittedTrackBlock.POWERED, false));
    this.activeEmbarkingTrack.apply(generator, CouplerTrackBlock.SHAPE, true, false,
        condition -> condition.term(PoweredOutfittedTrackBlock.POWERED, true));
    this.blockStateOutput.accept(generator);
    this.createSimpleFlatItemModel(block.asItem());
  }

  private void createDisembarkingTrack(Block block, StraightTrackModelSet trackModels) {
    var generator = MultiPartGenerator.multiPart(block);
    trackModels.apply(generator, CouplerTrackBlock.SHAPE, true, false);
    this.disembarkingTrackLeft.apply(generator, CouplerTrackBlock.SHAPE, true, false,
        condition -> condition
            .term(DisembarkingTrackBlock.MIRRORED, false)
            .term(PoweredOutfittedTrackBlock.POWERED, false));
    this.activeDisembarkingTrackLeft.apply(generator, CouplerTrackBlock.SHAPE, true, false,
        condition -> condition
            .term(DisembarkingTrackBlock.MIRRORED, false)
            .term(PoweredOutfittedTrackBlock.POWERED, true));
    this.disembarkingTrackRight.apply(generator, CouplerTrackBlock.SHAPE, true, false,
        condition -> condition
            .term(DisembarkingTrackBlock.MIRRORED, true)
            .term(PoweredOutfittedTrackBlock.POWERED, false));
    this.activeDisembarkingTrackRight.apply(generator, CouplerTrackBlock.SHAPE, true, false,
        condition -> condition
            .term(DisembarkingTrackBlock.MIRRORED, true)
            .term(PoweredOutfittedTrackBlock.POWERED, true));
    this.blockStateOutput.accept(generator);
    this.createSimpleFlatItemModel(block.asItem());
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
    return new StraightTrackModelSet()
        .setFlatModel(this.createPassiveRail(name))
        .setRaisedNorthEastModel(
            this.createVariant(name, ModelTemplates.RAIL_RAISED_NE, TextureMapping::rail))
        .setRaisedSouthWestModel(
            this.createVariant(name, ModelTemplates.RAIL_RAISED_SW, TextureMapping::rail));
  }

  private StraightTrackModelSet createActiveTrackModelSet(String name) {
    return new StraightTrackModelSet()
        .setFlatModel(this.createActiveRail(name))
        .setRaisedNorthEastModel(this.createVariant(name + "_on",
            ModelTemplates.RAIL_RAISED_NE, TextureMapping::rail))
        .setRaisedSouthWestModel(this.createVariant(name + "_on",
            ModelTemplates.RAIL_RAISED_SW, TextureMapping::rail));
  }

  private class StraightTrackModelSet {

    private ResourceLocation flatModel;
    private ResourceLocation raisedNorthEastModel;
    private ResourceLocation raisedSouthWestModel;

    private ResourceLocation getFlatModel() {
      return this.flatModel;
    }

    private StraightTrackModelSet setFlatModel(ResourceLocation flatModel) {
      this.flatModel = flatModel;
      return this;
    }

    private ResourceLocation getRaisedNorthEastModel() {
      return this.raisedNorthEastModel;
    }

    private StraightTrackModelSet setRaisedNorthEastModel(ResourceLocation raisedNorthEastModel) {
      this.raisedNorthEastModel = raisedNorthEastModel;
      return this;
    }

    private ResourceLocation getRaisedSouthWestModel() {
      return this.raisedSouthWestModel;
    }

    private StraightTrackModelSet setRaisedSouthWestModel(ResourceLocation raisedSouthWestModel) {
      this.raisedSouthWestModel = raisedSouthWestModel;
      return this;
    }

    private void apply(MultiPartGenerator generator, Property<RailShape> shapeProperty,
        boolean includeRaised, boolean reversed) {
      this.apply(generator, shapeProperty, includeRaised, reversed, null);
    }

    private void apply(MultiPartGenerator generator, Property<RailShape> shapeProperty,
        boolean includeRaised, boolean reversed,
        @Nullable Consumer<Condition.TerminalCondition> conditionModifier) {
      Supplier<Condition.TerminalCondition> condition = conditionModifier == null
          ? Condition::condition
          : () -> {
            var c = Condition.condition();
            conditionModifier.accept(c);
            return c;
          };
      generator
          .with(
              condition.get().term(shapeProperty, RailShape.NORTH_SOUTH),
              Variant.variant()
                  .with(VariantProperties.MODEL, this.flatModel)
                  .with(VariantProperties.Y_ROT,
                      reversed ? VariantProperties.Rotation.R180 : VariantProperties.Rotation.R0))
          .with(
              condition.get().term(shapeProperty, RailShape.EAST_WEST),
              Variant.variant()
                  .with(VariantProperties.MODEL, this.flatModel)
                  .with(VariantProperties.Y_ROT,
                      reversed ? VariantProperties.Rotation.R270 : VariantProperties.Rotation.R90));

      if (includeRaised) {
        generator.with(
            condition.get().term(shapeProperty, RailShape.ASCENDING_NORTH),
            Variant.variant()
                .with(VariantProperties.MODEL,
                    reversed ? this.raisedSouthWestModel : this.raisedNorthEastModel)
                .with(VariantProperties.Y_ROT,
                    reversed ? VariantProperties.Rotation.R180 : VariantProperties.Rotation.R0))
            .with(
                condition.get().term(shapeProperty, RailShape.ASCENDING_SOUTH),
                Variant.variant()
                    .with(VariantProperties.MODEL,
                        reversed ? this.raisedNorthEastModel : this.raisedSouthWestModel)
                    .with(VariantProperties.Y_ROT,
                        reversed ? VariantProperties.Rotation.R180 : VariantProperties.Rotation.R0))
            .with(
                condition.get().term(shapeProperty, RailShape.ASCENDING_EAST),
                Variant.variant()
                    .with(VariantProperties.MODEL,
                        reversed ? this.raisedSouthWestModel : this.raisedNorthEastModel)
                    .with(VariantProperties.Y_ROT,
                        reversed ? VariantProperties.Rotation.R270
                            : VariantProperties.Rotation.R90))
            .with(
                condition.get().term(shapeProperty, RailShape.ASCENDING_WEST),
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
