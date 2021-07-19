package mods.railcraft.util;

import java.util.Objects;
import java.util.function.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.util.EntityPredicates;

/**
 * Created by CovertJaguar on 10/19/2018 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public enum RCEntitySelectors implements Predicate<Entity> {

  LIVING {
    private final Predicate<Entity> predicate =
        Predicates.and(Objects::nonNull, EntityPredicates.ENTITY_STILL_ALIVE,
            EntityPredicates.NO_SPECTATORS);

    @Override
    public boolean test(Entity entity) {
      return predicate.test(entity);
    }
  },
  KILLABLE {
    @Override
    public boolean test(Entity entity) {
      return LIVING.test(entity)
          && EntityPredicates.ATTACK_ALLOWED.test(entity)
          && !(entity.getVehicle() instanceof AbstractMinecartEntity)
          && entity instanceof LivingEntity
          && ((LivingEntity) entity).getMaxHealth() < 100;
    }
  },
  /**
   * Checks if an entity does not form a part of the core game mechanics, e.g. pre-generation of map
   * via command block minecarts.
   */
  NON_MECHANICAL {
    @Override
    public boolean test(Entity entity) {
      return !entity.onlyOpCanSetNbt();
    }
  }
}
