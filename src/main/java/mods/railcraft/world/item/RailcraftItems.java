package mods.railcraft.world.item;

import java.util.Collection;
import java.util.function.Function;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.util.VariantSet;
import mods.railcraft.world.entity.vehicle.TankMinecart;
import mods.railcraft.world.entity.vehicle.locomotive.CreativeLocomotive;
import mods.railcraft.world.entity.vehicle.locomotive.ElectricLocomotive;
import mods.railcraft.world.entity.vehicle.locomotive.SteamLocomotive;
import mods.railcraft.world.item.tunnelbore.BronzeTunnelBoreHeadItem;
import mods.railcraft.world.item.tunnelbore.DiamondTunnelBoreHeadItem;
import mods.railcraft.world.item.tunnelbore.IronTunnelBoreHeadItem;
import mods.railcraft.world.item.tunnelbore.SteelTunnelBoreHeadItem;
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
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
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
      deferredRegister.register("low_pressure_steam_boiler_tank",
          () -> new PressureBoilerTankBlockItem(
              RailcraftBlocks.LOW_PRESSURE_STEAM_BOILER_TANK.get(),
              new Item.Properties()));

  public static final DeferredItem<PressureBoilerTankBlockItem> HIGH_PRESSURE_STEAM_BOILER_TANK =
      deferredRegister.register("high_pressure_steam_boiler_tank",
          () -> new PressureBoilerTankBlockItem(
              RailcraftBlocks.HIGH_PRESSURE_STEAM_BOILER_TANK.get(),
              new Item.Properties()));

  public static final DeferredItem<FueledFireboxBlockItem> SOLID_FUELED_FIREBOX =
      deferredRegister.register("solid_fueled_firebox",
          () -> new FueledFireboxBlockItem(RailcraftBlocks.SOLID_FUELED_FIREBOX.get(),
              new Item.Properties()));

  public static final DeferredItem<FueledFireboxBlockItem> FLUID_FUELED_FIREBOX =
      deferredRegister.register("fluid_fueled_firebox",
          () -> new FueledFireboxBlockItem(RailcraftBlocks.FLUID_FUELED_FIREBOX.get(),
              new Item.Properties()));

  public static final DeferredItem<Item> SIGNAL_LABEL =
      deferredRegister.register("signal_label",
          () -> new SignalLabelItem(new Item.Properties()));

  public static final DeferredItem<Item> TURBINE_BLADE = deferredRegister.registerSimpleItem("turbine_blade");

  public static final DeferredItem<Item> TURBINE_DISK = deferredRegister.registerSimpleItem("turbine_disk");

  public static final DeferredItem<Item> TURBINE_ROTOR =
      deferredRegister.register("turbine_rotor",
          () -> new TurbineRotorItem(new Item.Properties().stacksTo(1)));

  public static final DeferredItem<BlockItem> STEAM_TURBINE =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.STEAM_TURBINE);

  public static final DeferredItem<BlockItem> BLAST_FURNACE_BRICKS =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.BLAST_FURNACE_BRICKS);

  public static final DeferredItem<BlockItem> FEED_STATION =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.FEED_STATION);

  public static final DeferredItem<BlockItem> LOGBOOK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.LOGBOOK);

  public static final DeferredItem<BlockItem> FRAME_BLOCK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.FRAME);

  public static final DeferredItem<Item> CHARGE_METER =
      deferredRegister.register("charge_meter",
          () -> new ChargeMeterItem(new Item.Properties()
              .durability(0)
              .stacksTo(1)));

  public static final DeferredItem<BlockItem> NICKEL_ZINC_BATTERY =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.NICKEL_ZINC_BATTERY);

  public static final DeferredItem<BlockItem> NICKEL_IRON_BATTERY =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.NICKEL_IRON_BATTERY);

  public static final DeferredItem<BlockItem> ZINC_CARBON_BATTERY =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ZINC_CARBON_BATTERY);

  public static final DeferredItem<BlockItem> ZINC_CARBON_BATTERY_EMPTY =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ZINC_CARBON_BATTERY_EMPTY);

  public static final DeferredItem<BlockItem> ZINC_SILVER_BATTERY =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ZINC_SILVER_BATTERY);

  public static final DeferredItem<BlockItem> ZINC_SILVER_BATTERY_EMPTY =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ZINC_SILVER_BATTERY_EMPTY);

  public static final DeferredItem<BlockItem> STEEL_ANVIL =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.STEEL_ANVIL);

  public static final DeferredItem<BlockItem> CHIPPED_STEEL_ANVIL =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.CHIPPED_STEEL_ANVIL);

  public static final DeferredItem<BlockItem> DAMAGED_STEEL_ANVIL =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.DAMAGED_STEEL_ANVIL);

  public static final DeferredItem<BlockItem> STEEL_BLOCK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.STEEL_BLOCK);

  public static final DeferredItem<BlockItem> BRASS_BLOCK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.BRASS_BLOCK);

  public static final DeferredItem<BlockItem> BRONZE_BLOCK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.BRONZE_BLOCK);

  public static final DeferredItem<BlockItem> INVAR_BLOCK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.INVAR_BLOCK);

  public static final DeferredItem<BlockItem> LEAD_BLOCK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.LEAD_BLOCK);

  public static final DeferredItem<BlockItem> NICKEL_BLOCK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.NICKEL_BLOCK);

  public static final DeferredItem<BlockItem> SILVER_BLOCK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.SILVER_BLOCK);

  public static final DeferredItem<BlockItem> TIN_BLOCK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.TIN_BLOCK);

  public static final DeferredItem<BlockItem> ZINC_BLOCK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ZINC_BLOCK);

  public static final DeferredItem<BlockItem> LEAD_ORE =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.LEAD_ORE);

  public static final DeferredItem<BlockItem> NICKEL_ORE =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.NICKEL_ORE);

  public static final DeferredItem<BlockItem> SILVER_ORE =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.SILVER_ORE);

  public static final DeferredItem<BlockItem> TIN_ORE =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.TIN_ORE);

  public static final DeferredItem<BlockItem> ZINC_ORE =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ZINC_ORE);

  public static final DeferredItem<BlockItem> DEEPSLATE_LEAD_ORE =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.DEEPSLATE_LEAD_ORE);

  public static final DeferredItem<BlockItem> DEEPSLATE_NICKEL_ORE =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.DEEPSLATE_NICKEL_ORE);

  public static final DeferredItem<BlockItem> DEEPSLATE_SILVER_ORE =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.DEEPSLATE_SILVER_ORE);

  public static final DeferredItem<BlockItem> DEEPSLATE_TIN_ORE =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.DEEPSLATE_TIN_ORE);

  public static final DeferredItem<BlockItem> DEEPSLATE_ZINC_ORE =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.DEEPSLATE_ZINC_ORE);

  public static final DeferredItem<BlockItem> SULFUR_ORE =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.SULFUR_ORE);

  public static final DeferredItem<BlockItem> DEEPSLATE_SULFUR_ORE =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.DEEPSLATE_SULFUR_ORE);

  public static final DeferredItem<BlockItem> SALTPETER_ORE =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.SALTPETER_ORE);

  public static final DeferredItem<BlockItem> COKE_BLOCK =
      deferredRegister.register("coal_coke_block",
          () -> new CoalCokeBlockItem(new Properties()));

  public static final DeferredItem<Item> STEEL_SHEARS =
      deferredRegister.register("steel_shears",
          () -> new ShearsItem(new Item.Properties()
              .durability(500)));

  public static final DeferredItem<Item> STEEL_SWORD =
      deferredRegister.register("steel_sword",
          () -> new SwordItem(RailcraftItemTier.STEEL, 3, -2.4F,
              new Item.Properties()));

  public static final DeferredItem<Item> STEEL_SHOVEL =
      deferredRegister.register("steel_shovel",
          () -> new ShovelItem(RailcraftItemTier.STEEL, 1.5F, -3.0F,
              new Item.Properties()));

  public static final DeferredItem<Item> STEEL_PICKAXE =
      deferredRegister.register("steel_pickaxe",
          () -> new PickaxeItem(RailcraftItemTier.STEEL, 1, -2.8F,
              new Item.Properties()));

  public static final DeferredItem<Item> STEEL_AXE =
      deferredRegister.register("steel_axe",
          () -> new AxeItem(RailcraftItemTier.STEEL, 8.0F, -3F,
              new Item.Properties()));

  public static final DeferredItem<Item> STEEL_HOE =
      deferredRegister.register("steel_hoe",
          () -> new HoeItem(RailcraftItemTier.STEEL, -2, -0.5F,
              new Item.Properties()));

  public static final DeferredItem<Item> STEEL_BOOTS =
      deferredRegister.register("steel_boots",
          () -> new ArmorItem(RailcraftArmorMaterial.STEEL, ArmorItem.Type.BOOTS,
              new Item.Properties()));

  public static final DeferredItem<Item> STEEL_CHESTPLATE =
      deferredRegister.register("steel_chestplate",
          () -> new ArmorItem(RailcraftArmorMaterial.STEEL, ArmorItem.Type.CHESTPLATE,
              new Item.Properties()));

  public static final DeferredItem<Item> STEEL_HELMET =
      deferredRegister.register("steel_helmet",
          () -> new ArmorItem(RailcraftArmorMaterial.STEEL, ArmorItem.Type.HELMET,
              new Item.Properties()));

  public static final DeferredItem<Item> STEEL_LEGGINGS =
      deferredRegister.register("steel_leggings",
          () -> new ArmorItem(RailcraftArmorMaterial.STEEL, ArmorItem.Type.LEGGINGS,
              new Item.Properties()));

  public static final DeferredItem<Item> IRON_TUNNEL_BORE_HEAD =
      deferredRegister.register("iron_tunnel_bore_head",
          () -> new IronTunnelBoreHeadItem(new Item.Properties()
              .durability(1500)));

  public static final DeferredItem<Item> BRONZE_TUNNEL_BORE_HEAD =
      deferredRegister.register("bronze_tunnel_bore_head",
          () -> new BronzeTunnelBoreHeadItem(new Item.Properties()
              .durability(1200)));

  public static final DeferredItem<Item> STEEL_TUNNEL_BORE_HEAD =
      deferredRegister.register("steel_tunnel_bore_head",
          () -> new SteelTunnelBoreHeadItem(
              new Item.Properties()
                  .durability(3000)));

  public static final DeferredItem<Item> DIAMOND_TUNNEL_BORE_HEAD =
      deferredRegister.register("diamond_tunnel_bore_head",
          () -> new DiamondTunnelBoreHeadItem(new Item.Properties()
              .durability(6000)));

  public static final DeferredItem<Item> TANK_MINECART =
      deferredRegister.register("tank_minecart",
          () -> new CartItem(TankMinecart::new,
              new Item.Properties()
                  .stacksTo(1)));

  public static final DeferredItem<BlockItem> FLUID_LOADER =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.FLUID_LOADER);

  public static final DeferredItem<BlockItem> FLUID_UNLOADER =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.FLUID_UNLOADER);

  public static final DeferredItem<BlockItem> ADVANCED_ITEM_LOADER =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ADVANCED_ITEM_LOADER);

  public static final DeferredItem<BlockItem> ADVANCED_ITEM_UNLOADER =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ADVANCED_ITEM_UNLOADER);

  public static final DeferredItem<BlockItem> ITEM_LOADER =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ITEM_LOADER);

  public static final DeferredItem<BlockItem> ITEM_UNLOADER =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ITEM_UNLOADER);

  public static final DeferredItem<BlockItem> CART_DISPENSER =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.CART_DISPENSER);

  public static final DeferredItem<BlockItem> TRAIN_DISPENSER =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.TRAIN_DISPENSER);

  public static final DeferredItem<Item> IRON_SPIKE_MAUL =
      deferredRegister.register("iron_spike_maul",
          () -> new SpikeMaulItem(11.0F, -3.5F, Tiers.IRON,
              new Item.Properties()));

  public static final DeferredItem<Item> STEEL_SPIKE_MAUL =
      deferredRegister.register("steel_spike_maul",
          () -> new SpikeMaulItem(11.0F, -3.4F, RailcraftItemTier.STEEL,
              new Item.Properties()));

  public static final DeferredItem<Item> DIAMOND_SPIKE_MAUL =
      deferredRegister.register("diamond_spike_maul",
          () -> new SpikeMaulItem(11.0F, -3.3F, Tiers.DIAMOND,
              new Item.Properties()));

  public static final DeferredItem<BlockItem> SWITCH_TRACK_LEVER =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.SWITCH_TRACK_LEVER);

  public static final DeferredItem<BlockItem> SWITCH_TRACK_MOTOR =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.SWITCH_TRACK_MOTOR);

  public static final DeferredItem<BlockItem> SWITCH_TRACK_ROUTER =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.SWITCH_TRACK_ROUTER);

  public static final DeferredItem<Item> SIGNAL_TUNER =
      deferredRegister.register("signal_tuner",
          () -> new SignalTunerItem(new Item.Properties().stacksTo(1)));

  public static final DeferredItem<Item> SIGNAL_BLOCK_SURVEYOR =
      deferredRegister.register("signal_block_surveyor",
          () -> new SignalBlockSurveyorItem(new Item.Properties().stacksTo(1)));

  public static final DeferredItem<BlockItem> ANALOG_SIGNAL_CONTROLLER_BOX =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ANALOG_SIGNAL_CONTROLLER_BOX);

  public static final DeferredItem<BlockItem> SIGNAL_SEQUENCER_BOX =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.SIGNAL_SEQUENCER_BOX);

  public static final DeferredItem<BlockItem> SIGNAL_CAPACITOR_BOX =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.SIGNAL_CAPACITOR_BOX);

  public static final DeferredItem<BlockItem> SIGNAL_INTERLOCK_BOX =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.SIGNAL_INTERLOCK_BOX);

  public static final DeferredItem<BlockItem> SIGNAL_BLOCK_RELAY_BOX =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.SIGNAL_BLOCK_RELAY_BOX);

  public static final DeferredItem<BlockItem> SIGNAL_RECEIVER_BOX =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.SIGNAL_RECEIVER_BOX);

  public static final DeferredItem<BlockItem> SIGNAL_CONTROLLER_BOX =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.SIGNAL_CONTROLLER_BOX);

  public static final DeferredItem<BlockItem> DUAL_BLOCK_SIGNAL =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.DUAL_BLOCK_SIGNAL);

  public static final DeferredItem<BlockItem> DUAL_DISTANT_SIGNAL =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.DUAL_DISTANT_SIGNAL);

  public static final DeferredItem<BlockItem> DUAL_TOKEN_SIGNAL =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.DUAL_TOKEN_SIGNAL);

  public static final DeferredItem<BlockItem> BLOCK_SIGNAL =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.BLOCK_SIGNAL);

  public static final DeferredItem<BlockItem> DISTANT_SIGNAL =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.DISTANT_SIGNAL);

  public static final DeferredItem<BlockItem> TOKEN_SIGNAL =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.TOKEN_SIGNAL);

  public static final DeferredItem<TrackRemoverCartItem> TRACK_REMOVER =
      deferredRegister.register("track_remover",
          () -> new TrackRemoverCartItem(new Item.Properties()
              .rarity(Rarity.UNCOMMON)
              .stacksTo(1)));

  public static final DeferredItem<TrackLayerCartItem> TRACK_LAYER =
      deferredRegister.register("track_layer",
          () -> new TrackLayerCartItem(new Item.Properties()
              .rarity(Rarity.UNCOMMON)
              .stacksTo(1)));

  public static final DeferredItem<TrackRelayerCartItem> TRACK_RELAYER =
      deferredRegister.register("track_relayer",
          () -> new TrackRelayerCartItem(new Item.Properties()
              .rarity(Rarity.UNCOMMON)
              .stacksTo(1)));

  public static final DeferredItem<TrackUndercutterCartItem> TRACK_UNDERCUTTER =
      deferredRegister.register("track_undercutter",
          () -> new TrackUndercutterCartItem(new Item.Properties()
              .rarity(Rarity.UNCOMMON)
              .stacksTo(1)));

  public static final DeferredItem<Item> TUNNEL_BORE =
      deferredRegister.register("tunnel_bore",
          () -> new TunnelBoreItem(new Item.Properties()
              .rarity(Rarity.UNCOMMON)
              .stacksTo(1)));

  public static final DeferredItem<Item> CREATIVE_LOCOMOTIVE =
      deferredRegister.register("creative_locomotive",
          () -> new LocomotiveItem(CreativeLocomotive::new,
              DyeColor.BLACK, DyeColor.MAGENTA,
              new Item.Properties()
                  .stacksTo(1)));

  public static final DeferredItem<Item> ELECTRIC_LOCOMOTIVE =
      deferredRegister.register("electric_locomotive",
          () -> new LocomotiveItem(ElectricLocomotive::new,
              DyeColor.YELLOW, DyeColor.BLACK,
              new Item.Properties()
                  .stacksTo(1)));

  public static final DeferredItem<Item> STEAM_LOCOMOTIVE =
      deferredRegister.register("steam_locomotive",
          () -> new LocomotiveItem(SteamLocomotive::new,
              DyeColor.LIGHT_GRAY, DyeColor.GRAY,
              new Item.Properties()
                  .stacksTo(1)));

  public static final DeferredItem<Item> WHISTLE_TUNER =
      deferredRegister.register("whistle_tuner",
          () -> new Item(new Item.Properties().durability(250)));

  public static final DeferredItem<Item> GOLDEN_TICKET =
      deferredRegister.register("golden_ticket",
          () -> new GoldenTicketItem(new Item.Properties()
              .rarity(Rarity.UNCOMMON)));

  public static final DeferredItem<Item> TICKET =
      deferredRegister.register("ticket",
          () -> new TicketItem(new Item.Properties()));

  public static final DeferredItem<Item> ROUTING_TABLE_BOOK =
      deferredRegister.register("routing_table_book",
          () -> new RoutingTableBookItem(new Item.Properties()));

  public static final DeferredItem<Item> OVERALLS =
      deferredRegister.register("overalls",
          () -> new OverallsItem(new Item.Properties()));

  public static final DeferredItem<FirestoneOreBlockItem> FIRESTONE_ORE =
      deferredRegister.register("firestone_ore", () -> new FirestoneOreBlockItem(
          new Item.Properties()));

  public static final DeferredItem<FirestoneItem> RAW_FIRESTONE =
      deferredRegister.register("raw_firestone",
          () -> new FirestoneItem(true,
              new Item.Properties().rarity(Rarity.RARE)));

  public static final DeferredItem<RefinedFirestoneItem> REFINED_FIRESTONE =
      deferredRegister.register("refined_firestone",
          () -> new RefinedFirestoneItem(false, new Item.Properties()
              .stacksTo(1)
              .durability(RefinedFirestoneItem.CHARGES)
              .rarity(Rarity.RARE)));

  public static final DeferredItem<CrackedFirestoneItem> CRACKED_FIRESTONE =
      deferredRegister.register("cracked_firestone",
          () -> new CrackedFirestoneItem(new Item.Properties()
              .stacksTo(1)
              .durability(RefinedFirestoneItem.CHARGES)
              .rarity(Rarity.RARE)));

  public static final DeferredItem<FirestoneItem> CUT_FIRESTONE =
      deferredRegister.register("cut_firestone",
          () -> new FirestoneItem(true,
              new Item.Properties()
                  .stacksTo(1)
                  .durability(RefinedFirestoneItem.CHARGES)
                  .rarity(Rarity.RARE)));

  public static final DeferredItem<BlockItem> FORCE_TRACK_EMITTER =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.FORCE_TRACK_EMITTER);

  public static final DeferredItem<BlockItem> ABANDONED_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ABANDONED_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_LOCKING_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ABANDONED_LOCKING_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_BUFFER_STOP_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ABANDONED_BUFFER_STOP_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_ACTIVATOR_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ABANDONED_ACTIVATOR_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_BOOSTER_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ABANDONED_BOOSTER_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_CONTROL_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ABANDONED_CONTROL_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_GATED_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ABANDONED_GATED_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_DETECTOR_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ABANDONED_DETECTOR_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_COUPLER_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ABANDONED_COUPLER_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_EMBARKING_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ABANDONED_EMBARKING_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_DISEMBARKING_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ABANDONED_DISEMBARKING_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_DUMPING_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ABANDONED_DUMPING_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_WYE_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ABANDONED_WYE_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_TURNOUT_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ABANDONED_TURNOUT_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_JUNCTION_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ABANDONED_JUNCTION_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_LAUNCHER_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ABANDONED_LAUNCHER_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_ONE_WAY_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ABANDONED_ONE_WAY_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_WHISTLE_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ABANDONED_WHISTLE_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_LOCOMOTIVE_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ABANDONED_LOCOMOTIVE_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_THROTTLE_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ABANDONED_THROTTLE_TRACK);

  public static final DeferredItem<BlockItem> ABANDONED_ROUTING_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ABANDONED_ROUTING_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ELECTRIC_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_LOCKING_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ELECTRIC_LOCKING_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_BUFFER_STOP_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ELECTRIC_BUFFER_STOP_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_ACTIVATOR_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ELECTRIC_ACTIVATOR_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_BOOSTER_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ELECTRIC_BOOSTER_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_CONTROL_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ELECTRIC_CONTROL_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_GATED_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ELECTRIC_GATED_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_DETECTOR_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ELECTRIC_DETECTOR_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_COUPLER_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ELECTRIC_COUPLER_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_EMBARKING_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ELECTRIC_EMBARKING_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_DISEMBARKING_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ELECTRIC_DISEMBARKING_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_DUMPING_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ELECTRIC_DUMPING_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_WYE_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ELECTRIC_WYE_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_TURNOUT_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ELECTRIC_TURNOUT_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_JUNCTION_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ELECTRIC_JUNCTION_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_LAUNCHER_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ELECTRIC_LAUNCHER_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_ONE_WAY_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ELECTRIC_ONE_WAY_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_WHISTLE_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ELECTRIC_WHISTLE_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_LOCOMOTIVE_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ELECTRIC_LOCOMOTIVE_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_THROTTLE_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ELECTRIC_THROTTLE_TRACK);

  public static final DeferredItem<BlockItem> ELECTRIC_ROUTING_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ELECTRIC_ROUTING_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.HIGH_SPEED_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_TRANSITION_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.HIGH_SPEED_TRANSITION_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_LOCKING_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.HIGH_SPEED_LOCKING_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_ACTIVATOR_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.HIGH_SPEED_ACTIVATOR_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_BOOSTER_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.HIGH_SPEED_BOOSTER_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_DETECTOR_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.HIGH_SPEED_DETECTOR_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_WYE_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.HIGH_SPEED_WYE_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_TURNOUT_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.HIGH_SPEED_TURNOUT_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_JUNCTION_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.HIGH_SPEED_JUNCTION_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_WHISTLE_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.HIGH_SPEED_WHISTLE_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_LOCOMOTIVE_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.HIGH_SPEED_LOCOMOTIVE_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_THROTTLE_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.HIGH_SPEED_THROTTLE_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_ELECTRIC_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_ELECTRIC_TRANSITION_TRACK =
      deferredRegister.registerSimpleBlockItem(
          RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRANSITION_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_ELECTRIC_LOCKING_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCKING_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_ELECTRIC_BOOSTER_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_BOOSTER_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_ELECTRIC_DETECTOR_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_DETECTOR_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_ELECTRIC_WYE_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_WYE_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_ELECTRIC_TURNOUT_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_TURNOUT_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_ELECTRIC_JUNCTION_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_JUNCTION_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_ELECTRIC_WHISTLE_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_WHISTLE_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_ELECTRIC_LOCOMOTIVE_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCOMOTIVE_TRACK);

  public static final DeferredItem<BlockItem> HIGH_SPEED_ELECTRIC_THROTTLE_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_THROTTLE_TRACK);

  public static final DeferredItem<BlockItem> IRON_LOCKING_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.IRON_LOCKING_TRACK);

  public static final DeferredItem<BlockItem> IRON_BUFFER_STOP_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.IRON_BUFFER_STOP_TRACK);

  public static final DeferredItem<BlockItem> IRON_ACTIVATOR_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.IRON_ACTIVATOR_TRACK);

  public static final DeferredItem<BlockItem> IRON_BOOSTER_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.IRON_BOOSTER_TRACK);

  public static final DeferredItem<BlockItem> IRON_CONTROL_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.IRON_CONTROL_TRACK);

  public static final DeferredItem<BlockItem> IRON_GATED_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.IRON_GATED_TRACK);

  public static final DeferredItem<BlockItem> IRON_DETECTOR_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.IRON_DETECTOR_TRACK);

  public static final DeferredItem<BlockItem> IRON_COUPLER_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.IRON_COUPLER_TRACK);

  public static final DeferredItem<BlockItem> IRON_EMBARKING_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.IRON_EMBARKING_TRACK);

  public static final DeferredItem<BlockItem> IRON_DISEMBARKING_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.IRON_DISEMBARKING_TRACK);

  public static final DeferredItem<BlockItem> IRON_DUMPING_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.IRON_DUMPING_TRACK);

  public static final DeferredItem<BlockItem> IRON_WYE_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.IRON_WYE_TRACK);

  public static final DeferredItem<BlockItem> IRON_TURNOUT_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.IRON_TURNOUT_TRACK);

  public static final DeferredItem<BlockItem> IRON_JUNCTION_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.IRON_JUNCTION_TRACK);

  public static final DeferredItem<BlockItem> IRON_LAUNCHER_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.IRON_LAUNCHER_TRACK);

  public static final DeferredItem<BlockItem> IRON_ONE_WAY_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.IRON_ONE_WAY_TRACK);

  public static final DeferredItem<BlockItem> IRON_WHISTLE_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.IRON_WHISTLE_TRACK);

  public static final DeferredItem<BlockItem> IRON_LOCOMOTIVE_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.IRON_LOCOMOTIVE_TRACK);

  public static final DeferredItem<BlockItem> IRON_THROTTLE_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.IRON_THROTTLE_TRACK);

  public static final DeferredItem<BlockItem> IRON_ROUTING_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.IRON_ROUTING_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.REINFORCED_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_LOCKING_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.REINFORCED_LOCKING_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_BUFFER_STOP_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.REINFORCED_BUFFER_STOP_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_ACTIVATOR_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.REINFORCED_ACTIVATOR_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_BOOSTER_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.REINFORCED_BOOSTER_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_CONTROL_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.REINFORCED_CONTROL_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_GATED_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.REINFORCED_GATED_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_DETECTOR_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.REINFORCED_DETECTOR_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_COUPLER_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.REINFORCED_COUPLER_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_EMBARKING_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.REINFORCED_EMBARKING_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_DISEMBARKING_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.REINFORCED_DISEMBARKING_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_DUMPING_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.REINFORCED_DUMPING_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_WYE_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.REINFORCED_WYE_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_TURNOUT_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.REINFORCED_TURNOUT_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_JUNCTION_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.REINFORCED_JUNCTION_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_LAUNCHER_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.REINFORCED_LAUNCHER_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_ONE_WAY_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.REINFORCED_ONE_WAY_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_WHISTLE_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.REINFORCED_WHISTLE_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_LOCOMOTIVE_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.REINFORCED_LOCOMOTIVE_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_THROTTLE_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.REINFORCED_THROTTLE_TRACK);

  public static final DeferredItem<BlockItem> REINFORCED_ROUTING_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.REINFORCED_ROUTING_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.STRAP_IRON_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_LOCKING_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.STRAP_IRON_LOCKING_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_BUFFER_STOP_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.STRAP_IRON_BUFFER_STOP_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_ACTIVATOR_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.STRAP_IRON_ACTIVATOR_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_BOOSTER_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.STRAP_IRON_BOOSTER_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_CONTROL_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.STRAP_IRON_CONTROL_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_GATED_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.STRAP_IRON_GATED_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_DETECTOR_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.STRAP_IRON_DETECTOR_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_COUPLER_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.STRAP_IRON_COUPLER_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_EMBARKING_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.STRAP_IRON_EMBARKING_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_DISEMBARKING_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.STRAP_IRON_DISEMBARKING_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_DUMPING_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.STRAP_IRON_DUMPING_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_WYE_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.STRAP_IRON_WYE_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_TURNOUT_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.STRAP_IRON_TURNOUT_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_JUNCTION_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.STRAP_IRON_JUNCTION_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_LAUNCHER_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.STRAP_IRON_LAUNCHER_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_ONE_WAY_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.STRAP_IRON_ONE_WAY_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_WHISTLE_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.STRAP_IRON_WHISTLE_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_LOCOMOTIVE_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.STRAP_IRON_LOCOMOTIVE_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_THROTTLE_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.STRAP_IRON_THROTTLE_TRACK);

  public static final DeferredItem<BlockItem> STRAP_IRON_ROUTING_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.STRAP_IRON_ROUTING_TRACK);

  public static final DeferredItem<BlockItem> ELEVATOR_TRACK =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ELEVATOR_TRACK);

  public static final DeferredItem<Item> IRON_CROWBAR =
      deferredRegister.register("iron_crowbar",
          () -> new CrowbarItem(2.5F, -2.8F, Tiers.IRON,
              new Item.Properties()));

  public static final DeferredItem<Item> STEEL_CROWBAR =
      deferredRegister.register("steel_crowbar",
          () -> new CrowbarItem(2.5F, -2.7F, RailcraftItemTier.STEEL,
              new Item.Properties()));

  public static final DeferredItem<Item> DIAMOND_CROWBAR =
      deferredRegister.register("diamond_crowbar",
          () -> new CrowbarItem(2.5F, -2.4F, Tiers.DIAMOND,
              new Item.Properties()));

  public static final DeferredItem<Item> SEASONS_CROWBAR =
      deferredRegister.register("seasons_crowbar",
          () -> new SeasonsCrowbarItem(new Item.Properties()));

  public static final DeferredItem<Item> TRACK_PARTS =
      deferredRegister.registerSimpleItem("track_parts");

  public static final DeferredItem<Item> TRANSITION_TRACK_KIT =
      deferredRegister.register("transition_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED, RailcraftBlocks.HIGH_SPEED_TRANSITION_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
                  RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRANSITION_TRACK)));

  public static final DeferredItem<Item> LOCKING_TRACK_KIT =
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

  public static final DeferredItem<Item> BUFFER_STOP_TRACK_KIT =
      deferredRegister.register("buffer_stop_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_BUFFER_STOP_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_BUFFER_STOP_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_BUFFER_STOP_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED,
                  RailcraftBlocks.REINFORCED_BUFFER_STOP_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON,
                  RailcraftBlocks.STRAP_IRON_BUFFER_STOP_TRACK)));

  public static final DeferredItem<Item> ACTIVATOR_TRACK_KIT =
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

  public static final DeferredItem<Item> BOOSTER_TRACK_KIT =
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

  public static final DeferredItem<Item> CONTROL_TRACK_KIT =
      deferredRegister.register("control_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_CONTROL_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_CONTROL_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_CONTROL_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_CONTROL_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON, RailcraftBlocks.STRAP_IRON_CONTROL_TRACK)));

  public static final DeferredItem<Item> GATED_TRACK_KIT =
      deferredRegister.register("gated_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_GATED_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_GATED_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_GATED_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_GATED_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON, RailcraftBlocks.STRAP_IRON_GATED_TRACK)));

  public static final DeferredItem<Item> DETECTOR_TRACK_KIT =
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

  public static final DeferredItem<Item> COUPLER_TRACK_KIT =
      deferredRegister.register("coupler_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_COUPLER_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_COUPLER_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_COUPLER_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_COUPLER_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON, RailcraftBlocks.STRAP_IRON_COUPLER_TRACK)));

  public static final DeferredItem<Item> EMBARKING_TRACK_KIT =
      deferredRegister.register("embarking_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_EMBARKING_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_EMBARKING_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_EMBARKING_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_EMBARKING_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON,
                  RailcraftBlocks.STRAP_IRON_EMBARKING_TRACK)));

  public static final DeferredItem<Item> DISEMBARKING_TRACK_KIT =
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

  public static final DeferredItem<Item> DUMPING_TRACK_KIT =
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

  public static final DeferredItem<Item> LAUNCHER_TRACK_KIT =
      deferredRegister.register("launcher_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_LAUNCHER_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_LAUNCHER_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_LAUNCHER_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED,
                  RailcraftBlocks.REINFORCED_LAUNCHER_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON,
                  RailcraftBlocks.STRAP_IRON_LAUNCHER_TRACK)));

  public static final DeferredItem<Item> ONE_WAY_TRACK_KIT =
      deferredRegister.register("one_way_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_ONE_WAY_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_ONE_WAY_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_ONE_WAY_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED,
                  RailcraftBlocks.REINFORCED_ONE_WAY_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON,
                  RailcraftBlocks.STRAP_IRON_ONE_WAY_TRACK)));

  public static final DeferredItem<Item> WHISTLE_TRACK_KIT =
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

  public static final DeferredItem<Item> LOCOMOTIVE_TRACK_KIT =
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

  public static final DeferredItem<Item> THROTTLE_TRACK_KIT =
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

  public static final DeferredItem<Item> ROUTING_TRACK_KIT =
      deferredRegister.register("routing_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_ROUTING_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_ROUTING_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_ROUTING_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_ROUTING_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON, RailcraftBlocks.STRAP_IRON_ROUTING_TRACK)));

  public static final DeferredItem<Item> GOGGLES =
      deferredRegister.register("goggles",
          () -> new GogglesItem(new Item.Properties()));

  public static final DeferredItem<BlockItem> MANUAL_ROLLING_MACHINE =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.MANUAL_ROLLING_MACHINE);

  public static final DeferredItem<BlockItem> POWERED_ROLLING_MACHINE =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.POWERED_ROLLING_MACHINE);

  public static final DeferredItem<BlockItem> CRUSHER =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.CRUSHER);

  public static final DeferredItem<BlockItem> COKE_OVEN_BRICKS =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.COKE_OVEN_BRICKS);

  public static final DeferredItem<BlockItem> STEAM_OVEN =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.STEAM_OVEN);

  public static final DeferredItem<BlockItem> CRUSHED_OBSIDIAN =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.CRUSHED_OBSIDIAN);

  // ================================================================================
  // Crafting Materials
  // ================================================================================

  public static final DeferredItem<Item> COAL_COKE =
      deferredRegister.register("coal_coke", () -> new CoalCokeItem(new Item.Properties()));

  public static final DeferredItem<Item> STEEL_PLATE =
      deferredRegister.registerSimpleItem("steel_plate");
  public static final DeferredItem<Item> IRON_PLATE =
      deferredRegister.registerSimpleItem("iron_plate");
  public static final DeferredItem<Item> TIN_PLATE =
      deferredRegister.registerSimpleItem("tin_plate");
  public static final DeferredItem<Item> GOLD_PLATE =
      deferredRegister.registerSimpleItem("gold_plate");
  public static final DeferredItem<Item> LEAD_PLATE =
      deferredRegister.registerSimpleItem("lead_plate");
  public static final DeferredItem<Item> ZINC_PLATE =
      deferredRegister.registerSimpleItem("zinc_plate");
  public static final DeferredItem<Item> BRASS_PLATE =
      deferredRegister.registerSimpleItem("brass_plate");
  public static final DeferredItem<Item> INVAR_PLATE =
      deferredRegister.registerSimpleItem("invar_plate");
  public static final DeferredItem<Item> BRONZE_PLATE =
      deferredRegister.registerSimpleItem("bronze_plate");
  public static final DeferredItem<Item> COPPER_PLATE =
      deferredRegister.registerSimpleItem("copper_plate");
  public static final DeferredItem<Item> NICKEL_PLATE =
      deferredRegister.registerSimpleItem("nickel_plate");
  public static final DeferredItem<Item> SILVER_PLATE =
      deferredRegister.registerSimpleItem("silver_plate");

  public static final DeferredItem<Item> STEEL_INGOT =
      deferredRegister.registerSimpleItem("steel_ingot");
  public static final DeferredItem<Item> TIN_INGOT =
      deferredRegister.registerSimpleItem("tin_ingot");
  public static final DeferredItem<Item> ZINC_INGOT =
      deferredRegister.registerSimpleItem("zinc_ingot");
  public static final DeferredItem<Item> BRASS_INGOT =
      deferredRegister.registerSimpleItem("brass_ingot");
  public static final DeferredItem<Item> BRONZE_INGOT =
      deferredRegister.registerSimpleItem("bronze_ingot");
  public static final DeferredItem<Item> NICKEL_INGOT =
      deferredRegister.registerSimpleItem("nickel_ingot");
  public static final DeferredItem<Item> INVAR_INGOT =
      deferredRegister.registerSimpleItem("invar_ingot");
  public static final DeferredItem<Item> LEAD_INGOT =
      deferredRegister.registerSimpleItem("lead_ingot");
  public static final DeferredItem<Item> SILVER_INGOT =
      deferredRegister.registerSimpleItem("silver_ingot");

  public static final DeferredItem<Item> SALTPETER_DUST =
      deferredRegister.registerSimpleItem("saltpeter_dust");
  public static final DeferredItem<Item> COAL_DUST =
      deferredRegister.registerSimpleItem("coal_dust");
  public static final DeferredItem<Item> CHARCOAL_DUST =
      deferredRegister.registerSimpleItem("charcoal_dust");
  public static final DeferredItem<Item> SLAG =
      deferredRegister.registerSimpleItem("slag");
  public static final DeferredItem<Item> ENDER_DUST =
      deferredRegister.registerSimpleItem("ender_dust");
  public static final DeferredItem<Item> SULFUR_DUST =
      deferredRegister.registerSimpleItem("sulfur_dust");
  public static final DeferredItem<Item> OBSIDIAN_DUST =
      deferredRegister.registerSimpleItem("obsidian_dust");

  public static final DeferredItem<Item> STEEL_NUGGET =
      deferredRegister.registerSimpleItem("steel_nugget");
  public static final DeferredItem<Item> TIN_NUGGET =
      deferredRegister.registerSimpleItem("tin_nugget");
  public static final DeferredItem<Item> ZINC_NUGGET =
      deferredRegister.registerSimpleItem("zinc_nugget");
  public static final DeferredItem<Item> BRASS_NUGGET =
      deferredRegister.registerSimpleItem("brass_nugget");
  public static final DeferredItem<Item> BRONZE_NUGGET =
      deferredRegister.registerSimpleItem("bronze_nugget");
  public static final DeferredItem<Item> NICKEL_NUGGET =
      deferredRegister.registerSimpleItem("nickel_nugget");
  public static final DeferredItem<Item> INVAR_NUGGET =
      deferredRegister.registerSimpleItem("invar_nugget");
  public static final DeferredItem<Item> SILVER_NUGGET =
      deferredRegister.registerSimpleItem("silver_nugget");
  public static final DeferredItem<Item> LEAD_NUGGET =
      deferredRegister.registerSimpleItem("lead_nugget");

  public static final DeferredItem<Item> TIN_RAW =
      deferredRegister.registerSimpleItem("tin_raw");
  public static final DeferredItem<Item> ZINC_RAW =
      deferredRegister.registerSimpleItem("zinc_raw");
  public static final DeferredItem<Item> NICKEL_RAW =
      deferredRegister.registerSimpleItem("nickel_raw");
  public static final DeferredItem<Item> SILVER_RAW =
      deferredRegister.registerSimpleItem("silver_raw");
  public static final DeferredItem<Item> LEAD_RAW =
      deferredRegister.registerSimpleItem("lead_raw");

  public static final DeferredItem<Item> BUSHING_GEAR =
      deferredRegister.registerSimpleItem("bushing_gear");
  public static final DeferredItem<Item> TIN_GEAR =
      deferredRegister.registerSimpleItem("tin_gear");
  public static final DeferredItem<Item> GOLD_GEAR =
      deferredRegister.registerSimpleItem("gold_gear");
  public static final DeferredItem<Item> IRON_GEAR =
      deferredRegister.registerSimpleItem("iron_gear");
  public static final DeferredItem<Item> LEAD_GEAR =
      deferredRegister.registerSimpleItem("lead_gear");
  public static final DeferredItem<Item> ZINC_GEAR =
      deferredRegister.registerSimpleItem("zinc_gear");
  public static final DeferredItem<Item> BRASS_GEAR =
      deferredRegister.registerSimpleItem("brass_gear");
  public static final DeferredItem<Item> INVAR_GEAR =
      deferredRegister.registerSimpleItem("invar_gear");
  public static final DeferredItem<Item> STEEL_GEAR =
      deferredRegister.registerSimpleItem("steel_gear");
  public static final DeferredItem<Item> BRONZE_GEAR =
      deferredRegister.registerSimpleItem("bronze_gear");
  public static final DeferredItem<Item> COPPER_GEAR =
      deferredRegister.registerSimpleItem("copper_gear");
  public static final DeferredItem<Item> NICKEL_GEAR =
      deferredRegister.registerSimpleItem("nickel_gear");
  public static final DeferredItem<Item> SILVER_GEAR =
      deferredRegister.registerSimpleItem("silver_gear");

  public static final DeferredItem<Item> TIN_ELECTRODE =
      deferredRegister.registerSimpleItem("tin_electrode");
  public static final DeferredItem<Item> GOLD_ELECTRODE =
      deferredRegister.registerSimpleItem("gold_electrode");
  public static final DeferredItem<Item> IRON_ELECTRODE =
      deferredRegister.registerSimpleItem("iron_electrode");
  public static final DeferredItem<Item> LEAD_ELECTRODE =
      deferredRegister.registerSimpleItem("lead_electrode");
  public static final DeferredItem<Item> ZINC_ELECTRODE =
      deferredRegister.registerSimpleItem("zinc_electrode");
  public static final DeferredItem<Item> BRASS_ELECTRODE =
      deferredRegister.registerSimpleItem("brass_electrode");
  public static final DeferredItem<Item> INVAR_ELECTRODE =
      deferredRegister.registerSimpleItem("invar_electrode");
  public static final DeferredItem<Item> STEEL_ELECTRODE =
      deferredRegister.registerSimpleItem("steel_electrode");
  public static final DeferredItem<Item> BRONZE_ELECTRODE =
      deferredRegister.registerSimpleItem("bronze_electrode");
  public static final DeferredItem<Item> CARBON_ELECTRODE =
      deferredRegister.registerSimpleItem("carbon_electrode");
  public static final DeferredItem<Item> COPPER_ELECTRODE =
      deferredRegister.registerSimpleItem("copper_electrode");
  public static final DeferredItem<Item> NICKEL_ELECTRODE =
      deferredRegister.registerSimpleItem("nickel_electrode");
  public static final DeferredItem<Item> SILVER_ELECTRODE =
      deferredRegister.registerSimpleItem("silver_electrode");

  public static final DeferredItem<Item> CONTROLLER_CIRCUIT =
      deferredRegister.registerSimpleItem("controller_circuit");
  public static final DeferredItem<Item> RECEIVER_CIRCUIT =
      deferredRegister.registerSimpleItem("receiver_circuit");
  public static final DeferredItem<Item> SIGNAL_CIRCUIT =
      deferredRegister.registerSimpleItem("signal_circuit");
  public static final DeferredItem<Item> RADIO_CIRCUIT =
      deferredRegister.registerSimpleItem("radio_circuit");

  public static final DeferredItem<Item> WOODEN_RAIL =
      deferredRegister.registerSimpleItem("wooden_rail");
  public static final DeferredItem<Item> STANDARD_RAIL =
      deferredRegister.registerSimpleItem("standard_rail");
  public static final DeferredItem<Item> ADVANCED_RAIL =
      deferredRegister.registerSimpleItem("advanced_rail");
  public static final DeferredItem<Item> REINFORCED_RAIL =
      deferredRegister.registerSimpleItem("reinforced_rail");
  public static final DeferredItem<Item> HIGH_SPEED_RAIL =
      deferredRegister.registerSimpleItem("high_speed_rail");
  public static final DeferredItem<Item> ELECTRIC_RAIL =
      deferredRegister.registerSimpleItem("electric_rail");

  public static final DeferredItem<Item> BAG_OF_CEMENT =
      deferredRegister.registerSimpleItem("bag_of_cement");

  public static final DeferredItem<Item> WOODEN_TIE =
      deferredRegister.registerSimpleItem("wooden_tie");
  public static final DeferredItem<Item> STONE_TIE =
      deferredRegister.registerSimpleItem("stone_tie");

  public static final DeferredItem<Item> REBAR =
      deferredRegister.registerSimpleItem("rebar");
  public static final DeferredItem<Item> WOODEN_RAILBED =
      deferredRegister.registerSimpleItem("wooden_railbed");
  public static final DeferredItem<Item> STONE_RAILBED =
      deferredRegister.registerSimpleItem("stone_railbed");

  public static final DeferredItem<Item> SIGNAL_LAMP =
      deferredRegister.registerSimpleItem("signal_lamp");

  public static final DeferredItem<Item> CHARGE_SPOOL_LARGE =
      deferredRegister.registerSimpleItem("charge_spool_large");
  public static final DeferredItem<Item> CHARGE_SPOOL_MEDIUM =
      deferredRegister.registerSimpleItem("charge_spool_medium");
  public static final DeferredItem<Item> CHARGE_SPOOL_SMALL =
      deferredRegister.registerSimpleItem("charge_spool_small");

  public static final DeferredItem<Item> CHARGE_MOTOR =
      deferredRegister.registerSimpleItem("charge_motor");

  public static final DeferredItem<Item> CHARGE_COIL =
      deferredRegister.registerSimpleItem("charge_coil");

  public static final DeferredItem<Item> CHARGE_TERMINAL =
      deferredRegister.registerSimpleItem("charge_terminal");

  public static final DeferredItem<BlockItem> WATER_TANK_SIDING =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.WATER_TANK_SIDING);

  public static final DeferredItem<BlockItem> QUARRIED_STONE =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.QUARRIED_STONE);

  public static final DeferredItem<BlockItem> QUARRIED_COBBLESTONE =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.QUARRIED_COBBLESTONE);

  public static final DeferredItem<BlockItem> POLISHED_QUARRIED_STONE =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.POLISHED_QUARRIED_STONE);

  public static final DeferredItem<BlockItem> CHISELED_QUARRIED_STONE =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.CHISELED_QUARRIED_STONE);

  public static final DeferredItem<BlockItem> ETCHED_QUARRIED_STONE =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ETCHED_QUARRIED_STONE);

  public static final DeferredItem<BlockItem> QUARRIED_BRICKS =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.QUARRIED_BRICKS);

  public static final DeferredItem<BlockItem> QUARRIED_BRICK_STAIRS =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.QUARRIED_BRICK_STAIRS);

  public static final DeferredItem<BlockItem> QUARRIED_BRICK_SLAB =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.QUARRIED_BRICK_SLAB);

  public static final DeferredItem<BlockItem> QUARRIED_PAVER =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.QUARRIED_PAVER);

  public static final DeferredItem<BlockItem> QUARRIED_PAVER_STAIRS =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.QUARRIED_PAVER_STAIRS);

  public static final DeferredItem<BlockItem> QUARRIED_PAVER_SLAB =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.QUARRIED_PAVER_SLAB);

  public static final DeferredItem<BlockItem> ABYSSAL_STONE =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ABYSSAL_STONE);

  public static final DeferredItem<BlockItem> ABYSSAL_COBBLESTONE =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ABYSSAL_COBBLESTONE);

  public static final DeferredItem<BlockItem> POLISHED_ABYSSAL_STONE =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.POLISHED_ABYSSAL_STONE);

  public static final DeferredItem<BlockItem> CHISELED_ABYSSAL_STONE =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.CHISELED_ABYSSAL_STONE);

  public static final DeferredItem<BlockItem> ETCHED_ABYSSAL_STONE =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ETCHED_ABYSSAL_STONE);

  public static final DeferredItem<BlockItem> ABYSSAL_BRICKS =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ABYSSAL_BRICKS);

  public static final DeferredItem<BlockItem> ABYSSAL_BRICK_STAIRS =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ABYSSAL_BRICK_STAIRS);

  public static final DeferredItem<BlockItem> ABYSSAL_BRICK_SLAB =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ABYSSAL_BRICK_SLAB);

  public static final DeferredItem<BlockItem> ABYSSAL_PAVER =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ABYSSAL_PAVER);

  public static final DeferredItem<BlockItem> ABYSSAL_PAVER_STAIRS =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ABYSSAL_PAVER_STAIRS);

  public static final DeferredItem<BlockItem> ABYSSAL_PAVER_SLAB =
      deferredRegister.registerSimpleBlockItem(RailcraftBlocks.ABYSSAL_PAVER_SLAB);

  // ================================================================================
  // Buckets
  // ================================================================================

  public static final DeferredItem<Item> CREOSOTE_BUCKET =
      deferredRegister.register("creosote_bucket",
          () -> new BucketItem(RailcraftFluids.CREOSOTE,
              new Item.Properties()
                  .stacksTo(1)
                  .craftRemainder(Items.BUCKET)) {
            @Override
            public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
              return 800;
            }
          });

  public static final DeferredItem<Item> CREOSOTE_BOTTLE =
      deferredRegister.register("creosote_bottle",
          () -> new FluidBottleItem(RailcraftFluids.CREOSOTE,
              new Item.Properties()
                  .stacksTo(16)
                  .craftRemainder(Items.GLASS_BOTTLE)));
}
