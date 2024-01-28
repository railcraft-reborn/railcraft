package mods.railcraft.client.renderer;

import java.util.Collection;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mods.railcraft.client.util.RenderUtil;
import mods.railcraft.network.to_client.LinkedCartsMessage;
import mods.railcraft.world.item.GogglesItem;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;

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

        for (var linkedCart : this.linkedCarts) {
          var entity = player.level().getEntity(linkedCart.entityId());
          if (!(entity instanceof AbstractMinecart cart) || linkedCart.trainId() == null) {
            continue;
          }

          var consumer =
              Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.lines());
          var pose = poseStack.last();

          final int color = linkedCart.trainId().hashCode();
          float red = RenderUtil.getRed(color);
          float green = RenderUtil.getGreen(color);
          float blue = RenderUtil.getBlue(color);

          final var cartPosition = cart.getPosition(partialTick);
          final float cartX = (float) cartPosition.x();
          final float cartY = (float) cartPosition.y();
          final float cartZ = (float) cartPosition.z();

          consumer
              .vertex(pose.pose(), cartX, cartY, cartZ)
              .color(red, green, blue, 1)
              .normal(pose.normal(), 0, 0, 0)
              .endVertex();

          consumer
              .vertex(pose.pose(), cartX, cartY + 2, cartZ)
              .color(red, green, blue, 1)
              .normal(pose.normal(), 0, 0, 0)
              .endVertex();

          this.renderLink(player.level(), cartX, cartY, cartZ, linkedCart.linkAId(), red,
              green, blue, partialTick, consumer, pose);
          this.renderLink(player.level(), cartX, cartY, cartZ, linkedCart.linkBId(), red,
              green, blue, partialTick, consumer, pose);

          Minecraft.getInstance().renderBuffers().bufferSource().endBatch();
        }
        poseStack.popPose();
      }
    }
  }

  private void renderLink(Level level, float cartX, float cartY, float cartZ, int cartId,
      float red, float green, float blue, float partialTick, VertexConsumer consumer,
      PoseStack.Pose pose) {
    if (cartId == -1) {
      return;
    }

    var cartA = level.getEntity(cartId);
    if (cartA == null) {
      return;
    }

    var cartAPosition = cartA.getPosition(partialTick);
    consumer
        .vertex(pose.pose(), cartX, cartY + 2, cartZ)
        .color(red, green, blue, 1)
        .normal(pose.normal(), 0, 0, 0)
        .endVertex();
    consumer
        .vertex(pose.pose(), (float) cartAPosition.x(), (float) cartAPosition.y() + 1.5F,
            (float) cartAPosition.z())
        .color(red, green, blue, 1)
        .normal(pose.normal(), 0, 0, 0)
        .endVertex();
  }
}
