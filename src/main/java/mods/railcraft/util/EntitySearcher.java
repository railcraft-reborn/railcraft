package mods.railcraft.util;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.apache.commons.lang3.ArrayUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;

/**
 * The EntitySearcher is a utility class for searching for entities in the world.
 * <p>
 * It is based on similar principles to newer Java APIs, such as Streams, such that a search request
 * is very nearly a grammatically correct sentence.
 * <p>
 * Example:
 * {@code EntitySearcher.findMinecarts().around(pos).outTo(0.5).and(AbstractMinecartEntity::isBeingRidden).in(world)}
 *
 * This results in a flexible and robust design capable of being adapted to any use case. A much
 * superior solution to the old wall of utility functions, most of which were only ever used in one
 * place in the code.
 */
public final class EntitySearcher {

  private static final EntityTypeTest<Entity, Entity> ANY_TYPE =
      new EntityTypeTest<>() {

        @Override
        public Entity tryCast(Entity entity) {
          return entity;
        }

        @Override
        public Class<? extends Entity> getBaseClass() {
          return Entity.class;
        }
      };

  public static SearchParameters<AbstractMinecart> findMinecarts() {
    return find(AbstractMinecart.class);
  }

  public static SearchParameters<LivingEntity> findLiving() {
    return find(LivingEntity.class);
  }

  public static SearchParameters<Entity> find() {
    return find(ANY_TYPE);
  }

  public static <T extends Entity> SearchParameters<T> find(Class<T> clazz) {
    return find(EntityTypeTest.forClass(clazz));
  }

  public static <T extends Entity> SearchParameters<T> find(EntityTypeTest<Entity, T> typeTest) {
    return new SearchParameters<>(typeTest);
  }

  public static class SearchParameters<T extends Entity> {

    private final EntityTypeTest<Entity, T> typeTest;
    private BoxBuilder box = BoxBuilder.create();
    private Predicate<T> filter = ModEntitySelector.LIVING::test;

    public SearchParameters(EntityTypeTest<Entity, T> typeTest) {
      this.typeTest = typeTest;
    }

    public Stream<T> stream(Level level) {
      return this.list(level).stream();
    }

    public List<T> list(Level level) {
      if (this.box.isUndefined()) {
        throw new NullPointerException("Improperly defined EntitySearcher without a search box");
      }
      return level.getEntities(this.typeTest, this.box.build(), this.filter);
    }

    public SearchParameters<T> except(Entity entity) {
      this.filter = this.filter.and(e -> e != entity);
      return this;
    }

    public SearchParameters<T> in(AABB aabb) {
      this.box.fromAABB(aabb);
      return this;
    }

    public SearchParameters<T> box(Consumer<BoxBuilder> consumer) {
      consumer.accept(this.box);
      return this;
    }

    public SearchParameters<T> at(BlockPos pos) {
      this.box.at(pos);
      return this;
    }

    public SearchParameters<T> around(Entity entity) {
      this.box.fromAABB(entity.getBoundingBox());
      return this;
    }

    public SearchParameters<T> inflateHorizontally(double distance) {
      this.box.inflateHorizontally(distance);
      return this;
    }

    public SearchParameters<T> inflate(double distance) {
      this.box.inflate(distance);
      return this;
    }

    public SearchParameters<T> upTo(double distance) {
      this.box.upTo(distance);
      return this;
    }

    @SafeVarargs
    public final SearchParameters<T> and(Predicate<? super T>... filters) {
      if (!ArrayUtils.isEmpty(filters)) {
        this.filter = Predicates.and(this.filter, filters);
      }
      return this;
    }
  }
}
