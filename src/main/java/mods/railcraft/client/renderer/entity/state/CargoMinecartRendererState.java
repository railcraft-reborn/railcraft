package mods.railcraft.client.renderer.entity.state;

import net.minecraft.client.renderer.item.ItemStackRenderState;

public class CargoMinecartRendererState extends RailcraftMinecartRenderState {

  public boolean hasFilter;
  public ItemStackRenderState itemState = new ItemStackRenderState();
  public int slotsFilled;
  public boolean isBlock;
}
