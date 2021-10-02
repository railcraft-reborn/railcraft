package mods.railcraft.world.entity;

import mods.railcraft.util.LevelUtil;
import mods.railcraft.util.MiscTools;
import mods.railcraft.util.PlayerUtil;
import mods.railcraft.world.item.FirestoneItem;
import mods.railcraft.world.item.CrackedFirestoneItem;
import mods.railcraft.world.level.block.RitualBlock;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.RitualBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public class FirestoneItemEntity extends ItemEntity {

  private int clock = MiscTools.RANDOM.nextInt(100);
  private boolean refined;

  public FirestoneItemEntity(EntityType<? extends FirestoneItemEntity> type, World world) {
    super(type, world);
  }

  public FirestoneItemEntity(double x, double y, double z, World world) {
    this(RailcraftEntityTypes.FIRESTONE.get(), world);
    this.setPos(x, y, z);
    this.yRot = this.random.nextFloat() * 360.0F;
    this.setDeltaMovement(this.random.nextDouble() * 0.2D - 0.1D, 0.2D,
        this.random.nextDouble() * 0.2D - 0.1D);
  }

  public FirestoneItemEntity(double x, double y, double z, World world, ItemStack stack) {
    this(x, y, z, world);
    this.setItem(stack);
    this.lifespan = (stack.getItem() == null ? 6000 : stack.getEntityLifespan(world));
  }

  {
    this.setExtendedLifetime();
  }

  @Override
  public void tick() {
    super.tick();
    if (!this.level.isClientSide()) {
      clock++;
      if (clock % 4 != 0)
        return;
      ItemStack stack = getItem();
      FirestoneItem.trySpawnFire(this.level, this.blockPosition(), stack,
          PlayerUtil.getItemThrower(this));
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  protected void lavaHurt() {
    if (!this.refined || !this.isAlive() || this.level.isClientSide())
      return;
    BlockState firestoneBlock = RailcraftBlocks.RITUAL.get().defaultBlockState();
    BlockPos surface = this.blockPosition();
    if (this.level.getBlockState(surface).getMaterial() == Material.LAVA
        || this.level.getBlockState(surface.above()).getMaterial() == Material.LAVA)
      for (int i = 0; i < 10; i++) {
        surface = surface.above();
        if (this.level.getBlockState(surface).isAir(this.level, surface)
            && this.level.getBlockState(surface.below()).getMaterial() == Material.LAVA) {
          boolean cracked = getItem().getItem() instanceof CrackedFirestoneItem;
          if (LevelUtil.setBlockState(this.level, surface,
              firestoneBlock.setValue(RitualBlock.CRACKED, cracked),
              PlayerUtil.getItemThrower(this))) {
            TileEntity tile = this.level.getBlockEntity(surface);
            if (tile instanceof RitualBlockEntity) {
              RitualBlockEntity fireTile = (RitualBlockEntity) tile;
              ItemStack firestone = getItem();
              fireTile.charge = firestone.getMaxDamage() - firestone.getDamageValue();
              if (firestone.hasCustomHoverName())
                fireTile.setItemName(firestone.getDisplayName());
              this.remove();
              return;
            }
          }
        }
      }
  }

  public boolean isRefined() {
    return this.refined;
  }

  public void setRefined(boolean refined) {
    this.refined = refined;
  }

  @Override
  public void addAdditionalSaveData(CompoundNBT compound) {
    super.addAdditionalSaveData(compound);
    compound.putBoolean("refined", this.refined);
  }

  @Override
  public void readAdditionalSaveData(CompoundNBT compound) {
    this.refined = compound.getBoolean("refined");
    super.readAdditionalSaveData(compound);
  }
}
