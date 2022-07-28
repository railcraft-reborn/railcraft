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
import net.minecraft.DetectedVersion;
import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.item.DyeColor;

public class TextureTinter {

  private static final Path inputDir = Path.of("C:\\Users\\tarbitj\\Desktop\\input\\");
  private static final Path outputDir = Path.of("C:\\Users\\tarbitj\\Desktop\\output\\");

  public static void main(String[] args) throws IOException {
    // Bootstrap MC so we can access DyeColor
    SharedConstants.setVersion(DetectedVersion.BUILT_IN);
    Bootstrap.bootStrap();

    Files.walk(inputDir)
        .filter(Files::isRegularFile)
        .filter(path -> path.toString().endsWith(".png"))
        .flatMap(TextureTinter::process)
        .forEach(TextureTinter::save);
  }

  private static BufferedImage clone(BufferedImage bufferImage) {
    var colorModel = bufferImage.getColorModel();
    var raster = bufferImage.copyData(null);
    var alphaPremultiplied = colorModel.isAlphaPremultiplied();
    return new BufferedImage(colorModel, raster, alphaPremultiplied, null);
  }

  private record Result(String name, BufferedImage image) {

  }

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

  private static void save(Result result) {
    try {
      ImageIO.write(result.image(), result.name().split("\\.")[1],
          outputDir.resolve(result.name()).toFile());
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private static void tint(BufferedImage image, float red, float green, float blue) {
    for (int x = 0; x < image.getWidth(); x++) {
      for (int y = 0; y < image.getHeight(); y++) {
        Color pixelColor = new Color(image.getRGB(x, y), true);
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
}
