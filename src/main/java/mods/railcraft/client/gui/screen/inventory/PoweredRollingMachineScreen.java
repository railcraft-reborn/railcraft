package mods.railcraft.client.gui.screen.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.Railcraft;
import mods.railcraft.world.inventory.PoweredRollingMachineMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class PoweredRollingMachineScreen extends AbstractContainerScreen<PoweredRollingMachineMenu> {

  private static final ResourceLocation BACKGROUND_TEXTURE =
      new ResourceLocation(Railcraft.ID, "textures/gui/container/powered_rolling_machine.png");

  public PoweredRollingMachineScreen(PoweredRollingMachineMenu menu, Inventory inventory,
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
    this.blit(poseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    //float progress = this.menu.rollingProgress();
    float progress = 0.3F;
    // 24*0.1, basically 10% of 24. Rounded for safety!
    this.blit(poseStack, this.leftPos + 89, this.topPos + 36, 176, 0,
        Math.round(24.00F * progress), 12);
  }
}
