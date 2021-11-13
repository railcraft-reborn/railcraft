package mods.railcraft.data;

import java.util.function.BiConsumer;
import java.util.function.Supplier;
import com.google.gson.JsonElement;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.data.ModelTextures;
import net.minecraft.data.ModelsResourceUtil;
import net.minecraft.data.ModelsUtil;
import net.minecraft.data.StockModelShapes;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class RailcraftItemModelProvider {

  private final BiConsumer<ResourceLocation, Supplier<JsonElement>> output;

  public RailcraftItemModelProvider(BiConsumer<ResourceLocation, Supplier<JsonElement>> output) {
    this.output = output;
  }

  private void generateFlatItem(Item item, ModelsUtil model) {
    model.create(ModelsResourceUtil.getModelLocation(item), ModelTextures.layer0(item),
        this.output);
  }

  public void run() {
    this.generateFlatItem(RailcraftItems.IRON_TUNNEL_BORE_HEAD.get(), StockModelShapes.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.BRONZE_TUNNEL_BORE_HEAD.get(), StockModelShapes.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.STEEL_TUNNEL_BORE_HEAD.get(), StockModelShapes.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.DIAMOND_TUNNEL_BORE_HEAD.get(),
        StockModelShapes.FLAT_ITEM);

    this.generateFlatItem(RailcraftItems.TANK_MINECART.get(), StockModelShapes.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.TRACK_LAYER.get(), StockModelShapes.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.TRACK_REMOVER.get(), StockModelShapes.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.TUNNEL_BORE.get(), StockModelShapes.FLAT_ITEM);

    this.generateFlatItem(RailcraftItems.SIGNAL_BLOCK_SURVEYOR.get(), StockModelShapes.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.SIGNAL_TUNER.get(), StockModelShapes.FLAT_ITEM);

    this.generateFlatItem(RailcraftItems.WHISTLE_TUNER.get(), StockModelShapes.FLAT_ITEM);

    this.generateFlatItem(RailcraftItems.GOGGLES.get(), StockModelShapes.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.OVERALLS.get(), StockModelShapes.FLAT_ITEM);

    this.generateFlatItem(RailcraftItems.TICKET.get(), StockModelShapes.FLAT_ITEM);

    this.generateFlatItem(RailcraftItems.CONTROLLER_CIRCUIT.get(), StockModelShapes.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.RECEIVER_CIRCUIT.get(), StockModelShapes.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.SIGNAL_CIRCUIT.get(), StockModelShapes.FLAT_ITEM);

    this.generateFlatItem(RailcraftItems.WOODEN_RAIL.get(),
        StockModelShapes.FLAT_HANDHELD_ROD_ITEM);
    this.generateFlatItem(RailcraftItems.STANDARD_RAIL.get(),
        StockModelShapes.FLAT_HANDHELD_ROD_ITEM);
    this.generateFlatItem(RailcraftItems.ADVANCED_RAIL.get(),
        StockModelShapes.FLAT_HANDHELD_ROD_ITEM);
    this.generateFlatItem(RailcraftItems.ELECTRIC_RAIL.get(),
        StockModelShapes.FLAT_HANDHELD_ROD_ITEM);
    this.generateFlatItem(RailcraftItems.HIGH_SPEED_RAIL.get(),
        StockModelShapes.FLAT_HANDHELD_ROD_ITEM);
    this.generateFlatItem(RailcraftItems.REINFORCED_RAIL.get(),
        StockModelShapes.FLAT_HANDHELD_ROD_ITEM);

    this.generateFlatItem(RailcraftItems.WOODEN_RAILBED.get(), StockModelShapes.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.WOODEN_TIE.get(), StockModelShapes.FLAT_ITEM);

    this.generateFlatItem(RailcraftItems.STONE_RAILBED.get(), StockModelShapes.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.STONE_TIE.get(), StockModelShapes.FLAT_ITEM);

    this.generateFlatItem(RailcraftItems.BRASS_INGOT.get(), StockModelShapes.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.BRASS_NUGGET.get(), StockModelShapes.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.BRONZE_INGOT.get(), StockModelShapes.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.BRONZE_NUGGET.get(), StockModelShapes.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.COPPER_INGOT.get(), StockModelShapes.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.COPPER_NUGGET.get(), StockModelShapes.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.STEEL_INGOT.get(), StockModelShapes.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.STEEL_NUGGET.get(), StockModelShapes.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.TIN_INGOT.get(), StockModelShapes.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.TIN_NUGGET.get(), StockModelShapes.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.ZINC_INGOT.get(), StockModelShapes.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.ZINC_NUGGET.get(), StockModelShapes.FLAT_ITEM);

    this.generateFlatItem(RailcraftItems.COAL_COKE.get(), StockModelShapes.FLAT_ITEM);

    this.generateFlatItem(RailcraftItems.REBAR.get(), StockModelShapes.FLAT_HANDHELD_ROD_ITEM);

    this.generateFlatItem(RailcraftItems.DIAMOND_CROWBAR.get(),
        StockModelShapes.FLAT_HANDHELD_ITEM);
    this.generateFlatItem(RailcraftItems.IRON_CROWBAR.get(), StockModelShapes.FLAT_HANDHELD_ITEM);
    this.generateFlatItem(RailcraftItems.STEEL_CROWBAR.get(), StockModelShapes.FLAT_HANDHELD_ITEM);
    this.generateFlatItem(RailcraftItems.SEASONS_CROWBAR.get(),
        StockModelShapes.FLAT_HANDHELD_ITEM);

    this.generateFlatItem(RailcraftItems.IRON_SPIKE_MAUL.get(),
        StockModelShapes.FLAT_HANDHELD_ITEM);
    this.generateFlatItem(RailcraftItems.STEEL_SPIKE_MAUL.get(),
        StockModelShapes.FLAT_HANDHELD_ITEM);
    this.generateFlatItem(RailcraftItems.DIAMOND_SPIKE_MAUL.get(),
        StockModelShapes.FLAT_HANDHELD_ITEM);

    this.generateFlatItem(RailcraftItems.TRANSITION_TRACK_KIT.get(), StockModelShapes.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.LOCKING_TRACK_KIT.get(), StockModelShapes.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.BUFFER_STOP_TRACK_KIT.get(), StockModelShapes.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.ACTIVATOR_TRACK_KIT.get(), StockModelShapes.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.BOOSTER_TRACK_KIT.get(), StockModelShapes.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.CONTROL_TRACK_KIT.get(), StockModelShapes.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.GATED_TRACK_KIT.get(), StockModelShapes.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.DETECTOR_TRACK_KIT.get(), StockModelShapes.FLAT_ITEM);

  }
}
