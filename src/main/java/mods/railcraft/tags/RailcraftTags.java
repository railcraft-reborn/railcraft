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

    public static final Tags.IOptionalNamedTag<Item> STEEL = tag("steel");
    public static final Tags.IOptionalNamedTag<Item> COPPER = tag("copper");
    public static final Tags.IOptionalNamedTag<Item> TIN = tag("tin");
    public static final Tags.IOptionalNamedTag<Item> LEAD = tag("lead");
    public static final Tags.IOptionalNamedTag<Item> SILVER = tag("silver");
    public static final Tags.IOptionalNamedTag<Item> BRONZE = tag("bronze");
    public static final Tags.IOptionalNamedTag<Item> NICKEL = tag("nickel");
    public static final Tags.IOptionalNamedTag<Item> INVAR = tag("invar");
    public static final Tags.IOptionalNamedTag<Item> ZINC = tag("zinc");
    public static final Tags.IOptionalNamedTag<Item> BRASS = tag("brass");

    private static IOptionalNamedTag<Item> tag(String name) {
      return ItemTags.createOptional(new ResourceLocation(Railcraft.ID, name));
    }
  }

  public static class Blocks {

    public static final Tags.IOptionalNamedTag<Block> MAGIC_ORE = tag("magic_ore");

    private static IOptionalNamedTag<Block> tag(String name) {
      return BlockTags.createOptional(new ResourceLocation(Railcraft.ID, name));
    }
  }
}
