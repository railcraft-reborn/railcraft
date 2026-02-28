# BUG-0001: TankModule extractItem slot gating is inverted

## Symptom
Buckets and fluid containers disappear when external automation (hoppers, Mekanism pipes)
interacts with the iron/steel tank's inventory. Processed containers cannot be pulled out of
the output slot by pipes.

## Root Cause
In `TankModule.java`, the `extractItem` override on the `IItemHandler` blocked extraction
from `SLOT_OUTPUT` (slot 2) instead of blocking `SLOT_INPUT` (slot 0) and `SLOT_PROCESS`
(slot 1). This meant:

- External automation could yank items from the processing slot mid-operation, destroying
  the container before the fluid transfer result was placed back.
- The output slot was locked to external extraction, so finished items could never be
  pulled out by pipes/hoppers.

## Patch Summary
Changed the guard condition from `slot == SLOT_OUTPUT` to
`slot == SLOT_INPUT || slot == SLOT_PROCESS`.

## File Changed
`src/main/java/mods/railcraft/world/module/TankModule.java`

## Verification Steps
1. Place an iron/steel tank with a hopper or Mekanism pipe connected.
2. Put empty buckets into the input slot of the tank GUI while the tank contains fluid.
3. Confirm that filled buckets appear in the output slot and can be extracted by the
   hopper/pipe.
4. Confirm that items in the input and processing slots cannot be pulled out by external
   automation.
5. Confirm that no buckets are lost during the fill/drain cycle.
