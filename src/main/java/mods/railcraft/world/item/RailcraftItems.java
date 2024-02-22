package mods.railcraft.world.item;

import java.util.Collection;
import java.util.function.Function;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.util.VariantSet;
import mods.railcraft.world.entity.vehicle.TankMinecart;
import mods.railcraft.world.entity.vehicle.WorldSpikeMinecart;
import mods.railcraft.world.entity.vehicle.locomotive.CreativeLocomotive;
import mods.railcraft.world.entity.vehicle.locomotive.ElectricLocomotive;
import mods.railcraft.world.entity.vehicle.locomotive.SteamLocomotive;
import mods.railcraft.world.item.tunnelbore.TunnelBoreHeadItem;
import mods.railcraft.world.item.tunnelbore.TunnelBoreItem;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.track.TrackTypes;
import mods.railcraft.world.level.material.RailcraftFluids;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RailcraftItems {

  private static final DeferredRegister.Items deferredRegister =
      DeferredRegister.createItems(RailcraftConstants.ID);

  private static final Function<Block, BlockItem> BLOCK_TO_BLOCK_ITEM =
      block -> new BlockItem(block, new Item.Properties());

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
      deferredRegister.registerItem("low_pressure_steam_boiler_tank", properties ->
          new PressureBoilerTankBlockItem(
              RailcraftBlocks.LOW_PRESSURE_STEAM_BOILER_TANK.get(), properties));

  public static final DeferredItem<PressureBoilerTankBlockItem> HIGH_PRESSURE_STEAM_BOILER_TANK =
      deferredRegister.registerItem("high_pressure_steam_boiler_tank", properties ->
          new PressureBoilerTankBlockItem(
              RailcraftBlocks.HIGH_PRESSURE_STEAM_BOILER_TANK.get(), properties));

  public static final DeferredItem<FueledFireboxBlockItem> SOLID_FUELED_FIREBOX =
      deferredRegister.registerItem("solid_fueled_firebox", properties ->
          new FueledFireboxBlockItem(RailcraftBlocks.SOLID_FUELED_FIREBOX.get(), properties));

  public static final DeferredItem<FueledFireboxBlockItem> FLUID_FUELED_FIREBOX =
      deferredRegister.registerItem("fluid_fueled_firebox", properties ->
          new FueledFireboxBlockItem(RailcraftBlocks.FLUID_FUELED_FIREBOX.get(), properties));

  public static final DeferredItem<SignalLabelItem> SIGNAL_LABEL =
      deferredRegister.registerItem("signal_label", SignalLabelItem::new);

  public static final DeferredItem<Item> TURBINE_BLADE = registerBasic("turbine_blade");

  public static final DeferredItem<Item> TURBINE_DISK = registerBasic("turbine_disk");

  public static final DeferredItem<TurbineRotorItem> TURBINE_ROTOR =
      deferredRegister.registerItem("turbine_rotor", properties ->
          new TurbineRotorItem(properties.stacksTo(1)));

  public static final DeferredItem<BlockItem> STEAM_TURBINE =
      blockItem(RailcraftBlocks.STEAM_TURBINE);

  public static final DeferredItem<BlockItem> BLAST_FURNACE_BRICKS =
      blockItem(RailcraftBlocks.BLAST_FURNACE_BRICKS);

  public static final DeferredItem<BlockItem> FEED_STATION =
      blockItem(RailcraftBlocks.FEED_STATION);

  public static final DeferredItem<BlockItem> LOGBOOK = blockItem(RailcraftBlocks.LOGBOOK);

  public static final DeferredItem<BlockItem> FRAME_BLOCK = blockItem(RailcraftBlocks.FRAME);

  public static final DeferredItem<ChargeMeterItem> CHARGE_METER =
      deferredRegister.registerItem("charge_meter", properties ->
          new ChargeMeterItem(properties
              .durability(0)
              .stacksTo(1)));

  public static final DeferredItem<BlockItem> NICKEL_ZINC_BATTERY =
      blockItem(RailcraftBlocks.NICKEL_ZINC_BATTERY);

  public static final DeferredItem<BlockItem> NICKEL_IRON_BATTERY =
      blockItem(RailcraftBlocks.NICKEL_IRON_BATTERY);

  public static final DeferredItem<BlockItem> ZINC_CARBON_BATTERY =
      blockItem(RailcraftBlocks.ZINC_CARBON_BATTERY);

  public static final DeferredItem<BlockItem> ZINC_CARBON_BATTERY_EMPTY =
      blockItem(RailcraftBlocks.ZINC_CARBON_BATTERY_EMPTY);

  public static final DeferredItem<BlockItem> ZINC_SILVER_BATTERY =
      blockItem(RailcraftBlocks.ZINC_SILVER_BATTERY);

  public static final DeferredItem<BlockItem> ZINC_SILVER_BATTERY_EMPTY =
      blockItem(RailcraftBlocks.ZINC_SILVER_BATTERY_EMPTY);

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

  public static final DeferredItem<BlockItem> COKE_BLOCK =
      blockItem(RailcraftBlocks.COKE_BLOCK);

  public static final DeferredItem<ShearsItem> STEEL_SHEARS =
      deferredRegister.registerItem("steel_shears", properties ->
          new ShearsItem(properties.durability(500)));

  public static final DeferredItem<SwordItem> STEEL_SWORD =
      deferredRegister.registerItem("steel_sword", properties ->
          new SwordItem(RailcraftItemTier.STEEL, 3, -2.4F, properties));

  public static final DeferredItem<ShovelItem> STEEL_SHOVEL =
      deferredRegister.registerItem("steel_shovel", properties ->
          new ShovelItem(RailcraftItemTier.STEEL, 1.5F, -3.0F, properties));

  public static final DeferredItem<PickaxeItem> STEEL_PICKAXE =
      deferredRegister.registerItem("steel_pickaxe", properties ->
          new PickaxeItem(RailcraftItemTier.STEEL, 1, -2.8F, properties));

  public static final DeferredItem<AxeItem> STEEL_AXE =
      deferredRegister.registerItem("steel_axe", properties ->
          new AxeItem(RailcraftItemTier.STEEL, 8.0F, -3F, properties));

  public static final DeferredItem<HoeItem> STEEL_HOE =
      deferredRegister.registerItem("steel_hoe", properties ->
          new HoeItem(RailcraftItemTier.STEEL, -2, -0.5F, properties));

  public static final DeferredItem<ArmorItem> STEEL_BOOTS =
      deferredRegister.registerItem("steel_boots", properties ->
          new ArmorItem(RailcraftArmorMaterial.STEEL, ArmorItem.Type.BOOTS, properties));

  public static final DeferredItem<ArmorItem> STEEL_CHESTPLATE =
      deferredRegister.registerItem("steel_chestplate", properties ->
          new ArmorItem(RailcraftArmorMaterial.STEEL, ArmorItem.Type.CHESTPLATE, properties));

  public static final DeferredItem<ArmorItem> STEEL_HELMET =
      deferredRegister.registerItem("steel_helmet", properties ->
          new ArmorItem(RailcraftArmorMaterial.STEEL, ArmorItem.Type.HELMET, properties));

  public static final DeferredItem<ArmorItem> STEEL_LEGGINGS =
      deferredRegister.registerItem("steel_leggings", properties ->
          new ArmorItem(RailcraftArmorMaterial.STEEL, ArmorItem.Type.LEGGINGS, properties));

  public static final DeferredItem<TunnelBoreHeadItem> IRON_TUNNEL_BORE_HEAD =
      deferredRegister.registerItem("iron_tunnel_bore_head", properties ->
          new TunnelBoreHeadItem(Tiers.IRON, "iron", properties.durability(1500)));

  public static final DeferredItem<TunnelBoreHeadItem> BRONZE_TUNNEL_BORE_HEAD =
      deferredRegister.registerItem("bronze_tunnel_bore_head", properties ->
          new TunnelBoreHeadItem(RailcraftItemTier.BRONZE, "bronze", properties.durability(1200)));

  public static final DeferredItem<TunnelBoreHeadItem> STEEL_TUNNEL_BORE_HEAD =
      deferredRegister.registerItem("steel_tunnel_bore_head", properties ->
          new TunnelBoreHeadItem(RailcraftItemTier.STEEL, "steel", properties.durability(3000)));

  public static final DeferredItem<TunnelBoreHeadItem> DIAMOND_TUNNEL_BORE_HEAD =
      deferredRegister.registerItem("diamond_tunnel_bore_head", properties ->
          new TunnelBoreHeadItem(Tiers.DIAMOND, "diamond", properties.durability(6000)));

  public static final DeferredItem<BlockItem> FLUID_LOADER =
      blockItem(RailcraftBlocks.FLUID_LOADER);

  public static final DeferredItem<BlockItem> FLUID_UNLOADER =
      blockItem(RailcraftBlocks.FLUID_UNLOADER);

  public static final DeferredItem<BlockItem> ADVANCED_ITEM_LOADER =
      blockItem(RailcraftBlocks.ADVANCED_ITEM_LOADER);

  public static final DeferredItem<BlockItem> ADVANCED_ITEM_UNLOADER =
      blockItem(RailcraftBlocks.ADVANCED_ITEM_UNLOADER);

  public static final DeferredItem<BlockItem> ITEM_LOADER =
      blockItem(RailcraftBlocks.ITEM_LOADER);

  public static final DeferredItem<BlockItem> ITEM_UNLOADER =
      blockItem(RailcraftBlocks.ITEM_UNLOADER);

  public static final DeferredItem<BlockItem> CART_DISPENSER =
      blockItem(RailcraftBlocks.CART_DISPENSER);

  public static final DeferredItem<BlockItem> TRAIN_DISPENSER =
      blockItem(RailcraftBlocks.TRAIN_DISPENSER);

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
          new SpikeMaulItem(11.0F, -3.5F, Tiers.IRON, properties));

  public static final DeferredItem<SpikeMaulItem> STEEL_SPIKE_MAUL =
      deferredRegister.registerItem("steel_spike_maul", properties ->
          new SpikeMaulItem(11.0F, -3.4F, RailcraftItemTier.STEEL, properties));

  public static final DeferredItem<SpikeMaulItem> DIAMOND_SPIKE_MAUL =
      deferredRegister.registerItem("diamond_spike_maul", properties ->
          new SpikeMaulItem(11.0F, -3.3F, Tiers.DIAMOND, properties));

  public static final DeferredItem<BlockItem> SWITCH_TRACK_LEVER =
      blockItem(RailcraftBlocks.SWITCH_TRACK_LEVER);

  public static final DeferredItem<BlockItem> SWITCH_TRACK_MOTOR =
      blockItem(RailcraftBlocks.SWITCH_TRACK_MOTOR);

  public static final DeferredItem<BlockItem> SWITCH_TRACK_ROUTER =
      blockItem(RailcraftBlocks.SWITCH_TRACK_ROUTER);

  public static final DeferredItem<SignalTunerItem> SIGNAL_TUNER =
      deferredRegister.registerItem("signal_tuner", properties ->
          new SignalTunerItem(properties.stacksTo(1)));

  public static final DeferredItem<SignalBlockSurveyorItem> SIGNAL_BLOCK_SURVEYOR =
      deferredRegister.registerItem("signal_block_surveyor", properties ->
          new SignalBlockSurveyorItem(properties.stacksTo(1)));

  public static final DeferredItem<BlockItem> ANALOG_SIGNAL_CONTROLLER_BOX =
      blockItem(RailcraftBlocks.ANALOG_SIGNAL_CONTROLLER_BOX);

  public static final DeferredItem<BlockItem> SIGNAL_SEQUENCER_BOX =
      blockItem(RailcraftBlocks.SIGNAL_SEQUENCER_BOX);

  public static final DeferredItem<BlockItem> SIGNAL_CAPACITOR_BOX =
      blockItem(RailcraftBlocks.SIGNAL_CAPACITOR_BOX);

  public static final DeferredItem<BlockItem> SIGNAL_INTERLOCK_BOX =
      blockItem(RailcraftBlocks.SIGNAL_INTERLOCK_BOX);

  public static final DeferredItem<BlockItem> SIGNAL_BLOCK_RELAY_BOX =
      blockItem(RailcraftBlocks.SIGNAL_BLOCK_RELAY_BOX);

  public static final DeferredItem<BlockItem> SIGNAL_RECEIVER_BOX =
      blockItem(RailcraftBlocks.SIGNAL_RECEIVER_BOX);

  public static final DeferredItem<BlockItem> SIGNAL_CONTROLLER_BOX =
      blockItem(RailcraftBlocks.SIGNAL_CONTROLLER_BOX);

  public static final DeferredItem<BlockItem> DUAL_BLOCK_SIGNAL =
      blockItem(RailcraftBlocks.DUAL_BLOCK_SIGNAL);

  public static final DeferredItem<BlockItem> DUAL_DISTANT_SIGNAL =
      blockItem(RailcraftBlocks.DUAL_DISTANT_SIGNAL);

  public static final DeferredItem<BlockItem> DUAL_TOKEN_SIGNAL =
      blockItem(RailcraftBlocks.DUAL_TOKEN_SIGNAL);

  public static final DeferredItem<BlockItem> BLOCK_SIGNAL =
      blockItem(RailcraftBlocks.BLOCK_SIGNAL);

  public static final DeferredItem<BlockItem> DISTANT_SIGNAL =
      blockItem(RailcraftBlocks.DISTANT_SIGNAL);

  public static final DeferredItem<BlockItem> TOKEN_SIGNAL =
      blockItem(RailcraftBlocks.TOKEN_SIGNAL);

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
      deferredRegister.register("tank_minecart",
          () -> new CartItem(TankMinecart::new, new Item.Properties().stacksTo(1)));

  public static final DeferredItem<CartItem> WORLD_SPIKE_MINECART =
      deferredRegister.register("world_spike_minecart",
          () -> new CartItem(WorldSpikeMinecart::new, new Item.Properties()
              .stacksTo(1)
              .rarity(Rarity.UNCOMMON)));

  public static final DeferredItem<TunnelBoreItem> TUNNEL_BORE =
      deferredRegister.registerItem("tunnel_bore", properties ->
          new TunnelBoreItem(properties
              .rarity(Rarity.UNCOMMON)
              .stacksTo(1)));

  public static final DeferredItem<LocomotiveItem> CREATIVE_LOCOMOTIVE =
      deferredRegister.registerItem("creative_locomotive", properties ->
          new LocomotiveItem(CreativeLocomotive::new, DyeColor.BLACK, DyeColor.MAGENTA,
              properties.stacksTo(1)));

  public static final DeferredItem<LocomotiveItem> ELECTRIC_LOCOMOTIVE =
      deferredRegister.registerItem("electric_locomotive", properties ->
          new LocomotiveItem(ElectricLocomotive::new, DyeColor.YELLOW, DyeColor.BLACK,
              properties.stacksTo(1)));

  public static final DeferredItem<LocomotiveItem> STEAM_LOCOMOTIVE =
      deferredRegister.registerItem("steam_locomotive", properties ->
          new LocomotiveItem(SteamLocomotive::new, DyeColor.LIGHT_GRAY, DyeColor.GRAY,
              properties.stacksTo(1)));

  public static final DeferredItem<Item> WHISTLE_TUNER =
      deferredRegister.registerSimpleItem("whistle_tuner", new Item.Properties().durability(250));

  public static final DeferredItem<GoldenTicketItem> GOLDEN_TICKET =
      deferredRegister.registerItem("golden_ticket", properties ->
          new GoldenTicketItem(properties.rarity(Rarity.UNCOMMON)));

  public static final DeferredItem<TicketItem> TICKET =
      deferredRegister.registerItem("ticket", TicketItem::new);

  public static final DeferredItem<RoutingTableBookItem> ROUTING_TABLE_BOOK =
      deferredRegister.registerItem("routing_table_book", RoutingTableBookItem::new);

  public static final DeferredItem<OverallsItem> OVERALLS =
      deferredRegister.registerItem("overalls", OverallsItem::new);

  public static final DeferredItem<FirestoneOreBlockItem> FIRESTONE_ORE =
      deferredRegister.registerItem("firestone_ore", FirestoneOreBlockItem::new);

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

  public static final DeferredItem<BlockItem> FORCE_TRACK_EMITTER =
      blockItem(RailcraftBlocks.FORCE_TRACK_EMITTER);

  public static final DeferredItem<BlockItem> ABANDONED_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_LOCKING_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_LOCKING_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_BUFFER_STOP_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_BUFFER_STOP_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_ACTIVATOR_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_ACTIVATOR_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_BOOSTER_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_BOOSTER_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_CONTROL_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_CONTROL_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_GATED_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_GATED_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_DETECTOR_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_DETECTOR_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_COUPLER_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_COUPLER_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_EMBARKING_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_EMBARKING_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_DISEMBARKING_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_DISEMBARKING_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_DUMPING_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_DUMPING_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_WYE_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_WYE_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_TURNOUT_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_TURNOUT_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_JUNCTION_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_JUNCTION_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_LAUNCHER_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_LAUNCHER_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_ONE_WAY_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_ONE_WAY_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_WHISTLE_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_WHISTLE_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_LOCOMOTIVE_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_LOCOMOTIVE_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_THROTTLE_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_THROTTLE_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_ROUTING_TRACK =
      blockItem(RailcraftBlocks.ABANDONED_ROUTING_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_LOCKING_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_LOCKING_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_BUFFER_STOP_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_BUFFER_STOP_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_ACTIVATOR_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_ACTIVATOR_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_BOOSTER_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_BOOSTER_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_CONTROL_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_CONTROL_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_GATED_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_GATED_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_DETECTOR_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_DETECTOR_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_COUPLER_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_COUPLER_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_EMBARKING_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_EMBARKING_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_DISEMBARKING_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_DISEMBARKING_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_DUMPING_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_DUMPING_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_WYE_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_WYE_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_TURNOUT_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_TURNOUT_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_JUNCTION_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_JUNCTION_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_LAUNCHER_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_LAUNCHER_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_ONE_WAY_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_ONE_WAY_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_WHISTLE_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_WHISTLE_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_LOCOMOTIVE_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_LOCOMOTIVE_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_THROTTLE_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_THROTTLE_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_ROUTING_TRACK =
      blockItem(RailcraftBlocks.ELECTRIC_ROUTING_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_TRANSITION_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_TRANSITION_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_LOCKING_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_LOCKING_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_ACTIVATOR_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_ACTIVATOR_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_BOOSTER_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_BOOSTER_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_DETECTOR_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_DETECTOR_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_WYE_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_WYE_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_TURNOUT_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_TURNOUT_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_JUNCTION_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_JUNCTION_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_WHISTLE_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_WHISTLE_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_LOCOMOTIVE_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_LOCOMOTIVE_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_THROTTLE_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_THROTTLE_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_ELECTRIC_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_ELECTRIC_TRANSITION_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRANSITION_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_ELECTRIC_LOCKING_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCKING_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_ELECTRIC_BOOSTER_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_BOOSTER_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_ELECTRIC_DETECTOR_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_DETECTOR_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_ELECTRIC_WYE_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_WYE_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_ELECTRIC_TURNOUT_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_TURNOUT_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_ELECTRIC_JUNCTION_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_JUNCTION_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_ELECTRIC_WHISTLE_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_WHISTLE_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_ELECTRIC_LOCOMOTIVE_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCOMOTIVE_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_ELECTRIC_THROTTLE_TRACK =
      blockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_THROTTLE_TRACK);

  public static final DeferredItem<BlockItem> IRON_LOCKING_TRACK =
      blockItem(RailcraftBlocks.IRON_LOCKING_TRACK);

  public static final DeferredItem<BlockItem> IRON_BUFFER_STOP_TRACK =
      blockItem(RailcraftBlocks.IRON_BUFFER_STOP_TRACK);

  public static final DeferredItem<BlockItem> IRON_ACTIVATOR_TRACK =
      blockItem(RailcraftBlocks.IRON_ACTIVATOR_TRACK);

  public static final DeferredItem<BlockItem> IRON_BOOSTER_TRACK =
      blockItem(RailcraftBlocks.IRON_BOOSTER_TRACK);

  public static final DeferredItem<BlockItem> IRON_CONTROL_TRACK =
      blockItem(RailcraftBlocks.IRON_CONTROL_TRACK);

  public static final DeferredItem<BlockItem> IRON_GATED_TRACK =
      blockItem(RailcraftBlocks.IRON_GATED_TRACK);

  public static final DeferredItem<BlockItem> IRON_DETECTOR_TRACK =
      blockItem(RailcraftBlocks.IRON_DETECTOR_TRACK);

  public static final DeferredItem<BlockItem> IRON_COUPLER_TRACK =
      blockItem(RailcraftBlocks.IRON_COUPLER_TRACK);

  public static final DeferredItem<BlockItem> IRON_EMBARKING_TRACK =
      blockItem(RailcraftBlocks.IRON_EMBARKING_TRACK);

  public static final DeferredItem<BlockItem> IRON_DISEMBARKING_TRACK =
      blockItem(RailcraftBlocks.IRON_DISEMBARKING_TRACK);

  public static final DeferredItem<BlockItem> IRON_DUMPING_TRACK =
      blockItem(RailcraftBlocks.IRON_DUMPING_TRACK);

  public static final DeferredItem<BlockItem> IRON_WYE_TRACK =
      blockItem(RailcraftBlocks.IRON_WYE_TRACK);

  public static final DeferredItem<BlockItem> IRON_TURNOUT_TRACK =
      blockItem(RailcraftBlocks.IRON_TURNOUT_TRACK);

  public static final DeferredItem<BlockItem> IRON_JUNCTION_TRACK =
      blockItem(RailcraftBlocks.IRON_JUNCTION_TRACK);

  public static final DeferredItem<BlockItem> IRON_LAUNCHER_TRACK =
      blockItem(RailcraftBlocks.IRON_LAUNCHER_TRACK);

  public static final DeferredItem<BlockItem> IRON_ONE_WAY_TRACK =
      blockItem(RailcraftBlocks.IRON_ONE_WAY_TRACK);

  public static final DeferredItem<BlockItem> IRON_WHISTLE_TRACK =
      blockItem(RailcraftBlocks.IRON_WHISTLE_TRACK);

  public static final DeferredItem<BlockItem> IRON_LOCOMOTIVE_TRACK =
      blockItem(RailcraftBlocks.IRON_LOCOMOTIVE_TRACK);

  public static final DeferredItem<BlockItem> IRON_THROTTLE_TRACK =
      blockItem(RailcraftBlocks.IRON_THROTTLE_TRACK);

  public static final DeferredItem<BlockItem> IRON_ROUTING_TRACK =
      blockItem(RailcraftBlocks.IRON_ROUTING_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_LOCKING_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_LOCKING_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_BUFFER_STOP_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_BUFFER_STOP_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_ACTIVATOR_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_ACTIVATOR_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_BOOSTER_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_BOOSTER_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_CONTROL_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_CONTROL_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_GATED_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_GATED_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_DETECTOR_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_DETECTOR_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_COUPLER_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_COUPLER_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_EMBARKING_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_EMBARKING_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_DISEMBARKING_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_DISEMBARKING_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_DUMPING_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_DUMPING_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_WYE_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_WYE_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_TURNOUT_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_TURNOUT_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_JUNCTION_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_JUNCTION_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_LAUNCHER_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_LAUNCHER_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_ONE_WAY_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_ONE_WAY_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_WHISTLE_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_WHISTLE_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_LOCOMOTIVE_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_LOCOMOTIVE_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_THROTTLE_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_THROTTLE_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_ROUTING_TRACK =
      blockItem(RailcraftBlocks.REINFORCED_ROUTING_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_LOCKING_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_LOCKING_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_BUFFER_STOP_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_BUFFER_STOP_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_ACTIVATOR_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_ACTIVATOR_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_BOOSTER_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_BOOSTER_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_CONTROL_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_CONTROL_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_GATED_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_GATED_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_DETECTOR_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_DETECTOR_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_COUPLER_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_COUPLER_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_EMBARKING_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_EMBARKING_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_DISEMBARKING_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_DISEMBARKING_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_DUMPING_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_DUMPING_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_WYE_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_WYE_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_TURNOUT_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_TURNOUT_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_JUNCTION_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_JUNCTION_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_LAUNCHER_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_LAUNCHER_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_ONE_WAY_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_ONE_WAY_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_WHISTLE_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_WHISTLE_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_LOCOMOTIVE_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_LOCOMOTIVE_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_THROTTLE_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_THROTTLE_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_ROUTING_TRACK =
      blockItem(RailcraftBlocks.STRAP_IRON_ROUTING_TRACK);

  public static final DeferredItem<BlockItem> ELEVATOR_TRACK =
      blockItem(RailcraftBlocks.ELEVATOR_TRACK);

  public static final DeferredItem<CrowbarItem> IRON_CROWBAR =
      deferredRegister.registerItem("iron_crowbar", properties ->
          new CrowbarItem(2.5F, -2.8F, Tiers.IRON, properties));

  public static final DeferredItem<CrowbarItem> STEEL_CROWBAR =
      deferredRegister.registerItem("steel_crowbar", properties ->
          new CrowbarItem(2.5F, -2.7F, RailcraftItemTier.STEEL, properties));

  public static final DeferredItem<CrowbarItem> DIAMOND_CROWBAR =
      deferredRegister.registerItem("diamond_crowbar", properties ->
          new CrowbarItem(2.5F, -2.4F, Tiers.DIAMOND, properties));

  public static final DeferredItem<SeasonsCrowbarItem> SEASONS_CROWBAR =
      deferredRegister.registerItem("seasons_crowbar", SeasonsCrowbarItem::new);

  public static final DeferredItem<Item> TRACK_PARTS = registerBasic("track_parts");

  public static final DeferredItem<TrackKitItem> TRANSITION_TRACK_KIT =
      deferredRegister.register("transition_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED, RailcraftBlocks.HIGH_SPEED_TRANSITION_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
                  RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRANSITION_TRACK)));

  public static final DeferredItem<TrackKitItem> LOCKING_TRACK_KIT =
      deferredRegister.register("locking_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_LOCKING_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_LOCKING_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED, RailcraftBlocks.HIGH_SPEED_LOCKING_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
                  RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCKING_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_LOCKING_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_LOCKING_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON, RailcraftBlocks.STRAP_IRON_LOCKING_TRACK)));

  public static final DeferredItem<TrackKitItem> BUFFER_STOP_TRACK_KIT =
      deferredRegister.register("buffer_stop_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_BUFFER_STOP_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_BUFFER_STOP_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_BUFFER_STOP_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED,
                  RailcraftBlocks.REINFORCED_BUFFER_STOP_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON,
                  RailcraftBlocks.STRAP_IRON_BUFFER_STOP_TRACK)));

  public static final DeferredItem<TrackKitItem> ACTIVATOR_TRACK_KIT =
      deferredRegister.register("activator_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_ACTIVATOR_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_ACTIVATOR_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED, RailcraftBlocks.HIGH_SPEED_ACTIVATOR_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
                  RailcraftBlocks.HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_ACTIVATOR_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_ACTIVATOR_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON,
                  RailcraftBlocks.STRAP_IRON_ACTIVATOR_TRACK)));

  public static final DeferredItem<TrackKitItem> BOOSTER_TRACK_KIT =
      deferredRegister.register("booster_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_BOOSTER_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_BOOSTER_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED, RailcraftBlocks.HIGH_SPEED_BOOSTER_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
                  RailcraftBlocks.HIGH_SPEED_ELECTRIC_BOOSTER_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_BOOSTER_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_BOOSTER_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON, RailcraftBlocks.STRAP_IRON_BOOSTER_TRACK)));

  public static final DeferredItem<TrackKitItem> CONTROL_TRACK_KIT =
      deferredRegister.register("control_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_CONTROL_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_CONTROL_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_CONTROL_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_CONTROL_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON, RailcraftBlocks.STRAP_IRON_CONTROL_TRACK)));

  public static final DeferredItem<TrackKitItem> GATED_TRACK_KIT =
      deferredRegister.register("gated_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_GATED_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_GATED_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_GATED_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_GATED_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON, RailcraftBlocks.STRAP_IRON_GATED_TRACK)));

  public static final DeferredItem<TrackKitItem> DETECTOR_TRACK_KIT =
      deferredRegister.register("detector_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_DETECTOR_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_DETECTOR_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED, RailcraftBlocks.HIGH_SPEED_DETECTOR_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
                  RailcraftBlocks.HIGH_SPEED_ELECTRIC_DETECTOR_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_DETECTOR_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_DETECTOR_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON,
                  RailcraftBlocks.STRAP_IRON_DETECTOR_TRACK)));

  public static final DeferredItem<TrackKitItem> COUPLER_TRACK_KIT =
      deferredRegister.register("coupler_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_COUPLER_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_COUPLER_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_COUPLER_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_COUPLER_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON, RailcraftBlocks.STRAP_IRON_COUPLER_TRACK)));

  public static final DeferredItem<TrackKitItem> EMBARKING_TRACK_KIT =
      deferredRegister.register("embarking_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_EMBARKING_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_EMBARKING_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_EMBARKING_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_EMBARKING_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON,
                  RailcraftBlocks.STRAP_IRON_EMBARKING_TRACK)));

  public static final DeferredItem<TrackKitItem> DISEMBARKING_TRACK_KIT =
      deferredRegister.register("disembarking_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_DISEMBARKING_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_DISEMBARKING_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_DISEMBARKING_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED,
                  RailcraftBlocks.REINFORCED_DISEMBARKING_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON,
                  RailcraftBlocks.STRAP_IRON_DISEMBARKING_TRACK)));

  public static final DeferredItem<TrackKitItem> DUMPING_TRACK_KIT =
      deferredRegister.register("dumping_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_DUMPING_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_DUMPING_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_DUMPING_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED,
                  RailcraftBlocks.REINFORCED_DUMPING_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON,
                  RailcraftBlocks.STRAP_IRON_DUMPING_TRACK)));

  public static final DeferredItem<TrackKitItem> LAUNCHER_TRACK_KIT =
      deferredRegister.register("launcher_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_LAUNCHER_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_LAUNCHER_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_LAUNCHER_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED,
                  RailcraftBlocks.REINFORCED_LAUNCHER_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON,
                  RailcraftBlocks.STRAP_IRON_LAUNCHER_TRACK)));

  public static final DeferredItem<TrackKitItem> ONE_WAY_TRACK_KIT =
      deferredRegister.register("one_way_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_ONE_WAY_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_ONE_WAY_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_ONE_WAY_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED,
                  RailcraftBlocks.REINFORCED_ONE_WAY_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON,
                  RailcraftBlocks.STRAP_IRON_ONE_WAY_TRACK)));

  public static final DeferredItem<TrackKitItem> WHISTLE_TRACK_KIT =
      deferredRegister.register("whistle_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_WHISTLE_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_WHISTLE_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED, RailcraftBlocks.HIGH_SPEED_WHISTLE_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
                  RailcraftBlocks.HIGH_SPEED_ELECTRIC_WHISTLE_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_WHISTLE_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_WHISTLE_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON, RailcraftBlocks.STRAP_IRON_WHISTLE_TRACK)));

  public static final DeferredItem<TrackKitItem> LOCOMOTIVE_TRACK_KIT =
      deferredRegister.register("locomotive_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_LOCOMOTIVE_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_LOCOMOTIVE_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED, RailcraftBlocks.HIGH_SPEED_LOCOMOTIVE_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
                  RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCOMOTIVE_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_LOCOMOTIVE_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_LOCOMOTIVE_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON,
                  RailcraftBlocks.STRAP_IRON_LOCOMOTIVE_TRACK)));

  public static final DeferredItem<TrackKitItem> THROTTLE_TRACK_KIT =
      deferredRegister.register("throttle_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_THROTTLE_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_THROTTLE_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED, RailcraftBlocks.HIGH_SPEED_THROTTLE_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
                  RailcraftBlocks.HIGH_SPEED_ELECTRIC_THROTTLE_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_THROTTLE_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_THROTTLE_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON,
                  RailcraftBlocks.STRAP_IRON_THROTTLE_TRACK)));

  public static final DeferredItem<TrackKitItem> ROUTING_TRACK_KIT =
      deferredRegister.register("routing_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_ROUTING_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_ROUTING_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_ROUTING_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_ROUTING_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON, RailcraftBlocks.STRAP_IRON_ROUTING_TRACK)));

  public static final DeferredItem<GogglesItem> GOGGLES =
      deferredRegister.registerItem("goggles", GogglesItem::new);

  public static final DeferredItem<BlockItem> MANUAL_ROLLING_MACHINE =
      blockItem(RailcraftBlocks.MANUAL_ROLLING_MACHINE);

  public static final DeferredItem<BlockItem> POWERED_ROLLING_MACHINE =
      blockItem(RailcraftBlocks.POWERED_ROLLING_MACHINE);

  public static final DeferredItem<BlockItem> CRUSHER =
      blockItem(RailcraftBlocks.CRUSHER);

  public static final DeferredItem<BlockItem> COKE_OVEN_BRICKS =
      blockItem(RailcraftBlocks.COKE_OVEN_BRICKS);

  public static final DeferredItem<BlockItem> STEAM_OVEN =
      blockItem(RailcraftBlocks.STEAM_OVEN);

  public static final DeferredItem<BlockItem> CRUSHED_OBSIDIAN =
      blockItem(RailcraftBlocks.CRUSHED_OBSIDIAN);

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

  public static final DeferredItem<BlockItem> WATER_TANK_SIDING =
      blockItem(RailcraftBlocks.WATER_TANK_SIDING);

  public static final DeferredItem<BlockItem> QUARRIED_STONE =
      blockItem(RailcraftBlocks.QUARRIED_STONE);

  public static final DeferredItem<BlockItem> QUARRIED_COBBLESTONE =
      blockItem(RailcraftBlocks.QUARRIED_COBBLESTONE);

  public static final DeferredItem<BlockItem> POLISHED_QUARRIED_STONE =
      blockItem(RailcraftBlocks.POLISHED_QUARRIED_STONE);

  public static final DeferredItem<BlockItem> CHISELED_QUARRIED_STONE =
      blockItem(RailcraftBlocks.CHISELED_QUARRIED_STONE);

  public static final DeferredItem<BlockItem> ETCHED_QUARRIED_STONE =
      blockItem(RailcraftBlocks.ETCHED_QUARRIED_STONE);

  public static final DeferredItem<BlockItem> QUARRIED_BRICKS =
      blockItem(RailcraftBlocks.QUARRIED_BRICKS);

  public static final DeferredItem<BlockItem> QUARRIED_BRICK_STAIRS =
      blockItem(RailcraftBlocks.QUARRIED_BRICK_STAIRS);

  public static final DeferredItem<BlockItem> QUARRIED_BRICK_SLAB =
      blockItem(RailcraftBlocks.QUARRIED_BRICK_SLAB);

  public static final DeferredItem<BlockItem> QUARRIED_PAVER =
      blockItem(RailcraftBlocks.QUARRIED_PAVER);

  public static final DeferredItem<BlockItem> QUARRIED_PAVER_STAIRS =
      blockItem(RailcraftBlocks.QUARRIED_PAVER_STAIRS);

  public static final DeferredItem<BlockItem> QUARRIED_PAVER_SLAB =
      blockItem(RailcraftBlocks.QUARRIED_PAVER_SLAB);

  public static final DeferredItem<BlockItem> ABYSSAL_STONE =
      blockItem(RailcraftBlocks.ABYSSAL_STONE);

  public static final DeferredItem<BlockItem> ABYSSAL_COBBLESTONE =
      blockItem(RailcraftBlocks.ABYSSAL_COBBLESTONE);

  public static final DeferredItem<BlockItem> POLISHED_ABYSSAL_STONE =
      blockItem(RailcraftBlocks.POLISHED_ABYSSAL_STONE);

  public static final DeferredItem<BlockItem> CHISELED_ABYSSAL_STONE =
      blockItem(RailcraftBlocks.CHISELED_ABYSSAL_STONE);

  public static final DeferredItem<BlockItem> ETCHED_ABYSSAL_STONE =
      blockItem(RailcraftBlocks.ETCHED_ABYSSAL_STONE);

  public static final DeferredItem<BlockItem> ABYSSAL_BRICKS =
      blockItem(RailcraftBlocks.ABYSSAL_BRICKS);

  public static final DeferredItem<BlockItem> ABYSSAL_BRICK_STAIRS =
      blockItem(RailcraftBlocks.ABYSSAL_BRICK_STAIRS);

  public static final DeferredItem<BlockItem> ABYSSAL_BRICK_SLAB =
      blockItem(RailcraftBlocks.ABYSSAL_BRICK_SLAB);

  public static final DeferredItem<BlockItem> ABYSSAL_PAVER =
      blockItem(RailcraftBlocks.ABYSSAL_PAVER);

  public static final DeferredItem<BlockItem> ABYSSAL_PAVER_STAIRS =
      blockItem(RailcraftBlocks.ABYSSAL_PAVER_STAIRS);

  public static final DeferredItem<BlockItem> ABYSSAL_PAVER_SLAB =
      blockItem(RailcraftBlocks.ABYSSAL_PAVER_SLAB);

  public static final DeferredItem<BucketItem> CREOSOTE_BUCKET =
      deferredRegister.registerItem("creosote_bucket", properties ->
          new BucketItem(RailcraftFluids.CREOSOTE, properties
              .stacksTo(1)
              .craftRemainder(Items.BUCKET)));

  public static final DeferredItem<BlockItem> WORLD_SPIKE =
      blockItem(RailcraftBlocks.WORLD_SPIKE);

  public static final DeferredItem<BlockItem> PERSONAL_WORLD_SPIKE =
      blockItem(RailcraftBlocks.PERSONAL_WORLD_SPIKE);

  public static final DeferredItem<FluidBottleItem> CREOSOTE_BOTTLE =
      deferredRegister.registerItem("creosote_bottle", properties ->
          new FluidBottleItem(RailcraftFluids.CREOSOTE, properties
              .stacksTo(16)
              .craftRemainder(Items.GLASS_BOTTLE)));

  private static DeferredItem<Item> registerBasic(String name) {
    return deferredRegister.registerSimpleItem(name);
  }

  private static DeferredItem<BlockItem> blockItem(DeferredBlock<? extends Block> block) {
    return deferredRegister.registerSimpleBlockItem(block);
  }
}
