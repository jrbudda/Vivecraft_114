package org.vivecraft.gui.settings;

import java.util.HashSet;

import org.vivecraft.control.ButtonTuple;
import org.vivecraft.control.ButtonType;
import org.vivecraft.control.ControllerType;
import org.vivecraft.control.VRButtonMapping;

import com.google.common.collect.Sets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;

public class GuiKeyBindingSelection extends ExtendedList
{
    private final GuiVRControls controlsScreen;
	private int maxListLabelWidth;

    public GuiKeyBindingSelection(GuiVRControls controls)
    {
        super(Minecraft.getInstance(), controls.width + 45, controls.height, 63, controls.height - 32, 20);
        this.controlsScreen = controls;
        
        for (ControllerType controller : ControllerType.values())
        {
	        for (ButtonType button : controller.getController().getActiveButtons())
	        {
	        	String buttonName = new ButtonTuple(button, controller).toReadableString();
	            int j = minecraft.fontRenderer.getStringWidth(buttonName);
	            if (j > this.maxListLabelWidth) {
	                this.maxListLabelWidth = j;
	            }
	            this.addEntry(new GuiKeyBindingSelection.ButtonEntry(new ButtonTuple(button, controller), buttonName));
	            if (controller.getController().canButtonBeTouched(button)) {
	            	String buttonName2 = new ButtonTuple(button, controller, true).toReadableString();
		            int k = minecraft.fontRenderer.getStringWidth(buttonName2);
		            if (k > this.maxListLabelWidth) {
		                this.maxListLabelWidth = k;
		            }
		            this.addEntry(new GuiKeyBindingSelection.ButtonEntry(new ButtonTuple(button, controller, true), buttonName2));
	            }
	        }
        }    
    }
 
    @Override
    public int getRowWidth() {
    	return this.width;
    }
    
    @Override
    protected int getScrollbarPosition() {
    	return this.width - 100;
    }
    

    
    public class CategoryEntry extends ExtendedList.AbstractListEntry
    {
        private final String labelText;
        private final int labelWidth;

        public CategoryEntry(String name)
        {
            this.labelText = I18n.format(name);
            this.labelWidth = GuiKeyBindingSelection.this.minecraft.fontRenderer.getStringWidth(this.labelText);
        }
        
        @Override
		public void render(int index, int x, int y, int width, int height, int mouseX, int mouseY, boolean p_194999_5_,float partialTicks)
        {
            GuiKeyBindingSelection.this.minecraft.fontRenderer.drawString(TextFormatting.AQUA + this.labelText, GuiKeyBindingSelection.this.minecraft.currentScreen.width / 2 - this.labelWidth / 2, y + height - GuiKeyBindingSelection.this.minecraft.fontRenderer.FONT_HEIGHT - 1, 16777215);
        }
    }

    public class ButtonEntry extends ExtendedList.AbstractListEntry
    {
        private final ButtonTuple button;
        private final String buttonName;

        private ButtonEntry(ButtonTuple button, String buttonName)
        {
            this.button = button;
            this.buttonName = buttonName;
        }

        @Override
		public void render(int index, int x, int y, int width, int height, int mouseX, int mouseY, boolean p_194999_5_,float partialTicks)
        {
            TextFormatting formatting = TextFormatting.WHITE;
            boolean flag = getSelected() == this; //(mouseX <= GuiKeyBindingSelection.this.width * .6) && mouseY < this.getX() + slotHeight && mouseY > this.getY();
            boolean flag2 = GuiKeyBindingSelection.this.controlsScreen.mappingButtons != null && GuiKeyBindingSelection.this.controlsScreen.mappingButtons.contains(this.button);
            boolean flag3 = checkMappingConflict();
            if (flag) formatting = TextFormatting.GREEN;
            else if (flag3) formatting = TextFormatting.RED;
            GuiKeyBindingSelection.this.minecraft.fontRenderer.drawString(formatting + this.buttonName, x + 190 - GuiKeyBindingSelection.this.maxListLabelWidth, y + height / 2 - GuiKeyBindingSelection.this.minecraft.fontRenderer.FONT_HEIGHT / 2, 16777215);
            if (flag2) GuiKeyBindingSelection.this.minecraft.fontRenderer.drawString("->", y + 175 - GuiKeyBindingSelection.this.maxListLabelWidth, y + height / 2 - GuiKeyBindingSelection.this.minecraft.fontRenderer.FONT_HEIGHT / 2, 16777215);          
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

        	for (VRButtonMapping mapping : minecraft.vrSettings.buttonMappings.values()) {
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
