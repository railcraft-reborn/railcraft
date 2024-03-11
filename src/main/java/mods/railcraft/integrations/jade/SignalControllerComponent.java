package mods.railcraft.integrations.jade;

import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.level.block.entity.signal.SignalControllerBoxBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class SignalControllerComponent implements IBlockComponentProvider {

  @Override
  public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
    if (accessor.getBlockEntity() instanceof SignalControllerBoxBlockEntity signalController) {
      var aspect = signalController.getSignalAspect(null).getDisplayAspect();
      tooltip.add(Component.translatable(Translations.LookingAt.EMITTED_ASPECT)
          .append(aspect.getDisplayNameWithColor()));
    }
  }

  @Override
  public ResourceLocation getUid() {
    return RailcraftConstants.rl("signal_controller");
  }
}
