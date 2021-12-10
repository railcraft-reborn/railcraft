
package mods.railcraft.network;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.network.NetworkEvent;

public class NetworkUtil {

  public static Entity getEntityOrSender(NetworkEvent.Context context, int entityId) {
    return getEntityOrSender(context, entityId, Entity.class);
  }

  public static <T extends Entity> T getEntityOrSender(NetworkEvent.Context context, int entityId,
      Class<T> clazz) {
    switch (context.getDirection().getReceptionSide()) {
      case CLIENT:
        return getEntity(context, entityId, clazz);
      case SERVER:
        if (clazz.isInstance(context.getSender())) {
          return clazz.cast(context.getSender());
        } else {
          throw new IllegalStateException("Sender is not instance of: " + clazz.getName());
        }
      default:
        throw new IllegalStateException("Invalid side");
    }
  }

  public static Entity getEntity(NetworkEvent.Context context, int entityId) {
    return getEntity(context, entityId, Entity.class);
  }

  public static <T> T getEntity(NetworkEvent.Context context, int entityId,
      Class<T> clazz) {
    return LogicalSidedProvider.CLIENTWORLD.get(context.getDirection().getReceptionSide())
        .map(level -> level.getEntity(entityId))
        .filter(clazz::isInstance)
        .map(clazz::cast)
        .orElseThrow(() -> new IllegalStateException(
            String.format("Entity with ID %s of type %s is absent from client level", entityId,
                clazz.getName())));
  }
}
