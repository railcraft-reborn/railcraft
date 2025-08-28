package mods.railcraft.world.level.block.entity.track;

import java.util.Optional;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.attachment.RailcraftAttachmentTypes;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.track.outfitted.PoweredOutfittedTrackBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.state.BlockState;

public class LauncherTrackBlockEntity extends RailcraftBlockEntity {

  public static final int MIN_LAUNCH_FORCE = 5;
  private static final float LAUNCH_THRESHOLD = 0.01F;
  private byte launchForce = 5;

  public LauncherTrackBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.LAUNCHER_TRACK.get(), blockPos, blockState);
  }

  public void minecartPassed(AbstractMinecart cart) {
    if (PoweredOutfittedTrackBlock.isPowered(this.getBlockState())) {
      var motion = cart.getDeltaMovement();
      var newMotionX = motion.x();
      var newMotionZ = motion.z();
      if (Math.abs(motion.x()) > LAUNCH_THRESHOLD) {
        newMotionX = Math.copySign(0.6F, motion.x());
      }
      if (Math.abs(motion.z()) > LAUNCH_THRESHOLD) {
        newMotionZ = Math.copySign(0.6F, motion.z());
      }
      cart.setData(RailcraftAttachmentTypes.MAX_SPEED_AIR_LATERAL, Optional.of(0.6F));
      cart.setData(RailcraftAttachmentTypes.MAX_SPEED_AIR_VERTICAL, 0.5F);
      cart.setData(RailcraftAttachmentTypes.AIR_DRAG, 0.99999F);
      RollingStock.getOrThrow(cart).launch();
      cart.setDeltaMovement(newMotionX, this.getLaunchForce() * 0.1D, newMotionZ);
      cart.move(MoverType.SELF, cart.getDeltaMovement());
    }
  }

  public byte getLaunchForce() {
    return this.launchForce;
  }

  public void setLaunchForce(byte launchForce) {
    this.launchForce = (byte) Mth.clamp(launchForce, MIN_LAUNCH_FORCE,
        RailcraftConfig.SERVER.maxLauncherTrackForce.get());
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.saveAdditional(tag, provider);
    tag.putByte(CompoundTagKeys.LAUNCH_FORCE, this.launchForce);
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.loadAdditional(tag, provider);
    this.launchForce = tag.getByte(CompoundTagKeys.LAUNCH_FORCE).orElse((byte) 5);
  }

  @Override
  public void writeToBuf(RegistryFriendlyByteBuf out) {
    super.writeToBuf(out);
    out.writeByte(this.launchForce);
  }

  @Override
  public void readFromBuf(RegistryFriendlyByteBuf in) {
    super.readFromBuf(in);
    this.launchForce = in.readByte();
  }
}
