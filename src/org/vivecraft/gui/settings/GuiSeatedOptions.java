package org.vivecraft.gui.settings;

import org.vivecraft.gui.framework.GuiVROptionButton;
import org.vivecraft.gui.framework.GuiVROptionsBase;
import org.vivecraft.gui.framework.VROptionEntry;
import org.vivecraft.settings.VRSettings;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiSeatedOptions extends GuiVROptionsBase
{
	private VROptionEntry[] seatedOptions = new VROptionEntry[] {
			new VROptionEntry(VRSettings.VrOptions.X_SENSITIVITY),
			new VROptionEntry(VRSettings.VrOptions.Y_SENSITIVITY),
			new VROptionEntry(VRSettings.VrOptions.KEYHOLE),
			new VROptionEntry(VRSettings.VrOptions.SEATED_HUD_XHAIR),
			new VROptionEntry(VRSettings.VrOptions.WORLD_ROTATION_INCREMENT),
			new VROptionEntry(VRSettings.VrOptions.MOVE_MODE, (button, mousePos) -> {
				GuiSeatedOptions.this.reinit = true;
				return false;
			}, true)
	};


    static VRSettings.VrOptions[] teleportSettings = new VRSettings.VrOptions[]
    {
            VRSettings.VrOptions.LIMIT_TELEPORT,
            VRSettings.VrOptions.SIMULATE_FALLING,
    };
    
    static VRSettings.VrOptions[] limitedteleportSettings = new VRSettings.VrOptions[]
    {
            VRSettings.VrOptions.TELEPORT_UP_LIMIT,
            VRSettings.VrOptions.TELEPORT_DOWN_LIMIT,
            VRSettings.VrOptions.TELEPORT_HORIZ_LIMIT
    };
    
	static VRSettings.VrOptions[] freeMoveSettings = new VRSettings.VrOptions[]
			{
					VRSettings.VrOptions.SEATED_HMD,
					VRSettings.VrOptions.FOV_REDUCTION,
			};

	public GuiSeatedOptions(GuiScreen guiScreen) {
		super( guiScreen );
	}

	@Override
	public void initGui()
	{
		title = "Seated Settings";

		mc.vrSettings.vrFreeMove = mc.vrPlayer.getFreeMove();

		super.initGui(seatedOptions, true);

		if(mc.vrPlayer.getFreeMove())
			super.initGui(freeMoveSettings,false);
		else {
			super.initGui(teleportSettings,false);
			if (settings.vrLimitedSurvivalTeleport)
				super.initGui(limitedteleportSettings, false);
		}

		super.addDefaultButtons();
	}

    @Override
    protected void actionPerformed(GuiButton button) {
    	if(button.id == VRSettings.VrOptions.LIMIT_TELEPORT.ordinal() ||
    			button.id == VRSettings.VrOptions.MOVE_MODE.ordinal() ||
    			button.id == VRSettings.VrOptions.FREEMOVE_MODE.ordinal())
    		this.reinit = true;
    }

	@Override
	protected void loadDefaults() {
		VRSettings vrSettings=Minecraft.getMinecraft().vrSettings;
		vrSettings.keyholeX=15;
		vrSettings.xSensitivity=1;
		vrSettings.ySensitivity=1;
		vrSettings.vrFreeMove = true;
		vrSettings.useFOVReduction = false;
		vrSettings.seatedUseHMD = false;
		vrSettings.seatedHudAltMode = false;
		vrSettings.vrTeleportDownLimit = 4;
		vrSettings.vrTeleportUpLimit = 1;
		vrSettings.vrTeleportHorizLimit = 16;
		mc.vrPlayer.setFreeMove(true);
	}


}
