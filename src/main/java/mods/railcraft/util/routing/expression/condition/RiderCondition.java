package mods.railcraft.util.routing.expression.condition;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import mods.railcraft.Translations;
import mods.railcraft.util.routing.IBlockEntityRouting;
import mods.railcraft.util.routing.RoutingLogicException;
import mods.railcraft.world.entity.vehicle.Train;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraftforge.registries.ForgeRegistries;

public class RiderCondition extends ParsedCondition {

  private final String[] tokens;

  public RiderCondition(String line) throws RoutingLogicException {
    super("Rider", true, line);
    tokens = value.split(":");
    if (isRegex) {
      switch (tokens[0].toLowerCase(Locale.ROOT)) {
        case "any", "none", "mob", "animal", "unnamed", "entity" ->
            throw new RoutingLogicException(Translations.RoutingTable.ERROR_UNSUPPORTED_REGEX, line);
        case "named", "player" -> {
          if (tokens.length == 1) {
            throw new RoutingLogicException(Translations.RoutingTable.ERROR_UNSUPPORTED_REGEX, line);
          }
        }
      }
    }
    switch (tokens[0].toLowerCase(Locale.ROOT)) {
      case "any", "none", "mob", "animal", "unnamed" -> {
        if (tokens.length > 1) {
          throw new RoutingLogicException(Translations.RoutingTable.ERROR_MALFORMED_SYNTAX, line);
        }
      }
      case "entity" -> {
        if (tokens.length == 1) {
          throw new RoutingLogicException(Translations.RoutingTable.ERROR_MALFORMED_SYNTAX, line);
        }
      }
      case "named", "player" -> {
        if (tokens.length > 2) {
          throw new RoutingLogicException(Translations.RoutingTable.ERROR_MALFORMED_SYNTAX, line);
        }
      }
      default ->
          throw new RoutingLogicException(Translations.RoutingTable.UNRECOGNIZED_KEYWORD, line);
    }
  }

  @Override
  public boolean matches(IBlockEntityRouting blockEntityRouting, AbstractMinecart cart) {
    switch (tokens[0].toLowerCase(Locale.ROOT)) {
      case "any" -> {
        return !getPassengers(cart).isEmpty();
      }
      case "none" -> {
        return getPassengers(cart).isEmpty();
      }
      case "mob" -> {
        return getPassengers(cart).stream().anyMatch(e -> e instanceof Monster);
      }
      case "animal" -> {
        return getPassengers(cart).stream().anyMatch(e -> e instanceof Animal);
      }
      case "unnamed" -> {
        return getPassengers(cart).stream().anyMatch(e -> !e.hasCustomName());
      }
      case "entity" -> {
        return getPassengers(cart).stream()
            .anyMatch(e -> {
              var registryName = ForgeRegistries.ENTITY_TYPES.getKey(e.getType()).toString();
              return tokens[1].equalsIgnoreCase(registryName);
            });
      }
      case "player" -> {
        if (tokens.length == 2) {
          if (isRegex) {
            return getPassengers(cart).stream()
                .anyMatch(e -> e instanceof Player &&
                    e.getName().getString().matches(tokens[1]));
          } else {
            return getPassengers(cart).stream()
                .anyMatch(e -> e instanceof Player &&
                    e.getName().getString().equalsIgnoreCase(tokens[1]));
          }
        }
        return getPassengers(cart).stream().anyMatch(e -> e instanceof Player);
      }
      case "named" -> {
        if (tokens.length == 2) {
          if (isRegex) {
            return getPassengers(cart).stream()
                .anyMatch(e -> e.hasCustomName() &&
                    e.getCustomName().getString().matches(tokens[1]));
          } else {
            return getPassengers(cart).stream().anyMatch(
                e -> e.hasCustomName() &&
                    e.getCustomName().getString().equalsIgnoreCase(tokens[1]));
          }
        }
        return getPassengers(cart).stream().anyMatch(Entity::hasCustomName);
      }
    }
    return false;
  }

  private List<Entity> getPassengers(AbstractMinecart cart) {
    return Train.streamCarts(cart)
        .flatMap(c -> c.getPassengers().stream())
        .collect(Collectors.toList());
  }
}
