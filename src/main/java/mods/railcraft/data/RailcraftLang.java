package mods.railcraft.data;

import java.util.HashMap;

import mods.railcraft.Railcraft;
import mods.railcraft.client.Translations;
import mods.railcraft.util.ColorMap;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;

public class RailcraftLang extends LanguageProvider {
  private final String MODID;
  private final HashMap<DyeColor, String> colorTranslations;

  public RailcraftLang(DataGenerator gen) {
    super(gen, Railcraft.ID, "en_us");
    this.MODID = Railcraft.ID;
    colorTranslations = new HashMap<>();
    colorTranslations.put(DyeColor.WHITE, "White");
    colorTranslations.put(DyeColor.ORANGE, "Orange");
    colorTranslations.put(DyeColor.MAGENTA, "Magenta");
    colorTranslations.put(DyeColor.LIGHT_BLUE, "Light Blue");
    colorTranslations.put(DyeColor.YELLOW, "Yellow");
    colorTranslations.put(DyeColor.LIME, "Lime");
    colorTranslations.put(DyeColor.PINK, "Pink");
    colorTranslations.put(DyeColor.GRAY, "Gray");
    colorTranslations.put(DyeColor.LIGHT_GRAY, "Light Gray");
    colorTranslations.put(DyeColor.CYAN, "Cyan");
    colorTranslations.put(DyeColor.PURPLE, "Purple");
    colorTranslations.put(DyeColor.BLUE, "Blue");
    colorTranslations.put(DyeColor.BROWN, "Brown");
    colorTranslations.put(DyeColor.GREEN, "Green");
    colorTranslations.put(DyeColor.RED, "Red");
    colorTranslations.put(DyeColor.BLACK, "Black");
  }

  @Override
  protected void addTranslations() {
    add("itemGroup." + MODID, "Railcraft");
    add("itemGroup." + MODID + "_outfitted_tracks", "Railcraft Outfitted Tracks");
    add("itemGroup." + MODID + "_decorative_blocks", "Railcraft Decorative Blocks");

    blockTranslations();
    itemTranslations();
    entityTranslations();
    fluidTranslations();
    containerTranslations();
    tipsTranslations();
    screenTranslations();
  }

  private void blockTranslations() {
    add(RailcraftBlocks.LOW_PRESSURE_STEAM_BOILER_TANK.get(), "Low Pressure Steam Boiler Tank");
    add(RailcraftBlocks.HIGH_PRESSURE_STEAM_BOILER_TANK.get(), "High Pressure Steam Boiler Tank");
    add(RailcraftBlocks.SOLID_FUELED_FIREBOX.get(),"Solid Fueled Firebox");
    add(RailcraftBlocks.FLUID_FUELED_FIREBOX.get(),"Fluid Fueled Firebox");
    add(RailcraftBlocks.STEAM_TURBINE.get(), "Steam Turbine Housing");
    add(RailcraftBlocks.BLAST_FURNACE_BRICKS.get(), "Blast Furnace Bricks");
    add(RailcraftBlocks.FEED_STATION.get(), "Feed Station");
    add(RailcraftBlocks.STEEL_BLOCK.get(), "Steel Block");
    add(RailcraftBlocks.STEEL_ANVIL.get(), "Steel Anvil");
    add(RailcraftBlocks.CHIPPED_STEEL_ANVIL.get(), "Chipped Steel Anvil");
    add(RailcraftBlocks.DAMAGED_STEEL_ANVIL.get(), "Damaged Steel Anvil");
    add(RailcraftBlocks.FLUID_LOADER.get(), "Fluid Loader");
    add(RailcraftBlocks.FLUID_UNLOADER.get(), "Fluid Unloader");
    add(RailcraftBlocks.ADVANCED_ITEM_LOADER.get(), "Advanced Item Loader");
    add(RailcraftBlocks.ADVANCED_ITEM_UNLOADER.get(), "Advanced Item Unloader");
    add(RailcraftBlocks.ITEM_LOADER.get(), "Item Loader");
    add(RailcraftBlocks.ITEM_UNLOADER.get(), "Item Unloader");
    add(RailcraftBlocks.SWITCH_TRACK_LEVER.get(), "Switch Track Lever");
    add(RailcraftBlocks.SWITCH_TRACK_MOTOR.get(), "Switch Track Motor");
    add(RailcraftBlocks.BLOCK_SIGNAL.get(), "Block Signal");
    add(RailcraftBlocks.DISTANT_SIGNAL.get(), "Distant Signal");
    add(RailcraftBlocks.TOKEN_SIGNAL.get(), "Token Signal");
    add(RailcraftBlocks.DUAL_BLOCK_SIGNAL.get(), "Dual-Head Block Signal");
    add(RailcraftBlocks.DUAL_DISTANT_SIGNAL.get(), "Dual-Head Distant Signal");
    add(RailcraftBlocks.DUAL_TOKEN_SIGNAL.get(), "Dual-Head Token Signal");
    add(RailcraftBlocks.SIGNAL_CONTROLLER_BOX.get(), "Signal Controller Box");
    add(RailcraftBlocks.SIGNAL_RECEIVER_BOX.get(), "Signal Receiver Box");
    add(RailcraftBlocks.SIGNAL_CAPACITOR_BOX.get(), "Signal Capacitor Box");
    add(RailcraftBlocks.ANALOG_SIGNAL_CONTROLLER_BOX.get(), "Analog Signal Controller Box");
    add(RailcraftBlocks.SIGNAL_INTERLOCK_BOX.get(), "Signal Interlock Box");
    add(RailcraftBlocks.SIGNAL_SEQUENCER_BOX.get(), "Signal Sequencer Box");
    add(RailcraftBlocks.BLOCK_SIGNAL_RELAY_BOX.get(), "Block Signal Relay Box");
    add(RailcraftBlocks.MANUAL_ROLLING_MACHINE.get(), "Manual Rolling Machine");
    add(RailcraftBlocks.COKE_OVEN_BRICKS.get(), "Coke Oven Bricks");
    add(RailcraftBlocks.FORCE_TRACK_EMITTER.get(), "Force Track Emitter");

    colorBlock(RailcraftBlocks.STRENGTHENED_GLASS, "Strengthened Glass");
    colorBlock(RailcraftBlocks.POST, "Post");
    colorBlock(RailcraftBlocks.IRON_TANK_GAUGE, "Iron Tank Gauge");
    colorBlock(RailcraftBlocks.IRON_TANK_VALVE, "Iron Tank Valve");
    colorBlock(RailcraftBlocks.IRON_TANK_WALL, "Iron Tank Wall");
    colorBlock(RailcraftBlocks.STEEL_TANK_GAUGE, "Steel Tank Gauge");
    colorBlock(RailcraftBlocks.STEEL_TANK_VALVE, "Steel Tank Valve");
    colorBlock(RailcraftBlocks.STEEL_TANK_WALL, "Steel Tank Wall");

    trackTranslations();
  }

  private void itemTranslations() {
    add(RailcraftItems.SIGNAL_LABEL.get(), "Signal Label");
    add(RailcraftItems.SIGNAL_LAMP.get(), "Signal Lamp");
    add(RailcraftItems.TURBINE_ROTOR.get(), "Turbine Rotor");
    add(RailcraftItems.SLAG.get(), "Ground Blast Furnace Slag");
    add(RailcraftItems.COAL_COKE.get(), "Coal Coke");
    add(RailcraftItems.STEEL_SHEARS.get(), "Steel Shears");
    add(RailcraftItems.STEEL_SWORD.get(), "Steel Sword");
    add(RailcraftItems.STEEL_SHOVEL.get(), "Steel Shovel");
    add(RailcraftItems.STEEL_PICKAXE.get(), "Steel Pickaxe");
    add(RailcraftItems.STEEL_AXE.get(), "Steel Axe");
    add(RailcraftItems.STEEL_HOE.get(), "Steel Hoe");
    add(RailcraftItems.STEEL_BOOTS.get(), "Steel Boots");
    add(RailcraftItems.STEEL_CHESTPLATE.get(), "Steel Chestplate");
    add(RailcraftItems.STEEL_HELMET.get(), "Steel Helmet");
    add(RailcraftItems.STEEL_LEGGINGS.get(), "Steel Leggings");
    add(RailcraftItems.IRON_TUNNEL_BORE_HEAD.get(), "Iron Tunnel Bore Head");
    add(RailcraftItems.BRONZE_TUNNEL_BORE_HEAD.get(), "Bronze Tunnel Bore Head");
    add(RailcraftItems.STEEL_TUNNEL_BORE_HEAD.get(), "Steel Tunnel Bore Head");
    add(RailcraftItems.DIAMOND_TUNNEL_BORE_HEAD.get(), "Diamond Tunnel Bore Head");
    add(RailcraftItems.TANK_MINECART.get(), "Minecart with Tank");
    add(RailcraftItems.CRACKED_FIRESTONE.get(), "Cracked Firestone");
    add(RailcraftItems.RAW_FIRESTONE.get(), "Raw Firestone");
    add(RailcraftItems.CUT_FIRESTONE.get(), "Cut Firestone");
    add(RailcraftItems.REFINED_FIRESTONE.get(), "Refined Firestone");
    add(RailcraftItems.CREATIVE_LOCOMOTIVE.get(), "Creative Locomotive");
    add(RailcraftItems.STEAM_LOCOMOTIVE.get(), "Steam Locomotive");
    add(RailcraftItems.ELECTRIC_LOCOMOTIVE.get(), "Electric Locomotive");
    add(RailcraftItems.IRON_CROWBAR.get(), "Iron Crowbar");
    add(RailcraftItems.STEEL_CROWBAR.get(), "Steel Crowbar");
    add(RailcraftItems.DIAMOND_CROWBAR.get(), "Diamond Crowbar");
    add(RailcraftItems.SEASONS_CROWBAR.get(), "Seasons Crowbar");
    add(RailcraftItems.GOGGLES.get(), "Trackman's Goggles");
    add(RailcraftItems.SIGNAL_BLOCK_SURVEYOR.get(), "Signal Block Surveyor");
    add(RailcraftItems.SIGNAL_TUNER.get(), "Signal Tuner");
    add(RailcraftItems.OVERALLS.get(), "Overalls");
    add(RailcraftItems.TICKET.get(), "Ticket");
    add(RailcraftItems.WHISTLE_TUNER.get(), "Whistle Tuner");
    add(RailcraftItems.TUNNEL_BORE.get(), "Tunnel Bore");
    add(RailcraftItems.TRACK_LAYER.get(), "Track Layer");
    add(RailcraftItems.TRACK_REMOVER.get(), "Track Remover");
    add(RailcraftItems.IRON_SPIKE_MAUL.get(), "Iron Spike Maul");
    add(RailcraftItems.STEEL_SPIKE_MAUL.get(), "Steel Spike Maul");
    add(RailcraftItems.DIAMOND_SPIKE_MAUL.get(), "Diamond Spike Maul");
    add(RailcraftItems.STEEL_INGOT.get(), "Steel Ingot");
    add(RailcraftItems.TIN_INGOT.get(), "Tin Ingot");
    add(RailcraftItems.ZINC_INGOT.get(), "Zinc Ingot");
    add(RailcraftItems.BRASS_INGOT.get(), "Brass Ingot");
    add(RailcraftItems.BRONZE_INGOT.get(), "Bronze Ingot");
    add(RailcraftItems.STEEL_NUGGET.get(), "Steel Nugget");
    add(RailcraftItems.TIN_NUGGET.get(), "Tin Nugget");
    add(RailcraftItems.ZINC_NUGGET.get(), "Zinc Nugget");
    add(RailcraftItems.BRASS_NUGGET.get(), "Brass Nugget");
    add(RailcraftItems.BRONZE_NUGGET.get(), "Bronze Nugget");
    add(RailcraftItems.STEEL_PLATE.get(), "Steel Plate");
    add(RailcraftItems.IRON_PLATE.get(), "Iron Plate");
    add(RailcraftItems.CONTROLLER_CIRCUIT.get(), "Controller Circuit");
    add(RailcraftItems.RECEIVER_CIRCUIT.get(), "Receiver Circuit");
    add(RailcraftItems.SIGNAL_CIRCUIT.get(), "Signal Circuit");
    add(RailcraftItems.WOODEN_TIE.get(), "Wooden Tie");
    add(RailcraftItems.STONE_TIE.get(), "Stone Tie");
    add(RailcraftItems.WOODEN_RAILBED.get(), "Wooden Railbed");
    add(RailcraftItems.STONE_RAILBED.get(), "Stone Railbed");
    add(RailcraftItems.REBAR.get(), "Rebar");
    add(RailcraftItems.WOODEN_RAIL.get(), "Wooden Rail");
    add(RailcraftItems.STANDARD_RAIL.get(), "Standard Rail");
    add(RailcraftItems.ADVANCED_RAIL.get(), "Advanced Rail");
    add(RailcraftItems.REINFORCED_RAIL.get(), "Reinforced Rail");
    add(RailcraftItems.HIGH_SPEED_RAIL.get(), "High Speed Rail");
    add(RailcraftItems.ELECTRIC_RAIL.get(), "Electric Rail");
    add(RailcraftItems.CREOSOTE_BUCKET.get(), "Creosote Bucket");
    add(RailcraftItems.TRANSITION_TRACK_KIT.get(), "Transition Track Kit");
    add(RailcraftItems.LOCKING_TRACK_KIT.get(), "Locking Track Kit");
    add(RailcraftItems.BUFFER_STOP_TRACK_KIT.get(), "Buffer Stop Track Kit");
    add(RailcraftItems.ACTIVATOR_TRACK_KIT.get(), "Activator Track Kit");
    add(RailcraftItems.BOOSTER_TRACK_KIT.get(), "Booster Track Kit");
    add(RailcraftItems.CONTROL_TRACK_KIT.get(), "Control Track Kit");
    add(RailcraftItems.GATED_TRACK_KIT.get(), "Gated Track Kit");
    add(RailcraftItems.DETECTOR_TRACK_KIT.get(), "Detector Track Kit");
    add(RailcraftItems.COUPLER_TRACK_KIT.get(), "Coupler Track Kit");
    add(RailcraftItems.EMBARKING_TRACK_KIT.get(), "Embarking Track Kit");
    add(RailcraftItems.DISEMBARKING_TRACK_KIT.get(), "Disembarking Track Kit");
    add(RailcraftItems.LAUNCHER_TRACK_KIT.get(), "Launcher Track Kit");
  }

  private void entityTranslations() {
    add(RailcraftEntityTypes.TANK_MINECART.get(), "Minecart with Tank");
    add(RailcraftEntityTypes.CREATIVE_LOCOMOTIVE.get(), "Creative Locomotive");
    add(RailcraftEntityTypes.STEAM_LOCOMOTIVE.get(), "Steam Locomotive");
    add(RailcraftEntityTypes.ELECTRIC_LOCOMOTIVE.get(), "Electric Locomotive");
    add(RailcraftEntityTypes.TUNNEL_BORE.get(), "Tunnel Bore");
  }

  private void fluidTranslations() {
    add("fluid_type." + MODID + ".steam", "Steam");
    add("fluid_type." + MODID + ".creosote", "Creosote");
  }

  private void containerTranslations() {
    add(Translations.Container.MANUAL_ROLLING_MACHINE, "Manual Rolling Machine");
    add(Translations.Container.COKE_OVEN, "Coke Oven");
    add(Translations.Container.BLAST_FURNACE, "Blast Furnace");
    add(Translations.Container.TANK, "Tank");
    add(Translations.Container.STEAM_TURBINE, "Steam Turbine");
    add(Translations.Container.SOLID_FUELED_STEAM_BOILER, "Solid Fueled Steam Boiler");
    add(Translations.Container.FLUID_FUELED_STEAM_BOILER, "Fluid Fueled Steam Boiler");
  }

  private void tipsTranslations() {
    add(Translations.Tips.ROUTING_TICKET_ISSUER, "Issuer:");
    add(Translations.Tips.ROUTING_TICKET_DEST, "Destination:");
    add(Translations.Tips.ROUTING_TICKET_BLANK, "Blank Ticket");
    add(Translations.Tips.LOCOMOTIVE_SLOT_TICKET, "Insert Ticket");
    add(Translations.Tips.LOCOMOTIVE_ITEM_OWNER, "Owner: %s");
    add(Translations.Tips.LOCOMOTIVE_ITEM_PRIMARY, "Primary descriptor.(EDIT ME)");
    add(Translations.Tips.LOCOMOTIVE_ITEM_SECONDARY, "Secondary descriptor.(EDIT ME)");
    add(Translations.Tips.LOCOMOTIVE_ITEM_WHISTLE, "There is currently no whistle installed.");
  }

  private void screenTranslations() {
    add(Translations.Screen.STEAM_TURBINE_ROTOR, "Rotor:");
    add(Translations.Screen.STEAM_TURBINE_OUTPUT, "Output:");
    add(Translations.Screen.STEAM_TURBINE_USAGE, "Usage:");
    add(Translations.Screen.STEAM_MODE_DESC_IDLE, "Locomotive reduces fuel usage, but retains its heat. If the train is held by a track, it behaves as if idle.");
    add(Translations.Screen.STEAM_MODE_DESC_RUNNING, "Makes the locomotive move.");
    add(Translations.Screen.STEAM_MODE_DESC_SHUTDOWN, "Shuts off the locomotive. Halts all movement and begins the cool-down process.");
    add(Translations.Screen.LOCOMOTIVE_LOCK_LOCKED, "This cart is locked to %s. It will only accept tickets issued by them or an operator.");
    add(Translations.Screen.LOCOMOTIVE_LOCK_UNLOCKED, "Click to lock locomotive. Once locked, it will only accept tickets issued by you or an operator.");
    add(Translations.Screen.LOCOMOTIVE_LOCK_PRIVATE, "This cart is private to %s. It can only be controlled by them or an operator.");
    add(Translations.Screen.LOCOMOTIVE_MODE_SHUTDOWN, "Shutdown");
    add(Translations.Screen.LOCOMOTIVE_MODE_RUNNING, "Running");
    add(Translations.Screen.LOCOMOTIVE_MODE_IDLE, "Idle");
    add(Translations.Screen.SINGAL_CONTROLLER_BOX_DEFAULT, "Default aspect:");
    add(Translations.Screen.SINGAL_CONTROLLER_BOX_POWERED, "Powered aspect:");
    add(Translations.Screen.SIGNAL_CAPACITOR_BOX_DURATION, "Duration: %s");
    add(Translations.Screen.ACTION_SIGNAL_BOX_LOCKED, "This signal box is locked to %s. It can only be modified by them or an operator.");
    add(Translations.Screen.ACTION_SIGNAL_BOX_UNLOCKED, "Click to lock signal box. Once locked, it will only be modifiable by you or an operator");
    add(Translations.Screen.SWITCH_TRACK_MOTOR_REDSTONE, "Redstone Triggered");
    add(Translations.Screen.CART_FILTERS, "Carts");
    add(Translations.Screen.ITEM_MANIPULATOR_FILTERS, "Filters");
    add(Translations.Screen.ITEM_MANIPULATOR_BUFFER, "Buffer");
    add(Translations.Screen.TUNNEL_BORE_HEAD, "Head");
    add(Translations.Screen.TUNNEL_BORE_FUEL, "Fuel");
    add(Translations.Screen.TUNNEL_BORE_BALLAST, "Ballast");
    add(Translations.Screen.TUNNEL_BORE_TRACK, "Track");
    add(Translations.Screen.MULTIBLOCK_ASSEMBLY_FAILED, "Multiblock Assembly Failed");
    add(Translations.Screen.EMBARKING_TRACK_RADIUS, "Radius: %s");
    add(Translations.Screen.LAUNCHER_TRACK_LAUNCH_FORCE, "Launch Force: %s");
  }

  private void trackTranslations() {
    add(RailcraftBlocks.ABANDONED_TRACK.get(), "Abandoned Track");
    add(RailcraftBlocks.ABANDONED_LOCKING_TRACK.get(), "Abandoned Locking Track");
    add(RailcraftBlocks.ABANDONED_BUFFER_STOP_TRACK.get(), "Abandoned Buffer Stop Track");
    add(RailcraftBlocks.ABANDONED_ACTIVATOR_TRACK.get(), "Abandoned Activator Track");
    add(RailcraftBlocks.ABANDONED_BOOSTER_TRACK.get(), "Abandoned Booster Track");
    add(RailcraftBlocks.ABANDONED_CONTROL_TRACK.get(), "Abandoned Control Track");
    add(RailcraftBlocks.ABANDONED_GATED_TRACK.get(), "Abandoned Gated Track");
    add(RailcraftBlocks.ABANDONED_DETECTOR_TRACK.get(), "Abandoned Detector Track");
    add(RailcraftBlocks.ABANDONED_COUPLER_TRACK.get(), "Abandoned Coupler Track");
    add(RailcraftBlocks.ABANDONED_EMBARKING_TRACK.get(), "Abandoned Embarking Track");
    add(RailcraftBlocks.ABANDONED_DISEMBARKING_TRACK.get(), "Abandoned Disembarking Track");
    add(RailcraftBlocks.ABANDONED_TURNOUT_TRACK.get(), "Abandoned Turnout Track");
    add(RailcraftBlocks.ABANDONED_WYE_TRACK.get(), "Abandoned Wye Track");
    add(RailcraftBlocks.ABANDONED_JUNCTION_TRACK.get(), "Abandoned Junction Track");
    add(RailcraftBlocks.ABANDONED_LAUNCHER_TRACK.get(), "Abandoned Launcher Track");

    add(RailcraftBlocks.ELECTRIC_TRACK.get(), "Electric Track");
    add(RailcraftBlocks.ELECTRIC_LOCKING_TRACK.get(), "Electric Locking Track");
    add(RailcraftBlocks.ELECTRIC_BUFFER_STOP_TRACK.get(), "Electric Buffer Stop Track");
    add(RailcraftBlocks.ELECTRIC_ACTIVATOR_TRACK.get(), "Electric Activator Track");
    add(RailcraftBlocks.ELECTRIC_BOOSTER_TRACK.get(), "Electric Booster Track");
    add(RailcraftBlocks.ELECTRIC_CONTROL_TRACK.get(), "Electric Control Track");
    add(RailcraftBlocks.ELECTRIC_GATED_TRACK.get(), "Electric Gated Track");
    add(RailcraftBlocks.ELECTRIC_DETECTOR_TRACK.get(), "Electric Detector Track");
    add(RailcraftBlocks.ELECTRIC_COUPLER_TRACK.get(), "Electric Coupler Track");
    add(RailcraftBlocks.ELECTRIC_EMBARKING_TRACK.get(), "Electric Embarking Track");
    add(RailcraftBlocks.ELECTRIC_DISEMBARKING_TRACK.get(), "Electric Disembarking Track");
    add(RailcraftBlocks.ELECTRIC_TURNOUT_TRACK.get(), "Electric Turnout Track");
    add(RailcraftBlocks.ELECTRIC_WYE_TRACK.get(), "Electric Wye Track");
    add(RailcraftBlocks.ELECTRIC_JUNCTION_TRACK.get(), "Electric Junction Track");
    add(RailcraftBlocks.ELECTRIC_LAUNCHER_TRACK.get(), "Electric Launcher Track");

    add(RailcraftBlocks.HIGH_SPEED_TRACK.get(), "High Speed Track");
    add(RailcraftBlocks.HIGH_SPEED_LOCKING_TRACK.get(), "High Speed Locking Track");
    add(RailcraftBlocks.HIGH_SPEED_ACTIVATOR_TRACK.get(), "High Speed Activator Track");
    add(RailcraftBlocks.HIGH_SPEED_BOOSTER_TRACK.get(), "High Speed Booster Track");
    add(RailcraftBlocks.HIGH_SPEED_DETECTOR_TRACK.get(), "High Speed Detector Track");
    add(RailcraftBlocks.HIGH_SPEED_TURNOUT_TRACK.get(), "High Speed Turnout Track");
    add(RailcraftBlocks.HIGH_SPEED_WYE_TRACK.get(), "High Speed Wye Track");
    add(RailcraftBlocks.HIGH_SPEED_JUNCTION_TRACK.get(), "High Speed Junction Track");
    add(RailcraftBlocks.HIGH_SPEED_TRANSITION_TRACK.get(), "High Speed Transition Track");
    //"block.railcraft.high_speed_buffer_stop_track": "High Speed Buffer Stop Track",
    //"block.railcraft.high_speed_control_track": "High Speed Control Track",

    add(RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRACK.get(), "High Speed Electric Track");
    add(RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCKING_TRACK.get(), "High Speed Electric Locking Track");
    add(RailcraftBlocks.HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK.get(), "High Speed Electric Activator Track");
    add(RailcraftBlocks.HIGH_SPEED_ELECTRIC_BOOSTER_TRACK.get(), "High Speed Electric Booster Track");
    add(RailcraftBlocks.HIGH_SPEED_ELECTRIC_DETECTOR_TRACK.get(), "High Speed Electric Detector Track");
    add(RailcraftBlocks.HIGH_SPEED_ELECTRIC_TURNOUT_TRACK.get(), "High Speed Electric Turnout Track");
    add(RailcraftBlocks.HIGH_SPEED_ELECTRIC_WYE_TRACK.get(), "High Speed Electric Wye Track");
    add(RailcraftBlocks.HIGH_SPEED_ELECTRIC_JUNCTION_TRACK.get(), "High Speed Electric Junction Track");
    //"block.railcraft.high_speed_electric_transition_track": "High Speed Electric Transition Track",
    //"block.railcraft.high_speed_electric_buffer_stop_track": "High Speed Electric Buffer Stop Track",
    //"block.railcraft.high_speed_electric_control_track": "High Speed Electric Control Track",
    //"block.railcraft.high_speed_electric_detector_track": "High Speed Electric Detector Track",

    add(RailcraftBlocks.REINFORCED_TRACK.get(), "Reinforced Track");
    add(RailcraftBlocks.REINFORCED_LOCKING_TRACK.get(), "Reinforced Locking Track");
    add(RailcraftBlocks.REINFORCED_BUFFER_STOP_TRACK.get(), "Reinforced Buffer Stop Track");
    add(RailcraftBlocks.REINFORCED_ACTIVATOR_TRACK.get(), "Reinforced Activator Track");
    add(RailcraftBlocks.REINFORCED_BOOSTER_TRACK.get(), "Reinforced Booster Track");
    add(RailcraftBlocks.REINFORCED_CONTROL_TRACK.get(), "Reinforced Control Track");
    add(RailcraftBlocks.REINFORCED_GATED_TRACK.get(), "Reinforced Gated Track");
    add(RailcraftBlocks.REINFORCED_DETECTOR_TRACK.get(), "Reinforced Detector Track");
    add(RailcraftBlocks.REINFORCED_COUPLER_TRACK.get(), "Reinforced Coupler Track");
    add(RailcraftBlocks.REINFORCED_EMBARKING_TRACK.get(), "Reinforced Embarking Track");
    add(RailcraftBlocks.REINFORCED_DISEMBARKING_TRACK.get(), "Reinforced Disembarking Track");
    add(RailcraftBlocks.REINFORCED_TURNOUT_TRACK.get(), "Reinforced Turnout Track");
    add(RailcraftBlocks.REINFORCED_WYE_TRACK.get(), "Reinforced Wye Track");
    add(RailcraftBlocks.REINFORCED_JUNCTION_TRACK.get(), "Reinforced Junction Track");
    add(RailcraftBlocks.REINFORCED_LAUNCHER_TRACK.get(), "Reinforced Launcher Track");

    add(RailcraftBlocks.STRAP_IRON_TRACK.get(), "Strap Iron Track");
    add(RailcraftBlocks.STRAP_IRON_LOCKING_TRACK.get(), "Strap Iron Locking Track");
    add(RailcraftBlocks.STRAP_IRON_BUFFER_STOP_TRACK.get(), "Strap Iron Buffer Stop Track");
    add(RailcraftBlocks.STRAP_IRON_ACTIVATOR_TRACK.get(), "Strap Iron Activator Track");
    add(RailcraftBlocks.STRAP_IRON_BOOSTER_TRACK.get(), "Strap Iron Booster Track");
    add(RailcraftBlocks.STRAP_IRON_CONTROL_TRACK.get(), "Strap Iron Control Track");
    add(RailcraftBlocks.STRAP_IRON_GATED_TRACK.get(), "Strap Iron Gated Track");
    add(RailcraftBlocks.STRAP_IRON_DETECTOR_TRACK.get(), "Strap Iron Detector Track");
    add(RailcraftBlocks.STRAP_IRON_COUPLER_TRACK.get(), "Strap Iron Coupler Track");
    add(RailcraftBlocks.STRAP_IRON_EMBARKING_TRACK.get(), "Strap Iron Embarking Track");
    add(RailcraftBlocks.STRAP_IRON_DISEMBARKING_TRACK.get(), "Strap Iron Disembarking Track");
    add(RailcraftBlocks.STRAP_IRON_TURNOUT_TRACK.get(), "Strap Iron Turnout Track");
    add(RailcraftBlocks.STRAP_IRON_WYE_TRACK.get(), "Strap Iron Wye Track");
    add(RailcraftBlocks.STRAP_IRON_JUNCTION_TRACK.get(), "Strap Iron Junction Track");
    add(RailcraftBlocks.STRAP_IRON_LAUNCHER_TRACK.get(), "Strap Iron Launcher Track");

    add(RailcraftBlocks.IRON_LOCKING_TRACK.get(), "Iron Locking Track");
    add(RailcraftBlocks.IRON_BUFFER_STOP_TRACK.get(), "Iron Buffer Stop Track");
    add(RailcraftBlocks.IRON_ACTIVATOR_TRACK.get(), "Iron Activator Track");
    add(RailcraftBlocks.IRON_BOOSTER_TRACK.get(), "Iron Booster Track");
    add(RailcraftBlocks.IRON_CONTROL_TRACK.get(), "Iron Control Track");
    add(RailcraftBlocks.IRON_GATED_TRACK.get(), "Iron Gated Track");
    add(RailcraftBlocks.IRON_DETECTOR_TRACK.get(), "Iron Detector Track");
    add(RailcraftBlocks.IRON_COUPLER_TRACK.get(), "Iron Coupler Track");
    add(RailcraftBlocks.IRON_EMBARKING_TRACK.get(), "Iron Embarking Track");
    add(RailcraftBlocks.IRON_DISEMBARKING_TRACK.get(), "Iron Disembarking Track");
    add(RailcraftBlocks.IRON_TURNOUT_TRACK.get(), "Iron Turnout Track");
    add(RailcraftBlocks.IRON_WYE_TRACK.get(), "Iron Wye Track");
    add(RailcraftBlocks.IRON_JUNCTION_TRACK.get(), "Iron Junction Track");
    add(RailcraftBlocks.IRON_LAUNCHER_TRACK.get(), "Iron Launcher Track");

    add(RailcraftBlocks.ELEVATOR_TRACK.get(), "Elevator Track");
  }

  private <T extends Block> void colorBlock(ColorMap<T> blocks, String name) {
    blocks.forEach((color, block) -> {
      add(block.get(), colorTranslations.get(color) + " " + name);
    });
  }
}
