package mods.railcraft.data;

import java.util.function.Function;
import java.util.function.Supplier;
import mods.railcraft.Railcraft;
import mods.railcraft.Translations;
import mods.railcraft.season.Season;
import mods.railcraft.util.VariantRegistrar;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.entity.vehicle.MaintenanceMinecart;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.enchantment.RailcraftEnchantments;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.manipulator.ManipulatorBlockEntity;
import mods.railcraft.world.level.block.entity.track.CouplerTrackBlockEntity;
import mods.railcraft.world.level.block.track.outfitted.LockingMode;
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

    this.blockTranslations();
    this.itemTranslations();
    this.entityTranslations();
    this.fluidTranslations();
    this.containerTranslations();
    this.tipsTranslations();
    this.screenTranslations();
    this.jeiTranslations();
    this.signalTranslations();
    this.enchantmentTranslations();
    this.advancementTranslations();
    this.signalAspectTranslations();
    this.signalCapacitorTranslations();
    this.subtitleTranslations();
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
    this.addBlock(RailcraftBlocks.STEEL_BLOCK, "Block of Steel");
    this.addBlock(RailcraftBlocks.BRASS_BLOCK, "Block of Brass");
    this.addBlock(RailcraftBlocks.BRONZE_BLOCK, "Block of Bronze");
    this.addBlock(RailcraftBlocks.INVAR_BLOCK, "Block of Invar");
    this.addBlock(RailcraftBlocks.LEAD_BLOCK, "Block of Lead");
    this.addBlock(RailcraftBlocks.NICKEL_BLOCK, "Block of Nickel");
    this.addBlock(RailcraftBlocks.SILVER_BLOCK, "Block of Silver");
    this.addBlock(RailcraftBlocks.TIN_BLOCK, "Block of Tin");
    this.addBlock(RailcraftBlocks.ZINC_BLOCK, "Block of Zinc");
    this.addBlock(RailcraftBlocks.COKE_BLOCK, "Block of Coal Coke");
    this.addBlock(RailcraftBlocks.LEAD_ORE, "Lead Ore");
    this.addBlock(RailcraftBlocks.DEEPSLATE_LEAD_ORE, "Deepslate Lead Ore");
    this.addBlock(RailcraftBlocks.NICKEL_ORE, "Nickel Ore");
    this.addBlock(RailcraftBlocks.DEEPSLATE_NICKEL_ORE, "Deepslate Nickel Ore");
    this.addBlock(RailcraftBlocks.SILVER_ORE, "Silver Ore");
    this.addBlock(RailcraftBlocks.DEEPSLATE_SILVER_ORE, "Deepslate Silver Ore");
    this.addBlock(RailcraftBlocks.SULFUR_ORE, "Sulfur Ore");
    this.addBlock(RailcraftBlocks.DEEPSLATE_SULFUR_ORE, "Deepslate Sulfur Ore");
    this.addBlock(RailcraftBlocks.TIN_ORE, "Tin Ore");
    this.addBlock(RailcraftBlocks.DEEPSLATE_TIN_ORE, "Deepslate Tin Ore");
    this.addBlock(RailcraftBlocks.ZINC_ORE, "Zinc Ore");
    this.addBlock(RailcraftBlocks.DEEPSLATE_ZINC_ORE, "Deepslate Zinc Ore");
    this.addBlock(RailcraftBlocks.SALTPETER_ORE, "Saltpeter Ore");
    this.addBlock(RailcraftBlocks.FIRESTONE_ORE, "Firestone Ore");
    this.addBlock(RailcraftBlocks.STEEL_ANVIL, "Steel Anvil");
    this.addBlock(RailcraftBlocks.CHIPPED_STEEL_ANVIL, "Chipped Steel Anvil");
    this.addBlock(RailcraftBlocks.DAMAGED_STEEL_ANVIL, "Damaged Steel Anvil");
    this.addBlock(RailcraftBlocks.FLUID_LOADER, "Fluid Loader");
    this.addBlock(RailcraftBlocks.FLUID_UNLOADER, "Fluid Unloader");
    this.addBlock(RailcraftBlocks.ADVANCED_ITEM_LOADER, "Advanced Item Loader");
    this.addBlock(RailcraftBlocks.ADVANCED_ITEM_UNLOADER, "Advanced Item Unloader");
    this.addBlock(RailcraftBlocks.ITEM_LOADER, "Item Loader");
    this.addBlock(RailcraftBlocks.ITEM_UNLOADER, "Item Unloader");
    this.addBlock(RailcraftBlocks.CART_DISPENSER, "Cart Dispenser");
    this.addBlock(RailcraftBlocks.TRAIN_DISPENSER, "Train Dispenser");
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
    this.addBlock(RailcraftBlocks.SIGNAL_BLOCK_RELAY_BOX, "Signal Block Relay Box");
    this.addBlock(RailcraftBlocks.MANUAL_ROLLING_MACHINE, "Manual Rolling Machine");
    this.addBlock(RailcraftBlocks.CRUSHER, "Crusher");
    this.addBlock(RailcraftBlocks.COKE_OVEN_BRICKS, "Coke Oven Bricks");
    this.addBlock(RailcraftBlocks.FORCE_TRACK_EMITTER, "Force Track Emitter");
    this.addBlock(RailcraftBlocks.FORCE_TRACK, "Force Track");
    this.addBlock(RailcraftBlocks.CRUSHED_OBSIDIAN, "Crushed Obsidian");

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
    this.addItem(RailcraftItems.TURBINE_DISK, "Turbine Disk");
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
    this.addItem(RailcraftItems.TRACK_LAYER, "Track Layer Cart");
    this.addItem(RailcraftItems.TRACK_REMOVER, "Track Remover Cart");
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
    this.addItem(RailcraftItems.RADIO_CIRCUIT, "Radio Circuit");
    this.addItem(RailcraftItems.BAG_OF_CEMENT, "Bag of Cement");
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
    this.addItem(RailcraftItems.CREOSOTE_BOTTLE, "Creosote Bottle");
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
    this.addItem(RailcraftItems.ONE_WAY_TRACK_KIT, "One-Way Track Kit");
    this.addItem(RailcraftItems.LOCOMOTIVE_TRACK_KIT, "Locomotive Track Kit");
    this.addItem(RailcraftItems.CHARGE_SPOOL_LARGE, "Large Charge Wire Spool");
    this.addItem(RailcraftItems.CHARGE_SPOOL_MEDIUM, "Medium Charge Wire Spool");
    this.addItem(RailcraftItems.CHARGE_SPOOL_SMALL, "Small Charge Wire Spool");
    this.addItem(RailcraftItems.CHARGE_COIL, "Charge Coil");
    this.addItem(RailcraftItems.CHARGE_TERMINAL, "Charge Terminal");
    this.addItem(RailcraftItems.CHARGE_MOTOR, "Charge Motor");
  }

  private void entityTranslations() {
    this.addEntityType(RailcraftEntityTypes.TANK_MINECART, "Minecart with Tank");
    this.addEntityType(RailcraftEntityTypes.CREATIVE_LOCOMOTIVE, "Creative Locomotive");
    this.addEntityType(RailcraftEntityTypes.STEAM_LOCOMOTIVE, "Steam Locomotive");
    this.addEntityType(RailcraftEntityTypes.ELECTRIC_LOCOMOTIVE, "Electric Locomotive");
    this.addEntityType(RailcraftEntityTypes.TUNNEL_BORE, "Tunnel Bore");
    this.addEntityType(RailcraftEntityTypes.TRACK_LAYER, "Track Layer Cart");
    this.addEntityType(RailcraftEntityTypes.TRACK_REMOVER, "Track Remover Cart");
  }

  private void fluidTranslations() {
    this.addFluidType(RailcraftFluidTypes.STEAM, "Steam");
    this.addFluidType(RailcraftFluidTypes.CREOSOTE, "Creosote");
  }

  private void containerTranslations() {
    this.add(Translations.Container.MANUAL_ROLLING_MACHINE, "Manual Rolling Machine");
    this.add(Translations.Container.COKE_OVEN, "Coke Oven");
    this.add(Translations.Container.BLAST_FURNACE, "Blast Furnace");
    this.add(Translations.Container.CRUSHER, "Crusher");
    this.add(Translations.Container.TANK, "Tank");
    this.add(Translations.Container.STEAM_TURBINE, "Steam Turbine");
    this.add(Translations.Container.SOLID_FUELED_STEAM_BOILER, "Solid Fueled Steam Boiler");
    this.add(Translations.Container.FLUID_FUELED_STEAM_BOILER, "Fluid Fueled Steam Boiler");
  }

  private void tipsTranslations() {
    this.add(MaintenanceMinecart.Mode.SERVICE.getTranslationKey(), "Service");
    this.add(MaintenanceMinecart.Mode.TRANSPORT.getTranslationKey(), "Transport");

    this.add(Translations.Tips.ROUTING_TICKET_ISSUER, "Issuer:");
    this.add(Translations.Tips.ROUTING_TICKET_DEST, "Destination:");
    this.add(Translations.Tips.ROUTING_TICKET_BLANK, "Blank Ticket");
    this.add(Translations.Tips.LOCOMOTIVE_SLOT_TICKET, "Insert Ticket");
    this.add(Translations.Tips.LOCOMOTIVE_ITEM_OWNER, "Owner: %s");
    this.add(Translations.Tips.LOCOMOTIVE_ITEM_PRIMARY, "Primary descriptor.(EDIT ME)");
    this.add(Translations.Tips.LOCOMOTIVE_ITEM_SECONDARY, "Secondary descriptor.(EDIT ME)");
    this.add(Translations.Tips.LOCOMOTIVE_ITEM_WHISTLE, "There is currently no whistle installed.");
    this.add(Translations.Tips.TRACK_LAYER, "Lays track as it moves");
    this.add(Translations.Tips.TRACK_REMOVER, "Removes tracks it passes over");
    this.add(Translations.Tips.CRUSHED_OBSIDIAN, "Prevents Mobs Spawns");
    this.add(Translations.Tips.COKE_OVEN, "Multi-Block: 3x3x3 (Hollow)");
    this.add(Translations.Tips.BLAST_FURNACE, "Multi-Block: 3x4x3 (Hollow)");
    this.add(Translations.Tips.MULTIBLOCK3X2X2, "Multi-Block: 3x2x2");
    this.add(Translations.Tips.STEAM_TURBINE_DESC_1, "Generates FE from Steam");
    this.add(Translations.Tips.STEAM_TURBINE_DESC_2, "Outputs water to the bottom");
    this.add(Translations.Tips.STEAM_TURBINE_DESC_3, "Requires a Turbine Rotor");
    this.add(Translations.Tips.SPIKE_MAUL, "Converts track to switches and junctions.");
    this.add(Translations.Tips.OVERALLS, "Protection against the dangers of track-working");
    this.add(Translations.Tips.GOOGLES_DESC, "Right-click to change aura.");
    this.add(Translations.Tips.GOOGLES_AURA, "Current Aura: %s");
    this.add(Translations.Tips.GOOGLES_AURA_NONE, "None");
    this.add(Translations.Tips.GOOGLES_AURA_SHUNTING, "Shunting");
    this.add(Translations.Tips.GOOGLES_AURA_SIGNALLING, "Signalling");
    this.add(Translations.Tips.GOOGLES_AURA_SURVEYING, "Surveying");
    this.add(Translations.Tips.GOOGLES_AURA_TRACKING, "Tracking");
    this.add(Translations.Tips.GOOGLES_AURA_TUNING, "Tuning");
    this.add(Translations.Tips.GOOGLES_AURA_WORLDSPIKE, "Worldspike");
    this.add(Translations.Tips.CROWBAR_DESC, "Right-click while sneaking to link carts.");
    this.add(Translations.Tips.CROWBAR_LINK_BROKEN, "Link Broken");
    this.add(Translations.Tips.CROWBAR_LINK_CREATED, "Successfully Linked Carts");
    this.add(Translations.Tips.CROWBAR_LINK_FAILED, "Link Failed");
    this.add(Translations.Tips.CROWBAR_LINK_STARTED, "Started Linking Carts");
    this.add(Translations.Tips.CROWBAR_SEASON_DESC, "Current season: %s");

    this.add(Season.NONE.getTranslationKey(), "None");
    this.add(Season.DEFAULT.getTranslationKey(), "Default");
    this.add(Season.CHRISTMAS.getTranslationKey(), "Christmas");
    this.add(Season.HALLOWEEN.getTranslationKey(), "Halloween");

    this.add(Translations.Tips.SIGNAL_LABEL_DESC1, "- Rename in Anvil -");
    this.add(Translations.Tips.SIGNAL_LABEL_DESC2,
        "Right click while sneaking to name a signal or signal box");
    this.add(Translations.Tips.SENDS_SIGNALS_TO_RECEIVERS, "Sends signals to Receivers");
    this.add(Translations.Tips.SIGNAL_RECEIVER_BOX, "Listens to Controller signals");
    this.add(Translations.Tips.SIGNAL_INTERLOCK_BOX, "Permits only one aspect signal to pass");
    this.add(Translations.Tips.SIGNAL_SEQUENCER_BOX, "Loops through adjacent blocks");
    this.add(Translations.Tips.SIGNAL_BLOCK_RELAY_BOX, "Creates chains of Signal Blocks");
    this.add(Translations.Tips.FIRESTONE_EMPTY,
        "Its energies controlled, it could be useful if you could recharge it...");
    this.add(Translations.Tips.FIRESTONE_CHARGED,
        "Filled with energy, you only need to exert your will onto it to release it's blistering heat...");
    this.add(Translations.Tips.RAW_FIRESTONE,
        "It quickens in your hands, you can feel it in the very air around you...");
    this.add(Translations.Tips.CUT_FIRESTONE, "Still its energies rage out of control...");
    this.add(Translations.Tips.CURRENT_MODE, "Current Mode:");

    this.add(LockingMode.LOCKDOWN.getTranslationKey(), "Lockdown");
    this.add(LockingMode.TRAIN_LOCKDOWN.getTranslationKey(), "Train Lockdown");
    this.add(LockingMode.HOLDING.getTranslationKey(), "Holding");
    this.add(LockingMode.TRAIN_HOLDING.getTranslationKey(), "Train Holding");
    this.add(LockingMode.BOARDING.getTranslationKey(), "Boarding");
    this.add(LockingMode.BOARDING_REVERSED.getTranslationKey(), "Boarding Reversed");
    this.add(LockingMode.TRAIN_BOARDING.getTranslationKey(), "Train Boarding");
    this.add(LockingMode.TRAIN_BOARDING_REVERSED.getTranslationKey(), "Train Boarding Reversed");

    this.add(CouplerTrackBlockEntity.Mode.COUPLER.getTranslationKey(), "Coupler");
    this.add(CouplerTrackBlockEntity.Mode.DECOUPLER.getTranslationKey(), "Decoupler");
    this.add(CouplerTrackBlockEntity.Mode.AUTO_COUPLER.getTranslationKey(), "Auto Coupler");

    this.add(Translations.Tips.TRACK_KIT_CORNERS_UNSUPPORTED, "Corners are Unsupported");
    this.add(Translations.Tips.TRACK_KIT_SLOPES_UNSUPPORTED, "Slopes are Unsupported");
    this.add(Translations.Tips.TRACK_KIT_INVALID_TRACK_TYPE, "Invalid Track Type");

    this.add(ManipulatorBlockEntity.RedstoneMode.COMPLETE.getTranslationKey(), "Complete");
    this.add(ManipulatorBlockEntity.RedstoneMode.COMPLETE.getDescriptionKey(),
        "Process until operation is complete.");
    this.add(ManipulatorBlockEntity.RedstoneMode.IMMEDIATE.getTranslationKey(), "Immediate");
    this.add(ManipulatorBlockEntity.RedstoneMode.IMMEDIATE.getDescriptionKey(),
        "Process until out of room or supply.");
    this.add(ManipulatorBlockEntity.RedstoneMode.MANUAL.getTranslationKey(), "Manual");
    this.add(ManipulatorBlockEntity.RedstoneMode.MANUAL.getDescriptionKey(),
        "Never emit a redstone signal.");
    this.add(ManipulatorBlockEntity.RedstoneMode.PARTIAL.getTranslationKey(), "Partial");
    this.add(ManipulatorBlockEntity.RedstoneMode.PARTIAL.getDescriptionKey(),
        "Process until out of room or supply, but keep pristine carts.");

    this.add(ManipulatorBlockEntity.TransferMode.ALL.getTranslationKey(), "All");
    this.add(ManipulatorBlockEntity.TransferMode.ALL.getDescriptionKey(),
        "Move all matching items.");
    this.add(ManipulatorBlockEntity.TransferMode.EXCESS.getTranslationKey(), "Excess");
    this.add(ManipulatorBlockEntity.TransferMode.EXCESS.getDescriptionKey(),
        "Move until the source matches the filter.");
    this.add(ManipulatorBlockEntity.TransferMode.STOCK.getTranslationKey(), "Stock");
    this.add(ManipulatorBlockEntity.TransferMode.STOCK.getDescriptionKey(),
        "Move until the destination matches the filter.");
    this.add(ManipulatorBlockEntity.TransferMode.TRANSFER.getTranslationKey(), "Transfer");
    this.add(ManipulatorBlockEntity.TransferMode.TRANSFER.getDescriptionKey(),
        "Move exactly as much as is in the filter.");

    this.add(Translations.Tips.COAL_COKE_BLOCK, "%s Fuel Units");
    this.add(Translations.Tips.FORCE_TRACK_EMITTER, "Projects energy-based tracks");
    this.add(Translations.Tips.FEED_STATION, "Feeds animals");
    this.add(Translations.Tips.MANUAL_ROLLING_MACHINE, "Crafting bench for various metal items");
    this.add(Translations.Tips.ITEM_LOADER, "Loads items into carts");
    this.add(Translations.Tips.ITEM_UNLOADER, "Unloads items from carts");
    this.add(Translations.Tips.FLUID_LOADER, "Loads fluids into carts");
    this.add(Translations.Tips.FLUID_UNLOADER, "Unloads fluids from carts");
    this.add(Translations.Tips.CART_DISPENSER, "Dispenses carts onto tracks");
    this.add(Translations.Tips.TRAIN_DISPENSER, "Dispenses linked trains onto tracks");
    this.add(Translations.Tips.SIGNAL_BLOCK_SURVEYOR, "Links Signals together");
    this.add(Translations.Tips.SIGNAL_TUNER, "Links Controllers to Receivers");
    this.add(Translations.Tips.FUELED_BOILER_FIREBOX, "Multi-Block: Variable Size, Bottom Layer");
    this.add(Translations.Tips.PRESSURE_BOILER_TANK, "Multi-Block: Variable Size, Above Firebox");
    this.add(Translations.Tips.PRESSURE_BOILER_TANK_PRODUCTION, "Produces %smB of Steam per tick");
    this.add(Translations.Tips.DIMENSIONS, "Dimensions: %s");
    this.add(Translations.Tips.APPLY_REDSTONE_TO_ENABLE, "- Apply Redstone to enable -");
    this.add(Translations.Tips.APPLY_REDSTONE_TO_DISABLE, "- Apply Redstone to disable -");
    this.add(Translations.Tips.APPLY_REDSTONE_TO_CHANGE_DIRECTION,
        "- Apply Redstone to change direction -");
    this.add(Translations.Tips.APPLY_REDSTONE_TO_OPEN, "- Apply Redstone to open -");
    this.add(Translations.Tips.APPLY_REDSTONE_TO_RELEASE_CARTS,
        "- Apply Redstone to release carts -");
    this.add(Translations.Tips.APPLY_REDSTONE_TO_DISPENSE_CARTS,
        "- Apply Redstone to dispense carts -");
    this.add(Translations.Tips.APPLY_REDSTONE_TO_DISPENSE_TRAINS,
        "- Apply Redstone to dispense trains -");
    this.add(Translations.Tips.COMPARATOR_OUTPUT_FROM_CARTS,
        "- Can be used to get Comparator output from carts -");
    this.add(Translations.Tips.HIT_CROWBAR_TO_CHANGE_DIRECTION,
        "- Hit with Crowbar to change direction -");
    this.add(Translations.Tips.HIT_CROWBAR_TO_CHANGE_DETECTION_DIRECTION,
        "- Hit with Crowbar to change detection direction -");
    this.add(Translations.Tips.HIT_CROWBAR_TO_CHANGE_RANGE, "- Hit with Crowbar to change range -");
    this.add(Translations.Tips.HIT_CROWBAR_TO_CHANGE_MODE, "- Hit with Crowbar to change mode -");
    this.add(Translations.Tips.HIT_CROWBAR_TO_CHANGE_FORCE, "- Hit with Crowbar to change force -");
    this.add(Translations.Tips.HIT_CROWBAR_TO_ROTATE, "- Hit with Crowbar to rotate -");
    this.add(Translations.Tips.PAIR_WITH_CONTROL_TRACK, "- Pair with Control track-");
    this.add(Translations.Tips.PLACE_ABOVE_TRACK, "- Place 1-2 blocks above track -");
    this.add(Translations.Tips.PLACE_UNDER_TRACK, "- Place under track -");
    this.add(Translations.Tips.PLACE_OVER_TRACK, "- Place over track -");
    this.add(Translations.Tips.SLOW_UNPOWERED, "Slows if unpowered");
    this.add(Translations.Tips.ACTIVATOR_TRACK, "Activates passing carts");
    this.add(Translations.Tips.BOOSTER_TRACK, "Applies a boost force to the cart");
    this.add(Translations.Tips.BUFFER_STOP_TRACK, "End of the Line");
    this.add(Translations.Tips.CONTROL_TRACK, "Applies a small force to the cart");
    this.add(Translations.Tips.DETECTOR_TRACK,
        "Emits Redstone when a cart passes in the indicated direction");
    this.add(Translations.Tips.DISEMBARKING_TRACK,
        "Dismount riding entities in direction of arrow");
    this.add(Translations.Tips.EMBARKING_TRACK, "Loads entities into carts");
    this.add(Translations.Tips.GATED_TRACK, "Track with built-in gate");
    this.add(Translations.Tips.COUPLER_TRACK, "Couples/Decouples passing carts");
    this.add(Translations.Tips.LAUNCHER_TRACK, "Makes carts fly!");
    this.add(Translations.Tips.ONE_WAY_TRACK, "Carts can only pass in the direction of the arrow");
    this.add(Translations.Tips.LOCOMOTIVE_TRACK, "Starts/Stops locomotive");
    this.add(Translations.Tips.LOCKING_TRACK, "Stops and holds carts");
    this.add(Translations.Tips.TRANSITION_TRACK, "Transitions to/from high speed");
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
    this.add(Translations.Screen.PATTERN, "Pattern");
    this.add(Translations.Screen.STOCK, "Stock");
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
    this.addBlock(RailcraftBlocks.ABANDONED_ONE_WAY_TRACK, "Abandoned One-Way Track");
    this.addBlock(RailcraftBlocks.ABANDONED_LOCOMOTIVE_TRACK, "Abandoned Locomotive Track");

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
    this.addBlock(RailcraftBlocks.ELECTRIC_ONE_WAY_TRACK, "Electric One-Way Track");
    this.addBlock(RailcraftBlocks.ELECTRIC_LOCOMOTIVE_TRACK, "Electric Locomotive Track");

    this.addBlock(RailcraftBlocks.HIGH_SPEED_TRACK, "High Speed Track");
    this.addBlock(RailcraftBlocks.HIGH_SPEED_LOCKING_TRACK, "High Speed Locking Track");
    this.addBlock(RailcraftBlocks.HIGH_SPEED_ACTIVATOR_TRACK, "High Speed Activator Track");
    this.addBlock(RailcraftBlocks.HIGH_SPEED_BOOSTER_TRACK, "High Speed Booster Track");
    this.addBlock(RailcraftBlocks.HIGH_SPEED_DETECTOR_TRACK, "High Speed Detector Track");
    this.addBlock(RailcraftBlocks.HIGH_SPEED_TURNOUT_TRACK, "High Speed Turnout Track");
    this.addBlock(RailcraftBlocks.HIGH_SPEED_WYE_TRACK, "High Speed Wye Track");
    this.addBlock(RailcraftBlocks.HIGH_SPEED_JUNCTION_TRACK, "High Speed Junction Track");
    this.addBlock(RailcraftBlocks.HIGH_SPEED_TRANSITION_TRACK, "High Speed Transition Track");
    this.addBlock(RailcraftBlocks.HIGH_SPEED_LOCOMOTIVE_TRACK, "High Speed Locomotive Track");

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
    this.addBlock(RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCOMOTIVE_TRACK,
        "High Speed Electric Locomotive Track");

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
    this.addBlock(RailcraftBlocks.REINFORCED_ONE_WAY_TRACK, "Reinforced One-Way Track");
    this.addBlock(RailcraftBlocks.REINFORCED_LOCOMOTIVE_TRACK, "Reinforced Locomotive Track");

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
    this.addBlock(RailcraftBlocks.STRAP_IRON_ONE_WAY_TRACK, "Strap Iron One-Way Track");
    this.addBlock(RailcraftBlocks.STRAP_IRON_LOCOMOTIVE_TRACK, "Strap Iron Locomotive Track");

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
    this.addBlock(RailcraftBlocks.IRON_ONE_WAY_TRACK, "Iron One-Way Track");
    this.addBlock(RailcraftBlocks.IRON_LOCOMOTIVE_TRACK, "Iron Locomotive Track");

    this.addBlock(RailcraftBlocks.ELEVATOR_TRACK, "Elevator Track");
  }

  private void jeiTranslations() {
    this.add(Translations.Jei.METAL_ROLLING, "Metal Rolling");
    this.add(Translations.Jei.COKE_OVEN, "Coke Oven Smelting");
    this.add(Translations.Jei.BLAST_FURNACE, "Blast Furnace Smelting");
    this.add(Translations.Jei.CRUSHER, "Rock Crushing");
    this.add(Translations.Jei.CRUSHER_TIP, "(%s%% chance)");
  }

  private void signalTranslations() {
    this.add(Translations.Signal.SIGNAL_SURVEYOR_INVALID_TRACK, "No Track Found Near %s");
    this.add(Translations.Signal.SIGNAL_SURVEYOR_BEGIN, "Beginning Survey");
    this.add(Translations.Signal.SIGNAL_SURVEYOR_SUCCESS, "Successfully Paired Signals");
    this.add(Translations.Signal.SIGNAL_SURVEYOR_INVALID_PAIR, "Pairing Invalid");
    this.add(Translations.Signal.SIGNAL_SURVEYOR_LOST, "First Signal No Longer Exists");
    this.add(Translations.Signal.SIGNAL_SURVEYOR_UNLOADED,
        "First Signal's Chunk Has Been Unloaded");
    this.add(Translations.Signal.SIGNAL_SURVEYOR_ABANDONED, "Survey Abandoned");
    this.add(Translations.Signal.SIGNAL_SURVEYOR_INVALID_BLOCK, "Not a Valid Signal");

    this.add(Translations.Signal.SIGNAL_TUNER_BEGIN, "Started Pairing %s With a Receiver");
    this.add(Translations.Signal.SIGNAL_TUNER_ABANDONED, "Stopped Pairing");
    this.add(Translations.Signal.SIGNAL_TUNER_UNLOADED,
        "Signal Controller's Chunk Has Been Unloaded");
    this.add(Translations.Signal.SIGNAL_TUNER_LOST, "Signal Controller No Longer Exists");
    this.add(Translations.Signal.SIGNAL_TUNER_SUCCESS, "Successfully Paired Signal Receiver");
  }

  private void enchantmentTranslations() {
    this.addEnchantment(RailcraftEnchantments.WRECKING, "Wrecking");
    this.addEnchantment(RailcraftEnchantments.DESTRUCTION, "Destruction");
    this.addEnchantment(RailcraftEnchantments.IMPLOSION, "Implosion");
    this.addEnchantment(RailcraftEnchantments.SMACK, "Smack");
  }

  private void advancementTranslations() {
    this.add(Translations.Advancement.Tracks.ROOT, "Tracks");
    this.add(Translations.Advancement.Tracks.ROOT_DESC, "Railcraft Inc.'s dedication");
    this.add(Translations.Advancement.Tracks.MANUAL_ROLLING_MACHINE, "Tireless Rolling");
    this.add(Translations.Advancement.Tracks.MANUAL_ROLLING_MACHINE_DESC,
        "Build a manual rolling machine out of alloys");
    this.add(Translations.Advancement.Tracks.BLAST_FURNACE, "Steel Mill");
    this.add(Translations.Advancement.Tracks.BLAST_FURNACE_DESC, "Build a blast furnace");
    this.add(Translations.Advancement.Tracks.COKE_OVEN, "Coke Head");
    this.add(Translations.Advancement.Tracks.COKE_OVEN_DESC,
        "Read a coke oven brick's tooltip and build a complete one");
    this.add(Translations.Advancement.Tracks.CRUSHER, "Heavy Machinery");
    this.add(Translations.Advancement.Tracks.CRUSHER_DESC, "Build a crusher");
    this.add(Translations.Advancement.Tracks.FIRESTONE, "Intractable Energy");
    this.add(Translations.Advancement.Tracks.FIRESTONE_DESC,
        "Find a firestone ore on the floor of nether lava ocean and crush it with a rock crusher");
    this.add(Translations.Advancement.Tracks.HIGH_SPEED_TRACK, "Fired up Carts");
    this.add(Translations.Advancement.Tracks.HIGH_SPEED_TRACK_DESC,
        "Get some high speed tracks and ride your carts on them");
    this.add(Translations.Advancement.Tracks.JUNCTIONS, "Better Forks");
    this.add(Translations.Advancement.Tracks.JUNCTIONS_DESC,
        "Turn a regular track into turnouts, wyes, and intersections with a spike maul");
    this.add(Translations.Advancement.Tracks.REGULAR_TRACK, "Reasonably Priced");
    this.add(Translations.Advancement.Tracks.REGULAR_TRACK_DESC,
        "Manufacture tracks made of regular rails");
    this.add(Translations.Advancement.Tracks.WOODEN_TRACK, "Wood Age");
    this.add(Translations.Advancement.Tracks.WOODEN_TRACK_DESC,
        "Get some strap iron tracks that require very few iron");
    this.add(Translations.Advancement.Tracks.TRACK_KIT, "Multifunctional Rails");
    this.add(Translations.Advancement.Tracks.TRACK_KIT_DESC,
        "Make and place track kits on your flex tracks to empower them like this buffer stop one");

    this.add(Translations.Advancement.Carts.ROOT, "Railcraft Carts");
    this.add(Translations.Advancement.Carts.ROOT_DESC, "Vehicles and magic from Railcraft");
    this.add(Translations.Advancement.Carts.BED_CART, "Dreams on the wheels");
    this.add(Translations.Advancement.Carts.BED_CART_DESC, "Sleeping while riding the bed cart");
    this.add(Translations.Advancement.Carts.JUKEBOX_CART, "Moving Music");
    this.add(Translations.Advancement.Carts.JUKEBOX_CART_DESC, "Play a record in a jukebox cart");
    this.add(Translations.Advancement.Carts.LINK_CARTS, "Linking Carts");
    this.add(Translations.Advancement.Carts.LINK_CARTS_DESC, "Never forget to sneak!");
    this.add(Translations.Advancement.Carts.LOCOMOTIVE, "Rolling Through");
    this.add(Translations.Advancement.Carts.LOCOMOTIVE_DESC, "Power your train with locomotives");
    this.add(Translations.Advancement.Carts.SEASONS, "Anachronism");
    this.add(Translations.Advancement.Carts.SEASONS_DESC,
        "Tweak with a seasons crowbar the season of a cart");
    this.add(Translations.Advancement.Carts.SURPRISE, "Opportune Kaboom");
    this.add(Translations.Advancement.Carts.SURPRISE_DESC,
        "Explode a seasonal cart and collect (really?) gifts");
  }

  private void signalAspectTranslations() {
    this.add(Translations.SignalAspect.GREEN, "Green");
    this.add(Translations.SignalAspect.BLINK_YELLOW, "Blink Yellow");
    this.add(Translations.SignalAspect.YELLOW, "Yellow");
    this.add(Translations.SignalAspect.BLINK_RED, "Blink Red");
    this.add(Translations.SignalAspect.RED, "Red");
    this.add(Translations.SignalAspect.OFF, "Off");
  }

  private void signalCapacitorTranslations() {
    this.add(Translations.SignalCapacitor.RISING_EDGE, "Rising Edge");
    this.add(Translations.SignalCapacitor.RISING_EDGE_DESC,
        "Start timer as soon as the input signal is received.");
    this.add(Translations.SignalCapacitor.FALLING_EDGE, "Falling Edge");
    this.add(Translations.SignalCapacitor.FALLING_EDGE_DESC,
        "Start timer when the last input signal turns off.");
  }

  private void subtitleTranslations() {
    this.add(Translations.Subtitle.STEAM_WHISTLE, "Steam Locomotive Whistle");
    this.add(Translations.Subtitle.ELECTRIC_WHISTLE, "Electric Locomotive Whistle");
    this.add(Translations.Subtitle.STEAM_BURST, "Machine Steam Burst");
    this.add(Translations.Subtitle.STEAM_HISS, "Machine Steam Hiss");
    this.add(Translations.Subtitle.MACHINE_ZAP, "Machine Zap");
  }

  private void addFluidType(Supplier<? extends FluidType> key, String name) {
    this.add(key.get(), name);
  }

  private void add(FluidType key, String name) {
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
