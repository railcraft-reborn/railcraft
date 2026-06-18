package mods.railcraft.client.renderer.item;

import java.util.function.Consumer;
import org.joml.Vector3fc;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.client.renderer.blockentity.VoidChestRenderer;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.chest.ChestModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.resources.Identifier;

public class VoidChestItemRenderer implements NoDataSpecialModelRenderer {

  private final SpriteGetter sprites;
  private final ChestModel model;
  private final float openness;

  public VoidChestItemRenderer(SpriteGetter sprites, ChestModel model, float openness) {
    this.sprites = sprites;
    this.model = model;
    this.openness = openness;
  }

  @Override
  public void submit(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight,
      int packedOverlay, boolean hasFoil, int outlineColor) {
    nodeCollector.submitModel(
        this.model,
        this.openness,
        poseStack,
        VoidChestRenderer.VOID_CHEST.renderType(RenderTypes::entityCutout),
        packedLight,
        packedOverlay,
        -1,
        this.sprites.get(VoidChestRenderer.VOID_CHEST),
        outlineColor,
        null
    );
  }

  @Override
  public void getExtents(Consumer<Vector3fc> consumer) {
    PoseStack posestack = new PoseStack();
    this.model.setupAnim(this.openness);
    this.model.root().getExtentsForGui(posestack, consumer);
  }

  public record Unbaked(Identifier texture, float openness) implements SpecialModelRenderer.Unbaked {

    public static final MapCodec<VoidChestItemRenderer.Unbaked> MAP_CODEC =
        RecordCodecBuilder.mapCodec(instance -> instance.group(
            Identifier.CODEC.fieldOf("texture").forGetter(VoidChestItemRenderer.Unbaked::texture),
            Codec.FLOAT.optionalFieldOf("openness", 0.0F).forGetter(VoidChestItemRenderer.Unbaked::openness)
        ).apply(instance, VoidChestItemRenderer.Unbaked::new)
    );

    @Override
    public MapCodec<Unbaked> type() {
      return MAP_CODEC;
    }

    public Unbaked(Identifier identifier) {
      this(identifier, 0.0F);
    }

    @Override
    public SpecialModelRenderer<?> bake(SpecialModelRenderer.BakingContext context) {
      var chestModel = new ChestModel(context.entityModelSet().bakeLayer(ModelLayers.CHEST));
      return new VoidChestItemRenderer(context.sprites(), chestModel, this.openness);
    }
  }
}
