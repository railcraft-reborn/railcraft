package mods.railcraft.tags;

import mods.railcraft.api.core.RailcraftConstants;
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

    public static final TagKey<Item> STEEL_INGOT = ingotsTag("steel");
    public static final TagKey<Item> TIN_INGOT = ingotsTag("tin");
    public static final TagKey<Item> LEAD_INGOT = ingotsTag("lead");
    public static final TagKey<Item> SILVER_INGOT = ingotsTag("silver");
    public static final TagKey<Item> BRONZE_INGOT = ingotsTag("bronze");
    public static final TagKey<Item> NICKEL_INGOT = ingotsTag("nickel");
    public static final TagKey<Item> INVAR_INGOT = ingotsTag("invar");
    public static final TagKey<Item> ZINC_INGOT = ingotsTag("zinc");
    public static final TagKey<Item> BRASS_INGOT = ingotsTag("brass");

    public static final TagKey<Item> STEEL_NUGGET = nuggetsTag("steel");
    public static final TagKey<Item> TIN_NUGGET = nuggetsTag("tin");
    public static final TagKey<Item> LEAD_NUGGET = nuggetsTag("lead");
    public static final TagKey<Item> SILVER_NUGGET = nuggetsTag("silver");
    public static final TagKey<Item> BRONZE_NUGGET = nuggetsTag("bronze");
    public static final TagKey<Item> NICKEL_NUGGET = nuggetsTag("nickel");
    public static final TagKey<Item> INVAR_NUGGET = nuggetsTag("invar");
    public static final TagKey<Item> ZINC_NUGGET = nuggetsTag("zinc");
    public static final TagKey<Item> BRASS_NUGGET = nuggetsTag("brass");

    public static final TagKey<Item> STEEL_BLOCK = storageBlocksTag("steel");
    public static final TagKey<Item> TIN_BLOCK = storageBlocksTag("tin");
    public static final TagKey<Item> LEAD_BLOCK = storageBlocksTag("lead");
    public static final TagKey<Item> RAW_LEAD_BLOCK = storageBlocksTag("raw_lead");
    public static final TagKey<Item> SILVER_BLOCK = storageBlocksTag("silver");
    public static final TagKey<Item> RAW_SILVER_BLOCK = storageBlocksTag("raw_silver");
    public static final TagKey<Item> BRONZE_BLOCK = storageBlocksTag("bronze");
    public static final TagKey<Item> NICKEL_BLOCK = storageBlocksTag("nickel");
    public static final TagKey<Item> RAW_NICKEL_BLOCK = storageBlocksTag("raw_nickel");
    public static final TagKey<Item> INVAR_BLOCK = storageBlocksTag("invar");
    public static final TagKey<Item> ZINC_BLOCK = storageBlocksTag("zinc");
    public static final TagKey<Item> BRASS_BLOCK = storageBlocksTag("brass");

    public static final TagKey<Item> STEEL_PLATE = platesTag("steel");
    public static final TagKey<Item> IRON_PLATE = platesTag("iron");
    public static final TagKey<Item> TIN_PLATE = platesTag("tin");
    public static final TagKey<Item> GOLD_PLATE = platesTag("gold");
    public static final TagKey<Item> LEAD_PLATE = platesTag("lead");
    public static final TagKey<Item> ZINC_PLATE = platesTag("zinc");
    public static final TagKey<Item> BRASS_PLATE = platesTag("brass");
    public static final TagKey<Item> INVAR_PLATE = platesTag("invar");
    public static final TagKey<Item> BRONZE_PLATE = platesTag("bronze");
    public static final TagKey<Item> COPPER_PLATE = platesTag("copper");
    public static final TagKey<Item> NICKEL_PLATE = platesTag("nickel");
    public static final TagKey<Item> SILVER_PLATE = platesTag("silver");

    public static final TagKey<Item> STEEL_GEAR = gearsTag("steel");
    public static final TagKey<Item> IRON_GEAR = gearsTag("iron");
    public static final TagKey<Item> TIN_GEAR = gearsTag("tin");
    public static final TagKey<Item> GOLD_GEAR = gearsTag("gold");
    public static final TagKey<Item> LEAD_GEAR = gearsTag("lead");
    public static final TagKey<Item> ZINC_GEAR = gearsTag("zinc");
    public static final TagKey<Item> BRASS_GEAR = gearsTag("brass");
    public static final TagKey<Item> INVAR_GEAR = gearsTag("invar");
    public static final TagKey<Item> BRONZE_GEAR = gearsTag("bronze");
    public static final TagKey<Item> COPPER_GEAR = gearsTag("copper");
    public static final TagKey<Item> NICKEL_GEAR = gearsTag("nickel");
    public static final TagKey<Item> SILVER_GEAR = gearsTag("silver");


    public static final TagKey<Item> SLAG = forgeTag("slag");
    public static final TagKey<Item> COAL_COKE = forgeTag("coal_coke");
    public static final TagKey<Item> BRONZE_DUST = dustsTag("bronze");
    public static final TagKey<Item> LAPIS_DUST = dustsTag("lapis");
    public static final TagKey<Item> EMERALD_DUST = dustsTag("emerald");
    public static final TagKey<Item> GOLD_DUST = dustsTag("gold");
    public static final TagKey<Item> DIAMOND_DUST = dustsTag("diamond");
    public static final TagKey<Item> NETHERITE_DUST = dustsTag("netherite");
    public static final TagKey<Item> IRON_DUST = dustsTag("iron");
    public static final TagKey<Item> COPPER_DUST = dustsTag("copper");
    public static final TagKey<Item> STEEL_DUST = dustsTag("steel");
    public static final TagKey<Item> QUARTZ_DUST = dustsTag("quartz");
    public static final TagKey<Item> NICKEL_DUST = dustsTag("nickel");
    public static final TagKey<Item> LEAD_DUST = dustsTag("lead");
    public static final TagKey<Item> SILVER_DUST = dustsTag("silver");
    public static final TagKey<Item> TIN_DUST = dustsTag("tin");
    public static final TagKey<Item> SALTPETER_DUST = dustsTag("salt");
    public static final TagKey<Item> COAL_DUST = dustsTag("coal");
    public static final TagKey<Item> COAL_COKE_DUST = dustsTag("coal_coke");
    public static final TagKey<Item> CHARCOAL_DUST = dustsTag("charcoal");
    public static final TagKey<Item> SULFUR_DUST = dustsTag("sulfur");
    public static final TagKey<Item> OBSIDIAN_DUST = dustsTag("obsidian");

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


    public static final TagKey<Item> LEAD_ORE = oresTag("lead");
    public static final TagKey<Item> NICKEL_ORE = oresTag("nickel");
    public static final TagKey<Item> SILVER_ORE = oresTag("silver");
    public static final TagKey<Item> SULFUR_ORE = oresTag("sulfur");
    public static final TagKey<Item> TIN_ORE = oresTag("tin");
    public static final TagKey<Item> ZINC_ORE = oresTag("zinc");
    public static final TagKey<Item> SALTPETER_ORE = oresTag("salt");

    public static final TagKey<Item> TIN_RAW = rawMaterialsTag("tin");
    public static final TagKey<Item> ZINC_RAW = rawMaterialsTag("zinc");
    public static final TagKey<Item> NICKEL_RAW = rawMaterialsTag("nickel");
    public static final TagKey<Item> SILVER_RAW = rawMaterialsTag("silver");
    public static final TagKey<Item> LEAD_RAW = rawMaterialsTag("lead");

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
      return ItemTags.create(RailcraftConstants.rl(name));
    }

    public static TagKey<Item> forgeTag(String name) {
      return ItemTags.create(new ResourceLocation("forge", name));
    }

    public static TagKey<Item> oresTag(String name) {
      return forgeTag("ores/" + name);
    }

    public static TagKey<Item> rawMaterialsTag(String name) {
      return forgeTag("raw_materials/" + name);
    }

    public static TagKey<Item> ingotsTag(String name) {
      return forgeTag("ingots/" + name);
    }

    public static TagKey<Item> nuggetsTag(String name) {
      return forgeTag("nuggets/" + name);
    }

    public static TagKey<Item> platesTag(String name) {
      return forgeTag("plates/" + name);
    }

    public static TagKey<Item> gearsTag(String name) {
      return forgeTag("gears/" + name);
    }

    public static TagKey<Item> dustsTag(String name) {
      return forgeTag("dusts/" + name);
    }

    public static TagKey<Item> storageBlocksTag(String name) {
      return forgeTag("storage_blocks/" + name);
    }
  }

  public static class Blocks {

    public static final TagKey<Block> BALLAST = tag("ballast");
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
    public static final TagKey<Block> TUNNEL_BORE_MINEABLE_BLOCKS =
        tag("tunnel_bore_mineable_blocks");
    public static final TagKey<Block> TUNNEL_BORE_REPLACEABLE_BLOCKS =
        tag("tunnel_bore_replaceable_blocks");

    public static final TagKey<Block> LEAD_ORE = forgeTag("ores/lead");
    public static final TagKey<Block> NICKEL_ORE = forgeTag("ores/nickel");
    public static final TagKey<Block> SILVER_ORE = forgeTag("ores/silver");
    public static final TagKey<Block> SULFUR_ORE = forgeTag("ores/sulfur");
    public static final TagKey<Block> TIN_ORE = forgeTag("ores/tin");
    public static final TagKey<Block> ZINC_ORE = forgeTag("ores/zinc");
    public static final TagKey<Block> SALTPETER_ORE = forgeTag("ores/salt");

    private static TagKey<Block> tag(String name) {
      return BlockTags.create(RailcraftConstants.rl(name));
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
