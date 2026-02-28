# Mekanism Interop

## Status

Railcraft Reborn exposes fluid capabilities via the standard NeoForge
`Capabilities.FluidHandler.BLOCK` and `Capabilities.FluidHandler.ENTITY` APIs.

### Iron / Steel Tank

Mekanism mechanical pipes can transfer fluid to and from Railcraft Iron and
Steel Tanks via valve blocks. No special configuration required.

### Coke Oven

Coke Oven creosote extraction via external block capability remains
incompatible with Mekanism mechanical pipes. The capability invalidation fix
(BUG-0009) addresses a stale-cache issue but has not been verified against a
live Mekanism installation.

**Workaround**: Use a Railcraft Fluid Loader adjacent to the Coke Oven to
extract creosote into a tank or pipe network.

### Tank Minecarts

Mekanism pipes should be able to fill and drain Railcraft tank minecarts via
the entity fluid capability. Not independently verified.

## Technical Details

See [docs/fluids/interop-mekanism.md](../fluids/interop-mekanism.md) for
detailed codepath analysis, capability registration, and test matrix.
