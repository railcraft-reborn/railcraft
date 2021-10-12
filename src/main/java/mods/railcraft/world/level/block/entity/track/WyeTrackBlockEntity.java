package mods.railcraft.world.level.block.entity.track;

import java.util.List;
import java.util.UUID;
import mods.railcraft.api.track.ArrowDirection;
import mods.railcraft.world.entity.cart.CartTools;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.track.outfitted.SwitchTrackBlock;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;

public class WyeTrackBlockEntity extends SwitchTrackBlockEntity {

  public WyeTrackBlockEntity() {
    this(RailcraftBlockEntityTypes.WYE_TRACK.get());
  }

  public WyeTrackBlockEntity(TileEntityType<?> type) {
    super(type);
  }

  @Override
  public ArrowDirection getRedArrowDirection() {
    final boolean switched = SwitchTrackBlock.isSwitched(this.getBlockState());
    final Direction facing = SwitchTrackBlock.getFacing(this.getBlockState());
    return ArrowDirection.fromHorizontalDirection(
        switched ? facing.getCounterClockWise() : facing.getClockWise());
  }

  @Override
  public ArrowDirection getWhiteArrowDirection() {
    return ArrowDirection.fromHorizontalDirection(
        SwitchTrackBlock.getFacing(this.getBlockState()).getOpposite());
  }

  @Override
  protected List<UUID> getCartsAtLockEntrance() {
    return CartTools.getMinecartUUIDsAt(this.level,
        this.getBlockPos().relative(
            SwitchTrackBlock.getFacing(this.getBlockState()).getClockWise()),
        0.1F);
  }

  @Override
  protected List<UUID> getCartsAtDecisionEntrance() {
    return CartTools.getMinecartUUIDsAt(this.level,
        this.getBlockPos().relative(SwitchTrackBlock.getFacing(this.getBlockState()).getOpposite()),
        0.1F);
  }

  @Override
  protected List<UUID> getCartsAtSpringEntrance() {
    return CartTools.getMinecartUUIDsAt(this.level,
        this.getBlockPos().relative(
            SwitchTrackBlock.getFacing(this.getBlockState()).getCounterClockWise()),
        0.1F);
  }

  @Override
  public Direction getActuatorDirection() {
    return SwitchTrackBlock.getFacing(this.getBlockState());
  }
}
