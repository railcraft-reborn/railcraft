package mods.railcraft.world.level.block.charge;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import mods.railcraft.Translations;
import mods.railcraft.api.charge.Charge;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WireBlock extends ChargeBlock implements SimpleWaterloggedBlock {

  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
  public static final EnumProperty<Connection> DOWN = EnumProperty.create("down", Connection.class);
  public static final EnumProperty<Connection> UP = EnumProperty.create("up", Connection.class);
  public static final EnumProperty<Connection> NORTH =
      EnumProperty.create("north", Connection.class);
  public static final EnumProperty<Connection> SOUTH =
      EnumProperty.create("south", Connection.class);
  public static final EnumProperty<Connection> WEST = EnumProperty.create("west", Connection.class);
  public static final EnumProperty<Connection> EAST = EnumProperty.create("east", Connection.class);

  public static final Map<Direction, EnumProperty<Connection>> PROPERTY_BY_DIRECTION =
      new EnumMap<>(Map.of(
          Direction.DOWN, DOWN,
          Direction.UP, UP,
          Direction.NORTH, NORTH,
          Direction.SOUTH, SOUTH,
          Direction.WEST, WEST,
          Direction.EAST, EAST));

  private static final Map<Charge, Spec> CHARGE_SPECS =
      Spec.make(Charge.distribution, ConnectType.WIRE, 0.025F);

  private static final Map<Direction, Set<ConnectType>> CONNECTION_MATCHER = new EnumMap<>(Direction.class);

  private static final VoxelShape CORE_SHAPE = Block.box(6, 6, 6, 10, 10, 10);
  private static final Map<Direction, VoxelShape> ARM_SHAPES = new EnumMap<>(Map.of(
      Direction.DOWN, Block.box(6, 0, 6, 10, 6, 10),
      Direction.UP, Block.box(6, 10, 6, 10, 16, 10),
      Direction.NORTH, Block.box(6, 6, 0, 10, 10, 6),
      Direction.SOUTH, Block.box(6, 6, 10, 10, 10, 16),
      Direction.WEST, Block.box(0, 6, 6, 6, 10, 10),
      Direction.EAST, Block.box(10, 6, 6, 16, 10, 10)));

  static {
    for (var direction : Direction.values()) {
      CONNECTION_MATCHER.put(direction, EnumSet.of(ConnectType.BLOCK, ConnectType.WIRE));
    }
    CONNECTION_MATCHER.put(Direction.UP, EnumSet.allOf(ConnectType.class));
  }

  private final Map<BlockState, VoxelShape> shapes;

  public WireBlock(Properties properties) {
    super(properties);
    var defaultState = this.stateDefinition.any().setValue(WATERLOGGED, false);
    for (var property : PROPERTY_BY_DIRECTION.values()) {
      defaultState = defaultState.setValue(property, Connection.NONE);
    }
    this.registerDefaultState(defaultState);
    this.shapes = this.getShapeForEachState(WireBlock::computeShape);
  }

  private static VoxelShape computeShape(BlockState state) {
    var shape = CORE_SHAPE;
    for (var entry : PROPERTY_BY_DIRECTION.entrySet()) {
      if (state.getValue(entry.getValue()) != Connection.NONE) {
        shape = Shapes.or(shape, ARM_SHAPES.get(entry.getKey()));
      }
    }
    return shape;
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(WATERLOGGED, DOWN, UP, NORTH, SOUTH, WEST, EAST);
  }

  @Override
  public Map<Charge, Spec> getChargeSpecs(BlockState state, ServerLevel level, BlockPos pos) {
    return CHARGE_SPECS;
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    var level = context.getLevel();
    var pos = context.getClickedPos();
    var waterlogged = level.getFluidState(pos).is(Fluids.WATER);
    return this.getConnectedState(this.defaultBlockState().setValue(WATERLOGGED, waterlogged), level, pos);
  }

  @Override
  protected FluidState getFluidState(BlockState state) {
    return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
  }

  private BlockState getConnectedState(BlockState state, LevelAccessor level, BlockPos pos) {
    for (var direction : Direction.values()) {
      state = state.setValue(PROPERTY_BY_DIRECTION.get(direction), this.getConnection(level, pos, direction));
    }
    return state;
  }

  @Override
  protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
      LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
    if (state.getValue(WATERLOGGED)) {
      level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
    }
    // Charge specs can only be queried server side, the client receives the result via sync
    if (!(level instanceof ServerLevel)) {
      return state;
    }
    return state.setValue(PROPERTY_BY_DIRECTION.get(direction), this.getConnection(level, pos, direction));
  }

  private Connection getConnection(LevelAccessor level, BlockPos pos, Direction direction) {
    var neighborPos = pos.relative(direction);
    var neighborState = level.getBlockState(neighborPos);
    if (!(neighborState.getBlock() instanceof mods.railcraft.api.charge.ChargeBlock chargeBlock)) {
      return Connection.NONE;
    }
    if (!(level instanceof ServerLevel serverLevel)) {
      // Client prediction, the server will send the correct state right after
      return chargeBlock instanceof WireBlock ? Connection.WIRE : Connection.PLUG;
    }
    var spec = chargeBlock.getChargeSpecs(neighborState, serverLevel, neighborPos)
        .get(Charge.distribution);
    if (spec == null || !CONNECTION_MATCHER.get(direction).contains(spec.connectType())) {
      return Connection.NONE;
    }
    return spec.connectType() == ConnectType.WIRE ? Connection.WIRE : Connection.PLUG;
  }

  @Override
  protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos,
      CollisionContext context) {
    return this.shapes.get(state);
  }

  @Override
  protected boolean isSparking(BlockState state) {
    return false;
  }

  @Override
  public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
    // Junctions occasionally spark, like in the original Railcraft
    if (random.nextInt(50) == 25) {
      long connections = PROPERTY_BY_DIRECTION.values().stream()
          .filter(property -> state.getValue(property) != Connection.NONE)
          .count();
      if (connections > 2) {
        Charge.zapEffectProvider().zapEffectPoint(level,
            pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
      }
    }
  }

  @Override
  public void appendHoverText(ItemStack stack, Item.TooltipContext context,
      List<Component> tooltip, TooltipFlag flag) {
    tooltip.add(Component.translatable(Translations.Tips.WIRE).withStyle(ChatFormatting.GRAY));
  }

  public enum Connection implements StringRepresentable {

    NONE, WIRE, PLUG;

    private final String name = this.name().toLowerCase(Locale.ROOT);

    @Override
    public String getSerializedName() {
      return this.name;
    }
  }
}
