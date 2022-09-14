package mods.railcraft;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(Railcraft.ID)
@PrefixGameTestTemplate(false)
public class DetectorTrackTest {

  @GameTest(template = "detector_track")
  public static void detectorTrack(GameTestHelper helper) {
    helper.pressButton(0, 3, 1);
    helper.succeedWhen(() -> {
      helper.assertEntityPresent(EntityType.MINECART, 3, 2, 1);
      var redstoneLampPos = new BlockPos(3, 2, 0);
      helper.assertBlockProperty(redstoneLampPos, RedstoneLampBlock.LIT, true);
    });
  }
}
