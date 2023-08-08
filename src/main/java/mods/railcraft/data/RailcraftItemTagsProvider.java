package mods.railcraft.data;

import java.util.concurrent.CompletableFuture;
import mods.railcraft.Railcraft;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class RailcraftItemTagsProvider extends ItemTagsProvider {

  public RailcraftItemTagsProvider(PackOutput packOutput,
      CompletableFuture<HolderLookup.Provider> lookupProvider,
      CompletableFuture<TagsProvider.TagLookup<Block>> blockTagProvider,
      ExistingFileHelper fileHelper) {
    super(packOutput, lookupProvider, blockTagProvider, Railcraft.ID, fileHelper);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void addTags(HolderLookup.Provider provider) {
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

    this.tag(RailcraftTags.Items.TIN_RAW)
        .add(RailcraftItems.TIN_RAW.get());
    this.tag(RailcraftTags.Items.ZINC_RAW)
        .add(RailcraftItems.ZINC_RAW.get());
    this.tag(RailcraftTags.Items.NICKEL_RAW)
        .add(RailcraftItems.NICKEL_RAW.get());
    this.tag(RailcraftTags.Items.SILVER_RAW)
        .add(RailcraftItems.SILVER_RAW.get());
    this.tag(RailcraftTags.Items.LEAD_RAW)
        .add(RailcraftItems.LEAD_RAW.get());

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
    this.tag(RailcraftTags.Items.BRONZE_BLOCK)
        .add(RailcraftItems.BRONZE_BLOCK.get());
    this.tag(RailcraftTags.Items.BRASS_BLOCK)
        .add(RailcraftItems.BRASS_BLOCK.get());
    this.tag(RailcraftTags.Items.INVAR_BLOCK)
        .add(RailcraftItems.INVAR_BLOCK.get());
    this.tag(RailcraftTags.Items.LEAD_BLOCK)
        .add(RailcraftItems.LEAD_BLOCK.get());
    this.tag(RailcraftTags.Items.TIN_BLOCK)
        .add(RailcraftItems.TIN_BLOCK.get());
    this.tag(RailcraftTags.Items.SILVER_BLOCK)
        .add(RailcraftItems.SILVER_BLOCK.get());
    this.tag(RailcraftTags.Items.NICKEL_BLOCK)
        .add(RailcraftItems.NICKEL_BLOCK.get());
    this.tag(RailcraftTags.Items.ZINC_BLOCK)
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


    this.tag(RailcraftTags.Items.PLATE_CHEST_LOOT)
        .addTag(RailcraftTags.Items.STEEL_PLATE)
        .addTag(RailcraftTags.Items.IRON_PLATE)
        .addTag(RailcraftTags.Items.TIN_PLATE)
        .addTag(RailcraftTags.Items.GOLD_PLATE)
        .addTag(RailcraftTags.Items.LEAD_PLATE)
        .addTag(RailcraftTags.Items.ZINC_PLATE)
        .addTag(RailcraftTags.Items.BRASS_PLATE)
        .addTag(RailcraftTags.Items.INVAR_PLATE)
        .addTag(RailcraftTags.Items.BRONZE_PLATE)
        .addTag(RailcraftTags.Items.COPPER_PLATE)
        .addTag(RailcraftTags.Items.NICKEL_PLATE)
        .addTag(RailcraftTags.Items.SILVER_PLATE);

    this.tag(RailcraftTags.Items.GEAR_CHEST_LOOT)
        .addTag(RailcraftTags.Items.STEEL_GEAR)
        .addTag(RailcraftTags.Items.IRON_GEAR)
        .addTag(RailcraftTags.Items.TIN_GEAR)
        .addTag(RailcraftTags.Items.GOLD_GEAR)
        .addTag(RailcraftTags.Items.LEAD_GEAR)
        .addTag(RailcraftTags.Items.ZINC_GEAR)
        .addTag(RailcraftTags.Items.BRASS_GEAR)
        .addTag(RailcraftTags.Items.INVAR_GEAR)
        .addTag(RailcraftTags.Items.BRONZE_GEAR)
        .addTag(RailcraftTags.Items.COPPER_GEAR)
        .addTag(RailcraftTags.Items.NICKEL_GEAR)
        .addTag(RailcraftTags.Items.SILVER_GEAR);

    this.tag(RailcraftTags.Items.INGOT_CHEST_LOOT)
        .addTag(RailcraftTags.Items.STEEL_INGOT)
        .addTag(RailcraftTags.Items.TIN_INGOT)
        .addTag(RailcraftTags.Items.LEAD_INGOT)
        .addTag(RailcraftTags.Items.ZINC_INGOT)
        .addTag(RailcraftTags.Items.BRASS_INGOT)
        .addTag(RailcraftTags.Items.INVAR_INGOT)
        .addTag(RailcraftTags.Items.BRONZE_INGOT)
        .addTag(RailcraftTags.Items.NICKEL_INGOT)
        .addTag(RailcraftTags.Items.SILVER_INGOT);

    this.tag(RailcraftTags.Items.SALTPETER_DUST)
        .add(RailcraftItems.SALTPETER_DUST.get());
    this.tag(RailcraftTags.Items.COAL_DUST)
        .add(RailcraftItems.COAL_DUST.get());
    this.tag(RailcraftTags.Items.CHARCOAL_DUST)
        .add(RailcraftItems.CHARCOAL_DUST.get());
    this.tag(RailcraftTags.Items.COAL_COKE)
        .add(RailcraftItems.COAL_COKE.get());
    this.tag(RailcraftTags.Items.SULFUR_DUST)
        .add(RailcraftItems.SULFUR_DUST.get());
    this.tag(RailcraftTags.Items.OBSIDIAN_DUST)
        .add(RailcraftItems.OBSIDIAN_DUST.get());


    this.tag(RailcraftTags.Items.CROWBAR)
        .add(RailcraftItems.STEEL_CROWBAR.get())
        .add(RailcraftItems.IRON_CROWBAR.get())
        .add(RailcraftItems.DIAMOND_CROWBAR.get());

    this.tag(RailcraftTags.Items.TRACK_KIT)
        .add(RailcraftItems.ACTIVATOR_TRACK_KIT.get())
        .add(RailcraftItems.BOOSTER_TRACK_KIT.get())
        .add(RailcraftItems.COUPLER_TRACK_KIT.get())
        .add(RailcraftItems.CONTROL_TRACK_KIT.get())
        .add(RailcraftItems.WHISTLE_TRACK_KIT.get())
        .add(RailcraftItems.BUFFER_STOP_TRACK_KIT.get())
        .add(RailcraftItems.DETECTOR_TRACK_KIT.get())
        .add(RailcraftItems.DISEMBARKING_TRACK_KIT.get())
        .add(RailcraftItems.EMBARKING_TRACK_KIT.get())
        .add(RailcraftItems.GATED_TRACK_KIT.get())
        .add(RailcraftItems.LAUNCHER_TRACK_KIT.get())
        .add(RailcraftItems.LOCKING_TRACK_KIT.get())
        .add(RailcraftItems.LOCOMOTIVE_TRACK_KIT.get())
        .add(RailcraftItems.ONE_WAY_TRACK_KIT.get())
        .add(RailcraftItems.ROUTING_TRACK_KIT.get())
        .add(RailcraftItems.THROTTLE_TRACK_KIT.get())
        .add(RailcraftItems.TRANSITION_TRACK_KIT.get());

    this.copy(RailcraftTags.Blocks.IRON_TANK_WALL, RailcraftTags.Items.IRON_TANK_WALL);
    this.copy(RailcraftTags.Blocks.IRON_TANK_GAUGE, RailcraftTags.Items.IRON_TANK_GAUGE);
    this.copy(RailcraftTags.Blocks.IRON_TANK_VALVE, RailcraftTags.Items.IRON_TANK_VALVE);
    this.copy(RailcraftTags.Blocks.STEEL_TANK_WALL, RailcraftTags.Items.STEEL_TANK_WALL);
    this.copy(RailcraftTags.Blocks.STEEL_TANK_GAUGE, RailcraftTags.Items.STEEL_TANK_GAUGE);
    this.copy(RailcraftTags.Blocks.STEEL_TANK_VALVE, RailcraftTags.Items.STEEL_TANK_VALVE);
    this.copy(RailcraftTags.Blocks.POST, RailcraftTags.Items.POST);
    this.copy(RailcraftTags.Blocks.STRENGTHENED_GLASS, RailcraftTags.Items.STRENGTHENED_GLASS);
    this.copy(RailcraftTags.Blocks.LEAD_ORE, RailcraftTags.Items.LEAD_ORE);
    this.copy(RailcraftTags.Blocks.NICKEL_ORE, RailcraftTags.Items.NICKEL_ORE);
    this.copy(RailcraftTags.Blocks.SILVER_ORE, RailcraftTags.Items.SILVER_ORE);
    this.copy(RailcraftTags.Blocks.SULFUR_ORE, RailcraftTags.Items.SULFUR_ORE);
    this.copy(RailcraftTags.Blocks.TIN_ORE, RailcraftTags.Items.TIN_ORE);
    this.copy(RailcraftTags.Blocks.ZINC_ORE, RailcraftTags.Items.ZINC_ORE);
    this.copy(RailcraftTags.Blocks.SALTPETER_ORE, RailcraftTags.Items.SALTPETER_ORE);

    this.copy(RailcraftTags.Blocks.ABANDONED_TRACK, RailcraftTags.Items.ABANDONED_TRACK);
    this.copy(RailcraftTags.Blocks.ELECTRIC_TRACK, RailcraftTags.Items.ELECTRIC_TRACK);
    this.copy(RailcraftTags.Blocks.HIGH_SPEED_TRACK, RailcraftTags.Items.HIGH_SPEED_TRACK);
    this.copy(RailcraftTags.Blocks.HIGH_SPEED_ELECTRIC_TRACK,
        RailcraftTags.Items.HIGH_SPEED_ELECTRIC_TRACK);
    this.copy(RailcraftTags.Blocks.IRON_TRACK, RailcraftTags.Items.IRON_TRACK);
    this.copy(RailcraftTags.Blocks.REINFORCED_TRACK, RailcraftTags.Items.REINFORCED_TRACK);
    this.copy(RailcraftTags.Blocks.STRAP_IRON_TRACK, RailcraftTags.Items.STRAP_IRON_TRACK);

    this.copy(RailcraftTags.Blocks.QUARRIED, RailcraftTags.Items.QUARRIED);

    // TOOLS
    this.tag(RailcraftTags.Items.TOOLS_AXES_STEEL).add(RailcraftItems.STEEL_AXE.get());
    this.tag(RailcraftTags.Items.TOOLS_HOES_STEEL).add(RailcraftItems.STEEL_HOE.get());
    this.tag(RailcraftTags.Items.TOOLS_PICKAXES_STEEL).add(RailcraftItems.STEEL_PICKAXE.get());
    this.tag(RailcraftTags.Items.TOOLS_SHOVELS_STEEL).add(RailcraftItems.STEEL_SHOVEL.get());
    this.tag(RailcraftTags.Items.TOOLS_SWORDS_STEEL).add(RailcraftItems.STEEL_SWORD.get());
    this.tag(RailcraftTags.Items.ARMORS_HELMETS_STEEL).add(RailcraftItems.STEEL_HELMET.get());
    this.tag(RailcraftTags.Items.ARMORS_CHESTPLATES_STEEL)
        .add(RailcraftItems.STEEL_CHESTPLATE.get());
    this.tag(RailcraftTags.Items.ARMORS_LEGGINGS_STEEL).add(RailcraftItems.STEEL_LEGGINGS.get());
    this.tag(RailcraftTags.Items.ARMORS_BOOTS_STEEL).add(RailcraftItems.STEEL_BOOTS.get());


    this.tag(ItemTags.AXES).add(RailcraftItems.STEEL_AXE.get());
    this.tag(ItemTags.HOES).add(RailcraftItems.STEEL_HOE.get());
    this.tag(ItemTags.PICKAXES).add(RailcraftItems.STEEL_PICKAXE.get());
    this.tag(ItemTags.SHOVELS).add(RailcraftItems.STEEL_SHOVEL.get());
    this.tag(ItemTags.SWORDS).add(RailcraftItems.STEEL_SWORD.get());

    this.tag(Tags.Items.ARMORS_HELMETS).addTags(RailcraftTags.Items.ARMORS_HELMETS_STEEL);
    this.tag(Tags.Items.ARMORS_CHESTPLATES).addTags(RailcraftTags.Items.ARMORS_CHESTPLATES_STEEL);
    this.tag(Tags.Items.ARMORS_LEGGINGS).addTags(RailcraftTags.Items.ARMORS_LEGGINGS_STEEL);
    this.tag(Tags.Items.ARMORS_BOOTS).addTags(RailcraftTags.Items.ARMORS_BOOTS_STEEL);
  }

  @Override
  public String getName() {
    return "Railcraft Item Tags";
  }
}
