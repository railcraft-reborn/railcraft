package mods.railcraft.network.to_server;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

/**
 * Checks every serverbound handler runs before it touches the world.
 */
final class PacketChecks {

  /**
   * Same buffer menus use for {@code stillValid}.
   */
  private static final double INTERACTION_BUFFER = 4.0;

  private PacketChecks() {}

  /**
   * Whether the player may change the block at the given position. The position has to be
   * loaded, so a packet never loads or generates a chunk, and within the player's reach.
   */
  static boolean canEditBlock(Player player, BlockPos pos) {
    return player.level().isLoaded(pos)
        && player.isWithinBlockInteractionRange(pos, INTERACTION_BUFFER);
  }

  /**
   * Whether the player may change the given entity, it has to be within the player's reach.
   */
  static boolean canEditEntity(Player player, Entity entity) {
    return player.isWithinEntityInteractionRange(entity, INTERACTION_BUFFER);
  }
}
