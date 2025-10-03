package mods.railcraft.client.renderer.blockentity.state;

import mods.railcraft.world.level.material.StandardTank;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;

public class TankBlockRenderState extends BlockEntityRenderState {

  public float fluidMaxX;
  public float fluidMaxZ;
  public int maxY;
  public StandardTank tank;
}
