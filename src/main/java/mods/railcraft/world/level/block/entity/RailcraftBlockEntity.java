package mods.railcraft.world.level.block.entity;

import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import mods.railcraft.api.core.BlockEntityLike;
import mods.railcraft.api.core.NetworkSerializable;
import mods.railcraft.api.core.Ownable;
import mods.railcraft.network.PacketBuilder;
import mods.railcraft.world.level.block.entity.module.Module;
import mods.railcraft.world.level.block.entity.module.ModuleDispatcher;
import mods.railcraft.world.level.block.entity.module.ModuleProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public abstract class RailcraftBlockEntity extends BlockEntity
    implements NetworkSerializable, Ownable, BlockEntityLike, ModuleProvider {

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
    if (this.owner == null) {
      out.writeBoolean(true);
    } else {
      out.writeBoolean(false);
      out.writeUUID(this.owner.getId());
      out.writeUtf(this.owner.getName(), 16);
    }

    this.moduleDispatcher.writeToBuf(out);
  }

  @Override
  public void readFromBuf(FriendlyByteBuf in) {
    if (in.readBoolean()) {
      this.owner = null;
    } else {
      UUID ownerId = in.readUUID();
      String ownerName = in.readUtf(16);
      this.owner = new GameProfile(ownerId, ownerName);
    }

    this.moduleDispatcher.readFromBuf(in);
  }

  @Override
  public void syncToClient() {
    PacketBuilder.instance().sendTileEntityPacket(this);
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
  public boolean stillValid(Player player) {
    return stillValid(this, player);
  }

  public final void setOwner(@Nullable GameProfile profile) {
    this.owner = profile;
  }

  @Override
  @Nullable
  public final Optional<GameProfile> getOwner() {
    return Optional.ofNullable(this.owner);
  }

  public final boolean isOwner(@Nonnull GameProfile gameProfile) {
    return gameProfile.equals(this.owner);
  }

  public final boolean isOwnerOrOperator(@Nonnull GameProfile gameProfile) {
    return this.isOwner(gameProfile) || (!this.level.isClientSide()
        && ((ServerLevel) this.level).getServer().getPlayerList().isOp(gameProfile));
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    if (this.owner != null) {
      var ownerTag = new CompoundTag();
      NbtUtils.writeGameProfile(ownerTag, this.owner);
      tag.put("owner", ownerTag);
    }
    if (this.customName != null) {
      tag.putString("customName", Component.Serializer.toJson(this.customName));
    }

    tag.put("modules", this.moduleDispatcher.serializeNBT());
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    if (tag.contains("owner", Tag.TAG_COMPOUND)) {
      this.owner = NbtUtils.readGameProfile(tag.getCompound("owner"));
    }
    if (tag.contains("customName", Tag.TAG_STRING)) {
      this.customName = Component.Serializer.fromJson(tag.getString("customName"));
    }

    this.moduleDispatcher.deserializeNBT(tag.getCompound("modules"));
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    return this.moduleDispatcher.getCapability(cap, side);
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

  public void setCustomName(@Nullable Component name) {
    this.customName = name;
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

  public static boolean stillValid(BlockEntity blockEntity, Player player) {
    return !blockEntity.isRemoved()
        && blockEntity.getLevel().getBlockEntity(blockEntity.getBlockPos()) == blockEntity
        && player.distanceToSqr(blockEntity.getBlockPos().getX(), blockEntity.getBlockPos().getY(),
            blockEntity.getBlockPos().getZ()) <= 64;
  }
}
