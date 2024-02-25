package mods.railcraft.client.renderer;

import java.util.Collection;
import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.client.util.LineRenderer;
import mods.railcraft.network.play.LinkedCartsMessage;
import mods.railcraft.world.item.GogglesItem;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ShuntingAuraRenderer {

  private Collection<LinkedCartsMessage.LinkedCart> linkedCarts;

  public void clearCarts() {
    this.linkedCarts = null;
  }

  public void setLinkedCarts(Collection<LinkedCartsMessage.LinkedCart> linkedCarts) {
    this.linkedCarts = linkedCarts;
  }

  public void render(PoseStack poseStack, Camera mainCamera, float partialTick) {
    if (this.linkedCarts == null) {
      return;
    }

    var player = Minecraft.getInstance().player;
    var goggles = player.getItemBySlot(EquipmentSlot.HEAD);
    if (goggles.is(RailcraftItems.GOGGLES.get())) {
      var aura = GogglesItem.getAura(goggles);
      if (aura == GogglesItem.Aura.SHUNTING) {
        poseStack.pushPose();
        var projectedView = mainCamera.getPosition();
        poseStack.translate(-projectedView.x, -projectedView.y, -projectedView.z);

        var level = player.level();
        var bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        for (var linkedCart : this.linkedCarts) {
          var entity = level.getEntity(linkedCart.entityId());
          if (!(entity instanceof AbstractMinecart cart) || linkedCart.trainId() == null) {
            continue;
          }

          var renderer = LineRenderer.create(bufferSource);
          final int color = linkedCart.trainId().hashCode();
          final var cartPosition = cart.getPosition(partialTick);

          renderer.renderLine(poseStack, color, cartPosition, cartPosition.add(0, 2, 0));
          this.renderLink(level, cartPosition, linkedCart.linkAId(), color, partialTick, renderer, poseStack);
          this.renderLink(level, cartPosition, linkedCart.linkBId(), color, partialTick, renderer, poseStack);

          bufferSource.endBatch();
        }
        poseStack.popPose();
      }
    }
  }

  private void renderLink(Level level, Vec3 cartPosition, int cartId, int color, float partialTick, LineRenderer renderer, PoseStack poseStack) {
    if (cartId == -1) {
      return;
    }

    var cartA = level.getEntity(cartId);
    if (cartA == null) {
      return;
    }

    var cartAPosition = cartA.getPosition(partialTick);
    renderer.renderLine(poseStack, color, cartAPosition.add(0, 1.5f, 0), cartPosition.add(0, 2, 0));
  }
}
