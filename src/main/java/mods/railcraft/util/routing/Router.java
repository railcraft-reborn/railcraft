package mods.railcraft.util.routing;


import java.util.Optional;
import com.mojang.datafixers.util.Either;

public interface Router {

  void resetLogic();

  Optional<Either<RoutingLogic, RoutingLogicException>> logicResult();

  default Optional<RoutingLogic> logic() {
    return this.logicResult().flatMap(Either::left);
  }

  default Optional<RoutingLogicException> logicError() {
    return this.logicResult().flatMap(Either::right);
  }
}

