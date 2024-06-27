package mods.railcraft.world.item;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import com.mojang.authlib.GameProfile;
import mods.railcraft.Translations;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.api.item.Filter;
import mods.railcraft.api.item.MinecartFactory;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class LocomotiveItem extends CartItem implements Filter {

  private final DyeColor defaultPrimary;
  private final DyeColor defaultSecondary;

  public LocomotiveItem(MinecartFactory minecartPlacer, DyeColor primary, DyeColor secondary,
      Properties properties) {
    super(minecartPlacer, properties);
    this.defaultPrimary = primary;
    this.defaultSecondary = secondary;
  }

  @Override
  public boolean matches(ItemStack matcher, ItemStack target) {
    return target.is(this) && getPrimaryColor(matcher) == getPrimaryColor(target)
        && getSecondaryColor(matcher) == getSecondaryColor(target);
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip,
      TooltipFlag adv) {
    var owner = getOwner(stack);
    if (owner != null && StringUtils.isNotBlank(owner.getName())) {
      tooltip.add(Component.translatable(Translations.Tips.LOCOMOTIVE_ITEM_OWNER)
          .withStyle(ChatFormatting.AQUA)
          .append(" ")
          .append(Component.literal(owner.getName()).withStyle(ChatFormatting.GRAY)));
    }

    tooltip.add(Component.translatable(Translations.Tips.LOCOMOTIVE_ITEM_PRIMARY)
        .withStyle(ChatFormatting.AQUA)
        .append(" ")
        .append(Component.translatable("color.minecraft." + getPrimaryColor(stack).getName())
        .withStyle(ChatFormatting.GRAY)));

    tooltip.add(Component.translatable(Translations.Tips.LOCOMOTIVE_ITEM_SECONDARY)
        .withStyle(ChatFormatting.AQUA)
        .append(" ")
        .append(Component.translatable("color.minecraft." + getSecondaryColor(stack).getName())
        .withStyle(ChatFormatting.GRAY)));

    float whistle = getWhistlePitch(stack);
    if (whistle < 0) {
      tooltip.add(Component.translatable(Translations.Tips.LOCOMOTIVE_ITEM_NO_WHISTLE)
          .withStyle(ChatFormatting.GRAY));
    } else {
      tooltip.add(Component.translatable(Translations.Tips.LOCOMOTIVE_ITEM_WHISTLE)
          .withStyle(ChatFormatting.AQUA)
          .append(" ")
          .append(Component.literal(String.format("%.2f", whistle))
              .withStyle(ChatFormatting.GRAY)));
    }
  }

  public static void setItemColorData(ItemStack stack, DyeColor primaryColor,
      DyeColor secondaryColor) {
    var tag = stack.getOrCreateTag();
    tag.putInt(CompoundTagKeys.PRIMARY_COLOR, primaryColor.getId());
    tag.putInt(CompoundTagKeys.SECONDARY_COLOR, secondaryColor.getId());
  }

  public static void setItemWhistleData(ItemStack stack, float whistlePitch) {
    var tag = stack.getOrCreateTag();
    tag.putFloat(CompoundTagKeys.WHISTLE_PITCH, whistlePitch);
  }

  public static float getWhistlePitch(ItemStack stack) {
    var tag = stack.getTag();
    if (tag == null || !tag.contains(CompoundTagKeys.WHISTLE_PITCH, Tag.TAG_FLOAT))
      return -1;
    return tag.getFloat(CompoundTagKeys.WHISTLE_PITCH);
  }

  public static void setOwnerData(ItemStack stack, GameProfile owner) {
    var tag = stack.getOrCreateTag();
    NbtUtils.writeGameProfile(tag, owner);
  }

  @Nullable
  public static GameProfile getOwner(ItemStack stack) {
    var tag = stack.getOrCreateTag();
    return NbtUtils.readGameProfile(tag);
  }

  public static DyeColor getPrimaryColor(ItemStack stack) {
    var tag = stack.getOrCreateTag();
    if (tag.contains(CompoundTagKeys.PRIMARY_COLOR, Tag.TAG_INT)) {
      return DyeColor.byId(tag.getInt(CompoundTagKeys.PRIMARY_COLOR));
    } else {
      return ((LocomotiveItem) stack.getItem()).defaultPrimary;
    }
  }

  public static DyeColor getSecondaryColor(ItemStack stack) {
    var tag = stack.getOrCreateTag();
    if (tag.contains(CompoundTagKeys.SECONDARY_COLOR, Tag.TAG_INT)) {
      return DyeColor.byId(tag.getInt(CompoundTagKeys.SECONDARY_COLOR));
    } else {
      return ((LocomotiveItem) stack.getItem()).defaultSecondary;
    }
  }
}
