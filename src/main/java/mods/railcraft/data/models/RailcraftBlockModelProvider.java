package mods.railcraft.data.models;

import java.util.function.Function;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.Railcraft;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import mods.railcraft.world.level.block.AbstractStrengthenedGlassBlock;
import mods.railcraft.world.level.block.CrusherMultiblockBlock;
import mods.railcraft.world.level.block.ForceTrackEmitterBlock;
import mods.railcraft.world.level.block.FurnaceMultiblockBlock;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.SteamTurbineBlock;
import mods.railcraft.world.level.block.SteamTurbineBlock.Type;
import mods.railcraft.world.level.block.entity.track.CouplerTrackBlockEntity;
import mods.railcraft.world.level.block.manipulator.AdvancedItemLoaderBlock;
import mods.railcraft.world.level.block.manipulator.FluidManipulatorBlock;
import mods.railcraft.world.level.block.manipulator.ManipulatorBlock;
import mods.railcraft.world.level.block.post.Column;
import mods.railcraft.world.level.block.post.Connection;
import mods.railcraft.world.level.block.post.PostBlock;
import mods.railcraft.world.level.block.steamboiler.FireboxBlock;
import mods.railcraft.world.level.block.steamboiler.SteamBoilerTankBlock;
import mods.railcraft.world.level.block.tank.BaseTankBlock;
import mods.railcraft.world.level.block.tank.IronTankGaugeBlock;
import mods.railcraft.world.level.block.tank.TankValveBlock;
import mods.railcraft.world.level.block.track.AbandonedTrackBlock;
import mods.railcraft.world.level.block.track.ElevatorTrackBlock;
import mods.railcraft.world.level.block.track.ForceTrackBlock;
import mods.railcraft.world.level.block.track.TrackBlock;
import mods.railcraft.world.level.block.track.outfitted.ActivatorTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.BoosterTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.BufferStopTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.ControlTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.CouplerTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.DetectorTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.DetectorTrackBlock.Mode;
import mods.railcraft.world.level.block.track.outfitted.DisembarkingTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.EmbarkingTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.GatedTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.JunctionTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.LauncherTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.LockingMode;
import mods.railcraft.world.level.block.track.outfitted.LockingTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.LocomotiveTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.OneWayTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.OutfittedTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.PoweredOutfittedTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.ReversibleOutfittedTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.RoutingTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.SwitchTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.TransitionTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.TurnoutTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.WyeTrackBlock;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.blockstates.Condition;
import net.minecraft.data.models.blockstates.Condition.CompositeCondition;
import net.minecraft.data.models.blockstates.Condition.TerminalCondition;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class RailcraftBlockModelProvider extends BlockStateProvider {

  private static final String CUTOUT = "cutout";

  private static final ResourceLocation FLAT_TEMPLATE =
      new ResourceLocation("rail_flat");
  private static final ResourceLocation CORNER_TEMPLATE =
      new ResourceLocation("rail_curved");
  private static final ResourceLocation RAISED_NE_TEMPLATE =
      new ResourceLocation("template_rail_raised_ne");
  private static final ResourceLocation RAISED_SW_TEMPLATE =
      new ResourceLocation("template_rail_raised_sw");

  private StraightTrackModelSet activatorTrackModels;
  private StraightTrackModelSet activeActivatorTrackModels;
  private StraightTrackModelSet boosterTrackModels;
  private StraightTrackModelSet activeBoosterTrackModels;
  private StraightTrackModelSet embarkingTrack;
  private StraightTrackModelSet activeEmbarkingTrack;
  private StraightTrackModelSet launcherTrackModels;
  private StraightTrackModelSet activeLauncherTrackModels;
  private StraightTrackModelSet oneWayTrackModels;
  private StraightTrackModelSet activeOneWayTrackModels;
  private StraightTrackModelSet routingTrackModels;
  private StraightTrackModelSet activeRoutingTrackModels;
  private StraightTrackModelSet transitionTrackModels;
  private StraightTrackModelSet activeTransitionTrackModels;
  private StraightTrackModelSet detectorTrackModels;
  private StraightTrackModelSet activeDetectorTrackModels;
  private StraightTrackModelSet travelDetectorTrackModels;
  private StraightTrackModelSet activeTravelDetectorTrackModels;
  private StraightTrackModelSet locomotiveTrackShutdownModel;
  private StraightTrackModelSet locomotiveTrackIdleModel;
  private StraightTrackModelSet locomotiveTrackRunningModel;
  private StraightTrackModelSet activeLocomotiveTrackShutdownModel;
  private StraightTrackModelSet activeLocomotiveTrackIdleModel;
  private StraightTrackModelSet activeLocomotiveTrackRunningModel;
  private StraightTrackModelSet controlTrackModels;
  private StraightTrackModelSet couplerTrackCoupler;
  private StraightTrackModelSet activeCouplerTrackCoupler;
  private StraightTrackModelSet couplerTrackDecoupler;
  private StraightTrackModelSet activeCouplerTrackDecoupler;
  private StraightTrackModelSet couplerTrackAutoCoupler;
  private StraightTrackModelSet activeCouplerTrackAutoCoupler;
  private StraightTrackModelSet disembarkingTrackLeft;
  private StraightTrackModelSet activeDisembarkingTrackLeft;
  private StraightTrackModelSet disembarkingTrackRight;
  private StraightTrackModelSet activeDisembarkingTrackRight;

  public RailcraftBlockModelProvider(PackOutput packOutput, ExistingFileHelper fileHelper) {
    super(packOutput, Railcraft.ID, fileHelper);
  }

  private ResourceLocation key(Block block) {
    return ForgeRegistries.BLOCKS.getKey(block);
  }

  private String name(Block block) {
    return this.key(block).getPath();
  }

  private String name(Block block, String suffix) {
    return this.name(block) + suffix;
  }

  @Override
  public void simpleBlock(Block block) {
    super.simpleBlock(block);
    this.simpleBlockItem(block, this.cubeAll(block));
  }

  public void simpleStairsBlock(StairBlock stairBlock, Block textureBlock) {
    var texture = TextureMapping.getBlockTexture(textureBlock);
    this.stairsBlock(stairBlock, texture);
    this.itemModels().stairs(name(stairBlock), texture, texture, texture);
  }

  public void simpleSlabBlock(SlabBlock slabBlock, Block textureBlock) {
    var texture = TextureMapping.getBlockTexture(textureBlock);
    this.slabBlock(slabBlock, texture, texture);
    this.itemModels().slab(name(slabBlock), texture, texture, texture);
  }

  public void horizontalBlockPropertyIgnore(Block block,
      Function<BlockState, ModelFile> modelFunc, Property<?>... ignored) {
    this.getVariantBuilder(block)
        .forAllStatesExcept(state -> ConfiguredModel.builder()
            .modelFile(modelFunc.apply(state))
            .rotationY(
                ((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
            .build(), ignored);
  }

  private BlockModelBuilder cube(String name, ResourceLocation parent,
      ResourceLocation down, ResourceLocation up,
      ResourceLocation north, ResourceLocation south,
      ResourceLocation east, ResourceLocation west,
      ResourceLocation particle) {
    return this.models().withExistingParent(name, parent)
        .texture("down", down)
        .texture("up", up)
        .texture("north", north)
        .texture("south", south)
        .texture("east", east)
        .texture("west", west)
        .texture("particle", particle);
  }

  private BlockModelBuilder sideEnd(String name, ResourceLocation parent, ResourceLocation side,
      ResourceLocation end) {
    return this.models().withExistingParent(name, parent)
        .texture("side", side)
        .texture("end", end);
  }

  private void basicItem(Block block) {
    this.basicItem(block, "");
  }

  private void basicItem(Block block, String suffix) {
    this.itemModels().withExistingParent(this.name(block), "item/generated")
        .texture("layer0", this.modLoc("block/" + this.name(block) + suffix));
  }

  @Override
  protected void registerStatesAndModels() {
    for (var dyeColor : DyeColor.values()) {
      this.createStrengthenedGlass(RailcraftBlocks.STRENGTHENED_GLASS.variantFor(dyeColor).get());
      this.createStrengthenedGlass(RailcraftBlocks.IRON_TANK_GAUGE.variantFor(dyeColor).get());
      this.createStrengthenedGlass(RailcraftBlocks.STEEL_TANK_GAUGE.variantFor(dyeColor).get());

      this.createTankValve(RailcraftBlocks.IRON_TANK_VALVE.variantFor(dyeColor).get(),
          RailcraftBlocks.IRON_TANK_WALL.variantFor(dyeColor).get());
      this.createTankValve(RailcraftBlocks.STEEL_TANK_VALVE.variantFor(dyeColor).get(),
          RailcraftBlocks.STEEL_TANK_WALL.variantFor(dyeColor).get());
      this.createCubeColumnBlock(RailcraftBlocks.IRON_TANK_WALL.variantFor(dyeColor).get());
      this.createCubeColumnBlock(RailcraftBlocks.STEEL_TANK_WALL.variantFor(dyeColor).get());
      this.createPost(RailcraftBlocks.POST.variantFor(dyeColor).get());
    }


    this.simpleBlock(RailcraftBlocks.STEEL_BLOCK.get());
    this.simpleBlock(RailcraftBlocks.BRASS_BLOCK.get());
    this.simpleBlock(RailcraftBlocks.BRONZE_BLOCK.get());
    this.simpleBlock(RailcraftBlocks.INVAR_BLOCK.get());
    this.simpleBlock(RailcraftBlocks.LEAD_BLOCK.get());
    this.simpleBlock(RailcraftBlocks.NICKEL_BLOCK.get());
    this.simpleBlock(RailcraftBlocks.SILVER_BLOCK.get());
    this.simpleBlock(RailcraftBlocks.TIN_BLOCK.get());
    this.simpleBlock(RailcraftBlocks.ZINC_BLOCK.get());
    this.simpleBlock(RailcraftBlocks.COKE_BLOCK.get());
    this.simpleBlock(RailcraftBlocks.CRUSHED_OBSIDIAN.get());

    this.simpleBlock(RailcraftBlocks.LEAD_ORE.get());
    this.simpleBlock(RailcraftBlocks.NICKEL_ORE.get());
    this.simpleBlock(RailcraftBlocks.SILVER_ORE.get());
    this.simpleBlock(RailcraftBlocks.SULFUR_ORE.get());
    this.simpleBlock(RailcraftBlocks.TIN_ORE.get());
    this.simpleBlock(RailcraftBlocks.ZINC_ORE.get());
    this.simpleBlock(RailcraftBlocks.DEEPSLATE_LEAD_ORE.get());
    this.simpleBlock(RailcraftBlocks.DEEPSLATE_NICKEL_ORE.get());
    this.simpleBlock(RailcraftBlocks.DEEPSLATE_SILVER_ORE.get());
    this.simpleBlock(RailcraftBlocks.DEEPSLATE_SULFUR_ORE.get());
    this.simpleBlock(RailcraftBlocks.DEEPSLATE_TIN_ORE.get());
    this.simpleBlock(RailcraftBlocks.DEEPSLATE_ZINC_ORE.get());
    this.simpleBlock(RailcraftBlocks.SALTPETER_ORE.get());
    this.simpleBlock(RailcraftBlocks.FIRESTONE_ORE.get());

    this.simpleBlock(RailcraftBlocks.QUARRIED_STONE.get());
    this.simpleBlock(RailcraftBlocks.QUARRIED_COBBLESTONE.get());
    this.simpleBlock(RailcraftBlocks.POLISHED_QUARRIED_STONE.get());
    this.simpleBlock(RailcraftBlocks.CHISELED_QUARRIED_STONE.get());
    this.simpleBlock(RailcraftBlocks.ETCHED_QUARRIED_STONE.get());
    this.simpleBlock(RailcraftBlocks.QUARRIED_BRICKS.get());
    this.simpleBlock(RailcraftBlocks.QUARRIED_PAVER.get());
    this.simpleStairsBlock(RailcraftBlocks.QUARRIED_BRICK_STAIRS.get(),
        RailcraftBlocks.QUARRIED_BRICKS.get());
    this.simpleStairsBlock(RailcraftBlocks.QUARRIED_PAVER_STAIRS.get(),
        RailcraftBlocks.QUARRIED_PAVER.get());
    this.simpleSlabBlock(RailcraftBlocks.QUARRIED_BRICK_SLAB.get(),
        RailcraftBlocks.QUARRIED_BRICKS.get());
    this.simpleSlabBlock(RailcraftBlocks.QUARRIED_PAVER_SLAB.get(),
        RailcraftBlocks.QUARRIED_PAVER.get());

    this.fluidBlock(RailcraftBlocks.CREOSOTE.get());

    this.createSteelAnvil(RailcraftBlocks.STEEL_ANVIL.get());
    this.createSteelAnvil(RailcraftBlocks.CHIPPED_STEEL_ANVIL.get());
    this.createSteelAnvil(RailcraftBlocks.DAMAGED_STEEL_ANVIL.get());

    this.createCubeColumnBlock(RailcraftBlocks.FEED_STATION.get());
    this.createCubeColumnBlock(RailcraftBlocks.WATER_TANK_SIDING.get());
    this.createCubeTopBottomBlock(RailcraftBlocks.MANUAL_ROLLING_MACHINE.get());
    this.createCubeTopBottomBlock(RailcraftBlocks.POWERED_ROLLING_MACHINE.get());

    this.createFluidManipulator(RailcraftBlocks.FLUID_LOADER.get());
    this.createFluidManipulator(RailcraftBlocks.FLUID_UNLOADER.get());
    this.createManipulator(RailcraftBlocks.ITEM_LOADER.get());
    this.createManipulator(RailcraftBlocks.ITEM_UNLOADER.get());
    this.createDirectionalManipulator(RailcraftBlocks.ADVANCED_ITEM_LOADER.get());
    this.createDirectionalManipulator(RailcraftBlocks.ADVANCED_ITEM_UNLOADER.get());
    this.createDirectionalManipulator(RailcraftBlocks.CART_DISPENSER.get());
    this.createDirectionalManipulator(RailcraftBlocks.TRAIN_DISPENSER.get());

    this.createFirebox(RailcraftBlocks.SOLID_FUELED_FIREBOX.get());
    this.createFirebox(RailcraftBlocks.FLUID_FUELED_FIREBOX.get());
    this.createFurnaceMultiblockBricks(RailcraftBlocks.COKE_OVEN_BRICKS.get());
    this.createFurnaceMultiblockBricks(RailcraftBlocks.BLAST_FURNACE_BRICKS.get());
    this.createCrusherMultiblockBricks(RailcraftBlocks.CRUSHER.get());
    this.createSteamBoilerTank(RailcraftBlocks.LOW_PRESSURE_STEAM_BOILER_TANK.get());
    this.createSteamBoilerTank(RailcraftBlocks.HIGH_PRESSURE_STEAM_BOILER_TANK.get());

    this.createSteamTurbine(RailcraftBlocks.STEAM_TURBINE.get());

    this.createElevatorTrack(RailcraftBlocks.ELEVATOR_TRACK.get());
    this.createForceTrack(RailcraftBlocks.FORCE_TRACK.get());
    this.createForceTrackEmitter(RailcraftBlocks.FORCE_TRACK_EMITTER.get());


    // Not put in the constructor!
    this.activatorTrackModels = this.createTrackModelSet("activator_track");
    this.activeActivatorTrackModels = this.createActiveTrackModelSet("activator_track");
    this.boosterTrackModels = this.createTrackModelSet("booster_track");
    this.activeBoosterTrackModels = this.createActiveTrackModelSet("booster_track");
    this.embarkingTrack = this.createTrackModelSet("embarking_track");
    this.activeEmbarkingTrack = this.createActiveTrackModelSet("embarking_track");
    this.launcherTrackModels = this.createTrackModelSet("launcher_track");
    this.activeLauncherTrackModels = this.createActiveTrackModelSet("launcher_track");
    this.oneWayTrackModels = this.createTrackModelSet("one_way_track");
    this.activeOneWayTrackModels = this.createActiveTrackModelSet("one_way_track");
    this.routingTrackModels = this.createTrackModelSet("routing_track");
    this.activeRoutingTrackModels = this.createActiveTrackModelSet("routing_track");
    this.transitionTrackModels = this.createTrackModelSet("transition_track");
    this.activeTransitionTrackModels = this.createActiveTrackModelSet("transition_track");
    this.detectorTrackModels = this.createTrackModelSet("detector_track");
    this.activeDetectorTrackModels = this.createActiveTrackModelSet("detector_track");
    this.travelDetectorTrackModels = this.createTrackModelSet("detector_track_travel");
    this.activeTravelDetectorTrackModels = this.createActiveTrackModelSet("detector_track_travel");
    this.locomotiveTrackShutdownModel = this.createTrackModelSet("locomotive_track_shutdown");
    this.locomotiveTrackIdleModel = this.createTrackModelSet("locomotive_track_idle");
    this.locomotiveTrackRunningModel = this.createTrackModelSet("locomotive_track_running");
    this.activeLocomotiveTrackShutdownModel =
        this.createActiveTrackModelSet("locomotive_track_shutdown");
    this.activeLocomotiveTrackIdleModel = this.createActiveTrackModelSet("locomotive_track_idle");
    this.activeLocomotiveTrackRunningModel =
        this.createActiveTrackModelSet("locomotive_track_running");
    this.controlTrackModels = this.createTrackModelSet("control_track");
    this.couplerTrackCoupler = this.createTrackModelSet("coupler_track_coupler");
    this.activeCouplerTrackCoupler = this.createActiveTrackModelSet("coupler_track_coupler");
    this.couplerTrackDecoupler = this.createTrackModelSet("coupler_track_decoupler");
    this.activeCouplerTrackDecoupler = this.createActiveTrackModelSet("coupler_track_decoupler");
    this.couplerTrackAutoCoupler = this.createTrackModelSet("coupler_track_auto_coupler");
    this.activeCouplerTrackAutoCoupler =
        this.createActiveTrackModelSet("coupler_track_auto_coupler");
    this.disembarkingTrackLeft = this.createTrackModelSet("disembarking_track_left");
    this.activeDisembarkingTrackLeft = this.createActiveTrackModelSet("disembarking_track_left");
    this.disembarkingTrackRight = this.createTrackModelSet("disembarking_track_right");
    this.activeDisembarkingTrackRight = this.createActiveTrackModelSet("disembarking_track_right");

    this.createAbandonedTracks(RailcraftBlocks.ABANDONED_TRACK.get(),
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
        RailcraftBlocks.ABANDONED_LOCOMOTIVE_TRACK.get(),
        RailcraftBlocks.ABANDONED_ROUTING_TRACK.get());

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
        RailcraftBlocks.IRON_LOCOMOTIVE_TRACK.get(),
        RailcraftBlocks.IRON_ROUTING_TRACK.get());

    this.createTracks(RailcraftBlocks.STRAP_IRON_TRACK.get(),
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
        RailcraftBlocks.STRAP_IRON_LOCOMOTIVE_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_ROUTING_TRACK.get());

    this.createTracks(RailcraftBlocks.REINFORCED_TRACK.get(),
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
        RailcraftBlocks.REINFORCED_LOCOMOTIVE_TRACK.get(),
        RailcraftBlocks.REINFORCED_ROUTING_TRACK.get());

    this.createTracks(RailcraftBlocks.ELECTRIC_TRACK.get(),
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
        RailcraftBlocks.ELECTRIC_LOCOMOTIVE_TRACK.get(),
        RailcraftBlocks.ELECTRIC_ROUTING_TRACK.get());

    this.createHighSpeedTracks(RailcraftBlocks.HIGH_SPEED_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_TRANSITION_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_LOCKING_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ACTIVATOR_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_BOOSTER_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_DETECTOR_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_TURNOUT_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_WYE_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_JUNCTION_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_LOCOMOTIVE_TRACK.get());

    this.createHighSpeedTracks(RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRANSITION_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCKING_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_BOOSTER_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_DETECTOR_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_TURNOUT_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_WYE_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_JUNCTION_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCOMOTIVE_TRACK.get());
  }

  private void createStrengthenedGlass(Block block) {
    var endTexture = TextureMapping.getBlockTexture(block, "_top");

    var singleModel = this.models().cubeAll(this.name(block, "_single"), endTexture)
        .renderType(CUTOUT);
    var topModel = this.models().cubeColumn(this.name(block, "_top"),
        TextureMapping.getBlockTexture(block, "_side_top"), endTexture)
        .renderType(CUTOUT);
    var centerModel = this.models().cubeColumn(this.name(block, "_center"),
        TextureMapping.getBlockTexture(block, "_side_center"), endTexture)
        .renderType(CUTOUT);
    var bottomModel = this.models().cubeColumn(this.name(block, "_bottom"),
        TextureMapping.getBlockTexture(block, "_side_bottom"), endTexture)
        .renderType(CUTOUT);

    this.getVariantBuilder(block)
        .forAllStatesExcept(blockState -> ConfiguredModel.builder()
            .modelFile(switch (blockState.getValue(AbstractStrengthenedGlassBlock.TYPE)) {
              case SINGLE -> singleModel;
              case TOP -> topModel;
              case CENTER -> centerModel;
              case BOTTOM -> bottomModel;
            }).build(), IronTankGaugeBlock.LEVEL);

    this.itemModels().withExistingParent(this.name(block),
        this.modLoc(ModelProvider.BLOCK_FOLDER + "/" + this.name(block, "_single")));
  }

  private void createFluidManipulator(FluidManipulatorBlock<?> block) {
    var texture = TextureMapping.cubeBottomTop(block);
    var model = this.models().cubeBottomTop(this.name(block), texture.get(TextureSlot.SIDE),
        texture.get(TextureSlot.BOTTOM), texture.get(TextureSlot.TOP))
        .renderType(CUTOUT);

    this.simpleBlock(block, model);

    var side = new ResourceLocation(Railcraft.ID, "block/fluid_manipulator_side_inventory");
    var bottom = TextureMapping.getBlockTexture(block, "_bottom");
    var top = TextureMapping.getBlockTexture(block, "_top");
    this.models().cubeBottomTop(this.name(block, "_inventory"), side, bottom, top);

    this.itemModels().withExistingParent(this.name(block),
        this.modLoc(ModelProvider.BLOCK_FOLDER + "/" + this.name(block, "_inventory")));
  }

  private void createManipulator(ManipulatorBlock<?> block) {
    var texture = TextureMapping.cubeBottomTop(block);
    var model = this.models().cubeBottomTop(this.name(block), texture.get(TextureSlot.SIDE),
        texture.get(TextureSlot.BOTTOM), texture.get(TextureSlot.TOP));

    this.simpleBlock(block, model);
    this.simpleBlockItem(block, model);
  }

  private void createDirectionalManipulator(ManipulatorBlock<?> block) {

    var horizontalTexture = TextureMapping.orientableCubeOnlyTop(block);
    var horizontalModel = this.models().orientable(this.name(block),
        horizontalTexture.get(TextureSlot.SIDE), horizontalTexture.get(TextureSlot.FRONT),
        horizontalTexture.get(TextureSlot.TOP));

    var side = TextureMapping.getBlockTexture(block, "_side");
    var front = TextureMapping.getBlockTexture(block, "_front");
    var top = TextureMapping.getBlockTexture(block, "_top");
    var upModel = this.models().cubeBottomTop(this.name(block, "_up"), side, top, front);
    var downModel = this.models().cubeBottomTop(this.name(block, "_down"), side, front, top);

    this.getVariantBuilder(block)
        .forAllStatesExcept(blockState -> {
          var facing = blockState.getValue(BlockStateProperties.FACING);
          int yRot = 0;

          switch (facing) {
            case SOUTH:
              yRot = 180;
              break;
            case EAST:
              yRot = 90;
              break;
            case WEST:
              yRot = 270;
              break;
            case UP:
              return ConfiguredModel.builder().modelFile(upModel).build();
            case DOWN:
              return ConfiguredModel.builder().modelFile(downModel).build();
            default:
              break;
          }

          return ConfiguredModel.builder()
              .modelFile(horizontalModel)
              .rotationX(0)
              .rotationY(yRot)
              .build();
        }, AdvancedItemLoaderBlock.POWERED);

    simpleBlockItem(block, horizontalModel);
  }

  private void createFirebox(FireboxBlock block) {
    var endTexture = TextureMapping.getBlockTexture(block, "_end");
    var sideTexture = TextureMapping.getBlockTexture(block, "_side");
    var sideLitTexture = TextureMapping.getBlockTexture(block, "_side_lit");

    var model = this.models().cubeColumn(this.name(block), sideTexture, endTexture);
    var litModel = this.models().cubeColumn(this.name(block, "_lit"), sideLitTexture, endTexture);

    this.getVariantBuilder(block)
        .forAllStates(blockState -> {
          var lit = blockState.getValue(FireboxBlock.LIT);
          return ConfiguredModel.builder().modelFile(lit ? litModel : model).build();
        });

    this.simpleBlockItem(block, model);
  }

  private void createFurnaceMultiblockBricks(FurnaceMultiblockBlock block) {
    var blockTexture = TextureMapping.getBlockTexture(block);
    var sideTexture = TextureMapping.getBlockTexture(block, "_window");
    var sideLitTexture = TextureMapping.getBlockTexture(block, "_window_lit");

    var bricksModel = this.cubeAll(block);
    var windowModel = this.models().cubeBottomTop(name(block, "_window"), sideTexture,
        blockTexture, blockTexture);
    var litWindowModel = this.models().cubeBottomTop(name(block, "_window_lit"), sideLitTexture,
        blockTexture, blockTexture);

    this.getVariantBuilder(block)
        .forAllStates(blockState -> {
          var lit = blockState.getValue(FurnaceMultiblockBlock.LIT);
          var window = blockState.getValue(FurnaceMultiblockBlock.WINDOW);
          ModelFile model;
          if (!window) {
            model = bricksModel;
          } else if (lit) {
            model = litWindowModel;
          } else {
            model = windowModel;
          }
          return ConfiguredModel.builder().modelFile(model).build();
        });

    this.simpleBlockItem(block, bricksModel);
  }

  private void createCrusherMultiblockBricks(CrusherMultiblockBlock block) {
    var topTexture = TextureMapping.getBlockTexture(block, "_top");
    var sideTexture = TextureMapping.getBlockTexture(block, "_side_exporter");
    var bottomTexture = TextureMapping.getBlockTexture(block, "_side");

    var baseModel = this.models().cubeTop(name(block), bottomTexture, topTexture);
    var outputModel = this.models().cubeBottomTop(name(block, "_exporter"), sideTexture,
        bottomTexture, topTexture);

    this.getVariantBuilder(block)
        .forAllStates(blockState -> {
          var type = blockState.getValue(CrusherMultiblockBlock.TYPE);
          var rotated = blockState.getValue(CrusherMultiblockBlock.ROTATED);
          var output = blockState.getValue(CrusherMultiblockBlock.OUTPUT);
          if (output) {
            return ConfiguredModel.builder().modelFile(outputModel).build();
          } else if (type.equals(CrusherMultiblockBlock.Type.NONE)) {
            return ConfiguredModel.builder().modelFile(baseModel).build();
          } else {
            var suffix = "_top_" + type.getSerializedName();
            var model = this.models().cubeTop(name(block, suffix), bottomTexture,
                TextureMapping.getBlockTexture(block, suffix));
            return ConfiguredModel.builder()
                .modelFile(model)
                .rotationY(rotated ? 90 : 0)
                .build();
          }
        });

    this.simpleBlockItem(block, baseModel);
  }

  private void createSteamTurbine(Block block) {
    var sideTexture = TextureMapping.getBlockTexture(block, "_side");

    this.createSteamTurbineModel(block, sideTexture, "_inventory", false);
    this.itemModels().withExistingParent(this.name(block),
        this.modLoc(ModelProvider.BLOCK_FOLDER + "/" + this.name(block, "_inventory")));

    var noneVariant = this.models().cubeAll(this.name(block, "_side"), sideTexture);

    this.getVariantBuilder(block)
        .forAllStates(blockState -> {
          var type = blockState.getValue(SteamTurbineBlock.TYPE);
          var rotated = blockState.getValue(SteamTurbineBlock.ROTATED);

          if (type == Type.NONE) {
            return ConfiguredModel.builder().modelFile(noneVariant).build();
          } else {
            var model =
                this.createSteamTurbineModel(block, sideTexture, "_" + type.getSerializedName(),
                    type != SteamTurbineBlock.Type.WINDOW);
            return ConfiguredModel.builder()
                .modelFile(model)
                .rotationY(rotated ? 90 : 0)
                .build();
          }
        });
  }

  private BlockModelBuilder createSteamTurbineModel(Block block, ResourceLocation sideTexture,
      String suffix, boolean rotated) {
    var frontTexture = TextureMapping.getBlockTexture(block, suffix);
    var parent = this.modLoc(ModelProvider.BLOCK_FOLDER + "/template_mirrored_cube");
    return this.cube(this.name(block, suffix), parent,
        sideTexture,
        sideTexture,
        rotated ? sideTexture : frontTexture,
        rotated ? sideTexture : frontTexture,
        rotated ? frontTexture : sideTexture,
        rotated ? frontTexture : sideTexture,
        sideTexture);
  }

  private void createTankValve(TankValveBlock block, BaseTankBlock wallBlock) {
    var verticalModel = this.cube(this.name(block), this.mcLoc("cube"),
        TextureMapping.getBlockTexture(block, "_top"),
        TextureMapping.getBlockTexture(block, "_top"),
        TextureMapping.getBlockTexture(wallBlock, "_side"),
        TextureMapping.getBlockTexture(wallBlock, "_side"),
        TextureMapping.getBlockTexture(wallBlock, "_side"),
        TextureMapping.getBlockTexture(wallBlock, "_side"),
        TextureMapping.getBlockTexture(wallBlock, "_top"));

    var horizontalModel = this.cube(this.name(block, "_horizontal"), this.mcLoc("cube"),
        TextureMapping.getBlockTexture(wallBlock, "_top"),
        TextureMapping.getBlockTexture(wallBlock, "_top"),
        TextureMapping.getBlockTexture(block, "_front"),
        TextureMapping.getBlockTexture(block, "_front"),
        TextureMapping.getBlockTexture(wallBlock, "_side"),
        TextureMapping.getBlockTexture(wallBlock, "_side"),
        TextureMapping.getBlockTexture(wallBlock, "_top"));

    this.getVariantBuilder(block)
        .forAllStates(blockState -> {
          var axis = blockState.getValue(BlockStateProperties.AXIS);
          return switch (axis) {
            case Y -> ConfiguredModel.builder().modelFile(verticalModel).build();
            case Z -> ConfiguredModel.builder().modelFile(horizontalModel).build();
            case X -> ConfiguredModel.builder().modelFile(horizontalModel).rotationY(90).build();
          };
        });
    this.simpleBlockItem(block, verticalModel);
  }

  private void createCubeColumnBlock(Block block) {
    var model = this.models()
        .cubeColumn(this.name(block),
            TextureMapping.getBlockTexture(block, "_side"),
            TextureMapping.getBlockTexture(block, "_top"));
    this.simpleBlock(block, model);
    this.simpleBlockItem(block, model);
  }

  private void createCubeTopBottomBlock(Block block) {
    var sideTexture = TextureMapping.getBlockTexture(block, "_side");
    var bottomTexture = TextureMapping.getBlockTexture(block, "_bottom");
    var topTexture = TextureMapping.getBlockTexture(block, "_top");
    var model =
        this.models().cubeBottomTop(this.name(block), sideTexture, bottomTexture, topTexture);
    this.simpleBlock(block, model);
    this.simpleBlockItem(block, model);
  }

  private void createPost(PostBlock block) {
    var texture = TextureMapping.defaultTexture(block).get(TextureSlot.TEXTURE);
    var postFullColumnTemplate = this.modLoc("template_post_full_column");
    var postDoubleConnectionTemplate = this.modLoc("template_post_double_connection");
    var postTopColumnTemplate = this.modLoc("template_post_top_column");
    var postSmallColumnTemplate = this.modLoc("template_post_small_column");
    var postPlatformTemplate = this.modLoc("template_post_platform");
    var postSingleConnectionTemplate = this.modLoc("template_post_single_connection");
    var postInventoryTemplate = this.modLoc("post_inventory");

    var fullColumnModel = this.models()
        .singleTexture(this.name(block, "_full_column"), postFullColumnTemplate, texture);
    var doubleConnectionModel = this.models()
        .singleTexture(this.name(block, "_double_connection"), postDoubleConnectionTemplate,
            texture);
    var topColumnModel = this.models()
        .singleTexture(this.name(block, "_top_column"), postTopColumnTemplate, texture);
    var smallColumnModel = this.models()
        .singleTexture(this.name(block, "_small_column"), postSmallColumnTemplate, texture);
    var platformModel = this.models()
        .singleTexture(this.name(block, "_platform"), postPlatformTemplate, texture);
    var singleConnectionModel = this.models()
        .singleTexture(this.name(block, "_single_connection"), postSingleConnectionTemplate,
            texture);
    var inventoryModel = this.models()
        .singleTexture(this.name(block, "_inventory"), postInventoryTemplate, texture);

    this.itemModels().withExistingParent(this.name(block), inventoryModel.getLocation());

    this.getMultipartBuilder(block)
        .part()
        .modelFile(platformModel).addModel()
        .condition(PostBlock.COLUMN, Column.PLATFORM).end()
        .part()
        .modelFile(topColumnModel).addModel()
        .condition(PostBlock.COLUMN, Column.TOP).end()
        .part()
        .modelFile(smallColumnModel).addModel()
        .condition(PostBlock.COLUMN, Column.SMALL).end()
        .part()
        .modelFile(fullColumnModel).addModel()
        .condition(PostBlock.COLUMN, Column.FULL).end()
        .part()
        .modelFile(singleConnectionModel).uvLock(true).addModel()
        .condition(PostBlock.NORTH, Connection.SINGLE).end()
        .part()
        .modelFile(doubleConnectionModel).uvLock(true).addModel()
        .condition(PostBlock.NORTH, Connection.DOUBLE).end()
        .part()
        .modelFile(singleConnectionModel).uvLock(true).rotationY(180).addModel()
        .condition(PostBlock.SOUTH, Connection.SINGLE).end()
        .part()
        .modelFile(doubleConnectionModel).uvLock(true).rotationY(180).addModel()
        .condition(PostBlock.SOUTH, Connection.DOUBLE).end()
        .part()
        .modelFile(singleConnectionModel).uvLock(true).rotationY(90).addModel()
        .condition(PostBlock.EAST, Connection.SINGLE).end()
        .part()
        .modelFile(doubleConnectionModel).uvLock(true).rotationY(90).addModel()
        .condition(PostBlock.EAST, Connection.DOUBLE).end()
        .part()
        .modelFile(singleConnectionModel).uvLock(true).rotationY(270).addModel()
        .condition(PostBlock.WEST, Connection.SINGLE).end()
        .part()
        .modelFile(doubleConnectionModel).uvLock(true).rotationY(270).addModel()
        .condition(PostBlock.WEST, Connection.DOUBLE).end();
  }

  private void createSteamBoilerTank(SteamBoilerTankBlock block) {
    var end = TextureMapping.getBlockTexture(block, "_end");
    var side = TextureMapping.getBlockTexture(block, "_side");

    var steamBoilerTemplate = this.modLoc("template_steam_boiler_tank");
    var steamBoilerNETemplate = this.modLoc("template_steam_boiler_tank_ne");
    var steamBoilerNEWTemplate = this.modLoc("template_steam_boiler_tank_new");
    var steamBoilerNSETemplate = this.modLoc("template_steam_boiler_tank_nse");
    var steamBoilerNSWTemplate = this.modLoc("template_steam_boiler_tank_nsw");
    var steamBoilerNWTemplate = this.modLoc("template_steam_boiler_tank_nw");
    var steamBoilerSETemplate = this.modLoc("template_steam_boiler_tank_se");
    var steamBoilerSEWTemplate = this.modLoc("template_steam_boiler_tank_sew");
    var steamBoilerSWTemplate = this.modLoc("template_steam_boiler_tank_sw");

    var model = this.sideEnd(this.name(block), steamBoilerTemplate, side, end);
    var allModel = this.models().cubeColumn(this.name(block, "_all"), side, end);
    var northEastModel = this.sideEnd(this.name(block, "_ne"), steamBoilerNETemplate, side, end);
    var northEastWestModel =
        this.sideEnd(this.name(block, "_new"), steamBoilerNEWTemplate, side, end);
    var northSouthEastModel =
        this.sideEnd(this.name(block, "_nse"), steamBoilerNSETemplate, side, end);
    var northSouthWestModel =
        this.sideEnd(this.name(block, "_nsw"), steamBoilerNSWTemplate, side, end);
    var northWestModel = this.sideEnd(this.name(block, "_nw"), steamBoilerNWTemplate, side, end);
    var southEastModel = this.sideEnd(this.name(block, "_se"), steamBoilerSETemplate, side, end);
    var southEastWestModel =
        this.sideEnd(this.name(block, "_sew"), steamBoilerSEWTemplate, side, end);
    var southWestModel = this.sideEnd(this.name(block, "_sw"), steamBoilerSWTemplate, side, end);

    this.getVariantBuilder(block)
        .forAllStates(blockState -> {
          var type = blockState.getValue(SteamBoilerTankBlock.CONNECTION_TYPE);
          return ConfiguredModel.builder().modelFile(switch (type) {
            case ALL -> allModel;
            case NONE -> model;
            case NORTH_EAST -> northEastModel;
            case SOUTH_EAST -> southEastModel;
            case SOUTH_WEST -> southWestModel;
            case NORTH_WEST -> northWestModel;
            case NORTH_SOUTH_EAST -> northSouthEastModel;
            case SOUTH_EAST_WEST -> southEastWestModel;
            case NORTH_EAST_WEST -> northEastWestModel;
            case NORTH_SOUTH_WEST -> northSouthWestModel;
          }).build();
        });

    simpleBlockItem(block, model);
  }

  private void createSteelAnvil(AnvilBlock block) {
    var base = TextureMapping.getBlockTexture(RailcraftBlocks.STEEL_ANVIL.get());
    var top = TextureMapping.getBlockTexture(block, "_top");

    var template = mcLoc("template_anvil");
    var model = this.models().withExistingParent(name(block), template)
        .texture("top", top)
        .texture("body", base)
        .texture("particle", base);
    this.horizontalBlock(block, model, 0);

    this.simpleBlockItem(block, model);
  }

  public void fluidBlock(LiquidBlock block) {
    var model = this.models().withExistingParent(this.name(block), this.mcLoc("water"));
    this.simpleBlock(block, model);
  }

  private void createElevatorTrack(ElevatorTrackBlock block) {
    var texture = TextureMapping.defaultTexture(block).get(TextureSlot.TEXTURE);
    var textureOn = TextureMapping.getBlockTexture(block, "_on");
    var template = this.modLoc("template_elevator_track");

    var model = this.models()
        .singleTexture(this.name(block), template, texture)
        .renderType(CUTOUT);
    var activeModel = this.models()
        .singleTexture(this.name(block, "_on"), template, textureOn)
        .renderType(CUTOUT);

    this.getVariantBuilder(block)
        .forAllStates(blockState -> {
          var powered = blockState.getValue(ElevatorTrackBlock.POWERED);
          var facing = blockState.getValue(ElevatorTrackBlock.FACING);

          var yRot = switch (facing) {
            case SOUTH -> 180;
            case EAST -> 90;
            case WEST -> 270;
            default -> 0;
          };
          return ConfiguredModel.builder()
              .modelFile(powered ? activeModel : model)
              .rotationY(yRot)
              .build();
        });

    this.basicItem(block);
  }

  private void createForceTrack(ForceTrackBlock block) {
    var texture = TextureMapping.getBlockTexture(block);
    var template = this.modLoc("template_force_track");

    var model = this.models()
        .singleTexture(this.name(block), template, "rail", texture)
        .renderType(CUTOUT);

    this.getVariantBuilder(block)
        .partialState()
        .with(ForceTrackBlock.SHAPE, RailShape.NORTH_SOUTH)
        .setModels(ConfiguredModel.builder().modelFile(model).build())
        .partialState()
        .with(ForceTrackBlock.SHAPE, RailShape.EAST_WEST)
        .setModels(ConfiguredModel.builder().modelFile(model).rotationY(90).build());
  }

  private void createForceTrackEmitter(ForceTrackEmitterBlock block) {
    var front = TextureMapping.getBlockTexture(block, "_facing");
    var side = TextureMapping.getBlockTexture(block, "_side");
    var frontColored = TextureMapping.getBlockTexture(block, "_facing_colored");
    var sideColored = TextureMapping.getBlockTexture(block, "_side_colored");

    var frontUnpowered = TextureMapping.getBlockTexture(block, "_facing_unpowered");
    var sideUnpowered = TextureMapping.getBlockTexture(block, "_side_unpowered");
    var frontColoredUnpowered = TextureMapping.getBlockTexture(block,
        "_facing_unpowered_colored");
    var sideColoredUnpowered = TextureMapping.getBlockTexture(block, "_side_unpowered_colored");

    var template = this.modLoc("force_track_emitter");

    var modelUnpowered = this.models()
        .withExistingParent(this.name(block, "_unpowered"), template)
        .texture("front", frontUnpowered)
        .texture("side", sideUnpowered)
        .texture("colored_front", frontColoredUnpowered)
        .texture("colored_side", sideColoredUnpowered)
        .texture("particle", sideUnpowered)
        .renderType(CUTOUT);

    var modelPowered = this.models()
        .withExistingParent(this.name(block, "_powered"), template)
        .texture("front", front)
        .texture("side", side)
        .texture("colored_front", frontColored)
        .texture("colored_side", sideColored)
        .texture("particle", side)
        .renderType(CUTOUT);

    horizontalBlockPropertyIgnore(block, blockState -> {
      var powered = blockState.getValue(ForceTrackEmitterBlock.POWERED);
      return powered ? modelPowered : modelUnpowered;
    }, ForceTrackEmitterBlock.COLOR);
  }

  private void createTurnoutTrack(TurnoutTrackBlock block) {
    var northTexture = TextureMapping.getBlockTexture(block, "_north");
    var northSwitchedTexture = TextureMapping.getBlockTexture(block, "_north_switched");
    var southTexture = TextureMapping.getBlockTexture(block, "_south");
    var southSwitchedTexture = TextureMapping.getBlockTexture(block, "_south_switched");

    var northModel = this.models()
        .singleTexture(this.name(block, "_north"), FLAT_TEMPLATE, "rail", northTexture)
        .renderType(CUTOUT);
    var northSwitchedModel = this.models()
        .singleTexture(this.name(block, "_north_switched"), FLAT_TEMPLATE, "rail",
            northSwitchedTexture)
        .renderType(CUTOUT);
    var southModel = this.models()
        .singleTexture(this.name(block, "_south"), FLAT_TEMPLATE, "rail", southTexture)
        .renderType(CUTOUT);
    var southSwitchedModel = this.models()
        .singleTexture(this.name(block, "_south_switched"), FLAT_TEMPLATE, "rail",
            southSwitchedTexture)
        .renderType(CUTOUT);

    getVariantBuilder(block)
        .forAllStatesExcept(blockState -> {
          var shape = blockState.getValue(SwitchTrackBlock.SHAPE);
          var reversed = blockState.getValue(ReversibleOutfittedTrackBlock.REVERSED);
          var mirrored = blockState.getValue(TurnoutTrackBlock.MIRRORED);
          var switched = blockState.getValue(SwitchTrackBlock.SWITCHED);

          if (shape == RailShape.NORTH_SOUTH) {
            if (!reversed && !mirrored && !switched) {
              return ConfiguredModel.builder().modelFile(northModel).build();
            } else if (!reversed && !mirrored && switched) {
              return ConfiguredModel.builder().modelFile(northSwitchedModel).build();
            } else if (reversed && !mirrored && !switched) {
              return ConfiguredModel.builder().modelFile(northModel).rotationY(180).build();
            } else if (reversed && !mirrored && switched) {
              return ConfiguredModel.builder().modelFile(northSwitchedModel).rotationY(180).build();
            } else if (!reversed && mirrored && !switched) {
              return ConfiguredModel.builder().modelFile(southModel).rotationY(180).build();
            } else if (!reversed && mirrored && switched) {
              return ConfiguredModel.builder().modelFile(southSwitchedModel).rotationY(180).build();
            } else if (reversed && mirrored && !switched) {
              return ConfiguredModel.builder().modelFile(southModel).build();
            } else {
              return ConfiguredModel.builder().modelFile(southSwitchedModel).build();
            }
          } else if (shape == RailShape.EAST_WEST) {
            if (!reversed && !mirrored && !switched) {
              return ConfiguredModel.builder().modelFile(northModel).rotationY(90).build();
            } else if (!reversed && !mirrored && switched) {
              return ConfiguredModel.builder().modelFile(northSwitchedModel).rotationY(90).build();
            } else if (reversed && !mirrored && !switched) {
              return ConfiguredModel.builder().modelFile(northModel).rotationY(270).build();
            } else if (reversed && !mirrored && switched) {
              return ConfiguredModel.builder().modelFile(northSwitchedModel).rotationY(270).build();
            } else if (!reversed && mirrored && !switched) {
              return ConfiguredModel.builder().modelFile(southModel).rotationY(270).build();
            } else if (!reversed && mirrored && switched) {
              return ConfiguredModel.builder().modelFile(southSwitchedModel).rotationY(270).build();
            } else if (reversed && mirrored && !switched) {
              return ConfiguredModel.builder().modelFile(southModel).rotationY(90).build();
            } else {
              return ConfiguredModel.builder().modelFile(southSwitchedModel).rotationY(90).build();
            }
          }
          return ConfiguredModel.builder().build();
        }, TurnoutTrackBlock.WATERLOGGED);

    basicItem(block, "_north");
  }

  private void createWyeTrack(WyeTrackBlock block) {
    var eastTexture = TextureMapping.getBlockTexture(block, "_east");
    var eastSwitchedTexture = TextureMapping.getBlockTexture(block, "_east_switched");
    var westTexture = TextureMapping.getBlockTexture(block, "_west");
    var westSwitchedTexture = TextureMapping.getBlockTexture(block, "_west_switched");

    var eastModel = this.models()
        .singleTexture(this.name(block, "_east"), FLAT_TEMPLATE, "rail", eastTexture)
        .renderType(CUTOUT);
    var eastSwitchedModel = this.models()
        .singleTexture(this.name(block, "_east_switched"), FLAT_TEMPLATE, "rail",
            eastSwitchedTexture)
        .renderType(CUTOUT);
    var westModel = this.models()
        .singleTexture(this.name(block, "_west"), FLAT_TEMPLATE, "rail", westTexture)
        .renderType(CUTOUT);
    var westSwitchedModel = this.models()
        .singleTexture(this.name(block, "_west_switched"), FLAT_TEMPLATE, "rail",
            westSwitchedTexture)
        .renderType(CUTOUT);


    getVariantBuilder(block)
        .forAllStatesExcept(blockState -> {
          var railShape = blockState.getValue(SwitchTrackBlock.SHAPE);
          var reversed = blockState.getValue(ReversibleOutfittedTrackBlock.REVERSED);
          var switched = blockState.getValue(SwitchTrackBlock.SWITCHED);

          var facing = ReversibleOutfittedTrackBlock.getDirection(railShape, reversed);
          return switch (facing) {
            case NORTH -> ConfiguredModel.builder()
                .modelFile(switched ? eastSwitchedModel : eastModel)
                .rotationY(90)
                .build();
            case SOUTH -> ConfiguredModel.builder()
                .modelFile(switched ? westSwitchedModel : westModel)
                .rotationY(90)
                .build();
            case EAST -> ConfiguredModel.builder()
                .modelFile(switched ? westSwitchedModel : westModel)
                .build();
            case WEST -> ConfiguredModel.builder()
                .modelFile(switched ? eastSwitchedModel : eastModel)
                .build();
            default -> throw new UnsupportedOperationException();
          };
        }, WyeTrackBlock.WATERLOGGED);
    this.basicItem(block, "_east");
  }

  private void createJunctionTrack(JunctionTrackBlock block) {
    var model = this.models().withExistingParent(this.name(block), FLAT_TEMPLATE)
        .texture("rail", TextureMapping.getBlockTexture(block))
        .renderType(CUTOUT);
    this.simpleBlock(block, model);
    this.basicItem(block);
  }

  private void createFlexTrack(TrackBlock block) {
    var blockTexture = TextureMapping.getBlockTexture(block);
    var cornerTexture = TextureMapping.getBlockTexture(block, "_corner");

    var flatModel = this.models()
        .singleTexture(this.name(block), FLAT_TEMPLATE, "rail", blockTexture)
        .renderType(CUTOUT);
    var cornerModel = this.models()
        .singleTexture(this.name(block, "_corner"), CORNER_TEMPLATE, "rail", cornerTexture)
        .renderType(CUTOUT);
    var raisedNorthEastModel = this.models()
        .singleTexture(this.name(block, "_raised_ne"), RAISED_NE_TEMPLATE, "rail", blockTexture)
        .renderType(CUTOUT);
    var raisedSouthWestModel = this.models()
        .singleTexture(this.name(block, "_raised_sw"), RAISED_SW_TEMPLATE, "rail", blockTexture)
        .renderType(CUTOUT);

    this.getVariantBuilder(block)
        .forAllStatesExcept(blockState -> {
          var shape = blockState.getValue(BlockStateProperties.RAIL_SHAPE);
          int rotY = 0;
          BlockModelBuilder model = null;
          switch (shape) {
            case NORTH_SOUTH -> model = flatModel;
            case EAST_WEST -> {
              model = flatModel;
              rotY = 90;
            }
            case ASCENDING_EAST -> {
              model = raisedNorthEastModel;
              rotY = 90;
            }
            case ASCENDING_WEST -> {
              model = raisedSouthWestModel;
              rotY = 90;
            }
            case ASCENDING_NORTH -> model = raisedNorthEastModel;
            case ASCENDING_SOUTH -> model = raisedSouthWestModel;
            case SOUTH_EAST -> model = cornerModel;
            case SOUTH_WEST -> {
              model = cornerModel;
              rotY = 90;
            }
            case NORTH_WEST -> {
              model = cornerModel;
              rotY = 180;
            }
            case NORTH_EAST -> {
              model = cornerModel;
              rotY = 270;
            }
          }
          return ConfiguredModel.builder().modelFile(model).rotationY(rotY).build();
        }, BlockStateProperties.WATERLOGGED);

    basicItem(block);
  }

  private void createAbandonedFlexTrack(TrackBlock block) {
    var texture0 = TextureMapping.getBlockTexture(block, "_0");
    var texture1 = TextureMapping.getBlockTexture(block, "_1");
    var cornerTexture = TextureMapping.getBlockTexture(block, "_corner");

    var flatModel0 = this.models()
        .singleTexture(this.name(block, "_0"), FLAT_TEMPLATE, "rail", texture0)
        .renderType(CUTOUT);
    var flatModel1 = this.models()
        .singleTexture(this.name(block, "_1"), FLAT_TEMPLATE, "rail", texture1)
        .renderType(CUTOUT);
    var cornerModel = this.models()
        .singleTexture(this.name(block, "_corner"), CORNER_TEMPLATE, "rail", cornerTexture)
        .renderType(CUTOUT);
    var raisedNorthEastModel = this.models()
        .singleTexture(this.name(block, "_raised_ne"), RAISED_NE_TEMPLATE, "rail", texture0)
        .renderType(CUTOUT);
    var raisedSouthWestModel = this.models()
        .singleTexture(this.name(block, "_raised_sw"), RAISED_SW_TEMPLATE, "rail", texture0)
        .renderType(CUTOUT);

    this.getMultipartBuilder(block)
        .part()
        .modelFile(this.models().getExistingFile(this.mcLoc("block/grass"))).addModel()
        .condition(AbandonedTrackBlock.GRASS, true).end()
        .part()
        .modelFile(flatModel0).nextModel().modelFile(flatModel1).addModel()
        .condition(BlockStateProperties.RAIL_SHAPE, RailShape.NORTH_SOUTH).end()
        .part()
        .modelFile(flatModel0).rotationY(90).nextModel().modelFile(flatModel1).rotationY(90)
        .addModel()
        .condition(BlockStateProperties.RAIL_SHAPE, RailShape.EAST_WEST).end()
        .part()
        .modelFile(raisedNorthEastModel).rotationY(90).addModel()
        .condition(BlockStateProperties.RAIL_SHAPE, RailShape.ASCENDING_EAST).end()
        .part()
        .modelFile(raisedSouthWestModel).rotationY(90).addModel()
        .condition(BlockStateProperties.RAIL_SHAPE, RailShape.ASCENDING_WEST).end()
        .part()
        .modelFile(raisedNorthEastModel).addModel()
        .condition(BlockStateProperties.RAIL_SHAPE, RailShape.ASCENDING_NORTH).end()
        .part()
        .modelFile(raisedSouthWestModel).addModel()
        .condition(BlockStateProperties.RAIL_SHAPE, RailShape.ASCENDING_SOUTH).end()
        .part()
        .modelFile(cornerModel).addModel()
        .condition(BlockStateProperties.RAIL_SHAPE, RailShape.SOUTH_EAST).end()
        .part()
        .modelFile(cornerModel).rotationY(90).addModel()
        .condition(BlockStateProperties.RAIL_SHAPE, RailShape.SOUTH_WEST).end()
        .part()
        .modelFile(cornerModel).rotationY(180).addModel()
        .condition(BlockStateProperties.RAIL_SHAPE, RailShape.NORTH_WEST).end()
        .part()
        .modelFile(cornerModel).rotationY(270).addModel()
        .condition(BlockStateProperties.RAIL_SHAPE, RailShape.NORTH_EAST).end();

    this.basicItem(block, "_0");
  }

  private void createAbandonedTracks(TrackBlock block, LockingTrackBlock lockingTrackBlock,
      BufferStopTrackBlock bufferStopTrackBlock, ActivatorTrackBlock activatorTrackBlock,
      BoosterTrackBlock boosterTrackBlock, ControlTrackBlock controlTrackBlock,
      GatedTrackBlock gatedTrackBlock, DetectorTrackBlock detectorTrackBlock,
      CouplerTrackBlock couplerTrackBlock, EmbarkingTrackBlock embarkingTrackBlock,
      DisembarkingTrackBlock disembarkingTrackBlock, TurnoutTrackBlock turnoutTrackBlock,
      WyeTrackBlock wyeTrackBlock, JunctionTrackBlock junctionTrackBlock,
      LauncherTrackBlock launcherTrackBlock, OneWayTrackBlock oneWayTrackBlock,
      LocomotiveTrackBlock locomotiveTrackBlock, RoutingTrackBlock routingTrackBlock) {
    this.createAbandonedFlexTrack(block);
    this.createOutfittedTracks(block, lockingTrackBlock, bufferStopTrackBlock, activatorTrackBlock,
        boosterTrackBlock, controlTrackBlock, gatedTrackBlock, detectorTrackBlock,
        couplerTrackBlock, embarkingTrackBlock, disembarkingTrackBlock, turnoutTrackBlock,
        wyeTrackBlock, junctionTrackBlock, launcherTrackBlock, oneWayTrackBlock,
        locomotiveTrackBlock, routingTrackBlock);
  }

  private void createTracks(TrackBlock block, LockingTrackBlock lockingTrackBlock,
      BufferStopTrackBlock bufferStopTrackBlock, ActivatorTrackBlock activatorTrackBlock,
      BoosterTrackBlock boosterTrackBlock, ControlTrackBlock controlTrackBlock,
      GatedTrackBlock gatedTrackBlock, DetectorTrackBlock detectorTrackBlock,
      CouplerTrackBlock couplerTrackBlock, EmbarkingTrackBlock embarkingTrackBlock,
      DisembarkingTrackBlock disembarkingTrackBlock, TurnoutTrackBlock turnoutTrackBlock,
      WyeTrackBlock wyeTrackBlock, JunctionTrackBlock junctionTrackBlock,
      LauncherTrackBlock launcherTrackBlock, OneWayTrackBlock oneWayTrackBlock,
      LocomotiveTrackBlock locomotiveTrackBlock, RoutingTrackBlock routingTrackBlock) {
    this.createFlexTrack(block);
    this.createOutfittedTracks(block, lockingTrackBlock, bufferStopTrackBlock, activatorTrackBlock,
        boosterTrackBlock, controlTrackBlock, gatedTrackBlock, detectorTrackBlock,
        couplerTrackBlock, embarkingTrackBlock, disembarkingTrackBlock, turnoutTrackBlock,
        wyeTrackBlock, junctionTrackBlock, launcherTrackBlock, oneWayTrackBlock,
        locomotiveTrackBlock, routingTrackBlock);
  }

  private void createOutfittedTracks(Block block, LockingTrackBlock lockingTrackBlock,
      BufferStopTrackBlock bufferStopTrackBlock, ActivatorTrackBlock activatorTrackBlock,
      BoosterTrackBlock boosterTrackBlock, ControlTrackBlock controlTrackBlock,
      GatedTrackBlock gatedTrackBlock, DetectorTrackBlock detectorTrackBlock,
      CouplerTrackBlock couplerTrackBlock, EmbarkingTrackBlock embarkingTrackBlock,
      DisembarkingTrackBlock disembarkingTrackBlock, TurnoutTrackBlock turnoutTrackBlock,
      WyeTrackBlock wyeTrackBlock, JunctionTrackBlock junctionTrackBlock,
      LauncherTrackBlock launcherTrackBlock, OneWayTrackBlock oneWayTrackBlock,
      LocomotiveTrackBlock locomotiveTrackBlock, RoutingTrackBlock routingTrackBlock) {
    var outfittedTrackModels = this.createOutfittedTrackModelSet(block);

    this.createActiveOutfittedTrack(activatorTrackBlock, true, false, outfittedTrackModels,
        this.activatorTrackModels, this.activeActivatorTrackModels);
    this.createActiveOutfittedTrack(boosterTrackBlock, true, false, outfittedTrackModels,
        this.boosterTrackModels, this.activeBoosterTrackModels);
    this.createActiveOutfittedTrack(embarkingTrackBlock, true, false, outfittedTrackModels,
        this.embarkingTrack, this.activeEmbarkingTrack);
    this.createActiveOutfittedTrack(launcherTrackBlock, false, false, outfittedTrackModels,
        this.launcherTrackModels, this.activeLauncherTrackModels);
    this.createActiveOutfittedTrack(oneWayTrackBlock, false, true, outfittedTrackModels,
        this.oneWayTrackModels, this.activeOneWayTrackModels);
    this.createActiveOutfittedTrack(routingTrackBlock, true, false, outfittedTrackModels,
        this.routingTrackModels, this.activeRoutingTrackModels);

    this.createDetectorTrack(detectorTrackBlock, outfittedTrackModels,
        this.detectorTrackModels,
        this.activeDetectorTrackModels,
        this.travelDetectorTrackModels,
        this.activeTravelDetectorTrackModels);

    this.createLocomotiveTrack(locomotiveTrackBlock, outfittedTrackModels,
        this.locomotiveTrackShutdownModel, this.activeLocomotiveTrackShutdownModel,
        this.locomotiveTrackIdleModel, this.activeLocomotiveTrackIdleModel,
        this.locomotiveTrackRunningModel, this.activeLocomotiveTrackRunningModel);

    this.createControlTrack(controlTrackBlock, outfittedTrackModels, this.controlTrackModels);
    this.createGatedTrack(gatedTrackBlock, outfittedTrackModels, this.controlTrackModels);
    this.createCouplerTrack(couplerTrackBlock, outfittedTrackModels,
        this.couplerTrackCoupler,
        this.activeCouplerTrackCoupler,
        this.couplerTrackDecoupler,
        this.activeCouplerTrackDecoupler,
        this.couplerTrackAutoCoupler,
        this.activeCouplerTrackAutoCoupler);
    this.createDisembarkingTrack(disembarkingTrackBlock, outfittedTrackModels,
        this.disembarkingTrackLeft,
        this.activeDisembarkingTrackLeft,
        this.disembarkingTrackRight,
        this.activeDisembarkingTrackRight);

    this.createLockingTrack(lockingTrackBlock, outfittedTrackModels.flatModel());
    this.createBufferStopTrack(bufferStopTrackBlock, outfittedTrackModels.flatModel());
    this.createTurnoutTrack(turnoutTrackBlock);
    this.createWyeTrack(wyeTrackBlock);
    this.createJunctionTrack(junctionTrackBlock);
  }

  private void createHighSpeedTracks(TrackBlock block, TransitionTrackBlock transitionTrackBlock,
      LockingTrackBlock lockingTrackBlock, ActivatorTrackBlock activatorTrackBlock,
      BoosterTrackBlock boosterTrackBlock, DetectorTrackBlock detectorTrackBlock,
      TurnoutTrackBlock turnoutTrackBlock, WyeTrackBlock wyeTrackBlock,
      JunctionTrackBlock junctionTrackBlock, LocomotiveTrackBlock locomotiveTrackBlock) {
    this.createFlexTrack(block);
    var outfittedTrackModels = this.createOutfittedTrackModelSet(block);

    this.createActiveOutfittedTrack(activatorTrackBlock, true, false, outfittedTrackModels,
        this.activatorTrackModels, this.activeActivatorTrackModels);
    this.createActiveOutfittedTrack(boosterTrackBlock, true, false, outfittedTrackModels,
        this.boosterTrackModels, this.activeBoosterTrackModels);
    this.createActiveOutfittedTrack(transitionTrackBlock, true, true, outfittedTrackModels,
        this.transitionTrackModels, this.activeTransitionTrackModels);

    this.createDetectorTrack(detectorTrackBlock, outfittedTrackModels,
        this.detectorTrackModels,
        this.activeDetectorTrackModels,
        this.travelDetectorTrackModels,
        this.activeTravelDetectorTrackModels);

    this.createLocomotiveTrack(locomotiveTrackBlock, outfittedTrackModels,
        this.locomotiveTrackShutdownModel,
        this.activeLocomotiveTrackShutdownModel,
        this.locomotiveTrackIdleModel,
        this.activeLocomotiveTrackIdleModel,
        this.locomotiveTrackRunningModel,
        this.activeLocomotiveTrackRunningModel);

    this.createLockingTrack(lockingTrackBlock, outfittedTrackModels.flatModel());
    this.createTurnoutTrack(turnoutTrackBlock);
    this.createWyeTrack(wyeTrackBlock);
    this.createJunctionTrack(junctionTrackBlock);
  }

  private void createLockingTrack(LockingTrackBlock block, ModelFile trackModel) {
    var lockdownModel = this.createPassiveRail("locking_track_lockdown");
    var trainLockdownModel = this.createPassiveRail("locking_track_train_lockdown");
    var holdingModel = this.createPassiveRail("locking_track_holding");
    var trainHoldingModel = this.createPassiveRail("locking_track_train_holding");
    var boardingModel = this.createPassiveRail("locking_track_boarding");
    var boardingReversedModel = this.createPassiveRail("locking_track_boarding_reversed");
    var trainBoardingModel = this.createPassiveRail("locking_track_train_boarding");
    var trainBoardingReversedModel =
        this.createPassiveRail("locking_track_train_boarding_reversed");
    var activeLockdownModel = this.createActiveRail("locking_track_lockdown");
    var activeTrainLockdownModel = this.createActiveRail("locking_track_train_lockdown");
    var activeHoldingModel = this.createActiveRail("locking_track_holding");
    var activeTrainHoldingModel = this.createActiveRail("locking_track_train_holding");
    var activeBoardingModel = this.createActiveRail("locking_track_boarding");
    var activeBoardingReversedModel = this.createActiveRail("locking_track_boarding_reversed");
    var activeTrainBoardingModel = this.createActiveRail("locking_track_train_boarding");
    var activeTrainBoardingReversedModel =
        this.createActiveRail("locking_track_train_boarding_reversed");

    var builder = this.getMultipartBuilder(block)
        .part()
        .modelFile(trackModel).addModel()
        .condition(LockingTrackBlock.SHAPE, RailShape.NORTH_SOUTH).end()
        .part()
        .modelFile(trackModel).rotationY(90).addModel()
        .condition(LockingTrackBlock.SHAPE, RailShape.EAST_WEST).end();

    this.addLockingMode(LockingMode.LOCKDOWN, lockdownModel, activeLockdownModel, builder);
    this.addLockingMode(LockingMode.TRAIN_LOCKDOWN, trainLockdownModel, activeTrainLockdownModel,
        builder);
    this.addLockingMode(LockingMode.HOLDING, holdingModel, activeHoldingModel, builder);
    this.addLockingMode(LockingMode.TRAIN_HOLDING, trainHoldingModel, activeTrainHoldingModel,
        builder);
    this.addLockingMode(LockingMode.BOARDING, boardingModel, activeBoardingModel, builder);
    this.addLockingMode(LockingMode.BOARDING_REVERSED, boardingReversedModel,
        activeBoardingReversedModel, builder);
    this.addLockingMode(LockingMode.TRAIN_BOARDING, trainBoardingModel, activeTrainBoardingModel,
        builder);
    this.addLockingMode(LockingMode.TRAIN_BOARDING_REVERSED, trainBoardingReversedModel,
        activeTrainBoardingReversedModel, builder);

    this.itemModels().basicItem(block.asItem());
  }

  private void addLockingMode(LockingMode lockingMode, BlockModelBuilder model,
      BlockModelBuilder poweredModel, MultiPartBlockStateBuilder builder) {

    builder
        .part()
        .modelFile(model).addModel()
        .condition(LockingTrackBlock.LOCKING_MODE, lockingMode)
        .condition(LockingTrackBlock.POWERED, false)
        .condition(LockingTrackBlock.SHAPE, RailShape.NORTH_SOUTH).end()
        .part()
        .modelFile(poweredModel).addModel()
        .condition(LockingTrackBlock.LOCKING_MODE, lockingMode)
        .condition(LockingTrackBlock.POWERED, true)
        .condition(LockingTrackBlock.SHAPE, RailShape.NORTH_SOUTH).end()
        .part()
        .modelFile(model).rotationY(90).addModel()
        .condition(LockingTrackBlock.LOCKING_MODE, lockingMode)
        .condition(LockingTrackBlock.POWERED, false)
        .condition(LockingTrackBlock.SHAPE, RailShape.EAST_WEST).end()
        .part()
        .modelFile(poweredModel).rotationY(90).addModel()
        .condition(LockingTrackBlock.LOCKING_MODE, lockingMode)
        .condition(LockingTrackBlock.POWERED, true)
        .condition(LockingTrackBlock.SHAPE, RailShape.EAST_WEST).end();
  }

  private void createBufferStopTrack(BufferStopTrackBlock block, ModelFile trackModel) {
    var bufferStop = new ModelFile.UncheckedModelFile(this.modLoc("block/buffer_stop"));

    this.getMultipartBuilder(block)
        .part()
        .modelFile(bufferStop).addModel()
        .condition(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH)
        .condition(ReversibleOutfittedTrackBlock.REVERSED, false).end()
        .part()
        .modelFile(bufferStop).rotationY(180).addModel()
        .condition(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH)
        .condition(ReversibleOutfittedTrackBlock.REVERSED, true).end()
        .part()
        .modelFile(bufferStop).rotationY(90).addModel()
        .condition(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST)
        .condition(ReversibleOutfittedTrackBlock.REVERSED, false).end()
        .part()
        .modelFile(bufferStop).rotationY(270).addModel()
        .condition(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST)
        .condition(ReversibleOutfittedTrackBlock.REVERSED, true).end()
        .part()
        .modelFile(trackModel).addModel()
        .condition(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH).end()
        .part()
        .modelFile(trackModel).rotationY(90).addModel()
        .condition(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST).end();

    this.itemModels().basicItem(block.asItem());
  }

  private void createActiveOutfittedTrack(Block block, boolean allowedOnSlopes,
      boolean reversible, StraightTrackModelSet trackModels, StraightTrackModelSet trackKitModels,
      StraightTrackModelSet activeTrackKitModels) {

    var builder = this.getMultipartBuilder(block);
    trackModels.apply(builder, OutfittedTrackBlock.SHAPE, allowedOnSlopes, false);

    if (reversible) {
      trackKitModels.apply(builder, OutfittedTrackBlock.SHAPE, allowedOnSlopes, false,
          Condition.condition()
              .term(ReversibleOutfittedTrackBlock.REVERSED, false)
              .term(PoweredOutfittedTrackBlock.POWERED, false));
      activeTrackKitModels.apply(builder, OutfittedTrackBlock.SHAPE, allowedOnSlopes, false,
          Condition.condition()
              .term(ReversibleOutfittedTrackBlock.REVERSED, false)
              .term(PoweredOutfittedTrackBlock.POWERED, true));

      trackKitModels.apply(builder, OutfittedTrackBlock.SHAPE, allowedOnSlopes, true,
          Condition.condition()
              .term(ReversibleOutfittedTrackBlock.REVERSED, true)
              .term(PoweredOutfittedTrackBlock.POWERED, false));
      activeTrackKitModels.apply(builder, OutfittedTrackBlock.SHAPE, allowedOnSlopes, true,
          Condition.condition()
              .term(ReversibleOutfittedTrackBlock.REVERSED, true)
              .term(PoweredOutfittedTrackBlock.POWERED, true));
    } else {
      trackKitModels.apply(builder, OutfittedTrackBlock.SHAPE, allowedOnSlopes, false,
          Condition.condition().term(PoweredOutfittedTrackBlock.POWERED, false));
      activeTrackKitModels.apply(builder, OutfittedTrackBlock.SHAPE, allowedOnSlopes, false,
          Condition.condition().term(PoweredOutfittedTrackBlock.POWERED, true));
    }
    this.itemModels().basicItem(block.asItem());
  }

  private void createDetectorTrack(DetectorTrackBlock block, StraightTrackModelSet trackModels,
      StraightTrackModelSet detectorTrackModels, StraightTrackModelSet activeDetectorTrackModels,
      StraightTrackModelSet travelDetectorTrackModels,
      StraightTrackModelSet activeTravelDetectorTrackModels) {
    var builder = this.getMultipartBuilder(block);
    trackModels.apply(builder, DetectorTrackBlock.SHAPE, true, false);

    detectorTrackModels.apply(builder, DetectorTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.BI_DIRECTIONAL)
            .term(DetectorTrackBlock.POWERED, false));

    activeDetectorTrackModels.apply(builder, DetectorTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(DetectorTrackBlock.MODE, DetectorTrackBlock.Mode.BI_DIRECTIONAL)
            .term(DetectorTrackBlock.POWERED, true));

    travelDetectorTrackModels.apply(builder, DetectorTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(DetectorTrackBlock.MODE, Mode.TRAVEL)
            .term(DetectorTrackBlock.POWERED, false));

    activeTravelDetectorTrackModels.apply(builder, DetectorTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(DetectorTrackBlock.MODE, Mode.TRAVEL)
            .term(DetectorTrackBlock.POWERED, true));

    travelDetectorTrackModels.apply(builder, DetectorTrackBlock.SHAPE, true, true,
        Condition.condition()
            .term(DetectorTrackBlock.MODE, Mode.TRAVEL_REVERSED)
            .term(DetectorTrackBlock.POWERED, false));
    activeTravelDetectorTrackModels.apply(builder, DetectorTrackBlock.SHAPE, true, true,
        Condition.condition()
            .term(DetectorTrackBlock.MODE, Mode.TRAVEL_REVERSED)
            .term(DetectorTrackBlock.POWERED, true));
    this.itemModels().basicItem(block.asItem());
  }

  private void createLocomotiveTrack(LocomotiveTrackBlock block,
      StraightTrackModelSet trackModel, StraightTrackModelSet locomotiveTrackShutdownModel,
      StraightTrackModelSet activeLocomotiveTrackShutdownModel,
      StraightTrackModelSet locomotiveTrackIdleModel,
      StraightTrackModelSet activeLocomotiveTrackIdleModel,
      StraightTrackModelSet locomotiveTrackRunningModel,
      StraightTrackModelSet activeLocomotiveTrackRunningModel) {
    var builder = this.getMultipartBuilder(block);
    trackModel.apply(builder, LocomotiveTrackBlock.SHAPE, true, false);

    this.addLocomotiveMode(builder, Locomotive.Mode.SHUTDOWN, locomotiveTrackShutdownModel,
        activeLocomotiveTrackShutdownModel);
    this.addLocomotiveMode(builder, Locomotive.Mode.IDLE, locomotiveTrackIdleModel,
        activeLocomotiveTrackIdleModel);
    this.addLocomotiveMode(builder, Locomotive.Mode.RUNNING, locomotiveTrackRunningModel,
        activeLocomotiveTrackRunningModel);

    this.itemModels().basicItem(block.asItem());
  }

  private void addLocomotiveMode(MultiPartBlockStateBuilder builder,
      Locomotive.Mode locomotiveMode, StraightTrackModelSet model,
      StraightTrackModelSet poweredModel) {

    model.apply(builder, LocomotiveTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(LocomotiveTrackBlock.LOCOMOTIVE_MODE, locomotiveMode)
            .term(LocomotiveTrackBlock.POWERED, false));
    poweredModel.apply(builder, LocomotiveTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(LocomotiveTrackBlock.LOCOMOTIVE_MODE, locomotiveMode)
            .term(LocomotiveTrackBlock.POWERED, true));
  }

  private void createControlTrack(ControlTrackBlock block, StraightTrackModelSet trackModels,
      StraightTrackModelSet controlTrackModels) {
    var builder = this.getMultipartBuilder(block);
    trackModels.apply(builder, OutfittedTrackBlock.SHAPE, true, false);

    controlTrackModels.apply(builder, OutfittedTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(PoweredOutfittedTrackBlock.POWERED, false)
            .term(ControlTrackBlock.REVERSED, false));
    controlTrackModels.apply(builder, OutfittedTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(PoweredOutfittedTrackBlock.POWERED, true)
            .term(ControlTrackBlock.REVERSED, true));
    controlTrackModels.apply(builder, OutfittedTrackBlock.SHAPE, true, true,
        Condition.condition()
            .term(PoweredOutfittedTrackBlock.POWERED, true)
            .term(ControlTrackBlock.REVERSED, false));
    controlTrackModels.apply(builder, OutfittedTrackBlock.SHAPE, true, true,
        Condition.condition()
            .term(PoweredOutfittedTrackBlock.POWERED, false)
            .term(ControlTrackBlock.REVERSED, true));

    this.itemModels().basicItem(block.asItem());
  }

  private void createGatedTrack(GatedTrackBlock block, StraightTrackModelSet trackModels,
      StraightTrackModelSet controlTrackModels) {
    var closedGateModel = new ModelFile.UncheckedModelFile(
        ModelLocationUtils.getModelLocation(Blocks.OAK_FENCE_GATE));
    var openGateModel = new ModelFile.UncheckedModelFile(
        ModelLocationUtils.getModelLocation(Blocks.OAK_FENCE_GATE, "_open"));
    var closedWallGateModel = new ModelFile.UncheckedModelFile(
        ModelLocationUtils.getModelLocation(Blocks.OAK_FENCE_GATE, "_wall"));
    var openWallGateModel = new ModelFile.UncheckedModelFile(
        ModelLocationUtils.getModelLocation(Blocks.OAK_FENCE_GATE, "_wall_open"));

    var builder = this.getMultipartBuilder(block)
        .part()
        .modelFile(trackModels.flatModel()).addModel()
        .condition(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH).end()
        .part()
        .modelFile(trackModels.flatModel()).rotationY(90).addModel()
        .condition(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST).end()
        .part()
        .modelFile(controlTrackModels.flatModel()).addModel()
        .condition(GatedTrackBlock.ONE_WAY, true)
        .condition(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH)
        .condition(ReversibleOutfittedTrackBlock.REVERSED, false).end()
        .part()
        .modelFile(controlTrackModels.flatModel()).rotationY(180).addModel()
        .condition(GatedTrackBlock.ONE_WAY, true)
        .condition(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH)
        .condition(ReversibleOutfittedTrackBlock.REVERSED, true).end()
        .part()
        .modelFile(controlTrackModels.flatModel()).rotationY(90).addModel()
        .condition(GatedTrackBlock.ONE_WAY, true)
        .condition(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST)
        .condition(ReversibleOutfittedTrackBlock.REVERSED, false).end()
        .part()
        .modelFile(controlTrackModels.flatModel()).rotationY(270).addModel()
        .condition(GatedTrackBlock.ONE_WAY, true)
        .condition(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST)
        .condition(ReversibleOutfittedTrackBlock.REVERSED, true).end();

    this.addGateVariants(builder, false, false, closedGateModel);
    this.addGateVariants(builder, true, false, openGateModel);
    this.addGateVariants(builder, false, true, closedWallGateModel);
    this.addGateVariants(builder, true, true, openWallGateModel);

    this.itemModels().basicItem(block.asItem());
  }

  private void addGateVariants(MultiPartBlockStateBuilder builder, boolean open, boolean inWall,
      ModelFile model) {
    builder
        .part()
        .modelFile(model).rotationY(180).addModel()
        .condition(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH)
        .condition(ReversibleOutfittedTrackBlock.REVERSED, false)
        .condition(GatedTrackBlock.OPEN, open)
        .condition(GatedTrackBlock.IN_WALL, inWall).end()
        .part()
        .modelFile(model).addModel()
        .condition(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH)
        .condition(ReversibleOutfittedTrackBlock.REVERSED, true)
        .condition(GatedTrackBlock.OPEN, open)
        .condition(GatedTrackBlock.IN_WALL, inWall).end()
        .part()
        .modelFile(model).rotationY(270).addModel()
        .condition(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST)
        .condition(ReversibleOutfittedTrackBlock.REVERSED, false)
        .condition(GatedTrackBlock.OPEN, open)
        .condition(GatedTrackBlock.IN_WALL, inWall).end()
        .part()
        .modelFile(model).rotationY(90).addModel()
        .condition(OutfittedTrackBlock.SHAPE, RailShape.EAST_WEST)
        .condition(ReversibleOutfittedTrackBlock.REVERSED, true)
        .condition(GatedTrackBlock.OPEN, open)
        .condition(GatedTrackBlock.IN_WALL, inWall).end();
  }

  private void createCouplerTrack(CouplerTrackBlock block, StraightTrackModelSet trackModels,
      StraightTrackModelSet couplerTrackCoupler, StraightTrackModelSet activeCouplerTrackCoupler,
      StraightTrackModelSet couplerTrackDecoupler,
      StraightTrackModelSet activeCouplerTrackDecoupler,
      StraightTrackModelSet couplerTrackAutoCoupler,
      StraightTrackModelSet activeCouplerTrackAutoCoupler) {
    var builder = this.getMultipartBuilder(block);
    trackModels.apply(builder, CouplerTrackBlock.SHAPE, true, false);

    couplerTrackCoupler.apply(builder, CouplerTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(CouplerTrackBlock.MODE, CouplerTrackBlockEntity.Mode.COUPLER)
            .term(CouplerTrackBlock.POWERED, false));
    activeCouplerTrackCoupler.apply(builder, CouplerTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(CouplerTrackBlock.MODE, CouplerTrackBlockEntity.Mode.COUPLER)
            .term(CouplerTrackBlock.POWERED, true));
    couplerTrackDecoupler.apply(builder, CouplerTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(CouplerTrackBlock.MODE, CouplerTrackBlockEntity.Mode.DECOUPLER)
            .term(CouplerTrackBlock.POWERED, false));
    activeCouplerTrackDecoupler.apply(builder, CouplerTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(CouplerTrackBlock.MODE, CouplerTrackBlockEntity.Mode.DECOUPLER)
            .term(CouplerTrackBlock.POWERED, true));
    couplerTrackAutoCoupler.apply(builder, CouplerTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(CouplerTrackBlock.MODE, CouplerTrackBlockEntity.Mode.AUTO_COUPLER)
            .term(CouplerTrackBlock.POWERED, false));
    activeCouplerTrackAutoCoupler.apply(builder, CouplerTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(CouplerTrackBlock.MODE, CouplerTrackBlockEntity.Mode.AUTO_COUPLER)
            .term(CouplerTrackBlock.POWERED, true));

    this.itemModels().basicItem(block.asItem());
  }

  private void createDisembarkingTrack(DisembarkingTrackBlock block,
      StraightTrackModelSet trackModels, StraightTrackModelSet disembarkingTrackLeft,
      StraightTrackModelSet activeDisembarkingTrackLeft,
      StraightTrackModelSet disembarkingTrackRight,
      StraightTrackModelSet activeDisembarkingTrackRight) {
    var builder = this.getMultipartBuilder(block);
    trackModels.apply(builder, CouplerTrackBlock.SHAPE, true, false);

    disembarkingTrackLeft.apply(builder, CouplerTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(DisembarkingTrackBlock.MIRRORED, false)
            .term(PoweredOutfittedTrackBlock.POWERED, false));
    activeDisembarkingTrackLeft.apply(builder, CouplerTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(DisembarkingTrackBlock.MIRRORED, false)
            .term(PoweredOutfittedTrackBlock.POWERED, true));
    disembarkingTrackRight.apply(builder, CouplerTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(DisembarkingTrackBlock.MIRRORED, true)
            .term(PoweredOutfittedTrackBlock.POWERED, false));
    activeDisembarkingTrackRight.apply(builder, CouplerTrackBlock.SHAPE, true, false,
        Condition.condition()
            .term(DisembarkingTrackBlock.MIRRORED, true)
            .term(PoweredOutfittedTrackBlock.POWERED, true));

    this.itemModels().basicItem(block.asItem());
  }

  private BlockModelBuilder createVariant(String name, ModelTemplate model) {
    var texture = this.modLoc(ModelProvider.BLOCK_FOLDER + "/" + name);
    var modelName = name + model.suffix.orElse("");
    var parent = model.model.orElseThrow();

    return this.models()
        .singleTexture(modelName, parent, "rail", texture)
        .renderType(CUTOUT);
  }

  private BlockModelBuilder createPassiveRail(String name) {
    return this.createVariant(name, ModelTemplates.RAIL_FLAT);
  }

  private BlockModelBuilder createActiveRail(String name) {
    return this.createVariant(name + "_on", ModelTemplates.RAIL_FLAT);
  }

  private StraightTrackModelSet createOutfittedTrackModelSet(Block block) {
    return this.createTrackModelSet(this.name(block, "_outfitted"));
  }

  private StraightTrackModelSet createTrackModelSet(String name) {
    return new StraightTrackModelSet(this.createPassiveRail(name),
        this.createVariant(name, ModelTemplates.RAIL_RAISED_NE),
        this.createVariant(name, ModelTemplates.RAIL_RAISED_SW));
  }

  private StraightTrackModelSet createActiveTrackModelSet(String name) {
    return new StraightTrackModelSet(this.createActiveRail(name),
        this.createVariant(name + "_on", ModelTemplates.RAIL_RAISED_NE),
        this.createVariant(name + "_on", ModelTemplates.RAIL_RAISED_SW));
  }

  private record StraightTrackModelSet(
      ModelFile flatModel,
      ModelFile raisedNorthEastModel,
      ModelFile raisedSouthWestModel) {

    private void apply(MultiPartBlockStateBuilder builder, Property<RailShape> shapeProperty,
        boolean includeRaised, boolean reversed) {
      this.apply(builder, shapeProperty, includeRaised, reversed, null);
    }

    private static MultiPartBlockStateBuilder.PartBuilder conditions(
        MultiPartBlockStateBuilder.PartBuilder.ConditionGroup partialBuilder,
        @Nullable Condition conditions) {
      if (conditions instanceof CompositeCondition compositeCondition) {
        for (var sub : compositeCondition.subconditions) {
          conditions(partialBuilder, sub);
        }
        return partialBuilder.end();
      } else if (conditions instanceof TerminalCondition terminalCondition) {
        terminalCondition.terms.forEach((key, value) -> key.getValue(value)
            .ifPresent(v -> partialBuilder.conditions.put(key, v)));
      }
      return partialBuilder.end();
    }

    private void apply(MultiPartBlockStateBuilder builder, Property<RailShape> shapeProperty,
        boolean includeRaised, boolean reversed, @Nullable Condition conditions) {
      var partialBuilder = builder
          .part()
          .modelFile(this.flatModel).rotationY(reversed ? 180 : 0).addModel()
          .nestedGroup()
          .condition(shapeProperty, RailShape.NORTH_SOUTH);
      partialBuilder = conditions(partialBuilder, conditions).end()
          .part()
          .modelFile(this.flatModel).rotationY(reversed ? 270 : 90).addModel()
          .nestedGroup()
          .condition(shapeProperty, RailShape.EAST_WEST);
      builder = conditions(partialBuilder, conditions).end();

      if (includeRaised) {
        partialBuilder = builder
            .part()
            .modelFile(reversed ? this.raisedSouthWestModel : this.raisedNorthEastModel)
            .rotationY(reversed ? 180 : 0).addModel()
            .nestedGroup()
            .condition(shapeProperty, RailShape.ASCENDING_NORTH);
        partialBuilder = conditions(partialBuilder, conditions).end()
            .part()
            .modelFile(reversed ? this.raisedNorthEastModel : this.raisedSouthWestModel)
            .rotationY(reversed ? 180 : 0).addModel()
            .nestedGroup()
            .condition(shapeProperty, RailShape.ASCENDING_SOUTH);
        partialBuilder = conditions(partialBuilder, conditions).end()
            .part()
            .modelFile(reversed ? this.raisedSouthWestModel : this.raisedNorthEastModel)
            .rotationY(reversed ? 270 : 90).addModel()
            .nestedGroup()
            .condition(shapeProperty, RailShape.ASCENDING_EAST);
        partialBuilder = conditions(partialBuilder, conditions).end()
            .part()
            .modelFile(reversed ? this.raisedNorthEastModel : this.raisedSouthWestModel)
            .rotationY(reversed ? 270 : 90).addModel()
            .nestedGroup()
            .condition(shapeProperty, RailShape.ASCENDING_WEST);
        builder = conditions(partialBuilder, conditions).end();
      }
    }
  }
}
