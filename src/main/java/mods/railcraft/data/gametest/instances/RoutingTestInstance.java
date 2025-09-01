package mods.railcraft.data.gametest.instances;

import java.util.List;
import java.util.Optional;
import com.mojang.serialization.MapCodec;
import mods.railcraft.data.gametest.RailcraftGameTestInstances;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.entity.vehicle.locomotive.CreativeLocomotive;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.component.RailcraftDataComponents;
import mods.railcraft.world.item.component.RoutingTableBookContent;
import mods.railcraft.world.level.block.entity.SwitchTrackRouterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

public class RoutingTestInstance extends GameTestInstance {

  public static final MapCodec<RoutingTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(RoutingTestInstance::new);

  private static final BlockPos SPAWN_POINT_A = new BlockPos(3, 1, 1);
  private static final BlockPos SPAWN_POINT_B = new BlockPos(5, 1, 1);
  private static final BlockPos SPAWN_POINT_A_B = new BlockPos(7, 1, 1);
  private static final BlockPos SPAWN_POINT_A_C = new BlockPos(9, 1, 1);

  private static final BlockPos TRAIN_A = new BlockPos(12, 1, 16);
  private static final BlockPos TRAIN_B = new BlockPos(12, 1, 5);
  private static final BlockPos TRAIN_A_B = new BlockPos(12, 1, 12);
  private static final BlockPos TRAIN_A_C = new BlockPos(12, 1, 14);

  public RoutingTestInstance(TestData<Holder<TestEnvironmentDefinition>> info) {
    super(info);
  }

  @Override
  public void run(GameTestHelper helper) {
    // Setup books
    var bookA = new ItemStack(RailcraftItems.ROUTING_TABLE_BOOK.get());
    applyBookDest(bookA, "Dest=A");
    var bookB = new ItemStack(RailcraftItems.ROUTING_TABLE_BOOK.get());
    applyBookDest(bookB, "Dest=B");
    var bookA_B = new ItemStack(RailcraftItems.ROUTING_TABLE_BOOK.get());
    applyBookDest(bookA_B, "Dest=A/B");
    var bookA_C = new ItemStack(RailcraftItems.ROUTING_TABLE_BOOK.get());
    applyBookDest(bookA_C, "Dest=A/C");

    // Setup routers
    var router_A = helper.getBlockEntity(new BlockPos(9, 1, 6), SwitchTrackRouterBlockEntity.class);
    router_A.setItem(0, bookA);
    var router_B = helper.getBlockEntity(new BlockPos(12, 1, 8), SwitchTrackRouterBlockEntity.class);
    router_B.setItem(0, bookB);
    var router_A_B = helper.getBlockEntity(new BlockPos(8, 1, 12), SwitchTrackRouterBlockEntity.class);
    router_A_B.setItem(0, bookA_B);
    var router_A_C = helper.getBlockEntity(new BlockPos(8, 1, 14), SwitchTrackRouterBlockEntity.class);
    router_A_C.setItem(0, bookA_C);

    // Spawn trains
    var train_A = spawnNewLocomotive(helper, SPAWN_POINT_A, "A");
    var train_B = spawnNewLocomotive(helper, SPAWN_POINT_B, "B");
    var train_A_B = spawnNewLocomotive(helper, SPAWN_POINT_A_B, "A/B");
    var train_A_C = spawnNewLocomotive(helper, SPAWN_POINT_A_C, "A/C");

    helper.runAtTickTime(20, () -> {
      train_A.setMode(Locomotive.Mode.RUNNING);
    });
    helper.runAtTickTime(60, () -> {
      train_B.setMode(Locomotive.Mode.RUNNING);
    });
    helper.runAtTickTime(100, () -> {
      train_A_B.setMode(Locomotive.Mode.RUNNING);
    });
    helper.runAtTickTime(140, () -> {
      train_A_C.setMode(Locomotive.Mode.RUNNING);
    });

    helper.succeedWhen(() -> {
      helper.assertEntityInstancePresent(train_A, TRAIN_A);
      helper.assertEntityInstancePresent(train_B, TRAIN_B);
      helper.assertEntityInstancePresent(train_A_B, TRAIN_A_B);
      helper.assertEntityInstancePresent(train_A_C, TRAIN_A_C);
    });
  }

  private static void applyBookDest(ItemStack book, String dest) {
    var newContent = new RoutingTableBookContent(
        List.of(dest),
        "Dev",
        Optional.empty());
    book.set(RailcraftDataComponents.ROUTING_TABLE_BOOK.get(), newContent);
  }

  private static CreativeLocomotive spawnNewLocomotive(GameTestHelper helper, BlockPos pos,
      String dest) {
    var train = helper.spawn(RailcraftEntityTypes.CREATIVE_LOCOMOTIVE.get(), pos);
    train.reverse();
    train.setDestination(dest);
    train.setSpeed(Locomotive.Speed.NORMAL);
    return train;
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Routing Test");
  }
}
