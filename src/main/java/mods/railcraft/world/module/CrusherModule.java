package mods.railcraft.world.module;

import mods.railcraft.util.container.ContainerMapper;
import mods.railcraft.world.level.block.entity.CrusherBlockEntity;

public class CrusherModule extends ContainerModule<CrusherBlockEntity> {

  public static final int SLOT_INPUT = 0;
  public static final int SLOT_OUTPUT = 9;

  private final ContainerMapper inputContainer;
  private final ContainerMapper outputContainer;

  private int processTicks;

  public CrusherModule(CrusherBlockEntity provider) {
    super(provider, 2);
    inputContainer = ContainerMapper.make(this, SLOT_INPUT, 9);
    outputContainer = ContainerMapper.make(this, SLOT_OUTPUT, 9);
    processTicks = 200; // Make sure that it is read by the recipe so that it can be personalized
  }

  @Override
  public void serverTick() {

  }
}
