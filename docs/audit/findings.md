# Railcraft Reborn Audit Findings

Audit scope: NeoForge 1.21.1 capability contracts, cross-mod fluid/item automation,
client sync correctness, and soft corruption risks.

## Critical / High Findings (Patched)

### AUD-0001: TankModule extractItem slot guard inverted
- **Severity**: Critical
- **Feature**: Iron/Steel Tank automation
- **File**: `src/main/java/mods/railcraft/world/module/TankModule.java`
- **Bug**: `extractItem()` blocked SLOT_OUTPUT instead of SLOT_INPUT/SLOT_PROCESS. External
  automation could yank items mid-processing (bucket deletion) and could not pull finished
  items from the output slot.
- **Expected**: Block extraction from input/processing slots; allow extraction from output.
- **Status**: Patched (see `docs/bugs/BUG-0001-tank-module-slot-extraction.md`)

### AUD-0002: CompositeFluidHandler tank index not adjusted for sub-handlers
- **Severity**: Critical
- **Feature**: Train fluid interop (Mekanism pipes)
- **File**: `src/main/java/mods/railcraft/util/fluids/CompositeFluidHandler.java`
- **Bug**: `getFluidInTank()`, `getTankCapacity()`, `isFluidValid()` passed raw global tank
  index to sub-handlers instead of computing local slot index via `getSlotFromIndex()`.
  Causes wrong data for any cart beyond the first in a train.
- **Expected**: Local slot index must be used when delegating to sub-handlers.
- **Status**: Patched (see `docs/bugs/BUG-0002-composite-fluid-handler-index.md`)

### AUD-0003: ContainerMapper.removeItemNoUpdate missing start offset
- **Severity**: High
- **Feature**: All ContainerMapper consumers with non-zero start
- **File**: `src/main/java/mods/railcraft/util/container/ContainerMapper.java`
- **Bug**: `removeItemNoUpdate()` passed raw slot index to backing container, unlike every
  other method which adds `this.start`. Corrupts inventory for any mapper with start > 0.
- **Expected**: Add `this.start` offset and `validSlot()` bounds check.
- **Status**: Patched (see `docs/bugs/BUG-0003-container-mapper-offset.md`)

### AUD-0004: FluidTools.containsFluid all-vs-any logic
- **Severity**: High
- **Feature**: Fluid manipulator filter acceptance
- **File**: `src/main/java/mods/railcraft/util/fluids/FluidTools.java`
- **Bug**: `containsFluid()` required ALL tanks in an item to match the target fluid.
  Multi-tank containers from Mekanism or other mods were incorrectly rejected.
- **Expected**: Return true if ANY tank contains the target fluid.
- **Status**: Patched (see `docs/bugs/BUG-0004-contains-fluid-logic.md`)

### AUD-0005: FluidGaugeWidget sync skipped on empty-to-filled transition
- **Severity**: High
- **Feature**: Tank GUI display
- **File**: `src/main/java/mods/railcraft/gui/widget/FluidGaugeWidget.java`
- **Bug**: `requiresSync()` guarded change detection with
  `!this.lastSyncedFluidStack.isEmpty()`, so the first fill (empty -> has fluid) never
  triggered immediate sync. GUI lagged behind by up to 16 ticks.
- **Expected**: Sync on any state change regardless of previous state.
- **Status**: Patched (see `docs/bugs/BUG-0005-fluid-gauge-sync.md`)

### AUD-0006: CokeOvenModule liquid slots exposed to automation + mapper oversize
- **Severity**: High
- **Feature**: Coke oven automation
- **File**: `src/main/java/mods/railcraft/world/module/CokeOvenModule.java`
- **Bug**: (a) extractItem only blocked SLOT_INPUT, leaving SLOT_LIQUID_INPUT and
  SLOT_LIQUID_PROCESSING exposed to external extraction. (b) insertItem only allowed
  SLOT_INPUT, preventing automated bucket insertion. (c) fluidContainer mapper size was 4
  instead of 3, mapping a non-existent 6th slot.
- **Expected**: Block extraction from input and liquid processing slots; allow insertion
  into both SLOT_INPUT and SLOT_LIQUID_INPUT; mapper size = 3.
- **Status**: Patched (see `docs/bugs/BUG-0006-coke-oven-slot-extraction.md`)

### AUD-0007: TankBlockEntity.use() bypasses ValveFluidHandler height restrictions
- **Severity**: High
- **Feature**: Tank bucket interaction
- **File**: `src/main/java/mods/railcraft/world/level/block/entity/tank/TankBlockEntity.java`
  (line 131)
- **Bug**: `use()` called `FluidUtil.interactWithFluidHandler(player, hand, this.module.getTank())`
  directly on the raw `StandardTank`, completely bypassing the `ValveFluidHandler` that
  enforces height-based fill/drain restrictions. Players could fill through a bottom valve
  or drain through a top valve.
- **Expected**: Route through `this.fluidHandler` (the `ValveFluidHandler`) when available,
  falling back to raw tank only for non-valve blocks (which shouldn't have `use()` called
  on them anyway since they don't expose fluid capability).
- **Status**: Patched

### AUD-0008: TunnelBore missing ENTITY/ENTITY_AUTOMATION item capability
- **Severity**: High
- **Feature**: TunnelBore automation interop
- **File**: `src/main/java/mods/railcraft/Railcraft.java` (capability registration)
- **Bug**: TunnelBore has a 25-slot Container (fuel, ballast, track slots) but was not
  registered for `Capabilities.ItemHandler.ENTITY` or `ENTITY_AUTOMATION`. Other mods
  (Mekanism, hoppers) cannot interact with it via capabilities.
- **Expected**: Register both ENTITY and ENTITY_AUTOMATION for TunnelBore.
- **Status**: Patched

## Medium Findings (Documented, Not Patched)

### AUD-0009: No BlockCapabilityCache usage in the codebase
- **Severity**: Medium (performance)
- **Feature**: All block-level capability queries
- **Files**: `FluidTools.findNeighbors()`, `BlockModuleProvider.findAdjacentContainers()`,
  `DumpingTrackBlockEntity`, `TankBlockEntity.serverTick()`
- **Bug**: Per NeoForge documentation, `BlockCapabilityCache` should be used for capability
  queries that happen frequently (e.g., every tick). All Railcraft capability queries use
  raw `level.getCapability()` calls, which perform hash lookups, block entity fetches, and
  provider iteration on every call.
- **Expected**: Create `BlockCapabilityCache` instances for neighbors during `onLoad()` and
  use cached lookups in tick methods.
- **Impact**: Server tick performance degradation proportional to the number of active
  machines. Not a correctness issue.
- **Status**: Needs implementation (non-trivial refactor of `findNeighbors` and
  `findAdjacentContainers` patterns)

### AUD-0010: TankBlockEntity.serverTick() only pushes fluid, never pulls
- **Severity**: Medium (design limitation)
- **Feature**: Iron/Steel tank fluid distribution
- **File**: `src/main/java/mods/railcraft/world/level/block/entity/tank/TankBlockEntity.java`
  (line 66-76)
- **Bug**: `serverTick()` calls `FluidUtil.tryFluidTransfer(neighbor, this.fluidHandler, ...)`
  which pushes fluid FROM the tank TO neighbors. There is no pull direction. This means
  tanks cannot automatically receive fluid from adjacent blocks without a pipe or loader.
- **Expected**: This may be intentional design (valve height restrictions enforce
  fill-from-top-only). Document for awareness.
- **Status**: By design (document only)

### AUD-0011: DumpingTrackBlockEntity not registered for item capability
- **Severity**: Low
- **Feature**: Dumping track automation
- **File**: `src/main/java/mods/railcraft/world/level/block/entity/track/DumpingTrackBlockEntity.java`
- **Bug**: DumpingTrackBlockEntity extends RailcraftBlockEntity (not ContainerBlockEntity)
  but performs raw capability queries on neighbors. It is not registered for ItemHandler
  capability itself. This is likely intentional as the dumping track is not a storage
  block — it dumps items from carts to adjacent inventories.
- **Status**: By design (document only)

## Verified Correct

### Entity capability registration
- Cargo Minecart: ENTITY + ENTITY_AUTOMATION (ItemHandler) - Correct
- Tank Minecart: ENTITY (FluidHandler) - Correct
- Energy Minecart: ENTITY (EnergyStorage) - Correct
- Void Chest Minecart: ENTITY + ENTITY_AUTOMATION (ItemHandler) - Correct
- Electric Locomotive: ENTITY (EnergyStorage) - Correct
- Steam Locomotive: ENTITY (FluidHandler) + ENTITY + ENTITY_AUTOMATION (ItemHandler) - Correct
- RollingStock: Custom capability for ALL AbstractMinecart types - Correct

### Block entity capability registration
All block entities with `getFluidCap`, `getItemCap`, or `getEnergyCap` methods are
registered in `Railcraft.handleRegisterCapabilities()`. Multiblock members correctly proxy
to master block entities, returning null on client side.

### Menu/GUI sync
- RailcraftMenu uses a custom Widget system for fluid gauge sync (not DataSlot)
- Widget sync triggers on `broadcastChanges()` with per-widget `requiresSync()` checks
- Signal network sync uses `syncToClient()` with byte buffer serialization
- `setChanged()` calls are present in all critical data mutation paths (message handlers,
  container callbacks)

### NeoForge contract compliance
- Capabilities registered via `RegisterCapabilitiesEvent` (correct for 1.21.1)
- Block capabilities use `@Nullable Direction` context (correct)
- Entity capabilities registered per entity type (correct)
- Item capabilities registered for creosote bucket using `FluidBucketWrapper` (correct)
- Charge distribution registered as block-level `EnergyStorage` capability (correct)
