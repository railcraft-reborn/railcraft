package mods.railcraft.data.gametest.instances.review;

import java.lang.reflect.InvocationTargetException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/**
 * A single test instance type for all review regression tests. The test to run is a static method
 * taking a {@link GameTestHelper}, named <code>Class#method</code> with the class in this package,
 * so every test lives in its own file without a shared registry.
 */
public class ReviewTestInstance extends GameTestInstance {

  public static final MapCodec<ReviewTestInstance> CODEC =
      RecordCodecBuilder.mapCodec(instance -> instance.group(
          TestData.CODEC.forGetter(t -> t.info),
          Codec.STRING.fieldOf("test").forGetter(t -> t.test)
      ).apply(instance, ReviewTestInstance::new));

  private final String test;

  public ReviewTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info, String test) {
    super(info);
    this.test = test;
  }

  @Override
  public void run(GameTestHelper helper) {
    var separator = this.test.indexOf('#');
    if (separator < 0) {
      helper.fail(Component.literal("Malformed review test " + this.test));
      return;
    }
    try {
      var clazz = Class.forName(
          ReviewTestInstance.class.getPackageName() + "." + this.test.substring(0, separator));
      var method =
          clazz.getDeclaredMethod(this.test.substring(separator + 1), GameTestHelper.class);
      method.setAccessible(true);
      method.invoke(null, helper);
    } catch (InvocationTargetException e) {
      if (e.getCause() instanceof RuntimeException runtimeException) {
        throw runtimeException;
      }
      if (e.getCause() instanceof Error error) {
        throw error;
      }
      throw new IllegalStateException(e.getCause());
    } catch (ReflectiveOperationException e) {
      helper.fail(Component.literal("Unknown review test " + this.test));
    }
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Review test " + this.test);
  }
}
