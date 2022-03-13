package mods.railcraft.world.entity.vehicle;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import mods.railcraft.Railcraft;
import mods.railcraft.world.level.block.track.behaivor.HighSpeedTools;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

class MinecartExtensionImpl implements MinecartExtension {

  private static final double LINK_DRAG = 0.95;

  private final AbstractMinecart minecart;

  private final BiMap<Link, AbstractMinecart> linkedMinecarts = HashBiMap.create(2);

  // Server only
  @Nullable
  private Map<Link, UUID> unresolvedLinkedMinecarts;

  private final Map<Link, Boolean> autoLinkEnabled = new EnumMap<>(Link.class);

  private LaunchState launchState = LaunchState.LANDED;
  private int elevatorRemainingTicks;

  public MinecartExtensionImpl(AbstractMinecart minecart) {
    this.minecart = minecart;
  }

  @Override
  public Optional<AbstractMinecart> getLinkedMinecart(Link link) {
    this.resolveLinks();
    return Optional.ofNullable(this.linkedMinecarts.get(link));
  }

  private void resolveLinks() {
    if (this.unresolvedLinkedMinecarts != null && this.getLevel() instanceof ServerLevel level) {
      this.unresolvedLinkedMinecarts.forEach((link, minecartId) -> {
        var entity = level.getEntity(minecartId);
        if (entity instanceof AbstractMinecart minecart) {
          this.linkedMinecarts.put(link, minecart);
        }
      });
      this.unresolvedLinkedMinecarts = null;
    }
  }

  @Override
  public Optional<Link> getLink(AbstractMinecart minecart) {
    return Optional.ofNullable(this.linkedMinecarts.inverse().get(minecart));
  }

  @Override
  public void link(Link link, AbstractMinecart minecart) {
    this.linkedMinecarts.put(link, minecart);
    this.setAutoLinkEnabled(link, false);
  }

  @Override
  public void removeLink(Link link) {
    this.linkedMinecarts.remove(link);
  }

  @Override
  public boolean isAutoLinkEnabled(Link link) {
    return this.autoLinkEnabled.get(link);
  }

  @Override
  public void setAutoLinkEnabled(Link link, boolean autoLinkEnabled) {
    this.autoLinkEnabled.put(link, autoLinkEnabled);
  }

  @Override
  public LaunchState getLaunchState() {
    return this.launchState;
  }

  @Override
  public void setLaunchState(LaunchState launchState) {
    this.launchState = launchState;
  }

  @Override
  public int getElevatorRemainingTicks() {
    return this.elevatorRemainingTicks;
  }

  @Override
  public void setElevatorRemainingTicks(int elevatorRemainingTicks) {
    this.elevatorRemainingTicks = elevatorRemainingTicks;
  }

  @Override
  public AbstractMinecart getMinecart() {
    return this.minecart;
  }

  @Override
  public CompoundTag serializeNBT() {
    var tag = new CompoundTag();

    var linkedMinecartsTag = this.linkedMinecarts.entrySet().stream()
        .map(entry -> {
          var entryTag = new CompoundTag();
          entryTag.putString("link", entry.getKey().getName());
          if (entry.getValue() != null) {
            entryTag.putUUID("minecartId", entry.getValue().getUUID());
          }
          return entryTag;
        })
        .collect(Collectors.toCollection(ListTag::new));
    tag.put("linkedMinecarts", linkedMinecartsTag);

    var autoLinkEnabledTag = this.autoLinkEnabled.entrySet().stream()
        .map(entry -> {
          var entryTag = new CompoundTag();
          entryTag.putString("link", entry.getKey().getName());
          entryTag.putBoolean("autoLinkEnabled", entry.getValue());
          return entryTag;
        })
        .collect(Collectors.toCollection(ListTag::new));
    tag.put("autoLinkEnabled", autoLinkEnabledTag);

    tag.putString("launchState", this.launchState.getName());
    tag.putInt("elevatorRemainingTicks", this.elevatorRemainingTicks);

    return tag;
  }

  @Override
  public void deserializeNBT(CompoundTag tag) {
    var linkedMinecartsTag = tag.getList("linkedMinecarts", Tag.TAG_COMPOUND);
    for (int i = 0; i < linkedMinecartsTag.size(); i++) {
      var entryTag = linkedMinecartsTag.getCompound(i);
      Link.getByName(entryTag.getString("link")).ifPresent(
          link -> {
            if (this.unresolvedLinkedMinecarts == null) {
              this.unresolvedLinkedMinecarts = new EnumMap<>(Link.class);
            }
            this.unresolvedLinkedMinecarts.put(link,
                entryTag.hasUUID("minecartId") ? entryTag.getUUID("minecartId") : null);
          });
    }

    var autoLinkEnabledTag = tag.getList("autoLinkEnabled", Tag.TAG_COMPOUND);
    for (int i = 0; i < autoLinkEnabledTag.size(); i++) {
      var entryTag = autoLinkEnabledTag.getCompound(i);
      Link.getByName(entryTag.getString("link")).ifPresent(
          link -> this.autoLinkEnabled.put(link, entryTag.getBoolean("autoLinkEnabled")));
    }

    this.launchState =
        LaunchState.getByName(tag.getString("launchState")).orElse(LaunchState.LANDED);
    this.elevatorRemainingTicks = tag.getInt("elevatorRemainingTicks");
  }

  @Override
  public void tick() {
    if (!this.getLevel().isClientSide()) {
      this.adjustCart();
      Railcraft.getInstance().getMinecartHandler().handleTick(this.minecart);
    }
  }

  /**
   * This function inspects the links and determines if any physics adjustments need to be made.
   *
   * @param cart AbstractMinecartEntity
   */
  private void adjustCart() {
    if (this.getLaunchState().isLaunched() || this.isOnElevator()) {
      return;
    }

    var linkedA = this.adjustLinkedCart(this.minecart, Link.FRONT);
    var linkedB = this.adjustLinkedCart(this.minecart, Link.BACK);
    var linked = linkedA || linkedB;

    // Centroid
    // List<BlockPos> points =
    // Train.streamCarts(cart).map(Entity::getPosition).collect(Collectors.toList());
    // Vec2D centroid = new Vec2D(MathTools.centroid(points));
    //
    // Vec2D cartPos = new Vec2D(cart);
    // Vec2D unit = Vec2D.unit(cartPos, centroid);
    //
    // double amount = 0.2;
    // double pushX = amount * unit.getX();
    // double pushZ = amount * unit.getY();
    //
    // pushX = limitForce(pushX);
    // pushZ = limitForce(pushZ);
    //
    // cart.motionX += pushX;
    // cart.motionZ += pushZ;

    // Drag
    if (linked && !HighSpeedTools.isTravellingHighSpeed(this.minecart)) {
      this.minecart.setDeltaMovement(
          this.minecart.getDeltaMovement().multiply(LINK_DRAG, 1.0D, LINK_DRAG));
    }

    // Speed & End Drag
    Train.get(this.minecart).ifPresent(train -> {
      if (train.isTrainEnd(this.minecart)) {
        train.refreshMaxSpeed();
        // if (linked && !(cart instanceof EntityLocomotive)) {
        // double drag = 0.97;
        // cart.motionX *= drag;
        // cart.motionZ *= drag;
        // }
      }
    });

  }

  private boolean adjustLinkedCart(AbstractMinecart cart, Link linkType) {
    return MinecartExtension.getOrThrow(cart).getLinkedMinecart(linkType)
        .map(MinecartExtension::getOrThrow)
        .map(link -> {
          // sanity check to ensure links are consistent
          if (!LinkageManagerImpl.INSTANCE.areLinked(cart, link.getMinecart())) {
            // TODO something should happen here
            // boolean success = lm.repairLink(cart, link);
          }
          if (!link.getLaunchState().isLaunched() && !link.isOnElevator()) {
            LinkagePhysicsUtil.adjustVelocity(cart, link.getMinecart(), linkType);
            // adjustCartFromHistory(cart, link);
            return true;
          }
          return false;
        })
        .orElse(false);
  }
}
