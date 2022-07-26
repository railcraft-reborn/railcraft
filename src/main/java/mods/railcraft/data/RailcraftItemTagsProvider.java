package mods.railcraft.data;

import mods.railcraft.Railcraft;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
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

    this.tag(RailcraftTags.Items.STEEL_PLATE)
      .add(RailcraftItems.STEEL_PLATE.get());
    this.tag(RailcraftTags.Items.IRON_PLATE)
      .add(RailcraftItems.IRON_PLATE.get());
    this.tag(RailcraftTags.Items.SALTPETER_DUST)
      .add(RailcraftItems.SALTPETER_DUST.get());

    this.tag(RailcraftTags.Items.IRON_TANK_WALL)
      .add(RailcraftItems.IRON_TANK_WALL.resolveValues().toArray(Item[]::new));
    this.tag(RailcraftTags.Items.IRON_TANK_GAUGE)
      .add(RailcraftItems.IRON_TANK_GAUGE.resolveValues().toArray(Item[]::new));
    this.tag(RailcraftTags.Items.IRON_TANK_VALVE)
      .add(RailcraftItems.IRON_TANK_VALVE.resolveValues().toArray(Item[]::new));

    this.tag(RailcraftTags.Items.STEEL_TANK_WALL)
      .add(RailcraftItems.STEEL_TANK_WALL.resolveValues().toArray(Item[]::new));
    this.tag(RailcraftTags.Items.STEEL_TANK_GAUGE)
      .add(RailcraftItems.STEEL_TANK_GAUGE.resolveValues().toArray(Item[]::new));
    this.tag(RailcraftTags.Items.STEEL_TANK_VALVE)
      .add(RailcraftItems.STEEL_TANK_VALVE.resolveValues().toArray(Item[]::new));
  }

  @Override
  public String getName() {
    return "Railcraft Item Tags";
  }
}
