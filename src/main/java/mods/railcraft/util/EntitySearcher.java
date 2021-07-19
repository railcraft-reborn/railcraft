package mods.railcraft.util;

import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;
import com.google.common.collect.ForwardingList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * The EntitySearcher is a utility class for searching for entities in the world.
 *
 * It is based on similar principles to newer Java APIs, such as Streams, such that a search request
 * is very nearly a grammatically correct sentence.
 *
 * Example:
 * {@code EntitySearcher.findMinecarts().around(pos).outTo(0.5).and(AbstractMinecartEntity::isBeingRidden).in(world)}
 *
 * This results in a flexible and robust design capable of being adapted to any use case. A much
 * superior solution to the old wall of utility functions, most of which were only ever used in one
 * place in the code.
 *
 * Created by CovertJaguar on 8/29/2016 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public final class EntitySearcher {

  public static SearchParameters<AbstractMinecartEntity> findMinecarts() {
    return new SearchParameters<>(AbstractMinecartEntity.class);
  }

  public static <T extends Entity> SearchParameters<T> find(Class<T> entityClass) {
    return new SearchParameters<>(entityClass);
  }

  public static SearchParameters<LivingEntity> findLiving() {
    return new SearchParameters<>(LivingEntity.class);
  }

  public static SearchParameters<Entity> find() {
    return new SearchParameters<>(Entity.class);
  }

  public static SearchParameters<ItemEntity> findItem() {
    return new SearchParameters<>(ItemEntity.class);
  }

  public static class SearchParameters<T extends Entity> {

    private final Class<T> entityClass;
    private AABBFactory box = AABBFactory.start();
    private Predicate<T> filter = RCEntitySelectors.LIVING::test;

    public SearchParameters(Class<T> entityClass) {
      this.entityClass = entityClass;
    }

    public SearchResult<T> in(World world) {
      if (box.isUndefined())
        throw new NullPointerException("Improperly defined EntitySearcher without a search box");
      return new SearchResult<>(
          world.getEntitiesOfClass(entityClass, box.build(), filter::test));
    }

    public SearchParameters<T> except(Entity entity) {
      this.filter = filter.and(e -> e != entity);
      return this;
    }

    public SearchParameters<T> around(AxisAlignedBB area) {
      box.fromAABB(area);
      return this;
    }

    public SearchParameters<T> around(AABBFactory factory) {
      box = factory;
      return this;
    }

    public SearchParameters<T> around(BlockPos pos) {
      box.createBoxForTileAt(pos);
      return this;
    }

    public SearchParameters<T> around(Entity entity) {
      box.fromAABB(entity.getBoundingBox());
      return this;
    }

    public SearchParameters<T> growFlat(double distance) {
      box.growFlat(distance);
      return this;
    }

    public SearchParameters<T> outTo(double distance) {
      box.grow(distance);
      return this;
    }

    public SearchParameters<T> upTo(double distance) {
      box.upTo(distance);
      return this;
    }

    @SafeVarargs
    public final SearchParameters<T> and(Predicate<? super T>... filters) {
      if (!ArrayUtils.isEmpty(filters))
        this.filter = Predicates.and(filter, filters);
      return this;
    }
  }

  public static class SearchResult<T extends Entity> extends ForwardingList<T> {
    private final List<T> entities;

    private SearchResult(List<T> entities) {
      this.entities = entities;
    }

    @Override
    protected List<T> delegate() {
      return entities;
    }

    public @Nullable T any() {
      return entities.stream().findAny().orElse(null);
    }
  }
}
