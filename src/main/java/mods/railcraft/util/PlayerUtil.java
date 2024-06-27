package mods.railcraft.util;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import com.mojang.authlib.GameProfile;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.api.item.ActivationBlockingItem;
import mods.railcraft.api.track.TrackUtil;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public final class PlayerUtil {

  public static Component getUsername(Level level, @NotNull GameProfile gameProfile) {
    if (StringUtils.isNotBlank(gameProfile.getName())) {
      return Component.literal(gameProfile.getName());
    }
    var playerId = gameProfile.getId();
    if (playerId != null) {
      var player = level.getPlayerByUUID(playerId);
      if (player != null) {
        return player.getDisplayName();
      }
    }
    return Component.literal(RailcraftConstants.UNKNOWN_PLAYER);
  }

  public static boolean isOwnerOrOp(GameProfile owner, Player player) {
    return player.getGameProfile().equals(owner) || player.hasPermissions(Commands.LEVEL_GAMEMASTERS);
  }

  public static boolean isSamePlayer(GameProfile a, GameProfile b) {
    if (a.isComplete() && b.isComplete())
      return a.equals(b);
    if (a.getId() != null && b.getId() != null)
      return a.getId().equals(b.getId());
    return a.getName() != null && a.getName().equals(b.getName());
  }

  public static boolean doesItemBlockActivation(Player player, InteractionHand hand) {
    if (player.isShiftKeyDown() || hand == InteractionHand.OFF_HAND)
      return true;
    var heldItem = player.getItemInHand(hand);
    if (!heldItem.isEmpty()) {
      return TrackUtil.isRail(heldItem)
          || ReflectionUtil.isAnnotatedDeepSearch(ActivationBlockingItem.class, heldItem.getItem());
    }
    return false;
  }
}
