package mods.railcraft.world.level.block.track.outfitted;

import java.util.function.Supplier;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.util.TrackShapeHelper;
import mods.railcraft.world.entity.cart.LocomotiveEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class ControlTrackBlock extends PoweredTrackBlock {

  private static final double BOOST_AMOUNT = 0.02;
  private static final double SLOW_AMOUNT = 0.02;

  public static final BooleanProperty REVERSED = ReversibleOutfittedTrackBlock.REVERSED;

  public ControlTrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(trackType, properties);
    this.registerDefaultState(this.stateDefinition.any().setValue(REVERSED, false));
  }

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(REVERSED);
  }

  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context) {
    return super.getStateForPlacement(context)
        .setValue(REVERSED, context.getHorizontalDirection() == Direction.SOUTH
            || context.getHorizontalDirection() == Direction.WEST);
  }

  @Override
  public int getPowerPropagation(BlockState blockState, World level, BlockPos pos) {
    return 16;
  }

  @Override
  public void onMinecartPass(BlockState blockState, World level, BlockPos pos,
      AbstractMinecartEntity cart) {
    final RailShape trackShape = getRailShapeRaw(blockState);
    final Vector3d deltaMovement = cart.getDeltaMovement();
    final boolean powered = isPowered(blockState);
    final boolean reversed = isReversed(blockState);
    if (TrackShapeHelper.isNorthSouth(trackShape)) {
      if (deltaMovement.z() <= 0) {
        if (isPowered(blockState) ^ !reversed) {
          cart.setDeltaMovement(deltaMovement.subtract(0.0D, 0.0D, BOOST_AMOUNT));
        } else {
          cart.setDeltaMovement(deltaMovement.add(0.0D, 0.0D, SLOW_AMOUNT));
        }
      } else if (deltaMovement.z() >= 0) {
        if (!powered ^ !reversed) {
          cart.setDeltaMovement(deltaMovement.add(0.0D, 0.0D, BOOST_AMOUNT));
        } else {
          cart.setDeltaMovement(deltaMovement.subtract(0.0D, 0.0D, SLOW_AMOUNT));
        }
      }
    } else {
      if (deltaMovement.x() <= 0) {
        if (powered ^ reversed) {
          cart.setDeltaMovement(deltaMovement.subtract(BOOST_AMOUNT, 0.0D, 0.0D));
        } else {
          cart.setDeltaMovement(deltaMovement.add(SLOW_AMOUNT, 0.0D, 0.0D));
        }
      } else if (deltaMovement.x() >= 0) {
        if (!powered ^ reversed) {
          cart.setDeltaMovement(deltaMovement.add(BOOST_AMOUNT, 0.0D, 0.0D));
        } else {
          cart.setDeltaMovement(deltaMovement.subtract(SLOW_AMOUNT, 0.0D, 0.0D));
        }
      }
    }

    if (cart instanceof LocomotiveEntity && ((LocomotiveEntity) cart).isShutdown()) {
      double yaw = cart.yRot * Math.PI / 180D;
      double cos = Math.cos(yaw);
      double sin = Math.sin(yaw);
      float limit = 0.01f;
      if ((deltaMovement.x() > limit && cos < 0)
          || (deltaMovement.x() < -limit && cos > 0)
          || (deltaMovement.z() > limit && sin < 0)
          || (deltaMovement.z() < -limit && sin > 0)) {
        cart.yRot += 180D;
        cart.yRot = cart.yRot % 360.0F;
        cart.yRotO = cart.yRot;
      }
    }
  }

  @Override
  protected boolean crowbarWhack(BlockState blockState, World level, BlockPos pos,
      PlayerEntity player, Hand hand, ItemStack itemStack) {
    level.setBlockAndUpdate(pos, blockState.setValue(REVERSED, !blockState.getValue(REVERSED)));
    return true;
  }

  public static boolean isReversed(BlockState blockState) {
    return blockState.getValue(REVERSED);
  }
}
