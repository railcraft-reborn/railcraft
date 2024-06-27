package mods.railcraft.world.item;

import org.apache.commons.lang3.StringUtils;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.Translations;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.charge.ChargeBlock;
import mods.railcraft.api.charge.ChargeStorage;
import mods.railcraft.charge.ChargeCartStorageImpl;
import mods.railcraft.charge.ChargeNetworkImpl;
import mods.railcraft.charge.ChargeStorageBlockImpl;
import mods.railcraft.util.HumanReadableNumberFormatter;
import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

public class ChargeMeterItem extends Item {

  private static final int SECONDS_TO_RECORD = 5;

  public ChargeMeterItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    var level = context.getLevel();
    if (level.isClientSide) {
      return InteractionResult.PASS;
    }
    var pos = context.getClickedPos();
    var player = context.getPlayer();
    var returnValue = InteractionResult.PASS;
    var state = level.getBlockState(pos);
    if (state.getBlock() instanceof ChargeBlock chargeBlock) {
      var node = (ChargeNetworkImpl.ChargeNode) chargeBlock.getMeterAccess(Charge.distribution,
          state, (ServerLevel) level, pos);
      if (node != null && node.isValid() && !node.isGridNull()) {
        player.displayClientMessage(
            Component.translatable(Translations.ChargeMeter.START, SECONDS_TO_RECORD), false);
        node.startUsageRecording(SECONDS_TO_RECORD * SharedConstants.TICKS_PER_SECOND, avg -> {
          sendNetworkStat(player, node.getGrid());
          var battery = node.storage().orElse(null);
          if (battery == null) {
            sendNodeStat(player, avg, node);
          } else {
            sendProducerStat(player, battery, node);
          }
        });
        returnValue = InteractionResult.SUCCESS;
      }
    }
    return returnValue;
  }

  public static void sendCartStat(Player player, Component displayName,
      ChargeCartStorageImpl cartStorage) {
    player.displayClientMessage(CommonComponents.joinLines(
        displayName.copy().withStyle(ChatFormatting.BLUE),
        lineFormatter(Translations.ChargeMeter.CHARGE, cartStorage.getEnergyStored(), "FE"),
        lineFormatter(Translations.ChargeMeter.DRAW, cartStorage.getDraw(), "FE/t"),
        lineFormatter(Translations.ChargeMeter.LOSS, cartStorage.getLosses(), "FE/t")
    ), false);
  }

  private static void sendNetworkStat(Player player, ChargeNetworkImpl.ChargeGrid grid) {
    player.displayClientMessage(CommonComponents.joinLines(
        Component.translatable(Translations.ChargeMeter.NETWORK).withStyle(ChatFormatting.BLUE),
        lineFormatter(Translations.ChargeMeter.SIZE, grid.size(), ""),
        lineFormatter(Translations.ChargeMeter.CHARGE, grid.isInfinite() ? "INF" : grid.getCharge(), "FE"),
        lineFormatter(Translations.ChargeMeter.DRAW, grid.getAverageUsagePerTick(), "FE/t"),
        lineFormatter(Translations.ChargeMeter.MAX_DRAW, grid.getMaxDraw(), "FE/t"),
        lineFormatter(Translations.ChargeMeter.LOSS, grid.getLosses(), "FE/t"),
        lineFormatter(Translations.ChargeMeter.EFFICIENCY, grid.getEfficiency() * 100.0, "%")
    ), false);
  }

  private static void sendNodeStat(Player player, double avg, ChargeNetworkImpl.ChargeNode node) {
    player.displayClientMessage(CommonComponents.joinLines(
        Component.translatable(Translations.ChargeMeter.NODE).withStyle(ChatFormatting.BLUE),
        lineFormatter(Translations.ChargeMeter.DRAW, avg, "FE"),
        lineFormatter(Translations.ChargeMeter.LOSS, node.getChargeSpec().losses(), "FE/t")
    ), false);
  }

  private static void sendProducerStat(Player player, ChargeStorageBlockImpl battery,
      ChargeNetworkImpl.ChargeNode node) {
    var infiniteBattery = battery.getState() == ChargeStorage.State.INFINITE;
    var loss = node.getChargeSpec().losses() * RailcraftConfig.SERVER.lossMultiplier.get();
    player.displayClientMessage(CommonComponents.joinLines(
        Component.translatable(Translations.ChargeMeter.PRODUCER).withStyle(ChatFormatting.BLUE),
        lineFormatter(Translations.ChargeMeter.CHARGE, infiniteBattery ? "INF" : battery.getAvailableCharge(), "FE"),
        lineFormatter(Translations.ChargeMeter.MAX_DRAW, battery.getMaxDraw(), "FE/t"),
        lineFormatter(Translations.ChargeMeter.LOSS, loss, "FE/t"),
        lineFormatter(Translations.ChargeMeter.EFFICIENCY, battery.getEfficiency() * 100.0, "%")
    ), false);
  }

  private static Component lineFormatter(String translation, Object data, String unit) {
    if (data instanceof Double doubleArg)
      data = HumanReadableNumberFormatter.format(doubleArg);
    return Component.translatable(translation)
        .withStyle(ChatFormatting.GREEN)
        .append(Component.literal(" " + data + (StringUtils.isEmpty(unit) ? "" : " ") + unit)
            .withStyle(ChatFormatting.WHITE));
  }
}
