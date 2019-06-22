package org.vivecraft.gameplay.screenhandlers;

import java.util.function.Predicate;

import org.vivecraft.control.ButtonTuple;
import org.vivecraft.control.ButtonType;
import org.vivecraft.control.ControllerType;
import org.vivecraft.control.VRButtonMapping;
import org.vivecraft.control.VRInputEvent;
import org.vivecraft.gui.GuiKeyboard;
import org.vivecraft.gui.PhysicalKeyboard;
import org.vivecraft.provider.MCOpenVR;
import org.vivecraft.utils.Utils;

import de.fruitfly.ovr.structs.Matrix4f;
import de.fruitfly.ovr.structs.Vector3f;
import jopenvr.OpenVRUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.Main;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class KeyboardHandler {
	//keyboard
	public static Minecraft mc = Minecraft.getMinecraft();
	public static boolean Showing = false;
	public static GuiKeyboard UI = new GuiKeyboard();
	public static PhysicalKeyboard physicalKeyboard = new PhysicalKeyboard();
	public static Vec3d Pos_room = new Vec3d(0,0,0);
	public static Matrix4f Rotation_room = new Matrix4f();
	private static boolean lpl, lps, PointedL, PointedR;
	public static boolean keyboardForGui;
	public static Framebuffer Framebuffer = null;

	public static boolean setOverlayShowing(boolean showingState) {
		if (Main.kiosk) return false;
		if(mc.vrSettings.seated) showingState = false;
		int ret = 1;
		if (showingState) {		
            int i = mc.mainWindow.getScaledWidth();
            int j = mc.mainWindow.getScaledHeight();
            if (mc.vrSettings.physicalKeyboard)
				physicalKeyboard.show();
            else
            	UI.setWorldAndResolution(Minecraft.getMinecraft(), i, j);
			Showing = true;
      		orientOverlay(mc.currentScreen!=null);
      		RadialHandler.setOverlayShowing(false, null);
		} else {
			Showing = false;
		}
		return Showing;
	}

	public static void processGui() {
	
		PointedL = false;
		PointedR = false;
		
		if(!Showing) {
			return;
		}
		if(mc.vrSettings.seated) return;
		if(Rotation_room == null) return;

		if (mc.vrSettings.physicalKeyboard) {
			physicalKeyboard.process();
			return; // Skip the rest of this
		}
		
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

	
	public static void orientOverlay(boolean guiRelative) {
		
		keyboardForGui = false;
		if (!Showing) return;
		keyboardForGui = guiRelative;
		
		org.vivecraft.utils.lwjgl.Matrix4f matrix = new org.vivecraft.utils.lwjgl.Matrix4f();
		if (mc.vrSettings.physicalKeyboard) {
			Vec3d pos = mc.vrPlayer.vrdata_room_pre.hmd.getPosition();
			Vec3d offset = new Vec3d(0, -0.5, 0.3);
			offset = offset.rotateYaw((float)Math.toRadians(-mc.vrPlayer.vrdata_room_pre.hmd.getYaw()));
			Pos_room = new Vec3d(pos.x + offset.x, pos.y + offset.y, pos.z + offset.z);

			float yaw = (float)Math.PI + (float)Math.toRadians(-mc.vrPlayer.vrdata_room_pre.hmd.getYaw());
			Rotation_room = Matrix4f.rotationY(yaw);
			Rotation_room = Matrix4f.multiply(Rotation_room, OpenVRUtil.rotationXMatrix((float)Math.PI * 0.8f));
		} else if (guiRelative && GuiHandler.guiRotation_room != null) {
			org.vivecraft.utils.lwjgl.Matrix4f guiRot = Utils.convertOVRMatrix(GuiHandler.guiRotation_room);
			Vec3d guiUp = new Vec3d(guiRot.m10, guiRot.m11, guiRot.m12);
			Vec3d guiFwd = new Vec3d(guiRot.m20, guiRot.m21, guiRot.m22).scale(0.25f);
			guiUp = guiUp.scale(0.80f);
			matrix.translate(new org.vivecraft.utils.lwjgl.Vector3f((float)(GuiHandler.guiPos_room.x - guiUp.x), (float)(GuiHandler.guiPos_room.y - guiUp.y), (float)(GuiHandler.guiPos_room.z - guiUp.z)));
			matrix.translate(new org.vivecraft.utils.lwjgl.Vector3f((float)(guiFwd.x), (float)(guiFwd.y), (float)(guiFwd.z)));
			org.vivecraft.utils.lwjgl.Matrix4f.mul(matrix, guiRot, matrix);
			matrix.rotate((float)Math.toRadians(30), new org.vivecraft.utils.lwjgl.Vector3f(-1, 0, 0)); // tilt it a bit
			Rotation_room =   Utils.convertToOVRMatrix(matrix);
			Pos_room = new Vec3d(Rotation_room.M[0][3],Rotation_room.M[1][3],Rotation_room.M[2][3]);
			Rotation_room.M[0][3] = 0;
			Rotation_room.M[1][3] = 0;
			Rotation_room.M[2][3] = 0;

		} else { //copied from vrplayer.onguiuscreenchanged
			Vec3d v = mc.vrPlayer.vrdata_room_pre.hmd.getPosition();
			Vec3d adj = new Vec3d(0,-0.5,-2);
			Vec3d e = mc.vrPlayer.vrdata_room_pre.hmd.getCustomVector(adj);
			Pos_room = new Vec3d(
					(e.x  / 2 + v.x),
					(e.y / 2 + v.y),
					(e.z / 2 + v.z));

			Vec3d pos = mc.vrPlayer.vrdata_room_pre.hmd.getPosition();
			Vector3f look = new Vector3f();
			look.x = (float) (Pos_room.x - pos.x);
			look.y = (float) (Pos_room.y - pos.y);
			look.z = (float) (Pos_room.z - pos.z);

			float pitch = (float) Math.asin(look.y/look.length());
			float yaw = (float) ((float) Math.PI + Math.atan2(look.x, look.z));    
			Rotation_room = Matrix4f.rotationY((float) yaw);
		}
	}
	
	public static boolean handleInputEvent(VRInputEvent event) {
		if (!MCOpenVR.isBindingBound(MCOpenVR.keyToggleKeyboard) || mc.world == null) {
			if (event.getButtonState() && event.isButtonPressEvent() && event.getController().getType() == ControllerType.LEFT && (event.getButton() == ButtonType.VIVE_APPMENU || event.getButton() == ButtonType.OCULUS_BY)) {
				if (MCOpenVR.controllers[MCOpenVR.LEFT_CONTROLLER].isButtonPressed(ButtonType.VIVE_GRIP) || MCOpenVR.controllers[MCOpenVR.LEFT_CONTROLLER].isButtonPressed(ButtonType.OCULUS_HAND_TRIGGER)) {
					setOverlayShowing(!Showing);
					return true;
				}
			}
		}

		if(Showing) { // Left click, right click and shift bindings will work on keyboard, ignoring left/right controller designation.
			if (mc.vrSettings.physicalKeyboard)
				return physicalKeyboard.handleInputEvent(event); // Pass the input on to physical keyboard, skipping all this stuff

			VRButtonMapping leftClick = mc.vrSettings.buttonMappings.get(GuiHandler.keyLeftClick.getKeyDescription());
			VRButtonMapping rightClick = mc.vrSettings.buttonMappings.get(GuiHandler.keyRightClick.getKeyDescription());
			VRButtonMapping shift = mc.vrSettings.buttonMappings.get(GuiHandler.keyShift.getKeyDescription());
			Predicate<ButtonTuple> predicate = b -> b.button == event.getButton() && b.isTouch == event.isButtonTouchEvent();
			boolean isClick = leftClick.buttons.stream().anyMatch(predicate) || rightClick.buttons.stream().anyMatch(predicate);

			double d0 = Math.min(Math.max((int) UI.cursorX1, 0), mc.mainWindow.getWidth())
					 * (double)mc.mainWindow.getScaledWidth() / (double)mc.mainWindow.getWidth();
			double d1 = Math.min(Math.max((int) UI.cursorY1, 0), mc.mainWindow.getWidth())
					 * (double)mc.mainWindow.getScaledHeight() / (double)mc.mainWindow.getHeight();
			
			
			if(PointedL && event.getController().getType() == ControllerType.LEFT && isClick) {
				if(event.getButtonState()) {
					UI.mouseClicked((int)d0, (int)d1, 0);
				} else {
					UI.mouseReleased((int)d0, (int)d1, 0);
				}
				return true;
			}

			d0 = Math.min(Math.max((int) UI.cursorX2, 0), mc.mainWindow.getWidth())
					 * (double)mc.mainWindow.getScaledWidth() / (double)mc.mainWindow.getWidth();
			d1 = Math.min(Math.max((int) UI.cursorY2, 0), mc.mainWindow.getWidth())
					 * (double)mc.mainWindow.getScaledHeight() / (double)mc.mainWindow.getHeight();
			
			if(PointedR && event.getController().getType() == ControllerType.RIGHT && isClick) {
				if(event.getButtonState()) {
					UI.mouseClicked((int)d0, (int)d1, 0);
				} else  {
					UI.mouseReleased((int)d0, (int)d1, 0);
				}
				return true;
			}
			
			if((PointedL || PointedR) && shift.buttons.stream().anyMatch(predicate)) {
				UI.setShift(event.getButtonState());
				// Only block if this isn't a controller where shift is bound, so text can be selected
				return shift.buttons.stream().noneMatch(b -> b.controller == event.getController().getType());
			}
		}

		return false;
	}

	
}
