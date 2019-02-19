package org.vivecraft.gui;

import org.lwjgl.glfw.GLFW;
import org.vivecraft.gui.framework.TwoHandedGuiScreen;
import org.vivecraft.utils.InputSimulator;

import net.minecraft.client.gui.GuiButton;

public class GuiKeyboard extends TwoHandedGuiScreen
{

	private boolean isShift = false;

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui()
	{
		String arr = mc.vrSettings.keyboardKeys;
		String alt = mc.vrSettings.keyboardKeysShift;

		this.buttons.clear();
		this.eventListeners.clear();
		//this.buttons.add(new GuiSmallButtonEx(301, this.width / 2 - 78, this.height / 6 - 14, "Hide Hud (F1): " + mc.gameSettings.hideGUI));

		if(this.isShift)
			arr = alt;

		int cols = 13;
		int rows = 4;
		int margin = 32;
		int spacing = 2;
		int bwidth = 25;
		double tmp = (double)arr.length() / (double)cols;
		
		if (Math.floor(tmp) == tmp)
			rows = (int) tmp;
		else
			rows = (int) (tmp+1);	
		
		for (int r=0; r<rows;r++) {
			for (int i=0; i<cols;i++) {
				int c = r*cols+i;
				char x = 32;
				if (c<arr.length()) {
					x = arr.charAt(c);
				}	

				final String c1 = String.valueOf(x);

				GuiButton butt = new GuiButton(c, margin + i*(bwidth+spacing), margin + r*(20+spacing), bwidth, 20, c1) {
					@Override
					public void onClick(double mouseX, double mouseY) {
						InputSimulator.typeChars(c1);
					}
				};
				this.addButton(butt);
			}
		}
			
	
		this.addButton(new GuiButton(201, 0, margin + 3* (20 + spacing), 30, 20, "Shift") {
			public void onClick(double mouseX, double mouseY) {
				setShift(!GuiKeyboard.this.isShift);
			}; 
		});
		
		this.addButton(new GuiButton(199, margin + 4 * (bwidth+spacing), margin + rows * (20+spacing), 5 * (bwidth+spacing), 20, " ") {
			public void onClick(double mouseX, double mouseY) {
				InputSimulator.typeChars(" ");
			}; 
		});
		
		this.addButton(new GuiButton(202, cols * (bwidth+spacing) + margin, margin , 35 , 20, "BKSP") {
			@Override
			public void onClick(double mouseX, double mouseY) {
				mc.keyboardListener.onKeyEvent(mc.mainWindow.getHandle(), GLFW.GLFW_KEY_BACKSPACE, 0, GLFW.GLFW_PRESS, 0);
				mc.keyboardListener.onKeyEvent(mc.mainWindow.getHandle(), GLFW.GLFW_KEY_BACKSPACE, 0, GLFW.GLFW_RELEASE, 0);
			}
		});
		this.addButton(new GuiButton(203, cols * (bwidth+spacing) + margin, margin + 2*(20 + spacing) , 35 , 20, "ENTER") {
			@Override
			public void onClick(double mouseX, double mouseY) {
				mc.keyboardListener.onKeyEvent(mc.mainWindow.getHandle(), GLFW.GLFW_KEY_ENTER, 0, GLFW.GLFW_PRESS, 0);
				mc.keyboardListener.onKeyEvent(mc.mainWindow.getHandle(), GLFW.GLFW_KEY_ENTER, 0, GLFW.GLFW_RELEASE, 0);
			}
		});
		this.addButton(new GuiButton(204, 0, margin + (20 + spacing), 30, 20, "TAB") {
			public void onClick(double mouseX, double mouseY) {
				mc.keyboardListener.onKeyEvent(mc.mainWindow.getHandle(), GLFW.GLFW_KEY_TAB, 0, GLFW.GLFW_PRESS, 0);
				mc.keyboardListener.onKeyEvent(mc.mainWindow.getHandle(), GLFW.GLFW_KEY_TAB, 0, GLFW.GLFW_RELEASE, 0);
			}	
		});
		this.addButton(new GuiButton(205, 0, margin, 30, 20, "ESC") {
			public void onClick(double mouseX, double mouseY) {
				mc.keyboardListener.onKeyEvent(mc.mainWindow.getHandle(), GLFW.GLFW_KEY_ESCAPE, 0, GLFW.GLFW_PRESS, 0);
				mc.keyboardListener.onKeyEvent(mc.mainWindow.getHandle(), GLFW.GLFW_KEY_ESCAPE, 0, GLFW.GLFW_RELEASE, 0);
			}	
		});
	}

	public void setShift(boolean shift) {
		if(shift != this.isShift) {
			this.isShift = shift;
			this.reinit = true;
		}
	}
	
    /**
     * Draws the screen and all the components in it.
     */
    public void render(int mouseX, int mouseY, float partialTicks)
    {
    	this.drawDefaultBackground();
    	this.drawCenteredString(this.fontRenderer, "Keyboard", this.width / 2, 2, 16777215);
    	super.render(0, 0, partialTicks);

    }    

}
