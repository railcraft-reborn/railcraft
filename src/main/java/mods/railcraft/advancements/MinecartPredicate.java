package mods.railcraft.advancements;

import javax.annotation.Nullable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mods.railcraft.api.carts.CartUtil;
import mods.railcraft.api.core.Ownable;
import mods.railcraft.util.JsonUtil;
import mods.railcraft.world.entity.vehicle.MinecartExtension;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

/**
 * A utility for testing carts or so.
 */
public final class MinecartPredicate {

  public static final MinecartPredicate ANY =
      new MinecartPredicate(null, null, null, null, null, null,
          MinMaxBounds.Doubles.ANY, EntityPredicate.ANY);

  @Nullable
  private final Boolean highSpeed;
  @Nullable
  private final Boolean launched;
  @Nullable
  private final Boolean onElevator;
  @Nullable
  private final Boolean derailed;
  @Nullable
  private final Boolean mountable;
  @Nullable
  private final Boolean checksOwner;

  private final MinMaxBounds.Doubles speed;
  private final EntityPredicate parent;

  public MinecartPredicate(@Nullable Boolean highSpeed, @Nullable Boolean launched,
      @Nullable Boolean elevator, @Nullable Boolean derailed, @Nullable Boolean mountable,
      @Nullable Boolean checkOwner, MinMaxBounds.Doubles speed, EntityPredicate parent) {
    this.highSpeed = highSpeed;
    this.launched = launched;
    this.onElevator = elevator;
    this.derailed = derailed;
    this.mountable = mountable;
    this.checksOwner = checkOwner;
    this.speed = speed;
    this.parent = parent;
  }

  public boolean test(ServerPlayer player, AbstractMinecart cart) {
    var extension = MinecartExtension.getOrThrow(cart);

    if (this.highSpeed != null && extension.isHighSpeed() != this.highSpeed) {
      return false;
    }
    if (this.launched != null && extension.isLaunched() != this.launched) {
      return false;
    }
    if (this.onElevator != null && extension.isOnElevator() != this.onElevator) {
      return false;
    }
    if (this.derailed != null && extension.isDerailed() != this.derailed) {
      return false;
    }
    if (this.mountable != null && extension.isMountable() != this.mountable) {
      return false;
    }
    if (this.checksOwner != null && cart instanceof Ownable
        && ((Ownable) cart).getOwner().map(owner -> !owner.equals(player.getGameProfile()))
            .orElse(false)) {
      return false;
    }
    if (!this.speed.matchesSqr(CartUtil.getCartSpeedUncappedSquared(cart.getDeltaMovement()))) {
      return false;
    }
    return this.parent.matches(player, cart);
  }

  private void addOptionalBoolean(JsonObject json, String name, @Nullable Boolean value) {
    if (value != null) {
      json.addProperty(name, value);
    }
  }

  public JsonElement serializeToJson() {
    JsonObject json = new JsonObject();
    this.addOptionalBoolean(json, "high_speed", this.highSpeed);
    this.addOptionalBoolean(json, "launched", this.launched);
    this.addOptionalBoolean(json, "on_elevator", this.onElevator);
    this.addOptionalBoolean(json, "derailed", this.derailed);
    this.addOptionalBoolean(json, "mountable", this.mountable);
    this.addOptionalBoolean(json, "check_owner", this.checksOwner);
    json.add("speed", this.speed.serializeToJson());
    json.add("parent", this.parent.serializeToJson());
    return json;
  }

  public static MinecartPredicate deserialize(@Nullable JsonObject element) {
    if (element == null || element.isJsonNull()
        || (element.isJsonObject() && element.getAsJsonObject().size() < 1)) {
      return MinecartPredicate.ANY;
    }

    Boolean highSpeed = JsonUtil.getAsBoolean(element, "high_speed").orElse(null);
    Boolean launched = JsonUtil.getAsBoolean(element, "launched").orElse(null);
    Boolean elevator = JsonUtil.getAsBoolean(element, "on_elevator").orElse(null);
    Boolean derail = JsonUtil.getAsBoolean(element, "derailed").orElse(null);
    Boolean canMount = JsonUtil.getAsBoolean(element, "canMount").orElse(null);
    Boolean checksOwner = JsonUtil.getAsBoolean(element, "check_owner").orElse(null);
    MinMaxBounds.Doubles speed = MinMaxBounds.Doubles.fromJson(element.get("speed"));
    EntityPredicate parent = EntityPredicate.fromJson(element.get("parent"));

    return new MinecartPredicate(highSpeed, launched, elevator, derail, canMount, checksOwner,
        speed,
        parent);
  }
}
