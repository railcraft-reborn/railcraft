package mods.railcraft.client.emblem;

import java.util.Objects;
import org.jetbrains.annotations.ApiStatus;
import com.google.common.base.Preconditions;

/**
 *
 * @author CovertJaguar <https://www.railcraft.info/>
 */
public class EmblemToolsClient {

  private static EmblemPackageManager packageManager;
  private static EmblemItemRenderer renderer;

  public static EmblemPackageManager packageManager() {
    Objects.requireNonNull(packageManager);
    return packageManager;
  }

  @ApiStatus.Internal
  public static void _setPackageManager(EmblemPackageManager packageManager) {
    Preconditions.checkState(EmblemToolsClient.packageManager == null,
        "packageManager already set.");
    EmblemToolsClient.packageManager = packageManager;
  }

  public static EmblemItemRenderer renderer() {
    Objects.requireNonNull(renderer);
    return renderer;
  }

  @ApiStatus.Internal
  public static void _setRenderer(EmblemItemRenderer renderer) {
    Preconditions.checkState(EmblemToolsClient.renderer == null,
        "renderer already set.");
    EmblemToolsClient.renderer = renderer;
  }
}
