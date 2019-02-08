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
	
//	@Override
//	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) 
//	{
//		if (mouseButton == 0)
//		{
//			for (int i = 0; i < this.buttons.size(); ++i)
//			{
//				GuiButton guibutton = this.buttons.get(i);
//
//				if (guibutton.mouseClicked( mouseX, mouseY, mouseButton))
//				{
//					//this.selectedButton = guibutton;
//				
//					if((int)((int)cursorX2 * this.width / this.mc.displayWidth) == mouseX)
//						MCOpenVR.triggerHapticPulse(ControllerType.RIGHT, 2000);
//					else 
//						MCOpenVR.triggerHapticPulse(ControllerType.LEFT, 2000);
//
//					//this.actionPerformed(guibutton);
//				}
//			}
//		}
//	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		
        if (reinit)
        {
            initGui();
            reinit = false;
        }
		
        int mX1 = (int) (cursorX1 * this.width / this.mc.mainWindow.getScaledWidth());
        int mY1 = (int) (cursorY1 * this.height / this.mc.mainWindow.getScaledHeight());
        int mX2 = (int) (cursorX2 * this.width / this.mc.mainWindow.getScaledWidth());
        int mY2 = (int) (cursorY2 * this.height / this.mc.mainWindow.getScaledHeight());

        int hoveredButtonId1 = -1, hoveredButtonId2 = -1;
        for (int i = 0; i < this.buttons.size(); ++i)
        {
        	GuiButton butt = (GuiButton)this.buttons.get(i);
        	boolean buttonhovered1 = mX1 >= butt.x && mY1 >= butt.y && mX1 < butt.x + butt.getWidth() && mY1 < butt.y + 20;
        	boolean buttonhovered2 = mX2 >= butt.x && mY2 >= butt.y && mX2 < butt.x + butt.getWidth() && mY2 < butt.y + 20;
        	if(buttonhovered1)
        		butt.render(mX1, mY1, partialTicks);
        	else
        		butt.render(mX2, mY2, partialTicks);
        	
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
        
    	this.mc.ingameGUI.drawMouseMenuQuad(mX1, mY1);
    	this.mc.ingameGUI.drawMouseMenuQuad(mX2, mY2);

	}
}
