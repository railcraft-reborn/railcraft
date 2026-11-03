package mods.railcraft.data.gametest.instances.review;

import static mods.railcraft.data.gametest.instances.review.ReviewTestUtil.reproduced;
import static mods.railcraft.data.gametest.instances.review.ReviewTestUtil.setupError;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.network.to_server.SetActionSignalBoxMessage;
import mods.railcraft.network.to_server.SetAnalogSignalControllerBoxMessage;
import mods.railcraft.network.to_server.SetEmbarkingTrackMessage;
import mods.railcraft.network.to_server.SetFluidManipulatorMessage;
import mods.railcraft.network.to_server.SetItemDetectorMessage;
import mods.railcraft.network.to_server.SetItemManipulatorMessage;
import mods.railcraft.network.to_server.SetLauncherTrackMessage;
import mods.railcraft.network.to_server.SetRoutingDetectorMessage;
import mods.railcraft.network.to_server.SetRoutingTrackMessage;
import mods.railcraft.network.to_server.SetSignalCapacitorBoxMessage;
import mods.railcraft.network.to_server.SetSignalControllerBoxMessage;
import mods.railcraft.network.to_server.SetSwitchTrackMotorMessage;
import mods.railcraft.network.to_server.SetSwitchTrackRouterMessage;
import mods.railcraft.network.to_server.SetTankDetectorMessage;
import mods.railcraft.network.to_server.SetTrainDetectorMessage;
import mods.railcraft.util.routing.RouterBlockEntity;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.LockableSwitchTrackActuatorBlockEntity;
import mods.railcraft.world.level.block.entity.SwitchTrackRouterBlockEntity;
import mods.railcraft.world.level.block.entity.detector.ItemDetectorBlockEntity;
import mods.railcraft.world.level.block.entity.detector.TankDetectorBlockEntity;
import mods.railcraft.world.level.block.entity.detector.TrainDetectorBlockEntity;
import mods.railcraft.world.level.block.entity.manipulator.ManipulatorBlockEntity;
import mods.railcraft.world.level.block.entity.signal.LockableSignalBoxBlockEntity;
import mods.railcraft.world.level.block.entity.signal.SignalCapacitorBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Serverbound packet finding: C3.
 */
final class ServerboundPacketCheckTests {

  private ServerboundPacketCheckTests() {}

  static boolean isFullChunkLoaded(GameTestHelper helper, BlockPos absolutePos) {
    var chunkPos = ChunkPos.containing(absolutePos);
    return helper.getLevel().getChunkSource().hasChunk(chunkPos.x(), chunkPos.z());
  }

  private record Handler(String name, Function<BlockPos, Object> message,
      BiConsumer<Object, IPayloadContext> handle) {}

  @SuppressWarnings("unchecked")
  private static <T> Handler handler(String name, Function<BlockPos, T> message,
      BiConsumer<T, IPayloadContext> handle) {
    return new Handler(name, (Function<BlockPos, Object>) message,
        (BiConsumer<Object, IPayloadContext>) handle);
  }

  private static List<Handler> blockPosHandlers() {
    return List.of(
        handler("SetActionSignalBox", pos -> new SetActionSignalBoxMessage(pos,
            EnumSet.noneOf(SignalAspect.class), LockableSignalBoxBlockEntity.Lock.values()[0]),
            SetActionSignalBoxMessage::handle),
        handler("SetAnalogSignalControllerBox",
            pos -> new SetAnalogSignalControllerBoxMessage(pos, new HashMap<>()),
            SetAnalogSignalControllerBoxMessage::handle),
        handler("SetEmbarkingTrack", pos -> new SetEmbarkingTrackMessage(pos, 1),
            SetEmbarkingTrackMessage::handle),
        handler("SetFluidManipulator", pos -> new SetFluidManipulatorMessage(pos,
            ManipulatorBlockEntity.RedstoneMode.values()[0]), SetFluidManipulatorMessage::handle),
        handler("SetItemDetector", pos -> new SetItemDetectorMessage(pos,
            ItemDetectorBlockEntity.PrimaryMode.values()[0],
            ItemDetectorBlockEntity.FilterMode.values()[0]), SetItemDetectorMessage::handle),
        handler("SetItemManipulator", pos -> new SetItemManipulatorMessage(pos,
            ManipulatorBlockEntity.RedstoneMode.values()[0],
            ManipulatorBlockEntity.TransferMode.values()[0]), SetItemManipulatorMessage::handle),
        handler("SetLauncherTrack", pos -> new SetLauncherTrackMessage(pos, (byte) 5),
            SetLauncherTrackMessage::handle),
        handler("SetRoutingDetector", pos -> new SetRoutingDetectorMessage(pos,
            RouterBlockEntity.Railway.values()[0], SwitchTrackRouterBlockEntity.Lock.values()[0]),
            SetRoutingDetectorMessage::handle),
        handler("SetRoutingTrack", pos -> new SetRoutingTrackMessage(pos,
            LockableSwitchTrackActuatorBlockEntity.Lock.values()[0]),
            SetRoutingTrackMessage::handle),
        handler("SetSignalCapacitorBox", pos -> new SetSignalCapacitorBoxMessage(pos, (short) 10,
            SignalCapacitorBoxBlockEntity.Mode.values()[0]), SetSignalCapacitorBoxMessage::handle),
        handler("SetSignalControllerBox", pos -> new SetSignalControllerBoxMessage(pos,
            SignalAspect.GREEN, SignalAspect.RED), SetSignalControllerBoxMessage::handle),
        handler("SetSwitchTrackMotor", pos -> new SetSwitchTrackMotorMessage(pos,
            EnumSet.noneOf(SignalAspect.class), false,
            LockableSwitchTrackActuatorBlockEntity.Lock.values()[0]),
            SetSwitchTrackMotorMessage::handle),
        handler("SetSwitchTrackRouter", pos -> new SetSwitchTrackRouterMessage(pos,
            RouterBlockEntity.Railway.values()[0], SwitchTrackRouterBlockEntity.Lock.values()[0]),
            SetSwitchTrackRouterMessage::handle),
        handler("SetTankDetector", pos -> new SetTankDetectorMessage(pos,
            TankDetectorBlockEntity.Mode.values()[0]), SetTankDetectorMessage::handle),
        handler("SetTrainDetector", pos -> new SetTrainDetectorMessage(pos, 5),
            SetTrainDetectorMessage::handle));
  }

  /**
   * C3: a serverbound packet naming a position in an unloaded chunk must not load (or generate)
   * that chunk. Each handler gets its own far away chunk.
   */
  static void c3HandlersLoadFarChunks(GameTestHelper helper) {
    var spy = ReviewTestUtil.spyPlayer(helper);
    helper.startSequence()
        .thenExecute(() -> {
          var context = ReviewTestUtil.payloadContext(spy.player);
          var base = helper.absolutePos(BlockPos.ZERO).offset(60_000, 0, 60_000);
          var loaded = new ArrayList<String>();
          var skipped = 0;
          var start = System.nanoTime();
          var handlers = blockPosHandlers();
          for (var i = 0; i < handlers.size(); i++) {
            var handler = handlers.get(i);
            var pos = base.offset(i * 64, 0, 0);
            if (isFullChunkLoaded(helper, pos)) {
              skipped++;
              continue;
            }
            try {
              handler.handle().accept(handler.message().apply(pos), context);
            } catch (RuntimeException e) {
              // Only interested in the chunk side effect
            }
            if (isFullChunkLoaded(helper, pos)) {
              loaded.add(handler.name());
            }
          }
          var millis = (System.nanoTime() - start) / 1_000_000;
          spy.remove();
          if (skipped == handlers.size()) {
            setupError(helper, "C3", "all target chunks were already loaded");
          }
          if (!loaded.isEmpty()) {
            reproduced(helper, "C3", loaded.size() + " of " + (handlers.size() - skipped)
                + " serverbound handlers loaded a chunk ~85000 blocks from the sender on the "
                + "server thread (" + millis + " ms total): " + String.join(", ", loaded));
          }
        })
        .thenSucceed();
  }

  /**
   * C3: a player without the machine's menu open, far away, must not be able to change it.
   */
  static void c3RemoteEditWithoutMenu(GameTestHelper helper) {
    var pos = new BlockPos(4, 1, 4);
    helper.setBlock(pos, RailcraftBlocks.TRAIN_DETECTOR.get());
    var spy = ReviewTestUtil.spyPlayer(helper);
    helper.startSequence()
        .thenExecute(() -> {
          var detector = helper.getBlockEntity(pos, TrainDetectorBlockEntity.class);
          var before = detector.getTrainSize();
          var absolutePos = helper.absolutePos(pos);
          SetTrainDetectorMessage.handle(new SetTrainDetectorMessage(absolutePos, before + 4),
              ReviewTestUtil.payloadContext(spy.player));
          var distance = (int) spy.player.position()
              .distanceTo(net.minecraft.world.phys.Vec3.atCenterOf(absolutePos));
          var menu = spy.player.containerMenu.getClass().getSimpleName();
          spy.remove();
          if (detector.getTrainSize() != before) {
            reproduced(helper, "C3", "a player " + distance + " blocks away with only its "
                + menu + " open changed the train detector's size from " + before + " to "
                + detector.getTrainSize(), pos);
          }
        })
        .thenSucceed();
  }
}
