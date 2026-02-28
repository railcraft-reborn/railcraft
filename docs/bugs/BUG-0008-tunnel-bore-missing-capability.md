# BUG-0008: TunnelBore missing item handler capability registration

## Symptom
Mekanism pipes, hoppers, and other mods' automation systems cannot interact with the Tunnel
Bore's 25-slot inventory. The Tunnel Bore appears as having no item capability to external
mods, even though it has a full Container implementation.

## Root Cause
The `handleRegisterCapabilities` method in `Railcraft.java` registered ENTITY and
ENTITY_AUTOMATION item handler capabilities for Cargo Minecart, Void Chest Minecart, and
Steam Locomotive, but omitted the Tunnel Bore entity type entirely.

## Patch Summary
Added `registerEntity` calls for both `Capabilities.ItemHandler.ENTITY` and
`Capabilities.ItemHandler.ENTITY_AUTOMATION` for `RailcraftEntityTypes.TUNNEL_BORE`,
using `InvWrapper` consistent with other minecart registrations.

## File Changed
`src/main/java/mods/railcraft/Railcraft.java`

## Verification Steps
1. Spawn a Tunnel Bore in the world.
2. Attempt to connect a Mekanism pipe or hopper to it.
3. Confirm that items can be inserted and extracted through automation.
4. Verify that fuel, ballast, and track items can be supplied automatically.
