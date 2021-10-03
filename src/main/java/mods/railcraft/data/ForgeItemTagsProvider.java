package mods.railcraft.data;

import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ForgeItemTagsProvider extends ItemTagsProvider {

  public ForgeItemTagsProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagsProvider,
      ExistingFileHelper existingFileHelper) {
    super(dataGenerator, blockTagsProvider, "forge", existingFileHelper);
  }

  @Override
  protected void addTags() {
    this.tag(RailcraftTags.Items.STEEL_INGOT)
      .add(RailcraftItems.STEEL_INGOT.get());
    this.tag(RailcraftTags.Items.COPPER_INGOT)
      .add(RailcraftItems.COPPER_INGOT.get());
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
    this.tag(RailcraftTags.Items.COPPER_NUGGET)
      .add(RailcraftItems.COPPER_NUGGET.get());
    this.tag(RailcraftTags.Items.TIN_NUGGET)
      .add(RailcraftItems.TIN_NUGGET.get());
    this.tag(RailcraftTags.Items.ZINC_NUGGET)
      .add(RailcraftItems.ZINC_NUGGET.get());
    this.tag(RailcraftTags.Items.BRASS_NUGGET)
      .add(RailcraftItems.BRASS_NUGGET.get());
    this.tag(RailcraftTags.Items.BRONZE_NUGGET)
      .add(RailcraftItems.BRONZE_NUGGET.get());
  }

  @Override
  public String getName() {
    return "Forge Item Tags";
  }
}
