# PR: Fix fluid/pipe interop bugs and capability registration

## Summary

Fixes 9 bugs in fluid handling, inventory management, capability registration,
and multiblock interop. All fixes target correctness against the NeoForge
1.21.x capability contract. No behavioral changes beyond bug fixes.

## Bugs Fixed

### Critical
- **BUG-0001**: `TankModule` `extractItem` slot guard was inverted, blocking
  output slot extraction while exposing the processing slot to automation.
- **BUG-0002**: `CompositeFluidHandler` passed raw tank indices to sub-handlers
  instead of adjusting for the handler's offset, causing wrong-tank operations
  in linked train consists.
- **BUG-0009**: `CokeOvenBlockEntity.membershipChanged()` did not call
  `level.invalidateCapabilities()`, leaving stale null capability caches for
  external fluid transport mods after multiblock formation.

### High
- **BUG-0003**: `ContainerMapper.removeItemNoUpdate` did not apply the start
  offset, removing items from the wrong absolute slot.
- **BUG-0004**: `FluidTools.containsFluid` required all tanks to match instead
  of any, causing false negatives for multi-tank handlers.
- **BUG-0005**: `FluidGaugeWidget` skipped sync when a tank transitioned from
  empty to filled (previous fluid was EMPTY, so the fluid-type-changed check
  never triggered).
- **BUG-0006**: `CokeOvenModule` exposed the liquid processing slot (index 3)
  to automation extraction, and the `fluidContainer` mapper had an incorrect
  size.
- **BUG-0007**: `TankBlockEntity.use()` accessed the raw `StandardTank`
  directly instead of routing through `ValveFluidHandler`, bypassing
  height-based fill/drain restrictions for player bucket interactions.
- **BUG-0008**: `TunnelBoreEntity` was missing `ItemHandler.ENTITY` and
  `ENTITY_AUTOMATION` capability registrations, making its inventory
  inaccessible to external item transport.

## Build System

- Git commands in `build.gradle` now use `providers.exec` with
  `ignoreExitValue = true` for Gradle configuration-cache compatibility.
- Gradle wrapper set to 8.12.1.
- `makeChangelog` task gated behind `-PskipChangelog` property.

## Files Changed

### Java (bug fixes)
- `src/main/java/mods/railcraft/Railcraft.java`
- `src/main/java/mods/railcraft/world/module/CokeOvenModule.java`
- `src/main/java/mods/railcraft/world/module/TankModule.java` (BUG-0001)
- `src/main/java/mods/railcraft/util/fluids/CompositeFluidHandler.java` (BUG-0002)
- `src/main/java/mods/railcraft/util/container/ContainerMapper.java` (BUG-0003)
- `src/main/java/mods/railcraft/util/fluids/FluidTools.java` (BUG-0004)
- `src/main/java/mods/railcraft/client/gui/widget/FluidGaugeWidget.java` (BUG-0005)
- `src/main/java/mods/railcraft/world/level/block/entity/tank/TankBlockEntity.java` (BUG-0007)
- `src/main/java/mods/railcraft/world/level/block/entity/CokeOvenBlockEntity.java` (BUG-0009)

### Build
- `build.gradle`
- `gradle/wrapper/gradle-wrapper.properties`

### Documentation
- `docs/bugs/BUG-0001` through `BUG-0009`
- `docs/interop/mekanism.md`
- `docs/fluids/interop-mekanism.md`
- `README.md` (build quickstart)
- `CONTRIBUTING.md` (new)

## Verification

1. `./gradlew clean build -PskipChangelog=true` passes.
2. `./gradlew spotlessCheck` passes.
3. JAR produced at `build/libs/railcraft-reborn-1.21.1-1.2.11-snapshot.jar`.
4. Each bug has a dedicated doc under `docs/bugs/` with symptoms, root cause,
   fix, and manual verification steps.

## Coke Oven / Mekanism Note

The BUG-0009 fix (`invalidateCapabilities`) addresses a real stale-cache issue
but has **not** been verified against a live Mekanism installation. The
workaround (Railcraft Fluid Loader) is documented in
`docs/interop/mekanism.md`.
