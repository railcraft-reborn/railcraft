package mods.railcraft.data.gametest.instances.multiblock;

import com.mojang.serialization.MapCodec;
import mods.railcraft.data.gametest.RailcraftGameTestInstances;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.Blocks;

/**
 * Breaking the master must release every member, they must not keep exposing the inventory of a
 * removed coke oven.
 */
public class BrokenMasterReleasesMembersTestInstance extends CokeOvenTestInstance {

  public static final MapCodec<BrokenMasterReleasesMembersTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(BrokenMasterReleasesMembersTestInstance::new);

  public BrokenMasterReleasesMembersTestInstance(
      TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  @Override
  public void run(GameTestHelper helper) {
    buildCokeOven(helper);
    BlockPos[] masterPos = new BlockPos[1];
    helper.runAtTickTime(5, () -> {
      masterPos[0] = relativePos(helper, master(helper).getBlockPos());
      helper.setBlock(masterPos[0], Blocks.AIR);
    });
    helper.runAtTickTime(15, () -> {
      for (var pos : members(masterPos[0])) {
        helper.assertTrue(!brick(helper, pos).isFormed(),
            "%s must not be formed after its master was broken".formatted(pos));
        helper.assertTrue(itemHandler(helper, pos) == null,
            "%s must not expose an inventory after its master was broken".formatted(pos));
      }
      helper.succeed();
    });
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Broken Multiblock Master Releases Members");
  }
}
