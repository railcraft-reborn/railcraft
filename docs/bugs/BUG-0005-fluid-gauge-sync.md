# BUG-0005: FluidGaugeWidget initial sync skipped when tank fills from empty

## Symptom
The iron/steel tank GUI fluid gauge appears glitchy. When fluid first enters an empty tank,
the gauge does not update immediately; it only refreshes on the 16-tick periodic sync
interval. This causes a visible delay and makes the gauge appear to "jump" or lag behind
the actual fluid level.

## Root Cause
In `FluidGaugeWidget.requiresSync()`, the change-detection condition was guarded by
`!this.lastSyncedFluidStack.isEmpty()`. Since `lastSyncedFluidStack` starts as
`FluidStack.EMPTY`, the first fill (empty -> has fluid) never triggered an immediate sync.
The widget would only update on the periodic 16-tick interval.

This did not affect the coke oven because its fluid level changes gradually and the
periodic sync masked the issue. The iron tank, being a large multiblock that can receive
large amounts of fluid at once, made the delayed sync much more noticeable.

## Patch Summary
Removed the `!this.lastSyncedFluidStack.isEmpty()` guard from the sync condition. Now any
change between the last synced state and the current tank state triggers an immediate sync,
regardless of whether the previous state was empty.

## File Changed
`src/main/java/mods/railcraft/gui/widget/FluidGaugeWidget.java`

## Verification Steps
1. Open an iron/steel tank GUI with an empty tank.
2. Pour fluid into the tank (via bucket, pipe, or valve).
3. Verify the gauge updates immediately and smoothly, without delay or jumps.
4. Drain the tank completely and refill. Verify the gauge transitions are smooth in both
   directions.
5. Compare behavior with the coke oven gauge — both should now be equally responsive.
