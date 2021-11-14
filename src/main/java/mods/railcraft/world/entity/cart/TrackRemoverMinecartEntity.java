package mods.railcraft.world.entity.cart;

import java.util.HashSet;
import java.util.Set;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class TrackRemoverMinecartEntity extends MaintenanceMinecartEntity {

  private final Set<BlockPos> tracksBehind = new HashSet<>();
  private final Set<BlockPos> tracksRemoved = new HashSet<>();

  public TrackRemoverMinecartEntity(EntityType<?> type, World world) {
    super(type, world);
  }

  public TrackRemoverMinecartEntity(ItemStack itemStack, double x, double y, double z,
      ServerWorld world) {
    super(RailcraftEntityTypes.TRACK_REMOVER.get(), x, y, z, world);
  }

  @Override
  protected void moveAlongTrack(BlockPos pos, BlockState state) {
    super.moveAlongTrack(pos, state);
    if (this.level.isClientSide()) {
      return;
    }

    for (BlockPos track : this.tracksBehind) {
      if (track.equals(pos)) {
        continue;
      }
      this.removeTrack(track);
    }
    this.tracksBehind.removeAll(this.tracksRemoved);
    this.tracksRemoved.clear();

    this.addTravelledTrack(pos);
  }

  private void addTravelledTrack(BlockPos pos) {
    this.tracksBehind.add(pos);
  }

  private void removeTrack(BlockPos track) {
    if (this.getMode() == Mode.TRANSPORT) {
      return;
    }
    if (track.distSqr(this.blockPosition()) >= 9) {
      this.tracksRemoved.add(track);
    } else if (!AbstractRailBlock.isRail(this.level, track)) {
      this.tracksRemoved.add(track);
    } else if (this.level.getBlockState(track).is(RailcraftBlocks.FORCE_TRACK.get())) {
      this.tracksRemoved.add(track);
    } else if (EntitySearcher.findMinecarts().around(track).outTo(0.2f).in(this.level).isEmpty()) {
      this.removeOldTrack(track, this.level.getBlockState(track));
      this.blink();
      this.tracksRemoved.add(track);
    }
  }

  @Override
  protected boolean hasMenu() {
    return false;
  }

  @Override
  protected Container createMenu(int id, PlayerInventory playerInventory) {
    return null;
  }

  @Override
  public ItemStack getCartItem() {
    return RailcraftItems.TRACK_REMOVER.get().getDefaultInstance();
  }
}
