package mods.railcraft.data.gametest.instances.wye_track;

import com.mojang.serialization.MapCodec;
import mods.railcraft.data.gametest.RailcraftGameTestInstances;
import mods.railcraft.data.gametest.instances.JunctionPlacementHelper;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.track.outfitted.SwitchTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.WyeTrackBlock;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class WyeTrackPlacementTestInstance extends GameTestInstance {

  public static final MapCodec<WyeTrackPlacementTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(WyeTrackPlacementTestInstance::new);

  public WyeTrackPlacementTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  @Override
  public void run(GameTestHelper helper) {
    JunctionPlacementHelper.forEachPlacement(helper, RailcraftItems.IRON_WYE_TRACK.get(),
        (mainLine, branch) -> {
          var state = helper.getBlockState(JunctionPlacementHelper.CENTER);
          var block = (WyeTrackBlock) state.getBlock();
          var pos = helper.absolutePos(JunctionPlacementHelper.CENTER);
          var left = block.getRailDirection(
              state.setValue(SwitchTrackBlock.SWITCHED, false), helper.getLevel(), pos, null);
          var right = block.getRailDirection(
              state.setValue(SwitchTrackBlock.SWITCHED, true), helper.getLevel(), pos, null);
          var context = JunctionPlacementHelper.describe(helper, mainLine, branch);
          helper.assertTrue(JunctionPlacementHelper.connects(left, branch)
              && JunctionPlacementHelper.connects(right, branch),
              "Both routes must lead to the branch: " + context);
        });
    helper.succeed();
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Wye Track Placement");
  }
}
