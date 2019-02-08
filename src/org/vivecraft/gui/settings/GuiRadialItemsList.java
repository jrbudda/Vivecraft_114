package org.vivecraft.gui.settings;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.TextFormatting;

import org.apache.commons.lang3.ArrayUtils;

public class GuiRadialItemsList extends GuiListExtended
{
    private final GuiRadialConfiguration parent;
    private final Minecraft mc;
    private GuiListExtended.IGuiListEntry[] listEntries;
    private int maxListLabelWidth = 0;
    
    public GuiRadialItemsList(GuiRadialConfiguration parent, Minecraft mc)
    {
        super(mc, parent.width, parent.height, 63, parent.height - 32, 20);
        this.parent = parent;
        this.mc = mc;
        this.maxListLabelWidth = 90;
        buildList();
    }
    
    public void buildList() {
        KeyBinding[] bindings = ArrayUtils.clone(mc.gameSettings.keyBindings);
        Arrays.sort(bindings);
        
        String cat = null;
        int var7 = bindings.length;
        for (int i = 0; i < var7; i++)
        {
        	KeyBinding kb = bindings[i];       	
        	String s = kb != null ? kb.getKeyCategory() : null;
        	if (s == null) continue;
        	if (s != null && !s.equals(cat)) {
                cat = s;
                this.addEntry(new GuiRadialItemsList.CategoryEntry(cat));
            }
        	this.addEntry(new GuiRadialItemsList.MappingEntry(kb, this.parent));
        }
        
    }
    

    public class CategoryEntry extends GuiListExtended.IGuiListEntry
    {
        private final String labelText;
        private final int labelWidth;

        public CategoryEntry(String p_i45028_2_)
        {
            this.labelText = I18n.format(p_i45028_2_, new Object[0]);
            this.labelWidth = GuiRadialItemsList.this.mc.fontRenderer.getStringWidth(this.labelText);
        }

		@Override
        public void drawEntry(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean p_194999_5_, float partialTicks)
        {
            mc.fontRenderer.drawString(this.labelText, GuiRadialItemsList.this.mc.currentScreen.width / 2 - this.labelWidth / 2, this.getY() + entryWidth - GuiRadialItemsList.this.mc.fontRenderer.FONT_HEIGHT - 1, 6777215);
        }
    }

    public class MappingEntry extends GuiListExtended.IGuiListEntry
    {
        private final KeyBinding myKey;
        private GuiRadialConfiguration parentScreen;
        
        private MappingEntry(KeyBinding key, GuiRadialConfiguration parent)
        {
            this.myKey = key;
            this.parentScreen = parent;
        }      
        
		@Override
        public void drawEntry(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean p_194999_5_, float partialTicks)
        {
            TextFormatting formatting = TextFormatting.WHITE;
            if(p_194999_5_) formatting = TextFormatting.GREEN;
			mc.fontRenderer.drawString(formatting + I18n.format(this.myKey.getKeyDescription()), mc.currentScreen.width / 2 - maxListLabelWidth / 2, this.getY() + entryHeight / 2 - GuiRadialItemsList.this.mc.fontRenderer.FONT_HEIGHT / 2, 16777215);
        }
		
		@Override
		public boolean mouseClicked(double mouseX, double mouseY, int button) {
	     	parentScreen.setKey(myKey);
        	return true;
		}
    }
}
