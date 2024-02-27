package mods.railcraft.client;

import mods.railcraft.api.signal.TuningAuraHandler;
import mods.railcraft.api.signal.entity.SignalControllerEntity;
import mods.railcraft.client.renderer.blockentity.SignalAuraRenderUtil;
import mods.railcraft.particle.TuningAuraParticleOptions;
import mods.railcraft.world.item.GogglesItem;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.entity.BlockEntity;

public class TuningAuraHandlerImpl implements TuningAuraHandler {

  @Override
  public boolean isTuningAuraActive() {
    return GogglesItem.isGoggleAuraActive(GogglesItem.Aura.TUNING)
        || GogglesItem.isGoggleAuraActive(GogglesItem.Aura.SIGNALLING);
  }

  @Override
  public void spawnTuningAura(SignalControllerEntity start, BlockEntity dest) {
    var level = start.asBlockEntity().getLevel();
    var random = level.getRandom();
    if (random.nextInt(2) != 0) {
      return;
    }

    var pos = start.asBlockEntity().getBlockPos();
    var px = pos.getX() + getRandomParticleOffset(random);
    var py = pos.getY() + getRandomParticleOffset(random);
    var pz = pos.getZ() + getRandomParticleOffset(random);

    var color = GogglesItem.isGoggleAuraActive(GogglesItem.Aura.SIGNALLING)
        ? start.getSignalController().aspect().color()
        : SignalAuraRenderUtil.rainbow(pos, dest.getBlockPos());

    level.addParticle(
        new TuningAuraParticleOptions(dest.getBlockPos().getCenter(), color),
        px, py, pz, 0.0D, 0.0D, 0.0D);
  }

  private static double getRandomParticleOffset(RandomSource random) {
    return 0.5D + random.nextGaussian() * 0.1D;
  }
}
