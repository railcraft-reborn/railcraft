package mods.railcraft.data.tags;

import java.util.concurrent.CompletableFuture;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagCopyingItemTagProvider;

public class RailcraftItemTagsProvider extends BlockTagCopyingItemTagProvider {

  public RailcraftItemTagsProvider(PackOutput packOutput,
      CompletableFuture<HolderLookup.Provider> registries,
      CompletableFuture<TagLookup<Block>> blockTags) {
    super(packOutput, registries, blockTags, RailcraftConstants.ID);
  }

  @Override
  protected void addTags(HolderLookup.Provider registries) {
    this.tag(Tags.Items.INGOTS)
        .addTag(RailcraftTags.Items.STEEL_INGOT)
        .addTag(RailcraftTags.Items.TIN_INGOT)
        .addTag(RailcraftTags.Items.ZINC_INGOT)
        .addTag(RailcraftTags.Items.BRASS_INGOT)
        .addTag(RailcraftTags.Items.BRONZE_INGOT)
        .addTag(RailcraftTags.Items.NICKEL_INGOT)
        .addTag(RailcraftTags.Items.INVAR_INGOT)
        .addTag(RailcraftTags.Items.LEAD_INGOT)
        .addTag(RailcraftTags.Items.SILVER_INGOT);
    this.tag(RailcraftTags.Items.STEEL_INGOT)
        .add(RailcraftItems.STEEL_INGOT.key());
    this.tag(RailcraftTags.Items.TIN_INGOT)
        .add(RailcraftItems.TIN_INGOT.key());
    this.tag(RailcraftTags.Items.ZINC_INGOT)
        .add(RailcraftItems.ZINC_INGOT.key());
    this.tag(RailcraftTags.Items.BRASS_INGOT)
        .add(RailcraftItems.BRASS_INGOT.key());
    this.tag(RailcraftTags.Items.BRONZE_INGOT)
        .add(RailcraftItems.BRONZE_INGOT.key());
    this.tag(RailcraftTags.Items.NICKEL_INGOT)
        .add(RailcraftItems.NICKEL_INGOT.key());
    this.tag(RailcraftTags.Items.INVAR_INGOT)
        .add(RailcraftItems.INVAR_INGOT.key());
    this.tag(RailcraftTags.Items.LEAD_INGOT)
        .add(RailcraftItems.LEAD_INGOT.key());
    this.tag(RailcraftTags.Items.SILVER_INGOT)
        .add(RailcraftItems.SILVER_INGOT.key());

    this.tag(Tags.Items.RAW_MATERIALS)
        .addTag(RailcraftTags.Items.TIN_RAW)
        .addTag(RailcraftTags.Items.ZINC_RAW)
        .addTag(RailcraftTags.Items.NICKEL_RAW)
        .addTag(RailcraftTags.Items.SILVER_RAW)
        .addTag(RailcraftTags.Items.LEAD_RAW);
    this.tag(RailcraftTags.Items.TIN_RAW)
        .add(RailcraftItems.TIN_RAW.key());
    this.tag(RailcraftTags.Items.ZINC_RAW)
        .add(RailcraftItems.ZINC_RAW.key());
    this.tag(RailcraftTags.Items.NICKEL_RAW)
        .add(RailcraftItems.NICKEL_RAW.key());
    this.tag(RailcraftTags.Items.SILVER_RAW)
        .add(RailcraftItems.SILVER_RAW.key());
    this.tag(RailcraftTags.Items.LEAD_RAW)
        .add(RailcraftItems.LEAD_RAW.key());

    this.tag(Tags.Items.NUGGETS)
        .addTag(RailcraftTags.Items.STEEL_NUGGET)
        .addTag(RailcraftTags.Items.TIN_NUGGET)
        .addTag(RailcraftTags.Items.ZINC_NUGGET)
        .addTag(RailcraftTags.Items.BRASS_NUGGET)
        .addTag(RailcraftTags.Items.BRONZE_NUGGET)
        .addTag(RailcraftTags.Items.NICKEL_NUGGET)
        .addTag(RailcraftTags.Items.INVAR_NUGGET)
        .addTag(RailcraftTags.Items.SILVER_NUGGET)
        .addTag(RailcraftTags.Items.LEAD_NUGGET);
    this.tag(RailcraftTags.Items.STEEL_NUGGET)
        .add(RailcraftItems.STEEL_NUGGET.key());
    this.tag(RailcraftTags.Items.TIN_NUGGET)
        .add(RailcraftItems.TIN_NUGGET.key());
    this.tag(RailcraftTags.Items.ZINC_NUGGET)
        .add(RailcraftItems.ZINC_NUGGET.key());
    this.tag(RailcraftTags.Items.BRASS_NUGGET)
        .add(RailcraftItems.BRASS_NUGGET.key());
    this.tag(RailcraftTags.Items.BRONZE_NUGGET)
        .add(RailcraftItems.BRONZE_NUGGET.key());
    this.tag(RailcraftTags.Items.NICKEL_NUGGET)
        .add(RailcraftItems.NICKEL_NUGGET.key());
    this.tag(RailcraftTags.Items.INVAR_NUGGET)
        .add(RailcraftItems.INVAR_NUGGET.key());
    this.tag(RailcraftTags.Items.SILVER_NUGGET)
        .add(RailcraftItems.SILVER_NUGGET.key());
    this.tag(RailcraftTags.Items.LEAD_NUGGET)
        .add(RailcraftItems.LEAD_NUGGET.key());

    this.tag(RailcraftTags.Items.STEEL_BLOCK)
        .add(RailcraftItems.STEEL_BLOCK.key());
    this.tag(RailcraftTags.Items.BRONZE_BLOCK)
        .add(RailcraftItems.BRONZE_BLOCK.key());
    this.tag(RailcraftTags.Items.BRASS_BLOCK)
        .add(RailcraftItems.BRASS_BLOCK.key());
    this.tag(RailcraftTags.Items.INVAR_BLOCK)
        .add(RailcraftItems.INVAR_BLOCK.key());
    this.tag(RailcraftTags.Items.LEAD_BLOCK)
        .add(RailcraftItems.LEAD_BLOCK.key());
    this.tag(RailcraftTags.Items.TIN_BLOCK)
        .add(RailcraftItems.TIN_BLOCK.key());
    this.tag(RailcraftTags.Items.SILVER_BLOCK)
        .add(RailcraftItems.SILVER_BLOCK.key());
    this.tag(RailcraftTags.Items.NICKEL_BLOCK)
        .add(RailcraftItems.NICKEL_BLOCK.key());
    this.tag(RailcraftTags.Items.ZINC_BLOCK)
        .add(RailcraftItems.ZINC_BLOCK.key());
    this.tag(RailcraftTags.Items.COAL_COKE_BLOCK)
        .add(RailcraftItems.COAL_COKE_BLOCK.key());

    this.tag(Tags.Items.STORAGE_BLOCKS)
        .addTag(RailcraftTags.Items.STEEL_BLOCK)
        .addTag(RailcraftTags.Items.BRONZE_BLOCK)
        .addTag(RailcraftTags.Items.BRASS_BLOCK)
        .addTag(RailcraftTags.Items.INVAR_BLOCK)
        .addTag(RailcraftTags.Items.LEAD_BLOCK)
        .addTag(RailcraftTags.Items.TIN_BLOCK)
        .addTag(RailcraftTags.Items.SILVER_BLOCK)
        .addTag(RailcraftTags.Items.NICKEL_BLOCK)
        .addTag(RailcraftTags.Items.ZINC_BLOCK)
        .addTag(RailcraftTags.Items.COAL_COKE_BLOCK);

    this.tag(RailcraftTags.Items.PLATES)
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
    this.tag(RailcraftTags.Items.STEEL_PLATE)
        .add(RailcraftItems.STEEL_PLATE.key());
    this.tag(RailcraftTags.Items.IRON_PLATE)
        .add(RailcraftItems.IRON_PLATE.key());
    this.tag(RailcraftTags.Items.TIN_PLATE)
        .add(RailcraftItems.TIN_PLATE.key());
    this.tag(RailcraftTags.Items.GOLD_PLATE)
        .add(RailcraftItems.GOLD_PLATE.key());
    this.tag(RailcraftTags.Items.LEAD_PLATE)
        .add(RailcraftItems.LEAD_PLATE.key());
    this.tag(RailcraftTags.Items.ZINC_PLATE)
        .add(RailcraftItems.ZINC_PLATE.key());
    this.tag(RailcraftTags.Items.BRASS_PLATE)
        .add(RailcraftItems.BRASS_PLATE.key());
    this.tag(RailcraftTags.Items.INVAR_PLATE)
        .add(RailcraftItems.INVAR_PLATE.key());
    this.tag(RailcraftTags.Items.BRONZE_PLATE)
        .add(RailcraftItems.BRONZE_PLATE.key());
    this.tag(RailcraftTags.Items.COPPER_PLATE)
        .add(RailcraftItems.COPPER_PLATE.key());
    this.tag(RailcraftTags.Items.NICKEL_PLATE)
        .add(RailcraftItems.NICKEL_PLATE.key());
    this.tag(RailcraftTags.Items.SILVER_PLATE)
        .add(RailcraftItems.SILVER_PLATE.key());

    this.tag(RailcraftTags.Items.GEARS)
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
    this.tag(RailcraftTags.Items.STEEL_GEAR)
        .add(RailcraftItems.STEEL_GEAR.key());
    this.tag(RailcraftTags.Items.IRON_GEAR)
        .add(RailcraftItems.IRON_GEAR.key());
    this.tag(RailcraftTags.Items.TIN_GEAR)
        .add(RailcraftItems.TIN_GEAR.key());
    this.tag(RailcraftTags.Items.GOLD_GEAR)
        .add(RailcraftItems.GOLD_GEAR.key());
    this.tag(RailcraftTags.Items.LEAD_GEAR)
        .add(RailcraftItems.LEAD_GEAR.key());
    this.tag(RailcraftTags.Items.ZINC_GEAR)
        .add(RailcraftItems.ZINC_GEAR.key());
    this.tag(RailcraftTags.Items.BRASS_GEAR)
        .add(RailcraftItems.BRASS_GEAR.key());
    this.tag(RailcraftTags.Items.INVAR_GEAR)
        .add(RailcraftItems.INVAR_GEAR.key());
    this.tag(RailcraftTags.Items.BRONZE_GEAR)
        .add(RailcraftItems.BRONZE_GEAR.key());
    this.tag(RailcraftTags.Items.COPPER_GEAR)
        .add(RailcraftItems.COPPER_GEAR.key());
    this.tag(RailcraftTags.Items.NICKEL_GEAR)
        .add(RailcraftItems.NICKEL_GEAR.key());
    this.tag(RailcraftTags.Items.SILVER_GEAR)
        .add(RailcraftItems.SILVER_GEAR.key());


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
        .add(RailcraftItems.SALTPETER_DUST.key());
    this.tag(RailcraftTags.Items.COAL_DUST)
        .add(RailcraftItems.COAL_DUST.key());
    this.tag(RailcraftTags.Items.CHARCOAL_DUST)
        .add(RailcraftItems.CHARCOAL_DUST.key());
    this.tag(RailcraftTags.Items.COAL_COKE)
        .add(RailcraftItems.COAL_COKE.key());
    this.tag(RailcraftTags.Items.SULFUR_DUST)
        .add(RailcraftItems.SULFUR_DUST.key());
    this.tag(RailcraftTags.Items.OBSIDIAN_DUST)
        .add(RailcraftItems.OBSIDIAN_DUST.key());
    this.tag(RailcraftTags.Items.ENDER_DUST)
        .add(RailcraftItems.ENDER_DUST.key());

    this.tag(RailcraftTags.Items.CROWBAR)
        .add(RailcraftItems.STEEL_CROWBAR.key())
        .add(RailcraftItems.IRON_CROWBAR.key())
        .add(RailcraftItems.DIAMOND_CROWBAR.key());

    this.tag(RailcraftTags.Items.ENCHANTMENTS)
        .add(RailcraftItems.SEASONS_CROWBAR.key())
        .add(RailcraftItems.IRON_SPIKE_MAUL.key())
        .add(RailcraftItems.STEEL_SPIKE_MAUL.key())
        .add(RailcraftItems.DIAMOND_SPIKE_MAUL.key())
        .addTag(RailcraftTags.Items.CROWBAR);

    this.tag(RailcraftTags.Items.TRACK_KIT)
        .add(RailcraftItems.ACTIVATOR_TRACK_KIT.key())
        .add(RailcraftItems.BOOSTER_TRACK_KIT.key())
        .add(RailcraftItems.COUPLER_TRACK_KIT.key())
        .add(RailcraftItems.CONTROL_TRACK_KIT.key())
        .add(RailcraftItems.WHISTLE_TRACK_KIT.key())
        .add(RailcraftItems.BUFFER_STOP_TRACK_KIT.key())
        .add(RailcraftItems.DETECTOR_TRACK_KIT.key())
        .add(RailcraftItems.DISEMBARKING_TRACK_KIT.key())
        .add(RailcraftItems.EMBARKING_TRACK_KIT.key())
        .add(RailcraftItems.DUMPING_TRACK_KIT.key())
        .add(RailcraftItems.GATED_TRACK_KIT.key())
        .add(RailcraftItems.LAUNCHER_TRACK_KIT.key())
        .add(RailcraftItems.LOCKING_TRACK_KIT.key())
        .add(RailcraftItems.DELAYED_LOCKING_TRACK_KIT.key())
        .add(RailcraftItems.MESSENGER_TRACK_KIT.key())
        .add(RailcraftItems.LOCOMOTIVE_TRACK_KIT.key())
        .add(RailcraftItems.ONE_WAY_TRACK_KIT.key())
        .add(RailcraftItems.ROUTING_TRACK_KIT.key())
        .add(RailcraftItems.THROTTLE_TRACK_KIT.key())
        .add(RailcraftItems.TRANSITION_TRACK_KIT.key());

    this.tag(RailcraftTags.Items.SLAG).add(RailcraftItems.SLAG.key());

    this.addBackpackTags();

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

    this.copy(Tags.Blocks.ORE_RATES_SINGULAR, Tags.Items.ORE_RATES_SINGULAR);
    this.copy(Tags.Blocks.ORE_RATES_DENSE, Tags.Items.ORE_RATES_DENSE);
    this.copy(Tags.Blocks.ORES, Tags.Items.ORES);
    this.copy(Tags.Blocks.ORES_IN_GROUND_STONE, Tags.Items.ORES_IN_GROUND_STONE);
    this.copy(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE, Tags.Items.ORES_IN_GROUND_DEEPSLATE);
    this.copy(Tags.Blocks.ORES_IN_GROUND_NETHERRACK, Tags.Items.ORES_IN_GROUND_NETHERRACK);

    this.copy(RailcraftTags.Blocks.ABANDONED_TRACK, RailcraftTags.Items.ABANDONED_TRACK);
    this.copy(RailcraftTags.Blocks.ELECTRIC_TRACK, RailcraftTags.Items.ELECTRIC_TRACK);
    this.copy(RailcraftTags.Blocks.HIGH_SPEED_TRACK, RailcraftTags.Items.HIGH_SPEED_TRACK);
    this.copy(RailcraftTags.Blocks.HIGH_SPEED_ELECTRIC_TRACK,
        RailcraftTags.Items.HIGH_SPEED_ELECTRIC_TRACK);
    this.copy(RailcraftTags.Blocks.IRON_TRACK, RailcraftTags.Items.IRON_TRACK);
    this.copy(RailcraftTags.Blocks.REINFORCED_TRACK, RailcraftTags.Items.REINFORCED_TRACK);
    this.copy(RailcraftTags.Blocks.STRAP_IRON_TRACK, RailcraftTags.Items.STRAP_IRON_TRACK);

    this.copy(RailcraftTags.Blocks.QUARRIED, RailcraftTags.Items.QUARRIED);
    this.copy(RailcraftTags.Blocks.ABYSSAL, RailcraftTags.Items.ABYSSAL);
    this.copy(RailcraftTags.Blocks.DETECTOR, RailcraftTags.Items.DETECTOR);

    // TOOLS
    this.tag(Tags.Items.TOOLS_SHEAR).add(RailcraftItems.STEEL_SHEARS.key());
    this.tag(Tags.Items.MELEE_WEAPON_TOOLS).add(RailcraftItems.STEEL_SWORD.key());
    this.tag(RailcraftTags.Items.TOOLS_AXES_STEEL).add(RailcraftItems.STEEL_AXE.key());
    this.tag(RailcraftTags.Items.TOOLS_HOES_STEEL).add(RailcraftItems.STEEL_HOE.key());
    this.tag(RailcraftTags.Items.TOOLS_PICKAXES_STEEL).add(RailcraftItems.STEEL_PICKAXE.key());
    this.tag(RailcraftTags.Items.TOOLS_SHOVELS_STEEL).add(RailcraftItems.STEEL_SHOVEL.key());
    this.tag(RailcraftTags.Items.TOOLS_SWORDS_STEEL).add(RailcraftItems.STEEL_SWORD.key());
    this.tag(RailcraftTags.Items.ARMORS_HELMETS_STEEL).add(RailcraftItems.STEEL_HELMET.key());
    this.tag(RailcraftTags.Items.ARMORS_CHESTPLATES_STEEL)
        .add(RailcraftItems.STEEL_CHESTPLATE.key());
    this.tag(RailcraftTags.Items.ARMORS_LEGGINGS_STEEL).add(RailcraftItems.STEEL_LEGGINGS.key());
    this.tag(RailcraftTags.Items.ARMORS_BOOTS_STEEL).add(RailcraftItems.STEEL_BOOTS.key());


    this.tag(ItemTags.AXES).add(RailcraftItems.STEEL_AXE.key());
    this.tag(ItemTags.HOES).add(RailcraftItems.STEEL_HOE.key());
    this.tag(ItemTags.PICKAXES).add(RailcraftItems.STEEL_PICKAXE.key());
    this.tag(ItemTags.SHOVELS).add(RailcraftItems.STEEL_SHOVEL.key());
    this.tag(ItemTags.SWORDS).add(RailcraftItems.STEEL_SWORD.key());

    this.tag(ItemTags.HEAD_ARMOR).addTag(RailcraftTags.Items.ARMORS_HELMETS_STEEL);
    this.tag(ItemTags.CHEST_ARMOR).addTag(RailcraftTags.Items.ARMORS_CHESTPLATES_STEEL);
    this.tag(ItemTags.LEG_ARMOR)
        .add(RailcraftItems.OVERALLS.key())
        .addTag(RailcraftTags.Items.ARMORS_LEGGINGS_STEEL);
    this.tag(ItemTags.FOOT_ARMOR).addTag(RailcraftTags.Items.ARMORS_BOOTS_STEEL);

    this.tag(ItemTags.DURABILITY_ENCHANTABLE)
        .add(RailcraftItems.IRON_TUNNEL_BORE_HEAD.key())
        .add(RailcraftItems.BRONZE_TUNNEL_BORE_HEAD.key())
        .add(RailcraftItems.STEEL_TUNNEL_BORE_HEAD.key())
        .add(RailcraftItems.DIAMOND_TUNNEL_BORE_HEAD.key())
        .add(RailcraftItems.NETHERITE_TUNNEL_BORE_HEAD.key());
    this.tag(ItemTags.MINING_ENCHANTABLE)
        .add(RailcraftItems.IRON_TUNNEL_BORE_HEAD.key())
        .add(RailcraftItems.BRONZE_TUNNEL_BORE_HEAD.key())
        .add(RailcraftItems.STEEL_TUNNEL_BORE_HEAD.key())
        .add(RailcraftItems.DIAMOND_TUNNEL_BORE_HEAD.key())
        .add(RailcraftItems.NETHERITE_TUNNEL_BORE_HEAD.key());
    this.tag(ItemTags.MINING_LOOT_ENCHANTABLE)
        .add(RailcraftItems.IRON_TUNNEL_BORE_HEAD.key())
        .add(RailcraftItems.BRONZE_TUNNEL_BORE_HEAD.key())
        .add(RailcraftItems.STEEL_TUNNEL_BORE_HEAD.key())
        .add(RailcraftItems.DIAMOND_TUNNEL_BORE_HEAD.key())
        .add(RailcraftItems.NETHERITE_TUNNEL_BORE_HEAD.key());
  }

  private void addBackpackTags() {
    this.tag(RailcraftTags.Items.TRACKMAN_BACKPACK_ALLOW)
        .addTag(ItemTags.RAILS)
        .addTag(RailcraftTags.Items.ABANDONED_TRACK)
        .addTag(RailcraftTags.Items.ELECTRIC_TRACK)
        .addTag(RailcraftTags.Items.HIGH_SPEED_TRACK)
        .addTag(RailcraftTags.Items.HIGH_SPEED_ELECTRIC_TRACK)
        .addTag(RailcraftTags.Items.IRON_TRACK)
        .addTag(RailcraftTags.Items.REINFORCED_TRACK)
        .addTag(RailcraftTags.Items.STRAP_IRON_TRACK)
        .addTag(RailcraftTags.Items.TRACK_KIT)
        .addTag(RailcraftTags.Items.CROWBAR)
        .add(Items.MINECART.builtInRegistryHolder().key())
        .add(Items.CHEST_MINECART.builtInRegistryHolder().key())
        .add(Items.FURNACE_MINECART.builtInRegistryHolder().key())
        .add(Items.HOPPER_MINECART.builtInRegistryHolder().key())
        .add(Items.TNT_MINECART.builtInRegistryHolder().key())
        .add(Items.COMMAND_BLOCK_MINECART.builtInRegistryHolder().key())
        .add(RailcraftItems.ELEVATOR_TRACK.key())
        .add(RailcraftItems.TRACK_PARTS.key())
        .add(RailcraftItems.WOODEN_RAIL.key())
        .add(RailcraftItems.STANDARD_RAIL.key())
        .add(RailcraftItems.ADVANCED_RAIL.key())
        .add(RailcraftItems.REINFORCED_RAIL.key())
        .add(RailcraftItems.HIGH_SPEED_RAIL.key())
        .add(RailcraftItems.ELECTRIC_RAIL.key())
        .add(RailcraftItems.WOODEN_TIE.key())
        .add(RailcraftItems.STONE_TIE.key())
        .add(RailcraftItems.WOODEN_RAILBED.key())
        .add(RailcraftItems.STONE_RAILBED.key())
        .add(RailcraftItems.IRON_SPIKE_MAUL.key())
        .add(RailcraftItems.STEEL_SPIKE_MAUL.key())
        .add(RailcraftItems.DIAMOND_SPIKE_MAUL.key())
        .add(RailcraftItems.SWITCH_TRACK_LEVER.key())
        .add(RailcraftItems.SWITCH_TRACK_MOTOR.key())
        .add(RailcraftItems.SWITCH_TRACK_ROUTER.key())
        .add(RailcraftItems.CHARGE_METER.key())
        .add(RailcraftItems.WIRE.key())
        .add(RailcraftItems.WHISTLE_TUNER.key())
        .add(RailcraftItems.TICKET.key())
        .add(RailcraftItems.GOLDEN_TICKET.key())
        .add(RailcraftItems.OVERALLS.key())
        .add(RailcraftItems.CREOSOTE_BUCKET.key())
        .add(RailcraftItems.WORLD_SPIKE.key())
        .add(RailcraftItems.PERSONAL_WORLD_SPIKE.key())
        .add(RailcraftItems.ITEM_LOADER.key())
        .add(RailcraftItems.ITEM_UNLOADER.key())
        .add(RailcraftItems.ADVANCED_ITEM_LOADER.key())
        .add(RailcraftItems.ADVANCED_ITEM_UNLOADER.key())
        .add(RailcraftItems.FLUID_LOADER.key())
        .add(RailcraftItems.FLUID_UNLOADER.key())
        .add(RailcraftItems.CART_DISPENSER.key())
        .add(RailcraftItems.TRAIN_DISPENSER.key())
        .add(RailcraftItems.CARGO_MINECART.key())
        .add(RailcraftItems.TANK_MINECART.key())
        .add(RailcraftItems.ENERGY_MINECART.key())
        .add(RailcraftItems.VOID_CHEST_MINECART.key())
        .add(RailcraftItems.BED_MINECART.key())
        .add(RailcraftItems.WORLD_SPIKE_MINECART.key())
        .add(RailcraftItems.TRACK_LAYER.key())
        .add(RailcraftItems.TRACK_RELAYER.key())
        .add(RailcraftItems.TRACK_REMOVER.key())
        .add(RailcraftItems.TRACK_UNDERCUTTER.key())
        .add(RailcraftItems.TUNNEL_BORE.key())
        .add(RailcraftItems.STEAM_LOCOMOTIVE.key())
        .add(RailcraftItems.ELECTRIC_LOCOMOTIVE.key())
        .add(RailcraftItems.CREATIVE_LOCOMOTIVE.key());
    this.tag(RailcraftTags.Items.TRACKMAN_BACKPACK_REJECT);

    this.tag(RailcraftTags.Items.ICEMAN_BACKPACK_ALLOW)
        .add(Items.SNOWBALL.builtInRegistryHolder().key())
        .add(Items.SNOW.builtInRegistryHolder().key())
        .add(Items.SNOW_BLOCK.builtInRegistryHolder().key())
        .add(Items.ICE.builtInRegistryHolder().key())
        .add(Items.PACKED_ICE.builtInRegistryHolder().key())
        .add(Items.BLUE_ICE.builtInRegistryHolder().key());
    this.tag(RailcraftTags.Items.ICEMAN_BACKPACK_REJECT);
  }
}
