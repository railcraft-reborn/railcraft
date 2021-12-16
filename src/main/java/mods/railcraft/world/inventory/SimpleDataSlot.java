package mods.railcraft.world.inventory;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import net.minecraft.world.inventory.DataSlot;

public class SimpleDataSlot extends DataSlot {

  private final IntSupplier getter;
  private final IntConsumer setter;

  public SimpleDataSlot(IntSupplier getter, IntConsumer setter) {
    this.getter = getter;
    this.setter = setter;
  }

  @Override
  public int get() {
    return this.getter.getAsInt();
  }

  @Override
  public void set(int value) {
    this.setter.accept(value);
  }
}
