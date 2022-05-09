package mods.railcraft.util;

import java.util.function.Consumer;
import io.netty.buffer.Unpooled;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.network.play.EffectMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * Effects done on the logical server.
 */
public final class HostEffects implements Charge.ServerZapEffectProvider {

  public static final HostEffects INSTANCE = new HostEffects();

  public static void init() {} // classloading

  private HostEffects() {
    Charge._setHostZapEffectProvider(this);
  }

  public void teleportEffect(Entity entity, Vec3 destination) {
    if (entity.level.isClientSide())
      return;

    sendEffect(RemoteEffectType.TELEPORT, entity.level,
        new Vec3(entity.getX(), entity.getY(), entity.getZ()), data -> {
          data.writeDouble(entity.getX());
          data.writeDouble(entity.getY());
          data.writeDouble(entity.getZ());
          data.writeDouble(destination.x());
          data.writeDouble(destination.y());
          data.writeDouble(destination.z());
        });

    entity.level.playSound(
        null, entity, SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS, 0.25F, 1.0F);
  }

  public void forceTrackSpawnEffect(Level world, BlockPos pos, int color) {
    if (world.isClientSide())
      return;

    sendEffect(RemoteEffectType.FORCE_SPAWN, world, pos, data -> {
      data.writeBlockPos(pos);
      data.writeInt(color);
    });

    world.playSound(
        null, pos, SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS, 0.25F, 1.0F);
  }

  public void fireSparkEffect(Level world, Vec3 start, Vec3 end) {
    if (world.isClientSide())
      return;

    sendEffect(RemoteEffectType.FIRE_SPARK, world, start, data -> {
      data.writeDouble(start.x());
      data.writeDouble(start.y());
      data.writeDouble(start.z());
      data.writeDouble(end.x());
      data.writeDouble(end.y());
      data.writeDouble(end.z());
    });
  }

  @Override
  public void zapEffectDeath(Level world, Vec3 pos) {
    if (world.isClientSide())
      return;

    sendEffect(RemoteEffectType.ZAP_DEATH, world, pos,
        data -> {
          data.writeDouble(pos.x());
          data.writeDouble(pos.y());
          data.writeDouble(pos.z());
        });
  }

  public void blockCrack(Level world, BlockPos source, Vec3 pos, Vec3 velocity,
      BlockState state,
      String texture) {
    blockParticle(world, source, pos, velocity, state, texture, false);
  }

  public void blockDust(Level world, BlockPos source, Vec3 pos, Vec3 velocity,
      BlockState state,
      String texture) {
    blockParticle(world, source, pos, velocity, state, texture, true);
  }

  private void blockParticle(Level world, BlockPos source, Vec3 pos, Vec3 velocity,
      BlockState state, String texture, boolean dust) {
    sendEffect(RemoteEffectType.BLOCK_PARTICLE, world, pos, data -> {
      data.writeBlockPos(source);
      data.writeDouble(pos.x());
      data.writeDouble(pos.y());
      data.writeDouble(pos.z());
      data.writeDouble(velocity.x());
      data.writeDouble(velocity.y());
      data.writeDouble(velocity.z());
      data.writeInt(Block.getId(state));
      data.writeBoolean(dust);
      data.writeUtf(texture);
    });
  }

  private void sendEffect(RemoteEffectType type, Level world, BlockPos pos,
      Consumer<FriendlyByteBuf> writer) {
    preparePacket(type, writer).send(world, pos);
  }

  private void sendEffect(RemoteEffectType type, Level world, Vec3 pos,
      Consumer<FriendlyByteBuf> writer) {
    preparePacket(type, writer).send(world, pos);
  }

  private EffectMessage preparePacket(RemoteEffectType type, Consumer<FriendlyByteBuf> writer) {
    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
    writer.accept(buf);
    return new EffectMessage(type, buf);
  }
}
