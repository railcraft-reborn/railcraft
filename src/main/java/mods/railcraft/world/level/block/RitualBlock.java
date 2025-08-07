package mods.railcraft.world.level.block;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Nullable;
import com.mojang.serialization.MapCodec;
import mods.railcraft.util.BoxBuilder;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.RefinedFirestoneItem;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.RitualBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RitualBlock extends BaseEntityBlock {

  public static final BooleanProperty CRACKED = BooleanProperty.create("cracked");
  private static final VoxelShape SHAPE = Shapes.create(
      BoxBuilder.create()
          .box()
          .inflateHorizontally(-0.3)
          .raiseCeiling(0.0625F * -9.0)
          .shiftY(0.0625F * 12.0)
          .build());
  private static final MapCodec<RitualBlock> CODEC = simpleCodec(RitualBlock::new);

  public RitualBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.stateDefinition.any().setValue(CRACKED, false));
  }

  @Override
  protected MapCodec<? extends BaseEntityBlock> codec() {
    return CODEC;
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
  public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState blockState,
      boolean includeData, Player player) {
    return RefinedFirestoneItem.getItemCharged();
  }

  @SuppressWarnings("deprecation")
  @Override
  public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
    var drops = new ArrayList<ItemStack>();
    var blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
    if (blockEntity instanceof RitualBlockEntity firestone) {
      var item = state.getValue(CRACKED)
          ? RailcraftItems.CRACKED_FIRESTONE.get()
          : RailcraftItems.REFINED_FIRESTONE.get();
      var drop = item.getDefaultInstance();
      if (firestone.hasCustomName()) {
        drop.set(DataComponents.CUSTOM_NAME, firestone.getCustomName());
      }
      drop.setDamageValue(drop.getMaxDamage() - firestone.charge());
      drops.add(drop);
    } else {
      drops.add(RefinedFirestoneItem.getItemEmpty());
    }
    return drops;
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
