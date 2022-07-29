package mods.railcraft.data;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import joptsimple.OptionParser;
import net.minecraft.DetectedVersion;
import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.item.DyeColor;

public class TextureTinter {

  private final Path inputPath;
  private final Path outputPath;

  public TextureTinter(Path inputPath, Path outputPath) {
    this.inputPath = inputPath;
    this.outputPath = outputPath;
  }

  private void run() {
    // Bootstrap MC so we can access DyeColor
    SharedConstants.setVersion(DetectedVersion.BUILT_IN);
    Bootstrap.bootStrap();

    try (var stream = Files.walk(this.inputPath)) {
      stream.filter(Files::isRegularFile)
          .filter(path -> path.toString().endsWith(".png"))
          .flatMap(TextureTinter::process)
          .forEach(this::save);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private void save(Result result) {
    try {
      ImageIO.write(result.image(), result.name().split("\\.")[1],
          this.outputPath.resolve(result.name()).toFile());
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private static BufferedImage clone(BufferedImage bufferImage) {
    var colorModel = bufferImage.getColorModel();
    var raster = bufferImage.copyData(null);
    var alphaPremultiplied = colorModel.isAlphaPremultiplied();
    return new BufferedImage(colorModel, raster, alphaPremultiplied, null);
  }

  private record Result(String name, BufferedImage image) {}

  private static Stream<Result> process(Path path) {
    BufferedImage image;
    try {
      image = ImageIO.read(Files.newInputStream(path));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    return Arrays.stream(DyeColor.values())
        .map(dyeColor -> {
          var color = dyeColor.getTextureDiffuseColors();
          var coloredImage = clone(image);
          tint(coloredImage, color[0], color[1], color[2]);
          return new Result(path.getFileName().toString().replace("white", dyeColor.getName()),
              coloredImage);
        });
  }

  private static void tint(BufferedImage image, float red, float green, float blue) {
    for (int x = 0; x < image.getWidth(); x++) {
      for (int y = 0; y < image.getHeight(); y++) {
        var pixelColor = new Color(image.getRGB(x, y), true);
        int r = (int) ((toFloat(pixelColor.getRed()) * red) * 255);
        int g = (int) ((toFloat(pixelColor.getGreen()) * green) * 255);
        int b = (int) ((toFloat(pixelColor.getBlue()) * blue) * 255);
        int a = pixelColor.getAlpha();
        int rgba = (a << 24) | (r << 16) | (g << 8) | b;
        image.setRGB(x, y, rgba);
      }
    }
  }

  private static float toFloat(int value) {
    return value / 255.0F;
  }

  public static void main(String[] args) {
    var parser = new OptionParser();
    var inputSpec = parser.accepts("input").withRequiredArg();
    var outputSpec = parser.accepts("output").withRequiredArg();

    var optionSet = parser.parse(args);
    var inputPath = Path.of(inputSpec.value(optionSet));
    var outputPath = Path.of(outputSpec.value(optionSet));

    new TextureTinter(inputPath, outputPath).run();
  }
}
