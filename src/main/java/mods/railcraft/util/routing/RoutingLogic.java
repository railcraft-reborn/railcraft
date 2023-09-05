package mods.railcraft.util.routing;


import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;
import mods.railcraft.Translations;
import mods.railcraft.api.carts.NeedsFuel;
import mods.railcraft.api.carts.Paintable;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.carts.Routable;
import mods.railcraft.util.routing.expression.Expression;
import mods.railcraft.util.routing.expression.condition.ColorCondition;
import mods.railcraft.util.routing.expression.condition.DestCondition;
import mods.railcraft.util.routing.expression.condition.LocomotiveCondition;
import mods.railcraft.util.routing.expression.condition.NameCondition;
import mods.railcraft.util.routing.expression.condition.OwnerCondition;
import mods.railcraft.util.routing.expression.condition.RedstoneCondition;
import mods.railcraft.util.routing.expression.condition.RefuelCondition;
import mods.railcraft.util.routing.expression.condition.RiderCondition;
import mods.railcraft.util.routing.expression.condition.TypeCondition;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public record RoutingLogic(Deque<Expression> expressions) {

  public static RoutingLogic parseTable(Deque<String> data) throws RoutingLogicException {
    Deque<Expression> stack = new ArrayDeque<>();
    var it = data.descendingIterator();
    while (it.hasNext()) {
      String line = it.next().trim();
      if (line.startsWith("//") || line.startsWith("#") || line.isEmpty()) {
        continue;
      }
      stack.push(parseLine(line, stack));
    }
    return new RoutingLogic(stack);
  }

  private static AbstractMinecart getRoutableCart(AbstractMinecart cart) {
    var rollingStock = RollingStock.getOrThrow(cart);
    var train = rollingStock.train();
    if (train.size() <= 1) {
      return cart;
    }
    if (rollingStock.isEnd()) {
      if (cart instanceof Routable) {
        return cart;
      }
      if (cart instanceof Paintable) {
        return cart;
      }
      if (cart instanceof NeedsFuel) {
        return cart;
      }
    }
    return train.front().entity();
  }

  public boolean matches(RouterBlockEntity blockEntityRouting, AbstractMinecart cart) {
    if (this.expressions == null) {
      return false;
    }
    var controllingCart = getRoutableCart(cart);
    return this.expressions.stream()
        .anyMatch(expression -> expression.evaluate(blockEntityRouting, controllingCart));
  }

  private static Expression parseLine(String line, Deque<Expression> stack)
      throws RoutingLogicException {
    try {
      if (line.startsWith(DestCondition.KEYWORD)) {
        return DestCondition.parse(line);
      }
      if (line.startsWith(ColorCondition.KEYWORD)) {
        return ColorCondition.parse(line);
      }
      if (line.startsWith(OwnerCondition.KEYWORD)) {
        return OwnerCondition.parse(line);
      }
      if (line.startsWith(NameCondition.KEYWORD)) {
        return NameCondition.parse(line);
      }
      if (line.startsWith(TypeCondition.KEYWORD)) {
        return TypeCondition.parse(line);
      }
      if (line.startsWith(RefuelCondition.KEYWORD)) {
        return RefuelCondition.parse(line);
      }
      if (line.startsWith(RiderCondition.KEYWORD)) {
        return RiderCondition.parse(line);
      }
      if (line.startsWith(RedstoneCondition.KEYWORD)) {
        return RedstoneCondition.parse(line);
      }
      if (line.startsWith(LocomotiveCondition.KEYWORD)) {
        return LocomotiveCondition.parse(line);
      }
    } catch (RoutingLogicException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new RoutingLogicException(Translations.RoutingTable.ERROR_MALFORMED_SYNTAX, line);
    }
    if (line.equals("TRUE")) {
      return Expression.TRUE;
    }
    if (line.equals("FALSE")) {
      return Expression.FALSE;
    }
    try {
      if (line.equals("NOT")) {
        return stack.pop().negate();
      }
      if (line.equals("AND")) {
        return stack.pop().and(stack.pop());
      }
      if (line.equals("OR")) {
        return stack.pop().or(stack.pop());
      }
      if (line.equals("IF")) {
        return stack.pop().select(stack.pop(), stack.pop());
      }
    } catch (NoSuchElementException ex) {
      throw new RoutingLogicException(Translations.RoutingTable.ERROR_INSUFFICIENT_OPERAND, line);
    } catch (ClassCastException ex) {
      throw new RoutingLogicException(Translations.RoutingTable.ERROR_INVALID_OPERAND, line);
    }
    throw new RoutingLogicException(Translations.RoutingTable.UNRECOGNIZED_KEYWORD, line);
  }
}
