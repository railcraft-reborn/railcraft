package mods.railcraft.api.signals;

import mods.railcraft.api.core.IOwnable;
import mods.railcraft.plugins.WorldPlugin;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

/**
 * Created by CovertJaguar on 9/12/2016 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface ITile extends IOwnable {

  default RailcraftBlockEntity tile() {
    return (RailcraftBlockEntity) this;
  }

  void markBlockForUpdate();

  void setPlacedBy(BlockState state, LivingEntity placer, ItemStack stack);

  void neighborChanged(BlockState state, Block neighborBlock, BlockPos neighborPos);

  default void notifyBlocksOfNeighborChange() {
    if (tile().hasLevel())
      WorldPlugin.notifyBlocksOfNeighborChange(tile().getLevel(), tile().getBlockPos(),
          tile().getBlockState().getBlock());
  }
}
