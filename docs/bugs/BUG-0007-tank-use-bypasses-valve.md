# BUG-0007: TankBlockEntity.use() bypasses ValveFluidHandler height restrictions

## Symptom
Players can fill an iron/steel tank through a bottom valve or drain it through a top
valve using a bucket, even though pipe/automation access correctly enforces height-based
restrictions.

## Root Cause
`TankBlockEntity.use()` called `FluidUtil.interactWithFluidHandler(player, hand, this.module.getTank())`
directly on the raw `StandardTank`, bypassing the `ValveFluidHandler` that enforces:
- Fill only through valves above the master block
- Drain only through valves at or below the master + 1

The `ValveFluidHandler` is correctly used for capability-based access (pipes, loaders), but
the direct player interaction path skipped it entirely.

## Patch Summary
Changed `use()` to route through `this.fluidHandler` (the `ValveFluidHandler`) when
available, falling back to the raw tank only when no valve handler exists.

## File Changed
`src/main/java/mods/railcraft/world/level/block/entity/tank/TankBlockEntity.java`

## Verification Steps
1. Build a multi-layer iron tank with valves at top and bottom.
2. Try to fill the tank using a water bucket on a bottom valve.
3. Confirm the fill is rejected (bottom valves are drain-only).
4. Fill through a top valve and confirm it works.
5. Try to drain through a top valve and confirm it is rejected.
6. Drain through a bottom valve and confirm it works.
