package mods.railcraft.client.renderer;

import java.util.UUID;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import mods.railcraft.network.play.LinkedCartsMessage;
import mods.railcraft.world.entity.cart.CartTools;
import mods.railcraft.world.item.GogglesItem;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Created by CovertJaguar on 5/16/2017 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class ShuntingAuraRenderer {

  private final Minecraft minecraft = Minecraft.getInstance();
  private LinkedCartsMessage.LinkedCart[] linkedCarts;

  public void clearCarts() {
    this.linkedCarts = null;
  }

  public void setLinkedCarts(LinkedCartsMessage.LinkedCart[] linkedCarts) {
    this.linkedCarts = linkedCarts;
  }

  public void render(float partialTicks, PoseStack poseStack) {
    if (this.linkedCarts == null) {
      return;
    }

    final var player = this.minecraft.player;

    var goggles = player.getItemBySlot(EquipmentSlot.HEAD);
    if (goggles.getItem() == RailcraftItems.GOGGLES.get()) {
      GogglesItem.Aura aura = GogglesItem.getAura(goggles);
      if (aura == GogglesItem.Aura.SHUNTING) {
        poseStack.pushPose();
        {
          Vec3 projectedView = this.minecraft.gameRenderer.getMainCamera().getPosition();
          poseStack.translate(-projectedView.x, -projectedView.y, -projectedView.z);

          for (var linkedCart : this.linkedCarts) {
            var entity = this.minecraft.level.getEntity(linkedCart.entityId());
            if (!(entity instanceof AbstractMinecart) || linkedCart.trainId() == null) {
              continue;
            }
            AbstractMinecart cart = (AbstractMinecart) entity;

            var consumer =
                this.minecraft.renderBuffers().bufferSource().getBuffer(RenderType.lines());
            var matrix = poseStack.last().pose();

            final int color = linkedCart.trainId().hashCode();
            float red = (float) (color >> 16 & 255) / 255.0F;
            float green = (float) (color >> 8 & 255) / 255.0F;
            float blue = (float) (color & 255) / 255.0F;

            final var cartPosition = cart.getPosition(partialTicks);
            final float cartX = (float) cartPosition.x();
            final float cartY = (float) cartPosition.y();
            final float cartZ = (float) cartPosition.z();

            consumer
                .vertex(matrix, cartX, cartY, cartZ)
                .color(red, green, blue, 1.0F)
                .endVertex();

            consumer
                .vertex(matrix, cartX, cartY + 2.0F, cartZ)
                .color(red, green, blue, 1.0F)
                .endVertex();

            renderLink(this.minecraft.level, cartX, cartY, cartZ, linkedCart.linkAId(), red,
                green,
                blue,
                partialTicks, consumer, matrix);
            renderLink(this.minecraft.level, cartX, cartY, cartZ, linkedCart.linkBId(), red,
                green,
                blue,
                partialTicks, consumer, matrix);

            this.minecraft.renderBuffers().bufferSource().endBatch();
          }
        }
        poseStack.popPose();
      }
    }
  }

  private static void renderLink(Level level, float cartX, float cartY, float cartZ, UUID cartId,
      float red, float green, float blue, float partialTicks, VertexConsumer consumer,
      Matrix4f matrix) {
    var cartA = CartTools.getCartFromUUID(level, cartId);
    if (cartA != null) {
      final var cartAPosition = cartA.getPosition(partialTicks);
      consumer
          .vertex(matrix, cartX, cartY + 2.0F, cartZ)
          .color(red, green, blue, 1.0F)
          .endVertex();
      consumer
          .vertex(matrix, (float) cartAPosition.x(), (float) cartAPosition.y() + 1.5F,
              (float) cartAPosition.z())
          .color(red, green, blue, 1.0F)
          .endVertex();
    }
  }
}
