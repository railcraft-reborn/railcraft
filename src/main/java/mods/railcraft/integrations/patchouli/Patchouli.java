package mods.railcraft.integrations.patchouli;

import mods.railcraft.Railcraft;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import vazkii.patchouli.api.PatchouliAPI;

public class Patchouli {

  public static void setup() {
    var patchouliApi = PatchouliAPI.get();

    var crusherBlock = patchouliApi.predicateMatcher(RailcraftBlocks.CRUSHER.get(),
        Patchouli::validEdge);

    var crusher = patchouliApi.makeMultiblock(new String[][]{
            {"CC", "CC", "CC"},
            {"CC", "0C", "CC"}
        },
        'C', crusherBlock, '0', crusherBlock
    ).setSymmetrical(false);
    patchouliApi.registerMultiblock(new ResourceLocation(Railcraft.ID, "crusher"), crusher);
  }

  private static boolean validEdge(BlockState state) {
    return state.is(RailcraftBlocks.CRUSHER.get());
  }
}
