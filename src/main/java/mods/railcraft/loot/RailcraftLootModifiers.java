package mods.railcraft.loot;

import com.mojang.serialization.Codec;
import mods.railcraft.api.core.RailcraftConstants;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftLootModifiers {

  private static final DeferredRegister<Codec<? extends IGlobalLootModifier>> deferredRegister =
      DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, RailcraftConstants.ID);

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }

  public static final RegistryObject<Codec<? extends IGlobalLootModifier>> DUNGEON_LOOT_MODIFIER =
      deferredRegister.register("dungeon_loot_modifier", DungeonLootModifier.CODEC);
}
