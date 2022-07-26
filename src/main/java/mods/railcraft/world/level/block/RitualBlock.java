package mods.railcraft.world.level.block;

import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import mods.railcraft.util.BoxBuilder;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.RefinedFirestoneItem;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.RitualBlockEntity;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.extensions.common.IClientBlockExtensions;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class RitualBlock extends BaseEntityBlock {

  public static final BooleanProperty CRACKED = BooleanProperty.create("cracked");
  public static final VoxelShape SHAPE = Shapes.create(
      BoxBuilder.create()
          .box()
          .inflateHorizontally(-0.3)
          .raiseCeiling(0.0625F * -9.0)
          .shiftY(0.0625F * 12.0)
          .build());

  public RitualBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.stateDefinition.any().setValue(CRACKED, false));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(CRACKED);
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter source, BlockPos pos,
      CollisionContext context) {
    return SHAPE;
  }


  @Override
  public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter blockGetter,
      BlockPos pos, Player player) {
    return RefinedFirestoneItem.getItemCharged();
  }

  @SuppressWarnings("deprecation")
  @Override
  public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
    List<ItemStack> drops = super.getDrops(state, builder);
    BlockEntity tile = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
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
      drops.add(RefinedFirestoneItem.getItemEmpty());
    }
    return drops;
  }

  @Override
  public RenderShape getRenderShape(BlockState state) {
    return RenderShape.ENTITYBLOCK_ANIMATED;
  }

  @Override
  public void initializeClient(Consumer<IClientBlockExtensions> consumer) {
    consumer.accept(new IClientBlockExtensions() {
      @Override
      public boolean addDestroyEffects(BlockState state, Level level, BlockPos pos,
          ParticleEngine particleEngine) {
        return true;
      }

      @Override
      public boolean addHitEffects(BlockState state, Level level, HitResult result,
          ParticleEngine particleEngine) {
        return true;
      }
    });
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new RitualBlockEntity(blockPos, blockState);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
      BlockEntityType<T> type) {
    return createTickerHelper(type, RailcraftBlockEntityTypes.RITUAL.get(),
        level.isClientSide() ? RitualBlockEntity::clientTick : RitualBlockEntity::serverTick);
  }
}
