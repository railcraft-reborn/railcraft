package mods.railcraft.world.item;

import mods.railcraft.RailcraftConfig;
import mods.railcraft.Translations;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.charge.ChargeBlock;
import mods.railcraft.api.charge.ChargeStorage;
import mods.railcraft.charge.ChargeNetworkImpl;
import mods.railcraft.util.HumanReadableNumberFormatter;
import net.minecraft.SharedConstants;
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
        sendChat(player, Translations.ChargeMeter.START, SECONDS_TO_RECORD);
        node.startUsageRecording(SECONDS_TO_RECORD * SharedConstants.TICKS_PER_SECOND, avg -> {
          var grid = node.getGrid();
          sendChat(player, Translations.ChargeMeter.NETWORK, grid.size(),
              grid.isInfinite() ? "INF" : grid.getCharge(), grid.getAverageUsagePerTick(),
              grid.getMaxDraw(), grid.getLosses(), grid.getEfficiency() * 100.0);


          var battery = node.storage().orElse(null);
          if (battery == null) {
            sendChat(player, Translations.ChargeMeter.NODE, avg, node.getChargeSpec().losses());
          } else {
            var infiniteBattery = battery.getState() == ChargeStorage.State.INFINITE;
            sendChat(player, Translations.ChargeMeter.PRODUCER,
                infiniteBattery ? "INF" : battery.getAvailableCharge(),
                infiniteBattery ? "INF" : "NA",
                battery.getMaxDraw(),
                node.getChargeSpec().losses() * RailcraftConfig.SERVER.lossMultiplier.get(),
                battery.getEfficiency() * 100.0);
          }
        });
        returnValue = InteractionResult.SUCCESS;
      }
    }
    return returnValue;
  }

  public static void sendChat(Player player, String translation, Object... args) {
    for (int i = 0; i < args.length; i++) {
      if (args[i] instanceof Double doubleArg)
        args[i] = HumanReadableNumberFormatter.format(doubleArg);
    }
    player.displayClientMessage(Component.translatable(translation, args), false);
  }
}
