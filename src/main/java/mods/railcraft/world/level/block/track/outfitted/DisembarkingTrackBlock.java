package mods.railcraft.world.level.block.track.outfitted;

import java.util.function.Supplier;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.world.entity.vehicle.CartTools;
import mods.railcraft.world.entity.vehicle.MinecartExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.RailShape;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class DisembarkingTrackBlock extends PoweredOutfittedTrackBlock {

  private static final int TIME_TILL_NEXT_MOUNT = 40;

  public static final BooleanProperty MIRRORED = BooleanProperty.create("mirrored");

  public DisembarkingTrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(trackType, properties);
  }

  @Override
  protected BlockState buildDefaultState(BlockState blockState) {
    return super.buildDefaultState(blockState)
        .setValue(MIRRORED, false);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(MIRRORED);
  }

  @Override
  protected boolean crowbarWhack(BlockState blockState, Level level, BlockPos pos,
      Player player, InteractionHand hand, ItemStack itemStack) {
    level.setBlockAndUpdate(pos, blockState.cycle(MIRRORED));
    return true;
  }

  @Override
  public void onMinecartPass(BlockState blockState, Level level, BlockPos blockPos,
      AbstractMinecart cart) {
    super.onMinecartPass(blockState, level, blockPos, cart);
    if (isPowered(blockState) && cart.isVehicle()) {
      double x = blockPos.getX();
      double z = blockPos.getZ();
      double offset = 1;
      var railShape = getRailShapeRaw(blockState);
      var mirrored = isMirrored(blockState);
      if (railShape == RailShape.NORTH_SOUTH) {
        if (mirrored) {
          x += offset;
        } else {
          x -= offset;
        }
      } else if (mirrored) {
        z += offset;
      } else {
        z -= offset;
      }
      CartTools.removePassengers(cart, x + 0.5D, blockPos.getY() + 1.0D, z + 0.5D);
      MinecartExtension.getOrThrow(cart).setPreventMountRemainingTicks(TIME_TILL_NEXT_MOUNT);
    }
  }

  public static boolean isMirrored(BlockState blockState) {
    return blockState.getValue(MIRRORED);
  }
}
