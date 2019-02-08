package org.vivecraft.gui.settings;

import org.vivecraft.gui.framework.GuiVROptionsBase;
import org.vivecraft.settings.VRSettings;

import net.minecraft.client.gui.GuiScreen;

public class GuiOtherHUDSettings extends GuiVROptionsBase
{
    // VIVE START - hide options not supported by tracked controller UI
    static VRSettings.VrOptions[] hudOptions = new VRSettings.VrOptions[] {
            VRSettings.VrOptions.CROSSHAIR_SCALE,
            VRSettings.VrOptions.RENDER_CROSSHAIR_MODE,
            //VRSettings.VrOptions.CROSSHAIR_ROLL,
            VRSettings.VrOptions.RENDER_BLOCK_OUTLINE_MODE,
            VRSettings.VrOptions.MENU_CROSSHAIR_SCALE,
            VRSettings.VrOptions.CROSSHAIR_OCCLUSION,
            //VRSettings.VrOptions.MAX_CROSSHAIR_DISTANCE_AT_BLOCKREACH,
            VRSettings.VrOptions.CROSSHAIR_SCALES_WITH_DISTANCE,
            //VRSettings.VrOptions.CHAT_FADE_AWAY,
            VRSettings.VrOptions.DUMMY,
    };
    // VIVE END - hide options not supported by tracked controller UI

    public GuiOtherHUDSettings(GuiScreen guiScreen) {
        super( guiScreen );
    }

    @Override
    public void initGui()
    {
    	title = "Chat/Crosshair Settings";
    	super.initGui(hudOptions, true);  
    	super.addDefaultButtons();
    }

    @Override
    protected void loadDefaults() {
        this.settings.crosshairScale = 1.0f;
        this.settings.renderBlockOutlineMode = VRSettings.RENDER_BLOCK_OUTLINE_MODE_ALWAYS;
        this.settings.renderInGameCrosshairMode = VRSettings.RENDER_CROSSHAIR_MODE_ALWAYS;
        this.settings.menuCrosshairScale = 1f;
        this.settings.useCrosshairOcclusion = false;
        this.settings.crosshairScalesWithDistance = false;
    }
}
