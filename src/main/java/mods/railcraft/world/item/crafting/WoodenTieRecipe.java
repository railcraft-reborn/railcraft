package mods.railcraft.world.item.crafting;

import com.mojang.serialization.MapCodec;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class WoodenTieRecipe extends TieRecipe {

  private static final WoodenTieRecipe INSTANCE = new WoodenTieRecipe();
  private static final MapCodec<WoodenTieRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
  private static final StreamCodec<RegistryFriendlyByteBuf, WoodenTieRecipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);
  public static final RecipeSerializer<WoodenTieRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

  public WoodenTieRecipe() {
    super(RailcraftTags.Fluids.CREOSOTE,
        RailcraftItems.WOODEN_TIE.toStack(3));
  }

  @Override
  protected boolean testIngredient(ItemStack itemPresent, int index) {
    return itemPresent.is(ItemTags.WOODEN_SLABS);
  }

  @Override
  public RecipeSerializer<WoodenTieRecipe> getSerializer() {
    return SERIALIZER;
  }
}
