package mods.railcraft.data.gametest.instances.review;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.jspecify.annotations.Nullable;
import com.mojang.authlib.GameProfile;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.embedded.EmbeddedChannel;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.extensions.ICommonPacketListener;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Helpers shared by the review finding tests.
 */
final class ReviewTestUtil {

  private ReviewTestUtil() {}

  /**
   * Fails the test with a message that marks the finding as reproduced.
   */
  static void reproduced(GameTestHelper helper, String finding, String message) {
    helper.fail(Component.literal("REPRODUCED " + finding + ": " + message));
  }

  static void reproduced(GameTestHelper helper, String finding, String message, BlockPos pos) {
    helper.fail(Component.literal("REPRODUCED " + finding + ": " + message), pos);
  }

  static void setupError(GameTestHelper helper, String finding, String message) {
    helper.fail(Component.literal("SETUP " + finding + ": " + message));
  }

  // ---------------------------------------------------------------------------------------------
  // Reflection
  // ---------------------------------------------------------------------------------------------

  static Field findField(Class<?> clazz, String name) {
    for (var c = clazz; c != null; c = c.getSuperclass()) {
      try {
        var field = c.getDeclaredField(name);
        field.setAccessible(true);
        return field;
      } catch (NoSuchFieldException e) {
        // Try the super class
      }
    }
    throw new IllegalArgumentException("No field " + name + " in " + clazz);
  }

  @SuppressWarnings("unchecked")
  static <T> T getField(Object target, String name) {
    try {
      return (T) findField(target.getClass(), name).get(target);
    } catch (IllegalAccessException e) {
      throw new IllegalStateException(e);
    }
  }

  static void setField(Object target, String name, @Nullable Object value) {
    try {
      findField(target.getClass(), name).set(target, value);
    } catch (IllegalAccessException e) {
      throw new IllegalStateException(e);
    }
  }

  // ---------------------------------------------------------------------------------------------
  // Players
  // ---------------------------------------------------------------------------------------------

  /**
   * Creates a server player that records every packet sent to it. The player is added to the
   * level's player list only, it is never added to the chunk map, so it doesn't track any chunk:
   * it models a player who is somewhere else in the same dimension. Remove it with
   * {@link SpyPlayer#remove()}.
   */
  static SpyPlayer spyPlayer(GameTestHelper helper) {
    var origin = helper.absolutePos(BlockPos.ZERO);
    return spyPlayer(helper,
        new Vec3(origin.getX() + 100_000, 100, origin.getZ() + 100_000));
  }

  /**
   * Same as {@link #spyPlayer(GameTestHelper)}, but places the player at the given absolute
   * position. The player is still not added to the chunk map, but anything that iterates the
   * level's player list by distance (particles, sounds, "players near" packets) reaches it.
   */
  static SpyPlayer spyPlayer(GameTestHelper helper, Vec3 absolutePosition) {
    var level = helper.getLevel();
    var server = level.getServer();
    var profile = new GameProfile(UUID.randomUUID(), "review-spy");
    var player = new ServerPlayer(server, level, profile, ClientInformation.createDefault()) {
      @Override
      public GameType gameMode() {
        return GameType.SURVIVAL;
      }
    };
    player.setPos(absolutePosition);
    var connection = new Connection(PacketFlow.SERVERBOUND);
    new EmbeddedChannel(connection);
    var cookie = CommonListenerCookie.createInitial(profile, false);
    var spy = new SpyPlayer(player, level);
    // The constructor assigns itself to player.connection
    new SpyListener(server, connection, player, cookie, spy.packets);
    level.players().add(player);
    return spy;
  }

  static final class SpyPlayer {

    final ServerPlayer player;
    final ServerLevel level;
    final List<Packet<?>> packets = Collections.synchronizedList(new ArrayList<>());

    SpyPlayer(ServerPlayer player, ServerLevel level) {
      this.player = player;
      this.level = level;
    }

    boolean isTracking(BlockPos pos) {
      return this.level.getChunkSource().chunkMap
          .getPlayers(ChunkPos.containing(pos), false).contains(this.player);
    }

    void clear() {
      this.packets.clear();
    }

    long blockEntityPackets(BlockPos absolutePos) {
      synchronized (this.packets) {
        return this.packets.stream()
            .filter(ClientboundBlockEntityDataPacket.class::isInstance)
            .map(ClientboundBlockEntityDataPacket.class::cast)
            .filter(p -> p.getPos().equals(absolutePos))
            .count();
      }
    }

    long blockEntityPackets() {
      synchronized (this.packets) {
        return this.packets.stream()
            .filter(ClientboundBlockEntityDataPacket.class::isInstance)
            .count();
      }
    }

    long packetsOfType(Class<?> type) {
      synchronized (this.packets) {
        return this.packets.stream().filter(type::isInstance).count();
      }
    }

    void remove() {
      this.level.players().remove(this.player);
    }
  }

  private static final class SpyListener extends ServerGamePacketListenerImpl {

    private final List<Packet<?>> packets;

    SpyListener(net.minecraft.server.MinecraftServer server, Connection connection,
        ServerPlayer player, CommonListenerCookie cookie, List<Packet<?>> packets) {
      super(server, connection, player, cookie);
      this.packets = packets;
    }

    @Override
    public void send(Packet<?> packet, @Nullable ChannelFutureListener listener) {
      this.packets.add(packet);
    }
  }

  // ---------------------------------------------------------------------------------------------
  // Networking
  // ---------------------------------------------------------------------------------------------

  /**
   * A payload context for invoking serverbound handlers directly, as if the player had sent the
   * packet.
   */
  static IPayloadContext payloadContext(ServerPlayer player) {
    return new IPayloadContext() {
      @Override
      public ICommonPacketListener listener() {
        return player.connection;
      }

      @Override
      public Player player() {
        return player;
      }

      @Override
      public CompletableFuture<Void> enqueueWork(Runnable task) {
        task.run();
        return CompletableFuture.completedFuture(null);
      }

      @Override
      public <T> CompletableFuture<T> enqueueWork(java.util.function.Supplier<T> task) {
        return CompletableFuture.completedFuture(task.get());
      }

      @Override
      public PacketFlow flow() {
        return PacketFlow.SERVERBOUND;
      }

      @Override
      public void handle(CustomPacketPayload payload) {
        throw new UnsupportedOperationException();
      }

      @Override
      public void finishCurrentTask(
          net.minecraft.server.network.ConfigurationTask.Type type) {
        throw new UnsupportedOperationException();
      }
    };
  }
}
