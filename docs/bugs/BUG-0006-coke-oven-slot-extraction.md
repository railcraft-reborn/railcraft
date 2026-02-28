# BUG-0006: CokeOvenModule liquid processing slot extractable by automation

## Symptom
External automation (hoppers, Mekanism pipes) can pull items from the coke oven's liquid
processing slot (slot 3) while a fluid fill operation is in progress. This can cause fluid
containers (buckets) to disappear or duplicate.

## Root Cause
The `CokeOvenModule` item handler only blocked extraction from `SLOT_INPUT` (slot 0). The
liquid container processing slots (`SLOT_LIQUID_INPUT` = 2 and `SLOT_LIQUID_PROCESSING` = 3)
were unprotected, allowing external automation to yank a container mid-process.

Additionally, the insert guard only allowed insertion into `SLOT_INPUT`, which prevented
automation from inserting empty containers into `SLOT_LIQUID_INPUT` for automated creosote
bucketing.

## Patch Summary
- Blocked extraction from `SLOT_INPUT`, `SLOT_LIQUID_INPUT`, and `SLOT_LIQUID_PROCESSING`.
- Allowed insertion into both `SLOT_INPUT` and `SLOT_LIQUID_INPUT`.
- Fixed `fluidContainer` ContainerMapper size from `SLOT_LIQUID_OUTPUT` (4) to
  `SLOT_LIQUID_OUTPUT - SLOT_LIQUID_INPUT + 1` (3). The old size of 4 mapped a non-existent
  6th slot (index 5) in the 5-slot container, which could cause out-of-bounds access.

## File Changed
`src/main/java/mods/railcraft/world/module/CokeOvenModule.java`

## Verification Steps
1. Build a formed coke oven and connect a hopper or pipe to it.
2. Place coal in the input slot and empty buckets in the liquid input slot.
3. Confirm that creosote-filled buckets appear in the liquid output slot and are extractable.
4. Confirm that items in the liquid input and processing slots cannot be pulled by external
   automation.
5. Confirm that empty buckets can be inserted into the liquid input slot by automation.
