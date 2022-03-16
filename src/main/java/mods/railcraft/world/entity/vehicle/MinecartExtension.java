package mods.railcraft.world.entity.vehicle;

import java.util.Optional;
import mods.railcraft.Railcraft;
import mods.railcraft.api.carts.Link;
import mods.railcraft.api.carts.LinkageManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.INBTSerializable;

public interface MinecartExtension extends INBTSerializable<CompoundTag> {

  ResourceLocation KEY = new ResourceLocation(Railcraft.ID, "minecart_extension");

  Capability<MinecartExtension> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

  static MinecartExtension getOrThrow(AbstractMinecart minecart) {
    return minecart.getCapability(CAPABILITY)
        .orElseThrow(() -> new IllegalStateException("Missing minecart extension"));
  }

  static MinecartExtension create(AbstractMinecart minecart) {
    return new MinecartExtensionImpl(minecart);
  }

  void tick();

  Optional<AbstractMinecart> getLinkedMinecart(Link link);

  Optional<Link> getLink(AbstractMinecart minecart);

  default boolean isLinked(AbstractMinecart minecart) {
    return this.getLink(minecart).isPresent();
  }

  default boolean hasFreeLink(Link link) {
    return LinkageManager.hasLink(this.getMinecart(), link)
        && this.getLinkedMinecart(link).isEmpty();
  }

  default boolean hasFreeLink() {
    for (var link : Link.values()) {
      if (this.hasFreeLink(link)) {
        return true;
      }
    }
    return false;
  }

  void link(Link link, AbstractMinecart minecart);

  default boolean link(AbstractMinecart minecart) {
    for (var link : Link.values()) {
      if (this.hasFreeLink(link)) {
        this.link(link, minecart);
        return true;
      }
    }

    return false;
  }

  void removeLink(Link link);

  boolean isAutoLinkEnabled(Link link);

  void setAutoLinkEnabled(Link link, boolean autoLinkEnabled);

  default void setAutoLinkEnabled(boolean autoLinkEnabled) {
    for (var link : Link.values()) {
      this.setAutoLinkEnabled(link, autoLinkEnabled);
    }
  }

  boolean isLaunched();

  void launch();

  int getElevatorRemainingTicks();

  default boolean isOnElevator() {
    return this.getElevatorRemainingTicks() > 0;
  }

  void setElevatorRemainingTicks(int elevatorRemainingTicks);

  int getPreventMountRemainingTicks();

  default boolean isMountable() {
    return this.getPreventMountRemainingTicks() <= 0;
  }

  void setPreventMountRemainingTicks(int preventMountRemainingTicks);

  int getDerailedRemainingTicks();

  default boolean isDerailed() {
    return this.getDerailedRemainingTicks() > 0;
  }

  void setDerailedRemainingTicks(int derailedRemainingTicks);

  void primeExplosion();

  boolean isHighSpeed();

  void setHighSpeed(boolean highSpeed);

  AbstractMinecart getMinecart();

  default Level getLevel() {
    return this.getMinecart().getLevel();
  }
}
