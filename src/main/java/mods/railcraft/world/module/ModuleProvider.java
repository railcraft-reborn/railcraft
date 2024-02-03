package mods.railcraft.world.module;

import java.util.Optional;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * An abstracted level entity. This could represent an {@link Entity} or a {@link BlockEntity}.
 *
 * @author Sm0keySa1m0n
 */
public interface ModuleProvider {

  <T extends Module> Optional<T> getModule(Class<T> type);

  /**
   * Sync all modules to the client.
   */
  void syncToClient();

  /**
   * Marks all modules as requiring a save.
   */
  void save();

  boolean isStillValid(Player player);

  Level level();

  void dropItem(ItemStack itemStack);
}
