package mods.railcraft.world.item.crafting;

import com.mojang.serialization.MapCodec;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class BedMinecartDisassemblyRecipe extends CartDisassemblyRecipe {

  private static final BedMinecartDisassemblyRecipe INSTANCE = new BedMinecartDisassemblyRecipe();
  private static final MapCodec<BedMinecartDisassemblyRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
  private static final StreamCodec<RegistryFriendlyByteBuf, BedMinecartDisassemblyRecipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);
  public static final RecipeSerializer<BedMinecartDisassemblyRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

  public BedMinecartDisassemblyRecipe() {
    super(RailcraftItems.BED_MINECART.get(), Items.WHITE_BED);
  }

  @Override
  public RecipeSerializer<? extends CustomRecipe> getSerializer() {
    return SERIALIZER;
  }
}
