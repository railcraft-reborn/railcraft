package mods.railcraft.client.gui.screen.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import mods.railcraft.Railcraft;
import mods.railcraft.world.item.crafting.ManualRollingMachineMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

public class ManualRollingMachineScreen extends AbstractContainerScreen<ManualRollingMachineMenu> {

  private static final ResourceLocation BACKGROUND_TEXTURE =
      new ResourceLocation(Railcraft.ID, "textures/gui/container/manual_rolling_machine.png");

  public ManualRollingMachineScreen(ManualRollingMachineMenu menu, Inventory inventory,
      Component title) {
    super(menu, inventory, title);
  }

  @Override
  public void render(PoseStack matrixStack, int x, int y, float partialTicks) {
    this.renderBackground(matrixStack);
    super.render(matrixStack, x, y, partialTicks);
    this.renderTooltip(matrixStack, x, y);
  }

  @Override
  protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
    RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
    final int x = this.leftPos;
    final int y = this.topPos;
    // initial draw
    this.blit(matrixStack, x, y, 0, 0, this.imageWidth, this.imageHeight);
    // prog bar
    float prog = this.menu.rollingProgress();
    // 24*0.1, basicaly 10% of 24. Rounded for safety!
    this.blit(matrixStack, x + 89, y + 47, 176, 0, Math.round(24.00F * prog), 12);
  }
}
