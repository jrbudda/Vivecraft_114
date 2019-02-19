package org.vivecraft.gameplay.screenhandlers;

import java.util.function.Predicate;

import org.vivecraft.api.VRData.VRDevicePose;
import org.vivecraft.control.ButtonTuple;
import org.vivecraft.control.ControllerType;
import org.vivecraft.control.VRButtonMapping;
import org.vivecraft.control.VRInputEvent;
import org.vivecraft.gui.GuiRadial;
import org.vivecraft.provider.MCOpenVR;

import de.fruitfly.ovr.structs.Matrix4f;
import de.fruitfly.ovr.structs.Vector3f;
import jopenvr.OpenVRUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.Main;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class RadialHandler {
	//
	public static Minecraft mc = Minecraft.getMinecraft();
	private static boolean Showing = false;
	public static GuiRadial UI = new GuiRadial();
	public static Vec3d Pos_room = new Vec3d(0,0,0);
	public static Matrix4f Rotation_room = new Matrix4f();
	private static boolean lpl, lps, PointedL, PointedR;

	public static Framebuffer Framebuffer = null;

	private static ControllerType activecontroller;
	private static ButtonTuple activeButton;
	
	public static boolean setOverlayShowing(boolean showingState, ButtonTuple button) {
		if (Main.kiosk) return false;
		if(mc.vrSettings.seated) showingState = false;
		int ret = 1;
		if (showingState) {		
			int i = mc.mainWindow.getScaledWidth();
			int j = mc.mainWindow.getScaledHeight();
			UI.setWorldAndResolution(Minecraft.getMinecraft(), i, j);
			Showing = true;
			activecontroller = button.controller;
			orientOverlay(activecontroller);
			activeButton = button; 
		} else {
			Showing = false;
			activecontroller = null;
			activeButton = null;
		}

		return isShowing();
	}

	public static void processGui() {
		
		PointedL = false;
		PointedR = false;
		
		if(!isShowing()) {
			return;
		}
		if(mc.vrSettings.seated) return;
		if(Rotation_room == null) return;
		
		Vec2f tex1 = GuiHandler.getTexCoordsForCursor(Pos_room, Rotation_room, mc.currentScreen, GuiHandler.guiScale, mc.vrPlayer.vrdata_room_pre.getController(1));
		Vec2f tex2 = GuiHandler.getTexCoordsForCursor(Pos_room, Rotation_room, mc.currentScreen, GuiHandler.guiScale, mc.vrPlayer.vrdata_room_pre.getController(0));
	
		float u = tex2.x;
		float v = tex2.y;
		
		if (u<0 || v<0 || u>1 || v>1)
		{
			// offscreen
			UI.cursorX2 = -1.0f;
			UI.cursorY2 = -1.0f;
			PointedR = false;
		}
		else if (UI.cursorX2 == -1.0f)
		{
			UI.cursorX2 = (int) (u * mc.mainWindow.getWidth());
			UI.cursorY2 = (int) (v * mc.mainWindow.getHeight());
			PointedR = true;
		}
		else
		{
			// apply some smoothing between mouse positions
			float newX = (int) (u * mc.mainWindow.getWidth());
			float newY = (int) (v * mc.mainWindow.getHeight());
			UI.cursorX2 = UI.cursorX2 * 0.7f + newX * 0.3f;
			UI.cursorY2 = UI.cursorY2 * 0.7f + newY * 0.3f;
			PointedR = true;
		}
		
		 u = tex1.x;
		 v = tex1.y;
		
		if (u<0 || v<0 || u>1 || v>1)
		{
			// offscreen
			UI.cursorX1 = -1.0f;
			UI.cursorY1 = -1.0f;
			PointedL = false;
		}
		else if (UI.cursorX1 == -1.0f)
		{
			UI.cursorX1 = (int) (u * mc.mainWindow.getWidth());
			UI.cursorY1 = (int) (v * mc.mainWindow.getHeight());
			PointedL = true;
		}
		else
		{
			// apply some smoothing between mouse positions
			float newX = (int) (u * mc.mainWindow.getWidth());
			float newY = (int) (v * mc.mainWindow.getHeight());
			UI.cursorX1 = UI.cursorX1 * 0.7f + newX * 0.3f;
			UI.cursorY1 = UI.cursorY1 * 0.7f + newY * 0.3f;
			PointedL = true;
		}
	}


	public static void orientOverlay(ControllerType controller) {
		if (!isShowing()) return;

		VRDevicePose pose = mc.vrPlayer.vrdata_room_pre.hmd; //normal menu.
		float dist = 2;
		
		int id=0;
		if(controller == ControllerType.LEFT)
			id=1;

		if(mc.vrSettings.radialModeHold) { //open with controller centered, consistent motions.
			pose = mc.vrPlayer.vrdata_room_pre.getController(id);
			dist = 1.2f;
		}

		Matrix4f matrix = new Matrix4f();

		Vec3d v = pose.getPosition();
		Vec3d adj = new Vec3d(0,0,-dist);
		Vec3d e = pose.getCustomVector(adj);
		Pos_room = new Vec3d(
				(e.x / 2 + v.x),
				(e.y / 2 + v.y),
				(e.z / 2 + v.z));

		Vector3f look = new Vector3f();
		look.x = (float) (Pos_room.x - v.x);
		look.y = (float) (Pos_room.y - v.y);
		look.z = (float) (Pos_room.z - v.z);

		float pitch = (float) Math.asin(look.y/look.length());
		float yaw = (float) ((float) Math.PI + Math.atan2(look.x, look.z));    
		Rotation_room = Matrix4f.rotationY((float) yaw);
		Matrix4f tilt = OpenVRUtil.rotationXMatrix(pitch);	
		Rotation_room = Matrix4f.multiply(Rotation_room, tilt);	

	}

	public static boolean handleInputEvent(VRInputEvent event) {

		if(!isShowing()) return false;

		Predicate<ButtonTuple> predicate = b -> b.button.equals(event.getButton()) && b.isTouch == event.isButtonTouchEvent();
		
		VRButtonMapping shift = mc.vrSettings.buttonMappings.get(GuiHandler.keyShift.getKeyDescription());

		if((PointedL || PointedR) && shift.buttons.stream().anyMatch( b -> b.button.equals(event.getButton()) && b.isTouch == event.isButtonTouchEvent() && b.controller == event.getController().getType())) {
			if (event.getButtonState())
				UI.setShift(true);
			else
				UI.setShift(false);
			return true;
		}
		
		double d0 = Math.min(Math.max((int) UI.cursorX1, 0), mc.mainWindow.getWidth())
				 * (double)mc.mainWindow.getScaledWidth() / (double)mc.mainWindow.getWidth();
		double d1 = Math.min(Math.max((int) UI.cursorY1, 0), mc.mainWindow.getWidth())
				 * (double)mc.mainWindow.getScaledHeight() / (double)mc.mainWindow.getHeight();
		
		double d2 = Math.min(Math.max((int) UI.cursorX2, 0), mc.mainWindow.getWidth())
				 * (double)mc.mainWindow.getScaledWidth() / (double)mc.mainWindow.getWidth();
		double d3 = Math.min(Math.max((int) UI.cursorY2, 0), mc.mainWindow.getWidth())
				 * (double)mc.mainWindow.getScaledHeight() / (double)mc.mainWindow.getHeight();

		if(mc.vrSettings.radialModeHold) {
			
			if(activeButton == null || activecontroller == null) 
				return false;

			boolean ismeUp = event.getButtonState() == false &&  activeButton.button == event.getButton() && activecontroller == event.getController().getType();	
			
			if(ismeUp) {
				if (activecontroller == ControllerType.LEFT) {
					UI.mouseClicked((int)d0, (int)d1, 0);
				} else {
					UI.mouseClicked((int)d2, (int)d3, 0);
				}
				RadialHandler.setOverlayShowing(false, null);
				return false;
			}
			
		} else {
			VRButtonMapping leftClick = mc.vrSettings.buttonMappings.get(GuiHandler.keyLeftClick.getKeyDescription());
			VRButtonMapping rightClick = mc.vrSettings.buttonMappings.get(GuiHandler.keyRightClick.getKeyDescription());
			boolean isClick = leftClick.buttons.stream().anyMatch(predicate) || rightClick.buttons.stream().anyMatch(predicate);

			if(PointedL && event.getController().getType() == ControllerType.LEFT && isClick) {
				if(event.getButtonState()) {
					UI.mouseClicked((int)d0, (int)d1, 0);
				} else {
					UI.mouseReleased((int)d0, (int)d1, 0);
				}
				return true;
			}

			if(PointedR && event.getController().getType() == ControllerType.RIGHT && isClick) {
				if(event.getButtonState()) {
					UI.mouseClicked((int)d2, (int)d3, 0);
				} else  {
					UI.mouseReleased((int)d2, (int)d3, 0);
				}
				return true;
			}
		}
		return false;
	}

	public static boolean isShowing() {
		return Showing;
	}
}
