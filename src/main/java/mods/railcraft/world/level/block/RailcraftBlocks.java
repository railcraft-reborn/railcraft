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
import mods.railcraft.world.level.block.detector.AdvancedDetectorBlock;
import mods.railcraft.world.level.block.detector.AgeDetectorBlock;
import mods.railcraft.world.level.block.detector.AnimalDetectorBlock;
import mods.railcraft.world.level.block.detector.AnyDetectorBlock;
import mods.railcraft.world.level.block.detector.EmptyDetectorBlock;
import mods.railcraft.world.level.block.detector.ItemDetectorBlock;
import mods.railcraft.world.level.block.detector.LocomotiveDetectorBlock;
import mods.railcraft.world.level.block.detector.MobDetectorBlock;
import mods.railcraft.world.level.block.detector.PlayerDetectorBlock;
import mods.railcraft.world.level.block.detector.RoutingDetectorBlock;
import mods.railcraft.world.level.block.detector.SheepDetectorBlock;
import mods.railcraft.world.level.block.detector.TankDetectorBlock;
import mods.railcraft.world.level.block.detector.TrainDetectorBlock;
import mods.railcraft.world.level.block.detector.VillagerDetectorBlock;
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
import mods.railcraft.world.level.block.signal.TokenSignalBoxBlock;
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
import mods.railcraft.world.level.block.track.ElevatorTrackBlock;
import mods.railcraft.world.level.block.track.ForceTrackBlock;
import mods.railcraft.world.level.block.track.TrackBlock;
import mods.railcraft.world.level.block.track.TrackTypes;
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
import mods.railcraft.world.level.block.worldspike.PersonalWorldSpikeBlock;
import mods.railcraft.world.level.block.worldspike.WorldSpikeBlock;
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
          "%s_strengthened_glass",
          StrengthenedGlassBlock::new,
          RailcraftBlockProperties.strengthenedGlass());

  public static final VariantSet<DyeColor, Block, IronTankGaugeBlock> IRON_TANK_GAUGE =
      VariantSet.of(
          DyeColor.class,
          deferredRegister,
          "%s_iron_tank_gauge",
          IronTankGaugeBlock::new,
          RailcraftBlockProperties.ironTankGauge());

  public static final VariantSet<DyeColor, Block, IronTankValveBlock> IRON_TANK_VALVE =
      VariantSet.of(
          DyeColor.class,
          deferredRegister,
          "%s_iron_tank_valve",
          IronTankValveBlock::new,
          RailcraftBlockProperties.ironTank());

  public static final VariantSet<DyeColor, Block, IronTankWallBlock> IRON_TANK_WALL =
      VariantSet.of(
          DyeColor.class,
          deferredRegister,
          "%s_iron_tank_wall",
          IronTankWallBlock::new,
          RailcraftBlockProperties.ironTank());

  public static final VariantSet<DyeColor, Block, SteelTankGaugeBlock> STEEL_TANK_GAUGE =
      VariantSet.of(
          DyeColor.class,
          deferredRegister,
          "%s_steel_tank_gauge",
          SteelTankGaugeBlock::new,
          RailcraftBlockProperties.steelTankGauge());

  public static final VariantSet<DyeColor, Block, SteelTankValveBlock> STEEL_TANK_VALVE =
      VariantSet.of(
          DyeColor.class,
          deferredRegister,
          "%s_steel_tank_valve",
          SteelTankValveBlock::new,
          RailcraftBlockProperties.steelTank());

  public static final VariantSet<DyeColor, Block, SteelTankWallBlock> STEEL_TANK_WALL =
      VariantSet.of(
          DyeColor.class,
          deferredRegister,
          "%s_steel_tank_wall",
          SteelTankWallBlock::new,
          RailcraftBlockProperties.steelTank());

  public static final VariantSet<DyeColor, Block, PostBlock> POST =
      VariantSet.of(
          DyeColor.class,
          deferredRegister,
          "%s_post",
          PostBlock::new,
          RailcraftBlockProperties.post());

  public static final DeferredBlock<SteamBoilerTankBlock> LOW_PRESSURE_STEAM_BOILER_TANK =
      deferredRegister.registerBlock("low_pressure_steam_boiler_tank", SteamBoilerTankBlock::new,
          RailcraftBlockProperties.steamBoilerTank());

  public static final DeferredBlock<SteamBoilerTankBlock> HIGH_PRESSURE_STEAM_BOILER_TANK =
      deferredRegister.registerBlock("high_pressure_steam_boiler_tank", SteamBoilerTankBlock::new,
          RailcraftBlockProperties.steamBoilerTank());

  public static final DeferredBlock<SolidFueledFireboxBlock> SOLID_FUELED_FIREBOX =
      deferredRegister.registerBlock("solid_fueled_firebox", SolidFueledFireboxBlock::new,
          RailcraftBlockProperties.fueledFirebox());

  public static final DeferredBlock<FluidFueledFireboxBlock> FLUID_FUELED_FIREBOX =
      deferredRegister.registerBlock("fluid_fueled_firebox", FluidFueledFireboxBlock::new,
          RailcraftBlockProperties.fueledFirebox());

  public static final DeferredBlock<SteamTurbineBlock> STEAM_TURBINE =
      deferredRegister.registerBlock("steam_turbine",
          properties -> new SteamTurbineBlock(properties
              .mapColor(MapColor.METAL)
              .strength(3.5F)
              .requiresCorrectToolForDrops()
              .randomTicks()
              .sound(SoundType.METAL)));

  public static final DeferredBlock<WaterTankSiding> WATER_TANK_SIDING =
      deferredRegister.registerBlock("water_tank_siding",
          properties -> new WaterTankSiding(properties
              .mapColor(MapColor.WOOD)
              .strength(2.0F, 3.0F)
              .sound(SoundType.WOOD)
              .requiresCorrectToolForDrops()));

  public static final DeferredBlock<BlastFurnaceBricksBlock> BLAST_FURNACE_BRICKS =
      deferredRegister.registerBlock("blast_furnace_bricks",
          properties -> new BlastFurnaceBricksBlock(properties
              .mapColor(MapColor.STONE)
              .strength(3.5F)
              .isValidSpawn(RailcraftBlocks::never)
              .requiresCorrectToolForDrops()
              .lightLevel(litBlockEmission(13))
              .sound(SoundType.STONE)));

  public static final DeferredBlock<FeedStationBlock> FEED_STATION =
      deferredRegister.registerBlock("feed_station",
          properties -> new FeedStationBlock(
              properties
                  .mapColor(MapColor.WOOD)
                  .strength(1F)
                  .requiresCorrectToolForDrops()
                  .sound(SoundType.WOOD)));

  public static final DeferredBlock<ChimneyBlock> CHIMNEY =
      deferredRegister.registerBlock("chimney",
          properties -> new ChimneyBlock(
              properties
                  .mapColor(MapColor.STONE)
                  .strength(2F)
                  .requiresCorrectToolForDrops()
                  .noOcclusion()));

  public static final DeferredBlock<LogBookBlock> LOGBOOK =
      deferredRegister.registerBlock("logbook",
          properties -> new LogBookBlock(
              properties
                  .mapColor(MapColor.WOOD)
                  .strength(3F)
                  .requiresCorrectToolForDrops()
                  .sound(SoundType.WOOD)));

  public static final DeferredBlock<FrameBlock> FRAME =
      deferredRegister.registerBlock("frame",
          properties -> new FrameBlock(
              properties
                  .mapColor(MapColor.METAL)
                  .strength(3F)
                  .randomTicks()
                  .requiresCorrectToolForDrops()
                  .noOcclusion()
                  .sound(SoundType.METAL)));

  public static final DeferredBlock<NickelZincBatteryBlock> NICKEL_ZINC_BATTERY =
      deferredRegister.registerBlock("nickel_zinc_battery", NickelZincBatteryBlock::new,
          RailcraftBlockProperties.battery());

  public static final DeferredBlock<NickelIronBatteryBlock> NICKEL_IRON_BATTERY =
      deferredRegister.registerBlock("nickel_iron_battery", NickelIronBatteryBlock::new,
          RailcraftBlockProperties.battery());

  public static final DeferredBlock<ZincCarbonBatteryBlock> ZINC_CARBON_BATTERY =
      deferredRegister.registerBlock("zinc_carbon_battery", ZincCarbonBatteryBlock::new,
          RailcraftBlockProperties.battery());

  public static final DeferredBlock<EmptyBatteryBlock> ZINC_CARBON_BATTERY_EMPTY =
      deferredRegister.registerBlock("zinc_carbon_battery_empty", EmptyBatteryBlock::new,
          RailcraftBlockProperties.battery());

  public static final DeferredBlock<ZincSilverBatteryBlock> ZINC_SILVER_BATTERY =
      deferredRegister.registerBlock("zinc_silver_battery", ZincSilverBatteryBlock::new,
          RailcraftBlockProperties.battery());

  public static final DeferredBlock<EmptyBatteryBlock> ZINC_SILVER_BATTERY_EMPTY =
      deferredRegister.registerBlock("zinc_silver_battery_empty", EmptyBatteryBlock::new,
          RailcraftBlockProperties.battery());

  public static final DeferredBlock<AnvilBlock> STEEL_ANVIL =
      deferredRegister.registerBlock("steel_anvil", AnvilBlock::new,
          RailcraftBlockProperties.steelAnvil());

  public static final DeferredBlock<AnvilBlock> CHIPPED_STEEL_ANVIL =
      deferredRegister.registerBlock("chipped_steel_anvil", AnvilBlock::new,
          RailcraftBlockProperties.steelAnvil());

  public static final DeferredBlock<AnvilBlock> DAMAGED_STEEL_ANVIL =
      deferredRegister.registerBlock("damaged_steel_anvil", AnvilBlock::new,
          RailcraftBlockProperties.steelAnvil());

  public static final DeferredBlock<Block> STEEL_BLOCK =
      deferredRegister.registerSimpleBlock("steel_block", RailcraftBlockProperties::ingotBlock);

  public static final DeferredBlock<Block> BRASS_BLOCK =
      deferredRegister.registerSimpleBlock("brass_block", properties ->
          RailcraftBlockProperties.ingotBlock(properties)
              .strength(3.0F, 6.0F));

  public static final DeferredBlock<Block> BRONZE_BLOCK =
      deferredRegister.registerSimpleBlock("bronze_block", RailcraftBlockProperties::ingotBlock);

  public static final DeferredBlock<Block> INVAR_BLOCK =
      deferredRegister.registerSimpleBlock("invar_block", RailcraftBlockProperties::ingotBlock);

  public static final DeferredBlock<Block> LEAD_BLOCK =
      deferredRegister.registerSimpleBlock("lead_block", properties ->
          RailcraftBlockProperties.ingotBlock(properties)
              .strength(5.0F, 6.0F));

  public static final DeferredBlock<Block> NICKEL_BLOCK =
      deferredRegister.registerSimpleBlock("nickel_block", properties ->
          RailcraftBlockProperties.ingotBlock(properties)
              .strength(5.0F, 6.0F));

  public static final DeferredBlock<Block> SILVER_BLOCK =
      deferredRegister.registerSimpleBlock("silver_block", properties ->
          RailcraftBlockProperties.ingotBlock(properties)
          .strength(3.0F, 6.0F));

  public static final DeferredBlock<Block> TIN_BLOCK =
      deferredRegister.registerSimpleBlock("tin_block", properties ->
          RailcraftBlockProperties.ingotBlock(properties)
          .strength(3.0F, 6.0F));

  public static final DeferredBlock<Block> ZINC_BLOCK =
      deferredRegister.registerSimpleBlock("zinc_block", RailcraftBlockProperties::ingotBlock);

  public static final DeferredBlock<Block> LEAD_ORE =
      deferredRegister.registerBlock("lead_ore",
          properties -> new DropExperienceBlock(ConstantInt.of(0),
              RailcraftBlockProperties.oreInGroundStone(properties)));

  public static final DeferredBlock<Block> DEEPSLATE_LEAD_ORE =
      deferredRegister.registerBlock("deepslate_lead_ore",
          properties -> new DropExperienceBlock(ConstantInt.of(0),
              RailcraftBlockProperties.oreInGroundDeepslate(properties)));

  public static final DeferredBlock<Block> NICKEL_ORE =
      deferredRegister.registerBlock("nickel_ore",
          properties -> new DropExperienceBlock(ConstantInt.of(0),
              RailcraftBlockProperties.oreInGroundStone(properties)));

  public static final DeferredBlock<Block> DEEPSLATE_NICKEL_ORE =
      deferredRegister.registerBlock("deepslate_nickel_ore",
          properties -> new DropExperienceBlock(ConstantInt.of(0),
              RailcraftBlockProperties.oreInGroundDeepslate(properties)));

  public static final DeferredBlock<Block> SILVER_ORE =
      deferredRegister.registerBlock("silver_ore",
          properties -> new DropExperienceBlock(ConstantInt.of(0),
              RailcraftBlockProperties.oreInGroundStone(properties)));

  public static final DeferredBlock<Block> DEEPSLATE_SILVER_ORE =
      deferredRegister.registerBlock("deepslate_silver_ore",
          properties -> new DropExperienceBlock(ConstantInt.of(0),
              RailcraftBlockProperties.oreInGroundDeepslate(properties)));

  public static final DeferredBlock<Block> TIN_ORE =
      deferredRegister.registerBlock("tin_ore",
          properties -> new DropExperienceBlock(ConstantInt.of(0),
              RailcraftBlockProperties.oreInGroundStone(properties)));

  public static final DeferredBlock<Block> DEEPSLATE_TIN_ORE =
      deferredRegister.registerBlock("deepslate_tin_ore",
          properties -> new DropExperienceBlock(ConstantInt.of(0),
              RailcraftBlockProperties.oreInGroundDeepslate(properties)));

  public static final DeferredBlock<Block> ZINC_ORE =
      deferredRegister.registerBlock("zinc_ore",
          properties -> new DropExperienceBlock(ConstantInt.of(0),
              RailcraftBlockProperties.oreInGroundStone(properties)));

  public static final DeferredBlock<Block> DEEPSLATE_ZINC_ORE =
      deferredRegister.registerBlock("deepslate_zinc_ore",
          properties -> new DropExperienceBlock(ConstantInt.of(0),
              RailcraftBlockProperties.oreInGroundDeepslate(properties)));

  public static final DeferredBlock<Block> SULFUR_ORE =
      deferredRegister.registerBlock("sulfur_ore",
          properties -> new DropExperienceBlock(UniformInt.of(2, 5),
              RailcraftBlockProperties.oreInGroundStone(properties)));

  public static final DeferredBlock<Block> DEEPSLATE_SULFUR_ORE =
      deferredRegister.registerBlock("deepslate_sulfur_ore",
          properties -> new DropExperienceBlock(UniformInt.of(2, 5),
              RailcraftBlockProperties.oreInGroundDeepslate(properties)));

  public static final DeferredBlock<Block> SALTPETER_ORE =
      deferredRegister.registerBlock("saltpeter_ore",
          properties -> new DropExperienceBlock(UniformInt.of(2, 5), properties
              .mapColor(MapColor.SAND)
              .instrument(NoteBlockInstrument.SNARE)
              .requiresCorrectToolForDrops()
              .strength(0.8F)
              .sound(SoundType.SAND)));

  public static final DeferredBlock<CoalCokeBlock> COAL_COKE_BLOCK =
      deferredRegister.registerBlock("coal_coke_block",
          properties -> new CoalCokeBlock(5, 300, properties
              .mapColor(MapColor.COLOR_BLACK)
              .instrument(NoteBlockInstrument.BASEDRUM)
              .strength(5.0F, 10.0F)
              .sound(SoundType.STONE)));

  public static final DeferredBlock<FluidLoaderBlock> FLUID_LOADER =
      deferredRegister.registerBlock("fluid_loader", FluidLoaderBlock::new,
          RailcraftBlockProperties.fluidManipulator());

  public static final DeferredBlock<FluidUnloaderBlock> FLUID_UNLOADER =
      deferredRegister.registerBlock("fluid_unloader", FluidUnloaderBlock::new,
          RailcraftBlockProperties.fluidManipulator());

  public static final DeferredBlock<AdvancedItemLoaderBlock> ADVANCED_ITEM_LOADER =
      deferredRegister.registerBlock("advanced_item_loader", AdvancedItemLoaderBlock::new,
          RailcraftBlockProperties.manipulator());

  public static final DeferredBlock<AdvancedItemUnloaderBlock> ADVANCED_ITEM_UNLOADER =
      deferredRegister.registerBlock("advanced_item_unloader", AdvancedItemUnloaderBlock::new,
          RailcraftBlockProperties.manipulator());

  public static final DeferredBlock<ItemLoaderBlock> ITEM_LOADER =
      deferredRegister.registerBlock("item_loader", ItemLoaderBlock::new,
          RailcraftBlockProperties.manipulator());

  public static final DeferredBlock<ItemUnloaderBlock> ITEM_UNLOADER =
      deferredRegister.registerBlock("item_unloader", ItemUnloaderBlock::new,
          RailcraftBlockProperties.manipulator());

  public static final DeferredBlock<CartDispenserBlock> CART_DISPENSER =
      deferredRegister.registerBlock("cart_dispenser", CartDispenserBlock::new,
          RailcraftBlockProperties.manipulator());

  public static final DeferredBlock<TrainDispenserBlock> TRAIN_DISPENSER =
      deferredRegister.registerBlock("train_dispenser", TrainDispenserBlock::new,
          RailcraftBlockProperties.manipulator());

  public static final DeferredBlock<AdvancedDetectorBlock> ADVANCED_DETECTOR =
      deferredRegister.registerBlock("advanced_detector", AdvancedDetectorBlock::new,
          RailcraftBlockProperties.detector());

  public static final DeferredBlock<AgeDetectorBlock> AGE_DETECTOR =
      deferredRegister.registerBlock("age_detector", AgeDetectorBlock::new,
          RailcraftBlockProperties.detector());

  public static final DeferredBlock<AnimalDetectorBlock> ANIMAL_DETECTOR =
      deferredRegister.registerBlock("animal_detector", AnimalDetectorBlock::new,
          RailcraftBlockProperties.detector());

  public static final DeferredBlock<AnyDetectorBlock> ANY_DETECTOR =
      deferredRegister.registerBlock("any_detector", AnyDetectorBlock::new,
          RailcraftBlockProperties.detector());

  public static final DeferredBlock<EmptyDetectorBlock> EMPTY_DETECTOR =
      deferredRegister.registerBlock("empty_detector", EmptyDetectorBlock::new,
          RailcraftBlockProperties.detector());

  public static final DeferredBlock<ItemDetectorBlock> ITEM_DETECTOR =
      deferredRegister.registerBlock("item_detector", ItemDetectorBlock::new,
          RailcraftBlockProperties.detector());

  public static final DeferredBlock<LocomotiveDetectorBlock> LOCOMOTIVE_DETECTOR =
      deferredRegister.registerBlock("locomotive_detector", LocomotiveDetectorBlock::new,
          RailcraftBlockProperties.detector());

  public static final DeferredBlock<MobDetectorBlock> MOB_DETECTOR =
      deferredRegister.registerBlock("mob_detector", MobDetectorBlock::new,
          RailcraftBlockProperties.detector());

  public static final DeferredBlock<PlayerDetectorBlock> PLAYER_DETECTOR =
      deferredRegister.registerBlock("player_detector", PlayerDetectorBlock::new,
          RailcraftBlockProperties.detector());

  public static final DeferredBlock<RoutingDetectorBlock> ROUTING_DETECTOR =
      deferredRegister.registerBlock("routing_detector", RoutingDetectorBlock::new,
          RailcraftBlockProperties.detector());

  public static final DeferredBlock<SheepDetectorBlock> SHEEP_DETECTOR =
      deferredRegister.registerBlock("sheep_detector", SheepDetectorBlock::new,
          RailcraftBlockProperties.detector());

  public static final DeferredBlock<TankDetectorBlock> TANK_DETECTOR =
      deferredRegister.registerBlock("tank_detector", TankDetectorBlock::new,
          RailcraftBlockProperties.detector());

  public static final DeferredBlock<TrainDetectorBlock> TRAIN_DETECTOR =
      deferredRegister.registerBlock("train_detector", TrainDetectorBlock::new,
          RailcraftBlockProperties.detector());

  public static final DeferredBlock<VillagerDetectorBlock> VILLAGER_DETECTOR =
      deferredRegister.registerBlock("villager_detector", VillagerDetectorBlock::new,
          RailcraftBlockProperties.detector());

  public static final DeferredBlock<SwitchTrackLeverBlock> SWITCH_TRACK_LEVER =
      deferredRegister.registerBlock("switch_track_lever", SwitchTrackLeverBlock::new,
          RailcraftBlockProperties.railSupportBlocks());

  public static final DeferredBlock<SwitchTrackMotorBlock> SWITCH_TRACK_MOTOR =
      deferredRegister.registerBlock("switch_track_motor", SwitchTrackMotorBlock::new,
          RailcraftBlockProperties.railSupportBlocks());

  public static final DeferredBlock<SwitchTrackRouterBlock> SWITCH_TRACK_ROUTER =
      deferredRegister.registerBlock("switch_track_router", SwitchTrackRouterBlock::new,
          RailcraftBlockProperties.railSupportBlocks());

  public static final DeferredBlock<AnalogSignalControllerBoxBlock> ANALOG_SIGNAL_CONTROLLER_BOX =
      deferredRegister.registerBlock("analog_signal_controller_box",
          AnalogSignalControllerBoxBlock::new, RailcraftBlockProperties.railSupportBlocks());

  public static final DeferredBlock<SignalSequencerBoxBlock> SIGNAL_SEQUENCER_BOX =
      deferredRegister.registerBlock("signal_sequencer_box", SignalSequencerBoxBlock::new,
          RailcraftBlockProperties.railSupportBlocks());

  public static final DeferredBlock<SignalCapacitorBoxBlock> SIGNAL_CAPACITOR_BOX =
      deferredRegister.registerBlock("signal_capacitor_box", SignalCapacitorBoxBlock::new,
          RailcraftBlockProperties.railSupportBlocks());

  public static final DeferredBlock<SignalInterlockBoxBlock> SIGNAL_INTERLOCK_BOX =
      deferredRegister.registerBlock("signal_interlock_box", SignalInterlockBoxBlock::new,
          RailcraftBlockProperties.railSupportBlocks());

  public static final DeferredBlock<SignalBlockRelayBoxBlock> SIGNAL_BLOCK_RELAY_BOX =
      deferredRegister.registerBlock("signal_block_relay_box", SignalBlockRelayBoxBlock::new,
          RailcraftBlockProperties.railSupportBlocks());

  public static final DeferredBlock<SignalBoxBlock> SIGNAL_RECEIVER_BOX =
      deferredRegister.registerBlock("signal_receiver_box", SignalReceiverBoxBlock::new,
          RailcraftBlockProperties.railSupportBlocks());

  public static final DeferredBlock<SignalControllerBoxBlock> SIGNAL_CONTROLLER_BOX =
      deferredRegister.registerBlock("signal_controller_box", SignalControllerBoxBlock::new,
          RailcraftBlockProperties.railSupportBlocks());

  public static final DeferredBlock<TokenSignalBoxBlock> TOKEN_SIGNAL_BOX =
      deferredRegister.registerBlock("token_signal_box", TokenSignalBoxBlock::new,
          RailcraftBlockProperties.railSupportBlocks());

  public static final DeferredBlock<DualBlockSignalBlock> DUAL_BLOCK_SIGNAL =
      deferredRegister.registerBlock("dual_block_signal", DualBlockSignalBlock::new,
          RailcraftBlockProperties.railSupportBlocks());

  public static final DeferredBlock<DualDistantSignalBlock> DUAL_DISTANT_SIGNAL =
      deferredRegister.registerBlock("dual_distant_signal", DualDistantSignalBlock::new,
          RailcraftBlockProperties.railSupportBlocks());

  public static final DeferredBlock<DualTokenSignalBlock> DUAL_TOKEN_SIGNAL =
      deferredRegister.registerBlock("dual_token_signal", DualTokenSignalBlock::new,
          RailcraftBlockProperties.railSupportBlocks());

  public static final DeferredBlock<BlockSignalBlock> BLOCK_SIGNAL =
      deferredRegister.registerBlock("block_signal", BlockSignalBlock::new,
          RailcraftBlockProperties.railSupportBlocks());

  public static final DeferredBlock<DistantSignalBlock> DISTANT_SIGNAL =
      deferredRegister.registerBlock("distant_signal", DistantSignalBlock::new,
          RailcraftBlockProperties.railSupportBlocks());

  public static final DeferredBlock<TokenSignalBlock> TOKEN_SIGNAL =
      deferredRegister.registerBlock("token_signal", TokenSignalBlock::new,
          RailcraftBlockProperties.railSupportBlocks());

  public static final DeferredBlock<ForceTrackBlock> FORCE_TRACK =
      deferredRegister.registerBlock("force_track", ForceTrackBlock::new,
          properties -> properties
              .sound(SoundType.GLASS)
              .instabreak()
              .noCollision()
              .randomTicks()
              .noLootTable());

  public static final DeferredBlock<ForceTrackEmitterBlock> FORCE_TRACK_EMITTER =
      deferredRegister.registerBlock("force_track_emitter", ForceTrackEmitterBlock::new,
          properties -> properties
              .mapColor(MapColor.METAL)
              .requiresCorrectToolForDrops()
              .strength(1.5F, 6)
              .sound(SoundType.METAL)
              .randomTicks());

  public static final DeferredBlock<AbandonedTrackBlock> ABANDONED_TRACK =
      deferredRegister.registerBlock("abandoned_track", AbandonedTrackBlock::new,
          RailcraftBlockProperties::standardTrack);

  public static final DeferredBlock<LockingTrackBlock> ABANDONED_LOCKING_TRACK =
      deferredRegister.registerBlock("abandoned_locking_track",
          properties -> new LockingTrackBlock(TrackTypes.ABANDONED,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<BufferStopTrackBlock> ABANDONED_BUFFER_STOP_TRACK =
      deferredRegister.registerBlock("abandoned_buffer_stop_track",
          properties -> new BufferStopTrackBlock(TrackTypes.ABANDONED,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<ActivatorTrackBlock> ABANDONED_ACTIVATOR_TRACK =
      deferredRegister.registerBlock("abandoned_activator_track",
          properties -> new ActivatorTrackBlock(TrackTypes.ABANDONED,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<BoosterTrackBlock> ABANDONED_BOOSTER_TRACK =
      deferredRegister.registerBlock("abandoned_booster_track",
          properties -> new BoosterTrackBlock(TrackTypes.ABANDONED,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<ControlTrackBlock> ABANDONED_CONTROL_TRACK =
      deferredRegister.registerBlock("abandoned_control_track",
          properties -> new ControlTrackBlock(TrackTypes.ABANDONED,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<GatedTrackBlock> ABANDONED_GATED_TRACK =
      deferredRegister.registerBlock("abandoned_gated_track",
          properties -> new GatedTrackBlock(TrackTypes.ABANDONED,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<DetectorTrackBlock> ABANDONED_DETECTOR_TRACK =
      deferredRegister.registerBlock("abandoned_detector_track",
          properties -> new DetectorTrackBlock(TrackTypes.ABANDONED,
              RailcraftBlockProperties.standardTrack(properties).randomTicks()));

  public static final DeferredBlock<CouplerTrackBlock> ABANDONED_COUPLER_TRACK =
      deferredRegister.registerBlock("abandoned_coupler_track",
          properties -> new CouplerTrackBlock(TrackTypes.ABANDONED,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<EmbarkingTrackBlock> ABANDONED_EMBARKING_TRACK =
      deferredRegister.registerBlock("abandoned_embarking_track",
          properties -> new EmbarkingTrackBlock(TrackTypes.ABANDONED,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<DisembarkingTrackBlock> ABANDONED_DISEMBARKING_TRACK =
      deferredRegister.registerBlock("abandoned_disembarking_track",
          properties -> new DisembarkingTrackBlock(TrackTypes.ABANDONED,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<DumpingTrackBlock> ABANDONED_DUMPING_TRACK =
      deferredRegister.registerBlock("abandoned_dumping_track",
          properties -> new DumpingTrackBlock(TrackTypes.ABANDONED,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<WyeTrackBlock> ABANDONED_WYE_TRACK =
      deferredRegister.registerBlock("abandoned_wye_track",
          properties -> new WyeTrackBlock(TrackTypes.ABANDONED,
              RailcraftBlockProperties.standardTrack(properties).noLootTable()));

  public static final DeferredBlock<TurnoutTrackBlock> ABANDONED_TURNOUT_TRACK =
      deferredRegister.registerBlock("abandoned_turnout_track",
          properties -> new TurnoutTrackBlock(TrackTypes.ABANDONED,
              RailcraftBlockProperties.standardTrack(properties).noLootTable()));

  public static final DeferredBlock<JunctionTrackBlock> ABANDONED_JUNCTION_TRACK =
      deferredRegister.registerBlock("abandoned_junction_track",
          properties -> new JunctionTrackBlock(TrackTypes.ABANDONED,
              RailcraftBlockProperties.standardTrack(properties).noLootTable()));

  public static final DeferredBlock<LauncherTrackBlock> ABANDONED_LAUNCHER_TRACK =
      deferredRegister.registerBlock("abandoned_launcher_track",
          properties -> new LauncherTrackBlock(TrackTypes.ABANDONED,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<OneWayTrackBlock> ABANDONED_ONE_WAY_TRACK =
      deferredRegister.registerBlock("abandoned_one_way_track",
          properties -> new OneWayTrackBlock(TrackTypes.ABANDONED,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<WhistleTrackBlock> ABANDONED_WHISTLE_TRACK =
      deferredRegister.registerBlock("abandoned_whistle_track",
          properties -> new WhistleTrackBlock(TrackTypes.ABANDONED,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<LocomotiveTrackBlock> ABANDONED_LOCOMOTIVE_TRACK =
      deferredRegister.registerBlock("abandoned_locomotive_track",
          properties -> new LocomotiveTrackBlock(TrackTypes.ABANDONED,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<ThrottleTrackBlock> ABANDONED_THROTTLE_TRACK =
      deferredRegister.registerBlock("abandoned_throttle_track",
          properties -> new ThrottleTrackBlock(TrackTypes.ABANDONED,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<RoutingTrackBlock> ABANDONED_ROUTING_TRACK =
      deferredRegister.registerBlock("abandoned_routing_track",
          properties -> new RoutingTrackBlock(TrackTypes.ABANDONED,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<TrackBlock> ELECTRIC_TRACK =
      deferredRegister.registerBlock("electric_track",
          properties -> new TrackBlock(TrackTypes.ELECTRIC, properties),
          RailcraftBlockProperties::electricTrack);

  public static final DeferredBlock<LockingTrackBlock> ELECTRIC_LOCKING_TRACK =
      deferredRegister.registerBlock("electric_locking_track",
          properties -> new LockingTrackBlock(TrackTypes.ELECTRIC,
              RailcraftBlockProperties.electricTrack(properties)));

  public static final DeferredBlock<BufferStopTrackBlock> ELECTRIC_BUFFER_STOP_TRACK =
      deferredRegister.registerBlock("electric_buffer_stop_track",
          properties -> new BufferStopTrackBlock(TrackTypes.ELECTRIC,
              RailcraftBlockProperties.electricTrack(properties)));

  public static final DeferredBlock<ActivatorTrackBlock> ELECTRIC_ACTIVATOR_TRACK =
      deferredRegister.registerBlock("electric_activator_track",
          properties -> new ActivatorTrackBlock(TrackTypes.ELECTRIC,
              RailcraftBlockProperties.electricTrack(properties)));

  public static final DeferredBlock<BoosterTrackBlock> ELECTRIC_BOOSTER_TRACK =
      deferredRegister.registerBlock("electric_booster_track",
          properties -> new BoosterTrackBlock(TrackTypes.ELECTRIC,
              RailcraftBlockProperties.electricTrack(properties)));

  public static final DeferredBlock<ControlTrackBlock> ELECTRIC_CONTROL_TRACK =
      deferredRegister.registerBlock("electric_control_track",
          properties -> new ControlTrackBlock(TrackTypes.ELECTRIC,
              RailcraftBlockProperties.electricTrack(properties)));

  public static final DeferredBlock<GatedTrackBlock> ELECTRIC_GATED_TRACK =
      deferredRegister.registerBlock("electric_gated_track",
          properties -> new GatedTrackBlock(TrackTypes.ELECTRIC,
              RailcraftBlockProperties.electricTrack(properties)));

  public static final DeferredBlock<DetectorTrackBlock> ELECTRIC_DETECTOR_TRACK =
      deferredRegister.registerBlock("electric_detector_track",
          properties -> new DetectorTrackBlock(TrackTypes.ELECTRIC,
              RailcraftBlockProperties.electricTrack(properties)));

  public static final DeferredBlock<CouplerTrackBlock> ELECTRIC_COUPLER_TRACK =
      deferredRegister.registerBlock("electric_coupler_track",
          properties -> new CouplerTrackBlock(TrackTypes.ELECTRIC,
              RailcraftBlockProperties.electricTrack(properties)));

  public static final DeferredBlock<EmbarkingTrackBlock> ELECTRIC_EMBARKING_TRACK =
      deferredRegister.registerBlock("electric_embarking_track",
          properties -> new EmbarkingTrackBlock(TrackTypes.ELECTRIC,
              RailcraftBlockProperties.electricTrack(properties)));

  public static final DeferredBlock<DisembarkingTrackBlock> ELECTRIC_DISEMBARKING_TRACK =
      deferredRegister.registerBlock("electric_disembarking_track",
          properties -> new DisembarkingTrackBlock(TrackTypes.ELECTRIC,
              RailcraftBlockProperties.electricTrack(properties)));

  public static final DeferredBlock<DumpingTrackBlock> ELECTRIC_DUMPING_TRACK =
      deferredRegister.registerBlock("electric_dumping_track",
          properties -> new DumpingTrackBlock(TrackTypes.ELECTRIC,
              RailcraftBlockProperties.electricTrack(properties)));

  public static final DeferredBlock<WyeTrackBlock> ELECTRIC_WYE_TRACK =
      deferredRegister.registerBlock("electric_wye_track",
          properties -> new WyeTrackBlock(TrackTypes.ELECTRIC,
              RailcraftBlockProperties.electricTrack(properties).noLootTable()));

  public static final DeferredBlock<TurnoutTrackBlock> ELECTRIC_TURNOUT_TRACK =
      deferredRegister.registerBlock("electric_turnout_track",
          properties -> new TurnoutTrackBlock(TrackTypes.ELECTRIC,
              RailcraftBlockProperties.electricTrack(properties).noLootTable()));

  public static final DeferredBlock<JunctionTrackBlock> ELECTRIC_JUNCTION_TRACK =
      deferredRegister.registerBlock("electric_junction_track",
          properties -> new JunctionTrackBlock(TrackTypes.ELECTRIC,
              RailcraftBlockProperties.electricTrack(properties).noLootTable()));

  public static final DeferredBlock<LauncherTrackBlock> ELECTRIC_LAUNCHER_TRACK =
      deferredRegister.registerBlock("electric_launcher_track",
          properties -> new LauncherTrackBlock(TrackTypes.ELECTRIC,
              RailcraftBlockProperties.electricTrack(properties)));

  public static final DeferredBlock<OneWayTrackBlock> ELECTRIC_ONE_WAY_TRACK =
      deferredRegister.registerBlock("electric_one_way_track",
          properties -> new OneWayTrackBlock(TrackTypes.ELECTRIC,
              RailcraftBlockProperties.electricTrack(properties)));

  public static final DeferredBlock<WhistleTrackBlock> ELECTRIC_WHISTLE_TRACK =
      deferredRegister.registerBlock("electric_whistle_track",
          properties -> new WhistleTrackBlock(TrackTypes.ELECTRIC,
              RailcraftBlockProperties.electricTrack(properties)));
  public static final DeferredBlock<LocomotiveTrackBlock> ELECTRIC_LOCOMOTIVE_TRACK =
      deferredRegister.registerBlock("electric_locomotive_track",
          properties -> new LocomotiveTrackBlock(TrackTypes.ELECTRIC,
              RailcraftBlockProperties.electricTrack(properties)));

  public static final DeferredBlock<ThrottleTrackBlock> ELECTRIC_THROTTLE_TRACK =
      deferredRegister.registerBlock("electric_throttle_track",
          properties -> new ThrottleTrackBlock(TrackTypes.ELECTRIC,
              RailcraftBlockProperties.electricTrack(properties)));

  public static final DeferredBlock<RoutingTrackBlock> ELECTRIC_ROUTING_TRACK =
      deferredRegister.registerBlock("electric_routing_track",
          properties -> new RoutingTrackBlock(TrackTypes.ELECTRIC,
              RailcraftBlockProperties.electricTrack(properties)));

  public static final DeferredBlock<TrackBlock> HIGH_SPEED_TRACK =
      deferredRegister.registerBlock("high_speed_track",
          properties -> new TrackBlock(TrackTypes.HIGH_SPEED, properties),
          RailcraftBlockProperties::standardTrack);

  public static final DeferredBlock<TransitionTrackBlock> HIGH_SPEED_TRANSITION_TRACK =
      deferredRegister.registerBlock("high_speed_transition_track",
          properties -> new TransitionTrackBlock(TrackTypes.HIGH_SPEED,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<LockingTrackBlock> HIGH_SPEED_LOCKING_TRACK =
      deferredRegister.registerBlock("high_speed_locking_track",
          properties -> new LockingTrackBlock(TrackTypes.HIGH_SPEED,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<ActivatorTrackBlock> HIGH_SPEED_ACTIVATOR_TRACK =
      deferredRegister.registerBlock("high_speed_activator_track",
          properties -> new ActivatorTrackBlock(TrackTypes.HIGH_SPEED,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<BoosterTrackBlock> HIGH_SPEED_BOOSTER_TRACK =
      deferredRegister.registerBlock("high_speed_booster_track",
          properties -> new BoosterTrackBlock(TrackTypes.HIGH_SPEED,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<DetectorTrackBlock> HIGH_SPEED_DETECTOR_TRACK =
      deferredRegister.registerBlock("high_speed_detector_track",
          properties -> new DetectorTrackBlock(TrackTypes.HIGH_SPEED,
              RailcraftBlockProperties.standardTrack(properties).randomTicks()));

  public static final DeferredBlock<WyeTrackBlock> HIGH_SPEED_WYE_TRACK =
      deferredRegister.registerBlock("high_speed_wye_track",
          properties -> new WyeTrackBlock(TrackTypes.HIGH_SPEED,
              RailcraftBlockProperties.standardTrack(properties).noLootTable()));

  public static final DeferredBlock<TurnoutTrackBlock> HIGH_SPEED_TURNOUT_TRACK =
      deferredRegister.registerBlock("high_speed_turnout_track",
          properties -> new TurnoutTrackBlock(TrackTypes.HIGH_SPEED,
              RailcraftBlockProperties.standardTrack(properties).noLootTable()));

  public static final DeferredBlock<JunctionTrackBlock> HIGH_SPEED_JUNCTION_TRACK =
      deferredRegister.registerBlock("high_speed_junction_track",
          properties -> new JunctionTrackBlock(TrackTypes.HIGH_SPEED,
              RailcraftBlockProperties.standardTrack(properties).noLootTable()));

  public static final DeferredBlock<WhistleTrackBlock> HIGH_SPEED_WHISTLE_TRACK =
      deferredRegister.registerBlock("high_speed_whistle_track",
          properties -> new WhistleTrackBlock(TrackTypes.HIGH_SPEED,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<LocomotiveTrackBlock> HIGH_SPEED_LOCOMOTIVE_TRACK =
      deferredRegister.registerBlock("high_speed_locomotive_track",
          properties -> new LocomotiveTrackBlock(TrackTypes.HIGH_SPEED,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<ThrottleTrackBlock> HIGH_SPEED_THROTTLE_TRACK =
      deferredRegister.registerBlock("high_speed_throttle_track",
          properties -> new ThrottleTrackBlock(TrackTypes.HIGH_SPEED,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<TrackBlock> HIGH_SPEED_ELECTRIC_TRACK =
      deferredRegister.registerBlock("high_speed_electric_track",
          properties -> new TrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC, properties),
          RailcraftBlockProperties::electricTrack);

  public static final DeferredBlock<TransitionTrackBlock> HIGH_SPEED_ELECTRIC_TRANSITION_TRACK =
      deferredRegister.registerBlock("high_speed_electric_transition_track",
          properties -> new TransitionTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              RailcraftBlockProperties.electricTrack(properties)));

  public static final DeferredBlock<LockingTrackBlock> HIGH_SPEED_ELECTRIC_LOCKING_TRACK =
      deferredRegister.registerBlock("high_speed_electric_locking_track",
          properties -> new LockingTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              RailcraftBlockProperties.electricTrack(properties)));

  public static final DeferredBlock<ActivatorTrackBlock> HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK =
      deferredRegister.registerBlock("high_speed_electric_activator_track",
          properties -> new ActivatorTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              RailcraftBlockProperties.electricTrack(properties)));

  public static final DeferredBlock<BoosterTrackBlock> HIGH_SPEED_ELECTRIC_BOOSTER_TRACK =
      deferredRegister.registerBlock("high_speed_electric_booster_track",
          properties -> new BoosterTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              RailcraftBlockProperties.electricTrack(properties)));

  public static final DeferredBlock<DetectorTrackBlock> HIGH_SPEED_ELECTRIC_DETECTOR_TRACK =
      deferredRegister.registerBlock("high_speed_electric_detector_track",
          properties -> new DetectorTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              RailcraftBlockProperties.electricTrack(properties)));

  public static final DeferredBlock<WyeTrackBlock> HIGH_SPEED_ELECTRIC_WYE_TRACK =
      deferredRegister.registerBlock("high_speed_electric_wye_track",
          properties -> new WyeTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              RailcraftBlockProperties.electricTrack(properties).noLootTable()));

  public static final DeferredBlock<TurnoutTrackBlock> HIGH_SPEED_ELECTRIC_TURNOUT_TRACK =
      deferredRegister.registerBlock("high_speed_electric_turnout_track",
          properties -> new TurnoutTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              RailcraftBlockProperties.electricTrack(properties).noLootTable()));

  public static final DeferredBlock<JunctionTrackBlock> HIGH_SPEED_ELECTRIC_JUNCTION_TRACK =
      deferredRegister.registerBlock("high_speed_electric_junction_track",
          properties -> new JunctionTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              RailcraftBlockProperties.electricTrack(properties).noLootTable()));

  public static final DeferredBlock<WhistleTrackBlock> HIGH_SPEED_ELECTRIC_WHISTLE_TRACK =
      deferredRegister.registerBlock("high_speed_electric_whistle_track",
          properties -> new WhistleTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              RailcraftBlockProperties.electricTrack(properties)));

  public static final DeferredBlock<LocomotiveTrackBlock> HIGH_SPEED_ELECTRIC_LOCOMOTIVE_TRACK =
      deferredRegister.registerBlock("high_speed_electric_locomotive_track",
          properties -> new LocomotiveTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              RailcraftBlockProperties.electricTrack(properties)));

  public static final DeferredBlock<ThrottleTrackBlock> HIGH_SPEED_ELECTRIC_THROTTLE_TRACK =
      deferredRegister.registerBlock("high_speed_electric_throttle_track",
          properties -> new ThrottleTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              RailcraftBlockProperties.electricTrack(properties)));

  public static final DeferredBlock<LockingTrackBlock> IRON_LOCKING_TRACK =
      deferredRegister.registerBlock("iron_locking_track",
          properties -> new LockingTrackBlock(TrackTypes.IRON,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<BufferStopTrackBlock> IRON_BUFFER_STOP_TRACK =
      deferredRegister.registerBlock("iron_buffer_stop_track",
          properties -> new BufferStopTrackBlock(TrackTypes.IRON,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<ActivatorTrackBlock> IRON_ACTIVATOR_TRACK =
      deferredRegister.registerBlock("iron_activator_track",
          properties -> new ActivatorTrackBlock(TrackTypes.IRON,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<BoosterTrackBlock> IRON_BOOSTER_TRACK =
      deferredRegister.registerBlock("iron_booster_track",
          properties -> new BoosterTrackBlock(TrackTypes.IRON,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<ControlTrackBlock> IRON_CONTROL_TRACK =
      deferredRegister.registerBlock("iron_control_track",
          properties -> new ControlTrackBlock(TrackTypes.IRON,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<GatedTrackBlock> IRON_GATED_TRACK =
      deferredRegister.registerBlock("iron_gated_track",
          properties -> new GatedTrackBlock(TrackTypes.IRON,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<DetectorTrackBlock> IRON_DETECTOR_TRACK =
      deferredRegister.registerBlock("iron_detector_track",
          properties -> new DetectorTrackBlock(TrackTypes.IRON,
              RailcraftBlockProperties.standardTrack(properties).randomTicks()));

  public static final DeferredBlock<CouplerTrackBlock> IRON_COUPLER_TRACK =
      deferredRegister.registerBlock("iron_coupler_track",
          properties -> new CouplerTrackBlock(TrackTypes.IRON,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<EmbarkingTrackBlock> IRON_EMBARKING_TRACK =
      deferredRegister.registerBlock("iron_embarking_track",
          properties -> new EmbarkingTrackBlock(TrackTypes.IRON,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<DisembarkingTrackBlock> IRON_DISEMBARKING_TRACK =
      deferredRegister.registerBlock("iron_disembarking_track",
          properties -> new DisembarkingTrackBlock(TrackTypes.IRON,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<DumpingTrackBlock> IRON_DUMPING_TRACK =
      deferredRegister.registerBlock("iron_dumping_track",
          properties -> new DumpingTrackBlock(TrackTypes.IRON,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<WyeTrackBlock> IRON_WYE_TRACK =
      deferredRegister.registerBlock("iron_wye_track",
          properties -> new WyeTrackBlock(TrackTypes.IRON,
              RailcraftBlockProperties.standardTrack(properties).noLootTable()));

  public static final DeferredBlock<TurnoutTrackBlock> IRON_TURNOUT_TRACK =
      deferredRegister.registerBlock("iron_turnout_track",
          properties -> new TurnoutTrackBlock(TrackTypes.IRON,
              RailcraftBlockProperties.standardTrack(properties).noLootTable()));

  public static final DeferredBlock<JunctionTrackBlock> IRON_JUNCTION_TRACK =
      deferredRegister.registerBlock("iron_junction_track",
          properties -> new JunctionTrackBlock(TrackTypes.IRON,
              RailcraftBlockProperties.standardTrack(properties).noLootTable()));

  public static final DeferredBlock<LauncherTrackBlock> IRON_LAUNCHER_TRACK =
      deferredRegister.registerBlock("iron_launcher_track",
          properties -> new LauncherTrackBlock(TrackTypes.IRON,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<OneWayTrackBlock> IRON_ONE_WAY_TRACK =
      deferredRegister.registerBlock("iron_one_way_track",
          properties -> new OneWayTrackBlock(TrackTypes.IRON,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<WhistleTrackBlock> IRON_WHISTLE_TRACK =
      deferredRegister.registerBlock("iron_whistle_track",
          properties -> new WhistleTrackBlock(TrackTypes.IRON,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<LocomotiveTrackBlock> IRON_LOCOMOTIVE_TRACK =
      deferredRegister.registerBlock("iron_locomotive_track",
          properties -> new LocomotiveTrackBlock(TrackTypes.IRON,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<ThrottleTrackBlock> IRON_THROTTLE_TRACK =
      deferredRegister.registerBlock("iron_throttle_track",
          properties -> new ThrottleTrackBlock(TrackTypes.IRON,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<RoutingTrackBlock> IRON_ROUTING_TRACK =
      deferredRegister.registerBlock("iron_routing_track",
          properties -> new RoutingTrackBlock(TrackTypes.IRON,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<TrackBlock> REINFORCED_TRACK =
      deferredRegister.registerBlock("reinforced_track",
          properties -> new TrackBlock(TrackTypes.REINFORCED, properties),
          RailcraftBlockProperties::reinforcedTrack);

  public static final DeferredBlock<LockingTrackBlock> REINFORCED_LOCKING_TRACK =
      deferredRegister.registerBlock("reinforced_locking_track",
          properties -> new LockingTrackBlock(TrackTypes.REINFORCED,
              RailcraftBlockProperties.reinforcedTrack(properties)));

  public static final DeferredBlock<BufferStopTrackBlock> REINFORCED_BUFFER_STOP_TRACK =
      deferredRegister.registerBlock("reinforced_buffer_stop_track",
          properties -> new BufferStopTrackBlock(TrackTypes.REINFORCED,
              RailcraftBlockProperties.reinforcedTrack(properties)));

  public static final DeferredBlock<ActivatorTrackBlock> REINFORCED_ACTIVATOR_TRACK =
      deferredRegister.registerBlock("reinforced_activator_track",
          properties -> new ActivatorTrackBlock(TrackTypes.REINFORCED,
              RailcraftBlockProperties.reinforcedTrack(properties)));

  public static final DeferredBlock<BoosterTrackBlock> REINFORCED_BOOSTER_TRACK =
      deferredRegister.registerBlock("reinforced_booster_track",
          properties -> new BoosterTrackBlock(TrackTypes.REINFORCED,
              RailcraftBlockProperties.reinforcedTrack(properties)));

  public static final DeferredBlock<ControlTrackBlock> REINFORCED_CONTROL_TRACK =
      deferredRegister.registerBlock("reinforced_control_track",
          properties -> new ControlTrackBlock(TrackTypes.REINFORCED,
              RailcraftBlockProperties.reinforcedTrack(properties)));

  public static final DeferredBlock<GatedTrackBlock> REINFORCED_GATED_TRACK =
      deferredRegister.registerBlock("reinforced_gated_track",
          properties -> new GatedTrackBlock(TrackTypes.REINFORCED,
              RailcraftBlockProperties.reinforcedTrack(properties)));

  public static final DeferredBlock<DetectorTrackBlock> REINFORCED_DETECTOR_TRACK =
      deferredRegister.registerBlock("reinforced_detector_track",
          properties -> new DetectorTrackBlock(TrackTypes.REINFORCED,
              RailcraftBlockProperties.reinforcedTrack(properties).randomTicks()));

  public static final DeferredBlock<CouplerTrackBlock> REINFORCED_COUPLER_TRACK =
      deferredRegister.registerBlock("reinforced_coupler_track",
          properties -> new CouplerTrackBlock(TrackTypes.REINFORCED,
              RailcraftBlockProperties.reinforcedTrack(properties)));

  public static final DeferredBlock<EmbarkingTrackBlock> REINFORCED_EMBARKING_TRACK =
      deferredRegister.registerBlock("reinforced_embarking_track",
          properties -> new EmbarkingTrackBlock(TrackTypes.REINFORCED,
              RailcraftBlockProperties.reinforcedTrack(properties)));

  public static final DeferredBlock<DisembarkingTrackBlock> REINFORCED_DISEMBARKING_TRACK =
      deferredRegister.registerBlock("reinforced_disembarking_track",
          properties -> new DisembarkingTrackBlock(TrackTypes.REINFORCED,
              RailcraftBlockProperties.reinforcedTrack(properties)));

  public static final DeferredBlock<DumpingTrackBlock> REINFORCED_DUMPING_TRACK =
      deferredRegister.registerBlock("reinforced_dumping_track",
          properties -> new DumpingTrackBlock(TrackTypes.REINFORCED,
              RailcraftBlockProperties.reinforcedTrack(properties)));

  public static final DeferredBlock<WyeTrackBlock> REINFORCED_WYE_TRACK =
      deferredRegister.registerBlock("reinforced_wye_track",
          properties -> new WyeTrackBlock(TrackTypes.REINFORCED,
              RailcraftBlockProperties.reinforcedTrack(properties).noLootTable()));

  public static final DeferredBlock<TurnoutTrackBlock> REINFORCED_TURNOUT_TRACK =
      deferredRegister.registerBlock("reinforced_turnout_track",
          properties -> new TurnoutTrackBlock(TrackTypes.REINFORCED,
              RailcraftBlockProperties.reinforcedTrack(properties).noLootTable()));

  public static final DeferredBlock<JunctionTrackBlock> REINFORCED_JUNCTION_TRACK =
      deferredRegister.registerBlock("reinforced_junction_track",
          properties -> new JunctionTrackBlock(TrackTypes.REINFORCED,
              RailcraftBlockProperties.reinforcedTrack(properties).noLootTable()));

  public static final DeferredBlock<LauncherTrackBlock> REINFORCED_LAUNCHER_TRACK =
      deferredRegister.registerBlock("reinforced_launcher_track",
          properties -> new LauncherTrackBlock(TrackTypes.REINFORCED,
              RailcraftBlockProperties.reinforcedTrack(properties)));

  public static final DeferredBlock<OneWayTrackBlock> REINFORCED_ONE_WAY_TRACK =
      deferredRegister.registerBlock("reinforced_one_way_track",
          properties -> new OneWayTrackBlock(TrackTypes.REINFORCED,
              RailcraftBlockProperties.reinforcedTrack(properties)));

  public static final DeferredBlock<WhistleTrackBlock> REINFORCED_WHISTLE_TRACK =
      deferredRegister.registerBlock("reinforced_whistle_track",
          properties -> new WhistleTrackBlock(TrackTypes.REINFORCED,
              RailcraftBlockProperties.reinforcedTrack(properties)));

  public static final DeferredBlock<LocomotiveTrackBlock> REINFORCED_LOCOMOTIVE_TRACK =
      deferredRegister.registerBlock("reinforced_locomotive_track",
          properties -> new LocomotiveTrackBlock(TrackTypes.REINFORCED,
              RailcraftBlockProperties.reinforcedTrack(properties)));

  public static final DeferredBlock<ThrottleTrackBlock> REINFORCED_THROTTLE_TRACK =
      deferredRegister.registerBlock("reinforced_throttle_track",
          properties -> new ThrottleTrackBlock(TrackTypes.REINFORCED,
              RailcraftBlockProperties.reinforcedTrack(properties)));

  public static final DeferredBlock<RoutingTrackBlock> REINFORCED_ROUTING_TRACK =
      deferredRegister.registerBlock("reinforced_routing_track",
          properties -> new RoutingTrackBlock(TrackTypes.REINFORCED,
              RailcraftBlockProperties.reinforcedTrack(properties)));

  public static final DeferredBlock<TrackBlock> STRAP_IRON_TRACK =
      deferredRegister.registerBlock("strap_iron_track",
          properties -> new TrackBlock(TrackTypes.STRAP_IRON, properties),
          RailcraftBlockProperties::standardTrack);

  public static final DeferredBlock<LockingTrackBlock> STRAP_IRON_LOCKING_TRACK =
      deferredRegister.registerBlock("strap_iron_locking_track",
          properties -> new LockingTrackBlock(TrackTypes.STRAP_IRON,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<BufferStopTrackBlock> STRAP_IRON_BUFFER_STOP_TRACK =
      deferredRegister.registerBlock("strap_iron_buffer_stop_track",
          properties -> new BufferStopTrackBlock(TrackTypes.STRAP_IRON,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<ActivatorTrackBlock> STRAP_IRON_ACTIVATOR_TRACK =
      deferredRegister.registerBlock("strap_iron_activator_track",
          properties -> new ActivatorTrackBlock(TrackTypes.STRAP_IRON,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<BoosterTrackBlock> STRAP_IRON_BOOSTER_TRACK =
      deferredRegister.registerBlock("strap_iron_booster_track",
          properties -> new BoosterTrackBlock(TrackTypes.STRAP_IRON,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<ControlTrackBlock> STRAP_IRON_CONTROL_TRACK =
      deferredRegister.registerBlock("strap_iron_control_track",
          properties -> new ControlTrackBlock(TrackTypes.STRAP_IRON,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<GatedTrackBlock> STRAP_IRON_GATED_TRACK =
      deferredRegister.registerBlock("strap_iron_gated_track",
          properties -> new GatedTrackBlock(TrackTypes.STRAP_IRON,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<DetectorTrackBlock> STRAP_IRON_DETECTOR_TRACK =
      deferredRegister.registerBlock("strap_iron_detector_track",
          properties -> new DetectorTrackBlock(TrackTypes.STRAP_IRON,
              RailcraftBlockProperties.standardTrack(properties).randomTicks()));

  public static final DeferredBlock<CouplerTrackBlock> STRAP_IRON_COUPLER_TRACK =
      deferredRegister.registerBlock("strap_iron_coupler_track",
          properties -> new CouplerTrackBlock(TrackTypes.STRAP_IRON,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<EmbarkingTrackBlock> STRAP_IRON_EMBARKING_TRACK =
      deferredRegister.registerBlock("strap_iron_embarking_track",
          properties -> new EmbarkingTrackBlock(TrackTypes.STRAP_IRON,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<DisembarkingTrackBlock> STRAP_IRON_DISEMBARKING_TRACK =
      deferredRegister.registerBlock("strap_iron_disembarking_track",
          properties -> new DisembarkingTrackBlock(TrackTypes.STRAP_IRON,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<DumpingTrackBlock> STRAP_IRON_DUMPING_TRACK =
      deferredRegister.registerBlock("strap_iron_dumping_track",
          properties -> new DumpingTrackBlock(TrackTypes.STRAP_IRON,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<WyeTrackBlock> STRAP_IRON_WYE_TRACK =
      deferredRegister.registerBlock("strap_iron_wye_track",
          properties -> new WyeTrackBlock(TrackTypes.STRAP_IRON,
              RailcraftBlockProperties.standardTrack(properties).noLootTable()));

  public static final DeferredBlock<TurnoutTrackBlock> STRAP_IRON_TURNOUT_TRACK =
      deferredRegister.registerBlock("strap_iron_turnout_track",
          properties -> new TurnoutTrackBlock(TrackTypes.STRAP_IRON,
              RailcraftBlockProperties.standardTrack(properties).noLootTable()));

  public static final DeferredBlock<JunctionTrackBlock> STRAP_IRON_JUNCTION_TRACK =
      deferredRegister.registerBlock("strap_iron_junction_track",
          properties -> new JunctionTrackBlock(TrackTypes.STRAP_IRON,
              RailcraftBlockProperties.standardTrack(properties).noLootTable()));

  public static final DeferredBlock<LauncherTrackBlock> STRAP_IRON_LAUNCHER_TRACK =
      deferredRegister.registerBlock("strap_iron_launcher_track",
          properties -> new LauncherTrackBlock(TrackTypes.STRAP_IRON,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<OneWayTrackBlock> STRAP_IRON_ONE_WAY_TRACK =
      deferredRegister.registerBlock("strap_iron_one_way_track",
          properties -> new OneWayTrackBlock(TrackTypes.STRAP_IRON,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<WhistleTrackBlock> STRAP_IRON_WHISTLE_TRACK =
      deferredRegister.registerBlock("strap_iron_whistle_track",
          properties -> new WhistleTrackBlock(TrackTypes.STRAP_IRON,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<LocomotiveTrackBlock> STRAP_IRON_LOCOMOTIVE_TRACK =
      deferredRegister.registerBlock("strap_iron_locomotive_track",
          properties -> new LocomotiveTrackBlock(TrackTypes.STRAP_IRON,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<ThrottleTrackBlock> STRAP_IRON_THROTTLE_TRACK =
      deferredRegister.registerBlock("strap_iron_throttle_track",
          properties -> new ThrottleTrackBlock(TrackTypes.STRAP_IRON,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<RoutingTrackBlock> STRAP_IRON_ROUTING_TRACK =
      deferredRegister.registerBlock("strap_iron_routing_track",
          properties -> new RoutingTrackBlock(TrackTypes.STRAP_IRON,
              RailcraftBlockProperties.standardTrack(properties)));

  public static final DeferredBlock<ElevatorTrackBlock> ELEVATOR_TRACK =
      deferredRegister.registerBlock("elevator_track",
          properties -> new ElevatorTrackBlock(properties
              .noCollision()
              .strength(1.05F)
              .sound(SoundType.METAL)));

  public static final DeferredBlock<FirestoneBlock> FIRESTONE_ORE =
      deferredRegister.registerBlock("firestone_ore",
          properties -> new FirestoneBlock(properties
              .mapColor(MapColor.STONE)
              .lightLevel(__ -> 15)
              .strength(3, 5)));

  public static final DeferredBlock<RitualBlock> RITUAL =
      deferredRegister.registerBlock("ritual",
          properties -> new RitualBlock(properties
              .mapColor(MapColor.STONE)
              .lightLevel(__ -> 1)
              .noOcclusion()
              .noLootTable()));

  public static final DeferredBlock<ManualRollingMachineBlock> MANUAL_ROLLING_MACHINE =
      deferredRegister.registerBlock("manual_rolling_machine",
          properties -> new ManualRollingMachineBlock(properties
              .mapColor(MapColor.WOOD)
              .sound(SoundType.WOOD)
              .strength(2.5F)));

  public static final DeferredBlock<PoweredRollingMachineBlock> POWERED_ROLLING_MACHINE =
      deferredRegister.registerBlock("powered_rolling_machine",
          properties -> new PoweredRollingMachineBlock(properties
              .mapColor(MapColor.METAL)
              .sound(SoundType.METAL)
              .strength(3.0F)
              .randomTicks()));

  public static final DeferredBlock<CrusherMultiblockBlock> CRUSHER =
      deferredRegister.registerBlock("crusher",
          properties -> new CrusherMultiblockBlock(properties
              .mapColor(MapColor.METAL)
              .requiresCorrectToolForDrops()
              .randomTicks()
              .strength(3.5F)
              .sound(SoundType.STONE)));

  public static final DeferredBlock<CokeOvenBricksBlock> COKE_OVEN_BRICKS =
      deferredRegister.registerBlock("coke_oven_bricks",
          properties -> new CokeOvenBricksBlock(properties
              .mapColor(MapColor.STONE)
              .sound(SoundType.STONE)
              .isValidSpawn(RailcraftBlocks::never)
              .lightLevel(litBlockEmission(13))
              .strength(2F, 6.0F)));

  public static final DeferredBlock<SteamOvenBlock> STEAM_OVEN =
      deferredRegister.registerBlock("steam_oven",
          properties -> new SteamOvenBlock(properties
              .mapColor(MapColor.STONE)
              .requiresCorrectToolForDrops()
              .sound(SoundType.STONE)
              .strength(3.5F)));

  public static final DeferredBlock<CreosoteLiquidBlock> CREOSOTE =
      deferredRegister.registerBlock("creosote",
          properties -> new CreosoteLiquidBlock(
              properties
                  .mapColor(MapColor.WATER)
                  .liquid()
                  .noCollision()
                  .strength(50.0F)
                  .pushReaction(PushReaction.DESTROY)
                  .noLootTable()
                  .sound(SoundType.EMPTY)));

  public static final DeferredBlock<Block> CRUSHED_OBSIDIAN =
      deferredRegister.registerBlock("crushed_obsidian",
          properties -> new Block(properties
              .mapColor(MapColor.COLOR_BLACK)
              .isValidSpawn(RailcraftBlocks::never)
              .requiresCorrectToolForDrops()
              .strength(50, 1200)));

  public static final VariantSet<DecorativeBlock, Block, ? extends Block> DECORATIVE_STONE =
      VariantSet.of(
          DecorativeBlock.class,
          deferredRegister,
          "%s_stone",
          Block::new,
          BlockBehaviour.Properties.ofFullCopy(Blocks.STONE));

  public static final VariantSet<DecorativeBlock, Block, ? extends Block> DECORATIVE_COBBLESTONE =
      VariantSet.of(
          DecorativeBlock.class,
          deferredRegister,
          "%s_cobblestone",
          Block::new,
          BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE));

  public static final VariantSet<DecorativeBlock, Block, ? extends Block> POLISHED_DECORATIVE_STONE =
      VariantSet.of(
          DecorativeBlock.class,
          deferredRegister,
          "polished_%s_stone",
          Block::new,
          BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_ANDESITE));

  public static final VariantSet<DecorativeBlock, Block, ? extends Block> CHISELED_DECORATIVE_STONE =
      VariantSet.of(
          DecorativeBlock.class,
          deferredRegister,
          "chiseled_%s_stone",
          Block::new,
          BlockBehaviour.Properties.ofFullCopy(Blocks.CHISELED_STONE_BRICKS));

  public static final VariantSet<DecorativeBlock, Block, ? extends Block> ETCHED_DECORATIVE_STONE =
      VariantSet.of(
          DecorativeBlock.class,
          deferredRegister,
          "etched_%s_stone",
          Block::new,
          BlockBehaviour.Properties.ofFullCopy(Blocks.STONE));

  public static final VariantSet<DecorativeBlock, Block, ? extends Block> DECORATIVE_BRICKS =
      VariantSet.of(
          DecorativeBlock.class,
          deferredRegister,
          "%s_bricks",
          Block::new,
          BlockBehaviour.Properties.ofFullCopy(Blocks.STONE));

  public static final VariantSet<DecorativeBlock, Block, StairBlock> DECORATIVE_BRICK_STAIRS =
      VariantSet.of(
          DecorativeBlock.class,
          deferredRegister,
          "%s_brick_stairs",
          (properties, type) -> new StairBlock(DECORATIVE_BRICKS.variantFor(type).get().defaultBlockState(), properties),
          BlockBehaviour.Properties.ofFullCopy(Blocks.STONE));

  public static final VariantSet<DecorativeBlock, Block, SlabBlock> DECORATIVE_BRICK_SLAB =
      VariantSet.of(
          DecorativeBlock.class,
          deferredRegister,
          "%s_brick_slab",
          SlabBlock::new,
          BlockBehaviour.Properties.ofFullCopy(Blocks.STONE));

  public static final VariantSet<DecorativeBlock, Block, ? extends Block> DECORATIVE_PAVER =
      VariantSet.of(
          DecorativeBlock.class,
          deferredRegister,
          "%s_paver",
          Block::new,
          BlockBehaviour.Properties.ofFullCopy(Blocks.STONE));

  public static final VariantSet<DecorativeBlock, Block, StairBlock> DECORATIVE_PAVER_STAIRS =
      VariantSet.of(
          DecorativeBlock.class,
          deferredRegister,
          "%s_paver_stairs",
          (properties, type) -> new StairBlock(DECORATIVE_PAVER.variantFor(type).get().defaultBlockState(), properties),
          BlockBehaviour.Properties.ofFullCopy(Blocks.STONE));

  public static final VariantSet<DecorativeBlock, Block, SlabBlock> DECORATIVE_PAVER_SLAB =
      VariantSet.of(
          DecorativeBlock.class,
          deferredRegister,
          "%s_paver_slab",
          SlabBlock::new,
          BlockBehaviour.Properties.ofFullCopy(Blocks.STONE));

  public static final DeferredBlock<WorldSpikeBlock> WORLD_SPIKE =
      deferredRegister.registerBlock("world_spike", WorldSpikeBlock::new,
          RailcraftBlockProperties.worldSpike());

  public static final DeferredBlock<PersonalWorldSpikeBlock> PERSONAL_WORLD_SPIKE =
      deferredRegister.registerBlock("personal_world_spike", PersonalWorldSpikeBlock::new,
          RailcraftBlockProperties.worldSpike());

  public static final DeferredBlock<VoidChestBlock> VOID_CHEST =
      deferredRegister.register("void_chest",
          () -> new VoidChestBlock(BlockBehaviour.Properties.of()
              .mapColor(MapColor.METAL)
              .sound(SoundType.METAL)
              .requiresCorrectToolForDrops()
              .strength(50, 1200)
              .randomTicks()));

  private static ToIntFunction<BlockState> litBlockEmission(int light) {
    return blockState -> blockState.getValue(BlockStateProperties.LIT) ? light : 0;
  }

  private static Boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos,
      EntityType<?> entityType) {
    return false;
  }
}
