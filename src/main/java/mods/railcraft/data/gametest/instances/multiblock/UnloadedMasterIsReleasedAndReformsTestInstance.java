package mods.railcraft.data.gametest.instances.multiblock;

import com.mojang.serialization.MapCodec;
import mods.railcraft.data.gametest.RailcraftGameTestInstances;
import mods.railcraft.world.level.block.entity.CokeOvenBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Items;

/**
 * When the master's chunk unloads the members which stay loaded must stop exposing its inventory
 * right away, items inserted into it would be lost. Once the master is loaded again the structure
 * must re-form around it, with its contents.
 */
public class UnloadedMasterIsReleasedAndReformsTestInstance extends CokeOvenTestInstance {

  public static final MapCodec<UnloadedMasterIsReleasedAndReformsTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(UnloadedMasterIsReleasedAndReformsTestInstance::new);

  public UnloadedMasterIsReleasedAndReformsTestInstance(
      TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  @Override
  public void run(GameTestHelper helper) {
    buildCokeOven(helper);
    BlockPos[] masterPos = new BlockPos[1];
    CompoundTag[] savedMaster = new CompoundTag[1];
    helper.runAtTickTime(5, () -> {
      var master = master(helper);
      masterPos[0] = relativePos(helper, master.getBlockPos());
      insertCoal(helper, masterPos[0]);
      savedMaster[0] = unload(helper, master);
      assertMembersReleased(helper, masterPos[0]);
    });
    CokeOvenBlockEntity[] reloaded = new CokeOvenBlockEntity[1];
    helper.runAtTickTime(10, () -> {
      assertMembersReleased(helper, masterPos[0]);
      reloaded[0] = (CokeOvenBlockEntity) reload(helper, masterPos[0], savedMaster[0]);
    });
    helper.runAtTickTime(20, () -> {
      for (var pos : bricks()) {
        helper.assertTrue(brick(helper, pos).getMasterBlockEntity().orElse(null) == reloaded[0],
            "%s must belong to the reloaded master".formatted(pos));
      }
      var handler = itemHandler(helper, members(masterPos[0]).getFirst());
      helper.assertTrue(handler != null, "The re-formed coke oven must expose its inventory");
      helper.assertTrue(countItems(handler) == COAL,
          "The coke oven must still hold %s items, holds %s".formatted(COAL, countItems(handler)));
      helper.assertItemEntityNotPresent(Items.COAL);
      helper.succeed();
    });
  }

  private static void assertMembersReleased(GameTestHelper helper, BlockPos masterPos) {
    for (var pos : members(masterPos)) {
      helper.assertTrue(itemHandler(helper, pos) == null,
          "%s must not expose the inventory of an unloaded master".formatted(pos));
    }
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Unloaded Multiblock Master Is Released And Re-Forms");
  }
}
