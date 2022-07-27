package mods.railcraft.world.entity.vehicle;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import mods.railcraft.api.carts.Link;
import mods.railcraft.util.MathUtil;
import mods.railcraft.world.level.block.track.ElevatorTrackBlock;
import mods.railcraft.world.level.block.track.behaivor.HighSpeedTools;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.phys.Vec3;

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
  private int preventMountRemainingTicks;
  private int derailedRemainingTicks;

  private boolean explosionPending;
  private boolean highSpeed;

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
  public boolean isLaunched() {
    return this.launchState == LaunchState.LAUNCHED;
  }

  @Override
  public void launch() {
    this.launchState = LaunchState.LAUNCHING;
    this.minecart.setCanUseRail(false);
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
  public int getPreventMountRemainingTicks() {
    return this.preventMountRemainingTicks;
  }

  @Override
  public void setPreventMountRemainingTicks(int preventMountRemainingTicks) {
    this.preventMountRemainingTicks = preventMountRemainingTicks;
  }

  @Override
  public int getDerailedRemainingTicks() {
    return this.derailedRemainingTicks;
  }

  @Override
  public void setDerailedRemainingTicks(int derailedRemainingTicks) {
    this.derailedRemainingTicks = derailedRemainingTicks;
  }

  @Override
  public void primeExplosion() {
    this.explosionPending = true;
  }

  @Override
  public boolean isHighSpeed() {
    return this.highSpeed;
  }

  @Override
  public void setHighSpeed(boolean highSpeed) {
    this.highSpeed = highSpeed;
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
    tag.putInt("preventMountRemainingTicks", this.preventMountRemainingTicks);
    tag.putInt("derailedRemainingTicks", this.derailedRemainingTicks);
    tag.putBoolean("explosionPending", this.explosionPending);
    tag.putBoolean("highSpeed", this.highSpeed);

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
    this.preventMountRemainingTicks = tag.getInt("preventMountRemainingTicks");
    this.derailedRemainingTicks = tag.getInt("derailedRemainingTicks");
    this.explosionPending = tag.getBoolean("explosionPending");
    this.highSpeed = tag.getBoolean("highSpeed");

  }

  @Override
  public void tick() {
    if (!this.getLevel().isClientSide()) {
      this.adjustCart();

      if (this.preventMountRemainingTicks > 0) {
        this.preventMountRemainingTicks--;
      }

      if (this.elevatorRemainingTicks < ElevatorTrackBlock.ELEVATOR_TIMER) {
        this.minecart.setNoGravity(false);
      }

      if (this.elevatorRemainingTicks > 0) {
        this.elevatorRemainingTicks--;
      }

      if (this.derailedRemainingTicks > 0) {
        this.derailedRemainingTicks--;
      }

      if (this.explosionPending) {
        this.explosionPending = false;
        CartTools.explodeCart(this.getMinecart());
      }

      if (this.highSpeed) {
        if (CartTools.cartVelocityIsLessThan(this.getMinecart(), HighSpeedTools.SPEED_EXPLODE)) {
          this.highSpeed = false;
        } else if (launchState == LaunchState.LANDED) {
          HighSpeedTools.checkSafetyAndExplode(this.getLevel(), this.minecart.blockPosition(),
              this.getMinecart());
        }
      }

      // Fix flip
      var distance =
          MathUtil.getDistanceBetweenAngles(this.minecart.getYRot(), this.minecart.yRotO);
      var cutoff = 120F;
      if (distance < -cutoff || distance >= cutoff) {
        this.minecart.setYRot(this.minecart.getYRot() + 180.0F);
        this.minecart.flipped = !this.minecart.flipped;
        this.minecart.setYRot(this.minecart.getYRot() % 360.0F);
      }

      if (BaseRailBlock.isRail(this.getLevel(), this.minecart.blockPosition())) {
        this.minecart.fallDistance = 0;
        if (this.minecart.isVehicle()) {
          this.minecart.getPassengers().forEach(p -> p.fallDistance = 0);
        }
        if (this.launchState == LaunchState.LAUNCHED) {
          this.land();
        }
      } else if (this.launchState == LaunchState.LAUNCHING) {
        this.launchState = LaunchState.LAUNCHED;
        this.minecart.setCanUseRail(true);
      } else if (this.launchState == LaunchState.LAUNCHED && this.minecart.isOnGround()) {
        this.land();
      }

      Vec3 motion = this.minecart.getDeltaMovement();

      double motionX = Math.copySign(Math.min(Math.abs(motion.x()), 9.5), motion.x());
      double motionY = Math.copySign(Math.min(Math.abs(motion.y()), 9.5), motion.y());
      double motionZ = Math.copySign(Math.min(Math.abs(motion.z()), 9.5), motion.z());

      this.minecart.setDeltaMovement(motionX, motionY, motionZ);
    }
  }

  /**
   * Inspects the links and determines if any physics adjustments need to be made.
   */
  private void adjustCart() {
    if (this.isLaunched() || this.isOnElevator()) {
      return;
    }

    var linkedA = this.adjustLinkedCart(Link.FRONT);
    var linkedB = this.adjustLinkedCart(Link.BACK);
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
    if (linked && !this.isHighSpeed()) {
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

  private boolean adjustLinkedCart(Link linkType) {
    return this.getLinkedMinecart(linkType)
        .map(MinecartExtension::getOrThrow)
        .map(link -> {
          // sanity check to ensure links are consistent
          if (!LinkageManagerImpl.INSTANCE.areLinked(this.minecart, link.getMinecart())) {
            // TODO something should happen here
            // boolean success = lm.repairLink(cart, link);
          }
          if (!link.isLaunched() && !link.isOnElevator()) {
            LinkagePhysicsUtil.adjustVelocity(this.minecart, link.getMinecart(), linkType);
            // adjustCartFromHistory(cart, link);
            return true;
          }
          return false;
        })
        .orElse(false);
  }

  private void land() {
    this.launchState = LaunchState.LANDED;
    this.minecart.setMaxSpeedAirLateral(AbstractMinecart.DEFAULT_MAX_SPEED_AIR_LATERAL);
    this.minecart.setMaxSpeedAirVertical(AbstractMinecart.DEFAULT_MAX_SPEED_AIR_VERTICAL);
    this.minecart.setDragAir(AbstractMinecart.DEFAULT_AIR_DRAG);
  }
}
