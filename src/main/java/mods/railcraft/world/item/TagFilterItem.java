package mods.railcraft.world.item;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import org.apache.commons.lang3.StringUtils;
import mods.railcraft.Translations;
import mods.railcraft.api.item.Filter;
import mods.railcraft.integrations.jei.JeiSearchable;
import mods.railcraft.world.item.component.RailcraftDataComponents;
import mods.railcraft.world.item.component.TagFilterComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

public class TagFilterItem extends Item implements Filter, JeiSearchable {

  /**
   * Most specific tags first, so that a copper ingot starts on <code>c:ingots/copper</code> instead
   * of <code>c:ingots</code>.
   */
  private static final Comparator<TagKey<Item>> TAG_ORDER =
      Comparator.comparingInt((TagKey<Item> tag) -> -StringUtils.countMatches(tag.location().getPath(), '/'))
              .thenComparing(tag -> tag.location().toString());

  public TagFilterItem(Properties properties) {
    super(properties);
  }

  @Override
  public boolean matches(ItemStack matcher, ItemStack target) {
    var filter = matcher.get(RailcraftDataComponents.TAG_FILTER);
    return filter != null && target.is(filter.tag());
  }

  public static List<TagKey<Item>> getTags(Item item) {
    return item.builtInRegistryHolder().tags().sorted(TAG_ORDER).toList();
  }

  public static boolean setPrototype(ItemStack filter, Item prototype) {
    var tags = getTags(prototype);
    if (tags.isEmpty()) {
      return false;
    }
    filter.set(RailcraftDataComponents.TAG_FILTER,
        new TagFilterComponent(prototype.builtInRegistryHolder(), tags.getFirst()));
    return true;
  }

  public static boolean cycleTag(ItemStack filter) {
    var component = filter.get(RailcraftDataComponents.TAG_FILTER);
    if (component == null) {
      return false;
    }
    var tags = getTags(component.prototype().value());
    if (tags.isEmpty()) {
      return false;
    }
    // A tag which isn't there anymore, e.g. after a data pack change, starts over from the first
    var next = tags.get((tags.indexOf(component.tag()) + 1) % tags.size());
    filter.set(RailcraftDataComponents.TAG_FILTER, new TagFilterComponent(component.prototype(), next));
    return true;
  }

  @Override
  public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
    var itemStack = player.getItemInHand(usedHand);
    if (!itemStack.has(RailcraftDataComponents.TAG_FILTER)) {
      return InteractionResult.PASS;
    }
    if (!level.isClientSide() && cycleTag(itemStack)) {
      var tag = itemStack.get(RailcraftDataComponents.TAG_FILTER).tag();
      player.sendOverlayMessage(tagLine(tag, ChatFormatting.WHITE));
    }
    return InteractionResult.SUCCESS;
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context,
      TooltipDisplay tooltipDisplay, Consumer<Component> tooltip, TooltipFlag flag) {
    var component = stack.get(RailcraftDataComponents.TAG_FILTER);
    if (component == null) {
      tooltip.accept(Component.translatable(Translations.Tips.TAG_FILTER_BLANK)
          .withStyle(ChatFormatting.GRAY));
      return;
    }
    tooltip.accept(Component.translatable(Translations.Tips.TAG_FILTER_PROTOTYPE)
        .withStyle(ChatFormatting.AQUA)
        .append(CommonComponents.SPACE)
        .append(Component.translatable(component.prototype().value().getDescriptionId())
            .withStyle(ChatFormatting.GRAY)));
    tooltip.accept(tagLine(component.tag(), ChatFormatting.GRAY));
    tooltip.accept(Component.translatable(Translations.Tips.TAG_FILTER_CHANGE_TAG)
        .withStyle(ChatFormatting.DARK_GRAY));
  }

  @Override
  public Component jeiDescription() {
    return Component.translatable(Translations.Jei.TAG_FILTER);
  }

  private static Component tagLine(TagKey<Item> tag, ChatFormatting color) {
    return Component.translatable(Translations.Tips.TAG_FILTER_TAG)
        .withStyle(ChatFormatting.AQUA)
        .append(CommonComponents.SPACE)
        .append(Component.literal("#" + tag.location()).withStyle(color));
  }
}
