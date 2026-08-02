package mods.railcraft.world.item.crafting;

import com.mojang.serialization.MapCodec;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.common.Tags;

public class StoneTieRecipe extends TieRecipe {

  private static final StoneTieRecipe INSTANCE = new StoneTieRecipe();
  private static final MapCodec<StoneTieRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
  private static final StreamCodec<RegistryFriendlyByteBuf, StoneTieRecipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);
  public static final RecipeSerializer<StoneTieRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

  public StoneTieRecipe() {
    super(Tags.Fluids.WATER, () -> RailcraftItems.STONE_TIE.toStack());
  }

  @Override
  protected boolean testIngredient(ItemStack itemPresent, int index) {
    if (index == 0 || index == 2) {
      return itemPresent.is(RailcraftItems.BAG_OF_CEMENT.get());
    } else if (index == 1) {
      return itemPresent.is(RailcraftItems.REBAR.get());
    }
    return false;
  }

  @Override
  public RecipeSerializer<StoneTieRecipe> getSerializer() {
    return SERIALIZER;
  }
}
