package mods.railcraft.test;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

public class SignalSystemTest {

  // , batch = TestBatches.MATH
  @PrefixGameTestTemplate(false)
  @GameTest(template = "empty_1x1")
  public static void validateSignalSanity(final GameTestHelper test) {
    // 9, 1, 2 target
    // 8, 4, 2 target 1
    // 2, 3, 3 in
    // TODO finish this test (the snbt didnt save for some reason)
    test.succeed();

  }
}
