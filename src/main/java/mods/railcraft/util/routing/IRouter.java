package mods.railcraft.util.routing;


import java.util.Optional;

public interface IRouter {

  void resetLogic();

  Optional<RoutingLogic> getLogic();

  default boolean isLogicValid() {
    return getLogic().map(RoutingLogic::isValid).orElse(false);
  }
}

