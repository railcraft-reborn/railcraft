package mods.railcraft;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.track.DelayedLockingTrackBlockEntity;
import mods.railcraft.world.level.block.track.outfitted.DelayedLockingTrackBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(RailcraftConstants.ID)
@PrefixGameTestTemplate(false)
public class DelayedLockingTrackTest {

  private static final int Z = 3;
  private static final BlockPos TRACK_POS = new BlockPos(3, 2, Z);
  private static final BlockPos NEXT_POS = new BlockPos(4, 2, Z);
  private static final BlockPos END_POS = new BlockPos(6, 2, Z);
  private static final int DELAY = 40;

  @GameTest(template = "platform_7x7_tall", timeoutTicks = 200)
  public static void delayedLockingTrackHoldsThenReleases(GameTestHelper helper) {
    build(helper, true);
    launchCart(helper);
    helper.startSequence()
        // Long after the cart reached the track, but before the delay ran out
        .thenExecuteAfter(DELAY / 2 + 10, () -> {
          helper.assertBlockProperty(TRACK_POS, DelayedLockingTrackBlock.HOLDING, true);
          helper.assertEntityPresent(EntityType.MINECART, TRACK_POS);
        })
        // Released carts only get a small push, so just check it left the track
        .thenWaitUntil(() -> helper.assertEntityPresent(EntityType.MINECART, NEXT_POS))
        .thenSucceed();
  }

  @GameTest(template = "platform_7x7_tall", timeoutTicks = 40)
  public static void delayedLockingTrackUnpoweredLetsCartsPass(GameTestHelper helper) {
    build(helper, false);
    launchCart(helper);
    helper.succeedWhenEntityPresent(EntityType.MINECART, END_POS);
  }

  private static void build(GameTestHelper helper, boolean powered) {
    for (int x = 0; x <= 6; x++) {
      if (x != TRACK_POS.getX()) {
        helper.setBlock(new BlockPos(x, 2, Z),
            Blocks.RAIL.defaultBlockState().setValue(RailBlock.SHAPE, RailShape.EAST_WEST));
      }
    }
    helper.setBlock(TRACK_POS, RailcraftBlocks.IRON_DELAYED_LOCKING_TRACK.get()
        .defaultBlockState()
        .setValue(DelayedLockingTrackBlock.SHAPE, RailShape.EAST_WEST));
    // Powered after the track is placed, as a player would do
    if (powered) {
      helper.setBlock(TRACK_POS.below(), Blocks.REDSTONE_BLOCK);
    }
    var track = (DelayedLockingTrackBlockEntity) helper.getBlockEntity(TRACK_POS);
    track.setDelay(DELAY);
  }

  private static void launchCart(GameTestHelper helper) {
    var cart = helper.spawn(EntityType.MINECART, new Vec3(0.5, 2.1, Z + 0.5));
    cart.setDeltaMovement(0.4, 0, 0);
  }
}
