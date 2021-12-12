package mods.railcraft.client.gui.screen.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.Railcraft;
import mods.railcraft.world.inventory.FeedStationMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class FeedStationScreen extends AbstractContainerScreen<FeedStationMenu> {

  private static final ResourceLocation BACKGROUND_TEXTURE =
      new ResourceLocation(Railcraft.ID, "textures/gui/container/single_slot.png");

  public FeedStationScreen(FeedStationMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title);
    this.imageHeight = 140;
    this.inventoryLabelY = this.imageHeight - 94;
  }

  @Override
  protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
    this.renderBackground(poseStack);
    RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
    final int x = this.leftPos;
    final int y = this.topPos;
    this.blit(poseStack, x, y, 0, 0, this.imageWidth, this.imageHeight);
  }
}
