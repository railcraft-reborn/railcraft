package mods.railcraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;

public abstract class AbstractStrengthenedGlassBlock extends TransparentBlock {

  public static final Property<Type> TYPE = EnumProperty.create("type", Type.class);

  public AbstractStrengthenedGlassBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.stateDefinition.any().setValue(TYPE, Type.SINGLE));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(TYPE);
  }

  public enum Type implements StringRepresentable {

    SINGLE("single"), TOP("top"), CENTER("center"), BOTTOM("bottom");

    private final String name;

    private Type(String name) {
      this.name = name;
    }

    public static Type determine(BlockPos blockPos, Level level, Block block) {
      var above = level.getBlockState(blockPos.above()).is(block);
      var below = level.getBlockState(blockPos.below()).is(block);
      if (above && below) {
        return CENTER;
      } else if (above) {
        return BOTTOM;
      } else if (below) {
        return TOP;
      } else {
        return SINGLE;
      }
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }
  }
}
