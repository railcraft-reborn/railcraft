package mods.railcraft.advancements.criterion;

import javax.annotation.Nullable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mods.railcraft.Railcraft;
import mods.railcraft.api.carts.CartUtil;
import mods.railcraft.api.core.Ownable;
import mods.railcraft.util.JsonTools;
import mods.railcraft.world.entity.cart.CartConstants;
import mods.railcraft.world.level.block.track.behaivor.HighSpeedTools;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds.FloatBound;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

/**
 * A utility for testing carts or so.
 */
public final class CartPredicate {

  public static final CartPredicate ANY = new CartPredicate(null, null, null, null, null, null,
      FloatBound.ANY, EntityPredicate.ANY);

  @Nullable
  final Boolean highSpeed;
  @Nullable
  final Boolean launched;
  @Nullable
  final Boolean elevator;
  @Nullable
  final Boolean derail;
  @Nullable
  final Boolean canMount;
  @Nullable
  final Boolean checksOwner;

  final FloatBound speed;
  final EntityPredicate parent;

  public CartPredicate(@Nullable Boolean highSpeed, @Nullable Boolean launched,
      @Nullable Boolean elevator, @Nullable Boolean derail, @Nullable Boolean canMount,
      @Nullable Boolean checkOwner, FloatBound speed, EntityPredicate parent) {
    this.highSpeed = highSpeed;
    this.launched = launched;
    this.elevator = elevator;
    this.derail = derail;
    this.canMount = canMount;
    this.checksOwner = checkOwner;
    this.speed = speed;
    this.parent = parent;
  }

  public boolean test(ServerPlayerEntity player, AbstractMinecartEntity cart) {
    if (this.highSpeed != null && HighSpeedTools.isTravellingHighSpeed(cart) != this.highSpeed) {
      return false;
    }
    if (this.launched != null
        && Railcraft.getInstance().getLinkageHandler().isLaunched(cart) != this.launched) {
      return false;
    }
    if (this.elevator != null
        && Railcraft.getInstance().getLinkageHandler().isOnElevator(cart) != this.elevator) {
      return false;
    }
    if (this.derail != null
        && Railcraft.getInstance().getMinecartHandler().isDerailed(cart) != this.derail) {
      return false;
    }
    if (this.canMount != null
        && Railcraft.getInstance().getMinecartHandler().canMount(cart) != this.canMount) {
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
    this.addOptionalBoolean(json, CartConstants.TAG_HIGH_SPEED, this.highSpeed);
    this.addOptionalBoolean(json, CartConstants.TAG_LAUNCHED, this.launched);
    this.addOptionalBoolean(json, CartConstants.TAG_ELEVATOR, this.elevator);
    this.addOptionalBoolean(json, CartConstants.TAG_DERAIL, this.derail);
    this.addOptionalBoolean(json, "canMount", this.canMount);
    this.addOptionalBoolean(json, "check_owner", this.checksOwner);
    json.add("speed", this.speed.serializeToJson());
    json.add("parent", this.parent.serializeToJson());
    return json;
  }

  public static CartPredicate deserialize(@Nullable JsonObject element) {
    if (element == null || element.isJsonNull()) {
      return CartPredicate.ANY;
    }

    Boolean highSpeed = JsonTools.nullableBoolean(element, CartConstants.TAG_HIGH_SPEED);
    Boolean launched = JsonTools.nullableBoolean(element, CartConstants.TAG_LAUNCHED);
    Boolean elevator = JsonTools.nullableBoolean(element, CartConstants.TAG_ELEVATOR);
    Boolean derail = JsonTools.nullableBoolean(element, CartConstants.TAG_DERAIL);
    Boolean canMount = JsonTools.nullableBoolean(element, "canMount");
    Boolean checksOwner = JsonTools.nullableBoolean(element, "check_owner");
    FloatBound speed = FloatBound.fromJson(element.get("speed"));
    EntityPredicate parent = EntityPredicate.fromJson(element.get("parent"));

    return new CartPredicate(highSpeed, launched, elevator, derail, canMount, checksOwner, speed,
        parent);
  }
}
