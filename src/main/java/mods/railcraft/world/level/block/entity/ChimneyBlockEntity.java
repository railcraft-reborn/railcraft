package mods.railcraft.world.level.block.entity;

import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.particle.ChimneyParticleOptions;
import mods.railcraft.util.LevelUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class ChimneyBlockEntity extends RailcraftBlockEntity {

  private static final int SNOW_MELT_INTERVAL = 32;
  private int tick;
  private int color = DyeColor.BLACK.getFireworkColor();

  public ChimneyBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.SMOKER.get(), blockPos, blockState);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      ChimneyBlockEntity blockEntity) {
    if (level.hasNeighborSignal(blockPos)) {
      return;
    }
    if (++blockEntity.tick % SNOW_MELT_INTERVAL == 0) {
      blockEntity.tick = 0;
      var block = level.getBlockState(blockPos.above());
      if (block.is(Blocks.SNOW) || block.is(Blocks.SNOW_BLOCK)) {
        LevelUtil.setAir(level, blockPos.above());
      }
    }
  }

  public static void clientTick(Level level, BlockPos blockPos, BlockState blockState,
      ChimneyBlockEntity blockEntity) {
    if (level.hasNeighborSignal(blockPos)) {
      return;
    }
    if (!level.getBlockState(blockPos.above()).isAir()) {
      return;
    }
    var random = level.getRandom();
    double px = blockPos.getX() + random.nextFloat();
    double py = blockPos.getY() + random.nextFloat() * 0.5F + 1;
    double pz = blockPos.getZ() + random.nextFloat();
    level.addParticle(new ChimneyParticleOptions(blockEntity.color), px, py, pz, 0, 0, 0);
  }

  public void changeColor(ItemStack stack) {
    if (stack.isEmpty()) {
      return;
    }
    var dyeColor = stack.get(DataComponents.DYE);
    if (dyeColor != null) {
      this.color = dyeColor.getFireworkColor();
      this.setChanged();
    }
  }

  @Override
  protected void loadAdditional(ValueInput input) {
    super.loadAdditional(input);
    this.color = input.getInt(CompoundTagKeys.COLOR).orElse(DyeColor.BLACK.getFireworkColor());
  }

  @Override
  protected void saveAdditional(ValueOutput output) {
    super.saveAdditional(output);
    output.putInt(CompoundTagKeys.COLOR, this.color);
  }

  @Override
  public void writeToBuf(RegistryFriendlyByteBuf out) {
    super.writeToBuf(out);
    out.writeVarInt(this.color);
  }

  @Override
  public void readFromBuf(RegistryFriendlyByteBuf in) {
    super.readFromBuf(in);
    this.color = in.readVarInt();
  }
}
