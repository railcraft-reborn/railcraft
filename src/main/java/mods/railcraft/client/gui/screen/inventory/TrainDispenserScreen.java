package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.gui.screen.IngameWindowScreen;
import mods.railcraft.world.inventory.TrainDispenserMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class TrainDispenserScreen extends RailcraftMenuScreen<TrainDispenserMenu> {

  private static final Identifier WIDGETS_TEXTURE_LOCATION =
      RailcraftConstants.id("textures/gui/container/train_dispenser.png");

  private final static Component PATTERN =
      Component.translatable(Translations.Screen.PATTERN);
  private final static Component BUFFER =
      Component.translatable(Translations.Screen.ITEM_MANIPULATOR_BUFFER);

  public TrainDispenserScreen(TrainDispenserMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title, 193);
    this.inventoryLabelY = this.imageHeight - 94;
  }

  @Override
  protected void extractLabels(GuiGraphicsExtractor graphics, int xm, int ym) {
    super.extractLabels(graphics, xm, ym);
    graphics.text(this.font, PATTERN, this.titleLabelX, 18,
        IngameWindowScreen.TEXT_COLOR, false);
    graphics.text(this.font, BUFFER, this.titleLabelX, 50,
        IngameWindowScreen.TEXT_COLOR, false);
  }

  @Override
  public Identifier getWidgetsTexture() {
    return WIDGETS_TEXTURE_LOCATION;
  }
}
