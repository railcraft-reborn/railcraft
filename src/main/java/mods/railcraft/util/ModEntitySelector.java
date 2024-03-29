package mods.railcraft.util;

import java.util.Objects;
import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public enum ModEntitySelector implements Predicate<Entity> {

  LIVING {
    private final Predicate<Entity> predicate =
        Predicates.and(Objects::nonNull, EntitySelector.ENTITY_STILL_ALIVE,
            EntitySelector.NO_SPECTATORS);

    @Override
    public boolean test(Entity entity) {
      return predicate.test(entity);
    }
  },
  KILLABLE {
    @Override
    public boolean test(Entity entity) {
      return LIVING.test(entity)
          && entity.isAttackable()
          && !(entity.getVehicle() instanceof AbstractMinecart)
          && entity instanceof LivingEntity livingEntity
          && livingEntity.getMaxHealth() < 100;
    }
  },
  /**
   * Checks if an entity does not form a part of the core game mechanics,
   * e.g. pre-generation of map via command block minecarts.
   */
  NON_MECHANICAL {
    @Override
    public boolean test(Entity entity) {
      return !entity.onlyOpCanSetNbt();
    }
  }
}
