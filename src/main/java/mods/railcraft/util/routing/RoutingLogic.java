package mods.railcraft.util.routing;


import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.Translations;
import mods.railcraft.api.carts.IPaintedCart;
import mods.railcraft.api.carts.IRoutableCart;
import mods.railcraft.api.fuel.INeedsFuel;
import mods.railcraft.util.PowerUtil;
import mods.railcraft.util.routing.expression.ConstantExpression;
import mods.railcraft.util.routing.expression.Expression;
import mods.railcraft.util.routing.expression.IF;
import mods.railcraft.util.routing.expression.condition.AND;
import mods.railcraft.util.routing.expression.condition.ColorCondition;
import mods.railcraft.util.routing.expression.condition.Condition;
import mods.railcraft.util.routing.expression.condition.ConstantCondition;
import mods.railcraft.util.routing.expression.condition.DestCondition;
import mods.railcraft.util.routing.expression.condition.LocomotiveCondition;
import mods.railcraft.util.routing.expression.condition.NOT;
import mods.railcraft.util.routing.expression.condition.NameCondition;
import mods.railcraft.util.routing.expression.condition.OR;
import mods.railcraft.util.routing.expression.condition.OwnerCondition;
import mods.railcraft.util.routing.expression.condition.RedstoneCondition;
import mods.railcraft.util.routing.expression.condition.RefuelCondition;
import mods.railcraft.util.routing.expression.condition.RiderCondition;
import mods.railcraft.util.routing.expression.condition.TypeCondition;
import mods.railcraft.world.entity.vehicle.Train;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public class RoutingLogic {

  public static final String REGEX_SYMBOL = "\\?";

  private Deque<Expression> expressions;
  private RoutingLogicException error;

  private RoutingLogic(@Nullable Deque<String> data) {
    try {
      if (data != null) {
        this.parseTable(data);
      } else {
        throw new RoutingLogicException(Translations.RoutingTable.ERROR_BLANK);
      }
    } catch (RoutingLogicException ex) {
      this.error = ex;
    }
  }

  public static RoutingLogic buildLogic(@Nullable Deque<String> data) {
    return new RoutingLogic(data);
  }

  @Nullable
  public RoutingLogicException getError() {
    return this.error;
  }

  public boolean isValid() {
    return this.expressions != null;
  }

  private void parseTable(Deque<String> data) throws RoutingLogicException {
    Deque<Expression> stack = new ArrayDeque<>();
    var it = data.descendingIterator();
    while (it.hasNext()) {
      String line = it.next().trim();
      if (line.startsWith("//") || line.startsWith("#") || line.isEmpty()) {
        continue;
      }
      stack.push(parseLine(line, stack));
    }
    expressions = stack;
  }

  private AbstractMinecart getRoutableCart(AbstractMinecart cart) {
    // FIXME Train: Will this cause desync?
    // Note that this doesn't actually change the behavior as link information has never been available on the client.
    return Train.get(cart)
        .map(train -> {
          if (train.size() <= 1) {
            return cart;
          }
          if (train.isTrainEnd(cart)) {
            if (cart instanceof IRoutableCart) {
              return cart;
            }
            if (cart instanceof IPaintedCart) {
              return cart;
            }
            if (cart instanceof INeedsFuel) {
              return cart;
            }
          }
          if (train.getHeadLocomotive().isPresent()) {
            return train.getHeadLocomotive().get();
          }
          return cart;
        }).orElse(cart);
  }

  public boolean matches(IBlockEntityRouting blockEntityRouting, AbstractMinecart cart) {
    return evaluate(blockEntityRouting, cart) != PowerUtil.NO_POWER;
  }

  public int evaluate(IBlockEntityRouting blockEntityRouting, AbstractMinecart cart) {
    if (expressions == null) {
      return PowerUtil.NO_POWER;
    }
    var controllingCart = getRoutableCart(cart);
    return expressions.stream()
        .mapToInt(expression -> expression.evaluate(blockEntityRouting, controllingCart))
        .filter(value -> value != PowerUtil.NO_POWER)
        .findFirst()
        .orElse(PowerUtil.NO_POWER);
  }

  private Expression parseLine(String line, Deque<Expression> stack) throws RoutingLogicException {
    try {
      if (line.startsWith("Dest")) {
        return new DestCondition(line);
      }
      if (line.startsWith("Color")) {
        return new ColorCondition(line);
      }
      if (line.startsWith("Owner")) {
        return new OwnerCondition(line);
      }
      if (line.startsWith("Name")) {
        return new NameCondition(line);
      }
      if (line.startsWith("Type")) {
        return new TypeCondition(line);
      }
      if (line.startsWith("NeedsRefuel")) {
        return new RefuelCondition(line);
      }
      if (line.startsWith("Rider")) {
        return new RiderCondition(line);
      }
      if (line.startsWith("Redstone")) {
        return new RedstoneCondition(line);
      }
      if (line.startsWith("Loco")) {
        return new LocomotiveCondition(line);
      }
    } catch (RoutingLogicException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new RoutingLogicException(Translations.RoutingTable.ERROR_MALFORMED_SYNTAX, line);
    }
    if (line.equals("TRUE")) {
      return ConstantCondition.TRUE;
    }
    if (line.equals("FALSE")) {
      return ConstantCondition.FALSE;
    }
    try {
      return new ConstantExpression(Integer.parseInt(line));
    } catch (NumberFormatException ignored) {
      // not an integer; pass through
    } catch (IllegalArgumentException ex) {
      throw new RoutingLogicException(Translations.RoutingTable.ERROR_INVALID_CONSTANT, line);
    }
    try {
      if (line.equals("NOT")) {
        return new NOT((Condition) stack.pop());
      }
      if (line.equals("AND")) {
        return new AND((Condition) stack.pop(), (Condition) stack.pop());
      }
      if (line.equals("OR")) {
        return new OR((Condition) stack.pop(), (Condition) stack.pop());
      }
      if (line.equals("IF")) {
        return new IF((Condition) stack.pop(), stack.pop(), stack.pop());
      }
    } catch (NoSuchElementException ex) {
      throw new RoutingLogicException(Translations.RoutingTable.ERROR_INSUFFICIENT_OPERAND, line);
    } catch (ClassCastException ex) {
      throw new RoutingLogicException(Translations.RoutingTable.ERROR_INVALID_OPERAND, line);
    }
    throw new RoutingLogicException(Translations.RoutingTable.UNRECOGNIZED_KEYWORD, line);
  }
}
