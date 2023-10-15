package mods.railcraft.tags;

import mods.railcraft.Railcraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class RailcraftTags {

  public static class Items {

    public static final TagKey<Item> CROWBAR = tag("crowbar");

    public static final TagKey<Item> METAL = tag("metal");

    // ======================================================
    // Metals
    // ======================================================
    // [forge:ingots/[INGOT NAME]]
    public static final TagKey<Item> STEEL_INGOT = forgeTag("ingots/steel");
    public static final TagKey<Item> TIN_INGOT = forgeTag("ingots/tin");
    public static final TagKey<Item> LEAD_INGOT = forgeTag("ingots/lead");
    public static final TagKey<Item> SILVER_INGOT = forgeTag("ingots/silver");
    public static final TagKey<Item> BRONZE_INGOT = forgeTag("ingots/bronze");
    public static final TagKey<Item> NICKEL_INGOT = forgeTag("ingots/nickel");
    public static final TagKey<Item> INVAR_INGOT = forgeTag("ingots/invar");
    public static final TagKey<Item> ZINC_INGOT = forgeTag("ingots/zinc");
    public static final TagKey<Item> BRASS_INGOT = forgeTag("ingots/brass");


    public static final TagKey<Item> STEEL_NUGGET = forgeTag("nuggets/steel");
    public static final TagKey<Item> TIN_NUGGET = forgeTag("nuggets/tin");
    public static final TagKey<Item> LEAD_NUGGET = forgeTag("nuggets/lead");
    public static final TagKey<Item> SILVER_NUGGET = forgeTag("nuggets/silver");
    public static final TagKey<Item> BRONZE_NUGGET = forgeTag("nuggets/bronze");
    public static final TagKey<Item> NICKEL_NUGGET = forgeTag("nuggets/nickel");
    public static final TagKey<Item> INVAR_NUGGET = forgeTag("nuggets/invar");
    public static final TagKey<Item> ZINC_NUGGET = forgeTag("nuggets/zinc");
    public static final TagKey<Item> BRASS_NUGGET = forgeTag("nuggets/brass");


    public static final TagKey<Item> STEEL_BLOCK = forgeTag("storage_blocks/steel");
    public static final TagKey<Item> TIN_BLOCK = forgeTag("storage_blocks/tin");
    public static final TagKey<Item> LEAD_BLOCK = forgeTag("storage_blocks/lead");
    public static final TagKey<Item> SILVER_BLOCK = forgeTag("storage_blocks/silver");
    public static final TagKey<Item> BRONZE_BLOCK = forgeTag("storage_blocks/bronze");
    public static final TagKey<Item> NICKEL_BLOCK = forgeTag("storage_blocks/nickel");
    public static final TagKey<Item> INVAR_BLOCK = forgeTag("storage_blocks/invar");
    public static final TagKey<Item> ZINC_BLOCK = forgeTag("storage_blocks/zinc");
    public static final TagKey<Item> BRASS_BLOCK = forgeTag("storage_blocks/brass");

    public static final TagKey<Item> STEEL_PLATE = forgeTag("plates/steel");
    public static final TagKey<Item> IRON_PLATE = forgeTag("plates/iron");
    public static final TagKey<Item> TIN_PLATE = forgeTag("plates/tin");
    public static final TagKey<Item> GOLD_PLATE = forgeTag("plates/gold");
    public static final TagKey<Item> LEAD_PLATE = forgeTag("plates/lead");
    public static final TagKey<Item> ZINC_PLATE = forgeTag("plates/zinc");
    public static final TagKey<Item> BRASS_PLATE = forgeTag("plates/brass");
    public static final TagKey<Item> INVAR_PLATE = forgeTag("plates/invar");
    public static final TagKey<Item> BRONZE_PLATE = forgeTag("plates/bronze");
    public static final TagKey<Item> COPPER_PLATE = forgeTag("plates/copper");
    public static final TagKey<Item> NICKEL_PLATE = forgeTag("plates/nickel");
    public static final TagKey<Item> SILVER_PLATE = forgeTag("plates/silver");

    public static final TagKey<Item> STEEL_GEAR = forgeTag("gears/steel");
    public static final TagKey<Item> IRON_GEAR = forgeTag("gears/iron");
    public static final TagKey<Item> TIN_GEAR = forgeTag("gears/tin");
    public static final TagKey<Item> GOLD_GEAR = forgeTag("gears/gold");
    public static final TagKey<Item> LEAD_GEAR = forgeTag("gears/lead");
    public static final TagKey<Item> ZINC_GEAR = forgeTag("gears/zinc");
    public static final TagKey<Item> BRASS_GEAR = forgeTag("gears/brass");
    public static final TagKey<Item> INVAR_GEAR = forgeTag("gears/invar");
    public static final TagKey<Item> BRONZE_GEAR = forgeTag("gears/bronze");
    public static final TagKey<Item> COPPER_GEAR = forgeTag("gears/copper");
    public static final TagKey<Item> NICKEL_GEAR = forgeTag("gears/nickel");
    public static final TagKey<Item> SILVER_GEAR = forgeTag("gears/silver");


    public static final TagKey<Item> SLAG = forgeTag("slag");
    public static final TagKey<Item> COAL_COKE = forgeTag("coal_coke");
    public static final TagKey<Item> SALTPETER_DUST = forgeTag("dusts/salt");
    public static final TagKey<Item> COAL_DUST = forgeTag("dusts/coal");
    public static final TagKey<Item> CHARCOAL_DUST = forgeTag("dusts/charcoal");
    public static final TagKey<Item> SULFUR_DUST = forgeTag("dusts/sulfur");
    public static final TagKey<Item> OBSIDIAN_DUST = forgeTag("dusts/obsidian");

    public static final TagKey<Item> IRON_TANK_GAUGE = tag("iron_tank_gauge");
    public static final TagKey<Item> IRON_TANK_VALVE = tag("iron_tank_valve");
    public static final TagKey<Item> IRON_TANK_WALL = tag("iron_tank_wall");

    public static final TagKey<Item> STEEL_TANK_GAUGE = tag("steel_tank_gauge");
    public static final TagKey<Item> STEEL_TANK_VALVE = tag("steel_tank_valve");
    public static final TagKey<Item> STEEL_TANK_WALL = tag("steel_tank_wall");

    public static final TagKey<Item> PLATE_CHEST_LOOT = tag("plate_chest_loot");
    public static final TagKey<Item> GEAR_CHEST_LOOT = tag("gear_chest_loot");
    public static final TagKey<Item> INGOT_CHEST_LOOT = tag("ingot_chest_loot");

    public static final TagKey<Item> QUARRIED = tag("quarried");
    public static final TagKey<Item> ABYSSAL = tag("abyssal");
    public static final TagKey<Item> DETECTOR = tag("detector");

    public static final TagKey<Item> POST = tag("post");
    public static final TagKey<Item> STRENGTHENED_GLASS = tag("strengthened_glass");
    public static final TagKey<Item> ABANDONED_TRACK = tag("abandoned_track");
    public static final TagKey<Item> ELECTRIC_TRACK = tag("electric_track");
    public static final TagKey<Item> HIGH_SPEED_TRACK = tag("high_speed_track");
    public static final TagKey<Item> HIGH_SPEED_ELECTRIC_TRACK = tag("high_speed_electric_track");
    public static final TagKey<Item> IRON_TRACK = tag("iron_track");
    public static final TagKey<Item> REINFORCED_TRACK = tag("reinforced_track");
    public static final TagKey<Item> STRAP_IRON_TRACK = tag("strap_iron_track");

    public static final TagKey<Item> TRACK_KIT = tag("track_kit");


    public static final TagKey<Item> LEAD_ORE = forgeTag("ores/lead");
    public static final TagKey<Item> NICKEL_ORE = forgeTag("ores/nickel");
    public static final TagKey<Item> SILVER_ORE = forgeTag("ores/silver");
    public static final TagKey<Item> SULFUR_ORE = forgeTag("ores/sulfur");
    public static final TagKey<Item> TIN_ORE = forgeTag("ores/tin");
    public static final TagKey<Item> ZINC_ORE = forgeTag("ores/zinc");
    public static final TagKey<Item> SALTPETER_ORE = forgeTag("ores/salt");

    public static final TagKey<Item> TIN_RAW = forgeTag("raw_materials/tin");
    public static final TagKey<Item> ZINC_RAW = forgeTag("raw_materials/zinc");
    public static final TagKey<Item> NICKEL_RAW = forgeTag("raw_materials/nickel");
    public static final TagKey<Item> SILVER_RAW = forgeTag("raw_materials/silver");
    public static final TagKey<Item> LEAD_RAW = forgeTag("raw_materials/lead");

    public static final TagKey<Item> TOOLS_SWORDS_STEEL = forgeTag("tools/swords/steel");
    public static final TagKey<Item> TOOLS_AXES_STEEL = forgeTag("tools/axes/steel");
    public static final TagKey<Item> TOOLS_PICKAXES_STEEL = forgeTag("tools/pickaxes/steel");
    public static final TagKey<Item> TOOLS_SHOVELS_STEEL = forgeTag("tools/shovels/steel");
    public static final TagKey<Item> TOOLS_HOES_STEEL = forgeTag("tools/hoes/steel");

    public static final TagKey<Item> ARMORS_HELMETS_STEEL = forgeTag("armors/helmets/steel");
    public static final TagKey<Item> ARMORS_CHESTPLATES_STEEL = forgeTag("armors/chestplates/steel");
    public static final TagKey<Item> ARMORS_LEGGINGS_STEEL = forgeTag("armors/leggings/steel");
    public static final TagKey<Item> ARMORS_BOOTS_STEEL = forgeTag("armors/boots/steel");


    private static TagKey<Item> tag(String name) {
      return ItemTags.create(Railcraft.rl(name));
    }

    private static TagKey<Item> forgeTag(String name) {
      return ItemTags.create(new ResourceLocation("forge", name));
    }
  }

  public static class Blocks {

    public static final TagKey<Block> BALLAST = tag("ballast");
    public static final TagKey<Block> MAGIC_ORE = tag("magic_ore");
    public static final TagKey<Block> SWITCH_TRACK_ACTUATOR = tag("switch_track_actuator");
    public static final TagKey<Block> ASPECT_RECEIVER = tag("aspect_receiver");
    public static final TagKey<Block> ASPECT_EMITTER = tag("aspect_emitter");
    public static final TagKey<Block> POST = tag("post");
    public static final TagKey<Block> STRENGTHENED_GLASS = tag("strengthened_glass");
    public static final TagKey<Block> SIGNAL = tag("signal");
    public static final TagKey<Block> ABANDONED_TRACK = tag("abandoned_track");
    public static final TagKey<Block> ELECTRIC_TRACK = tag("electric_track");
    public static final TagKey<Block> HIGH_SPEED_TRACK = tag("high_speed_track");
    public static final TagKey<Block> HIGH_SPEED_ELECTRIC_TRACK = tag("high_speed_electric_track");
    public static final TagKey<Block> IRON_TRACK = tag("iron_track");
    public static final TagKey<Block> REINFORCED_TRACK = tag("reinforced_track");
    public static final TagKey<Block> STRAP_IRON_TRACK = tag("strap_iron_track");

    public static final TagKey<Block> IRON_TANK_GAUGE = tag("iron_tank_gauge");
    public static final TagKey<Block> IRON_TANK_VALVE = tag("iron_tank_valve");
    public static final TagKey<Block> IRON_TANK_WALL = tag("iron_tank_wall");

    public static final TagKey<Block> STEEL_TANK_GAUGE = tag("steel_tank_gauge");
    public static final TagKey<Block> STEEL_TANK_VALVE = tag("steel_tank_valve");
    public static final TagKey<Block> STEEL_TANK_WALL = tag("steel_tank_wall");

    public static final TagKey<Block> QUARRIED = tag("quarried");
    public static final TagKey<Block> ABYSSAL = tag("abyssal");
    public static final TagKey<Block> DETECTOR = tag("detector");

    public static final TagKey<Block> MINEABLE_WITH_CROWBAR = tag("mineable/crowbar");

    public static final TagKey<Block> LEAD_ORE = forgeTag("ores/lead");
    public static final TagKey<Block> NICKEL_ORE = forgeTag("ores/nickel");
    public static final TagKey<Block> SILVER_ORE = forgeTag("ores/silver");
    public static final TagKey<Block> SULFUR_ORE = forgeTag("ores/sulfur");
    public static final TagKey<Block> TIN_ORE = forgeTag("ores/tin");
    public static final TagKey<Block> ZINC_ORE = forgeTag("ores/zinc");
    public static final TagKey<Block> SALTPETER_ORE = forgeTag("ores/salt");

    private static TagKey<Block> tag(String name) {
      return BlockTags.create(Railcraft.rl(name));
    }

    private static TagKey<Block> forgeTag(String name) {
      return BlockTags.create(new ResourceLocation("forge", name));
    }
  }

  public static class Fluids {

    public static final TagKey<Fluid> STEAM = forgeTag("steam");
    public static final TagKey<Fluid> CREOSOTE = forgeTag("creosote");

    private static TagKey<Fluid> forgeTag(String name) {
      return FluidTags.create(new ResourceLocation("forge", name));
    }
  }
}
