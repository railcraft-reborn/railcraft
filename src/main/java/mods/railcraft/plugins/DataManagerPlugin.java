package mods.railcraft.plugins;

import java.util.Arrays;
import mods.railcraft.Railcraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Created by CovertJaguar on 6/12/2016 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class DataManagerPlugin {

  public abstract static class DataSerializerIO<T> implements IDataSerializer<T> {

    private final String name;

    protected DataSerializerIO(String name) {
      this.name = name;
    }

    public ResourceLocation getResourceName() {
      return new ResourceLocation(Railcraft.ID, name);
    }

    @Override
    public T copy(T value) {
      return value;
    }
  }

  public static final DataSerializerIO<FluidStack> FLUID_STACK =
      new DataSerializerIO<FluidStack>("fluid.stack") {
        @Override
        public final void write(PacketBuffer buf, FluidStack value) {
          buf.writeFluidStack(value);
        }

        @Override
        public final FluidStack read(PacketBuffer buf) {
          return buf.readFluidStack();
        }

        @Override
        public FluidStack copy(FluidStack value) {
          return value.copy();
        }
      };

  public static final DataSerializerIO<byte[]> BYTE_ARRAY =
      new DataSerializerIO<byte[]>("byte.array") {
        @Override
        public void write(PacketBuffer packetBuffer, byte[] bytes) {
          packetBuffer.writeByteArray(bytes);
        }

        @Override
        public byte[] read(PacketBuffer packetBuffer) {
          return packetBuffer.readByteArray();
        }

        @Override
        public byte[] copy(byte[] value) {
          return Arrays.copyOf(value, value.length);
        }
      };

  public static void register() {
    register(FLUID_STACK);
    register(BYTE_ARRAY);
  }

  private static void register(DataSerializerIO<?> dataSerializer) {
    ForgeRegistries.DATA_SERIALIZERS.register(
        new DataSerializerEntry(dataSerializer).setRegistryName(dataSerializer.getResourceName()));
  }

  public static <T extends Enum<T>> void writeEnum(EntityDataManager dataManager,
      DataParameter<Byte> parameter, Enum<T> value) {
    dataManager.set(parameter, (byte) value.ordinal());
  }

  public static <T extends Enum<T>> T readEnum(EntityDataManager dataManager,
      DataParameter<Byte> parameter, T[] values) {
    return values[dataManager.get(parameter)];
  }
}
