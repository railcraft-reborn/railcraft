package mods.railcraft.client.gui.widget.button;

import java.util.Optional;
import java.util.function.Consumer;
import mods.railcraft.gui.button.ButtonState;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public final class MultiButton<T extends ButtonState<T>> extends RailcraftButton {

  private final Consumer<T> stateCallback;
  private final TooltipFactory<? super T> tooltipFactory;
  private T state;
  private boolean locked;

  private MultiButton(Builder<T> builder) {
    super(builder);
    this.stateCallback = builder.stateCallback;
    this.tooltipFactory = builder.tooltipFactory;
    this.setState(builder.state);
  }

  @Override
  public void onPress() {
    if (!this.locked && this.active) {
      this.setState(this.state.getNext());
    }
  }

  public T getState() {
    return this.state;
  }

  public void setState(T state) {
    this.state = state;
    this.setTexturePosition(this.state.getTexturePosition());
    this.setMessage(this.state.getLabel());
    this.setTooltip(this.tooltipFactory.createTooltip(state).orElse(null));
    if (this.state != state && this.stateCallback != null) {
      this.stateCallback.accept(state);
    }
  }

  public static <T extends ButtonState<T>> Builder<T> builder(
      TexturePosition texturePosition, T state) {
    return new Builder<>(texturePosition, state);
  }

  public static class Builder<T extends ButtonState<T>>
      extends AbstractBuilder<Builder<T>, MultiButton<T>> {

    private final T state;

    @Nullable
    private Consumer<T> stateCallback;
    private TooltipFactory<? super T> tooltipFactory = TooltipFactory.DEFAULT;

    public Builder(TexturePosition texturePosition, T state) {
      super(MultiButton::new, Component.empty(), null, texturePosition);
      this.state = state;
    }

    public Builder<T> stateCallback(@Nullable Consumer<T> stateCallback) {
      this.stateCallback = stateCallback;
      return this;
    }

    public Builder<T> tooltipFactory(TooltipFactory<? super T> tooltipFactory) {
      this.tooltipFactory = tooltipFactory;
      return this;
    }
  }

  @FunctionalInterface
  public interface TooltipFactory<T extends ButtonState<?>> {

    TooltipFactory<ButtonState<?>> DEFAULT = state ->
        Optional.of(Tooltip.create(CommonComponents.joinLines(state.getTooltip())));

    Optional<Tooltip> createTooltip(T state);
  }
}
