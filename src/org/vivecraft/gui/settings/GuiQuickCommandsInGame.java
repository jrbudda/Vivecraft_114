package org.vivecraft.gui.settings;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;

public class GuiQuickCommandsInGame extends GuiScreen
{
    private int field_146445_a;
    private int field_146444_f;
    private static final String __OBFID = "CL_00000703";
    
    @Override
    public void initGui()
    {
    	KeyBinding.unPressAllKeys();
    	this.field_146445_a = 0;
    	this.buttons.clear();
    	byte var1 = -16;
    	boolean var2 = true;

    	String[] chatcommands = mc.vrSettings.vrQuickCommands;

    	int w = 0;
    	for (int i = 0; i < chatcommands.length; i++) {
    		
    		w = i > 5 ? 1 : 0;
    		String com  = chatcommands[i];
    		this.addButton(new GuiButton(200 + i, this.width / 2 - 125 + 127 * w, 36 + (i-6*w) * 24, 125, 20, com.toString()) 
    		{
    			public void onClick(double mouseX, double mouseY) {
    				GuiQuickCommandsInGame.this.mc.displayGuiScreen(null);
    				GuiQuickCommandsInGame.this.mc.player.sendChatMessage(this.displayString);
    			};
    		});     

    	}
    	this.addButton(new GuiButton(102, this.width / 2 -50, this.height -30  + var1, 100, 20, "Cancel")
		{
			public void onClick(double mouseX, double mouseY) {
				GuiQuickCommandsInGame.this.mc.displayGuiScreen(new GuiIngameMenu());
			};
		});   
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, "Quick Commands", this.width / 2, 16, 16777215);
        super.render(mouseX, mouseY, partialTicks);
    }
}