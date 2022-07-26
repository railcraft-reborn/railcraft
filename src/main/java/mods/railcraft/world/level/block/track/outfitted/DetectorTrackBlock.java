package mods.railcraft.world.level.block.track.outfitted;

import mods.railcraft.api.track.RailShapeUtil;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.util.PowerUtil;
import mods.railcraft.world.entity.vehicle.CartConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.RailShape;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class DetectorTrackBlock extends OutfittedTrackBlock {

  public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
  public static final EnumProperty<Mode> MODE = EnumProperty.create("mode", Mode.class);

  public DetectorTrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(trackType, properties);
  }

  @Override
  protected BlockState buildDefaultState(BlockState blockState) {
    return super.buildDefaultState(blockState)
        .setValue(POWERED, false)
        .setValue(MODE, Mode.BI_DIRECTIONAL);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(POWERED, MODE);
  }

  @Override
  public void tick(BlockState blockState, ServerLevel level, BlockPos blockPos, RandomSource random) {
    blockState.getValue(MODE).updatePowerState(blockState, level, blockPos);
  }

  @Override
  public void onMinecartPass(BlockState blockState, Level level, BlockPos blockPos,
      AbstractMinecart cart) {
    super.onMinecartPass(blockState, level, blockPos, cart);
    blockState.getValue(MODE).updatePowerState(blockState, level, blockPos);
  }

  @Override
  protected boolean crowbarWhack(BlockState blockState, Level level, BlockPos blockPos,
      Player player, InteractionHand hand, ItemStack itemStack) {
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
  public int getSignal(BlockState blockState, BlockGetter level, BlockPos blockPos,
      Direction direction) {
    return blockState.getValue(POWERED) ? PowerUtil.FULL_POWER : PowerUtil.NO_POWER;
  }

  public enum Mode implements StringRepresentable {

    BI_DIRECTIONAL("bi_directional") {
      @Override
      protected void updatePowerState(BlockState blockState, Level level, BlockPos blockPos) {
        setTrackPowering(blockState, level, blockPos, !findCarts(level, blockPos).isEmpty());
      }
    },
    TRAVEL("travel") {
      @Override
      protected void updatePowerState(BlockState blockState, Level level, BlockPos blockPos) {
        updatePowerState(blockState, level, blockPos, false);
      }
    },
    TRAVEL_REVERSED("travel_reversed") {
      @Override
      protected void updatePowerState(BlockState blockState, Level level, BlockPos blockPos) {
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

    protected void updatePowerState(BlockState blockState, Level level, BlockPos blockPos) {
      return;
    }

    protected static void updatePowerState(BlockState blockState, Level level, BlockPos blockPos,
        boolean reversed) {
      List<AbstractMinecart> carts = findCarts(level, blockPos);
      if (carts.isEmpty()) {
        setTrackPowering(blockState, level, blockPos, false);
        return;
      }
      RailShape shape = getRailShapeRaw(blockState);
      Predicate<AbstractMinecart> travelling = RailShapeUtil.isEastWest(shape)
          ? cart -> reversed ? cart.getDeltaMovement().x() < 0.0D
              : cart.getDeltaMovement().x() > 0.0D
          : cart -> reversed ? cart.getDeltaMovement().z() > 0.0D
              : cart.getDeltaMovement().z() < 0.0D;
      setTrackPowering(blockState, level, blockPos, carts.stream().anyMatch(travelling));
    }

    private static void setTrackPowering(BlockState blockState, Level level, BlockPos blockPos,
        boolean powered) {
      if (powered) {
        level.scheduleTick(blockPos, blockState.getBlock(),
            CartConstants.DETECTED_POWER_OUTPUT_FADE);
      }
      if (powered != blockState.getValue(POWERED)) {
        level.setBlockAndUpdate(blockPos, blockState.setValue(POWERED, powered));
      }
    }

    private static List<AbstractMinecart> findCarts(Level level, BlockPos blockPos) {
      return EntitySearcher.findMinecarts().around(blockPos).upTo(-0.2F).search(level);
    }
  }
}
