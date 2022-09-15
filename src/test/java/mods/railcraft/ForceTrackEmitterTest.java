package mods.railcraft;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(Railcraft.ID)
@PrefixGameTestTemplate(false)
public class ForceTrackEmitterTest {

  @GameTest(template = "force_track_active", timeoutTicks = 200)
  public static void forceTrackActive(GameTestHelper helper) {
    var blockEntity = helper.getBlockEntity(new BlockPos(4, 2, 2));
    helper.onEachTick(() -> blockEntity
        .getCapability(ForgeCapabilities.ENERGY)
        .ifPresent(x -> x.receiveEnergy(10000, false)));

    helper.pressButton(1, 4, 2);
    helper.succeedWhenEntityPresent(EntityType.MINECART, 10, 3, 2);
  }
}
