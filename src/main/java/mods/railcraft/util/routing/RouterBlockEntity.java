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
import mods.railcraft.world.item.component.RailcraftDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;

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

  default Deque<String> loadPages(ItemStack book) {
    Deque<String> contents = new LinkedList<>();
    var writableBookContent = book.get(RailcraftDataComponents.ROUTING_TABLE_BOOK);
    if (writableBookContent != null) {
      writableBookContent
          .pages()
          .forEach(x -> {
            var page = x.split("\n");
            contents.addAll(Arrays.asList(page));
          });
    }
    return contents;
  }

  enum Railway implements ButtonState<Railway>, StringRepresentable {

    PUBLIC("public"),
    PRIVATE("private");

    public static final StringRepresentable.EnumCodec<Railway> CODEC =
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
  }
}
