package mods.railcraft.client.renderer;

import java.util.UUID;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import mods.railcraft.network.play.LinkedCartsMessage;
import mods.railcraft.world.entity.cart.CartTools;
import mods.railcraft.world.item.GogglesItem;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

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

  public void render(float partialTicks, MatrixStack matrixStack) {
    if (this.linkedCarts == null) {
      return;
    }

    final PlayerEntity player = minecraft.player;

    ItemStack goggles = player.getItemBySlot(EquipmentSlotType.HEAD);
    if (goggles.getItem() == RailcraftItems.GOGGLES.get()) {
      GogglesItem.Aura aura = GogglesItem.getAura(goggles);
      if (aura == GogglesItem.Aura.SHUNTING) {
        matrixStack.pushPose();
        {
          Vector3d projectedView = this.minecraft.gameRenderer.getMainCamera().getPosition();
          matrixStack.translate(-projectedView.x, -projectedView.y, -projectedView.z);

          for (LinkedCartsMessage.LinkedCart linkedCart : this.linkedCarts) {
            Entity entity = this.minecraft.level.getEntity(linkedCart.getEntityId());
            if (!(entity instanceof AbstractMinecartEntity) || linkedCart.getTrainId() == null) {
              continue;
            }
            AbstractMinecartEntity cart = (AbstractMinecartEntity) entity;

            IVertexBuilder builder =
                this.minecraft.renderBuffers().bufferSource().getBuffer(RenderType.lines());
            Matrix4f matrix = matrixStack.last().pose();

            final int color = linkedCart.getTrainId().hashCode();
            float red = (float) (color >> 16 & 255) / 255.0F;
            float green = (float) (color >> 8 & 255) / 255.0F;
            float blue = (float) (color & 255) / 255.0F;

            final Vector3d cartPosition = cart.getPosition(partialTicks);
            final float cartX = (float) cartPosition.x();
            final float cartY = (float) cartPosition.y();
            final float cartZ = (float) cartPosition.z();

            builder
                .vertex(matrix, cartX, cartY, cartZ)
                .color(red, green, blue, 1.0F)
                .endVertex();

            builder
                .vertex(matrix, cartX, cartY + 2.0F, cartZ)
                .color(red, green, blue, 1.0F)
                .endVertex();

            renderLink(this.minecraft.level, cartX, cartY, cartZ, linkedCart.getLinkAId(), red,
                green,
                blue,
                partialTicks, builder, matrix);
            renderLink(this.minecraft.level, cartX, cartY, cartZ, linkedCart.getLinkBId(), red,
                green,
                blue,
                partialTicks, builder, matrix);

            this.minecraft.renderBuffers().bufferSource().endBatch();
          }
        }
        matrixStack.popPose();
      }
    }
  }

  private static void renderLink(World level, float cartX, float cartY, float cartZ, UUID cartId,
      float red, float green, float blue, float partialTicks, IVertexBuilder builder,
      Matrix4f matrix) {
    AbstractMinecartEntity cartA = CartTools.getCartFromUUID(level, cartId);
    if (cartA != null) {
      final Vector3d cartAPosition = cartA.getPosition(partialTicks);
      builder
          .vertex(matrix, cartX, cartY + 2.0F, cartZ)
          .color(red, green, blue, 1.0F)
          .endVertex();
      builder
          .vertex(matrix, (float) cartAPosition.x(), (float) cartAPosition.y() + 1.5F,
              (float) cartAPosition.z())
          .color(red, green, blue, 1.0F)
          .endVertex();
    }
  }
}
