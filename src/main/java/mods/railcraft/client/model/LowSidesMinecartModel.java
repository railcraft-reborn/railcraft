package mods.railcraft.client.model;

import java.util.Arrays;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class LowSidesMinecartModel<T extends Entity> extends SegmentedModel<T> {

  private final ModelRenderer[] cubes = new ModelRenderer[6];

  public LowSidesMinecartModel() {
    this(0f);
  }

  public LowSidesMinecartModel(float scale) {
    cubes[0] = new ModelRenderer(this, 0, 10);
    cubes[1] = new ModelRenderer(this, 0, 0);
    cubes[2] = new ModelRenderer(this, 0, 0);
    cubes[3] = new ModelRenderer(this, 0, 0);
    cubes[4] = new ModelRenderer(this, 0, 0);
    cubes[5] = new ModelRenderer(this, 44, 10);
    byte length = 20;
    byte heightEnds = 8;
    byte heightSides = 6;
    byte width = 16;
    byte yOffset = 4;
    cubes[0].addBox((float) (-length / 2), (float) (-width / 2), -1.0F, length, width, 2,
        scale);
    cubes[0].setPos(0.0F, (float) yOffset, 0.0F);
    cubes[5].addBox((float) (-length / 2 + 1), (float) (-width / 2 + 1), -1.0F, length - 2,
        width - 2, 1, scale);
    cubes[5].setPos(0.0F, (float) yOffset, 0.0F);
    cubes[1].addBox((float) (-length / 2 + 2), (float) (-heightEnds - 1), -1.0F, length - 4,
        heightEnds, 2, scale);
    cubes[1].setPos((float) (-length / 2 + 1), (float) yOffset, 0.0F);
    cubes[2].addBox((float) (-length / 2 + 2), (float) (-heightEnds - 1), -1.0F, length - 4,
        heightEnds, 2, scale);
    cubes[2].setPos((float) (length / 2 - 1), (float) yOffset, 0.0F);
    cubes[3].addBox((float) (-length / 2 + 2), (float) (-heightSides - 1), -1.0F, length - 4,
        heightSides, 2, scale);
    cubes[3].setPos(0.0F, (float) yOffset, (float) (-width / 2 + 1));
    cubes[4].addBox((float) (-length / 2 + 2), (float) (-heightSides - 1), -1.0F, length - 4,
        heightSides, 2, scale);
    cubes[4].setPos(0.0F, (float) yOffset, (float) (width / 2 - 1));
    cubes[0].xRot = ((float) Math.PI / 2F);
    cubes[1].yRot = ((float) Math.PI * 3F / 2F);
    cubes[2].yRot = ((float) Math.PI / 2F);
    cubes[3].yRot = (float) Math.PI;
    cubes[5].xRot = -((float) Math.PI / 2F);
  }

  @Override
  public void setupAnim(T p_225597_1_, float p_225597_2_, float p_225597_3_, float p_225597_4_,
      float p_225597_5_, float p_225597_6_) {
    this.cubes[5].y = 4.0F - p_225597_4_;
  }

  @Override
  public Iterable<ModelRenderer> parts() {
    return Arrays.asList(this.cubes);
  }
}
