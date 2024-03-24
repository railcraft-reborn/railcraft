package mods.railcraft.advancements;

import java.util.Optional;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

/**
 * TODO: Implament this on carts.
 */
public class CartRidingTrigger extends SimpleCriterionTrigger<CartRidingTrigger.TriggerInstance> {

  // private static final int FREQUENCY = 20;

  // private final Map<ServerPlayerEntity, AbstractMinecartEntity> mounting =
  // new MapMaker().weakKeys().weakValues().makeMap();

  // private int counter;

  // CartRidingTrigger() {
  // MinecraftForge.EVENT_BUS.register(this);
  // }

  /**
   * Invoked when the user rides a cart.
   */
  public void trigger(ServerPlayer playerEntity, AbstractMinecart cart) {
    this.trigger(playerEntity, criterionInstance -> criterionInstance.matches(playerEntity, cart));
  }

  @Override
  public Codec<TriggerInstance> codec() {
    return TriggerInstance.CODEC;
  }

  // @SubscribeEvent
  // public void onMount(EntityMountEvent event) {
  // if (!(event.getEntityMounting() instanceof ServerPlayerEntity)
  // || !(event.getEntityBeingMounted() instanceof AbstractMinecartEntity)) {
  // return;
  // }

  // ServerPlayerEntity rider = (ServerPlayerEntity) event.getEntityMounting();
  // AbstractMinecartEntity cart = (AbstractMinecartEntity) event.getEntityBeingMounted();

  // if (event.isMounting()) {
  // mounting.put(rider, cart);
  // } else {
  // mounting.remove(rider);
  // }
  // }

  // @SubscribeEvent
  // public void tick(ServerTickEvent event) {
  // if (counter != FREQUENCY) {
  // counter++;
  // return;
  // }
  // counter = 0;

  // for (Entry<ServerPlayerEntity, AbstractMinecartEntity> entry : mounting.entrySet()) {
  // trigger(entry.getKey(), instance -> instance.test(entry.getKey(), entry.getValue()));
  // }
  // }

  public record TriggerInstance(
      Optional<ContextAwarePredicate> player,
      Optional<MinecartPredicate> cart) implements SimpleCriterionTrigger.SimpleInstance {

    public static final Codec<TriggerInstance> CODEC =
        RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player")
                .forGetter(TriggerInstance::player),
            ExtraCodecs.strictOptionalField(MinecartPredicate.CODEC, "cart")
                .forGetter(TriggerInstance::cart)
        ).apply(instance, TriggerInstance::new));

    public static TriggerInstance hasRidden() {
      return new TriggerInstance(Optional.empty(), Optional.empty());
    }

    public boolean matches(ServerPlayer player, AbstractMinecart cart) {
      return this.cart.map(x -> x.matches(player, cart)).orElse(true);
    }

    @Override
    public Optional<ContextAwarePredicate> player() {
      return player;
    }
  }
}
