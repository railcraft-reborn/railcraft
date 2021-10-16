package mods.railcraft.world.level.block.entity;

import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import mods.railcraft.api.core.BlockEntityLike;
import mods.railcraft.api.core.Ownable;
import mods.railcraft.api.core.Syncable;
import mods.railcraft.network.PacketBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

public abstract class RailcraftBlockEntity extends TileEntity
    implements Syncable, Ownable, BlockEntityLike {

  @Nullable
  private GameProfile owner;

  @Nullable
  private ITextComponent customName;

  public RailcraftBlockEntity(TileEntityType<?> type) {
    super(type);
  }

  @Override
  public final SUpdateTileEntityPacket getUpdatePacket() {
    return new SUpdateTileEntityPacket(this.getBlockPos(), 0, this.getUpdateTag());
  }

  @Override
  public final CompoundNBT getUpdateTag() {
    CompoundNBT nbt = super.getUpdateTag();
    PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
    this.writeSyncData(packetBuffer);
    byte[] syncData = new byte[packetBuffer.readableBytes()];
    packetBuffer.readBytes(syncData);
    nbt.putByteArray("sync", syncData);
    return nbt;
  }

  @Override
  public final void handleUpdateTag(BlockState blockState, CompoundNBT nbt) {
    byte[] bytes = nbt.getByteArray("sync");
    this.readSyncData(new PacketBuffer(Unpooled.wrappedBuffer(bytes)));
  }

  @Override
  public final void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
    this.handleUpdateTag(this.getBlockState(), pkt.getTag());
  }

  @Override
  public void writeSyncData(PacketBuffer data) {
    if (this.owner == null) {
      data.writeBoolean(true);
    } else {
      data.writeBoolean(false);
      data.writeUUID(this.owner.getId());
      data.writeUtf(this.owner.getName(), 16);
    }
  }

  @Override
  public void readSyncData(PacketBuffer data) {
    if (data.readBoolean()) {
      this.owner = null;
    } else {
      UUID ownerId = data.readUUID();
      String ownerName = data.readUtf(16);
      this.owner = new GameProfile(ownerId, ownerName);
    }
  }

  @Override
  public void syncToClient() {
    PacketBuilder.instance().sendTileEntityPacket(this);
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
        && ((ServerWorld) this.level).getServer().getPlayerList().isOp(gameProfile));
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    if (this.owner != null) {
      CompoundNBT ownerTag = new CompoundNBT();
      NBTUtil.writeGameProfile(ownerTag, this.owner);
      data.put("owner", ownerTag);
    }
    if (this.customName != null) {
      data.putString("customName", ITextComponent.Serializer.toJson(this.customName));
    }
    return data;
  }

  @Override
  public void load(BlockState blockState, CompoundNBT data) {
    super.load(blockState, data);
    if (data.contains("owner", Constants.NBT.TAG_COMPOUND)) {
      this.owner = NBTUtil.readGameProfile(data.getCompound("owner"));
    }
    if (data.contains("customName", Constants.NBT.TAG_STRING)) {
      this.customName = ITextComponent.Serializer.fromJson(data.getString("customName"));
    }
  }

  public void updateNeighbors() {
    if (this.hasLevel()) {
      this.level.updateNeighborsAt(this.getBlockPos(), this.getBlockState().getBlock());
    }
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

  public void setCustomName(@Nullable ITextComponent name) {
    this.customName = name;
  }

  @Override
  public ITextComponent getName() {
    return this.hasCustomName() ? this.customName : this.getBlockState().getBlock().getName();
  }

  @Override
  public ITextComponent getDisplayName() {
    return this.getName();
  }

  @Override
  public final TileEntity asBlockEntity() {
    return this;
  }

  public static boolean stillValid(TileEntity tile, PlayerEntity player) {
    return !tile.isRemoved() && tile.getLevel().getBlockEntity(tile.getBlockPos()) == tile
        && player.distanceToSqr(tile.getBlockPos().getX(), tile.getBlockPos().getY(),
            tile.getBlockPos().getZ()) <= 64;
  }
}
