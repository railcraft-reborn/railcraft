package mods.railcraft.integrations.jade;

import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.level.block.entity.signal.SignalControllerBoxBlockEntity;
import mods.railcraft.world.level.block.entity.signal.SignalReceiverBoxBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

class SignalComponent implements IBlockComponentProvider {

  static final SignalComponent INSTANCE = new SignalComponent();

  private SignalComponent() {
  }

  @Override
  public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
    var blockEntity = accessor.getBlockEntity();
    if (blockEntity instanceof SignalControllerBoxBlockEntity signalController) {
      var aspect = signalController.getSignalAspect(null).getDisplayAspect();
      tooltip.add(Component.translatable(Translations.LookingAt.ASPECT_SENT)
          .append(aspect.getDisplayNameWithColor()));
    } else if (blockEntity instanceof SignalReceiverBoxBlockEntity signalReceiver) {
      var aspect = signalReceiver.getSignalAspect(null).getDisplayAspect();
      tooltip.add(Component.translatable(Translations.LookingAt.ASPECT_RECEIVED)
          .append(aspect.getDisplayNameWithColor()));
    }
  }

  @Override
  public ResourceLocation getUid() {
    return RailcraftConstants.rl("signals");
  }
}
