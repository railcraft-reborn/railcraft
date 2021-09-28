package mods.railcraft.gui.button;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class MultiButtonController<T extends ButtonState>
    implements INBTSerializable<CompoundNBT> {

  private int currentStateIndex;
  private final T[] validStates;

  @SafeVarargs
  private MultiButtonController(int startState, T... validStates) {
    this.currentStateIndex = startState;
    this.validStates = validStates;
  }

  private MultiButtonController(MultiButtonController<T> controller) {
    this.currentStateIndex = controller.currentStateIndex;
    this.validStates = controller.validStates;
  }

  @SafeVarargs
  public static <T extends ButtonState> MultiButtonController<T> create(int startState,
      T... validStates) {
    return new MultiButtonController<>(startState, validStates);
  }

  public static <T extends ButtonState> MultiButtonController<T> create(
      MultiButtonController<T> controller) {
    return new MultiButtonController<>(controller);
  }

  public MultiButtonController<T> copy() {
    return create(this);
  }

  public T[] getValidStates() {
    return this.validStates;
  }

  public int incrementState() {
    int newState = this.currentStateIndex + 1;
    if (newState >= this.validStates.length) {
      newState = 0;
    }
    this.currentStateIndex = newState;
    return this.currentStateIndex;
  }

  public int decrementState() {
    int newState = this.currentStateIndex - 1;
    if (newState < 0) {
      newState = this.validStates.length - 1;
    }
    this.currentStateIndex = newState;
    return this.currentStateIndex;
  }

  public void setCurrentState(int state) {
    this.currentStateIndex = state;
  }

  public void setCurrentState(T state) {
    for (int i = 0; i < this.validStates.length; i++) {
      if (this.validStates[i] == state) {
        this.currentStateIndex = i;
        return;
      }
    }
  }

  public int getCurrentStateIndex() {
    return this.currentStateIndex;
  }

  public T getCurrentState() {
    return this.validStates[this.currentStateIndex];
  }

  public boolean is(T state) {
    return this.getCurrentState() == state;
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT tag = new CompoundNBT();
    tag.putString("currentState", this.getCurrentState().getSerializedName());
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundNBT tag) {
    String name = tag.getString("currentState");
    for (int i = 0; i < this.validStates.length; i++) {
      if (this.validStates[i].getSerializedName().equals(name)) {
        this.currentStateIndex = i;
        break;
      }
    }
  }
}
