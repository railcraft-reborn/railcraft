package mods.railcraft.world.item;

import java.util.function.Function;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.Railcraft;
import mods.railcraft.util.VariantRegistrar;
import mods.railcraft.world.entity.vehicle.TankMinecart;
import mods.railcraft.world.entity.vehicle.locomotive.CreativeLocomotive;
import mods.railcraft.world.entity.vehicle.locomotive.ElectricLocomotive;
import mods.railcraft.world.entity.vehicle.locomotive.SteamLocomotive;
import mods.railcraft.world.item.track.ActivatorTrackBlockItem;
import mods.railcraft.world.item.track.BoosterTrackBlockItem;
import mods.railcraft.world.item.track.BufferStopTrackBlockItem;
import mods.railcraft.world.item.track.ControlTrackBlockItem;
import mods.railcraft.world.item.track.CouplerTrackBlockItem;
import mods.railcraft.world.item.track.DetectorTrackBlockItem;
import mods.railcraft.world.item.track.DisembarkingTrackBlockItem;
import mods.railcraft.world.item.track.EmbarkingTrackBlockItem;
import mods.railcraft.world.item.track.GatedTrackBlockItem;
import mods.railcraft.world.item.track.LauncherTrackBlockItem;
import mods.railcraft.world.item.track.LockingTrackBlockItem;
import mods.railcraft.world.item.track.LocomotiveTrackBlockItem;
import mods.railcraft.world.item.track.OneWayTrackBlockItem;
import mods.railcraft.world.item.track.TransitionTrackBlockItem;
import mods.railcraft.world.item.tunnelbore.BronzeTunnelBoreHeadItem;
import mods.railcraft.world.item.tunnelbore.DiamondTunnelBoreHeadItem;
import mods.railcraft.world.item.tunnelbore.IronTunnelBoreHeadItem;
import mods.railcraft.world.item.tunnelbore.SteelTunnelBoreHeadItem;
import mods.railcraft.world.item.tunnelbore.TunnelBoreItem;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.track.TrackTypes;
import mods.railcraft.world.level.material.fluid.RailcraftFluids;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
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
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftItems {

  private static final DeferredRegister<Item> deferredRegister =
      DeferredRegister.create(ForgeRegistries.ITEMS, Railcraft.ID);

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }


  public static final VariantRegistrar<DyeColor, BlockItem> STRENGTHENED_GLASS =
      VariantRegistrar.from(DyeColor.class, deferredRegister);
  public static final VariantRegistrar<DyeColor, BlockItem> POST =
      VariantRegistrar.from(DyeColor.class, deferredRegister);
  public static final VariantRegistrar<DyeColor, BlockItem> IRON_TANK_GAUGE =
      VariantRegistrar.from(DyeColor.class, deferredRegister);
  public static final VariantRegistrar<DyeColor, BlockItem> IRON_TANK_VALVE =
      VariantRegistrar.from(DyeColor.class, deferredRegister);
  public static final VariantRegistrar<DyeColor, BlockItem> IRON_TANK_WALL =
      VariantRegistrar.from(DyeColor.class, deferredRegister);
  public static final VariantRegistrar<DyeColor, BlockItem> STEEL_TANK_GAUGE =
      VariantRegistrar.from(DyeColor.class, deferredRegister);
  public static final VariantRegistrar<DyeColor, BlockItem> STEEL_TANK_VALVE =
      VariantRegistrar.from(DyeColor.class, deferredRegister);
  public static final VariantRegistrar<DyeColor, BlockItem> STEEL_TANK_WALL =
      VariantRegistrar.from(DyeColor.class, deferredRegister);

  static {
    var tabFactory = blockItemFactory();
    STRENGTHENED_GLASS.registerUsing(RailcraftBlocks.STRENGTHENED_GLASS, tabFactory);
    POST.registerUsing(RailcraftBlocks.POST, tabFactory);

    IRON_TANK_GAUGE.registerUsing(RailcraftBlocks.IRON_TANK_GAUGE, tabFactory);
    IRON_TANK_VALVE.registerUsing(RailcraftBlocks.IRON_TANK_VALVE, tabFactory);
    IRON_TANK_WALL.registerUsing(RailcraftBlocks.IRON_TANK_WALL, tabFactory);

    STEEL_TANK_GAUGE.registerUsing(RailcraftBlocks.STEEL_TANK_GAUGE, tabFactory);
    STEEL_TANK_VALVE.registerUsing(RailcraftBlocks.STEEL_TANK_VALVE, tabFactory);
    STEEL_TANK_WALL.registerUsing(RailcraftBlocks.STEEL_TANK_WALL, tabFactory);
  }

  public static final RegistryObject<PressureBoilerTankBlockItem> LOW_PRESSURE_STEAM_BOILER_TANK =
      deferredRegister.register("low_pressure_steam_boiler_tank",
          () -> new PressureBoilerTankBlockItem(RailcraftBlocks.LOW_PRESSURE_STEAM_BOILER_TANK.get(),
              new Item.Properties()));

  public static final RegistryObject<PressureBoilerTankBlockItem> HIGH_PRESSURE_STEAM_BOILER_TANK =
      deferredRegister.register("high_pressure_steam_boiler_tank",
          () -> new PressureBoilerTankBlockItem(RailcraftBlocks.HIGH_PRESSURE_STEAM_BOILER_TANK.get(),
              new Item.Properties()));

  public static final RegistryObject<FueledFireboxBlockItem> SOLID_FUELED_FIREBOX =
      deferredRegister.register("solid_fueled_firebox",
          () -> new FueledFireboxBlockItem(RailcraftBlocks.SOLID_FUELED_FIREBOX.get(),
              new Item.Properties()));

  public static final RegistryObject<FueledFireboxBlockItem> FLUID_FUELED_FIREBOX =
      deferredRegister.register("fluid_fueled_firebox",
          () -> new FueledFireboxBlockItem(RailcraftBlocks.FLUID_FUELED_FIREBOX.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> SIGNAL_LABEL =
      deferredRegister.register("signal_label",
          () -> new SignalLabelItem(new Item.Properties()));

  public static final RegistryObject<Item> TURBINE_BLADE = registerBasic("turbine_blade");

  public static final RegistryObject<Item> TURBINE_DISK = registerBasic("turbine_disk");

  public static final RegistryObject<Item> TURBINE_ROTOR =
      deferredRegister.register("turbine_rotor",
          () -> new TurbineRotorItem(new Item.Properties()));

  public static final RegistryObject<Item> STEAM_TURBINE =
      deferredRegister.register("steam_turbine",
          () -> new BlockItem(RailcraftBlocks.STEAM_TURBINE.get(), new Item.Properties()));


  public static final RegistryObject<Item> BLAST_FURNACE_BRICKS =
      deferredRegister.register("blast_furnace_bricks",
          () -> new BlockItem(RailcraftBlocks.BLAST_FURNACE_BRICKS.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> FEED_STATION =
      deferredRegister.register("feed_station",
          () -> new BlockItem(RailcraftBlocks.FEED_STATION.get(), new Item.Properties()));

  public static final RegistryObject<Item> STEEL_ANVIL =
      deferredRegister.register("steel_anvil",
          () -> new BlockItem(RailcraftBlocks.STEEL_ANVIL.get(), new Item.Properties()));

  public static final RegistryObject<Item> CHIPPED_STEEL_ANVIL =
      deferredRegister.register("chipped_steel_anvil",
          () -> new BlockItem(RailcraftBlocks.CHIPPED_STEEL_ANVIL.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> DAMAGED_STEEL_ANVIL =
      deferredRegister.register("damaged_steel_anvil",
          () -> new BlockItem(RailcraftBlocks.DAMAGED_STEEL_ANVIL.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> STEEL_BLOCK =
      deferredRegister.register("steel_block",
          () -> new BlockItem(RailcraftBlocks.STEEL_BLOCK.get(), new Item.Properties()));

  public static final RegistryObject<Item> BRASS_BLOCK =
      deferredRegister.register("brass_block",
          () -> new BlockItem(RailcraftBlocks.BRASS_BLOCK.get(), new Item.Properties()));

  public static final RegistryObject<Item> BRONZE_BLOCK =
      deferredRegister.register("bronze_block",
          () -> new BlockItem(RailcraftBlocks.BRONZE_BLOCK.get(), new Item.Properties()));

  public static final RegistryObject<Item> INVAR_BLOCK =
      deferredRegister.register("invar_block",
          () -> new BlockItem(RailcraftBlocks.INVAR_BLOCK.get(), new Item.Properties()));

  public static final RegistryObject<Item> LEAD_BLOCK =
      deferredRegister.register("lead_block",
          () -> new BlockItem(RailcraftBlocks.LEAD_BLOCK.get(), new Item.Properties()));

  public static final RegistryObject<Item> NICKEL_BLOCK =
      deferredRegister.register("nickel_block",
          () -> new BlockItem(RailcraftBlocks.NICKEL_BLOCK.get(), new Item.Properties()));

  public static final RegistryObject<Item> SILVER_BLOCK =
      deferredRegister.register("silver_block",
          () -> new BlockItem(RailcraftBlocks.SILVER_BLOCK.get(), new Item.Properties()));

  public static final RegistryObject<Item> TIN_BLOCK =
      deferredRegister.register("tin_block",
          () -> new BlockItem(RailcraftBlocks.TIN_BLOCK.get(), new Item.Properties()));

  public static final RegistryObject<Item> ZINC_BLOCK =
      deferredRegister.register("zinc_block",
          () -> new BlockItem(RailcraftBlocks.ZINC_BLOCK.get(), new Item.Properties()));

  public static final RegistryObject<Item> LEAD_ORE =
      deferredRegister.register("lead_ore",
          () -> new BlockItem(RailcraftBlocks.LEAD_ORE.get(), new Properties()));

  public static final RegistryObject<Item> NICKEL_ORE =
      deferredRegister.register("nickel_ore",
          () -> new BlockItem(RailcraftBlocks.NICKEL_ORE.get(), new Properties()));

  public static final RegistryObject<Item> SILVER_ORE =
      deferredRegister.register("silver_ore",
          () -> new BlockItem(RailcraftBlocks.SILVER_ORE.get(), new Properties()));

  public static final RegistryObject<Item> TIN_ORE =
      deferredRegister.register("tin_ore",
          () -> new BlockItem(RailcraftBlocks.TIN_ORE.get(), new Properties()));

  public static final RegistryObject<Item> ZINC_ORE =
      deferredRegister.register("zinc_ore",
          () -> new BlockItem(RailcraftBlocks.ZINC_ORE.get(), new Properties()));

  public static final RegistryObject<Item> DEEPSLATE_LEAD_ORE =
      deferredRegister.register("deepslate_lead_ore",
          () -> new BlockItem(RailcraftBlocks.DEEPSLATE_LEAD_ORE.get(), new Properties()));

  public static final RegistryObject<Item> DEEPSLATE_NICKEL_ORE =
      deferredRegister.register("deepslate_nickel_ore",
          () -> new BlockItem(RailcraftBlocks.DEEPSLATE_NICKEL_ORE.get(), new Properties()));

  public static final RegistryObject<Item> DEEPSLATE_SILVER_ORE =
      deferredRegister.register("deepslate_silver_ore",
          () -> new BlockItem(RailcraftBlocks.DEEPSLATE_SILVER_ORE.get(), new Properties()));

  public static final RegistryObject<Item> DEEPSLATE_TIN_ORE =
      deferredRegister.register("deepslate_tin_ore",
          () -> new BlockItem(RailcraftBlocks.DEEPSLATE_TIN_ORE.get(), new Properties()));

  public static final RegistryObject<Item> DEEPSLATE_ZINC_ORE =
      deferredRegister.register("deepslate_zinc_ore",
          () -> new BlockItem(RailcraftBlocks.DEEPSLATE_ZINC_ORE.get(), new Properties()));

  public static final RegistryObject<Item> SULFUR_ORE =
      deferredRegister.register("sulfur_ore",
          () -> new BlockItem(RailcraftBlocks.SULFUR_ORE.get(), new Properties()));

  public static final RegistryObject<Item> DEEPSLATE_SULFUR_ORE =
      deferredRegister.register("deepslate_sulfur_ore",
          () -> new BlockItem(RailcraftBlocks.DEEPSLATE_SULFUR_ORE.get(), new Properties()));

  public static final RegistryObject<Item> SALTPETER_ORE =
      deferredRegister.register("saltpeter_ore",
          () -> new BlockItem(RailcraftBlocks.SALTPETER_ORE.get(), new Properties()));

  public static final RegistryObject<Item> COKE_BLOCK =
      deferredRegister.register("coal_coke_block",
          () -> new CoalCokeBlockItem(new Properties()));

  public static final RegistryObject<Item> STEEL_SHEARS =
      deferredRegister.register("steel_shears",
          () -> new ShearsItem(new Item.Properties()
              .durability(500)));

  public static final RegistryObject<Item> STEEL_SWORD =
      deferredRegister.register("steel_sword",
          () -> new SwordItem(RailcraftItemTier.STEEL, 3, -2.4F,
              new Item.Properties()));

  public static final RegistryObject<Item> STEEL_SHOVEL =
      deferredRegister.register("steel_shovel",
          () -> new ShovelItem(RailcraftItemTier.STEEL, 1.5F, -3.0F,
              new Item.Properties()));

  public static final RegistryObject<Item> STEEL_PICKAXE =
      deferredRegister.register("steel_pickaxe",
          () -> new PickaxeItem(RailcraftItemTier.STEEL, 1, -2.8F,
              new Item.Properties()));

  public static final RegistryObject<Item> STEEL_AXE =
      deferredRegister.register("steel_axe",
          () -> new AxeItem(RailcraftItemTier.STEEL, 8.0F, -3F,
              new Item.Properties()));

  public static final RegistryObject<Item> STEEL_HOE =
      deferredRegister.register("steel_hoe",
          () -> new HoeItem(RailcraftItemTier.STEEL, -2, -0.5F,
              new Item.Properties()));

  public static final RegistryObject<Item> STEEL_BOOTS =
      deferredRegister.register("steel_boots",
          () -> new ArmorItem(RailcraftArmorMaterial.STEEL, EquipmentSlot.FEET,
              new Item.Properties()));

  public static final RegistryObject<Item> STEEL_CHESTPLATE =
      deferredRegister.register("steel_chestplate",
          () -> new ArmorItem(RailcraftArmorMaterial.STEEL, EquipmentSlot.CHEST,
              new Item.Properties()));

  public static final RegistryObject<Item> STEEL_HELMET =
      deferredRegister.register("steel_helmet",
          () -> new ArmorItem(RailcraftArmorMaterial.STEEL, EquipmentSlot.HEAD,
              new Item.Properties()));

  public static final RegistryObject<Item> STEEL_LEGGINGS =
      deferredRegister.register("steel_leggings",
          () -> new ArmorItem(RailcraftArmorMaterial.STEEL, EquipmentSlot.LEGS,
              new Item.Properties()));

  public static final RegistryObject<Item> IRON_TUNNEL_BORE_HEAD =
      deferredRegister.register("iron_tunnel_bore_head",
          () -> new IronTunnelBoreHeadItem(new Item.Properties()
              .durability(1500)));

  public static final RegistryObject<Item> BRONZE_TUNNEL_BORE_HEAD =
      deferredRegister.register("bronze_tunnel_bore_head",
          () -> new BronzeTunnelBoreHeadItem(new Item.Properties()
              .durability(1200)));

  public static final RegistryObject<Item> STEEL_TUNNEL_BORE_HEAD =
      deferredRegister.register("steel_tunnel_bore_head",
          () -> new SteelTunnelBoreHeadItem(
              new Item.Properties()
                  .durability(3000)));

  public static final RegistryObject<Item> DIAMOND_TUNNEL_BORE_HEAD =
      deferredRegister.register("diamond_tunnel_bore_head",
          () -> new DiamondTunnelBoreHeadItem(new Item.Properties()
              .durability(6000)));

  public static final RegistryObject<Item> TANK_MINECART =
      deferredRegister.register("tank_minecart",
          () -> new CartItem(TankMinecart::new,
              new Item.Properties()
                  .stacksTo(1)));

  public static final RegistryObject<Item> FLUID_LOADER =
      deferredRegister.register("fluid_loader",
          () -> new BlockItem(RailcraftBlocks.FLUID_LOADER.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> FLUID_UNLOADER =
      deferredRegister.register("fluid_unloader",
          () -> new BlockItem(RailcraftBlocks.FLUID_UNLOADER.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> ADVANCED_ITEM_LOADER =
      deferredRegister.register("advanced_item_loader",
          () -> new BlockItem(RailcraftBlocks.ADVANCED_ITEM_LOADER.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> ADVANCED_ITEM_UNLOADER =
      deferredRegister.register("advanced_item_unloader",
          () -> new BlockItem(RailcraftBlocks.ADVANCED_ITEM_UNLOADER.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> ITEM_LOADER =
      deferredRegister.register("item_loader",
          () -> new BlockItem(RailcraftBlocks.ITEM_LOADER.get(), new Item.Properties()));

  public static final RegistryObject<Item> ITEM_UNLOADER =
      deferredRegister.register("item_unloader",
          () -> new BlockItem(RailcraftBlocks.ITEM_UNLOADER.get(), new Item.Properties()));

  public static final RegistryObject<Item> CART_DISPENSER =
      deferredRegister.register("cart_dispenser",
          () -> new BlockItem(RailcraftBlocks.CART_DISPENSER.get(), new Item.Properties()));

  public static final RegistryObject<Item> TRAIN_DISPENSER =
      deferredRegister.register("train_dispenser",
          () -> new BlockItem(RailcraftBlocks.TRAIN_DISPENSER.get(), new Item.Properties()));

  public static final RegistryObject<Item> IRON_SPIKE_MAUL =
      deferredRegister.register("iron_spike_maul",
          () -> new SpikeMaulItem(11.0F, -3.5F, Tiers.IRON,
              new Item.Properties()));

  public static final RegistryObject<Item> STEEL_SPIKE_MAUL =
      deferredRegister.register("steel_spike_maul",
          () -> new SpikeMaulItem(11.0F, -3.4F, RailcraftItemTier.STEEL,
              new Item.Properties()));

  public static final RegistryObject<Item> DIAMOND_SPIKE_MAUL =
      deferredRegister.register("diamond_spike_maul",
          () -> new SpikeMaulItem(11.0F, -3.3F, Tiers.DIAMOND,
              new Item.Properties()));

  public static final RegistryObject<Item> SWITCH_TRACK_LEVER =
      deferredRegister.register("switch_track_lever",
          () -> new BlockItem(RailcraftBlocks.SWITCH_TRACK_LEVER.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> SWITCH_TRACK_MOTOR =
      deferredRegister.register("switch_track_motor",
          () -> new BlockItem(RailcraftBlocks.SWITCH_TRACK_MOTOR.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> SWITCH_TRACK_ROUTING =
      deferredRegister.register("switch_track_routing",
          () -> new BlockItem(RailcraftBlocks.SWITCH_TRACK_ROUTING.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> SIGNAL_TUNER =
      deferredRegister.register("signal_tuner",
          () -> new SignalTunerItem(new Item.Properties().stacksTo(1)));

  public static final RegistryObject<Item> SIGNAL_BLOCK_SURVEYOR =
      deferredRegister.register("signal_block_surveyor",
          () -> new SignalBlockSurveyorItem(new Item.Properties().stacksTo(1)));

  public static final RegistryObject<Item> ANALOG_SIGNAL_CONTROLLER_BOX =
      deferredRegister.register("analog_signal_controller_box",
          () -> new BlockItem(
              RailcraftBlocks.ANALOG_SIGNAL_CONTROLLER_BOX.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> SIGNAL_SEQUENCER_BOX =
      deferredRegister.register("signal_sequencer_box",
          () -> new BlockItem(
              RailcraftBlocks.SIGNAL_SEQUENCER_BOX.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> SIGNAL_CAPACITOR_BOX =
      deferredRegister.register("signal_capacitor_box",
          () -> new BlockItem(
              RailcraftBlocks.SIGNAL_CAPACITOR_BOX.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> SIGNAL_INTERLOCK_BOX =
      deferredRegister.register("signal_interlock_box",
          () -> new BlockItem(
              RailcraftBlocks.SIGNAL_INTERLOCK_BOX.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> SIGNAL_BLOCK_RELAY_BOX =
      deferredRegister.register("signal_block_relay_box",
          () -> new BlockItem(
              RailcraftBlocks.SIGNAL_BLOCK_RELAY_BOX.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> SIGNAL_RECEIVER_BOX =
      deferredRegister.register("signal_receiver_box",
          () -> new BlockItem(
              RailcraftBlocks.SIGNAL_RECEIVER_BOX.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> SIGNAL_CONTROLLER_BOX =
      deferredRegister.register("signal_controller_box",
          () -> new BlockItem(
              RailcraftBlocks.SIGNAL_CONTROLLER_BOX.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> DUAL_BLOCK_SIGNAL =
      deferredRegister.register("dual_block_signal",
          () -> new BlockItem(
              RailcraftBlocks.DUAL_BLOCK_SIGNAL.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> DUAL_DISTANT_SIGNAL =
      deferredRegister.register("dual_distant_signal",
          () -> new BlockItem(
              RailcraftBlocks.DUAL_DISTANT_SIGNAL.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> DUAL_TOKEN_SIGNAL =
      deferredRegister.register("dual_token_signal",
          () -> new BlockItem(
              RailcraftBlocks.DUAL_TOKEN_SIGNAL.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> BLOCK_SIGNAL =
      deferredRegister.register("block_signal",
          () -> new BlockItem(
              RailcraftBlocks.BLOCK_SIGNAL.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> DISTANT_SIGNAL =
      deferredRegister.register("distant_signal",
          () -> new BlockItem(
              RailcraftBlocks.DISTANT_SIGNAL.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> TOKEN_SIGNAL =
      deferredRegister.register("token_signal",
          () -> new BlockItem(
              RailcraftBlocks.TOKEN_SIGNAL.get(),
              new Item.Properties()));

  public static final RegistryObject<TrackRemoverCartItem> TRACK_REMOVER =
      deferredRegister.register("track_remover",
          () -> new TrackRemoverCartItem(new Item.Properties()
              .rarity(Rarity.UNCOMMON)
              .stacksTo(1)));

  public static final RegistryObject<TrackLayerCartItem> TRACK_LAYER =
      deferredRegister.register("track_layer",
          () -> new TrackLayerCartItem(new Item.Properties()
              .rarity(Rarity.UNCOMMON)
              .stacksTo(1)));

  public static final RegistryObject<Item> TUNNEL_BORE =
      deferredRegister.register("tunnel_bore",
          () -> new TunnelBoreItem(new Item.Properties()
              .rarity(Rarity.UNCOMMON)
              .stacksTo(1)));

  public static final RegistryObject<Item> CREATIVE_LOCOMOTIVE =
      deferredRegister.register("creative_locomotive",
          () -> new LocomotiveItem(CreativeLocomotive::new,
              DyeColor.BLACK, DyeColor.MAGENTA,
              new Item.Properties()
                  .stacksTo(1)));

  public static final RegistryObject<Item> ELECTRIC_LOCOMOTIVE =
      deferredRegister.register("electric_locomotive",
          () -> new LocomotiveItem(ElectricLocomotive::new,
              DyeColor.YELLOW, DyeColor.BLACK,
              new Item.Properties()
                  .stacksTo(1)));

  public static final RegistryObject<Item> STEAM_LOCOMOTIVE =
      deferredRegister.register("steam_locomotive",
          () -> new LocomotiveItem(SteamLocomotive::new,
              DyeColor.LIGHT_GRAY, DyeColor.GRAY,
              new Item.Properties()
                  .stacksTo(1)));

  public static final RegistryObject<Item> WHISTLE_TUNER =
      deferredRegister.register("whistle_tuner",
          () -> new Item(new Item.Properties().durability(250)));

  public static final RegistryObject<Item> GOLDEN_TICKET =
      deferredRegister.register("golden_ticket",
          () -> new GoldenTicketItem(new Item.Properties()
              .rarity(Rarity.UNCOMMON)));

  public static final RegistryObject<Item> TICKET =
      deferredRegister.register("ticket",
          () -> new TicketItem(new Item.Properties()));

  public static final RegistryObject<Item> ROUTING_TABLE_BOOK =
      deferredRegister.register("routing_table_book",
          () -> new RoutingTableBookItem(new Item.Properties()));

  public static final RegistryObject<Item> OVERALLS =
      deferredRegister.register("overalls",
          () -> new OverallsItem(new Item.Properties()));

  public static final RegistryObject<FirestoneOreBlockItem> FIRESTONE_ORE =
      deferredRegister.register("firestone_ore", () -> new FirestoneOreBlockItem(
          new Item.Properties()));

  public static final RegistryObject<FirestoneItem> RAW_FIRESTONE =
      deferredRegister.register("raw_firestone", () -> new FirestoneItem(
          new Item.Properties().rarity(Rarity.RARE)));

  public static final RegistryObject<RefinedFirestoneItem> REFINED_FIRESTONE =
      deferredRegister.register("refined_firestone",
          () -> new RefinedFirestoneItem(new Item.Properties()
              .stacksTo(1)
              .durability(RefinedFirestoneItem.CHARGES)
              .rarity(Rarity.RARE)));

  public static final RegistryObject<CrackedFirestoneItem> CRACKED_FIRESTONE =
      deferredRegister.register("cracked_firestone",
          () -> new CrackedFirestoneItem(new Item.Properties()
              .stacksTo(1)
              .durability(RefinedFirestoneItem.CHARGES)
              .rarity(Rarity.RARE)));

  public static final RegistryObject<FirestoneItem> CUT_FIRESTONE =
      deferredRegister.register("cut_firestone", () -> new FirestoneItem(
          new Item.Properties()
              .stacksTo(1)
              .durability(RefinedFirestoneItem.CHARGES)
              .rarity(Rarity.RARE)));

  public static final RegistryObject<BlockItem> FORCE_TRACK_EMITTER =
      deferredRegister.register("force_track_emitter",
          () -> new BlockItem(
              RailcraftBlocks.FORCE_TRACK_EMITTER.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> ABANDONED_TRACK =
      deferredRegister.register("abandoned_track",
          () -> new BlockItem(RailcraftBlocks.ABANDONED_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> ABANDONED_LOCKING_TRACK =
      deferredRegister.register("abandoned_locking_track",
          () -> new LockingTrackBlockItem(RailcraftBlocks.ABANDONED_LOCKING_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> ABANDONED_BUFFER_STOP_TRACK =
      deferredRegister.register("abandoned_buffer_stop_track",
          () -> new BufferStopTrackBlockItem(RailcraftBlocks.ABANDONED_BUFFER_STOP_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> ABANDONED_ACTIVATOR_TRACK =
      deferredRegister.register("abandoned_activator_track",
          () -> new ActivatorTrackBlockItem(RailcraftBlocks.ABANDONED_ACTIVATOR_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> ABANDONED_BOOSTER_TRACK =
      deferredRegister.register("abandoned_booster_track",
          () -> new BoosterTrackBlockItem(RailcraftBlocks.ABANDONED_BOOSTER_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> ABANDONED_CONTROL_TRACK =
      deferredRegister.register("abandoned_control_track",
          () -> new ControlTrackBlockItem(RailcraftBlocks.ABANDONED_CONTROL_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> ABANDONED_GATED_TRACK =
      deferredRegister.register("abandoned_gated_track",
          () -> new GatedTrackBlockItem(RailcraftBlocks.ABANDONED_GATED_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> ABANDONED_DETECTOR_TRACK =
      deferredRegister.register("abandoned_detector_track",
          () -> new DetectorTrackBlockItem(RailcraftBlocks.ABANDONED_DETECTOR_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> ABANDONED_COUPLER_TRACK =
      deferredRegister.register("abandoned_coupler_track",
          () -> new CouplerTrackBlockItem(RailcraftBlocks.ABANDONED_COUPLER_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> ABANDONED_EMBARKING_TRACK =
      deferredRegister.register("abandoned_embarking_track",
          () -> new EmbarkingTrackBlockItem(RailcraftBlocks.ABANDONED_EMBARKING_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> ABANDONED_DISEMBARKING_TRACK =
      deferredRegister.register("abandoned_disembarking_track",
          () -> new DisembarkingTrackBlockItem(RailcraftBlocks.ABANDONED_DISEMBARKING_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> ABANDONED_WYE_TRACK =
      deferredRegister.register("abandoned_wye_track",
          () -> new BlockItem(RailcraftBlocks.ABANDONED_WYE_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> ABANDONED_TURNOUT_TRACK =
      deferredRegister.register("abandoned_turnout_track",
          () -> new BlockItem(RailcraftBlocks.ABANDONED_TURNOUT_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> ABANDONED_JUNCTION_TRACK =
      deferredRegister.register("abandoned_junction_track",
          () -> new BlockItem(RailcraftBlocks.ABANDONED_JUNCTION_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> ABANDONED_LAUNCHER_TRACK =
      deferredRegister.register("abandoned_launcher_track",
          () -> new LauncherTrackBlockItem(RailcraftBlocks.ABANDONED_LAUNCHER_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> ABANDONED_ONE_WAY_TRACK =
      deferredRegister.register("abandoned_one_way_track",
          () -> new OneWayTrackBlockItem(RailcraftBlocks.ABANDONED_ONE_WAY_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> ABANDONED_LOCOMOTIVE_TRACK =
      deferredRegister.register("abandoned_locomotive_track",
          () -> new LocomotiveTrackBlockItem(RailcraftBlocks.ABANDONED_LOCOMOTIVE_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> ELECTRIC_TRACK =
      deferredRegister.register("electric_track",
          () -> new BlockItem(RailcraftBlocks.ELECTRIC_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> ELECTRIC_LOCKING_TRACK =
      deferredRegister.register("electric_locking_track",
          () -> new LockingTrackBlockItem(RailcraftBlocks.ELECTRIC_LOCKING_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> ELECTRIC_BUFFER_STOP_TRACK =
      deferredRegister.register("electric_buffer_stop_track",
          () -> new BufferStopTrackBlockItem(RailcraftBlocks.ELECTRIC_BUFFER_STOP_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> ELECTRIC_ACTIVATOR_TRACK =
      deferredRegister.register("electric_activator_track",
          () -> new ActivatorTrackBlockItem(RailcraftBlocks.ELECTRIC_ACTIVATOR_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> ELECTRIC_BOOSTER_TRACK =
      deferredRegister.register("electric_booster_track",
          () -> new BoosterTrackBlockItem(RailcraftBlocks.ELECTRIC_BOOSTER_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> ELECTRIC_CONTROL_TRACK =
      deferredRegister.register("electric_control_track",
          () -> new ControlTrackBlockItem(RailcraftBlocks.ELECTRIC_CONTROL_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> ELECTRIC_GATED_TRACK =
      deferredRegister.register("electric_gated_track",
          () -> new GatedTrackBlockItem(RailcraftBlocks.ELECTRIC_GATED_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> ELECTRIC_DETECTOR_TRACK =
      deferredRegister.register("electric_detector_track",
          () -> new DetectorTrackBlockItem(RailcraftBlocks.ELECTRIC_DETECTOR_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> ELECTRIC_COUPLER_TRACK =
      deferredRegister.register("electric_coupler_track",
          () -> new CouplerTrackBlockItem(RailcraftBlocks.ELECTRIC_COUPLER_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> ELECTRIC_EMBARKING_TRACK =
      deferredRegister.register("electric_embarking_track",
          () -> new EmbarkingTrackBlockItem(RailcraftBlocks.ELECTRIC_EMBARKING_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> ELECTRIC_DISEMBARKING_TRACK =
      deferredRegister.register("electric_disembarking_track",
          () -> new DisembarkingTrackBlockItem(RailcraftBlocks.ELECTRIC_DISEMBARKING_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> ELECTRIC_WYE_TRACK =
      deferredRegister.register("electric_wye_track",
          () -> new BlockItem(RailcraftBlocks.ELECTRIC_WYE_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> ELECTRIC_TURNOUT_TRACK =
      deferredRegister.register("electric_turnout_track",
          () -> new BlockItem(RailcraftBlocks.ELECTRIC_TURNOUT_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> ELECTRIC_JUNCTION_TRACK =
      deferredRegister.register("electric_junction_track",
          () -> new BlockItem(RailcraftBlocks.ELECTRIC_JUNCTION_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> ELECTRIC_LAUNCHER_TRACK =
      deferredRegister.register("electric_launcher_track",
          () -> new LauncherTrackBlockItem(RailcraftBlocks.ELECTRIC_LAUNCHER_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> ELECTRIC_ONE_WAY_TRACK =
      deferredRegister.register("electric_one_way_track",
          () -> new OneWayTrackBlockItem(RailcraftBlocks.ELECTRIC_ONE_WAY_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> ELECTRIC_LOCOMOTIVE_TRACK =
      deferredRegister.register("electric_locomotive_track",
          () -> new LocomotiveTrackBlockItem(RailcraftBlocks.ELECTRIC_LOCOMOTIVE_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> HIGH_SPEED_TRACK =
      deferredRegister.register("high_speed_track",
          () -> new BlockItem(RailcraftBlocks.HIGH_SPEED_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> HIGH_SPEED_TRANSITION_TRACK =
      deferredRegister.register("high_speed_transition_track",
          () -> new TransitionTrackBlockItem(RailcraftBlocks.HIGH_SPEED_TRANSITION_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> HIGH_SPEED_LOCKING_TRACK =
      deferredRegister.register("high_speed_locking_track",
          () -> new LockingTrackBlockItem(RailcraftBlocks.HIGH_SPEED_LOCKING_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> HIGH_SPEED_ACTIVATOR_TRACK =
      deferredRegister.register("high_speed_activator_track",
          () -> new ActivatorTrackBlockItem(RailcraftBlocks.HIGH_SPEED_ACTIVATOR_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> HIGH_SPEED_BOOSTER_TRACK =
      deferredRegister.register("high_speed_booster_track",
          () -> new BoosterTrackBlockItem(RailcraftBlocks.HIGH_SPEED_BOOSTER_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> HIGH_SPEED_DETECTOR_TRACK =
      deferredRegister.register("high_speed_detector_track",
          () -> new DetectorTrackBlockItem(RailcraftBlocks.HIGH_SPEED_DETECTOR_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> HIGH_SPEED_WYE_TRACK =
      deferredRegister.register("high_speed_wye_track",
          () -> new BlockItem(RailcraftBlocks.HIGH_SPEED_WYE_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> HIGH_SPEED_TURNOUT_TRACK =
      deferredRegister.register("high_speed_turnout_track",
          () -> new BlockItem(RailcraftBlocks.HIGH_SPEED_TURNOUT_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> HIGH_SPEED_JUNCTION_TRACK =
      deferredRegister.register("high_speed_junction_track",
          () -> new BlockItem(RailcraftBlocks.HIGH_SPEED_JUNCTION_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> HIGH_SPEED_LOCOMOTIVE_TRACK =
      deferredRegister.register("high_speed_locomotive_track",
          () -> new LocomotiveTrackBlockItem(RailcraftBlocks.HIGH_SPEED_LOCOMOTIVE_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> HIGH_SPEED_ELECTRIC_TRACK =
      deferredRegister.register("high_speed_electric_track",
          () -> new BlockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> HIGH_SPEED_ELECTRIC_TRANSITION_TRACK =
      deferredRegister.register("high_speed_electric_transition_track",
          () -> new TransitionTrackBlockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRANSITION_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> HIGH_SPEED_ELECTRIC_LOCKING_TRACK =
      deferredRegister.register("high_speed_electric_locking_track",
          () -> new LockingTrackBlockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCKING_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK =
      deferredRegister.register("high_speed_electric_activator_track",
          () -> new ActivatorTrackBlockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> HIGH_SPEED_ELECTRIC_BOOSTER_TRACK =
      deferredRegister.register("high_speed_electric_booster_track",
          () -> new BoosterTrackBlockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_BOOSTER_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> HIGH_SPEED_ELECTRIC_DETECTOR_TRACK =
      deferredRegister.register("high_speed_electric_detector_track",
          () -> new DetectorTrackBlockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_DETECTOR_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> HIGH_SPEED_ELECTRIC_WYE_TRACK =
      deferredRegister.register("high_speed_electric_wye_track",
          () -> new BlockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_WYE_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> HIGH_SPEED_ELECTRIC_TURNOUT_TRACK =
      deferredRegister.register("high_speed_electric_turnout_track",
          () -> new BlockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_TURNOUT_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> HIGH_SPEED_ELECTRIC_JUNCTION_TRACK =
      deferredRegister.register("high_speed_electric_junction_track",
          () -> new BlockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_JUNCTION_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> HIGH_SPEED_ELECTRIC_LOCOMOTIVE_TRACK =
      deferredRegister.register("high_speed_electric_locomotive_track",
          () -> new LocomotiveTrackBlockItem(RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCOMOTIVE_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> IRON_LOCKING_TRACK =
      deferredRegister.register("iron_locking_track",
          () -> new LockingTrackBlockItem(RailcraftBlocks.IRON_LOCKING_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> IRON_BUFFER_STOP_TRACK =
      deferredRegister.register("iron_buffer_stop_track",
          () -> new BufferStopTrackBlockItem(RailcraftBlocks.IRON_BUFFER_STOP_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> IRON_ACTIVATOR_TRACK =
      deferredRegister.register("iron_activator_track",
          () -> new ActivatorTrackBlockItem(RailcraftBlocks.IRON_ACTIVATOR_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> IRON_BOOSTER_TRACK =
      deferredRegister.register("iron_booster_track",
          () -> new BoosterTrackBlockItem(RailcraftBlocks.IRON_BOOSTER_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> IRON_CONTROL_TRACK =
      deferredRegister.register("iron_control_track",
          () -> new ControlTrackBlockItem(RailcraftBlocks.IRON_CONTROL_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> IRON_GATED_TRACK =
      deferredRegister.register("iron_gated_track",
          () -> new GatedTrackBlockItem(RailcraftBlocks.IRON_GATED_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> IRON_DETECTOR_TRACK =
      deferredRegister.register("iron_detector_track",
          () -> new DetectorTrackBlockItem(RailcraftBlocks.IRON_DETECTOR_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> IRON_COUPLER_TRACK =
      deferredRegister.register("iron_coupler_track",
          () -> new CouplerTrackBlockItem(RailcraftBlocks.IRON_COUPLER_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> IRON_EMBARKING_TRACK =
      deferredRegister.register("iron_embarking_track",
          () -> new EmbarkingTrackBlockItem(RailcraftBlocks.IRON_EMBARKING_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> IRON_DISEMBARKING_TRACK =
      deferredRegister.register("iron_disembarking_track",
          () -> new DisembarkingTrackBlockItem(RailcraftBlocks.IRON_DISEMBARKING_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> IRON_WYE_TRACK =
      deferredRegister.register("iron_wye_track",
          () -> new BlockItem(RailcraftBlocks.IRON_WYE_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> IRON_TURNOUT_TRACK =
      deferredRegister.register("iron_turnout_track",
          () -> new BlockItem(RailcraftBlocks.IRON_TURNOUT_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> IRON_JUNCTION_TRACK =
      deferredRegister.register("iron_junction_track",
          () -> new BlockItem(RailcraftBlocks.IRON_JUNCTION_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> IRON_LAUNCHER_TRACK =
      deferredRegister.register("iron_launcher_track",
          () -> new LauncherTrackBlockItem(RailcraftBlocks.IRON_LAUNCHER_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> IRON_ONE_WAY_TRACK =
      deferredRegister.register("iron_one_way_track",
          () -> new OneWayTrackBlockItem(RailcraftBlocks.IRON_ONE_WAY_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> IRON_LOCOMOTIVE_TRACK =
      deferredRegister.register("iron_locomotive_track",
          () -> new LocomotiveTrackBlockItem(RailcraftBlocks.IRON_LOCOMOTIVE_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> REINFORCED_TRACK =
      deferredRegister.register("reinforced_track",
          () -> new BlockItem(RailcraftBlocks.REINFORCED_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> REINFORCED_LOCKING_TRACK =
      deferredRegister.register("reinforced_locking_track",
          () -> new LockingTrackBlockItem(RailcraftBlocks.REINFORCED_LOCKING_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> REINFORCED_BUFFER_STOP_TRACK =
      deferredRegister.register("reinforced_buffer_stop_track",
          () -> new BufferStopTrackBlockItem(RailcraftBlocks.REINFORCED_BUFFER_STOP_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> REINFORCED_ACTIVATOR_TRACK =
      deferredRegister.register("reinforced_activator_track",
          () -> new ActivatorTrackBlockItem(RailcraftBlocks.REINFORCED_ACTIVATOR_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> REINFORCED_BOOSTER_TRACK =
      deferredRegister.register("reinforced_booster_track",
          () -> new BoosterTrackBlockItem(RailcraftBlocks.REINFORCED_BOOSTER_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> REINFORCED_CONTROL_TRACK =
      deferredRegister.register("reinforced_control_track",
          () -> new ControlTrackBlockItem(RailcraftBlocks.REINFORCED_CONTROL_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> REINFORCED_GATED_TRACK =
      deferredRegister.register("reinforced_gated_track",
          () -> new GatedTrackBlockItem(RailcraftBlocks.REINFORCED_GATED_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> REINFORCED_DETECTOR_TRACK =
      deferredRegister.register("reinforced_detector_track",
          () -> new DetectorTrackBlockItem(RailcraftBlocks.REINFORCED_DETECTOR_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> REINFORCED_COUPLER_TRACK =
      deferredRegister.register("reinforced_coupler_track",
          () -> new CouplerTrackBlockItem(RailcraftBlocks.REINFORCED_COUPLER_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> REINFORCED_EMBARKING_TRACK =
      deferredRegister.register("reinforced_embarking_track",
          () -> new EmbarkingTrackBlockItem(RailcraftBlocks.REINFORCED_EMBARKING_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> REINFORCED_DISEMBARKING_TRACK =
      deferredRegister.register("reinforced_disembarking_track",
          () -> new DisembarkingTrackBlockItem(RailcraftBlocks.REINFORCED_DISEMBARKING_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> REINFORCED_WYE_TRACK =
      deferredRegister.register("reinforced_wye_track",
          () -> new BlockItem(RailcraftBlocks.REINFORCED_WYE_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> REINFORCED_TURNOUT_TRACK =
      deferredRegister.register("reinforced_turnout_track",
          () -> new BlockItem(RailcraftBlocks.REINFORCED_TURNOUT_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> REINFORCED_JUNCTION_TRACK =
      deferredRegister.register("reinforced_junction_track",
          () -> new BlockItem(RailcraftBlocks.REINFORCED_JUNCTION_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> REINFORCED_LAUNCHER_TRACK =
      deferredRegister.register("reinforced_launcher_track",
          () -> new LauncherTrackBlockItem(RailcraftBlocks.REINFORCED_LAUNCHER_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> REINFORCED_ONE_WAY_TRACK =
      deferredRegister.register("reinforced_one_way_track",
          () -> new OneWayTrackBlockItem(RailcraftBlocks.REINFORCED_ONE_WAY_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> REINFORCED_LOCOMOTIVE_TRACK =
      deferredRegister.register("reinforced_locomotive_track",
          () -> new LocomotiveTrackBlockItem(RailcraftBlocks.REINFORCED_LOCOMOTIVE_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> STRAP_IRON_TRACK =
      deferredRegister.register("strap_iron_track",
          () -> new BlockItem(RailcraftBlocks.STRAP_IRON_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> STRAP_IRON_LOCKING_TRACK =
      deferredRegister.register("strap_iron_locking_track",
          () -> new LockingTrackBlockItem(RailcraftBlocks.STRAP_IRON_LOCKING_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> STRAP_IRON_BUFFER_STOP_TRACK =
      deferredRegister.register("strap_iron_buffer_stop_track",
          () -> new BufferStopTrackBlockItem(RailcraftBlocks.STRAP_IRON_BUFFER_STOP_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> STRAP_IRON_ACTIVATOR_TRACK =
      deferredRegister.register("strap_iron_activator_track",
          () -> new ActivatorTrackBlockItem(RailcraftBlocks.STRAP_IRON_ACTIVATOR_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> STRAP_IRON_BOOSTER_TRACK =
      deferredRegister.register("strap_iron_booster_track",
          () -> new BoosterTrackBlockItem(RailcraftBlocks.STRAP_IRON_BOOSTER_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> STRAP_IRON_CONTROL_TRACK =
      deferredRegister.register("strap_iron_control_track",
          () -> new ControlTrackBlockItem(RailcraftBlocks.STRAP_IRON_CONTROL_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> STRAP_IRON_GATED_TRACK =
      deferredRegister.register("strap_iron_gated_track",
          () -> new GatedTrackBlockItem(RailcraftBlocks.STRAP_IRON_GATED_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> STRAP_IRON_DETECTOR_TRACK =
      deferredRegister.register("strap_iron_detector_track",
          () -> new DetectorTrackBlockItem(RailcraftBlocks.STRAP_IRON_DETECTOR_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> STRAP_IRON_COUPLER_TRACK =
      deferredRegister.register("strap_iron_coupler_track",
          () -> new CouplerTrackBlockItem(RailcraftBlocks.STRAP_IRON_COUPLER_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> STRAP_IRON_EMBARKING_TRACK =
      deferredRegister.register("strap_iron_embarking_track",
          () -> new EmbarkingTrackBlockItem(RailcraftBlocks.STRAP_IRON_EMBARKING_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> STRAP_IRON_DISEMBARKING_TRACK =
      deferredRegister.register("strap_iron_disembarking_track",
          () -> new DisembarkingTrackBlockItem(RailcraftBlocks.STRAP_IRON_DISEMBARKING_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> STRAP_IRON_WYE_TRACK =
      deferredRegister.register("strap_iron_wye_track",
          () -> new BlockItem(RailcraftBlocks.STRAP_IRON_WYE_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> STRAP_IRON_TURNOUT_TRACK =
      deferredRegister.register("strap_iron_turnout_track",
          () -> new BlockItem(RailcraftBlocks.STRAP_IRON_TURNOUT_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> STRAP_IRON_JUNCTION_TRACK =
      deferredRegister.register("strap_iron_junction_track",
          () -> new BlockItem(RailcraftBlocks.STRAP_IRON_JUNCTION_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> STRAP_IRON_LAUNCHER_TRACK =
      deferredRegister.register("strap_iron_launcher_track",
          () -> new LauncherTrackBlockItem(RailcraftBlocks.STRAP_IRON_LAUNCHER_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> STRAP_IRON_ONE_WAY_TRACK =
      deferredRegister.register("strap_iron_one_way_track",
          () -> new OneWayTrackBlockItem(RailcraftBlocks.STRAP_IRON_ONE_WAY_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> STRAP_IRON_LOCOMOTIVE_TRACK =
      deferredRegister.register("strap_iron_locomotive_track",
          () -> new LocomotiveTrackBlockItem(RailcraftBlocks.STRAP_IRON_LOCOMOTIVE_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> ELEVATOR_TRACK =
      deferredRegister.register("elevator_track",
          () -> new BlockItem(RailcraftBlocks.ELEVATOR_TRACK.get(),
              new Item.Properties()));

  public static final RegistryObject<Item> IRON_CROWBAR =
      deferredRegister.register("iron_crowbar",
          () -> new CrowbarItem(2.5F, -2.8F, Tiers.IRON,
              new Item.Properties()));

  public static final RegistryObject<Item> STEEL_CROWBAR =
      deferredRegister.register("steel_crowbar",
          () -> new CrowbarItem(2.5F, -2.7F, RailcraftItemTier.STEEL,
              new Item.Properties()));

  public static final RegistryObject<Item> DIAMOND_CROWBAR =
      deferredRegister.register("diamond_crowbar",
          () -> new CrowbarItem(2.5F, -2.4F, Tiers.DIAMOND,
              new Item.Properties()));

  public static final RegistryObject<Item> SEASONS_CROWBAR =
      deferredRegister.register("seasons_crowbar",
          () -> new SeasonsCrowbarItem(new Item.Properties()));

  public static final RegistryObject<Item> TRACK_PARTS = registerBasic("track_parts");

  public static final RegistryObject<Item> TRANSITION_TRACK_KIT =
      deferredRegister.register("transition_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED, RailcraftBlocks.HIGH_SPEED_TRANSITION_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
                  RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRANSITION_TRACK)));

  public static final RegistryObject<Item> LOCKING_TRACK_KIT =
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

  public static final RegistryObject<Item> BUFFER_STOP_TRACK_KIT =
      deferredRegister.register("buffer_stop_track_kit",
          () -> new TrackKitItem( new TrackKitItem.Properties()
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_BUFFER_STOP_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_BUFFER_STOP_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_BUFFER_STOP_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED,
                  RailcraftBlocks.REINFORCED_BUFFER_STOP_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON,
                  RailcraftBlocks.STRAP_IRON_BUFFER_STOP_TRACK)));

  public static final RegistryObject<Item> ACTIVATOR_TRACK_KIT =
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
              .addOutfittedBlock(TrackTypes.STRAP_IRON, RailcraftBlocks.STRAP_IRON_ACTIVATOR_TRACK)));

  public static final RegistryObject<Item> BOOSTER_TRACK_KIT =
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

  public static final RegistryObject<Item> CONTROL_TRACK_KIT =
      deferredRegister.register("control_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_CONTROL_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_CONTROL_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_CONTROL_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_CONTROL_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON, RailcraftBlocks.STRAP_IRON_CONTROL_TRACK)));

  public static final RegistryObject<Item> GATED_TRACK_KIT =
      deferredRegister.register("gated_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_GATED_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_GATED_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_GATED_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_GATED_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON, RailcraftBlocks.STRAP_IRON_GATED_TRACK)));

  public static final RegistryObject<Item> DETECTOR_TRACK_KIT =
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
              .addOutfittedBlock(TrackTypes.STRAP_IRON, RailcraftBlocks.STRAP_IRON_DETECTOR_TRACK)));

  public static final RegistryObject<Item> COUPLER_TRACK_KIT =
      deferredRegister.register("coupler_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_COUPLER_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_COUPLER_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_COUPLER_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_COUPLER_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON, RailcraftBlocks.STRAP_IRON_COUPLER_TRACK)));

  public static final RegistryObject<Item> EMBARKING_TRACK_KIT =
      deferredRegister.register("embarking_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .setAllowedOnSlopes(true)
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_EMBARKING_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_EMBARKING_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_EMBARKING_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_EMBARKING_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON, RailcraftBlocks.STRAP_IRON_EMBARKING_TRACK)));

  public static final RegistryObject<Item> DISEMBARKING_TRACK_KIT =
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

  public static final RegistryObject<Item> LAUNCHER_TRACK_KIT =
      deferredRegister.register("launcher_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_LAUNCHER_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_LAUNCHER_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_LAUNCHER_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED,
                  RailcraftBlocks.REINFORCED_LAUNCHER_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON,
                  RailcraftBlocks.STRAP_IRON_LAUNCHER_TRACK)));

  public static final RegistryObject<Item> ONE_WAY_TRACK_KIT =
      deferredRegister.register("one_way_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_ONE_WAY_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_ONE_WAY_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_ONE_WAY_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED,
                  RailcraftBlocks.REINFORCED_ONE_WAY_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON,
                  RailcraftBlocks.STRAP_IRON_ONE_WAY_TRACK)));

  public static final RegistryObject<Item> LOCOMOTIVE_TRACK_KIT =
      deferredRegister.register("locomotive_track_kit",
          () -> new TrackKitItem(new TrackKitItem.Properties()
              .addOutfittedBlock(TrackTypes.ABANDONED, RailcraftBlocks.ABANDONED_LOCOMOTIVE_TRACK)
              .addOutfittedBlock(TrackTypes.ELECTRIC, RailcraftBlocks.ELECTRIC_LOCOMOTIVE_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED, RailcraftBlocks.HIGH_SPEED_LOCOMOTIVE_TRACK)
              .addOutfittedBlock(TrackTypes.HIGH_SPEED_ELECTRIC,
                  RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCOMOTIVE_TRACK)
              .addOutfittedBlock(TrackTypes.IRON, RailcraftBlocks.IRON_LOCOMOTIVE_TRACK)
              .addOutfittedBlock(TrackTypes.REINFORCED, RailcraftBlocks.REINFORCED_LOCOMOTIVE_TRACK)
              .addOutfittedBlock(TrackTypes.STRAP_IRON, RailcraftBlocks.STRAP_IRON_LOCOMOTIVE_TRACK)));

  public static final RegistryObject<Item> GOGGLES =
      deferredRegister.register("goggles",
          () -> new GogglesItem(new Item.Properties()));

  public static final RegistryObject<BlockItem> MANUAL_ROLLING_MACHINE =
      deferredRegister.register("manual_rolling_machine",
          () -> new BlockItem(RailcraftBlocks.MANUAL_ROLLING_MACHINE.get(),
              new Item.Properties()));

  public static final RegistryObject<BlockItem> CRUSHER =
      deferredRegister.register("crusher",
          () -> new BlockItem(RailcraftBlocks.CRUSHER.get(),
              new Item.Properties()));

  public static final RegistryObject<BlockItem> COKE_OVEN_BRICKS =
      deferredRegister.register("coke_oven_bricks",
          () -> new BlockItem(RailcraftBlocks.COKE_OVEN_BRICKS.get(),
              new Item.Properties()));

  public static final RegistryObject<BlockItem> CRUSHED_OBSIDIAN =
      deferredRegister.register("crushed_obsidian",
          () -> new BlockItem(RailcraftBlocks.CRUSHED_OBSIDIAN.get(),
              new Item.Properties()));

  // ================================================================================
  // Crafting Materials
  // ================================================================================

  public static final RegistryObject<Item> COAL_COKE = deferredRegister.register("coal_coke",
      () -> new CoalCokeItem(new Item.Properties()));

  public static final RegistryObject<Item> STEEL_PLATE = registerBasic("steel_plate");
  public static final RegistryObject<Item> IRON_PLATE = registerBasic("iron_plate");
  public static final RegistryObject<Item> TIN_PLATE = registerBasic("tin_plate");
  public static final RegistryObject<Item> GOLD_PLATE = registerBasic("gold_plate");
  public static final RegistryObject<Item> LEAD_PLATE = registerBasic("lead_plate");
  public static final RegistryObject<Item> ZINC_PLATE = registerBasic("zinc_plate");
  public static final RegistryObject<Item> BRASS_PLATE = registerBasic("brass_plate");
  public static final RegistryObject<Item> INVAR_PLATE = registerBasic("invar_plate");
  public static final RegistryObject<Item> BRONZE_PLATE = registerBasic("bronze_plate");
  public static final RegistryObject<Item> COPPER_PLATE = registerBasic("copper_plate");
  public static final RegistryObject<Item> NICKEL_PLATE = registerBasic("nickel_plate");
  public static final RegistryObject<Item> SILVER_PLATE = registerBasic("silver_plate");

  public static final RegistryObject<Item> STEEL_INGOT = registerBasic("steel_ingot");
  public static final RegistryObject<Item> TIN_INGOT = registerBasic("tin_ingot");
  public static final RegistryObject<Item> ZINC_INGOT = registerBasic("zinc_ingot");
  public static final RegistryObject<Item> BRASS_INGOT = registerBasic("brass_ingot");
  public static final RegistryObject<Item> BRONZE_INGOT = registerBasic("bronze_ingot");
  public static final RegistryObject<Item> NICKEL_INGOT = registerBasic("nickel_ingot");
  public static final RegistryObject<Item> INVAR_INGOT = registerBasic("invar_ingot");
  public static final RegistryObject<Item> LEAD_INGOT = registerBasic("lead_ingot");
  public static final RegistryObject<Item> SILVER_INGOT = registerBasic("silver_ingot");

  public static final RegistryObject<Item> SALTPETER_DUST = registerBasic("saltpeter_dust");
  public static final RegistryObject<Item> COAL_DUST = registerBasic("coal_dust");
  public static final RegistryObject<Item> CHARCOAL_DUST = registerBasic("charcoal_dust");
  public static final RegistryObject<Item> SLAG = registerBasic("slag");
  public static final RegistryObject<Item> ENDER_DUST = registerBasic("ender_dust");
  public static final RegistryObject<Item> SULFUR_DUST = registerBasic("sulfur_dust");
  public static final RegistryObject<Item> OBSIDIAN_DUST = registerBasic("obsidian_dust");

  public static final RegistryObject<Item> STEEL_NUGGET = registerBasic("steel_nugget");
  public static final RegistryObject<Item> TIN_NUGGET = registerBasic("tin_nugget");
  public static final RegistryObject<Item> ZINC_NUGGET = registerBasic("zinc_nugget");
  public static final RegistryObject<Item> BRASS_NUGGET = registerBasic("brass_nugget");
  public static final RegistryObject<Item> BRONZE_NUGGET = registerBasic("bronze_nugget");
  public static final RegistryObject<Item> NICKEL_NUGGET = registerBasic("nickel_nugget");
  public static final RegistryObject<Item> INVAR_NUGGET = registerBasic("invar_nugget");
  public static final RegistryObject<Item> SILVER_NUGGET = registerBasic("silver_nugget");
  public static final RegistryObject<Item> LEAD_NUGGET = registerBasic("lead_nugget");

  public static final RegistryObject<Item> TIN_RAW = registerBasic("tin_raw");
  public static final RegistryObject<Item> ZINC_RAW = registerBasic("zinc_raw");
  public static final RegistryObject<Item> NICKEL_RAW = registerBasic("nickel_raw");
  public static final RegistryObject<Item> SILVER_RAW = registerBasic("silver_raw");
  public static final RegistryObject<Item> LEAD_RAW = registerBasic("lead_raw");

  public static final RegistryObject<Item> BUSHING_GEAR = registerBasic("bushing_gear");
  public static final RegistryObject<Item> TIN_GEAR = registerBasic("tin_gear");
  public static final RegistryObject<Item> GOLD_GEAR = registerBasic("gold_gear");
  public static final RegistryObject<Item> IRON_GEAR = registerBasic("iron_gear");
  public static final RegistryObject<Item> LEAD_GEAR = registerBasic("lead_gear");
  public static final RegistryObject<Item> ZINC_GEAR = registerBasic("zinc_gear");
  public static final RegistryObject<Item> BRASS_GEAR = registerBasic("brass_gear");
  public static final RegistryObject<Item> INVAR_GEAR = registerBasic("invar_gear");
  public static final RegistryObject<Item> STEEL_GEAR = registerBasic("steel_gear");
  public static final RegistryObject<Item> BRONZE_GEAR = registerBasic("bronze_gear");
  public static final RegistryObject<Item> COPPER_GEAR = registerBasic("copper_gear");
  public static final RegistryObject<Item> NICKEL_GEAR = registerBasic("nickel_gear");
  public static final RegistryObject<Item> SILVER_GEAR = registerBasic("silver_gear");

  public static final RegistryObject<Item> TIN_ELECTRODE = registerBasic("tin_electrode");
  public static final RegistryObject<Item> GOLD_ELECTRODE = registerBasic("gold_electrode");
  public static final RegistryObject<Item> IRON_ELECTRODE = registerBasic("iron_electrode");
  public static final RegistryObject<Item> LEAD_ELECTRODE = registerBasic("lead_electrode");
  public static final RegistryObject<Item> ZINC_ELECTRODE = registerBasic("zinc_electrode");
  public static final RegistryObject<Item> BRASS_ELECTRODE = registerBasic("brass_electrode");
  public static final RegistryObject<Item> INVAR_ELECTRODE = registerBasic("invar_electrode");
  public static final RegistryObject<Item> STEEL_ELECTRODE = registerBasic("steel_electrode");
  public static final RegistryObject<Item> BRONZE_ELECTRODE = registerBasic("bronze_electrode");
  public static final RegistryObject<Item> CARBON_ELECTRODE = registerBasic("carbon_electrode");
  public static final RegistryObject<Item> COPPER_ELECTRODE = registerBasic("copper_electrode");
  public static final RegistryObject<Item> NICKEL_ELECTRODE = registerBasic("nickel_electrode");
  public static final RegistryObject<Item> SILVER_ELECTRODE = registerBasic("silver_electrode");

  public static final RegistryObject<Item> CONTROLLER_CIRCUIT = registerBasic("controller_circuit");
  public static final RegistryObject<Item> RECEIVER_CIRCUIT = registerBasic("receiver_circuit");
  public static final RegistryObject<Item> SIGNAL_CIRCUIT = registerBasic("signal_circuit");
  public static final RegistryObject<Item> RADIO_CIRCUIT = registerBasic("radio_circuit");

  public static final RegistryObject<Item> WOODEN_RAIL = registerBasic("wooden_rail");
  public static final RegistryObject<Item> STANDARD_RAIL = registerBasic("standard_rail");
  public static final RegistryObject<Item> ADVANCED_RAIL = registerBasic("advanced_rail");
  public static final RegistryObject<Item> REINFORCED_RAIL = registerBasic("reinforced_rail");
  public static final RegistryObject<Item> HIGH_SPEED_RAIL = registerBasic("high_speed_rail");
  public static final RegistryObject<Item> ELECTRIC_RAIL = registerBasic("electric_rail");

  public static final RegistryObject<Item> BAG_OF_CEMENT = registerBasic("bag_of_cement");

  public static final RegistryObject<Item> WOODEN_TIE = registerBasic("wooden_tie");
  public static final RegistryObject<Item> STONE_TIE = registerBasic("stone_tie");

  public static final RegistryObject<Item> REBAR = registerBasic("rebar");
  public static final RegistryObject<Item> WOODEN_RAILBED = registerBasic("wooden_railbed");
  public static final RegistryObject<Item> STONE_RAILBED = registerBasic("stone_railbed");

  public static final RegistryObject<Item> SIGNAL_LAMP = registerBasic("signal_lamp");

  public static final RegistryObject<Item> CHARGE_SPOOL_LARGE = registerBasic("charge_spool_large");
  public static final RegistryObject<Item> CHARGE_SPOOL_MEDIUM =
      registerBasic("charge_spool_medium");
  public static final RegistryObject<Item> CHARGE_SPOOL_SMALL = registerBasic("charge_spool_small");

  public static final RegistryObject<Item> CHARGE_MOTOR = registerBasic("charge_motor");

  public static final RegistryObject<Item> CHARGE_COIL = registerBasic("charge_coil");

  public static final RegistryObject<Item> CHARGE_TERMINAL = registerBasic("charge_terminal");

  public static final RegistryObject<BlockItem> WATER_TANK_SIDING =
      deferredRegister.register("water_tank_siding",
          () -> new BlockItem(RailcraftBlocks.WATER_TANK_SIDING.get(),
              new Item.Properties()));

  public static final RegistryObject<BlockItem> QUARRIED_STONE =
      deferredRegister.register("quarried_stone",
          () -> new BlockItem(RailcraftBlocks.QUARRIED_STONE.get(),
              new Item.Properties()));

  public static final RegistryObject<BlockItem> QUARRIED_COBBLESTONE =
      deferredRegister.register("quarried_cobblestone",
          () -> new BlockItem(RailcraftBlocks.QUARRIED_COBBLESTONE.get(),
              new Item.Properties()));

  public static final RegistryObject<BlockItem> POLISHED_QUARRIED_STONE =
      deferredRegister.register("polished_quarried_stone",
          () -> new BlockItem(RailcraftBlocks.POLISHED_QUARRIED_STONE.get(),
              new Item.Properties()));

  public static final RegistryObject<BlockItem> CHISELED_QUARRIED_STONE =
      deferredRegister.register("chiseled_quarried_stone",
          () -> new BlockItem(RailcraftBlocks.CHISELED_QUARRIED_STONE.get(),
              new Item.Properties()));

  public static final RegistryObject<BlockItem> ETCHED_QUARRIED_STONE =
      deferredRegister.register("etched_quarried_stone",
          () -> new BlockItem(RailcraftBlocks.ETCHED_QUARRIED_STONE.get(),
              new Item.Properties()));

  public static final RegistryObject<BlockItem> QUARRIED_BRICKS =
      deferredRegister.register("quarried_bricks",
          () -> new BlockItem(RailcraftBlocks.QUARRIED_BRICKS.get(),
              new Item.Properties()));

  public static final RegistryObject<BlockItem> QUARRIED_BRICK_STAIRS =
      deferredRegister.register("quarried_brick_stairs",
          () -> new BlockItem(RailcraftBlocks.QUARRIED_BRICK_STAIRS.get(),
              new Item.Properties()));

  public static final RegistryObject<BlockItem> QUARRIED_BRICK_SLAB =
      deferredRegister.register("quarried_brick_slab",
          () -> new BlockItem(RailcraftBlocks.QUARRIED_BRICK_SLAB.get(),
              new Item.Properties()));

  public static final RegistryObject<BlockItem> QUARRIED_PAVER =
      deferredRegister.register("quarried_paver",
          () -> new BlockItem(RailcraftBlocks.QUARRIED_PAVER.get(),
              new Item.Properties()));

  public static final RegistryObject<BlockItem> QUARRIED_PAVER_STAIRS =
      deferredRegister.register("quarried_paver_stairs",
          () -> new BlockItem(RailcraftBlocks.QUARRIED_PAVER_STAIRS.get(),
              new Item.Properties()));

  public static final RegistryObject<BlockItem> QUARRIED_PAVER_SLAB =
      deferredRegister.register("quarried_paver_slab",
          () -> new BlockItem(RailcraftBlocks.QUARRIED_PAVER_SLAB.get(),
              new Item.Properties()));

  // ================================================================================
  // Buckets
  // ================================================================================

  public static final RegistryObject<Item> CREOSOTE_BUCKET =
      deferredRegister.register("creosote_bucket",
          () -> new BucketItem(RailcraftFluids.CREOSOTE,
              new Item.Properties()
                  .stacksTo(1)
                  .craftRemainder(Items.BUCKET)) {
            @Override
            public ICapabilityProvider initCapabilities(ItemStack stack,
                @Nullable CompoundTag nbt) {
              return new FluidBucketWrapper(stack);
            }

            @Override
            public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
              return 800;
            }
          });

  public static final RegistryObject<Item> CREOSOTE_BOTTLE =
      deferredRegister.register("creosote_bottle",
          () -> new FluidBottleItem(RailcraftFluids.CREOSOTE,
              new Item.Properties()
                  .stacksTo(16)
                  .craftRemainder(Items.GLASS_BOTTLE)));

  // ================================================================================
  // Utils
  // ================================================================================

  private static Function<Block, BlockItem> blockItemFactory() {
    return block -> new BlockItem(block, new Item.Properties());
  }

  private static RegistryObject<Item> registerBasic(String name) {
    return deferredRegister.register(name,
        () -> new Item(new Item.Properties()));
  }
}
