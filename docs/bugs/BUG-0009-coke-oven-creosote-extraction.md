# BUG-0009: Coke Oven creosote extraction fails for external fluid transport mods

## Severity: Critical

## Symptoms
- Mekanism mechanical pipes connect to a formed Coke Oven but cannot extract creosote.
- The Coke Oven GUI shows creosote in the internal tank (multiblock is formed and
  producing correctly).
- Mekanism pipes work correctly with Railcraft Iron/Steel Tank valves, confirming
  pipes and the Mekanism transfer pattern (simulate then execute) are functional.
- The failure is specific to the Coke Oven capability exposure path.

## Root cause
`CokeOvenBlockEntity.membershipChanged()` did not call
`level.invalidateCapabilities(this.getBlockPos())` when the multiblock formed or
disbanded. This left NeoForge's `BlockCapabilityCache` (used by Mekanism and other
mods) holding a stale `null` capability reference.

The underlying mechanism:
1. Before the multiblock forms, a pipe queries `FluidHandler.BLOCK` on a Coke Oven
   brick. `getFluidCap()` calls `getMasterBlockEntity()` which returns
   `Optional.empty()` (no membership yet), so the capability returns `null`.
2. Mekanism's `BlockCapabilityCache` stores this `null` result.
3. When the multiblock forms, `membershipChanged()` calls `setBlockAndUpdate()` to
   set the WINDOW property. For 'B' (brick) blocks, WINDOW was already `false`, so
   the block state does not actually change. Minecraft's `Level.setBlockAndUpdate()`
   short-circuits when old state == new state, skipping the implicit capability
   cache invalidation that accompanies block state changes.
4. For 'W' (window) blocks, the WINDOW property changes to `true`, so
   `setBlockAndUpdate` DOES trigger implicit invalidation. But 'W' blocks are only
   on the middle layer (4 out of 26 blocks), so most blocks remain stale.
5. The pipe's cache is never invalidated. Subsequent drain attempts resolve to the
   stale `null` handler and return 0.

Contrast with `TankBlockEntity.membershipChanged()` which explicitly calls
`level.invalidateCapabilities(this.getBlockPos())` (on disband only, but tank
valves also change block state on formation, implicitly invalidating).

## Fix
Added `this.level.invalidateCapabilities(this.getBlockPos())` at the end of
`CokeOvenBlockEntity.membershipChanged()`, called unconditionally for both
formation and disbanding. This ensures that any `BlockCapabilityCache` held by
external mods (Mekanism, Pipez, etc.) is invalidated and re-queries the capability.

On re-query after formation:
- `getMasterBlockEntity()` returns the master (membership is already set by
  `setMembership()` before `membershipChanged()` is called).
- `CokeOvenModule.getTank()` returns the `StandardTank`.
- The tank has `disableFill = true`, so `fill()` returns 0 (no external filling).
- `drain()` delegates to `FluidTank.drain()` which works normally for both
  `SIMULATE` and `EXECUTE` actions.

### Files changed
- `src/main/java/mods/railcraft/world/level/block/entity/CokeOvenBlockEntity.java`

### Drain-only contract
The `StandardTank` is created with `.disableFill()` in `CokeOvenModule`:
- `fill(FluidStack, FluidAction)` returns 0.
- `internalFill()` bypasses the flag (used only by `craftAndPushImp()` to add
  creosote from recipes).
- `drain()` is unrestricted and works correctly for both `SIMULATE` and `EXECUTE`.

### Capability exposure scope
All 26 blocks of the formed Coke Oven expose the `FluidHandler.BLOCK` capability
on all faces. There is no side or layer gating. This matches the user-facing
expectation that a pipe can attach to any face of any block in the formed structure.

## Verification steps

### Manual test matrix (with Mekanism installed)
1. Build a 3x3x3 Coke Oven, confirm it forms (windows appear on middle layer).
2. Place coal in the input slot; wait for creosote to appear in the GUI tank.
3. Attach a Mekanism Mechanical Pipe to the bottom face of a bottom-layer brick.
   Connect the pipe to a Mekanism Basic Fluid Tank.
   **Expected**: Creosote drains from the Coke Oven into the tank.
4. Attach a pipe to a side face of a bottom-layer brick.
   **Expected**: Creosote drains.
5. Attach a pipe to an upper-layer brick (top layer or middle-layer 'B' block).
   **Expected**: Creosote drains (no layer restriction).
6. Attempt to fill the Coke Oven from a Mekanism tank containing water.
   **Expected**: No fluid enters the Coke Oven (fill returns 0).
7. Break and reform the Coke Oven with pipes attached.
   **Expected**: After reformation, draining resumes within one tick.
