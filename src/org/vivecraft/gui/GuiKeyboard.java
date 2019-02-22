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
				InputSimulator.pressKey(GLFW.GLFW_KEY_BACKSPACE);
				InputSimulator.releaseKey(GLFW.GLFW_KEY_BACKSPACE);
			}
		});
		this.addButton(new GuiButton(203, cols * (bwidth+spacing) + margin, margin + 2*(20 + spacing) , 35 , 20, "ENTER") {
			@Override
			public void onClick(double mouseX, double mouseY) {
				InputSimulator.pressKey(GLFW.GLFW_KEY_ENTER);
				InputSimulator.releaseKey(GLFW.GLFW_KEY_ENTER);
			}
		});
		this.addButton(new GuiButton(204, 0, margin + (20 + spacing), 30, 20, "TAB") {
			public void onClick(double mouseX, double mouseY) {
				InputSimulator.pressKey(GLFW.GLFW_KEY_TAB);
				InputSimulator.releaseKey(GLFW.GLFW_KEY_TAB);
			}	
		});
		this.addButton(new GuiButton(205, 0, margin, 30, 20, "ESC") {
			public void onClick(double mouseX, double mouseY) {
				InputSimulator.pressKey(GLFW.GLFW_KEY_ESCAPE);
				InputSimulator.releaseKey(GLFW.GLFW_KEY_ESCAPE);
			}	
		});
		this.addButton(new GuiButton(206, (cols - 1) * (bwidth + spacing) + margin, margin + rows * (20 + spacing), bwidth, 20, "\u2191") {
			public void onClick(double mouseX, double mouseY) {
				InputSimulator.pressKey(GLFW.GLFW_KEY_UP);
				InputSimulator.releaseKey(GLFW.GLFW_KEY_UP);
			}
		});
		this.addButton(new GuiButton(207, (cols - 1) * (bwidth + spacing) + margin, margin + (rows + 1) * (20 + spacing), bwidth, 20, "\u2193") {
			public void onClick(double mouseX, double mouseY) {
				InputSimulator.pressKey(GLFW.GLFW_KEY_DOWN);
				InputSimulator.releaseKey(GLFW.GLFW_KEY_DOWN);
			}
		});
		this.addButton(new GuiButton(208, (cols - 2) * (bwidth + spacing) + margin, margin + (rows + 1) * (20 + spacing), bwidth, 20, "\u2190") {
			public void onClick(double mouseX, double mouseY) {
				InputSimulator.pressKey(GLFW.GLFW_KEY_LEFT);
				InputSimulator.releaseKey(GLFW.GLFW_KEY_LEFT);
			}
		});
		this.addButton(new GuiButton(209, cols * (bwidth + spacing) + margin, margin + (rows + 1) * (20 + spacing), bwidth, 20, "\u2192") {
			public void onClick(double mouseX, double mouseY) {
				InputSimulator.pressKey(GLFW.GLFW_KEY_RIGHT);
				InputSimulator.releaseKey(GLFW.GLFW_KEY_RIGHT);
			}
		});
		this.addButton(new GuiButton(210, margin, margin + -1 * (20 + spacing), 35, 20, "CUT") {
			public void onClick(double mouseX, double mouseY) {
				InputSimulator.pressKey(GLFW.GLFW_KEY_LEFT_CONTROL);
				InputSimulator.pressKey(GLFW.GLFW_KEY_X);
				InputSimulator.releaseKey(GLFW.GLFW_KEY_X);
				InputSimulator.releaseKey(GLFW.GLFW_KEY_LEFT_CONTROL);
			}
		});
		this.addButton(new GuiButton(211, 35 + spacing + margin, margin + -1 * (20 + spacing), 35, 20, "COPY") {
			public void onClick(double mouseX, double mouseY) {
				InputSimulator.pressKey(GLFW.GLFW_KEY_LEFT_CONTROL);
				InputSimulator.pressKey(GLFW.GLFW_KEY_C);
				InputSimulator.releaseKey(GLFW.GLFW_KEY_C);
				InputSimulator.releaseKey(GLFW.GLFW_KEY_LEFT_CONTROL);
			}
		});
		this.addButton(new GuiButton(212, 2 * (35 + spacing) + margin, margin + -1 * (20 + spacing), 35, 20, "PASTE") {
			public void onClick(double mouseX, double mouseY) {
				InputSimulator.pressKey(GLFW.GLFW_KEY_LEFT_CONTROL);
				InputSimulator.pressKey(GLFW.GLFW_KEY_V);
				InputSimulator.releaseKey(GLFW.GLFW_KEY_V);
				InputSimulator.releaseKey(GLFW.GLFW_KEY_LEFT_CONTROL);
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
