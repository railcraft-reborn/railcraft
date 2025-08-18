package mods.railcraft.client.color.item;

import org.jetbrains.annotations.Nullable;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.world.item.component.RailcraftDataComponents;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public record LocomotiveColor(int index) implements ItemTintSource {
  public static final MapCodec<LocomotiveColor> MAP_CODEC =
      RecordCodecBuilder.mapCodec(loco ->
          loco.group(
              Codec.INT.fieldOf("index").forGetter(LocomotiveColor::index))
              .apply(loco, LocomotiveColor::new));

  @Override
  public int calculate(ItemStack itemStack, @Nullable ClientLevel clientLevel,
      @Nullable LivingEntity livingEntity) {
    var color = itemStack.get(RailcraftDataComponents.LOCOMOTIVE_COLOR);
    var selectedColor = index == 0 ? color.primary() : color.secondary();
    return ARGB.opaque(selectedColor.getMapColor().col);
  }

  @Override
  public MapCodec<? extends ItemTintSource> type() {
    return MAP_CODEC;
  }
}
