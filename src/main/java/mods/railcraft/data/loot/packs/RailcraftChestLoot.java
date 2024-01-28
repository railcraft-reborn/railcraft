package mods.railcraft.data.loot.packs;

import java.util.function.BiConsumer;
import mods.railcraft.Railcraft;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.TagEntry;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class RailcraftChestLoot implements LootTableSubProvider {

  // If you change the name, remember to regenerate the chest inside the structure
  // /setblock x y z minecraft:chest[facing=south]{LootTable:"railcraft:chests/component_workshop"}
  private static final ResourceLocation COMPONENT_WORKSHOP =
      Railcraft.rl("chests/component_workshop");
  public static final ResourceLocation ABANDONED_MINESHAFT =
      Railcraft.rl("chests/abandoned_mineshaft");
  public static final ResourceLocation SIMPLE_DUNGEON = Railcraft.rl("chests/simple_dungeon");

  @Override
  public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
    consumer.accept(COMPONENT_WORKSHOP, LootTable.lootTable()
        .withPool(LootPool.lootPool()
            .name("railcraft_tools")
            .setRolls(ConstantValue.exactly(1))
            .add(LootItem.lootTableItem(RailcraftItems.IRON_CROWBAR.get()).setWeight(2))
            .add(LootItem.lootTableItem(RailcraftItems.SEASONS_CROWBAR.get()).setWeight(1))
            .add(LootItem.lootTableItem(RailcraftItems.IRON_SPIKE_MAUL.get()).setWeight(1))
            .add(LootItem.lootTableItem(RailcraftItems.CHARGE_METER.get()).setWeight(1))
            //.add(LootItem.lootTableItem(RailcraftItems.MAGNIFY_GLASS.get()).setWeight(1))
            .add(LootItem.lootTableItem(RailcraftItems.SIGNAL_BLOCK_SURVEYOR.get()).setWeight(1))
            .add(LootItem.lootTableItem(RailcraftItems.SIGNAL_TUNER.get()).setWeight(1))
            .add(LootItem.lootTableItem(RailcraftItems.GOGGLES.get()).setWeight(1))
            .add(LootItem.lootTableItem(RailcraftItems.OVERALLS.get()).setWeight(2))
        )
        .withPool(LootPool.lootPool()
            .name("railcraft_carts")
            .setRolls(UniformGenerator.between(1, 2))
            .add(LootItem.lootTableItem(Items.RAIL).setWeight(5))
            .add(LootItem.lootTableItem(Items.CHEST_MINECART).setWeight(3))
            .add(LootItem.lootTableItem(Items.HOPPER_MINECART).setWeight(1))
            .add(LootItem.lootTableItem(Items.TNT_MINECART).setWeight(1))
            //.add(LootItem.lootTableItem(RailcraftItems.CARGO_MINECART.get()).setWeight(3))
            .add(LootItem.lootTableItem(Items.FURNACE_MINECART).setWeight(1))
            .add(LootItem.lootTableItem(RailcraftItems.TANK_MINECART.get()).setWeight(1))
            //.add(LootItem.lootTableItem(RailcraftItems.WORK_MINECART.get()).setWeight(1))
            //.add(LootItem.lootTableItem(RailcraftItems.TNT_WOOD_MINECART.get()).setWeight(1))
            //.add(LootItem.lootTableItem(RailcraftItems.BED_MINECART.get()).setWeight(1))
            //.add(LootItem.lootTableItem(RailcraftItems.JUKEBOX_MINECART.get()).setWeight(1))
        )
        .withPool(LootPool.lootPool()
            .name("railcraft_resources")
            .setRolls(UniformGenerator.between(1, 2))
            .add(LootItem.lootTableItem(Items.COAL).setWeight(5)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 8))))
            .add(TagEntry.expandTag(RailcraftTags.Items.PLATE_CHEST_LOOT).setWeight(2)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 8))))
            .add(TagEntry.expandTag(RailcraftTags.Items.GEAR_CHEST_LOOT).setWeight(1)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))
            .add(TagEntry.expandTag(RailcraftTags.Items.INGOT_CHEST_LOOT).setWeight(4)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 8))))
            .add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(1)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 8))))
            .add(LootItem.lootTableItem(RailcraftItems.CREOSOTE_BOTTLE.get()).setWeight(2)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(16, 32))))
        )
        .withPool(LootPool.lootPool()
            .name("railcraft_tracks")
            .setRolls(UniformGenerator.between(1, 2))
            .add(LootItem.lootTableItem(Items.RAIL).setWeight(1)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(16, 32))))
        )
    );

    consumer.accept(ABANDONED_MINESHAFT, LootTable.lootTable()
        .withPool(LootPool.lootPool()
            .name("railcraft_tools")
            .setRolls(ConstantValue.exactly(1))
            .add(LootItem.lootTableItem(RailcraftItems.IRON_CROWBAR.get()).setWeight(5))
            .add(LootItem.lootTableItem(RailcraftItems.IRON_SPIKE_MAUL.get()).setWeight(5))
            .add(LootItem.lootTableItem(RailcraftItems.OVERALLS.get()).setWeight(5))
        )
        .withPool(LootPool.lootPool()
            .name("railcraft_carts")
            .setRolls(UniformGenerator.between(1, 2))
            .add(LootItem.lootTableItem(Items.MINECART).setWeight(5))
            .add(LootItem.lootTableItem(Items.CHEST_MINECART).setWeight(5))
            .add(LootItem.lootTableItem(Items.HOPPER_MINECART).setWeight(1))
            .add(LootItem.lootTableItem(Items.TNT_MINECART).setWeight(1))
            .add(LootItem.lootTableItem(Items.HOPPER_MINECART).setWeight(1))
            //.add(LootItem.lootTableItem(RailcraftItems.CARGO_MINECART.get()).setWeight(5))
            .add(LootItem.lootTableItem(RailcraftItems.TANK_MINECART.get()).setWeight(1))
            //.add(LootItem.lootTableItem(RailcraftItems.WORK_MINECART.get()).setWeight(1))
            .add(LootItem.lootTableItem(RailcraftItems.STEAM_LOCOMOTIVE.get()).setWeight(1))
        )
        .withPool(LootPool.lootPool()
            .name("railcraft_resources")
            .setRolls(UniformGenerator.between(0, 2))
            .add(LootItem.lootTableItem(Items.COAL).setWeight(5)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 8))))
            .add(TagEntry.expandTag(RailcraftTags.Items.PLATE_CHEST_LOOT).setWeight(2)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 8))))
            .add(TagEntry.expandTag(RailcraftTags.Items.GEAR_CHEST_LOOT).setWeight(1)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))
            .add(TagEntry.expandTag(RailcraftTags.Items.INGOT_CHEST_LOOT).setWeight(4)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 8))))
            .add(LootItem.lootTableItem(RailcraftItems.CREOSOTE_BOTTLE.get()).setWeight(2)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(16, 32))))
        )
    );
    consumer.accept(SIMPLE_DUNGEON, LootTable.lootTable()
        .withPool(LootPool.lootPool()
            .name("railcraft_general")
            .setRolls(UniformGenerator.between(0, 1))
            .add(LootItem.lootTableItem(RailcraftItems.STEEL_SWORD.get()).setWeight(1))
            .add(LootItem.lootTableItem(RailcraftItems.STEEL_AXE.get()).setWeight(1))
            .add(LootItem.lootTableItem(RailcraftItems.STEEL_PICKAXE.get()).setWeight(1))
            .add(LootItem.lootTableItem(RailcraftItems.STEEL_BOOTS.get()).setWeight(1))
            .add(LootItem.lootTableItem(RailcraftItems.STEEL_HELMET.get()).setWeight(1))
            .add(LootItem.lootTableItem(RailcraftItems.STEEL_LEGGINGS.get()).setWeight(1))
            .add(LootItem.lootTableItem(RailcraftItems.STEEL_CHESTPLATE.get()).setWeight(1))
            .add(TagEntry.expandTag(RailcraftTags.Items.INGOT_CHEST_LOOT).setWeight(2)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 8))))
        )
    );
  }
}
