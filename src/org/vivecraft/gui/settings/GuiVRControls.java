/**
 * Copyright 2013 Mark Browning, StellaArtois
 * Licensed under the LGPL 3.0 or later (See LICENSE.md for details)
 */
package org.vivecraft.gui.settings;

import org.vivecraft.gui.framework.GuiVROptionsBase;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.TextFormatting;

public class GuiVRControls extends GuiVROptionsBase {
	public GuiVRControls(Screen par1GuiScreen) {
		super(par1GuiScreen);
	}
   
	@Override
    public void init() {
        vrTitle = "VR Control Remapping";
        super.addDefaultButtons();
    }

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		super.render(mouseX, mouseY, partialTicks);
		this.drawCenteredString(minecraft.fontRenderer, "Bindings are now handled by SteamVR Input.", this.width / 2, this.height / 2 - minecraft.fontRenderer.FONT_HEIGHT / 2 - 3, 16777215);
		this.drawCenteredString(minecraft.fontRenderer, "Go to Settings > Controller Binding in the dashboard.", this.width / 2, this.height / 2 + minecraft.fontRenderer.FONT_HEIGHT / 2 + 3, 16777215);
	}
}
