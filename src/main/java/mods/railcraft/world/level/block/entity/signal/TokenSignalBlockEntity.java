package mods.railcraft.world.level.block.entity.signal;

import java.util.Objects;
import java.util.UUID;
import org.jspecify.annotations.Nullable;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.api.signal.SimpleSignalController;
import mods.railcraft.api.signal.TokenSignalEntity;
import mods.railcraft.api.signal.TrackLocator;
import mods.railcraft.api.signal.entity.SignalControllerEntity;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.util.TimerBag;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.signal.SimpleTokenRing;
import mods.railcraft.world.signal.TokenRingManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

public class TokenSignalBlockEntity extends AbstractSignalBlockEntity
    implements SignalControllerEntity, TokenSignalEntity {

  private final SimpleSignalController signalController =
      new SimpleSignalController(1, this::syncToClient, this, false,
          __ -> this.level.getLightEngine().checkBlock(this.getBlockPos()));

  private UUID ringId = UUID.randomUUID();
  @Nullable
  private Vec3 ringCentroidPos;

  private final TimerBag<UUID> cartTimers = new TimerBag<>(8);
  private final TrackLocator trackLocator;

  public TokenSignalBlockEntity(BlockPos blockPos, BlockState blockState) {
    this(RailcraftBlockEntityTypes.TOKEN_SIGNAL.get(), blockPos, blockState);
  }

  public TokenSignalBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
    super(type, blockPos, blockState);
    this.trackLocator = new TrackLocator(this::getLevel, blockPos);
  }

  @Override
  public void onLoad() {
    super.onLoad();
    if (!this.level.isClientSide()) {
      this.signalController.refresh();
    }
  }

  @Override
  public void preRemoveSideEffects(BlockPos pos, BlockState state) {
    super.preRemoveSideEffects(pos, state);
    this.blockRemoved();
  }

  protected void blockRemoved() {
    this.signalController.destroy();
    if (!this.level.isClientSide()) {
      this.signalNetwork().removePeer(this.getBlockPos());
    }
  }

  public static void clientTick(Level level, BlockPos blockPos, BlockState blockState,
      TokenSignalBlockEntity blockEntity) {
    blockEntity.signalController.spawnTuningAuraParticles();
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      TokenSignalBlockEntity blockEntity) {

    var tokenRing = blockEntity.signalNetwork();
    if (!Objects.equals(blockEntity.ringCentroidPos, tokenRing.getCentroid())) {
      blockEntity.ringCentroidPos = tokenRing.getCentroid();
      blockEntity.syncToClient();
    }

    blockEntity.cartTimers.tick();
    if (blockEntity.trackLocator.trackStatus() == TrackLocator.Status.VALID) {
      var trackPos = blockEntity.trackLocator.trackPos();
      if (trackPos != null) {
        EntitySearcher.findMinecarts()
            .at(trackPos)
            .stream(level)
            .filter(cart -> blockEntity.cartTimers.add(cart.getUUID()))
            .forEach(tokenRing::markCart);
      }
    }

    blockEntity.signalController.setSignalAspect(blockEntity.signalNetwork().aspect());
  }

  @Override
  public SignalAspect getPrimarySignalAspect() {
    return this.signalController.aspect();
  }

  @Override
  protected void saveAdditional(ValueOutput output) {
    super.saveAdditional(output);
    output.putChild(CompoundTagKeys.NETWORK, this.signalController);
    output.store(CompoundTagKeys.TOKEN_RING_ID, UUIDUtil.CODEC, this.ringId);
  }

  @Override
  protected void loadAdditional(ValueInput input) {
    super.loadAdditional(input);
    input.readChild(CompoundTagKeys.SIGNAL_CONTROLLER, this.signalController);
    this.ringId = input.read(CompoundTagKeys.TOKEN_RING_ID, UUIDUtil.CODEC).orElse(UUID.randomUUID());
  }

  @Override
  public void writeToBuf(RegistryFriendlyByteBuf data) {
    super.writeToBuf(data);
    this.signalController.writeToBuf(data);
    data.writeNullable(this.ringCentroidPos,  (buffer, value) -> buffer.writeVec3(value));
    data.writeUUID(this.ringId);
  }

  @Override
  public void readFromBuf(RegistryFriendlyByteBuf data) {
    super.readFromBuf(data);
    this.signalController.readFromBuf(data);
    this.ringCentroidPos = data.readNullable(buffer -> buffer.readVec3());
    this.ringId = data.readUUID();
  }

  @Override
  public UUID ringId() {
    return this.ringId;
  }

  public void setRingId(UUID tokenRingId) {
    this.ringId = tokenRingId;
    this.setChanged();
  }

  @Override
  public Vec3 ringCentroidPos() {
    if (this.ringCentroidPos == null)
      return this.getBlockPos().getCenter();
    return this.ringCentroidPos;
  }

  @Override
  public SimpleSignalController getSignalController() {
    return this.signalController;
  }

  @Override
  public SimpleTokenRing signalNetwork() {
    if (this.level.isClientSide()) {
      throw new IllegalStateException("Token ring is not available on the client.");
    }
    return TokenRingManager.get((ServerLevel) this.level)
        .getTokenRingNetwork(this.ringId, this.getBlockPos());
  }

  @Override
  public TrackLocator trackLocator() {
    return this.trackLocator;
  }
}
