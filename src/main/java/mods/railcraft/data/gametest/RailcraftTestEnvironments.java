package mods.railcraft.data.gametest;

import java.util.List;
import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.ResourceKey;

public class RailcraftTestEnvironments {

  public static final ResourceKey<TestEnvironmentDefinition<?>> DEFAULT =
      ResourceKey.create(Registries.TEST_ENVIRONMENT, RailcraftConstants.id("default"));

  public static void bootstrap(BootstrapContext<TestEnvironmentDefinition<?>> bootstrap) {
    bootstrap.register(DEFAULT, new TestEnvironmentDefinition.AllOf(List.of()));
  }
}
