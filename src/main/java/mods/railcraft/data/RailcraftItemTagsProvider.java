package mods.railcraft.data;

import mods.railcraft.Railcraft;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class RailcraftItemTagsProvider extends ItemTagsProvider {

  public RailcraftItemTagsProvider(DataGenerator generator, BlockTagsProvider blockTagsProvider,
      ExistingFileHelper fileHelper) {
    super(generator, blockTagsProvider, Railcraft.ID, fileHelper);
  }

  @Override
  protected void addTags() {
    this.tag(RailcraftTags.Items.STEEL_INGOT)
      .add(RailcraftItems.STEEL_INGOT.get());
    this.tag(RailcraftTags.Items.TIN_INGOT)
      .add(RailcraftItems.TIN_INGOT.get());
    this.tag(RailcraftTags.Items.ZINC_INGOT)
      .add(RailcraftItems.ZINC_INGOT.get());
    this.tag(RailcraftTags.Items.BRASS_INGOT)
      .add(RailcraftItems.BRASS_INGOT.get());
    this.tag(RailcraftTags.Items.BRONZE_INGOT)
      .add(RailcraftItems.BRONZE_INGOT.get());

    this.tag(RailcraftTags.Items.STEEL_NUGGET)
      .add(RailcraftItems.STEEL_NUGGET.get());
    this.tag(RailcraftTags.Items.TIN_NUGGET)
      .add(RailcraftItems.TIN_NUGGET.get());
    this.tag(RailcraftTags.Items.ZINC_NUGGET)
      .add(RailcraftItems.ZINC_NUGGET.get());
    this.tag(RailcraftTags.Items.BRASS_NUGGET)
      .add(RailcraftItems.BRASS_NUGGET.get());
    this.tag(RailcraftTags.Items.BRONZE_NUGGET)
      .add(RailcraftItems.BRONZE_NUGGET.get());

    this.tag(RailcraftTags.Items.STEEL_BLOCK)
      .add(RailcraftItems.STEEL_BLOCK.get());
  }

  @Override
  public String getName() {
    return "Railcraft Item Tags";
  }
}
