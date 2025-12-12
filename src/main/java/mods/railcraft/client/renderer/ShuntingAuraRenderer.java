package mods.railcraft.client.renderer;

import java.util.Collection;
import org.jspecify.annotations.Nullable;
import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.client.util.LineRenderer;
import mods.railcraft.client.util.RenderUtil;
import mods.railcraft.network.to_client.LinkedCartsMessage;
import mods.railcraft.world.item.GogglesItem;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ShuntingAuraRenderer {

  @Nullable
  private Collection<LinkedCartsMessage.LinkedCart> linkedCarts;

  public void clearCarts() {
    this.linkedCarts = null;
  }

  public void setLinkedCarts(Collection<LinkedCartsMessage.LinkedCart> linkedCarts) {
    this.linkedCarts = linkedCarts;
  }

  public void render(PoseStack poseStack, CameraRenderState cameraState, float partialTick) {
    if (this.linkedCarts == null) {
      return;
    }

    var player = Minecraft.getInstance().player;
    var goggles = player.getItemBySlot(EquipmentSlot.HEAD);
    if (goggles.is(RailcraftItems.GOGGLES.get())) {
      var aura = GogglesItem.getAura(goggles);
      if (aura == GogglesItem.Aura.SHUNTING) {
        poseStack.pushPose();
        var projectedView = cameraState.pos;
        poseStack.translate(-projectedView.x, -projectedView.y, -projectedView.z);

        var level = player.level();
        var bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        for (var linkedCart : this.linkedCarts) {
          var entity = level.getEntity(linkedCart.entityId());
          if (!(entity instanceof AbstractMinecart cart) || linkedCart.trainId().isEmpty()) {
            continue;
          }
          if (SharedConstants.IS_RUNNING_IN_IDE) {
            this.renderCartDebugName(cart, poseStack, bufferSource);
          }

          var renderer = LineRenderer.simple(bufferSource);
          final int color = RenderUtil.replaceAlpha(linkedCart.trainId().hashCode(), 255);
          final var cartPosition = cart.getPosition(partialTick);

          renderer.renderLine(poseStack, color, cartPosition, cartPosition.add(0, 2, 0));
          this.renderLink(level, cartPosition, linkedCart.linkAId(), color, partialTick, renderer,
              poseStack);
          this.renderLink(level, cartPosition, linkedCart.linkBId(), color, partialTick, renderer,
              poseStack);

          bufferSource.endBatch(RenderTypes.lines());
        }
        poseStack.popPose();
      }
    }
  }

  private void renderCartDebugName(AbstractMinecart minecart, PoseStack poseStack,
      MultiBufferSource bufferSource) {
    var text = String.valueOf(minecart.getId());
    var position = minecart.position();
    var font = Minecraft.getInstance().font;
    float length = (float)(-font.width(text) / 2);
    poseStack.pushPose();
    poseStack.translate(position.x, position.y + 2.2, position.z);
    poseStack.mulPose(Minecraft.getInstance().gameRenderer.getMainCamera().rotation());
    poseStack.scale(0.025F, -0.025F, 0.025F);
    var matrix4f = poseStack.last().pose();
    font.drawInBatch(text, length, 0, 0xFFFF0000, false, matrix4f, bufferSource,
        Font.DisplayMode.NORMAL, 0, 15728880);
    poseStack.popPose();
  }

  private void renderLink(Level level, Vec3 cartPosition, int cartId, int color, float partialTick,
      LineRenderer renderer, PoseStack poseStack) {
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
