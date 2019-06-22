package org.vivecraft.gui.settings;

import java.util.HashSet;

import org.vivecraft.control.ButtonTuple;
import org.vivecraft.control.ButtonType;
import org.vivecraft.control.ControllerType;
import org.vivecraft.control.VRButtonMapping;

import com.google.common.collect.Sets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;

public class GuiKeyBindingSelection extends GuiListExtended
{
    private final GuiVRControls controlsScreen;
	private int maxListLabelWidth;

    public GuiKeyBindingSelection(GuiVRControls controls)
    {
        super(Minecraft.getMinecraft(), controls.width + 45, controls.height, 63, controls.height - 32, 20);
        this.controlsScreen = controls;
        
        for (ControllerType controller : ControllerType.values())
        {
	        for (ButtonType button : controller.getController().getActiveButtons())
	        {
	        	String buttonName = new ButtonTuple(button, controller).toReadableString();
	            int j = mc.fontRenderer.getStringWidth(buttonName);
	            if (j > this.maxListLabelWidth) {
	                this.maxListLabelWidth = j;
	            }
	            this.addEntry(new GuiKeyBindingSelection.ButtonEntry(new ButtonTuple(button, controller), buttonName));
	            if (controller.getController().canButtonBeTouched(button)) {
	            	String buttonName2 = new ButtonTuple(button, controller, true).toReadableString();
		            int k = mc.fontRenderer.getStringWidth(buttonName2);
		            if (k > this.maxListLabelWidth) {
		                this.maxListLabelWidth = k;
		            }
		            this.addEntry(new GuiKeyBindingSelection.ButtonEntry(new ButtonTuple(button, controller, true), buttonName2));
	            }
	        }
        }    
    }
 
    @Override
    public int getListWidth() {
    	return this.width;
    }
    
    @Override
    protected int getScrollBarX() {
		return this.width - 100;
    }
    
    public class CategoryEntry extends IGuiListEntry
    {
        private final String labelText;
        private final int labelWidth;

        public CategoryEntry(String name)
        {
            this.labelText = I18n.format(name);
            this.labelWidth = GuiKeyBindingSelection.this.mc.fontRenderer.getStringWidth(this.labelText);
        }
        
        @Override
        public void drawEntry(int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks)
        {
            GuiKeyBindingSelection.this.mc.fontRenderer.drawString(TextFormatting.AQUA + this.labelText, GuiKeyBindingSelection.this.mc.currentScreen.width / 2 - this.labelWidth / 2, this.getY() + slotHeight - GuiKeyBindingSelection.this.mc.fontRenderer.FONT_HEIGHT - 1, 16777215);
        }
    }

    public class ButtonEntry extends GuiListExtended.IGuiListEntry
    {
        private final ButtonTuple button;
        private final String buttonName;

        private ButtonEntry(ButtonTuple button, String buttonName)
        {
            this.button = button;
            this.buttonName = buttonName;
        }

        @Override
		public void drawEntry(int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks)
        {
            TextFormatting formatting = TextFormatting.WHITE;
            boolean flag = isSelected; //(mouseX <= GuiKeyBindingSelection.this.width * .6) && mouseY < this.getX() + slotHeight && mouseY > this.getY();
            boolean flag2 = GuiKeyBindingSelection.this.controlsScreen.mappingButtons != null && GuiKeyBindingSelection.this.controlsScreen.mappingButtons.contains(this.button);
            boolean flag3 = checkMappingConflict();
            if (flag) formatting = TextFormatting.GREEN;
            else if (flag3) formatting = TextFormatting.RED;
            GuiKeyBindingSelection.this.mc.fontRenderer.drawString(formatting + this.buttonName, this.getX() + 190 - GuiKeyBindingSelection.this.maxListLabelWidth, this.getY() + slotHeight / 2 - GuiKeyBindingSelection.this.mc.fontRenderer.FONT_HEIGHT / 2, 16777215);
            if (flag2) GuiKeyBindingSelection.this.mc.fontRenderer.drawString("->", this.getX() + 175 - GuiKeyBindingSelection.this.maxListLabelWidth, this.getY() + slotHeight / 2 - GuiKeyBindingSelection.this.mc.fontRenderer.FONT_HEIGHT / 2, 16777215);          
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
        	if(GuiKeyBindingSelection.this.controlsScreen.mapping == null) return false; //how did u get here?
        	if (GuiKeyBindingSelection.this.controlsScreen.mappingButtons.contains(this.button))
        		GuiKeyBindingSelection.this.controlsScreen.mappingButtons.remove(this.button);
        	else
        		GuiKeyBindingSelection.this.controlsScreen.mappingButtons.add(this.button);
        	return true;
        }

        private boolean checkMappingConflict() {
        	if (GuiKeyBindingSelection.this.controlsScreen.mapping == null)
        		return false;

			HashSet<ButtonTuple> set = new HashSet<>();
			set.add(this.button);

        	for (VRButtonMapping mapping : mc.vrSettings.buttonMappings.values()) {
				if (mapping == GuiKeyBindingSelection.this.controlsScreen.mapping)
					continue;
				if (mapping.isGUIBinding() != GuiKeyBindingSelection.this.controlsScreen.mapping.isGUIBinding())
					continue;
				if (mapping.conflictsWith(set, GuiKeyBindingSelection.this.controlsScreen.mappingModifiers, GuiKeyBindingSelection.this.controlsScreen.mapping.isModifierBinding()))
					return true;
        	}

        	return false;
        }
    }
}
