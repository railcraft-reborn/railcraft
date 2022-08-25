package mods.railcraft.world.level.block;

import java.util.Collection;
import java.util.function.ToIntFunction;
import mods.railcraft.Railcraft;
import mods.railcraft.util.VariantRegistrar;
import mods.railcraft.world.level.block.manipulator.AdvancedItemLoaderBlock;
import mods.railcraft.world.level.block.manipulator.AdvancedItemUnloaderBlock;
import mods.railcraft.world.level.block.manipulator.FluidLoaderBlock;
import mods.railcraft.world.level.block.manipulator.FluidUnloaderBlock;
import mods.railcraft.world.level.block.manipulator.ItemLoaderBlock;
import mods.railcraft.world.level.block.manipulator.ItemUnloaderBlock;
import mods.railcraft.world.level.block.post.PostBlock;
import mods.railcraft.world.level.block.signal.AnalogSignalControllerBoxBlock;
import mods.railcraft.world.level.block.signal.BlockSignalBlock;
import mods.railcraft.world.level.block.signal.SignalBlockRelayBoxBlock;
import mods.railcraft.world.level.block.signal.DistantSignalBlock;
import mods.railcraft.world.level.block.signal.DualBlockSignalBlock;
import mods.railcraft.world.level.block.signal.DualDistantSignalBlock;
import mods.railcraft.world.level.block.signal.DualSignalBlock;
import mods.railcraft.world.level.block.signal.DualTokenSignalBlock;
import mods.railcraft.world.level.block.signal.SignalBoxBlock;
import mods.railcraft.world.level.block.signal.SignalCapacitorBoxBlock;
import mods.railcraft.world.level.block.signal.SignalControllerBoxBlock;
import mods.railcraft.world.level.block.signal.SignalInterlockBoxBlock;
import mods.railcraft.world.level.block.signal.SignalReceiverBoxBlock;
import mods.railcraft.world.level.block.signal.SignalSequencerBoxBlock;
import mods.railcraft.world.level.block.signal.SingleSignalBlock;
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
import mods.railcraft.world.level.block.track.ElevatorTrackBlock;
import mods.railcraft.world.level.block.track.ForceTrackBlock;
import mods.railcraft.world.level.block.track.TrackBlock;
import mods.railcraft.world.level.block.track.TrackConstants;
import mods.railcraft.world.level.block.track.TrackTypes;
import mods.railcraft.world.level.block.track.actuator.SwitchTrackActuatorBlock;
import mods.railcraft.world.level.block.track.actuator.SwitchTrackLeverBlock;
import mods.railcraft.world.level.block.track.actuator.SwitchTrackMotorBlock;
import mods.railcraft.world.level.block.track.outfitted.ActivatorTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.BoosterTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.BufferStopTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.ControlTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.CouplerTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.DetectorTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.DisembarkingTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.EmbarkingTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.GatedTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.JunctionTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.LauncherTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.LockingTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.LocomotiveTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.OneWayTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.TransitionTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.TurnoutTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.WyeTrackBlock;
import mods.railcraft.world.level.material.RailcraftMaterials;
import mods.railcraft.world.level.material.fluid.RailcraftFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftBlocks {

  private static final DeferredRegister<Block> deferredRegister =
      DeferredRegister.create(ForgeRegistries.BLOCKS, Railcraft.ID);

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }

  public static Collection<RegistryObject<Block>> entries() {
    return deferredRegister.getEntries();
  }

  public static final VariantRegistrar<DyeColor, StrengthenedGlassBlock> STRENGTHENED_GLASS =
      VariantRegistrar.<DyeColor, StrengthenedGlassBlock>from(DyeColor.class, deferredRegister)
          .register("strengthened_glass", RailcraftBlocks::buildStrengthenedGlass);
  public static final VariantRegistrar<DyeColor, IronTankGaugeBlock> IRON_TANK_GAUGE =
      VariantRegistrar.<DyeColor, IronTankGaugeBlock>from(DyeColor.class, deferredRegister)
          .register("iron_tank_gauge", RailcraftBlocks::buildIronTankGauge);
  public static final VariantRegistrar<DyeColor, IronTankValveBlock> IRON_TANK_VALVE =
      VariantRegistrar.<DyeColor, IronTankValveBlock>from(DyeColor.class, deferredRegister)
          .register("iron_tank_valve", RailcraftBlocks::buildIronTankValve);
  public static final VariantRegistrar<DyeColor, IronTankWallBlock> IRON_TANK_WALL =
      VariantRegistrar.<DyeColor, IronTankWallBlock>from(DyeColor.class, deferredRegister)
          .register("iron_tank_wall", RailcraftBlocks::buildIronTankWall);
  public static final VariantRegistrar<DyeColor, SteelTankGaugeBlock> STEEL_TANK_GAUGE =
      VariantRegistrar.<DyeColor, SteelTankGaugeBlock>from(DyeColor.class, deferredRegister)
          .register("steel_tank_gauge", RailcraftBlocks::buildSteelTankGauge);
  public static final VariantRegistrar<DyeColor, SteelTankValveBlock> STEEL_TANK_VALVE =
      VariantRegistrar.<DyeColor, SteelTankValveBlock>from(DyeColor.class, deferredRegister)
          .register("steel_tank_valve", RailcraftBlocks::buildSteelTankValve);
  public static final VariantRegistrar<DyeColor, SteelTankWallBlock> STEEL_TANK_WALL =
      VariantRegistrar.<DyeColor, SteelTankWallBlock>from(DyeColor.class, deferredRegister)
          .register("steel_tank_wall", RailcraftBlocks::buildSteelTankWall);
  public static final VariantRegistrar<DyeColor, PostBlock> POST =
      VariantRegistrar.<DyeColor, PostBlock>from(DyeColor.class, deferredRegister)
          .register("post", RailcraftBlocks::buildPost);

  private static StrengthenedGlassBlock buildStrengthenedGlass() {
    return new StrengthenedGlassBlock(BlockBehaviour.Properties.of(Material.GLASS)
        .sound(SoundType.GLASS)
        .noOcclusion()
        .strength(1.0F, 5.0F)
        .isValidSpawn(RailcraftBlocks::never)
        .isRedstoneConductor(RailcraftBlocks::never)
        .isSuffocating(RailcraftBlocks::never)
        .isViewBlocking(RailcraftBlocks::never));
  }

  private static IronTankGaugeBlock buildIronTankGauge() {
    return new IronTankGaugeBlock(BlockBehaviour.Properties.of(Material.GLASS)
        .sound(SoundType.GLASS)
        .noOcclusion()
        .strength(1.0F, 5.0F)
        .isValidSpawn(RailcraftBlocks::never)
        .isRedstoneConductor(RailcraftBlocks::never)
        .isSuffocating(RailcraftBlocks::never)
        .isViewBlocking(RailcraftBlocks::never)
        .lightLevel(LightBlock.LIGHT_EMISSION));
  }

  private static IronTankValveBlock buildIronTankValve() {
    return new IronTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
        .sound(SoundType.METAL)
        .noOcclusion()
        .explosionResistance(12));
  }

  private static IronTankWallBlock buildIronTankWall() {
    return new IronTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
        .sound(SoundType.METAL)
        .noOcclusion()
        .explosionResistance(12));
  }

  private static SteelTankGaugeBlock buildSteelTankGauge() {
    return new SteelTankGaugeBlock(BlockBehaviour.Properties.of(Material.GLASS)
        .sound(SoundType.GLASS)
        .noOcclusion()
        .strength(1.0F, 5.0F)
        .isValidSpawn(RailcraftBlocks::never)
        .isRedstoneConductor(RailcraftBlocks::never)
        .isSuffocating(RailcraftBlocks::never)
        .isViewBlocking(RailcraftBlocks::never)
        .lightLevel(LightBlock.LIGHT_EMISSION));
  }

  private static SteelTankValveBlock buildSteelTankValve() {
    return new SteelTankValveBlock(BlockBehaviour.Properties.of(Material.METAL)
        .sound(SoundType.METAL)
        .noOcclusion()
        .explosionResistance(15));
  }

  private static SteelTankWallBlock buildSteelTankWall() {
    return new SteelTankWallBlock(BlockBehaviour.Properties.of(Material.METAL)
        .sound(SoundType.METAL)
        .noOcclusion()
        .explosionResistance(15));
  }

  private static PostBlock buildPost() {
    return new PostBlock(BlockBehaviour.Properties.of(Material.METAL)
        .sound(SoundType.METAL));
  }

  public static final RegistryObject<SteamBoilerTankBlock> LOW_PRESSURE_STEAM_BOILER_TANK =
      deferredRegister.register("low_pressure_steam_boiler_tank",
          () -> new SteamBoilerTankBlock(BlockBehaviour.Properties.of(Material.METAL)
              .noOcclusion()
              .sound(SoundType.METAL)));

  public static final RegistryObject<SteamBoilerTankBlock> HIGH_PRESSURE_STEAM_BOILER_TANK =
      deferredRegister.register("high_pressure_steam_boiler_tank",
          () -> new SteamBoilerTankBlock(BlockBehaviour.Properties.of(Material.METAL)
              .noOcclusion()
              .sound(SoundType.METAL)));

  public static final RegistryObject<SolidFueledFireboxBlock> SOLID_FUELED_FIREBOX =
      deferredRegister.register("solid_fueled_firebox",
          () -> new SolidFueledFireboxBlock(BlockBehaviour.Properties.of(Material.STONE)
              .lightLevel(litBlockEmission(13))
              .sound(SoundType.STONE)));

  public static final RegistryObject<FluidFueledFireboxBlock> FLUID_FUELED_FIREBOX =
      deferredRegister.register("fluid_fueled_firebox",
          () -> new FluidFueledFireboxBlock(BlockBehaviour.Properties.of(Material.STONE)
              .lightLevel(litBlockEmission(13))
              .sound(SoundType.METAL)));

  public static final RegistryObject<Block> STEAM_TURBINE =
      deferredRegister.register("steam_turbine",
          () -> new SteamTurbineBlock(BlockBehaviour.Properties.of(Material.METAL)
              .randomTicks()
              .sound(SoundType.METAL)));

  public static final RegistryObject<BlastFurnaceBricksBlock> BLAST_FURNACE_BRICKS =
      deferredRegister.register("blast_furnace_bricks",
          () -> new BlastFurnaceBricksBlock(BlockBehaviour.Properties.of(Material.STONE)
              .lightLevel(litBlockEmission(13))
              .sound(SoundType.STONE)));

  public static final RegistryObject<Block> FEED_STATION =
      deferredRegister.register("feed_station",
          () -> new FeedStationBlock(
              BlockBehaviour.Properties.of(Material.STONE, MaterialColor.WOOD)
                  .sound(SoundType.WOOD)));

  public static final RegistryObject<AnvilBlock> STEEL_ANVIL =
      deferredRegister.register("steel_anvil",
          () -> new AnvilBlock(
              BlockBehaviour.Properties.of(Material.HEAVY_METAL, MaterialColor.METAL)
                  .requiresCorrectToolForDrops()
                  .strength(5.0F, 2000.0F)
                  .sound(SoundType.ANVIL)));

  public static final RegistryObject<AnvilBlock> CHIPPED_STEEL_ANVIL =
      deferredRegister.register("chipped_steel_anvil",
          () -> new AnvilBlock(
              BlockBehaviour.Properties.of(Material.HEAVY_METAL, MaterialColor.METAL)
                  .requiresCorrectToolForDrops()
                  .strength(5.0F, 2000.0F)
                  .sound(SoundType.ANVIL)));

  public static final RegistryObject<AnvilBlock> DAMAGED_STEEL_ANVIL =
      deferredRegister.register("damaged_steel_anvil",
          () -> new AnvilBlock(
              BlockBehaviour.Properties.of(Material.HEAVY_METAL, MaterialColor.METAL)
                  .requiresCorrectToolForDrops()
                  .strength(5.0F, 2000.0F)
                  .sound(SoundType.ANVIL)));

  public static final RegistryObject<Block> STEEL_BLOCK =
      deferredRegister.register("steel_block",
          () -> new Block(BlockBehaviour.Properties.of(Material.METAL)
              .strength(5.0F, 15.0F)
              .sound(SoundType.METAL)
              .requiresCorrectToolForDrops()));

  public static final RegistryObject<Block> BRASS_BLOCK =
      deferredRegister.register("brass_block",
          () -> new Block(BlockBehaviour.Properties.of(Material.METAL)
              .strength(3.0F, 6.0F)
              .sound(SoundType.METAL)
              .requiresCorrectToolForDrops()));

  public static final RegistryObject<Block> BRONZE_BLOCK =
      deferredRegister.register("bronze_block",
          () -> new Block(BlockBehaviour.Properties.of(Material.METAL)
              .strength(3.0F, 6.0F)
              .sound(SoundType.METAL)
              .requiresCorrectToolForDrops()));

  public static final RegistryObject<Block> INVAR_BLOCK =
      deferredRegister.register("invar_block",
          () -> new Block(BlockBehaviour.Properties.of(Material.METAL)
              .strength(5.0F, 6.0F)
              .sound(SoundType.METAL)
              .requiresCorrectToolForDrops()));

  public static final RegistryObject<Block> LEAD_BLOCK =
      deferredRegister.register("lead_block",
          () -> new Block(BlockBehaviour.Properties.of(Material.METAL)
              .strength(5.0F, 6.0F)
              .sound(SoundType.METAL)
              .requiresCorrectToolForDrops()));

  public static final RegistryObject<Block> NICKEL_BLOCK =
      deferredRegister.register("nickel_block",
          () -> new Block(BlockBehaviour.Properties.of(Material.METAL)
              .strength(5.0F, 6.0F)
              .sound(SoundType.METAL)
              .requiresCorrectToolForDrops()));

  public static final RegistryObject<Block> SILVER_BLOCK =
      deferredRegister.register("silver_block",
          () -> new Block(BlockBehaviour.Properties.of(Material.METAL)
              .strength(3.0F, 6.0F)
              .sound(SoundType.METAL)
              .requiresCorrectToolForDrops()));

  public static final RegistryObject<Block> TIN_BLOCK =
      deferredRegister.register("tin_block",
          () -> new Block(BlockBehaviour.Properties.of(Material.METAL)
              .strength(3.0F, 6.0F)
              .sound(SoundType.METAL)
              .requiresCorrectToolForDrops()));

  public static final RegistryObject<Block> ZINC_BLOCK =
      deferredRegister.register("zinc_block",
          () -> new Block(BlockBehaviour.Properties.of(Material.METAL)
              .strength(5.0F, 15.0F)
              .sound(SoundType.METAL)
              .requiresCorrectToolForDrops()));

  public static final RegistryObject<Block> COKE_BLOCK =
      deferredRegister.register("coal_coke_block",
          () -> new CoalCokeBlock(5, 300, BlockBehaviour.Properties.of(Material.STONE)
              .strength(5.0F, 10.0F)
              .sound(SoundType.STONE)));

  public static final RegistryObject<FluidLoaderBlock> FLUID_LOADER =
      deferredRegister.register("fluid_loader",
          () -> new FluidLoaderBlock(BlockBehaviour.Properties.of(Material.STONE)
              .sound(SoundType.STONE)
              .noOcclusion()));

  public static final RegistryObject<FluidUnloaderBlock> FLUID_UNLOADER =
      deferredRegister.register("fluid_unloader",
          () -> new FluidUnloaderBlock(BlockBehaviour.Properties.of(Material.STONE)
              .sound(SoundType.STONE)
              .noOcclusion()));

  public static final RegistryObject<AdvancedItemLoaderBlock> ADVANCED_ITEM_LOADER =
      deferredRegister.register("advanced_item_loader",
          () -> new AdvancedItemLoaderBlock(BlockBehaviour.Properties.of(Material.STONE)
              .sound(SoundType.STONE)));

  public static final RegistryObject<AdvancedItemUnloaderBlock> ADVANCED_ITEM_UNLOADER =
      deferredRegister.register("advanced_item_unloader",
          () -> new AdvancedItemUnloaderBlock(BlockBehaviour.Properties.of(Material.STONE)
              .sound(SoundType.STONE)));

  public static final RegistryObject<ItemLoaderBlock> ITEM_LOADER =
      deferredRegister.register("item_loader",
          () -> new ItemLoaderBlock(BlockBehaviour.Properties.of(Material.STONE)
              .sound(SoundType.STONE)));

  public static final RegistryObject<ItemUnloaderBlock> ITEM_UNLOADER =
      deferredRegister.register("item_unloader",
          () -> new ItemUnloaderBlock(BlockBehaviour.Properties.of(Material.STONE)
              .sound(SoundType.STONE)));

  public static final RegistryObject<SwitchTrackActuatorBlock> SWITCH_TRACK_LEVER =
      deferredRegister.register("switch_track_lever",
          () -> new SwitchTrackLeverBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .sound(SoundType.METAL)
              .noOcclusion()));

  public static final RegistryObject<SwitchTrackActuatorBlock> SWITCH_TRACK_MOTOR =
      deferredRegister.register("switch_track_motor",
          () -> new SwitchTrackMotorBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .sound(SoundType.METAL)
              .noOcclusion()));

  public static final RegistryObject<SignalBoxBlock> ANALOG_SIGNAL_CONTROLLER_BOX =
      deferredRegister.register("analog_signal_controller_box",
          () -> new AnalogSignalControllerBoxBlock(
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .strength(8.0F, 50.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion()));

  public static final RegistryObject<SignalBoxBlock> SIGNAL_SEQUENCER_BOX =
      deferredRegister.register("signal_sequencer_box",
          () -> new SignalSequencerBoxBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .sound(SoundType.METAL)
              .noOcclusion()));

  public static final RegistryObject<SignalBoxBlock> SIGNAL_CAPACITOR_BOX =
      deferredRegister.register("signal_capacitor_box",
          () -> new SignalCapacitorBoxBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .sound(SoundType.METAL)
              .noOcclusion()));

  public static final RegistryObject<SignalBoxBlock> SIGNAL_INTERLOCK_BOX =
      deferredRegister.register("signal_interlock_box",
          () -> new SignalInterlockBoxBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .sound(SoundType.METAL)
              .noOcclusion()));

  public static final RegistryObject<SignalBoxBlock> SIGNAL_BLOCK_RELAY_BOX =
      deferredRegister.register("signal_block_relay_box",
          () -> new SignalBlockRelayBoxBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .sound(SoundType.METAL)
              .noOcclusion()));

  public static final RegistryObject<SignalBoxBlock> SIGNAL_RECEIVER_BOX =
      deferredRegister.register("signal_receiver_box",
          () -> new SignalReceiverBoxBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .sound(SoundType.METAL)
              .noOcclusion()));

  public static final RegistryObject<SignalBoxBlock> SIGNAL_CONTROLLER_BOX =
      deferredRegister.register("signal_controller_box",
          () -> new SignalControllerBoxBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .sound(SoundType.METAL)
              .noOcclusion()));

  public static final RegistryObject<DualSignalBlock> DUAL_BLOCK_SIGNAL =
      deferredRegister.register("dual_block_signal",
          () -> new DualBlockSignalBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .noOcclusion()));

  public static final RegistryObject<DualSignalBlock> DUAL_DISTANT_SIGNAL =
      deferredRegister.register("dual_distant_signal",
          () -> new DualDistantSignalBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .noOcclusion()));

  public static final RegistryObject<DualSignalBlock> DUAL_TOKEN_SIGNAL =
      deferredRegister.register("dual_token_signal",
          () -> new DualTokenSignalBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .noOcclusion()));

  public static final RegistryObject<SingleSignalBlock> BLOCK_SIGNAL =
      deferredRegister.register("block_signal",
          () -> new BlockSignalBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .noOcclusion()));

  public static final RegistryObject<SingleSignalBlock> DISTANT_SIGNAL =
      deferredRegister.register("distant_signal",
          () -> new DistantSignalBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .noOcclusion()));

  public static final RegistryObject<SingleSignalBlock> TOKEN_SIGNAL =
      deferredRegister.register("token_signal",
          () -> new TokenSignalBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .strength(8.0F, 50.0F)
              .noOcclusion()));

  public static final RegistryObject<ForceTrackBlock> FORCE_TRACK =
      deferredRegister.register("force_track",
          () -> new ForceTrackBlock(BlockBehaviour.Properties.of(Material.DECORATION)
              .sound(SoundType.GLASS)
              .instabreak()
              .noCollission()
              .randomTicks()));

  public static final RegistryObject<ForceTrackEmitterBlock> FORCE_TRACK_EMITTER =
      deferredRegister.register("force_track_emitter",
          () -> new ForceTrackEmitterBlock(BlockBehaviour.Properties.of(Material.METAL)
              .sound(SoundType.METAL)
              .randomTicks()));

  public static final RegistryObject<TrackBlock> ABANDONED_TRACK =
      deferredRegister.register("abandoned_track",
          () -> new AbandonedTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<LockingTrackBlock> ABANDONED_LOCKING_TRACK =
      deferredRegister.register("abandoned_locking_track",
          () -> new LockingTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<BufferStopTrackBlock> ABANDONED_BUFFER_STOP_TRACK =
      deferredRegister.register("abandoned_buffer_stop_track",
          () -> new BufferStopTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<ActivatorTrackBlock> ABANDONED_ACTIVATOR_TRACK =
      deferredRegister.register("abandoned_activator_track",
          () -> new ActivatorTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<BoosterTrackBlock> ABANDONED_BOOSTER_TRACK =
      deferredRegister.register("abandoned_booster_track",
          () -> new BoosterTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ABANDONED_CONTROL_TRACK =
      deferredRegister.register("abandoned_control_track",
          () -> new ControlTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ABANDONED_GATED_TRACK =
      deferredRegister.register("abandoned_gated_track",
          () -> new GatedTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ABANDONED_DETECTOR_TRACK =
      deferredRegister.register("abandoned_detector_track",
          () -> new DetectorTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ABANDONED_COUPLER_TRACK =
      deferredRegister.register("abandoned_coupler_track",
          () -> new CouplerTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ABANDONED_EMBARKING_TRACK =
      deferredRegister.register("abandoned_embarking_track",
          () -> new EmbarkingTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ABANDONED_DISEMBARKING_TRACK =
      deferredRegister.register("abandoned_disembarking_track",
          () -> new DisembarkingTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<WyeTrackBlock> ABANDONED_WYE_TRACK =
      deferredRegister.register("abandoned_wye_track",
          () -> new WyeTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TurnoutTrackBlock> ABANDONED_TURNOUT_TRACK =
      deferredRegister.register("abandoned_turnout_track",
          () -> new TurnoutTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<JunctionTrackBlock> ABANDONED_JUNCTION_TRACK =
      deferredRegister.register("abandoned_junction_track",
          () -> new JunctionTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ABANDONED_LAUNCHER_TRACK =
      deferredRegister.register("abandoned_launcher_track",
          () -> new LauncherTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ABANDONED_ONE_WAY_TRACK =
      deferredRegister.register("abandoned_one_way_track",
          () -> new OneWayTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ABANDONED_LOCOMOTIVE_TRACK =
      deferredRegister.register("abandoned_locomotive_track",
          () -> new LocomotiveTrackBlock(TrackTypes.ABANDONED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ELECTRIC_TRACK =
      deferredRegister.register("electric_track",
          () -> new TrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .randomTicks()
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<LockingTrackBlock> ELECTRIC_LOCKING_TRACK =
      deferredRegister.register("electric_locking_track",
          () -> new LockingTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .randomTicks()
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<BufferStopTrackBlock> ELECTRIC_BUFFER_STOP_TRACK =
      deferredRegister.register("electric_buffer_stop_track",
          () -> new BufferStopTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .randomTicks()
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<ActivatorTrackBlock> ELECTRIC_ACTIVATOR_TRACK =
      deferredRegister.register("electric_activator_track",
          () -> new ActivatorTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .randomTicks()
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<BoosterTrackBlock> ELECTRIC_BOOSTER_TRACK =
      deferredRegister.register("electric_booster_track",
          () -> new BoosterTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .randomTicks()
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ELECTRIC_CONTROL_TRACK =
      deferredRegister.register("electric_control_track",
          () -> new ControlTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .randomTicks()
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, 3.5F)));

  public static final RegistryObject<TrackBlock> ELECTRIC_GATED_TRACK =
      deferredRegister.register("electric_gated_track",
          () -> new GatedTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .randomTicks()
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ELECTRIC_DETECTOR_TRACK =
      deferredRegister.register("electric_detector_track",
          () -> new DetectorTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .randomTicks()
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ELECTRIC_COUPLER_TRACK =
      deferredRegister.register("electric_coupler_track",
          () -> new CouplerTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ELECTRIC_EMBARKING_TRACK =
      deferredRegister.register("electric_embarking_track",
          () -> new EmbarkingTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ELECTRIC_DISEMBARKING_TRACK =
      deferredRegister.register("electric_disembarking_track",
          () -> new DisembarkingTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<WyeTrackBlock> ELECTRIC_WYE_TRACK =
      deferredRegister.register("electric_wye_track",
          () -> new WyeTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TurnoutTrackBlock> ELECTRIC_TURNOUT_TRACK =
      deferredRegister.register("electric_turnout_track",
          () -> new TurnoutTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<JunctionTrackBlock> ELECTRIC_JUNCTION_TRACK =
      deferredRegister.register("electric_junction_track",
          () -> new JunctionTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ELECTRIC_LAUNCHER_TRACK =
      deferredRegister.register("electric_launcher_track",
          () -> new LauncherTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ELECTRIC_ONE_WAY_TRACK =
      deferredRegister.register("electric_one_way_track",
          () -> new OneWayTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> ELECTRIC_LOCOMOTIVE_TRACK =
      deferredRegister.register("electric_locomotive_track",
          () -> new LocomotiveTrackBlock(TrackTypes.ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_TRACK =
      deferredRegister.register("high_speed_track",
          () -> new TrackBlock(TrackTypes.HIGH_SPEED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_TRANSITION_TRACK =
      deferredRegister.register("high_speed_transition_track",
          () -> new TransitionTrackBlock(TrackTypes.HIGH_SPEED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<LockingTrackBlock> HIGH_SPEED_LOCKING_TRACK =
      deferredRegister.register("high_speed_locking_track",
          () -> new LockingTrackBlock(TrackTypes.HIGH_SPEED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<ActivatorTrackBlock> HIGH_SPEED_ACTIVATOR_TRACK =
      deferredRegister.register("high_speed_activator_track",
          () -> new ActivatorTrackBlock(TrackTypes.HIGH_SPEED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<BoosterTrackBlock> HIGH_SPEED_BOOSTER_TRACK =
      deferredRegister.register("high_speed_booster_track",
          () -> new BoosterTrackBlock(TrackTypes.HIGH_SPEED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_DETECTOR_TRACK =
      deferredRegister.register("high_speed_detector_track",
          () -> new DetectorTrackBlock(TrackTypes.HIGH_SPEED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<WyeTrackBlock> HIGH_SPEED_WYE_TRACK =
      deferredRegister.register("high_speed_wye_track",
          () -> new WyeTrackBlock(TrackTypes.HIGH_SPEED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TurnoutTrackBlock> HIGH_SPEED_TURNOUT_TRACK =
      deferredRegister.register("high_speed_turnout_track",
          () -> new TurnoutTrackBlock(TrackTypes.HIGH_SPEED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<JunctionTrackBlock> HIGH_SPEED_JUNCTION_TRACK =
      deferredRegister.register("high_speed_junction_track",
          () -> new JunctionTrackBlock(TrackTypes.HIGH_SPEED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_LOCOMOTIVE_TRACK =
      deferredRegister.register("high_speed_locomotive_track",
          () -> new LocomotiveTrackBlock(TrackTypes.HIGH_SPEED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_ELECTRIC_TRACK =
      deferredRegister.register("high_speed_electric_track",
          () -> new TrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_ELECTRIC_TRANSITION_TRACK =
      deferredRegister.register("high_speed_electric_transition_track",
          () -> new TransitionTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<LockingTrackBlock> HIGH_SPEED_ELECTRIC_LOCKING_TRACK =
      deferredRegister.register("high_speed_electric_locking_track",
          () -> new LockingTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<ActivatorTrackBlock> HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK =
      deferredRegister.register("high_speed_electric_activator_track",
          () -> new ActivatorTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<BoosterTrackBlock> HIGH_SPEED_ELECTRIC_BOOSTER_TRACK =
      deferredRegister.register("high_speed_electric_booster_track",
          () -> new BoosterTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_ELECTRIC_DETECTOR_TRACK =
      deferredRegister.register("high_speed_electric_detector_track",
          () -> new DetectorTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<WyeTrackBlock> HIGH_SPEED_ELECTRIC_WYE_TRACK =
      deferredRegister.register("high_speed_electric_wye_track",
          () -> new WyeTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TurnoutTrackBlock> HIGH_SPEED_ELECTRIC_TURNOUT_TRACK =
      deferredRegister.register("high_speed_electric_turnout_track",
          () -> new TurnoutTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<JunctionTrackBlock> HIGH_SPEED_ELECTRIC_JUNCTION_TRACK =
      deferredRegister.register("high_speed_electric_junction_track",
          () -> new JunctionTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> HIGH_SPEED_ELECTRIC_LOCOMOTIVE_TRACK =
      deferredRegister.register("high_speed_electric_locomotive_track",
          () -> new LocomotiveTrackBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<LockingTrackBlock> IRON_LOCKING_TRACK =
      deferredRegister.register("iron_locking_track",
          () -> new LockingTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<BufferStopTrackBlock> IRON_BUFFER_STOP_TRACK =
      deferredRegister.register("iron_buffer_stop_track",
          () -> new BufferStopTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<ActivatorTrackBlock> IRON_ACTIVATOR_TRACK =
      deferredRegister.register("iron_activator_track",
          () -> new ActivatorTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<BoosterTrackBlock> IRON_BOOSTER_TRACK =
      deferredRegister.register("iron_booster_track",
          () -> new BoosterTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> IRON_CONTROL_TRACK =
      deferredRegister.register("iron_control_track",
          () -> new ControlTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> IRON_GATED_TRACK =
      deferredRegister.register("iron_gated_track",
          () -> new GatedTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> IRON_DETECTOR_TRACK =
      deferredRegister.register("iron_detector_track",
          () -> new DetectorTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .randomTicks()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> IRON_COUPLER_TRACK =
      deferredRegister.register("iron_coupler_track",
          () -> new CouplerTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> IRON_EMBARKING_TRACK =
      deferredRegister.register("iron_embarking_track",
          () -> new EmbarkingTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> IRON_DISEMBARKING_TRACK =
      deferredRegister.register("iron_disembarking_track",
          () -> new DisembarkingTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<WyeTrackBlock> IRON_WYE_TRACK =
      deferredRegister.register("iron_wye_track",
          () -> new WyeTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TurnoutTrackBlock> IRON_TURNOUT_TRACK =
      deferredRegister.register("iron_turnout_track",
          () -> new TurnoutTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<JunctionTrackBlock> IRON_JUNCTION_TRACK =
      deferredRegister.register("iron_junction_track",
          () -> new JunctionTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> IRON_LAUNCHER_TRACK =
      deferredRegister.register("iron_launcher_track",
          () -> new LauncherTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> IRON_ONE_WAY_TRACK =
      deferredRegister.register("iron_one_way_track",
          () -> new OneWayTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> IRON_LOCOMOTIVE_TRACK =
      deferredRegister.register("iron_locomotive_track",
          () -> new LocomotiveTrackBlock(TrackTypes.IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> REINFORCED_TRACK =
      deferredRegister.register("reinforced_track",
          () -> new TrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<LockingTrackBlock> REINFORCED_LOCKING_TRACK =
      deferredRegister.register("reinforced_locking_track",
          () -> new LockingTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<BufferStopTrackBlock> REINFORCED_BUFFER_STOP_TRACK =
      deferredRegister.register("reinforced_buffer_stop_track",
          () -> new BufferStopTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<ActivatorTrackBlock> REINFORCED_ACTIVATOR_TRACK =
      deferredRegister.register("reinforced_activator_track",
          () -> new ActivatorTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<BoosterTrackBlock> REINFORCED_BOOSTER_TRACK =
      deferredRegister.register("reinforced_booster_track",
          () -> new BoosterTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> REINFORCED_CONTROL_TRACK =
      deferredRegister.register("reinforced_control_track",
          () -> new ControlTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> REINFORCED_GATED_TRACK =
      deferredRegister.register("reinforced_gated_track",
          () -> new GatedTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> REINFORCED_DETECTOR_TRACK =
      deferredRegister.register("reinforced_detector_track",
          () -> new DetectorTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> REINFORCED_COUPLER_TRACK =
      deferredRegister.register("reinforced_coupler_track",
          () -> new CouplerTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> REINFORCED_EMBARKING_TRACK =
      deferredRegister.register("reinforced_embarking_track",
          () -> new EmbarkingTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> REINFORCED_DISEMBARKING_TRACK =
      deferredRegister.register("reinforced_disembarking_track",
          () -> new DisembarkingTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<WyeTrackBlock> REINFORCED_WYE_TRACK =
      deferredRegister.register("reinforced_wye_track",
          () -> new WyeTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TurnoutTrackBlock> REINFORCED_TURNOUT_TRACK =
      deferredRegister.register("reinforced_turnout_track",
          () -> new TurnoutTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<JunctionTrackBlock> REINFORCED_JUNCTION_TRACK =
      deferredRegister.register("reinforced_junction_track",
          () -> new JunctionTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> REINFORCED_LAUNCHER_TRACK =
      deferredRegister.register("reinforced_launcher_track",
          () -> new LauncherTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> REINFORCED_ONE_WAY_TRACK =
      deferredRegister.register("reinforced_one_way_track",
          () -> new OneWayTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> REINFORCED_LOCOMOTIVE_TRACK =
      deferredRegister.register("reinforced_locomotive_track",
          () -> new LocomotiveTrackBlock(TrackTypes.REINFORCED,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.REINFORCED_RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_TRACK =
      deferredRegister.register("strap_iron_track",
          () -> new TrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<LockingTrackBlock> STRAP_IRON_LOCKING_TRACK =
      deferredRegister.register("strap_iron_locking_track",
          () -> new LockingTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<BufferStopTrackBlock> STRAP_IRON_BUFFER_STOP_TRACK =
      deferredRegister.register("strap_iron_buffer_stop_track",
          () -> new BufferStopTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<ActivatorTrackBlock> STRAP_IRON_ACTIVATOR_TRACK =
      deferredRegister.register("strap_iron_activator_track",
          () -> new ActivatorTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<BoosterTrackBlock> STRAP_IRON_BOOSTER_TRACK =
      deferredRegister.register("strap_iron_booster_track",
          () -> new BoosterTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_CONTROL_TRACK =
      deferredRegister.register("strap_iron_control_track",
          () -> new ControlTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_GATED_TRACK =
      deferredRegister.register("strap_iron_gated_track",
          () -> new GatedTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_DETECTOR_TRACK =
      deferredRegister.register("strap_iron_detector_track",
          () -> new DetectorTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_COUPLER_TRACK =
      deferredRegister.register("strap_iron_coupler_track",
          () -> new CouplerTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_EMBARKING_TRACK =
      deferredRegister.register("strap_iron_embarking_track",
          () -> new EmbarkingTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_DISEMBARKING_TRACK =
      deferredRegister.register("strap_iron_disembarking_track",
          () -> new DisembarkingTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<WyeTrackBlock> STRAP_IRON_WYE_TRACK =
      deferredRegister.register("strap_iron_wye_track",
          () -> new WyeTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TurnoutTrackBlock> STRAP_IRON_TURNOUT_TRACK =
      deferredRegister.register("strap_iron_turnout_track",
          () -> new TurnoutTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<JunctionTrackBlock> STRAP_IRON_JUNCTION_TRACK =
      deferredRegister.register("strap_iron_junction_track",
          () -> new JunctionTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_LAUNCHER_TRACK =
      deferredRegister.register("strap_iron_launcher_track",
          () -> new LauncherTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_ONE_WAY_TRACK =
      deferredRegister.register("strap_iron_one_way_track",
          () -> new OneWayTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<TrackBlock> STRAP_IRON_LOCOMOTIVE_TRACK =
      deferredRegister.register("strap_iron_locomotive_track",
          () -> new LocomotiveTrackBlock(TrackTypes.STRAP_IRON,
              BlockBehaviour.Properties.of(Material.DECORATION)
                  .noCollission()
                  .strength(TrackConstants.HARDNESS, TrackConstants.RESISTANCE)
                  .sound(SoundType.METAL)));

  public static final RegistryObject<ElevatorTrackBlock> ELEVATOR_TRACK =
      deferredRegister.register("elevator_track",
          () -> new ElevatorTrackBlock(BlockBehaviour.Properties.of(RailcraftMaterials.ELEVATOR)
              .noCollission()
              .strength(1.05F)
              .sound(SoundType.METAL)));

  // firestone (ORE)
  public static final RegistryObject<Block> FIRESTONE =
      deferredRegister.register("firestone",
          () -> new FirestoneBlock(BlockBehaviour.Properties.of(Material.STONE)
              .lightLevel(__ -> 15)
              .strength(3, 5)));

  public static final RegistryObject<Block> RITUAL =
      deferredRegister.register("ritual",
          () -> new RitualBlock(BlockBehaviour.Properties.of(Material.STONE)
              .lightLevel(__ -> 1)
              .noOcclusion()));

  public static final RegistryObject<Block> MANUAL_ROLLING_MACHINE =
      deferredRegister.register("manual_rolling_machine",
          () -> new ManualRollingMachineBlock(BlockBehaviour.Properties.of(Material.WOOD)
              .sound(SoundType.WOOD)
              .strength(2.0F)));

  public static final RegistryObject<Block> CRUSHER =
      deferredRegister.register("crusher",
          () -> new CrusherMultiblockBlock(BlockBehaviour.Properties.of(Material.STONE)
              .sound(SoundType.STONE)));

  public static final RegistryObject<CokeOvenBricksBlock> COKE_OVEN_BRICKS =
      deferredRegister.register("coke_oven_bricks",
          () -> new CokeOvenBricksBlock(BlockBehaviour.Properties.of(Material.STONE)
              .sound(SoundType.STONE)
              .lightLevel(litBlockEmission(13))
              .strength(2F, 6.0F)));

  public static final RegistryObject<LiquidBlock> CREOSOTE =
      deferredRegister.register("creosote",
          () -> new LiquidBlock(RailcraftFluids.CREOSOTE,
              BlockBehaviour.Properties.of(Material.WATER)
                  .noCollission().strength(50.0F).noLootTable()));

  public static final RegistryObject<Block> CRUSHED_OBSIDIAN =
      deferredRegister.register("crushed_obsidian",
          () -> new CrushedObsidian(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK)
              .requiresCorrectToolForDrops()
              .strength(50, 1200)));

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
