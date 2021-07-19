package mods.railcraft.client.renderer.model;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import mods.railcraft.world.level.block.SignalBoxBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.client.renderer.model.BlockPart;
import net.minecraft.client.renderer.model.BlockPartFace;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IModelTransform;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.geometry.IModelGeometry;

public class TextureReplacementModel implements IModelGeometry<TextureReplacementModel> {

  private final Map<Direction, Map<String, String>> textureReplacements;
  private final BlockModel delegateModel;

  public TextureReplacementModel(Map<Direction, Map<String, String>> textureReplacements,
      BlockModel delegateModel) {
    this.textureReplacements = textureReplacements;
    this.delegateModel = delegateModel;
  }

  @Override
  public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery,
      Function<RenderMaterial, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform,
      ItemOverrideList overrides, ResourceLocation modelLocation) {
    ImmutableMap.Builder<Direction, Map<ResourceLocation, BakedQuad>> replacements =
        ImmutableMap.builder();

    @SuppressWarnings("deprecation")
    List<BlockPart> elements = this.delegateModel.getElements();
    for (Map.Entry<Direction, Map<String, String>> replacementsEntry : this.textureReplacements
        .entrySet()) {
      Direction direction = replacementsEntry.getKey();
      ImmutableMap.Builder<ResourceLocation, BakedQuad> faceReplacements =
          ImmutableMap.builder();
      for (BlockPart element : elements) {
        BlockPartFace face = element.faces.get(direction);
        String replacementTexture = replacementsEntry.getValue().get(face.texture);
        if (replacementTexture != null) {

          RenderMaterial replacementMaterial =
              this.delegateModel.getMaterial(replacementTexture);
          TextureAtlasSprite replacementSprite = spriteGetter.apply(replacementMaterial);
          RenderMaterial originalMaterial =
              this.delegateModel.getMaterial(face.texture);
          BakedQuad replacementQuad =
              BlockModel.makeBakedQuad(element, face, replacementSprite, direction,
                  modelTransform, modelLocation);
          faceReplacements.put(originalMaterial.texture(), replacementQuad);
        }
      }

      replacements.put(direction, faceReplacements.build());
    }

    @SuppressWarnings("deprecation")
    IBakedModel delegateBakedModel =
        this.delegateModel.bake(bakery, spriteGetter, modelTransform, modelLocation);

    return new Baked(delegateBakedModel, replacements.build());
  }

  @Override
  public Collection<RenderMaterial> getTextures(IModelConfiguration owner,
      Function<ResourceLocation, IUnbakedModel> modelGetter,
      Set<Pair<String, String>> missingTextureErrors) {
    return this.delegateModel.getMaterials(modelGetter, missingTextureErrors);
  }


  public static class Baked implements IBakedModel {

    private final IBakedModel delegateBakedModel;
    private final Map<Direction, Map<ResourceLocation, BakedQuad>> replacements;

    public Baked(IBakedModel delegateModel,
        Map<Direction, Map<ResourceLocation, BakedQuad>> replacements) {
      this.delegateBakedModel = delegateModel;
      this.replacements = replacements;
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction face,
        Random p_200117_3_) {
      face = Direction.SOUTH;
      @SuppressWarnings("deprecation")
      List<BakedQuad> originalQuads = this.delegateBakedModel.getQuads(state, face, p_200117_3_);
//      if (state != null && face != null && face.getAxis().isHorizontal()
//          && SignalBoxBlock.isConnected(state, face)) {
        ImmutableList.Builder<BakedQuad> finalQuads = ImmutableList.builder();
        Map<ResourceLocation, BakedQuad> replacementQuads = this.replacements.get(face);
        for (BakedQuad quad : originalQuads) {
          BakedQuad replacementQuad = replacementQuads.get(quad.getSprite().getName());
          finalQuads.add(replacementQuad == null ? quad : replacementQuad);
        }
        return finalQuads.build();
//      }
//      return originalQuads;
    }

    @Override
    public boolean useAmbientOcclusion() {
      return this.delegateBakedModel.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
      return this.delegateBakedModel.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
      return this.delegateBakedModel.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
      return this.delegateBakedModel.isCustomRenderer();
    }

    @SuppressWarnings("deprecation")
    @Override
    public TextureAtlasSprite getParticleIcon() {
      return this.delegateBakedModel.getParticleIcon();
    }

    @SuppressWarnings("deprecation")
    @Override
    public ItemCameraTransforms getTransforms() {
      return this.delegateBakedModel.getTransforms();
    }

    @Override
    public ItemOverrideList getOverrides() {
      return this.delegateBakedModel.getOverrides();
    }
  }

  public static class Loader implements IModelLoader<TextureReplacementModel> {

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {}

    @Override
    public TextureReplacementModel read(JsonDeserializationContext deserializationContext,
        JsonObject modelContents) {
      BlockModel delegateModel =
          deserializationContext.deserialize(modelContents.get("model"), BlockModel.class);
      ImmutableMap.Builder<Direction, Map<String, String>> textureReplacements =
          ImmutableMap.builder();
      JsonObject textureReplacementsJson = modelContents.getAsJsonObject("textureReplacements");
      for (Direction direction : Direction.values()) {
        JsonObject replacementsJson =
            textureReplacementsJson.getAsJsonObject(direction.getSerializedName());
        if (replacementsJson != null) {
          ImmutableMap.Builder<String, String> replacements = ImmutableMap.builder();
          for (Entry<String, JsonElement> entry : replacementsJson.entrySet()) {
            replacements.put(entry.getKey(), entry.getValue().getAsString());
          }
          textureReplacements.put(direction, replacements.build());
        }
      }
      return new TextureReplacementModel(textureReplacements.build(), delegateModel);
    }
  }
}
