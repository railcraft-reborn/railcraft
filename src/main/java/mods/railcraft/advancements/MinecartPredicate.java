package mods.railcraft.advancements;

import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.core.Ownable;
import mods.railcraft.world.entity.vehicle.MinecartUtil;
import net.minecraft.Util;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

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
          ExtraCodecs.strictOptionalField(Codec.BOOL, "highSpeed")
              .forGetter(MinecartPredicate::highSpeed),
          ExtraCodecs.strictOptionalField(Codec.BOOL, "launched")
              .forGetter(MinecartPredicate::launched),
          ExtraCodecs.strictOptionalField(Codec.BOOL, "onElevator")
              .forGetter(MinecartPredicate::onElevator),
          ExtraCodecs.strictOptionalField(Codec.BOOL, "derailed")
              .forGetter(MinecartPredicate::derailed),
          ExtraCodecs.strictOptionalField(Codec.BOOL, "mountable")
              .forGetter(MinecartPredicate::mountable),
          ExtraCodecs.strictOptionalField(Codec.BOOL, "checksOwner")
              .forGetter(MinecartPredicate::checksOwner),
          ExtraCodecs.strictOptionalField(MinMaxBounds.Doubles.CODEC, "speed",
              MinMaxBounds.Doubles.ANY).forGetter(MinecartPredicate::speed),
          ExtraCodecs.strictOptionalField(EntityPredicate.CODEC, "parent")
              .forGetter(MinecartPredicate::parent)
      ).apply(instance, MinecartPredicate::new));

  public static Optional<MinecartPredicate> fromJson(@Nullable JsonElement jsonElement) {
    return jsonElement != null && !jsonElement.isJsonNull()
        ? Optional.of(Util.getOrThrow(
            CODEC.parse(JsonOps.INSTANCE, jsonElement), JsonParseException::new))
        : Optional.empty();
  }

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

  public JsonElement serializeToJson() {
    return Util.getOrThrow(CODEC.encodeStart(JsonOps.INSTANCE, this), IllegalStateException::new);
  }
}
