package mods.railcraft.event;

import mods.railcraft.world.level.block.entity.RailcraftBlockEntity;
import net.minecraftforge.eventbus.api.Event;

public abstract class MultiBlockEvent extends Event {

  private final RailcraftBlockEntity master;

  MultiBlockEvent(RailcraftBlockEntity master) {
    this.master = master;
  }

  public RailcraftBlockEntity getMaster() {
    return master;
  }

  public static final class Form extends MultiBlockEvent {

    public Form(RailcraftBlockEntity tile) {
      super(tile);
    }
  }
}
