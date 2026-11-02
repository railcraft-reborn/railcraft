package mods.railcraft.client.renderer.blockentity.state;

import mods.railcraft.world.level.block.SteamEngineBlock;
import mods.railcraft.world.level.block.entity.SteamEngineBlockEntity.EnergyStage;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;

public class SteamEngineRenderState extends BlockEntityRenderState {

  public SteamEngineBlock.Tier tier = SteamEngineBlock.Tier.COMMERCIAL;
  public Direction facing = Direction.UP;
  public EnergyStage stage = EnergyStage.BLUE;
  public float progress;
}
