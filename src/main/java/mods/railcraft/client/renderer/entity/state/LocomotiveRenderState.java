package mods.railcraft.client.renderer.entity.state;

import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;

public class LocomotiveRenderState extends RailcraftMinecartRenderState {

  public int primaryColor;
  public int secondaryColor;
  public Locomotive.Mode mode;
  public String destination = "";
}
