package mods.railcraft.data.loot.packs;

import java.util.function.BiConsumer;
import mods.railcraft.Railcraft;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class RailcraftChestLoot implements LootTableSubProvider {

  // If you change the name, remember to regenerate the chest inside the structure
  // /setblock x y z minecraft:chest[facing=south]{LootTable:"railcraft:chests/component_workshop_chest_loot"}
  private static final ResourceLocation COMPONENT_WORKSHOP_CHEST_LOOT =
      new ResourceLocation(Railcraft.ID, "chests/component_workshop_chest_loot");

  @Override
  public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
    consumer.accept(COMPONENT_WORKSHOP_CHEST_LOOT, LootTable.lootTable()
        .withPool(LootPool.lootPool()
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
            .setRolls(UniformGenerator.between(1, 2))
            .add(LootItem.lootTableItem(Items.COAL).setWeight(5)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 8))))
            // FIXME
            //.add(TagEntry.expandTag(RailcraftTags.Items.COPPER_PLATE).setWeight(2)
            //    .apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 8))))
            // FIXME
            //.add(TagEntry.expandTag(RailcraftTags.Items.COPPER_GEAR).setWeight(1)
            //    .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))
            // FIXME
            //.add(TagEntry.expandTag(RailcraftTags.Items.INGOT).setWeight(4)
            //    .apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 8))))
            .add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(1)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 8))))
            .add(LootItem.lootTableItem(RailcraftItems.CREOSOTE_BOTTLE.get()).setWeight(2)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(16, 32))))
        )
        .withPool(LootPool.lootPool()
            .setRolls(UniformGenerator.between(1, 2))
            .add(LootItem.lootTableItem(Items.RAIL).setWeight(1)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(16,32))))
        )
    );
  }
}
