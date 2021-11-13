package mods.railcraft.client.model;

import java.util.function.Function;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.ResourceLocation;

/**
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class SimpleCubeModel extends SimpleTexturedModel {

  public SimpleCubeModel() {
    this(RenderType::entityCutoutNoCull);
  }

  public SimpleCubeModel(Function<ResourceLocation, RenderType> renderTypeFactory) {
    super(renderTypeFactory);
    this.renderer.setTexSize(64, 32);
    this.renderer.addBox(-8F, -8F, -8F, 16, 16, 16);
  }
}
