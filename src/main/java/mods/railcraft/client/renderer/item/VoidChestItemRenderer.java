package mods.railcraft.client.renderer.item;

import java.util.Set;
import org.joml.Vector3f;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.client.renderer.blockentity.VoidChestRenderer;
import net.minecraft.client.model.ChestModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;

public class VoidChestItemRenderer implements NoDataSpecialModelRenderer {

  private final MaterialSet materials;
  private final ChestModel model;
  private final float openness;

  public VoidChestItemRenderer(MaterialSet materials, ChestModel model, float openness) {
    this.materials = materials;
    this.model = model;
    this.openness = openness;
  }

  @Override
  public void submit(ItemDisplayContext displayContext, PoseStack poseStack,
      SubmitNodeCollector nodeCollector, int packedLight, int packedOverlay, boolean hasFoil,
      int outlineColor) {
    nodeCollector.submitModel(
        this.model,
        this.openness,
        poseStack,
        VoidChestRenderer.VOID_CHEST.renderType(RenderType::entityCutout),
        packedLight,
        packedOverlay,
        -1,
        this.materials.get(VoidChestRenderer.VOID_CHEST),
        outlineColor,
        null
    );
  }

  @Override
  public void getExtents(Set<Vector3f> output) {
    PoseStack posestack = new PoseStack();
    this.model.setupAnim(this.openness);
    this.model.root().getExtentsForGui(posestack, output);
  }

  public record Unbaked(ResourceLocation texture, float openness) implements SpecialModelRenderer.Unbaked {

    public static final MapCodec<VoidChestItemRenderer.Unbaked> MAP_CODEC =
        RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("texture").forGetter(VoidChestItemRenderer.Unbaked::texture),
            Codec.FLOAT.optionalFieldOf("openness", 0.0F).forGetter(VoidChestItemRenderer.Unbaked::openness)
        ).apply(instance, VoidChestItemRenderer.Unbaked::new)
    );

    @Override
    public MapCodec<Unbaked> type() {
      return MAP_CODEC;
    }

    public Unbaked(ResourceLocation resourceLocation) {
      this(resourceLocation, 0.0F);
    }

    @Override
    public SpecialModelRenderer<?> bake(SpecialModelRenderer.BakingContext context) {
      var chestModel = new ChestModel(context.entityModelSet().bakeLayer(ModelLayers.CHEST));
      return new VoidChestItemRenderer(context.materials(), chestModel, this.openness);
    }
  }
}
