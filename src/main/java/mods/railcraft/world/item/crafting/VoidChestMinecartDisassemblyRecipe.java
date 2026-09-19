package mods.railcraft.world.item.crafting;

import com.mojang.serialization.MapCodec;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class VoidChestMinecartDisassemblyRecipe extends CartDisassemblyRecipe {

  private static final VoidChestMinecartDisassemblyRecipe INSTANCE = new VoidChestMinecartDisassemblyRecipe();
  private static final MapCodec<VoidChestMinecartDisassemblyRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
  private static final StreamCodec<RegistryFriendlyByteBuf, VoidChestMinecartDisassemblyRecipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);
  public static final RecipeSerializer<VoidChestMinecartDisassemblyRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

  public VoidChestMinecartDisassemblyRecipe() {
    super(RailcraftItems.VOID_CHEST_MINECART.get(), RailcraftItems.VOID_CHEST.get());
  }

  @Override
  public RecipeSerializer<? extends CustomRecipe> getSerializer() {
    return SERIALIZER;
  }
}
