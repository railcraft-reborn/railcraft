package mods.railcraft.world.level.block.entity;

import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.particle.ChimneyParticleOptions;
import mods.railcraft.util.LevelUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

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
    if (stack.getItem() instanceof DyeItem dye) {
      this.color = dye.getDyeColor().getFireworkColor();
      this.setChanged();
    }
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.loadAdditional(tag, provider);
    this.color = tag.getInt(CompoundTagKeys.COLOR).orElse(DyeColor.BLACK.getFireworkColor());
  }

  @Override
  public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.saveAdditional(tag, provider);
    tag.putInt(CompoundTagKeys.COLOR, this.color);
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
