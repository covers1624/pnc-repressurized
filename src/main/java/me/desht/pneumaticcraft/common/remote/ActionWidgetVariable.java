package me.desht.pneumaticcraft.common.remote;

import me.desht.pneumaticcraft.client.gui.GuiRemoteEditor;
import me.desht.pneumaticcraft.client.gui.remote.GuiRemoteVariable;
import me.desht.pneumaticcraft.client.gui.widget.IGuiWidget;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.NBTTagCompound;

public abstract class ActionWidgetVariable<Widget extends IGuiWidget> extends ActionWidget<Widget> {
    private String variableName = "";

    public ActionWidgetVariable(Widget widget) {
        super(widget);
    }

    public ActionWidgetVariable() {
    }

    @Override
    public void readFromNBT(NBTTagCompound tag, int guiLeft, int guiTop) {
        super.readFromNBT(tag, guiLeft, guiTop);
        variableName = tag.getString("variableName");
    }

    @Override
    public NBTTagCompound toNBT(int guiLeft, int guiTop) {
        NBTTagCompound tag = super.toNBT(guiLeft, guiTop);
        tag.setString("variableName", variableName);
        return tag;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public GuiScreen getGui(GuiRemoteEditor guiRemote) {
        return new GuiRemoteVariable(this, guiRemote);
    }

    public abstract void onActionPerformed();

    public void onKeyTyped() {
    }

    public abstract void onVariableChange();
}
