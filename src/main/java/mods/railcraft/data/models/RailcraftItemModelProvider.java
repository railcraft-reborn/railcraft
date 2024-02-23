package mods.railcraft.data.models;

import java.util.Objects;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class RailcraftItemModelProvider extends ItemModelProvider {

  public RailcraftItemModelProvider(PackOutput packOutput, ExistingFileHelper fileHelper) {
    super(packOutput, RailcraftConstants.ID, fileHelper);
  }

  private ItemModelBuilder basicCustomItem(Item item, String model) {
    var rl = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item));
    return withExistingParent(rl.getPath(), "item/" + model)
        .texture("layer0", modLoc("item/" + rl.getPath()));
  }

  private ItemModelBuilder basicHandheldItem(Item item) {
    return basicCustomItem(item, "handheld");
  }

  private ItemModelBuilder basicHandheldRodItem(Item item) {
    return basicCustomItem(item, "handheld_rod");
  }

  @Override
  protected void registerModels() {
    this.basicItem(RailcraftItems.SIGNAL_LABEL.get());
    this.basicItem(RailcraftItems.SIGNAL_LAMP.get());
    this.basicItem(RailcraftItems.TURBINE_ROTOR.get());
    this.basicItem(RailcraftItems.TURBINE_BLADE.get());
    this.basicItem(RailcraftItems.TURBINE_DISK.get());
    this.basicItem(RailcraftItems.IRON_TUNNEL_BORE_HEAD.get());
    this.basicItem(RailcraftItems.BRONZE_TUNNEL_BORE_HEAD.get());
    this.basicItem(RailcraftItems.STEEL_TUNNEL_BORE_HEAD.get());
    this.basicItem(RailcraftItems.DIAMOND_TUNNEL_BORE_HEAD.get());
    this.basicItem(RailcraftItems.TANK_MINECART.get());
    this.basicItem(RailcraftItems.WORLD_SPIKE_MINECART.get());
    this.basicItem(RailcraftItems.TRACK_LAYER.get());
    this.basicItem(RailcraftItems.TRACK_RELAYER.get());
    this.basicItem(RailcraftItems.TRACK_REMOVER.get());
    this.basicItem(RailcraftItems.TRACK_UNDERCUTTER.get());
    this.basicItem(RailcraftItems.TUNNEL_BORE.get());
    this.basicItem(RailcraftItems.SIGNAL_BLOCK_SURVEYOR.get());
    this.basicItem(RailcraftItems.SIGNAL_TUNER.get());
    this.basicItem(RailcraftItems.WHISTLE_TUNER.get());
    this.basicItem(RailcraftItems.GOGGLES.get());
    this.basicItem(RailcraftItems.OVERALLS.get());
    this.basicItem(RailcraftItems.STEEL_SHEARS.get());
    this.basicItem(RailcraftItems.STEEL_BOOTS.get());
    this.basicItem(RailcraftItems.STEEL_CHESTPLATE.get());
    this.basicItem(RailcraftItems.STEEL_LEGGINGS.get());
    this.basicItem(RailcraftItems.STEEL_HELMET.get());
    this.basicItem(RailcraftItems.CREOSOTE_BUCKET.get());
    this.basicItem(RailcraftItems.CREOSOTE_BOTTLE.get());
    this.basicItem(RailcraftItems.GOLDEN_TICKET.get());
    this.basicItem(RailcraftItems.TICKET.get());
    this.basicItem(RailcraftItems.ROUTING_TABLE_BOOK.get());
    this.basicItem(RailcraftItems.CONTROLLER_CIRCUIT.get());
    this.basicItem(RailcraftItems.RECEIVER_CIRCUIT.get());
    this.basicItem(RailcraftItems.SIGNAL_CIRCUIT.get());
    this.basicItem(RailcraftItems.RADIO_CIRCUIT.get());
    this.basicItem(RailcraftItems.BAG_OF_CEMENT.get());
    this.basicItem(RailcraftItems.WOODEN_RAILBED.get());
    this.basicItem(RailcraftItems.WOODEN_TIE.get());
    this.basicItem(RailcraftItems.STONE_RAILBED.get());
    this.basicItem(RailcraftItems.STONE_TIE.get());
    this.basicItem(RailcraftItems.COAL_COKE.get());
    this.basicItem(RailcraftItems.BRASS_INGOT.get());
    this.basicItem(RailcraftItems.BRASS_NUGGET.get());
    this.basicItem(RailcraftItems.BRONZE_INGOT.get());
    this.basicItem(RailcraftItems.BRONZE_NUGGET.get());
    this.basicItem(RailcraftItems.STEEL_INGOT.get());
    this.basicItem(RailcraftItems.STEEL_NUGGET.get());
    this.basicItem(RailcraftItems.TIN_INGOT.get());
    this.basicItem(RailcraftItems.TIN_NUGGET.get());
    this.basicItem(RailcraftItems.ZINC_INGOT.get());
    this.basicItem(RailcraftItems.ZINC_NUGGET.get());
    this.basicItem(RailcraftItems.NICKEL_INGOT.get());
    this.basicItem(RailcraftItems.NICKEL_NUGGET.get());
    this.basicItem(RailcraftItems.INVAR_INGOT.get());
    this.basicItem(RailcraftItems.INVAR_NUGGET.get());
    this.basicItem(RailcraftItems.LEAD_INGOT.get());
    this.basicItem(RailcraftItems.LEAD_NUGGET.get());
    this.basicItem(RailcraftItems.SILVER_INGOT.get());
    this.basicItem(RailcraftItems.SILVER_NUGGET.get());
    this.basicItem(RailcraftItems.TIN_RAW.get());
    this.basicItem(RailcraftItems.ZINC_RAW.get());
    this.basicItem(RailcraftItems.NICKEL_RAW.get());
    this.basicItem(RailcraftItems.SILVER_RAW.get());
    this.basicItem(RailcraftItems.LEAD_RAW.get());
    this.basicItem(RailcraftItems.STEEL_PLATE.get());
    this.basicItem(RailcraftItems.IRON_PLATE.get());
    this.basicItem(RailcraftItems.TIN_PLATE.get());
    this.basicItem(RailcraftItems.GOLD_PLATE.get());
    this.basicItem(RailcraftItems.LEAD_PLATE.get());
    this.basicItem(RailcraftItems.ZINC_PLATE.get());
    this.basicItem(RailcraftItems.BRASS_PLATE.get());
    this.basicItem(RailcraftItems.INVAR_PLATE.get());
    this.basicItem(RailcraftItems.BRONZE_PLATE.get());
    this.basicItem(RailcraftItems.COPPER_PLATE.get());
    this.basicItem(RailcraftItems.NICKEL_PLATE.get());
    this.basicItem(RailcraftItems.SILVER_PLATE.get());
    this.basicItem(RailcraftItems.BUSHING_GEAR.get());
    this.basicItem(RailcraftItems.STEEL_GEAR.get());
    this.basicItem(RailcraftItems.IRON_GEAR.get());
    this.basicItem(RailcraftItems.TIN_GEAR.get());
    this.basicItem(RailcraftItems.GOLD_GEAR.get());
    this.basicItem(RailcraftItems.LEAD_GEAR.get());
    this.basicItem(RailcraftItems.ZINC_GEAR.get());
    this.basicItem(RailcraftItems.BRASS_GEAR.get());
    this.basicItem(RailcraftItems.INVAR_GEAR.get());
    this.basicItem(RailcraftItems.BRONZE_GEAR.get());
    this.basicItem(RailcraftItems.COPPER_GEAR.get());
    this.basicItem(RailcraftItems.NICKEL_GEAR.get());
    this.basicItem(RailcraftItems.SILVER_GEAR.get());
    this.basicItem(RailcraftItems.STEEL_ELECTRODE.get());
    this.basicItem(RailcraftItems.IRON_ELECTRODE.get());
    this.basicItem(RailcraftItems.TIN_ELECTRODE.get());
    this.basicItem(RailcraftItems.GOLD_ELECTRODE.get());
    this.basicItem(RailcraftItems.LEAD_ELECTRODE.get());
    this.basicItem(RailcraftItems.ZINC_ELECTRODE.get());
    this.basicItem(RailcraftItems.BRASS_ELECTRODE.get());
    this.basicItem(RailcraftItems.INVAR_ELECTRODE.get());
    this.basicItem(RailcraftItems.BRONZE_ELECTRODE.get());
    this.basicItem(RailcraftItems.CARBON_ELECTRODE.get());
    this.basicItem(RailcraftItems.COPPER_ELECTRODE.get());
    this.basicItem(RailcraftItems.NICKEL_ELECTRODE.get());
    this.basicItem(RailcraftItems.SILVER_ELECTRODE.get());
    this.basicItem(RailcraftItems.SALTPETER_DUST.get());
    this.basicItem(RailcraftItems.COAL_DUST.get());
    this.basicItem(RailcraftItems.CHARCOAL_DUST.get());
    this.basicItem(RailcraftItems.SLAG.get());
    this.basicItem(RailcraftItems.ENDER_DUST.get());
    this.basicItem(RailcraftItems.SULFUR_DUST.get());
    this.basicItem(RailcraftItems.OBSIDIAN_DUST.get());
    this.basicItem(RailcraftItems.TRACK_PARTS.get());
    this.basicItem(RailcraftItems.TRANSITION_TRACK_KIT.get());
    this.basicItem(RailcraftItems.LOCKING_TRACK_KIT.get());
    this.basicItem(RailcraftItems.BUFFER_STOP_TRACK_KIT.get());
    this.basicItem(RailcraftItems.ACTIVATOR_TRACK_KIT.get());
    this.basicItem(RailcraftItems.BOOSTER_TRACK_KIT.get());
    this.basicItem(RailcraftItems.CONTROL_TRACK_KIT.get());
    this.basicItem(RailcraftItems.GATED_TRACK_KIT.get());
    this.basicItem(RailcraftItems.DETECTOR_TRACK_KIT.get());
    this.basicItem(RailcraftItems.COUPLER_TRACK_KIT.get());
    this.basicItem(RailcraftItems.EMBARKING_TRACK_KIT.get());
    this.basicItem(RailcraftItems.DISEMBARKING_TRACK_KIT.get());
    this.basicItem(RailcraftItems.DUMPING_TRACK_KIT.get());
    this.basicItem(RailcraftItems.LAUNCHER_TRACK_KIT.get());
    this.basicItem(RailcraftItems.ONE_WAY_TRACK_KIT.get());
    this.basicItem(RailcraftItems.WHISTLE_TRACK_KIT.get());
    this.basicItem(RailcraftItems.LOCOMOTIVE_TRACK_KIT.get());
    this.basicItem(RailcraftItems.THROTTLE_TRACK_KIT.get());
    this.basicItem(RailcraftItems.ROUTING_TRACK_KIT.get());
    this.basicItem(RailcraftItems.CHARGE_SPOOL_LARGE.get());
    this.basicItem(RailcraftItems.CHARGE_SPOOL_MEDIUM.get());
    this.basicItem(RailcraftItems.CHARGE_SPOOL_SMALL.get());
    this.basicItem(RailcraftItems.CHARGE_COIL.get());
    this.basicItem(RailcraftItems.CHARGE_TERMINAL.get());
    this.basicItem(RailcraftItems.CHARGE_MOTOR.get());
    this.basicItem(RailcraftItems.CHARGE_METER.get());

    this.basicHandheldItem(RailcraftItems.STEEL_SWORD.get());
    this.basicHandheldItem(RailcraftItems.STEEL_SHOVEL.get());
    this.basicHandheldItem(RailcraftItems.STEEL_PICKAXE.get());
    this.basicHandheldItem(RailcraftItems.STEEL_AXE.get());
    this.basicHandheldItem(RailcraftItems.STEEL_HOE.get());
    this.basicHandheldItem(RailcraftItems.DIAMOND_CROWBAR.get());
    this.basicHandheldItem(RailcraftItems.IRON_CROWBAR.get());
    this.basicHandheldItem(RailcraftItems.STEEL_CROWBAR.get());
    this.basicHandheldItem(RailcraftItems.SEASONS_CROWBAR.get());
    this.basicHandheldItem(RailcraftItems.IRON_SPIKE_MAUL.get());
    this.basicHandheldItem(RailcraftItems.STEEL_SPIKE_MAUL.get());
    this.basicHandheldItem(RailcraftItems.DIAMOND_SPIKE_MAUL.get());

    this.basicHandheldRodItem(RailcraftItems.WOODEN_RAIL.get());
    this.basicHandheldRodItem(RailcraftItems.STANDARD_RAIL.get());
    this.basicHandheldRodItem(RailcraftItems.ADVANCED_RAIL.get());
    this.basicHandheldRodItem(RailcraftItems.ELECTRIC_RAIL.get());
    this.basicHandheldRodItem(RailcraftItems.HIGH_SPEED_RAIL.get());
    this.basicHandheldRodItem(RailcraftItems.REINFORCED_RAIL.get());
    this.basicHandheldRodItem(RailcraftItems.REBAR.get());
  }
}
