package mods.railcraft.advancements.criterion;

import javax.annotation.Nullable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mods.railcraft.Railcraft;
import mods.railcraft.api.carts.CartUtil;
import mods.railcraft.api.core.Ownable;
import mods.railcraft.util.JsonTools;
import mods.railcraft.world.entity.vehicle.CartConstants;
import mods.railcraft.world.level.block.track.behaivor.HighSpeedTools;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

/**
 * A utility for testing carts or so.
 */
public final class CartPredicate {

  public static final CartPredicate ANY = new CartPredicate(null, null, null, null, null, null,
      MinMaxBounds.Doubles.ANY, EntityPredicate.ANY);

  @Nullable
  private final Boolean highSpeed;
  @Nullable
  private final Boolean launched;
  @Nullable
  private final Boolean elevator;
  @Nullable
  private final Boolean derail;
  @Nullable
  private final Boolean canMount;
  @Nullable
  private final Boolean checksOwner;

  private final MinMaxBounds.Doubles speed;
  private final EntityPredicate parent;

  public CartPredicate(@Nullable Boolean highSpeed, @Nullable Boolean launched,
      @Nullable Boolean elevator, @Nullable Boolean derail, @Nullable Boolean canMount,
      @Nullable Boolean checkOwner, MinMaxBounds.Doubles speed, EntityPredicate parent) {
    this.highSpeed = highSpeed;
    this.launched = launched;
    this.elevator = elevator;
    this.derail = derail;
    this.canMount = canMount;
    this.checksOwner = checkOwner;
    this.speed = speed;
    this.parent = parent;
  }

  public boolean test(ServerPlayer player, AbstractMinecart cart) {
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
    if (element == null || element.isJsonNull()
        || (element.isJsonObject() && element.getAsJsonObject().size() < 1)) {
      return CartPredicate.ANY;
    }

    Boolean highSpeed = JsonTools.nullableBoolean(element, CartConstants.TAG_HIGH_SPEED);
    Boolean launched = JsonTools.nullableBoolean(element, CartConstants.TAG_LAUNCHED);
    Boolean elevator = JsonTools.nullableBoolean(element, CartConstants.TAG_ELEVATOR);
    Boolean derail = JsonTools.nullableBoolean(element, CartConstants.TAG_DERAIL);
    Boolean canMount = JsonTools.nullableBoolean(element, "canMount");
    Boolean checksOwner = JsonTools.nullableBoolean(element, "check_owner");
    MinMaxBounds.Doubles speed = MinMaxBounds.Doubles.fromJson(element.get("speed"));
    EntityPredicate parent = EntityPredicate.fromJson(element.get("parent"));

    return new CartPredicate(highSpeed, launched, elevator, derail, canMount, checksOwner, speed,
        parent);
  }
}
