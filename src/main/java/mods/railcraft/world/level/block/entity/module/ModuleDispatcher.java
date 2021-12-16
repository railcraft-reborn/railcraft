package mods.railcraft.world.level.block.entity.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mods.railcraft.api.core.NetworkSerializable;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public class ModuleDispatcher
    implements NetworkSerializable, INBTSerializable<CompoundTag>, ICapabilityProvider {

  private final Map<String, Module> modules = new HashMap<>();
  private final List<ICapabilityProvider> capabilityProviders = new ArrayList<>();

  public <T extends Module & ICapabilityProvider> T registerCapabilityModule(String name,
      T module) {
    this.capabilityProviders.add(module);
    return this.registerModule(name, module);
  }

  public <T extends Module> T registerModule(String name, T module) {
    if (this.modules.put(name, module) != null) {
      throw new IllegalStateException("Module already registered with name: " + name);
    }
    return module;
  }

  public boolean hasModules() {
    return !this.modules.isEmpty();
  }

  public void serverTick() {
    this.modules.values().forEach(Module::serverTick);
  }

  @Override
  public void writeToBuf(FriendlyByteBuf out) {
    out.writeVarInt(this.modules.size());
    this.modules.forEach((name, module) -> {
      out.writeUtf(name);
      module.writeToBuf(out);
    });
  }

  @Override
  public void readFromBuf(FriendlyByteBuf in) {
    var size = in.readVarInt();
    for (int i = 0; i < size; i++) {
      var name = in.readUtf();
      var module = this.modules.get(name);
      if (module == null) {
        throw new IllegalStateException("Missing module: " + name);
      }
    }
  }

  @Override
  public CompoundTag serializeNBT() {
    var tag = new CompoundTag();
    this.modules.forEach((name, module) -> tag.put(name, module.serializeNBT()));
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundTag tag) {
    if (tag.isEmpty()) {
      return;
    }
    this.modules.forEach((name, module) -> {
      var moduleTag = tag.getCompound(name);
      if (!moduleTag.isEmpty()) {
        module.deserializeNBT(moduleTag);
      }
    });
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    return this.capabilityProviders.stream()
        .map(provider -> provider.getCapability(cap, side))
        .filter(LazyOptional::isPresent)
        .findFirst()
        .orElse(LazyOptional.empty());
  }
}
