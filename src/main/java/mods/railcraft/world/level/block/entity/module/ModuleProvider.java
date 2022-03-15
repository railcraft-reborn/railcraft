package mods.railcraft.world.level.block.entity.module;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface ModuleProvider {

  <T extends Module> Optional<T> getModule(Class<T> type);

  void syncToClient();

  boolean stillValid(Player player);

  BlockPos getBlockPos();

  Level getLevel();
}
