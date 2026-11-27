package mods.railcraft.data.gametest.instances.delayed_locking_track;

import com.mojang.serialization.MapCodec;
import mods.railcraft.data.gametest.RailcraftGameTestInstances;
import mods.railcraft.world.level.block.track.outfitted.DelayedLockingTrackBlock;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityTypes;

public class DelayedLockingTrackHoldsThenReleasesTestInstance extends GameTestInstance {

  public static final MapCodec<DelayedLockingTrackHoldsThenReleasesTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(DelayedLockingTrackHoldsThenReleasesTestInstance::new);

  public DelayedLockingTrackHoldsThenReleasesTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  @Override
  public void run(GameTestHelper helper) {
    DelayedLockingTrackTestHelper.build(helper, true);
    DelayedLockingTrackTestHelper.launchCart(helper);
    helper.startSequence()
        // Long after the cart reached the track, but before the delay ran out
        .thenExecuteAfter(DelayedLockingTrackTestHelper.DELAY / 2 + 10, () -> {
          helper.assertBlockProperty(DelayedLockingTrackTestHelper.TRACK_POS,
              DelayedLockingTrackBlock.HOLDING, true);
          helper.assertEntityPresent(EntityTypes.MINECART, DelayedLockingTrackTestHelper.TRACK_POS);
        })
        // Released carts only get a small push, so just check it left the track
        .thenWaitUntil(() -> helper.assertEntityPresent(EntityTypes.MINECART,
            DelayedLockingTrackTestHelper.NEXT_POS))
        .thenSucceed();
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Delayed Locking Track Holds Then Releases");
  }
}
