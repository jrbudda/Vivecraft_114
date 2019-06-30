package org.vivecraft.render;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockReader;

public class VRActiveRenderInfo extends ActiveRenderInfo {

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		super.clear();
	}

	@Override
	public float getPitch() {
		// TODO Auto-generated method stub
		return super.getPitch();
	}

	@Override
	public float getYaw() {
		// TODO Auto-generated method stub
		return super.getYaw();
	}

	@Override
	public void update(IBlockReader worldIn, Entity renderViewEntity, boolean thirdPersonIn,
			boolean thirdPersonReverseIn, float partialTicks) {
		this.world = worldIn;
		this.field_216791_c = renderViewEntity;
		Minecraft mc = Minecraft.getInstance();
		// This is the center position of the camera, not the exact eye.
		switch (mc.currentPass) {
		case CENTER:
		case LEFT:
		case RIGHT:
			this.setPostion(mc.vrPlayer.vrdata_world_render.hmd.getPosition());
			// this.setDirection(mc.vrPlayer.vrdata_world_render.hmd.getYaw(),mc.vrPlayer.vrdata_world_render.hmd.getPitch());
			// this.updateLook();
			this.pitch = -mc.vrPlayer.vrdata_world_render.hmd.getPitch();
			this.yaw = mc.vrPlayer.vrdata_world_render.hmd.getYaw();
			this.look = mc.vrPlayer.vrdata_world_render.hmd.getDirection();
			break;
		case THIRD:
			this.setPostion(mc.vrPlayer.vrdata_world_render.hmd.getPosition());
			this.look = mc.vrPlayer.vrdata_world_render.hmd.getDirection().scale(-1);
			mc.gameRenderer.applyMRCameraRotation(false);
			break;
		default:
			break;
		}

	}

	@Override
	public void interpolateHeight() {
		// noop
	}

	@Override
	public boolean isValid() {
		return Minecraft.getInstance().vrPlayer.vrdata_world_render != null;
	}

	@Override
	public boolean isThirdPerson() {
		return false;
		// ehhhh  return Minecraft.getInstance().currentPass == RenderPass.THIRD;
	}

}
