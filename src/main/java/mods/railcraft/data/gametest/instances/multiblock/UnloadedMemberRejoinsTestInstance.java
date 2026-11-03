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
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * A member whose chunk unloads must not make the master disband, which would drop its contents,
 * and the member must rejoin the structure once it's loaded again.
 */
public class UnloadedMemberRejoinsTestInstance extends CokeOvenTestInstance {

  public static final MapCodec<UnloadedMemberRejoinsTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(UnloadedMemberRejoinsTestInstance::new);

  public UnloadedMemberRejoinsTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  @Override
  public void run(GameTestHelper helper) {
    buildCokeOven(helper);
    CokeOvenBlockEntity[] master = new CokeOvenBlockEntity[1];
    BlockPos[] memberPos = new BlockPos[1];
    CompoundTag[] savedMember = new CompoundTag[1];
    helper.runAtTickTime(5, () -> {
      master[0] = master(helper);
      var masterPos = relativePos(helper, master[0].getBlockPos());
      insertCoal(helper, masterPos);
      memberPos[0] = members(masterPos).getFirst();
      savedMember[0] = unload(helper, brick(helper, memberPos[0]));
    });
    CokeOvenBlockEntity[] reloaded = new CokeOvenBlockEntity[1];
    helper.runAtTickTime(10, () -> {
      helper.assertTrue(master[0].isFormed(),
          "The master must stay formed while a member is unloaded");
      helper.assertItemEntityNotPresent(Items.COAL);
      reloaded[0] = (CokeOvenBlockEntity) reload(helper, memberPos[0], savedMember[0]);
    });
    helper.runAtTickTime(20, () -> {
      helper.assertTrue(reloaded[0].getMasterBlockEntity().orElse(null) == master[0],
          "The reloaded member must rejoin its master");
      helper.assertTrue(master[0].streamMembers().noneMatch(BlockEntity::isRemoved),
          "The master must not keep removed members");
      var handler = itemHandler(helper, memberPos[0]);
      helper.assertTrue(handler != null, "The reloaded member must expose the inventory");
      helper.assertTrue(countItems(handler) == COAL,
          "The coke oven must still hold %s items, holds %s".formatted(COAL, countItems(handler)));
      helper.assertItemEntityNotPresent(Items.COAL);
      helper.succeed();
    });
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Unloaded Multiblock Member Rejoins");
  }
}
