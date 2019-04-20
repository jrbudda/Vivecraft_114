package org.vivecraft.gameplay.screenhandlers;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import org.vivecraft.api.VRData.VRDevicePose;
import org.vivecraft.control.VRButtonMapping;
import org.vivecraft.provider.MCOpenVR;
import org.vivecraft.utils.InputSimulator;
import org.vivecraft.utils.OpenVRUtil;

import de.fruitfly.ovr.structs.Matrix4f;
import de.fruitfly.ovr.structs.Quatf;
import de.fruitfly.ovr.structs.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiEnchantment;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.GuiScreenLoading;
import net.minecraft.client.gui.GuiWinGame;
import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.client.gui.inventory.GuiBrewingStand;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.gui.inventory.GuiDispenser;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.client.gui.inventory.GuiShulkerBox;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class GuiHandler {
	public static Minecraft mc = Minecraft.getMinecraft();
	//TODO: to hell with all these conversions.
	//sets mouse position for currentscreen

	static boolean lastPressedLeftClick;
	static boolean lastPressedRightClick;
	static boolean lastPressedMiddleClick;
	static boolean lastPressedShift;
	static boolean lastPressedCtrl;
	static boolean lastPressedAlt;

	// For mouse menu emulation
	private static double controllerMouseX = -1.0f;
	private static double controllerMouseY = -1.0f;
	public static boolean controllerMouseValid;
	public static int controllerMouseTicks;

	public static float guiScale = 1.0f;
	public static float guiScaleApplied = 1.0f;
	public static Vec3d IPoint = new Vec3d(0, 0, 0);

	public static Vec3d guiPos_room = new Vec3d(0,0,0);
	public static Matrix4f guiRotation_room = new Matrix4f();

	public static final KeyBinding keyMenuButton = new KeyBinding("GUI Menu Button", GLFW.GLFW_KEY_UNKNOWN, "Vivecraft GUI");
	public static final KeyBinding keyLeftClick = new KeyBinding("GUI Left Click", GLFW.GLFW_KEY_UNKNOWN, "Vivecraft GUI");
	public static final KeyBinding keyRightClick = new KeyBinding("GUI Right Click", GLFW.GLFW_KEY_UNKNOWN, "Vivecraft GUI");
	public static final KeyBinding keyMiddleClick = new KeyBinding("GUI Middle Click", GLFW.GLFW_KEY_UNKNOWN, "Vivecraft GUI");
	public static final KeyBinding keyShift = new KeyBinding("GUI Shift", GLFW.GLFW_KEY_UNKNOWN, "Vivecraft GUI");
	public static final KeyBinding keyCtrl = new KeyBinding("GUI Ctrl", GLFW.GLFW_KEY_UNKNOWN, "Vivecraft GUI");
	public static final KeyBinding keyAlt = new KeyBinding("GUI Alt", GLFW.GLFW_KEY_UNKNOWN, "Vivecraft GUI");
	public static final KeyBinding keyScrollUp = new KeyBinding("GUI Scroll Up", GLFW.GLFW_KEY_UNKNOWN, "Vivecraft GUI");
	public static final KeyBinding keyScrollDown = new KeyBinding("GUI Scroll Down", GLFW.GLFW_KEY_UNKNOWN, "Vivecraft GUI");
	public static Framebuffer guiFramebuffer = null;


	public static void processGui() {
		if(mc.currentScreen == null)return;
		if(mc.vrSettings.seated) return;
		if(guiRotation_room == null) return;

		Vec2f tex = getTexCoordsForCursor(guiPos_room, guiRotation_room, mc.currentScreen, guiScale, mc.vrPlayer.vrdata_room_pre.getController(0));

		float u = tex.x;
		float v = tex.y;

		if (u<0 || v<0 || u>1 || v>1)
		{
			// offscreen
			controllerMouseX = -1.0f;
			controllerMouseY = -1.0f;
		}
		else if (controllerMouseX == -1.0f)
		{
			controllerMouseX = (int) (u * mc.mainWindow.getWidth());
			controllerMouseY = (int) (v * mc.mainWindow.getHeight());
		}
		else
		{
			// apply some smoothing between mouse positions
			float newX = (int) (u * mc.mainWindow.getWidth());
			float newY = (int) (v * mc.mainWindow.getHeight());
			controllerMouseX = controllerMouseX * 0.7f + newX * 0.3f;
			controllerMouseY = controllerMouseY * 0.7f + newY * 0.3f;
		}

		if (controllerMouseX >= 0 && controllerMouseX < mc.mainWindow.getWidth()
				&& controllerMouseY >=0 && controllerMouseY < mc.mainWindow.getHeight())
		{
			// mouse on screen
			int mouseX = Math.min(Math.max((int) controllerMouseX, 0), mc.mainWindow.getWidth());
			int mouseY = Math.min(Math.max((int) controllerMouseY, 0), mc.mainWindow.getHeight());

			int deltaX = 0;//?
			int deltaY = 0;//?

			if (MCOpenVR.controllerDeviceIndex[MCOpenVR.RIGHT_CONTROLLER] != -1)
			{
				InputSimulator.setMousePos(mouseX, mouseY);
				controllerMouseValid = true;

			}
		} else { //mouse off screen
			if(controllerMouseTicks == 0)
				controllerMouseValid = false;
			if(controllerMouseTicks>0)controllerMouseTicks--;
		}
	}

	public static Vec2f getTexCoordsForCursor(Vec3d guiPos_room, Matrix4f guiRotation_room, GuiScreen screen, float guiScale, VRDevicePose controller) {
		Vector3f controllerPos = new Vector3f();
		Vec3d con = controller.getPosition();
		controllerPos.x	= (float) con.x;
		controllerPos.y	= (float) con.y;
		controllerPos.z	= (float) con.z;

		Vec3d controllerdir = controller.getDirection();
		Vector3f cdir = new Vector3f((float)controllerdir.x,(float) controllerdir.y,(float) controllerdir.z);
		Vector3f forward = new Vector3f(0,0,1);

		Vector3f guiNormal = guiRotation_room.transform(forward);
		Vector3f guiRight = guiRotation_room.transform(new Vector3f(1,0,0));
		Vector3f guiUp = guiRotation_room.transform(new Vector3f(0,1,0));
		float guiNormalDotControllerDirection = guiNormal.dot(cdir);
		if (Math.abs(guiNormalDotControllerDirection) > 0.00001f)
		{//pointed normal to the GUI
			float guiWidth = 1.0f;		
			float guiHalfWidth = guiWidth * 0.5f;		
			float guiHeight = 1.0f;	
			float guiHalfHeight = guiHeight * 0.5f;

			Vector3f gp = new Vector3f();

			gp.x = (float) (guiPos_room.x);// + interPolatedRoomOrigin.x ) ;
			gp.y = (float) (guiPos_room.y);// + interPolatedRoomOrigin.y ) ;
			gp.z = (float) (guiPos_room.z);// + interPolatedRoomOrigin.z ) ;

			Vector3f guiTopLeft = gp.subtract(guiUp.divide(1.0f / guiHalfHeight)).subtract(guiRight.divide(1.0f/guiHalfWidth));

			float intersectDist = -guiNormal.dot(controllerPos.subtract(guiTopLeft)) / guiNormalDotControllerDirection;
			if (intersectDist > 0) {
				Vector3f pointOnPlane = controllerPos.add(cdir.divide(1.0f / intersectDist));

				Vector3f relativePoint = pointOnPlane.subtract(guiTopLeft);
				float u = relativePoint.dot(guiRight.divide(1.0f / guiWidth));
				float v = relativePoint.dot(guiUp.divide(1.0f / guiWidth));

				float AR = (float) mc.mainWindow.getScaledHeight() / mc.mainWindow.getScaledWidth();

				u = (u - 0.5f) / 1.5f / guiScale + 0.5f;
				v = (v - 0.5f) / AR / 1.5f / guiScale + 0.5f;

				v = 1 - v;

				return new Vec2f(u, v);
			}
		}
		return new Vec2f(-1, -1);
	}

	public static void processBindingsGui() {
		if (controllerMouseX >= 0 && controllerMouseX < mc.mainWindow.getWidth()
				&& controllerMouseY >=0 && controllerMouseY < mc.mainWindow.getWidth())
		{
			//This is how the MouseHelper do.
			/*double deltaX = (controllerMouseX - lastMouseX)
			 * (double)mc.mainWindow.getScaledWidth() / (double)mc.mainWindow.getWidth();
			double deltaY = (controllerMouseY - lastMouseY)
			 * (double)mc.mainWindow.getScaledHeight() / (double)mc.mainWindow.getHeight();
			double d0 = Math.min(Math.max((int) controllerMouseX, 0), mc.mainWindow.getWidth())
			 * (double)mc.mainWindow.getScaledWidth() / (double)mc.mainWindow.getWidth();
			double d1 = Math.min(Math.max((int) controllerMouseY, 0), mc.mainWindow.getWidth())
			 * (double)mc.mainWindow.getScaledHeight() / (double)mc.mainWindow.getHeight();*/

			if (MCOpenVR.controllerDeviceIndex[MCOpenVR.RIGHT_CONTROLLER] != -1)
			{
				//if (keyLeftClick.isKeyDown() && mc.currentScreen != null)
				//	mc.currentScreen.mouseDragged(d0, d1, 0, deltaX, deltaY);//Signals mouse move

				//LMB
				if (keyLeftClick.isPressed() && mc.currentScreen != null)
				{ //press left mouse button
					//if (Display.isActive())
					//	KeyboardSimulator.robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
					//else
					//	mc.currentScreen.mouseClicked(d0, d1, 0);
					InputSimulator.pressMouse(GLFW.GLFW_MOUSE_BUTTON_LEFT);
					lastPressedLeftClick = true;
				}	

				if (!keyLeftClick.isKeyDown() && lastPressedLeftClick && mc.currentScreen != null) {
					//release left mouse button
					//if (Display.isActive()) KeyboardSimulator.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
					//else 
					//	mc.currentScreen.mouseReleased(d0, d1, 0);
					InputSimulator.releaseMouse(GLFW.GLFW_MOUSE_BUTTON_LEFT);
					lastPressedLeftClick = false;
				}
				//end LMB

				//RMB
				if (keyRightClick.isPressed() && mc.currentScreen != null) {
					//press right mouse button
					//if (Display.isActive()) KeyboardSimulator.robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
					//else 
					//	mc.currentScreen.mouseClicked(d0, d1, 1);
					InputSimulator.pressMouse(GLFW.GLFW_MOUSE_BUTTON_RIGHT);
					lastPressedRightClick = true;
				}	

				//if (keyRightClick.isKeyDown() && mc.currentScreen != null)
				//	mc.currentScreen.mouseDragged(d0, d1, 0, deltaX, deltaY);//Signals mouse move

				if (!keyRightClick.isKeyDown() && lastPressedRightClick && mc.currentScreen != null) {
					//release right mouse button
					//if (Display.isActive())
					//	KeyboardSimulator.robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
					//else
					//	mc.currentScreen.mouseReleased(d0, d1, 1);
					InputSimulator.releaseMouse(GLFW.GLFW_MOUSE_BUTTON_RIGHT);
					lastPressedRightClick = false;
				}	
				//end RMB	

				//MMB
				if (keyMiddleClick.isPressed() && mc.currentScreen != null) {
					//press middle mouse button
					//if (Display.isActive())
					//	KeyboardSimulator.robot.mousePress(InputEvent.BUTTON2_DOWN_MASK);
					//else
					//	mc.currentScreen.mouseClicked(d0, d1, 2);
					InputSimulator.pressMouse(GLFW.GLFW_MOUSE_BUTTON_MIDDLE);
					lastPressedMiddleClick = true;
				}	

				//if (keyMiddleClick.isKeyDown() && mc.currentScreen != null)
				//	mc.currentScreen.mouseDragged(d0, d1, 0, deltaX, deltaY);//Signals mouse move

				if (!keyMiddleClick.isKeyDown() && lastPressedMiddleClick && mc.currentScreen != null) {
					//release middle mouse button
					//if (Display.isActive())
					//	KeyboardSimulator.robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
					//else 
					//	mc.currentScreen.mouseReleased(d0, d1, 2);
					InputSimulator.releaseMouse(GLFW.GLFW_MOUSE_BUTTON_MIDDLE);
					lastPressedMiddleClick = false;
				}	
				//end MMB

			}
		}

		//lastMouseX = controllerMouseX;
		//lastMouseY = controllerMouseY;

		if (MCOpenVR.controllerDeviceIndex[MCOpenVR.LEFT_CONTROLLER] != -1) {
			//Shift
			if (keyShift.isPressed())
			{
				//press Shift
				//if (mc.currentScreen != null) mc.currentScreen.pressShiftFake = true;
				//if (Display.isActive()) KeyboardSimulator.robot.keyPress(KeyEvent.VK_SHIFT);
				InputSimulator.pressKey(GLFW.GLFW_KEY_LEFT_SHIFT);
				lastPressedShift = true;
			}


			if (!keyShift.isKeyDown() && lastPressedShift)
			{
				//release Shift
				//if (mc.currentScreen != null) mc.currentScreen.pressShiftFake = false;
				//if (Display.isActive()) KeyboardSimulator.robot.keyRelease(KeyEvent.VK_SHIFT);
				InputSimulator.releaseKey(GLFW.GLFW_KEY_LEFT_SHIFT);
				lastPressedShift = false;
			}	
			//end Shift

			//Ctrl
			if (keyCtrl.isPressed())
			{
				//press Ctrl
				//if (Display.isActive()) KeyboardSimulator.robot.keyPress(KeyEvent.VK_CONTROL);
				InputSimulator.pressKey(GLFW.GLFW_KEY_LEFT_CONTROL);
				lastPressedCtrl = true;
			}


			if (!keyCtrl.isKeyDown() && lastPressedCtrl)
			{
				//release Ctrl
				//if (Display.isActive()) KeyboardSimulator.robot.keyRelease(KeyEvent.VK_CONTROL);
				InputSimulator.releaseKey(GLFW.GLFW_KEY_LEFT_CONTROL);
				lastPressedCtrl = false;
			}	
			//end Ctrl

			//Alt
			if (keyAlt.isPressed())
			{
				//press Alt
				//if (Display.isActive()) KeyboardSimulator.robot.keyPress(KeyEvent.VK_ALT);
				InputSimulator.pressKey(GLFW.GLFW_KEY_LEFT_ALT);
				lastPressedAlt = true;
			}


			if (!keyAlt.isKeyDown() && lastPressedAlt)
			{
				//release Alt
				//if (Display.isActive()) KeyboardSimulator.robot.keyRelease(KeyEvent.VK_ALT);
				InputSimulator.releaseKey(GLFW.GLFW_KEY_LEFT_ALT);
				lastPressedAlt = false;
			}	
			//end Alt

		}

		if (keyScrollUp.isPressed()) {		
			MCOpenVR.triggerBindingHapticPulse(keyScrollUp, 400);

			//	if(mc.isGameFocused()) {
			//		KeyboardSimulator.robot.mouseWheel(-120);
			//	} else {
			//		mc.currentScreen.mouseScrolled(4);
			//	}
			InputSimulator.scrollMouse(0, 4);
		}

		if (keyScrollDown.isPressed()) {
				MCOpenVR.triggerBindingHapticPulse(keyScrollDown, 400);
			//	if(mc.isGameFocused()) {
			//		KeyboardSimulator.robot.mouseWheel(120);
			//	} else {
			//		mc.currentScreen.mouseScrolled(-4);
			//	}
			InputSimulator.scrollMouse(0, -4);
		}

		if(keyMenuButton.isPressed()) { //handle esc
			//if(mc.isGameFocused()){ //keep this one for now.
				InputSimulator.pressKey(GLFW.GLFW_KEY_ESCAPE); //window focus... yadda yadda
				InputSimulator.releaseKey(GLFW.GLFW_KEY_ESCAPE); //window focus... yadda yadda
			//}
			//else {
			//	if (mc.player != null) mc.player.closeScreen();
			//	else mc.displayGuiScreen((GuiScreen)null);
			//}

			KeyboardHandler.setOverlayShowing(false);	
		}
	}

	
	
	public static void onGuiScreenChanged(GuiScreen previousScreen, GuiScreen newScreen, boolean unpressKeys)
	{
		if(unpressKeys){
			for (VRButtonMapping mapping : mc.vrSettings.buttonMappings.values()) {
				if(newScreen!=null) {
					if(mapping.isGUIBinding() && mapping.keyBinding != mc.gameSettings.keyBindInventory)
						mapping.actuallyUnpress();
				} else
					mapping.actuallyUnpress();
			}
		}

		if(newScreen == null) {
			//just insurance
			guiPos_room = null;
			guiRotation_room = null;
			guiScale=1;
			if(KeyboardHandler.keyboardForGui)
				KeyboardHandler.setOverlayShowing(false);
		} else {
			RadialHandler.setOverlayShowing(false, null);
		}

		if (mc.world == null || newScreen instanceof GuiWinGame) {
			mc.vrSettings.vrWorldRotationCached = mc.vrSettings.vrWorldRotation;
			mc.vrSettings.vrWorldRotation = 0;
		} else { //these dont update when screen open.
			if (mc.vrSettings.vrWorldRotationCached != 0) {
				mc.vrSettings.vrWorldRotation = mc.vrSettings.vrWorldRotationCached;
				mc.vrSettings.vrWorldRotationCached = 0;
			}
		}

		boolean staticScreen = mc.entityRenderer == null || mc.entityRenderer.isInMenuRoom();
		staticScreen &= !mc.vrSettings.seated && !mc.vrSettings.menuAlwaysFollowFace;
		staticScreen |= newScreen instanceof GuiScreenLoading;

		if (staticScreen) {
			//TODO reset scale things
			guiScale = 2.0f;
			float[] playArea = MCOpenVR.getPlayAreaSize();
			guiPos_room = new Vec3d(
					(float) (0),
					(float) (1.3f),
					(float) (playArea != null ? -playArea[1] / 2f : -1.5f));			

			guiRotation_room = new Matrix4f();
			guiRotation_room.M[0][0] = guiRotation_room.M[1][1] = guiRotation_room.M[2][2] = guiRotation_room.M[3][3] = 1.0F;
			guiRotation_room.M[0][1] = guiRotation_room.M[1][0] = guiRotation_room.M[2][3] = guiRotation_room.M[3][1] = 0.0F;
			guiRotation_room.M[0][2] = guiRotation_room.M[1][2] = guiRotation_room.M[2][0] = guiRotation_room.M[3][2] = 0.0F;
			guiRotation_room.M[0][3] = guiRotation_room.M[1][3] = guiRotation_room.M[2][1] = guiRotation_room.M[3][0] = 0.0F;

			return;
		}

		if((previousScreen==null && newScreen != null) || (newScreen instanceof GuiChat || newScreen instanceof GuiScreenBook || newScreen instanceof GuiEditSign))		
		{
			Quatf controllerOrientationQuat;
			boolean appearOverBlock = (newScreen instanceof GuiCrafting)
					|| (newScreen instanceof GuiChest)
					|| (newScreen instanceof GuiShulkerBox)
					|| (newScreen instanceof GuiHopper)
					|| (newScreen instanceof GuiFurnace)
					|| (newScreen instanceof GuiBrewingStand)
					|| (newScreen instanceof GuiBeacon)
					|| (newScreen instanceof GuiDispenser)
					|| (newScreen instanceof GuiEnchantment)
					|| (newScreen instanceof GuiRepair)
					;

			if(appearOverBlock && mc.objectMouseOver != null && mc.objectMouseOver.type == RayTraceResult.Type.BLOCK){	
				//appear over block.
				Vec3d temp =new Vec3d(mc.objectMouseOver.getBlockPos().getX() + 0.5f,
						mc.objectMouseOver.getBlockPos().getY(),
						mc.objectMouseOver.getBlockPos().getZ() + 0.5f);

				Vec3d temp_room = mc.vrPlayer.world_to_room_pos(temp, mc.vrPlayer.vrdata_world_pre);			
				Vec3d pos = mc.vrPlayer.vrdata_room_pre.hmd.getPosition();

				double dist = temp_room.subtract(pos).length();
				guiScale = (float) Math.sqrt(dist);

				//idk it works.
				Vec3d guiPosWorld = new Vec3d(temp.x, mc.objectMouseOver.getBlockPos().getY() + 1.1 + (0.5f * guiScale/2), temp.z);

				guiPos_room = mc.vrPlayer.world_to_room_pos(guiPosWorld, mc.vrPlayer.vrdata_world_pre);	

				Vector3f look = new Vector3f();
				look.x = (float) (guiPos_room.x - pos.x);
				look.y = (float) (guiPos_room.y - pos.y);
				look.z = (float) (guiPos_room.z - pos.z);

				float pitch = (float) Math.asin(look.y/look.length());
				float yaw = (float) ((float) Math.PI + Math.atan2(look.x, look.z));    
				guiRotation_room = Matrix4f.rotationY((float) yaw);
				Matrix4f tilt = OpenVRUtil.rotationXMatrix(pitch);	
				guiRotation_room = Matrix4f.multiply(guiRotation_room,tilt);		

			}				
			else{
				//static screens like menu, inventory, and dead.
				Vec3d adj = new Vec3d(0,0,-2);
				if (newScreen instanceof GuiChat){
					adj = new Vec3d(0,0.5,-2);
				} else if (newScreen instanceof GuiScreenBook || newScreen instanceof GuiEditSign) {
					adj = new Vec3d(0,0.25,-2);
				}

				Vec3d v = mc.vrPlayer.vrdata_room_pre.hmd.getPosition();
				Vec3d e = mc.vrPlayer.vrdata_room_pre.hmd.getCustomVector(adj);
				guiPos_room = new Vec3d(
						(e.x  / 2 + v.x),
						(e.y / 2 + v.y),
						(e.z / 2 + v.z));

				Vec3d pos = mc.vrPlayer.vrdata_room_pre.hmd.getPosition();
				Vector3f look = new Vector3f();
				look.x = (float) (guiPos_room.x - pos.x);
				look.y = (float) (guiPos_room.y - pos.y);
				look.z = (float) (guiPos_room.z - pos.z);

				float pitch = (float) Math.asin(look.y/look.length());
				float yaw = (float) ((float) Math.PI + Math.atan2(look.x, look.z));    
				guiRotation_room = Matrix4f.rotationY((float) yaw);
				Matrix4f tilt = OpenVRUtil.rotationXMatrix(pitch);	
				guiRotation_room = Matrix4f.multiply(guiRotation_room,tilt);		

			}
		}

		KeyboardHandler.orientOverlay(newScreen!=null);

	}


}
