# Railcraft Reborn Capability Contracts

## Registration

All capabilities are registered via `RegisterCapabilitiesEvent` in `Railcraft.java`
(method `handleRegisterCapabilities`). This is the NeoForge 1.21.1 standard approach.

## Block Entity Capabilities

| Block Entity | ItemHandler | FluidHandler | EnergyStorage |
|---|---|---|---|
| Coke Oven | via multiblock master | via multiblock master (drain-only tank) | - |
| Blast Furnace | via multiblock master | - | - |
| Steam Oven | via multiblock master | via multiblock master | - |
| Steam Boiler | direct (fuel) | direct (water/steam) | - |
| Steam Turbine | - | via multiblock master | via multiblock master |
| Crusher | via multiblock master | - | via multiblock master |
| Iron Tank | - | via ValveFluidHandler (valve blocks only) | - |
| Steel Tank | - | via ValveFluidHandler (valve blocks only) | - |
| Powered Rolling Machine | direct | - | via Charge distribution |
| Cart Dispenser | direct | - | - |
| Train Dispenser | direct | - | - |
| Feed Station | direct | - | - |
| Fluid Loader | direct | direct | - |
| Fluid Unloader | direct | direct | - |
| Item Loader | direct | - | - |
| Item Unloader | direct | - | - |
| Void Chest | direct | - | - |
| Water Tank Siding | - | direct | - |

### Multiblock Proxy Pattern

Multiblock block entities (coke oven, blast furnace, steam oven, crusher, steam turbine,
iron/steel tank) use a master-member pattern:

1. Each member block entity is registered for capabilities.
2. `getItemCap(side)` / `getFluidCap(side)` resolve the master block entity.
3. The master delegates to its module's handler.
4. If the multiblock is not formed or the entity is on the client side, null is returned.

### Valve Height Restrictions (Iron/Steel Tank)

`ValveFluidHandler` enforces fill/drain rules based on valve height relative to the master:
- **Fill**: Only through valves where `blockPos.getY() > masterPos.getY()`.
- **Drain**: Only through valves where `blockPos.getY() <= masterPos.getY() + 1`.

Non-valve blocks (walls, gauges) return null from `getFluidCap()`.

Player bucket interactions (`TankBlockEntity.use()`) now route through the ValveFluidHandler
when available, enforcing the same height restrictions as pipe/automation access.

## Entity Capabilities

| Entity | ItemHandler | ItemHandler.AUTOMATION | FluidHandler | EnergyStorage |
|---|---|---|---|---|
| Cargo Minecart | InvWrapper | InvWrapper | - | - |
| Void Chest Minecart | InvWrapper | InvWrapper | - | - |
| Tank Minecart | - | - | TankManager | - |
| Energy Minecart | - | - | - | BatteryCart |
| Steam Locomotive | fuel ContainerMapper | fuel ContainerMapper | TankManager | - |
| Electric Locomotive | - | - | - | BatteryCart |
| Tunnel Bore | InvWrapper | InvWrapper | - | - |

### RollingStock Custom Capability

A custom `RollingStock.CAPABILITY` is registered for ALL `AbstractMinecart` entity types
(including vanilla). This is used internally by Railcraft for train linking and cart
management. It reads from `RailcraftAttachmentTypes.MINECART_ROLLING_STOCK`.

## Item Capabilities

| Item | Capability |
|---|---|
| Creosote Bucket | FluidHandler.ITEM via FluidBucketWrapper |

## Block-Level Capabilities (Non-BlockEntity)

Charge distribution blocks register `EnergyStorage.BLOCK` via `Charge.distribution`:
- Force Track Emitter
- Nickel Zinc Battery
- Nickel Iron Battery
- Zinc Carbon Battery / Empty
- Zinc Silver Battery / Empty
- Frame
- Powered Rolling Machine

## Contract Notes

- Capability providers MUST return null if the capability is not available for the given
  context (direction, entity state, etc.). Railcraft follows this correctly.
- `level.invalidateCapabilities(pos)` is called in `membershipChanged(null)` when a
  multiblock is deformed, correctly invalidating cached capabilities.
- Capability registration is done once per entity type, not per instance.
