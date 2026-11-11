package mods.railcraft.client.renderer.item;

import java.util.function.Consumer;
import org.joml.Vector3fc;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.client.model.RailcraftModelLayers;
import mods.railcraft.client.model.SteamEngineModel;
import mods.railcraft.client.renderer.blockentity.SteamEngineRenderer;
import mods.railcraft.world.level.block.SteamEngineBlock;
import mods.railcraft.world.level.block.entity.SteamEngineBlockEntity.EnergyStage;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;

public class SteamEngineItemRenderer implements NoDataSpecialModelRenderer {

  private static final SteamEngineModel.State STATE =
      new SteamEngineModel.State(EnergyStage.BLUE, 0.0F);

  private final SteamEngineModel model;
  private final SteamEngineBlock.Tier tier;

  public SteamEngineItemRenderer(SteamEngineModel model, SteamEngineBlock.Tier tier) {
    this.model = model;
    this.tier = tier;
  }

  @Override
  public void submit(PoseStack poseStack, SubmitNodeCollector collector, int packedLight,
      int packedOverlay, boolean hasFoil, int outlineColor) {
    collector.submitModel(
        this.model,
        STATE,
        poseStack,
        this.model.renderType(SteamEngineRenderer.getTexture(this.tier)),
        packedLight,
        packedOverlay,
        -1,
        null,
        outlineColor);
  }

  @Override
  public void getExtents(Consumer<Vector3fc> consumer) {
    this.model.setupAnim(STATE);
    this.model.root().getExtentsForGui(new PoseStack(), consumer);
  }

  public record Unbaked(SteamEngineBlock.Tier tier) implements NoDataSpecialModelRenderer.Unbaked {

    public static final MapCodec<Unbaked> MAP_CODEC =
        RecordCodecBuilder.mapCodec(instance -> instance.group(
            SteamEngineBlock.Tier.CODEC.fieldOf("tier").forGetter(Unbaked::tier)
        ).apply(instance, Unbaked::new));

    @Override
    public MapCodec<Unbaked> type() {
      return MAP_CODEC;
    }

    @Override
    public SteamEngineItemRenderer bake(SpecialModelRenderer.BakingContext context) {
      var model = new SteamEngineModel(
          context.entityModelSet().bakeLayer(RailcraftModelLayers.STEAM_ENGINE));
      return new SteamEngineItemRenderer(model, this.tier);
    }
  }
}
