package mods.railcraft.world.entity;

import mods.railcraft.util.LevelUtil;
import mods.railcraft.util.PlayerUtil;
import mods.railcraft.world.item.CrackedFirestoneItem;
import mods.railcraft.world.item.FirestoneItem;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.RitualBlock;
import mods.railcraft.world.level.block.entity.RitualBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.Vec3;

/**
 * @author CovertJaguar <https://www.railcraft.info/>
 */
public class FirestoneItemEntity extends ItemEntity {

  private int clock;
  private boolean refined;

  public FirestoneItemEntity(EntityType<? extends FirestoneItemEntity> type, Level level) {
    super(type, level);
    this.setExtendedLifetime();
  }

  public FirestoneItemEntity(Level level, Vec3 position, ItemStack itemStack) {
    this(RailcraftEntityTypes.FIRESTONE.get(), level);
    this.setPos(position.x, position.y, position.z);
    this.setDeltaMovement(this.random.nextDouble() * 0.2D - 0.1D, 0.2D,
        this.random.nextDouble() * 0.2D - 0.1D);
    this.setItem(itemStack);
    this.lifespan = itemStack.getEntityLifespan(level);
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

  @Override
  public void lavaHurt() {
    if (!this.refined || !this.isAlive() || this.level.isClientSide())
      return;
    BlockState firestoneBlock = RailcraftBlocks.RITUAL.get().defaultBlockState();
    BlockPos surface = this.blockPosition();
    if (this.level.getBlockState(surface).getMaterial() == Material.LAVA
        || this.level.getBlockState(surface.above()).getMaterial() == Material.LAVA)
      for (int i = 0; i < 10; i++) {
        surface = surface.above();
        if (this.level.getBlockState(surface).isAir()
            && this.level.getBlockState(surface.below()).getMaterial() == Material.LAVA) {
          boolean cracked = getItem().getItem() instanceof CrackedFirestoneItem;
          if (LevelUtil.setBlockState(this.level, surface,
              firestoneBlock.setValue(RitualBlock.CRACKED, cracked),
              PlayerUtil.getItemThrower(this))) {
            BlockEntity tile = this.level.getBlockEntity(surface);
            if (tile instanceof RitualBlockEntity fireTile) {
              ItemStack firestone = getItem();
              fireTile.charge = firestone.getMaxDamage() - firestone.getDamageValue();
              if (firestone.hasCustomHoverName())
                fireTile.setItemName(firestone.getDisplayName());
              this.kill();
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
  public void addAdditionalSaveData(CompoundTag compound) {
    super.addAdditionalSaveData(compound);
    compound.putBoolean("refined", this.refined);
  }

  @Override
  public void readAdditionalSaveData(CompoundTag compound) {
    this.refined = compound.getBoolean("refined");
    super.readAdditionalSaveData(compound);
  }
}
