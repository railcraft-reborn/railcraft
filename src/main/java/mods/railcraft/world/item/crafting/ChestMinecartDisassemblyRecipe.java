package mods.railcraft.world.item.crafting;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ChestMinecartDisassemblyRecipe extends CartDisassemblyRecipe {

  private static final ChestMinecartDisassemblyRecipe INSTANCE = new ChestMinecartDisassemblyRecipe();
  private static final MapCodec<ChestMinecartDisassemblyRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
  private static final StreamCodec<RegistryFriendlyByteBuf, ChestMinecartDisassemblyRecipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);
  public static final RecipeSerializer<ChestMinecartDisassemblyRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

  public ChestMinecartDisassemblyRecipe() {
    super(Items.CHEST_MINECART, Items.CHEST);
  }

  @Override
  public RecipeSerializer<ChestMinecartDisassemblyRecipe> getSerializer() {
    return SERIALIZER;
  }
}
