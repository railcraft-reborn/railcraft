package mods.railcraft.data;

import java.util.function.Function;
import java.util.function.Supplier;
import mods.railcraft.Railcraft;
import mods.railcraft.Translations;
import mods.railcraft.util.VariantRegistrar;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.material.fluid.RailcraftFluidTypes;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.fluids.FluidType;

public class RailcraftLanguageProvider extends LanguageProvider {

  private static final String LOCALE = "en_us";

  public RailcraftLanguageProvider(DataGenerator gen) {
    super(gen, Railcraft.ID, LOCALE);
  }

  @Override
  protected void addTranslations() {
    this.add(Translations.Tab.RAILCRAFT, "Railcraft");
    this.add(Translations.Tab.RAILCRAFT_OUTFITTED_TRACKS, "Railcraft Outfitted Tracks");
    this.add(Translations.Tab.RAILCRAFT_DECORATIVE_BLOCKS, "Railcraft Decorative Blocks");

    this.add(Translations.Misc.SIGNAL_SURVEYOR_INVALID_TRACK, "§cNo Track Found Near %s");
    this.add(Translations.Misc.SIGNAL_SURVEYOR_BEGIN, "§dBeginning Survey");
    this.add(Translations.Misc.SIGNAL_SURVEYOR_SUCCESS, "§aSuccessfully Paired Signals");
    this.add(Translations.Misc.SIGNAL_SURVEYOR_INVALID_PAIR, "§cPairing Invalid");
    this.add(Translations.Misc.SIGNAL_SURVEYOR_LOST, "§cFirst Signal No Longer Exists");
    this.add(Translations.Misc.SIGNAL_SURVEYOR_UNLOADED,
        "§cFirst Signal's Chunk Has Been Unloaded");
    this.add(Translations.Misc.SIGNAL_SURVEYOR_ABANDONED, "§dSurvey Abandoned");
    this.add(Translations.Misc.SIGNAL_SURVEYOR_INVALID_BLOCK, "§cNot a Valid Signal");

    this.add(Translations.Misc.SIGNAL_TUNER_BEGIN, "§dStarted Pairing %s§d With a Receiver");
    this.add(Translations.Misc.SIGNAL_TUNER_ABANDONED, "§dStopped Pairing");
    this.add(Translations.Misc.SIGNAL_TUNER_UNLOADED,
        "§cSignal Controller's Chunk Has Been Unloaded");
    this.add(Translations.Misc.SIGNAL_TUNER_LOST, "§cSignal Controller No Longer Exists");
    this.add(Translations.Misc.SIGNAL_TUNER_SUCCESS, "§aSuccessfully Paired Signal Receiver");

    this.blockTranslations();
    this.itemTranslations();
    this.entityTranslations();
    this.fluidTranslations();
    this.containerTranslations();
    this.tipsTranslations();
    this.screenTranslations();
    this.jeiTranslations();
  }

  private void blockTranslations() {
    this.addBlock(RailcraftBlocks.LOW_PRESSURE_STEAM_BOILER_TANK,
        "Low Pressure Steam Boiler Tank");
    this.addBlock(RailcraftBlocks.HIGH_PRESSURE_STEAM_BOILER_TANK,
        "High Pressure Steam Boiler Tank");
    this.addBlock(RailcraftBlocks.SOLID_FUELED_FIREBOX, "Solid Fueled Firebox");
    this.addBlock(RailcraftBlocks.FLUID_FUELED_FIREBOX, "Fluid Fueled Firebox");
    this.addBlock(RailcraftBlocks.STEAM_TURBINE, "Steam Turbine Housing");
    this.addBlock(RailcraftBlocks.BLAST_FURNACE_BRICKS, "Blast Furnace Bricks");
    this.addBlock(RailcraftBlocks.FEED_STATION, "Feed Station");
    this.addBlock(RailcraftBlocks.STEEL_BLOCK, "Steel Block");
    this.addBlock(RailcraftBlocks.STEEL_ANVIL, "Steel Anvil");
    this.addBlock(RailcraftBlocks.CHIPPED_STEEL_ANVIL, "Chipped Steel Anvil");
    this.addBlock(RailcraftBlocks.DAMAGED_STEEL_ANVIL, "Damaged Steel Anvil");
    this.addBlock(RailcraftBlocks.FLUID_LOADER, "Fluid Loader");
    this.addBlock(RailcraftBlocks.FLUID_UNLOADER, "Fluid Unloader");
    this.addBlock(RailcraftBlocks.ADVANCED_ITEM_LOADER, "Advanced Item Loader");
    this.addBlock(RailcraftBlocks.ADVANCED_ITEM_UNLOADER, "Advanced Item Unloader");
    this.addBlock(RailcraftBlocks.ITEM_LOADER, "Item Loader");
    this.addBlock(RailcraftBlocks.ITEM_UNLOADER, "Item Unloader");
    this.addBlock(RailcraftBlocks.SWITCH_TRACK_LEVER, "Switch Track Lever");
    this.addBlock(RailcraftBlocks.SWITCH_TRACK_MOTOR, "Switch Track Motor");
    this.addBlock(RailcraftBlocks.BLOCK_SIGNAL, "Block Signal");
    this.addBlock(RailcraftBlocks.DISTANT_SIGNAL, "Distant Signal");
    this.addBlock(RailcraftBlocks.TOKEN_SIGNAL, "Token Signal");
    this.addBlock(RailcraftBlocks.DUAL_BLOCK_SIGNAL, "Dual-Head Block Signal");
    this.addBlock(RailcraftBlocks.DUAL_DISTANT_SIGNAL, "Dual-Head Distant Signal");
    this.addBlock(RailcraftBlocks.DUAL_TOKEN_SIGNAL, "Dual-Head Token Signal");
    this.addBlock(RailcraftBlocks.SIGNAL_CONTROLLER_BOX, "Signal Controller Box");
    this.addBlock(RailcraftBlocks.SIGNAL_RECEIVER_BOX, "Signal Receiver Box");
    this.addBlock(RailcraftBlocks.SIGNAL_CAPACITOR_BOX, "Signal Capacitor Box");
    this.addBlock(RailcraftBlocks.ANALOG_SIGNAL_CONTROLLER_BOX, "Analog Signal Controller Box");
    this.addBlock(RailcraftBlocks.SIGNAL_INTERLOCK_BOX, "Signal Interlock Box");
    this.addBlock(RailcraftBlocks.SIGNAL_SEQUENCER_BOX, "Signal Sequencer Box");
    this.addBlock(RailcraftBlocks.BLOCK_SIGNAL_RELAY_BOX, "Block Signal Relay Box");
    this.addBlock(RailcraftBlocks.MANUAL_ROLLING_MACHINE, "Manual Rolling Machine");
    this.addBlock(RailcraftBlocks.COKE_OVEN_BRICKS, "Coke Oven Bricks");
    this.addBlock(RailcraftBlocks.FORCE_TRACK_EMITTER, "Force Track Emitter");

    this.addBlockColorVariants(RailcraftBlocks.STRENGTHENED_GLASS, "Strengthened Glass");
    this.addBlockColorVariants(RailcraftBlocks.POST, "Post");
    this.addBlockColorVariants(RailcraftBlocks.IRON_TANK_GAUGE, "Iron Tank Gauge");
    this.addBlockColorVariants(RailcraftBlocks.IRON_TANK_VALVE, "Iron Tank Valve");
    this.addBlockColorVariants(RailcraftBlocks.IRON_TANK_WALL, "Iron Tank Wall");
    this.addBlockColorVariants(RailcraftBlocks.STEEL_TANK_GAUGE, "Steel Tank Gauge");
    this.addBlockColorVariants(RailcraftBlocks.STEEL_TANK_VALVE, "Steel Tank Valve");
    this.addBlockColorVariants(RailcraftBlocks.STEEL_TANK_WALL, "Steel Tank Wall");

    this.trackTranslations();
  }

  private void itemTranslations() {
    this.addItem(RailcraftItems.SIGNAL_LABEL, "Signal Label");
    this.addItem(RailcraftItems.SIGNAL_LAMP, "Signal Lamp");
    this.addItem(RailcraftItems.TURBINE_ROTOR, "Turbine Rotor");
    this.addItem(RailcraftItems.TURBINE_BLADE, "Turbine Blade");
    this.addItem(RailcraftItems.SLAG, "Ground Blast Furnace Slag");
    this.addItem(RailcraftItems.COAL_COKE, "Coal Coke");
    this.addItem(RailcraftItems.STEEL_SHEARS, "Steel Shears");
    this.addItem(RailcraftItems.STEEL_SWORD, "Steel Sword");
    this.addItem(RailcraftItems.STEEL_SHOVEL, "Steel Shovel");
    this.addItem(RailcraftItems.STEEL_PICKAXE, "Steel Pickaxe");
    this.addItem(RailcraftItems.STEEL_AXE, "Steel Axe");
    this.addItem(RailcraftItems.STEEL_HOE, "Steel Hoe");
    this.addItem(RailcraftItems.STEEL_BOOTS, "Steel Boots");
    this.addItem(RailcraftItems.STEEL_CHESTPLATE, "Steel Chestplate");
    this.addItem(RailcraftItems.STEEL_HELMET, "Steel Helmet");
    this.addItem(RailcraftItems.STEEL_LEGGINGS, "Steel Leggings");
    this.addItem(RailcraftItems.IRON_TUNNEL_BORE_HEAD, "Iron Tunnel Bore Head");
    this.addItem(RailcraftItems.BRONZE_TUNNEL_BORE_HEAD, "Bronze Tunnel Bore Head");
    this.addItem(RailcraftItems.STEEL_TUNNEL_BORE_HEAD, "Steel Tunnel Bore Head");
    this.addItem(RailcraftItems.DIAMOND_TUNNEL_BORE_HEAD, "Diamond Tunnel Bore Head");
    this.addItem(RailcraftItems.TANK_MINECART, "Minecart with Tank");
    this.addItem(RailcraftItems.CRACKED_FIRESTONE, "Cracked Firestone");
    this.addItem(RailcraftItems.RAW_FIRESTONE, "Raw Firestone");
    this.addItem(RailcraftItems.CUT_FIRESTONE, "Cut Firestone");
    this.addItem(RailcraftItems.REFINED_FIRESTONE, "Refined Firestone");
    this.addItem(RailcraftItems.CREATIVE_LOCOMOTIVE, "Creative Locomotive");
    this.addItem(RailcraftItems.STEAM_LOCOMOTIVE, "Steam Locomotive");
    this.addItem(RailcraftItems.ELECTRIC_LOCOMOTIVE, "Electric Locomotive");
    this.addItem(RailcraftItems.IRON_CROWBAR, "Iron Crowbar");
    this.addItem(RailcraftItems.STEEL_CROWBAR, "Steel Crowbar");
    this.addItem(RailcraftItems.DIAMOND_CROWBAR, "Diamond Crowbar");
    this.addItem(RailcraftItems.SEASONS_CROWBAR, "Seasons Crowbar");
    this.addItem(RailcraftItems.GOGGLES, "Trackman's Goggles");
    this.addItem(RailcraftItems.SIGNAL_BLOCK_SURVEYOR, "Signal Block Surveyor");
    this.addItem(RailcraftItems.SIGNAL_TUNER, "Signal Tuner");
    this.addItem(RailcraftItems.OVERALLS, "Overalls");
    this.addItem(RailcraftItems.TICKET, "Ticket");
    this.addItem(RailcraftItems.WHISTLE_TUNER, "Whistle Tuner");
    this.addItem(RailcraftItems.TUNNEL_BORE, "Tunnel Bore");
    this.addItem(RailcraftItems.TRACK_LAYER, "Track Layer");
    this.addItem(RailcraftItems.TRACK_REMOVER, "Track Remover");
    this.addItem(RailcraftItems.IRON_SPIKE_MAUL, "Iron Spike Maul");
    this.addItem(RailcraftItems.STEEL_SPIKE_MAUL, "Steel Spike Maul");
    this.addItem(RailcraftItems.DIAMOND_SPIKE_MAUL, "Diamond Spike Maul");
    this.addItem(RailcraftItems.STEEL_INGOT, "Steel Ingot");
    this.addItem(RailcraftItems.TIN_INGOT, "Tin Ingot");
    this.addItem(RailcraftItems.ZINC_INGOT, "Zinc Ingot");
    this.addItem(RailcraftItems.BRASS_INGOT, "Brass Ingot");
    this.addItem(RailcraftItems.NICKEL_INGOT, "Nickel Ingot");
    this.addItem(RailcraftItems.INVAR_INGOT, "Invar Ingot");
    this.addItem(RailcraftItems.BRONZE_INGOT, "Bronze Ingot");
    this.addItem(RailcraftItems.SILVER_INGOT, "Silver Ingot");
    this.addItem(RailcraftItems.LEAD_INGOT, "Lead Ingot");
    this.addItem(RailcraftItems.STEEL_NUGGET, "Steel Nugget");
    this.addItem(RailcraftItems.TIN_NUGGET, "Tin Nugget");
    this.addItem(RailcraftItems.ZINC_NUGGET, "Zinc Nugget");
    this.addItem(RailcraftItems.BRASS_NUGGET, "Brass Nugget");
    this.addItem(RailcraftItems.BRONZE_NUGGET, "Bronze Nugget");
    this.addItem(RailcraftItems.NICKEL_NUGGET, "Nickel Nugget");
    this.addItem(RailcraftItems.INVAR_NUGGET, "Invar Nugget");
    this.addItem(RailcraftItems.SILVER_NUGGET, "Silver Nugget");
    this.addItem(RailcraftItems.LEAD_NUGGET, "Lead Nugget");
    this.addItem(RailcraftItems.STEEL_PLATE, "Steel Plate");
    this.addItem(RailcraftItems.IRON_PLATE, "Iron Plate");
    this.addItem(RailcraftItems.TIN_PLATE, "Tin Plate");
    this.addItem(RailcraftItems.GOLD_PLATE, "Gold Plate");
    this.addItem(RailcraftItems.LEAD_PLATE, "Lead Plate");
    this.addItem(RailcraftItems.ZINC_PLATE, "Zinc Plate");
    this.addItem(RailcraftItems.BRASS_PLATE, "Brass Plate");
    this.addItem(RailcraftItems.INVAR_PLATE, "Invar Plate");
    this.addItem(RailcraftItems.BRONZE_PLATE, "Bronze Plate");
    this.addItem(RailcraftItems.COPPER_PLATE, "Copper Plate");
    this.addItem(RailcraftItems.NICKEL_PLATE, "Nickel Plate");
    this.addItem(RailcraftItems.SILVER_PLATE, "Silver Plate");
    this.addItem(RailcraftItems.BUSHING_GEAR, "Gear Bushing");
    this.addItem(RailcraftItems.STEEL_GEAR, "Steel Gear");
    this.addItem(RailcraftItems.IRON_GEAR, "Iron Gear");
    this.addItem(RailcraftItems.TIN_GEAR, "Tin Gear");
    this.addItem(RailcraftItems.GOLD_GEAR, "Gold Gear");
    this.addItem(RailcraftItems.LEAD_GEAR, "Lead Gear");
    this.addItem(RailcraftItems.ZINC_GEAR, "Zinc Gear");
    this.addItem(RailcraftItems.BRASS_GEAR, "Brass Gear");
    this.addItem(RailcraftItems.INVAR_GEAR, "Invar Gear");
    this.addItem(RailcraftItems.BRONZE_GEAR, "Bronze Gear");
    this.addItem(RailcraftItems.COPPER_GEAR, "Copper Gear");
    this.addItem(RailcraftItems.NICKEL_GEAR, "Nickel Gear");
    this.addItem(RailcraftItems.SILVER_GEAR, "Silver Gear");
    this.addItem(RailcraftItems.STEEL_ELECTRODE, "Steel Electrode");
    this.addItem(RailcraftItems.IRON_ELECTRODE, "Iron Electrode");
    this.addItem(RailcraftItems.TIN_ELECTRODE, "Tin Electrode");
    this.addItem(RailcraftItems.GOLD_ELECTRODE, "Gold Electrode");
    this.addItem(RailcraftItems.LEAD_ELECTRODE, "Lead Electrode");
    this.addItem(RailcraftItems.ZINC_ELECTRODE, "Zinc Electrode");
    this.addItem(RailcraftItems.BRASS_ELECTRODE, "Brass Electrode");
    this.addItem(RailcraftItems.INVAR_ELECTRODE, "Invar Electrode");
    this.addItem(RailcraftItems.BRONZE_ELECTRODE, "Bronze Electrode");
    this.addItem(RailcraftItems.COPPER_ELECTRODE, "Copper Electrode");
    this.addItem(RailcraftItems.CARBON_ELECTRODE, "Carbon Electrode");
    this.addItem(RailcraftItems.NICKEL_ELECTRODE, "Nickel Electrode");
    this.addItem(RailcraftItems.SILVER_ELECTRODE, "Silver Electrode");
    this.addItem(RailcraftItems.SALTPETER_DUST, "Saltpeter Dust");
    this.addItem(RailcraftItems.COAL_DUST, "Coal Dust");
    this.addItem(RailcraftItems.CHARCOAL_DUST, "Charcoal Dust");
    this.addItem(RailcraftItems.SLAG_DUST, "Slag Dust");
    this.addItem(RailcraftItems.ENDER_DUST, "Ender Dust");
    this.addItem(RailcraftItems.SULFUR_DUST, "Sulfur Dust");
    this.addItem(RailcraftItems.OBSIDIAN_DUST, "Obsidian Dust");
    this.addItem(RailcraftItems.CONTROLLER_CIRCUIT, "Controller Circuit");
    this.addItem(RailcraftItems.RECEIVER_CIRCUIT, "Receiver Circuit");
    this.addItem(RailcraftItems.SIGNAL_CIRCUIT, "Signal Circuit");
    this.addItem(RailcraftItems.WOODEN_TIE, "Wooden Tie");
    this.addItem(RailcraftItems.STONE_TIE, "Stone Tie");
    this.addItem(RailcraftItems.WOODEN_RAILBED, "Wooden Railbed");
    this.addItem(RailcraftItems.STONE_RAILBED, "Stone Railbed");
    this.addItem(RailcraftItems.REBAR, "Rebar");
    this.addItem(RailcraftItems.WOODEN_RAIL, "Wooden Rail");
    this.addItem(RailcraftItems.STANDARD_RAIL, "Standard Rail");
    this.addItem(RailcraftItems.ADVANCED_RAIL, "Advanced Rail");
    this.addItem(RailcraftItems.REINFORCED_RAIL, "Reinforced Rail");
    this.addItem(RailcraftItems.HIGH_SPEED_RAIL, "High Speed Rail");
    this.addItem(RailcraftItems.ELECTRIC_RAIL, "Electric Rail");
    this.addItem(RailcraftItems.CREOSOTE_BUCKET, "Creosote Bucket");
    this.addItem(RailcraftItems.TRACK_PARTS, "Track Parts");
    this.addItem(RailcraftItems.TRANSITION_TRACK_KIT, "Transition Track Kit");
    this.addItem(RailcraftItems.LOCKING_TRACK_KIT, "Locking Track Kit");
    this.addItem(RailcraftItems.BUFFER_STOP_TRACK_KIT, "Buffer Stop Track Kit");
    this.addItem(RailcraftItems.ACTIVATOR_TRACK_KIT, "Activator Track Kit");
    this.addItem(RailcraftItems.BOOSTER_TRACK_KIT, "Booster Track Kit");
    this.addItem(RailcraftItems.CONTROL_TRACK_KIT, "Control Track Kit");
    this.addItem(RailcraftItems.GATED_TRACK_KIT, "Gated Track Kit");
    this.addItem(RailcraftItems.DETECTOR_TRACK_KIT, "Detector Track Kit");
    this.addItem(RailcraftItems.COUPLER_TRACK_KIT, "Coupler Track Kit");
    this.addItem(RailcraftItems.EMBARKING_TRACK_KIT, "Embarking Track Kit");
    this.addItem(RailcraftItems.DISEMBARKING_TRACK_KIT, "Disembarking Track Kit");
    this.addItem(RailcraftItems.LAUNCHER_TRACK_KIT, "Launcher Track Kit");
  }

  private void entityTranslations() {
    this.addEntityType(RailcraftEntityTypes.TANK_MINECART, "Minecart with Tank");
    this.addEntityType(RailcraftEntityTypes.CREATIVE_LOCOMOTIVE, "Creative Locomotive");
    this.addEntityType(RailcraftEntityTypes.STEAM_LOCOMOTIVE, "Steam Locomotive");
    this.addEntityType(RailcraftEntityTypes.ELECTRIC_LOCOMOTIVE, "Electric Locomotive");
    this.addEntityType(RailcraftEntityTypes.TUNNEL_BORE, "Tunnel Bore");
  }

  private void fluidTranslations() {
    this.addFluidType(RailcraftFluidTypes.STEAM, "Steam");
    this.addFluidType(RailcraftFluidTypes.CREOSOTE, "Creosote");
  }

  private void containerTranslations() {
    this.add(Translations.Container.MANUAL_ROLLING_MACHINE, "Manual Rolling Machine");
    this.add(Translations.Container.COKE_OVEN, "Coke Oven");
    this.add(Translations.Container.BLAST_FURNACE, "Blast Furnace");
    this.add(Translations.Container.TANK, "Tank");
    this.add(Translations.Container.STEAM_TURBINE, "Steam Turbine");
    this.add(Translations.Container.SOLID_FUELED_STEAM_BOILER, "Solid Fueled Steam Boiler");
    this.add(Translations.Container.FLUID_FUELED_STEAM_BOILER, "Fluid Fueled Steam Boiler");
  }

  private void tipsTranslations() {
    this.add(Translations.Tips.ROUTING_TICKET_ISSUER, "Issuer:");
    this.add(Translations.Tips.ROUTING_TICKET_DEST, "Destination:");
    this.add(Translations.Tips.ROUTING_TICKET_BLANK, "Blank Ticket");
    this.add(Translations.Tips.LOCOMOTIVE_SLOT_TICKET, "Insert Ticket");
    this.add(Translations.Tips.LOCOMOTIVE_ITEM_OWNER, "Owner: %s");
    this.add(Translations.Tips.LOCOMOTIVE_ITEM_PRIMARY, "Primary descriptor.(EDIT ME)");
    this.add(Translations.Tips.LOCOMOTIVE_ITEM_SECONDARY, "Secondary descriptor.(EDIT ME)");
    this.add(Translations.Tips.LOCOMOTIVE_ITEM_WHISTLE, "There is currently no whistle installed.");
  }

  private void screenTranslations() {
    this.add(Translations.Screen.STEAM_TURBINE_ROTOR, "Rotor:");
    this.add(Translations.Screen.STEAM_TURBINE_OUTPUT, "Output:");
    this.add(Translations.Screen.STEAM_TURBINE_USAGE, "Usage:");
    this.add(Translations.Screen.STEAM_MODE_DESC_IDLE,
        "Locomotive reduces fuel usage, but retains its heat. If the train is held by a track, it behaves as if idle.");
    this.add(Translations.Screen.STEAM_MODE_DESC_RUNNING, "Makes the locomotive move.");
    this.add(Translations.Screen.STEAM_MODE_DESC_SHUTDOWN,
        "Shuts off the locomotive. Halts all movement and begins the cool-down process.");
    this.add(Translations.Screen.LOCOMOTIVE_LOCK_LOCKED,
        "This cart is locked to %s. It will only accept tickets issued by them or an operator.");
    this.add(Translations.Screen.LOCOMOTIVE_LOCK_UNLOCKED,
        "Click to lock locomotive. Once locked, it will only accept tickets issued by you or an operator.");
    this.add(Translations.Screen.LOCOMOTIVE_LOCK_PRIVATE,
        "This cart is private to %s. It can only be controlled by them or an operator.");
    this.add(Translations.Screen.LOCOMOTIVE_MODE_SHUTDOWN, "Shutdown");
    this.add(Translations.Screen.LOCOMOTIVE_MODE_RUNNING, "Running");
    this.add(Translations.Screen.LOCOMOTIVE_MODE_IDLE, "Idle");
    this.add(Translations.Screen.SINGAL_CONTROLLER_BOX_DEFAULT, "Default aspect:");
    this.add(Translations.Screen.SINGAL_CONTROLLER_BOX_POWERED, "Powered aspect:");
    this.add(Translations.Screen.SIGNAL_CAPACITOR_BOX_DURATION, "Duration: %s");
    this.add(Translations.Screen.ACTION_SIGNAL_BOX_LOCKED,
        "This signal box is locked to %s. It can only be modified by them or an operator.");
    this.add(Translations.Screen.ACTION_SIGNAL_BOX_UNLOCKED,
        "Click to lock signal box. Once locked, it will only be modifiable by you or an operator");
    this.add(Translations.Screen.SWITCH_TRACK_MOTOR_REDSTONE, "Redstone Triggered");
    this.add(Translations.Screen.CART_FILTERS, "Carts");
    this.add(Translations.Screen.ITEM_MANIPULATOR_FILTERS, "Filters");
    this.add(Translations.Screen.ITEM_MANIPULATOR_BUFFER, "Buffer");
    this.add(Translations.Screen.TUNNEL_BORE_HEAD, "Head");
    this.add(Translations.Screen.TUNNEL_BORE_FUEL, "Fuel");
    this.add(Translations.Screen.TUNNEL_BORE_BALLAST, "Ballast");
    this.add(Translations.Screen.TUNNEL_BORE_TRACK, "Track");
    this.add(Translations.Screen.MULTIBLOCK_ASSEMBLY_FAILED, "Multiblock Assembly Failed");
    this.add(Translations.Screen.EMBARKING_TRACK_RADIUS, "Radius: %s");
    this.add(Translations.Screen.LAUNCHER_TRACK_LAUNCH_FORCE, "Launch Force: %s");
  }

  private void trackTranslations() {
    this.addBlock(RailcraftBlocks.ABANDONED_TRACK, "Abandoned Track");
    this.addBlock(RailcraftBlocks.ABANDONED_LOCKING_TRACK, "Abandoned Locking Track");
    this.addBlock(RailcraftBlocks.ABANDONED_BUFFER_STOP_TRACK, "Abandoned Buffer Stop Track");
    this.addBlock(RailcraftBlocks.ABANDONED_ACTIVATOR_TRACK, "Abandoned Activator Track");
    this.addBlock(RailcraftBlocks.ABANDONED_BOOSTER_TRACK, "Abandoned Booster Track");
    this.addBlock(RailcraftBlocks.ABANDONED_CONTROL_TRACK, "Abandoned Control Track");
    this.addBlock(RailcraftBlocks.ABANDONED_GATED_TRACK, "Abandoned Gated Track");
    this.addBlock(RailcraftBlocks.ABANDONED_DETECTOR_TRACK, "Abandoned Detector Track");
    this.addBlock(RailcraftBlocks.ABANDONED_COUPLER_TRACK, "Abandoned Coupler Track");
    this.addBlock(RailcraftBlocks.ABANDONED_EMBARKING_TRACK, "Abandoned Embarking Track");
    this.addBlock(RailcraftBlocks.ABANDONED_DISEMBARKING_TRACK, "Abandoned Disembarking Track");
    this.addBlock(RailcraftBlocks.ABANDONED_TURNOUT_TRACK, "Abandoned Turnout Track");
    this.addBlock(RailcraftBlocks.ABANDONED_WYE_TRACK, "Abandoned Wye Track");
    this.addBlock(RailcraftBlocks.ABANDONED_JUNCTION_TRACK, "Abandoned Junction Track");
    this.addBlock(RailcraftBlocks.ABANDONED_LAUNCHER_TRACK, "Abandoned Launcher Track");

    this.addBlock(RailcraftBlocks.ELECTRIC_TRACK, "Electric Track");
    this.addBlock(RailcraftBlocks.ELECTRIC_LOCKING_TRACK, "Electric Locking Track");
    this.addBlock(RailcraftBlocks.ELECTRIC_BUFFER_STOP_TRACK, "Electric Buffer Stop Track");
    this.addBlock(RailcraftBlocks.ELECTRIC_ACTIVATOR_TRACK, "Electric Activator Track");
    this.addBlock(RailcraftBlocks.ELECTRIC_BOOSTER_TRACK, "Electric Booster Track");
    this.addBlock(RailcraftBlocks.ELECTRIC_CONTROL_TRACK, "Electric Control Track");
    this.addBlock(RailcraftBlocks.ELECTRIC_GATED_TRACK, "Electric Gated Track");
    this.addBlock(RailcraftBlocks.ELECTRIC_DETECTOR_TRACK, "Electric Detector Track");
    this.addBlock(RailcraftBlocks.ELECTRIC_COUPLER_TRACK, "Electric Coupler Track");
    this.addBlock(RailcraftBlocks.ELECTRIC_EMBARKING_TRACK, "Electric Embarking Track");
    this.addBlock(RailcraftBlocks.ELECTRIC_DISEMBARKING_TRACK, "Electric Disembarking Track");
    this.addBlock(RailcraftBlocks.ELECTRIC_TURNOUT_TRACK, "Electric Turnout Track");
    this.addBlock(RailcraftBlocks.ELECTRIC_WYE_TRACK, "Electric Wye Track");
    this.addBlock(RailcraftBlocks.ELECTRIC_JUNCTION_TRACK, "Electric Junction Track");
    this.addBlock(RailcraftBlocks.ELECTRIC_LAUNCHER_TRACK, "Electric Launcher Track");

    this.addBlock(RailcraftBlocks.HIGH_SPEED_TRACK, "High Speed Track");
    this.addBlock(RailcraftBlocks.HIGH_SPEED_LOCKING_TRACK, "High Speed Locking Track");
    this.addBlock(RailcraftBlocks.HIGH_SPEED_ACTIVATOR_TRACK, "High Speed Activator Track");
    this.addBlock(RailcraftBlocks.HIGH_SPEED_BOOSTER_TRACK, "High Speed Booster Track");
    this.addBlock(RailcraftBlocks.HIGH_SPEED_DETECTOR_TRACK, "High Speed Detector Track");
    this.addBlock(RailcraftBlocks.HIGH_SPEED_TURNOUT_TRACK, "High Speed Turnout Track");
    this.addBlock(RailcraftBlocks.HIGH_SPEED_WYE_TRACK, "High Speed Wye Track");
    this.addBlock(RailcraftBlocks.HIGH_SPEED_JUNCTION_TRACK, "High Speed Junction Track");
    this.addBlock(RailcraftBlocks.HIGH_SPEED_TRANSITION_TRACK, "High Speed Transition Track");

    this.addBlock(RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRACK,
        "High Speed Electric Track");
    this.addBlock(RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRANSITION_TRACK,
        "High Speed Electric Transition Track");
    this.addBlock(RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCKING_TRACK,
        "High Speed Electric Locking Track");
    this.addBlock(RailcraftBlocks.HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK,
        "High Speed Electric Activator Track");
    this.addBlock(RailcraftBlocks.HIGH_SPEED_ELECTRIC_BOOSTER_TRACK,
        "High Speed Electric Booster Track");
    this.addBlock(RailcraftBlocks.HIGH_SPEED_ELECTRIC_DETECTOR_TRACK,
        "High Speed Electric Detector Track");
    this.addBlock(RailcraftBlocks.HIGH_SPEED_ELECTRIC_TURNOUT_TRACK,
        "High Speed Electric Turnout Track");
    this.addBlock(RailcraftBlocks.HIGH_SPEED_ELECTRIC_WYE_TRACK,
        "High Speed Electric Wye Track");
    this.addBlock(RailcraftBlocks.HIGH_SPEED_ELECTRIC_JUNCTION_TRACK,
        "High Speed Electric Junction Track");

    this.addBlock(RailcraftBlocks.REINFORCED_TRACK, "Reinforced Track");
    this.addBlock(RailcraftBlocks.REINFORCED_LOCKING_TRACK, "Reinforced Locking Track");
    this.addBlock(RailcraftBlocks.REINFORCED_BUFFER_STOP_TRACK, "Reinforced Buffer Stop Track");
    this.addBlock(RailcraftBlocks.REINFORCED_ACTIVATOR_TRACK, "Reinforced Activator Track");
    this.addBlock(RailcraftBlocks.REINFORCED_BOOSTER_TRACK, "Reinforced Booster Track");
    this.addBlock(RailcraftBlocks.REINFORCED_CONTROL_TRACK, "Reinforced Control Track");
    this.addBlock(RailcraftBlocks.REINFORCED_GATED_TRACK, "Reinforced Gated Track");
    this.addBlock(RailcraftBlocks.REINFORCED_DETECTOR_TRACK, "Reinforced Detector Track");
    this.addBlock(RailcraftBlocks.REINFORCED_COUPLER_TRACK, "Reinforced Coupler Track");
    this.addBlock(RailcraftBlocks.REINFORCED_EMBARKING_TRACK, "Reinforced Embarking Track");
    this.addBlock(RailcraftBlocks.REINFORCED_DISEMBARKING_TRACK, "Reinforced Disembarking Track");
    this.addBlock(RailcraftBlocks.REINFORCED_TURNOUT_TRACK, "Reinforced Turnout Track");
    this.addBlock(RailcraftBlocks.REINFORCED_WYE_TRACK, "Reinforced Wye Track");
    this.addBlock(RailcraftBlocks.REINFORCED_JUNCTION_TRACK, "Reinforced Junction Track");
    this.addBlock(RailcraftBlocks.REINFORCED_LAUNCHER_TRACK, "Reinforced Launcher Track");

    this.addBlock(RailcraftBlocks.STRAP_IRON_TRACK, "Strap Iron Track");
    this.addBlock(RailcraftBlocks.STRAP_IRON_LOCKING_TRACK, "Strap Iron Locking Track");
    this.addBlock(RailcraftBlocks.STRAP_IRON_BUFFER_STOP_TRACK, "Strap Iron Buffer Stop Track");
    this.addBlock(RailcraftBlocks.STRAP_IRON_ACTIVATOR_TRACK, "Strap Iron Activator Track");
    this.addBlock(RailcraftBlocks.STRAP_IRON_BOOSTER_TRACK, "Strap Iron Booster Track");
    this.addBlock(RailcraftBlocks.STRAP_IRON_CONTROL_TRACK, "Strap Iron Control Track");
    this.addBlock(RailcraftBlocks.STRAP_IRON_GATED_TRACK, "Strap Iron Gated Track");
    this.addBlock(RailcraftBlocks.STRAP_IRON_DETECTOR_TRACK, "Strap Iron Detector Track");
    this.addBlock(RailcraftBlocks.STRAP_IRON_COUPLER_TRACK, "Strap Iron Coupler Track");
    this.addBlock(RailcraftBlocks.STRAP_IRON_EMBARKING_TRACK, "Strap Iron Embarking Track");
    this.addBlock(RailcraftBlocks.STRAP_IRON_DISEMBARKING_TRACK, "Strap Iron Disembarking Track");
    this.addBlock(RailcraftBlocks.STRAP_IRON_TURNOUT_TRACK, "Strap Iron Turnout Track");
    this.addBlock(RailcraftBlocks.STRAP_IRON_WYE_TRACK, "Strap Iron Wye Track");
    this.addBlock(RailcraftBlocks.STRAP_IRON_JUNCTION_TRACK, "Strap Iron Junction Track");
    this.addBlock(RailcraftBlocks.STRAP_IRON_LAUNCHER_TRACK, "Strap Iron Launcher Track");

    this.addBlock(RailcraftBlocks.IRON_LOCKING_TRACK, "Iron Locking Track");
    this.addBlock(RailcraftBlocks.IRON_BUFFER_STOP_TRACK, "Iron Buffer Stop Track");
    this.addBlock(RailcraftBlocks.IRON_ACTIVATOR_TRACK, "Iron Activator Track");
    this.addBlock(RailcraftBlocks.IRON_BOOSTER_TRACK, "Iron Booster Track");
    this.addBlock(RailcraftBlocks.IRON_CONTROL_TRACK, "Iron Control Track");
    this.addBlock(RailcraftBlocks.IRON_GATED_TRACK, "Iron Gated Track");
    this.addBlock(RailcraftBlocks.IRON_DETECTOR_TRACK, "Iron Detector Track");
    this.addBlock(RailcraftBlocks.IRON_COUPLER_TRACK, "Iron Coupler Track");
    this.addBlock(RailcraftBlocks.IRON_EMBARKING_TRACK, "Iron Embarking Track");
    this.addBlock(RailcraftBlocks.IRON_DISEMBARKING_TRACK, "Iron Disembarking Track");
    this.addBlock(RailcraftBlocks.IRON_TURNOUT_TRACK, "Iron Turnout Track");
    this.addBlock(RailcraftBlocks.IRON_WYE_TRACK, "Iron Wye Track");
    this.addBlock(RailcraftBlocks.IRON_JUNCTION_TRACK, "Iron Junction Track");
    this.addBlock(RailcraftBlocks.IRON_LAUNCHER_TRACK, "Iron Launcher Track");

    this.addBlock(RailcraftBlocks.ELEVATOR_TRACK, "Elevator Track");
  }

  public void jeiTranslations() {
    this.add(Translations.Jei.METAL_ROLLING, "Metal Rolling");
  }

  public void addFluidType(Supplier<? extends FluidType> key, String name) {
    this.add(key.get(), name);
  }

  public void add(FluidType key, String name) {
    this.add(key.getDescriptionId(), name);
  }

  private void addBlockColorVariants(
      VariantRegistrar<DyeColor, ? extends Block> blocks, String name) {
    this.addBlockVariants(blocks, name, RailcraftLanguageProvider::getColorName);
  }

  private <K extends Enum<K> & StringRepresentable> void addBlockVariants(
      VariantRegistrar<K, ? extends Block> blocks, String name, Function<K, String> nameGetter) {
    blocks.forEach((color, block) -> this.addBlock(block, nameGetter.apply(color) + " " + name));
  }

  private static String getColorName(DyeColor color) {
    return switch (color) {
      case WHITE -> "White";
      case ORANGE -> "Orange";
      case MAGENTA -> "Magenta";
      case LIGHT_BLUE -> "Light Blue";
      case YELLOW -> "Yellow";
      case LIME -> "Lime";
      case PINK -> "Pink";
      case GRAY -> "Gray";
      case LIGHT_GRAY -> "Light Gray";
      case CYAN -> "Cyan";
      case PURPLE -> "Purple";
      case BLUE -> "Blue";
      case BROWN -> "Brown";
      case GREEN -> "Green";
      case RED -> "Red";
      case BLACK -> "Black";
    };
  }
}
