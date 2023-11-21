package mods.railcraft.world.entity.npc;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import org.jetbrains.annotations.Nullable;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.trading.MerchantOffer;

public class RailcraftVillagerTrades {

  public static void addTradeForTrackman(Int2ObjectMap<List<VillagerTrades.ItemListing>> trades) {
    BiFunction<ItemStack, RandomSource, ItemStack> enchanter = (stack, rand) -> {
      EnchantmentHelper.enchantItem(rand, stack, 15 + rand.nextInt(16), true);
      return stack;
    };

    trades.get(1)
        .add(new GenericTrade(new Offer(Items.EMERALD), new Offer(Items.COAL, 16, 24)));
    trades.get(1)
        .add(new GenericTrade(new Offer(Items.EMERALD),
            new Offer(RailcraftItems.COAL_COKE.get(), 16, 24)));
    trades.get(1)
        .add(new GenericTrade(new Offer(Items.RAIL, 30, 34), new Offer(Items.EMERALD, 2, 3)));

    trades.get(2)
        .add(new TrackKitTrade());
    trades.get(2)
        .add(new TrackKitTrade());

    trades.get(3)
        .add(new TrackKitTrade());
    trades.get(3)
        .add(new GenericTrade(new Offer(RailcraftItems.STEEL_CROWBAR.get()),
            new Offer(Items.EMERALD, 24, 52)).setEnchanter(enchanter).setUse(t -> 3));
    trades.get(3)
        .add(new GenericTrade(new Offer(RailcraftItems.WHISTLE_TUNER.get()),
            new Offer(Items.EMERALD, 1, 2)));
    trades.get(3)
        .add(new GenericTrade(new Offer(RailcraftItems.SIGNAL_BLOCK_SURVEYOR.get()),
            new Offer(Items.EMERALD, 6, 8)));
    trades.get(3)
        .add(new GenericTrade(new Offer(RailcraftItems.SIGNAL_TUNER.get()),
            new Offer(Items.EMERALD, 6, 8)));
    trades.get(3)
        .add(new GenericTrade(new Offer(RailcraftItems.GOGGLES.get()),
            new Offer(Items.EMERALD, 4, 8)));
    trades.get(3)
        .add(new GenericTrade(new Offer(RailcraftItems.OVERALLS.get()),
            new Offer(Items.EMERALD, 19, 32)).setEnchanter(enchanter).setUse(t -> 3));
  }

  public static void addTradeForCartman(Int2ObjectMap<List<VillagerTrades.ItemListing>> trades) {
    trades.get(1)
        .add(new GenericTrade(new Offer(Items.EMERALD), new Offer(Items.COAL, 16, 24)));
    trades.get(1)
        .add(new GenericTrade(new Offer(Items.EMERALD),
            new Offer(RailcraftItems.COAL_COKE.get(), 8, 12)));

    trades.get(2)
        .add(new CartTrade(false, 4, 7));
    trades.get(2)
        .add(new CartTrade(false, 4, 7));

    trades.get(3)
        .add(new CartTrade(false, 3, 5));
    trades.get(3)
        .add(new CartTrade(true, 30, 40));
    trades.get(3)
        .add(new CartTrade(true, 30, 40));
  }

  public static void addTradeForToolSmith(Int2ObjectMap<List<VillagerTrades.ItemListing>> trades) {
    trades.get(1)
        .add(new GenericTrade(new Offer(Items.EMERALD), new Offer(Items.COAL, 16, 24)));
    trades.get(1)
        .add(new GenericTrade(new Offer(Items.EMERALD),
            new Offer(RailcraftItems.COAL_COKE.get(), 8, 12)));
    trades.get(1)
        .add(new GenericTrade(new Offer(Items.EMERALD), new Offer(Items.IRON_INGOT, 7, 9)));

    trades.get(2)
        .add(new GenericTrade(new Offer(RailcraftItems.STEEL_INGOT.get()),
            new Offer(Items.EMERALD, 1, 2), new Offer(Items.IRON_INGOT)));
    trades.get(2)
        .add(new GenericTrade(new Offer(RailcraftItems.STEEL_INGOT.get()),
            new Offer(Items.EMERALD, 3, 4)));
    trades.get(2)
        .add(new GenericTrade(new Offer(RailcraftItems.SLAG.get(), 1, 2),
            new Offer(Items.EMERALD, 2, 4)));

    trades.get(3)
        .add(new GenericTrade(new Offer(RailcraftItems.STEEL_GEAR.get()),
            new Offer(Items.EMERALD, 9, 16)));
  }

  public static void addTradeForArmorer(Int2ObjectMap<List<VillagerTrades.ItemListing>> trades) {
    trades.get(1)
        .add(new GenericTrade(new Offer(Items.EMERALD), new Offer(Items.COAL, 16, 24)));
    trades.get(1)
        .add(new GenericTrade(new Offer(RailcraftItems.COAL_COKE.get(), 4, 6),
            new Offer(Items.EMERALD)));

    trades.get(2)
        .add(new GenericTrade(new Offer(Items.EMERALD),
            new Offer(Items.COPPER_INGOT, 7, 9)));
    trades.get(2)
        .add(new GenericTrade(new Offer(Items.EMERALD),
            new Offer(RailcraftItems.TIN_INGOT.get(), 7, 9)));
    trades.get(2)
        .add(new GenericTrade(new Offer(Items.EMERALD),
            new Offer(RailcraftItems.ZINC_INGOT.get(), 7, 9)));
    trades.get(2)
        .add(new GenericTrade(new Offer(Items.EMERALD),
            new Offer(RailcraftItems.NICKEL_INGOT.get(), 7, 9)));

    trades.get(3)
        .add(new GenericTrade(new Offer(RailcraftItems.BRASS_INGOT.get()),
            new Offer(Items.EMERALD, 2, 3)));
    trades.get(3)
        .add(new GenericTrade(new Offer(RailcraftItems.BRONZE_INGOT.get()),
            new Offer(Items.EMERALD, 2, 3)));
    trades.get(3)
        .add(new GenericTrade(new Offer(RailcraftItems.INVAR_INGOT.get()),
            new Offer(Items.EMERALD, 2, 3)));
    trades.get(3)
        .add(new GenericTrade(new Offer(RailcraftItems.BRONZE_GEAR.get()),
            new Offer(Items.EMERALD, 6, 12)));
    trades.get(3)
        .add(new GenericTrade(new Offer(RailcraftItems.BRASS_GEAR.get()),
            new Offer(Items.EMERALD, 6, 12)));
    trades.get(3)
        .add(new GenericTrade(new Offer(RailcraftItems.INVAR_GEAR.get()),
            new Offer(Items.EMERALD, 6, 12)));
  }

  private static class Offer {

    private final Item item;
    private final int min, max;

    private Offer(Item item, int min, int max) {
      this.item = item;
      this.min = min;
      this.max = max;
    }

    Offer(Item item, int amount) {
      this(item, amount, amount);
    }

    Offer(Item item) {
      this(item, 1);
    }
  }

  private static class GenericTrade implements VillagerTrades.ItemListing {

    private static final ToIntFunction<GenericTrade> USE_SETTER = t -> 7;
    private static final BiFunction<ItemStack, RandomSource, ItemStack> DEFAULT_ENCHANTER =
        (stack, rand) -> stack;
    private final Offer sale;
    private final Offer[] offers;
    private ToIntFunction<GenericTrade> maxUseSetter;
    private BiFunction<ItemStack, RandomSource, ItemStack> enchanter;

    public GenericTrade(Offer sale, Offer... offers) {
      this.sale = sale;
      this.offers = offers;
      this.maxUseSetter = USE_SETTER;
      this.enchanter = DEFAULT_ENCHANTER;
    }

    public MerchantOffer getOffer(Entity trader, RandomSource random) {
      var sellStack = prepareStack(random, sale);
      var buyStack1 = prepareStack(random, offers[0]);
      var buyStack2 = ItemStack.EMPTY;
      if (offers.length >= 2) {
        buyStack2 = prepareStack(random, offers[1]);
      }

      return new MerchantOffer(buyStack1, buyStack2, enchanter.apply(sellStack, random),
          12, 15, maxUseSetter.applyAsInt(this));
    }

    GenericTrade setUse(ToIntFunction<GenericTrade> f) {
      this.maxUseSetter = f;
      return this;
    }

    GenericTrade setEnchanter(BiFunction<ItemStack, RandomSource, ItemStack> enchanter) {
      this.enchanter = enchanter;
      return this;
    }

    private ItemStack prepareStack(RandomSource random, Offer offer) {
      return new ItemStack(offer.item, stackSize(random, offer));
    }

    private int stackSize(RandomSource random, Offer offer) {
      return random.nextIntBetweenInclusive(offer.min, offer.max);
    }
  }

  private static class TrackKitTrade implements VillagerTrades.ItemListing {

    private static final List<ItemStack> TRACK_KITS;

    static {
      TRACK_KITS = RailcraftItems.entries().stream()
          .map(Supplier::get)
          .map(ItemStack::new)
          .filter(kit -> kit.is(RailcraftTags.Items.TRACK_KIT))
          .collect(Collectors.toList());
    }

    @Nullable
    @Override
    public MerchantOffer getOffer(Entity trader, RandomSource random) {
      if (TRACK_KITS.isEmpty()) {
        return null;
      }
      var stack = TRACK_KITS.get(random.nextInt(TRACK_KITS.size()));
      if (stack.isEmpty()) {
        return null;
      }
      int size = random.nextIntBetweenInclusive(2, 6);
      return new MerchantOffer(new ItemStack(Items.EMERALD, size), stack, 12, 15, 7);
    }
  }

  private static class CartTrade implements VillagerTrades.ItemListing {

    private static final List<EntityType<? extends AbstractMinecart>> CHEAP = new ArrayList<>();
    private static final List<EntityType<? extends AbstractMinecart>> EXPENSIVE = new ArrayList<>();
    private final List<EntityType<? extends AbstractMinecart>> current;

    private final int priceLow, priceHigh;

    static {
      CHEAP.add(EntityType.MINECART);
      CHEAP.add(EntityType.CHEST_MINECART);
      CHEAP.add(EntityType.HOPPER_MINECART);
      CHEAP.add(EntityType.TNT_MINECART);
      // CHEAP.add(RailcraftCarts.CARGO);
      // CHEAP.add(RailcraftCarts.JUKEBOX);
      // CHEAP.add(RailcraftCarts.BED);
      CHEAP.add(RailcraftEntityTypes.TANK_MINECART.get());
      // CHEAP.add(RailcraftCarts.TNT_WOOD);
      // CHEAP.add(RailcraftCarts.WORK);
      EXPENSIVE.add(RailcraftEntityTypes.ELECTRIC_LOCOMOTIVE.get());
      EXPENSIVE.add(RailcraftEntityTypes.STEAM_LOCOMOTIVE.get());
      // EXPENSIVE.add(RailcraftCarts.WORLDSPIKE_PERSONAL);
      // EXPENSIVE.add(RailcraftCarts.WORLDSPIKE_STANDARD);
      // EXPENSIVE.add(RailcraftCarts.CHEST_METALS);
      // EXPENSIVE.add(RailcraftCarts.CHEST_VOID);
      EXPENSIVE.add(RailcraftEntityTypes.TRACK_LAYER.get());
      EXPENSIVE.add(RailcraftEntityTypes.TRACK_RELAYER.get());
      EXPENSIVE.add(RailcraftEntityTypes.TRACK_REMOVER.get());
      EXPENSIVE.add(RailcraftEntityTypes.TRACK_UNDERCUTTER.get());
      EXPENSIVE.add(RailcraftEntityTypes.TUNNEL_BORE.get());
    }

    CartTrade(boolean expensive, int priceLow, int priceHigh) {
      this.current = expensive ? EXPENSIVE : CHEAP;
      this.priceHigh = priceHigh;
      this.priceLow = priceLow;
    }

    @Nullable
    @Override
    public MerchantOffer getOffer(Entity trader, RandomSource random) {
      if (current.isEmpty()) {
        return null;
      }
      var stack = current.get(random.nextInt(current.size()))
          .create(trader.level())
          .getPickResult();

      int size = random.nextIntBetweenInclusive(priceLow, priceHigh);
      return new MerchantOffer(new ItemStack(Items.EMERALD, size), stack, 12, 15, 7);
    }
  }
}
