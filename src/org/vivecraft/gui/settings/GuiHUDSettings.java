package org.vivecraft.gui.settings;

import java.util.function.BiFunction;

import org.vivecraft.gameplay.screenhandlers.GuiHandler;
import org.vivecraft.gameplay.screenhandlers.KeyboardHandler;
import org.vivecraft.gui.framework.GuiVROptionButton;
import org.vivecraft.gui.framework.GuiVROptionsBase;
import org.vivecraft.gui.framework.VROptionEntry;
import org.vivecraft.settings.VRSettings;
import org.vivecraft.settings.VRSettings.VrOptions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.math.Vec2f;
import org.apache.commons.lang3.ArrayUtils;

public class GuiHUDSettings extends GuiVROptionsBase
{
    static VROptionEntry[] hudOptions = new VROptionEntry[] {
            new VROptionEntry(VRSettings.VrOptions.HUD_HIDE),
            new VROptionEntry(VRSettings.VrOptions.HUD_LOCK_TO),
            new VROptionEntry(VRSettings.VrOptions.HUD_OCCLUSION),
            //new VROptionEntry(VRSettings.VrOptions.HUD_SCALE),
            //new VROptionEntry(VRSettings.VrOptions.HUD_DISTANCE),
            //new VROptionEntry(VRSettings.VrOptions.HUD_PITCH),
            //new VROptionEntry(VRSettings.VrOptions.HUD_YAW),
            new VROptionEntry(VRSettings.VrOptions.HUD_OPACITY),
            new VROptionEntry(VRSettings.VrOptions.RENDER_MENU_BACKGROUND),
            new VROptionEntry(VRSettings.VrOptions.TOUCH_HOTBAR),
            new VROptionEntry(VRSettings.VrOptions.MENU_ALWAYS_FOLLOW_FACE, (button, mousePos) -> {
                GuiHandler.onGuiScreenChanged(Minecraft.getMinecraft().currentScreen, Minecraft.getMinecraft().currentScreen, false);
                return false;
            }),
            new VROptionEntry(VRSettings.VrOptions.AUTO_OPEN_KEYBOARD),
			new VROptionEntry(VRSettings.VrOptions.PHYSICAL_KEYBOARD, (button, mousePos) -> {
				KeyboardHandler.setOverlayShowing(false);
				return false;
			}),
    };

    public GuiHUDSettings(GuiScreen guiScreen) {
        super(guiScreen);  
    }


    @Override
    public void initGui()
    {
    	title = "HUD and GUI Settings";

        VROptionEntry[] optionEntries = hudOptions;

		if(!mc.vrSettings.seated) {
            BiFunction<GuiVROptionButton, Vec2f, Boolean> func = (button, mousePos) -> {
				GuiHUDSettings.this.settings.saveOptions();
				GuiVRControls guiVRControls = new GuiVRControls(GuiHUDSettings.this);
				guiVRControls.guiFilter = true;
				GuiHUDSettings.this.mc.displayGuiScreen(guiVRControls);
				return false;
			};
            optionEntries = ArrayUtils.add(optionEntries, new VROptionEntry("GUI Buttons...", func));
		}

    	super.initGui(optionEntries, true);
        super.addDefaultButtons();
    }
    
    @Override
    protected void loadDefaults() {
        this.settings.hudDistance = 1.25f;
        this.settings.hudScale = 1.5f;
        this.settings.hudPitchOffset = -2f;
        this.settings.hudYawOffset = 0f;
        this.settings.hudOpacity = 1f;
        this.settings.menuBackground = false;
        this.settings.vrHudLockMode = settings.HUD_LOCK_HAND;
        this.settings.hudOcclusion = true;
        this.settings.menuAlwaysFollowFace = false;
        this.settings.autoOpenKeyboard = false;
        this.settings.physicalKeyboard = true;
        this.mc.gameSettings.hideGUI = false;
    }
}
