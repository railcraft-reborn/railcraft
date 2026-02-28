# Railcraft Reborn Automation Safety

## Slot Gating Strategy

Railcraft uses two mechanisms to protect slots from external automation:

### 1. IItemHandler Wrapper Override (Module-Level)

Block entities that extend `ModuleBlockEntity` (via modules like `TankModule`,
`CokeOvenModule`) create custom `InvWrapper` instances that override `extractItem()` and
`insertItem()` to restrict slot access.

**TankModule** (3 slots):
- Slot 0 (SLOT_INPUT): Insert allowed, extract blocked
- Slot 1 (SLOT_PROCESS): Insert blocked, extract blocked
- Slot 2 (SLOT_OUTPUT): Insert blocked, extract allowed

**CokeOvenModule** (5 slots):
- Slot 0 (SLOT_INPUT): Insert allowed, extract blocked
- Slot 1 (SLOT_OUTPUT): Insert blocked, extract allowed
- Slot 2 (SLOT_LIQUID_INPUT): Insert allowed, extract blocked
- Slot 3 (SLOT_LIQUID_PROCESSING): Insert blocked, extract blocked
- Slot 4 (SLOT_LIQUID_OUTPUT): Insert blocked, extract allowed

### 2. WorldlyContainer / SidedInvWrapper (ContainerBlockEntity)

Block entities extending `ContainerBlockEntity` use `ItemHandlerFactory.wrap(this, side)`
which creates a `SidedInvWrapper` that respects the `WorldlyContainer` interface methods:
- `getSlotsForFace(side)`: Which slots are visible per side
- `canPlaceItemThroughFace(slot, stack, side)`: Insert gate
- `canTakeItemThroughFace(slot, stack, side)`: Extract gate

**ManipulatorBlockEntity** uses this pattern for its buffer and cart filter slots.

## ContainerMapper

`ContainerMapper` provides a windowed view into a larger Container:
- `start`: First slot index in the backing container
- `size`: Number of slots exposed
- All slot-accessing methods add `start` to the local index
- `validSlot()` performs bounds checking

Used by: CokeOvenModule (liquid slots), TunnelBore (fuel/ballast/track sub-containers),
SteamLocomotive (fuel slots).

## Container Processing Pipeline (FluidTools.processContainer)

The 3-slot pipeline for automated fluid container fill/drain:
1. **Input slot**: Accepts empty/filled containers from automation
2. **Processing slot**: Container being actively filled/drained (not accessible externally)
3. **Output slot**: Finished container ready for extraction

Each tick, `processContainer()` moves items: input -> processing -> (fill/drain) -> output.
This prevents race conditions where automation extracts a container mid-transfer.

## Known Safety Gaps

1. **BlockCapabilityCache not used**: All neighbor capability queries are raw
   `level.getCapability()` calls. This is a performance issue, not a safety issue.
   See `docs/audit/findings.md` AUD-0009.

2. **TunnelBore**: Exposes full 25-slot inventory via InvWrapper with no slot gating. All
   slots (head, fuel, ballast, track) are visible and accessible to automation. This is
   intentional as the TunnelBore has distinct slot types that use `canPlaceItem()` for
   insert filtering.
