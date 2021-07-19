package mods.railcraft.advancements.criterion;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Tuple;

/**
 * Implements 3 methods to make life easier.
 */
public abstract class BaseTrigger<T extends ICriterionInstance> implements ICriterionTrigger<T> {

  protected final ListenerManager<T> manager = new ListenerManager<>();

  @Override
  public void addPlayerListener(PlayerAdvancements playerAdvancementsIn, Listener<T> listener) {
    manager.add(playerAdvancementsIn, listener);
  }

  @Override
  public void removePlayerListener(PlayerAdvancements playerAdvancementsIn, Listener<T> listener) {
    manager.remove(playerAdvancementsIn, listener);
  }

  @Override
  public void removePlayerListeners(PlayerAdvancements playerAdvancementsIn) {
    manager.remove(playerAdvancementsIn);
  }

  void trigger(ServerPlayerEntity player, Predicate<? super T> predicate) {
    PlayerAdvancements advancements = player.getAdvancements();
    Collection<Listener<T>> done = manager.get(advancements).parallelStream()
        .filter(listener -> predicate.test(listener.getTriggerInstance()))
        .collect(Collectors.toList());
    for (Listener<T> listener : done) {
      listener.run(advancements);
    }
  }

  void trigger(Predicate<? super T> predicate) {
    Collection<Tuple<PlayerAdvancements, Listener<T>>> done = manager.allStream()
        .filter(tuple -> predicate.test(tuple.getB().getTriggerInstance()))
        .collect(Collectors.toList());
    for (Tuple<PlayerAdvancements, Listener<T>> tuple : done) {
      tuple.getB().run(tuple.getA());
    }
  }
}
