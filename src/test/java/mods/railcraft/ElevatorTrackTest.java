package mods.railcraft;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(Railcraft.ID)
@PrefixGameTestTemplate(false)
public class ElevatorTrackTest {

    @GameTest(template = "elevator_track_up")
    public static void elevatorTrackUp(GameTestHelper helper) {
        helper.pressButton(7, 3, 1);
        helper.succeedWhenEntityPresent(EntityType.MINECART, 2, 7, 1);
    }

    @GameTest(template = "elevator_track_down")
    public static void elevatorTrackDown(GameTestHelper helper) {
        helper.pressButton(1, 8, 1);
        helper.succeedWhenEntityPresent(EntityType.MINECART, 6, 2, 1);
    }
}
