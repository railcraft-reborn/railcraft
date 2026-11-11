package mods.railcraft.world.level.block;

import java.util.Optional;
import org.jspecify.annotations.Nullable;
import com.mojang.serialization.Codec;
import mods.railcraft.api.item.Crowbar;
import mods.railcraft.particle.RailcraftParticleTypes;
import mods.railcraft.util.fluids.FluidTools;
import mods.railcraft.world.level.block.entity.HobbyistSteamEngineBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.SteamEngineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public class SteamEngineBlock extends BaseEntityBlock {

  public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;


  private final Tier tier;

  public SteamEngineBlock(Tier tier, Properties properties) {
    super(properties);
    this.tier = tier;
    this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
  }

  public Tier getTier() {
    return this.tier;
  }


  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    var level = context.getLevel();
    var pos = context.getClickedPos();
    // Prefer the block the player clicked on, then any other neighbour accepting energy
    var clicked = context.getClickedFace().getOpposite();
    if (canReceiveEnergy(level, pos, clicked)) {
      return this.defaultBlockState().setValue(FACING, clicked);
    }
    return this.defaultBlockState()
        .setValue(FACING, findEnergyReceiver(level, pos, Direction.DOWN).orElse(Direction.UP));
  }

  /**
   * Finds the next direction, after {@code current}, with a block able to receive energy.
   */
  public static Optional<Direction> findEnergyReceiver(Level level, BlockPos pos,
      Direction current) {
    for (int i = 1; i <= Direction.values().length; i++) {
      var direction = Direction.from3DDataValue((current.get3DDataValue() + i) % 6);
      if (canReceiveEnergy(level, pos, direction)) {
        return Optional.of(direction);
      }
    }
    return Optional.empty();
  }

  public static boolean canReceiveEnergy(Level level, BlockPos pos, Direction direction) {
    var handler = level.getCapability(Capabilities.Energy.BLOCK,
        pos.relative(direction), direction.getOpposite());
    if (handler == null) {
      return false;
    }
    // Simulated insertion: the transaction is never committed
    try (var tx = Transaction.openRoot()) {
      return handler.insert(1, tx) > 0;
    }
  }

  @Override
  public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos,
      Rotation direction) {
    // Called by the crowbar: clears an overheat or points the engine to the next energy receiver
    if (level instanceof Level realLevel
        && realLevel.getBlockEntity(pos) instanceof SteamEngineBlockEntity engine) {
      if (engine.isOverheated()) {
        engine.resetOverheat();
        return state;
      }
      var current = state.getValue(FACING);
      return findEnergyReceiver(realLevel, pos, current)
          .filter(facing -> facing != current)
          .map(facing -> state.setValue(FACING, facing))
          .orElse(state);
    }
    return state;
  }

  @Override
  public BlockState rotate(BlockState state, Rotation rotation) {
    return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState mirror(BlockState state, Mirror mirror) {
    return state.rotate(mirror.getRotation(state.getValue(FACING)));
  }

  @Override
  protected RenderShape getRenderShape(BlockState state) {
    return RenderShape.INVISIBLE;
  }

  @Override
  protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos,
      CollisionContext context) {
    return Shapes.block();
  }

  @Override
  protected boolean useShapeForLightOcclusion(BlockState state) {
    return true;
  }

  @Override
  protected VoxelShape getOcclusionShape(BlockState state) {
    return Shapes.empty();
  }

  @Override
  protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level,
      BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
    if (itemStack.getItem() instanceof Crowbar) {
      // Rotation and overheat reset are handled by the crowbar, don't open the GUI
      return InteractionResult.SUCCESS;
    }
    if (level.getBlockEntity(pos) instanceof SteamEngineBlockEntity engine
        && FluidTools.interactWithFluidHandler(player, hand, engine.getTankManager())) {
      return InteractionResult.SUCCESS;
    }
    return InteractionResult.TRY_WITH_EMPTY_HAND;
  }

  @Override
  protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
      Player player, BlockHitResult hitResult) {
    if (player instanceof ServerPlayer serverPlayer
        && level.getBlockEntity(pos) instanceof SteamEngineBlockEntity engine) {
      serverPlayer.openMenu(engine, pos);
    }
    return InteractionResult.SUCCESS;
  }

  @Override
  protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block,
      @Nullable Orientation orientation, boolean movedByPiston) {
    super.neighborChanged(state, level, pos, block, orientation, movedByPiston);
    if (!level.isClientSide()
        && level.getBlockEntity(pos) instanceof SteamEngineBlockEntity engine) {
      engine.checkPower();
    }
  }

  @Override
  protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState,
      boolean movedByPiston) {
    super.onPlace(state, level, pos, oldState, movedByPiston);
    if (!oldState.is(this) || oldState.getValue(FACING) != state.getValue(FACING)) {
      // Energy and fluid capabilities depend on the facing
      level.invalidateCapabilities(pos);
    }
  }

  @Override
  public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
    if (level.getBlockEntity(pos) instanceof SteamEngineBlockEntity engine
        && (engine.isActive() || engine.isOverheated())
        && random.nextInt(SteamEngineBlockEntity.EnergyStage.OVERHEAT.getParticleRate())
            < engine.getEnergyStage().getParticleRate()) {
      level.addParticle(RailcraftParticleTypes.STEAM.get(),
          pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.5,
          pos.getY() + 0.5 + (random.nextDouble() - 0.5) * 0.5,
          pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.5,
          (random.nextDouble() - 0.5) * 0.02, 0.01 + random.nextDouble() * 0.01,
          (random.nextDouble() - 0.5) * 0.02);
    }
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return this.tier == Tier.HOBBYIST
        ? new HobbyistSteamEngineBlockEntity(pos, state)
        : new SteamEngineBlockEntity(pos, state);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
      BlockEntityType<T> type) {
    if (this.tier == Tier.HOBBYIST) {
      return createTickerHelper(type, RailcraftBlockEntityTypes.HOBBYIST_STEAM_ENGINE.get(),
          level.isClientSide()
              ? SteamEngineBlockEntity::clientTick
              : SteamEngineBlockEntity::serverTick);
    }
    return createTickerHelper(type, RailcraftBlockEntityTypes.STEAM_ENGINE.get(),
        level.isClientSide()
            ? SteamEngineBlockEntity::clientTick
            : SteamEngineBlockEntity::serverTick);
  }

  public enum Tier implements StringRepresentable {

    HOBBYIST("hobbyist", 20, 100_000),
    COMMERCIAL("commercial", 40, 200_000),
    INDUSTRIAL("industrial", 80, 300_000);

    public static final Codec<Tier> CODEC = StringRepresentable.fromEnum(Tier::values);

    /**
     * Steam consumed for each 10 FE produced.
     */
    private static final int STEAM_PER_10FE = 5;

    private final String name;
    private final int output;
    private final int capacity;

    Tier(String name, int output, int capacity) {
      this.name = name;
      this.output = output;
      this.capacity = capacity;
    }

    /**
     * FE generated per tick while running.
     */
    public int getOutput() {
      return this.output;
    }

    /**
     * mB of steam consumed per tick while running.
     */
    public int getSteamUsedPerTick() {
      return STEAM_PER_10FE * (this.output / 10);
    }

    public int getCapacity() {
      return this.capacity;
    }

    /**
     * Maximum FE pushed by each piston stroke.
     */
    public int getMaxExtract() {
      return this.output * 8;
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }
  }
}
