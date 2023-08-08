package mods.railcraft.client.renderer;

import java.util.Collection;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mods.railcraft.network.play.LinkedCartsMessage;
import mods.railcraft.world.item.GogglesItem;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;

public class ShuntingAuraRenderer {

  private final Minecraft minecraft = Minecraft.getInstance();
  private Collection<LinkedCartsMessage.LinkedCart> linkedCarts;

  public void clearCarts() {
    this.linkedCarts = null;
  }

  public void setLinkedCarts(Collection<LinkedCartsMessage.LinkedCart> linkedCarts) {
    this.linkedCarts = linkedCarts;
  }

  public void render(float partialTick, PoseStack poseStack) {
    if (this.linkedCarts == null) {
      return;
    }

    final var player = this.minecraft.player;

    var goggles = player.getItemBySlot(EquipmentSlot.HEAD);
    if (goggles.getItem() == RailcraftItems.GOGGLES.get()) {
      var aura = GogglesItem.getAura(goggles);
      if (aura == GogglesItem.Aura.SHUNTING) {
        poseStack.pushPose();
        {
          var projectedView = this.minecraft.gameRenderer.getMainCamera().getPosition();
          poseStack.translate(-projectedView.x, -projectedView.y, -projectedView.z);

          for (var linkedCart : this.linkedCarts) {
            var entity = this.minecraft.level.getEntity(linkedCart.entityId());
            if (!(entity instanceof AbstractMinecart) || linkedCart.trainId() == null) {
              continue;
            }
            AbstractMinecart cart = (AbstractMinecart) entity;

            var consumer =
                this.minecraft.renderBuffers().bufferSource().getBuffer(RenderType.lines());
            var pose = poseStack.last();

            final int color = linkedCart.trainId().hashCode();
            float red = (float) (color >> 16 & 255) / 255.0F;
            float green = (float) (color >> 8 & 255) / 255.0F;
            float blue = (float) (color & 255) / 255.0F;

            final var cartPosition = cart.getPosition(partialTick);
            final float cartX = (float) cartPosition.x();
            final float cartY = (float) cartPosition.y();
            final float cartZ = (float) cartPosition.z();

            consumer
                .vertex(pose.pose(), cartX, cartY, cartZ)
                .color(red, green, blue, 1.0F)
                .normal(pose.normal(), 0.0F, 0.0F, 0.0F)
                .endVertex();

            consumer
                .vertex(pose.pose(), cartX, cartY + 2.0F, cartZ)
                .color(red, green, blue, 1.0F)
                .normal(pose.normal(), 0.0F, 0.0F, 0.0F)
                .endVertex();

            this.renderLink(this.minecraft.level, cartX, cartY, cartZ, linkedCart.linkAId(), red,
                green, blue, partialTick, consumer, pose);
            this.renderLink(this.minecraft.level, cartX, cartY, cartZ, linkedCart.linkBId(), red,
                green, blue, partialTick, consumer, pose);

            this.minecraft.renderBuffers().bufferSource().endBatch();
          }
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

    var cartA = this.minecraft.level.getEntity(cartId);
    if (cartA == null) {
      return;
    }

    var cartAPosition = cartA.getPosition(partialTick);
    consumer
        .vertex(pose.pose(), cartX, cartY + 2.0F, cartZ)
        .color(red, green, blue, 1.0F)
        .normal(pose.normal(), 0.0F, 0.0F, 0.0F)
        .endVertex();
    consumer
        .vertex(pose.pose(), (float) cartAPosition.x(), (float) cartAPosition.y() + 1.5F,
            (float) cartAPosition.z())
        .color(red, green, blue, 1.0F)
        .normal(pose.normal(), 0.0F, 0.0F, 0.0F)
        .endVertex();
  }
}
