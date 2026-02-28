# BUG-0004: FluidTools.containsFluid uses wrong matching logic

## Symptom
The fluid manipulator's `canPlaceItem` check for filtered fluid containers rejects valid
containers from mods that use multi-tank items (e.g., Mekanism). A container holding the
correct fluid in one tank but a different fluid (or empty) in another tank would be
incorrectly rejected.

## Root Cause
`FluidTools.containsFluid()` iterated over all tanks in an item's fluid handler and
returned `false` if ANY tank did not match the target fluid. The correct logic is to return
`true` if ANY tank matches.

Original (wrong): "all tanks must contain the target fluid"
Fixed: "at least one tank must contain the target fluid"

## Patch Summary
Changed the loop logic from fail-on-mismatch to succeed-on-match. The loop now returns
`true` on the first matching tank and defaults to `false` if no tanks match.

## File Changed
`src/main/java/mods/railcraft/util/fluids/FluidTools.java`

## Verification Steps
1. Set a fluid filter on a fluid manipulator (loader or unloader).
2. Attempt to insert a multi-tank fluid container that holds the filtered fluid in one of
   its tanks.
3. Verify the container is accepted into the input slot.
4. Verify that containers with none of the matching fluid are still correctly rejected.
