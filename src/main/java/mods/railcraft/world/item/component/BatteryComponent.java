package mods.railcraft.world.item.component;

import java.util.function.Consumer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.Translations;
import mods.railcraft.api.charge.ChargeStorage;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.world.level.block.charge.BatterySpecs;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

public record BatteryComponent(
    boolean isRechargeable,
    int capacity,
    int maxDraw,
    float loss,
    int efficiency)
    implements TooltipProvider {

  public static final Codec<BatteryComponent> CODEC =
      RecordCodecBuilder.create(instance -> instance.group(
          Codec.BOOL.fieldOf(CompoundTagKeys.IS_RECHARGEABLE)
              .forGetter(BatteryComponent::isRechargeable),
          Codec.INT.fieldOf(CompoundTagKeys.CAPACITY)
              .forGetter(BatteryComponent::capacity),
          Codec.INT.fieldOf(CompoundTagKeys.MAX_DRAW)
              .forGetter(BatteryComponent::maxDraw),
          Codec.FLOAT.fieldOf(CompoundTagKeys.LOSS)
              .forGetter(BatteryComponent::loss),
          Codec.INT.fieldOf(CompoundTagKeys.EFFICIENCY)
              .forGetter(BatteryComponent::efficiency)
      ).apply(instance, BatteryComponent::new));

  public static final StreamCodec<FriendlyByteBuf, BatteryComponent> STREAM_CODEC =
      StreamCodec.composite(
          ByteBufCodecs.BOOL, BatteryComponent::isRechargeable,
          ByteBufCodecs.INT, BatteryComponent::capacity,
          ByteBufCodecs.INT, BatteryComponent::maxDraw,
          ByteBufCodecs.FLOAT, BatteryComponent::loss,
          ByteBufCodecs.INT, BatteryComponent::efficiency,
          BatteryComponent::new);

  public static BatteryComponent from(BatterySpecs batterySpecs) {
    var spec = batterySpecs.buildSpec();
    return new BatteryComponent(
        spec.storageSpec().initialState().equals(ChargeStorage.State.RECHARGEABLE),
        spec.storageSpec().capacity() / 1000,
        spec.storageSpec().maxDraw(),
        spec.losses(),
        (int) (spec.storageSpec().efficiency() * 100)
    );
  }

  @Override
  public void addToTooltip(Item.TooltipContext tooltipContext, Consumer<Component> consumer,
      TooltipFlag tooltipFlag, DataComponentGetter dataComponentGetter) {
    consumer.accept(Component.translatable(isRechargeable
            ? Translations.Tips.TYPE_RECHARGEABLE
            : Translations.Tips.TYPE_DISPOSABLE)
        .withStyle(ChatFormatting.BLUE));
    consumer.accept(Component.translatable(Translations.Tips.CAPACITY, capacity)
        .withStyle(ChatFormatting.GRAY));
    consumer.accept(Component.translatable(Translations.Tips.MAX_DRAW, maxDraw)
        .withStyle(ChatFormatting.GRAY));
    consumer.accept(Component.translatable(Translations.Tips.LOSS, loss)
        .withStyle(ChatFormatting.GRAY));
    consumer.accept(Component.translatable(Translations.Tips.EFFICIENCY, efficiency)
        .withStyle(ChatFormatting.GRAY));
  }
}
