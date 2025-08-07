package mods.railcraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import com.mojang.datafixers.util.Pair;
import mods.railcraft.attachment.RailcraftAttachmentTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.MinecartBehavior;
import net.minecraft.world.entity.vehicle.OldMinecartBehavior;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;

@Mixin(value = OldMinecartBehavior.class)
public abstract class OldMinecartBehaviorMixin extends MinecartBehavior {

  protected OldMinecartBehaviorMixin(AbstractMinecart minecart) {
    super(minecart);
  }

  private OldMinecartBehavior self() {
    return (OldMinecartBehavior) (Object) this;
  }

  /*@Redirect(method = "moveAlongTrack",
      at = @At(value = "INVOKE",
          target = "Lnet/minecraft/world/entity/vehicle/AbstractMinecart;getMaxSpeed(Lnet/minecraft/server/level/ServerLevel;)D"))
  private double railcraft$bypassgetMaxSpeed(AbstractMinecart minecart, ServerLevel level) {
    return getCustomMaxSpeed(level);
  }*/

  private double getCustomMaxSpeed(ServerLevel level) {
    if (!minecart.getData(RailcraftAttachmentTypes.CAN_USE_RAIL)) {
      return self().getMaxSpeed(level);
    } else {
      BlockPos pos = this.getCurrentRailPosition(minecart, level);
      BlockState state = level.getBlockState(pos);
      if (!state.is(BlockTags.RAILS)) {
        return self().getMaxSpeed(level);
      } else {
        float railMaxSpeed = ((BaseRailBlock)state.getBlock()).getRailMaxSpeed(state, level, pos, minecart);
        return Math.min(railMaxSpeed, minecart.getData(RailcraftAttachmentTypes.CURRENT_SPEED_CAP_ON_RAIL));
      }
    }
  }

  /**
   * This method is a direct copy of the original method from OldMinecartBehavior with
   * some Railcraft Patches.
   */
  @Overwrite
  public void moveAlongTrack(ServerLevel p_376285_) {
    BlockPos blockpos = this.minecart.getCurrentBlockPosOrRailBelow();
    BlockState blockstate = self().level().getBlockState(blockpos);
    this.minecart.resetFallDistance();
    double d0 = this.minecart.getX();
    double d1 = this.minecart.getY();
    double d2 = this.minecart.getZ();
    Vec3 vec3 = self().getPos(d0, d1, d2);
    d1 = (double)blockpos.getY();
    boolean flag = false;
    boolean flag1 = false;
    Block var14 = blockstate.getBlock();
    if (var14 instanceof PoweredRailBlock poweredRail) {
      if (!poweredRail.isActivatorRail()) {
        flag = (Boolean)blockstate.getValue(PoweredRailBlock.POWERED);
        flag1 = !flag;
      }
    }

    double d3 = (double)0.0078125F;
    if (this.minecart.isInWater()) {
      d3 *= 0.2;
    }

    Vec3 vec31 = this.getDeltaMovement();
    RailShape railshape = ((BaseRailBlock)blockstate.getBlock()).getRailDirection(blockstate, this.level(), blockpos, this.minecart);
    switch (railshape) {
      case ASCENDING_EAST:
        this.setDeltaMovement(vec31.add(-d3, (double)0.0F, (double)0.0F));
        ++d1;
        break;
      case ASCENDING_WEST:
        this.setDeltaMovement(vec31.add(d3, (double)0.0F, (double)0.0F));
        ++d1;
        break;
      case ASCENDING_NORTH:
        this.setDeltaMovement(vec31.add((double)0.0F, (double)0.0F, d3));
        ++d1;
        break;
      case ASCENDING_SOUTH:
        this.setDeltaMovement(vec31.add((double)0.0F, (double)0.0F, -d3));
        ++d1;
    }

    vec31 = this.getDeltaMovement();
    Pair<Vec3i, Vec3i> pair = AbstractMinecart.exits(railshape);
    Vec3i vec3i = (Vec3i)pair.getFirst();
    Vec3i vec3i1 = (Vec3i)pair.getSecond();
    double d4 = (double)(vec3i1.getX() - vec3i.getX());
    double d5 = (double)(vec3i1.getZ() - vec3i.getZ());
    double d6 = Math.sqrt(d4 * d4 + d5 * d5);
    double d7 = vec31.x * d4 + vec31.z * d5;
    if (d7 < (double)0.0F) {
      d4 = -d4;
      d5 = -d5;
    }

    double d8 = Math.min((double)2.0F, vec31.horizontalDistance());
    vec31 = new Vec3(d8 * d4 / d6, vec31.y, d8 * d5 / d6);
    this.setDeltaMovement(vec31);
    Entity entity = this.minecart.getFirstPassenger();
    Entity var33 = this.minecart.getFirstPassenger();
    Vec3 vec32;
    if (var33 instanceof ServerPlayer serverplayer) {
      vec32 = serverplayer.getLastClientMoveIntent();
    } else {
      vec32 = Vec3.ZERO;
    }

    if (entity instanceof Player && vec32.lengthSqr() > (double)0.0F) {
      Vec3 vec35 = vec32.normalize();
      double d22 = this.getDeltaMovement().horizontalDistanceSqr();
      if (vec35.lengthSqr() > (double)0.0F && d22 < 0.01) {
        this.setDeltaMovement(this.getDeltaMovement().add(vec32.x * 0.001, (double)0.0F, vec32.z * 0.001));
        flag1 = false;
      }
    }

    if (flag1 && this.minecart.getData(RailcraftAttachmentTypes.SHOULD_DO_RAIL_FUNCTIONS)) { //RAILCRAFT PATCH
      double d20 = this.getDeltaMovement().horizontalDistance();
      if (d20 < 0.03) {
        this.setDeltaMovement(Vec3.ZERO);
      } else {
        this.setDeltaMovement(this.getDeltaMovement().multiply((double)0.5F, (double)0.0F, (double)0.5F));
      }
    }

    double d21 = (double)blockpos.getX() + (double)0.5F + (double)vec3i.getX() * (double)0.5F;
    double d9 = (double)blockpos.getZ() + (double)0.5F + (double)vec3i.getZ() * (double)0.5F;
    double d10 = (double)blockpos.getX() + (double)0.5F + (double)vec3i1.getX() * (double)0.5F;
    double d11 = (double)blockpos.getZ() + (double)0.5F + (double)vec3i1.getZ() * (double)0.5F;
    d4 = d10 - d21;
    d5 = d11 - d9;
    double d12;
    if (d4 == (double)0.0F) {
      d12 = d2 - (double)blockpos.getZ();
    } else if (d5 == (double)0.0F) {
      d12 = d0 - (double)blockpos.getX();
    } else {
      double d13 = d0 - d21;
      double d14 = d2 - d9;
      d12 = (d13 * d4 + d14 * d5) * (double)2.0F;
    }

    d0 = d21 + d4 * d12;
    d2 = d9 + d5 * d12;
    this.setPos(d0, d1, d2);
    double d23 = this.minecart.isVehicle() ? (double)0.75F : (double)1.0F;
    double d24 = this.getCustomMaxSpeed(p_376285_); //RAILCRAFT PATCH
    vec31 = this.getDeltaMovement();
    this.minecart.move(
        MoverType.SELF, new Vec3(Mth.clamp(d23 * vec31.x, -d24, d24), (double)0.0F, Mth.clamp(d23 * vec31.z, -d24, d24)));
    if (vec3i.getY() != 0 && Mth.floor(this.minecart.getX()) - blockpos.getX() == vec3i.getX() && Mth.floor(this.minecart.getZ()) - blockpos.getZ() == vec3i.getZ()) {
      this.setPos(this.minecart.getX(), this.minecart.getY() + (double)vec3i.getY(), this.minecart.getZ());
    } else if (vec3i1.getY() != 0 && Mth.floor(this.minecart.getX()) - blockpos.getX() == vec3i1.getX() && Mth.floor(this.minecart.getZ()) - blockpos.getZ() == vec3i1.getZ()) {
      this.setPos(this.minecart.getX(), this.minecart.getY() + (double)vec3i1.getY(), this.minecart.getZ());
    }

    this.setDeltaMovement(this.minecart.applyNaturalSlowdown(this.getDeltaMovement()));
    Vec3 vec33 = self().getPos(this.minecart.getX(), this.minecart.getY(), this.minecart.getZ());
    if (vec33 != null && vec3 != null) {
      double d15 = (vec3.y - vec33.y) * 0.05;
      Vec3 vec34 = this.getDeltaMovement();
      double d16 = vec34.horizontalDistance();
      if (d16 > (double)0.0F) {
        this.setDeltaMovement(vec34.multiply((d16 + d15) / d16, (double)1.0F, (d16 + d15) / d16));
      }

      this.setPos(this.minecart.getX(), vec33.y, this.minecart.getZ());
    }

    int j = Mth.floor(this.minecart.getX());
    int i = Mth.floor(this.minecart.getZ());
    if (j != blockpos.getX() || i != blockpos.getZ()) {
      Vec3 vec36 = this.getDeltaMovement();
      double d25 = vec36.horizontalDistance();
      this.setDeltaMovement(d25 * (double)(j - blockpos.getX()), vec36.y, d25 * (double)(i - blockpos.getZ()));
    }

    if (this.minecart.getData(RailcraftAttachmentTypes.SHOULD_DO_RAIL_FUNCTIONS)) { //RAILCRAFT PATCH
      BaseRailBlock baserailblock = (BaseRailBlock) blockstate.getBlock(); //RAILCRAFT PATCH
      baserailblock.onMinecartPass(blockstate, level(), blockpos, this.minecart); //RAILCRAFT PATCH
    } //RAILCRAFT PATCH

    if (flag && this.minecart.getData(RailcraftAttachmentTypes.SHOULD_DO_RAIL_FUNCTIONS)) { //RAILCRAFT PATCH
      Vec3 vec37 = this.getDeltaMovement();
      double d26 = vec37.horizontalDistance();
      if (d26 > 0.01) {
        double d17 = 0.06;
        this.setDeltaMovement(vec37.add(vec37.x / d26 * 0.06, (double)0.0F, vec37.z / d26 * 0.06));
      } else {
        Vec3 vec38 = this.getDeltaMovement();
        double d18 = vec38.x;
        double d19 = vec38.z;
        if (railshape == RailShape.EAST_WEST) {
          if (this.minecart.isRedstoneConductor(blockpos.west())) {
            d18 = 0.02;
          } else if (this.minecart.isRedstoneConductor(blockpos.east())) {
            d18 = -0.02;
          }
        } else {
          if (railshape != RailShape.NORTH_SOUTH) {
            return;
          }

          if (this.minecart.isRedstoneConductor(blockpos.north())) {
            d19 = 0.02;
          } else if (this.minecart.isRedstoneConductor(blockpos.south())) {
            d19 = -0.02;
          }
        }

        this.setDeltaMovement(d18, vec38.y, d19);
      }
    }

  }

  private BlockPos getCurrentRailPosition(AbstractMinecart minecart, ServerLevel level) {
    int x = Mth.floor(minecart.getX());
    int y = Mth.floor(minecart.getY());
    int z = Mth.floor(minecart.getZ());
    BlockPos pos = new BlockPos(x, y, z);
    if (level.getBlockState(pos.below()).is(BlockTags.RAILS)) {
      pos = pos.below();
    }
    return pos;
  }
}
