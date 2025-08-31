package mods.railcraft.world.item;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.util.VariantSet;
import mods.railcraft.world.entity.vehicle.EnergyMinecart;
import mods.railcraft.world.entity.vehicle.TankMinecart;
import mods.railcraft.world.entity.vehicle.locomotive.CreativeLocomotive;
import mods.railcraft.world.entity.vehicle.locomotive.ElectricLocomotive;
import mods.railcraft.world.entity.vehicle.locomotive.SteamLocomotive;
import mods.railcraft.world.item.charge.BatteryBlockItem;
import mods.railcraft.world.item.charge.EmptyBatteryBlockItem;
import mods.railcraft.world.item.component.AuraComponent;
import mods.railcraft.world.item.component.BatteryComponent;
import mods.railcraft.world.item.component.LocomotiveColorComponent;
import mods.railcraft.world.item.component.LocomotiveWhistlePitchComponent;
import mods.railcraft.world.item.component.RailcraftDataComponents;
import mods.railcraft.world.item.component.RoutingTableBookContent;
import mods.railcraft.world.item.component.SeasonComponent;
import mods.railcraft.world.item.manipulator.AdvancedItemLoaderBlockItem;
import mods.railcraft.world.item.manipulator.AdvancedItemUnloaderBlockItem;
import mods.railcraft.world.item.manipulator.CartDispenserBlockItem;
import mods.railcraft.world.item.manipulator.FluidLoaderBlockItem;
import mods.railcraft.world.item.manipulator.FluidUnloaderBlockItem;
import mods.railcraft.world.item.manipulator.ItemLoaderBlockItem;
import mods.railcraft.world.item.manipulator.ItemUnloaderBlockItem;
import mods.railcraft.world.item.manipulator.TrainDispenserBlockItem;
import mods.railcraft.world.item.signal.BlockSignalBlockItem;
import mods.railcraft.world.item.signal.DistantSignalBlockItem;
import mods.railcraft.world.item.signal.DualBlockSignalBlockItem;
import mods.railcraft.world.item.signal.DualDistantSignalBlockItem;
import mods.railcraft.world.item.signal.DualTokenSignalBlockItem;
import mods.railcraft.world.item.signal.SignalBlockRelayBoxBlockItem;
import mods.railcraft.world.item.signal.SignalCapacitorBoxBlockItem;
import mods.railcraft.world.item.signal.SignalControllerBoxBlockItem;
import mods.railcraft.world.item.signal.SignalInterlockBoxBlockItem;
import mods.railcraft.world.item.signal.SignalReceiverBoxBlockItem;
import mods.railcraft.world.item.signal.SignalSequencerBoxBlockItem;
import mods.railcraft.world.item.signal.TokenSignalBlockItem;
import mods.railcraft.world.item.signal.TokenSignalBoxBlockItem;
import mods.railcraft.world.item.track.AbandonedTrackBlockItem;
import mods.railcraft.world.item.track.ElectricTrackBlockItem;
import mods.railcraft.world.item.track.HighSpeedElectricTrackBlockItem;
import mods.railcraft.world.item.track.HighSpeedTrackBlockItem;
import mods.railcraft.world.item.track.ReinforcedTrackBlockItem;
import mods.railcraft.world.item.track.StrapIronTrackBlockItem;
import mods.railcraft.world.item.track.actuator.SwitchTrackLeverBlockItem;
import mods.railcraft.world.item.track.actuator.SwitchTrackMotorBlockItem;
import mods.railcraft.world.item.track.actuator.SwitchTrackRouterBlockItem;
import mods.railcraft.world.item.track.outfitted.ActivatorTrackBlockItem;
import mods.railcraft.world.item.track.outfitted.BoosterTrackBlockItem;
import mods.railcraft.world.item.track.outfitted.BufferStopTrackBlockItem;
import mods.railcraft.world.item.track.outfitted.ControlTrackBlockItem;
import mods.railcraft.world.item.track.outfitted.CouplerTrackBlockItem;
import mods.railcraft.world.item.track.outfitted.DetectorTrackBlockItem;
import mods.railcraft.world.item.track.outfitted.DisembarkingTrackBlockItem;
import mods.railcraft.world.item.track.outfitted.DumpingTrackBlockItem;
import mods.railcraft.world.item.track.outfitted.EmbarkingTrackBlockItem;
import mods.railcraft.world.item.track.outfitted.GatedTrackBlockItem;
import mods.railcraft.world.item.track.outfitted.LauncherTrackBlockItem;
import mods.railcraft.world.item.track.outfitted.LockingTrackBlockItem;
import mods.railcraft.world.item.track.outfitted.LocomotiveTrackBlockItem;
import mods.railcraft.world.item.track.outfitted.OneWayTrackBlockItem;
import mods.railcraft.world.item.track.outfitted.RoutingTrackBlockItem;
import mods.railcraft.world.item.track.outfitted.ThrottleTrackBlockItem;
import mods.railcraft.world.item.track.outfitted.TransitionTrackBlockItem;
import mods.railcraft.world.item.track.outfitted.WhistleTrackBlockItem;
import mods.railcraft.world.item.tunnelbore.TunnelBoreHeadItem;
import mods.railcraft.world.item.tunnelbore.TunnelBoreItem;
import mods.railcraft.world.level.block.DecorativeBlock;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.charge.BatterySpecs;
import mods.railcraft.world.level.block.track.TrackTypes;
import mods.railcraft.world.level.material.RailcraftFluids;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RailcraftItems {

  private static final DeferredRegister.Items deferredRegister =
      DeferredRegister.createItems(RailcraftConstants.ID);

  private static final BiFunction<Item.Properties, Block, BlockItem> BLOCK_TO_BLOCK_ITEM =
      (properties, block) -> new BlockItem(block, properties);

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }

  public static Collection<DeferredHolder<Item, ? extends Item>> entries() {
    return deferredRegister.getEntries();
  }

  public static final VariantSet<DyeColor, Item, BlockItem> STRENGTHENED_GLASS =
      VariantSet.ofMapped(
          DyeColor.class,
          deferredRegister,
          RailcraftBlocks.STRENGTHENED_GLASS,
          BLOCK_TO_BLOCK_ITEM);
  public static final VariantSet<DyeColor, Item, BlockItem> POST =
      VariantSet.ofMapped(
          DyeColor.class,
          deferredRegister,
          RailcraftBlocks.POST,
          BLOCK_TO_BLOCK_ITEM);
  public static final VariantSet<DyeColor, Item, BlockItem> IRON_TANK_GAUGE =
      VariantSet.ofMapped(
          DyeColor.class,
          deferredRegister,
          RailcraftBlocks.IRON_TANK_GAUGE,
          BLOCK_TO_BLOCK_ITEM);
  public static final VariantSet<DyeColor, Item, BlockItem> IRON_TANK_VALVE =
      VariantSet.ofMapped(
          DyeColor.class,
          deferredRegister,
          RailcraftBlocks.IRON_TANK_VALVE,
          BLOCK_TO_BLOCK_ITEM);
  public static final VariantSet<DyeColor, Item, BlockItem> IRON_TANK_WALL =
      VariantSet.ofMapped(
          DyeColor.class,
          deferredRegister,
          RailcraftBlocks.IRON_TANK_WALL,
          BLOCK_TO_BLOCK_ITEM);
  public static final VariantSet<DyeColor, Item, BlockItem> STEEL_TANK_GAUGE =
      VariantSet.ofMapped(
          DyeColor.class,
          deferredRegister,
          RailcraftBlocks.STEEL_TANK_GAUGE,
          BLOCK_TO_BLOCK_ITEM);
  public static final VariantSet<DyeColor, Item, BlockItem> STEEL_TANK_VALVE =
      VariantSet.ofMapped(
          DyeColor.class,
          deferredRegister,
          RailcraftBlocks.STEEL_TANK_VALVE,
          BLOCK_TO_BLOCK_ITEM);
  public static final VariantSet<DyeColor, Item, BlockItem> STEEL_TANK_WALL =
      VariantSet.ofMapped(
          DyeColor.class,
          deferredRegister,
          RailcraftBlocks.STEEL_TANK_WALL,
          BLOCK_TO_BLOCK_ITEM);

  public static final DeferredItem<PressureBoilerTankBlockItem> LOW_PRESSURE_STEAM_BOILER_TANK =
      customBlockItem("low_pressure_steam_boiler_tank", properties ->
          new PressureBoilerTankBlockItem(
              RailcraftBlocks.LOW_PRESSURE_STEAM_BOILER_TANK.get(), properties));

  public static final DeferredItem<PressureBoilerTankBlockItem> HIGH_PRESSURE_STEAM_BOILER_TANK =
      customBlockItem("high_pressure_steam_boiler_tank", properties ->
          new PressureBoilerTankBlockItem(
              RailcraftBlocks.HIGH_PRESSURE_STEAM_BOILER_TANK.get(), properties));

  public static final DeferredItem<FueledFireboxBlockItem> SOLID_FUELED_FIREBOX =
      customBlockItem("solid_fueled_firebox", properties ->
          new FueledFireboxBlockItem(RailcraftBlocks.SOLID_FUELED_FIREBOX.get(), properties));

  public static final DeferredItem<FueledFireboxBlockItem> FLUID_FUELED_FIREBOX =
      customBlockItem("fluid_fueled_firebox", properties ->
          new FueledFireboxBlockItem(RailcraftBlocks.FLUID_FUELED_FIREBOX.get(), properties));

  public static final DeferredItem<SignalLabelItem> SIGNAL_LABEL =
      deferredRegister.registerItem("signal_label", SignalLabelItem::new);

  public static final DeferredItem<Item> TURBINE_BLADE = registerBasic("turbine_blade");

  public static final DeferredItem<Item> TURBINE_DISK = registerBasic("turbine_disk");

  public static final DeferredItem<TurbineRotorItem> TURBINE_ROTOR =
      deferredRegister.registerItem("turbine_rotor", properties ->
          new TurbineRotorItem(properties.stacksTo(1)));

  public static final DeferredItem<SteamTurbineBlockItem> STEAM_TURBINE =
      blockItem(RailcraftBlocks.STEAM_TURBINE, SteamTurbineBlockItem::new);

  public static final DeferredItem<BlastFurnaceBricksBlockItem> BLAST_FURNACE_BRICKS =
      blockItem(RailcraftBlocks.BLAST_FURNACE_BRICKS, BlastFurnaceBricksBlockItem::new);

  public static final DeferredItem<FeedStationBlockItem> FEED_STATION =
      blockItem(RailcraftBlocks.FEED_STATION, FeedStationBlockItem::new);

  public static final DeferredItem<ChimneyBlockItem> CHIMNEY =
      blockItem(RailcraftBlocks.CHIMNEY, ChimneyBlockItem::new);

  public static final DeferredItem<LogBookBlockItem> LOGBOOK =
      blockItem(RailcraftBlocks.LOGBOOK, LogBookBlockItem::new);

  public static final DeferredItem<FrameBlockItem> FRAME_BLOCK =
      blockItem(RailcraftBlocks.FRAME, FrameBlockItem::new);

  public static final DeferredItem<ChargeMeterItem> CHARGE_METER =
      deferredRegister.registerItem("charge_meter", properties ->
          new ChargeMeterItem(properties
              .durability(0)
              .stacksTo(1)));

  public static final DeferredItem<BatteryBlockItem> NICKEL_ZINC_BATTERY =
      blockItem(RailcraftBlocks.NICKEL_ZINC_BATTERY, (block, properties) ->
          new BatteryBlockItem(block, properties
              .component(RailcraftDataComponents.BATTERY, BatteryComponent.from(BatterySpecs.NICKEL_ZINC))));

  public static final DeferredItem<BatteryBlockItem> NICKEL_IRON_BATTERY =
      blockItem(RailcraftBlocks.NICKEL_IRON_BATTERY, (block, properties) ->
          new BatteryBlockItem(block, properties
              .component(RailcraftDataComponents.BATTERY, BatteryComponent.from(BatterySpecs.NICKEL_IRON))));

  public static final DeferredItem<BatteryBlockItem> ZINC_CARBON_BATTERY =
      blockItem(RailcraftBlocks.ZINC_CARBON_BATTERY, (block, properties) ->
          new BatteryBlockItem(block, properties
              .component(RailcraftDataComponents.BATTERY, BatteryComponent.from(BatterySpecs.ZINC_CARBON))));

  public static final DeferredItem<EmptyBatteryBlockItem> ZINC_CARBON_BATTERY_EMPTY =
      blockItem(RailcraftBlocks.ZINC_CARBON_BATTERY_EMPTY, EmptyBatteryBlockItem::new);

  public static final DeferredItem<BatteryBlockItem> ZINC_SILVER_BATTERY =
      blockItem(RailcraftBlocks.ZINC_SILVER_BATTERY, (block, properties) ->
          new BatteryBlockItem(block, properties
              .component(RailcraftDataComponents.BATTERY, BatteryComponent.from(BatterySpecs.ZINC_SILVER))));

  public static final DeferredItem<EmptyBatteryBlockItem> ZINC_SILVER_BATTERY_EMPTY =
      blockItem(RailcraftBlocks.ZINC_SILVER_BATTERY_EMPTY, EmptyBatteryBlockItem::new);

  public static final DeferredItem<BlockItem> STEEL_ANVIL =
      blockItem(RailcraftBlocks.STEEL_ANVIL);

  public static final DeferredItem<BlockItem> CHIPPED_STEEL_ANVIL =
      blockItem(RailcraftBlocks.CHIPPED_STEEL_ANVIL);

  public static final DeferredItem<BlockItem> DAMAGED_STEEL_ANVIL =
      blockItem(RailcraftBlocks.DAMAGED_STEEL_ANVIL);

  public static final DeferredItem<BlockItem> STEEL_BLOCK =
      blockItem(RailcraftBlocks.STEEL_BLOCK);

  public static final DeferredItem<BlockItem> BRASS_BLOCK =
      blockItem(RailcraftBlocks.BRASS_BLOCK);

  public static final DeferredItem<BlockItem> BRONZE_BLOCK =
      blockItem(RailcraftBlocks.BRONZE_BLOCK);

  public static final DeferredItem<BlockItem> INVAR_BLOCK =
      blockItem(RailcraftBlocks.INVAR_BLOCK);

  public static final DeferredItem<BlockItem> LEAD_BLOCK =
      blockItem(RailcraftBlocks.LEAD_BLOCK);

  public static final DeferredItem<BlockItem> NICKEL_BLOCK =
      blockItem(RailcraftBlocks.NICKEL_BLOCK);

  public static final DeferredItem<BlockItem> SILVER_BLOCK =
      blockItem(RailcraftBlocks.SILVER_BLOCK);

  public static final DeferredItem<BlockItem> TIN_BLOCK =
      blockItem(RailcraftBlocks.TIN_BLOCK);

  public static final DeferredItem<BlockItem> ZINC_BLOCK =
      blockItem(RailcraftBlocks.ZINC_BLOCK);

  public static final DeferredItem<BlockItem> LEAD_ORE =
      blockItem(RailcraftBlocks.LEAD_ORE);

  public static final DeferredItem<BlockItem> NICKEL_ORE =
      blockItem(RailcraftBlocks.NICKEL_ORE);

  public static final DeferredItem<BlockItem> SILVER_ORE =
      blockItem(RailcraftBlocks.SILVER_ORE);

  public static final DeferredItem<BlockItem> TIN_ORE =
      blockItem(RailcraftBlocks.TIN_ORE);

  public static final DeferredItem<BlockItem> ZINC_ORE =
      blockItem(RailcraftBlocks.ZINC_ORE);

  public static final DeferredItem<BlockItem> DEEPSLATE_LEAD_ORE =
      blockItem(RailcraftBlocks.DEEPSLATE_LEAD_ORE);

  public static final DeferredItem<BlockItem> DEEPSLATE_NICKEL_ORE =
      blockItem(RailcraftBlocks.DEEPSLATE_NICKEL_ORE);

  public static final DeferredItem<BlockItem> DEEPSLATE_SILVER_ORE =
      blockItem(RailcraftBlocks.DEEPSLATE_SILVER_ORE);

  public static final DeferredItem<BlockItem> DEEPSLATE_TIN_ORE =
      blockItem(RailcraftBlocks.DEEPSLATE_TIN_ORE);

  public static final DeferredItem<BlockItem> DEEPSLATE_ZINC_ORE =
      blockItem(RailcraftBlocks.DEEPSLATE_ZINC_ORE);

  public static final DeferredItem<BlockItem> SULFUR_ORE =
      blockItem(RailcraftBlocks.SULFUR_ORE);

  public static final DeferredItem<BlockItem> DEEPSLATE_SULFUR_ORE =
      blockItem(RailcraftBlocks.DEEPSLATE_SULFUR_ORE);

  public static final DeferredItem<BlockItem> SALTPETER_ORE =
      blockItem(RailcraftBlocks.SALTPETER_ORE);

  public static final DeferredItem<CoalCokeBlockItem> COAL_COKE_BLOCK =
      blockItem(RailcraftBlocks.COAL_COKE_BLOCK, CoalCokeBlockItem::new);

  public static final DeferredItem<ShearsItem> STEEL_SHEARS =
      deferredRegister.registerItem("steel_shears", properties ->
          new ShearsItem(properties.durability(500)));

  public static final DeferredItem<Item> STEEL_SWORD =
      deferredRegister.registerItem("steel_sword", properties ->
          new Item(properties.sword(RailcraftToolMaterial.STEEL, 3, -2.4F)));

  public static final DeferredItem<ShovelItem> STEEL_SHOVEL =
      deferredRegister.registerItem("steel_shovel", properties ->
          new ShovelItem(RailcraftToolMaterial.STEEL, 1.5F, -3F, properties));

  public static final DeferredItem<Item> STEEL_PICKAXE =
      deferredRegister.registerItem("steel_pickaxe", properties ->
          new Item(properties.pickaxe(RailcraftToolMaterial.STEEL, 1, -2.8F)));

  public static final DeferredItem<AxeItem> STEEL_AXE =
      deferredRegister.registerItem("steel_axe", properties ->
          new AxeItem(RailcraftToolMaterial.STEEL, 8F, -3F, properties));

  public static final DeferredItem<HoeItem> STEEL_HOE =
      deferredRegister.registerItem("steel_hoe", properties ->
          new HoeItem(RailcraftToolMaterial.STEEL, -2, -0.5F, properties));

  public static final DeferredItem<Item> STEEL_BOOTS =
      deferredRegister.registerItem("steel_boots", properties ->
          new Item(properties.humanoidArmor(RailcraftArmorMaterials.STEEL, ArmorType.BOOTS)));

  public static final DeferredItem<Item> STEEL_CHESTPLATE =
      deferredRegister.registerItem("steel_chestplate", properties ->
          new Item(properties.humanoidArmor(RailcraftArmorMaterials.STEEL, ArmorType.CHESTPLATE)));

  public static final DeferredItem<Item> STEEL_HELMET =
      deferredRegister.registerItem("steel_helmet", properties ->
          new Item(properties.humanoidArmor(RailcraftArmorMaterials.STEEL, ArmorType.HELMET)));

  public static final DeferredItem<Item> STEEL_LEGGINGS =
      deferredRegister.registerItem("steel_leggings", properties ->
          new Item(properties.humanoidArmor(RailcraftArmorMaterials.STEEL, ArmorType.LEGGINGS)));

  public static final DeferredItem<TunnelBoreHeadItem> IRON_TUNNEL_BORE_HEAD =
      deferredRegister.registerItem("iron_tunnel_bore_head", properties ->
          new TunnelBoreHeadItem(ToolMaterial.IRON, "iron", properties.durability(1500)));

  public static final DeferredItem<TunnelBoreHeadItem> BRONZE_TUNNEL_BORE_HEAD =
      deferredRegister.registerItem("bronze_tunnel_bore_head", properties ->
          new TunnelBoreHeadItem(RailcraftToolMaterial.BRONZE, "bronze", properties.durability(1200)));

  public static final DeferredItem<TunnelBoreHeadItem> STEEL_TUNNEL_BORE_HEAD =
      deferredRegister.registerItem("steel_tunnel_bore_head", properties ->
          new TunnelBoreHeadItem(RailcraftToolMaterial.STEEL, "steel", properties.durability(3000)));

  public static final DeferredItem<TunnelBoreHeadItem> DIAMOND_TUNNEL_BORE_HEAD =
      deferredRegister.registerItem("diamond_tunnel_bore_head", properties ->
          new TunnelBoreHeadItem(ToolMaterial.DIAMOND, "diamond", properties.durability(6000)));

  public static final DeferredItem<FluidLoaderBlockItem> FLUID_LOADER =
      blockItem(RailcraftBlocks.FLUID_LOADER, FluidLoaderBlockItem::new);

  public static final DeferredItem<FluidUnloaderBlockItem> FLUID_UNLOADER =
      blockItem(RailcraftBlocks.FLUID_UNLOADER, FluidUnloaderBlockItem::new);

  public static final DeferredItem<AdvancedItemLoaderBlockItem> ADVANCED_ITEM_LOADER =
      blockItem(RailcraftBlocks.ADVANCED_ITEM_LOADER, AdvancedItemLoaderBlockItem::new);

  public static final DeferredItem<AdvancedItemUnloaderBlockItem> ADVANCED_ITEM_UNLOADER =
      blockItem(RailcraftBlocks.ADVANCED_ITEM_UNLOADER, AdvancedItemUnloaderBlockItem::new);

  public static final DeferredItem<ItemLoaderBlockItem> ITEM_LOADER =
      blockItem(RailcraftBlocks.ITEM_LOADER, ItemLoaderBlockItem::new);

  public static final DeferredItem<ItemUnloaderBlockItem> ITEM_UNLOADER =
      blockItem(RailcraftBlocks.ITEM_UNLOADER, ItemUnloaderBlockItem::new);

  public static final DeferredItem<CartDispenserBlockItem> CART_DISPENSER =
      blockItem(RailcraftBlocks.CART_DISPENSER, CartDispenserBlockItem::new);

  public static final DeferredItem<TrainDispenserBlockItem> TRAIN_DISPENSER =
      blockItem(RailcraftBlocks.TRAIN_DISPENSER, TrainDispenserBlockItem::new);

  public static final DeferredItem<BlockItem> ADVANCED_DETECTOR =
      blockItem(RailcraftBlocks.ADVANCED_DETECTOR);

  public static final DeferredItem<BlockItem> AGE_DETECTOR =
      blockItem(RailcraftBlocks.AGE_DETECTOR);

  public static final DeferredItem<BlockItem> ANIMAL_DETECTOR =
      blockItem(RailcraftBlocks.ANIMAL_DETECTOR);

  public static final DeferredItem<BlockItem> ANY_DETECTOR =
      blockItem(RailcraftBlocks.ANY_DETECTOR);

  public static final DeferredItem<BlockItem> EMPTY_DETECTOR =
      blockItem(RailcraftBlocks.EMPTY_DETECTOR);

  public static final DeferredItem<BlockItem> ITEM_DETECTOR =
      blockItem(RailcraftBlocks.ITEM_DETECTOR);

  public static final DeferredItem<BlockItem> LOCOMOTIVE_DETECTOR =
      blockItem(RailcraftBlocks.LOCOMOTIVE_DETECTOR);

  public static final DeferredItem<BlockItem> MOB_DETECTOR =
      blockItem(RailcraftBlocks.MOB_DETECTOR);

  public static final DeferredItem<BlockItem> PLAYER_DETECTOR =
      blockItem(RailcraftBlocks.PLAYER_DETECTOR);

  public static final DeferredItem<BlockItem> ROUTING_DETECTOR =
      blockItem(RailcraftBlocks.ROUTING_DETECTOR);

  public static final DeferredItem<BlockItem> SHEEP_DETECTOR =
      blockItem(RailcraftBlocks.SHEEP_DETECTOR);

  public static final DeferredItem<BlockItem> TANK_DETECTOR =
      blockItem(RailcraftBlocks.TANK_DETECTOR);

  public static final DeferredItem<BlockItem> TRAIN_DETECTOR =
      blockItem(RailcraftBlocks.TRAIN_DETECTOR);

  public static final DeferredItem<BlockItem> VILLAGER_DETECTOR =
      blockItem(RailcraftBlocks.VILLAGER_DETECTOR);

  public static final DeferredItem<SpikeMaulItem> IRON_SPIKE_MAUL =
      deferredRegister.registerItem("iron_spike_maul", properties ->
          new SpikeMaulItem(11.0F, -3.5F, ToolMaterial.IRON, properties));

  public static final DeferredItem<SpikeMaulItem> STEEL_SPIKE_MAUL =
      deferredRegister.registerItem("steel_spike_maul", properties ->
          new SpikeMaulItem(11.0F, -3.4F, RailcraftToolMaterial.STEEL, properties));

  public static final DeferredItem<SpikeMaulItem> DIAMOND_SPIKE_MAUL =
      deferredRegister.registerItem("diamond_spike_maul", properties ->
          new SpikeMaulItem(11.0F, -3.3F, ToolMaterial.DIAMOND, properties));

  public static final DeferredItem<SwitchTrackLeverBlockItem> SWITCH_TRACK_LEVER =
      blockItem(RailcraftBlocks.SWITCH_TRACK_LEVER, SwitchTrackLeverBlockItem::new);

  public static final DeferredItem<SwitchTrackMotorBlockItem> SWITCH_TRACK_MOTOR =
      blockItem(RailcraftBlocks.SWITCH_TRACK_MOTOR, SwitchTrackMotorBlockItem::new);

  public static final DeferredItem<SwitchTrackRouterBlockItem> SWITCH_TRACK_ROUTER =
      blockItem(RailcraftBlocks.SWITCH_TRACK_ROUTER, SwitchTrackRouterBlockItem::new);

  public static final DeferredItem<SignalTunerItem> SIGNAL_TUNER =
      deferredRegister.registerItem("signal_tuner", properties ->
          new SignalTunerItem(properties.stacksTo(1)));

  public static final DeferredItem<SignalBlockSurveyorItem> SIGNAL_BLOCK_SURVEYOR =
      deferredRegister.registerItem("signal_block_surveyor", properties ->
          new SignalBlockSurveyorItem(properties.stacksTo(1)));

  public static final DeferredItem<SignalControllerBoxBlockItem> ANALOG_SIGNAL_CONTROLLER_BOX =
      blockItem(RailcraftBlocks.ANALOG_SIGNAL_CONTROLLER_BOX, SignalControllerBoxBlockItem::new);

  public static final DeferredItem<SignalSequencerBoxBlockItem> SIGNAL_SEQUENCER_BOX =
      blockItem(RailcraftBlocks.SIGNAL_SEQUENCER_BOX, SignalSequencerBoxBlockItem::new);

  public static final DeferredItem<SignalCapacitorBoxBlockItem> SIGNAL_CAPACITOR_BOX =
      blockItem(RailcraftBlocks.SIGNAL_CAPACITOR_BOX, SignalCapacitorBoxBlockItem::new);

  public static final DeferredItem<SignalInterlockBoxBlockItem> SIGNAL_INTERLOCK_BOX =
      blockItem(RailcraftBlocks.SIGNAL_INTERLOCK_BOX, SignalInterlockBoxBlockItem::new);

  public static final DeferredItem<SignalBlockRelayBoxBlockItem> SIGNAL_BLOCK_RELAY_BOX =
      blockItem(RailcraftBlocks.SIGNAL_BLOCK_RELAY_BOX, SignalBlockRelayBoxBlockItem::new);

  public static final DeferredItem<SignalReceiverBoxBlockItem> SIGNAL_RECEIVER_BOX =
      blockItem(RailcraftBlocks.SIGNAL_RECEIVER_BOX, SignalReceiverBoxBlockItem::new);

  public static final DeferredItem<SignalControllerBoxBlockItem> SIGNAL_CONTROLLER_BOX =
      blockItem(RailcraftBlocks.SIGNAL_CONTROLLER_BOX, SignalControllerBoxBlockItem::new);

  public static final DeferredItem<TokenSignalBoxBlockItem> TOKEN_SIGNAL_BOX =
      blockItem(RailcraftBlocks.TOKEN_SIGNAL_BOX, TokenSignalBoxBlockItem::new);

  public static final DeferredItem<DualBlockSignalBlockItem> DUAL_BLOCK_SIGNAL =
      blockItem(RailcraftBlocks.DUAL_BLOCK_SIGNAL, DualBlockSignalBlockItem::new);

  public static final DeferredItem<DualDistantSignalBlockItem> DUAL_DISTANT_SIGNAL =
      blockItem(RailcraftBlocks.DUAL_DISTANT_SIGNAL, DualDistantSignalBlockItem::new);

  public static final DeferredItem<DualTokenSignalBlockItem> DUAL_TOKEN_SIGNAL =
      blockItem(RailcraftBlocks.DUAL_TOKEN_SIGNAL, DualTokenSignalBlockItem::new);

  public static final DeferredItem<BlockSignalBlockItem> BLOCK_SIGNAL =
      blockItem(RailcraftBlocks.BLOCK_SIGNAL, BlockSignalBlockItem::new);

  public static final DeferredItem<DistantSignalBlockItem> DISTANT_SIGNAL =
      blockItem(RailcraftBlocks.DISTANT_SIGNAL, DistantSignalBlockItem::new);

  public static final DeferredItem<TokenSignalBlockItem> TOKEN_SIGNAL =
      blockItem(RailcraftBlocks.TOKEN_SIGNAL, TokenSignalBlockItem::new);

  public static final DeferredItem<TrackRemoverCartItem> TRACK_REMOVER =
      deferredRegister.registerItem("track_remover", properties ->
          new TrackRemoverCartItem(properties
              .rarity(Rarity.UNCOMMON)
              .stacksTo(1)));

  public static final DeferredItem<TrackLayerCartItem> TRACK_LAYER =
      deferredRegister.registerItem("track_layer", properties ->
          new TrackLayerCartItem(properties
              .rarity(Rarity.UNCOMMON)
              .stacksTo(1)));

  public static final DeferredItem<TrackRelayerCartItem> TRACK_RELAYER =
      deferredRegister.registerItem("track_relayer", properties ->
          new TrackRelayerCartItem(properties
              .rarity(Rarity.UNCOMMON)
              .stacksTo(1)));

  public static final DeferredItem<TrackUndercutterCartItem> TRACK_UNDERCUTTER =
      deferredRegister.registerItem("track_undercutter", properties ->
          new TrackUndercutterCartItem(properties
              .rarity(Rarity.UNCOMMON)
              .stacksTo(1)));

  public static final DeferredItem<CartItem> TANK_MINECART =
      deferredRegister.registerItem("tank_minecart",
          properties -> new CartItem(TankMinecart::new, properties.stacksTo(1)));

  public static final DeferredItem<CartItem> ENERGY_MINECART =
      deferredRegister.registerItem("energy_minecart",
          properties -> new CartItem(EnergyMinecart::new, properties.stacksTo(1)));

  public static final DeferredItem<WorldSpikeMinecartItem> WORLD_SPIKE_MINECART =
      deferredRegister.registerItem("world_spike_minecart",
          properties -> new WorldSpikeMinecartItem(properties
              .stacksTo(1)
              .rarity(Rarity.UNCOMMON)));

  public static final DeferredItem<TunnelBoreItem> TUNNEL_BORE =
      deferredRegister.registerItem("tunnel_bore", properties ->
          new TunnelBoreItem(properties
              .rarity(Rarity.UNCOMMON)
              .stacksTo(1)));

  public static final DeferredItem<LocomotiveItem> CREATIVE_LOCOMOTIVE =
      deferredRegister.registerItem("creative_locomotive", properties ->
          new LocomotiveItem(CreativeLocomotive::new,
              properties.stacksTo(1)
                  .component(RailcraftDataComponents.LOCOMOTIVE_COLOR,
                      new LocomotiveColorComponent(DyeColor.BLACK, DyeColor.MAGENTA))
                  .component(RailcraftDataComponents.LOCOMOTIVE_WHISTLE_PITCH,
                      LocomotiveWhistlePitchComponent.NO_WHISTLE)));

  public static final DeferredItem<LocomotiveItem> ELECTRIC_LOCOMOTIVE =
      deferredRegister.registerItem("electric_locomotive", properties ->
          new LocomotiveItem(ElectricLocomotive::new,
              properties.stacksTo(1)
                  .component(RailcraftDataComponents.LOCOMOTIVE_COLOR,
                      new LocomotiveColorComponent(DyeColor.YELLOW, DyeColor.BLACK))
                  .component(RailcraftDataComponents.LOCOMOTIVE_WHISTLE_PITCH,
                      LocomotiveWhistlePitchComponent.NO_WHISTLE)));

  public static final DeferredItem<LocomotiveItem> STEAM_LOCOMOTIVE =
      deferredRegister.registerItem("steam_locomotive", properties ->
          new LocomotiveItem(SteamLocomotive::new,
              properties.stacksTo(1)
                  .component(RailcraftDataComponents.LOCOMOTIVE_COLOR,
                      new LocomotiveColorComponent(DyeColor.LIGHT_GRAY, DyeColor.GRAY))
                  .component(RailcraftDataComponents.LOCOMOTIVE_WHISTLE_PITCH,
                      LocomotiveWhistlePitchComponent.NO_WHISTLE)));

  public static final DeferredItem<Item> WHISTLE_TUNER =
      deferredRegister.registerSimpleItem("whistle_tuner", new Item.Properties().durability(250));

  public static final DeferredItem<GoldenTicketItem> GOLDEN_TICKET =
      deferredRegister.registerItem("golden_ticket", properties ->
          new GoldenTicketItem(properties.rarity(Rarity.UNCOMMON)));

  public static final DeferredItem<TicketItem> TICKET =
      deferredRegister.registerItem("ticket", TicketItem::new);

  public static final DeferredItem<RoutingTableBookItem> ROUTING_TABLE_BOOK =
      deferredRegister.registerItem("routing_table_book", properties ->
          new RoutingTableBookItem(properties
              .component(RailcraftDataComponents.ROUTING_TABLE_BOOK,
                  RoutingTableBookContent.EMPTY)));

  public static final DeferredItem<OverallsItem> OVERALLS =
      deferredRegister.registerItem("overalls", properties ->
          new OverallsItem(properties
              .humanoidArmor(RailcraftArmorMaterials.OVERALLS, ArmorType.LEGGINGS)));

  public static final DeferredItem<FirestoneOreBlockItem> FIRESTONE_ORE =
      customBlockItem("firestone_ore", FirestoneOreBlockItem::new);

  public static final DeferredItem<FirestoneItem> RAW_FIRESTONE =
      deferredRegister.registerItem("raw_firestone", properties ->
          new FirestoneItem(true, properties.rarity(Rarity.RARE)));

  public static final DeferredItem<RefinedFirestoneItem> REFINED_FIRESTONE =
      deferredRegister.registerItem("refined_firestone", properties ->
          new RefinedFirestoneItem(false, properties
              .stacksTo(1)
              .durability(RefinedFirestoneItem.CHARGES)
              .rarity(Rarity.RARE)));

  public static final DeferredItem<CrackedFirestoneItem> CRACKED_FIRESTONE =
      deferredRegister.registerItem("cracked_firestone", properties ->
          new CrackedFirestoneItem(properties
              .stacksTo(1)
              .durability(RefinedFirestoneItem.CHARGES)
              .rarity(Rarity.RARE)));

  public static final DeferredItem<FirestoneItem> CUT_FIRESTONE =
      deferredRegister.registerItem("cut_firestone", properties ->
          new FirestoneItem(true, properties
              .stacksTo(1)
              .durability(RefinedFirestoneItem.CHARGES)
              .rarity(Rarity.RARE)));

  public static final DeferredItem<ForceTrackEmitterBlockItem> FORCE_TRACK_EMITTER =
      blockItem(RailcraftBlocks.FORCE_TRACK_EMITTER, ForceTrackEmitterBlockItem::new);

  public static final DeferredItem<AbandonedTrackBlockItem> ABANDONED_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_TRACK, AbandonedTrackBlockItem::new);

  public static final DeferredItem<LockingTrackBlockItem> ABANDONED_LOCKING_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_LOCKING_TRACK, LockingTrackBlockItem::new);

  public static final DeferredItem<BufferStopTrackBlockItem> ABANDONED_BUFFER_STOP_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_BUFFER_STOP_TRACK, BufferStopTrackBlockItem::new);

  public static final DeferredItem<ActivatorTrackBlockItem> ABANDONED_ACTIVATOR_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_ACTIVATOR_TRACK, ActivatorTrackBlockItem::new);

  public static final DeferredItem<BoosterTrackBlockItem> ABANDONED_BOOSTER_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_BOOSTER_TRACK, BoosterTrackBlockItem::new);

  public static final DeferredItem<ControlTrackBlockItem> ABANDONED_CONTROL_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_CONTROL_TRACK, ControlTrackBlockItem::new);

  public static final DeferredItem<GatedTrackBlockItem> ABANDONED_GATED_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_GATED_TRACK, GatedTrackBlockItem::new);

  public static final DeferredItem<DetectorTrackBlockItem> ABANDONED_DETECTOR_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_DETECTOR_TRACK, DetectorTrackBlockItem::new);

  public static final DeferredItem<CouplerTrackBlockItem> ABANDONED_COUPLER_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_COUPLER_TRACK, CouplerTrackBlockItem::new);

  public static final DeferredItem<EmbarkingTrackBlockItem> ABANDONED_EMBARKING_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_EMBARKING_TRACK, EmbarkingTrackBlockItem::new);

  public static final DeferredItem<DisembarkingTrackBlockItem> ABANDONED_DISEMBARKING_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_DISEMBARKING_TRACK, DisembarkingTrackBlockItem::new);

  public static final DeferredItem<DumpingTrackBlockItem> ABANDONED_DUMPING_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_DUMPING_TRACK, DumpingTrackBlockItem::new);

  public static final DeferredItem<BlockItem> ABANDONED_WYE_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_WYE_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_TURNOUT_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_TURNOUT_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_JUNCTION_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_JUNCTION_TRACK);

  public static final DeferredItem<LauncherTrackBlockItem> ABANDONED_LAUNCHER_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_LAUNCHER_TRACK, LauncherTrackBlockItem::new);

  public static final DeferredItem<OneWayTrackBlockItem> ABANDONED_ONE_WAY_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_ONE_WAY_TRACK, OneWayTrackBlockItem::new);

  public static final DeferredItem<WhistleTrackBlockItem> ABANDONED_WHISTLE_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_WHISTLE_TRACK, WhistleTrackBlockItem::new);

  public static final DeferredItem<LocomotiveTrackBlockItem> ABANDONED_LOCOMOTIVE_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_LOCOMOTIVE_TRACK, LocomotiveTrackBlockItem::new);

  public static final DeferredItem<ThrottleTrackBlockItem> ABANDONED_THROTTLE_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_THROTTLE_TRACK, ThrottleTrackBlockItem::new);

  public static final DeferredItem<RoutingTrackBlockItem> ABANDONED_ROUTING_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_ROUTING_TRACK, RoutingTrackBlockItem::new);

  public static final DeferredItem<ElectricTrackBlockItem> ELECTRIC_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_TRACK, ElectricTrackBlockItem::new);

  public static final DeferredItem<LockingTrackBlockItem> ELECTRIC_LOCKING_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_LOCKING_TRACK, LockingTrackBlockItem::new);

  public static final DeferredItem<BufferStopTrackBlockItem> ELECTRIC_BUFFER_STOP_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_BUFFER_STOP_TRACK, BufferStopTrackBlockItem::new);

  public static final DeferredItem<ActivatorTrackBlockItem> ELECTRIC_ACTIVATOR_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_ACTIVATOR_TRACK, ActivatorTrackBlockItem::new);

  public static final DeferredItem<BoosterTrackBlockItem> ELECTRIC_BOOSTER_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_BOOSTER_TRACK, BoosterTrackBlockItem::new);

  public static final DeferredItem<ControlTrackBlockItem> ELECTRIC_CONTROL_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_CONTROL_TRACK, ControlTrackBlockItem::new);

  public static final DeferredItem<GatedTrackBlockItem> ELECTRIC_GATED_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_GATED_TRACK, GatedTrackBlockItem::new);

  public static final DeferredItem<DetectorTrackBlockItem> ELECTRIC_DETECTOR_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_DETECTOR_TRACK, DetectorTrackBlockItem::new);

  public static final DeferredItem<CouplerTrackBlockItem> ELECTRIC_COUPLER_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_COUPLER_TRACK, CouplerTrackBlockItem::new);

  public static final DeferredItem<EmbarkingTrackBlockItem> ELECTRIC_EMBARKING_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_EMBARKING_TRACK, EmbarkingTrackBlockItem::new);

  public static final DeferredItem<DisembarkingTrackBlockItem> ELECTRIC_DISEMBARKING_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_DISEMBARKING_TRACK, DisembarkingTrackBlockItem::new);

  public static final DeferredItem<DumpingTrackBlockItem> ELECTRIC_DUMPING_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_DUMPING_TRACK, DumpingTrackBlockItem::new);

  public static final DeferredItem<BlockItem> ELECTRIC_WYE_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_WYE_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_TURNOUT_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_TURNOUT_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_JUNCTION_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_JUNCTION_TRACK);

  public static final DeferredItem<LauncherTrackBlockItem> ELECTRIC_LAUNCHER_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_LAUNCHER_TRACK, LauncherTrackBlockItem::new);

  public static final DeferredItem<OneWayTrackBlockItem> ELECTRIC_ONE_WAY_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_ONE_WAY_TRACK, OneWayTrackBlockItem::new);

  public static final DeferredItem<WhistleTrackBlockItem> ELECTRIC_WHISTLE_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_WHISTLE_TRACK, WhistleTrackBlockItem::new);

  public static final DeferredItem<LocomotiveTrackBlockItem> ELECTRIC_LOCOMOTIVE_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_LOCOMOTIVE_TRACK, LocomotiveTrackBlockItem::new);

  public static final DeferredItem<ThrottleTrackBlockItem> ELECTRIC_THROTTLE_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_THROTTLE_TRACK, ThrottleTrackBlockItem::new);

  public static final DeferredItem<RoutingTrackBlockItem> ELECTRIC_ROUTING_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_ROUTING_TRACK, RoutingTrackBlockItem::new);

  public static final DeferredItem<HighSpeedTrackBlockItem> HIGH_SPEED_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_TRACK, HighSpeedTrackBlockItem::new);

  public static final DeferredItem<TransitionTrackBlockItem> HIGH_SPEED_TRANSITION_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_TRANSITION_TRACK, TransitionTrackBlockItem::new);

  public static final DeferredItem<LockingTrackBlockItem> HIGH_SPEED_LOCKING_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_LOCKING_TRACK, LockingTrackBlockItem::new);

  public static final DeferredItem<ActivatorTrackBlockItem> HIGH_SPEED_ACTIVATOR_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_ACTIVATOR_TRACK, ActivatorTrackBlockItem::new);

  public static final DeferredItem<BoosterTrackBlockItem> HIGH_SPEED_BOOSTER_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_BOOSTER_TRACK, BoosterTrackBlockItem::new);

  public static final DeferredItem<DetectorTrackBlockItem> HIGH_SPEED_DETECTOR_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_DETECTOR_TRACK, DetectorTrackBlockItem::new);

  public static final DeferredItem<BlockItem> HIGH_SPEED_WYE_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_WYE_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_TURNOUT_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_TURNOUT_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_JUNCTION_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_JUNCTION_TRACK);

  public static final DeferredItem<WhistleTrackBlockItem> HIGH_SPEED_WHISTLE_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_WHISTLE_TRACK, WhistleTrackBlockItem::new);

  public static final DeferredItem<LocomotiveTrackBlockItem> HIGH_SPEED_LOCOMOTIVE_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_LOCOMOTIVE_TRACK, LocomotiveTrackBlockItem::new);

  public static final DeferredItem<ThrottleTrackBlockItem> HIGH_SPEED_THROTTLE_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_THROTTLE_TRACK, ThrottleTrackBlockItem::new);

  public static final DeferredItem<HighSpeedElectricTrackBlockItem> HIGH_SPEED_ELECTRIC_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRACK, HighSpeedElectricTrackBlockItem::new);

  public static final DeferredItem<TransitionTrackBlockItem> HIGH_SPEED_ELECTRIC_TRANSITION_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRANSITION_TRACK, TransitionTrackBlockItem::new);

  public static final DeferredItem<LockingTrackBlockItem> HIGH_SPEED_ELECTRIC_LOCKING_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCKING_TRACK, LockingTrackBlockItem::new);

  public static final DeferredItem<ActivatorTrackBlockItem> HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK, ActivatorTrackBlockItem::new);

  public static final DeferredItem<BoosterTrackBlockItem> HIGH_SPEED_ELECTRIC_BOOSTER_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_BOOSTER_TRACK, BoosterTrackBlockItem::new);

  public static final DeferredItem<DetectorTrackBlockItem> HIGH_SPEED_ELECTRIC_DETECTOR_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_DETECTOR_TRACK, DetectorTrackBlockItem::new);

  public static final DeferredItem<BlockItem> HIGH_SPEED_ELECTRIC_WYE_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_WYE_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_ELECTRIC_TURNOUT_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_TURNOUT_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_ELECTRIC_JUNCTION_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_JUNCTION_TRACK);

  public static final DeferredItem<WhistleTrackBlockItem> HIGH_SPEED_ELECTRIC_WHISTLE_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_WHISTLE_TRACK, WhistleTrackBlockItem::new);

  public static final DeferredItem<LocomotiveTrackBlockItem> HIGH_SPEED_ELECTRIC_LOCOMOTIVE_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCOMOTIVE_TRACK, LocomotiveTrackBlockItem::new);

  public static final DeferredItem<ThrottleTrackBlockItem> HIGH_SPEED_ELECTRIC_THROTTLE_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_THROTTLE_TRACK, ThrottleTrackBlockItem::new);

  public static final DeferredItem<LockingTrackBlockItem> IRON_LOCKING_TRACK =
      blockItem(RailcraftBlocks.IRON_LOCKING_TRACK, LockingTrackBlockItem::new);

  public static final DeferredItem<BufferStopTrackBlockItem> IRON_BUFFER_STOP_TRACK =
      blockItem(RailcraftBlocks.IRON_BUFFER_STOP_TRACK, BufferStopTrackBlockItem::new);

  public static final DeferredItem<ActivatorTrackBlockItem> IRON_ACTIVATOR_TRACK =
      blockItem(RailcraftBlocks.IRON_ACTIVATOR_TRACK, ActivatorTrackBlockItem::new);

  public static final DeferredItem<BoosterTrackBlockItem> IRON_BOOSTER_TRACK =
      blockItem(RailcraftBlocks.IRON_BOOSTER_TRACK, BoosterTrackBlockItem::new);

  public static final DeferredItem<ControlTrackBlockItem> IRON_CONTROL_TRACK =
      blockItem(RailcraftBlocks.IRON_CONTROL_TRACK, ControlTrackBlockItem::new);

  public static final DeferredItem<GatedTrackBlockItem> IRON_GATED_TRACK =
      blockItem(RailcraftBlocks.IRON_GATED_TRACK, GatedTrackBlockItem::new);

  public static final DeferredItem<DetectorTrackBlockItem> IRON_DETECTOR_TRACK =
      blockItem(RailcraftBlocks.IRON_DETECTOR_TRACK, DetectorTrackBlockItem::new);

  public static final DeferredItem<CouplerTrackBlockItem> IRON_COUPLER_TRACK =
      blockItem(RailcraftBlocks.IRON_COUPLER_TRACK, CouplerTrackBlockItem::new);

  public static final DeferredItem<EmbarkingTrackBlockItem> IRON_EMBARKING_TRACK =
      blockItem(RailcraftBlocks.IRON_EMBARKING_TRACK, EmbarkingTrackBlockItem::new);

  public static final DeferredItem<DisembarkingTrackBlockItem> IRON_DISEMBARKING_TRACK =
      blockItem(RailcraftBlocks.IRON_DISEMBARKING_TRACK, DisembarkingTrackBlockItem::new);

  public static final DeferredItem<DumpingTrackBlockItem> IRON_DUMPING_TRACK =
      blockItem(RailcraftBlocks.IRON_DUMPING_TRACK, DumpingTrackBlockItem::new);

  public static final DeferredItem<BlockItem> IRON_WYE_TRACK =
      blockItem(RailcraftBlocks.IRON_WYE_TRACK);

  public static final DeferredItem<BlockItem> IRON_TURNOUT_TRACK =
      blockItem(RailcraftBlocks.IRON_TURNOUT_TRACK);

  public static final DeferredItem<BlockItem> IRON_JUNCTION_TRACK =
      blockItem(RailcraftBlocks.IRON_JUNCTION_TRACK);

  public static final DeferredItem<LauncherTrackBlockItem> IRON_LAUNCHER_TRACK =
      blockItem(RailcraftBlocks.IRON_LAUNCHER_TRACK, LauncherTrackBlockItem::new);

  public static final DeferredItem<OneWayTrackBlockItem> IRON_ONE_WAY_TRACK =
      blockItem(RailcraftBlocks.IRON_ONE_WAY_TRACK, OneWayTrackBlockItem::new);

  public static final DeferredItem<WhistleTrackBlockItem> IRON_WHISTLE_TRACK =
      blockItem(RailcraftBlocks.IRON_WHISTLE_TRACK, WhistleTrackBlockItem::new);

  public static final DeferredItem<LocomotiveTrackBlockItem> IRON_LOCOMOTIVE_TRACK =
      blockItem(RailcraftBlocks.IRON_LOCOMOTIVE_TRACK, LocomotiveTrackBlockItem::new);

  public static final DeferredItem<ThrottleTrackBlockItem> IRON_THROTTLE_TRACK =
      blockItem(RailcraftBlocks.IRON_THROTTLE_TRACK, ThrottleTrackBlockItem::new);

  public static final DeferredItem<RoutingTrackBlockItem> IRON_ROUTING_TRACK =
      blockItem(RailcraftBlocks.IRON_ROUTING_TRACK, RoutingTrackBlockItem::new);

  public static final DeferredItem<ReinforcedTrackBlockItem> REINFORCED_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_TRACK, ReinforcedTrackBlockItem::new);

  public static final DeferredItem<LockingTrackBlockItem> REINFORCED_LOCKING_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_LOCKING_TRACK, LockingTrackBlockItem::new);

  public static final DeferredItem<BufferStopTrackBlockItem> REINFORCED_BUFFER_STOP_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_BUFFER_STOP_TRACK, BufferStopTrackBlockItem::new);

  public static final DeferredItem<ActivatorTrackBlockItem> REINFORCED_ACTIVATOR_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_ACTIVATOR_TRACK, ActivatorTrackBlockItem::new);

  public static final DeferredItem<BoosterTrackBlockItem> REINFORCED_BOOSTER_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_BOOSTER_TRACK, BoosterTrackBlockItem::new);

  public static final DeferredItem<ControlTrackBlockItem> REINFORCED_CONTROL_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_CONTROL_TRACK, ControlTrackBlockItem::new);

  public static final DeferredItem<GatedTrackBlockItem> REINFORCED_GATED_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_GATED_TRACK, GatedTrackBlockItem::new);

  public static final DeferredItem<DetectorTrackBlockItem> REINFORCED_DETECTOR_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_DETECTOR_TRACK, DetectorTrackBlockItem::new);

  public static final DeferredItem<CouplerTrackBlockItem> REINFORCED_COUPLER_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_COUPLER_TRACK, CouplerTrackBlockItem::new);

  public static final DeferredItem<EmbarkingTrackBlockItem> REINFORCED_EMBARKING_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_EMBARKING_TRACK, EmbarkingTrackBlockItem::new);

  public static final DeferredItem<DisembarkingTrackBlockItem> REINFORCED_DISEMBARKING_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_DISEMBARKING_TRACK, DisembarkingTrackBlockItem::new);

  public static final DeferredItem<DumpingTrackBlockItem> REINFORCED_DUMPING_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_DUMPING_TRACK, DumpingTrackBlockItem::new);

  public static final DeferredItem<BlockItem> REINFORCED_WYE_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_WYE_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_TURNOUT_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_TURNOUT_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_JUNCTION_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_JUNCTION_TRACK);

  public static final DeferredItem<LauncherTrackBlockItem> REINFORCED_LAUNCHER_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_LAUNCHER_TRACK, LauncherTrackBlockItem::new);

  public static final DeferredItem<OneWayTrackBlockItem> REINFORCED_ONE_WAY_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_ONE_WAY_TRACK, OneWayTrackBlockItem::new);

  public static final DeferredItem<WhistleTrackBlockItem> REINFORCED_WHISTLE_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_WHISTLE_TRACK, WhistleTrackBlockItem::new);

  public static final DeferredItem<LocomotiveTrackBlockItem> REINFORCED_LOCOMOTIVE_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_LOCOMOTIVE_TRACK, LocomotiveTrackBlockItem::new);

  public static final DeferredItem<ThrottleTrackBlockItem> REINFORCED_THROTTLE_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_THROTTLE_TRACK, ThrottleTrackBlockItem::new);

  public static final DeferredItem<RoutingTrackBlockItem> REINFORCED_ROUTING_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_ROUTING_TRACK, RoutingTrackBlockItem::new);

  public static final DeferredItem<StrapIronTrackBlockItem> STRAP_IRON_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_TRACK, StrapIronTrackBlockItem::new);

  public static final DeferredItem<LockingTrackBlockItem> STRAP_IRON_LOCKING_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_LOCKING_TRACK, LockingTrackBlockItem::new);

  public static final DeferredItem<BufferStopTrackBlockItem> STRAP_IRON_BUFFER_STOP_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_BUFFER_STOP_TRACK, BufferStopTrackBlockItem::new);

  public static final DeferredItem<ActivatorTrackBlockItem> STRAP_IRON_ACTIVATOR_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_ACTIVATOR_TRACK, ActivatorTrackBlockItem::new);

  public static final DeferredItem<BoosterTrackBlockItem> STRAP_IRON_BOOSTER_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_BOOSTER_TRACK, BoosterTrackBlockItem::new);

  public static final DeferredItem<ControlTrackBlockItem> STRAP_IRON_CONTROL_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_CONTROL_TRACK, ControlTrackBlockItem::new);

  public static final DeferredItem<GatedTrackBlockItem> STRAP_IRON_GATED_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_GATED_TRACK, GatedTrackBlockItem::new);

  public static final DeferredItem<DetectorTrackBlockItem> STRAP_IRON_DETECTOR_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_DETECTOR_TRACK, DetectorTrackBlockItem::new);

  public static final DeferredItem<CouplerTrackBlockItem> STRAP_IRON_COUPLER_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_COUPLER_TRACK, CouplerTrackBlockItem::new);

  public static final DeferredItem<EmbarkingTrackBlockItem> STRAP_IRON_EMBARKING_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_EMBARKING_TRACK, EmbarkingTrackBlockItem::new);

  public static final DeferredItem<DisembarkingTrackBlockItem> STRAP_IRON_DISEMBARKING_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_DISEMBARKING_TRACK, DisembarkingTrackBlockItem::new);

  public static final DeferredItem<DumpingTrackBlockItem> STRAP_IRON_DUMPING_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_DUMPING_TRACK, DumpingTrackBlockItem::new);

  public static final DeferredItem<BlockItem> STRAP_IRON_WYE_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_WYE_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_TURNOUT_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_TURNOUT_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_JUNCTION_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_JUNCTION_TRACK);

  public static final DeferredItem<LauncherTrackBlockItem> STRAP_IRON_LAUNCHER_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_LAUNCHER_TRACK, LauncherTrackBlockItem::new);

  public static final DeferredItem<OneWayTrackBlockItem> STRAP_IRON_ONE_WAY_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_ONE_WAY_TRACK, OneWayTrackBlockItem::new);

  public static final DeferredItem<WhistleTrackBlockItem> STRAP_IRON_WHISTLE_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_WHISTLE_TRACK, WhistleTrackBlockItem::new);

  public static final DeferredItem<LocomotiveTrackBlockItem> STRAP_IRON_LOCOMOTIVE_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_LOCOMOTIVE_TRACK, LocomotiveTrackBlockItem::new);

  public static final DeferredItem<ThrottleTrackBlockItem> STRAP_IRON_THROTTLE_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_THROTTLE_TRACK, ThrottleTrackBlockItem::new);

  public static final DeferredItem<RoutingTrackBlockItem> STRAP_IRON_ROUTING_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_ROUTING_TRACK, RoutingTrackBlockItem::new);

  public static final DeferredItem<BlockItem> ELEVATOR_TRACK =
      blockItem(RailcraftBlocks.ELEVATOR_TRACK);

  public static final DeferredItem<CrowbarItem> IRON_CROWBAR =
      deferredRegister.registerItem("iron_crowbar", properties ->
          new CrowbarItem(properties
              .tool(ToolMaterial.IRON, RailcraftTags.Blocks.MINEABLE_WITH_CROWBAR, 2.5F, -2.8F, 0)));

  public static final DeferredItem<CrowbarItem> STEEL_CROWBAR =
      deferredRegister.registerItem("steel_crowbar", properties ->
          new CrowbarItem(properties
              .tool(RailcraftToolMaterial.STEEL, RailcraftTags.Blocks.MINEABLE_WITH_CROWBAR, 2.5F, -2.7F, 0)));

  public static final DeferredItem<CrowbarItem> DIAMOND_CROWBAR =
      deferredRegister.registerItem("diamond_crowbar", properties ->
          new CrowbarItem(properties
              .tool(ToolMaterial.DIAMOND, RailcraftTags.Blocks.MINEABLE_WITH_CROWBAR, 2.5F, -2.4F, 0)));

  public static final DeferredItem<SeasonsCrowbarItem> SEASONS_CROWBAR =
      deferredRegister.registerItem("seasons_crowbar", properties ->
          new SeasonsCrowbarItem(properties
              .tool(ToolMaterial.DIAMOND, RailcraftTags.Blocks.MINEABLE_WITH_CROWBAR, 2.5F, -2.4F, 0)
              .component(RailcraftDataComponents.SEASON, SeasonComponent.DEFAULT)));

  public static final DeferredItem<Item> TRACK_PARTS = registerBasic("track_parts");

  public static final DeferredItem<TrackKitItem> TRANSITION_TRACK_KIT =
      deferredRegister.registerItem("transition_track_kit",
          properties -> new TrackKitItem((TrackKitItem.Properties) properties),
          new TrackKitItem.Properties()
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED, RailcraftBlocks.HIGH_SPEED_TRANSITION_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
                  RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRANSITION_TRACK));

  public static final DeferredItem<TrackKitItem> LOCKING_TRACK_KIT =
      deferredRegister.registerItem("locking_track_kit",
          properties -> new TrackKitItem((TrackKitItem.Properties) properties),
          new TrackKitItem.Properties()
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_LOCKING_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_LOCKING_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED, RailcraftBlocks.HIGH_SPEED_LOCKING_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
                  RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCKING_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_LOCKING_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_LOCKING_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON, RailcraftBlocks.STRAP_IRON_LOCKING_TRACK));

  public static final DeferredItem<TrackKitItem> BUFFER_STOP_TRACK_KIT =
      deferredRegister.registerItem("buffer_stop_track_kit",
          properties -> new TrackKitItem((TrackKitItem.Properties) properties),
          new TrackKitItem.Properties()
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_BUFFER_STOP_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_BUFFER_STOP_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_BUFFER_STOP_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED,
                  RailcraftBlocks.REINFORCED_BUFFER_STOP_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON,
                  RailcraftBlocks.STRAP_IRON_BUFFER_STOP_TRACK));

  public static final DeferredItem<TrackKitItem> ACTIVATOR_TRACK_KIT =
      deferredRegister.registerItem("activator_track_kit",
          properties -> new TrackKitItem((TrackKitItem.Properties) properties),
          new TrackKitItem.Properties()
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_ACTIVATOR_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_ACTIVATOR_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED, RailcraftBlocks.HIGH_SPEED_ACTIVATOR_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
                  RailcraftBlocks.HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_ACTIVATOR_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_ACTIVATOR_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON,
                  RailcraftBlocks.STRAP_IRON_ACTIVATOR_TRACK));

  public static final DeferredItem<TrackKitItem> BOOSTER_TRACK_KIT =
      deferredRegister.registerItem("booster_track_kit",
          properties -> new TrackKitItem((TrackKitItem.Properties) properties),
          new TrackKitItem.Properties()
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_BOOSTER_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_BOOSTER_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED, RailcraftBlocks.HIGH_SPEED_BOOSTER_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
                  RailcraftBlocks.HIGH_SPEED_ELECTRIC_BOOSTER_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_BOOSTER_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_BOOSTER_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON, RailcraftBlocks.STRAP_IRON_BOOSTER_TRACK));

  public static final DeferredItem<TrackKitItem> CONTROL_TRACK_KIT =
      deferredRegister.registerItem("control_track_kit",
          properties -> new TrackKitItem((TrackKitItem.Properties) properties),
          new TrackKitItem.Properties()
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_CONTROL_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_CONTROL_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_CONTROL_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_CONTROL_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON, RailcraftBlocks.STRAP_IRON_CONTROL_TRACK));

  public static final DeferredItem<TrackKitItem> GATED_TRACK_KIT =
      deferredRegister.registerItem("gated_track_kit",
          properties -> new TrackKitItem((TrackKitItem.Properties) properties),
          new TrackKitItem.Properties()
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_GATED_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_GATED_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_GATED_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_GATED_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON, RailcraftBlocks.STRAP_IRON_GATED_TRACK));

  public static final DeferredItem<TrackKitItem> DETECTOR_TRACK_KIT =
      deferredRegister.registerItem("detector_track_kit",
          properties -> new TrackKitItem((TrackKitItem.Properties) properties),
          new TrackKitItem.Properties()
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_DETECTOR_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_DETECTOR_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED, RailcraftBlocks.HIGH_SPEED_DETECTOR_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
                  RailcraftBlocks.HIGH_SPEED_ELECTRIC_DETECTOR_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_DETECTOR_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_DETECTOR_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON,
                  RailcraftBlocks.STRAP_IRON_DETECTOR_TRACK));

  public static final DeferredItem<TrackKitItem> COUPLER_TRACK_KIT =
      deferredRegister.registerItem("coupler_track_kit",
          properties -> new TrackKitItem((TrackKitItem.Properties) properties),
          new TrackKitItem.Properties()
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_COUPLER_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_COUPLER_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_COUPLER_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_COUPLER_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON, RailcraftBlocks.STRAP_IRON_COUPLER_TRACK));

  public static final DeferredItem<TrackKitItem> EMBARKING_TRACK_KIT =
      deferredRegister.registerItem("embarking_track_kit",
          properties -> new TrackKitItem((TrackKitItem.Properties) properties),
          new TrackKitItem.Properties()
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_EMBARKING_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_EMBARKING_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_EMBARKING_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_EMBARKING_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON,
                  RailcraftBlocks.STRAP_IRON_EMBARKING_TRACK));

  public static final DeferredItem<TrackKitItem> DISEMBARKING_TRACK_KIT =
      deferredRegister.registerItem("disembarking_track_kit",
          properties -> new TrackKitItem((TrackKitItem.Properties) properties),
          new TrackKitItem.Properties()
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_DISEMBARKING_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_DISEMBARKING_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_DISEMBARKING_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED,
                  RailcraftBlocks.REINFORCED_DISEMBARKING_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON,
                  RailcraftBlocks.STRAP_IRON_DISEMBARKING_TRACK));

  public static final DeferredItem<TrackKitItem> DUMPING_TRACK_KIT =
      deferredRegister.registerItem("dumping_track_kit",
          properties -> new TrackKitItem((TrackKitItem.Properties) properties),
          new TrackKitItem.Properties()
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_DUMPING_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_DUMPING_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_DUMPING_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED,
                  RailcraftBlocks.REINFORCED_DUMPING_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON,
                  RailcraftBlocks.STRAP_IRON_DUMPING_TRACK));

  public static final DeferredItem<TrackKitItem> LAUNCHER_TRACK_KIT =
      deferredRegister.registerItem("launcher_track_kit",
          properties -> new TrackKitItem((TrackKitItem.Properties) properties),
          new TrackKitItem.Properties()
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_LAUNCHER_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_LAUNCHER_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_LAUNCHER_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED,
                  RailcraftBlocks.REINFORCED_LAUNCHER_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON,
                  RailcraftBlocks.STRAP_IRON_LAUNCHER_TRACK));

  public static final DeferredItem<TrackKitItem> ONE_WAY_TRACK_KIT =
      deferredRegister.registerItem("one_way_track_kit",
          properties -> new TrackKitItem((TrackKitItem.Properties) properties),
          new TrackKitItem.Properties()
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_ONE_WAY_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_ONE_WAY_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_ONE_WAY_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED,
                  RailcraftBlocks.REINFORCED_ONE_WAY_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON,
                  RailcraftBlocks.STRAP_IRON_ONE_WAY_TRACK));

  public static final DeferredItem<TrackKitItem> WHISTLE_TRACK_KIT =
      deferredRegister.registerItem("whistle_track_kit",
          properties -> new TrackKitItem((TrackKitItem.Properties) properties),
          new TrackKitItem.Properties()
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_WHISTLE_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_WHISTLE_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED, RailcraftBlocks.HIGH_SPEED_WHISTLE_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
                  RailcraftBlocks.HIGH_SPEED_ELECTRIC_WHISTLE_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_WHISTLE_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_WHISTLE_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON, RailcraftBlocks.STRAP_IRON_WHISTLE_TRACK));

  public static final DeferredItem<TrackKitItem> LOCOMOTIVE_TRACK_KIT =
      deferredRegister.registerItem("locomotive_track_kit",
          properties -> new TrackKitItem((TrackKitItem.Properties) properties),
          new TrackKitItem.Properties()
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_LOCOMOTIVE_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_LOCOMOTIVE_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED, RailcraftBlocks.HIGH_SPEED_LOCOMOTIVE_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
                  RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCOMOTIVE_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_LOCOMOTIVE_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_LOCOMOTIVE_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON,
                  RailcraftBlocks.STRAP_IRON_LOCOMOTIVE_TRACK));

  public static final DeferredItem<TrackKitItem> THROTTLE_TRACK_KIT =
      deferredRegister.registerItem("throttle_track_kit",
          properties -> new TrackKitItem((TrackKitItem.Properties) properties),
          new TrackKitItem.Properties()
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_THROTTLE_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_THROTTLE_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED, RailcraftBlocks.HIGH_SPEED_THROTTLE_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
                  RailcraftBlocks.HIGH_SPEED_ELECTRIC_THROTTLE_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_THROTTLE_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_THROTTLE_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON,
                  RailcraftBlocks.STRAP_IRON_THROTTLE_TRACK));

  public static final DeferredItem<TrackKitItem> ROUTING_TRACK_KIT =
      deferredRegister.registerItem("routing_track_kit",
          properties -> new TrackKitItem((TrackKitItem.Properties) properties),
          new TrackKitItem.Properties()
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_ROUTING_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_ROUTING_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_ROUTING_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_ROUTING_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON, RailcraftBlocks.STRAP_IRON_ROUTING_TRACK));

  public static final DeferredItem<GogglesItem> GOGGLES =
      deferredRegister.registerItem("goggles", properties ->
          new GogglesItem(properties
              .humanoidArmor(RailcraftArmorMaterials.GOGGLES, ArmorType.HELMET)
              .component(RailcraftDataComponents.AURA, AuraComponent.NONE)));

  public static final DeferredItem<ManualRollingMachineBlockItem> MANUAL_ROLLING_MACHINE =
      blockItem(RailcraftBlocks.MANUAL_ROLLING_MACHINE, ManualRollingMachineBlockItem::new);

  public static final DeferredItem<PoweredRollingMachineBlockItem> POWERED_ROLLING_MACHINE =
      blockItem(RailcraftBlocks.POWERED_ROLLING_MACHINE, PoweredRollingMachineBlockItem::new);

  public static final DeferredItem<CrusherMultiblockBlockItem> CRUSHER =
      blockItem(RailcraftBlocks.CRUSHER, CrusherMultiblockBlockItem::new);

  public static final DeferredItem<CokeOvenBricksBlockItem> COKE_OVEN_BRICKS =
      blockItem(RailcraftBlocks.COKE_OVEN_BRICKS, CokeOvenBricksBlockItem::new);

  public static final DeferredItem<SteamOvenBlockItem> STEAM_OVEN =
      blockItem(RailcraftBlocks.STEAM_OVEN, SteamOvenBlockItem::new);

  public static final DeferredItem<CrushedObsidianBlockItem> CRUSHED_OBSIDIAN =
      blockItem(RailcraftBlocks.CRUSHED_OBSIDIAN, CrushedObsidianBlockItem::new);

  public static final DeferredItem<Item> COAL_COKE = registerBasic("coal_coke");
  public static final DeferredItem<Item> STEEL_PLATE = registerBasic("steel_plate");
  public static final DeferredItem<Item> IRON_PLATE = registerBasic("iron_plate");
  public static final DeferredItem<Item> TIN_PLATE = registerBasic("tin_plate");
  public static final DeferredItem<Item> GOLD_PLATE = registerBasic("gold_plate");
  public static final DeferredItem<Item> LEAD_PLATE = registerBasic("lead_plate");
  public static final DeferredItem<Item> ZINC_PLATE = registerBasic("zinc_plate");
  public static final DeferredItem<Item> BRASS_PLATE = registerBasic("brass_plate");
  public static final DeferredItem<Item> INVAR_PLATE = registerBasic("invar_plate");
  public static final DeferredItem<Item> BRONZE_PLATE = registerBasic("bronze_plate");
  public static final DeferredItem<Item> COPPER_PLATE = registerBasic("copper_plate");
  public static final DeferredItem<Item> NICKEL_PLATE = registerBasic("nickel_plate");
  public static final DeferredItem<Item> SILVER_PLATE = registerBasic("silver_plate");

  public static final DeferredItem<Item> STEEL_INGOT = registerBasic("steel_ingot");
  public static final DeferredItem<Item> TIN_INGOT = registerBasic("tin_ingot");
  public static final DeferredItem<Item> ZINC_INGOT = registerBasic("zinc_ingot");
  public static final DeferredItem<Item> BRASS_INGOT = registerBasic("brass_ingot");
  public static final DeferredItem<Item> BRONZE_INGOT = registerBasic("bronze_ingot");
  public static final DeferredItem<Item> NICKEL_INGOT = registerBasic("nickel_ingot");
  public static final DeferredItem<Item> INVAR_INGOT = registerBasic("invar_ingot");
  public static final DeferredItem<Item> LEAD_INGOT = registerBasic("lead_ingot");
  public static final DeferredItem<Item> SILVER_INGOT = registerBasic("silver_ingot");

  public static final DeferredItem<Item> SALTPETER_DUST = registerBasic("saltpeter_dust");
  public static final DeferredItem<Item> COAL_DUST = registerBasic("coal_dust");
  public static final DeferredItem<Item> CHARCOAL_DUST = registerBasic("charcoal_dust");
  public static final DeferredItem<Item> SLAG = registerBasic("slag");
  public static final DeferredItem<Item> ENDER_DUST = registerBasic("ender_dust");
  public static final DeferredItem<Item> SULFUR_DUST = registerBasic("sulfur_dust");
  public static final DeferredItem<Item> OBSIDIAN_DUST = registerBasic("obsidian_dust");

  public static final DeferredItem<Item> STEEL_NUGGET = registerBasic("steel_nugget");
  public static final DeferredItem<Item> TIN_NUGGET = registerBasic("tin_nugget");
  public static final DeferredItem<Item> ZINC_NUGGET = registerBasic("zinc_nugget");
  public static final DeferredItem<Item> BRASS_NUGGET = registerBasic("brass_nugget");
  public static final DeferredItem<Item> BRONZE_NUGGET = registerBasic("bronze_nugget");
  public static final DeferredItem<Item> NICKEL_NUGGET = registerBasic("nickel_nugget");
  public static final DeferredItem<Item> INVAR_NUGGET = registerBasic("invar_nugget");
  public static final DeferredItem<Item> SILVER_NUGGET = registerBasic("silver_nugget");
  public static final DeferredItem<Item> LEAD_NUGGET = registerBasic("lead_nugget");

  public static final DeferredItem<Item> TIN_RAW =registerBasic("tin_raw");
  public static final DeferredItem<Item> ZINC_RAW = registerBasic("zinc_raw");
  public static final DeferredItem<Item> NICKEL_RAW = registerBasic("nickel_raw");
  public static final DeferredItem<Item> SILVER_RAW = registerBasic("silver_raw");
  public static final DeferredItem<Item> LEAD_RAW = registerBasic("lead_raw");

  public static final DeferredItem<Item> BUSHING_GEAR = registerBasic("bushing_gear");
  public static final DeferredItem<Item> TIN_GEAR = registerBasic("tin_gear");
  public static final DeferredItem<Item> GOLD_GEAR = registerBasic("gold_gear");
  public static final DeferredItem<Item> IRON_GEAR = registerBasic("iron_gear");
  public static final DeferredItem<Item> LEAD_GEAR = registerBasic("lead_gear");
  public static final DeferredItem<Item> ZINC_GEAR = registerBasic("zinc_gear");
  public static final DeferredItem<Item> BRASS_GEAR = registerBasic("brass_gear");
  public static final DeferredItem<Item> INVAR_GEAR = registerBasic("invar_gear");
  public static final DeferredItem<Item> STEEL_GEAR = registerBasic("steel_gear");
  public static final DeferredItem<Item> BRONZE_GEAR = registerBasic("bronze_gear");
  public static final DeferredItem<Item> COPPER_GEAR = registerBasic("copper_gear");
  public static final DeferredItem<Item> NICKEL_GEAR = registerBasic("nickel_gear");
  public static final DeferredItem<Item> SILVER_GEAR = registerBasic("silver_gear");

  public static final DeferredItem<Item> TIN_ELECTRODE = registerBasic("tin_electrode");
  public static final DeferredItem<Item> GOLD_ELECTRODE = registerBasic("gold_electrode");
  public static final DeferredItem<Item> IRON_ELECTRODE = registerBasic("iron_electrode");
  public static final DeferredItem<Item> LEAD_ELECTRODE = registerBasic("lead_electrode");
  public static final DeferredItem<Item> ZINC_ELECTRODE = registerBasic("zinc_electrode");
  public static final DeferredItem<Item> BRASS_ELECTRODE = registerBasic("brass_electrode");
  public static final DeferredItem<Item> INVAR_ELECTRODE = registerBasic("invar_electrode");
  public static final DeferredItem<Item> STEEL_ELECTRODE = registerBasic("steel_electrode");
  public static final DeferredItem<Item> BRONZE_ELECTRODE = registerBasic("bronze_electrode");
  public static final DeferredItem<Item> CARBON_ELECTRODE = registerBasic("carbon_electrode");
  public static final DeferredItem<Item> COPPER_ELECTRODE = registerBasic("copper_electrode");
  public static final DeferredItem<Item> NICKEL_ELECTRODE = registerBasic("nickel_electrode");
  public static final DeferredItem<Item> SILVER_ELECTRODE = registerBasic("silver_electrode");

  public static final DeferredItem<Item> CONTROLLER_CIRCUIT = registerBasic("controller_circuit");
  public static final DeferredItem<Item> RECEIVER_CIRCUIT = registerBasic("receiver_circuit");
  public static final DeferredItem<Item> SIGNAL_CIRCUIT = registerBasic("signal_circuit");
  public static final DeferredItem<Item> RADIO_CIRCUIT = registerBasic("radio_circuit");

  public static final DeferredItem<Item> WOODEN_RAIL = registerBasic("wooden_rail");
  public static final DeferredItem<Item> STANDARD_RAIL = registerBasic("standard_rail");
  public static final DeferredItem<Item> ADVANCED_RAIL = registerBasic("advanced_rail");
  public static final DeferredItem<Item> REINFORCED_RAIL = registerBasic("reinforced_rail");
  public static final DeferredItem<Item> HIGH_SPEED_RAIL = registerBasic("high_speed_rail");
  public static final DeferredItem<Item> ELECTRIC_RAIL = registerBasic("electric_rail");

  public static final DeferredItem<Item> BAG_OF_CEMENT = registerBasic("bag_of_cement");

  public static final DeferredItem<Item> WOODEN_TIE = registerBasic("wooden_tie");
  public static final DeferredItem<Item> STONE_TIE = registerBasic("stone_tie");

  public static final DeferredItem<Item> REBAR = registerBasic("rebar");
  public static final DeferredItem<Item> WOODEN_RAILBED = registerBasic("wooden_railbed");
  public static final DeferredItem<Item> STONE_RAILBED = registerBasic("stone_railbed");

  public static final DeferredItem<Item> SIGNAL_LAMP = registerBasic("signal_lamp");

  public static final DeferredItem<Item> CHARGE_SPOOL_LARGE =
      registerBasic("charge_spool_large");
  public static final DeferredItem<Item> CHARGE_SPOOL_MEDIUM =
      registerBasic("charge_spool_medium");
  public static final DeferredItem<Item> CHARGE_SPOOL_SMALL =
      registerBasic("charge_spool_small");

  public static final DeferredItem<Item> CHARGE_MOTOR = registerBasic("charge_motor");

  public static final DeferredItem<Item> CHARGE_COIL = registerBasic("charge_coil");

  public static final DeferredItem<Item> CHARGE_TERMINAL = registerBasic("charge_terminal");

  public static final DeferredItem<WaterTankSidingBlockItem> WATER_TANK_SIDING =
      blockItem(RailcraftBlocks.WATER_TANK_SIDING, WaterTankSidingBlockItem::new);

  public static final VariantSet<DecorativeBlock, Item, BlockItem> DECORATIVE_STONE =
      VariantSet.ofMapped(
          DecorativeBlock.class,
          deferredRegister,
          RailcraftBlocks.DECORATIVE_STONE,
          BLOCK_TO_BLOCK_ITEM);

  public static final VariantSet<DecorativeBlock, Item, BlockItem> DECORATIVE_COBBLESTONE =
      VariantSet.ofMapped(
          DecorativeBlock.class,
          deferredRegister,
          RailcraftBlocks.DECORATIVE_COBBLESTONE,
          BLOCK_TO_BLOCK_ITEM);

  public static final VariantSet<DecorativeBlock, Item, BlockItem> POLISHED_DECORATIVE_STONE =
      VariantSet.ofMapped(
          DecorativeBlock.class,
          deferredRegister,
          RailcraftBlocks.POLISHED_DECORATIVE_STONE,
          BLOCK_TO_BLOCK_ITEM);

  public static final VariantSet<DecorativeBlock, Item, BlockItem> CHISELED_DECORATIVE_STONE =
      VariantSet.ofMapped(
          DecorativeBlock.class,
          deferredRegister,
          RailcraftBlocks.CHISELED_DECORATIVE_STONE,
          BLOCK_TO_BLOCK_ITEM);

  public static final VariantSet<DecorativeBlock, Item, BlockItem> ETCHED_DECORATIVE_STONE =
      VariantSet.ofMapped(
          DecorativeBlock.class,
          deferredRegister,
          RailcraftBlocks.ETCHED_DECORATIVE_STONE,
          BLOCK_TO_BLOCK_ITEM);

  public static final VariantSet<DecorativeBlock, Item, BlockItem> DECORATIVE_BRICKS =
      VariantSet.ofMapped(
          DecorativeBlock.class,
          deferredRegister,
          RailcraftBlocks.DECORATIVE_BRICKS,
          BLOCK_TO_BLOCK_ITEM);

  public static final VariantSet<DecorativeBlock, Item, BlockItem> DECORATIVE_BRICK_STAIRS =
      VariantSet.ofMapped(
          DecorativeBlock.class,
          deferredRegister,
          RailcraftBlocks.DECORATIVE_BRICK_STAIRS,
          BLOCK_TO_BLOCK_ITEM);

  public static final VariantSet<DecorativeBlock, Item, BlockItem> DECORATIVE_BRICK_SLAB =
      VariantSet.ofMapped(
          DecorativeBlock.class,
          deferredRegister,
          RailcraftBlocks.DECORATIVE_BRICK_SLAB,
          BLOCK_TO_BLOCK_ITEM);

  public static final VariantSet<DecorativeBlock, Item, BlockItem> DECORATIVE_PAVER =
      VariantSet.ofMapped(
          DecorativeBlock.class,
          deferredRegister,
          RailcraftBlocks.DECORATIVE_PAVER,
          BLOCK_TO_BLOCK_ITEM);

  public static final VariantSet<DecorativeBlock, Item, BlockItem> DECORATIVE_PAVER_STAIRS =
      VariantSet.ofMapped(
          DecorativeBlock.class,
          deferredRegister,
          RailcraftBlocks.DECORATIVE_PAVER_STAIRS,
          BLOCK_TO_BLOCK_ITEM);

  public static final VariantSet<DecorativeBlock, Item, BlockItem> DECORATIVE_PAVER_SLAB =
      VariantSet.ofMapped(
          DecorativeBlock.class,
          deferredRegister,
          RailcraftBlocks.DECORATIVE_PAVER_SLAB,
          BLOCK_TO_BLOCK_ITEM);

  public static final DeferredItem<BucketItem> CREOSOTE_BUCKET =
      deferredRegister.registerItem("creosote_bucket", properties ->
          new BucketItem(RailcraftFluids.CREOSOTE.get(), properties
              .stacksTo(1)
              .craftRemainder(Items.BUCKET)));

  public static final DeferredItem<BlockItem> WORLD_SPIKE =
      blockItem(RailcraftBlocks.WORLD_SPIKE);

  public static final DeferredItem<BlockItem> PERSONAL_WORLD_SPIKE =
      blockItem(RailcraftBlocks.PERSONAL_WORLD_SPIKE);

  private static DeferredItem<Item> registerBasic(String name) {
    return deferredRegister.registerSimpleItem(name);
  }

  private static DeferredItem<BlockItem> blockItem(DeferredBlock<? extends Block> block) {
    return deferredRegister.registerSimpleBlockItem(block);
  }

  public static <I extends BlockItem> DeferredItem<I> customBlockItem(String name,
      Function<Item.Properties, ? extends I> func) {
    return  deferredRegister.registerItem(name, p -> func.apply(p.useBlockDescriptionPrefix()));
  }

  public static <I extends BlockItem> DeferredItem<I> blockItem(
      DeferredBlock<? extends Block> block,
      BiFunction<? super Block, ? super Item.Properties, ? extends I> factory) {
    var name = block.unwrapKey().orElseThrow().location().getPath();
    return deferredRegister.registerItem(name,
        p -> factory.apply(block.value(), p.useBlockDescriptionPrefix()));
  }
}
