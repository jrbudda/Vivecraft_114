/**
 * Copyright 2013 Mark Browning, StellaArtois
 * Licensed under the LGPL 3.0 or later (See LICENSE.md for details)
 */
package org.vivecraft.settings;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import org.vivecraft.provider.MCOpenVR;
import org.vivecraft.settings.profile.ProfileManager;
import org.vivecraft.settings.profile.ProfileReader;
import org.vivecraft.settings.profile.ProfileWriter;
import org.vivecraft.utils.Quaternion;
import org.vivecraft.utils.Vector3;

import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.math.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class VRSettings
{
    public static final int VERSION = 2;
    public static final Logger logger = LogManager.getLogger();
	public static VRSettings inst;
	public JSONObject defaults = new JSONObject();
    public static final int UNKNOWN_VERSION = 0;
    public final String DEGREE  = "\u00b0";

    public static final int INERTIA_NONE = 0;
    public static final int INERTIA_NORMAL = 1;
    public static final int INERTIA_LARGE = 2;
    public static final int INERTIA_MASSIVE = 3;

    public static final int BOW_MODE_ON = 2;
    public static final int BOW_MODE_VANILLA = 1;
    public static final int BOW_MODE_OFF = 0;

    public static final float INERTIA_NONE_ADD_FACTOR = 1f / 0.01f;
    public static final float INERTIA_NORMAL_ADD_FACTOR = 1f;
    public static final float INERTIA_LARGE_ADD_FACTOR = 1f / 4f;
    public static final float INERTIA_MASSIVE_ADD_FACTOR = 1f / 16f;
    public static final int RENDER_FIRST_PERSON_FULL = 0;
    public static final int RENDER_FIRST_PERSON_HAND = 1;
    public static final int RENDER_FIRST_PERSON_NONE = 2;
    public static final int RENDER_CROSSHAIR_MODE_ALWAYS = 0;
    public static final int RENDER_CROSSHAIR_MODE_HUD = 1;
    public static final int RENDER_CROSSHAIR_MODE_NEVER = 2;
    public static final int RENDER_BLOCK_OUTLINE_MODE_ALWAYS = 0;
    public static final int RENDER_BLOCK_OUTLINE_MODE_HUD = 1;
    public static final int RENDER_BLOCK_OUTLINE_MODE_NEVER = 2;
  
    public static final int MIRROR_OFF = 10;
    public static final int MIRROR_ON_DUAL = 11;
    public static final int MIRROR_ON_SINGLE = 12;
    public static final int MIRROR_FIRST_PERSON = 13;
    public static final int MIRROR_THIRD_PERSON = 14;
    public static final int MIRROR_MIXED_REALITY = 15;
    
    public static final int HUD_LOCK_HEAD= 1;
    public static final int HUD_LOCK_HAND= 2;
    public static final int HUD_LOCK_WRIST= 3;
    public static final int HUD_LOCK_BODY= 4;

    public static final int FREEMOVE_CONTROLLER= 1;
    public static final int FREEMOVE_HMD= 2;
    public static final int FREEMOVE_RUNINPLACE= 3;
    @Deprecated
    public static final int FREEMOVE_JOYPAD = 4;

    public static final int MENU_WORLD_BOTH = 0;
    public static final int MENU_WORLD_CUSTOM = 1;
    public static final int MENU_WORLD_OFFICIAL = 2;
    
    public static final int NO_SHADER = -1;

    public int version = UNKNOWN_VERSION;

    public int renderFullFirstPersonModelMode = RENDER_FIRST_PERSON_HAND;   // VIVE - hand only by default
    public int shaderIndex = NO_SHADER;
    public String stereoProviderPluginID = "openvr";
    public String badStereoProviderPluginID = "";
    public boolean storeDebugAim = false;
    public int smoothRunTickCount = 20;
    public boolean smoothTick = false;
    //Jrbudda's Options

    public String[] vrQuickCommands;
    public String[] vrRadialItems;
    public String[] vrRadialItemsAlt;
    
    //Control
    public boolean vrReverseHands = false;
    public boolean vrReverseShootingEye = false;
    public float vrWorldScale = 1.0f;
    public float vrWorldRotation = 0f;
	public float vrWorldRotationCached;
    public float vrWorldRotationIncrement = 45f;
    public float xSensitivity=1f;
    public float ySensitivity=1f;
    public float keyholeX=15;
    public double headToHmdLength=0.10f;
    public float autoCalibration=-1;
    public float manualCalibration=-1;
    public float playerEyeHeight = 1.62f;
	public boolean alwaysSimulateKeyboard = false;
	public int bowMode = BOW_MODE_ON;
	public String keyboardKeys =  "`1234567890-=qwertyuiop[]\\asdfghjkl;\':\"zxcvbnm,./?<>";
	public String keyboardKeysShift ="~!@#$%^&*()_+QWERTYUIOP{}|ASDFGHJKL;\':\"ZXCVBNM,./?<>";
	public boolean firstRun = true;
	//

    //Locomotion
    public int inertiaFactor = INERTIA_NORMAL;
    public boolean walkUpBlocks = true;     // VIVE default to enable climbing
    public boolean simulateFalling = true;  // VIVE if HMD is over empty space, fall
    public boolean weaponCollision = true;  // VIVE weapon hand collides with blocks/enemies
    public float movementSpeedMultiplier = 1.0f;   // VIVE - use full speed by default
    public int vrFreeMoveMode = this.FREEMOVE_CONTROLLER;
    public boolean vrLimitedSurvivalTeleport = true;
   
    public int vrTeleportUpLimit = 1;
    public int vrTeleportDownLimit = 4;
    public int vrTeleportHorizLimit = 16;

    public boolean seated = false;
    public boolean seatedUseHMD = false;
    public float jumpThreshold=0.05f;
    public float sneakThreshold=0.4f;
    public boolean realisticJumpEnabled=true;
    public boolean realisticSneakEnabled=true;
    public boolean realisticClimbEnabled=true;
    public boolean realisticSwimEnabled=true;
    public boolean realisticRowEnabled=true;
    public boolean backpackSwitching = true;
    public boolean physicalGuiEnabled = false;
    public float walkMultiplier=1;
    public boolean vrAllowCrawling = false; //unused
    public boolean vrShowBlueCircleBuddy = true;
    public boolean vehicleRotation = true; 
    public boolean animaltouching = true;
    public boolean analogMovement = true;
    public boolean autoSprint = true;
    public float autoSprintThreshold = 0.9f;
    public boolean allowStandingOriginOffset = false;
    public boolean seatedFreeMove = false;
    public boolean forceStandingFreeMove = false;
    //
 
    //Rendering
    public boolean useFsaa = true;   // default to off
    public boolean useFOVReduction = false;   // default to off
    public boolean vrUseStencil = true;
    public boolean insideBlockSolidColor = false; //unused
    public float renderScaleFactor = 1.0f;
    public int displayMirrorMode = MIRROR_ON_DUAL;
	public boolean shouldRenderSelf=false;
	public boolean tmpRenderSelf;
	public int menuWorldSelection = MENU_WORLD_BOTH;
    //
    
    //Mixed Reality
    public Color mixedRealityKeyColor = new Color(0, 0, 0);
    public float mixedRealityAspectRatio = 16F / 9F;
    public boolean mixedRealityRenderHands = false;
    public boolean mixedRealityUnityLike = true;
    public boolean mixedRealityMRPlusUndistorted = true;
    public boolean mixedRealityAlphaMask = false;
    public float mixedRealityFov = 40;
    public float vrFixedCamposX = 0;
    public float vrFixedCamposY = 0;
    public float vrFixedCamposZ = 0;
    public Quaternion vrFixedCamrotQuat = new Quaternion();
    public float mrMovingCamOffsetX = 0;
    public float mrMovingCamOffsetY = 0;
    public float mrMovingCamOffsetZ = 0;
    public Quaternion mrMovingCamOffsetRotQuat = new Quaternion();
    //
    
    //HUD/GUI
    public boolean vrTouchHotbar = true;    
    public float hudScale = 1.0f;
    public float hudDistance = 1.25f;
    public float hudPitchOffset = -2f;
    public float hudYawOffset = 0.0f;
    public boolean floatInventory = true; //false not working yet, have to account for rotation and tilt in MCOpenVR>processGui()
	public boolean menuAlwaysFollowFace;
    public int vrHudLockMode = HUD_LOCK_HAND;
    public boolean hudOcclusion = true;
    public float crosshairScale = 1.0f;
	public boolean crosshairScalesWithDistance = false;
    public int renderInGameCrosshairMode = RENDER_CROSSHAIR_MODE_ALWAYS;
    public int renderBlockOutlineMode = RENDER_BLOCK_OUTLINE_MODE_ALWAYS;
    public float hudOpacity = 1f;
    public boolean menuBackground = false;
    public float   menuCrosshairScale = 1f;
    public boolean useCrosshairOcclusion = false;
	public boolean seatedHudAltMode = true;
	public boolean autoOpenKeyboard = false;
	public int forceHardwareDetection = 0; // 0 = off, 1 = vive, 2 = oculus
	public boolean radialModeHold = true;
	public boolean physicalKeyboard = true;
	public boolean allowAdvancedBindings = false;
    //
     	
    private Minecraft mc;


	
    public VRSettings( Minecraft minecraft, File dataDir )
    {
        // Assumes GameSettings (and hence optifine's settings) have been read first

    	mc = minecraft;
    	inst = this;

        // Store our class defaults to a member variable for later use
    	storeDefaults();

        // Legacy config files. Note that in general these files will be by-passed
        // by the Profile handling in ProfileManager. loadOptions and saveOptions ill
        // be redirected to the profile manager using ProfileReader and ProfileWriter
        // respectively.

        // Load settings from the file
        this.loadOptions();
    }

    public void loadOptions()
    {
        loadOptions(null);
    }

    public void loadDefaults()
    {
        loadOptions(this.defaults);
    }
    
    public void loadOptions(JSONObject theProfiles)
    {
        // Load Minecrift options
        try
        {
            ProfileReader optionsVRReader = new ProfileReader(ProfileManager.PROFILE_SET_VR, theProfiles);

            String var2 = "";
           
            while ((var2 = optionsVRReader.readLine()) != null)
            {
                try
                {
                    String[] optionTokens = var2.split(":");

                    if (optionTokens[0].equals("version"))
                    {
                        this.version = Integer.parseInt(optionTokens[1]);
                    }

//                    if (optionTokens[0].equals("firstLoad"))
//                    {
//                        this.firstLoad = optionTokens[1].equals("true");
//                    }

                    if (optionTokens[0].equals("stereoProviderPluginID"))
                    {
                        this.stereoProviderPluginID = optionTokens[1];
                    }

                    if (optionTokens[0].equals("badStereoProviderPluginID"))
                    {
                        if (optionTokens.length > 1) {  // Trap if no entry
                            this.badStereoProviderPluginID = optionTokens[1];
                        }
                    }

                    if (optionTokens[0].equals("hudOpacity"))
                    {
                        this.hudOpacity = this.parseFloat(optionTokens[1]);
                        if(hudOpacity< 0.15f)
                        	hudOpacity = 1.0f;
                    }
                    if (optionTokens[0].equals("menuBackground"))
                    {
                        this.menuBackground = optionTokens[1].equals("true");
                    }

                    if (optionTokens[0].equals("renderFullFirstPersonModelMode"))
                    {
                        this.renderFullFirstPersonModelMode = Integer.parseInt(optionTokens[1]);
                    }

                    if (optionTokens[0].equals("shaderIndex"))
                    {
                        this.shaderIndex = Integer.parseInt(optionTokens[1]);
                    }

                    if (optionTokens[0].equals("walkUpBlocks"))
                    {
                        this.walkUpBlocks = optionTokens[1].equals("true");
                    }

                    if (optionTokens[0].equals("displayMirrorMode"))
                    {
                        this.displayMirrorMode = Integer.parseInt(optionTokens[1]);
                    }

                    if (optionTokens[0].equals("mixedRealityKeyColor"))
                    {
                        String[] split = optionTokens[1].split(",");
                        this.mixedRealityKeyColor = new Color(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
                    }

                    if (optionTokens[0].equals("mixedRealityRenderHands"))
                    {
                        this.mixedRealityRenderHands = optionTokens[1].equals("true");
                    }

                    if (optionTokens[0].equals("mixedRealityUnityLike"))
                    {
                        this.mixedRealityUnityLike = optionTokens[1].equals("true");
                    }

                    if (optionTokens[0].equals("mixedRealityUndistorted"))
                    {
                        this.mixedRealityMRPlusUndistorted = optionTokens[1].equals("true");
                    }

                    if (optionTokens[0].equals("mixedRealityAlphaMask"))
                    {
                        this.mixedRealityAlphaMask = optionTokens[1].equals("true");
                    }
                    
                    if (optionTokens[0].equals("mixedRealityFov"))
                    {
                        this.mixedRealityFov = this.parseFloat(optionTokens[1]);
                    }

                    if (optionTokens[0].equals("insideBlockSolidColor"))
                    {
                        this.insideBlockSolidColor = optionTokens[1].equals("true");
                    }

                    if (optionTokens[0].equals("headHudScale"))
                    {
                        this.hudScale = this.parseFloat(optionTokens[1]);
                    }

                    if (optionTokens[0].equals("renderScaleFactor"))
                    {
                        this.renderScaleFactor = this.parseFloat(optionTokens[1]);
                    }

                    if (optionTokens[0].equals("vrHudLockMode"))
                    {
                        this.vrHudLockMode =  Integer.parseInt(optionTokens[1]);
                        if(this.vrHudLockMode == HUD_LOCK_BODY)
                        	this.vrHudLockMode = HUD_LOCK_HAND;
                    }

                    if (optionTokens[0].equals("hudDistance"))
                    {
                        this.hudDistance = this.parseFloat(optionTokens[1]);
                    }

                    if (optionTokens[0].equals("hudPitchOffset"))
                    {
                        this.hudPitchOffset = this.parseFloat(optionTokens[1]);
                    }

                    if (optionTokens[0].equals("hudYawOffset"))
                    {
                        this.hudYawOffset = this.parseFloat(optionTokens[1]);
                    }

                    if (optionTokens[0].equals("useFsaa"))
                    {
                        this.useFsaa = optionTokens[1].equals("true");
                    }

                    if (optionTokens[0].equals("movementSpeedMultiplier"))
                    {
                        this.movementSpeedMultiplier = this.parseFloat(optionTokens[1]);
                    }

                    if (optionTokens[0].equals("renderInGameCrosshairMode"))
                    {
                        this.renderInGameCrosshairMode = Integer.parseInt(optionTokens[1]);
                    }
                    
                    if (optionTokens[0].equals("crosshairScalesWithDistance"))
                    {
                    	 this.crosshairScalesWithDistance = optionTokens[1].equals("true");
                    }

                    if (optionTokens[0].equals("renderBlockOutlineMode"))
                    {
                        this.renderBlockOutlineMode = Integer.parseInt(optionTokens[1]);
                    }

                    if (optionTokens[0].equals("crosshairScale"))
                    {
                        this.crosshairScale = this.parseFloat(optionTokens[1]);
                    }

                    if (optionTokens[0].equals("menuCrosshairScale"))
                    {
                        this.menuCrosshairScale = this.parseFloat(optionTokens[1]);
                    }

                    if (optionTokens[0].equals("renderInGameCrosshairMode"))
                    {
                        this.renderInGameCrosshairMode = Integer.parseInt(optionTokens[1]);
                    }

                    if (optionTokens[0].equals("renderBlockOutlineMode"))
                    {
                        this.renderBlockOutlineMode = Integer.parseInt(optionTokens[1]);
                    }

                    if (optionTokens[0].equals("hudOcclusion"))
                    {
                        this.hudOcclusion = optionTokens[1].equals("true");
                    }

                    if (optionTokens[0].equals("menuAlwaysFollowFace"))
                    {
                        this.menuAlwaysFollowFace = optionTokens[1].equals("true");
                    }

                    if (optionTokens[0].equals("useCrosshairOcclusion"))
                    {
                        this.useCrosshairOcclusion = optionTokens[1].equals("true");
                    }

                    if (optionTokens[0].equals("inertiaFactor"))
                    {
                        this.inertiaFactor = Integer.parseInt(optionTokens[1]);
                    }

                    if (optionTokens[0].equals("smoothRunTickCount"))
                    {
                        this.smoothRunTickCount = Integer.parseInt(optionTokens[1]);
                    }

                    if (optionTokens[0].equals("smoothTick"))
                    {
                        this.smoothTick = optionTokens[1].equals("true");
                    }

                    // VIVE START - new options
                    if (optionTokens[0].equals("simulateFalling"))
                    {
                        this.simulateFalling = optionTokens[1].equals("true");
                    }
                    if (optionTokens[0].equals("weaponCollision"))
                    {
                        this.weaponCollision = optionTokens[1].equals("true");
                    }
                    if (optionTokens[0].equals("animalTouching"))
                    {
                        this.animaltouching = optionTokens[1].equals("true");
                    }
                    // VIVE END - new options
                    //JRBUDDA
                    if (optionTokens[0].equals("allowCrawling"))
                    {
                        this.vrAllowCrawling = optionTokens[1].equals("true");
                    }
                    if (optionTokens[0].equals("limitedTeleport"))
                    {
                        this.vrLimitedSurvivalTeleport = optionTokens[1].equals("true");
                    }
                    if (optionTokens[0].equals("reverseHands"))
                    {
                        this.vrReverseHands = optionTokens[1].equals("true");
                    }
                    if (optionTokens[0].equals("stencilOn"))
                    {
                        this.vrUseStencil = optionTokens[1].equals("true");
                    }
                    if (optionTokens[0].equals("bcbOn"))
                    {
                        this.vrShowBlueCircleBuddy = optionTokens[1].equals("true");
                    }
                    if (optionTokens[0].equals("worldScale"))
                    {
                        this.vrWorldScale = this.parseFloat(optionTokens[1]);
                    }
                    if (optionTokens[0].equals("worldRotation"))
                    {
                        this.vrWorldRotation = this.parseFloat(optionTokens[1]);
                    }
                    if (optionTokens[0].equals("vrWorldRotationIncrement"))
                    {
                        this.vrWorldRotationIncrement =  this.parseFloat(optionTokens[1]);
                    }
                    if (optionTokens[0].equals("vrFixedCamposX"))
                    {
                        this.vrFixedCamposX =  this.parseFloat(optionTokens[1]);
                    }
                    if (optionTokens[0].equals("vrFixedCamposY"))
                    {
                        this.vrFixedCamposY =  this.parseFloat(optionTokens[1]);
                    }
                    if (optionTokens[0].equals("vrFixedCamposZ"))
                    {
                        this.vrFixedCamposZ =  this.parseFloat(optionTokens[1]);
                    }
                    if (optionTokens[0].equals("vrFixedCamrotW"))
                    {
                        this.vrFixedCamrotQuat.w = this.parseFloat(optionTokens[1]);
                    }
                    if (optionTokens[0].equals("vrFixedCamrotX"))
                    {
                        this.vrFixedCamrotQuat.x = this.parseFloat(optionTokens[1]);
                    }
                    if (optionTokens[0].equals("vrFixedCamrotY"))
                    {
                        this.vrFixedCamrotQuat.y = this.parseFloat(optionTokens[1]);
                    }
                    if (optionTokens[0].equals("vrFixedCamrotZ"))
                    {
                        this.vrFixedCamrotQuat.z = this.parseFloat(optionTokens[1]);
                    }
                    if (optionTokens[0].equals("mrMovingCamOffsetX"))
                    {
                        this.mrMovingCamOffsetX =  this.parseFloat(optionTokens[1]);
                    }
                    if (optionTokens[0].equals("mrMovingCamOffsetY"))
                    {
                        this.mrMovingCamOffsetY =  this.parseFloat(optionTokens[1]);
                    }
                    if (optionTokens[0].equals("mrMovingCamOffsetZ"))
                    {
                        this.mrMovingCamOffsetZ =  this.parseFloat(optionTokens[1]);
                    }
                    if (optionTokens[0].equals("mrMovingCamOffsetRotW"))
                    {
                        this.mrMovingCamOffsetRotQuat.w = this.parseFloat(optionTokens[1]);
                    }
                    if (optionTokens[0].equals("mrMovingCamOffsetRotX"))
                    {
                        this.mrMovingCamOffsetRotQuat.x = this.parseFloat(optionTokens[1]);
                    }
                    if (optionTokens[0].equals("mrMovingCamOffsetRotY"))
                    {
                        this.mrMovingCamOffsetRotQuat.y = this.parseFloat(optionTokens[1]);
                    }
                    if (optionTokens[0].equals("mrMovingCamOffsetRotZ"))
                    {
                        this.mrMovingCamOffsetRotQuat.z = this.parseFloat(optionTokens[1]);
                    }
                    if (optionTokens[0].equals("vrTouchHotbar"))
                    {
                    	  this.vrTouchHotbar = optionTokens[1].equals("true");
                    }
                    if (optionTokens[0].equals("seated"))
                    {
                    	  this.seated = optionTokens[1].equals("true");
                    }

                    if(optionTokens[0].equals("jumpThreshold")){
                        this.jumpThreshold=this.parseFloat(optionTokens[1]);
                    }

                    if(optionTokens[0].equals("sneakThreshold")){
                        this.sneakThreshold=this.parseFloat(optionTokens[1]);
                    }

                    if(optionTokens[0].equals("realisticSneakEnabled")){
                        this.realisticSneakEnabled=optionTokens[1].equals("true");
                    }
					if(optionTokens[0].equals("physicalGuiEnabled")){
						this.physicalGuiEnabled=optionTokens[1].equals("true");
						//TEMP
						this.physicalGuiEnabled=false;
					}
                    if(optionTokens[0].equals("seatedhmd")){
                        this.seatedUseHMD=optionTokens[1].equals("true");
                    }
                    if(optionTokens[0].equals("seatedHudAltMode")){
                        this.seatedHudAltMode=optionTokens[1].equals("true");
                    }
                    if(optionTokens[0].equals("realisticJumpEnabled")){
                        this.realisticJumpEnabled=optionTokens[1].equals("true");
                    }
                    if(optionTokens[0].equals("realisticClimbEnabled")){
                        this.realisticClimbEnabled=optionTokens[1].equals("true");
                    }
                    if(optionTokens[0].equals("realisticSwimEnabled")){
                        this.realisticSwimEnabled=optionTokens[1].equals("true");
                    }
                    if(optionTokens[0].equals("realisticRowEnabled")){
                        this.realisticRowEnabled=optionTokens[1].equals("true");
                    }

                    if(optionTokens[0].equals("headToHmdLength")){
                        this.headToHmdLength=parseFloat(optionTokens[1]);
                    }

                    if(optionTokens[0].equals("walkMultiplier")){
                        this.walkMultiplier=parseFloat(optionTokens[1]);
                    }
                    
                    if (optionTokens[0].equals("vrFreeMoveMode"))
                    {
                        this.vrFreeMoveMode =  Integer.parseInt(optionTokens[1]);
                        if (this.vrFreeMoveMode == FREEMOVE_JOYPAD)
                            this.vrFreeMoveMode = FREEMOVE_CONTROLLER;
                    }

                    if(optionTokens[0].equals("xSensitivity")){
                        this.xSensitivity=parseFloat(optionTokens[1]);
                    }

                    if(optionTokens[0].equals("ySensitivity")){
                        this.ySensitivity=parseFloat(optionTokens[1]);
                    }

                    if(optionTokens[0].equals("keyholeX")){
                        this.keyholeX=parseFloat(optionTokens[1]);
                    }

                    if(optionTokens[0].equals("autoCalibration")){
                        this.autoCalibration=parseFloat(optionTokens[1]);
                    }

                    if(optionTokens[0].equals("manualCalibration")){
                        this.manualCalibration=parseFloat(optionTokens[1]);
                    }
                    
                    if(optionTokens[0].equals("vehicleRotation")){
                        this.vehicleRotation=optionTokens[1].equals("true");
                    }
                    
                    if(optionTokens[0].equals("fovReduction")){
                        this.useFOVReduction=optionTokens[1].equals("true");
                    }

                    if(optionTokens[0].equals("alwaysSimulateKeyboard")){
                        this.alwaysSimulateKeyboard=optionTokens[1].equals("true");
                    }
                    
                    if(optionTokens[0].equals("autoOpenKeyboard")){
                        this.autoOpenKeyboard=optionTokens[1].equals("true");
                    }
                    
                    if(optionTokens[0].equals("forceHardwareDetection")){
                        this.forceHardwareDetection=Integer.parseInt(optionTokens[1]);
                    }
                    
                    if(optionTokens[0].equals("backpackSwitching")){
                        this.backpackSwitching=optionTokens[1].equals("true");
                    }
                    
                    if(optionTokens[0].equals("analogMovement")){
                        this.analogMovement = optionTokens[1].equals("true");
                    }
                    
                    if(optionTokens[0].equals("bowMode")){
                        this.bowMode = Integer.parseInt(optionTokens[1]);
                    }
                    
                    if(optionTokens[0].equals("hideGUI")){
                        this.mc.gameSettings.hideGUI = optionTokens[1].equals("true");
                    }
                    
                    if(optionTokens[0].equals("teleportLimitUp")){
                        this.vrTeleportUpLimit = Integer.parseInt(optionTokens[1]);
                    }
                    
                    if(optionTokens[0].equals("teleportLimitDown")){
                        this.vrTeleportDownLimit = Integer.parseInt(optionTokens[1]);
                    }
                    
                    if(optionTokens[0].equals("teleportLimitHoriz")){
                        this.vrTeleportHorizLimit = Integer.parseInt(optionTokens[1]);
                    }

					if(optionTokens[0].equals("radialModeHold")){
						this.radialModeHold = optionTokens[1].equals("true");
					}

					if(optionTokens[0].equals("physicalKeyboard")){
					    this.physicalKeyboard = optionTokens[1].equals("true");
                    }

					if(optionTokens[0].equals("originOffset")){
                        String[] split = optionTokens[1].split(",");
					    MCOpenVR.offset = new Vector3(Float.parseFloat(split[0]), Float.parseFloat(split[1]), Float.parseFloat(split[2]));
                    }

					if(optionTokens[0].equals("allowStandingOriginOffset")){
					    this.allowStandingOriginOffset = optionTokens[1].equals("true");
                    }

                    if(optionTokens[0].equals("seatedFreeMove")){
                        this.seatedFreeMove = optionTokens[1].equals("true");
                    }
                    
                    if(optionTokens[0].equals("forceStandingFreeMove")){
                    	this.forceStandingFreeMove = optionTokens[1].equals("true");
                    }

                    if(optionTokens[0].equals("allowAdvancedBindings")){
                    	this.allowAdvancedBindings = optionTokens[1].equals("true");
                    }

                    if(optionTokens[0].equals("menuWorldSelection")){
                        this.menuWorldSelection = Integer.parseInt(optionTokens[1]);
                    }

                    if(optionTokens[0].equals("firstRun")){
                    	this.firstRun = optionTokens[1].equals("true");
                    }
                    
                    
                    if(optionTokens[0].equals("keyboardKeys")){
                    	String value = optionTokens[1];
                    	for (int i = 2; i < optionTokens.length;i++) {
                    		value += ":" + optionTokens[i];
                    	}
                        this.keyboardKeys = value;
                        
                        int len = value.length();
                        
//                        System.out.println("Main Keys: " + value + " " + len);
//
//                        for (int i = 0; i < len; i++) {
//                            System.out.println(value.charAt(i) + "|" + String.valueOf(value.charAt(i)) );
//						}
//                                               
                    }
                    
                    if(optionTokens[0].equals("keyboardKeysShift")){
                    	String value = optionTokens[1];
                    	
                    	for (int i = 2; i < optionTokens.length;i++) {
                    		value += ":" + optionTokens[i];
                    	}
                    	
                        this.keyboardKeysShift = value;
                        
//                        int len = value.length();
//                        System.out.println("Shift Keys: " + value + " " + len);
//                        
//                        for (int i = 0; i < len; i++) {
//                            System.out.println(value.charAt(i) + "|" + String.valueOf(value.charAt(i)) );
//						}
//                        
                       
                    }

                    if(optionTokens[0].startsWith("QUICKCOMMAND_")){
                    	String[] pts = optionTokens[0].split("_");
                    	int i = Integer.parseInt(pts[1]);
                    	if (optionTokens.length == 1) 
                    		vrQuickCommands[i] = "";
                    	else
                    		vrQuickCommands[i] = optionTokens[1];
                    }
                    
                    if(optionTokens[0].startsWith("RADIAL_")){
                    	String[] pts = optionTokens[0].split("_");
                    	int i = Integer.parseInt(pts[1]);
                    	if (optionTokens.length == 1) 
                    		vrRadialItems[i] = "";
                    	else
                    		vrRadialItems[i] = optionTokens[1];
                    }
                    
                    if(optionTokens[0].startsWith("RADIALALT_")){
                    	String[] pts = optionTokens[0].split("_");
                    	int i = Integer.parseInt(pts[1]);
                    	if (optionTokens.length == 1) 
                    		vrRadialItemsAlt[i] = "";
                    	else
                    		vrRadialItemsAlt[i] = optionTokens[1];
                    }

                    //END JRBUDDA

                }
                catch (Exception var7)
                {
                    logger.warn("Skipping bad VR option: " + var2);
                    var7.printStackTrace();
                }
            }           
            optionsVRReader.close();
        }
        catch (Exception var8)
        {
            logger.warn("Failed to load VR options!");
            var8.printStackTrace();
        }
    }

    public void resetSettings()
    {
        // Get the Minecrift defaults
        loadDefaults();
    }
    
    public String getButtonDisplayString( VRSettings.VrOptions par1EnumOptions )
    {
        String var2 = par1EnumOptions.getEnumString();

        String var3 = var2 + ": ";
        String var4 = var3;
        String var5;

        switch( par1EnumOptions) {
            case OTHER_HUD_SETTINGS:
                return var2;
            case OTHER_RENDER_SETTINGS:
                return var2;
            case LOCOMOTION_SETTINGS:
                return var2;
	        case MOVEMENT_MULTIPLIER:
	            return var4 + String.format("%.2f", this.movementSpeedMultiplier);
	        case HUD_OPACITY:
	        	if( this.hudOpacity > 0.99)
	        		return var4 + "Opaque";
	            return var4 + String.format("%.2f", this.hudOpacity);
            case RENDER_MENU_BACKGROUND:
                return this.menuBackground ? var4 + "ON" : var4 + "OFF";
	        case RENDER_FULL_FIRST_PERSON_MODEL_MODE:
                if (this.renderFullFirstPersonModelMode == RENDER_FIRST_PERSON_FULL)
                    return var4 + "Full";
                else if (this.renderFullFirstPersonModelMode == RENDER_FIRST_PERSON_HAND)
                    return var4 + "Hand";
                else if (this.renderFullFirstPersonModelMode == RENDER_FIRST_PERSON_NONE)
                    return var4 + "None";
            case MIRROR_DISPLAY:
                switch(this.displayMirrorMode) {
                    case MIRROR_OFF:
                    default:
                        return var4 + "OFF";
                    case MIRROR_ON_DUAL:
                        return var4 + "Dual";
                    case MIRROR_ON_SINGLE:
                        return var4 + "Single";
                    case MIRROR_FIRST_PERSON:
                        return var4 + "1st Person";
                    case MIRROR_THIRD_PERSON:
                        return var4 + "3rd Person";
                    case MIRROR_MIXED_REALITY:
                        return var4 + "Mixed Reality";

                }
            case MIXED_REALITY_KEY_COLOR:
                if (this.mixedRealityKeyColor.equals(new Color(0, 0, 0))) {
                	return var4 + "Black";
                } else if (this.mixedRealityKeyColor.equals(new Color(255, 0, 0))) {
                	return var4 + "Red";
                } else if (this.mixedRealityKeyColor.equals(new Color(255, 255, 0))) {
                	return var4 + "Yellow";
                } else if (this.mixedRealityKeyColor.equals(new Color(0, 255, 0))) {
                	return var4 + "Green";
                } else if (this.mixedRealityKeyColor.equals(new Color(0, 255, 255))) {
                	return var4 + "Cyan";
                } else if (this.mixedRealityKeyColor.equals(new Color(0, 0, 255))) {
                	return var4 + "Blue";
                } else if (this.mixedRealityKeyColor.equals(new Color(255, 0, 255))) {
                	return var4 + "Magenta";
                }
                return var4 + this.mixedRealityKeyColor.getRed() + " " + this.mixedRealityKeyColor.getGreen() + " " + this.mixedRealityKeyColor.getBlue();
             case MIXED_REALITY_RENDER_HANDS:
                return this.mixedRealityRenderHands ? var4 + "ON" : var4 + "OFF";
            case MIXED_REALITY_UNITY_LIKE:
                 return this.mixedRealityUnityLike ? var4 + "Unity" : var4 + "Side-by-Side";
            case MIXED_REALITY_UNDISTORTED:
                return this.mixedRealityMRPlusUndistorted ? var4 + "ON" : var4 + "OFF";
            case MIXED_REALITY_ALPHA_MASK:
                return this.mixedRealityAlphaMask ? var4 + "ON" : var4 + "OFF";
            case MIXED_REALITY_FOV:
            	return var4 + String.format("%.0f\u00B0", this.mc.vrSettings.mixedRealityFov);
            case INSIDE_BLOCK_SOLID_COLOR:
            	return this.insideBlockSolidColor ? var4 + "Solid Color" : var4 + "Texture";
            case WALK_UP_BLOCKS:
                return this.walkUpBlocks ? var4 + "ON" : var4 + "OFF";
 	        case HUD_SCALE:
	            return var4 + String.format("%.2f", this.hudScale);
            case HUD_LOCK_TO:
                switch (this.vrHudLockMode) {
                // VIVE - lock to hand instead of body
                case HUD_LOCK_HAND:
                	return var4 + "Hand";
                case HUD_LOCK_HEAD:
                	return var4 + "Head";
                case HUD_LOCK_WRIST:
                	return var4 + "Wrist";
                case HUD_LOCK_BODY:
                    return var4 + "Body";
                }
	        case HUD_DISTANCE:
	            return var4 + String.format("%.2f", this.hudDistance);
	        case HUD_PITCH:
	            return var4 + String.format("%.0f", this.hudPitchOffset);
            case HUD_YAW:
            	return var4 + String.format("%.0f", this.hudYawOffset);
            case HUD_HIDE:
                return this.mc.gameSettings.hideGUI ? var4 + "YES" : var4 + "NO";
            case RENDER_SCALEFACTOR:
            	return var4 + String.format("%.1f", this.renderScaleFactor);
            case FSAA:
            	return this.useFsaa ? var4 + "ON" : var4 + "OFF";
            case CROSSHAIR_SCALE:
	            return var4 + String.format("%.2f", this.crosshairScale);
            case MENU_CROSSHAIR_SCALE:
                return var4 + String.format("%.2f", this.menuCrosshairScale);
            case CROSSHAIR_SCALES_WITH_DISTANCE:
	        	return this.crosshairScalesWithDistance ? var4 + "ON" : var4 + "OFF";
	        case RENDER_CROSSHAIR_MODE:
                if (this.renderInGameCrosshairMode == RENDER_CROSSHAIR_MODE_HUD)
                    return var4 + "With HUD";
                else if (this.renderInGameCrosshairMode == RENDER_CROSSHAIR_MODE_ALWAYS)
                    return var4 + "Always";
                else if (this.renderInGameCrosshairMode == RENDER_CROSSHAIR_MODE_NEVER)
                    return var4 + "Never";
	        case RENDER_BLOCK_OUTLINE_MODE:
                if (this.renderBlockOutlineMode == RENDER_BLOCK_OUTLINE_MODE_HUD)
                    return var4 + "With HUD";
                else if (this.renderBlockOutlineMode == RENDER_BLOCK_OUTLINE_MODE_ALWAYS)
                    return var4 + "Always";
                else if (this.renderBlockOutlineMode == RENDER_BLOCK_OUTLINE_MODE_NEVER)
                    return var4 + "Never";
	        case HUD_OCCLUSION:
	        	return this.hudOcclusion ? var4 + "ON" : var4 + "OFF";
	        case MENU_ALWAYS_FOLLOW_FACE:
	        	return this.menuAlwaysFollowFace ? var4 + "Always" : var4 + "Seated";
	        case CROSSHAIR_OCCLUSION:
	        	return this.useCrosshairOcclusion ? var4 + "ON" : var4 + "OFF";
	        case MONO_FOV:
	        	return var4 + String.format("%.0f\u00B0", this.mc.gameSettings.fov);
	        case INERTIA_FACTOR:
	        	if (this.inertiaFactor == INERTIA_NONE)
	        		return var4 + "Automan";
	        	else if (this.inertiaFactor == INERTIA_NORMAL)
	        		return var4 + "Normal";
                else if (this.inertiaFactor == INERTIA_LARGE)
                    return var4 + "A Lot";
                else if (this.inertiaFactor == INERTIA_MASSIVE)
                    return var4 + "Even More";
                // VIVE START - new options
            case SIMULATE_FALLING:
                return this.simulateFalling ? var4 + "ON" : var4 + "OFF";
            case WEAPON_COLLISION:
                return this.weaponCollision ? var4 + "ON" : var4 + "OFF";
            case ANIMAL_TOUCHING:
                return this.animaltouching ? var4 + "ON" : var4 + "OFF";
                // VIVE END - new options
                //JRBUDDA
            case ALLOW_CRAWLING:
                return this.vrAllowCrawling ? var4 + "ON" : var4 + "OFF"; 
            case LIMIT_TELEPORT:
                return this.vrLimitedSurvivalTeleport ? var4 + "ON" : var4 + "OFF";
            case REVERSE_HANDS:
            	return this.vrReverseHands ? var4 + "ON" : var4 + "OFF";
            case STENCIL_ON:
            	return this.vrUseStencil ? var4 + "ON" : var4 + "OFF";
            case BCB_ON:
            	return this.vrShowBlueCircleBuddy ? var4 + "ON" : var4 + "OFF";
            case WORLD_SCALE:
	            return var4 + String.format("%.2f", this.vrWorldScale)+ "x" ;
            case WORLD_ROTATION:
	            return var4 + String.format("%.0f", this.vrWorldRotation);
            case WORLD_ROTATION_INCREMENT:
	            return var4 + (this.vrWorldRotationIncrement == 0 ? "Smooth" : String.format("%.0f", this.vrWorldRotationIncrement));
            case TOUCH_HOTBAR:
            	return this.vrTouchHotbar ? var4 + "ON" : var4 + "OFF";
            case PLAY_MODE_SEATED:
            	return this.seated ? var4 + "Seated" : var4 + "Standing";
                //END JRBUDDA
            case REALISTIC_JUMP:
                return this.realisticJumpEnabled ? var4 + "ON" : var4 + "OFF";
            case SEATED_HMD:
                return this.seatedUseHMD ? var4 + "HMD" : var4 + "Crosshair";
            case SEATED_HUD_XHAIR:
                return this.seatedHudAltMode ? var4 + "Crosshair" : var4 + "HMD";
            case REALISTIC_SNEAK:
                return this.realisticSneakEnabled ? var4 + "ON" : var4 + "OFF";
			case PHYSICAL_GUI:
				return this.physicalGuiEnabled ? var4 + "ON" : var4 + "OFF";
            case REALISTIC_CLIMB:
                return this.realisticClimbEnabled ? var4 + "ON" : var4 + "OFF";
            case REALISTIC_SWIM:
                return this.realisticSwimEnabled ? var4 + "ON" : var4 + "OFF";
            case REALISTIC_ROW:
                return this.realisticRowEnabled ? var4 + "ON" : var4 + "OFF";
            case VEHICLE_ROTATION:
                return this.vehicleRotation ? var4 + "ON" : var4 + "OFF";
            case CALIBRATE_HEIGHT:
                return var2;
            case WALK_MULTIPLIER:
                return var4+ String.format("%.1f",walkMultiplier);
            case X_SENSITIVITY:
                return var4+ String.format("%.2f",xSensitivity);
            case Y_SENSITIVITY:
                return var4+ String.format("%.2f",ySensitivity);
            case KEYHOLE:
                return var4+ String.format("%.0f",keyholeX);
            case RESET_ORIGIN:
                return var2;
            case FREEMOVE_MODE:
                switch (this.vrFreeMoveMode) {
                // VIVE - lock to hand instead of body
                case FREEMOVE_CONTROLLER:
                	return var4 + "Controller";
                case FREEMOVE_HMD:
                	return var4 + "HMD";
                case FREEMOVE_RUNINPLACE:
                	return var4 + "RunInPlace";
                }
            case FOV_REDUCTION:
                return this.useFOVReduction ? var4 + "ON" : var4 + "OFF";
            case AUTO_OPEN_KEYBOARD:
                return this.autoOpenKeyboard ? var4 + "ON" : var4 + "OFF";
            case BACKPACK_SWITCH:
                return this.backpackSwitching ? var4 + "ON" : var4 + "OFF";
            case ANALOG_MOVEMENT:
                return this.analogMovement ? var4 + "ON" : var4 + "OFF";
            case AUTO_SPRINT:
                return this.autoSprint ? var4 + "ON" : var4 + "OFF";
            case AUTO_SPRINT_THRESHOLD:
                return var4 + String.format("%.2f", autoSprintThreshold);
            case RADIAL_MODE_HOLD:
                return this.radialModeHold ? var4 + "HOLD" : var4 + "PRESS";
            case PHYSICAL_KEYBOARD:
                return this.physicalKeyboard ? var4 + "Physical" : var4 + "Pointer";
            case BOW_MODE:
            	if(this.bowMode == BOW_MODE_OFF)
            		return var4 + " OFF";
            	else if(this.bowMode == BOW_MODE_ON)
            		return var4 + " ON";
            	else if (this.bowMode == BOW_MODE_VANILLA)
            		return var4 + "VANILLA";
            	else return var4 + " wtf?";
            case TELEPORT_UP_LIMIT:
	            return var4 +  (this.vrTeleportUpLimit > 0 ? this.vrTeleportUpLimit+ " Blocks" :" OFF");
            case TELEPORT_DOWN_LIMIT:
	            return var4 +  (this.vrTeleportDownLimit > 0 ? this.vrTeleportDownLimit+ " Blocks" :" OFF");
            case TELEPORT_HORIZ_LIMIT:
	            return var4 +  (this.vrTeleportHorizLimit > 0 ? this.vrTeleportHorizLimit+ " Blocks" :" OFF");
            case ALLOW_STANDING_ORIGIN_OFFSET:
                return this.allowStandingOriginOffset ? var4 + "YES" : var4 + "NO";
            case SEATED_FREE_MOVE:
                return this.seatedFreeMove ? var4 + "Free Move" : var4 + "Teleport";
            case FORCE_STANDING_FREE_MOVE:
            	return this.forceStandingFreeMove ? var4 + "YES" : var4 + "NO";
            case ALLOW_ADVANCED_BINDINGS:
            	return this.allowAdvancedBindings ? var4 + "YES" : var4 + "NO";
            case MENU_WORLD_SELECTION:
                switch (this.menuWorldSelection) {
                    case MENU_WORLD_BOTH:
                        return var4 + "Official & Custom";
                    case MENU_WORLD_CUSTOM:
                        return var4 + "Custom Only";
                    case MENU_WORLD_OFFICIAL:
                        return var4 + "Official Only";
                }
            default:
            		return "";
        }
    }

    public float getOptionFloatValue(VRSettings.VrOptions par1EnumOptions)
    {
    	switch( par1EnumOptions ) {
			case MOVEMENT_MULTIPLIER :
				return this.movementSpeedMultiplier ;
			case HUD_SCALE :
				return this.hudScale ;
			case HUD_OPACITY :
				return this.hudOpacity ;
			case HUD_DISTANCE :
				return this.hudDistance ;
			case HUD_PITCH :
				return this.hudPitchOffset ;
            case HUD_YAW :
                return this.hudYawOffset ;
			case CROSSHAIR_SCALE :
				return this.crosshairScale ;
            case MENU_CROSSHAIR_SCALE :
                return this.menuCrosshairScale ;
            case WALK_MULTIPLIER:
                return this.walkMultiplier;
            case X_SENSITIVITY:
                return this.xSensitivity;
            case Y_SENSITIVITY:
                return this.ySensitivity;
            case KEYHOLE:
                return this.keyholeX;
            case AUTO_SPRINT_THRESHOLD:
                return this.autoSprintThreshold;
            // VIVE START - new options
            case WORLD_SCALE:          	
            	if(vrWorldScale ==  0.1f) return 0;
            	if(vrWorldScale ==  0.25f) return 1;
            	if(vrWorldScale >=  0.5f && vrWorldScale <=  2.0f) return (vrWorldScale / 0.1f) - 3f;
            	if(vrWorldScale == 3) return 18;
            	if(vrWorldScale == 4) return 19;
            	if(vrWorldScale == 6) return 20;
            	if(vrWorldScale == 8) return 21;
            	if(vrWorldScale == 10) return 22;
            	if(vrWorldScale == 12) return 23;
            	if(vrWorldScale == 16) return 24;
            	if(vrWorldScale == 20) return 25;
            	if(vrWorldScale == 30) return 26;
            	if(vrWorldScale == 50) return 27;
            	if(vrWorldScale == 75) return 28;
            	if(vrWorldScale == 100) return 29;
            	return 7;
            case WORLD_ROTATION:
                return vrWorldRotation;
            case WORLD_ROTATION_INCREMENT:
            	if(vrWorldRotationIncrement == 0) return -1;
            	if(vrWorldRotationIncrement == 10f) return 0;
            	if(vrWorldRotationIncrement == 36f) return 1;            	
            	if(vrWorldRotationIncrement == 45f) return 2;
            	if(vrWorldRotationIncrement == 90f) return 3;
            	if(vrWorldRotationIncrement == 180f) return 4;
            	return 0;
            case MONO_FOV:
            	return (float) this.mc.gameSettings.fov;
			case MIXED_REALITY_FOV:
				return this.mixedRealityFov;
            case RENDER_SCALEFACTOR:
            	return this.renderScaleFactor;
            case BOW_MODE:
            	return this.bowMode;
            case TELEPORT_UP_LIMIT:          	
            	return this.vrTeleportUpLimit;
            case TELEPORT_DOWN_LIMIT:          	
            	return this.vrTeleportDownLimit;
            case TELEPORT_HORIZ_LIMIT:          	
            	return this.vrTeleportHorizLimit;

            // VIVE END - new options
            default:
                return 0.0f;
    	}
    }
    /**
     * For non-float options. Toggles the option on/off, or cycles through the list i.e. render distances.
     */
    public void setOptionValue(VRSettings.VrOptions par1EnumOptions)
    {
    	switch( par1EnumOptions )
    	{
            case RENDER_MENU_BACKGROUND:
                this.menuBackground = !this.menuBackground;
                break;
	        case RENDER_FULL_FIRST_PERSON_MODEL_MODE:
                this.renderFullFirstPersonModelMode++;
                if (this.renderFullFirstPersonModelMode > RENDER_FIRST_PERSON_NONE)
                    this.renderFullFirstPersonModelMode = RENDER_FIRST_PERSON_FULL;
	            break;
             case MIRROR_DISPLAY:
                this.displayMirrorMode++;
                if (this.displayMirrorMode > MIRROR_MIXED_REALITY)
                    this.displayMirrorMode = MIRROR_OFF;
                this.mc.stereoProvider.reinitFrameBuffers("Mirror Setting Changed");
                break;
            case MIXED_REALITY_KEY_COLOR:
            	if (this.mixedRealityKeyColor.equals(new Color(0, 0, 0))) {
            		this.mixedRealityKeyColor = new Color(255, 0, 0);
	            } else if (this.mixedRealityKeyColor.equals(new Color(255, 0, 0))) {
	            	this.mixedRealityKeyColor = new Color(255, 255, 0);
	            } else if (this.mixedRealityKeyColor.equals(new Color(255, 255, 0))) {
	            	this.mixedRealityKeyColor = new Color(0, 255, 0);
	            } else if (this.mixedRealityKeyColor.equals(new Color(0, 255, 0))) {
	            	this.mixedRealityKeyColor = new Color(0, 255, 255);
	            } else if (this.mixedRealityKeyColor.equals(new Color(0, 255, 255))) {
	            	this.mixedRealityKeyColor = new Color(0, 0, 255);
	            } else if (this.mixedRealityKeyColor.equals(new Color(0, 0, 255))) {
	            	this.mixedRealityKeyColor = new Color(255, 0, 255);
	            } else if (this.mixedRealityKeyColor.equals(new Color(255, 0, 255))) {
	            	this.mixedRealityKeyColor = new Color(0, 0, 0);
	            } else {
	            	this.mixedRealityKeyColor = new Color(0, 0, 0);
	            }
                break;
            case MIXED_REALITY_RENDER_HANDS:
            	this.mixedRealityRenderHands = !this.mixedRealityRenderHands;
            	break;
            case MIXED_REALITY_UNITY_LIKE:
            	this.mixedRealityUnityLike = !this.mixedRealityUnityLike;
            	mc.stereoProvider.reinitFrameBuffers("MR Setting Changed");
            	break;
            case MIXED_REALITY_UNDISTORTED:
            	this.mixedRealityMRPlusUndistorted = !this.mixedRealityMRPlusUndistorted;
            	mc.stereoProvider.reinitFrameBuffers("MR Setting Changed");
            	break;
            case MIXED_REALITY_ALPHA_MASK:
            	this.mixedRealityAlphaMask = !this.mixedRealityAlphaMask;
            	mc.stereoProvider.reinitFrameBuffers("MR Setting Changed");
            	break;
            case INSIDE_BLOCK_SOLID_COLOR:
            	this.insideBlockSolidColor = !this.insideBlockSolidColor;
            	break;
            case WALK_UP_BLOCKS:
                this.walkUpBlocks = !this.walkUpBlocks;
                break;
            case HUD_LOCK_TO:
                switch (this.vrHudLockMode) {
                // VIVE - lock to hand instead of body
                case HUD_LOCK_HAND:
                	this.vrHudLockMode = HUD_LOCK_HEAD;
                	break;
                case HUD_LOCK_HEAD:
                   	this.vrHudLockMode = HUD_LOCK_WRIST;
                	break;
                case HUD_LOCK_WRIST:
                   	this.vrHudLockMode = HUD_LOCK_HAND;
                	break;
                case HUD_LOCK_BODY:
                    this.vrHudLockMode = HUD_LOCK_HAND;
                }
                break;
            case HUD_HIDE:
                this.mc.gameSettings.hideGUI = !this.mc.gameSettings.hideGUI;
                break;
	        case FSAA:
	            this.useFsaa = !this.useFsaa;
	            break;
  	        case RENDER_CROSSHAIR_MODE:
	            this.renderInGameCrosshairMode++;
                if (this.renderInGameCrosshairMode > RENDER_CROSSHAIR_MODE_NEVER)
                    this.renderInGameCrosshairMode = RENDER_CROSSHAIR_MODE_ALWAYS;
	            break;
	        case RENDER_BLOCK_OUTLINE_MODE:
                this.renderBlockOutlineMode++;
                if (this.renderBlockOutlineMode > RENDER_BLOCK_OUTLINE_MODE_NEVER)
                    this.renderBlockOutlineMode = RENDER_BLOCK_OUTLINE_MODE_ALWAYS;
	            break;
	            
	        case HUD_OCCLUSION:
	            this.hudOcclusion = !this.hudOcclusion;
	            break;
	        case MENU_ALWAYS_FOLLOW_FACE:
	            this.menuAlwaysFollowFace = !this.menuAlwaysFollowFace;
	            break;
            case CROSSHAIR_OCCLUSION:
                this.useCrosshairOcclusion = !this.useCrosshairOcclusion;
                break;
             case INERTIA_FACTOR:
                this.inertiaFactor +=1;
                if (this.inertiaFactor > INERTIA_MASSIVE)
                    this.inertiaFactor = INERTIA_NONE;
                break;
             // VIVE START - new options
            case SIMULATE_FALLING:
                this.simulateFalling = !this.simulateFalling;
                break;
            case WEAPON_COLLISION:
                this.weaponCollision = !this.weaponCollision;
                break;
            case ANIMAL_TOUCHING:
                this.animaltouching = !this.animaltouching;
                break;
            // VIVE END - new options
                //JRBUDDA
            case ALLOW_CRAWLING:
                this.vrAllowCrawling = !this.vrAllowCrawling;
                break;
            case LIMIT_TELEPORT:
                this.vrLimitedSurvivalTeleport = !this.vrLimitedSurvivalTeleport;
                break;
            case REVERSE_HANDS:
                this.vrReverseHands = !this.vrReverseHands;
                break;
            case STENCIL_ON:
                this.vrUseStencil = !this.vrUseStencil;
                break;
            case BCB_ON:
                this.vrShowBlueCircleBuddy = !this.vrShowBlueCircleBuddy;
                break;
            case TOUCH_HOTBAR:
                this.vrTouchHotbar = !this.vrTouchHotbar;
                break;
            case PLAY_MODE_SEATED:
                this.seated = !this.seated;
                break;
                //JRBUDDA
            case REALISTIC_JUMP:
                realisticJumpEnabled = !realisticJumpEnabled;
                break;
            case SEATED_HMD:
                seatedUseHMD = !seatedUseHMD;
                break;
            case SEATED_HUD_XHAIR:
                seatedHudAltMode = !seatedHudAltMode;
                break;
            case REALISTIC_SWIM:
                realisticSwimEnabled = !realisticSwimEnabled;
                break;
            case REALISTIC_CLIMB:
                realisticClimbEnabled = !realisticClimbEnabled;
                break;
            case REALISTIC_ROW:
                realisticRowEnabled = !realisticRowEnabled;
                break;
            case REALISTIC_SNEAK:
                realisticSneakEnabled = !realisticSneakEnabled;
                break;
			case PHYSICAL_GUI:
				physicalGuiEnabled = !physicalGuiEnabled;
				break;
            case BACKPACK_SWITCH:
                backpackSwitching = !backpackSwitching;
                break;
            case VEHICLE_ROTATION:
                vehicleRotation = !vehicleRotation;
                break;
            case CALIBRATE_HEIGHT:
                if(seated) {
                    MCOpenVR.resetPosition();
                }
                playerEyeHeight = (float) Minecraft.getInstance().vrPlayer.vrdata_room_pre.hmd.getPosition().y;
                break;
            case FREEMOVE_MODE:
                switch (this.vrFreeMoveMode) {
                case FREEMOVE_CONTROLLER:
                	this.vrFreeMoveMode = FREEMOVE_HMD;
                	break;
                case FREEMOVE_HMD:
                   	this.vrFreeMoveMode = FREEMOVE_RUNINPLACE;
                	break;
                case FREEMOVE_RUNINPLACE:
                   	this.vrFreeMoveMode = FREEMOVE_CONTROLLER;
                	break;
                }
                break;
            case FOV_REDUCTION:
            	useFOVReduction = !useFOVReduction;
            	break;     
            case CROSSHAIR_SCALES_WITH_DISTANCE:
            	crosshairScalesWithDistance = !crosshairScalesWithDistance;
            	break;
            case AUTO_OPEN_KEYBOARD:
            	autoOpenKeyboard = !autoOpenKeyboard;
            	break;
            case ANALOG_MOVEMENT:
            	analogMovement = !analogMovement;
            	break;
            case AUTO_SPRINT:
                autoSprint = !autoSprint;
                break;
            case BOW_MODE:
            	this.bowMode++;
            	if(this.bowMode>2) this.bowMode = 0;
            	break;
            case RADIAL_MODE_HOLD:
            	this.radialModeHold = !this.radialModeHold;
            	break;
            case PHYSICAL_KEYBOARD:
                this.physicalKeyboard = !this.physicalKeyboard;
                break;
            case ALLOW_STANDING_ORIGIN_OFFSET:
                this.allowStandingOriginOffset = !this.allowStandingOriginOffset;
                break;
            case SEATED_FREE_MOVE:
                this.seatedFreeMove = !this.seatedFreeMove;
                break;
            case FORCE_STANDING_FREE_MOVE:
            	this.forceStandingFreeMove = !this.forceStandingFreeMove;
            	break;
            case ALLOW_ADVANCED_BINDINGS:
            	this.allowAdvancedBindings = !this.allowAdvancedBindings;
            	break;
            case MENU_WORLD_SELECTION:
                switch (this.menuWorldSelection) {
                    case MENU_WORLD_BOTH:
                        this.menuWorldSelection = MENU_WORLD_CUSTOM;
                        break;
                    case MENU_WORLD_CUSTOM:
                        this.menuWorldSelection = MENU_WORLD_OFFICIAL;
                        break;
                    case MENU_WORLD_OFFICIAL:
                        this.menuWorldSelection = MENU_WORLD_BOTH;
                        break;
                }
                break;
            default:
            	break;
    	}

        this.saveOptions();
    }

    public void setOptionFloatValue(VRSettings.VrOptions par1EnumOptions, float par2)
    {
    	switch( par1EnumOptions ) {
	        case MOVEMENT_MULTIPLIER:
	            this.movementSpeedMultiplier = par2;
	            break;
	        case HUD_SCALE:
	            this.hudScale = par2;
	        	break;
	        case HUD_OPACITY:
	            this.hudOpacity = par2;
	        	break;
	        case HUD_DISTANCE:
	            this.hudDistance = par2;
	        	break;
	        case HUD_PITCH:
	            this.hudPitchOffset = par2;
	        	break;
            case HUD_YAW:
                this.hudYawOffset = par2;
                break;
	        case CROSSHAIR_SCALE:
	            this.crosshairScale = par2;
	        	break;
            case MENU_CROSSHAIR_SCALE:
                this.menuCrosshairScale = par2;
                break;
            case WALK_MULTIPLIER:
                this.walkMultiplier=par2;
                break;
            // VIVE START - new options
            case WORLD_SCALE:
            	if(par2 ==  0) vrWorldScale = 0.1f;
            	else if(par2 ==  1) vrWorldScale = 0.25f;
            	else if(par2 >=  2 && par2 <=  17) vrWorldScale = (float) (par2 * 0.1 + 0.3);
            	else if(par2 == 18) vrWorldScale = 3f;
            	else if(par2 == 19) vrWorldScale = 4f;
            	else if(par2 == 20) vrWorldScale = 6f;
            	else if(par2 == 21) vrWorldScale = 8f;
            	else if(par2 == 22) vrWorldScale = 10f;
            	else if(par2 == 23) vrWorldScale = 12f;
            	else if(par2 == 24) vrWorldScale = 16f;
            	else if(par2 == 25) vrWorldScale = 20f;
               	else if(par2 == 26) vrWorldScale = 30f;
               	else if(par2 == 27) vrWorldScale = 50f;
               	else if(par2 == 28) vrWorldScale = 75f;
               	else if(par2 == 29) vrWorldScale = 100f;           	         	
            	else vrWorldScale = 1;           	
                break;
            case WORLD_ROTATION:
                this.vrWorldRotation = par2;
                MCOpenVR.seatedRot = par2;
                break;
            case WORLD_ROTATION_INCREMENT:
            	this.vrWorldRotation = 0;
            	if(par2 == -1f) this.vrWorldRotationIncrement =  0f;
            	if(par2 == 0f) this.vrWorldRotationIncrement =  10f;
            	if(par2 == 1f) this.vrWorldRotationIncrement =  36f;            	
            	if(par2 == 2f) this.vrWorldRotationIncrement =  45f;
            	if(par2 == 3f) this.vrWorldRotationIncrement =  90f;
            	if(par2 == 4f) this.vrWorldRotationIncrement =  180f;
                break;
            case X_SENSITIVITY:
                this.xSensitivity=par2;
                break;
            case Y_SENSITIVITY:
                this.ySensitivity=par2;
                break;
            case KEYHOLE:
            	this.keyholeX=par2;
            	break;
            case MONO_FOV:
            	this.mc.gameSettings.fov = par2;
            	break;
	        case MIXED_REALITY_FOV:
	            this.mixedRealityFov = par2;
	        	break;
            case RENDER_SCALEFACTOR:
            	this.renderScaleFactor = par2;
            	break;
            case TELEPORT_DOWN_LIMIT:
            	this.vrTeleportDownLimit = (int) par2;
            	break;
            case TELEPORT_UP_LIMIT:
            	this.vrTeleportUpLimit = (int) par2;
            	break;
            case TELEPORT_HORIZ_LIMIT:
            	this.vrTeleportHorizLimit = (int) par2;
            	break;
            case AUTO_SPRINT_THRESHOLD:
                this.autoSprintThreshold = par2;
            	// VIVE END - new options
            default:
            	break;
    	}
	
        this.saveOptions();
    }



    public void saveOptions()
    {
        saveOptions(null); // Use null for current profile
    }

    private void storeDefaults()
    {
        saveOptions(this.defaults);
    }

    private void saveOptions(JSONObject theProfiles)
    {
        // Save Minecrift settings
        try
        {
            ProfileWriter var5 = new ProfileWriter(ProfileManager.PROFILE_SET_VR, theProfiles);

            var5.println("version:" + version);
            var5.println("newlyCreated:" + false );
            //var5.println("firstLoad:" + this.firstLoad );  
            var5.println("playerEyeHeight:" + this.playerEyeHeight);
            var5.println("stereoProviderPluginID:"+ this.stereoProviderPluginID);
            var5.println("badStereoProviderPluginID:"+ this.badStereoProviderPluginID);
            var5.println("hudOpacity:" + this.hudOpacity);
            var5.println("menuBackground:" + this.menuBackground);
            var5.println("renderFullFirstPersonModelMode:" + this.renderFullFirstPersonModelMode);
            var5.println("shaderIndex:" + this.shaderIndex);
            var5.println("displayMirrorMode:" + this.displayMirrorMode);
            var5.println("mixedRealityKeyColor:" + this.mixedRealityKeyColor.getRed() + "," + this.mixedRealityKeyColor.getGreen() + "," + this.mixedRealityKeyColor.getBlue());
            var5.println("mixedRealityRenderHands:" + this.mixedRealityRenderHands);
            var5.println("mixedRealityUnityLike:" + this.mixedRealityUnityLike);
            var5.println("mixedRealityUndistorted:" + this.mixedRealityMRPlusUndistorted);
            var5.println("mixedRealityAlphaMask:" + this.mixedRealityAlphaMask);
            var5.println("mixedRealityFov:" + this.mixedRealityFov);
            var5.println("insideBlockSolidColor:" + this.insideBlockSolidColor);
            var5.println("walkUpBlocks:" + this.walkUpBlocks);
            var5.println("headHudScale:" + this.hudScale);
            var5.println("renderScaleFactor:" + this.renderScaleFactor);
            var5.println("vrHudLockMode:" + this.vrHudLockMode);
            var5.println("hudDistance:" + this.hudDistance);
            var5.println("hudPitchOffset:" + this.hudPitchOffset);
            var5.println("hudYawOffset:" + this.hudYawOffset);
            var5.println("useFsaa:" + this.useFsaa);
            var5.println("movementSpeedMultiplier:" + this.movementSpeedMultiplier);
            var5.println("renderInGameCrosshairMode:" + this.renderInGameCrosshairMode);
            var5.println("renderBlockOutlineMode:" + this.renderBlockOutlineMode);
            var5.println("hudOcclusion:" + this.hudOcclusion);
            var5.println("menuAlwaysFollowFace:" + this.menuAlwaysFollowFace);
            var5.println("useCrosshairOcclusion:" + this.useCrosshairOcclusion);
            var5.println("crosshairScale:" + this.crosshairScale);
            var5.println("menuCrosshairScale:" + this.menuCrosshairScale);
            var5.println("crosshairScalesWithDistance:" + this.crosshairScalesWithDistance);
            var5.println("inertiaFactor:" + this.inertiaFactor);
            var5.println("smoothRunTickCount:" + this.smoothRunTickCount);
            var5.println("smoothTick:" + this.smoothTick);
            //VIVE
            var5.println("simulateFalling:" + this.simulateFalling);
            var5.println("weaponCollision:" + this.weaponCollision);
            var5.println("animalTouching:" + this.animaltouching);
            //END VIVE
            
            //JRBUDDA
            var5.println("allowCrawling:" + this.vrAllowCrawling);
            var5.println("limitedTeleport:" + this.vrLimitedSurvivalTeleport);
            var5.println("reverseHands:" + this.vrReverseHands);
            var5.println("stencilOn:" + this.vrUseStencil);
            var5.println("bcbOn:" + this.vrShowBlueCircleBuddy);
            var5.println("worldScale:" + this.vrWorldScale);
            var5.println("worldRotation:" + this.vrWorldRotation);
            var5.println("vrWorldRotationIncrement:" + this.vrWorldRotationIncrement);
            var5.println("vrFixedCamposX:" + this.vrFixedCamposX);
            var5.println("vrFixedCamposY:" + this.vrFixedCamposY);
            var5.println("vrFixedCamposZ:" + this.vrFixedCamposZ);
            var5.println("vrFixedCamrotW:" + this.vrFixedCamrotQuat.w);
            var5.println("vrFixedCamrotX:" + this.vrFixedCamrotQuat.x);
            var5.println("vrFixedCamrotY:" + this.vrFixedCamrotQuat.y);
            var5.println("vrFixedCamrotZ:" + this.vrFixedCamrotQuat.z);
            var5.println("mrMovingCamOffsetX:" + this.mrMovingCamOffsetX);
            var5.println("mrMovingCamOffsetY:" + this.mrMovingCamOffsetY);
            var5.println("mrMovingCamOffsetZ:" + this.mrMovingCamOffsetZ);
            var5.println("mrMovingCamOffsetRotW:" + this.mrMovingCamOffsetRotQuat.w);
            var5.println("mrMovingCamOffsetRotX:" + this.mrMovingCamOffsetRotQuat.x);
            var5.println("mrMovingCamOffsetRotY:" + this.mrMovingCamOffsetRotQuat.y);
            var5.println("mrMovingCamOffsetRotZ:" + this.mrMovingCamOffsetRotQuat.z);
            var5.println("vrTouchHotbar:" + this.vrTouchHotbar);
            var5.println("seatedhmd:" + this.seatedUseHMD);
            var5.println("seatedHudAltMode:" + this.seatedHudAltMode);
            var5.println("seated:" + this.seated);
            var5.println("jumpThreshold:" + this.jumpThreshold);
            var5.println("sneakThreshold:" + this.sneakThreshold);
            var5.println("realisticJumpEnabled:" + this.realisticJumpEnabled);
            var5.println("realisticSwimEnabled:" + this.realisticSwimEnabled);
            var5.println("realisticClimbEnabled:" + this.realisticClimbEnabled);
            var5.println("realisticRowEnabled:" + this.realisticRowEnabled);
            var5.println("realisticSneakEnabled:" + this.realisticSneakEnabled);
			var5.println("physicalGuiEnabled:"+this.physicalGuiEnabled);
            var5.println("headToHmdLength:" + this.headToHmdLength);
            var5.println("walkMultiplier:" + this.walkMultiplier);
            var5.println("vrFreeMoveMode:" + this.vrFreeMoveMode);
            var5.println("xSensitivity:" + this.xSensitivity);
            var5.println("ySensitivity:" + this.ySensitivity);
            var5.println("keyholeX:" + this.keyholeX);
            var5.println("autoCalibration:" + this.autoCalibration);
            var5.println("manualCalibration:" + this.manualCalibration);
            var5.println("vehicleRotation:" + this.vehicleRotation);
            var5.println("fovReduction:" + this.useFOVReduction);
            var5.println("alwaysSimulateKeyboard:" + this.alwaysSimulateKeyboard);
            var5.println("autoOpenKeyboard:" + this.autoOpenKeyboard);
            var5.println("forceHardwareDetection:" + this.forceHardwareDetection);
            var5.println("backpackSwitching:" + this.backpackSwitching);
            var5.println("analogMovement:" + this.analogMovement);
            var5.println("hideGUI:" + this.mc.gameSettings.hideGUI);
            var5.println("bowMode:" + this.bowMode);
            var5.println("keyboardKeys:" + this.keyboardKeys);
            var5.println("keyboardKeysShift:" + this.keyboardKeysShift);
            var5.println("radialModeHold:" + this.radialModeHold);
            var5.println("physicalKeyboard:" + this.physicalKeyboard);
            var5.println("originOffset:" + MCOpenVR.offset.getX() + "," + MCOpenVR.offset.getY() + "," + MCOpenVR.offset.getZ());
            var5.println("allowStandingOriginOffset:" + this.allowStandingOriginOffset);
            var5.println("seatedFreeMove:" + this.seatedFreeMove);
            var5.println("forceStandingFreeMove:" + this.forceStandingFreeMove);
            var5.println("allowAdvancedBindings:" + this.allowAdvancedBindings);
            var5.println("menuWorldSelection:" + this.menuWorldSelection);
            var5.println("firstRun:" + this.firstRun);
            
            if (vrQuickCommands == null) vrQuickCommands = getQuickCommandsDefaults(); //defaults
            
            for (int i = 0; i < 11 ; i++){
            	var5.println("QUICKCOMMAND_" + i + ":" + vrQuickCommands[i]);
            }
   
            if (vrRadialItems == null) 
            	vrRadialItems = getRadialItemsDefault(); //defaults           
            for (int i = 0; i < 8 ; i++){
            	var5.println("RADIAL_" + i + ":" + vrRadialItems[i]);
            }
            
            if (vrRadialItemsAlt == null) 
            	vrRadialItemsAlt = new String[8]; //defaults           
            for (int i = 0; i < 8 ; i++){
            	var5.println("RADIALALT_" + i + ":" + vrRadialItemsAlt[i]);
            }

            //END JRBUDDA
            var5.close();
        }
        catch (Exception var3)
        {
            logger.warn("Failed to save VR options: " + var3.getMessage());
            var3.printStackTrace();
        }
    }

  
    public void setMinecraftPlayerEyeHeight(float eyeHeight)
    {
        this.playerEyeHeight = eyeHeight;
    }

    public float getMinecraftPlayerEyeHeight(){
        return playerEyeHeight;
    }



    /**
     * Parses a string into a float.
     */
    private float parseFloat(String par1Str)
    {
        return par1Str.equals("true") ? 1.0F : (par1Str.equals("false") ? 0.0F : Float.parseFloat(par1Str));
    }

    public float getHeadTrackSensitivity()
    {
        //if (this.useQuaternions)
            return 1.0f;

        //return this.headTrackSensitivity;  // TODO: If head track sensitivity is working again... if
    }

    public static double getInertiaAddFactor(int inertiaFactor)
    {
        float addFac = INERTIA_NORMAL_ADD_FACTOR;
        switch (inertiaFactor)
        {
            case INERTIA_NONE:
                addFac = INERTIA_NONE_ADD_FACTOR;
                break;
            case INERTIA_LARGE:
                addFac = INERTIA_LARGE_ADD_FACTOR;
                break;
            case INERTIA_MASSIVE:
                addFac = INERTIA_MASSIVE_ADD_FACTOR;
                break;
        }
        return addFac;
    }


    public static enum VrOptions
    {
        HUD_SCALE("Head HUD Size", true, false,0.35f,2.5f,0.01f,new String[] {
                "Relative size HUD takes up in field-of-view",
                "  The units are just relative, not in degrees",
                "  or a fraction of FOV or anything"
        }),
        HUD_DISTANCE("Head HUD Distance", true, false,0.25f,5.0f,0.01f,new String[] {
                "Distance the floating HUD is drawn in front of your body",
                "  The relative size of the HUD is unchanged by this",
                "  Distance is in meters (though isn't obstructed by blocks)"
        }),
        HUD_PITCH("HUD Vertical Offset", true, false, -45f, 45f, 1f, new String[] {
                "The vertical offset of the HUD, in degrees.",
                "  Negative values are down, positive up."
        }),
        HUD_YAW("HUD Horiz. Offset", true, false, -100f, 100f, 1f, new String[] {
                "The horizontal offset of the HUD, in degrees.",
                "  Negative values are to the left, positive to",
                "  the right."
        }),
        HUD_LOCK_TO("HUD Orientation Lock", false, true, new String[] {
                "Specifies to which orientation the HUD is locked to.",
                "  HAND:  The HUD will appear just above your off-hand",
                "  HEAD:  The HUD will always appear in your field of view",
                "straight ahead",
                "  WRIST:  The HUD will appear on the inside of your off-hand",
                "arm. It will 'pop out' when looked at."
        }),
        HUD_OPACITY("HUD Opacity", true, false,0.15f, 1.0f, 0.05f, new String[] {
                "How transparent to draw the in-game HUD and UI",
        }),
        HUD_HIDE("Hide HUD (F1)", false, true, null),
        RENDER_MENU_BACKGROUND("HUD/GUI Background", false, true,new String[] {
                "Specifies whether the in game GUI menus have a ",
                "semi-transparent background or not.",
                "  ON:  Semi-transparent background on in-game menus.",
                "  OFF: No background on in-game menus."
        }),
        HUD_OCCLUSION("HUD Occlusion", false, true,new String[] {
                "Specifies whether the HUD is occluded by closer objects.",
                "  ON:  The HUD will be hidden by closer objects. May",
                "       be hidden completely in confined environments!",
                "  OFF: The HUD is always visible. Stereo depth issues",
                "       may be noticable."
        }),
        MENU_ALWAYS_FOLLOW_FACE("Main Menu Follow", false, true,new String[] {
                "Specifies when the main menu follows your look direction.",
                "  SEATED: The main menu will only follow in seated mode.",
                "  ALWAYS The main menu will always follow."
        }),
        CROSSHAIR_OCCLUSION("Crosshair Occlusion", false, true,null),
        CHAT_FADE_AWAY("Chat Persistence", false, true,null),
        DUMMY("Dummy", false, true,null),
        DUMMY_SMALL("Dummy", false, true,null),
        VR_RENDERER("Stereo Renderer", false, true,null),
        VR_HEAD_ORIENTATION("Head Orientation", false, true,null),
        VR_HEAD_POSITION("Head Position", false, true,null),
        VR_CONTROLLER("Controller", false, true,null),
        CROSSHAIR_SCALE("Crosshair Size", true, false, 0.25f, 1.0f, 0.01f, new String[] {
                "Sets the size of the in-game crosshair"
        }),
        MENU_CROSSHAIR_SCALE("Menu Crosshair Size", true, false,0.25f, 2.5f, 0.05f, new String[] {
                "Sets the size of the menu crosshair"
        }),
        RENDER_CROSSHAIR_MODE("Show Crosshair", false, true,new String[] {
                "Set the in-game crosshair display mode",
                "  Always:   The crosshair is always shown even if the",
                "            HUD is disabled",
                "  With HUD: The crosshair is only shown when the HUD",
                "            is enabled",
                "  Never:    The crosshair is never shown"
        }),
        CROSSHAIR_ROLL("Roll Crosshair", false, true,new String[] {
                "Sets the crosshair roll behaviour.",
                "  With Head: The crosshair rolls with your head.",
                "  With HUD:  The crosshair appears to roll, keeping",
                "             the same orientation as the HUD."
        }),
        CROSSHAIR_SCALES_WITH_DISTANCE("Crosshair Scaling", false, true,new String[] {
                "Determines how the crosshair changes with distance.",
                "  ON:   The crosshair will grow larger in the distance to",
                "   remain the same apparent size from your viewpoint.",
                "  OFF: The crosshair will always be the same size",
                "   in the world and appear to shrink with distance."
        }),
        RENDER_BLOCK_OUTLINE_MODE("Show Block Outline", false, true,new String[] {
                "Sets the in-game block outline display mode.",
                "  Always:   The block outline is always shown even if",
                "            the HUD is disabled",
                "  With HUD: The block outline is only shown when the",
                "            HUD is enabled",
                "  Never:    The block outline is never shown"
        }),
        LOAD_MUMBLE_LIB("Load Mumble Lib", false, true,null),
        RENDER_OWN_HEADWEAR("Render Own Headwear", false, true,null),
        RENDER_FULL_FIRST_PERSON_MODEL_MODE("First Person Model", false, true,null),
        RENDER_PLAYER_OFFSET("View Body Offset", true, false,0,1,0.1f,null),
        AUTO_OPEN_KEYBOARD("Always Open Keyboard", false, true,new String[] {
        		"If disabled, the keyboard will only open when you",
        		"click a text field, or if a text field can't lose focus.",
        		"",
                "If enabled, the keyboard will open automatically",
                "any time a text field comes into focus. Enabling this will",
                "cause it to open in unwanted situations with mods."
        }),
        RADIAL_MODE_HOLD("Radial Menu Mode", false, true, new String[] {
                "HOLD: Hold radial menu button, hover over",
                "selection and release.",
                "PRESS: Press radial menu button, click buttons,",
                "press menu button again to dismiss."
        }),
        PHYSICAL_KEYBOARD("Keyboard Type", false, true, new String[] {
                "Which type of VR keyboard to use for typing.",
                "Physical: A keyboard which you can type on like",
                "a real keyboard.",
                "Pointer: A keyboard which you can use by pointing",
                "either controller's crosshair at and pressing buttons."
        }),


        //HMD/render
        FSAA("FSAA", false, true,new String[] {
                "Uses a fancier method of resampling the",
                "game before sending it to the HMD. Works best",
                "at high render scales. "}),
        MIRROR_DISPLAY("Mirror Display", false, true,new String[] {
                "Mirrors image on HMD to separate desktop window.",
                "Can be set to OFF, single or dual hmd-view, ",
                "first-person undistorted, third person undistorted",
                "and Mixed Reality. The undistorted and MR views have",
                "a performance cost"
        }),
        MIXED_REALITY_KEY_COLOR("Key Color", false, false,new String[] {
                "The color drawn to the \"transparent\" areas of the",
                "mixed reality view. Other colors in-game will be",
                "prevented from matching this so it doesn't cause",
                "weirdness."
        }),
        MIXED_REALITY_RENDER_HANDS("Show Hands", false, true,new String[] {
                "Render hands on the mixed reality view. Only",
                "toggles rendering of the actual hand models, items",
                "will still be rendered."
        }),
        MIXED_REALITY_UNITY_LIKE("Layout", false, true,new String[] {
                "Choose between Unity-style 4-pane layout, or 2-pane",
                "side by side"
        }),
        MIXED_REALITY_UNDISTORTED("Undistorted Pass", false, true,new String[] {
                "Include an undistorted view in 4-pane layout Requires",
                "an extra render pass. Otherwise the HMD view will be used."
        }),
        MIXED_REALITY_ALPHA_MASK("Alpha Mask", false, true,new String[] {
                "In Unity layout, if yes, will draw a grayscale alpha mask",
                "to the upper-right quadrant (like Unity) for use in",
                "maskingthe foreground layer. Otherwise, foreground will",
                "be drawn with key color for use with color key effect."
        }),
        MIXED_REALITY_FOV("Camera FOV", true, false, 0, 179, 1, new String[] {
        		"The FOV used for the mixed reality mirror mode."
        }),
        
        INSIDE_BLOCK_SOLID_COLOR("Inside Block", false, true,new String[] {
                "Whether to render the block texture or a solid",
                "color when eye is inside of a block."
        }),
        WALK_UP_BLOCKS("Walk up blocks", false, true,new String[] {
                "Allows you to set the ability to walk up blocks without",
                "having to jump. HOTKEY - RCtrl-B",
                "WARNING: May trigger anti-cheat warnings if on a",
                "Multiplayer server!!",
                "  OFF: (Default) You will have to jump up blocks.",
                "  ON:  You can walk up single blocks. May reduce",
                "       locomotion induced simulator sickness for some."
        }),
        //Movement/aiming controls
        DECOUPLE_LOOK_MOVE("Decouple Look/Move", false, true,null),
        MOVEMENT_MULTIPLIER("Move. Speed Multiplier", true, false, 0.15f, 1.3f, 0.01f, new String[] {
                "Sets a movement multiplier, allowing slower movement",
                "than default. This may help reduce locomotion induced",
                "simulator sickness.",
                "WARNING: May trigger anti-cheat warnings if on a",
                "Multiplayer server!!",
                "Defaults to standard Minecraft movement (1.0)",
                "speed)."
        }),
        STRAFE_MULTIPLIER("Strafe Speed Multiplier", true, false, 0f, 1.0f, 0.01f, new String[] {
                "Sets an additional strafe (side-to-side) movement",
                "multiplier. This is applied on top of the movement",
                "multiplier. A value of zero will disable strafe.",
                "This may help reduce locomotion induced simulator",
                "sickness. WARNING: May trigger anti-cheat warnings",
                "if on a Multiplayer server!!",
                "Defaults to 0.33 (1.0 is standard Minecraft movement",
                "speed)."
        }),
        PITCH_AFFECTS_CAMERA("Pitch Affects Camera", false, true,null),
        JOYSTICK_DEADZONE("Joystick Deadzone",true,false,0,1,0.1f,null),
        KEYHOLE_HEAD_RELATIVE("Keyhole Moves With Head",false,true,null),
        MOUSE_AIM_TYPE("Aim Type",false,true,null),
        CROSSHAIR_HEAD_RELATIVE("Cursor Relative To",false,true,null),
        MOVEAIM_HYDRA_USE_CONTROLLER_ONE("Controller", false, true,null),
        JOYSTICK_AIM_TYPE("Aim Type", false, false,null),
        AIM_PITCH_OFFSET("Vertical Cursor Offset",true,false,0,1,0.1f,null),
        INERTIA_FACTOR("Player Inertia",false,true,new String[]{
                "Sets the player's movement inertia in single player",
                "mode. Lower inertia means faster acceleration, higher",
                "inertia slower accelaration. High inertia may reduce",
                "motion sickness for some, but beware of cliff edges!!",
                "  Normal: (Default) Standard Minecraft player",
                "           movement.",
                "  Automan < Normal < A lot < Even More. Does not",
                "  affect lava, water or jumping movement currently."
        }),

        // VIVE START - new options
        SIMULATE_FALLING("Simulate falling", false, true,new String[] {
                "If enabled the player will falls to the ground in TP mode",
                "when standing above empty space. Also allows jumping"
        }),
        WEAPON_COLLISION("Weapon collision", false, true,new String[] {
                "If enabled, you can swing your pickaxe at blocks to",
                "mine them, or your sword at enemies to hit them."
        }),
        ANIMAL_TOUCHING("Animal Interaction", false, true,new String[] {
                "If enabled, touching a passive mob (animal) without a",
                "weapon will right-click (interact) instead of attacking.",
                "Turn off for Piggy Slapping, Josh.",
        }),
        // VIVE END - new options

        //JRBUDDA VIVE
        ALLOW_CRAWLING("Allow crawling",false, true,new String[] {
                "If enabled the player will be able to duck under block"
        }),
        LIMIT_TELEPORT("Limit in Survival",false, true,new String[] {
                "If enabled the arc teleporter will be have restrictions",
                "in survival mode. It will not be able to jump up the side", 
                "of blocks, it will consume food, and it will have an energy",
                "bar that refills over time."
        }),
        REVERSE_HANDS("Reverse Hands",false, true,new String[] {
				"Swap left/right hands as dominant",
				"  ON: Left dominant, weirdo.",
				"  OFF: Right dominant"
    		}),
        STENCIL_ON("Use Eye Stencil", false, true,new String[] {
                "Mask out areas of the screen outside the FOV.",
                "Improves performance."
        }), 
        BCB_ON("Show Body Position", false, true,new String[] {
                "Shows your body position as a square shadow on the ground.",
                "This is your Square Shadow Buddy (tm).",
                "Do not lose your Square Shadow Buddy."
        }),    
        WORLD_SCALE("World Scale", true, false, 0, 29, 1, new String[] {
                "Scales the player in the world.",
                "Above one makes you larger",
                "And below one makes you small",
                "And the ones that mother gives you",
                "don't do anything at all."
        }),
        WORLD_ROTATION("World Rotation", true, false, 0, 360, 30, new String[] {
                "Adds extra rotation to your HMD.",
                "More useful bound to a button or ",
                "changed with the arrow keys."
        }),
        WORLD_ROTATION_INCREMENT("Rotation Increment", true, false,-1, 4, 1, new String[] {
                "How many degrees to rotate when",
                "rotating the world."             
        }),
        TOUCH_HOTBAR("Touch Hotbar Enabled", false, true,new String[] {
                "If enabled allow you to touch the hotbar with",
                "your main hand to select an item."
        }),
        PLAY_MODE_SEATED("Play Mode", false, true,new String[] {
                "Standing or seated play mode",
                "Standing is vastly superior."           
        }),
        RENDER_SCALEFACTOR("Render Scale Factor", true, false, 0.5f, 4, 0.1f, new String[] {
                "The internal rendering scale of the game, relative",
                "to the native HMD display. Higher values improve visual",
                "quality, espeically with FSAA on, at the cost of performance"
        }),
        MONO_FOV("Undistorted FOV", true, false, 0, 179, 1, new String[] {
                "The FOV used for the undistorted mirror mode."
        }),
        //END JRBUDDA
        REALISTIC_JUMP("Roomscale Jumping",false,true,new String[]{
                "If turned on, once you jump in real life",
                "Your player will also jump. Also enables",
                "Jump Boots."
        }),
        REALISTIC_SNEAK("Roomscale Sneaking",false,true,new String[]{
        		"If turned on, once you duck in real life",
                "Your player will also sneak"
        }),
		PHYSICAL_GUI("Physical GUIs",false,true,new String[]{
				"If turned on, GUIs will be replaced",
				"with 3d interactable models",
				"If Backpack tracking is enabled, reaching on your back",
				"will bring out your inventory bag"
		}),
        REALISTIC_CLIMB("Roomscale Climbing",false,true,new String[]{
                "If turned on, allow climbing ladders and vines",
                "by touching them. Also enables Climb Claws."
        }),
        REALISTIC_SWIM("Roomscale Swimming",false,true,new String[]{
                "If turned on, allow swimming by doing the breaststoke",
                "with the controllers."
        }),
        REALISTIC_ROW("Roomscale Rowing",false,true,new String[]{
                "Row, row, row your boat... by flapping your arms like mad."
        }),
        CALIBRATE_HEIGHT("Calibrate Height",false,true,null),
        WALK_MULTIPLIER("Walking Multipier",true,false,1f, 10f, 0.1f, new String[]{
                "Multiplies your position in the room by a factor",
                "Allows you to walk around more,",
                "but may cause motion sickness"
        }),
        FREEMOVE_MODE("Free Move Type", false, true,new String[] {
                "The source for freemove direction.","",
                "Controller: Offhand controller pointing direction",
                "HMD: Headset look direction",
                "Run In Place:",
                "    Direction is based on how controllers are swinging."
    	}),
        VEHICLE_ROTATION("Vehicle Rotation",false,true,new String[] {
                "Riding in a vehicle will rotate the world",
                "as the vehicle rotates. May be disorienting."
                
        }),
        //SEATED
        RESET_ORIGIN("Reset Origin",false,true,new String[] {
                "Recenter the player's feet in the world to 1.62m below",
                "the current HMD position. For non-absolute tracking",
                "systems or seated play."
        }),
        X_SENSITIVITY("Rotation Speed",true,false, 0.1f, 5f, 0.01f, new String[] {
                "Speed the view will rotate when pushed on the edge of the keyhole"
        }),
        Y_SENSITIVITY("Y Sensitivity",true,false, 0.1f, 5f, 0.01f, new String[] {
                "Vertical speed of the crosshair related to the mouse"
        }),
        KEYHOLE("Keyhole",true,false, 0f, 40f, 5f, new String[] {
                "The number of degrees to the left and right of center",
                "Where the view will begin to rotate."
        }),
        FOV_REDUCTION("FOV Comfort Reduction",false,true,new String[] {
                "Shrinks the field of view while moving. Can help with",
                "motion sickness."
        }),
        // OTher buttons
        OTHER_HUD_SETTINGS("Overlay/Crosshair/Chat...", false, true, new String[] {
                "Configure Crosshair and overlay settings."
        }),
        OTHER_RENDER_SETTINGS("IPD / FOV...", false, true,new String[] {
                "Configure IPD and FOV border settings."
        }),
        LOCOMOTION_SETTINGS("Locomotion Settings...", false, true, null),
        SEATED_HMD("Forward Direction",false,true,new String[] {
                "The direction the forward (W) key will go. You can ",
                "HMD view direction or crosshair pointing direction"
        }),
        SEATED_HUD_XHAIR("HUD Follows",false,true,new String[] {
                "The direction the HUD will be placed.",
                "HMD view direction or crosshair pointing direction"
        }), 
        BACKPACK_SWITCH("Backpack Switching",false,true,new String[]{
                "If turned on, reaching behind your head with the right",
                "controller will swap to the 1st hotbar slot, or back to the",
                "previous slot. Doing the same with the left controller will",
                "swap the left and right hand items."
        }),
        ANALOG_MOVEMENT("Analog Movement",false,true,new String[] {
                "Walking speed will be determined by the controller button",
                "axis, if the bound button has a variable axis."    ,"",
                "For full analog control it is better to use 'Move/Strafe'", "or 'Move/Rotate'."
        }),
        AUTO_SPRINT("Auto-sprint", false, true, new String[] {
                "While using freemove, sprint will automatically activate",
                "when the axis reaches the configured threshold."
        }),
        AUTO_SPRINT_THRESHOLD("Auto-sprint Threshold", true, false, 0.5f, 1f, 0.01f, new String[] {
                "The axis threshold at which auto-sprint activates."
        }), 
        BOW_MODE("Roomscale Bow Mode", false, true,new String[]{
                "Sets when to use Roomscale Archery",
                "OFF: Never",
                "Vanilla: Only for the vanilla bow, no mod items",
                "ON: Always for any item that uses the 'bow' action"                        
        }),
        TELEPORT_DOWN_LIMIT("Down Limit", true, false, 0, 16, 1, new String[] {
                "Limit the number of blocks you can teleport below you"
        }),
        TELEPORT_UP_LIMIT("Up Limit", true, false, 0, 4, 1,  new String[] {
                "Limit the number of blocks you can teleport above you"
        }),
        TELEPORT_HORIZ_LIMIT("Distance Limit", true, false, 0, 32, 1, new String[] {
                "Limit the number of blocks you can teleport sideways you"
        }),
        ALLOW_STANDING_ORIGIN_OFFSET("Allow Origin Offset", false, true, new String[] {
                "Allows the \"Reset Origin\" button to be used in",
                "standing mode, for those that wish to play physically",
                "seated while using tracked controllers."
        }),
        SEATED_FREE_MOVE("Movement Type", false, true, new String[] {
        		"Which locomotion mode to use in seated mode.",
        		"",
        		"Teleport: Press any direction to activate.",
        		"Free Move: WASD movement like vanilla Minecraft."
        }),
        FORCE_STANDING_FREE_MOVE("Force Free Move", false, true, new String[] {
        		"Forces the use of the fallback walk forwards",
        		"binding (left trigger by default). For more movement",
        		"options, edit the SteamVR controller bindings.",
        		"",
        		"Note that this disables the teleport binding."
        }),
        ALLOW_ADVANCED_BINDINGS("Show Advanced Bindings", false, true, new String[] {
        		"Unhides additional SteamVR bindings for climbey,",
        		"keyboard and mixed reality.",
        		"",
        		"Requires a restart to take effect."
        }),
        MENU_WORLD_SELECTION("Menu Worlds", false, false, new String[] {
                "Which menu worlds to load on startup.",
                "",
                "If no custom worlds are found, official worlds",
                "will be used regardless."
        });
//        ANISOTROPIC_FILTERING("options.anisotropicFiltering", true, false, 1.0F, 16.0F, 0.0F)
//                {
//                    private static final String __OBFID = "CL_00000654";
//                    protected float snapToStep(float p_148264_1_)
//                    {
//                        return (float) MathHelper.roundUpToPowerOfTwo((int) p_148264_1_);
//                    }
//                },

        private final boolean enumFloat;
        private final boolean enumBoolean;
        private final String enumString;
        private final float valueStep;
        private float valueMin;
        private float valueMax;
        private String[] tooltip;
        
        public static VRSettings.VrOptions getEnumOptions(int par0)
        {
            VRSettings.VrOptions[] aoptions = values();
            int j = aoptions.length;

            for (int k = 0; k < j; ++k)
            {
                VRSettings.VrOptions options = aoptions[k];

                if (options.returnEnumOrdinal() == par0)
                {
                    return options;
                }
            }

            return null;
        }

        private VrOptions(String par3Str, boolean isfloat, boolean isbool, String[] tooltip)
        {
            this(par3Str, isfloat, isbool, 0.0F, 1.0F, 0.0F, tooltip);
        	//TEMP
        	if (isfloat) {
        		int insertbreakpointhere = 0;
        	}
        }

        private VrOptions(String name, boolean isfloat, boolean isboolean, float min, float max, float step, String[] tooltip)
        {
            this.enumString = name;
            this.enumFloat = isfloat;
            this.enumBoolean = isboolean;
            this.valueMin = min;
            this.valueMax = max;
            this.valueStep = step;
            this.tooltip = tooltip;
        }
        
        public boolean getEnumFloat()
        {
            return this.enumFloat;
        }

        public boolean getEnumBoolean()
        {
            return this.enumBoolean;
        }

        public int returnEnumOrdinal()
        {
            return this.ordinal();
        }

        public String getEnumString()
        {
            return this.enumString;
        }

        public float getValueMax()
        {
            return this.valueMax;
        }
        
        public float getValueMin()
        {
            return this.valueMin;
        }
        
        public String[] getToolTip()
        {
            return this.tooltip;
        }

        public void setValueMax(float p_148263_1_)
        {
            this.valueMax = p_148263_1_;
        }

        protected float snapToStep(float p_148264_1_)
        {
            if (this.valueStep > 0.0F)
            {
                p_148264_1_ = this.valueStep * (float)Math.round(p_148264_1_ / this.valueStep);
            }

            return p_148264_1_;
        }
        
        public double normalizeValue(float value)
        {
            return MathHelper.clamp((this.snapToStep(value) - this.valueMin) / (this.valueMax - this.valueMin), 0.0D, 1.0D);
        }
        
        public double denormalizeValue(float value)
        {
            return this.snapToStep((float) (this.valueMin + (this.valueMax - this.valueMin) * MathHelper.clamp(value, 0.0D, 1.0D)));
        }
    }

    public static synchronized void initSettings( Minecraft mc, File dataDir )
    {
        ProfileManager.init(dataDir);
        mc.gameSettings = new GameSettings( mc, dataDir );
       // mc.gameSettings.saveOptions();
        mc.vrSettings = new VRSettings( mc, dataDir );
        mc.vrSettings.saveOptions();
    }

    public static synchronized void loadAll( Minecraft mc )
    {
        mc.gameSettings.loadOptions();
        mc.vrSettings.loadOptions();
    }

    public static synchronized void saveAll( Minecraft mc )
    {
        mc.gameSettings.saveOptions();
        mc.vrSettings.saveOptions();
    }

    public static synchronized void resetAll( Minecraft mc )
    {
        mc.gameSettings.resetSettings();
        mc.vrSettings.resetSettings();
    }

    public static synchronized String getCurrentProfile()
    {
        return ProfileManager.getCurrentProfileName();
    }

    public static synchronized boolean profileExists(String profile)
    {
        return ProfileManager.profileExists(profile);
    }

    public static synchronized SortedSet<String> getProfileList()
    {
        return ProfileManager.getProfileList();
    }

    public static synchronized boolean setCurrentProfile(String profile)
    {
        StringBuilder error = new StringBuilder();
        return setCurrentProfile(profile, error);
    }

    public static synchronized boolean setCurrentProfile(String profile, StringBuilder error)
    {
        boolean result = true;
        Minecraft mc = Minecraft.getInstance();

        // Save settings in current profile
        VRSettings.saveAll(mc);

        // Set the new profile
        result = ProfileManager.setCurrentProfile(profile, error);

        if (result) {
            // Load new profile
            VRSettings.loadAll(mc);
        }

        return result;
    }

    public static synchronized boolean createProfile(String profile, boolean useDefaults, StringBuilder error)
    {
        boolean result = true;
        Minecraft mc = Minecraft.getInstance();
        String originalProfile = VRSettings.getCurrentProfile();

        // Save settings in original profile
        VRSettings.saveAll(mc);

        // Create the new profile
        if (!ProfileManager.createProfile(profile, error))
            return false;

        // Set the new profile
        ProfileManager.setCurrentProfile(profile, error);

        // Save existing settings as new profile...

        if (useDefaults) {
            // ...unless set to use defaults
            VRSettings.resetAll(mc);
        }

        // Save new profile settings to file
        VRSettings.saveAll(mc);

        // Select the original profile
        ProfileManager.setCurrentProfile(originalProfile, error);
        VRSettings.loadAll(mc);

        return result;
    }

    public static synchronized boolean deleteProfile(String profile)
    {
        StringBuilder error = new StringBuilder();
        return deleteProfile(profile, error);
    }

    public static synchronized boolean deleteProfile(String profile, StringBuilder error)
    {
        Minecraft mc = Minecraft.getInstance();

        // Save settings in current profile
        VRSettings.saveAll(mc);

        // Nuke the profile data
        if (!ProfileManager.deleteProfile(profile, error))
            return false;

        // Load settings in case the selected profile has changed
        VRSettings.loadAll(mc);

        return true;
    }

    public static synchronized boolean duplicateProfile(String originalProfile, String newProfile, StringBuilder error)
    {
        Minecraft mc = Minecraft.getInstance();

        // Save settings in current profile
        VRSettings.saveAll(mc);

        // Duplicate the profile data
        if (!ProfileManager.duplicateProfile(originalProfile, newProfile, error))
            return false;

        return true;
    }

    public static synchronized boolean renameProfile(String originalProfile, String newProfile, StringBuilder error)
    {
        Minecraft mc = Minecraft.getInstance();

        // Save settings in current profile
        VRSettings.saveAll(mc);

        // Rename the profile
        if (!ProfileManager.renameProfile(originalProfile, newProfile, error))
            return false;

        return true;
    }
    
    public String[] getQuickCommandsDefaults(){
    	
    	String[] out = new String[12];
    	out[0] = "/gamemode survival";
    	out[1] = "/gamemode creative";
    	out[2] = "/help";
    	out[3] = "/home";
    	out[4] = "/sethome";
    	out[5] = "/spawn";
    	out[6] = "hi!";
    	out[7] = "bye!";
    	out[8] = "follow me!";
    	out[9] = "take this!";
    	out[10] = "thank you!";
    	out[11] = "praise the sun!";

    	return out;
    	
    }
    
    public String[] getRadialItemsDefault(){  	
    	String[] out = new String[8];
    	out[0] = "key.drop";
    	out[1] = "key.chat";
    	out[2] = "vivecraft.key.rotateRight";
    	out[3] = "key.pickItem";
    	out[4] = "vivecraft.key.toggleMovement";
    	out[5] = "vivecraft.key.togglePlayerList";
    	out[6] = "vivecraft.key.rotateLeft";
    	out[7] = "vivecraft.key.quickTorch";

    	return out;   	
    }

	public double normalizeValue(float optionFloatValue) {
		// TODO Auto-generated method stub
		return 0;
	}
}

