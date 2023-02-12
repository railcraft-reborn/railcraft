package mods.railcraft.client.gui.screen.inventory;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.Railcraft;
import mods.railcraft.Translations;
import mods.railcraft.client.gui.widget.button.MultiButton;
import mods.railcraft.world.inventory.SwitchTrackRoutingMenu;
import mods.railcraft.world.level.block.entity.LockableSwitchTrackActuatorBlockEntity;
import mods.railcraft.world.level.block.entity.SwitchTrackMotorBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SwitchTrackRoutingScreen extends RailcraftMenuScreen<SwitchTrackRoutingMenu> {

  private static final ResourceLocation BACKGROUND_TEXTURE =
      new ResourceLocation(Railcraft.ID, "textures/gui/container/routing.png");

  private static final Component ROUTING_TABLE =
      Component.translatable(Translations.Screen.ROUTING_TABLE_BOOK);

  private SwitchTrackMotorBlockEntity switchTrackRouting;

  private MultiButton<LockableSwitchTrackActuatorBlockEntity.Lock> lockButton;

  public SwitchTrackRoutingScreen(SwitchTrackRoutingMenu menu, Inventory inventory,
      Component title) {
    super(menu, inventory, title);
    this.imageHeight = 158;
    this.imageWidth = 176;
    this.inventoryLabelY = this.imageHeight - 94;
  }

  @Override
  public ResourceLocation getWidgetsTexture() {
    return BACKGROUND_TEXTURE;
  }

  @Override
  protected void init() {
    super.init();
  }

  @Override
  protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
    super.renderLabels(poseStack, mouseX, mouseY);
    this.font.draw(poseStack, ROUTING_TABLE, 64, 29, 4210752);
  }
}
