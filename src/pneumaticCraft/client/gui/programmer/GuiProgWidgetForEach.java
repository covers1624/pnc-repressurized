package pneumaticCraft.client.gui.programmer;

import net.minecraft.client.resources.I18n;
import pneumaticCraft.client.gui.GuiProgrammer;
import pneumaticCraft.client.gui.widget.WidgetComboBox;
import pneumaticCraft.common.progwidgets.ProgWidgetForEachCoordinate;

public class GuiProgWidgetForEach extends GuiProgWidgetAreaShow<ProgWidgetForEachCoordinate>{

    private WidgetComboBox variableField;

    public GuiProgWidgetForEach(ProgWidgetForEachCoordinate widget, GuiProgrammer guiProgrammer){
        super(widget, guiProgrammer);
    }

    @Override
    public void initGui(){
        super.initGui();

        variableField = new WidgetComboBox(fontRendererObj, guiLeft + 10, guiTop + 42, 160, fontRendererObj.FONT_HEIGHT + 1);
        variableField.setElements(guiProgrammer.te.getAllVariables());
        addWidget(variableField);
        variableField.setText(widget.getVariable());
    }

    @Override
    public void keyTyped(char chr, int keyCode){
        if(keyCode == 1) {
            widget.setVariable(variableField.getText());
        }
        super.keyTyped(chr, keyCode);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        super.drawScreen(mouseX, mouseY, partialTicks);
        fontRendererObj.drawString(I18n.format("gui.progWidget.coordinate.variableName"), guiLeft + 10, guiTop + 30, 0xFF000000);
    }
}
