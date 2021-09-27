package mods.railcraft.world.level.block;

import java.util.List;
import mods.railcraft.util.AABBFactory;
import mods.railcraft.world.item.ItemFirestoneRefined;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.entity.RitualBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class RitualBlock extends ContainerBlock {

  public static final BooleanProperty CRACKED = BooleanProperty.create("cracked");
  public static final VoxelShape SHAPE = VoxelShapes.create(
      AABBFactory.start()
          .box()
          .expandHorizontally(-0.3)
          .raiseCeiling(0.0625F * -9.0)
          .shiftY(0.0625F * 12.0)
          .build());

  public RitualBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.stateDefinition.any().setValue(CRACKED, false));
  }

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(CRACKED);
  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader source, BlockPos pos,
      ISelectionContext context) {
    return SHAPE;
  }


  @Override
  public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world,
      BlockPos pos, PlayerEntity player) {
    return ItemFirestoneRefined.getItemCharged();
  }

  @SuppressWarnings("deprecation")
  @Override
  public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
    List<ItemStack> drops = super.getDrops(state, builder);
    TileEntity tile = builder.getOptionalParameter(LootParameters.BLOCK_ENTITY);
    if (tile instanceof RitualBlockEntity) {
      RitualBlockEntity firestone = (RitualBlockEntity) tile;
      Item item = state.getValue(CRACKED)
          ? RailcraftItems.CRACKED_FIRESTONE.get()
          : RailcraftItems.REFINED_FIRESTONE.get();
      if (item != null) {
        ItemStack drop = new ItemStack(item, 1);
        if (firestone.hasCustomName())
          drop.setHoverName(firestone.getCustomName());
        drops.add(drop);
      }
    } else {
      drops.add(ItemFirestoneRefined.getItemEmpty());
    }
    return drops;
  }

  @Override
  public BlockRenderType getRenderShape(BlockState state) {
    return BlockRenderType.ENTITYBLOCK_ANIMATED;
  }

  @Override
  public boolean canBeReplacedByLeaves(BlockState state, IWorldReader world, BlockPos pos) {
    return false;
  }

  @Override
  public boolean addDestroyEffects(BlockState state, World world, BlockPos pos,
      ParticleManager effectRenderer) {
    return true;
  }

  @Override
  public boolean addHitEffects(BlockState state, World worldObj, RayTraceResult target,
      ParticleManager manager) {
    return true;
  }

  @Override
  public TileEntity newBlockEntity(IBlockReader blockGetter) {
    return new RitualBlockEntity();
  }
}
