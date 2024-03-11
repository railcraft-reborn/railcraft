package mods.railcraft.integrations.jade;

import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class LocomotiveComponent implements IEntityComponentProvider {

  @Override
  public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
    var entity = accessor.getEntity();
    if (entity instanceof Locomotive locomotive) {
      tooltip.add(Component.translatable(Translations.LookingAt.MODE)
          .append(locomotive.getMode().getDisplayName()));
      tooltip.add(
          Component.translatable(Translations.LookingAt.SPEED, locomotive.getSpeed().getLevel()));
      var reverse = locomotive.isReverse() ? Translations.LookingAt.YES : Translations.LookingAt.NO;
      tooltip.add(Component.translatable(Translations.LookingAt.REVERSE)
          .append(Component.translatable(reverse)));
    }
  }

  @Override
  public ResourceLocation getUid() {
    return RailcraftConstants.rl("locomotive");
  }
}
