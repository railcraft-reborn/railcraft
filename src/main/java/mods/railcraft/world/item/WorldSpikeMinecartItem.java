package mods.railcraft.world.item;

import mods.railcraft.Translations;
import mods.railcraft.integrations.jei.JeiSearchable;
import mods.railcraft.world.entity.vehicle.WorldSpikeMinecart;
import net.minecraft.network.chat.Component;

public class WorldSpikeMinecartItem extends CartItem implements JeiSearchable {

  public WorldSpikeMinecartItem(Properties properties) {
    super(WorldSpikeMinecart::new, properties);
  }

  @Override
  public Component jeiDescription() {
    return Component.translatable(Translations.Jei.WORLD_SPIKE_MINECART);
  }
}
