package mods.railcraft.tags;

import mods.railcraft.Railcraft;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.Tags.IOptionalNamedTag;

public class RailcraftTags {

  public static class Items {

    public static final Tags.IOptionalNamedTag<Item> CROWBAR = tag("crowbar");

    public static final Tags.IOptionalNamedTag<Item> METAL = tag("metal");

    // ======================================================
    // Metals
    // ======================================================
    // [forge:ingots/[INGOT NAME]]
    public static final Tags.IOptionalNamedTag<Item> STEEL_INGOT = forgeTag("ingots/steel");
    public static final Tags.IOptionalNamedTag<Item> COPPER_INGOT = forgeTag("ingots/copper");
    public static final Tags.IOptionalNamedTag<Item> TIN_INGOT = forgeTag("ingots/tin");
    public static final Tags.IOptionalNamedTag<Item> LEAD_INGOT = forgeTag("ingots/lead");
    public static final Tags.IOptionalNamedTag<Item> SILVER_INGOT = forgeTag("ingots/silver");
    public static final Tags.IOptionalNamedTag<Item> BRONZE_INGOT = forgeTag("ingots/bronze");
    public static final Tags.IOptionalNamedTag<Item> NICKEL_INGOT = forgeTag("ingots/nickel");
    public static final Tags.IOptionalNamedTag<Item> INVAR_INGOT = forgeTag("ingots/invar");
    public static final Tags.IOptionalNamedTag<Item> ZINC_INGOT = forgeTag("ingots/zinc");
    public static final Tags.IOptionalNamedTag<Item> BRASS_INGOT = forgeTag("ingots/brass");

    // nuggets
    public static final Tags.IOptionalNamedTag<Item> STEEL_NUGGET = forgeTag("nuggets/steel");
    public static final Tags.IOptionalNamedTag<Item> COPPER_NUGGET = forgeTag("nuggets/copper");
    public static final Tags.IOptionalNamedTag<Item> TIN_NUGGET = forgeTag("nuggets/tin");
    public static final Tags.IOptionalNamedTag<Item> LEAD_NUGGET = forgeTag("nuggets/lead");
    public static final Tags.IOptionalNamedTag<Item> SILVER_NUGGET = forgeTag("nuggets/silver");
    public static final Tags.IOptionalNamedTag<Item> BRONZE_NUGGET = forgeTag("nuggets/bronze");
    public static final Tags.IOptionalNamedTag<Item> NICKEL_NUGGET = forgeTag("nuggets/nickel");
    public static final Tags.IOptionalNamedTag<Item> INVAR_NUGGET = forgeTag("nuggets/invar");
    public static final Tags.IOptionalNamedTag<Item> ZINC_NUGGET = forgeTag("nuggets/zinc");
    public static final Tags.IOptionalNamedTag<Item> BRASS_NUGGET = forgeTag("nuggets/brass");

    // blocks
    public static final Tags.IOptionalNamedTag<Item> STEEL_BLOCK = forgeTag("blocks/steel");
    public static final Tags.IOptionalNamedTag<Item> COPPER_BLOCK = forgeTag("blocks/copper");
    public static final Tags.IOptionalNamedTag<Item> TIN_BLOCK = forgeTag("blocks/tin");
    public static final Tags.IOptionalNamedTag<Item> LEAD_BLOCK = forgeTag("blocks/lead");
    public static final Tags.IOptionalNamedTag<Item> SILVER_BLOCK = forgeTag("blocks/silver");
    public static final Tags.IOptionalNamedTag<Item> BRONZE_BLOCK = forgeTag("blocks/bronze");
    public static final Tags.IOptionalNamedTag<Item> NICKEL_BLOCK = forgeTag("blocks/nickel");
    public static final Tags.IOptionalNamedTag<Item> INVAR_BLOCK = forgeTag("blocks/invar");
    public static final Tags.IOptionalNamedTag<Item> ZINC_BLOCK = forgeTag("blocks/zinc");
    public static final Tags.IOptionalNamedTag<Item> BRASS_BLOCK = forgeTag("blocks/brass");

    private static IOptionalNamedTag<Item> tag(String name) {
      return ItemTags.createOptional(new ResourceLocation(Railcraft.ID, name));
    }

    private static IOptionalNamedTag<Item> forgeTag(String name) {
      return ItemTags.createOptional(new ResourceLocation("forge", name));
    }
  }

  public static class Blocks {

    public static final Tags.IOptionalNamedTag<Block> MAGIC_ORE = tag("magic_ore");
    public static final Tags.IOptionalNamedTag<Block> SWITCH_TRACK_ACTUATOR =
        tag("switch_track_actuator");
    public static final Tags.IOptionalNamedTag<Block> SIGNAL_INTERLOCK_BOX =
        tag("signal_interlock_box");
    public static final Tags.IOptionalNamedTag<Block> ASPECT_RECEIVER =
        tag("aspect_receiver");
    public static final Tags.IOptionalNamedTag<Block> ASPECT_EMITTER =
        tag("aspect_emitter");
    public static final Tags.IOptionalNamedTag<Block> POST = tag("post");
    public static final Tags.IOptionalNamedTag<Block> SIGNAL = tag("signal");

    private static IOptionalNamedTag<Block> tag(String name) {
      return BlockTags.createOptional(new ResourceLocation(Railcraft.ID, name));
    }
  }
}
