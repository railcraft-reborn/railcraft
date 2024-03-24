package mods.railcraft.world.module;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import mods.railcraft.api.core.NetworkSerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class ModuleDispatcher implements NetworkSerializable, INBTSerializable<CompoundTag> {

  private final Map<String, Module> moduleByName = new HashMap<>();
  private final Map<Class<?>, Module> moduleByType = new HashMap<>();

  public <T extends Module> T registerModule(String name, T module) {
    if (this.moduleByName.put(name, module) != null) {
      throw new IllegalStateException("Module already registered with name: " + name);
    }

    Class<?> clazz = module.getClass();
    do {
      if (!clazz.isAnnotationPresent(SharedModule.class)
          && this.moduleByType.put(clazz, module) != null) {
        throw new IllegalStateException(
            "Module already registered with type: " + clazz.getName());
      }
      clazz = clazz.getSuperclass();
    } while (Module.class.isAssignableFrom(clazz));

    return module;
  }

  @SuppressWarnings("unchecked")
  public <T> Optional<T> getModule(Class<T> type) {
    return Optional.ofNullable((T) this.moduleByType.get(type));
  }

  public void serverTick() {
    this.moduleByName.values().forEach(Module::serverTick);
  }

  @Override
  public void writeToBuf(FriendlyByteBuf out) {
    out.writeMap(this.moduleByName,
        FriendlyByteBuf::writeUtf, (buf, module) -> module.writeToBuf(buf));
  }

  @Override
  public void readFromBuf(FriendlyByteBuf in) {
    var size = in.readVarInt();
    for (int i = 0; i < size; i++) {
      var name = in.readUtf();
      var module = this.moduleByName.get(name);
      if (module == null) {
        throw new IllegalStateException("Missing module: " + name);
      }
      module.readFromBuf(in);
    }
  }

  @Override
  public CompoundTag serializeNBT() {
    var tag = new CompoundTag();
    this.moduleByName.forEach((name, module) -> tag.put(name, module.serializeNBT()));
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundTag tag) {
    if (tag.isEmpty()) {
      return;
    }
    this.moduleByName.forEach((name, module) -> {
      var moduleTag = tag.getCompound(name);
      if (!moduleTag.isEmpty()) {
        module.deserializeNBT(moduleTag);
      }
    });
  }
}
