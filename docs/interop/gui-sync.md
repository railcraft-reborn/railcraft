# Railcraft Reborn GUI Sync Architecture

## Block Entity Sync

### RailcraftBlockEntity Base Pattern

All Railcraft block entities inherit from `RailcraftBlockEntity`, which implements a custom
byte-buffer sync mechanism:

1. **Chunk Load Sync**: `getUpdateTag()` serializes state via `writeToBuf()` into a byte
   array stored under `CompoundTagKeys.SYNC`. `handleUpdateTag()` deserializes via
   `readFromBuf()`.

2. **Block Update Sync**: `getUpdatePacket()` returns
   `ClientboundBlockEntityDataPacket.create(this)`, which uses the same NBT mechanism.

3. **Manual Sync**: `syncToClient()` sends the update packet to all tracking players.

### Multiblock Sync

`MultiblockBlockEntity` extends this with membership data (relative position, master
position). `syncToClient()` is called in `setMembership()` and `membershipChanged()`.

### Tank Sync

`TankBlockEntity` calls both `setChanged()` and `syncToClient()` in `tankChanged()`,
ensuring both persistence and immediate client notification when fluid levels change.

## Menu/Widget Sync

### Widget System (Custom)

Railcraft uses a custom `Widget` system instead of vanilla `DataSlot` for complex data:

1. `RailcraftMenu.broadcastChanges()` iterates all registered widgets
2. Each widget's `requiresSync(player)` is checked
3. If sync needed, `sendWidgetPacket()` serializes widget data to a buffer
4. `SyncWidgetMessage` packet is sent to the client
5. Client-side widget `readFromBuf()` deserializes and updates display

**FluidGaugeWidget** uses this system to sync fluid type and amount. It has:
- Immediate sync on any fluid state change (type or amount differs from last synced state)
- Periodic fallback sync every 16 ticks

### DataSlot Usage (Simple Values)

Used for simple integer values like burn time in `SteamLocomotiveMenu` via `SimpleDataSlot`.
NeoForge patches the vanilla 16-bit limitation to support full 32-bit integers.

### Slot Sync (Items)

Standard NeoForge `Slot` / `SlotItemHandler` instances sync item stacks automatically
through the vanilla `AbstractContainerMenu.broadcastChanges()` mechanism.

## Sync Correctness Notes

1. `setChanged()` marks the chunk dirty for disk persistence. This is separate from
   `syncToClient()` which handles network sync.

2. Message handlers (e.g., `SetFluidManipulatorMessage`, `SetItemManipulatorMessage`) call
   `setChanged()` after modifying block entity state, ensuring persistence.

3. The custom byte-buffer sync (`writeToBuf`/`readFromBuf`) is more efficient than full NBT
   serialization for network transfer, as it avoids serializing unchanged fields.

4. `level.invalidateCapabilities(pos)` is called when multiblock membership changes to null,
   ensuring capability caches held by other mods are properly invalidated.
