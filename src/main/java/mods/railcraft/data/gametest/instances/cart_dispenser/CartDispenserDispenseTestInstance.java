package mods.railcraft.data.gametest.instances.cart_dispenser;

import com.mojang.serialization.MapCodec;
import mods.railcraft.data.gametest.RailcraftGameTestInstances;
import mods.railcraft.world.level.block.entity.manipulator.CartDispenserBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CartDispenserDispenseTestInstance extends GameTestInstance {

  public static final MapCodec<CartDispenserDispenseTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(CartDispenserDispenseTestInstance::new);

  public CartDispenserDispenseTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  @Override
  public void run(GameTestHelper helper) {
    var be = helper.getBlockEntity(new BlockPos(0, 1, 0), CartDispenserBlockEntity.class);
    be.insert(new ItemStack(Items.MINECART));
    helper.pressButton(0, 1, 1);
    helper.succeedWhenEntityPresent(EntityType.MINECART, 1, 1, 0);
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Cart Dispenser Dispense");
  }
}
