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

    // nuggets
    public static final TagKey<Item> STEEL_NUGGET = forgeTag("nuggets/steel");
    public static final TagKey<Item> TIN_NUGGET = forgeTag("nuggets/tin");
    public static final TagKey<Item> LEAD_NUGGET = forgeTag("nuggets/lead");
    public static final TagKey<Item> SILVER_NUGGET = forgeTag("nuggets/silver");
    public static final TagKey<Item> BRONZE_NUGGET = forgeTag("nuggets/bronze");
    public static final TagKey<Item> NICKEL_NUGGET = forgeTag("nuggets/nickel");
    public static final TagKey<Item> INVAR_NUGGET = forgeTag("nuggets/invar");
    public static final TagKey<Item> ZINC_NUGGET = forgeTag("nuggets/zinc");
    public static final TagKey<Item> BRASS_NUGGET = forgeTag("nuggets/brass");

    // blocks
    public static final TagKey<Item> STEEL_BLOCK = forgeTag("blocks/steel");
    public static final TagKey<Item> TIN_BLOCK = forgeTag("blocks/tin");
    public static final TagKey<Item> LEAD_BLOCK = forgeTag("blocks/lead");
    public static final TagKey<Item> SILVER_BLOCK = forgeTag("blocks/silver");
    public static final TagKey<Item> BRONZE_BLOCK = forgeTag("blocks/bronze");
    public static final TagKey<Item> NICKEL_BLOCK = forgeTag("blocks/nickel");
    public static final TagKey<Item> INVAR_BLOCK = forgeTag("blocks/invar");
    public static final TagKey<Item> ZINC_BLOCK = forgeTag("blocks/zinc");
    public static final TagKey<Item> BRASS_BLOCK = forgeTag("blocks/brass");

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
    public static final TagKey<Item> SALTPETER_DUST = forgeTag("dusts/salt");

    public static final TagKey<Item> IRON_TANK_GAUGE = tag("iron_tank_gauge");
    public static final TagKey<Item> IRON_TANK_VALVE = tag("iron_tank_valve");
    public static final TagKey<Item> IRON_TANK_WALL = tag("iron_tank_wall");

    public static final TagKey<Item> STEEL_TANK_GAUGE = tag("steel_tank_gauge");
    public static final TagKey<Item> STEEL_TANK_VALVE = tag("steel_tank_valve");
    public static final TagKey<Item> STEEL_TANK_WALL = tag("steel_tank_wall");

    public static final TagKey<Item> POST = tag("post");

    public static final TagKey<Item> STRENGTHENED_GLASS = tag("strengthened_glass");

    private static TagKey<Item> tag(String name) {
      return ItemTags.create(new ResourceLocation(Railcraft.ID, name));
    }

    private static TagKey<Item> forgeTag(String name) {
      return ItemTags.create(new ResourceLocation("forge", name));
    }
  }

  public static class Blocks {

    public static final TagKey<Block> BALLAST = tag("ballast");
    public static final TagKey<Block> MAGIC_ORE = tag("magic_ore");
    public static final TagKey<Block> SWITCH_TRACK_ACTUATOR =
        tag("switch_track_actuator");
    public static final TagKey<Block> ASPECT_RECEIVER =
        tag("aspect_receiver");
    public static final TagKey<Block> ASPECT_EMITTER =
        tag("aspect_emitter");
    public static final TagKey<Block> POST = tag("post");
    public static final TagKey<Block> SIGNAL = tag("signal");

    public static final TagKey<Block> IRON_TANK_GAUGE = tag("iron_tank_gauge");
    public static final TagKey<Block> IRON_TANK_VALVE = tag("iron_tank_valve");
    public static final TagKey<Block> IRON_TANK_WALL = tag("iron_tank_wall");

    public static final TagKey<Block> STEEL_TANK_GAUGE = tag("steel_tank_gauge");
    public static final TagKey<Block> STEEL_TANK_VALVE = tag("steel_tank_valve");
    public static final TagKey<Block> STEEL_TANK_WALL = tag("steel_tank_wall");

    public static final TagKey<Block> MINEABLE_WITH_CROWBAR =
        tag("mineable/crowbar");

    private static TagKey<Block> tag(String name) {
      return BlockTags.create(new ResourceLocation(Railcraft.ID, name));
    }
  }

  public static class Fluids {

    public static final TagKey<Fluid> STEAM = forgeTag("steam");
    public static final TagKey<Fluid> GASEOUS = forgeTag("gaseous");

    private static TagKey<Fluid> forgeTag(String name) {
      return FluidTags.create(new ResourceLocation("forge", name));
    }
  }
}
