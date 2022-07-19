package mods.railcraft.gui.widget;

import mods.railcraft.RailcraftConfig;
import mods.railcraft.world.level.material.fluid.tank.StandardTank;
import mods.railcraft.world.module.WaterCollectionModule;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WaterCollectionGaugeWidget extends FluidGaugeWidget {

  private final Collection<WaterCollectionModule> modules;

  private List<Component> tooltip;

  public WaterCollectionGaugeWidget(Collection<WaterCollectionModule> modules, StandardTank tank,
      int x, int y, int u, int v, int w, int h) {
    super(tank, x, y, u, v, w, h);
    this.modules = modules;
  }

  @Override
  public List<Component> getTooltip() {
    return this.tooltip;
  }

  private void refresh() {
    var count = 0;
    var temperaturePenalty = 0.0D;
    var humidityMultiplier = 0.0D;
    var precipitationMultiplier = 0.0D;

    for (var module : this.modules) {
      var state = module.getState();
      if (state.skyVisible()) {
        count++;
        temperaturePenalty += state.temperaturePenalty();
        humidityMultiplier += state.humidityMultiplier();
        precipitationMultiplier += state.precipitationMultiplier();
      }
    }

    if (count > 0) {
      humidityMultiplier /= count;
      precipitationMultiplier /= count;
    }

    this.tooltip = new ArrayList<>();

    var baseRate = RailcraftConfig.server.waterCollectionRate.get();

    this.tooltip.addAll(this.tank.getTooltip());
    this.tooltip.add(Component.empty());
    this.tooltip.add(Component.translatable("gui.railcraft.tank.sky", count).withStyle(
        ChatFormatting.DARK_GRAY));
    this.tooltip.add(Component.translatable("gui.railcraft.tank.base",
        baseRate * count)
            .withStyle(ChatFormatting.DARK_GRAY));
    this.tooltip.add(Component.translatable("gui.railcraft.tank.humidity", humidityMultiplier)
        .withStyle(ChatFormatting.DARK_GRAY));
    this.tooltip
        .add(Component.translatable("gui.railcraft.tank.precipitation",
            precipitationMultiplier).withStyle(ChatFormatting.DARK_GRAY));
    this.tooltip
        .add(Component.translatable("gui.railcraft.tank.temp", -temperaturePenalty).withStyle(
            ChatFormatting.DARK_GRAY));
    this.tooltip.add(Component.translatable("gui.railcraft.tank.final",
        (baseRate
            * count
            * humidityMultiplier
            * precipitationMultiplier)
            - temperaturePenalty).withStyle(
                ChatFormatting.DARK_GRAY));
  }

  @Override
  public void writeToBuf(ServerPlayer player, FriendlyByteBuf out) {
    super.writeToBuf(player, out);
    this.refresh();
    out.writeVarInt(this.tooltip.size());
    this.tooltip.forEach(out::writeComponent);
  }

  @Override
  public void readFromBuf(FriendlyByteBuf in) {
    super.readFromBuf(in);
    this.tooltip = new ArrayList<>();
    var size = in.readVarInt();
    for (int i = 0; i < size; i++) {
      this.tooltip.add(in.readComponent());
    }
  }
}
