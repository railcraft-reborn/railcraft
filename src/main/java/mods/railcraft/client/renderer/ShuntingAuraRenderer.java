package mods.railcraft.client.renderer;

import java.util.Collection;
import org.jspecify.annotations.Nullable;
import mods.railcraft.client.util.RenderUtil;
import mods.railcraft.network.to_client.LinkedCartsMessage;
import mods.railcraft.world.item.GogglesItem;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.gizmos.TextGizmo;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;

public class ShuntingAuraRenderer {

  @Nullable
  private Collection<LinkedCartsMessage.LinkedCart> linkedCarts;

  public void clearCarts() {
    this.linkedCarts = null;
  }

  public void setLinkedCarts(Collection<LinkedCartsMessage.LinkedCart> linkedCarts) {
    this.linkedCarts = linkedCarts;
  }

  public void render(float partialTick) {
    var player = Minecraft.getInstance().player;
    if (this.linkedCarts == null || player == null) {
      return;
    }
    var goggles = player.getItemBySlot(EquipmentSlot.HEAD);
    if (!goggles.is(RailcraftItems.GOGGLES.get()) || GogglesItem.getAura(goggles) != GogglesItem.Aura.SHUNTING) {
      return;
    }
    var level = player.level();
    for (var linkedCart : this.linkedCarts) {
      var entity = level.getEntity(linkedCart.entityId());
      if (!(entity instanceof AbstractMinecart cart) || linkedCart.trainId().isEmpty()) {
        continue;
      }
      int color = RenderUtil.replaceAlpha(linkedCart.trainId().hashCode(), 255);
      var position = cart.getPosition(partialTick);
      Gizmos.line(position, position.add(0, 2, 0), color);
      for (int id : new int[]{linkedCart.linkAId(), linkedCart.linkBId()}) {
        var other = level.getEntity(id);
        if (other != null) {
          Gizmos.line(other.getPosition(partialTick).add(0, 1.5, 0), position.add(0, 2, 0), color);
        }
      }
      if (SharedConstants.IS_RUNNING_IN_IDE) {
        Gizmos.billboardText(String.valueOf(cart.getId()), position.add(0, 2.2, 0),
            TextGizmo.Style.forColorAndCentered(0xFFFF0000));
      }
    }
  }
}
