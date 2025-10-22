package mods.railcraft.world.entity.vehicle;

import java.util.HashSet;
import java.util.Set;
import mods.railcraft.api.carts.CartAdvanceable;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;

public class TrackRemover extends MaintenanceMinecart implements CartAdvanceable {

  private final Set<BlockPos> tracksBehind = new HashSet<>();
  private final Set<BlockPos> tracksRemoved = new HashSet<>();

  public TrackRemover(EntityType<?> type, Level level) {
    super(type, level);
  }

  public TrackRemover(ItemStack itemStack, Level level, double x, double y, double z) {
    super(itemStack, RailcraftEntityTypes.TRACK_REMOVER.get(), level, x, y, z);
  }

  @Override
  public void advanceOnTrack(ServerLevel serverLevel) {
    for (var track : this.tracksBehind) {
      if (track.equals(this.blockPosition())) {
        continue;
      }
      this.removeTrack(track);
    }
    this.tracksBehind.removeAll(this.tracksRemoved);
    this.tracksRemoved.clear();

    this.addTravelledTrack(this.blockPosition());
  }

  @Override
  protected void moveAlongTrack(ServerLevel serverLevel) {
    super.moveAlongTrack(serverLevel);
    this.advanceOnTrack(serverLevel);
  }

  private void addTravelledTrack(BlockPos pos) {
    this.tracksBehind.add(pos);
  }

  private void removeTrack(BlockPos track) {
    if (this.mode() == Mode.OFF) {
      return;
    }
    if (track.distSqr(this.blockPosition()) >= 9) {
      this.tracksRemoved.add(track);
    } else if (!BaseRailBlock.isRail(this.level(), track)) {
      this.tracksRemoved.add(track);
    } else if (this.level().getBlockState(track).is(RailcraftBlocks.FORCE_TRACK.get())) {
      this.tracksRemoved.add(track);
    } else if (EntitySearcher.findMinecarts().at(track).inflate(0.2f).list(this.level()).isEmpty()) {
      this.removeOldTrack(track, this.level().getBlockState(track));
      this.blink();
      this.tracksRemoved.add(track);
    }
  }

  @Override
  protected boolean hasMenu() {
    return false;
  }

  @Override
  protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
    return null;
  }

  @Override
  public Item getDropItem() {
    return RailcraftItems.TRACK_REMOVER.get();
  }
}
