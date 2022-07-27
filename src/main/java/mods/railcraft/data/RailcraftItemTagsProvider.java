package mods.railcraft.data;

import mods.railcraft.Railcraft;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Item;
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
    this.tag(RailcraftTags.Items.NICKEL_INGOT)
        .add(RailcraftItems.NICKEL_INGOT.get());
    this.tag(RailcraftTags.Items.INVAR_INGOT)
        .add(RailcraftItems.INVAR_INGOT.get());

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
    this.tag(RailcraftTags.Items.NICKEL_NUGGET)
        .add(RailcraftItems.NICKEL_NUGGET.get());
    this.tag(RailcraftTags.Items.INVAR_NUGGET)
        .add(RailcraftItems.INVAR_NUGGET.get());

    this.tag(RailcraftTags.Items.STEEL_BLOCK)
        .add(RailcraftItems.STEEL_BLOCK.get());

    this.tag(RailcraftTags.Items.STEEL_PLATE)
        .add(RailcraftItems.STEEL_PLATE.get());
    this.tag(RailcraftTags.Items.IRON_PLATE)
        .add(RailcraftItems.IRON_PLATE.get());

    this.tag(RailcraftTags.Items.TIN_PLATE)
      .add(RailcraftItems.TIN_PLATE.get());
    this.tag(RailcraftTags.Items.GOLD_PLATE)
      .add(RailcraftItems.GOLD_PLATE.get());
    this.tag(RailcraftTags.Items.LEAD_PLATE)
      .add(RailcraftItems.LEAD_PLATE.get());
    this.tag(RailcraftTags.Items.ZINC_PLATE)
      .add(RailcraftItems.ZINC_PLATE.get());
    this.tag(RailcraftTags.Items.BRASS_PLATE)
      .add(RailcraftItems.BRASS_PLATE.get());
    this.tag(RailcraftTags.Items.INVAR_PLATE)
      .add(RailcraftItems.INVAR_PLATE.get());
    this.tag(RailcraftTags.Items.BRONZE_PLATE)
      .add(RailcraftItems.BRONZE_PLATE.get());
    this.tag(RailcraftTags.Items.COPPER_PLATE)
      .add(RailcraftItems.COPPER_PLATE.get());
    this.tag(RailcraftTags.Items.NICKEL_PLATE)
      .add(RailcraftItems.NICKEL_PLATE.get());
    this.tag(RailcraftTags.Items.SILVER_PLATE)
      .add(RailcraftItems.SILVER_PLATE.get());


    this.tag(RailcraftTags.Items.SALTPETER_DUST)
        .add(RailcraftItems.SALTPETER_DUST.get());

    this.tag(RailcraftTags.Items.IRON_TANK_WALL)
        .add(RailcraftItems.IRON_TANK_WALL.resolveVariants().toArray(Item[]::new));
    this.tag(RailcraftTags.Items.IRON_TANK_GAUGE)
        .add(RailcraftItems.IRON_TANK_GAUGE.resolveVariants().toArray(Item[]::new));
    this.tag(RailcraftTags.Items.IRON_TANK_VALVE)
        .add(RailcraftItems.IRON_TANK_VALVE.resolveVariants().toArray(Item[]::new));

    this.tag(RailcraftTags.Items.STEEL_TANK_WALL)
        .add(RailcraftItems.STEEL_TANK_WALL.resolveVariants().toArray(Item[]::new));
    this.tag(RailcraftTags.Items.STEEL_TANK_GAUGE)
        .add(RailcraftItems.STEEL_TANK_GAUGE.resolveVariants().toArray(Item[]::new));
    this.tag(RailcraftTags.Items.STEEL_TANK_VALVE)
        .add(RailcraftItems.STEEL_TANK_VALVE.resolveVariants().toArray(Item[]::new));

    this.tag(RailcraftTags.Items.STRENGTHENED_GLASS)
        .add(RailcraftItems.STRENGTHENED_GLASS.resolveVariants().toArray(Item[]::new));
  }

  @Override
  public String getName() {
    return "Railcraft Item Tags";
  }
}
