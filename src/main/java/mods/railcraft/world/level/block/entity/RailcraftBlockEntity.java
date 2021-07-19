package mods.railcraft.world.level.block.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import mods.railcraft.api.core.INetworkedObject;
import mods.railcraft.api.core.RailcraftFakePlayer;
import mods.railcraft.api.signals.ITile;
import mods.railcraft.network.PacketBuilder;
import mods.railcraft.plugins.PlayerPlugin;
import mods.railcraft.util.AdjacentBlockEntityCache;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public abstract class RailcraftBlockEntity extends TileEntity implements INetworkedObject, ITile {

  protected final AdjacentBlockEntityCache adjacentCache = new AdjacentBlockEntityCache(this);

  private GameProfile owner = RailcraftFakePlayer.UNKNOWN_USER_PROFILE;
  private @Nullable UUID uuid;

  @Nullable
  private ITextComponent customName;

  public RailcraftBlockEntity(TileEntityType<?> type) {
    super(type);
  }

  public static boolean isUsableByPlayerHelper(TileEntity tile, PlayerEntity player) {
    return !tile.isRemoved() && tile.getLevel().getBlockEntity(tile.getBlockPos()) == tile
        && player.distanceToSqr(tile.getBlockPos().getX(), tile.getBlockPos().getY(),
            tile.getBlockPos().getZ()) <= 64;
  }

  public UUID getUUID() {
    if (uuid == null)
      uuid = UUID.randomUUID();
    return uuid;
  }

  public AdjacentBlockEntityCache getAdjacentCache() {
    return this.adjacentCache;
  }

  @Override
  public final SUpdateTileEntityPacket getUpdatePacket() {
    return new SUpdateTileEntityPacket(getBlockPos(), 0, getUpdateTag());
  }

  @Override
  public final CompoundNBT getUpdateTag() {
    CompoundNBT nbt = super.getUpdateTag();
    PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());

    writePacketData(packetBuffer);
    byte[] syncData = new byte[packetBuffer.readableBytes()];
    packetBuffer.readBytes(syncData);
    nbt.putByteArray("sync", syncData);

    return nbt;
  }

  @Override
  public final void handleUpdateTag(BlockState blockState, CompoundNBT nbt) {
    byte[] bytes = nbt.getByteArray("sync");
    readPacketData(new PacketBuffer(Unpooled.wrappedBuffer(bytes)));
  }

  @Override
  public final void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
    handleUpdateTag(this.getBlockState(), pkt.getTag());
  }

  @Override
  public void markBlockForUpdate() {
    if (this.hasLevel()) {
      BlockState state = getBlockState();
      if (state.getBlock().hasTileEntity(state))
        this.level.sendBlockUpdated(this.getBlockPos(), state, state, Constants.BlockFlags.DEFAULT);
    }
  }

  @Override
  public void sendUpdateToClient() {
    PacketBuilder.instance().sendTileEntityPacket(this);
  }

  @OverridingMethodsMustInvokeSuper
  @Override
  public void setPlacedBy(BlockState state, @Nullable LivingEntity placer,
      ItemStack stack) {
    if (placer instanceof PlayerEntity)
      owner = ((PlayerEntity) placer).getGameProfile();
    notifyBlocksOfNeighborChange();
  }

  @Override
  public void neighborChanged(BlockState state, Block neighborBlock, BlockPos neighborPos) {
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
    setOwner(RailcraftFakePlayer.UNKNOWN_USER_PROFILE);
  }

  protected final void setOwner(GameProfile profile) {
    owner = profile;
    // sendUpdateToClient(); Sending this when a te is initialized will cause client net handler
    // errors because the tile is not yet on client
  }

  @Override
  public final GameProfile getOwner() {
    return owner;
  }

  public final boolean isOwner(GameProfile player) {
    return PlayerPlugin.isSamePlayer(owner, player);
  }

  public List<String> getDebugOutput() {
    List<String> debug = new ArrayList<>();
    debug.add("Railcraft Tile Entity Data Dump");
    debug.add("Object: " + this);
    if (!this.level.getGameRules().getBoolean(GameRules.RULE_REDUCEDDEBUGINFO))
      debug.add(String.format("Coordinates: d=%d, %s", this.getLevel().dimension(), getBlockPos()));
    debug.add("Owner: " + owner.getName());
    debug.addAll(adjacentCache.getDebugOutput());
    return debug;
  }

  @Override
  @OverridingMethodsMustInvokeSuper
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    PlayerPlugin.writeOwnerToNBT(data, owner);
    if (this.uuid != null)
      data.putUUID("id", this.uuid);
    if (this.customName != null)
      data.putString("customName", ITextComponent.Serializer.toJson(this.customName));
    return data;
  }

  @Override
  @OverridingMethodsMustInvokeSuper
  public void load(BlockState blockState, CompoundNBT data) {
    super.load(blockState, data);
    owner = PlayerPlugin.readOwnerFromNBT(data);
    if (data.hasUUID("id"))
      uuid = data.getUUID("id");
    if (data.contains("customName", Constants.NBT.TAG_STRING))
      customName = ITextComponent.Serializer.fromJson(data.getString("customName"));
  }

  public final int getX() {
    return getBlockPos().getX();
  }

  public final int getY() {
    return getBlockPos().getY();
  }

  public final int getZ() {
    return getBlockPos().getZ();
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
    return hasCustomName() ? customName : this.getBlockState().getBlock().getName();
  }

  @Override
  public ITextComponent getDisplayName() {
    return this.getName();
  }

  @Override
  public final World theWorld() {
    return this.level;
  }
}
