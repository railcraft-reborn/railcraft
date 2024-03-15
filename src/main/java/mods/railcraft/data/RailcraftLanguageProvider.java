package mods.railcraft.data;

import java.util.function.Function;
import java.util.function.Supplier;
import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.season.Season;
import mods.railcraft.util.VariantSet;
import mods.railcraft.world.effect.RailcraftMobEffects;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.entity.npc.RailcraftVillagerProfession;
import mods.railcraft.world.entity.vehicle.MaintenanceMinecart;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.alchemy.RailcraftPotions;
import mods.railcraft.world.item.enchantment.RailcraftEnchantments;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.manipulator.ManipulatorBlockEntity;
import mods.railcraft.world.level.block.entity.track.CouplerTrackBlockEntity;
import mods.railcraft.world.level.block.track.outfitted.LockingMode;
import mods.railcraft.world.level.material.RailcraftFluidTypes;
import net.minecraft.data.PackOutput;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.fluids.FluidType;

public class RailcraftLanguageProvider extends LanguageProvider {

  public RailcraftLanguageProvider(PackOutput packOutput) {
    super(packOutput, RailcraftConstants.ID, "en_us");
  }

  @Override
  protected void addTranslations() {
    this.add(Translations.Tab.RAILCRAFT, RailcraftConstants.NAME);
    this.add(Translations.Tab.RAILCRAFT_OUTFITTED_TRACKS, RailcraftConstants.NAME + " Outfitted Tracks");
    this.add(Translations.Tab.RAILCRAFT_DECORATIVE_BLOCKS, RailcraftConstants.NAME + " Decorative Blocks");

    this.add("fml.menu.mods.info.displayname." + RailcraftConstants.ID, RailcraftConstants.NAME);
    this.add("fml.menu.mods.info.description." + RailcraftConstants.ID, "Redefine your rails");

    this.blockTranslations();
    this.itemTranslations();
    this.entityTranslations();
    this.fluidTranslations();
    this.containerTranslations();
    this.tipsTranslations();
    this.screenTranslations();
    this.jeiTranslations();
    this.lookingAtTranslations();
    this.signalTranslations();
    this.enchantmentTranslations();
    this.advancementTranslations();
    this.signalAspectTranslations();
    this.signalCapacitorTranslations();
    this.subtitleTranslations();
    this.effectTranslations();
    this.potionTranslations();
    this.villagerTranslations();
    this.damageSourceTranslations();
    this.routingTableTranslations();
    this.chargeMeterTranslations();
    this.keyBindingTranslations();
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
    this.addBlock(RailcraftBlocks.LOGBOOK, "Logbook");
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
    this.addBlock(RailcraftBlocks.ADVANCED_DETECTOR, "Advanced Cart Detector");
    this.addBlock(RailcraftBlocks.AGE_DETECTOR, "Age Cart Detector");
    this.addBlock(RailcraftBlocks.ANIMAL_DETECTOR, "Animal Cart Detector");
    this.addBlock(RailcraftBlocks.ANY_DETECTOR, "Any Cart Detector");
    this.addBlock(RailcraftBlocks.EMPTY_DETECTOR, "Empty Cart Detector");
    this.addBlock(RailcraftBlocks.ITEM_DETECTOR, "Item Cart Detector");
    this.addBlock(RailcraftBlocks.LOCOMOTIVE_DETECTOR, "Locomotive Cart Detector");
    this.addBlock(RailcraftBlocks.MOB_DETECTOR, "Mob Cart Detector");
    this.addBlock(RailcraftBlocks.PLAYER_DETECTOR, "Player Cart Detector");
    this.addBlock(RailcraftBlocks.ROUTING_DETECTOR, "Routing Cart Detector");
    this.addBlock(RailcraftBlocks.SHEEP_DETECTOR, "Sheep Cart Detector");
    this.addBlock(RailcraftBlocks.TANK_DETECTOR, "Tank Cart Detector");
    this.addBlock(RailcraftBlocks.TRAIN_DETECTOR, "Train Cart Detector");
    this.addBlock(RailcraftBlocks.VILLAGER_DETECTOR, "Villager Cart Detector");
    this.addBlock(RailcraftBlocks.SWITCH_TRACK_LEVER, "Switch Track Lever");
    this.addBlock(RailcraftBlocks.SWITCH_TRACK_MOTOR, "Switch Track Motor");
    this.addBlock(RailcraftBlocks.SWITCH_TRACK_ROUTER, "Switch Track Router");
    this.addBlock(RailcraftBlocks.BLOCK_SIGNAL, "Block Signal");
    this.addBlock(RailcraftBlocks.DISTANT_SIGNAL, "Distant Signal");
    this.addBlock(RailcraftBlocks.TOKEN_SIGNAL, "Token Signal");
    this.addBlock(RailcraftBlocks.DUAL_BLOCK_SIGNAL, "Dual-Head Block Signal");
    this.addBlock(RailcraftBlocks.DUAL_DISTANT_SIGNAL, "Dual-Head Distant Signal");
    this.addBlock(RailcraftBlocks.DUAL_TOKEN_SIGNAL, "Dual-Head Token Signal");
    this.addBlock(RailcraftBlocks.TOKEN_SIGNAL_BOX, "Token Signal Box");
    this.addBlock(RailcraftBlocks.SIGNAL_CONTROLLER_BOX, "Signal Controller Box");
    this.addBlock(RailcraftBlocks.SIGNAL_RECEIVER_BOX, "Signal Receiver Box");
    this.addBlock(RailcraftBlocks.SIGNAL_CAPACITOR_BOX, "Signal Capacitor Box");
    this.addBlock(RailcraftBlocks.ANALOG_SIGNAL_CONTROLLER_BOX, "Analog Signal Controller Box");
    this.addBlock(RailcraftBlocks.SIGNAL_INTERLOCK_BOX, "Signal Interlock Box");
    this.addBlock(RailcraftBlocks.SIGNAL_SEQUENCER_BOX, "Signal Sequencer Box");
    this.addBlock(RailcraftBlocks.SIGNAL_BLOCK_RELAY_BOX, "Signal Block Relay Box");
    this.addBlock(RailcraftBlocks.MANUAL_ROLLING_MACHINE, "Manual Rolling Machine");
    this.addBlock(RailcraftBlocks.POWERED_ROLLING_MACHINE, "Powered Rolling Machine");
    this.addBlock(RailcraftBlocks.CRUSHER, "Crusher");
    this.addBlock(RailcraftBlocks.COKE_OVEN_BRICKS, "Coke Oven Bricks");
    this.addBlock(RailcraftBlocks.STEAM_OVEN, "Steam Oven");
    this.addBlock(RailcraftBlocks.FORCE_TRACK_EMITTER, "Force Track Emitter");
    this.addBlock(RailcraftBlocks.FORCE_TRACK, "Force Track");
    this.addBlock(RailcraftBlocks.CRUSHED_OBSIDIAN, "Crushed Obsidian");
    this.addBlock(RailcraftBlocks.WATER_TANK_SIDING, "Water Tank Siding");

    this.addBlock(RailcraftBlocks.QUARRIED_STONE, "Quarried Stone");
    this.addBlock(RailcraftBlocks.QUARRIED_COBBLESTONE, "Quarried Cobblestone");
    this.addBlock(RailcraftBlocks.POLISHED_QUARRIED_STONE, "Polished Quarried Stone");
    this.addBlock(RailcraftBlocks.CHISELED_QUARRIED_STONE, "Chiseled Quarried Stone");
    this.addBlock(RailcraftBlocks.ETCHED_QUARRIED_STONE, "Etched Quarried Stone");
    this.addBlock(RailcraftBlocks.QUARRIED_BRICKS, "Quarried Bricks");
    this.addBlock(RailcraftBlocks.QUARRIED_BRICK_STAIRS, "Quarried Brick Stairs");
    this.addBlock(RailcraftBlocks.QUARRIED_BRICK_SLAB, "Quarried Brick Slab");
    this.addBlock(RailcraftBlocks.QUARRIED_PAVER, "Quarried Paver");
    this.addBlock(RailcraftBlocks.QUARRIED_PAVER_STAIRS, "Quarried Paver Stairs");
    this.addBlock(RailcraftBlocks.QUARRIED_PAVER_SLAB, "Quarried Paver Slab");

    this.addBlock(RailcraftBlocks.ABYSSAL_STONE, "Abyssal Stone");
    this.addBlock(RailcraftBlocks.ABYSSAL_COBBLESTONE, "Abyssal Cobblestone");
    this.addBlock(RailcraftBlocks.POLISHED_ABYSSAL_STONE, "Polished Abyssal Stone");
    this.addBlock(RailcraftBlocks.CHISELED_ABYSSAL_STONE, "Chiseled Abyssal Stone");
    this.addBlock(RailcraftBlocks.ETCHED_ABYSSAL_STONE, "Etched Abyssal Stone");
    this.addBlock(RailcraftBlocks.ABYSSAL_BRICKS, "Abyssal Bricks");
    this.addBlock(RailcraftBlocks.ABYSSAL_BRICK_STAIRS, "Abyssal Brick Stairs");
    this.addBlock(RailcraftBlocks.ABYSSAL_BRICK_SLAB, "Abyssal Brick Slab");
    this.addBlock(RailcraftBlocks.ABYSSAL_PAVER, "Abyssal Paver");
    this.addBlock(RailcraftBlocks.ABYSSAL_PAVER_STAIRS, "Abyssal Paver Stairs");
    this.addBlock(RailcraftBlocks.ABYSSAL_PAVER_SLAB, "Abyssal Paver Slab");

    this.addBlock(RailcraftBlocks.WORLD_SPIKE, "Worldspike");
    this.addBlock(RailcraftBlocks.PERSONAL_WORLD_SPIKE, "Personal Worldspike");

    this.addBlock(RailcraftBlocks.FRAME, "Frame");
    this.addBlock(RailcraftBlocks.NICKEL_IRON_BATTERY, "Nickel-Iron Battery");
    this.addBlock(RailcraftBlocks.NICKEL_ZINC_BATTERY, "Nickel-Zinc Battery");
    this.addBlock(RailcraftBlocks.ZINC_CARBON_BATTERY, "Zinc-Carbon Battery");
    this.addBlock(RailcraftBlocks.ZINC_CARBON_BATTERY_EMPTY, "Zinc-Carbon Empty Battery");
    this.addBlock(RailcraftBlocks.ZINC_SILVER_BATTERY, "Zinc-Silver Battery");
    this.addBlock(RailcraftBlocks.ZINC_SILVER_BATTERY_EMPTY, "Zinc-Silver Empty Battery");

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
    this.addItem(RailcraftItems.ENERGY_MINECART, "Minecart with Energy cell");
    this.addItem(RailcraftItems.WORLD_SPIKE_MINECART, "Minecart with Worldspike");
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
    this.addItem(RailcraftItems.GOLDEN_TICKET, "Golden Ticket");
    this.addItem(RailcraftItems.TICKET, "Ticket");
    this.addItem(RailcraftItems.ROUTING_TABLE_BOOK, "Routing Table Book");
    this.addItem(RailcraftItems.WHISTLE_TUNER, "Whistle Tuner");
    this.addItem(RailcraftItems.TUNNEL_BORE, "Tunnel Bore");
    this.addItem(RailcraftItems.TRACK_LAYER, "Track Layer Cart");
    this.addItem(RailcraftItems.TRACK_RELAYER, "Track Relayer Cart");
    this.addItem(RailcraftItems.TRACK_REMOVER, "Track Remover Cart");
    this.addItem(RailcraftItems.TRACK_UNDERCUTTER, "Track Undercutter Cart");
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
    this.addItem(RailcraftItems.TIN_RAW, "Raw Tin");
    this.addItem(RailcraftItems.ZINC_RAW, "Raw Zinc");
    this.addItem(RailcraftItems.NICKEL_RAW, "Raw Nickel");
    this.addItem(RailcraftItems.SILVER_RAW, "Raw Silver");
    this.addItem(RailcraftItems.LEAD_RAW, "Raw Lead");
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
    this.addItem(RailcraftItems.SLAG, "Granulated Slag");
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
    this.addItem(RailcraftItems.DUMPING_TRACK_KIT, "Dumping Track Kit");
    this.addItem(RailcraftItems.LAUNCHER_TRACK_KIT, "Launcher Track Kit");
    this.addItem(RailcraftItems.ONE_WAY_TRACK_KIT, "One-Way Track Kit");
    this.addItem(RailcraftItems.WHISTLE_TRACK_KIT, "Whistle Track Kit");
    this.addItem(RailcraftItems.LOCOMOTIVE_TRACK_KIT, "Locomotive Track Kit");
    this.addItem(RailcraftItems.THROTTLE_TRACK_KIT, "Throttle Track Kit");
    this.addItem(RailcraftItems.ROUTING_TRACK_KIT, "Routing Track Kit");
    this.addItem(RailcraftItems.CHARGE_SPOOL_LARGE, "Large Charge Wire Spool");
    this.addItem(RailcraftItems.CHARGE_SPOOL_MEDIUM, "Medium Charge Wire Spool");
    this.addItem(RailcraftItems.CHARGE_SPOOL_SMALL, "Small Charge Wire Spool");
    this.addItem(RailcraftItems.CHARGE_COIL, "Charge Coil");
    this.addItem(RailcraftItems.CHARGE_TERMINAL, "Charge Terminal");
    this.addItem(RailcraftItems.CHARGE_MOTOR, "Charge Motor");
    this.addItem(RailcraftItems.CHARGE_METER, "Charge Meter");
  }

  private void entityTranslations() {
    this.addEntityType(RailcraftEntityTypes.TANK_MINECART, "Minecart with Tank");
    this.addEntityType(RailcraftEntityTypes.ENERGY_MINECART, "Minecart with Energy cell");
    this.addEntityType(RailcraftEntityTypes.WORLD_SPIKE, "Minecart with Worldspike");
    this.addEntityType(RailcraftEntityTypes.CREATIVE_LOCOMOTIVE, "Creative Locomotive");
    this.addEntityType(RailcraftEntityTypes.STEAM_LOCOMOTIVE, "Steam Locomotive");
    this.addEntityType(RailcraftEntityTypes.ELECTRIC_LOCOMOTIVE, "Electric Locomotive");
    this.addEntityType(RailcraftEntityTypes.TUNNEL_BORE, "Tunnel Bore");
    this.addEntityType(RailcraftEntityTypes.TRACK_LAYER, "Track Layer Cart");
    this.addEntityType(RailcraftEntityTypes.TRACK_RELAYER, "Track Relayer Cart");
    this.addEntityType(RailcraftEntityTypes.TRACK_REMOVER, "Track Remover Cart");
    this.addEntityType(RailcraftEntityTypes.TRACK_UNDERCUTTER, "Track Undercutter Cart");
  }

  private void fluidTranslations() {
    this.addFluidType(RailcraftFluidTypes.STEAM, "Steam");
    this.addFluidType(RailcraftFluidTypes.CREOSOTE, "Creosote");
  }

  private void containerTranslations() {
    this.add(Translations.Container.COKE_OVEN, "Coke Oven");
    this.add(Translations.Container.BLAST_FURNACE, "Blast Furnace");
    this.add(Translations.Container.CRUSHER, "Crusher");
    this.add(Translations.Container.STEAM_OVEN, "Steam Oven");
    this.add(Translations.Container.TANK, "Tank");
    this.add(Translations.Container.WATER_TANK_SIDING, "Water Tank");
    this.add(Translations.Container.STEAM_TURBINE, "Steam Turbine");
    this.add(Translations.Container.SOLID_FUELED_STEAM_BOILER, "Solid Fueled Steam Boiler");
    this.add(Translations.Container.FLUID_FUELED_STEAM_BOILER, "Fluid Fueled Steam Boiler");
  }

  private void tipsTranslations() {
    this.add(MaintenanceMinecart.Mode.ON.getTipsKey(),
        "While On, will actively perform maintenance functions");
    this.add(MaintenanceMinecart.Mode.OFF.getTipsKey(),
        "While Off, will not perform maintenance functions and will have higher max speed");

    this.add(Translations.Tips.ROUTING_TICKET_ISSUER, "Issuer:");
    this.add(Translations.Tips.ROUTING_TICKET_DEST, "Destination:");
    this.add(Translations.Tips.ROUTING_TICKET_BLANK, "Blank Ticket");
    this.add(Translations.Tips.ROUTING_TABLE_BOOK_LAST_EDIT, "Last edited by %s");
    this.add(Translations.Tips.LOCOMOTIVE_SLOT_TICKET, "Insert Ticket");
    this.add(Translations.Tips.LOCOMOTIVE_ITEM_OWNER, "Owner:");
    this.add(Translations.Tips.LOCOMOTIVE_ITEM_PRIMARY, "Primary color:");
    this.add(Translations.Tips.LOCOMOTIVE_ITEM_SECONDARY, "Secondary color:");
    this.add(Translations.Tips.LOCOMOTIVE_ITEM_NO_WHISTLE, "There is currently no whistle installed");
    this.add(Translations.Tips.LOCOMOTIVE_ITEM_WHISTLE, "Whistle pitch:");
    this.add(Translations.Tips.TRACK_LAYER, "Lays track as it moves");
    this.add(Translations.Tips.TRACK_RELAYER, "Replaces one track with another");
    this.add(Translations.Tips.TRACK_REMOVER, "Removes tracks it passes over");
    this.add(Translations.Tips.TRACK_UNDERCUTTER, "Replaces the blocks under the track");
    this.add(Translations.Tips.CRUSHED_OBSIDIAN, "Prevents Mobs Spawns");
    this.add(Translations.Tips.COKE_OVEN, "Multi-Block: 3x3x3 (Hollow)");
    this.add(Translations.Tips.WATER_TANK_SIDING, "Multi-Block: 3x3x3 (Hollow)");
    this.add(Translations.Tips.BLAST_FURNACE, "Multi-Block: 3x4x3 (Hollow)");
    this.add(Translations.Tips.MULTIBLOCK2X2X2, "Multi-Block: 2x2x2");
    this.add(Translations.Tips.MULTIBLOCK3X2X2, "Multi-Block: 3x2x2");
    this.add(Translations.Tips.STEAM_TURBINE_DESC_1, "Generates FE from Steam");
    this.add(Translations.Tips.STEAM_TURBINE_DESC_2, "Outputs water to the bottom");
    this.add(Translations.Tips.STEAM_TURBINE_DESC_3, "Requires a Turbine Rotor");
    this.add(Translations.Tips.SPIKE_MAUL, "Converts track to switches and junctions.");
    this.add(Translations.Tips.OVERALLS, "Protection against the dangers of track-working");
    this.add(Translations.Tips.NONE, "None");
    this.add(Translations.Tips.GOOGLES_DESC, "Right-click to change aura.");
    this.add(Translations.Tips.GOOGLES_AURA, "Current Aura:");
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
    this.add(Translations.Tips.CROWBAR_SEASON_DESC, "Current season:");

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
    this.add(Translations.Tips.TOKEN_SIGNAL_BOX, "Detects carts entering/leaving Token Area");
    this.add(Translations.Tips.FIRESTONE_EMPTY,
        "Its energies controlled, it could be useful if you could recharge it...");
    this.add(Translations.Tips.FIRESTONE_CHARGED,
        "Filled with energy, you only need to exert your will onto it to release it's blistering heat...");
    this.add(Translations.Tips.RAW_FIRESTONE,
        "It quickens in your hands, you can feel it in the very air around you...");
    this.add(Translations.Tips.CUT_FIRESTONE, "Still its energies rage out of control...");
    this.add(Translations.Tips.FIRESTONE_ORE, "It lies in the lava beds in the nether");
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
    this.add(Translations.Tips.LOGBOOK, "Records visitors");
    this.add(Translations.Tips.ROLLING_MACHINE, "Crafting bench for various metal items");
    this.add(Translations.Tips.ITEM_LOADER, "Loads items into carts");
    this.add(Translations.Tips.ITEM_UNLOADER, "Unloads items from carts");
    this.add(Translations.Tips.FLUID_LOADER, "Loads fluids into carts");
    this.add(Translations.Tips.FLUID_UNLOADER, "Unloads fluids from carts");
    this.add(Translations.Tips.CART_DISPENSER, "Dispenses carts onto tracks");
    this.add(Translations.Tips.TRAIN_DISPENSER, "Dispenses linked trains onto tracks");
    this.add(Translations.Tips.FRAME, "Use to power track");
    this.add(Translations.Tips.SIGNAL_BLOCK_SURVEYOR, "Links Signals together");
    this.add(Translations.Tips.LINKS_CONTROLLERS_TO_RECEIVERS, "Links Controllers to Receivers");
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
    this.add(Translations.Tips.HIT_CROWBAR_TO_CHANGE_TICKET,
        "- Hit with Crowbar to change ticket -");
    this.add(Translations.Tips.HIT_CROWBAR_TO_ROTATE, "- Hit with Crowbar to rotate -");
    this.add(Translations.Tips.PAIR_WITH_CONTROL_TRACK, "- Pair with Control track -");
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
    this.add(Translations.Tips.DUMPING_TRACK, "Drops entities or items through the track");
    this.add(Translations.Tips.GATED_TRACK, "Track with built-in gate");
    this.add(Translations.Tips.COUPLER_TRACK, "Couples/Decouples passing carts");
    this.add(Translations.Tips.LAUNCHER_TRACK, "Makes carts fly!");
    this.add(Translations.Tips.ONE_WAY_TRACK, "Carts can only pass in the direction of the arrow");
    this.add(Translations.Tips.WHISTLE_TRACK, "Passing locomotives will blow their whistle");
    this.add(Translations.Tips.LOCOMOTIVE_TRACK, "Starts/Stops locomotive");
    this.add(Translations.Tips.THROTTLE_TRACK, "Adjusts the speed of passing locomotive");
    this.add(Translations.Tips.LOCKING_TRACK, "Stops and holds carts");
    this.add(Translations.Tips.TRANSITION_TRACK, "Transitions to/from high speed");
    this.add(Translations.Tips.ROUTING_TRACK, "Sets a passing locomotive's ticket");
    this.add(Translations.Tips.SWITCH_TRACKS, "Controls adjacent switch tracks");
    this.add(Translations.Tips.MANUAL_OPERATION, "Manual Operation");
    this.add(Translations.Tips.SCRIPTED_LOGIC_OPERATION, "Scripted Logic Operation");
    this.add(Translations.Tips.RELEVANT_TOOLS, "Relevant tools:");
    this.add(Translations.Tips.SIGNAL_TUNER, "Signal Tuner");
    this.add(Translations.Tips.AERIAL_LINKAGES, "Aerial linkages:");
    this.add(Translations.Tips.RECEIVERS, "%sx Receivers");
    this.add(Translations.Tips.REDSTONE_LINKAGE, "Redstone linkage:");
    this.add(Translations.Tips.LISTEN, "Listen");
    this.add(Translations.Tips.CLICK_TO_CRAFT, "- Click to craft -");
    this.add(Translations.Tips.ABANDONED_TRACK, "Can be suspended over gaps in terrain");
    this.add(Translations.Tips.REINFORCED_TRACK, "125% of the speed of normal track");
    this.add(Translations.Tips.STRAP_IRON_TRACK, "30% of the speed of iron track");
    this.add(Translations.Tips.DANGER, "Danger:");
    this.add(Translations.Tips.DERAILMENT_RISK, "Derailment Risk");
    this.add(Translations.Tips.HIGH_VOLTAGE, "High Voltage");
    this.add(Translations.Tips.HIGH_SPEED, "High Speed");
    this.add(Translations.Tips.HIGH_VOLTAGE_SPEED, "High Voltage/Speed");
    this.add(Translations.Tips.POWERED_BY_ELECTRICITY, "Powered by electricity");
    this.add(Translations.Tips.USE_ELECTRIC_LOCOMOTIVE, "Use with Electric Locomotive");
    this.add(Translations.Tips.VERY_FAST, "Very fast");
    this.add(Translations.Tips.REQUIRE_BOOSTERS_TRANSITION,
        "Requires Boosters/Transitions to reach speed");
    this.add(Translations.Tips.CANNOT_MAKE_CORNERS_HIGH_SPEED,
        "Cannot make corners at High Speed");
    this.add(Translations.Tips.EXPLOSION_RESISTANT, "Explosion resistant");
    this.add(Translations.Tips.CHARGE_NETWORK_BATTERY, "Charge Network Battery");
    this.add(Translations.Tips.CHARGE_NETWORK_EMPTY_BATTERY, "Charge Network Empty Battery");
    this.add(Translations.Tips.CAPACITY, "Capacity: %sKFE");
    this.add(Translations.Tips.MAX_DRAW, "Max Draw: %s FE/t");
    this.add(Translations.Tips.LOSS, "Loss: %s FE/t");
    this.add(Translations.Tips.EFFICIENCY, "Efficiency: %s%%");
    this.add(Translations.Tips.TYPE_DISPOSABLE, "Type: Disposable");
    this.add(Translations.Tips.TYPE_RECHARGEABLE, "Type: Rechargeable");
    this.add(Translations.Tips.BLOCK_SIGNAL, "Detects carts in signal block");
    this.add(Translations.Tips.SIGNAL_BLOCKS, "%sx Signal block");
    this.add(Translations.Tips.CONTROLLERS, "%sx Controller block");
    this.add(Translations.Tips.SIGNAL_SURVEYOR, "Signal Surveyor");
    this.add(Translations.Tips.DISTANT_SIGNAL, "Displays aspect of linked controller");
    this.add(Translations.Tips.TOKEN_SIGNAL, "Detects carts entering/leaving Token Area");
    this.add(Translations.Tips.TOKEN_AREA, "%sx Token Area");
    this.add(Translations.Tips.EMPTY, "Empty");
    this.add(Translations.Tips.ENERGY, "Energy:");
    this.add(Translations.Tips.PERCENTAGE, "Percentage:");
  }

  private void screenTranslations() {
    this.add(MaintenanceMinecart.Mode.ON.getTranslationKey(), "On");
    this.add(MaintenanceMinecart.Mode.OFF.getTranslationKey(), "Off");

    this.add(Translations.Screen.STEAM_TURBINE_ROTOR, "Rotor:");
    this.add(Translations.Screen.STEAM_TURBINE_OUTPUT, "Output:");
    this.add(Translations.Screen.STEAM_TURBINE_USAGE, "Usage:");
    this.add(Translations.Screen.STEAM_MODE_DESC_IDLE,
        "Locomotive reduces fuel usage, but retains its heat. If the train is held by a track, it behaves as if idle.");
    this.add(Translations.Screen.STEAM_MODE_DESC_RUNNING, "Makes the locomotive move.");
    this.add(Translations.Screen.STEAM_MODE_DESC_SHUTDOWN,
        "Shuts off the locomotive. Halts all movement and begins the cool-down process.");
    this.add(Translations.Screen.ELECTRIC_MODE_DESC_RUNNING, "Makes the locomotive move.");
    this.add(Translations.Screen.ELECTRIC_MODE_DESC_SHUTDOWN, "Stops the locomotive.");
    this.add(Translations.Screen.CREATIVE_MODE_DESC_RUNNING, "Makes the locomotive move.");
    this.add(Translations.Screen.CREATIVE_MODE_DESC_SHUTDOWN, "Stops the locomotive.");
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
    this.add(Translations.Screen.WATER_TANK_SEE_SKY, "Can see sky: %s");
    this.add(Translations.Screen.WATER_TANK_BASE_RATE, "Base rate: %s mB/sec");
    this.add(Translations.Screen.WATER_TANK_HUMIDITY, "Humidity multiplier: %s");
    this.add(Translations.Screen.WATER_TANK_PRECIPITATION, "Precipitation multiplier: %s");
    this.add(Translations.Screen.WATER_TANK_TEMP, "Temperature modifier: %s mB");
    this.add(Translations.Screen.WATER_TANK_FINAL_RATE, "Final rate: %s mB/sec");
    this.add(Translations.Screen.PATTERN, "Pattern");
    this.add(Translations.Screen.STOCK, "Stock");
    this.add(Translations.Screen.UNDER, "Under");
    this.add(Translations.Screen.SIDES, "Sides");
    this.add(Translations.Screen.HELP, "Help");
    this.add(Translations.Screen.NAME, "Name");
    this.add(Translations.Screen.GOLDEN_TICKET_TITLE, "Golden Ticket");
    this.add(Translations.Screen.GOLDEN_TICKET_DESC_1, "This ticket is valid");
    this.add(Translations.Screen.GOLDEN_TICKET_DESC_2, "for the following destination:");
    this.add(Translations.Screen.GOLDEN_TICKET_ABOUT, "About the Golden Ticket");
    this.add(Translations.Screen.GOLDEN_TICKET_HELP, """
        The Golden Ticket is an unlimited use Ticket that can be used to ride any public train \
        or train owned by the issuer. It can also be combined with paper to produce single \
        use tickets.""");
    this.add(Translations.Screen.ROUTING_TABLE_BOOK, "Routing Table Book");
    this.add(Translations.Screen.SWITCH_TRACK_ROUTER_PUBLIC_RAILWAY, "Public Railway");
    this.add(Translations.Screen.SWITCH_TRACK_ROUTER_PUBLIC_RAILWAY_DESC,
        "Will route any locomotive");
    this.add(Translations.Screen.SWITCH_TRACK_ROUTER_PRIVATE_RAILWAY, "Private Railway");
    this.add(Translations.Screen.SWITCH_TRACK_ROUTER_PRIVATE_RAILWAY_DESC,
        "Will only route locomotive that belong to %s");
    this.add(Translations.Screen.LOCOMOTIVE_DETECTOR_PRIMARY, "Primary Color");
    this.add(Translations.Screen.LOCOMOTIVE_DETECTOR_SECONDARY, "Secondary Color");
    this.add(Translations.Screen.FILTER, "Filter");
    this.add(Translations.Screen.TANK_DETECTOR_VOID, "Match any amount");
    this.add(Translations.Screen.TANK_DETECTOR_EMPTY, "Match empty tanks");
    this.add(Translations.Screen.TANK_DETECTOR_NOT_EMPTY, "Match non-empty tanks");
    this.add(Translations.Screen.TANK_DETECTOR_FULL, "Match full tanks");
    this.add(Translations.Screen.TANK_DETECTOR_QUARTER, "Match more than quarter full tanks");
    this.add(Translations.Screen.TANK_DETECTOR_HALF, "Match more than half full tanks");
    this.add(Translations.Screen.TANK_DETECTOR_MOST, "Match more than 3/4th full tanks");
    this.add(Translations.Screen.TANK_DETECTOR_LESS_THAN_QUARTER,
        "Match less than quarter full tanks");
    this.add(Translations.Screen.TANK_DETECTOR_LESS_THAN_HALF, "Match less than half full tanks");
    this.add(Translations.Screen.TANK_DETECTOR_LESS_THAN_MOST, "Match less than 3/4th full tanks");
    this.add(Translations.Screen.TANK_DETECTOR_LESS_THAN_FULL, "Match less than full tanks");
    this.add(Translations.Screen.TANK_DETECTOR_ANALOG,
        "Emit Analog Redstone based on amount in tank");
    this.add(Translations.Screen.TRAIN_DETECTOR_SIZE, "Train Size: %s");
    this.add(Translations.Screen.ITEM_DETECTOR_EMPTY, "Empty");
    this.add(Translations.Screen.ITEM_DETECTOR_FULL, "Full");
    this.add(Translations.Screen.ITEM_DETECTOR_ANYTHING, "Anything");
    this.add(Translations.Screen.ITEM_DETECTOR_FILTERED, "Filtered");
    this.add(Translations.Screen.ITEM_DETECTOR_NOT_EMPTY, "Not empty");
    this.add(Translations.Screen.ITEM_DETECTOR_ANALOG, "Analog");
    this.add(Translations.Screen.ITEM_DETECTOR_AT_LEAST, "At least");
    this.add(Translations.Screen.ITEM_DETECTOR_AT_MOST, "At most");
    this.add(Translations.Screen.ITEM_DETECTOR_EXACTLY, "Exactly");
    this.add(Translations.Screen.ITEM_DETECTOR_LESS_THAN, "Less than");
    this.add(Translations.Screen.ITEM_DETECTOR_GREATER_THAN, "Greater than");
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
    this.addBlock(RailcraftBlocks.ABANDONED_DUMPING_TRACK, "Abandoned Dumping Track");
    this.addBlock(RailcraftBlocks.ABANDONED_TURNOUT_TRACK, "Abandoned Turnout Track");
    this.addBlock(RailcraftBlocks.ABANDONED_WYE_TRACK, "Abandoned Wye Track");
    this.addBlock(RailcraftBlocks.ABANDONED_JUNCTION_TRACK, "Abandoned Junction Track");
    this.addBlock(RailcraftBlocks.ABANDONED_LAUNCHER_TRACK, "Abandoned Launcher Track");
    this.addBlock(RailcraftBlocks.ABANDONED_ONE_WAY_TRACK, "Abandoned One-Way Track");
    this.addBlock(RailcraftBlocks.ABANDONED_WHISTLE_TRACK, "Abandoned Whistle Track");
    this.addBlock(RailcraftBlocks.ABANDONED_LOCOMOTIVE_TRACK, "Abandoned Locomotive Track");
    this.addBlock(RailcraftBlocks.ABANDONED_THROTTLE_TRACK, "Abandoned Throttle Track");
    this.addBlock(RailcraftBlocks.ABANDONED_ROUTING_TRACK, "Abandoned Routing Track");

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
    this.addBlock(RailcraftBlocks.ELECTRIC_DUMPING_TRACK, "Electric Dumping Track");
    this.addBlock(RailcraftBlocks.ELECTRIC_TURNOUT_TRACK, "Electric Turnout Track");
    this.addBlock(RailcraftBlocks.ELECTRIC_WYE_TRACK, "Electric Wye Track");
    this.addBlock(RailcraftBlocks.ELECTRIC_JUNCTION_TRACK, "Electric Junction Track");
    this.addBlock(RailcraftBlocks.ELECTRIC_LAUNCHER_TRACK, "Electric Launcher Track");
    this.addBlock(RailcraftBlocks.ELECTRIC_ONE_WAY_TRACK, "Electric One-Way Track");
    this.addBlock(RailcraftBlocks.ELECTRIC_WHISTLE_TRACK, "Electric Whistle Track");
    this.addBlock(RailcraftBlocks.ELECTRIC_LOCOMOTIVE_TRACK, "Electric Locomotive Track");
    this.addBlock(RailcraftBlocks.ELECTRIC_THROTTLE_TRACK, "Electric Throttle Track");
    this.addBlock(RailcraftBlocks.ELECTRIC_ROUTING_TRACK, "Electric Routing Track");

    this.addBlock(RailcraftBlocks.HIGH_SPEED_TRACK, "High Speed Track");
    this.addBlock(RailcraftBlocks.HIGH_SPEED_LOCKING_TRACK, "High Speed Locking Track");
    this.addBlock(RailcraftBlocks.HIGH_SPEED_ACTIVATOR_TRACK, "High Speed Activator Track");
    this.addBlock(RailcraftBlocks.HIGH_SPEED_BOOSTER_TRACK, "High Speed Booster Track");
    this.addBlock(RailcraftBlocks.HIGH_SPEED_DETECTOR_TRACK, "High Speed Detector Track");
    this.addBlock(RailcraftBlocks.HIGH_SPEED_TURNOUT_TRACK, "High Speed Turnout Track");
    this.addBlock(RailcraftBlocks.HIGH_SPEED_WYE_TRACK, "High Speed Wye Track");
    this.addBlock(RailcraftBlocks.HIGH_SPEED_JUNCTION_TRACK, "High Speed Junction Track");
    this.addBlock(RailcraftBlocks.HIGH_SPEED_TRANSITION_TRACK, "High Speed Transition Track");
    this.addBlock(RailcraftBlocks.HIGH_SPEED_WHISTLE_TRACK, "High Speed Whistle Track");
    this.addBlock(RailcraftBlocks.HIGH_SPEED_LOCOMOTIVE_TRACK, "High Speed Locomotive Track");
    this.addBlock(RailcraftBlocks.HIGH_SPEED_THROTTLE_TRACK, "High Speed Throttle Track");

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
    this.addBlock(RailcraftBlocks.HIGH_SPEED_ELECTRIC_WHISTLE_TRACK,
        "High Speed Electric Whistle Track");
    this.addBlock(RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCOMOTIVE_TRACK,
        "High Speed Electric Locomotive Track");
    this.addBlock(RailcraftBlocks.HIGH_SPEED_ELECTRIC_THROTTLE_TRACK,
        "High Speed Electric Throttle Track");

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
    this.addBlock(RailcraftBlocks.REINFORCED_DUMPING_TRACK, "Reinforced Dumping Track");
    this.addBlock(RailcraftBlocks.REINFORCED_TURNOUT_TRACK, "Reinforced Turnout Track");
    this.addBlock(RailcraftBlocks.REINFORCED_WYE_TRACK, "Reinforced Wye Track");
    this.addBlock(RailcraftBlocks.REINFORCED_JUNCTION_TRACK, "Reinforced Junction Track");
    this.addBlock(RailcraftBlocks.REINFORCED_LAUNCHER_TRACK, "Reinforced Launcher Track");
    this.addBlock(RailcraftBlocks.REINFORCED_ONE_WAY_TRACK, "Reinforced One-Way Track");
    this.addBlock(RailcraftBlocks.REINFORCED_WHISTLE_TRACK, "Reinforced Whistle Track");
    this.addBlock(RailcraftBlocks.REINFORCED_LOCOMOTIVE_TRACK, "Reinforced Locomotive Track");
    this.addBlock(RailcraftBlocks.REINFORCED_THROTTLE_TRACK, "Reinforced Throttle Track");
    this.addBlock(RailcraftBlocks.REINFORCED_ROUTING_TRACK, "Reinforced Routing Track");

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
    this.addBlock(RailcraftBlocks.STRAP_IRON_DUMPING_TRACK, "Strap Iron Dumping Track");
    this.addBlock(RailcraftBlocks.STRAP_IRON_TURNOUT_TRACK, "Strap Iron Turnout Track");
    this.addBlock(RailcraftBlocks.STRAP_IRON_WYE_TRACK, "Strap Iron Wye Track");
    this.addBlock(RailcraftBlocks.STRAP_IRON_JUNCTION_TRACK, "Strap Iron Junction Track");
    this.addBlock(RailcraftBlocks.STRAP_IRON_LAUNCHER_TRACK, "Strap Iron Launcher Track");
    this.addBlock(RailcraftBlocks.STRAP_IRON_ONE_WAY_TRACK, "Strap Iron One-Way Track");
    this.addBlock(RailcraftBlocks.STRAP_IRON_WHISTLE_TRACK, "Strap Iron Whistle Track");
    this.addBlock(RailcraftBlocks.STRAP_IRON_LOCOMOTIVE_TRACK, "Strap Iron Locomotive Track");
    this.addBlock(RailcraftBlocks.STRAP_IRON_THROTTLE_TRACK, "Strap Iron Throttle Track");
    this.addBlock(RailcraftBlocks.STRAP_IRON_ROUTING_TRACK, "Strap Iron Routing Track");

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
    this.addBlock(RailcraftBlocks.IRON_DUMPING_TRACK, "Iron Dumping Track");
    this.addBlock(RailcraftBlocks.IRON_TURNOUT_TRACK, "Iron Turnout Track");
    this.addBlock(RailcraftBlocks.IRON_WYE_TRACK, "Iron Wye Track");
    this.addBlock(RailcraftBlocks.IRON_JUNCTION_TRACK, "Iron Junction Track");
    this.addBlock(RailcraftBlocks.IRON_LAUNCHER_TRACK, "Iron Launcher Track");
    this.addBlock(RailcraftBlocks.IRON_ONE_WAY_TRACK, "Iron One-Way Track");
    this.addBlock(RailcraftBlocks.IRON_WHISTLE_TRACK, "Iron Whistle Track");
    this.addBlock(RailcraftBlocks.IRON_LOCOMOTIVE_TRACK, "Iron Locomotive Track");
    this.addBlock(RailcraftBlocks.IRON_THROTTLE_TRACK, "Iron Throttle Track");
    this.addBlock(RailcraftBlocks.IRON_ROUTING_TRACK, "Iron Routing Track");

    this.addBlock(RailcraftBlocks.ELEVATOR_TRACK, "Elevator Track");
  }

  private void jeiTranslations() {
    this.add(Translations.Jei.METAL_ROLLING, "Metal Rolling");
    this.add(Translations.Jei.COKE_OVEN, "Coke Oven Smelting");
    this.add(Translations.Jei.BLAST_FURNACE, "Blast Furnace Smelting");
    this.add(Translations.Jei.CRUSHER, "Rock Crushing");
    this.add(Translations.Jei.CRUSHER_TIP, "(%s%% chance)");
    this.add(Translations.Jei.MANUAL_ROLLING_MACHINE, """
        Machine for rolling various shapes of metal. If there is only enough items in the grid \
        for one cycle, you can click the output display to force crafting.""");
    this.add(Translations.Jei.POWERED_ROLLING_MACHINE, """
        Machine for rolling various shapes of metal. Can be automated by simply piping items \
        in and out. If there is only enough items in the grid for one cycle, you can click the \
        output display to force crafting.""");
    this.add(Translations.Jei.FEED_STATION, """
        Allows you to feed nearby animals automatically. Can be disabled with Redstone. \
        Will stop feeding if there are too many animals nearby to prevent over-population.""");
    this.add(Translations.Jei.LOGBOOK, """
        The Logbook records any player that passes within a 16 blocks radius of the block and \
        the day they visited. It can only be broken by the owner.""");
    this.add(Translations.Jei.BLOCK_SIGNAL, """
        This is the basic signal used for cart detection. It pairs one-to-one with an adjacent \
        Block Signal and will detect any carts between the two and sends the resulting aspect to \
        a paired Receiver.""");
    this.add(Translations.Jei.DISTANT_SIGNAL, """
        This type of signal is primarily intended for cosmetic usage. It doesn't interface with \
        carts at all and simply displays the aspect sent to it from a paired Controller.""");
    this.add(Translations.Jei.TOKEN_SIGNAL, """
        This signal can be used to define an area to monitor for carts entering and leaving. \
        It pairs into a net of signals, with no particular limit to the number of signals \
        defining the area. Recommended for junctions and diagonals. It will send the resulting \
        aspect to a paired Receiver.""");
    this.add(Translations.Jei.DUAL_BLOCK_SIGNAL, """
        This signal contains a standard Block Signal on the upper lamp and a Distant Signal on \
        the lower. It is both a Controller and a Receiver.""");
    this.add(Translations.Jei.DUAL_DISTANT_SIGNAL,
        "This signal contains two Distant Signals. They can be paired to different Controllers.");
    this.add(Translations.Jei.DUAL_TOKEN_SIGNAL, """
        This signal a Token Signal on the upper lamp and Distant Signal on the lower. \
        It is both a Controller and a Receiver.""");
    this.add(Translations.Jei.TUNNEL_BORE,
        "Machine designed for digging tunnels and laying track.");
    this.add(Translations.Jei.NICKEL_IRON_BATTERY,
        "A battery designed to work with the charge network. Good for general purpose usage.");
    this.add(Translations.Jei.NICKEL_ZINC_BATTERY, """
        A battery designed to work with the charge network. \
        Good for low draw, long term storage.""");
    this.add(Translations.Jei.DISPOSABLE_BATTERY, """
        A battery designed to work with the charge network. \
        Good for cheap, single use storage. Comes fully charged.""");
    this.add(Translations.Jei.DISPOSABLE_BATTERY_EMPTY, """
        A battery designed to work with the charge network. \
        Good for cheap, single use storage. \
        This one is empty and can be recycled in the Crusher.""");
    this.add(Translations.Jei.WORLD_SPIKE, """
        Loads a 3x3 area of chunks at world load and keeps them loaded.""");
    this.add(Translations.Jei.PERSONAL_WORLD_SPIKE, """
       Loads a 3x3 area of chunks at world load and keeps them loaded. \
       It can only be broken by the owner.""");
    this.add(Translations.Jei.WORLD_SPIKE_MINECART, """
       Load the current chunk and the north, east, south and west chunks, and keeps them loaded.""");
    this.add(Translations.Jei.PAINT, "Paint");
    this.add(Translations.Jei.COPY_TAG, "Copy Tag");
    this.add(Translations.Jei.REPAIR, "Repair");
    this.add(Translations.Jei.SPLIT, "Split");
  }

  private void lookingAtTranslations() {
    this.add(Translations.LookingAt.SIGNALS, "Signals");
    this.add(Translations.LookingAt.SWITCH_TRACK, "Switch Track");
    this.add(Translations.LookingAt.LOCOMOTIVE, "Locomotive");
    this.add(Translations.LookingAt.TRACK_COMPONENT, "Track Component");
    this.add(Translations.LookingAt.ASPECT_SENT, "Aspect sent: ");
    this.add(Translations.LookingAt.ASPECT_RECEIVED, "Aspect received: ");
    this.add(Translations.LookingAt.ASPECT_RELAYED, "Aspect relayed: ");
    this.add(Translations.LookingAt.SWITCHED, "Switched: ");
    this.add(Translations.LookingAt.YES, "Yes");
    this.add(Translations.LookingAt.NO, "No");
    this.add(Translations.LookingAt.MODE, "Mode: ");
    this.add(Translations.LookingAt.SPEED, "Speed: %sx");
    this.add(Translations.LookingAt.REVERSE, "Reverse: ");
  }

  private void signalTranslations() {
    this.add(Translations.Signal.SIGNAL_SURVEYOR_INVALID_TRACK, "Track not found");
    this.add(Translations.Signal.SIGNAL_SURVEYOR_BEGIN, "Beginning signal survey");
    this.add(Translations.Signal.SIGNAL_SURVEYOR_SUCCESS, "Successfully paired %s to %s");
    this.add(Translations.Signal.SIGNAL_SURVEYOR_INVALID_PAIR, "Invalid pair");
    this.add(Translations.Signal.SIGNAL_SURVEYOR_LOST, "First signal has been destroyed");
    this.add(Translations.Signal.SIGNAL_SURVEYOR_ABANDONED, "Signal survey abandoned");
    this.add(Translations.Signal.SIGNAL_SURVEYOR_INVALID_BLOCK, "Invalid signal");

    this.add(Translations.Signal.SIGNAL_TUNER_BEGIN, "Started pairing %s with a receiver");
    this.add(Translations.Signal.SIGNAL_TUNER_INVALID_CONTROLLER, "Invalid signal controller");
    this.add(Translations.Signal.SIGNAL_TUNER_INVALID_RECEIVER, "Invalid signal receiver");
    this.add(Translations.Signal.SIGNAL_TUNER_ABANDONED, "Pairing abandoned");
    this.add(Translations.Signal.SIGNAL_TUNER_ALREADY_PAIRED, "%s is already paired to %s");
    this.add(Translations.Signal.SIGNAL_TUNER_LOST, "Signal controller has been destroyed");
    this.add(Translations.Signal.SIGNAL_TUNER_SUCCESS, "Successfully paired %s to %s");
  }

  private void enchantmentTranslations() {
    this.addEnchantment(RailcraftEnchantments.WRECKING, "Wrecking");
    this.addEnchantment(RailcraftEnchantments.DESTRUCTION, "Destruction");
    this.addEnchantment(RailcraftEnchantments.IMPLOSION, "Implosion");
    this.addEnchantment(RailcraftEnchantments.SMACK, "Smack");

    this.add(Translations.EnchantmentDescriptions.WRECKING, "Increases attack damage.");
    this.add(Translations.EnchantmentDescriptions.DESTRUCTION, "Deconstructs blocks in a wider area.");
    this.add(Translations.EnchantmentDescriptions.IMPLOSION, "Increases damage against creepers.");
    this.add(Translations.EnchantmentDescriptions.SMACK, "Increases the boost applied to trains.");
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

  private void effectTranslations() {
    this.add(RailcraftMobEffects.CREOSOTE.get(), "Creosote");
  }

  private void potionTranslations() {
    final String prefixPotion = "item.minecraft.potion.effect.";
    final String prefixSplashPotion = "item.minecraft.splash_potion.effect.";
    final String prefixLingeringPotion = "item.minecraft.lingering_potion.effect.";
    final String prefixTippedArrow = "item.minecraft.tipped_arrow.effect.";

    final String creosote = RailcraftPotions.CREOSOTE.getId().getPath();
    this.add(prefixPotion + creosote, "Potion of Creosote");
    this.add(prefixSplashPotion + creosote, "Splash Potion of Creosote");
    this.add(prefixLingeringPotion + creosote, "Lingering Potion of Creosote");
    this.add(prefixTippedArrow + creosote, "Arrow of Creosote");
  }

  private void villagerTranslations() {
    final String trackman = Translations.makeKey("entity.minecraft.villager",
        RailcraftVillagerProfession.TRACKMAN.get().name());
    final String railman = Translations.makeKey("entity.minecraft.villager",
        RailcraftVillagerProfession.CARTMAN.get().name());

    this.add(trackman, "Trackman");
    this.add(railman, "Cartman");
  }

  private void damageSourceTranslations() {
    this.add(Translations.DamageSource.BORE.get(0), "%s got in the way of progress");
    this.add(Translations.DamageSource.BORE.get(1), "%s tried to stop the Bore");
    this.add(Translations.DamageSource.BORE.get(2), "%s was crushed by the Bore");
    this.add(Translations.DamageSource.BORE.get(3),
        "%s thought they could do better than John Henry");
    this.add(Translations.DamageSource.BORE.get(4), "%s got stuck between a rock and a hard place");
    this.add(Translations.DamageSource.BORE.get(5), "%s was on the wrong side of progress");

    this.add(Translations.DamageSource.CRUSHER.get(0), "%s tripped near heavy machinery");
    this.add(Translations.DamageSource.CRUSHER.get(1),
        "%s is not the man of steel, they crush just like the rest of us");
    this.add(Translations.DamageSource.CRUSHER.get(2),
        "%s stared into the abyss, then they slipped");
    this.add(Translations.DamageSource.CRUSHER.get(3),
        "%s is going to need to replace their stuff");
    this.add(Translations.DamageSource.CRUSHER.get(4), "%s ignored the \"Watch Your Step!\" signs");
    this.add(Translations.DamageSource.CRUSHER.get(5), "On sale now! %s Blood Jelly");
    this.add(Translations.DamageSource.CRUSHER.get(6), "%s was turned into paste");
    this.add(Translations.DamageSource.CRUSHER.get(7), "%s should have read the manual");

    this.add(Translations.DamageSource.ELECTRIC.get(0), "%s is saying hello to Nikola Tesla");
    this.add(Translations.DamageSource.ELECTRIC.get(1),
        "%s discovered the meaning of \"High Voltage\"");
    this.add(Translations.DamageSource.ELECTRIC.get(2),
        "%s was enlightened to wonders of electrification");
    this.add(Translations.DamageSource.ELECTRIC.get(3), "%s should have read the warning label");
    this.add(Translations.DamageSource.ELECTRIC.get(4), "%s made a shocking revelation");
    this.add(Translations.DamageSource.ELECTRIC.get(5),
        "%s learned something about electricity, IT HURTS!");

    this.add(Translations.DamageSource.STEAM.get(0), "%s got cooked, nice and juicy");
    this.add(Translations.DamageSource.STEAM.get(1),
        "%s is not immune to high temperatures, despite claims to the contrary");
    this.add(Translations.DamageSource.STEAM.get(2), "%s discovered how painful Steam can be");
    this.add(Translations.DamageSource.STEAM.get(3), "%s had an industrial accident");
    this.add(Translations.DamageSource.STEAM.get(4), "%s probably shouldn't have stepped there");
    this.add(Translations.DamageSource.STEAM.get(5),
        "%s discovered that Steam baths are best left to saunas");

    this.add(Translations.DamageSource.TRACK_ELECTRIC.get(0), "%s pissed on an electrified track");
    this.add(Translations.DamageSource.TRACK_ELECTRIC.get(1),
        "%s discovered the meaning of \"High Voltage\"");
    this.add(Translations.DamageSource.TRACK_ELECTRIC.get(2),
        "%s was enlightened to wonders of electrification");
    this.add(Translations.DamageSource.TRACK_ELECTRIC.get(3), "%s tripped on the third rail");
    this.add(Translations.DamageSource.TRACK_ELECTRIC.get(4), "%s made a shocking revelation");
    this.add(Translations.DamageSource.TRACK_ELECTRIC.get(5),
        "%s learned something about electricity, IT HURTS!");

    this.add(Translations.DamageSource.TRAIN.get(0), "%s got hit by a train");
    this.add(Translations.DamageSource.TRAIN.get(1), "%s was playing on the tracks");
    this.add(Translations.DamageSource.TRAIN.get(2),
        "%s played chicken with a train; the train won");
    this.add(Translations.DamageSource.TRAIN.get(3), "%s caught the wrong train");
    this.add(Translations.DamageSource.TRAIN.get(4), "%s bought a one-way ticket to the afterlife");
    this.add(Translations.DamageSource.TRAIN.get(5), "%s should have worn their Overalls");
  }

  private void routingTableTranslations() {
    this.add(Translations.RoutingTable.ERROR_BLANK, "Blank Table");
    this.add(Translations.RoutingTable.ERROR_INVALID_CONSTANT, "Invalid Constant");
    this.add(Translations.RoutingTable.ERROR_MALFORMED_SYNTAX, "Malformed Syntax");
    this.add(Translations.RoutingTable.ERROR_INSUFFICIENT_OPERAND, "Insufficient Operands");
    this.add(Translations.RoutingTable.ERROR_INVALID_OPERAND, "Invalid Operand");
    this.add(Translations.RoutingTable.ERROR_INVALID_REGEX, "Invalid Regex Pattern");
    this.add(Translations.RoutingTable.ERROR_UNSUPPORTED_REGEX, "Regex Unsupported");
    this.add(Translations.RoutingTable.UNRECOGNIZED_KEYWORD, "Unrecognized Keyword");

    this.add(Translations.RoutingTable.MANUAL_PAGES.get(0), """
        The Routing Table when placed in a Routing Detector or Switch will define a set of rules \
        that are used to match against any passing Locomotive. These rules are define using a \
        simple logic syntax that allows you to create rules as simple or complex as you like. \
        The syntax is a Prefix Notation script, the Operators are followed by the Operands. \
        Only one keyword is allowed per line. If no Operator is specified, OR is assumed. \
        Routing Tables can be copied by placing two or more in a crafting grid.""");
    this.add(Translations.RoutingTable.MANUAL_PAGES.get(1), """
        Operator Keywords:
          AND - Two Operands, both must be true.
          OR - Two Operands, one must be true.
          NOT - Invert the following Operand.
          IF - Three Operands: cond, then, else.
               If cond is true, use then;
               otherwise, use else.
        """);
    this.add(Translations.RoutingTable.MANUAL_PAGES.get(2), """
        Condition Keywords:
          Dest=<string>
            If the Locomotive's Destination
            string starts with this string,
            the condition is true.
            "Dest=null" will match carts
            with no destination.
          Owner=<username>
            True if the Locomotive belongs
            to this person.
        """);
    this.add(Translations.RoutingTable.MANUAL_PAGES.get(3), """
        Condition Keywords:
          Name=<entityname>
            True if it matches the
            Minecart's name.
            "Name=null" will match carts
            with no custom name.
          Type=<modid:itemname>
            True if it matches the
            Minecart's item name.
        """);
    this.add(Translations.RoutingTable.MANUAL_PAGES.get(4), """
        Condition Keywords:
          Rider=<type>[:<qualifier>]
            True if the Train contains
                a matching passenger
                Simple Types:
                  any, none, mob, animal, unnamed
                Qualifier Capable Types:
                  player, named, entity
                Regex Capable Types:
                  player, named
                See GitHub Issue #844 for examples
        """);
    this.add(Translations.RoutingTable.MANUAL_PAGES.get(5), """
        Condition Keywords:
          Color=<primary>,<secondary>
            True if the Locomotive's primary
            and secondary colors match.
            Accepts "Any" as a wildcard.
            Colors: Black, Red, Green, Brown, Blue,
            Purple, Cyan, LightGray, Gray, Pink, Lime,
            Yellow, LightBlue, Magenta, Orange, White
          NeedsRefuel=<true/false>
            If the Locomotive is low on Fuel
            or Water this will divert it.
        """);
    this.add(Translations.RoutingTable.MANUAL_PAGES.get(6), """
        Condition Keywords:
          Redstone=<true/false>
            True if the Routing Block
            is being powered by Redstone.
        """);
    this.add(Translations.RoutingTable.MANUAL_PAGES.get(7), """
        Condition Keywords:
          Loco=<string>
            True if locomotive matches
            parameter string.
            Accepted strings: electric, steam,
            creative, none.
            Using parameter none will only return
            true if there is no locomotive.
        """);
    this.add(Translations.RoutingTable.MANUAL_PAGES.get(8), """
        Example Script:
          Dest=TheFarLands
          Color=Black,Red
          AND
          NOT
          Owner=Steve
          Dest=SecretHideout/OceanEntrance
        """);
    this.add(Translations.RoutingTable.MANUAL_PAGES.get(9), """
        Result:
          The script on the previous page will
          match a Locomotive with a destination
          of "TheFarLands/Milliways",
          or that is painted black and red,
          or that has a destination of
          "SecretHideout/OceanEntrance",
          but only if its not owned by Steve.
        """);
    this.add(Translations.RoutingTable.MANUAL_PAGES.get(10), """
        Regular Expressions:
          Some Conditions support
          Regular Expressions.
          To use a regex, add a '?' before the '='.
          Rules are standard Java Patterns.
        Supporting Conditions:
          Dest, Name
        Example:
          Dest?=.*Hill
        """);
    this.add(Translations.RoutingTable.MANUAL_PAGES.get(11), """
        Analog Output:
          IF can be used with integer constants
          for analog output. Ints and IF can
          only be used at top level, or as
          then or else to IF. TRUE and FALSE
          are usable anywhere.
        Example:
          IF
          Dest=Town
          8
          IF
          Dest=City
          4
          FALSE
        """);
  }

  private void chargeMeterTranslations() {
    this.add(Translations.ChargeMeter.START, "Recording data over %s seconds...");
    this.add(Translations.ChargeMeter.CHARGE, "Charge:");
    this.add(Translations.ChargeMeter.DRAW, "Draw:");
    this.add(Translations.ChargeMeter.LOSS, "Loss:");
    this.add(Translations.ChargeMeter.NETWORK, "Network:");
    this.add(Translations.ChargeMeter.SIZE, "Size:");
    this.add(Translations.ChargeMeter.MAX_DRAW, "MaxDraw:");
    this.add(Translations.ChargeMeter.EFFICIENCY, "Efficiency:");
    this.add(Translations.ChargeMeter.NODE, "Node:");
    this.add(Translations.ChargeMeter.PRODUCER, "Producer:");
  }

  private void keyBindingTranslations() {
    this.add(Translations.KeyBinding.CATEGORY, RailcraftConstants.NAME);
    this.add(Translations.KeyBinding.CHANGE_AURA, "Change Aura");
  }

  private void addFluidType(Supplier<? extends FluidType> key, String name) {
    this.add(key.get(), name);
  }

  private void add(FluidType key, String name) {
    this.add(key.getDescriptionId(), name);
  }

  private void addBlockColorVariants(
      VariantSet<DyeColor, Block, ? extends Block> blocks, String name) {
    this.addBlockVariants(blocks, name, RailcraftLanguageProvider::getColorName);
  }

  private <K extends Enum<K> & StringRepresentable> void addBlockVariants(
      VariantSet<K, Block, ? extends Block> blocks, String name, Function<K, String> nameGetter) {
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
