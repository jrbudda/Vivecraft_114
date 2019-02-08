package org.vivecraft.gui.settings;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiTextField;

public class GuiQuickCommandsList extends GuiListExtended
{
    private final GuiQuickCommandEditor parent;
    private final Minecraft mc;
        
    public GuiQuickCommandsList(GuiQuickCommandEditor parent, Minecraft mc)
    {
        super(mc, parent.width, parent.height, 24, parent.height - 24, 20);
        this.parent = parent;
        this.mc = mc;
        
        String[] commands = mc.vrSettings.vrQuickCommands;
               
        String var5 = null;
        int var4 = 0;
        int var7 = commands.length;
        for (int i = 0; i < var7; i++)
        {
        	String kb = commands[i];
            int width = mc.fontRenderer.getStringWidth(kb);
            this.addEntry(new GuiQuickCommandsList.CommandEntry(kb, this));
        }
    }
        
    public class CommandEntry extends GuiListExtended.IGuiListEntry
    {
        private final GuiButton btnDelete;
        public final GuiTextField txt;
        
        private CommandEntry(String command, GuiQuickCommandsList parent)
        {
            this.btnDelete = new GuiButton(0, 0, 0, 48, 18, "X") {
            	@Override
            	public void onClick(double mouseX, double mouseY) {
                	CommandEntry.this.txt.setText("");
                	CommandEntry.this.txt.setFocused(true); 
            	}
            };          
            txt = new GuiTextField(0,mc.fontRenderer, parent.width / 2 - 100, 60, 200, 20);
            txt.setText(command);
        }
               
        @Override
        public boolean keyPressed(int key, int action, int mods) {
        	if (txt.isFocused()) 
        		return txt.keyPressed(key, action, mods);
        	return super.keyPressed(key, action, mods);
        }
        
        @Override
		public void drawEntry(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean p_194999_5_,	float partialTicks)        {
        	txt.x = getX();
        	txt.y = getY();
        	
        	txt.drawTextField(mouseX, mouseY, partialTicks);
        	//GuiQuickCommandsList.this.mc.fontRenderer.drawString(command, x + 40  - GuiQuickCommandsList.this.maxListLabelWidth, y + p_148279_5_ / 2 - GuiQuickCommandsList.this.mc.fontRenderer.FONT_HEIGHT / 2, 16777215);

        	this.btnDelete.x = txt.x+140;
        	this.btnDelete.y= txt.y;
        	this.btnDelete.visible = true;
        	this.btnDelete.render(mouseX, mouseY, partialTicks);
        }
    }
}
