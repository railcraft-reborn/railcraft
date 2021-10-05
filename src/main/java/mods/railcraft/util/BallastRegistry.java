package mods.railcraft.util;

import java.util.HashSet;
import java.util.Set;
import mods.railcraft.util.inventory.InvTools;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;

/**
 * Register an item here to designate it as a possible ballast that can be used in the Bore.
 * <p/>
 * It is expected that ballast is affected by gravity.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public abstract class BallastRegistry {

  private static final Set<BlockState> ballastRegistry = new HashSet<>();

  static {
    registerBallast(Blocks.GRAVEL);
  }

  public static void registerBallast(Block block) {
    ballastRegistry.add(block.defaultBlockState());
  }

  public static boolean isItemBallast(ItemStack stack) {
    if (stack.isEmpty())
      return false;
    BlockState state = InvTools.getBlockStateFromStack(stack);
    return state != null && ballastRegistry.contains(state);
  }

  public static Set<BlockState> getRegisteredBallasts() {
    return ballastRegistry;
  }
}
