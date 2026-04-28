package mods.railcraft.client;

import org.lwjgl.glfw.GLFW;
import com.mojang.blaze3d.platform.InputConstants;
import mods.railcraft.Translations;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;

public enum KeyBinding {

  CHANGE_AURA(Translations.KeyBinding.CHANGE_AURA, GLFW.GLFW_KEY_G),
  REVERSE(Translations.KeyBinding.REVERSE, GLFW.GLFW_KEY_N),
  FASTER(Translations.KeyBinding.FASTER, GLFW.GLFW_KEY_PERIOD),
  SLOWER(Translations.KeyBinding.SLOWER, GLFW.GLFW_KEY_COMMA),
  MODE_CHANGE(Translations.KeyBinding.MODE, GLFW.GLFW_KEY_M),
  WHISTLE(Translations.KeyBinding.WHISTLE, GLFW.GLFW_KEY_B);

  private final KeyMapping keyMapping;

  KeyBinding(String name, int keycode) {
    this.keyMapping = new KeyMapping(name, KeyConflictContext.IN_GAME,
        InputConstants.Type.KEYSYM, keycode, Translations.KeyBinding.CATEGORY);
  }

  public KeyMapping getKeyMapping() {
    return this.keyMapping;
  }

  public boolean consumeClick() {
    return this.keyMapping.consumeClick();
  }
}
