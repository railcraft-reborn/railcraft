package mods.railcraft.world.level.block;

import java.util.List;
import mods.railcraft.Translations.Tips;
import mods.railcraft.world.level.block.entity.CrusherBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
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
import org.jetbrains.annotations.Nullable;

public class CrusherMultiblockBlock extends MultiblockBlock {

  public static final Property<Type> TYPE = EnumProperty.create("type", Type.class);
  public static final Property<Boolean> ROTATED = BooleanProperty.create("rotated");
  public static final Property<Boolean> OUTPUT = BooleanProperty.create("output");

  public CrusherMultiblockBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(TYPE, Type.NONE)
        .setValue(ROTATED, false)
        .setValue(OUTPUT, false));
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
  public void appendHoverText(ItemStack stack, @Nullable BlockGetter level,
      List<Component> lines, TooltipFlag flag) {
    lines.add(Component.translatable(Tips.CRUSHER).withStyle(ChatFormatting.GRAY));
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

    private Type(String name) {
      this.name = name;
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }
  }
}
