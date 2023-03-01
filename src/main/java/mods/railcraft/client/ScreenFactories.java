package mods.railcraft.client;

import mods.railcraft.client.gui.screen.ActionSignalBoxScreen;
import mods.railcraft.client.gui.screen.AnalogSignalControllerBoxScreen;
import mods.railcraft.client.gui.screen.EmbarkingTrackScreen;
import mods.railcraft.client.gui.screen.GoldenTicketScreen;
import mods.railcraft.client.gui.screen.LauncherTrackScreen;
import mods.railcraft.client.gui.screen.RoutingTableBookScreen;
import mods.railcraft.client.gui.screen.SignalCapacitorBoxScreen;
import mods.railcraft.client.gui.screen.SignalControllerBoxScreen;
import mods.railcraft.client.gui.screen.SwitchTrackMotorScreen;
import mods.railcraft.world.level.block.entity.SwitchTrackMotorBlockEntity;
import mods.railcraft.world.level.block.entity.signal.ActionSignalBoxBlockEntity;
import mods.railcraft.world.level.block.entity.signal.AnalogSignalControllerBoxBlockEntity;
import mods.railcraft.world.level.block.entity.signal.SignalCapacitorBoxBlockEntity;
import mods.railcraft.world.level.block.entity.signal.SignalControllerBoxBlockEntity;
import mods.railcraft.world.level.block.entity.track.LauncherTrackBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class ScreenFactories {

  public static void openSignalControllerBoxScreen(SignalControllerBoxBlockEntity signalBox) {
    Minecraft.getInstance().setScreen(new SignalControllerBoxScreen(signalBox));
  }

  public static void openAnalogSignalControllerBoxScreen(
      AnalogSignalControllerBoxBlockEntity signalBox) {
    Minecraft.getInstance().setScreen(new AnalogSignalControllerBoxScreen(signalBox));
  }

  public static void openSignalCapacitorBoxScreen(SignalCapacitorBoxBlockEntity signalBox) {
    Minecraft.getInstance().setScreen(new SignalCapacitorBoxScreen(signalBox));
  }

  public static void openActionSignalBoxScreen(ActionSignalBoxBlockEntity signalBox) {
    Minecraft.getInstance().setScreen(new ActionSignalBoxScreen(signalBox));
  }

  public static void openSwitchTrackMotorScreen(SwitchTrackMotorBlockEntity switchTrackMotor) {
    Minecraft.getInstance().setScreen(new SwitchTrackMotorScreen(switchTrackMotor));
  }

  public static void openEmbarkingTrackScreen(BlockState blockState, BlockPos blockPos) {
    Minecraft.getInstance().setScreen(new EmbarkingTrackScreen(blockState, blockPos));
  }

  public static void openLauncherTrackScreen(LauncherTrackBlockEntity track) {
    Minecraft.getInstance().setScreen(new LauncherTrackScreen(track));
  }

  public static void openGoldenTicketScreen(ItemStack itemStack, InteractionHand hand) {
    Minecraft.getInstance().setScreen(new GoldenTicketScreen(itemStack, hand));
  }

  public static void openRoutingTableBookScreen(Player player, ItemStack itemStack,
      InteractionHand hand) {
    Minecraft.getInstance().setScreen(new RoutingTableBookScreen(player, itemStack, hand));
  }
}
