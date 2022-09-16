package mods.railcraft.world.item;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import com.mojang.authlib.GameProfile;
import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstantsAPI;
import mods.railcraft.api.item.Filter;
import mods.railcraft.api.item.MinecartFactory;
import mods.railcraft.client.emblem.EmblemToolsClient;
import mods.railcraft.util.PlayerUtil;
import mods.railcraft.util.container.ContainerTools;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class LocomotiveItem extends CartItem implements Filter {

  private final DyeColor defaultPrimary;
  private final DyeColor defaultSecondary;
  // protected final ItemStack sample;

  public LocomotiveItem(MinecartFactory minecartPlacer, DyeColor primary, DyeColor secondary,
      Properties properties) {
    super(minecartPlacer, properties);
    this.defaultPrimary = primary;
    this.defaultSecondary = secondary;

    // this.sample = new ItemStack(this, 1);
    // setItemColorData(sample, primary, secondary);
  }

  @Override
  public boolean matches(ItemStack matcher, ItemStack target) {
    return target.getItem() == this && getPrimaryColor(matcher) == getPrimaryColor(target)
        && getSecondaryColor(matcher) == getSecondaryColor(target);
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> info,
      TooltipFlag adv) {
    super.appendHoverText(stack, world, info, adv);
    var owner = getOwner(stack);
    if (owner.getName() != null
        && !RailcraftConstantsAPI.UNKNOWN_PLAYER.equalsIgnoreCase(owner.getName())) {
      info.add(Component.translatable(Translations.Tips.LOCOMOTIVE_ITEM_OWNER, owner.getName())
          .withStyle(ChatFormatting.GRAY));
    }

    var primary = getPrimaryColor(stack);
    info.add(Component.translatable(Translations.Tips.LOCOMOTIVE_ITEM_PRIMARY, primary.getName())
        .withStyle(ChatFormatting.GRAY));

    var secondary = getSecondaryColor(stack);
    info.add(
        Component.translatable(Translations.Tips.LOCOMOTIVE_ITEM_SECONDARY, secondary.getName())
            .withStyle(ChatFormatting.GRAY));

    float whistle = getWhistlePitch(stack);
    var whisteText = whistle < 0 ? "???" : String.format("%.2f", whistle);
    info.add(Component.translatable(Translations.Tips.LOCOMOTIVE_ITEM_WHISTLE, whisteText)
        .withStyle(ChatFormatting.GRAY));

    getEmblem(stack)
        .flatMap(EmblemToolsClient.packageManager()::getEmblem)
        .ifPresent(emblem -> info.add(
            Component.translatable("gui.railcraft.locomotive.tips.item.emblem",
                emblem.displayName())));
  }

  public static void setItemColorData(ItemStack stack, DyeColor primaryColor,
      DyeColor secondaryColor) {
    CompoundTag nbt = ContainerTools.getItemData(stack);
    nbt.putInt("primaryColor", primaryColor.getId());
    nbt.putInt("secondaryColor", secondaryColor.getId());
  }

  public static void setItemWhistleData(ItemStack stack, float whistlePitch) {
    CompoundTag nbt = ContainerTools.getItemData(stack);
    nbt.putFloat("whistlePitch", whistlePitch);
  }

  public static float getWhistlePitch(ItemStack stack) {
    CompoundTag nbt = stack.getTag();
    if (nbt == null || !nbt.contains("whistlePitch", Tag.TAG_FLOAT))
      return -1;
    return nbt.getFloat("whistlePitch");
  }

  public static void setOwnerData(ItemStack stack, GameProfile owner) {
    CompoundTag nbt = ContainerTools.getItemData(stack);
    PlayerUtil.writeOwnerToNBT(nbt, owner);
  }

  public static GameProfile getOwner(ItemStack stack) {
    CompoundTag nbt = stack.getTag();
    if (nbt == null)
      return new GameProfile(null, RailcraftConstantsAPI.UNKNOWN_PLAYER);
    return PlayerUtil.readOwnerFromNBT(nbt);
  }

  public static void setEmblem(ItemStack stack, String emblemIdentifier) {
    CompoundTag nbt = ContainerTools.getItemData(stack);
    nbt.putString("emblem", emblemIdentifier);
  }

  public static Optional<String> getEmblem(ItemStack stack) {
    var tag = stack.getTag();
    return tag == null || !tag.contains("emblem", Tag.TAG_STRING)
        ? Optional.empty()
        : Optional.of(tag.getString("emblem"));
  }

  public static void setModel(ItemStack stack, String modelTag) {
    CompoundTag nbt = ContainerTools.getItemData(stack);
    nbt.putString("model", modelTag);
  }

  public static String getModel(ItemStack stack) {
    CompoundTag nbt = stack.getTag();
    if (nbt == null || !nbt.contains("model", Tag.TAG_STRING))
      return "default";
    return nbt.getString("model");
  }

  public static DyeColor getPrimaryColor(ItemStack stack) {
    CompoundTag nbt = ContainerTools.getItemData(stack);
    if (nbt.contains("primaryColor", Tag.TAG_INT)) {
      return DyeColor.byId(nbt.getInt("primaryColor"));
    } else {
      return ((LocomotiveItem) stack.getItem()).defaultPrimary;
    }
  }

  public static DyeColor getSecondaryColor(ItemStack stack) {
    CompoundTag nbt = ContainerTools.getItemData(stack);
    if (nbt.contains("secondaryColor", Tag.TAG_INT)) {
      return DyeColor.byId(nbt.getInt("secondaryColor"));
    } else {
      return ((LocomotiveItem) stack.getItem()).defaultSecondary;
    }
  }
}
