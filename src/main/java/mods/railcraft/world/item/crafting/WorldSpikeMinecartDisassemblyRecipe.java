package mods.railcraft.world.item.crafting;

import com.mojang.serialization.MapCodec;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class WorldSpikeMinecartDisassemblyRecipe extends CartDisassemblyRecipe {

  private static final WorldSpikeMinecartDisassemblyRecipe INSTANCE = new WorldSpikeMinecartDisassemblyRecipe();
  private static final MapCodec<WorldSpikeMinecartDisassemblyRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
  private static final StreamCodec<RegistryFriendlyByteBuf, WorldSpikeMinecartDisassemblyRecipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);
  public static final RecipeSerializer<WorldSpikeMinecartDisassemblyRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

  public WorldSpikeMinecartDisassemblyRecipe() {
    super(RailcraftItems.WORLD_SPIKE_MINECART.get(), RailcraftItems.WORLD_SPIKE.get());
  }

  @Override
  public RecipeSerializer<WorldSpikeMinecartDisassemblyRecipe> getSerializer() {
    return SERIALIZER;
  }
}
