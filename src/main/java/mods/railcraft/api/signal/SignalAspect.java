/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.signal;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Represents a Signal state.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public enum SignalAspect implements IStringSerializable {

  /**
   * The All Clear.
   */
  GREEN("green", 5),
  /**
   * Typically means pairing in progress.
   */
  BLINK_YELLOW("blink_yellow", 3),
  /**
   * Caution, cart heading away.
   */
  YELLOW("yellow", 5),
  /**
   * Maintenance warning, the signal is malfunctioning.
   */
  BLINK_RED("blink_red", 3),
  /**
   * Stop!
   */
  RED("red", 5),
  /**
   * Can't happen, really it can't (or shouldn't). Only used when rendering blink states (for the
   * texture offset).
   */
  OFF("off", 0);

  private static final SignalAspect[] VALUES = values();

  private static final Map<String, SignalAspect> byName = Arrays.stream(VALUES)
      .collect(Collectors.toMap(SignalAspect::getSerializedName, Function.identity()));

  private final String name;
  private final int blockLight;
  private final ITextComponent displayName;

  private static final int LAMP_LIGHT = 12;
  private static final int BLINK_DELAY_TICKS = 16;

  private static boolean blinkState;
  private static int blinkTimer;

  private SignalAspect(String name, int blockLight) {
    this.name = name;
    this.blockLight = blockLight;
    this.displayName = new TranslationTextComponent("signal_aspect." + name);
  }

  /**
   * Returns the level at which the Aspect emits light.
   */
  public int getBlockLight() {
    return this.blockLight;
  }

  public ITextComponent getDisplayName() {
    return this.displayName;
  }

  @Override
  public String getSerializedName() {
    return this.name;
  }

  /**
   * Returns the texture brightness for this specific aspect.
   *
   * @return brightness
   */
  public int getLampLight() {
    return this == OFF ? 0 : LAMP_LIGHT;
  }

  /**
   * Returns true if the aspect is one of the blink states.
   *
   * @return true if blinks
   */
  public boolean isBlinkAspect() {
    return this == BLINK_YELLOW || this == BLINK_RED;
  }

  /**
   * Returns true if the aspect should appear off. The return value varies for Blink states.
   *
   * @return true if appears off.
   */
  public boolean isOffState() {
    return this == OFF || (this.isBlinkAspect() && !getBlinkState());
  }

  /**
   * Returns the SignalAspect that should be used during rendering. This will vary for blinking
   * SignalAspects based on the global blink state.
   *
   * @return the SignalAspect that should be rendered
   */
  public SignalAspect getDisplayAspect() {
    if (this.isOffState())
      return OFF;
    if (this == BLINK_YELLOW)
      return YELLOW;
    if (this == BLINK_RED)
      return RED;
    return this;
  }

  public SignalAspect getNext() {
    return values()[(this.ordinal() + 1) % values().length];
  }

  public SignalAspect getPrevious() {
    return values()[this.ordinal() == 0 ? values().length - 1
        : this.ordinal() - 1];
  }

  /**
   * Return true if the light is currently off.
   *
   * @return true if the light is currently off.
   */
  public static boolean getBlinkState() {
    return blinkState;
  }

  public static void tickBlinkState() {
    if (blinkTimer++ >= BLINK_DELAY_TICKS) {
      blinkTimer = 0;
      blinkState = !blinkState;
    }
  }

  /**
   * Tests two Aspects and determines which is more restrictive. The concept of "most restrictive"
   * refers to which aspect enforces the most limitations of movement to a train.
   * <p/>
   * In Railcraft the primary use is in Signal Box logic.
   *
   * @param first aspect one
   * @param second aspect two
   * @return The most restrictive Aspect
   */
  public static SignalAspect mostRestrictive(@Nullable SignalAspect first,
      @Nullable SignalAspect second) {
    if (first == null && second == null)
      return RED;
    if (first == null)
      return second;
    if (second == null)
      return first;
    if (first.ordinal() > second.ordinal())
      return first;
    return second;
  }

  public static Optional<SignalAspect> getByName(String name) {
    return Optional.ofNullable(byName.get(name));
  }
}
