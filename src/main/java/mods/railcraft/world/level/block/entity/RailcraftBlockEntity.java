package mods.railcraft.world.level.block.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import mods.railcraft.api.core.Ownable;
import mods.railcraft.api.core.RailcraftFakePlayer;
import mods.railcraft.api.core.Syncable;
import mods.railcraft.api.signals.BlockEntityLike;
import mods.railcraft.network.PacketBuilder;
import mods.railcraft.plugins.PlayerPlugin;
import mods.railcraft.util.AdjacentBlockEntityCache;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.GameRules;
import net.minecraftforge.common.util.Constants;

public abstract class RailcraftBlockEntity extends TileEntity
    implements Syncable, Ownable, BlockEntityLike {

  protected final AdjacentBlockEntityCache adjacentCache = new AdjacentBlockEntityCache(this);

  private GameProfile owner = RailcraftFakePlayer.UNKNOWN_USER_PROFILE;

  @Nullable
  private UUID id;

  @Nullable
  private ITextComponent customName;

  public RailcraftBlockEntity(TileEntityType<?> type) {
    super(type);
  }

  public UUID getId() {
    if (this.id == null)
      this.id = UUID.randomUUID();
    return this.id;
  }

  public AdjacentBlockEntityCache getAdjacentCache() {
    return this.adjacentCache;
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
  public void syncToClient() {
    PacketBuilder.instance().sendTileEntityPacket(this);
  }

  public void resetAdjacentCacheTimers() {
    this.adjacentCache.resetTimers();
  }

  @Override
  public void setRemoved() {
    this.adjacentCache.purge();
    super.setRemoved();
  }

  @Override
  public void clearRemoved() {
    this.adjacentCache.purge();
    super.clearRemoved();
  }

  public final void clearOwner() {
    this.setOwner(RailcraftFakePlayer.UNKNOWN_USER_PROFILE);
  }

  protected final void setOwner(GameProfile profile) {
    this.owner = profile;
    // sendUpdateToClient(); Sending this when a te is initialized will cause client net handler
    // errors because the tile is not yet on client
  }

  @Override
  public final GameProfile getOwner() {
    return this.owner;
  }

  public final boolean isOwner(GameProfile player) {
    return PlayerPlugin.isSamePlayer(this.owner, player);
  }

  public List<String> getDebugOutput() {
    List<String> debug = new ArrayList<>();
    debug.add("Railcraft Tile Entity Data Dump");
    debug.add("Object: " + this);
    if (!this.level.getGameRules().getBoolean(GameRules.RULE_REDUCEDDEBUGINFO))
      debug.add(String.format("Coordinates: d=%d, %s", this.getLevel().dimension(), getBlockPos()));
    debug.add("Owner: " + this.owner.getName());
    debug.addAll(this.adjacentCache.getDebugOutput());
    return debug;
  }

  @Override
  @OverridingMethodsMustInvokeSuper
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    PlayerPlugin.writeOwnerToNBT(data, this.owner);
    if (this.id != null)
      data.putUUID("id", this.id);
    if (this.customName != null)
      data.putString("customName", ITextComponent.Serializer.toJson(this.customName));
    return data;
  }

  @Override
  @OverridingMethodsMustInvokeSuper
  public void load(BlockState blockState, CompoundNBT data) {
    super.load(blockState, data);
    this.owner = PlayerPlugin.readOwnerFromNBT(data);
    if (data.hasUUID("id"))
      this.id = data.getUUID("id");
    if (data.contains("customName", Constants.NBT.TAG_STRING))
      this.customName = ITextComponent.Serializer.fromJson(data.getString("customName"));
  }

  public void updateNeighbors() {
    if (this.hasLevel())
      this.level.updateNeighborsAt(this.getBlockPos(), this.getBlockState().getBlock());
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

  public static boolean isUsableByPlayerHelper(TileEntity tile, PlayerEntity player) {
    return !tile.isRemoved() && tile.getLevel().getBlockEntity(tile.getBlockPos()) == tile
        && player.distanceToSqr(tile.getBlockPos().getX(), tile.getBlockPos().getY(),
            tile.getBlockPos().getZ()) <= 64;
  }
}
