package mods.railcraft.client.renderer.blockentity.state;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;

public class RitualBlockRenderState extends BlockEntityRenderState {

  public float yOffset;
  public float yaw;
  public int id;
  public ItemStackRenderState itemState = new ItemStackRenderState();
}
