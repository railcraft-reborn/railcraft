package mods.railcraft.advancements;

import java.util.Optional;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.core.Ownable;
import mods.railcraft.world.entity.vehicle.MinecartUtil;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;

public record MinecartPredicate(
    Optional<Boolean> highSpeed,
    Optional<Boolean> launched,
    Optional<Boolean> onElevator,
    Optional<Boolean> derailed,
    Optional<Boolean> mountable,
    Optional<Boolean> checksOwner,
    MinMaxBounds.Doubles speed,
    Optional<EntityPredicate> parent
) {

  public static final Codec<MinecartPredicate> CODEC = RecordCodecBuilder.create(
      instance -> instance.group(
          Codec.BOOL.optionalFieldOf("highSpeed")
              .forGetter(MinecartPredicate::highSpeed),
          Codec.BOOL.optionalFieldOf("launched")
              .forGetter(MinecartPredicate::launched),
          Codec.BOOL.optionalFieldOf("onElevator")
              .forGetter(MinecartPredicate::onElevator),
          Codec.BOOL.optionalFieldOf("derailed")
              .forGetter(MinecartPredicate::derailed),
          Codec.BOOL.optionalFieldOf("mountable")
              .forGetter(MinecartPredicate::mountable),
          Codec.BOOL.optionalFieldOf("checksOwner")
              .forGetter(MinecartPredicate::checksOwner),
          MinMaxBounds.Doubles.CODEC.optionalFieldOf("speed",
              MinMaxBounds.Doubles.ANY).forGetter(MinecartPredicate::speed),
          EntityPredicate.CODEC.optionalFieldOf("parent")
              .forGetter(MinecartPredicate::parent)
      ).apply(instance, MinecartPredicate::new));

  public boolean matches(ServerPlayer player, AbstractMinecart cart) {
    var rollingStock = RollingStock.getOrThrow(cart);

    if (this.highSpeed.isPresent() && rollingStock.isHighSpeed() != this.highSpeed.get()) {
      return false;
    }
    if (this.launched.isPresent() && rollingStock.isLaunched() != this.launched.get()) {
      return false;
    }
    if (this.onElevator.isPresent() && rollingStock.isOnElevator() != this.onElevator.get()) {
      return false;
    }
    if (this.derailed.isPresent() && rollingStock.isDerailed() != this.derailed.get()) {
      return false;
    }
    if (this.mountable.isPresent() && rollingStock.isMountable() != this.mountable.get()) {
      return false;
    }
    if (this.checksOwner.isPresent() && cart instanceof Ownable ownable
        && ownable.getOwner()
        .map(owner -> !owner.equals(player.getGameProfile())).orElse(false)) {
      return false;
    }
    if (!this.speed.matchesSqr(MinecartUtil.getCartSpeedUncappedSquared(cart.getDeltaMovement()))) {
      return false;
    }
    return this.parent.map(x -> x.matches(player, cart)).orElse(true);
  }
}
