package mods.railcraft.client;

import mods.railcraft.api.signal.TuningAuraHandler;
import mods.railcraft.client.renderer.blockentity.SignalAuraRenderUtil;
import mods.railcraft.particle.TuningAuraParticleOptions;
import mods.railcraft.world.item.GogglesItem;
import net.minecraft.client.Minecraft;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

public class TuningAuraHandlerImpl implements TuningAuraHandler {

  private final Minecraft minecraft = Minecraft.getInstance();

  @Override
  public boolean isTuningAuraActive() {
    return GogglesItem.isGoggleAuraActive(this.minecraft.player, GogglesItem.Aura.TUNING)
        || GogglesItem.isGoggleAuraActive(this.minecraft.player, GogglesItem.Aura.SIGNALLING);
  }

  @Override
  public void spawnTuningAura(BlockEntity start, BlockEntity dest) {
    var level = start.getLevel();
    var random = level.getRandom();
    if (random.nextInt(2) != 0) {
      return;
    }

    var pos = start.getBlockPos();
    var px = pos.getX() + getRandomParticleOffset(random);
    var py = pos.getY() + getRandomParticleOffset(random);
    var pz = pos.getZ() + getRandomParticleOffset(random);

    var colorProfile =
        GogglesItem.isGoggleAuraActive(this.minecraft.player, GogglesItem.Aura.SIGNALLING)
            ? SignalAuraRenderUtil.ColorProfile.CONTROLLER_ASPECT
            : SignalAuraRenderUtil.ColorProfile.COORD_RAINBOW;

    int color = colorProfile.getColor(start, start.getBlockPos(), dest.getBlockPos());

    level.addParticle(
        new TuningAuraParticleOptions(Vec3.atCenterOf(dest.getBlockPos()), color),
        px, py, pz, 0.0D, 0.0D, 0.0D);
  }

  private static double getRandomParticleOffset(RandomSource random) {
    return 0.5D + random.nextGaussian() * 0.1D;
  }
}
