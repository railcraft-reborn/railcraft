package mods.railcraft.data.models;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;

public class RailcraftItemModelProvider extends ModelProvider {

  public RailcraftItemModelProvider(PackOutput packOutput) {
    super(packOutput, RailcraftConstants.ID);
  }

  private void basicHandheldItem(ItemModelGenerators itemModels, Item item) {
    itemModels.generateFlatItem(item, ModelTemplates.FLAT_HANDHELD_ITEM);
  }

  private void basicHandheldRodItem(ItemModelGenerators itemModels, Item item) {
    itemModels.generateFlatItem(item, ModelTemplates.FLAT_HANDHELD_ROD_ITEM);
  }

  private void locomotiveItem(ItemModelGenerators itemModels, Item item) {
    var rl = BuiltInRegistries.ITEM.getKey(item);
    var path = rl.getPath().replace("creative", "electric");
    itemModels.generateLayeredItem(item, modLocation("item/%s_layer0".formatted(path)),
        modLocation("item/%s_layer1".formatted(path)));
  }

  @Override
  protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
    itemModels.generateFlatItem(RailcraftItems.SIGNAL_LABEL.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.SIGNAL_LAMP.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.TURBINE_ROTOR.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.TURBINE_BLADE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.TURBINE_DISK.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.IRON_TUNNEL_BORE_HEAD.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.BRONZE_TUNNEL_BORE_HEAD.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.STEEL_TUNNEL_BORE_HEAD.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.DIAMOND_TUNNEL_BORE_HEAD.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.TANK_MINECART.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.ENERGY_MINECART.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.WORLD_SPIKE_MINECART.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.TRACK_LAYER.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.TRACK_RELAYER.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.TRACK_REMOVER.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.TRACK_UNDERCUTTER.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.TUNNEL_BORE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.SIGNAL_BLOCK_SURVEYOR.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.SIGNAL_TUNER.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.WHISTLE_TUNER.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.GOGGLES.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.OVERALLS.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.STEEL_SHEARS.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.STEEL_BOOTS.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.STEEL_CHESTPLATE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.STEEL_LEGGINGS.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.STEEL_HELMET.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.CREOSOTE_BUCKET.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.CREOSOTE_BOTTLE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.GOLDEN_TICKET.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.TICKET.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.ROUTING_TABLE_BOOK.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.CONTROLLER_CIRCUIT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.RECEIVER_CIRCUIT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.SIGNAL_CIRCUIT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.RADIO_CIRCUIT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.BAG_OF_CEMENT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.WOODEN_RAILBED.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.WOODEN_TIE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.STONE_RAILBED.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.STONE_TIE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.COAL_COKE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.BRASS_INGOT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.BRASS_NUGGET.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.BRONZE_INGOT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.BRONZE_NUGGET.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.STEEL_INGOT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.STEEL_NUGGET.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.TIN_INGOT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.TIN_NUGGET.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.ZINC_INGOT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.ZINC_NUGGET.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.NICKEL_INGOT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.NICKEL_NUGGET.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.INVAR_INGOT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.INVAR_NUGGET.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.LEAD_INGOT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.LEAD_NUGGET.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.SILVER_INGOT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.SILVER_NUGGET.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.TIN_RAW.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.ZINC_RAW.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.NICKEL_RAW.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.SILVER_RAW.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.LEAD_RAW.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.STEEL_PLATE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.IRON_PLATE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.TIN_PLATE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.GOLD_PLATE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.LEAD_PLATE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.ZINC_PLATE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.BRASS_PLATE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.INVAR_PLATE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.BRONZE_PLATE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.COPPER_PLATE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.NICKEL_PLATE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.SILVER_PLATE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.BUSHING_GEAR.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.STEEL_GEAR.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.IRON_GEAR.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.TIN_GEAR.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.GOLD_GEAR.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.LEAD_GEAR.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.ZINC_GEAR.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.BRASS_GEAR.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.INVAR_GEAR.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.BRONZE_GEAR.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.COPPER_GEAR.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.NICKEL_GEAR.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.SILVER_GEAR.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.STEEL_ELECTRODE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.IRON_ELECTRODE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.TIN_ELECTRODE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.GOLD_ELECTRODE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.LEAD_ELECTRODE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.ZINC_ELECTRODE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.BRASS_ELECTRODE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.INVAR_ELECTRODE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.BRONZE_ELECTRODE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.CARBON_ELECTRODE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.COPPER_ELECTRODE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.NICKEL_ELECTRODE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.SILVER_ELECTRODE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.SALTPETER_DUST.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.COAL_DUST.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.CHARCOAL_DUST.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.SLAG.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.ENDER_DUST.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.SULFUR_DUST.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.OBSIDIAN_DUST.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.TRACK_PARTS.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.TRANSITION_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.LOCKING_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.BUFFER_STOP_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.ACTIVATOR_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.BOOSTER_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.CONTROL_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.GATED_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.DETECTOR_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.COUPLER_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.EMBARKING_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.DISEMBARKING_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.DUMPING_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.LAUNCHER_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.ONE_WAY_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.WHISTLE_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.LOCOMOTIVE_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.THROTTLE_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.ROUTING_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.CHARGE_SPOOL_LARGE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.CHARGE_SPOOL_MEDIUM.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.CHARGE_SPOOL_SMALL.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.CHARGE_COIL.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.CHARGE_TERMINAL.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.CHARGE_MOTOR.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(RailcraftItems.CHARGE_METER.get(), ModelTemplates.FLAT_ITEM);

    this.basicHandheldItem(itemModels, RailcraftItems.STEEL_SWORD.get());
    this.basicHandheldItem(itemModels, RailcraftItems.STEEL_SHOVEL.get());
    this.basicHandheldItem(itemModels, RailcraftItems.STEEL_PICKAXE.get());
    this.basicHandheldItem(itemModels, RailcraftItems.STEEL_AXE.get());
    this.basicHandheldItem(itemModels, RailcraftItems.STEEL_HOE.get());
    this.basicHandheldItem(itemModels, RailcraftItems.DIAMOND_CROWBAR.get());
    this.basicHandheldItem(itemModels, RailcraftItems.IRON_CROWBAR.get());
    this.basicHandheldItem(itemModels, RailcraftItems.STEEL_CROWBAR.get());
    this.basicHandheldItem(itemModels, RailcraftItems.SEASONS_CROWBAR.get());
    this.basicHandheldItem(itemModels, RailcraftItems.IRON_SPIKE_MAUL.get());
    this.basicHandheldItem(itemModels, RailcraftItems.STEEL_SPIKE_MAUL.get());
    this.basicHandheldItem(itemModels, RailcraftItems.DIAMOND_SPIKE_MAUL.get());

    this.basicHandheldRodItem(itemModels, RailcraftItems.WOODEN_RAIL.get());
    this.basicHandheldRodItem(itemModels, RailcraftItems.STANDARD_RAIL.get());
    this.basicHandheldRodItem(itemModels, RailcraftItems.ADVANCED_RAIL.get());
    this.basicHandheldRodItem(itemModels, RailcraftItems.ELECTRIC_RAIL.get());
    this.basicHandheldRodItem(itemModels, RailcraftItems.HIGH_SPEED_RAIL.get());
    this.basicHandheldRodItem(itemModels, RailcraftItems.REINFORCED_RAIL.get());
    this.basicHandheldRodItem(itemModels, RailcraftItems.REBAR.get());

    this.locomotiveItem(itemModels, RailcraftItems.STEAM_LOCOMOTIVE.get());
    this.locomotiveItem(itemModels, RailcraftItems.ELECTRIC_LOCOMOTIVE.get());
    this.locomotiveItem(itemModels, RailcraftItems.CREATIVE_LOCOMOTIVE.get());
  }
}
