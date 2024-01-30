package mods.railcraft.world.level.block;

import java.util.Collection;
import java.util.function.ToIntFunction;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.util.VariantSet;
import mods.railcraft.world.level.block.charge.EmptyBatteryBlock;
import mods.railcraft.world.level.block.charge.FrameBlock;
import mods.railcraft.world.level.block.charge.NickelIronBatteryBlock;
import mods.railcraft.world.level.block.charge.NickelZincBatteryBlock;
import mods.railcraft.world.level.block.charge.ZincCarbonBatteryBlock;
import mods.railcraft.world.level.block.charge.ZincSilverBatteryBlock;
import mods.railcraft.world.level.block.manipulator.AdvancedItemLoaderBlock;
import mods.railcraft.world.level.block.manipulator.AdvancedItemUnloaderBlock;
import mods.railcraft.world.level.block.manipulator.CartDispenserBlock;
import mods.railcraft.world.level.block.manipulator.FluidLoaderBlock;
import mods.railcraft.world.level.block.manipulator.FluidUnloaderBlock;
import mods.railcraft.world.level.block.manipulator.ItemLoaderBlock;
import mods.railcraft.world.level.block.manipulator.ItemUnloaderBlock;
import mods.railcraft.world.level.block.manipulator.TrainDispenserBlock;
import mods.railcraft.world.level.block.post.PostBlock;
import mods.railcraft.world.level.block.signal.AnalogSignalControllerBoxBlock;
import mods.railcraft.world.level.block.signal.BlockSignalBlock;
import mods.railcraft.world.level.block.signal.DistantSignalBlock;
import mods.railcraft.world.level.block.signal.DualBlockSignalBlock;
import mods.railcraft.world.level.block.signal.DualDistantSignalBlock;
import mods.railcraft.world.level.block.signal.DualTokenSignalBlock;
import mods.railcraft.world.level.block.signal.SignalBlockRelayBoxBlock;
import mods.railcraft.world.level.block.signal.SignalBoxBlock;
import mods.railcraft.world.level.block.signal.SignalCapacitorBoxBlock;
import mods.railcraft.world.level.block.signal.SignalControllerBoxBlock;
import mods.railcraft.world.level.block.signal.SignalInterlockBoxBlock;
import mods.railcraft.world.level.block.signal.SignalReceiverBoxBlock;
import mods.railcraft.world.level.block.signal.SignalSequencerBoxBlock;
import mods.railcraft.world.level.block.signal.TokenSignalBlock;
import mods.railcraft.world.level.block.steamboiler.FluidFueledFireboxBlock;
import mods.railcraft.world.level.block.steamboiler.SolidFueledFireboxBlock;
import mods.railcraft.world.level.block.steamboiler.SteamBoilerTankBlock;
import mods.railcraft.world.level.block.tank.IronTankGaugeBlock;
import mods.railcraft.world.level.block.tank.IronTankValveBlock;
import mods.railcraft.world.level.block.tank.IronTankWallBlock;
import mods.railcraft.world.level.block.tank.SteelTankGaugeBlock;
import mods.railcraft.world.level.block.tank.SteelTankValveBlock;
import mods.railcraft.world.level.block.tank.SteelTankWallBlock;
import mods.railcraft.world.level.block.track.AbandonedTrackBlock;
import mods.railcraft.world.level.block.track.ElectricTrackBlock;
import mods.railcraft.world.level.block.track.ElevatorTrackBlock;
import mods.railcraft.world.level.block.track.ForceTrackBlock;
import mods.railcraft.world.level.block.track.HighSpeedElectricTrackBlock;
import mods.railcraft.world.level.block.track.HighSpeedTrackBlock;
import mods.railcraft.world.level.block.track.ReinforcedTrackBlock;
import mods.railcraft.world.level.block.track.StrapIronTrackBlock;
import mods.railcraft.world.level.block.track.TrackConstants;
import mods.railcraft.world.level.block.track.TrackTypes;
import mods.railcraft.world.level.block.track.actuator.SwitchTrackActuatorBlock;
import mods.railcraft.world.level.block.track.actuator.SwitchTrackLeverBlock;
import mods.railcraft.world.level.block.track.actuator.SwitchTrackMotorBlock;
import mods.railcraft.world.level.block.track.actuator.SwitchTrackRouterBlock;
import mods.railcraft.world.level.block.track.outfitted.ActivatorTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.BoosterTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.BufferStopTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.ControlTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.CouplerTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.DetectorTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.DisembarkingTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.DumpingTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.EmbarkingTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.GatedTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.JunctionTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.LauncherTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.LockingTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.LocomotiveTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.OneWayTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.RoutingTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.ThrottleTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.TransitionTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.TurnoutTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.WhistleTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.WyeTrackBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RailcraftBlocks {

  private static final DeferredRegister.Blocks deferredRegister =
      DeferredRegister.createBlocks(RailcraftConstants.ID);

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }

  public static Collection<DeferredHolder<Block, ? extends Block>> entries() {
    return deferredRegister.getEntries();
  }

  public static final VariantSet<DyeColor, Block, StrengthenedGlassBlock> STRENGTHENED_GLASS =
      VariantSet.of(
          DyeColor.class,
          deferredRegister,
          "strengthened_glass",
          RailcraftBlocks::buildStrengthenedGlass);
  public static final VariantSet<DyeColor, Block, IronTankGaugeBlock> IRON_TANK_GAUGE =
      VariantSet.of(
          DyeColor.class,
          deferredRegister,
          "iron_tank_gauge",
          RailcraftBlocks::buildIronTankGauge);
  public static final VariantSet<DyeColor, Block, IronTankValveBlock> IRON_TANK_VALVE =
      VariantSet.of(
          DyeColor.class,
          deferredRegister,
          "iron_tank_valve",
          RailcraftBlocks::buildIronTankValve);
  public static final VariantSet<DyeColor, Block, IronTankWallBlock> IRON_TANK_WALL =
      VariantSet.of(
          DyeColor.class,
          deferredRegister,
          "iron_tank_wall",
          RailcraftBlocks::buildIronTankWall);
  public static final VariantSet<DyeColor, Block, SteelTankGaugeBlock> STEEL_TANK_GAUGE =
      VariantSet.of(
          DyeColor.class,
          deferredRegister,
          "steel_tank_gauge",
          RailcraftBlocks::buildSteelTankGauge);
  public static final VariantSet<DyeColor, Block, SteelTankValveBlock> STEEL_TANK_VALVE =
      VariantSet.of(
          DyeColor.class,
          deferredRegister,
          "steel_tank_valve",
          RailcraftBlocks::buildSteelTankValve);
  public static final VariantSet<DyeColor, Block, SteelTankWallBlock> STEEL_TANK_WALL =
      VariantSet.of(
          DyeColor.class,
          deferredRegister,
          "steel_tank_wall",
          RailcraftBlocks::buildSteelTankWall);
  public static final VariantSet<DyeColor, Block, PostBlock> POST =
      VariantSet.of(
          DyeColor.class,
          deferredRegister,
          "post",
          RailcraftBlocks::buildPost);

  private static StrengthenedGlassBlock buildStrengthenedGlass() {
    return new StrengthenedGlassBlock(BlockBehaviour.Properties.of()
        .instrument(NoteBlockInstrument.HAT)
        .sound(SoundType.GLASS)
        .noOcclusion()
        .strength(1.0F, 5.0F)
        .requiresCorrectToolForDrops()
        .isValidSpawn(RailcraftBlocks::never)
        .isRedstoneConductor(RailcraftBlocks::never)
        .isSuffocating(RailcraftBlocks::never)
        .isViewBlocking(RailcraftBlocks::never));
  }

  private static IronTankGaugeBlock buildIronTankGauge() {
    return new IronTankGaugeBlock(BlockBehaviour.Properties.of()
        .instrument(NoteBlockInstrument.HAT)
        .sound(SoundType.GLASS)
        .noOcclusion()
        .strength(1.0F, 5.0F)
        .requiresCorrectToolForDrops()
        .isValidSpawn(RailcraftBlocks::never)
        .isRedstoneConductor(RailcraftBlocks::never)
        .isSuffocating(RailcraftBlocks::never)
        .isViewBlocking(RailcraftBlocks::never)
        .lightLevel(LightBlock.LIGHT_EMISSION));
  }

  private static IronTankValveBlock buildIronTankValve() {
    return new IronTankValveBlock(BlockBehaviour.Properties.of()
        .mapColor(MapColor.METAL)
        .sound(SoundType.METAL)
        .noOcclusion()
        .strength(1.0F, 5.0F)
        .requiresCorrectToolForDrops()
        .explosionResistance(12));
  }

  private static IronTankWallBlock buildIronTankWall() {
    return new IronTankWallBlock(BlockBehaviour.Properties.of()
        .mapColor(MapColor.METAL)
        .sound(SoundType.METAL)
        .noOcclusion()
        .strength(1.0F, 5.0F)
        .requiresCorrectToolForDrops()
        .explosionResistance(12));
  }

  private static SteelTankGaugeBlock buildSteelTankGauge() {
    return new SteelTankGaugeBlock(BlockBehaviour.Properties.of()
        .instrument(NoteBlockInstrument.HAT)
        .sound(SoundType.GLASS)
        .noOcclusion()
        .strength(1.5F, 6.0F)
        .requiresCorrectToolForDrops()
        .isValidSpawn(RailcraftBlocks::never)
        .isRedstoneConductor(RailcraftBlocks::never)
        .isSuffocating(RailcraftBlocks::never)
        .isViewBlocking(RailcraftBlocks::never)
        .lightLevel(LightBlock.LIGHT_EMISSION));
  }

  private static SteelTankValveBlock buildSteelTankValve() {
    return new SteelTankValveBlock(BlockBehaviour.Properties.of()
        .mapColor(MapColor.METAL)
        .sound(SoundType.METAL)
        .noOcclusion()
        .strength(1.5F, 6.0F)
        .requiresCorrectToolForDrops()
        .explosionResistance(15));
  }

  private static SteelTankWallBlock buildSteelTankWall() {
    return new SteelTankWallBlock(BlockBehaviour.Properties.of()
        .mapColor(MapColor.METAL)
        .sound(SoundType.METAL)
        .noOcclusion()
        .strength(1.5F, 6.0F)
        .requiresCorrectToolForDrops()
        .explosionResistance(15));
  }

  private static PostBlock buildPost() {
    return new PostBlock(BlockBehaviour.Properties.of()
        .mapColor(MapColor.METAL)
        .strength(2.0F, 3.0F)
        .requiresCorrectToolForDrops()
        .sound(SoundType.METAL));
  }

  public static final DeferredBlock<SteamBoilerTankBlock> LOW_PRESSURE_STEAM_BOILER_TANK =
      deferredRegister.register("low_pressure_steam_boiler_tank",
          () -> new SteamBoilerTankBlock(BlockBehaviour.Properties.of()
              .mapColor(MapColor.METAL)
              .strength(5.0F, 6.0F)
              .requiresCorrectToolForDrops()
              .noOcclusion()
              .sound(SoundType.METAL)));

  public static final DeferredBlock<SteamBoilerTankBlock> HIGH_PRESSURE_STEAM_BOILER_TANK =
      deferredRegister.register("high_pressure_steam_boiler_tank",
          () -> new SteamBoilerTankBlock(
              BlockBehaviour.Properties.ofFullCopy(LOW_PRESSURE_STEAM_BOILER_TANK.get())));

  public static final DeferredBlock<SolidFueledFireboxBlock> SOLID_FUELED_FIREBOX =
      deferredRegister.register("solid_fueled_firebox",
          () -> new SolidFueledFireboxBlock(BlockBehaviour.Properties.of()
              .mapColor(MapColor.STONE)
              .strength(3.5F)
              .requiresCorrectToolForDrops()
              .lightLevel(litBlockEmission(13))
              .sound(SoundType.METAL)));

  public static final DeferredBlock<FluidFueledFireboxBlock> FLUID_FUELED_FIREBOX =
      deferredRegister.register("fluid_fueled_firebox",
          () -> new FluidFueledFireboxBlock(
              BlockBehaviour.Properties.ofFullCopy(SOLID_FUELED_FIREBOX.get())));

  public static final DeferredBlock<SteamTurbineBlock> STEAM_TURBINE =
      deferredRegister.register("steam_turbine",
          () -> new SteamTurbineBlock(BlockBehaviour.Properties.of()
              .mapColor(MapColor.METAL)
              .strength(3.5F)
              .requiresCorrectToolForDrops()
              .randomTicks()
              .sound(SoundType.METAL)));

  public static final DeferredBlock<WaterTankSiding> WATER_TANK_SIDING =
      deferredRegister.register("water_tank_siding",
          () -> new WaterTankSiding(BlockBehaviour.Properties.of()
              .mapColor(MapColor.WOOD)
              .strength(2.0F, 3.0F)
              .sound(SoundType.WOOD)
              .requiresCorrectToolForDrops()));

  public static final DeferredBlock<BlastFurnaceBricksBlock> BLAST_FURNACE_BRICKS =
      deferredRegister.register("blast_furnace_bricks",
          () -> new BlastFurnaceBricksBlock(BlockBehaviour.Properties.of()
              .mapColor(MapColor.STONE)
              .strength(3.5F)
              .requiresCorrectToolForDrops()
              .lightLevel(litBlockEmission(13))
              .sound(SoundType.STONE)));

  public static final DeferredBlock<FeedStationBlock> FEED_STATION =
      deferredRegister.register("feed_station",
          () -> new FeedStationBlock(
              BlockBehaviour.Properties.of()
                  .mapColor(MapColor.WOOD)
                  .strength(1F)
                  .requiresCorrectToolForDrops()
                  .sound(SoundType.WOOD)));

  public static final DeferredBlock<LogBookBlock> LOGBOOK =
      deferredRegister.register("logbook",
          () -> new LogBookBlock(
              BlockBehaviour.Properties.of()
                  .mapColor(MapColor.WOOD)
                  .strength(3F)
                  .requiresCorrectToolForDrops()
                  .sound(SoundType.WOOD)));

  public static final DeferredBlock<FrameBlock> FRAME =
      deferredRegister.register("frame",
          () -> new FrameBlock(
              BlockBehaviour.Properties.of()
                  .mapColor(MapColor.METAL)
                  .strength(3F)
                  .randomTicks()
                  .requiresCorrectToolForDrops()
                  .noOcclusion()
                  .sound(SoundType.METAL)));

  public static final DeferredBlock<NickelZincBatteryBlock> NICKEL_ZINC_BATTERY =
      deferredRegister.register("nickel_zinc_battery",
          () -> new NickelZincBatteryBlock(
              BlockBehaviour.Properties.of()
                  .mapColor(MapColor.METAL)
                  .strength(3F)
                  .randomTicks()
                  .requiresCorrectToolForDrops()
                  .sound(SoundType.METAL)));

  public static final DeferredBlock<NickelIronBatteryBlock> NICKEL_IRON_BATTERY =
      deferredRegister.register("nickel_iron_battery",
          () -> new NickelIronBatteryBlock(
              BlockBehaviour.Properties.ofFullCopy(NICKEL_ZINC_BATTERY.get())));

  public static final DeferredBlock<ZincCarbonBatteryBlock> ZINC_CARBON_BATTERY =
      deferredRegister.register("zinc_carbon_battery",
          () -> new ZincCarbonBatteryBlock(
              BlockBehaviour.Properties.ofFullCopy(NICKEL_ZINC_BATTERY.get())));

  public static final DeferredBlock<EmptyBatteryBlock> ZINC_CARBON_BATTERY_EMPTY =
      deferredRegister.register("zinc_carbon_battery_empty",
          () -> new EmptyBatteryBlock(
              BlockBehaviour.Properties.ofFullCopy(NICKEL_ZINC_BATTERY.get())));

  public static final DeferredBlock<ZincSilverBatteryBlock> ZINC_SILVER_BATTERY =
      deferredRegister.register("zinc_silver_battery",
          () -> new ZincSilverBatteryBlock(
              BlockBehaviour.Properties.ofFullCopy(NICKEL_ZINC_BATTERY.get())));

  public static final DeferredBlock<EmptyBatteryBlock> ZINC_SILVER_BATTERY_EMPTY =
      deferredRegister.register("zinc_silver_battery_empty",
          () -> new EmptyBatteryBlock(
              BlockBehaviour.Properties.ofFullCopy(NICKEL_ZINC_BATTERY.get())));

  public static final DeferredBlock<AnvilBlock> STEEL_ANVIL =
      deferredRegister.register("steel_anvil",
          () -> new AnvilBlock(
              BlockBehaviour.Properties.of()
                  .mapColor(MapColor.METAL)
                  .pushReaction(PushReaction.BLOCK)
                  .requiresCorrectToolForDrops()
                  .strength(5.0F, 2000.0F)
                  .sound(SoundType.ANVIL)));

  public static final DeferredBlock<AnvilBlock> CHIPPED_STEEL_ANVIL =
      deferredRegister.register("chipped_steel_anvil",
          () -> new AnvilBlock(BlockBehaviour.Properties.ofFullCopy(STEEL_ANVIL.get())));

  public static final DeferredBlock<AnvilBlock> DAMAGED_STEEL_ANVIL =
      deferredRegister.register("damaged_steel_anvil",
          () -> new AnvilBlock(BlockBehaviour.Properties.ofFullCopy(STEEL_ANVIL.get())));

  public static final DeferredBlock<Block> STEEL_BLOCK =
      deferredRegister.register("steel_block",
          () -> new Block(BlockBehaviour.Properties.of()
              .mapColor(MapColor.METAL)
              .strength(5.0F, 15.0F)
              .sound(SoundType.METAL)
              .requiresCorrectToolForDrops()));

  public static final DeferredBlock<Block> BRASS_BLOCK =
      deferredRegister.register("brass_block",
          () -> new Block(BlockBehaviour.Properties.of()
              .mapColor(MapColor.METAL)
              .strength(3.0F, 6.0F)
              .sound(SoundType.METAL)
              .requiresCorrectToolForDrops()));

  public static final DeferredBlock<Block> BRONZE_BLOCK =
      deferredRegister.register("bronze_block",
          () -> new Block(BlockBehaviour.Properties.of()
              .mapColor(MapColor.METAL)
              .strength(3.0F, 6.0F)
              .sound(SoundType.METAL)
              .requiresCorrectToolForDrops()));

  public static final DeferredBlock<Block> INVAR_BLOCK =
      deferredRegister.register("invar_block",
          () -> new Block(BlockBehaviour.Properties.of()
              .mapColor(MapColor.METAL)
              .strength(5.0F, 6.0F)
              .sound(SoundType.METAL)
              .requiresCorrectToolForDrops()));

  public static final DeferredBlock<Block> LEAD_BLOCK =
      deferredRegister.register("lead_block",
          () -> new Block(BlockBehaviour.Properties.of()
              .mapColor(MapColor.METAL)
              .strength(5.0F, 6.0F)
              .sound(SoundType.METAL)
              .requiresCorrectToolForDrops()));

  public static final DeferredBlock<Block> NICKEL_BLOCK =
      deferredRegister.register("nickel_block",
          () -> new Block(BlockBehaviour.Properties.of()
              .mapColor(MapColor.METAL)
              .strength(5.0F, 6.0F)
              .sound(SoundType.METAL)
              .requiresCorrectToolForDrops()));

  public static final DeferredBlock<Block> SILVER_BLOCK =
      deferredRegister.register("silver_block",
          () -> new Block(BlockBehaviour.Properties.of()
              .mapColor(MapColor.METAL)
              .strength(3.0F, 6.0F)
              .sound(SoundType.METAL)
              .requiresCorrectToolForDrops()));

  public static final DeferredBlock<Block> TIN_BLOCK =
      deferredRegister.register("tin_block",
          () -> new Block(BlockBehaviour.Properties.of()
              .mapColor(MapColor.METAL)
              .strength(3.0F, 6.0F)
              .sound(SoundType.METAL)
              .requiresCorrectToolForDrops()));

  public static final DeferredBlock<Block> ZINC_BLOCK =
      deferredRegister.register("zinc_block",
          () -> new Block(BlockBehaviour.Properties.of()
              .mapColor(MapColor.METAL)
              .strength(5.0F, 15.0F)
              .requiresCorrectToolForDrops()));

  public static final DeferredBlock<Block> LEAD_ORE =
      deferredRegister.register("lead_ore",
          () -> new DropExperienceBlock(ConstantInt.of(0), BlockBehaviour.Properties.of()
              .mapColor(MapColor.STONE)
              .instrument(NoteBlockInstrument.BASEDRUM)
              .strength(3.0F, 3.0F)
              .requiresCorrectToolForDrops()));

  public static final DeferredBlock<Block> DEEPSLATE_LEAD_ORE =
      deferredRegister.register("deepslate_lead_ore",
          () -> new DropExperienceBlock(ConstantInt.of(0),
              BlockBehaviour.Properties.ofFullCopy(LEAD_ORE.get())
                  .mapColor(MapColor.DEEPSLATE)
                  .strength(4.5F, 3.0F)
                  .sound(SoundType.DEEPSLATE)));

  public static final DeferredBlock<Block> NICKEL_ORE =
      deferredRegister.register("nickel_ore",
          () -> new DropExperienceBlock(ConstantInt.of(0), BlockBehaviour.Properties.of()
              .mapColor(MapColor.STONE)
              .instrument(NoteBlockInstrument.BASEDRUM)
              .strength(3.0F, 3.0F)
              .requiresCorrectToolForDrops()));

  public static final DeferredBlock<Block> DEEPSLATE_NICKEL_ORE =
      deferredRegister.register("deepslate_nickel_ore",
          () -> new DropExperienceBlock(ConstantInt.of(0),
              BlockBehaviour.Properties.ofFullCopy(NICKEL_ORE.get())
                  .mapColor(MapColor.DEEPSLATE)
                  .strength(4.5F, 3.0F)
                  .sound(SoundType.DEEPSLATE)));

  public static final DeferredBlock<Block> SILVER_ORE =
      deferredRegister.register("silver_ore",
          () -> new DropExperienceBlock(ConstantInt.of(0), BlockBehaviour.Properties.of()
              .mapColor(MapColor.STONE)
              .instrument(NoteBlockInstrument.BASEDRUM)
              .strength(3.0F, 3.0F)
              .requiresCorrectToolForDrops()));

  public static final DeferredBlock<Block> DEEPSLATE_SILVER_ORE =
      deferredRegister.register("deepslate_silver_ore",
          () -> new DropExperienceBlock(ConstantInt.of(0),
              BlockBehaviour.Properties.ofFullCopy(SILVER_ORE.get())
                  .mapColor(MapColor.DEEPSLATE)
                  .strength(4.5F, 3.0F)
                  .sound(SoundType.DEEPSLATE)));

  public static final DeferredBlock<Block> SULFUR_ORE =
      deferredRegister.register("sulfur_ore",
          () -> new DropExperienceBlock(UniformInt.of(2, 5), BlockBehaviour.Properties.of()
              .mapColor(MapColor.STONE)
              .instrument(NoteBlockInstrument.BASEDRUM)
              .strength(3.0F, 3.0F)
              .requiresCorrectToolForDrops()));

  public static final DeferredBlock<Block> DEEPSLATE_SULFUR_ORE =
      deferredRegister.register("deepslate_sulfur_ore",
          () -> new DropExperienceBlock(UniformInt.of(2, 5),
              BlockBehaviour.Properties.ofFullCopy(SULFUR_ORE.get())
                  .mapColor(MapColor.DEEPSLATE)
                  .strength(4.5F, 3.0F)
                  .sound(SoundType.DEEPSLATE)));

  public static final DeferredBlock<Block> TIN_ORE =
      deferredRegister.register("tin_ore",
          () -> new DropExperienceBlock(ConstantInt.of(0), BlockBehaviour.Properties.of()
              .mapColor(MapColor.STONE)
              .instrument(NoteBlockInstrument.BASEDRUM)
              .strength(3.0F, 3.0F)
              .requiresCorrectToolForDrops()));

  public static final DeferredBlock<Block> DEEPSLATE_TIN_ORE =
      deferredRegister.register("deepslate_tin_ore",
          () -> new DropExperienceBlock(ConstantInt.of(0),
              BlockBehaviour.Properties.ofFullCopy(TIN_ORE.get())
                  .mapColor(MapColor.DEEPSLATE)
                  .strength(4.5F, 3.0F)
                  .sound(SoundType.DEEPSLATE)));

  public static final DeferredBlock<Block> ZINC_ORE =
      deferredRegister.register("zinc_ore",
          () -> new DropExperienceBlock(ConstantInt.of(0), BlockBehaviour.Properties.of()
              .mapColor(MapColor.STONE)
              .instrument(NoteBlockInstrument.BASEDRUM)
              .strength(3.0F, 3.0F)
              .requiresCorrectToolForDrops()));

  public static final DeferredBlock<Block> DEEPSLATE_ZINC_ORE =
      deferredRegister.register("deepslate_zinc_ore",
          () -> new DropExperienceBlock(ConstantInt.of(0),
              BlockBehaviour.Properties.ofFullCopy(ZINC_ORE.get())
                  .mapColor(MapColor.DEEPSLATE)
                  .strength(4.5F, 3.0F)
                  .sound(SoundType.DEEPSLATE)));

  public static final DeferredBlock<Block> COKE_BLOCK =
      deferredRegister.register("coal_coke_block",
          () -> new CoalCokeBlock(5, 300, BlockBehaviour.Properties.of()
              .mapColor(MapColor.COLOR_BLACK)
              .instrument(NoteBlockInstrument.BASEDRUM)
              .strength(5.0F, 10.0F)
              .sound(SoundType.STONE)));

  public static final DeferredBlock<Block> SALTPETER_ORE =
      deferredRegister.register("saltpeter_ore",
          () -> new DropExperienceBlock(UniformInt.of(2, 5), BlockBehaviour.Properties.of()
              .mapColor(MapColor.SAND)
              .instrument(NoteBlockInstrument.SNARE)
              .requiresCorrectToolForDrops()
              .strength(0.8F)
              .sound(SoundType.SAND)));

  public static final DeferredBlock<FluidLoaderBlock> FLUID_LOADER =
      deferredRegister.register("fluid_loader",
          () -> new FluidLoaderBlock(BlockBehaviour.Properties.of()
              .mapColor(MapColor.STONE)
              .strength(3.5F)
              .requiresCorrectToolForDrops()
              .sound(SoundType.STONE)
              .noOcclusion()));

  public static final DeferredBlock<FluidUnloaderBlock> FLUID_UNLOADER =
      deferredRegister.register("fluid_unloader",
          () -> new FluidUnloaderBlock(BlockBehaviour.Properties.ofFullCopy(FLUID_LOADER.get())));

  public static final DeferredBlock<AdvancedItemLoaderBlock> ADVANCED_ITEM_LOADER =
      deferredRegister.register("advanced_item_loader",
          () -> new AdvancedItemLoaderBlock(BlockBehaviour.Properties.of()
              .mapColor(MapColor.STONE)
              .strength(3.5F)
              .requiresCorrectToolForDrops()
              .sound(SoundType.STONE)));

  public static final DeferredBlock<AdvancedItemUnloaderBlock> ADVANCED_ITEM_UNLOADER =
      deferredRegister.register("advanced_item_unloader",
          () -> new AdvancedItemUnloaderBlock(
              BlockBehaviour.Properties.ofFullCopy(ADVANCED_ITEM_LOADER.get())));

  public static final DeferredBlock<ItemLoaderBlock> ITEM_LOADER =
      deferredRegister.register("item_loader",
          () -> new ItemLoaderBlock(
              BlockBehaviour.Properties.ofFullCopy(ADVANCED_ITEM_LOADER.get())));

  public static final DeferredBlock<ItemUnloaderBlock> ITEM_UNLOADER =
      deferredRegister.register("item_unloader",
          () -> new ItemUnloaderBlock(
              BlockBehaviour.Properties.ofFullCopy(ADVANCED_ITEM_LOADER.get())));

  public static final DeferredBlock<CartDispenserBlock> CART_DISPENSER =
      deferredRegister.register("cart_dispenser",
          () -> new CartDispenserBlock(
              BlockBehaviour.Properties.ofFullCopy(ADVANCED_ITEM_LOADER.get())));

  public static final DeferredBlock<TrainDispenserBlock> TRAIN_DISPENSER =
      deferredRegister.register("train_dispenser",
          () -> new TrainDispenserBlock(
              BlockBehaviour.Properties.ofFullCopy(ADVANCED_ITEM_LOADER.get())));

  public static final DeferredBlock<SwitchTrackActuatorBlock> SWITCH_TRACK_LEVER =
      deferredRegister.register("switch_track_lever",
          () -> new SwitchTrackLeverBlock(BlockBehaviour.Properties.of()
              .strength(8.0F, 50.0F)
              .sound(SoundType.METAL)
              .noOcclusion()));

  public static final DeferredBlock<SwitchTrackActuatorBlock> SWITCH_TRACK_MOTOR =
      deferredRegister.register("switch_track_motor",
          () -> new SwitchTrackMotorBlock(
              BlockBehaviour.Properties.ofFullCopy(SWITCH_TRACK_LEVER.get())));

  public static final DeferredBlock<SwitchTrackRouterBlock> SWITCH_TRACK_ROUTER =
      deferredRegister.register("switch_track_router",
          () -> new SwitchTrackRouterBlock(
              BlockBehaviour.Properties.ofFullCopy(SWITCH_TRACK_LEVER.get())));

  public static final DeferredBlock<SignalBoxBlock> ANALOG_SIGNAL_CONTROLLER_BOX =
      deferredRegister.register("analog_signal_controller_box",
          () -> new AnalogSignalControllerBoxBlock(
              BlockBehaviour.Properties.ofFullCopy(SWITCH_TRACK_LEVER.get())));

  public static final DeferredBlock<SignalBoxBlock> SIGNAL_SEQUENCER_BOX =
      deferredRegister.register("signal_sequencer_box",
          () -> new SignalSequencerBoxBlock(
              BlockBehaviour.Properties.ofFullCopy(SWITCH_TRACK_LEVER.get())));

  public static final DeferredBlock<SignalBoxBlock> SIGNAL_CAPACITOR_BOX =
      deferredRegister.register("signal_capacitor_box",
          () -> new SignalCapacitorBoxBlock(
              BlockBehaviour.Properties.ofFullCopy(SWITCH_TRACK_LEVER.get())));

  public static final DeferredBlock<SignalBoxBlock> SIGNAL_INTERLOCK_BOX =
      deferredRegister.register("signal_interlock_box",
          () -> new SignalInterlockBoxBlock(
              BlockBehaviour.Properties.ofFullCopy(SWITCH_TRACK_LEVER.get())));

  public static final DeferredBlock<SignalBoxBlock> SIGNAL_BLOCK_RELAY_BOX =
      deferredRegister.register("signal_block_relay_box",
          () -> new SignalBlockRelayBoxBlock(
              BlockBehaviour.Properties.ofFullCopy(SWITCH_TRACK_LEVER.get())));

  public static final DeferredBlock<SignalBoxBlock> SIGNAL_RECEIVER_BOX =
      deferredRegister.register("signal_receiver_box",
          () -> new SignalReceiverBoxBlock(
              BlockBehaviour.Properties.ofFullCopy(SWITCH_TRACK_LEVER.get())));

  public static final DeferredBlock<SignalBoxBlock> SIGNAL_CONTROLLER_BOX =
      deferredRegister.register("signal_controller_box",
          () -> new SignalControllerBoxBlock(
              BlockBehaviour.Properties.ofFullCopy(SWITCH_TRACK_LEVER.get())));

  public static final DeferredBlock<DualBlockSignalBlock> DUAL_BLOCK_SIGNAL =
      deferredRegister.register("dual_block_signal",
          () -> new DualBlockSignalBlock(
              BlockBehaviour.Properties.ofFullCopy(SWITCH_TRACK_LEVER.get())));

  public static final DeferredBlock<DualDistantSignalBlock> DUAL_DISTANT_SIGNAL =
      deferredRegister.register("dual_distant_signal",
          () -> new DualDistantSignalBlock(
              BlockBehaviour.Properties.ofFullCopy(SWITCH_TRACK_LEVER.get())));

  public static final DeferredBlock<DualTokenSignalBlock> DUAL_TOKEN_SIGNAL =
      deferredRegister.register("dual_token_signal",
          () -> new DualTokenSignalBlock(
              BlockBehaviour.Properties.ofFullCopy(SWITCH_TRACK_LEVER.get())));

  public static final DeferredBlock<BlockSignalBlock> BLOCK_SIGNAL =
      deferredRegister.register("block_signal",
          () -> new BlockSignalBlock(
              BlockBehaviour.Properties.ofFullCopy(SWITCH_TRACK_LEVER.get())));

  public static final DeferredBlock<DistantSignalBlock> DISTANT_SIGNAL =
      deferredRegister.register("distant_signal",
          () -> new DistantSignalBlock(
              BlockBehaviour.Properties.ofFullCopy(SWITCH_TRACK_LEVER.get())));

  public static final DeferredBlock<TokenSignalBlock> TOKEN_SIGNAL =
      deferredRegister.register("token_signal",
          () -> new TokenSignalBlock(
              BlockBehaviour.Properties.ofFullCopy(SWITCH_TRACK_LEVER.get())));

  public static final DeferredBlock<ForceTrackBlock> FORCE_TRACK =
      deferredRegister.register("force_track",
          () -> new ForceTrackBlock(BlockBehaviour.Properties.of()
              .sound(SoundType.GLASS)
              .instabreak()
              .noCollission()
              .randomTicks()
              .noLootTable()));

  public static final DeferredBlock<ForceTrackEmitterBlock> FORCE_TRACK_EMITTER =
      deferredRegister.register("force_track_emitter",
          () -> new ForceTrackEmitterBlock(BlockBehaviour.Properties.of()
              .mapColor(MapColor.METAL)
              .requiresCorrectToolForDrops()
              .strength(1.5F, 6)
              .sound(SoundType.METAL)
              .randomTicks()));

  public static final DeferredBlock<AbandonedTrackBlock> ABANDONED_TRACK =
      deferredRegister.register("abandoned_track",
          () -> new AbandonedTrackBlock(BlockBehaviour.Properties.of()
              .noCollission()
              .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
              .sound(SoundType.METAL)));

  public static final DeferredBlock<LockingTrackBlock> ABANDONED_LOCKING_TRACK =
      deferredRegister.register("abandoned_locking_track",
          () -> new LockingTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.ofFullCopy(ABANDONED_TRACK.get())));

  public static final DeferredBlock<BufferStopTrackBlock> ABANDONED_BUFFER_STOP_TRACK =
      deferredRegister.register("abandoned_buffer_stop_track",
          () -> new BufferStopTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.ofFullCopy(ABANDONED_TRACK.get())));

  public static final DeferredBlock<ActivatorTrackBlock> ABANDONED_ACTIVATOR_TRACK =
      deferredRegister.register("abandoned_activator_track",
          () -> new ActivatorTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.ofFullCopy(ABANDONED_TRACK.get())));

  public static final DeferredBlock<BoosterTrackBlock> ABANDONED_BOOSTER_TRACK =
      deferredRegister.register("abandoned_booster_track",
          () -> new BoosterTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.ofFullCopy(ABANDONED_TRACK.get())));

  public static final DeferredBlock<ControlTrackBlock> ABANDONED_CONTROL_TRACK =
      deferredRegister.register("abandoned_control_track",
          () -> new ControlTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.ofFullCopy(ABANDONED_TRACK.get())));

  public static final DeferredBlock<GatedTrackBlock> ABANDONED_GATED_TRACK =
      deferredRegister.register("abandoned_gated_track",
          () -> new GatedTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.ofFullCopy(ABANDONED_TRACK.get())));

  public static final DeferredBlock<DetectorTrackBlock> ABANDONED_DETECTOR_TRACK =
      deferredRegister.register("abandoned_detector_track",
          () -> new DetectorTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.ofFullCopy(ABANDONED_TRACK.get())));

  public static final DeferredBlock<CouplerTrackBlock> ABANDONED_COUPLER_TRACK =
      deferredRegister.register("abandoned_coupler_track",
          () -> new CouplerTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.ofFullCopy(ABANDONED_TRACK.get())));

  public static final DeferredBlock<EmbarkingTrackBlock> ABANDONED_EMBARKING_TRACK =
      deferredRegister.register("abandoned_embarking_track",
          () -> new EmbarkingTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.ofFullCopy(ABANDONED_TRACK.get())));

  public static final DeferredBlock<DisembarkingTrackBlock> ABANDONED_DISEMBARKING_TRACK =
      deferredRegister.register("abandoned_disembarking_track",
          () -> new DisembarkingTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.ofFullCopy(ABANDONED_TRACK.get())));

  public static final DeferredBlock<DumpingTrackBlock> ABANDONED_DUMPING_TRACK =
      deferredRegister.register("abandoned_dumping_track",
          () -> new DumpingTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.ofFullCopy(ABANDONED_TRACK.get())));

  public static final DeferredBlock<WyeTrackBlock> ABANDONED_WYE_TRACK =
      deferredRegister.register("abandoned_wye_track",
          () -> new WyeTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.ofFullCopy(ABANDONED_TRACK.get()).noLootTable()));
  public static final DeferredBlock<TurnoutTrackBlock> ABANDONED_TURNOUT_TRACK =
      deferredRegister.register("abandoned_turnout_track",
          () -> new TurnoutTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.ofFullCopy(ABANDONED_TRACK.get()).noLootTable()));

  public static final DeferredBlock<JunctionTrackBlock> ABANDONED_JUNCTION_TRACK =
      deferredRegister.register("abandoned_junction_track",
          () -> new JunctionTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.ofFullCopy(ABANDONED_TRACK.get()).noLootTable()));

  public static final DeferredBlock<LauncherTrackBlock> ABANDONED_LAUNCHER_TRACK =
      deferredRegister.register("abandoned_launcher_track",
          () -> new LauncherTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.ofFullCopy(ABANDONED_TRACK.get())));

  public static final DeferredBlock<OneWayTrackBlock> ABANDONED_ONE_WAY_TRACK =
      deferredRegister.register("abandoned_one_way_track",
          () -> new OneWayTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.ofFullCopy(ABANDONED_TRACK.get())));

  public static final DeferredBlock<WhistleTrackBlock> ABANDONED_WHISTLE_TRACK =
      deferredRegister.register("abandoned_whistle_track",
          () -> new WhistleTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.ofFullCopy(ABANDONED_TRACK.get())));

  public static final DeferredBlock<LocomotiveTrackBlock> ABANDONED_LOCOMOTIVE_TRACK =
      deferredRegister.register("abandoned_locomotive_track",
          () -> new LocomotiveTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.ofFullCopy(ABANDONED_TRACK.get())));

  public static final DeferredBlock<ThrottleTrackBlock> ABANDONED_THROTTLE_TRACK =
      deferredRegister.register("abandoned_throttle_track",
          () -> new ThrottleTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.ofFullCopy(ABANDONED_TRACK.get())));

  public static final DeferredBlock<RoutingTrackBlock> ABANDONED_ROUTING_TRACK =
      deferredRegister.register("abandoned_routing_track",
          () -> new RoutingTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.ofFullCopy(ABANDONED_TRACK.get())));

  public static final DeferredBlock<ElectricTrackBlock> ELECTRIC_TRACK =
      deferredRegister.register("electric_track",
          () -> new ElectricTrackBlock(BlockBehaviour.Properties.of()
              .randomTicks()
              .noCollission()
              .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
              .sound(SoundType.METAL)));

  public static final DeferredBlock<LockingTrackBlock> ELECTRIC_LOCKING_TRACK =
      deferredRegister.register("electric_locking_track",
          () -> new LockingTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.ofFullCopy(ELECTRIC_TRACK.get())));

  public static final DeferredBlock<BufferStopTrackBlock> ELECTRIC_BUFFER_STOP_TRACK =
      deferredRegister.register("electric_buffer_stop_track",
          () -> new BufferStopTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.ofFullCopy(ELECTRIC_TRACK.get())));

  public static final DeferredBlock<ActivatorTrackBlock> ELECTRIC_ACTIVATOR_TRACK =
      deferredRegister.register("electric_activator_track",
          () -> new ActivatorTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.ofFullCopy(ELECTRIC_TRACK.get())));

  public static final DeferredBlock<BoosterTrackBlock> ELECTRIC_BOOSTER_TRACK =
      deferredRegister.register("electric_booster_track",
          () -> new BoosterTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.ofFullCopy(ELECTRIC_TRACK.get())));

  public static final DeferredBlock<ControlTrackBlock> ELECTRIC_CONTROL_TRACK =
      deferredRegister.register("electric_control_track",
          () -> new ControlTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.ofFullCopy(ELECTRIC_TRACK.get())));

  public static final DeferredBlock<GatedTrackBlock> ELECTRIC_GATED_TRACK =
      deferredRegister.register("electric_gated_track",
          () -> new GatedTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.ofFullCopy(ELECTRIC_TRACK.get())));

  public static final DeferredBlock<DetectorTrackBlock> ELECTRIC_DETECTOR_TRACK =
      deferredRegister.register("electric_detector_track",
          () -> new DetectorTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.ofFullCopy(ELECTRIC_TRACK.get())));

  public static final DeferredBlock<CouplerTrackBlock> ELECTRIC_COUPLER_TRACK =
      deferredRegister.register("electric_coupler_track",
          () -> new CouplerTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.ofFullCopy(ELECTRIC_TRACK.get())));

  public static final DeferredBlock<EmbarkingTrackBlock> ELECTRIC_EMBARKING_TRACK =
      deferredRegister.register("electric_embarking_track",
          () -> new EmbarkingTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.ofFullCopy(ELECTRIC_TRACK.get())));

  public static final DeferredBlock<DisembarkingTrackBlock> ELECTRIC_DISEMBARKING_TRACK =
      deferredRegister.register("electric_disembarking_track",
          () -> new DisembarkingTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.ofFullCopy(ELECTRIC_TRACK.get())));

  public static final DeferredBlock<DumpingTrackBlock> ELECTRIC_DUMPING_TRACK =
      deferredRegister.register("electric_dumping_track",
          () -> new DumpingTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.ofFullCopy(ELECTRIC_TRACK.get())));

  public static final DeferredBlock<WyeTrackBlock> ELECTRIC_WYE_TRACK =
      deferredRegister.register("electric_wye_track",
          () -> new WyeTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.ofFullCopy(ELECTRIC_TRACK.get()).noLootTable()));

  public static final DeferredBlock<TurnoutTrackBlock> ELECTRIC_TURNOUT_TRACK =
      deferredRegister.register("electric_turnout_track",
          () -> new TurnoutTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.ofFullCopy(ELECTRIC_TRACK.get()).noLootTable()));

  public static final DeferredBlock<JunctionTrackBlock> ELECTRIC_JUNCTION_TRACK =
      deferredRegister.register("electric_junction_track",
          () -> new JunctionTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.ofFullCopy(ELECTRIC_TRACK.get()).noLootTable()));

  public static final DeferredBlock<LauncherTrackBlock> ELECTRIC_LAUNCHER_TRACK =
      deferredRegister.register("electric_launcher_track",
          () -> new LauncherTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.ofFullCopy(ELECTRIC_TRACK.get())));

  public static final DeferredBlock<OneWayTrackBlock> ELECTRIC_ONE_WAY_TRACK =
      deferredRegister.register("electric_one_way_track",
          () -> new OneWayTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.ofFullCopy(ELECTRIC_TRACK.get())));

  public static final DeferredBlock<WhistleTrackBlock> ELECTRIC_WHISTLE_TRACK =
      deferredRegister.register("electric_whistle_track",
          () -> new WhistleTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.ofFullCopy(ELECTRIC_TRACK.get())));
  public static final DeferredBlock<LocomotiveTrackBlock> ELECTRIC_LOCOMOTIVE_TRACK =
      deferredRegister.register("electric_locomotive_track",
          () -> new LocomotiveTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.ofFullCopy(ELECTRIC_TRACK.get())));

  public static final DeferredBlock<ThrottleTrackBlock> ELECTRIC_THROTTLE_TRACK =
      deferredRegister.register("electric_throttle_track",
          () -> new ThrottleTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.ofFullCopy(ELECTRIC_TRACK.get())));

  public static final DeferredBlock<RoutingTrackBlock> ELECTRIC_ROUTING_TRACK =
      deferredRegister.register("electric_routing_track",
          () -> new RoutingTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.ofFullCopy(ELECTRIC_TRACK.get())));

  public static final DeferredBlock<HighSpeedTrackBlock> HIGH_SPEED_TRACK =
      deferredRegister.register("high_speed_track",
          () -> new HighSpeedTrackBlock(BlockBehaviour.Properties.of()
              .noCollission()
              .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
              .sound(SoundType.METAL)));

  public static final DeferredBlock<TransitionTrackBlock> HIGH_SPEED_TRANSITION_TRACK =
      deferredRegister.register("high_speed_transition_track",
          () -> new TransitionTrackBlock(TrackTypes.HIGH_SPEED,
              BlockBehaviour.Properties.ofFullCopy(HIGH_SPEED_TRACK.get())));

  public static final DeferredBlock<LockingTrackBlock> HIGH_SPEED_LOCKING_TRACK =
      deferredRegister.register("high_speed_locking_track",
          () -> new LockingTrackBlock(TrackTypes.HIGH_SPEED,
              BlockBehaviour.Properties.ofFullCopy(HIGH_SPEED_TRACK.get())));

  public static final DeferredBlock<ActivatorTrackBlock> HIGH_SPEED_ACTIVATOR_TRACK =
      deferredRegister.register("high_speed_activator_track",
          () -> new ActivatorTrackBlock(TrackTypes.HIGH_SPEED,
              BlockBehaviour.Properties.ofFullCopy(HIGH_SPEED_TRACK.get())));

  public static final DeferredBlock<BoosterTrackBlock> HIGH_SPEED_BOOSTER_TRACK =
      deferredRegister.register("high_speed_booster_track",
          () -> new BoosterTrackBlock(TrackTypes.HIGH_SPEED,
              BlockBehaviour.Properties.ofFullCopy(HIGH_SPEED_TRACK.get())));

  public static final DeferredBlock<DetectorTrackBlock> HIGH_SPEED_DETECTOR_TRACK =
      deferredRegister.register("high_speed_detector_track",
          () -> new DetectorTrackBlock(TrackTypes.HIGH_SPEED,
              BlockBehaviour.Properties.ofFullCopy(HIGH_SPEED_TRACK.get())));

  public static final DeferredBlock<WyeTrackBlock> HIGH_SPEED_WYE_TRACK =
      deferredRegister.register("high_speed_wye_track",
          () -> new WyeTrackBlock(TrackTypes.HIGH_SPEED,
              BlockBehaviour.Properties.ofFullCopy(HIGH_SPEED_TRACK.get()).noLootTable()));

  public static final DeferredBlock<TurnoutTrackBlock> HIGH_SPEED_TURNOUT_TRACK =
      deferredRegister.register("high_speed_turnout_track",
          () -> new TurnoutTrackBlock(TrackTypes.HIGH_SPEED,
              BlockBehaviour.Properties.ofFullCopy(HIGH_SPEED_TRACK.get()).noLootTable()));

  public static final DeferredBlock<JunctionTrackBlock> HIGH_SPEED_JUNCTION_TRACK =
      deferredRegister.register("high_speed_junction_track",
          () -> new JunctionTrackBlock(TrackTypes.HIGH_SPEED,
              BlockBehaviour.Properties.ofFullCopy(HIGH_SPEED_TRACK.get()).noLootTable()));

  public static final DeferredBlock<WhistleTrackBlock> HIGH_SPEED_WHISTLE_TRACK =
      deferredRegister.register("high_speed_whistle_track",
          () -> new WhistleTrackBlock(TrackTypes.HIGH_SPEED,
              BlockBehaviour.Properties.ofFullCopy(HIGH_SPEED_TRACK.get())));

  public static final DeferredBlock<LocomotiveTrackBlock> HIGH_SPEED_LOCOMOTIVE_TRACK =
      deferredRegister.register("high_speed_locomotive_track",
          () -> new LocomotiveTrackBlock(TrackTypes.HIGH_SPEED,
              BlockBehaviour.Properties.ofFullCopy(HIGH_SPEED_TRACK.get())));

  public static final DeferredBlock<ThrottleTrackBlock> HIGH_SPEED_THROTTLE_TRACK =
      deferredRegister.register("high_speed_throttle_track",
          () -> new ThrottleTrackBlock(TrackTypes.HIGH_SPEED,
              BlockBehaviour.Properties.ofFullCopy(HIGH_SPEED_TRACK.get())));

  public static final DeferredBlock<HighSpeedElectricTrackBlock> HIGH_SPEED_ELECTRIC_TRACK =
      deferredRegister.register("high_speed_electric_track",
          () -> new HighSpeedElectricTrackBlock(BlockBehaviour.Properties.of()
              .noCollission()
              .randomTicks()
              .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
              .sound(SoundType.METAL)));

  public static final DeferredBlock<TransitionTrackBlock> HIGH_SPEED_ELECTRIC_TRANSITION_TRACK =
      deferredRegister.register("high_speed_electric_transition_track",
          () -> new TransitionTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              BlockBehaviour.Properties.ofFullCopy(HIGH_SPEED_ELECTRIC_TRACK.get())));

  public static final DeferredBlock<LockingTrackBlock> HIGH_SPEED_ELECTRIC_LOCKING_TRACK =
      deferredRegister.register("high_speed_electric_locking_track",
          () -> new LockingTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              BlockBehaviour.Properties.ofFullCopy(HIGH_SPEED_ELECTRIC_TRACK.get())));

  public static final DeferredBlock<ActivatorTrackBlock> HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK =
      deferredRegister.register("high_speed_electric_activator_track",
          () -> new ActivatorTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              BlockBehaviour.Properties.ofFullCopy(HIGH_SPEED_ELECTRIC_TRACK.get())));

  public static final DeferredBlock<BoosterTrackBlock> HIGH_SPEED_ELECTRIC_BOOSTER_TRACK =
      deferredRegister.register("high_speed_electric_booster_track",
          () -> new BoosterTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              BlockBehaviour.Properties.ofFullCopy(HIGH_SPEED_ELECTRIC_TRACK.get())));

  public static final DeferredBlock<DetectorTrackBlock> HIGH_SPEED_ELECTRIC_DETECTOR_TRACK =
      deferredRegister.register("high_speed_electric_detector_track",
          () -> new DetectorTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              BlockBehaviour.Properties.ofFullCopy(HIGH_SPEED_ELECTRIC_TRACK.get())));

  public static final DeferredBlock<WyeTrackBlock> HIGH_SPEED_ELECTRIC_WYE_TRACK =
      deferredRegister.register("high_speed_electric_wye_track",
          () -> new WyeTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              BlockBehaviour.Properties.ofFullCopy(HIGH_SPEED_ELECTRIC_TRACK.get()).noLootTable()));

  public static final DeferredBlock<TurnoutTrackBlock> HIGH_SPEED_ELECTRIC_TURNOUT_TRACK =
      deferredRegister.register("high_speed_electric_turnout_track",
          () -> new TurnoutTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              BlockBehaviour.Properties.ofFullCopy(HIGH_SPEED_ELECTRIC_TRACK.get()).noLootTable()));

  public static final DeferredBlock<JunctionTrackBlock> HIGH_SPEED_ELECTRIC_JUNCTION_TRACK =
      deferredRegister.register("high_speed_electric_junction_track",
          () -> new JunctionTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              BlockBehaviour.Properties.ofFullCopy(HIGH_SPEED_ELECTRIC_TRACK.get()).noLootTable()));

  public static final DeferredBlock<WhistleTrackBlock> HIGH_SPEED_ELECTRIC_WHISTLE_TRACK =
      deferredRegister.register("high_speed_electric_whistle_track",
          () -> new WhistleTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              BlockBehaviour.Properties.ofFullCopy(HIGH_SPEED_ELECTRIC_TRACK.get())));

  public static final DeferredBlock<LocomotiveTrackBlock> HIGH_SPEED_ELECTRIC_LOCOMOTIVE_TRACK =
      deferredRegister.register("high_speed_electric_locomotive_track",
          () -> new LocomotiveTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              BlockBehaviour.Properties.ofFullCopy(HIGH_SPEED_ELECTRIC_TRACK.get())));

  public static final DeferredBlock<ThrottleTrackBlock> HIGH_SPEED_ELECTRIC_THROTTLE_TRACK =
      deferredRegister.register("high_speed_electric_throttle_track",
          () -> new ThrottleTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              BlockBehaviour.Properties.ofFullCopy(HIGH_SPEED_ELECTRIC_TRACK.get())));

  public static final DeferredBlock<LockingTrackBlock> IRON_LOCKING_TRACK =
      deferredRegister.register("iron_locking_track",
          () -> new LockingTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of()
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final DeferredBlock<BufferStopTrackBlock> IRON_BUFFER_STOP_TRACK =
      deferredRegister.register("iron_buffer_stop_track",
          () -> new BufferStopTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.ofFullCopy(IRON_LOCKING_TRACK.get())));

  public static final DeferredBlock<ActivatorTrackBlock> IRON_ACTIVATOR_TRACK =
      deferredRegister.register("iron_activator_track",
          () -> new ActivatorTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.ofFullCopy(IRON_LOCKING_TRACK.get())));

  public static final DeferredBlock<BoosterTrackBlock> IRON_BOOSTER_TRACK =
      deferredRegister.register("iron_booster_track",
          () -> new BoosterTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.ofFullCopy(IRON_LOCKING_TRACK.get())));

  public static final DeferredBlock<ControlTrackBlock> IRON_CONTROL_TRACK =
      deferredRegister.register("iron_control_track",
          () -> new ControlTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.ofFullCopy(IRON_LOCKING_TRACK.get())));

  public static final DeferredBlock<GatedTrackBlock> IRON_GATED_TRACK =
      deferredRegister.register("iron_gated_track",
          () -> new GatedTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.ofFullCopy(IRON_LOCKING_TRACK.get())));

  public static final DeferredBlock<DetectorTrackBlock> IRON_DETECTOR_TRACK =
      deferredRegister.register("iron_detector_track",
          () -> new DetectorTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.ofFullCopy(IRON_LOCKING_TRACK.get())));

  public static final DeferredBlock<CouplerTrackBlock> IRON_COUPLER_TRACK =
      deferredRegister.register("iron_coupler_track",
          () -> new CouplerTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.ofFullCopy(IRON_LOCKING_TRACK.get())));

  public static final DeferredBlock<EmbarkingTrackBlock> IRON_EMBARKING_TRACK =
      deferredRegister.register("iron_embarking_track",
          () -> new EmbarkingTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.ofFullCopy(IRON_LOCKING_TRACK.get())));

  public static final DeferredBlock<DisembarkingTrackBlock> IRON_DISEMBARKING_TRACK =
      deferredRegister.register("iron_disembarking_track",
          () -> new DisembarkingTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.ofFullCopy(IRON_LOCKING_TRACK.get())));

  public static final DeferredBlock<DumpingTrackBlock> IRON_DUMPING_TRACK =
      deferredRegister.register("iron_dumping_track",
          () -> new DumpingTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.ofFullCopy(IRON_LOCKING_TRACK.get())));

  public static final DeferredBlock<WyeTrackBlock> IRON_WYE_TRACK =
      deferredRegister.register("iron_wye_track",
          () -> new WyeTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.ofFullCopy(IRON_LOCKING_TRACK.get()).noLootTable()));

  public static final DeferredBlock<TurnoutTrackBlock> IRON_TURNOUT_TRACK =
      deferredRegister.register("iron_turnout_track",
          () -> new TurnoutTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.ofFullCopy(IRON_LOCKING_TRACK.get()).noLootTable()));

  public static final DeferredBlock<JunctionTrackBlock> IRON_JUNCTION_TRACK =
      deferredRegister.register("iron_junction_track",
          () -> new JunctionTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.ofFullCopy(IRON_LOCKING_TRACK.get()).noLootTable()));

  public static final DeferredBlock<LauncherTrackBlock> IRON_LAUNCHER_TRACK =
      deferredRegister.register("iron_launcher_track",
          () -> new LauncherTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.ofFullCopy(IRON_LOCKING_TRACK.get())));

  public static final DeferredBlock<OneWayTrackBlock> IRON_ONE_WAY_TRACK =
      deferredRegister.register("iron_one_way_track",
          () -> new OneWayTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.ofFullCopy(IRON_LOCKING_TRACK.get())));

  public static final DeferredBlock<WhistleTrackBlock> IRON_WHISTLE_TRACK =
      deferredRegister.register("iron_whistle_track",
          () -> new WhistleTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.ofFullCopy(IRON_LOCKING_TRACK.get())));

  public static final DeferredBlock<LocomotiveTrackBlock> IRON_LOCOMOTIVE_TRACK =
      deferredRegister.register("iron_locomotive_track",
          () -> new LocomotiveTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.ofFullCopy(IRON_LOCKING_TRACK.get())));

  public static final DeferredBlock<ThrottleTrackBlock> IRON_THROTTLE_TRACK =
      deferredRegister.register("iron_throttle_track",
          () -> new ThrottleTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.ofFullCopy(IRON_LOCKING_TRACK.get())));

  public static final DeferredBlock<RoutingTrackBlock> IRON_ROUTING_TRACK =
      deferredRegister.register("iron_routing_track",
          () -> new RoutingTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.ofFullCopy(IRON_LOCKING_TRACK.get())));

  public static final DeferredBlock<ReinforcedTrackBlock> REINFORCED_TRACK =
      deferredRegister.register("reinforced_track",
          () -> new ReinforcedTrackBlock(BlockBehaviour.Properties.of()
              .noCollission()
              .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
              .sound(SoundType.METAL)));

  public static final DeferredBlock<LockingTrackBlock> REINFORCED_LOCKING_TRACK =
      deferredRegister.register("reinforced_locking_track",
          () -> new LockingTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.ofFullCopy(REINFORCED_TRACK.get())));

  public static final DeferredBlock<BufferStopTrackBlock> REINFORCED_BUFFER_STOP_TRACK =
      deferredRegister.register("reinforced_buffer_stop_track",
          () -> new BufferStopTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.ofFullCopy(REINFORCED_TRACK.get())));

  public static final DeferredBlock<ActivatorTrackBlock> REINFORCED_ACTIVATOR_TRACK =
      deferredRegister.register("reinforced_activator_track",
          () -> new ActivatorTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.ofFullCopy(REINFORCED_TRACK.get())));

  public static final DeferredBlock<BoosterTrackBlock> REINFORCED_BOOSTER_TRACK =
      deferredRegister.register("reinforced_booster_track",
          () -> new BoosterTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.ofFullCopy(REINFORCED_TRACK.get())));

  public static final DeferredBlock<ControlTrackBlock> REINFORCED_CONTROL_TRACK =
      deferredRegister.register("reinforced_control_track",
          () -> new ControlTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.ofFullCopy(REINFORCED_TRACK.get())));

  public static final DeferredBlock<GatedTrackBlock> REINFORCED_GATED_TRACK =
      deferredRegister.register("reinforced_gated_track",
          () -> new GatedTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.ofFullCopy(REINFORCED_TRACK.get())));

  public static final DeferredBlock<DetectorTrackBlock> REINFORCED_DETECTOR_TRACK =
      deferredRegister.register("reinforced_detector_track",
          () -> new DetectorTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.ofFullCopy(REINFORCED_TRACK.get())));

  public static final DeferredBlock<CouplerTrackBlock> REINFORCED_COUPLER_TRACK =
      deferredRegister.register("reinforced_coupler_track",
          () -> new CouplerTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.ofFullCopy(REINFORCED_TRACK.get())));

  public static final DeferredBlock<EmbarkingTrackBlock> REINFORCED_EMBARKING_TRACK =
      deferredRegister.register("reinforced_embarking_track",
          () -> new EmbarkingTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.ofFullCopy(REINFORCED_TRACK.get())));

  public static final DeferredBlock<DisembarkingTrackBlock> REINFORCED_DISEMBARKING_TRACK =
      deferredRegister.register("reinforced_disembarking_track",
          () -> new DisembarkingTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.ofFullCopy(REINFORCED_TRACK.get())));

  public static final DeferredBlock<DumpingTrackBlock> REINFORCED_DUMPING_TRACK =
      deferredRegister.register("reinforced_dumping_track",
          () -> new DumpingTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.ofFullCopy(REINFORCED_TRACK.get())));

  public static final DeferredBlock<WyeTrackBlock> REINFORCED_WYE_TRACK =
      deferredRegister.register("reinforced_wye_track",
          () -> new WyeTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.ofFullCopy(REINFORCED_TRACK.get()).noLootTable()));

  public static final DeferredBlock<TurnoutTrackBlock> REINFORCED_TURNOUT_TRACK =
      deferredRegister.register("reinforced_turnout_track",
          () -> new TurnoutTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.ofFullCopy(REINFORCED_TRACK.get()).noLootTable()));

  public static final DeferredBlock<JunctionTrackBlock> REINFORCED_JUNCTION_TRACK =
      deferredRegister.register("reinforced_junction_track",
          () -> new JunctionTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.ofFullCopy(REINFORCED_TRACK.get()).noLootTable()));

  public static final DeferredBlock<LauncherTrackBlock> REINFORCED_LAUNCHER_TRACK =
      deferredRegister.register("reinforced_launcher_track",
          () -> new LauncherTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.ofFullCopy(REINFORCED_TRACK.get())));

  public static final DeferredBlock<OneWayTrackBlock> REINFORCED_ONE_WAY_TRACK =
      deferredRegister.register("reinforced_one_way_track",
          () -> new OneWayTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.ofFullCopy(REINFORCED_TRACK.get())));

  public static final DeferredBlock<WhistleTrackBlock> REINFORCED_WHISTLE_TRACK =
      deferredRegister.register("reinforced_whistle_track",
          () -> new WhistleTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.ofFullCopy(REINFORCED_TRACK.get())));

  public static final DeferredBlock<LocomotiveTrackBlock> REINFORCED_LOCOMOTIVE_TRACK =
      deferredRegister.register("reinforced_locomotive_track",
          () -> new LocomotiveTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.ofFullCopy(REINFORCED_TRACK.get())));

  public static final DeferredBlock<ThrottleTrackBlock> REINFORCED_THROTTLE_TRACK =
      deferredRegister.register("reinforced_throttle_track",
          () -> new ThrottleTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.ofFullCopy(REINFORCED_TRACK.get())));

  public static final DeferredBlock<RoutingTrackBlock> REINFORCED_ROUTING_TRACK =
      deferredRegister.register("reinforced_routing_track",
          () -> new RoutingTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.ofFullCopy(REINFORCED_TRACK.get())));

  public static final DeferredBlock<StrapIronTrackBlock> STRAP_IRON_TRACK =
      deferredRegister.register("strap_iron_track",
          () -> new StrapIronTrackBlock(BlockBehaviour.Properties.of()
              .noCollission()
              .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
              .sound(SoundType.METAL)));

  public static final DeferredBlock<LockingTrackBlock> STRAP_IRON_LOCKING_TRACK =
      deferredRegister.register("strap_iron_locking_track",
          () -> new LockingTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.ofFullCopy(STRAP_IRON_TRACK.get())));

  public static final DeferredBlock<BufferStopTrackBlock> STRAP_IRON_BUFFER_STOP_TRACK =
      deferredRegister.register("strap_iron_buffer_stop_track",
          () -> new BufferStopTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.ofFullCopy(STRAP_IRON_TRACK.get())));

  public static final DeferredBlock<ActivatorTrackBlock> STRAP_IRON_ACTIVATOR_TRACK =
      deferredRegister.register("strap_iron_activator_track",
          () -> new ActivatorTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.ofFullCopy(STRAP_IRON_TRACK.get())));

  public static final DeferredBlock<BoosterTrackBlock> STRAP_IRON_BOOSTER_TRACK =
      deferredRegister.register("strap_iron_booster_track",
          () -> new BoosterTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.ofFullCopy(STRAP_IRON_TRACK.get())));

  public static final DeferredBlock<ControlTrackBlock> STRAP_IRON_CONTROL_TRACK =
      deferredRegister.register("strap_iron_control_track",
          () -> new ControlTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.ofFullCopy(STRAP_IRON_TRACK.get())));

  public static final DeferredBlock<GatedTrackBlock> STRAP_IRON_GATED_TRACK =
      deferredRegister.register("strap_iron_gated_track",
          () -> new GatedTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.ofFullCopy(STRAP_IRON_TRACK.get())));

  public static final DeferredBlock<DetectorTrackBlock> STRAP_IRON_DETECTOR_TRACK =
      deferredRegister.register("strap_iron_detector_track",
          () -> new DetectorTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.ofFullCopy(STRAP_IRON_TRACK.get())));

  public static final DeferredBlock<CouplerTrackBlock> STRAP_IRON_COUPLER_TRACK =
      deferredRegister.register("strap_iron_coupler_track",
          () -> new CouplerTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.ofFullCopy(STRAP_IRON_TRACK.get())));

  public static final DeferredBlock<EmbarkingTrackBlock> STRAP_IRON_EMBARKING_TRACK =
      deferredRegister.register("strap_iron_embarking_track",
          () -> new EmbarkingTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.ofFullCopy(STRAP_IRON_TRACK.get())));

  public static final DeferredBlock<DisembarkingTrackBlock> STRAP_IRON_DISEMBARKING_TRACK =
      deferredRegister.register("strap_iron_disembarking_track",
          () -> new DisembarkingTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.ofFullCopy(STRAP_IRON_TRACK.get())));

  public static final DeferredBlock<DumpingTrackBlock> STRAP_IRON_DUMPING_TRACK =
      deferredRegister.register("strap_iron_dumping_track",
          () -> new DumpingTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.ofFullCopy(STRAP_IRON_TRACK.get())));

  public static final DeferredBlock<WyeTrackBlock> STRAP_IRON_WYE_TRACK =
      deferredRegister.register("strap_iron_wye_track",
          () -> new WyeTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.ofFullCopy(STRAP_IRON_TRACK.get()).noLootTable()));

  public static final DeferredBlock<TurnoutTrackBlock> STRAP_IRON_TURNOUT_TRACK =
      deferredRegister.register("strap_iron_turnout_track",
          () -> new TurnoutTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.ofFullCopy(STRAP_IRON_TRACK.get()).noLootTable()));

  public static final DeferredBlock<JunctionTrackBlock> STRAP_IRON_JUNCTION_TRACK =
      deferredRegister.register("strap_iron_junction_track",
          () -> new JunctionTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.ofFullCopy(STRAP_IRON_TRACK.get()).noLootTable()));

  public static final DeferredBlock<LauncherTrackBlock> STRAP_IRON_LAUNCHER_TRACK =
      deferredRegister.register("strap_iron_launcher_track",
          () -> new LauncherTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.ofFullCopy(STRAP_IRON_TRACK.get())));

  public static final DeferredBlock<OneWayTrackBlock> STRAP_IRON_ONE_WAY_TRACK =
      deferredRegister.register("strap_iron_one_way_track",
          () -> new OneWayTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.ofFullCopy(STRAP_IRON_TRACK.get())));

  public static final DeferredBlock<WhistleTrackBlock> STRAP_IRON_WHISTLE_TRACK =
      deferredRegister.register("strap_iron_whistle_track",
          () -> new WhistleTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.ofFullCopy(STRAP_IRON_TRACK.get())));

  public static final DeferredBlock<LocomotiveTrackBlock> STRAP_IRON_LOCOMOTIVE_TRACK =
      deferredRegister.register("strap_iron_locomotive_track",
          () -> new LocomotiveTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.ofFullCopy(STRAP_IRON_TRACK.get())));

  public static final DeferredBlock<ThrottleTrackBlock> STRAP_IRON_THROTTLE_TRACK =
      deferredRegister.register("strap_iron_throttle_track",
          () -> new ThrottleTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.ofFullCopy(STRAP_IRON_TRACK.get())));

  public static final DeferredBlock<RoutingTrackBlock> STRAP_IRON_ROUTING_TRACK =
      deferredRegister.register("strap_iron_routing_track",
          () -> new RoutingTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.ofFullCopy(STRAP_IRON_TRACK.get())));

  public static final DeferredBlock<ElevatorTrackBlock> ELEVATOR_TRACK =
      deferredRegister.register("elevator_track",
          () -> new ElevatorTrackBlock(BlockBehaviour.Properties.of()
              .noCollission()
              .strength(1.05F)
              .sound(SoundType.METAL)));

  public static final DeferredBlock<FirestoneBlock> FIRESTONE_ORE =
      deferredRegister.register("firestone_ore",
          () -> new FirestoneBlock(BlockBehaviour.Properties.of()
              .mapColor(MapColor.STONE)
              .lightLevel(__ -> 15)
              .strength(3, 5)));

  public static final DeferredBlock<RitualBlock> RITUAL =
      deferredRegister.register("ritual",
          () -> new RitualBlock(BlockBehaviour.Properties.of()
              .mapColor(MapColor.STONE)
              .lightLevel(__ -> 1)
              .noOcclusion()
              .noLootTable()));

  public static final DeferredBlock<ManualRollingMachineBlock> MANUAL_ROLLING_MACHINE =
      deferredRegister.register("manual_rolling_machine",
          () -> new ManualRollingMachineBlock(BlockBehaviour.Properties.of()
              .mapColor(MapColor.WOOD)
              .sound(SoundType.WOOD)
              .strength(2.5F)));

  public static final DeferredBlock<PoweredRollingMachineBlock> POWERED_ROLLING_MACHINE =
      deferredRegister.register("powered_rolling_machine",
          () -> new PoweredRollingMachineBlock(BlockBehaviour.Properties.of()
              .mapColor(MapColor.METAL)
              .sound(SoundType.METAL)
              .strength(3.0F)
              .randomTicks()));

  public static final DeferredBlock<CrusherMultiblockBlock> CRUSHER =
      deferredRegister.register("crusher",
          () -> new CrusherMultiblockBlock(BlockBehaviour.Properties.of()
              .mapColor(MapColor.METAL)
              .requiresCorrectToolForDrops()
              .randomTicks()
              .strength(3.5F)
              .sound(SoundType.STONE)));

  public static final DeferredBlock<CokeOvenBricksBlock> COKE_OVEN_BRICKS =
      deferredRegister.register("coke_oven_bricks",
          () -> new CokeOvenBricksBlock(BlockBehaviour.Properties.of()
              .mapColor(MapColor.STONE)
              .sound(SoundType.STONE)
              .lightLevel(litBlockEmission(13))
              .strength(2F, 6.0F)));

  public static final DeferredBlock<SteamOvenBlock> STEAM_OVEN =
      deferredRegister.register("steam_oven",
          () -> new SteamOvenBlock(BlockBehaviour.Properties.of()
              .mapColor(MapColor.STONE)
              .requiresCorrectToolForDrops()
              .sound(SoundType.STONE)
              .strength(3.5F)));

  public static final DeferredBlock<CreosoteLiquidBlock> CREOSOTE =
      deferredRegister.register("creosote",
          () -> new CreosoteLiquidBlock(
              BlockBehaviour.Properties.of()
                  .mapColor(MapColor.WATER)
                  .liquid()
                  .noCollission()
                  .strength(50.0F)
                  .pushReaction(PushReaction.DESTROY)
                  .noLootTable()
                  .sound(SoundType.EMPTY)));

  public static final DeferredBlock<CrushedObsidian> CRUSHED_OBSIDIAN =
      deferredRegister.register("crushed_obsidian",
          () -> new CrushedObsidian(BlockBehaviour.Properties.of()
              .mapColor(MapColor.COLOR_BLACK)
              .requiresCorrectToolForDrops()
              .strength(50, 1200)));

  public static final DeferredBlock<Block> QUARRIED_STONE =
      deferredRegister.register("quarried_stone",
          () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));

  public static final DeferredBlock<Block> QUARRIED_COBBLESTONE =
      deferredRegister.register("quarried_cobblestone",
          () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE)));

  public static final DeferredBlock<Block> POLISHED_QUARRIED_STONE =
      deferredRegister.register("polished_quarried_stone",
          () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_ANDESITE)));

  public static final DeferredBlock<Block> CHISELED_QUARRIED_STONE =
      deferredRegister.register("chiseled_quarried_stone",
          () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.CHISELED_STONE_BRICKS)));

  public static final DeferredBlock<Block> ETCHED_QUARRIED_STONE =
      deferredRegister.register("etched_quarried_stone",
          () -> new Block(BlockBehaviour.Properties.ofFullCopy(QUARRIED_STONE.get())));

  public static final DeferredBlock<Block> QUARRIED_BRICKS =
      deferredRegister.register("quarried_bricks",
          () -> new Block(BlockBehaviour.Properties.ofFullCopy(QUARRIED_STONE.get())));

  public static final DeferredBlock<StairBlock> QUARRIED_BRICK_STAIRS =
      deferredRegister.register("quarried_brick_stairs",
          () -> new StairBlock(() -> QUARRIED_BRICKS.get().defaultBlockState(),
              BlockBehaviour.Properties.ofFullCopy(QUARRIED_STONE.get())));

  public static final DeferredBlock<SlabBlock> QUARRIED_BRICK_SLAB =
      deferredRegister.register("quarried_brick_slab",
          () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(QUARRIED_STONE.get())));

  public static final DeferredBlock<Block> QUARRIED_PAVER =
      deferredRegister.register("quarried_paver",
          () -> new Block(BlockBehaviour.Properties.ofFullCopy(QUARRIED_STONE.get())));

  public static final DeferredBlock<StairBlock> QUARRIED_PAVER_STAIRS =
      deferredRegister.register("quarried_paver_stairs",
          () -> new StairBlock(() -> QUARRIED_PAVER.get().defaultBlockState(),
              BlockBehaviour.Properties.ofFullCopy(QUARRIED_STONE.get())));

  public static final DeferredBlock<SlabBlock> QUARRIED_PAVER_SLAB =
      deferredRegister.register("quarried_paver_slab",
          () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(QUARRIED_STONE.get())));

  public static final DeferredBlock<Block> ABYSSAL_STONE =
      deferredRegister.register("abyssal_stone",
          () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));

  public static final DeferredBlock<Block> ABYSSAL_COBBLESTONE =
      deferredRegister.register("abyssal_cobblestone",
          () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE)));

  public static final DeferredBlock<Block> POLISHED_ABYSSAL_STONE =
      deferredRegister.register("polished_abyssal_stone",
          () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_ANDESITE)));

  public static final DeferredBlock<Block> CHISELED_ABYSSAL_STONE =
      deferredRegister.register("chiseled_abyssal_stone",
          () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.CHISELED_STONE_BRICKS)));

  public static final DeferredBlock<Block> ETCHED_ABYSSAL_STONE =
      deferredRegister.register("etched_abyssal_stone",
          () -> new Block(BlockBehaviour.Properties.ofFullCopy(ABYSSAL_STONE.get())));

  public static final DeferredBlock<Block> ABYSSAL_BRICKS =
      deferredRegister.register("abyssal_bricks",
          () -> new Block(BlockBehaviour.Properties.ofFullCopy(ABYSSAL_STONE.get())));

  public static final DeferredBlock<StairBlock> ABYSSAL_BRICK_STAIRS =
      deferredRegister.register("abyssal_brick_stairs",
          () -> new StairBlock(() -> ABYSSAL_BRICKS.get().defaultBlockState(),
              BlockBehaviour.Properties.ofFullCopy(ABYSSAL_STONE.get())));

  public static final DeferredBlock<SlabBlock> ABYSSAL_BRICK_SLAB =
      deferredRegister.register("abyssal_brick_slab",
          () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(ABYSSAL_STONE.get())));

  public static final DeferredBlock<Block> ABYSSAL_PAVER =
      deferredRegister.register("abyssal_paver",
          () -> new Block(BlockBehaviour.Properties.ofFullCopy(ABYSSAL_STONE.get())));

  public static final DeferredBlock<StairBlock> ABYSSAL_PAVER_STAIRS =
      deferredRegister.register("abyssal_paver_stairs",
          () -> new StairBlock(() -> ABYSSAL_PAVER.get().defaultBlockState(),
              BlockBehaviour.Properties.ofFullCopy(ABYSSAL_STONE.get())));

  public static final DeferredBlock<SlabBlock> ABYSSAL_PAVER_SLAB =
      deferredRegister.register("abyssal_paver_slab",
          () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(ABYSSAL_STONE.get())));

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
