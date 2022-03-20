package mods.railcraft.world.damagesource;

import mods.railcraft.util.MiscTools;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 *
 * @author CovertJaguar <https://www.railcraft.info/>
 */
public class RailcraftDamageSource extends DamageSource {

  public static final RailcraftDamageSource BORE = new RailcraftDamageSource("bore");
  public static final RailcraftDamageSource CRUSHER = new RailcraftDamageSource("crusher", 8);
  public static final RailcraftDamageSource ELECTRIC = new RailcraftDamageSource("electric");
  public static final RailcraftDamageSource STEAM = new RailcraftDamageSource("steam");
  public static final RailcraftDamageSource TRACK_ELECTRIC =
      new RailcraftDamageSource("track.electric");
  public static final RailcraftDamageSource TRAIN = new RailcraftDamageSource("train");
  public static final RailcraftDamageSource CREOSOTE = new RailcraftDamageSource("creosote");

  static {
    BORE.bypassArmor();
    ELECTRIC.bypassArmor();
    TRACK_ELECTRIC.bypassArmor();
    TRAIN.bypassArmor();
    CREOSOTE.bypassArmor();
  }

  private final int numMessages;

  private RailcraftDamageSource(String tag) {
    this(tag, 6);
  }

  private RailcraftDamageSource(String tag, int numMessages) {
    super(tag);
    this.numMessages = numMessages;
  }

  @Override
  public Component getLocalizedDeathMessage(LivingEntity entity) {
    String locTag =
        "death.railcraft." + this.msgId + "." + (MiscTools.RANDOM.nextInt(this.numMessages) + 1);
    return new TranslatableComponent(locTag, entity.getName());
  }

  public static final EventHandler EVENT_HANDLER = new EventHandler();

  public static class EventHandler {

    @SubscribeEvent
    public void modifyDrops(LivingDropsEvent event) {
      if (event.getSource() == STEAM)
        for (var entityItem : event.getDrops()) {
          var drop = entityItem.getItem();
          var level = event.getEntityLiving().getLevel();
          var cooked = level.getRecipeManager()
              .getRecipeFor(RecipeType.SMELTING, new SimpleContainer(drop), level)
              .map(SmeltingRecipe::getResultItem)
              .orElse(ItemStack.EMPTY);
          if (!cooked.isEmpty() && MiscTools.RANDOM.nextDouble() < 0.5) {
            cooked = cooked.copy();
            cooked.setCount(drop.getCount());
            entityItem.setItem(cooked);
          }
        }
    }
  }
}
