package mods.railcraft.client.renderer.entity.state;

import mods.railcraft.world.level.material.StandardTank;
import net.minecraft.client.renderer.item.ItemStackRenderState;

public class TankMinecartRendererState extends RailcraftMinecartRenderState {

  public boolean isFilling;
  public StandardTank tankManager;
  public boolean hasFilter;
  public ItemStackRenderState itemState = new ItemStackRenderState();
}
