package mods.railcraft.integrations.forestry;

/*
import java.util.List;
import forestry.api.IForestryApi;
import forestry.api.core.backpacks.EnumBackpackType;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.tags.RailcraftTags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

// Railcraft backpacks, made with the Forestry backpack API. Ported from Railcraft 1.12.2.
//
// Only load this class when Forestry is installed.
public final class RailcraftBackpacks {

  private static final DeferredRegister.Items deferredRegister =
      DeferredRegister.createItems(RailcraftConstants.ID);

  public static final RailcraftBackpackDefinition TRACKMAN = new RailcraftBackpackDefinition(
      0x0094FF, 0xFFFFFF,
      RailcraftTags.Items.TRACKMAN_BACKPACK_ALLOW, RailcraftTags.Items.TRACKMAN_BACKPACK_REJECT);
  public static final RailcraftBackpackDefinition ICEMAN = new RailcraftBackpackDefinition(
      0xFFFFFF, 0xFFFFFF,
      RailcraftTags.Items.ICEMAN_BACKPACK_ALLOW, RailcraftTags.Items.ICEMAN_BACKPACK_REJECT);

  public static final DeferredItem<Item> TRACKMAN_BACKPACK =
      backpack("trackman_backpack", TRACKMAN, EnumBackpackType.NORMAL);
  public static final DeferredItem<Item> WOVEN_TRACKMAN_BACKPACK =
      backpack("woven_trackman_backpack", TRACKMAN, EnumBackpackType.WOVEN);
  public static final DeferredItem<Item> ICEMAN_BACKPACK =
      backpack("iceman_backpack", ICEMAN, EnumBackpackType.NORMAL);
  public static final DeferredItem<Item> WOVEN_ICEMAN_BACKPACK =
      backpack("woven_iceman_backpack", ICEMAN, EnumBackpackType.WOVEN);

  private RailcraftBackpacks() {}

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
    NeoForge.EVENT_BUS.register(IcemanBackpackHandler.class);
  }

  public static List<DeferredItem<Item>> normalBackpacks() {
    return List.of(TRACKMAN_BACKPACK, ICEMAN_BACKPACK);
  }

  public static List<DeferredItem<Item>> wovenBackpacks() {
    return List.of(WOVEN_TRACKMAN_BACKPACK, WOVEN_ICEMAN_BACKPACK);
  }

  public static void addToCreativeTab(CreativeModeTab.Output output) {
    output.accept(TRACKMAN_BACKPACK.get());
    output.accept(WOVEN_TRACKMAN_BACKPACK.get());
    output.accept(ICEMAN_BACKPACK.get());
    output.accept(WOVEN_ICEMAN_BACKPACK.get());
  }

  private static DeferredItem<Item> backpack(String name, RailcraftBackpackDefinition definition,
      EnumBackpackType type) {
    return deferredRegister.registerItem(name, properties ->
        IForestryApi.INSTANCE.getBackpackInterface().createBackpack(definition, type, properties));
  }
}
*/
