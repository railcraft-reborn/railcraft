package mods.railcraft.world.level.block.entity;

import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import com.google.common.primitives.Bytes;
import com.mojang.serialization.Codec;
import io.netty.buffer.Unpooled;
import mods.railcraft.api.core.BlockEntityLike;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.api.core.NetworkSerializable;
import mods.railcraft.api.core.Ownable;
import mods.railcraft.world.module.BlockModuleProvider;
import mods.railcraft.world.module.Module;
import mods.railcraft.world.module.ModuleDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.NameAndId;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.network.connection.ConnectionType;

public abstract class RailcraftBlockEntity extends BlockEntity
    implements NetworkSerializable, Ownable, BlockEntityLike, BlockModuleProvider {

  protected final ModuleDispatcher moduleDispatcher = new ModuleDispatcher();

  @Nullable
  private NameAndId owner;

  @Nullable
  private Component customName;

  public RailcraftBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
    super(type, blockPos, blockState);
  }

  @Override
  public final ClientboundBlockEntityDataPacket getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  @Override
  public final CompoundTag getUpdateTag(HolderLookup.Provider provider) {
    var tag = super.getUpdateTag(provider);
    if (this.level == null) {
      return tag;
    }

    var friendlyBuf = new FriendlyByteBuf(Unpooled.buffer());
    var packetBuffer = new RegistryFriendlyByteBuf(friendlyBuf, level.registryAccess(), ConnectionType.OTHER);
    this.writeToBuf(packetBuffer);
    byte[] syncData = new byte[packetBuffer.readableBytes()];
    packetBuffer.readBytes(syncData);
    tag.putByteArray(CompoundTagKeys.SYNC, syncData);
    return tag;
  }

  @Override
  public void handleUpdateTag(ValueInput input) {
    if (this.level == null) {
      return;
    }
    input.read(CompoundTagKeys.SYNC, Codec.BYTE.listOf()).ifPresent(bytes -> {
      var syncData = Bytes.toArray(bytes);
      var friendlyBuf = new FriendlyByteBuf(Unpooled.wrappedBuffer(syncData));
      var packetBuffer = new RegistryFriendlyByteBuf(friendlyBuf, level.registryAccess(), ConnectionType.OTHER);
      this.readFromBuf(packetBuffer);
    });
  }

  @Override
  public void onDataPacket(Connection net, ValueInput valueInput) {
    this.handleUpdateTag(valueInput);
  }

  @Override
  public void writeToBuf(RegistryFriendlyByteBuf out) {
    out.writeNullable(this.owner, (friendlyByteBuf, gameProfile) -> {
      friendlyByteBuf.writeUUID(gameProfile.id());
      friendlyByteBuf.writeUtf(gameProfile.name());
    });
    out.writeNullable(this.customName, (friendlyByteBuf, component) ->
        ComponentSerialization.STREAM_CODEC.encode((RegistryFriendlyByteBuf) friendlyByteBuf, component));
    this.moduleDispatcher.writeToBuf(out);
  }

  @Override
  public void readFromBuf(RegistryFriendlyByteBuf in) {
    this.owner = in.readNullable(friendlyByteBuf -> {
      var id = friendlyByteBuf.readUUID();
      var name = friendlyByteBuf.readUtf();
      return new NameAndId(id, name);
    });
    this.customName = in.readNullable(friendlyByteBuf ->
        ComponentSerialization.STREAM_CODEC.decode((RegistryFriendlyByteBuf) friendlyByteBuf));
    this.moduleDispatcher.readFromBuf(in);
  }

  @Override
  public BlockPos blockPos() {
    return this.getBlockPos();
  }

  @Nullable
  @Override
  public Level level() {
    return this.level;
  }

  @Override
  public void syncToClient() {
    if (this.level instanceof ServerLevel serverLevel) {
      var packet = this.getUpdatePacket();
      serverLevel.players().forEach(player -> player.connection.send(packet));
    }
  }

  @Override
  public void save() {
    this.setChanged();
  }

  @Override
  public <T extends Module> Optional<T> getModule(Class<T> type) {
    return this.moduleDispatcher.getModule(type);
  }

  @Override
  public boolean isStillValid(Player player) {
    return isStillValid(this, player, 64);
  }

  public final void setOwner(@Nullable NameAndId profile) {
    this.owner = profile;
  }

  @Override
  public final Optional<NameAndId> getOwner() {
    return Optional.ofNullable(this.owner);
  }

  public final boolean isOwner(NameAndId gameProfile) {
    return gameProfile.equals(this.owner);
  }

  public final boolean isOwnerOrOperator(NameAndId gameProfile) {
    return this.isOwner(gameProfile) || (this.level instanceof ServerLevel serverLevel
        && serverLevel.getServer().getPlayerList().isOp(gameProfile));
  }

  @Override
  protected void saveAdditional(ValueOutput output) {
    super.saveAdditional(output);
    output.storeNullable(CompoundTagKeys.OWNER, NameAndId.CODEC, this.owner);
    output.storeNullable(CompoundTagKeys.CUSTOM_NAME, ComponentSerialization.CODEC, this.customName);
    output.putChild(CompoundTagKeys.MODULES, this.moduleDispatcher);
  }

  @Override
  protected void loadAdditional(ValueInput input) {
    super.loadAdditional(input);
    this.owner = input.read(CompoundTagKeys.OWNER, NameAndId.CODEC).orElse(null);
    this.customName = input.read(CompoundTagKeys.CUSTOM_NAME, ComponentSerialization.CODEC).orElse(null);
    input.readChild(CompoundTagKeys.MODULES, this.moduleDispatcher);
  }

  @Override
  public boolean hasCustomName() {
    return this.customName != null;
  }

  @Override
  @Nullable
  public Component getCustomName() {
    return this.customName;
  }

  protected void setCustomName(@Nullable Component name) {
    this.customName = name;
    this.syncToClient();
  }

  @Override
  public Component getName() {
    return this.hasCustomName() ? this.customName : this.getBlockState().getBlock().getName();
  }

  @Override
  public Component getDisplayName() {
    return this.getName();
  }

  @Override
  public final BlockEntity asBlockEntity() {
    return this;
  }

  public static boolean isStillValid(BlockEntity blockEntity, Player player, int maxDistance) {
    var pos = blockEntity.getBlockPos();
    var distance = player.distanceToSqr(pos.getX(), pos.getY(), pos.getZ());
    return !blockEntity.isRemoved()
        && blockEntity.getLevel().getBlockEntity(pos).equals(blockEntity)
        && distance <= maxDistance;
  }
}
