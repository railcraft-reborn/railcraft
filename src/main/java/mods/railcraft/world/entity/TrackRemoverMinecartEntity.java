package mods.railcraft.world.entity;

import java.util.HashSet;
import java.util.Set;
import mods.railcraft.api.core.CollectionToolsAPI;
import mods.railcraft.plugins.WorldPlugin;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TrackRemoverMinecartEntity extends AbstractMaintenanceMinecartEntity {

  private final Set<BlockPos> tracksBehind = CollectionToolsAPI.blockPosSet(HashSet::new);
  private final Set<BlockPos> tracksRemoved = CollectionToolsAPI.blockPosSet(HashSet::new);

  public TrackRemoverMinecartEntity(EntityType<?> type, World world) {
    super(type, world);
  }

  public TrackRemoverMinecartEntity(double x, double y, double z, World world) {
    super(RailcraftEntityTypes.MOW_TRACK_REMOVER.get(), x, y, z, world);
  }

  @Override
  protected void moveAlongTrack(BlockPos pos, BlockState state) {
    super.moveAlongTrack(pos, state);
    if (this.level.isClientSide())
      return;

    for (BlockPos track : this.tracksBehind) {
      if (track.equals(pos))
        continue;
      this.removeTrack(track);
    }
    this.tracksBehind.removeAll(this.tracksRemoved);
    this.tracksRemoved.clear();

    this.addTravelledTrack(pos);
  }

  private void addTravelledTrack(BlockPos pos) {
    tracksBehind.add(pos);
  }

  private void removeTrack(BlockPos track) {
    if (this.getMode() == CartMode.TRANSPORT)
      return;
    if (track.distSqr(this.blockPosition()) >= 9)
      this.tracksRemoved.add(track);
    else if (!AbstractRailBlock.isRail(this.level, track))
      this.tracksRemoved.add(track);
    else if (WorldPlugin.isBlockAt(this.level, track, RailcraftBlocks.FORCE_TRACK.get()))
      this.tracksRemoved.add(track);
    else if (EntitySearcher.findMinecarts().around(track).outTo(0.2f).in(this.level).isEmpty()) {
      this.removeOldTrack(track, this.level.getBlockState(track));
      this.blink();
      this.tracksRemoved.add(track);
    }
  }

  @Override
  protected Container createMenu(int id, PlayerInventory playerInventory) {
    return null;
  }

  @Override
  public ItemStack getContents() {
    return ItemStack.EMPTY;
  }

  @Override
  public Item getItem() {
    return RailcraftItems.MOW_TRACK_REMOVER.get();
  }
}
