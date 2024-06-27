package mods.railcraft.integrations.jei;

import java.util.ArrayList;
import java.util.List;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import mezz.jei.api.ingredients.ITypedIngredient;
import mods.railcraft.client.gui.screen.inventory.RailcraftMenuScreen;
import mods.railcraft.network.NetworkChannel;
import mods.railcraft.network.play.SetFilterSlotMessage;
import mods.railcraft.world.inventory.slot.RailcraftSlot;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.item.ItemStack;

public class GhostIngredientHandler<T extends RailcraftMenuScreen<?>>
    implements IGhostIngredientHandler<T> {

  @Override
  public <I> List<Target<I>> getTargetsTyped(T gui, ITypedIngredient<I> ingredient,
      boolean doStart) {
    var targets = new ArrayList<Target<I>>();

    for (var slot : gui.getMenu().slots) {
      if (!slot.isActive()) {
        continue;
      }

      var bounds = new Rect2i(gui.getGuiLeft() + slot.x, gui.getGuiTop() + slot.y, 17, 17);

      if (ingredient.getIngredient() instanceof ItemStack itemStack) {
        if ((slot instanceof RailcraftSlot railcraftSlot && railcraftSlot.isPhantom())) {
          if (railcraftSlot.mayPlace(itemStack)) {
            targets.add(new Target<>() {
              @Override
              public Rect2i getArea() {
                return bounds;
              }

              @Override
              public void accept(I ingredient) {
                var itemStack = ((ItemStack) ingredient).copy();
                NetworkChannel.GAME.sendToServer(new SetFilterSlotMessage(slot.index, itemStack));
                slot.set(itemStack);
              }
            });
          }
        }
      }
    }
    return targets;
  }

  @Override
  public void onComplete() {
  }
}
