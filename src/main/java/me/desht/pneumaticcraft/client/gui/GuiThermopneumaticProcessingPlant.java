package me.desht.pneumaticcraft.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.api.crafting.TemperatureRange;
import me.desht.pneumaticcraft.api.heat.IHeatExchangerLogic;
import me.desht.pneumaticcraft.client.gui.widget.WidgetButtonExtended;
import me.desht.pneumaticcraft.client.gui.widget.WidgetTank;
import me.desht.pneumaticcraft.client.gui.widget.WidgetTemperature;
import me.desht.pneumaticcraft.client.util.PointXY;
import me.desht.pneumaticcraft.common.heat.HeatUtil;
import me.desht.pneumaticcraft.common.inventory.ContainerThermopneumaticProcessingPlant;
import me.desht.pneumaticcraft.common.tileentity.TileEntityThermopneumaticProcessingPlant;
import me.desht.pneumaticcraft.common.util.PneumaticCraftUtils;
import me.desht.pneumaticcraft.lib.Textures;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.Collections;
import java.util.List;

public class GuiThermopneumaticProcessingPlant extends
        GuiPneumaticContainerBase<ContainerThermopneumaticProcessingPlant,TileEntityThermopneumaticProcessingPlant> {

    private WidgetTemperature tempWidget;
    private WidgetButtonExtended dumpButton;
    private int nExposedFaces;

    public GuiThermopneumaticProcessingPlant(ContainerThermopneumaticProcessingPlant container, PlayerInventory inv, ITextComponent displayString) {
        super(container, inv, displayString);
        ySize = 212;
    }

    @Override
    protected ResourceLocation getGuiTexture() {
        return Textures.GUI_THERMOPNEUMATIC_PROCESSING_PLANT;
    }

    @Override
    public void init() {
        super.init();

        addButton(new WidgetTank(guiLeft + 13, guiTop + 19, te.getInputTank()));
        addButton(new WidgetTank(guiLeft + 79, guiTop + 19, te.getOutputTank()));

        tempWidget = new WidgetTemperature(guiLeft + 105, guiTop + 25, TemperatureRange.of(273, 673), 273, 50);
        addButton(tempWidget);

        dumpButton = new WidgetButtonExtended(guiLeft + 12, guiTop + 86, 18, 20, "").withTag("dump");
        dumpButton.setRenderedIcon(Textures.GUI_RIGHT_ARROW);
        dumpButton.setTooltipText(PneumaticCraftUtils.splitString(I18n.format("pneumaticcraft.gui.thermopneumatic.moveInput")));
        addButton(dumpButton);

        nExposedFaces = HeatUtil.countExposedFaces(Collections.singletonList(te));
    }

    @Override
    public void tick() {
        super.tick();

        if (te.maxTemperature > te.minTemperature) {
            tempWidget.setOperatingRange(TemperatureRange.of(te.minTemperature, te.maxTemperature));
        } else {
            tempWidget.setOperatingRange(null);
        }
        te.getHeatCap(null).ifPresent(l -> tempWidget.setTemperature(l.getTemperatureAsInt()));
        tempWidget.autoScaleForTemperature();

        if (hasShiftDown()) {
            dumpButton.setRenderedIcon(Textures.GUI_X_BUTTON);
            dumpButton.setTooltipText(PneumaticCraftUtils.splitString(I18n.format("pneumaticcraft.gui.thermopneumatic.dumpInput")));
        } else {
            dumpButton.setRenderedIcon(Textures.GUI_RIGHT_ARROW);
            dumpButton.setTooltipText(PneumaticCraftUtils.splitString(I18n.format("pneumaticcraft.gui.thermopneumatic.moveInput")));
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int x, int y) {
        super.drawGuiContainerBackgroundLayer(partialTicks, x, y);

        // animated progress bar
        double progress = te.getCraftingPercentage();
        int progressWidth = (int) (progress * 48);
        bindGuiTexture();
        blit(guiLeft + 30, guiTop + 36, xSize, 0, progressWidth, 30);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        String containerName = title.getFormattedText();
        RenderSystem.pushMatrix();
        RenderSystem.scaled(0.95, 1.0, 1);
        font.drawString(containerName, xSize / 2f - font.getStringWidth(containerName) / 2.1f , 5, 0x404040);
        RenderSystem.popMatrix();
        super.drawGuiContainerForegroundLayer(x, y);

    }

    @Override
    protected PointXY getInvNameOffset() {
        return null;
    }

    @Override
    protected PointXY getGaugeLocation() {
        int xStart = (width - xSize) / 2;
        int yStart = (height - ySize) / 2;
        return new PointXY(xStart + xSize * 3 / 4 + 14, yStart + ySize / 4 - 2);
    }

    @Override
    public void addProblems(List<String> curInfo) {
        super.addProblems(curInfo);

        if (!te.hasRecipe) {
            curInfo.add("pneumaticcraft.gui.tab.problems.thermopneumaticProcessingPlant.noSufficientIngredients");
        } else {
            int temp = te.getCapability(PNCCapabilities.HEAT_EXCHANGER_CAPABILITY)
                    .map(IHeatExchangerLogic::getTemperatureAsInt).orElseThrow(RuntimeException::new);
            if (temp < te.minTemperature) {
                curInfo.add("pneumaticcraft.gui.tab.problems.notEnoughHeat");
            }
        }
    }

    @Override
    protected void addWarnings(List<String> curInfo) {
        super.addWarnings(curInfo);

        if (nExposedFaces > 0) {
            curInfo.add(I18n.format("pneumaticcraft.gui.tab.problems.exposedFaces", nExposedFaces, 6));
        }
    }
}
