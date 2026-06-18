package mods.railcraft.data.gametest.instances.cart_dispenser;

import com.mojang.serialization.MapCodec;
import mods.railcraft.data.gametest.RailcraftGameTestInstances;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;

public class CartDispenserPickUpTestInstance extends GameTestInstance {

  public static final MapCodec<CartDispenserPickUpTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(CartDispenserPickUpTestInstance::new);

  public CartDispenserPickUpTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  @Override
  public void run(GameTestHelper helper) {
    helper.spawn(EntityType.MINECART, new BlockPos(1, 1, 0));
    helper.pressButton(0, 1, 1);
    helper.succeedWhenEntityNotPresent(EntityType.MINECART, 1, 1, 0);
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Cart Dispenser Pick Up");
  }
}
