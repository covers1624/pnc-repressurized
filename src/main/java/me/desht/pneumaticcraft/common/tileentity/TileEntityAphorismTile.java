package me.desht.pneumaticcraft.common.tileentity;

import me.desht.pneumaticcraft.common.network.DescSynced;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityAphorismTile extends TileEntityBase {
    private String[] textLines = new String[]{""};

    @DescSynced
    public int textRotation;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setInteger("textRotation", textRotation);
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        textRotation = tag.getInteger("textRotation");
    }

    @Override
    public void writeToPacket(NBTTagCompound tag) {
        super.writeToPacket(tag);
        tag.setInteger("lines", textLines.length);
        for (int i = 0; i < textLines.length; i++) {
            tag.setString("line" + i, textLines[i]);
        }
    }

    @Override
    public void readFromPacket(NBTTagCompound tag) {
        super.readFromPacket(tag);
        int lines = tag.getInteger("lines");
        textLines = new String[lines];
        for (int i = 0; i < lines; i++) {
            textLines[i] = tag.getString("line" + i);
        }
    }

    public String[] getTextLines() {
        return textLines;
    }

    public void setTextLines(String[] textLines) {
        this.textLines = textLines;
        sendDescriptionPacket();
    }
}
