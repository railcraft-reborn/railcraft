# BUG-0003: ContainerMapper.removeItemNoUpdate missing start offset

## Symptom
When `removeItemNoUpdate` is called on a ContainerMapper with a non-zero start offset, it
operates on the wrong slot in the backing container, potentially removing items from
unrelated slots.

## Root Cause
Every other method in `ContainerMapper` (`getItem`, `removeItem`, `setItem`,
`canPlaceItem`) correctly adds `this.start` to the slot index before delegating to the
backing container. `removeItemNoUpdate` was the sole exception, passing the raw slot index
directly.

## Patch Summary
Added `this.validSlot(slot)` bounds check and `this.start + slot` offset to
`removeItemNoUpdate`, consistent with all other slot-accessing methods.

## File Changed
`src/main/java/mods/railcraft/util/container/ContainerMapper.java`

## Verification Steps
1. This is a correctness fix for internal API consistency.
2. Any code path that calls `removeItemNoUpdate` on a ContainerMapper with `start > 0`
   would previously corrupt inventory state.
3. Verify that container operations on mapped sub-ranges of larger inventories work
   correctly (e.g., coke oven liquid slots, which use a ContainerMapper starting at slot 2).
