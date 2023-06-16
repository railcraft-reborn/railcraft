package mods.railcraft.client.gui.screen;

import java.util.BitSet;
import java.util.EnumMap;
import java.util.Map;
import java.util.regex.Pattern;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.network.NetworkChannel;
import mods.railcraft.network.play.SetAnalogSignalControllerBoxAttributesMessage;
import mods.railcraft.world.level.block.entity.signal.AnalogSignalControllerBoxBlockEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;

public class AnalogSignalControllerBoxScreen extends IngameWindowScreen {

  private final AnalogSignalControllerBoxBlockEntity signalBox;
  private static final Pattern PATTERN_RANGE = Pattern.compile("(\\d+)-(\\d+)|(\\d+)");
  // When doing Pattern.matcher, these are the groups: ^ 1 ^ 2 ^ 3

  private final Map<SignalAspect, BitSet> signalAspectTriggerSignals =
      new EnumMap<>(SignalAspect.class);
  private final Map<SignalAspect, EditBox> textFields =
      new EnumMap<>(SignalAspect.class);

  public AnalogSignalControllerBoxScreen(AnalogSignalControllerBoxBlockEntity signalBox) {
    super(signalBox.getDisplayName(), LARGE_WINDOW_TEXTURE, 176, 113);
    this.signalBox = signalBox;
    for (var entry : signalBox.getSignalAspectTriggerSignals().entrySet()) {
      this.signalAspectTriggerSignals.put(entry.getKey(), (BitSet) entry.getValue().clone());
    }
  }

  private String rangeToString(BitSet b) {
    var s = new StringBuilder();
    var start = -1;
    for (var i = 0; i < 16; i++) {
      if (b.get(i)) {
        if (start == -1) {
          s.append(i);
          start = i;
        }
      } else if (start != -1) {
        if (i - 1 == start)
          s.append(",");
        else
          s.append("-").append(i - 1).append(",");
        start = -1;
      }
    }
    if (start != -1 && start != 15) {
      s.append("-15");
      start = 15;
    }

    if ((s.length() == 0) || start == 15)
      return s.toString();
    else
      return s.substring(0, s.length() - 1); // Remove trailing comma
  }

  private void parseRegex(String regex, BitSet bits) {
    bits.clear();
    var m = PATTERN_RANGE.matcher(regex);
    while (m.find()) {
      if (m.groupCount() >= 3 && m.group(3) != null) {
        int i = Integer.parseInt(m.group(3));
        if (i >= 0 && i <= 15)
          bits.set(i);
      } else {
        int start = Integer.parseInt(m.group(1));
        int end = Integer.parseInt(m.group(2));
        if (start >= 0 && end >= 0 && end <= 15 && start <= end)
          for (int i = start; i <= end; i++) {
            bits.set(i);
          }
      }
    }
  }

  @Override
  public void init() {
    var centeredX = (this.width - this.windowWidth) / 2;
    var centeredY = (this.height - this.windowHeight) / 2;

    for (var entry : this.signalAspectTriggerSignals.entrySet()) {
      var textField = new EditBox(this.font, centeredX + 72,
          centeredY + getYPosFromIndex(entry.getKey().ordinal()), 95, 10,
          entry.getKey().getDisplayName());
      textField.setMaxLength(37);
      textField.setValue(this.rangeToString(entry.getValue()));
      textField.setFilter(string -> {
        for (var ch : string.toCharArray()) {
          if (!Character.isDigit(ch) && ch != ',' && ch != '-') {
            return false;
          }
        }
        return true;
      });
      this.addRenderableWidget(textField);
      this.textFields.put(entry.getKey(), textField);
    }
  }

  @Override
  protected void renderContent(GuiGraphics guiGraphics, int mouseX, int mouseY,
      float partialTicks) {
    for (var aspect : SignalAspect.values()) {
      guiGraphics.drawString(this.font, aspect.getDisplayName(), 10,
          getYPosFromIndex(aspect.ordinal()) + 1, TEXT_COLOR, false);
    }
  }

  @Override
  public void removed() {
    if (this.minecraft.level != null && this.minecraft.level.isClientSide()) {
      for (Map.Entry<SignalAspect, BitSet> entry : this.signalAspectTriggerSignals.entrySet()) {
        this.parseRegex(this.textFields.get(entry.getKey()).getValue(), entry.getValue());
      }
      this.signalBox.setSignalAspectTriggerSignals(this.signalAspectTriggerSignals);
      NetworkChannel.GAME.sendToServer(
          new SetAnalogSignalControllerBoxAttributesMessage(this.signalBox.getBlockPos(),
              this.signalAspectTriggerSignals));
    }
  }

  private static int getYPosFromIndex(int i) {
    return 22 + i * 14;
  }

}
