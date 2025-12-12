package mods.railcraft.world.level.block;

import java.util.Map;
import org.jspecify.annotations.Nullable;
import com.mojang.serialization.MapCodec;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.charge.ChargeBlock;
import mods.railcraft.api.charge.ChargeStorage;
import mods.railcraft.world.level.block.entity.CrusherBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;

public class CrusherMultiblockBlock extends MultiblockBlock implements ChargeBlock {

  public static final Property<Type> TYPE = EnumProperty.create("type", Type.class);
  public static final Property<Boolean> ROTATED = BooleanProperty.create("rotated");
  public static final Property<Boolean> OUTPUT = BooleanProperty.create("output");

  private static final Map<Charge, Spec> CHARGE_SPECS =
      Spec.make(Charge.distribution, ChargeBlock.ConnectType.BLOCK, 0.025F,
          new ChargeStorage.Spec(ChargeStorage.State.DISABLED,
              80000, 8000, 1));

  private static final MapCodec<CrusherMultiblockBlock> CODEC =
      simpleCodec(CrusherMultiblockBlock::new);

  public CrusherMultiblockBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(TYPE, Type.NONE)
        .setValue(ROTATED, false)
        .setValue(OUTPUT, false));
  }

  @Override
  protected MapCodec<? extends MultiblockBlock> codec() {
    return CODEC;
  }

  @Override
  protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
    builder.add(TYPE, ROTATED, OUTPUT);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new CrusherBlockEntity(pos, state);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
      BlockEntityType<T> type) {
    return level.isClientSide()
        ? null
        : createTickerHelper(type, RailcraftBlockEntityTypes.CRUSHER.get(),
            CrusherBlockEntity::serverTick);
  }

  @Override
  public Map<Charge, Spec> getChargeSpecs(BlockState state, ServerLevel level, BlockPos pos) {
    return CHARGE_SPECS;
  }

  @Override
  public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
    Charge.zapEffectProvider().throwSparks(state, level, pos, random, 50);
  }

  @Override
  protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    super.tick(state, level, pos, random);
    this.registerNode(state, level, pos);
  }

  @Override
  protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState,
      boolean isMoving) {
    super.onPlace(state, level, pos, oldState, isMoving);
    if (!state.is(oldState.getBlock())) {
      this.registerNode(state, (ServerLevel) level, pos);
    }
  }

  public enum Type implements StringRepresentable {

    NONE("none"),
    NORTH("north"),
    NORTH_EAST("north_east"),
    NORTH_WEST("north_west"),
    SOUTH("south"),
    SOUTH_EAST("south_east"),
    SOUTH_WEST("south_west");

    private final String name;

    Type(String name) {
      this.name = name;
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }
  }
}
