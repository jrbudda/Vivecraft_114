package org.vivecraft.gui;

import java.util.ArrayList;
import java.util.List;

import org.vivecraft.control.ControllerType;
import org.vivecraft.control.VRInputEvent;
import org.vivecraft.gameplay.screenhandlers.KeyboardHandler;
import org.vivecraft.utils.InputSimulator;
import org.vivecraft.utils.Utils;
import org.vivecraft.utils.lwjgl.Matrix4f;
import org.vivecraft.utils.lwjgl.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class PhysicalKeyboard {
	private final Minecraft mc = Minecraft.getMinecraft();
	private boolean reinit;
	private boolean shift;
	private boolean shiftKeyToggled;
	private List<KeyButton> keys;

	private int rows = 4;
	private int columns = 13;
	private float spacing = .008f;
	private float keyWidth = .05f;
	private float keyHeight = .05f;
	private float keyWidthSpecial = keyWidth * 2 + spacing;

	private KeyButton[] pressedKey = new KeyButton[2];
	private long[] pressTime = new long[2];

	public PhysicalKeyboard() {
		this.keys = new ArrayList<>();
	}

	public void init() {
		this.keys.clear();

		String chars = mc.vrSettings.keyboardKeys;
		if (this.shift)
			chars = mc.vrSettings.keyboardKeysShift;

		float calcRows = (float)chars.length() / (float)columns;
		if (Math.abs(rows - calcRows) > 0.01F)
			rows = MathHelper.ceil(calcRows);

		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				int index = row * columns + column;
				char ch = ' ';
				if (index < chars.length()) {
					ch = chars.charAt(index);
				}

				final char chr = ch;
				this.addKey(new KeyButton(String.valueOf(ch), keyWidthSpecial + spacing + column * (keyWidth + spacing), row * (keyHeight + spacing), keyWidth, keyHeight) {
					@Override
					public void onPressed() {
						InputSimulator.typeChar(chr);
					}
				});
			}
		}

		KeyButton shiftKey = this.addKey(new KeyButton("Shift", 0, 3 * (keyHeight + spacing), keyWidthSpecial, keyHeight) {
			@Override
			public void onPressed() {
				setShift(!PhysicalKeyboard.this.shift);
				PhysicalKeyboard.this.shiftKeyToggled = true;
			}
		});
		if (this.shift) {
			shiftKey.color.red = 0;
			shiftKey.color.blue = 0;
		}
		if (this.shiftKeyToggled) {
			shiftKey.pressed = true;
		}

		this.addKey(new KeyButton(" ", keyWidthSpecial + spacing + ((columns - 5) / 2.0F) * (keyWidth + spacing), rows * (keyHeight + spacing), 5 * (keyWidth + spacing) - spacing, keyHeight) {
			@Override
			public void onPressed() {
				InputSimulator.typeChar(' ');
			}
		});

		this.addKey(new KeyButton("Tab", 0, keyHeight + spacing, keyWidthSpecial, keyHeight) {
			@Override
			public void onPressed() {
				InputSimulator.pressKey(GLFW.GLFW_KEY_TAB);
				InputSimulator.releaseKey(GLFW.GLFW_KEY_TAB);
			}
		});

		this.addKey(new KeyButton("Esc", 0, 0, keyWidthSpecial, keyHeight) {
			@Override
			public void onPressed() {
				InputSimulator.pressKey(GLFW.GLFW_KEY_ESCAPE);
				InputSimulator.releaseKey(GLFW.GLFW_KEY_ESCAPE);
			}
		});

		this.addKey(new KeyButton("Bksp", keyWidthSpecial + spacing + columns * (keyWidth + spacing), 0, keyWidthSpecial, keyHeight) {
			@Override
			public void onPressed() {
				InputSimulator.pressKey(GLFW.GLFW_KEY_BACKSPACE);
				InputSimulator.releaseKey(GLFW.GLFW_KEY_BACKSPACE);
			}
		});

		this.addKey(new KeyButton("Enter", keyWidthSpecial + spacing + columns * (keyWidth + spacing), 2 * (keyHeight + spacing), keyWidthSpecial, keyHeight) {
			@Override
			public void onPressed() {
				InputSimulator.pressKey(GLFW.GLFW_KEY_ENTER);
				InputSimulator.releaseKey(GLFW.GLFW_KEY_ENTER);
			}
		});

		this.addKey(new KeyButton("\u2191", keyWidthSpecial + spacing + columns * (keyWidth + spacing), 3 * (keyHeight + spacing), keyWidth, keyHeight) {
			@Override
			public void onPressed() {
				InputSimulator.pressKey(GLFW.GLFW_KEY_UP);
				InputSimulator.releaseKey(GLFW.GLFW_KEY_UP);
			}
		});

		this.addKey(new KeyButton("\u2193", keyWidthSpecial + spacing + (columns + 1) * (keyWidth + spacing), 3 * (keyHeight + spacing), keyWidth, keyHeight) {
			@Override
			public void onPressed() {
				InputSimulator.pressKey(GLFW.GLFW_KEY_DOWN);
				InputSimulator.releaseKey(GLFW.GLFW_KEY_DOWN);
			}
		});

		this.reinit = false;
		this.shiftKeyToggled = false;
	}

	public void process() {
		if (this.reinit)
			init();

		for (int c = 0; c < 2; c++) {
			ControllerType controller = ControllerType.values()[c];
			KeyButton key = findTouchedKey(controller);
			if (key != null) {
				if (pressedKey[c] != null && key.label.equals(pressedKey[c].label)) {
					pressedKey[c] = key;
				}
				if (key != pressedKey[c] && Utils.milliTime() - pressTime[c] >= 150) {
					if (pressedKey[c] != null) {
						pressedKey[c].unpress(controller);
						pressedKey[c] = null;
					}
					key.press(controller);
					pressedKey[c] = key;
				}
				pressTime[c] = Utils.milliTime();
			} else if (pressedKey[c] != null) {
				pressedKey[c].unpress(controller);
				pressedKey[c] = null;
			}
		}
	}

	public boolean handleInputEvent(VRInputEvent event) {
		return false;
	}

	private Vector3f getCenterPos() {
		return new Vector3f(((keyWidth + spacing) * (columns + (columns % 2.0F / 2.0F)) + (keyWidthSpecial + spacing) * 2.0F) / 2.0F, (keyHeight + spacing) * (rows + 1), 0.0F);
	}

	private KeyButton findTouchedKey(ControllerType controller) {
		// Transform the controller into keyboard space
		Matrix4f matrix = new Matrix4f();
		matrix.translate(getCenterPos());
		Matrix4f.mul(matrix, (Matrix4f)Utils.convertOVRMatrix(KeyboardHandler.Rotation_room).invert(), matrix);
		matrix.translate((Vector3f)Utils.convertToVector3f(KeyboardHandler.Pos_room).negate());
		Vec3d pos = Utils.convertToVec3d(Utils.transformVector(matrix, Utils.convertToVector3f(mc.vrPlayer.vrdata_room_pre.getController(controller.ordinal()).getPosition()), true));

		// Do intersection checks
		for (KeyButton key : keys) {
			if (key.getCollisionBoundingBox().contains(pos))
				return key;
		}
		return null;
	}

	public void render() {
		Vector3f center = getCenterPos();
		GlStateManager.translatef(-center.x, -center.y, -center.z);
		GlStateManager.disableTexture2D();
		GlStateManager.depthMask(false);
		GlStateManager.disableCull();
		GlStateManager.enableAlphaTest();
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

		Tessellator tess = Tessellator.getInstance();
		for (KeyButton key : keys) {
			AxisAlignedBB box = key.getRenderBoundingBox();
			GlStateManager.Color color = key.getRenderColor();

			// Shaders goes crazy without this
			mc.getTextureManager().bindTexture(new ResourceLocation("vivecraft/textures/white.png"));

			// Alright let's draw a box
			BufferBuilder buf = tess.getBuffer();
			buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_NORMAL);

			buf.pos(box.minX, box.minY, box.minZ).color(color.red, color.green, color.blue, color.alpha).normal(0, 0, -1).endVertex();
			buf.pos(box.minX, box.maxY, box.minZ).color(color.red, color.green, color.blue, color.alpha).normal(0, 0, -1).endVertex();
			buf.pos(box.maxX, box.maxY, box.minZ).color(color.red, color.green, color.blue, color.alpha).normal(0, 0, -1).endVertex();
			buf.pos(box.maxX, box.minY, box.minZ).color(color.red, color.green, color.blue, color.alpha).normal(0, 0, -1).endVertex();

			buf.pos(box.minX, box.minY, box.minZ).color(color.red, color.green, color.blue, color.alpha).normal(0, -1, 0).endVertex();
			buf.pos(box.maxX, box.minY, box.minZ).color(color.red, color.green, color.blue, color.alpha).normal(0, -1, 0).endVertex();
			buf.pos(box.maxX, box.minY, box.maxZ).color(color.red, color.green, color.blue, color.alpha).normal(0, -1, 0).endVertex();
			buf.pos(box.minX, box.minY, box.maxZ).color(color.red, color.green, color.blue, color.alpha).normal(0, -1, 0).endVertex();

			buf.pos(box.minX, box.minY, box.minZ).color(color.red, color.green, color.blue, color.alpha).normal(-1, 0, 0).endVertex();
			buf.pos(box.minX, box.minY, box.maxZ).color(color.red, color.green, color.blue, color.alpha).normal(-1, 0, 0).endVertex();
			buf.pos(box.minX, box.maxY, box.maxZ).color(color.red, color.green, color.blue, color.alpha).normal(-1, 0, 0).endVertex();
			buf.pos(box.minX, box.maxY, box.minZ).color(color.red, color.green, color.blue, color.alpha).normal(-1, 0, 0).endVertex();

			buf.pos(box.maxX, box.maxY, box.maxZ).color(color.red, color.green, color.blue, color.alpha).normal(0, 0, 1).endVertex();
			buf.pos(box.minX, box.maxY, box.maxZ).color(color.red, color.green, color.blue, color.alpha).normal(0, 0, 1).endVertex();
			buf.pos(box.minX, box.minY, box.maxZ).color(color.red, color.green, color.blue, color.alpha).normal(0, 0, 1).endVertex();
			buf.pos(box.maxX, box.minY, box.maxZ).color(color.red, color.green, color.blue, color.alpha).normal(0, 0, 1).endVertex();

			buf.pos(box.maxX, box.maxY, box.maxZ).color(color.red, color.green, color.blue, color.alpha).normal(0, 1, 0).endVertex();
			buf.pos(box.maxX, box.maxY, box.minZ).color(color.red, color.green, color.blue, color.alpha).normal(0, 1, 0).endVertex();
			buf.pos(box.minX, box.maxY, box.minZ).color(color.red, color.green, color.blue, color.alpha).normal(0, 1, 0).endVertex();
			buf.pos(box.minX, box.maxY, box.maxZ).color(color.red, color.green, color.blue, color.alpha).normal(0, 1, 0).endVertex();

			buf.pos(box.maxX, box.maxY, box.maxZ).color(color.red, color.green, color.blue, color.alpha).normal(1, 0, 0).endVertex();
			buf.pos(box.maxX, box.minY, box.maxZ).color(color.red, color.green, color.blue, color.alpha).normal(1, 0, 0).endVertex();
			buf.pos(box.maxX, box.minY, box.minZ).color(color.red, color.green, color.blue, color.alpha).normal(1, 0, 0).endVertex();
			buf.pos(box.maxX, box.maxY, box.minZ).color(color.red, color.green, color.blue, color.alpha).normal(1, 0, 0).endVertex();

			// Woo that was fun
			tess.draw();

			FontRenderer fontRenderer = mc.getRenderManager().getFontRenderer();
			GlStateManager.enableTexture2D();
			GlStateManager.disableLighting();

			// Calculate text position
			float textScale = 0.0025F;
			float stringWidth = fontRenderer.getStringWidth(key.label) * textScale;
			float stringHeight = fontRenderer.FONT_HEIGHT * textScale;
			float textX = (float)box.minX + ((float)box.maxX - (float)box.minX) / 2 - stringWidth / 2;
			float textY = (float)box.minY + ((float)box.maxY - (float)box.minY) / 2 - stringHeight / 2;
			float textZ = (float)box.minZ + ((float)box.maxZ - (float)box.minZ) / 2;

			// Draw the text
			GlStateManager.translatef(0, 0, textZ);
			GlStateManager.scalef(textScale, textScale, 1.0F);
			fontRenderer.drawStringWithShadow(key.label, textX / textScale, textY / textScale, 0xFFFFFFFF);
			GlStateManager.scalef(1.0F / textScale, 1.0F / textScale, 1.0F);
			GlStateManager.translatef(0, 0, -textZ);

			GlStateManager.disableTexture2D();
			GlStateManager.enableLighting();
		}

		GlStateManager.enableTexture2D();
		GlStateManager.enableCull();
	}

	public void show() {
		this.reinit = true;
	}

	private KeyButton addKey(KeyButton key) {
		keys.add(key);
		return key;
	}

	public boolean isShift() {
		return this.shift;
	}

	public void setShift(boolean shift) {
		if (shift != this.shift) {
			this.shift = shift;
			this.reinit = true;
		}
	}

	private static abstract class KeyButton {
		public final String label;
		public final AxisAlignedBB boundingBox;
		public GlStateManager.Color color = new GlStateManager.Color(1.0F, 1.0F, 1.0F, 0.5F);
		public boolean pressed;

		public KeyButton(String label, float x, float y, float width, float height) {
			this.label = label;
			this.boundingBox = new AxisAlignedBB(x, y, 0.0, x + width, y + height, 0.035);
		}

		public AxisAlignedBB getRenderBoundingBox() {
			if (pressed)
				return boundingBox.offset(0, 0, 0.015);
			return boundingBox;
		}

		public AxisAlignedBB getCollisionBoundingBox() {
			if (pressed)
				return boundingBox.expand(0, 0, 0.05);
			return boundingBox;
		}

		public GlStateManager.Color getRenderColor() {
			GlStateManager.Color retColor = new GlStateManager.Color(this.color.red, this.color.green, this.color.blue, this.color.alpha);
			if (!pressed) {
				retColor.red *= 0.5F;
				retColor.green *= 0.5F;
				retColor.blue *= 0.5F;
			}

			return retColor;
		}

		public final void press(ControllerType controller) {
			Minecraft.getMinecraft().getSoundHandler().play(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
			controller.getController().triggerHapticPulse(600);
			this.pressed = true;
			this.onPressed();
		}

		public final void unpress(ControllerType controller) {
			this.pressed = false;
		}

		public abstract void onPressed();
	}
}
