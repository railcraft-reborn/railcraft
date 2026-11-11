package mods.railcraft.world.level.levelgen.feature.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

public record QuarriedConfiguration(
    RuleTest targetProvider, BlockStateProvider stateProvider) {

  public static final Codec<QuarriedConfiguration> CODEC =
      RecordCodecBuilder.create(instance -> instance.group(
          RuleTest.CODEC.fieldOf("target_provider")
              .forGetter(QuarriedConfiguration::targetProvider),
          BlockStateProvider.DIRECT_CODEC.fieldOf("state_provider")
              .forGetter(QuarriedConfiguration::stateProvider)
      ).apply(instance, QuarriedConfiguration::new));
}
