/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.signals;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;

/**
 * Represents a Signal state.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public enum SignalAspect {

  /**
   * The All Clear.
   */
  GREEN(0, 5, "gui.railcraft.aspect.green.name"),
  /**
   * Typically means pairing in progress.
   */
  BLINK_YELLOW(1, 3, "gui.railcraft.aspect.blink.yellow.name"),
  /**
   * Caution, cart heading away.
   */
  YELLOW(2, 5, "gui.railcraft.aspect.yellow.name"),
  /**
   * Maintenance warning, the signal is malfunctioning.
   */
  BLINK_RED(3, 3, "gui.railcraft.aspect.blink.red.name"),
  /**
   * Stop!
   */
  RED(4, 5, "gui.railcraft.aspect.red.name"),
  /**
   * Can't happen, really it can't (or shouldn't). Only used when rendering blink states (for the
   * texture offset).
   */
  OFF(5, 0, "gui.railcraft.aspect.off.name");

  private static final SignalAspect[] VALUES = values();

  private static final SignalAspect[] BY_ID = Arrays.stream(VALUES)
      .sorted(Comparator.comparingInt(SignalAspect::getId))
      .toArray(length -> new SignalAspect[length]);

  private final int id;
  private final int blockLight;
  private final String localizationTag;

  private static final int LAMP_LIGHT = 12;
  private static final int BLINK_DELAY_TICKS = 16;

  private static boolean blinkState;
  private static int blinkTimer;

  private SignalAspect(int id, int blockLight, String localizationTag) {
    this.id = id;
    this.blockLight = blockLight;
    this.localizationTag = localizationTag;
  }

  public int getId() {
    return this.id;
  }

  /**
   * Returns the texture brightness for this specific aspect.
   *
   * @return brightness
   */
  public int getLampLight() {
    if (this == OFF)
      return 0;
    return LAMP_LIGHT;
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
    return this == OFF || (isBlinkAspect() && !getBlinkState());
  }

  /**
   * Returns the SignalAspect that should be used during rendering. This will vary for blinking
   * SignalAspects based on the global blink state.
   *
   * @return the SignalAspect that should be rendered
   */
  public SignalAspect getDisplayAspect() {
    if (isOffState())
      return OFF;
    if (this == BLINK_YELLOW)
      return YELLOW;
    if (this == BLINK_RED)
      return RED;
    return this;
  }

  /**
   * Returns the level at which the Aspect emits light.
   */
  public int getBlockLight() {
    return this.blockLight;
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
   * Read an aspect from NBT.
   */
  public static SignalAspect readFromNBT(CompoundNBT nbt, String tag) {
    if (nbt.contains(tag, Constants.NBT.TAG_INT))
      return getById(nbt.getInt(tag));
    return RED;
  }

  /**
   * Write an aspect to NBT.
   */
  public void writeToNBT(CompoundNBT nbt, String tag) {
    nbt.putInt(tag, this.id);
  }

  public String getLocalizationTag() {
    return localizationTag;
  }

  @Override
  public String toString() {
    String[] sa = name().split("_");
    StringBuilder out = new StringBuilder();
    for (String s : sa) {
      out.append(s, 0, 1).append(s.substring(1).toLowerCase(Locale.ENGLISH)).append(" ");
    }
    return out.toString().trim();
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

  public static SignalAspect getById(int id) {
    if (id < 0 || id >= BY_ID.length)
      return SignalAspect.RED;
    return BY_ID[id];
  }
}
