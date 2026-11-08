package mods.railcraft.integrations.buildcraft;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import buildcraft.api.v2.BuildCraftRegistries;
import buildcraft.api.v2.client.StatementPresentation;
import buildcraft.api.v2.content.BuildCraftContent;
import buildcraft.api.v2.content.ContentRegistrar;
import buildcraft.api.v2.statement.ActionType;
import buildcraft.api.v2.statement.ParameterSchema;
import buildcraft.api.v2.statement.StatementCollector;
import buildcraft.api.v2.statement.StatementContext;
import buildcraft.api.v2.statement.StatementContexts;
import buildcraft.api.v2.statement.StatementContributor;
import buildcraft.api.v2.statement.StatementResult;
import buildcraft.api.v2.statement.StatementScope;
import buildcraft.api.v2.statement.TriggerType;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.world.level.block.entity.SteamEngineBlockEntity.EnergyStage;
import mods.railcraft.world.level.material.steam.SteamConstants;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * BuildCraft gate triggers and actions, ported from Railcraft 1.12.2. Uses the BuildCraft API v2.
 *
 * <p>Only load this class when BuildCraft is installed.
 */
public final class BuildCraftIntegration {

  public static final String MOD_ID = "buildcraftlib";

  private static final List<Trigger> TRIGGERS = List.of(
      trigger(GateStatement.TRIGGER_WORK, GateTargets::isWorker,
          (target, side) -> GateTargets.hasWork(target)),
      trigger(GateStatement.TRIGGER_CART, GateTargets::isCartHandler,
          (target, side) -> GateTargets.hasCart(target)),
      trigger(GateStatement.TRIGGER_FUEL, GateTargets::usesFuel,
          (target, side) -> GateTargets.needsFuel(target)),
      temperature(GateStatement.TRIGGER_TEMP_COLD, 0, SteamConstants.BOILING_POINT),
      temperature(GateStatement.TRIGGER_TEMP_WARM,
          SteamConstants.BOILING_POINT, SteamConstants.SUPER_HEATED),
      temperature(GateStatement.TRIGGER_TEMP_HOT, SteamConstants.SUPER_HEATED, Float.MAX_VALUE),
      engine(GateStatement.TRIGGER_ENGINE_BLUE, EnergyStage.BLUE),
      engine(GateStatement.TRIGGER_ENGINE_GREEN, EnergyStage.GREEN),
      engine(GateStatement.TRIGGER_ENGINE_YELLOW, EnergyStage.YELLOW),
      engine(GateStatement.TRIGGER_ENGINE_ORANGE, EnergyStage.ORANGE),
      engine(GateStatement.TRIGGER_ENGINE_RED, EnergyStage.RED, EnergyStage.OVERHEAT),
      trigger(GateStatement.TRIGGER_MAINTENANCE, GateTargets::needsMaintenanceSupported,
          (target, side) -> GateTargets.needsMaintenance(target)),
      aspect(GateStatement.TRIGGER_ASPECT_GREEN, SignalAspect.GREEN),
      aspect(GateStatement.TRIGGER_ASPECT_YELLOW_BLINK, SignalAspect.BLINK_YELLOW),
      aspect(GateStatement.TRIGGER_ASPECT_YELLOW, SignalAspect.YELLOW),
      aspect(GateStatement.TRIGGER_ASPECT_RED_BLINK, SignalAspect.BLINK_RED),
      aspect(GateStatement.TRIGGER_ASPECT_RED, SignalAspect.RED),
      aspect(GateStatement.TRIGGER_ASPECT_OFF, SignalAspect.OFF)
  );

  private static final List<Action> ACTIONS = List.of(
      new Action(GateStatement.ACTION_PAUSE, GateTargets::isWorker, GateTargets::pause),
      new Action(GateStatement.ACTION_SEND_CART, GateTargets::isCartHandler, GateTargets::sendCart)
  );

  private BuildCraftIntegration() {}

  /**
   * Registers the statements, their presentations and the contributor that offers them in gates.
   * Must run before BuildCraft freezes its registries (on load complete).
   */
  public static void init() {
    ContentRegistrar registrar = BuildCraftContent.addon(RailcraftConstants.ID);
    for (var trigger : TRIGGERS) {
      var id = trigger.statement.getId();
      registrar.trigger(new TriggerType(id, ParameterSchema.EMPTY,
          (context, parameters) -> target(context)
              .filter(target -> trigger.condition.test(target, context.side()))
              .isPresent()));
      registerPresentation(registrar, trigger.statement);
    }
    for (var action : ACTIONS) {
      var id = action.statement.getId();
      registrar.action(new ActionType(id, ParameterSchema.EMPTY, (context, parameters) -> {
        if (context.mode().isSimulation()) {
          return StatementResult.pass();
        }
        var target = target(context);
        if (target.isEmpty() || target.get().getLevel() == null
            || target.get().getLevel().isClientSide()) {
          return StatementResult.pass();
        }
        action.executor.accept(target.get());
        return StatementResult.success();
      }));
      registerPresentation(registrar, action.statement);
    }
    registrar.statementContributor("gates", new Contributor());
  }

  private static void registerPresentation(ContentRegistrar registrar, GateStatement statement) {
    var id = statement.getId();
    registrar.register(BuildCraftRegistries.STATEMENT_PRESENTATIONS, id,
        new StatementPresentation(id, statement.getTranslationKey(), null, statement.getIcon()));
  }

  private static Optional<BlockEntity> target(StatementContext context) {
    return context.views().get(StatementContexts.EXTERNAL_TARGET);
  }

  private static Trigger trigger(GateStatement statement, Predicate<BlockEntity> supports,
      BiPredicate<BlockEntity, Direction> condition) {
    return new Trigger(statement, supports, condition);
  }

  private static Trigger temperature(GateStatement statement, float min, float max) {
    return trigger(statement, GateTargets::hasTemperature,
        (target, side) -> GateTargets.temperature(target)
            .map(temperature -> temperature >= min && temperature < max)
            .orElse(false));
  }

  private static Trigger engine(GateStatement statement, EnergyStage first, EnergyStage... rest) {
    Set<EnergyStage> stages = EnumSet.of(first, rest);
    return trigger(statement, GateTargets::isEngine,
        (target, side) -> GateTargets.energyStage(target)
            .map(stages::contains)
            .orElse(false));
  }

  private static Trigger aspect(GateStatement statement, SignalAspect aspect) {
    return trigger(statement, GateTargets::hasSignalAspect,
        (target, side) -> GateTargets.signalAspect(target, side)
            .map(aspect::equals)
            .orElse(false));
  }

  /**
   * @param supports whether the trigger is offered for a block entity
   * @param condition whether the trigger is active, the direction is the side of the gate's pipe
   *        the block entity is attached to
   */
  private record Trigger(GateStatement statement, Predicate<BlockEntity> supports,
      BiPredicate<BlockEntity, Direction> condition) {}

  private record Action(GateStatement statement, Predicate<BlockEntity> supports,
      Consumer<BlockEntity> executor) {}

  /**
   * Offers the statements supported by the block entity a gate is attached to.
   */
  private static final class Contributor implements StatementContributor {

    @Override
    public void contribute(StatementContext context, StatementCollector collector) {
      target(context).ifPresent(target -> {
        for (var trigger : TRIGGERS) {
          if (trigger.supports.test(target)) {
            collector.addTrigger(trigger.statement.getId());
          }
        }
        for (var action : ACTIONS) {
          if (action.supports.test(target)) {
            collector.addAction(action.statement.getId());
          }
        }
      });
    }

    @Override
    public StatementScope scope(Identifier id) {
      return RailcraftConstants.ID.equals(id.getNamespace())
          ? StatementScope.EXTERNAL
          : StatementScope.INTERNAL;
    }
  }
}
