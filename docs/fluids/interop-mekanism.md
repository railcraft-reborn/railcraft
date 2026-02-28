# Mekanism Fluid Interop

## Overview

Railcraft Reborn exposes fluid capabilities via the standard NeoForge
`Capabilities.FluidHandler.BLOCK` and `Capabilities.FluidHandler.ENTITY` APIs. Mekanism's
Mechanical Pipes use these same APIs, so interop is achieved through the standard capability
contract with no mod-specific code required.

## Expected Behavior

### Coke Oven -> Mekanism Pipe

- Mekanism pipe connects to any block in the formed 3x3x3 coke oven multiblock.
- Each member block entity proxies `getFluidCap()` to the master block's `CokeOvenModule`
  tank.
- The coke oven tank has `disableFill = true` (internal fill only via recipe output). Pipes
  can **drain** creosote but cannot push fluid in.
- BUG-0009 fixed a stale capability cache issue that prevented external mods from
  discovering the handler after multiblock formation.
- **Not verified**: This fix has not been tested against a live Mekanism installation.
  Workaround: use a Railcraft Fluid Loader to extract creosote.

### Mekanism Pipe -> Iron/Steel Tank

- Mekanism pipe connects to tank **valve** blocks only. Non-valve blocks (walls, gauges) do
  not expose `IFluidHandler` via capabilities.
- The `ValveFluidHandler` restricts access by height:
  - **Fill**: Only through valves above the master block (Y offset > 0).
  - **Drain**: Only through valves at or near the bottom (Y offset <= 1).
- Expected: Mekanism pipe fills the tank through upper valves and drains through lower
  valves.

### Mekanism Pipe -> Tank Minecart

- Tank minecarts expose their `StandardTank` via `Capabilities.FluidHandler.ENTITY`.
- Filter items in the minecart restrict which fluids are accepted.
- Expected: Mekanism pipe can fill/drain tank minecarts directly.

### Fluid Loader/Unloader with Mekanism

- The Fluid Loader pulls from adjacent blocks (all sides except DOWN) using NeoForge
  `FluidUtil.tryFluidTransfer`.
- The Fluid Unloader pushes to adjacent blocks (all sides except UP).
- Both expose `IFluidHandler` via `Capabilities.FluidHandler.BLOCK` on all sides.
- Expected: Mekanism pipes can connect to loaders/unloaders to supply or receive fluid.

## Test Matrix

| Source | Sink | Direction | Expected | Notes |
|---|---|---|---|---|
| Coke Oven | Mekanism Tank | Drain via pipe | Creosote transfers | Coke oven is drain-only |
| Mekanism Tank | Iron Tank (upper valve) | Fill via pipe | Fluid fills tank | Valve must be above master |
| Iron Tank (lower valve) | Mekanism Tank | Drain via pipe | Fluid drains from tank | Valve Y offset <= 1 |
| Mekanism Tank | Tank Minecart | Fill via pipe | Fluid fills cart | Respects filter if set |
| Tank Minecart | Mekanism Tank | Drain via pipe | Fluid drains from cart | |
| Mekanism Tank | Fluid Loader | Fill via pipe | Loader receives fluid | Loader then fills carts |
| Fluid Unloader | Mekanism Tank | Push via pipe | Unloader pushes fluid | Unloader pulls from carts |

## Audit Notes

### Coke Oven Codepaths

- **Block Entity**: `CokeOvenBlockEntity` extends `MultiblockBlockEntity`
- **Module**: `CokeOvenModule` extends `CookingModule`
  - 5-slot inventory: input (0), output (1), liquid input (2), liquid processing (3),
    liquid output (4)
  - `StandardTank` with 64-bucket capacity, `disableFill()` set
  - Creosote produced via `internalFill()` which bypasses `disableFill`
- **Capability Registration**: `Railcraft.java` line 217-218, registers
  `Capabilities.FluidHandler.BLOCK` for `COKE_OVEN` block entity type
- **Capability Exposure**: `CokeOvenBlockEntity.getFluidCap()` resolves master block and
  returns `CokeOvenModule.getTank()` (the raw `StandardTank`)
- **All faces exposed**: The capability registration does not filter by direction; any side
  can drain creosote

### Iron/Steel Tank Codepaths

- **Block Entities**: `IronTankBlockEntity` / `SteelTankBlockEntity` extend `TankBlockEntity`
- **TankBlockEntity** extends `MultiblockBlockEntity`
  - Uses `TankModule` with a `StandardTank` of dynamic capacity
  - `membershipChanged()` creates `ValveFluidHandler` for valve blocks only
  - Non-valve blocks return `null` from `getFluidCap()` (no capability exposed)
- **ValveFluidHandler**: Proxies to master tank with height-based fill/drain restrictions
- **Capability Registration**: `Railcraft.java` lines 242-245
- **GUI Sync**: Uses `FluidGaugeWidget` via `RailcraftMenu.broadcastChanges()` -> widget
  system -> `SyncWidgetMessage` packet

### Known Issues Found and Fixed

1. **BUG-0001**: TankModule slot extraction was inverted (blocked output, exposed processing)
2. **BUG-0002**: CompositeFluidHandler passed unadjusted tank indices to sub-handlers
3. **BUG-0003**: ContainerMapper.removeItemNoUpdate missing start offset
4. **BUG-0004**: FluidTools.containsFluid required all tanks to match instead of any
5. **BUG-0005**: FluidGaugeWidget skipped sync when tank filled from empty state
6. **BUG-0006**: CokeOvenModule liquid processing slot was extractable by automation
7. **BUG-0009**: Coke Oven capability cache not invalidated on multiblock formation

### BUG-0009 Details (Coke Oven Creosote Extraction)

**Symptom**: Mekanism mechanical pipes could not extract creosote from a formed Coke Oven
despite the GUI showing fluid in the tank.

**Root cause**: `CokeOvenBlockEntity.membershipChanged()` did not call
`level.invalidateCapabilities(this.getBlockPos())`. For 'B' (brick) blocks on the bottom
and top layers, the block state does not change when the multiblock forms (WINDOW was
already `false`), so NeoForge's implicit capability cache invalidation never fires.
Mekanism's `BlockCapabilityCache` retains a stale `null` handler from before formation.

**Fix**: Added `this.level.invalidateCapabilities(this.getBlockPos())` at the end of
`membershipChanged()`, called unconditionally for both formation and disbanding. This
forces external mods to re-query the capability, picking up the now-valid
`StandardTank` (drain-only via `disableFill`).

**Supported extraction faces**: All 26 blocks of the formed Coke Oven, all faces.
No side or layer gating.

### Next Steps

- Add debug logging gated behind a config flag for capability queries, fill/drain calls,
  and GUI sync values to aid future troubleshooting.
- Verify iron tank `serverTick()` push behavior works correctly with Mekanism pipes
  (currently pushes to DOWN and horizontal neighbors but only from valve blocks).
- Consider whether `TankBlockEntity.use()` should route through `ValveFluidHandler` instead
  of directly accessing the raw tank, to enforce height-based restrictions on player
  bucket interactions.
