package org.vivecraft.gui.framework;

import org.vivecraft.control.ControllerType;
import org.vivecraft.provider.MCOpenVR;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;

public abstract class TwoHandedGuiScreen extends GuiScreen
{
	public float cursorX1, cursorY1;
	public float cursorX2, cursorY2;
	private int lastHoveredButtonId1 = -1, lastHoveredButtonId2 = -1;
	protected boolean reinit;
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) 
	{
		if(super.mouseClicked(mouseX, mouseY, mouseButton))
		{	
			double d0 = Math.min(Math.max((int)cursorX2, 0), mc.mainWindow.getWidth())
					 * (double)mc.mainWindow.getScaledWidth() / (double)mc.mainWindow.getWidth();
			if( (int)d0 == mouseX)
				MCOpenVR.triggerHapticPulse(ControllerType.RIGHT, 2000);
			else 
				MCOpenVR.triggerHapticPulse(ControllerType.LEFT, 2000);
			return true;
		}
		return false;
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		
        if (reinit)
        {
            initGui();
            reinit = false;
        }
		       
        double mX1 = (cursorX1 * this.width / this.mc.mainWindow.getScaledWidth())
        		 * (double)mc.mainWindow.getScaledWidth() / (double)mc.mainWindow.getWidth();
        double mY1 = (cursorY1 * this.height / this.mc.mainWindow.getScaledHeight())
        		 * (double)mc.mainWindow.getScaledWidth() / (double)mc.mainWindow.getWidth();
        double mX2 = (cursorX2 * this.width / this.mc.mainWindow.getScaledWidth())
        		 * (double)mc.mainWindow.getScaledWidth() / (double)mc.mainWindow.getWidth();
        double mY2 = (cursorY2 * this.height / this.mc.mainWindow.getScaledHeight())
        		 * (double)mc.mainWindow.getScaledWidth() / (double)mc.mainWindow.getWidth();

        int hoveredButtonId1 = -1, hoveredButtonId2 = -1;
        for (int i = 0; i < this.buttons.size(); ++i)
        {
        	GuiButton butt = (GuiButton)this.buttons.get(i);
        	boolean buttonhovered1 = mX1 >= butt.x && mY1 >= butt.y && mX1 < butt.x + butt.getWidth() && mY1 < butt.y + 20;
        	boolean buttonhovered2 = mX2 >= butt.x && mY2 >= butt.y && mX2 < butt.x + butt.getWidth() && mY2 < butt.y + 20;
        	if(buttonhovered1)
        		butt.render((int)mX1, (int)mY1, partialTicks);
        	else
        		butt.render((int)mX2, (int)mY2, partialTicks);
        	
        	if (buttonhovered1)
        		hoveredButtonId1 = butt.id;
        	if (buttonhovered2)
        		hoveredButtonId2 = butt.id;
        }

        if (hoveredButtonId1 == -1) {
        	lastHoveredButtonId1 = -1;
        } else if (lastHoveredButtonId1 != hoveredButtonId1) {
			MCOpenVR.triggerHapticPulse(ControllerType.LEFT, 300);
    		lastHoveredButtonId1 = hoveredButtonId1;
    	}
        if (hoveredButtonId2 == -1) {
        	lastHoveredButtonId2 = -1;
        } else if (lastHoveredButtonId2 != hoveredButtonId2) {
			MCOpenVR.triggerHapticPulse(ControllerType.RIGHT, 300);
    		lastHoveredButtonId2 = hoveredButtonId2;
    	}

        for (int j = 0; j < this.labels.size(); ++j)
        {
            ((GuiLabel)this.labels.get(j)).render(mouseX, mouseY, partialTicks);
        }
        
    	this.mc.ingameGUI.drawMouseMenuQuad((int)mX1, (int)mY1);
    	this.mc.ingameGUI.drawMouseMenuQuad((int)mX2, (int)mY2);

	}
}
