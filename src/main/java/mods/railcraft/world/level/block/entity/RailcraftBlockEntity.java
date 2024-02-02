package mods.railcraft.world.level.block.entity;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import mods.railcraft.api.core.BlockEntityLike;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.api.core.NetworkSerializable;
import mods.railcraft.api.core.Ownable;
import mods.railcraft.network.NetworkChannel;
import mods.railcraft.world.module.BlockModuleProvider;
import mods.railcraft.world.module.Module;
import mods.railcraft.world.module.ModuleDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class RailcraftBlockEntity extends BlockEntity
    implements NetworkSerializable, Ownable, BlockEntityLike, BlockModuleProvider {

  protected final ModuleDispatcher moduleDispatcher = new ModuleDispatcher();

  @Nullable
  private GameProfile owner;

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
  public final CompoundTag getUpdateTag() {
    CompoundTag nbt = super.getUpdateTag();
    FriendlyByteBuf packetBuffer = new FriendlyByteBuf(Unpooled.buffer());
    this.writeToBuf(packetBuffer);
    byte[] syncData = new byte[packetBuffer.readableBytes()];
    packetBuffer.readBytes(syncData);
    nbt.putByteArray("sync", syncData);
    return nbt;
  }

  @Override
  public final void handleUpdateTag(CompoundTag tag) {
    byte[] bytes = tag.getByteArray("sync");
    this.readFromBuf(new FriendlyByteBuf(Unpooled.wrappedBuffer(bytes)));
  }

  @Override
  public final void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket packet) {
    this.handleUpdateTag(packet.getTag());
  }

  @Override
  public void writeToBuf(FriendlyByteBuf out) {
    out.writeNullable(this.owner, FriendlyByteBuf::writeGameProfile);
    out.writeNullable(this.customName, FriendlyByteBuf::writeComponent);
    this.moduleDispatcher.writeToBuf(out);
  }

  @Override
  public void readFromBuf(FriendlyByteBuf in) {
    this.owner = in.readNullable(FriendlyByteBuf::readGameProfile);
    this.customName = in.readNullable(FriendlyByteBuf::readComponent);
    this.moduleDispatcher.readFromBuf(in);
  }

  @Override
  public BlockPos blockPos() {
    return this.getBlockPos();
  }

  @Override
  public Level level() {
    return this.getLevel();
  }

  @Override
  public void syncToClient() {
    if (this.level instanceof ServerLevel serverLevel) {
      var packet = this.getUpdatePacket();
      NetworkChannel.sendToTrackingChunk(packet, serverLevel, this.getBlockPos());
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

  public final void setOwner(@Nullable GameProfile profile) {
    this.owner = profile;
  }

  @Override
  @NotNull
  public final Optional<GameProfile> getOwner() {
    return Optional.ofNullable(this.owner);
  }

  public final boolean isOwner(@NotNull GameProfile gameProfile) {
    return gameProfile.equals(this.owner);
  }

  public final boolean isOwnerOrOperator(@NotNull GameProfile gameProfile) {
    return this.isOwner(gameProfile) || (!this.level.isClientSide()
        && ((ServerLevel) this.level).getServer().getPlayerList().isOp(gameProfile));
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    if (this.owner != null) {
      var ownerTag = new CompoundTag();
      NbtUtils.writeGameProfile(ownerTag, this.owner);
      tag.put(CompoundTagKeys.OWNER, ownerTag);
    }
    if (this.customName != null) {
      tag.putString(CompoundTagKeys.CUSTOM_NAME, Component.Serializer.toJson(this.customName));
    }

    tag.put(CompoundTagKeys.MODULES, this.moduleDispatcher.serializeNBT());
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    if (tag.contains(CompoundTagKeys.OWNER, Tag.TAG_COMPOUND)) {
      this.owner = NbtUtils.readGameProfile(tag.getCompound(CompoundTagKeys.OWNER));
    }
    if (tag.contains(CompoundTagKeys.CUSTOM_NAME, Tag.TAG_STRING)) {
      this.customName = Component.Serializer.fromJson(tag.getString(CompoundTagKeys.CUSTOM_NAME));
    }

    this.moduleDispatcher.deserializeNBT(tag.getCompound(CompoundTagKeys.MODULES));
  }

  public final int getX() {
    return this.getBlockPos().getX();
  }

  public final int getY() {
    return this.getBlockPos().getY();
  }

  public final int getZ() {
    return this.getBlockPos().getZ();
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
