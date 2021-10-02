package mods.railcraft.world.item;

import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import org.apache.logging.log4j.util.Strings;
import com.mojang.authlib.GameProfile;
import mods.railcraft.api.core.RailcraftConstantsAPI;
import mods.railcraft.api.item.Filter;
import mods.railcraft.client.emblem.Emblem;
import mods.railcraft.client.emblem.EmblemToolsClient;
import mods.railcraft.util.PlayerUtil;
import mods.railcraft.util.inventory.InvTools;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class LocomotiveItem extends CartItem implements Filter {

  private final DyeColor defaultPrimary;
  private final DyeColor defaultSecondary;
  protected final ItemStack sample;

  public LocomotiveItem(Supplier<? extends EntityType<?>> cart,
      DyeColor primary, DyeColor secondary, Properties properties) {
    super(cart, properties);
    this.defaultPrimary = primary;
    this.defaultSecondary = secondary;
    this.sample = new ItemStack(this, 1);
    setItemColorData(sample, primary, secondary);
  }

  @Override
  public boolean matches(ItemStack matcher, ItemStack target) {
    return target.getItem() == this && getPrimaryColor(matcher) == getPrimaryColor(target)
        && getSecondaryColor(matcher) == getSecondaryColor(target);
  }


  // @Override
  // public ColorPlugin.IColorFunctionItem colorHandler() {
  // return (stack, tintIndex) -> {
  // switch (tintIndex) {
  // case 0:
  // return getPrimaryColor(stack).getHexColor();
  // case 1:
  // return getSecondaryColor(stack).getHexColor();
  // default:
  // return DyeColor.WHITE.getHexColor();
  // }
  // };
  // }

  // @Override
  // @SideOnly(Side.CLIENT)
  // public IIcon getIcon(ItemStack stack, int pass) {
  // String rendererTag = getModel(stack);
  // LocomotiveModelRenderer renderer = renderType.getRenderer(rendererTag);
  // if (renderer == null)
  // return RenderTools.getMissingTexture();
  // IIcon[] icons = renderer.getItemIcons();
  // if (pass >= icons.length || icons[pass] == null)
  // return blankIcon;
  // return renderer.getItemIcons()[pass];
  // }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> info,
      ITooltipFlag adv) {
    super.appendHoverText(stack, world, info, adv);
    GameProfile owner = getOwner(stack);
    if (owner.getName() != null
        && !RailcraftConstantsAPI.UNKNOWN_PLAYER.equalsIgnoreCase(owner.getName())) {
      info.add(new TranslationTextComponent("gui.railcraft.locomotive.tips.item.owner",
          owner.getName()));
    }

    DyeColor primary = getPrimaryColor(stack);
    info.add(new TranslationTextComponent("gui.railcraft.locomotive.tips.item.primary",
        primary.getName()));

    DyeColor secondary = getSecondaryColor(stack);
    info.add(new TranslationTextComponent("gui.railcraft.locomotive.tips.item.secondary",
        secondary.getName()));

    float whistle = getWhistlePitch(stack);
    info.add(new TranslationTextComponent("gui.railcraft.locomotive.tips.item.whistle",
        whistle < 0 ? "???" : String.format("%.2f", whistle)));

    String emblemIdent = getEmblem(stack);
    if (!Strings.isEmpty(emblemIdent) && EmblemToolsClient.packageManager != null) {
      Emblem emblem = EmblemToolsClient.packageManager.getEmblem(emblemIdent);
      if (emblem != null) {
        info.add(new TranslationTextComponent("gui.railcraft.locomotive.tips.item.emblem",
            emblem.displayName));
      }
    }
  }

  public static void setItemColorData(ItemStack stack, DyeColor primaryColor,
      DyeColor secondaryColor) {
    CompoundNBT nbt = InvTools.getItemData(stack);
    nbt.putInt("primaryColor", primaryColor.getId());
    nbt.putInt("secondaryColor", secondaryColor.getId());
  }

  public static void setItemWhistleData(ItemStack stack, float whistlePitch) {
    CompoundNBT nbt = InvTools.getItemData(stack);
    nbt.putFloat("whistlePitch", whistlePitch);
  }

  public static float getWhistlePitch(ItemStack stack) {
    CompoundNBT nbt = stack.getTag();
    if (nbt == null || !nbt.contains("whistlePitch", Constants.NBT.TAG_FLOAT))
      return -1;
    return nbt.getFloat("whistlePitch");
  }

  public static void setOwnerData(ItemStack stack, GameProfile owner) {
    CompoundNBT nbt = InvTools.getItemData(stack);
    PlayerUtil.writeOwnerToNBT(nbt, owner);
  }

  public static GameProfile getOwner(ItemStack stack) {
    CompoundNBT nbt = stack.getTag();
    if (nbt == null)
      return new GameProfile(null, RailcraftConstantsAPI.UNKNOWN_PLAYER);
    return PlayerUtil.readOwnerFromNBT(nbt);
  }

  public static void setEmblem(ItemStack stack, String emblemIdentifier) {
    CompoundNBT nbt = InvTools.getItemData(stack);
    nbt.putString("emblem", emblemIdentifier);
  }

  public static String getEmblem(ItemStack stack) {
    CompoundNBT nbt = stack.getTag();
    if (nbt == null || !nbt.contains("emblem", Constants.NBT.TAG_STRING))
      return "";
    return nbt.getString("emblem");
  }

  public static void setModel(ItemStack stack, String modelTag) {
    CompoundNBT nbt = InvTools.getItemData(stack);
    nbt.putString("model", modelTag);
  }

  public static String getModel(ItemStack stack) {
    CompoundNBT nbt = stack.getTag();
    if (nbt == null || !nbt.contains("model", Constants.NBT.TAG_STRING))
      return "default";
    return nbt.getString("model");
  }

  public static DyeColor getPrimaryColor(ItemStack stack) {
    CompoundNBT nbt = InvTools.getItemData(stack);
    if (nbt.contains("primaryColor", Constants.NBT.TAG_INT)) {
      return DyeColor.byId(nbt.getInt("primaryColor"));
    } else {
      return ((LocomotiveItem) stack.getItem()).defaultPrimary;
    }
  }

  public static DyeColor getSecondaryColor(ItemStack stack) {
    CompoundNBT nbt = InvTools.getItemData(stack);
    if (nbt.contains("secondaryColor", Constants.NBT.TAG_INT)) {
      return DyeColor.byId(nbt.getInt("secondaryColor"));
    } else {
      return ((LocomotiveItem) stack.getItem()).defaultSecondary;
    }
  }
}
