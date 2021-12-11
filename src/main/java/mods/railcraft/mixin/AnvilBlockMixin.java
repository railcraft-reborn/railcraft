package mods.railcraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(AnvilBlock.class)
public class AnvilBlockMixin {

  @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
  private static void damage(BlockState blockState,
      CallbackInfoReturnable<BlockState> callbackInfo) {
    if (blockState.is(RailcraftBlocks.STEEL_ANVIL.get())) {
      callbackInfo.setReturnValue(RailcraftBlocks.CHIPPED_STEEL_ANVIL.get().defaultBlockState()
          .setValue(AnvilBlock.FACING, blockState.getValue(AnvilBlock.FACING)));
    } else if (blockState.is(RailcraftBlocks.CHIPPED_STEEL_ANVIL.get())) {
      callbackInfo.setReturnValue(RailcraftBlocks.DAMAGED_STEEL_ANVIL.get().defaultBlockState()
          .setValue(AnvilBlock.FACING, blockState.getValue(AnvilBlock.FACING)));
    }
  }
}
