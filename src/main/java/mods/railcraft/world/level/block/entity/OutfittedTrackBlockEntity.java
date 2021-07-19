package mods.railcraft.world.level.block.entity;

import mods.railcraft.api.tracks.IOutfittedTrackTile;
import mods.railcraft.api.tracks.ITrackKitInstance;
import mods.railcraft.api.tracks.TrackKit;
import mods.railcraft.api.tracks.TrackKitMissing;
import mods.railcraft.api.tracks.TrackRegistry;
import mods.railcraft.world.item.IMagnifiable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

public class OutfittedTrackBlockEntity extends RailcraftBlockEntity
    implements IOutfittedTrackTile, IMagnifiable {

  private ITrackKitInstance trackKitInstance = new TrackKitMissing(false);

  public OutfittedTrackBlockEntity() {
    super(RailcraftBlockEntityTypes.OUTFITTED_TRACK.get());
  }

  public OutfittedTrackBlockEntity(TileEntityType<?> type) {
    super(type);
  }

  @Override
  public ITrackKitInstance getTrackKitInstance() {
    return this.trackKitInstance;
  }

  public void setTrackKitInstance(ITrackKitInstance trackKitInstance) {
    this.trackKitInstance = trackKitInstance;
    this.trackKitInstance.setTile(this);
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    data.putString(TrackKit.NBT_TAG, getTrackKitInstance().getTrackKit().getSerializedName());
    this.trackKitInstance.writeToNBT(data);
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);

    if (data.contains(TrackKit.NBT_TAG, Constants.NBT.TAG_STRING)) {
      TrackKit trackKit = TrackRegistry.TRACK_KIT.get(data);
      setTrackKitInstance(trackKit.createInstance());
    }

    this.trackKitInstance.setTile(this);
    this.trackKitInstance.readFromNBT(data);
  }

  @Override
  public void setPlacedBy(BlockState state, LivingEntity placer, ItemStack stack) {
    super.setPlacedBy(state, placer, stack);
    this.trackKitInstance.setPlacedBy(state, placer, stack);
  }

  @Override
  public void neighborChanged(BlockState state, Block neighborBlock, BlockPos neighborPos) {
    super.neighborChanged(state, neighborBlock, neighborPos);
    this.trackKitInstance.neighborChanged(state, neighborBlock);
  }

  @Override
  public void writePacketData(PacketBuffer data) {
    super.writePacketData(data);
    data.writeInt(TrackRegistry.TRACK_TYPE.getId(getTrackType()));
    data.writeInt(TrackRegistry.TRACK_KIT.getId(getTrackKitInstance().getTrackKit()));
    trackKitInstance.writePacketData(data);
  }

  @Override
  public void readPacketData(PacketBuffer data) {
    super.readPacketData(data);
    boolean needsUpdate = false;
    TrackKit kit = TrackRegistry.TRACK_KIT.get(data.readInt());
    if (getTrackKitInstance().getTrackKit() != kit) {
      setTrackKitInstance(kit.createInstance());
      needsUpdate = true;
    }
    if (needsUpdate)
      markBlockForUpdate();
    trackKitInstance.readPacketData(data);
  }

  @Override
  public void onMagnify(PlayerEntity viewer) {
    if (trackKitInstance instanceof IMagnifiable) {
      ((IMagnifiable) trackKitInstance).onMagnify(viewer);
    }
  }
}
