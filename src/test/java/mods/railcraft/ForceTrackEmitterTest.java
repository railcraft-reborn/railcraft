package mods.railcraft;

import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(RailcraftConstants.ID)
@PrefixGameTestTemplate(false)
public class ForceTrackEmitterTest {

  @GameTest(template = "force_track_active", timeoutTicks = 200)
  public static void forceTrackActive(GameTestHelper helper) {
    var pos =  helper.absolutePos(new BlockPos(4, 2, 2));
    helper.onEachTick(() -> {
      var energyStorage = helper.getLevel()
          .getCapability(Capabilities.EnergyStorage.BLOCK, pos, null);
      if (energyStorage != null) {
        energyStorage.receiveEnergy(10000, false);
      }
    });

    helper.pressButton(1, 4, 2);
    helper.succeedWhenEntityPresent(EntityType.MINECART, 10, 3, 2);
  }
}
