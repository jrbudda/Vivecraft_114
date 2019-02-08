/**
 * Copyright 2013 Mark Browning, StellaArtois
 * Licensed under the LGPL 3.0 or later (See LICENSE.md for details)
 */
package org.vivecraft.gui.settings;

import java.awt.Color;

import org.vivecraft.gui.framework.GuiVROptionsBase;
import org.vivecraft.settings.VRSettings;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiRenderOpticsSettings  extends GuiVROptionsBase
{
    static VRSettings.VrOptions[] monoDisplayOptions = new VRSettings.VrOptions[] {
            VRSettings.VrOptions.MONO_FOV,
            VRSettings.VrOptions.DUMMY,
            VRSettings.VrOptions.FSAA,
    };

    static VRSettings.VrOptions[] openVRDisplayOptions = new VRSettings.VrOptions[] {
            VRSettings.VrOptions.RENDER_SCALEFACTOR,
            VRSettings.VrOptions.MIRROR_DISPLAY,     
            VRSettings.VrOptions.FSAA,
            VRSettings.VrOptions.STENCIL_ON,
            VRSettings.VrOptions.DUMMY,
    };
    
    static VRSettings.VrOptions[] MROptions = new VRSettings.VrOptions[] {
            VRSettings.VrOptions.MIXED_REALITY_UNITY_LIKE,
            VRSettings.VrOptions.MIXED_REALITY_RENDER_HANDS,
            VRSettings.VrOptions.MIXED_REALITY_KEY_COLOR,
            VRSettings.VrOptions.MIXED_REALITY_FOV,
            VRSettings.VrOptions.MIXED_REALITY_UNDISTORTED,
            VRSettings.VrOptions.MONO_FOV,
            VRSettings.VrOptions.MIXED_REALITY_ALPHA_MASK,
    };
    
    static VRSettings.VrOptions[] UDOptions = new VRSettings.VrOptions[] {
            VRSettings.VrOptions.MONO_FOV,
    };
    
    static VRSettings.VrOptions[] TUDOptions = new VRSettings.VrOptions[] {
            VRSettings.VrOptions.MIXED_REALITY_FOV,
    };

    public GuiRenderOpticsSettings(GuiScreen par1GuiScreen)
    {
    	super( par1GuiScreen);
    }

    @Override
    public void initGui()
    {
        title = "Stereo Renderer Settings";
    	super.initGui(openVRDisplayOptions, true);
    	if(mc.vrSettings.displayMirrorMode == settings.MIRROR_MIXED_REALITY){
//    		GuiSmallButtonEx mr = new GuiSmallButtonEx(0, this.width / 2 - 68, this.height / 6 + 65, "Mixed Reality Options");
//    		mr.enabled = false;
//    		this.buttons.add(mr);
    		VRSettings.VrOptions[] buttons = new VRSettings.VrOptions[MROptions.length];
    		System.arraycopy(MROptions, 0, buttons, 0, MROptions.length);
    		for (int i = 0; i < buttons.length; i++) {
    			VRSettings.VrOptions option = buttons[i];
    			if (option == VRSettings.VrOptions.MONO_FOV && (!mc.vrSettings.mixedRealityMRPlusUndistorted || !mc.vrSettings.mixedRealityUnityLike))
    				buttons[i] = VRSettings.VrOptions.DUMMY;
    			if (option == VRSettings.VrOptions.MIXED_REALITY_ALPHA_MASK && !mc.vrSettings.mixedRealityUnityLike)
    				buttons[i] = VRSettings.VrOptions.DUMMY;
    			if (option == VRSettings.VrOptions.MIXED_REALITY_UNDISTORTED && !mc.vrSettings.mixedRealityUnityLike)
    				buttons[i] = VRSettings.VrOptions.DUMMY;
    			if (option == VRSettings.VrOptions.MIXED_REALITY_KEY_COLOR && mc.vrSettings.mixedRealityAlphaMask && mc.vrSettings.mixedRealityUnityLike)
    				buttons[i] = VRSettings.VrOptions.DUMMY;
    		} 		
    		super.initGui(buttons, false);
    	}else if(mc.vrSettings.displayMirrorMode == settings.MIRROR_FIRST_PERSON ){
    		super.initGui(UDOptions, false);
    	}else if( mc.vrSettings.displayMirrorMode == settings.MIRROR_THIRD_PERSON){
    		super.initGui(TUDOptions, false);
    	}
    	super.addDefaultButtons();
    }
    
    @Override
    protected void loadDefaults() {
    	this.settings.renderScaleFactor = 1.0f;
    	this.settings.displayMirrorMode = VRSettings.MIRROR_ON_DUAL;
    	this.settings.mixedRealityKeyColor = new Color(0, 0, 0);
    	this.settings.mixedRealityRenderHands = false;
    	this.settings.insideBlockSolidColor = false;
    	this.settings.mixedRealityUnityLike = true;
    	this.settings.mixedRealityMRPlusUndistorted = true;
    	this.settings.mixedRealityAlphaMask = false;
    	this.settings.mixedRealityFov = 40;
    	this.mc.gameSettings.fovSetting = 70f;
    	this.settings.useFsaa = true;
    	this.settings.vrUseStencil = true;
        this.mc.stereoProvider.reinitFrameBuffers("Defaults Loaded");
    }
    
    @Override
    protected void actionPerformed(GuiButton button) {
    	if (button.id == VRSettings.VrOptions.MIRROR_DISPLAY.ordinal() ||
        		button.id == VRSettings.VrOptions.FSAA.ordinal() || 
        		button.id == VRSettings.VrOptions.RENDER_SCALEFACTOR.ordinal())
        	{
                this.mc.stereoProvider.reinitFrameBuffers("Render Setting Changed");
        	}
    }
}

