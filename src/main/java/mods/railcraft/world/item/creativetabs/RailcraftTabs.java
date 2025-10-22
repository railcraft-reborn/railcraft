package mods.railcraft.world.item.creativetabs;

import mods.railcraft.Translations;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

public class RailcraftTabs {
  public static final CreativeModeTab MAIN = new CreativeModeTab(Translations.Tab.RAILCRAFT) {
    @Override
    public ItemStack makeIcon() {
      return new ItemStack(RailcraftItems.IRON_CROWBAR.get());
    }
  };
  public static final CreativeModeTab OUTFITTED_TRACKS = new CreativeModeTab(Translations.Tab.RAILCRAFT_OUTFITTED_TRACKS) {
    @Override
    public ItemStack makeIcon() {
      return new ItemStack(RailcraftItems.IRON_DETECTOR_TRACK.get());
    }
  };
  public static final CreativeModeTab DECORATIVE = new CreativeModeTab(Translations.Tab.RAILCRAFT_DECORATIVE_BLOCKS) {
    @Override
    public ItemStack makeIcon() {
      return new ItemStack(RailcraftItems.STRENGTHENED_GLASS.variantFor(DyeColor.BLACK).get());
    }
  };
}
