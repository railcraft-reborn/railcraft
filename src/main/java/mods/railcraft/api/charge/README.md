# Charge

<!-- Editor note: yes, these are supposed to be paragraphs, go enable your editor wrapping. -->

Any block that wants to interact with the Charge network should implement `IChargeBlock` and ensure that they call the proper add/remove functions.

Everything else is done through `IAccess`.

Example code:

```java
Charge.distribution.network(world).access(pos).useCharge(500.0);
```

## General Charge Network Overview

The Charge Network is unique in that component blocks aren't required to have a `net.minecraft.tileentity.TileEntity`.

This means the vast majority of the network is made up of dumb static blocks resulting in very low cpu overheard. This remains true even for very large grids. The network of grids is maintained as a separate data structure outside the world. Each grid, which is defined as a collection of connected charge blocks, only ticks its battery objects. Only blocks which store or provide Charge to the grid need battery blocks; wires, tracks and consumers do not. This means that only a small percentage of the grid is using cpu resources, while the bulk exists passively simply as means to define connectivity.

### Side-Effects

One side effect of this is that the Charge API cannot use the Forge's Capability API. That only works on Tile Entities, not blocks. So don't bother asking for one, it won't happen.

### Construction

The grid is constructed lazily as things access it. It will slowly expand from there at a rate of a few hundred blocks per tick per network. Grids form and grow and merge, a relatively cheap operation, as they encounter other grids. However, any time a block is removed from the grid, the entire grid is destroyed and must reform. So removal is the more expensive operation, avoid it if possible. It is not recommended for blocks to remove themselves from the network for any reason other than destruction. Block removal from the network could disrupt the grid its connected too for several ticks, this may only be noticeable on grids of over several thousand blocks.

Every block on the grid has a generic loss over time. It various with the type of block, more details on that in the `mods.railcraft.api.charge.IChargeBlock.ChargeSpec`. The value is calculated as the grid is constructed and removed from the grid every tick. Consider it representative of resistive losses and current leakage. All large scale real life power systems suffer from these loss effects and are often major concerns when designing these systems.

When a consumer asks to remove Charge from the grid, it goes to the list of batteries and tries to remove from each in turn. The batteries are sorted based on their `IBatteryBlock.State`. The order batteries are drawn from is as such: source->rechargeable->disposable. Additionally they are further sorted based on efficiency. Each battery has an efficiency value associated with it. This efficiency value defines how expensive it is to extract charge from the battery. Generators have perfect efficiency and are more efficient than batteries which are more efficient than transformers. To get a more efficient grid, add more generators or high efficiency batteries.

### Charge Handling

Charge is added to the grid by grabbing a source battery and adding Charge to the battery directly. Generally a block that provides a source battery will use a Tile Entity to handle creating and adding Charge to its own battery. From there the grid handles distribution.

This is achieved by leveling the Charge in all the rechargeable batteries in the grid every tick. The benefit of this is that even if the grid is split apart for any reason, charge will be evenly distributed to the component parts. Batteries store their charge levels in their own NBT file alongside the world, this allows them to not rely on Tile Entities for serialization.

### Continious operation

This brings us to another side effect of maintaining grids and batteries outside the world. The grid will continue to operation unhindered even if large parts of it exist in chunks that are currently not loaded. At the moment, to get this benefit the entire grid needs to be loaded at least once per restart, though not all at the same time. Research is being done on how difficult it would be to persist nodes as well as batteries. But that is an enhancement for the future.

The unit of measurement used for Charge is now based on Forge Energy. This means, ignoring efficiency losses, that 1 Charge equals 1 FE/RF. This allows for simple conversion between the two systems. However, as a simplification, Railcraft's Charge lacks a concept of voltage. There are however plans for separate transmission and distribution networks, which would result in similar needs for transformers to convert from one to the other. You will see traces of these new features scattered throughout this API. The framework is in place, it just lacks the blocks themselves to make it work.

As of 2021, Charge changed from 1 Charge unit == 1 IC2 EU to 1 Charge unit == 1 FE/RF
