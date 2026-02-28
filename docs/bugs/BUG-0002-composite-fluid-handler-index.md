# BUG-0002: CompositeFluidHandler tank index not adjusted for sub-handlers

## Symptom
When Mekanism pipes or other mods query fluid information from a train (multiple linked
carts), they receive incorrect data for any cart beyond the first one. This can cause
incorrect fluid display in external GUIs, failed fluid transfers, and potential
IndexOutOfBoundsExceptions.

## Root Cause
In `CompositeFluidHandler.java`, the methods `getFluidInTank()`, `getTankCapacity()`, and
`isFluidValid()` correctly identified the sub-handler index for a given global tank index,
but then passed the raw (unadjusted) global tank index to the sub-handler instead of the
local slot index. The `getSlotFromIndex()` method existed but was never called.

For example, with two single-tank carts (tanks 0 and 1 globally), querying tank 1 would
find handler index 1 correctly, but then call `handler[1].getFluidInTank(1)` instead of
`handler[1].getFluidInTank(0)`.

## Patch Summary
Added `getSlotFromIndex(tank, index)` calls in all three methods to convert the global tank
index to a local slot index before delegating to the sub-handler.

## File Changed
`src/main/java/mods/railcraft/util/fluids/CompositeFluidHandler.java`

## Verification Steps
1. Link two or more tank minecarts into a train.
2. Connect a Mekanism pipe or other mod's fluid transport to the train.
3. Verify that fluid information for all carts in the train is reported correctly.
4. Verify that fluid can be transferred to/from all carts in the train, not just the first.
