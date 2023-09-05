package mods.railcraft.util.routing.expression.condition;

import java.util.Locale;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Stream;
import mods.railcraft.Translations;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.util.routing.RoutingLogicException;
import mods.railcraft.util.routing.RoutingStatementParser;
import mods.railcraft.util.routing.expression.Expression;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraftforge.registries.ForgeRegistries;

public class RiderCondition {

  public static final String KEYWORD = "Rider";

  public static Expression parse(String line) throws RoutingLogicException {
    var statement = RoutingStatementParser.parse(KEYWORD, true, line);
    var tokens = statement.value().split(":");
    var type = Type.valueOf(tokens[0].toUpperCase(Locale.ROOT));

    if (statement.isRegex()) {
      checkRegexSyntax(type, tokens, line);
    }
    checkSyntax(type, tokens, line);

    var context = new Context(statement, tokens);
    return (router, minecart) -> type.matches(getPassengers(minecart), context);
  }

  private static Stream<Entity> getPassengers(AbstractMinecart minecart) {
    return RollingStock.getOrThrow(minecart)
        .train()
        .stream()
        .map(RollingStock::entity)
        .flatMap(c -> c.getPassengers().stream());
  }

  private static void checkRegexSyntax(Type type, String[] tokens, String line)
      throws RoutingLogicException {
    switch (type) {
      case ANY, NONE, MOB, ANIMAL, UNNAMED, ENTITY -> throw new RoutingLogicException(
          Translations.RoutingTable.ERROR_UNSUPPORTED_REGEX, line);
      case NAMED, PLAYER -> {
        if (tokens.length == 1) {
          throw new RoutingLogicException(Translations.RoutingTable.ERROR_UNSUPPORTED_REGEX,
              line);
        }
      }
    }
  }

  private static void checkSyntax(Type type, String[] tokens, String line)
      throws RoutingLogicException {
    switch (type) {
      case ANY, NONE, MOB, ANIMAL, UNNAMED -> {
        if (tokens.length > 1) {
          throw new RoutingLogicException(Translations.RoutingTable.ERROR_MALFORMED_SYNTAX, line);
        }
      }
      case ENTITY -> {
        if (tokens.length == 1) {
          throw new RoutingLogicException(Translations.RoutingTable.ERROR_MALFORMED_SYNTAX, line);
        }
      }
      case NAMED, PLAYER -> {
        if (tokens.length > 2) {
          throw new RoutingLogicException(Translations.RoutingTable.ERROR_MALFORMED_SYNTAX, line);
        }
      }
      default -> throw new RoutingLogicException(Translations.RoutingTable.UNRECOGNIZED_KEYWORD,
          line);
    }
  }


  private enum Type {

    ANY(passengers -> passengers.count() > 0),
    NONE(passengers -> passengers.count() == 0),
    MOB(passengers -> passengers.anyMatch(Monster.class::isInstance)),
    ANIMAL(passengers -> passengers.anyMatch(Animal.class::isInstance)),
    UNNAMED(passengers -> passengers.anyMatch(p -> !p.hasCustomName())),
    ENTITY((passengers, context) -> passengers.anyMatch(e -> {
      var registryName = ForgeRegistries.ENTITY_TYPES.getKey(e.getType()).toString();
      return context.tokens[1].equalsIgnoreCase(registryName);
    })),
    PLAYER((passengers, context) -> {
      if (context.tokens.length < 2) {
        return passengers.anyMatch(Player.class::isInstance);
      }
      BiPredicate<String, String> predicate = context.statement.isRegex()
          ? String::matches
          : String::equalsIgnoreCase;
      return passengers.anyMatch(e -> e instanceof Player &&
          predicate.test(e.getName().getString(), context.tokens[1]));
    }),
    NAMED((passengers, context) -> {
      if (context.tokens.length < 2) {
        return passengers.anyMatch(Entity::hasCustomName);
      }
      BiPredicate<String, String> predicate = context.statement.isRegex()
          ? String::matches
          : String::equalsIgnoreCase;
      return passengers.anyMatch(e -> e.hasCustomName() &&
          predicate.test(e.getCustomName().getString(), context.tokens[1]));
    });

    private final BiPredicate<Stream<Entity>, Context> predicate;

    Type(Predicate<Stream<Entity>> predicate) {
      this((passengers, __) -> predicate.test(passengers));
    }

    Type(BiPredicate<Stream<Entity>, Context> predicate) {
      this.predicate = predicate;
    }

    private boolean matches(Stream<Entity> passengers, Context context) {
      return this.predicate.test(passengers, context);
    }
  }

  private record Context(RoutingStatementParser.ParsedStatement statement, String[] tokens) {}
}
