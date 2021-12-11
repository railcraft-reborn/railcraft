package mods.railcraft.client.gui.screen.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import mods.railcraft.Railcraft;
import mods.railcraft.world.item.crafting.CokeOvenMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

public class CokeOvenMenuScreen extends AbstractContainerScreen<CokeOvenMenu> {

  private static final ResourceLocation BACKGROUND_TEXTURE =
      new ResourceLocation(Railcraft.ID, "textures/gui/container/coke_oven.png");

  public CokeOvenMenuScreen(CokeOvenMenu menu, Inventory inventory,
      Component title) {
    super(menu, inventory, title);
  }

  @Override
  public void render(PoseStack poseStack, int x, int y, float partialTicks) {
    this.renderBackground(poseStack);
    super.render(poseStack, x, y, partialTicks);
    this.renderTooltip(poseStack, x, y);
  }

  @Override
  protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
    RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
    final int x = this.leftPos;
    final int y = this.topPos;
    // initial draw
    this.blit(poseStack, x, y, 0, 0, this.imageWidth, this.imageHeight);

    // prog bar
    float prog = this.menu.getBurnProgress();
    // 24*0.1, basicaly 10% of 24. Rounded for safety!
    // first xy is pos to place, second is asset loc, third is how much to place
    this.blit(poseStack, x + 34, y + 44, 176, 61, Math.round(22.00F * prog), 15);
    // FIRE DRAW
    if (this.menu.isActualyBurning()) {
      this.blit(poseStack, x + 17, y + 27, 176, 47, 14, 14);
    }

    // fluid draw
    // TODO: implement & cleanup railcraft gui code
    // float fluidProg = this.menu.tankFillProgress();
    // this.blit(matrixStack, x + 35, y + 44, 176, 61, Math.round(24.00F * fluidProg), 12);
  }
}
