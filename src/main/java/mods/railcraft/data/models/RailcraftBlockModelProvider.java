package mods.railcraft.data.models;

import static net.minecraftforge.client.model.generators.ModelProvider.BLOCK_FOLDER;

import java.util.Map.Entry;
import mods.railcraft.Railcraft;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import mods.railcraft.world.level.block.AbstractStrengthenedGlassBlock;
import mods.railcraft.world.level.block.FurnaceMultiblockBlock;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.SteamTurbineBlock;
import mods.railcraft.world.level.block.SteamTurbineBlock.Type;
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
import mods.railcraft.world.level.block.track.outfitted.DetectorTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.DetectorTrackBlock.Mode;
import mods.railcraft.world.level.block.track.outfitted.EmbarkingTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.JunctionTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.LauncherTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.LockingMode;
import mods.railcraft.world.level.block.track.outfitted.LockingTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.LocomotiveTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.OneWayTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.OutfittedTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.PoweredOutfittedTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.ReversibleOutfittedTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.SwitchTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.TransitionTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.TurnoutTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.WyeTrackBlock;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.models.blockstates.Condition;
import net.minecraft.data.models.blockstates.Condition.CompositeCondition;
import net.minecraft.data.models.blockstates.Condition.Operation;
import net.minecraft.data.models.blockstates.Condition.TerminalCondition;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder.PartBuilder;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder.PartBuilder.ConditionGroup;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class RailcraftBlockModelProvider extends BlockStateProvider {

    private static final String CUTOUT = "cutout";

    private final ResourceLocation FLAT_TEMPLATE = mcLoc("rail_flat");
    private final ResourceLocation CORNER_TEMPLATE = mcLoc("rail_curved");
    private final ResourceLocation RAISED_NE_TEMPLATE = mcLoc("template_rail_raised_ne");
    private final ResourceLocation RAISED_SW_TEMPLATE = mcLoc("template_rail_raised_sw");

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

    public RailcraftBlockModelProvider(DataGenerator generator,
        ExistingFileHelper existingFileHelper) {
        super(generator, Railcraft.ID, existingFileHelper);
    }

    private ResourceLocation key(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

    private String name(Block block) {
        return key(block).getPath();
    }
    private String name(Block block, String suffix) {
        return name(block) + suffix;
    }

    private BlockModelBuilder cube(String name, ResourceLocation parent, ResourceLocation down,
        ResourceLocation up,
        ResourceLocation north,
        ResourceLocation south, ResourceLocation east, ResourceLocation west,
        ResourceLocation particle) {
        return models().withExistingParent(name, parent)
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
        return models().withExistingParent(name, parent)
            .texture("side", side)
            .texture("end", end);
    }

    private void basicItem(Block block) {
        basicItem(block, "");
    }

    private void basicItem(Block block, String suffix) {
        itemModels().withExistingParent(name(block), "item/generated")
            .texture("layer0", modLoc("block/" + name(block) + suffix));
    }

    @Override
    protected void registerStatesAndModels() {
        for (DyeColor dyeColor : DyeColor.values()) {
            createStrengthenedGlass(RailcraftBlocks.STRENGTHENED_GLASS.variantFor(dyeColor).get());
            createStrengthenedGlass(RailcraftBlocks.IRON_TANK_GAUGE.variantFor(dyeColor).get());
            createStrengthenedGlass(RailcraftBlocks.STEEL_TANK_GAUGE.variantFor(dyeColor).get());

            createTankValve(RailcraftBlocks.IRON_TANK_VALVE.variantFor(dyeColor).get(),
                RailcraftBlocks.IRON_TANK_WALL.variantFor(dyeColor).get());
            createTankValve(RailcraftBlocks.STEEL_TANK_VALVE.variantFor(dyeColor).get(),
                RailcraftBlocks.STEEL_TANK_WALL.variantFor(dyeColor).get());
            createCubeColumnBlock(RailcraftBlocks.IRON_TANK_WALL.variantFor(dyeColor).get());
            createCubeColumnBlock(RailcraftBlocks.STEEL_TANK_WALL.variantFor(dyeColor).get());
            createPost(RailcraftBlocks.POST.variantFor(dyeColor).get());
        }


        simpleBlock(RailcraftBlocks.STEEL_BLOCK.get());
        simpleBlock(RailcraftBlocks.BRASS_BLOCK.get());
        simpleBlock(RailcraftBlocks.BRONZE_BLOCK.get());
        simpleBlock(RailcraftBlocks.INVAR_BLOCK.get());
        simpleBlock(RailcraftBlocks.LEAD_BLOCK.get());
        simpleBlock(RailcraftBlocks.NICKEL_BLOCK.get());
        simpleBlock(RailcraftBlocks.SILVER_BLOCK.get());
        simpleBlock(RailcraftBlocks.TIN_BLOCK.get());
        simpleBlock(RailcraftBlocks.ZINC_BLOCK.get());
        simpleBlock(RailcraftBlocks.COKE_BLOCK.get());
        simpleBlock(RailcraftBlocks.CRUSHED_OBSIDIAN.get());

        fluidBlock(RailcraftBlocks.CREOSOTE.get());

        createSteelAnvil(RailcraftBlocks.STEEL_ANVIL.get());
        createSteelAnvil(RailcraftBlocks.CHIPPED_STEEL_ANVIL.get());
        createSteelAnvil(RailcraftBlocks.DAMAGED_STEEL_ANVIL.get());

        createCubeColumnBlock(RailcraftBlocks.FEED_STATION.get());
        createCubeTopBottomBlock(RailcraftBlocks.MANUAL_ROLLING_MACHINE.get());
        createCubeTopBlock(RailcraftBlocks.CRUSHER.get());

        createFluidManipulator(RailcraftBlocks.FLUID_LOADER.get());
        createFluidManipulator(RailcraftBlocks.FLUID_UNLOADER.get());
        createManipulator(RailcraftBlocks.ITEM_LOADER.get());
        createManipulator(RailcraftBlocks.ITEM_UNLOADER.get());
        createDirectionalManipulator(RailcraftBlocks.ADVANCED_ITEM_LOADER.get());
        createDirectionalManipulator(RailcraftBlocks.ADVANCED_ITEM_UNLOADER.get());

        createFirebox(RailcraftBlocks.SOLID_FUELED_FIREBOX.get());
        createFirebox(RailcraftBlocks.FLUID_FUELED_FIREBOX.get());
        createFurnaceMultiblockBricks(RailcraftBlocks.COKE_OVEN_BRICKS.get());
        createFurnaceMultiblockBricks(RailcraftBlocks.BLAST_FURNACE_BRICKS.get());
        createSteamBoilerTank(RailcraftBlocks.LOW_PRESSURE_STEAM_BOILER_TANK.get());
        createSteamBoilerTank(RailcraftBlocks.HIGH_PRESSURE_STEAM_BOILER_TANK.get());

        createSteamTurbine(RailcraftBlocks.STEAM_TURBINE.get());

        createElevatorTrack(RailcraftBlocks.ELEVATOR_TRACK.get());
        createForceTrack(RailcraftBlocks.FORCE_TRACK.get());


        // Not put in the constructor!
        this.activatorTrackModels = createTrackModelSet("activator_track");
        this.activeActivatorTrackModels = createActiveTrackModelSet("activator_track");
        this.boosterTrackModels = createTrackModelSet("booster_track");
        this.activeBoosterTrackModels = createActiveTrackModelSet("booster_track");
        this.embarkingTrack = createTrackModelSet("embarking_track");
        this.activeEmbarkingTrack = createActiveTrackModelSet("embarking_track");
        this.launcherTrackModels = createTrackModelSet("launcher_track");
        this.activeLauncherTrackModels = createActiveTrackModelSet("launcher_track");
        this.oneWayTrackModels = createTrackModelSet("one_way_track");
        this.activeOneWayTrackModels = createActiveTrackModelSet("one_way_track");
        this.transitionTrackModels = createTrackModelSet("transition_track");
        this.activeTransitionTrackModels = createActiveTrackModelSet("transition_track");
        this.detectorTrackModels = createTrackModelSet("detector_track");
        this.activeDetectorTrackModels = createActiveTrackModelSet("detector_track");
        this.travelDetectorTrackModels = createTrackModelSet("detector_track_travel");
        this.activeTravelDetectorTrackModels = createActiveTrackModelSet("detector_track_travel");
        this.locomotiveTrackShutdownModel = createTrackModelSet("locomotive_track_shutdown");
        this.locomotiveTrackIdleModel = createTrackModelSet("locomotive_track_idle");
        this.locomotiveTrackRunningModel = createTrackModelSet("locomotive_track_running");
        this.activeLocomotiveTrackShutdownModel =
            createActiveTrackModelSet("locomotive_track_shutdown");
        this.activeLocomotiveTrackIdleModel = createActiveTrackModelSet("locomotive_track_idle");
        this.activeLocomotiveTrackRunningModel =
            createActiveTrackModelSet("locomotive_track_running");
        this.controlTrackModels = createTrackModelSet("control_track");



        createAbandonedTracks(RailcraftBlocks.ABANDONED_TRACK.get(),
            RailcraftBlocks.ABANDONED_LOCKING_TRACK.get(),
            RailcraftBlocks.ABANDONED_BUFFER_STOP_TRACK.get(),
            RailcraftBlocks.ABANDONED_ACTIVATOR_TRACK.get(),
            RailcraftBlocks.ABANDONED_BOOSTER_TRACK.get(),
            RailcraftBlocks.ABANDONED_CONTROL_TRACK.get(),
            RailcraftBlocks.ABANDONED_DETECTOR_TRACK.get(),
            RailcraftBlocks.ABANDONED_EMBARKING_TRACK.get(),
            RailcraftBlocks.ABANDONED_TURNOUT_TRACK.get(),
            RailcraftBlocks.ABANDONED_WYE_TRACK.get(),
            RailcraftBlocks.ABANDONED_JUNCTION_TRACK.get(),
            RailcraftBlocks.ABANDONED_LAUNCHER_TRACK.get(),
            RailcraftBlocks.ABANDONED_ONE_WAY_TRACK.get(),
            RailcraftBlocks.ABANDONED_LOCOMOTIVE_TRACK.get());

        createOutfittedTracks(Blocks.RAIL,
            RailcraftBlocks.IRON_LOCKING_TRACK.get(),
            RailcraftBlocks.IRON_BUFFER_STOP_TRACK.get(),
            RailcraftBlocks.IRON_ACTIVATOR_TRACK.get(),
            RailcraftBlocks.IRON_BOOSTER_TRACK.get(),
            RailcraftBlocks.IRON_CONTROL_TRACK.get(),
            RailcraftBlocks.IRON_DETECTOR_TRACK.get(),
            RailcraftBlocks.IRON_EMBARKING_TRACK.get(),
            RailcraftBlocks.IRON_TURNOUT_TRACK.get(),
            RailcraftBlocks.IRON_WYE_TRACK.get(),
            RailcraftBlocks.IRON_JUNCTION_TRACK.get(),
            RailcraftBlocks.IRON_LAUNCHER_TRACK.get(),
            RailcraftBlocks.IRON_ONE_WAY_TRACK.get(),
            RailcraftBlocks.IRON_LOCOMOTIVE_TRACK.get());

        createTracks(RailcraftBlocks.STRAP_IRON_TRACK.get(),
            RailcraftBlocks.STRAP_IRON_LOCKING_TRACK.get(),
            RailcraftBlocks.STRAP_IRON_BUFFER_STOP_TRACK.get(),
            RailcraftBlocks.STRAP_IRON_ACTIVATOR_TRACK.get(),
            RailcraftBlocks.STRAP_IRON_BOOSTER_TRACK.get(),
            RailcraftBlocks.STRAP_IRON_CONTROL_TRACK.get(),
            RailcraftBlocks.STRAP_IRON_DETECTOR_TRACK.get(),
            RailcraftBlocks.STRAP_IRON_EMBARKING_TRACK.get(),
            RailcraftBlocks.STRAP_IRON_TURNOUT_TRACK.get(),
            RailcraftBlocks.STRAP_IRON_WYE_TRACK.get(),
            RailcraftBlocks.STRAP_IRON_JUNCTION_TRACK.get(),
            RailcraftBlocks.STRAP_IRON_LAUNCHER_TRACK.get(),
            RailcraftBlocks.STRAP_IRON_ONE_WAY_TRACK.get(),
            RailcraftBlocks.STRAP_IRON_LOCOMOTIVE_TRACK.get());

        createTracks(RailcraftBlocks.REINFORCED_TRACK.get(),
            RailcraftBlocks.REINFORCED_LOCKING_TRACK.get(),
            RailcraftBlocks.REINFORCED_BUFFER_STOP_TRACK.get(),
            RailcraftBlocks.REINFORCED_ACTIVATOR_TRACK.get(),
            RailcraftBlocks.REINFORCED_BOOSTER_TRACK.get(),
            RailcraftBlocks.REINFORCED_CONTROL_TRACK.get(),
            RailcraftBlocks.REINFORCED_DETECTOR_TRACK.get(),
            RailcraftBlocks.REINFORCED_EMBARKING_TRACK.get(),
            RailcraftBlocks.REINFORCED_TURNOUT_TRACK.get(),
            RailcraftBlocks.REINFORCED_WYE_TRACK.get(),
            RailcraftBlocks.REINFORCED_JUNCTION_TRACK.get(),
            RailcraftBlocks.REINFORCED_LAUNCHER_TRACK.get(),
            RailcraftBlocks.REINFORCED_ONE_WAY_TRACK.get(),
            RailcraftBlocks.REINFORCED_LOCOMOTIVE_TRACK.get());

        createTracks(RailcraftBlocks.ELECTRIC_TRACK.get(),
            RailcraftBlocks.ELECTRIC_LOCKING_TRACK.get(),
            RailcraftBlocks.ELECTRIC_BUFFER_STOP_TRACK.get(),
            RailcraftBlocks.ELECTRIC_ACTIVATOR_TRACK.get(),
            RailcraftBlocks.ELECTRIC_BOOSTER_TRACK.get(),
            RailcraftBlocks.ELECTRIC_CONTROL_TRACK.get(),
            RailcraftBlocks.ELECTRIC_DETECTOR_TRACK.get(),
            RailcraftBlocks.ELECTRIC_EMBARKING_TRACK.get(),
            RailcraftBlocks.ELECTRIC_TURNOUT_TRACK.get(),
            RailcraftBlocks.ELECTRIC_WYE_TRACK.get(),
            RailcraftBlocks.ELECTRIC_JUNCTION_TRACK.get(),
            RailcraftBlocks.ELECTRIC_LAUNCHER_TRACK.get(),
            RailcraftBlocks.ELECTRIC_ONE_WAY_TRACK.get(),
            RailcraftBlocks.ELECTRIC_LOCOMOTIVE_TRACK.get());

        createHighSpeedTracks(RailcraftBlocks.HIGH_SPEED_TRACK.get(),
            RailcraftBlocks.HIGH_SPEED_TRANSITION_TRACK.get(),
            RailcraftBlocks.HIGH_SPEED_LOCKING_TRACK.get(),
            RailcraftBlocks.HIGH_SPEED_ACTIVATOR_TRACK.get(),
            RailcraftBlocks.HIGH_SPEED_BOOSTER_TRACK.get(),
            RailcraftBlocks.HIGH_SPEED_DETECTOR_TRACK.get(),
            RailcraftBlocks.HIGH_SPEED_TURNOUT_TRACK.get(),
            RailcraftBlocks.HIGH_SPEED_WYE_TRACK.get(),
            RailcraftBlocks.HIGH_SPEED_JUNCTION_TRACK.get(),
            RailcraftBlocks.HIGH_SPEED_LOCOMOTIVE_TRACK.get());

        createHighSpeedTracks(RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRACK.get(),
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

        var singleModel = models().cubeAll(name(block, "_single"), endTexture)
            .renderType(CUTOUT);
        var topModel = models().cubeColumn(name(block, "_top"),
                TextureMapping.getBlockTexture(block, "_side_top"), endTexture)
            .renderType(CUTOUT);
        var centerModel = models().cubeColumn(name(block, "_center"),
                TextureMapping.getBlockTexture(block, "_side_center"), endTexture)
            .renderType(CUTOUT);
        var bottomModel = models().cubeColumn(name(block, "_bottom"),
                TextureMapping.getBlockTexture(block, "_side_bottom"), endTexture)
            .renderType(CUTOUT);

        getVariantBuilder(block)
            .forAllStatesExcept(blockState -> ConfiguredModel.builder()
                .modelFile(switch (blockState.getValue(AbstractStrengthenedGlassBlock.TYPE)) {
                    case SINGLE -> singleModel;
                    case TOP -> topModel;
                    case CENTER -> centerModel;
                    case BOTTOM -> bottomModel;
                }).build(), IronTankGaugeBlock.LEVEL);

        itemModels().withExistingParent(name(block),
            modLoc(BLOCK_FOLDER + "/" + name(block, "_single")));
    }

    private void createFluidManipulator(FluidManipulatorBlock block) {
        var texture = TextureMapping.cubeBottomTop(block);
        var model = models().cubeBottomTop(name(block), texture.get(TextureSlot.SIDE),
                texture.get(TextureSlot.BOTTOM), texture.get(TextureSlot.TOP))
            .renderType(CUTOUT);

        simpleBlock(block, model);

        var side = new ResourceLocation(Railcraft.ID, "block/fluid_manipulator_side_inventory");
        var bottom = TextureMapping.getBlockTexture(block, "_bottom");
        var top = TextureMapping.getBlockTexture(block, "_top");
        models().cubeBottomTop(name(block, "_inventory"), side, bottom, top);

        itemModels().withExistingParent(name(block),
            modLoc(BLOCK_FOLDER + "/" + name(block, "_inventory")));
    }

    private void createManipulator(ManipulatorBlock block) {
        var texture = TextureMapping.cubeBottomTop(block);
        var model = models().cubeBottomTop(name(block), texture.get(TextureSlot.SIDE),
            texture.get(TextureSlot.BOTTOM), texture.get(TextureSlot.TOP));

        simpleBlock(block, model);
    }

    private void createDirectionalManipulator(ManipulatorBlock block) {

        var horizontalTexture = TextureMapping.orientableCubeOnlyTop(block);
        var horizontalModel = models().orientable(name(block),
            horizontalTexture.get(TextureSlot.SIDE), horizontalTexture.get(TextureSlot.FRONT),
            horizontalTexture.get(TextureSlot.TOP));

        var side = TextureMapping.getBlockTexture(block, "_side");
        var front = TextureMapping.getBlockTexture(block, "_front");
        var top = TextureMapping.getBlockTexture(block, "_top");
        var upModel = models().cubeBottomTop(name(block, "_up"), side, top, front);
        var downModel = models().cubeBottomTop(name(block, "_down"), side, front, top);

        getVariantBuilder(block)
            .forAllStatesExcept(blockState -> {
                Direction facing = blockState.getValue(BlockStateProperties.FACING);
                int yRot = 0;

                switch (facing) {
                    case SOUTH : yRot = 180; break;
                    case EAST : yRot = 90; break;
                    case WEST : yRot = 270; break;
                    case UP : return ConfiguredModel.builder().modelFile(upModel).build();
                    case DOWN: return ConfiguredModel.builder().modelFile(downModel).build();
                    default: break;
                }

                return ConfiguredModel.builder()
                    .modelFile(horizontalModel)
                    .rotationX(0)
                    .rotationY(yRot)
                    .build();
            }, AdvancedItemLoaderBlock.POWERED);
    }

    private void createFirebox(FireboxBlock block) {
        var endTexture = TextureMapping.getBlockTexture(block, "_end");
        var sideTexture = TextureMapping.getBlockTexture(block, "_side");
        var sideLitTexture = TextureMapping.getBlockTexture(block, "_side_lit");

        var model = models().cubeColumn(name(block), sideTexture, endTexture);
        var litModel = models().cubeColumn(name(block, "_lit"), sideLitTexture, endTexture);

        getVariantBuilder(block)
            .forAllStates(blockState -> {
                var lit = blockState.getValue(FireboxBlock.LIT);
                return ConfiguredModel.builder().modelFile(lit ? litModel : model).build();
            });
    }

    private void createFurnaceMultiblockBricks(FurnaceMultiblockBlock block) {
        var bricksModel = cubeAll(block);
        var blockTexture = TextureMapping.getBlockTexture(block);
        var sideTexture = TextureMapping.getBlockTexture(block, "_window");
        var sideLitTexture = TextureMapping.getBlockTexture(block, "_window_lit");

        var windowModel = models().cubeBottomTop(name(block, "_window"), sideTexture,
            blockTexture, blockTexture);
        var litWindowModel = models().cubeBottomTop(name(block, "_window_lit"), sideLitTexture,
            blockTexture, blockTexture);

        getVariantBuilder(block)
            .forAllStates(blockState -> {
                var lit = blockState.getValue(FurnaceMultiblockBlock.LIT);
                var window = blockState.getValue(FurnaceMultiblockBlock.WINDOW);
                ModelFile model;
                if(!window)
                    model = bricksModel;
                else if (lit)
                    model = litWindowModel;
                else
                    model = windowModel;
                return ConfiguredModel.builder().modelFile(model).build();
            });
    }

    private void createSteamTurbine(Block block) {
        var sideTexture = TextureMapping.getBlockTexture(block, "_side");

        createSteamTurbineModel(block, sideTexture, "_inventory", false);
        itemModels().withExistingParent(name(block),
            modLoc(BLOCK_FOLDER + "/" + name(block, "_inventory")));

        var noneVariant = models().cubeAll(name(block, "_side"), sideTexture);

        getVariantBuilder(block)
            .forAllStates(blockState -> {
                var type = blockState.getValue(SteamTurbineBlock.TYPE);
                var rotated = blockState.getValue(SteamTurbineBlock.ROTATED);

                if(type == Type.NONE) {
                    return ConfiguredModel.builder().modelFile(noneVariant).build();
                } else {
                    var model = this.createSteamTurbineModel(block, sideTexture, "_" + type.getSerializedName(),
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
        var parent = modLoc(BLOCK_FOLDER + "/template_mirrored_cube");
        return cube(name(block, suffix), parent,
            sideTexture,
            sideTexture,
            rotated ? sideTexture : frontTexture,
            rotated ? sideTexture : frontTexture,
            rotated ? frontTexture : sideTexture,
            rotated ? frontTexture : sideTexture,
            sideTexture);
    }

    private void createTankValve(TankValveBlock block, BaseTankBlock wallBlock) {
        var verticalModel = cube(name(block), mcLoc("cube"),
            TextureMapping.getBlockTexture(block, "_top"),
            TextureMapping.getBlockTexture(block, "_top"),
            TextureMapping.getBlockTexture(wallBlock, "_side"),
            TextureMapping.getBlockTexture(wallBlock, "_side"),
            TextureMapping.getBlockTexture(wallBlock, "_side"),
            TextureMapping.getBlockTexture(wallBlock, "_side"),
            TextureMapping.getBlockTexture(wallBlock, "_top"));

        var horizontalModel = cube(name(block, "_horizontal"), mcLoc("cube"),
            TextureMapping.getBlockTexture(wallBlock, "_top"),
            TextureMapping.getBlockTexture(wallBlock, "_top"),
            TextureMapping.getBlockTexture(block, "_front"),
            TextureMapping.getBlockTexture(block, "_front"),
            TextureMapping.getBlockTexture(wallBlock, "_side"),
            TextureMapping.getBlockTexture(wallBlock, "_side"),
            TextureMapping.getBlockTexture(wallBlock, "_top"));

        getVariantBuilder(block)
            .forAllStates(blockState -> {
                var axis = blockState.getValue(BlockStateProperties.AXIS);
                return switch (axis) {
                    case Y -> ConfiguredModel.builder().modelFile(verticalModel).build();
                    case Z -> ConfiguredModel.builder().modelFile(horizontalModel).build();
                    case X -> ConfiguredModel.builder().modelFile(horizontalModel).rotationY(90).build();
                };
            });
    }

    private void createCubeColumnBlock(Block block) {
        var model = models().cubeColumn(name(block), TextureMapping.getBlockTexture(block, "_side"),
            TextureMapping.getBlockTexture(block, "_top"));
        simpleBlock(block, model);
    }

    private void createCubeTopBottomBlock(Block block) {
        var sideTexture = TextureMapping.getBlockTexture(block, "_side");
        var bottomTexture = TextureMapping.getBlockTexture(block, "_bottom");
        var topTexture = TextureMapping.getBlockTexture(block, "_top");
        var model = models().cubeBottomTop(name(block), sideTexture, bottomTexture, topTexture);
        simpleBlock(block, model);
    }

    private void createCubeTopBlock(Block block) {
        var sideTexture = TextureMapping.getBlockTexture(block, "_side");
        var topTexture = TextureMapping.getBlockTexture(block, "_top");
        var model = models().cubeTop(name(block), sideTexture, topTexture);
        simpleBlock(block, model);
    }

    private void createPost(PostBlock block) {
        var texture = TextureMapping.defaultTexture(block).get(TextureSlot.TEXTURE);
        var postFullColumnTemplate = modLoc("template_post_full_column");
        var postDoubleConnectionTemplate = modLoc("template_post_double_connection");
        var postTopColumnTemplate = modLoc("template_post_top_column");
        var postSmallColumnTemplate = modLoc("template_post_small_column");
        var postPlatformTemplate = modLoc("template_post_platform");
        var postSingleConnectionTemplate = modLoc("template_post_single_connection");
        var postInventoryTemplate = modLoc("post_inventory");

        var fullColumnModel = models()
            .singleTexture(name(block, "_full_column"), postFullColumnTemplate, texture);
        var doubleConnectionModel = models()
            .singleTexture(name(block, "_double_connection"), postDoubleConnectionTemplate, texture);
        var topColumnModel = models()
            .singleTexture(name(block, "_top_column"), postTopColumnTemplate, texture);
        var smallColumnModel = models()
            .singleTexture(name(block, "_small_column"), postSmallColumnTemplate, texture);
        var platformModel = models()
            .singleTexture(name(block, "_platform"), postPlatformTemplate, texture);
        var singleConnectionModel = models()
            .singleTexture(name(block, "_single_connection"), postSingleConnectionTemplate, texture);
        var inventoryModel = models()
            .singleTexture(name(block, "_inventory"), postInventoryTemplate, texture);

        itemModels().withExistingParent(name(block), inventoryModel.getLocation());

        getMultipartBuilder(block)
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

        var steamBoilerTemplate = modLoc("template_steam_boiler_tank");
        var steamBoilerNETemplate = modLoc("template_steam_boiler_tank_ne");
        var steamBoilerNEWTemplate = modLoc("template_steam_boiler_tank_new");
        var steamBoilerNSETemplate = modLoc("template_steam_boiler_tank_nse");
        var steamBoilerNSWTemplate = modLoc("template_steam_boiler_tank_nsw");
        var steamBoilerNWTemplate = modLoc("template_steam_boiler_tank_nw");
        var steamBoilerSETemplate = modLoc("template_steam_boiler_tank_se");
        var steamBoilerSEWTemplate = modLoc("template_steam_boiler_tank_sew");
        var steamBoilerSWTemplate = modLoc("template_steam_boiler_tank_sw");

        var model = sideEnd(name(block), steamBoilerTemplate, side, end);
        var allModel = models().cubeColumn(name(block, "_all"), side, end);
        var northEastModel = sideEnd(name(block, "_ne"), steamBoilerNETemplate, side, end);
        var northEastWestModel = sideEnd(name(block, "_new"), steamBoilerNEWTemplate, side, end);
        var northSouthEastModel = sideEnd(name(block, "_nse"), steamBoilerNSETemplate, side, end);
        var northSouthWestModel = sideEnd(name(block, "_nsw"), steamBoilerNSWTemplate, side, end);
        var northWestModel = sideEnd(name(block, "_nw"), steamBoilerNWTemplate, side, end);
        var southEastModel = sideEnd(name(block, "_se"), steamBoilerSETemplate, side, end);
        var southEastWestModel = sideEnd(name(block, "_sew"), steamBoilerSEWTemplate, side, end);
        var southWestModel = sideEnd(name(block, "_sw"), steamBoilerSWTemplate, side, end);

        getVariantBuilder(block)
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
    }

    private void createSteelAnvil(AnvilBlock block) {
        var base = TextureMapping.getBlockTexture(RailcraftBlocks.STEEL_ANVIL.get());
        var top = TextureMapping.getBlockTexture(block, "_top");

        var template = mcLoc("template_anvil");
        var model = models().withExistingParent(name(block), template)
            .texture("top", top)
            .texture("body", base)
            .texture("particle", base);
        horizontalBlock(block, model, 0);
    }

    public void fluidBlock(LiquidBlock block) {
        var model = models().withExistingParent(name(block), mcLoc("water"));
        simpleBlock(block, model);
    }

    private void createElevatorTrack(ElevatorTrackBlock block) {
        var texture =  TextureMapping.defaultTexture(block).get(TextureSlot.TEXTURE);
        var textureOn =  TextureMapping.getBlockTexture(block, "_on");
        var template = modLoc("template_elevator_track");

        var model = models()
            .singleTexture(name(block), template, texture)
            .renderType(CUTOUT);
        var activeModel = models()
            .singleTexture(name(block, "_on"), template, textureOn)
            .renderType(CUTOUT);

        getVariantBuilder(block)
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

        basicItem(block);
    }

    private void createForceTrack(ForceTrackBlock block) {
        var texture = TextureMapping.getBlockTexture(block);
        var template = modLoc("template_force_track");

        var model = models()
            .singleTexture(name(block), template, "rail", texture)
            .renderType(CUTOUT);

        getVariantBuilder(block)
            .partialState()
            .with(ForceTrackBlock.SHAPE, RailShape.NORTH_SOUTH)
            .setModels(ConfiguredModel.builder().modelFile(model).build())
            .partialState()
            .with(ForceTrackBlock.SHAPE, RailShape.EAST_WEST)
            .setModels(ConfiguredModel.builder().modelFile(model).rotationY(90).build());
    }

    private void createTurnoutTrack(TurnoutTrackBlock block) {
        var northTexture = TextureMapping.getBlockTexture(block, "_north");
        var northSwitchedTexture = TextureMapping.getBlockTexture(block, "_north_switched");
        var southTexture = TextureMapping.getBlockTexture(block, "_south");
        var southSwitchedTexture = TextureMapping.getBlockTexture(block, "_south_switched");

        var northModel = models()
            .singleTexture(name(block, "_north"), FLAT_TEMPLATE, "rail", northTexture)
            .renderType(CUTOUT);
        var northSwitchedModel = models()
            .singleTexture(name(block, "_north_switched"), FLAT_TEMPLATE, "rail", northSwitchedTexture)
            .renderType(CUTOUT);
        var southModel = models()
            .singleTexture(name(block, "_south"), FLAT_TEMPLATE, "rail", southTexture)
            .renderType(CUTOUT);
        var southSwitchedModel = models()
            .singleTexture(name(block, "_south_switched"), FLAT_TEMPLATE, "rail", southSwitchedTexture)
            .renderType(CUTOUT);

        getVariantBuilder(block)
            .forAllStatesExcept(blockState -> {
                var shape = blockState.getValue(SwitchTrackBlock.SHAPE);
                var reversed = blockState.getValue(ReversibleOutfittedTrackBlock.REVERSED);
                var mirrored = blockState.getValue(TurnoutTrackBlock.MIRRORED);
                var switched = blockState.getValue(SwitchTrackBlock.SWITCHED);

                if(shape == RailShape.NORTH_SOUTH) {
                    if(!reversed && !mirrored && !switched) {
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
                } else if(shape == RailShape.EAST_WEST) {
                    if(!reversed && !mirrored && !switched) {
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

        var eastModel = models()
            .singleTexture(name(block, "_east"), FLAT_TEMPLATE, "rail", eastTexture)
            .renderType(CUTOUT);
        var eastSwitchedModel = models()
            .singleTexture(name(block, "_east_switched"), FLAT_TEMPLATE, "rail", eastSwitchedTexture)
            .renderType(CUTOUT);
        var westModel = models()
            .singleTexture(name(block, "_west"), FLAT_TEMPLATE, "rail", westTexture)
            .renderType(CUTOUT);
        var westSwitchedModel = models()
            .singleTexture(name(block, "_west_switched"), FLAT_TEMPLATE, "rail", westSwitchedTexture)
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
        basicItem(block, "_east");
    }

    private void createJunctionTrack(JunctionTrackBlock block) {
        var model = models().withExistingParent(name(block), FLAT_TEMPLATE)
            .texture("rail", TextureMapping.getBlockTexture(block))
            .renderType(CUTOUT);
        simpleBlock(block, model);
        basicItem(block);
    }

    private void createFlexTrack(TrackBlock block) {
        var blockTexture = TextureMapping.getBlockTexture(block);
        var cornerTexture = TextureMapping.getBlockTexture(block, "_corner");

        var flatModel = models()
            .singleTexture(name(block), FLAT_TEMPLATE, "rail", blockTexture)
            .renderType(CUTOUT);
        var cornerModel = models()
            .singleTexture(name(block, "_corner"), CORNER_TEMPLATE, "rail", cornerTexture)
            .renderType(CUTOUT);
        var raisedNorthEastModel = models()
            .singleTexture(name(block, "_raised_ne"), RAISED_NE_TEMPLATE, "rail", blockTexture)
            .renderType(CUTOUT);
        var raisedSouthWestModel = models()
            .singleTexture(name(block, "_raised_sw"), RAISED_SW_TEMPLATE, "rail", blockTexture)
            .renderType(CUTOUT);

        getVariantBuilder(block)
            .forAllStatesExcept(blockState -> {
                RailShape shape = blockState.getValue(BlockStateProperties.RAIL_SHAPE);
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

        var flatModel0 =  models()
            .singleTexture(name(block, "_0"), FLAT_TEMPLATE, "rail", texture0)
            .renderType(CUTOUT);
        var flatModel1 =  models()
            .singleTexture(name(block, "_1"), FLAT_TEMPLATE, "rail", texture1)
            .renderType(CUTOUT);
        var cornerModel = models()
            .singleTexture(name(block, "_corner"), CORNER_TEMPLATE, "rail", cornerTexture)
            .renderType(CUTOUT);
        var raisedNorthEastModel = models()
            .singleTexture(name(block, "_raised_ne"), RAISED_NE_TEMPLATE, "rail", texture0)
            .renderType(CUTOUT);
        var raisedSouthWestModel = models()
            .singleTexture(name(block, "_raised_sw"), RAISED_SW_TEMPLATE, "rail", texture0)
            .renderType(CUTOUT);

        getMultipartBuilder(block)
            .part()
            .modelFile(models().getExistingFile(mcLoc("block/grass"))).addModel()
            .condition(AbandonedTrackBlock.GRASS, true).end()
            .part()
            .modelFile(flatModel0).nextModel().modelFile(flatModel1).addModel()
            .condition(BlockStateProperties.RAIL_SHAPE, RailShape.NORTH_SOUTH).end()
            .part()
            .modelFile(flatModel0).rotationY(90).nextModel().modelFile(flatModel1).rotationY(90).addModel()
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

        basicItem(block, "_0");
    }

    private void createAbandonedTracks(TrackBlock block, LockingTrackBlock lockingTrackBlock,
        BufferStopTrackBlock bufferStopTrackBlock, ActivatorTrackBlock activatorTrackBlock,
        BoosterTrackBlock boosterTrackBlock, ControlTrackBlock controlTrackBlock,
        DetectorTrackBlock detectorTrackBlock, EmbarkingTrackBlock embarkingTrackBlock,
        TurnoutTrackBlock turnoutTrackBlock, WyeTrackBlock wyeTrackBlock,
        JunctionTrackBlock junctionTrackBlock, LauncherTrackBlock launcherTrackBlock,
        OneWayTrackBlock oneWayTrackBlock, LocomotiveTrackBlock locomotiveTrackBlock) {
        createAbandonedFlexTrack(block);
        createOutfittedTracks(block, lockingTrackBlock, bufferStopTrackBlock, activatorTrackBlock,
            boosterTrackBlock, controlTrackBlock, detectorTrackBlock, embarkingTrackBlock,
            turnoutTrackBlock, wyeTrackBlock, junctionTrackBlock, launcherTrackBlock,
            oneWayTrackBlock, locomotiveTrackBlock);
    }

    private void createTracks(TrackBlock block, LockingTrackBlock lockingTrackBlock,
        BufferStopTrackBlock bufferStopTrackBlock, ActivatorTrackBlock activatorTrackBlock,
        BoosterTrackBlock boosterTrackBlock, ControlTrackBlock controlTrackBlock,
        DetectorTrackBlock detectorTrackBlock, EmbarkingTrackBlock embarkingTrackBlock,
        TurnoutTrackBlock turnoutTrackBlock, WyeTrackBlock wyeTrackBlock,
        JunctionTrackBlock junctionTrackBlock, LauncherTrackBlock launcherTrackBlock,
        OneWayTrackBlock oneWayTrackBlock, LocomotiveTrackBlock locomotiveTrackBlock) {
        createFlexTrack(block);
        createOutfittedTracks(block, lockingTrackBlock, bufferStopTrackBlock, activatorTrackBlock,
            boosterTrackBlock, controlTrackBlock, detectorTrackBlock, embarkingTrackBlock,
            turnoutTrackBlock, wyeTrackBlock, junctionTrackBlock, launcherTrackBlock,
            oneWayTrackBlock, locomotiveTrackBlock);
    }

    private void createOutfittedTracks(Block block, LockingTrackBlock lockingTrackBlock,
        BufferStopTrackBlock bufferStopTrackBlock, ActivatorTrackBlock activatorTrackBlock,
        BoosterTrackBlock boosterTrackBlock, ControlTrackBlock controlTrackBlock,
        DetectorTrackBlock detectorTrackBlock, EmbarkingTrackBlock embarkingTrackBlock,
        TurnoutTrackBlock turnoutTrackBlock, WyeTrackBlock wyeTrackBlock,
        JunctionTrackBlock junctionTrackBlock, LauncherTrackBlock launcherTrackBlock,
        OneWayTrackBlock oneWayTrackBlock, LocomotiveTrackBlock locomotiveTrackBlock) {
        var outfittedTrackModels = this.createOutfittedTrackModelSet(block);

        createActiveOutfittedTrack(activatorTrackBlock, true, false, outfittedTrackModels,
            activatorTrackModels, activeActivatorTrackModels);
        createActiveOutfittedTrack(boosterTrackBlock, true, false, outfittedTrackModels,
            boosterTrackModels, activeBoosterTrackModels);
        createActiveOutfittedTrack(embarkingTrackBlock, true, false, outfittedTrackModels,
            embarkingTrack, activeEmbarkingTrack);
        createActiveOutfittedTrack(launcherTrackBlock, false, false, outfittedTrackModels,
            launcherTrackModels, activeLauncherTrackModels);
        createActiveOutfittedTrack(oneWayTrackBlock, false, true, outfittedTrackModels,
            oneWayTrackModels, activeOneWayTrackModels);

        createDetectorTrack(detectorTrackBlock, outfittedTrackModels, detectorTrackModels,
            activeDetectorTrackModels, travelDetectorTrackModels, activeTravelDetectorTrackModels);

        createLocomotiveTrack(locomotiveTrackBlock, outfittedTrackModels,
            locomotiveTrackShutdownModel, activeLocomotiveTrackShutdownModel,
            locomotiveTrackIdleModel, activeLocomotiveTrackIdleModel,
            locomotiveTrackRunningModel, activeLocomotiveTrackRunningModel);

        this.createControlTrack(controlTrackBlock, outfittedTrackModels, controlTrackModels);

        createLockingTrack(lockingTrackBlock, outfittedTrackModels.flatModel());
        createBufferStopTrack(bufferStopTrackBlock, outfittedTrackModels.flatModel());
        createTurnoutTrack(turnoutTrackBlock);
        createWyeTrack(wyeTrackBlock);
        createJunctionTrack(junctionTrackBlock);
    }

    private void createHighSpeedTracks(TrackBlock block, TransitionTrackBlock transitionTrackBlock,
        LockingTrackBlock lockingTrackBlock, ActivatorTrackBlock activatorTrackBlock,
        BoosterTrackBlock boosterTrackBlock, DetectorTrackBlock detectorTrackBlock,
        TurnoutTrackBlock turnoutTrackBlock, WyeTrackBlock wyeTrackBlock,
        JunctionTrackBlock junctionTrackBlock, LocomotiveTrackBlock locomotiveTrackBlock) {
        createFlexTrack(block);
        var outfittedTrackModels = this.createOutfittedTrackModelSet(block);

        createActiveOutfittedTrack(activatorTrackBlock, true, false, outfittedTrackModels,
            activatorTrackModels, activeActivatorTrackModels);
        createActiveOutfittedTrack(boosterTrackBlock, true, false, outfittedTrackModels,
            boosterTrackModels, activeBoosterTrackModels);
        createActiveOutfittedTrack(transitionTrackBlock, true, true, outfittedTrackModels,
            transitionTrackModels, activeTransitionTrackModels);

        createDetectorTrack(detectorTrackBlock, outfittedTrackModels, detectorTrackModels,
            activeDetectorTrackModels, travelDetectorTrackModels, activeTravelDetectorTrackModels);

        createLocomotiveTrack(locomotiveTrackBlock, outfittedTrackModels,
            locomotiveTrackShutdownModel, activeLocomotiveTrackShutdownModel,
            locomotiveTrackIdleModel, activeLocomotiveTrackIdleModel,
            locomotiveTrackRunningModel, activeLocomotiveTrackRunningModel);

        createLockingTrack(lockingTrackBlock, outfittedTrackModels.flatModel());
        createTurnoutTrack(turnoutTrackBlock);
        createWyeTrack(wyeTrackBlock);
        createJunctionTrack(junctionTrackBlock);
    }

    private void createLockingTrack(LockingTrackBlock block, ModelFile trackModel) {
        var lockdownModel = createPassiveRail("locking_track_lockdown");
        var trainLockdownModel = createPassiveRail("locking_track_train_lockdown");
        var holdingModel = createPassiveRail("locking_track_holding");
        var trainHoldingModel = createPassiveRail("locking_track_train_holding");
        var boardingModel = createPassiveRail("locking_track_boarding");
        var boardingReversedModel = createPassiveRail("locking_track_boarding_reversed");
        var trainBoardingModel = createPassiveRail("locking_track_train_boarding");
        var trainBoardingReversedModel = createPassiveRail("locking_track_train_boarding_reversed");
        var activeLockdownModel = createActiveRail("locking_track_lockdown");
        var activeTrainLockdownModel = createActiveRail("locking_track_train_lockdown");
        var activeHoldingModel = createActiveRail("locking_track_holding");
        var activeTrainHoldingModel = createActiveRail("locking_track_train_holding");
        var activeBoardingModel = createActiveRail("locking_track_boarding");
        var activeBoardingReversedModel = createActiveRail("locking_track_boarding_reversed");
        var activeTrainBoardingModel = createActiveRail("locking_track_train_boarding");
        var activeTrainBoardingReversedModel = createActiveRail("locking_track_train_boarding_reversed");

        var builder = getMultipartBuilder(block)
            .part()
            .modelFile(trackModel).addModel()
            .condition(LockingTrackBlock.SHAPE, RailShape.NORTH_SOUTH).end()
            .part()
            .modelFile(trackModel).rotationY(90).addModel()
            .condition(LockingTrackBlock.SHAPE, RailShape.EAST_WEST).end();

        addLockingMode(LockingMode.LOCKDOWN, lockdownModel, activeLockdownModel, builder);
        addLockingMode(LockingMode.TRAIN_LOCKDOWN, trainLockdownModel, activeTrainLockdownModel,
            builder);
        addLockingMode(LockingMode.HOLDING, holdingModel, activeHoldingModel, builder);
        addLockingMode(LockingMode.TRAIN_HOLDING, trainHoldingModel, activeTrainHoldingModel,
            builder);
        addLockingMode(LockingMode.BOARDING, boardingModel, activeBoardingModel, builder);
        addLockingMode(LockingMode.BOARDING_REVERSED, boardingReversedModel,
            activeBoardingReversedModel, builder);
        addLockingMode(LockingMode.TRAIN_BOARDING, trainBoardingModel, activeTrainBoardingModel,
            builder);
        addLockingMode(LockingMode.TRAIN_BOARDING_REVERSED, trainBoardingReversedModel,
            activeTrainBoardingReversedModel, builder);

        itemModels().basicItem(block.asItem());
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
        var bufferStop = new ModelFile.UncheckedModelFile(modLoc("block/buffer_stop"));

        getMultipartBuilder(block)
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

        itemModels().basicItem(block.asItem());
    }

    private void createActiveOutfittedTrack(Block block, boolean allowedOnSlopes,
        boolean reversible, StraightTrackModelSet trackModels, StraightTrackModelSet trackKitModels,
        StraightTrackModelSet activeTrackKitModels) {

        var builder = getMultipartBuilder(block);
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
        itemModels().basicItem(block.asItem());
    }

    private void createDetectorTrack(DetectorTrackBlock block, StraightTrackModelSet trackModels,
        StraightTrackModelSet detectorTrackModels, StraightTrackModelSet activeDetectorTrackModels,
        StraightTrackModelSet travelDetectorTrackModels,
        StraightTrackModelSet activeTravelDetectorTrackModels) {
        var builder = getMultipartBuilder(block);
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
        itemModels().basicItem(block.asItem());
    }

    private void createLocomotiveTrack(LocomotiveTrackBlock block,
        StraightTrackModelSet trackModel, StraightTrackModelSet locomotiveTrackShutdownModel,
        StraightTrackModelSet activeLocomotiveTrackShutdownModel,
        StraightTrackModelSet locomotiveTrackIdleModel,
        StraightTrackModelSet activeLocomotiveTrackIdleModel,
        StraightTrackModelSet locomotiveTrackRunningModel,
        StraightTrackModelSet activeLocomotiveTrackRunningModel) {
        var builder = getMultipartBuilder(block);
        trackModel.apply(builder, LocomotiveTrackBlock.SHAPE, true, false);

        addLocomotiveMode(builder, Locomotive.Mode.SHUTDOWN, locomotiveTrackShutdownModel,
            activeLocomotiveTrackShutdownModel);
        addLocomotiveMode(builder, Locomotive.Mode.IDLE, locomotiveTrackIdleModel,
            activeLocomotiveTrackIdleModel);
        addLocomotiveMode(builder, Locomotive.Mode.RUNNING, locomotiveTrackRunningModel,
            activeLocomotiveTrackRunningModel);

        itemModels().basicItem(block.asItem());
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
        var builder = getMultipartBuilder(block);
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

        itemModels().basicItem(block.asItem());
    }

    private BlockModelBuilder createVariant(String name, ModelTemplate model) {
        var texture = modLoc(BLOCK_FOLDER + "/" + name);
        var modelName = name + model.suffix.orElse("");
        var parent = model.model.orElseThrow();

        return models()
            .singleTexture(modelName, parent, "rail", texture)
            .renderType(CUTOUT);
    }

    private BlockModelBuilder createPassiveRail(String name) {
        return createVariant(name, ModelTemplates.RAIL_FLAT);
    }

    private BlockModelBuilder createActiveRail(String name) {
        return createVariant(name + "_on", ModelTemplates.RAIL_FLAT);
    }

    private StraightTrackModelSet createOutfittedTrackModelSet(Block block) {
        return createTrackModelSet(name(block, "_outfitted"));
    }

    private StraightTrackModelSet createTrackModelSet(String name) {
        return new StraightTrackModelSet(createPassiveRail(name),
            createVariant(name, ModelTemplates.RAIL_RAISED_NE),
            createVariant(name, ModelTemplates.RAIL_RAISED_SW));
    }

    private StraightTrackModelSet createActiveTrackModelSet(String name) {
        return new StraightTrackModelSet(createActiveRail(name),
            createVariant(name + "_on", ModelTemplates.RAIL_RAISED_NE),
            createVariant(name + "_on", ModelTemplates.RAIL_RAISED_SW));
    }

    private record StraightTrackModelSet(
        ModelFile flatModel,
        ModelFile raisedNorthEastModel,
        ModelFile raisedSouthWestModel) {

        private void apply (MultiPartBlockStateBuilder builder, Property<RailShape> shapeProperty,
            boolean includeRaised, boolean reversed) {
            apply(builder, shapeProperty, includeRaised, reversed, null);
        }

        private static PartBuilder conditions(ConditionGroup partialBuilder,
            @Nullable Condition conditions) {
            if(conditions instanceof CompositeCondition compositeCondition) {
                for(var sub : compositeCondition.subconditions) {
                    conditions(partialBuilder, sub);
                }
                return partialBuilder.end();
            } else if (conditions instanceof TerminalCondition terminalCondition) {
                for (Entry<Property<?>, String> set : terminalCondition.terms.entrySet()) {
                    var key = set.getKey();
                    var value = key.getValue(set.getValue());
                    value.ifPresent(v -> partialBuilder.conditions.put(key, v));
                }
            }
            return partialBuilder.end();
        }

        private void apply (MultiPartBlockStateBuilder builder, Property<RailShape> shapeProperty,
            boolean includeRaised, boolean reversed, @Nullable Condition conditions) {

            var partialBuilder = builder
                .part()
                .modelFile(flatModel).rotationY(reversed ? 180 : 0).addModel()
                .nestedGroup()
                .condition(shapeProperty, RailShape.NORTH_SOUTH);
            partialBuilder = conditions(partialBuilder, conditions).end()
                .part()
                .modelFile(flatModel).rotationY(reversed ? 270 : 90).addModel()
                .nestedGroup()
                .condition(shapeProperty, RailShape.EAST_WEST);
            builder = conditions(partialBuilder, conditions).end();

            if(includeRaised) {
                partialBuilder = builder
                    .part()
                    .modelFile(reversed ? raisedSouthWestModel : raisedNorthEastModel)
                    .rotationY(reversed ? 180 : 0).addModel()
                    .nestedGroup()
                    .condition(shapeProperty, RailShape.ASCENDING_NORTH);
                partialBuilder = conditions(partialBuilder, conditions).end()
                    .part()
                    .modelFile(reversed ? raisedNorthEastModel : raisedSouthWestModel)
                    .rotationY(reversed ? 180 : 0).addModel()
                    .nestedGroup()
                    .condition(shapeProperty, RailShape.ASCENDING_SOUTH);
                partialBuilder = conditions(partialBuilder, conditions).end()
                    .part()
                    .modelFile(reversed ? raisedSouthWestModel : raisedNorthEastModel)
                    .rotationY(reversed ? 270 : 90).addModel()
                    .nestedGroup()
                    .condition(shapeProperty, RailShape.ASCENDING_EAST);
                partialBuilder = conditions(partialBuilder, conditions).end()
                    .part()
                    .modelFile(reversed ? raisedNorthEastModel : raisedSouthWestModel)
                    .rotationY(reversed ? 270 : 90).addModel()
                    .nestedGroup()
                    .condition(shapeProperty, RailShape.ASCENDING_WEST);
                builder = conditions(partialBuilder, conditions).end();
            }
        }
    }
}
