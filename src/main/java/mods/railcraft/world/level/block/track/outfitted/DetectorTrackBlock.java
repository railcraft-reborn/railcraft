package mods.railcraft.world.level.block.track.outfitted;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.function.Supplier;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.util.PowerUtil;
import mods.railcraft.util.TrackShapeHelper;
import mods.railcraft.world.entity.cart.CartConstants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class DetectorTrackBlock extends OutfittedTrackBlock {

  public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
  public static final EnumProperty<Mode> MODE = EnumProperty.create("mode", Mode.class);

  public DetectorTrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(trackType, properties);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(this.getShapeProperty(), RailShape.NORTH_SOUTH)
        .setValue(POWERED, false)
        .setValue(MODE, Mode.BI_DIRECTIONAL));
  }

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(POWERED, MODE);
  }

  @Override
  public void tick(BlockState blockState, ServerWorld level, BlockPos blockPos, Random random) {
    blockState.getValue(MODE).updatePowerState(blockState, level, blockPos);
  }

  @Override
  public void onMinecartPass(BlockState blockState, World level, BlockPos blockPos,
      AbstractMinecartEntity cart) {
    super.onMinecartPass(blockState, level, blockPos, cart);
    blockState.getValue(MODE).updatePowerState(blockState, level, blockPos);
  }

  @Override
  protected boolean crowbarWhack(BlockState blockState, World level, BlockPos blockPos,
      PlayerEntity player, Hand hand, ItemStack itemStack) {
    Mode mode = blockState.getValue(MODE);
    if (player.isCrouching()) {
      mode = mode.previous();
    } else {
      mode = mode.next();
    }
    level.setBlockAndUpdate(blockPos, blockState.setValue(MODE, mode));
    return true;
  }

  @Override
  public int getSignal(BlockState blockState, IBlockReader level, BlockPos blockPos,
      Direction direction) {
    return blockState.getValue(POWERED) ? PowerUtil.FULL_POWER : PowerUtil.NO_POWER;
  }

  public enum Mode implements IStringSerializable {

    BI_DIRECTIONAL("bi_directional") {
      @Override
      protected void updatePowerState(BlockState blockState, World level, BlockPos blockPos) {
        setTrackPowering(blockState, level, blockPos, !findCarts(level, blockPos).isEmpty());
      }
    },
    TRAVEL("travel") {
      @Override
      protected void updatePowerState(BlockState blockState, World level, BlockPos blockPos) {
        updatePowerState(blockState, level, blockPos, false);
      }
    },
    TRAVEL_REVERSED("travel_reversed") {
      @Override
      protected void updatePowerState(BlockState blockState, World level, BlockPos blockPos) {
        updatePowerState(blockState, level, blockPos, true);
      }
    };

    private final String name;

    private Mode(String name) {
      this.name = name;
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }

    private Mode next() {
      return values()[(this.ordinal() + 1) % values().length];
    }

    private Mode previous() {
      return values()[(this.ordinal() + values().length - 1) % values().length];
    }

    protected abstract void updatePowerState(BlockState blockState, World level, BlockPos blockPos);

    private static void updatePowerState(BlockState blockState, World level, BlockPos blockPos,
        boolean reversed) {
      List<AbstractMinecartEntity> carts = findCarts(level, blockPos);
      if (carts.isEmpty()) {
        setTrackPowering(blockState, level, blockPos, false);
        return;
      }
      RailShape shape = getRailShapeRaw(blockState);
      Predicate<AbstractMinecartEntity> travelling = TrackShapeHelper.isEastWest(shape)
          ? cart -> reversed ? cart.getDeltaMovement().x() < 0.0D
              : cart.getDeltaMovement().x() > 0.0D
          : cart -> reversed ? cart.getDeltaMovement().z() > 0.0D
              : cart.getDeltaMovement().z() < 0.0D;
      setTrackPowering(blockState, level, blockPos, carts.stream().anyMatch(travelling));
    }

    private static void setTrackPowering(BlockState blockState, World level, BlockPos blockPos,
        boolean powered) {
      if (powered) {
        level.getBlockTicks().scheduleTick(blockPos, blockState.getBlock(),
            CartConstants.DETECTED_POWER_OUTPUT_FADE);
      }
      if (powered != blockState.getValue(POWERED)) {
        level.setBlockAndUpdate(blockPos, blockState.setValue(POWERED, powered));
      }
    }

    private static List<AbstractMinecartEntity> findCarts(World level, BlockPos blockPos) {
      return EntitySearcher.findMinecarts().around(blockPos).upTo(-0.2F).in(level);
    }
  }
}
