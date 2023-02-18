package mods.railcraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import mods.railcraft.world.level.block.track.TrackBlock;
import mods.railcraft.world.level.block.track.outfitted.GatedTrackBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.core.Direction;

@Mixin(FenceBlock.class)
public class FenceBlockMixin {

  @Inject(method = "connectsTo", at = @At("HEAD"), cancellable = true)
  public void railcraft$connectToGatedTrack(BlockState blockState, boolean sturdy, Direction face,
      CallbackInfoReturnable<Boolean> callbackInfo) {
    if (blockState.getBlock() instanceof GatedTrackBlock) {
      RailShape railShape = TrackBlock.getRailShapeRaw(blockState);
      callbackInfo.setReturnValue(face.getAxis() == Direction.Axis.X
          ? railShape == RailShape.NORTH_SOUTH
          : railShape == RailShape.EAST_WEST);
    }
  }
}
