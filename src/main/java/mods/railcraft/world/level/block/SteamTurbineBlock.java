package mods.railcraft.world.level.block;

import java.util.Map;
import java.util.Random;
import javax.annotation.Nullable;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.charge.ChargeBlock;
import mods.railcraft.api.charge.ChargeStorage;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.module.SteamTurbineModule;
import mods.railcraft.world.level.block.entity.multiblock.SteamTurbineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;

public class SteamTurbineBlock extends MultiblockBlock implements ChargeBlock {

  public static final Property<Type> TYPE = EnumProperty.create("type", Type.class);

  public static final Property<Boolean> ROTATED = BooleanProperty.create("rotated");

  private static final Map<Charge, Spec> CHARGE_SPECS =
      Spec.make(Charge.distribution, ChargeBlock.ConnectType.BLOCK, 0,
          new ChargeStorage.Spec(ChargeStorage.State.DISABLED,
              SteamTurbineModule.CHARGE_OUTPUT, SteamTurbineModule.CHARGE_OUTPUT, 1));

  public SteamTurbineBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(TYPE, Type.NONE)
        .setValue(ROTATED, false));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(TYPE, ROTATED);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new SteamTurbineBlockEntity(blockPos, blockState);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
      BlockEntityType<T> type) {
    return level.isClientSide() ? null
        : createTickerHelper(type, RailcraftBlockEntityTypes.STEAM_TURBINE.get(),
            SteamTurbineBlockEntity::serverTick);
  }

  /*
   * ======= ChargeBlock implementation =======
   */

  @Override
  public Map<Charge, Spec> getChargeSpecs(BlockState state,
      ServerLevel level, BlockPos pos) {
    return CHARGE_SPECS;
  }

  @Override
  public void animateTick(BlockState state, Level level, BlockPos pos, Random rand) {
    Charge.zapEffectProvider().throwSparks(state, level, pos, rand, 50);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void tick(BlockState blockState, ServerLevel level, BlockPos blockPos, Random random) {
    super.tick(blockState, level, blockPos, random);
    this.registerNode(blockState, level, blockPos);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState oldState,
      boolean moved) {
    super.onPlace(blockState, level, blockPos, oldState, moved);
    if (!blockState.is(oldState.getBlock())) {
      this.registerNode(blockState, (ServerLevel) level, blockPos);
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newState,
      boolean moved) {
    super.onRemove(blockState, level, blockPos, newState, moved);
    if (!blockState.is(newState.getBlock())) {
      this.deregisterNode((ServerLevel) level, blockPos);
    }
  }

  @Override
  public boolean hasAnalogOutputSignal(BlockState state) {
    return true;
  }

  @Override
  public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
    return Charge.distribution.network((ServerLevel) level).access(pos).getComparatorOutput();
  }

  public enum Type implements StringRepresentable {

    TOP_LEFT("top_left"),
    TOP_RIGHT("top_right"),
    BOTTOM_LEFT("bottom_left"),
    BOTTOM_RIGHT("bottom_right"),
    WINDOW("window"),
    NONE("none");

    private final String name;

    private Type(String name) {
      this.name = name;
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }

    @Override
    public String toString() {
      return this.name;
    }
  }
}
