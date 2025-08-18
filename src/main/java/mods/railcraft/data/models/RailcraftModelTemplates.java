package mods.railcraft.data.models;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;
import org.apache.commons.lang3.NotImplementedException;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

class RailcraftModelTemplates {

   private static final ModelTemplate STEEL_ANVIL_TEMPLATE = new ModelTemplate(
       ModelTemplates.ANVIL.model,
      Optional.empty(),
      TextureSlot.TOP,
      RailcraftTextureSlot.BODY,
      TextureSlot.PARTICLE
  );

  static final TexturedModel.Provider STEEL_ANVIL_TEMPLATE_PROVIDER = TexturedModel.createDefault(
      block -> {
        var base = TextureMapping.getBlockTexture(RailcraftBlocks.STEEL_ANVIL.get());
        var top = TextureMapping.getBlockTexture(block, "_top");
        return new TextureMapping()
            .put(TextureSlot.TOP, top)
            .put(RailcraftTextureSlot.BODY, base)
            .put(TextureSlot.PARTICLE, base);
      },
      STEEL_ANVIL_TEMPLATE
  );

  private static final ModelTemplate SIGNAL_BOX_TEMPLATE = new ModelTemplate(
      Optional.of(ModelLocationUtils
          .decorateBlockModelLocation(RailcraftConstants.rl("signal_box").toString())),
      Optional.empty(),
      TextureSlot.UP
  );

  static final TexturedModel.Provider SIGNAL_BOX_TEMPLATE_PROVIDER = TexturedModel.createDefault(
      block -> new TextureMapping()
          .put(TextureSlot.UP, RailcraftConstants.rl("entity/signal_box/" + name(block))),
      SIGNAL_BOX_TEMPLATE
  );

  private static final ModelTemplate DUAL_SIGNAL_INVENTORY_TEMPLATE = new ModelTemplate(
      Optional.of(ModelLocationUtils
          .decorateBlockModelLocation(RailcraftConstants.rl("dual_signal_inventory").toString())),
      Optional.of("_inventory"),
      RailcraftTextureSlot.TOP_LAMP,
      RailcraftTextureSlot.BOTTOM_LAMP
  );

  static final TexturedModel.Provider DUAL_SIGNAL_INVENTORY_TEMPLATE_PROVIDER = TexturedModel.createDefault(
      block -> {
        ResourceLocation topLamp;
        ResourceLocation bottomLamp;
        if (block == RailcraftBlocks.DUAL_DISTANT_SIGNAL.get()) {
          topLamp = RailcraftConstants.rl("entity/signal_aspect/red");
          bottomLamp = RailcraftConstants.rl("entity/signal_aspect/green");
        } else if (block == RailcraftBlocks.DUAL_TOKEN_SIGNAL.get()) {
          topLamp = RailcraftConstants.rl("entity/signal_aspect/green");
          bottomLamp = RailcraftConstants.rl("entity/signal_aspect/yellow");
        } else if (block == RailcraftBlocks.DUAL_BLOCK_SIGNAL.get()) {
          topLamp = RailcraftConstants.rl("entity/signal_aspect/green");
          bottomLamp = RailcraftConstants.rl("entity/signal_aspect/red");
        } else {
          throw new NotImplementedException();
        }

        return new TextureMapping()
            .put(RailcraftTextureSlot.TOP_LAMP, topLamp)
            .put(RailcraftTextureSlot.BOTTOM_LAMP, bottomLamp);
      },
      DUAL_SIGNAL_INVENTORY_TEMPLATE
  );

  static final ModelTemplate FRAME_TEMPLATE = new ModelTemplate(
      Optional.of(ModelLocationUtils
          .decorateBlockModelLocation(RailcraftConstants.rl("frame_template").toString())),
      Optional.empty(),
      TextureSlot.SIDE,
      TextureSlot.TOP
  );

  private static final ModelTemplate LOGBOOK_TEMPLATE = new ModelTemplate(
      Optional.of(ModelLocationUtils
          .decorateBlockModelLocation(RailcraftConstants.rl("logbook_template").toString())),
      Optional.empty(),
      TextureSlot.SIDE,
      TextureSlot.TOP,
      TextureSlot.BOTTOM,
      RailcraftTextureSlot.PAPER,
      RailcraftTextureSlot.COVER
  );

  static final TexturedModel.Provider LOGBOOK_TEMPLATE_PROVIDER = TexturedModel.createDefault(
      block -> {
        var sideTexture = TextureMapping.getBlockTexture(block, "_side");
        var topTexture = TextureMapping.getBlockTexture(block, "_top");
        var bottomTexture = TextureMapping.getBlockTexture(block, "_bottom");
        var coverTexture = TextureMapping.getBlockTexture(block, "_cover");
        var paperTexture = TextureMapping.getBlockTexture(block, "_paper");

        return new TextureMapping()
            .put(RailcraftTextureSlot.PAPER, paperTexture)
            .put(RailcraftTextureSlot.COVER, coverTexture)
            .put(TextureSlot.SIDE, sideTexture)
            .put(TextureSlot.TOP, topTexture)
            .put(TextureSlot.BOTTOM, bottomTexture);
      },
      LOGBOOK_TEMPLATE
  );

  private static final ModelTemplate CHIMNEY_TEMPLATE = new ModelTemplate(
      Optional.of(ModelLocationUtils
          .decorateBlockModelLocation(RailcraftConstants.rl("chimney_template").toString())),
      Optional.empty(),
      TextureSlot.SIDE,
      TextureSlot.TOP,
      TextureSlot.BOTTOM,
      RailcraftTextureSlot.INTERIOR,
      TextureSlot.PARTICLE
  );

  static final TexturedModel.Provider CHIMNEY_TEMPLATE_PROVIDER = TexturedModel.createDefault(
      block -> {
        var topTexture = TextureMapping.getBlockTexture(block, "_top");
        var bottomTexture = TextureMapping.getBlockTexture(block, "_bottom");
        var sideTexture = TextureMapping.getBlockTexture(block, "_side");
        var interiorTexture = TextureMapping.getBlockTexture(block, "_interior");

        return new TextureMapping()
            .put(TextureSlot.TOP, topTexture)
            .put(TextureSlot.BOTTOM, bottomTexture)
            .put(TextureSlot.SIDE, sideTexture)
            .put(RailcraftTextureSlot.INTERIOR, interiorTexture)
            .put(TextureSlot.PARTICLE, sideTexture);
      },
      CHIMNEY_TEMPLATE
  );

  private static final ModelTemplate STEAM_BOILER_TEMPLATE = new ModelTemplate(
      Optional.of(ModelLocationUtils
          .decorateBlockModelLocation(RailcraftConstants.rl("template_steam_boiler_tank").toString())),
      Optional.empty(),
      TextureSlot.SIDE,
      TextureSlot.END
  );

  private static final Function<Block, TextureMapping> STEAM_BOILER_MAPPING_FN = block -> {
    var end = TextureMapping.getBlockTexture(block, "_end");
    var side = TextureMapping.getBlockTexture(block, "_side");

    return new TextureMapping()
        .put(TextureSlot.SIDE, side)
        .put(TextureSlot.END, end);
  };

  static final TexturedModel.Provider STEAM_BOILER_TEMPLATE_PROVIDER =
      TexturedModel.createDefault(STEAM_BOILER_MAPPING_FN, STEAM_BOILER_TEMPLATE);

  private static final ModelTemplate STEAM_BOILER_NE_TEMPLATE = new ModelTemplate(
      Optional.of(ModelLocationUtils
          .decorateBlockModelLocation(RailcraftConstants.rl("template_steam_boiler_tank_ne").toString())),
      Optional.of("_ne"),
      TextureSlot.SIDE,
      TextureSlot.END
  );

  static final TexturedModel.Provider STEAM_BOILER_NE_TEMPLATE_PROVIDER =
      TexturedModel.createDefault(STEAM_BOILER_MAPPING_FN, STEAM_BOILER_NE_TEMPLATE);

  private static final ModelTemplate STEAM_BOILER_NEW_TEMPLATE = new ModelTemplate(
      Optional.of(ModelLocationUtils
          .decorateBlockModelLocation(RailcraftConstants.rl("template_steam_boiler_tank_new").toString())),
      Optional.of("_new"),
      TextureSlot.SIDE,
      TextureSlot.END
  );

  static final TexturedModel.Provider STEAM_BOILER_NEW_TEMPLATE_PROVIDER =
      TexturedModel.createDefault(STEAM_BOILER_MAPPING_FN, STEAM_BOILER_NEW_TEMPLATE);

  private static final ModelTemplate STEAM_BOILER_NSE_TEMPLATE = new ModelTemplate(
      Optional.of(ModelLocationUtils
          .decorateBlockModelLocation(RailcraftConstants.rl("template_steam_boiler_tank_nse").toString())),
      Optional.of("_nse"),
      TextureSlot.SIDE,
      TextureSlot.END
  );

  static final TexturedModel.Provider STEAM_BOILER_NSE_TEMPLATE_PROVIDER =
      TexturedModel.createDefault(STEAM_BOILER_MAPPING_FN, STEAM_BOILER_NSE_TEMPLATE);

  private static final ModelTemplate STEAM_BOILER_NSW_TEMPLATE = new ModelTemplate(
      Optional.of(ModelLocationUtils
          .decorateBlockModelLocation(RailcraftConstants.rl("template_steam_boiler_tank_nsw").toString())),
      Optional.of("_nsw"),
      TextureSlot.SIDE,
      TextureSlot.END
  );

  static final TexturedModel.Provider STEAM_BOILER_NSW_TEMPLATE_PROVIDER =
      TexturedModel.createDefault(STEAM_BOILER_MAPPING_FN, STEAM_BOILER_NSW_TEMPLATE);

  private static final ModelTemplate STEAM_BOILER_NW_TEMPLATE = new ModelTemplate(
      Optional.of(ModelLocationUtils
          .decorateBlockModelLocation(RailcraftConstants.rl("template_steam_boiler_tank_nw").toString())),
      Optional.of("_nw"),
      TextureSlot.SIDE,
      TextureSlot.END
  );

  static final TexturedModel.Provider STEAM_BOILER_NW_TEMPLATE_PROVIDER =
      TexturedModel.createDefault(STEAM_BOILER_MAPPING_FN, STEAM_BOILER_NW_TEMPLATE);

  private static final ModelTemplate STEAM_BOILER_SE_TEMPLATE = new ModelTemplate(
      Optional.of(ModelLocationUtils
          .decorateBlockModelLocation(RailcraftConstants.rl("template_steam_boiler_tank_se").toString())),
      Optional.of("_se"),
      TextureSlot.SIDE,
      TextureSlot.END
  );

  static final TexturedModel.Provider STEAM_BOILER_SE_TEMPLATE_PROVIDER =
      TexturedModel.createDefault(STEAM_BOILER_MAPPING_FN, STEAM_BOILER_SE_TEMPLATE);

  private static final ModelTemplate STEAM_BOILER_SEW_TEMPLATE = new ModelTemplate(
      Optional.of(ModelLocationUtils
          .decorateBlockModelLocation(RailcraftConstants.rl("template_steam_boiler_tank_sew").toString())),
      Optional.of("_sew"),
      TextureSlot.SIDE,
      TextureSlot.END
  );

  static final TexturedModel.Provider STEAM_BOILER_SEW_TEMPLATE_PROVIDER =
      TexturedModel.createDefault(STEAM_BOILER_MAPPING_FN, STEAM_BOILER_SEW_TEMPLATE);

  private static final ModelTemplate STEAM_BOILER_SW_TEMPLATE = new ModelTemplate(
      Optional.of(ModelLocationUtils
          .decorateBlockModelLocation(RailcraftConstants.rl("template_steam_boiler_tank_sw").toString())),
      Optional.of("_sw"),
      TextureSlot.SIDE,
      TextureSlot.END
  );

  static final TexturedModel.Provider STEAM_BOILER_SW_TEMPLATE_PROVIDER =
      TexturedModel.createDefault(STEAM_BOILER_MAPPING_FN, STEAM_BOILER_SW_TEMPLATE);


  static final ModelTemplate MIRRORED_CUBE_TEMPLATE = new ModelTemplate(
      Optional.of(ModelLocationUtils
          .decorateBlockModelLocation(RailcraftConstants.rl("template_mirrored_cube").toString())),
      Optional.empty(),
      TextureSlot.DOWN,
      TextureSlot.UP,
      TextureSlot.NORTH,
      TextureSlot.SOUTH,
      TextureSlot.EAST,
      TextureSlot.WEST,
      TextureSlot.PARTICLE
  );

  static final ModelTemplate BATTERY_TEMPLATE = new ModelTemplate(
      Optional.of(ModelLocationUtils
          .decorateBlockModelLocation(RailcraftConstants.rl("battery").toString())),
      Optional.empty(),
      TextureSlot.BOTTOM,
      TextureSlot.TOP,
      RailcraftTextureSlot.SIDE_A,
      RailcraftTextureSlot.SIDE_B
  );

  static final TexturedModel.Provider BATTERY_TEMPLATE_PROVIDER = TexturedModel.createDefault(
      block -> {
        var top = TextureMapping.getBlockTexture(block, "_top");
        var bottom = TextureMapping.getBlockTexture(block, "_bottom");
        var sideA = TextureMapping.getBlockTexture(block, "_side_a");
        var sideB = TextureMapping.getBlockTexture(block, "_side_b");

        return new TextureMapping()
            .put(TextureSlot.TOP, top)
            .put(TextureSlot.BOTTOM, bottom)
            .put(RailcraftTextureSlot.SIDE_A, sideA)
            .put(RailcraftTextureSlot.SIDE_B, sideB);
      },
      BATTERY_TEMPLATE
  );

  static final ModelTemplate FORCE_TRACK_EMITTER_TEMPLATE = new ModelTemplate(
      Optional.of(ModelLocationUtils
          .decorateBlockModelLocation(RailcraftConstants.rl("force_track_emitter").toString())),
      Optional.empty(),
      TextureSlot.FRONT,
      TextureSlot.SIDE,
      TextureSlot.PARTICLE,
      RailcraftTextureSlot.COLORED_FRONT,
      RailcraftTextureSlot.COLORED_SIDE
  );

  static final ModelTemplate FORCE_TRACK_TEMPLATE = new ModelTemplate(
      Optional.of(ModelLocationUtils
          .decorateBlockModelLocation(RailcraftConstants.rl("template_force_track").toString())),
      Optional.empty(),
      TextureSlot.RAIL
  );

  static final ModelTemplate ELEVATOR_TRACK_TEMPLATE = new ModelTemplate(
      Optional.of(ModelLocationUtils
          .decorateBlockModelLocation(RailcraftConstants.rl("template_elevator_track").toString())),
      Optional.empty(),
      TextureSlot.TEXTURE
  );

  enum OUTFITTED_TRACK_PROVIDER {
    RAIL_FLAT(ModelTemplates.RAIL_FLAT),
    RAIL_RAISED_NE(ModelTemplates.RAIL_RAISED_NE),
    RAIL_RAISED_SW(ModelTemplates.RAIL_RAISED_SW);

    private final ModelTemplate template;
    private final String suffix;

    OUTFITTED_TRACK_PROVIDER(ModelTemplate template) {
      this.template = template;
      this.suffix = "_outfitted" + template.suffix.orElse("");
    }

    public TexturedModel.Provider getProvider() {
      return TexturedModel.createDefault(
          block -> TextureMapping
              .singleSlot(TextureSlot.RAIL, TextureMapping.getBlockTexture(block, "_outfitted")),
          template.extend().renderType(RailcraftModelProvider.CUTOUT).suffix(suffix).build()
      );
    }
  }

  enum TrackType {
    ACTIVATOR_TRACK("activator_track"),
    ACTIVATOR_TRACK_ON("activator_track"),
    BOOSTER_TRACK("booster_track"),
    BOOSTER_TRACK_ON("booster_track"),
    EMBARKING_TRACK("embarking_track"),
    EMBARKING_TRACK_ON("embarking_track"),
    DUMPING_TRACK("dumping_track"),
    DUMPING_TRACK_ON("dumping_track"),
    LAUNCHER_TRACK("launcher_track"),
    LAUNCHER_TRACK_ON("launcher_track"),
    ONE_WAY_TRACK("one_way_track"),
    ONE_WAY_TRACK_ON("one_way_track"),
    ROUTING_TRACK("routing_track"),
    ROUTING_TRACK_ON("routing_track"),
    WHISTLE_TRACK("whistle_track"),
    WHISTLE_TRACK_ON("whistle_track"),
    DETECTOR_TRACK("detector_track"),
    DETECTOR_TRACK_ON("detector_track"),
    DETECTOR_TRACK_TRAVEL("detector_track_travel"),
    DETECTOR_TRACK_TRAVEL_ON("detector_track_travel"),
    LOCOMOTIVE_TRACK_SHUTDOWN("locomotive_track_shutdown"),
    LOCOMOTIVE_TRACK_SHUTDOWN_ON("locomotive_track_shutdown"),
    LOCOMOTIVE_TRACK_IDLE("locomotive_track_idle"),
    LOCOMOTIVE_TRACK_IDLE_ON("locomotive_track_idle"),
    LOCOMOTIVE_TRACK_RUNNING("locomotive_track_running"),
    LOCOMOTIVE_TRACK_RUNNING_ON("locomotive_track_running"),
    COUPLER_TRACK_COUPLER("coupler_track_coupler"),
    COUPLER_TRACK_COUPLER_ON("coupler_track_coupler"),
    COUPLER_TRACK_DECOUPLER("coupler_track_decoupler"),
    COUPLER_TRACK_DECOUPLER_ON("coupler_track_decoupler"),
    COUPLER_TRACK_AUTO_COUPLER("coupler_track_auto_coupler"),
    COUPLER_TRACK_AUTO_COUPLER_ON("coupler_track_auto_coupler"),
    DISEMBARKING_TRACK_LEFT("disembarking_track_left"),
    DISEMBARKING_TRACK_LEFT_ON("disembarking_track_left"),
    DISEMBARKING_TRACK_RIGHT("disembarking_track_right"),
    DISEMBARKING_TRACK_RIGHT_ON("disembarking_track_right"),
    TRANSITION_TRACK("transition_track"),
    TRANSITION_TRACK_ON("transition_track"),
    RAIL_OUTFITTED("rail_outfitted"),
    CONTROL_TRACK("control_track");

    private final String folderName;

    TrackType(String folderName) {
      this.folderName = folderName;
    }
  }

  enum TRACK_PROVIDER {
    RAIL_FLAT(ModelTemplates.RAIL_FLAT),
    RAIL_RAISED_NE(ModelTemplates.RAIL_RAISED_NE),
    RAIL_RAISED_SW(ModelTemplates.RAIL_RAISED_SW);

    private final ModelTemplate template;

    TRACK_PROVIDER(ModelTemplate template) {
      this.template = template;
    }

    private String getName(TrackType type) {
      return type.name().toLowerCase(Locale.ROOT) + template.suffix.orElse("");
    }

    public ResourceLocation getModel(TrackType type) {
      var path = "block/track_template/%s/%s".formatted(type.folderName, getName(type));
      return RailcraftConstants.rl(path);
    }
  }

  enum THROTTLE_TRACK_PROVIDER {
    THROTTLE_TRACK_1,
    THROTTLE_TRACK_2,
    THROTTLE_TRACK_3,
    THROTTLE_TRACK_4;

    private String getName(boolean active, boolean reverse) {
      var suffixBuilder = new StringBuilder(this.name().toLowerCase(Locale.ROOT));
      if (reverse) {
        suffixBuilder.append("_reverse");
      }
      if (active) {
        suffixBuilder.append("_on");
      }
      return suffixBuilder.toString();
    }

    public ResourceLocation getModel(boolean active, boolean reverse) {
      return RailcraftConstants.rl("block/track_template/throttle_track/" + getName(active, reverse));
    }
  }

  enum LOCKING_TRACK_PROVIDER {
    LOCKDOWN, LOCKDOWN_ON,
    TRAIN_LOCKDOWN, TRAIN_LOCKDOWN_ON,
    HOLDING, HOLDING_ON,
    TRAIN_HOLDING, TRAIN_HOLDING_ON,
    BOARDING, BOARDING_ON,
    BOARDING_REVERSED, BOARDING_REVERSED_ON,
    TRAIN_BOARDING, TRAIN_BOARDING_ON,
    TRAIN_BOARDING_REVERSED, TRAIN_BOARDING_REVERSED_ON;

    private String getName() {
      return this.name().toLowerCase(Locale.ROOT);
    }

    public ResourceLocation getModel() {
      return RailcraftConstants.rl("block/track_template/locking_track/" + getName());
    }
  }

  private static ResourceLocation key(Block block) {
    return BuiltInRegistries.BLOCK.getKey(block);
  }

  private static String name(Block block) {
    return key(block).getPath();
  }
}
