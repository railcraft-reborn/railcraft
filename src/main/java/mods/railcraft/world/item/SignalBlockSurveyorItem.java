package mods.railcraft.world.item;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.Translations;
import mods.railcraft.api.signal.entity.MonitoringSignalEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

@SuppressWarnings("rawtypes")
public class SignalBlockSurveyorItem
    extends PairingToolItem<MonitoringSignalEntity, MonitoringSignalEntity> {

  public SignalBlockSurveyorItem(Properties properties) {
    super(properties);
  }

  @Override
  public void appendHoverText(ItemStack stack, Level level, List<Component> tooltipComponents,
      TooltipFlag isAdvanced) {
    tooltipComponents.add(Component
        .translatable(Translations.Tips.SIGNAL_BLOCK_SURVEYOR)
        .withStyle(ChatFormatting.GRAY));
  }

  @Override
  protected Class<MonitoringSignalEntity> targetType() {
    return MonitoringSignalEntity.class;
  }

  @Override
  protected Class<MonitoringSignalEntity> peerType() {
    return MonitoringSignalEntity.class;
  }

  @Override
  protected Component getMessageForState(State state) {
    return switch (state) {
      case ABANDONED -> Component
          .translatable(Translations.Signal.SIGNAL_SURVEYOR_ABANDONED)
          .withStyle(ChatFormatting.LIGHT_PURPLE);
      case LOST_TARGET -> Component
          .translatable(Translations.Signal.SIGNAL_SURVEYOR_LOST)
          .withStyle(ChatFormatting.RED);
      case INVALID_TARGET, INVALID_PEER -> Component
          .translatable(Translations.Signal.SIGNAL_SURVEYOR_INVALID_BLOCK)
          .withStyle(ChatFormatting.RED);
    };
  }

  @Override
  protected void abandon(@Nullable MonitoringSignalEntity target) {
    if (target != null) {
      target.signalNetwork().stopLinking();
    }
  }

  @Override
  protected Result begin(MonitoringSignalEntity target) {
    var trackStatus = target.trackLocator().trackStatus();
    if (trackStatus.invalid()) {
      return Result.failure(Component
          .translatable(Translations.Signal.SIGNAL_SURVEYOR_INVALID_TRACK)
          .withStyle(ChatFormatting.RED));
    }
    target.signalNetwork().startLinking();
    return Result.success(Component
        .translatable(Translations.Signal.SIGNAL_SURVEYOR_BEGIN)
        .withStyle(ChatFormatting.LIGHT_PURPLE));
  }

  @SuppressWarnings("unchecked")
  @Override
  protected Result complete(MonitoringSignalEntity target,
      MonitoringSignalEntity peer) {
    if (!tryLinking(target, peer)) {
      return Result.failure(Component
          .translatable(Translations.Signal.SIGNAL_SURVEYOR_INVALID_PAIR)
          .withStyle(ChatFormatting.RED));
    }
    target.signalNetwork().stopLinking();
    peer.signalNetwork().stopLinking();
    return Result.success(Component
        .translatable(Translations.Signal.SIGNAL_SURVEYOR_SUCCESS,
            peer.getDisplayName(), target.getDisplayName())
        .withStyle(ChatFormatting.GREEN));
  }

  public static <T, T2> boolean tryLinking(MonitoringSignalEntity<T> signal1,
      MonitoringSignalEntity<T2> signal2) {
    return signal1.type().isInstance(signal2)
        && signal2.type().isInstance(signal1)
        && signal1.signalNetwork().addPeer(signal1.type().cast(signal2))
        && signal2.signalNetwork().addPeer(signal2.type().cast(signal1));
  }
}
