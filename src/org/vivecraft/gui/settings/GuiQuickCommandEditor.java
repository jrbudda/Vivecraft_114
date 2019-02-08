/**
 * Copyright 2013 Mark Browning, StellaArtois
 * Licensed under the LGPL 3.0 or later (See LICENSE.md for details)
 */
package org.vivecraft.gui.settings;

import org.vivecraft.gui.framework.GuiVROptionsBase;

import net.minecraft.client.gui.GuiScreen;


public class GuiQuickCommandEditor extends GuiVROptionsBase {

	private GuiQuickCommandsList guiList;
	
	public GuiQuickCommandEditor(GuiScreen par1GuiScreen) {
		super(par1GuiScreen);
	}

	@Override
    public void initGui() {
        title = "Quick Commands";
    	this.guiList = new GuiQuickCommandsList(this, mc);
    	super.initGui();
    	super.addDefaultButtons();
    	this.visibleList = guiList;
    }
}
