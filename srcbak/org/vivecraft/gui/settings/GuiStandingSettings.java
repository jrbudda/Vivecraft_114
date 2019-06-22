package org.vivecraft.gui.settings;

import java.util.ArrayList;
import java.util.Collections;

import org.vivecraft.gui.framework.GuiVROptionButton;
import org.vivecraft.gui.framework.GuiVROptionsBase;
import org.vivecraft.gui.framework.VROptionEntry;
import org.vivecraft.settings.VRSettings;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiStandingSettings extends GuiVROptionsBase
{
	private VROptionEntry[] locomotionSettings = new VROptionEntry[]
			{
					new VROptionEntry(VRSettings.VrOptions.REVERSE_HANDS),
					new VROptionEntry(VRSettings.VrOptions.WALK_UP_BLOCKS),
					new VROptionEntry(VRSettings.VrOptions.VEHICLE_ROTATION),
					new VROptionEntry(VRSettings.VrOptions.BCB_ON),
					new VROptionEntry(VRSettings.VrOptions.WALK_MULTIPLIER),
					new VROptionEntry(VRSettings.VrOptions.ALLOW_MODE_SWITCH),
					new VROptionEntry(VRSettings.VrOptions.WORLD_ROTATION_INCREMENT),
					new VROptionEntry(VRSettings.VrOptions.ALLOW_STANDING_ORIGIN_OFFSET),
					new VROptionEntry(VRSettings.VrOptions.MOVE_MODE, (button, mousePos) -> {
						GuiStandingSettings.this.reinit = true;
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
					VRSettings.VrOptions.FREEMOVE_MODE,
					VRSettings.VrOptions.INERTIA_FACTOR,
					VRSettings.VrOptions.MOVEMENT_MULTIPLIER,
					VRSettings.VrOptions.FOV_REDUCTION,
			};
	static VRSettings.VrOptions[] freeMoveSettingsJP = new VRSettings.VrOptions[]
			{
					VRSettings.VrOptions.ANALOG_DEADZONE,
					VRSettings.VrOptions.FREEMOVE_WMR_STICK
			};
	static VRSettings.VrOptions[] freeMoveSettingsNJP = new VRSettings.VrOptions[]
			{
					VRSettings.VrOptions.ANALOG_DEADZONE,
					VRSettings.VrOptions.ANALOG_MOVEMENT
			};

	public GuiStandingSettings(GuiScreen guiScreen) {
		super(guiScreen);
	}

	@Override
	public void initGui()
	{
		title = "Standing Locomotion Settings";

		super.initGui(locomotionSettings, true);
		if(mc.vrPlayer.getFreeMove()){
			ArrayList<VRSettings.VrOptions> fm = new ArrayList<>();
			Collections.addAll(fm, freeMoveSettings);
			if(settings.vrFreeMoveMode == settings.FREEMOVE_JOYPAD) 
				Collections.addAll(fm, freeMoveSettingsJP);
			else if (settings.vrFreeMoveMode != settings.FREEMOVE_RUNINPLACE) 
				Collections.addAll(fm, freeMoveSettingsNJP);
			super.initGui(fm.toArray(new VRSettings.VrOptions[0]), false);
		}
		else {
			super.initGui(teleportSettings, false);
			if (settings.vrLimitedSurvivalTeleport)
				super.initGui(limitedteleportSettings, false);
		}

		mc.vrSettings.vrFreeMove = mc.vrPlayer.getFreeMove();

		super.addDefaultButtons();
	}

	@Override
	protected void loadDefaults() {
		VRSettings vr = mc.vrSettings;
		vr.inertiaFactor = VRSettings.INERTIA_NORMAL;
		vr.movementSpeedMultiplier = 1f;
		vr.simulateFalling = true;
		vr.vrAllowCrawling = false;
		vr.vrAllowLocoModeSwotch = true;
		vr.vrFreeMove = false;
		vr.vrLimitedSurvivalTeleport = true;
		vr.vrShowBlueCircleBuddy = true;
		vr.walkMultiplier=1;
		vr.vrFreeMoveMode = vr.FREEMOVE_CONTROLLER;
		vr.vehicleRotation = true;
		vr.useFOVReduction = false;
		vr.walkUpBlocks = true;
		vr.analogMovement = true;
		vr.vrTeleportDownLimit = 4;
		vr.vrTeleportUpLimit = 1;
		vr.vrTeleportHorizLimit = 16;    
		Minecraft.getMinecraft().gameSettings.viewBobbing = true;
		Minecraft.getMinecraft().gameSettings.saveOptions();
		Minecraft.getMinecraft().vrSettings.saveOptions();
		this.reinit = true;
	}


	@Override
	protected void actionPerformed(GuiButton button) {
		if(button.id == VRSettings.VrOptions.LIMIT_TELEPORT.ordinal() ||
				button.id == VRSettings.VrOptions.MOVE_MODE.ordinal() ||
				button.id == VRSettings.VrOptions.FREEMOVE_MODE.ordinal())
			this.reinit = true;
	}

}
