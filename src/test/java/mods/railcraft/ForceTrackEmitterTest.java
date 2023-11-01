package mods.railcraft;

import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.capabilities.Capabilities;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(RailcraftConstants.ID)
@PrefixGameTestTemplate(false)
public class ForceTrackEmitterTest {

  @GameTest(template = "force_track_active", timeoutTicks = 200)
  public static void forceTrackActive(GameTestHelper helper) {
    var blockEntity = helper.getBlockEntity(new BlockPos(4, 2, 2));
    helper.onEachTick(() -> blockEntity
        .getCapability(Capabilities.ENERGY)
        .ifPresent(x -> x.receiveEnergy(10000, false)));

    helper.pressButton(1, 4, 2);
    helper.succeedWhenEntityPresent(EntityType.MINECART, 10, 3, 2);
  }
}
