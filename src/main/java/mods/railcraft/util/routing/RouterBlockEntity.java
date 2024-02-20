package mods.railcraft.util.routing;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Either;
import mods.railcraft.Translations;
import mods.railcraft.api.util.EnumUtil;
import mods.railcraft.client.gui.widget.button.ButtonTexture;
import mods.railcraft.client.gui.widget.button.TexturePosition;
import mods.railcraft.gui.button.ButtonState;
import mods.railcraft.util.container.ForwardingContainer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.MenuProvider;

public interface RouterBlockEntity extends MenuProvider, ForwardingContainer {

  boolean isPowered();

  void neighborChanged();

  Railway getRailway();

  void setRailway(@Nullable GameProfile gameProfile);

  Optional<Either<RoutingLogic, RoutingLogicException>> logicResult();

  default Optional<RoutingLogic> logic() {
    return this.logicResult().flatMap(x -> x.left());
  }

  default Optional<RoutingLogicException> logicError() {
    return this.logicResult().flatMap(x -> x.right());
  }

  void resetLogic();

  default Deque<String> loadPages(CompoundTag tag) {
    Deque<String> contents = new LinkedList<>();
    var pages = tag.getList("pages", Tag.TAG_STRING).copy();
    for (int i = 0; i < pages.size(); i++) {
      var page = pages.getString(i).split("\n");
      contents.addAll(Arrays.asList(page));
    }
    return contents;
  }

  enum Railway implements ButtonState<Railway>, StringRepresentable {

    PUBLIC("public"),
    PRIVATE("private");

    private static final StringRepresentable.EnumCodec<Railway> CODEC =
        StringRepresentable.fromEnum(Railway::values);

    private final String name;

    Railway(String name) {
      this.name = name;
    }

    @Override
    public Component label() {
      return Component.translatable(Translations.makeKey("screen",
          String.format("router_block_entity.%s_railway", this.name)));
    }

    @Override
    public TexturePosition texturePosition() {
      return ButtonTexture.SMALL_BUTTON;
    }

    @Override
    public Railway next() {
      return EnumUtil.next(this, values());
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }

    public static Railway fromName(String name) {
      return CODEC.byName(name, PUBLIC);
    }
  }
}
