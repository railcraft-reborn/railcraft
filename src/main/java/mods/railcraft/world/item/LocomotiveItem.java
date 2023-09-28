package mods.railcraft.world.item;

import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import com.mojang.authlib.GameProfile;
import mods.railcraft.Translations;
import mods.railcraft.api.item.Filter;
import mods.railcraft.api.item.MinecartFactory;
import mods.railcraft.client.emblem.EmblemClientUtil;
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
    if (owner != null && !StringUtils.isBlank(owner.getName())) {
      tooltip.add(Component.translatable(Translations.Tips.LOCOMOTIVE_ITEM_OWNER, owner.getName())
          .withStyle(ChatFormatting.GRAY));
    }

    tooltip.add(Component.translatable(Translations.Tips.LOCOMOTIVE_ITEM_PRIMARY)
        .append(" ")
        .append(Component.translatable("color.minecraft." + getPrimaryColor(stack).getName()))
        .withStyle(ChatFormatting.GRAY));

    tooltip.add(Component.translatable(Translations.Tips.LOCOMOTIVE_ITEM_SECONDARY)
        .append(" ")
        .append(Component.translatable("color.minecraft." + getSecondaryColor(stack).getName()))
        .withStyle(ChatFormatting.GRAY));

    float whistle = getWhistlePitch(stack);
    var whistleText = whistle < 0 ? "???" : String.format("%.2f", whistle);
    tooltip.add(Component.translatable(Translations.Tips.LOCOMOTIVE_ITEM_WHISTLE, whistleText)
        .withStyle(ChatFormatting.GRAY));

    getEmblem(stack)
        .flatMap(EmblemClientUtil.packageManager()::getEmblem)
        .ifPresent(emblem -> tooltip.add(
            Component.translatable("gui.railcraft.locomotive.tips.item.emblem",
                emblem.displayName())));
  }

  public static void setItemColorData(ItemStack stack, DyeColor primaryColor,
      DyeColor secondaryColor) {
    var tag = stack.getOrCreateTag();
    tag.putInt("primaryColor", primaryColor.getId());
    tag.putInt("secondaryColor", secondaryColor.getId());
  }

  public static void setItemWhistleData(ItemStack stack, float whistlePitch) {
    var tag = stack.getOrCreateTag();
    tag.putFloat("whistlePitch", whistlePitch);
  }

  public static float getWhistlePitch(ItemStack stack) {
    var tag = stack.getTag();
    if (tag == null || !tag.contains("whistlePitch", Tag.TAG_FLOAT))
      return -1;
    return tag.getFloat("whistlePitch");
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

  public static void setEmblem(ItemStack stack, String emblemIdentifier) {
    var tag = stack.getOrCreateTag();
    tag.putString("emblem", emblemIdentifier);
  }

  public static Optional<String> getEmblem(ItemStack stack) {
    var tag = stack.getOrCreateTag();
    return !tag.contains("emblem", Tag.TAG_STRING)
        ? Optional.empty()
        : Optional.of(tag.getString("emblem"));
  }

  public static void setModel(ItemStack stack, String modelTag) {
    var tag = stack.getOrCreateTag();
    tag.putString("model", modelTag);
  }

  public static String getModel(ItemStack stack) {
    var tag = stack.getOrCreateTag();
    if (!tag.contains("model", Tag.TAG_STRING))
      return "default";
    return tag.getString("model");
  }

  public static DyeColor getPrimaryColor(ItemStack stack) {
    var tag = stack.getOrCreateTag();
    if (tag.contains("primaryColor", Tag.TAG_INT)) {
      return DyeColor.byId(tag.getInt("primaryColor"));
    } else {
      return ((LocomotiveItem) stack.getItem()).defaultPrimary;
    }
  }

  public static DyeColor getSecondaryColor(ItemStack stack) {
    var tag = stack.getOrCreateTag();
    if (tag.contains("secondaryColor", Tag.TAG_INT)) {
      return DyeColor.byId(tag.getInt("secondaryColor"));
    } else {
      return ((LocomotiveItem) stack.getItem()).defaultSecondary;
    }
  }
}
