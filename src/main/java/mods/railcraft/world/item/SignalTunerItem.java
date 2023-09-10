package mods.railcraft.world.item;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.Translations;
import mods.railcraft.api.signal.entity.SignalControllerEntity;
import mods.railcraft.api.signal.entity.SignalReceiverEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class SignalTunerItem extends PairingToolItem<SignalControllerEntity, SignalReceiverEntity> {

  public SignalTunerItem(Properties properties) {
    super(properties);
  }

  @Override
  public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip,
      TooltipFlag flag) {
    tooltip.add(Component
        .translatable(Translations.Tips.LINKS_CONTROLLERS_TO_RECEIVERS)
        .withStyle(ChatFormatting.GRAY));
  }

  @Override
  protected Class<SignalControllerEntity> targetType() {
    return SignalControllerEntity.class;
  }

  @Override
  protected Class<SignalReceiverEntity> peerType() {
    return SignalReceiverEntity.class;
  }

  @Override
  protected Component getMessageForState(State state) {
    return switch (state) {
      case ABANDONED -> Component
          .translatable(Translations.Signal.SIGNAL_TUNER_ABANDONED)
          .withStyle(ChatFormatting.LIGHT_PURPLE);
      case LOST_TARGET -> Component
          .translatable(Translations.Signal.SIGNAL_TUNER_LOST)
          .withStyle(ChatFormatting.RED);
      case INVALID_TARGET -> Component
          .translatable(Translations.Signal.SIGNAL_TUNER_INVALID_CONTROLLER)
          .withStyle(ChatFormatting.RED);
      case INVALID_PEER -> Component
          .translatable(Translations.Signal.SIGNAL_TUNER_INVALID_RECEIVER)
          .withStyle(ChatFormatting.RED);
    };
  }

  @Override
  protected void abandon(@Nullable SignalControllerEntity target) {
    if (target != null) {
      target.getSignalController().stopLinking();
    }
  }

  @Override
  protected Result begin(SignalControllerEntity target) {
    target.getSignalController().startLinking();
    return Result.success(Component
        .translatable(Translations.Signal.SIGNAL_TUNER_BEGIN, target.getDisplayName())
        .withStyle(ChatFormatting.LIGHT_PURPLE));
  }

  @Override
  protected Result complete(SignalControllerEntity target, SignalReceiverEntity peer) {
    var success = target.getSignalController().addPeer(peer);
    target.getSignalController().stopLinking();
    return new Result(success, success
        ? Component
            .translatable(Translations.Signal.SIGNAL_TUNER_SUCCESS,
                peer.getDisplayName(), target.getDisplayName())
            .withStyle(ChatFormatting.GREEN)
        : Component
            .translatable(Translations.Signal.SIGNAL_TUNER_ALREADY_PAIRED,
                peer.getDisplayName(), target.getDisplayName())
            .withStyle(ChatFormatting.RED));
  }
}
