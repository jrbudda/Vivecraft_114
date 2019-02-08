package org.vivecraft.gui.settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import org.vivecraft.control.ButtonTuple;
import org.vivecraft.control.VRButtonMapping;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;

public class GuiVRControlsList extends GuiListExtended
{
    private final GuiVRControls parent;
    private final Minecraft mc;
    
    public GuiVRControlsList(GuiVRControls parent, Minecraft mc)
    {
        super(Minecraft.getMinecraft(), parent.width + 45, parent.height, 63, parent.height - 32, 20);
        this.parent = parent;
        this.mc = mc;
        buildList();
    }
    
	@Override
	protected int getScrollBarX() {
		return this.width - 100;
	}    
	
	@Override
	public int getListWidth() {
		return this.width;
	}
    
    public void buildList() {
        ArrayList<VRButtonMapping> bindings = new ArrayList<>(mc.vrSettings.buttonMappings.values());
        Collections.sort(bindings);

        String cat = null;
        int var7 = bindings.size();
        for (int i = 0; i < var7; i++)
        {
        	VRButtonMapping kb = bindings.get(i);
        	
        	if (parent.guiFilter != kb.isGUIBinding()) continue;
        	String s = kb.keyBinding != null ? kb.keyBinding.getKeyCategory() : (kb.functionDesc.startsWith("keyboard") ? "Keyboard Emulation" : null);
        	if (s == null) continue;
        	if (s != null && !s.equals(cat)) {
                cat = s;
                this.addEntry(new GuiVRControlsList.CategoryEntry(cat));
            }
            this.addEntry(new GuiVRControlsList.MappingEntry(kb, null));
        }     
    }
    
    private boolean checkMappingConflict(VRButtonMapping mapping) {
    	for (VRButtonMapping vb : mc.vrSettings.buttonMappings.values()) {
    		if (vb == mapping) continue;
    		if (vb.isGUIBinding() != mapping.isGUIBinding()) continue;
    		for (ButtonTuple button : vb.buttons) {
    			if (button.controller.getController().isButtonActive(button.button)) {
	    			for (ButtonTuple button2 : mapping.buttons) {
	    				if (button.equals(button2)) return true;
	    			}
    			}
    		}
    	}
    	return false;
    }

    public class CategoryEntry extends GuiListExtended.IGuiListEntry
    {
        private final String labelText;
        private final int labelWidth;

        public CategoryEntry(String p_i45028_2_)
        {
            this.labelText = I18n.format(p_i45028_2_, new Object[0]);
            this.labelWidth = GuiVRControlsList.this.mc.fontRenderer.getStringWidth(this.labelText);
        }

		@Override
        public void drawEntry(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean p_194999_5_, float partialTicks)
        {
            mc.fontRenderer.drawString(this.labelText, GuiVRControlsList.this.mc.currentScreen.width / 2 - this.labelWidth / 2, this.getY() + entryHeight - GuiVRControlsList.this.mc.fontRenderer.FONT_HEIGHT - 1, 16777215);
        }

    }

    public class MappingEntry extends GuiListExtended.IGuiListEntry
    {
        private final VRButtonMapping myKey;
        private final GuiButton btnChangeKeyBinding;
        private final GuiButton btnChangeKeyBindingList;
        private final GuiButton btnDeleteKeyBinding;
        private GuiTextField guiEnterText;
        private GuiVRControls parentScreen;
        
        private MappingEntry(VRButtonMapping key, GuiVRControls parent)
        {
            this.myKey = key;
            this.btnChangeKeyBinding = new GuiButton(0, 0, 0, 120, 18, "") {};
            this.btnChangeKeyBindingList = new GuiButton(0, 0, 0, 18, 18, "M") {};
            this.btnDeleteKeyBinding = new GuiButton(0, 0, 0, 18, 18, TextFormatting.RED + "X") {};
            this.parentScreen = parent;
            updateButtonText();
        }
        
        private void updateButtonText() {
            String str = "";
            for (ButtonTuple tuple : myKey.buttons) {
            	if (tuple.controller.getController().isButtonActive(tuple.button)) {
            		if (!str.isEmpty()) {
            			str = "Multiple";
            			break;
            		}
            		str = tuple.toReadableString();
            	}
            }
            if (str.isEmpty()) str = "None";
            else str = str.substring(0, Math.min(18, str.length()));

            if (parent.pressMode && parent.mapping == myKey) {
            	this.btnChangeKeyBinding.displayString = "> " + TextFormatting.YELLOW + str + TextFormatting.RESET + " <";
            } else if (!str.equals("None") && checkMappingConflict(myKey)) {
            	this.btnChangeKeyBinding.displayString = TextFormatting.RED + str;
            } else {
            	this.btnChangeKeyBinding.displayString = str;
            }
        }
        
		@Override
        public void drawEntry(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean p_194999_5_, float partialTicks)
        {
        	GuiVRControlsList.this.mc.fontRenderer.drawString(I18n.format(this.myKey.functionId), getX() + 40, getY() + entryHeight / 2 - GuiVRControlsList.this.mc.fontRenderer.FONT_HEIGHT / 2, 16777215);
        	this.btnChangeKeyBinding.x = GuiVRControlsList.this.mc.currentScreen.width / 2;
        	this.btnChangeKeyBinding.y = getY();
        	updateButtonText();
        
        	boolean var10 = GuiVRControlsList.this.parent.mapping == myKey;
        	this.btnChangeKeyBinding.render(mouseX, mouseY, partialTicks);
        	        	
        	this.btnChangeKeyBindingList.x = GuiVRControlsList.this.mc.currentScreen.width / 2 + 122;
        	this.btnChangeKeyBindingList.y = getY();
        	this.btnChangeKeyBindingList.render(mouseX, mouseY, partialTicks);

        	if (myKey.functionDesc.startsWith("keyboard ")) {
	        	this.btnDeleteKeyBinding.x = GuiVRControlsList.this.mc.currentScreen.width / 2 + 122 + 18 + 2;
	        	this.btnDeleteKeyBinding.y = getY();
	        	this.btnDeleteKeyBinding.render(mouseX, mouseY, partialTicks);
        	}
        }
        
		@Override
		public boolean mouseClicked(double mouseX, double mouseY, int button) {
		   	if (this.btnChangeKeyBinding.mouseClicked(mouseX, mouseY, button))
        	{
        		if (!parent.pressMode) {
        			parent.pressMode = true;
                	parent.mapping = myKey;   
                	parent.mappingButtons = new HashSet<>(myKey.buttons);
                	return true;
        		} else if (parent.mapping == myKey) {
    				parent.pressMode = false;
        			parent.mapping = null;
        			parent.mappingButtons = null;
        			return true;
        		}
        		return false;
        	}
        	else if (this.btnChangeKeyBindingList.mouseClicked(mouseX, mouseY, button))
            {           	
        		if (parent.pressMode) return false;
            	parent.selectionMode = true;
            	parent.mapping = myKey;   
            	parent.mappingButtons = new HashSet<>(myKey.buttons);
            	return true;          
            }
            else if (this.btnDeleteKeyBinding.mouseClicked(mouseX, mouseY, button))
            {           	
            	if (parent.pressMode) return false;
            	GuiVRControlsList.this.mc.vrSettings.buttonMappings.remove(myKey.functionId);
            	GuiVRControlsList.this.buildList();
            	return true;          
            }
            else
            {
                return false;
            }
		}
		


//        @Override
//        public boolean mouseReleased(double mouseX, double mouseY, int button) {
//            this.btnChangeKeyBinding.mouseReleased(mouseX, mouseY, button);
//            this.btnDeleteKeyBinding.mouseReleased(mouseX, mouseY, button);
//        }
        

    }
}
