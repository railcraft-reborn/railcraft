package mods.railcraft.data.gametest.instances;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.TicketItem;
import mods.railcraft.world.level.block.entity.track.RoutingTrackBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;

public class RoutingTrackTestInstance extends GameTestInstance {

  public static final MapCodec<RoutingTrackTestInstance> CODEC =
      RecordCodecBuilder.mapCodec(instance -> instance.group(
          Codec.BOOL.fieldOf("pullLever").forGetter(t -> t.pullLever),
          TestData.CODEC.forGetter(t -> t.info)
      ).apply(instance, RoutingTrackTestInstance::new));

  private static final BlockPos SPAWN_POINT = new BlockPos(1, 1, 3);
  private static final BlockPos DEST = new BlockPos(1, 1,6);
  private static final BlockPos LEVER_POS = new BlockPos(0, 1, 4);
  private static final BlockPos ROUTING_TRACK_POS = new BlockPos(1, 1, 4);
  private final boolean pullLever;

  public RoutingTrackTestInstance(boolean pullLever, TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
    this.pullLever = pullLever;
  }

  @Override
  public void run(GameTestHelper helper) {
    if (pullLever) {
      helper.pullLever(LEVER_POS);
    }

    final var dest = "HOME";
    var routingTrack = helper.getBlockEntity(ROUTING_TRACK_POS, RoutingTrackBlockEntity.class);
    var goldenTicket = new ItemStack(RailcraftItems.GOLDEN_TICKET.get());
    TicketItem.setTicketData(goldenTicket, dest, helper.makeMockPlayer(GameType.CREATIVE).nameAndId());
    routingTrack.setItem(0, goldenTicket);

    var train = helper.spawn(RailcraftEntityTypes.CREATIVE_LOCOMOTIVE.get(), SPAWN_POINT);
    train.setReverse(true);
    train.setSpeed(Locomotive.Speed.NORMAL);
    train.setMode(Locomotive.Mode.RUNNING);
    helper.succeedWhen(() -> {
      helper.assertEntityInstancePresent(train, DEST);
      if (pullLever) {
        if (train.getDestination().equals(dest)) {
          helper.succeed();
        } else {
          helper.fail(Component.literal("Expected destination: " + dest));
        }
      } else {
        if (train.getDestination().isEmpty()) {
          helper.succeed();
        } else {
          helper.fail(Component.literal("Expected empty destination"));
        }
      }
    });
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    if (this.pullLever) {
      return Component.literal("Routing Track with Lever Pulled");
    }
    return Component.literal("Routing Track without Lever Pulled");
  }
}
