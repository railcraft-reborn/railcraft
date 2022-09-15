package mods.railcraft.data;

import mods.railcraft.Railcraft;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.tags.RailcraftTags.Blocks;
import mods.railcraft.tags.RailcraftTags.Items;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class RailcraftItemTagsProvider extends ItemTagsProvider {

  public RailcraftItemTagsProvider(DataGenerator generator, BlockTagsProvider blockTagsProvider,
      ExistingFileHelper fileHelper) {
    super(generator, blockTagsProvider, Railcraft.ID, fileHelper);
  }

  @SuppressWarnings("unchecked")
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
    this.tag(RailcraftTags.Items.LEAD_INGOT)
        .add(RailcraftItems.LEAD_INGOT.get());
    this.tag(RailcraftTags.Items.SILVER_INGOT)
        .add(RailcraftItems.SILVER_INGOT.get());

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
    this.tag(RailcraftTags.Items.SILVER_NUGGET)
        .add(RailcraftItems.SILVER_NUGGET.get());
    this.tag(RailcraftTags.Items.LEAD_NUGGET)
        .add(RailcraftItems.LEAD_NUGGET.get());

    this.tag(RailcraftTags.Items.STEEL_BLOCK)
        .add(RailcraftItems.STEEL_BLOCK.get());
    this.tag(Items.BRONZE_BLOCK)
        .add(RailcraftItems.BRONZE_BLOCK.get());
    this.tag(Items.BRASS_BLOCK)
        .add(RailcraftItems.BRASS_BLOCK.get());
    this.tag(Items.INVAR_BLOCK)
        .add(RailcraftItems.INVAR_BLOCK.get());
    this.tag(Items.LEAD_BLOCK)
        .add(RailcraftItems.LEAD_BLOCK.get());
    this.tag(Items.TIN_BLOCK)
        .add(RailcraftItems.TIN_BLOCK.get());
    this.tag(Items.SILVER_BLOCK)
        .add(RailcraftItems.SILVER_BLOCK.get());
    this.tag(Items.NICKEL_BLOCK)
        .add(RailcraftItems.NICKEL_BLOCK.get());
    this.tag(Items.ZINC_BLOCK)
        .add(RailcraftItems.ZINC_BLOCK.get());

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

    this.tag(RailcraftTags.Items.STEEL_GEAR)
        .add(RailcraftItems.STEEL_GEAR.get());
    this.tag(RailcraftTags.Items.IRON_GEAR)
        .add(RailcraftItems.IRON_GEAR.get());
    this.tag(RailcraftTags.Items.TIN_GEAR)
        .add(RailcraftItems.TIN_GEAR.get());
    this.tag(RailcraftTags.Items.GOLD_GEAR)
        .add(RailcraftItems.GOLD_GEAR.get());
    this.tag(RailcraftTags.Items.LEAD_GEAR)
        .add(RailcraftItems.LEAD_GEAR.get());
    this.tag(RailcraftTags.Items.ZINC_GEAR)
        .add(RailcraftItems.ZINC_GEAR.get());
    this.tag(RailcraftTags.Items.BRASS_GEAR)
        .add(RailcraftItems.BRASS_GEAR.get());
    this.tag(RailcraftTags.Items.INVAR_GEAR)
        .add(RailcraftItems.INVAR_GEAR.get());
    this.tag(RailcraftTags.Items.BRONZE_GEAR)
        .add(RailcraftItems.BRONZE_GEAR.get());
    this.tag(RailcraftTags.Items.COPPER_GEAR)
        .add(RailcraftItems.COPPER_GEAR.get());
    this.tag(RailcraftTags.Items.NICKEL_GEAR)
        .add(RailcraftItems.NICKEL_GEAR.get());
    this.tag(RailcraftTags.Items.SILVER_GEAR)
        .add(RailcraftItems.SILVER_GEAR.get());

    this.tag(RailcraftTags.Items.SALTPETER_DUST)
        .add(RailcraftItems.SALTPETER_DUST.get());
    this.tag(RailcraftTags.Items.COAL_DUST)
        .add(RailcraftItems.COAL_DUST.get());
    this.tag(RailcraftTags.Items.CHARCOAL_DUST)
        .add(RailcraftItems.CHARCOAL_DUST.get());
    this.tag(RailcraftTags.Items.SULFUR_DUST)
        .add(RailcraftItems.SULFUR_DUST.get());
    this.tag(RailcraftTags.Items.OBSIDIAN_DUST)
        .add(RailcraftItems.OBSIDIAN_DUST.get());


    this.tag(Items.CROWBAR)
        .add(RailcraftItems.STEEL_CROWBAR.get())
        .add(RailcraftItems.IRON_CROWBAR.get())
        .add(RailcraftItems.DIAMOND_CROWBAR.get());


    this.tag(RailcraftTags.Items.STRENGTHENED_GLASS)
        .add(RailcraftItems.STRENGTHENED_GLASS.resolveVariants().toArray(Item[]::new));


    this.copy(Blocks.IRON_TANK_WALL, Items.IRON_TANK_WALL);
    this.copy(Blocks.IRON_TANK_GAUGE, Items.IRON_TANK_GAUGE);
    this.copy(Blocks.IRON_TANK_VALVE, Items.IRON_TANK_VALVE);
    this.copy(Blocks.STEEL_TANK_WALL, Items.STEEL_TANK_WALL);
    this.copy(Blocks.STEEL_TANK_GAUGE, Items.STEEL_TANK_GAUGE);
    this.copy(Blocks.STEEL_TANK_VALVE, Items.STEEL_TANK_VALVE);
    this.copy(Blocks.POST, Items.POST);
    this.copy(Blocks.LEAD_ORE, Items.LEAD_ORE);
    this.copy(Blocks.NICKEL_ORE, Items.NICKEL_ORE);
    this.copy(Blocks.SILVER_ORE, Items.SILVER_ORE);
    this.copy(Blocks.SULFUR_ORE, Items.SULFUR_ORE);
    this.copy(Blocks.TIN_ORE, Items.TIN_ORE);
    this.copy(Blocks.ZINC_ORE, Items.ZINC_ORE);
    this.copy(Blocks.SALTPETER_ORE, Items.SALTPETER_ORE);

    this.copy(Blocks.ABANDONED_TRACK, Items.ABANDONED_TRACK);
    this.copy(Blocks.ELECTRIC_TRACK, Items.ELECTRIC_TRACK);
    this.copy(Blocks.HIGH_SPEED_TRACK, Items.HIGH_SPEED_TRACK);
    this.copy(Blocks.HIGH_SPEED_ELECTRIC_TRACK, Items.HIGH_SPEED_ELECTRIC_TRACK);
    this.copy(Blocks.IRON_TRACK, Items.IRON_TRACK);
    this.copy(Blocks.REINFORCED_TRACK, Items.REINFORCED_TRACK);
    this.copy(Blocks.STRAP_IRON_TRACK, Items.STRAP_IRON_TRACK);



    // TOOLS
    this.tag(Items.TOOLS_AXES_STEEL).add(RailcraftItems.STEEL_AXE.get());
    this.tag(Items.TOOLS_HOES_STEEL).add(RailcraftItems.STEEL_HOE.get());
    this.tag(Items.TOOLS_PICKAXES_STEEL).add(RailcraftItems.STEEL_PICKAXE.get());
    this.tag(Items.TOOLS_SHOVELS_STEEL).add(RailcraftItems.STEEL_SHOVEL.get());
    this.tag(Items.TOOLS_SWORDS_STEEL).add(RailcraftItems.STEEL_SWORD.get());
    this.tag(Items.ARMORS_HELMETS_STEEL).add(RailcraftItems.STEEL_HELMET.get());
    this.tag(Items.ARMORS_CHESTPLATES_STEEL).add(RailcraftItems.STEEL_CHESTPLATE.get());
    this.tag(Items.ARMORS_LEGGINGS_STEEL).add(RailcraftItems.STEEL_LEGGINGS.get());
    this.tag(Items.ARMORS_BOOTS_STEEL).add(RailcraftItems.STEEL_BOOTS.get());


    this.tag(Tags.Items.TOOLS_AXES).add(RailcraftItems.STEEL_AXE.get());
    this.tag(Tags.Items.TOOLS_HOES).add(RailcraftItems.STEEL_HOE.get());
    this.tag(Tags.Items.TOOLS_PICKAXES).add(RailcraftItems.STEEL_PICKAXE.get());
    this.tag(Tags.Items.TOOLS_SHOVELS).add(RailcraftItems.STEEL_SHOVEL.get());
    this.tag(Tags.Items.TOOLS_SWORDS).add(RailcraftItems.STEEL_SWORD.get());

    this.tag(Tags.Items.ARMORS_HELMETS).addTags(Items.ARMORS_HELMETS_STEEL);
    this.tag(Tags.Items.ARMORS_CHESTPLATES).addTags(Items.ARMORS_CHESTPLATES_STEEL);
    this.tag(Tags.Items.ARMORS_LEGGINGS).addTags(Items.ARMORS_LEGGINGS_STEEL);
    this.tag(Tags.Items.ARMORS_BOOTS).addTags(Items.ARMORS_BOOTS_STEEL);
  }

  @Override
  public String getName() {
    return "Railcraft Item Tags";
  }
}
