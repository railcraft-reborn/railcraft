package mods.railcraft.world.entity;

import mods.railcraft.plugins.PlayerPlugin;
import mods.railcraft.plugins.WorldPlugin;
import mods.railcraft.util.MiscTools;
import mods.railcraft.world.item.ItemFirestone;
import mods.railcraft.world.item.ItemFirestoneCracked;
import mods.railcraft.world.level.block.BlockRitual;
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
      ItemFirestone.trySpawnFire(this.level, this.blockPosition(), stack,
          PlayerPlugin.getItemThrower(this));
    }
  }

  @Override
  protected void lavaHurt() {
    if (!refined || !this.isAlive() || this.level.isClientSide())
      return;
    BlockState firestoneBlock = RailcraftBlocks.RITUAL.get().defaultBlockState();
    BlockPos surface = this.blockPosition();
    if (WorldPlugin.getBlockMaterial(this.level, surface) == Material.LAVA
        || WorldPlugin.getBlockMaterial(this.level, surface.above()) == Material.LAVA)
      for (int i = 0; i < 10; i++) {
        surface = surface.above();
        if (WorldPlugin.isAir(this.level, surface)
            && WorldPlugin.getBlockMaterial(this.level, surface.below()) == Material.LAVA) {
          boolean cracked = getItem().getItem() instanceof ItemFirestoneCracked;
          if (WorldPlugin.setBlockState(this.level, surface,
              firestoneBlock.setValue(BlockRitual.CRACKED, cracked),
              PlayerPlugin.getItemThrower(this))) {
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
    return refined;
  }

  public void setRefined(boolean refined) {
    this.refined = refined;
  }

  @Override
  public void addAdditionalSaveData(CompoundNBT compound) {
    super.addAdditionalSaveData(compound);
    compound.putBoolean("refined", refined);
  }

  @Override
  public void readAdditionalSaveData(CompoundNBT compound) {
    refined = compound.getBoolean("refined");
    super.readAdditionalSaveData(compound);
  }
}
