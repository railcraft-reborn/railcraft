package mods.railcraft;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.TicketItem;
import mods.railcraft.world.level.block.entity.track.RoutingTrackBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(RailcraftConstants.ID)
@PrefixGameTestTemplate(false)
public class RoutingTrackTest {

  private static final BlockPos SPAWN_POINT = new BlockPos(1, 2, 3);
  private static final BlockPos DEST = new BlockPos(1,2,6);
  private static final BlockPos LEVER_POS = new BlockPos(0, 2, 4);
  private static final BlockPos ROUTING_TRACK_POS = new BlockPos(1, 2, 4);


  @GameTest(template = "routing_track")
  public static void routingTrackActive(GameTestHelper helper) {
    helper.pullLever(LEVER_POS);
    final var dest = "HOME";
    if (helper.getBlockEntity(ROUTING_TRACK_POS) instanceof RoutingTrackBlockEntity routingTrack) {
      var goldenTicket = new ItemStack(RailcraftItems.GOLDEN_TICKET.get());
      TicketItem.setTicketData(goldenTicket, dest, RailcraftConstants.FAKE_GAMEPROFILE);
      routingTrack.setItem(0, goldenTicket);
    }
    var train = helper.spawn(RailcraftEntityTypes.CREATIVE_LOCOMOTIVE.get(), SPAWN_POINT);
    train.setReverse(true);
    train.setSpeed(Locomotive.Speed.NORMAL);
    train.setMode(Locomotive.Mode.RUNNING);
    helper.succeedWhen(() -> {
      helper.assertEntityInstancePresent(train, DEST);
      if (train.getDestination().equals(dest)) {
        helper.succeed();
      } else {
        helper.fail("Expected destination: " + dest);
      }
    });
  }

  @GameTest(template = "routing_track")
  public static void routingTrackPassive(GameTestHelper helper) {
    final var dest = "HOME";
    if (helper.getBlockEntity(ROUTING_TRACK_POS) instanceof RoutingTrackBlockEntity routingTrack) {
      var goldenTicket = new ItemStack(RailcraftItems.GOLDEN_TICKET.get());
      TicketItem.setTicketData(goldenTicket, dest, RailcraftConstants.FAKE_GAMEPROFILE);
      routingTrack.setItem(0, goldenTicket);
    }
    var train = helper.spawn(RailcraftEntityTypes.CREATIVE_LOCOMOTIVE.get(), SPAWN_POINT);
    train.setReverse(true);
    train.setSpeed(Locomotive.Speed.NORMAL);
    train.setMode(Locomotive.Mode.RUNNING);
    helper.succeedWhen(() -> {
      helper.assertEntityInstancePresent(train, DEST);
      if (train.getDestination().isEmpty()) {
        helper.succeed();
      } else {
        helper.fail("Expected empty destination");
      }
    });
  }
}
