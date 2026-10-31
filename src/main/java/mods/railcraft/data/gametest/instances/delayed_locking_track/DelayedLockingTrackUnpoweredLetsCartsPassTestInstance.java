package mods.railcraft.data.gametest.instances.delayed_locking_track;

import com.mojang.serialization.MapCodec;
import mods.railcraft.data.gametest.RailcraftGameTestInstances;

import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;

public class DelayedLockingTrackUnpoweredLetsCartsPassTestInstance extends GameTestInstance {

  public static final MapCodec<DelayedLockingTrackUnpoweredLetsCartsPassTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(DelayedLockingTrackUnpoweredLetsCartsPassTestInstance::new);

  public DelayedLockingTrackUnpoweredLetsCartsPassTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  @Override
  public void run(GameTestHelper helper) {
    DelayedLockingTrackTestHelper.build(helper, false);
    DelayedLockingTrackTestHelper.launchCart(helper);
    helper.succeedWhenEntityPresent(EntityType.MINECART, DelayedLockingTrackTestHelper.END_POS);
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Delayed Locking Track Unpowered Lets Carts Pass");
  }
}
