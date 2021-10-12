package mods.railcraft.world.level.block;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import javax.annotation.Nullable;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.charge.IChargeBlock;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.util.Optionals;
import mods.railcraft.util.inventory.InvTools;
import mods.railcraft.world.level.block.entity.ForceTrackEmitterBlockEntity;
import mods.railcraft.world.level.block.entity.ForceTrackEmitterState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

// TODO:
// https://nekoyue.github.io/ForgeJavaDocs-NG/javadoc/1.16.5/net/minecraftforge/energy/EnergyStorage.html
public class ForceTrackEmitterBlock extends ContainerBlock implements IChargeBlock {

  public static final DyeColor DEFAULT_COLOR = DyeColor.LIGHT_BLUE;
  public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
  public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
  public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);
  private static final Map<Charge, ChargeSpec> CHARGE_SPECS =
      ChargeSpec.make(Charge.distribution, ConnectType.BLOCK, 0.1);

  public ForceTrackEmitterBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(FACING, Direction.NORTH)
        .setValue(POWERED, false)
        .setValue(COLOR, DEFAULT_COLOR));
  }

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(FACING, POWERED, COLOR);
  }

  @Override
  public BlockRenderType getRenderShape(BlockState state) {
    return BlockRenderType.MODEL;
  }

  @Override
  public Map<Charge, ChargeSpec> getChargeSpecs(BlockState state, IBlockReader world,
      BlockPos pos) {
    return CHARGE_SPECS;
  }

  @SuppressWarnings("deprecation")
  @Override
  public ActionResultType use(BlockState state, World worldIn, BlockPos pos,
      PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
    if (super.use(state, worldIn, pos, player, hand, rayTraceResult).consumesAction())
      return ActionResultType.CONSUME;
    if (player.isShiftKeyDown())
      return ActionResultType.FAIL;
    ItemStack heldItem = player.getItemInHand(hand);
    if (heldItem.isEmpty() || hand == Hand.OFF_HAND)
      return ActionResultType.FAIL;
    TileEntity blockEntity = worldIn.getBlockEntity(pos);
    if (blockEntity instanceof ForceTrackEmitterBlockEntity) {
      ForceTrackEmitterBlockEntity t = (ForceTrackEmitterBlockEntity) blockEntity;
      if (Optionals.test(Optional.ofNullable(DyeColor.getColor(heldItem)), t::setColor)) {
        if (!player.isCreative())
          player.setItemInHand(hand, InvTools.depleteItem(heldItem));
        return ActionResultType.SUCCESS;
      }
    }
    return ActionResultType.FAIL;
  }

  private ItemStack getItem(BlockState blockState) {
    ItemStack itemStack = this.asItem().getDefaultInstance();
    CompoundNBT tag = itemStack.getOrCreateTag();
    tag.putInt("color", blockState.getValue(COLOR).getId());
    return itemStack;
  }

  @Override
  public ItemStack getPickBlock(BlockState blockState, RayTraceResult target, IBlockReader world,
      BlockPos pos, PlayerEntity player) {
    return this.getItem(blockState);
  }

  @Override
  public List<ItemStack> getDrops(BlockState blockState, LootContext.Builder builder) {
    return Collections.singletonList(this.getItem(blockState));
  }

  @Override
  public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
    if (stateIn.getValue(POWERED))
      Charge.effects().throwSparks(stateIn, worldIn, pos, rand, 10);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
    super.randomTick(state, worldIn, pos, rand);
    registerNode(state, worldIn, pos);
  }

  @Override
  public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction) {
    if (!LevelUtil.getBlockEntity(world, pos, ForceTrackEmitterBlockEntity.class)
        .map(ForceTrackEmitterBlockEntity::getState)
        .filter(ForceTrackEmitterState.RETRACTED::equals)
        .isPresent()) {
      return state;
    }
    return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
  }

  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context) {
    return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onPlace(BlockState state, World worldIn, BlockPos pos, BlockState oldState,
      boolean something) {
    super.onPlace(state, worldIn, pos, oldState, something);
    registerNode(state, worldIn, pos);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void neighborChanged(BlockState state, World world, BlockPos pos, Block block,
      BlockPos changedPos, boolean something) {
    super.neighborChanged(state, world, pos, block, changedPos, something);
    LevelUtil.getBlockEntity(world, pos, ForceTrackEmitterBlockEntity.class)
        .ifPresent(ForceTrackEmitterBlockEntity::checkSignal);
  }

  @Override
  public void setPlacedBy(World world, BlockPos pos, BlockState state,
      @Nullable LivingEntity livingEntity, ItemStack itemStack) {
    super.setPlacedBy(world, pos, state, livingEntity, itemStack);
    DyeColor color = DyeColor.getColor(itemStack);
    if (color != null) {
      state.setValue(COLOR, color);
    }
    LevelUtil.getBlockEntity(world, pos, ForceTrackEmitterBlockEntity.class)
        .ifPresent(ForceTrackEmitterBlockEntity::checkSignal);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState,
      boolean something) {
    super.onRemove(state, worldIn, pos, newState, something);
    deregisterNode(worldIn, pos);
  }

  @Override
  public TileEntity newBlockEntity(IBlockReader p_196283_1_) {
    return new ForceTrackEmitterBlockEntity();
  }

  public static Direction getFacing(BlockState blockState) {
    return blockState.getValue(FACING);
  }
}
