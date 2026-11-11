package mods.railcraft.client;

import com.mojang.blaze3d.platform.InputConstants;
import mods.railcraft.Translations;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyConflictContext;

public enum KeyBinding {

  CHANGE_AURA(Translations.KeyBinding.CHANGE_AURA, InputConstants.KEY_G),
  REVERSE(Translations.KeyBinding.REVERSE, InputConstants.KEY_N),
  FASTER(Translations.KeyBinding.FASTER, InputConstants.KEY_PERIOD),
  SLOWER(Translations.KeyBinding.SLOWER, InputConstants.KEY_COMMA),
  MODE_CHANGE(Translations.KeyBinding.MODE, InputConstants.KEY_M),
  WHISTLE(Translations.KeyBinding.WHISTLE, InputConstants.KEY_B),
  SLEEP(Translations.KeyBinding.SLEEP, InputConstants.KEY_DOWN);

  private final KeyMapping keyMapping;

  KeyBinding(String name, int keycode) {
    this.keyMapping = new KeyMapping(name, KeyConflictContext.IN_GAME,
          InputConstants.Type.KEYBOARD, keycode, RailcraftKeyMappingCategory.DEFAULT);
  }

  public KeyMapping getKeyMapping() {
    return this.keyMapping;
  }

  public boolean consumeClick() {
    return this.keyMapping.consumeClick();
  }
}
