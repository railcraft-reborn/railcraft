package mods.railcraft.client;

import org.lwjgl.glfw.GLFW;
import com.mojang.blaze3d.platform.InputConstants;
import mods.railcraft.Translations;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyConflictContext;

public class KeyBinding {

  public static final KeyMapping CHANGE_AURA_KEY =
      new KeyMapping(Translations.KeyBinding.CHANGE_AURA, KeyConflictContext.IN_GAME,
          InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G, Translations.KeyBinding.CATEGORY);
}
