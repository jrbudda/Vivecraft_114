package org.vivecraft.utils;

import java.util.function.Predicate;
import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagCollection;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.world.EnumLightType;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.Heightmap;

public class FakeBlockAccess implements IWorldReader {
	private DimensionType dimensionType;
	private WorldType worldType;
	private Dimension dimension;
	private IBlockState[] blocks;
	private byte[] skylightmap;
	private byte[] blocklightmap;
	private Biome[] biomemap;
	private int xSize;
	private int ySize;
	private int zSize;
	private int ground;
	
	public FakeBlockAccess(IBlockState[] blocks, byte[] skylightmap, byte[] blocklightmap, Biome[] biomemap, int xSize, int ySize, int zSize, int ground, DimensionType dimensionType, WorldType worldType, boolean hasSkyLight) {
		this.blocks = blocks;
		this.skylightmap = skylightmap;
		this.blocklightmap = blocklightmap;
		this.biomemap = biomemap;
		this.xSize = xSize;
		this.ySize = ySize;
		this.zSize = zSize;
		this.ground = ground;
		this.dimensionType = dimensionType;
		this.worldType = worldType;

		this.dimension = dimensionType.create();
		MCReflection.Dimension_hasSkyLight.set(this.dimension, hasSkyLight);
	}
	
	private int encodeCoords(int x, int z) {
		return z * xSize + x;
	}
	
	private int encodeCoords(int x, int y, int z) {
		return (y * zSize + z) * xSize + x;
	}

	private int encodeCoords(BlockPos pos) {
		return encodeCoords(pos.getX(), pos.getY(), pos.getZ());
	}
	
	private boolean checkCoords(int x, int y, int z) {
		return x >= 0 && y >= 0 && z >= 0 && x < xSize && y < ySize && z < xSize;
	}

	private boolean checkCoords(BlockPos pos) {
		return checkCoords(pos.getX(), pos.getY(), pos.getZ());
	}
	
	public int getGround() {
		return ground;
	}
	
	public int getXSize() {
		return xSize;
	}

	public int getYSize() {
		return ySize;
	}

	public int getZSize() {
		return zSize;
	}
	
	public DimensionType getDimensionType() {
		return dimensionType;
	}

	public WorldType getWorldType() {
		return worldType;
	}

	public double getVoidFogYFactor() {
		return this.worldType == WorldType.FLAT ? 1.0D : 0.03125D;
	}

	public double getHorizon() {
		return this.worldType == WorldType.FLAT ? 0.0D : 63.0D;
	}

	@Override
	public IBlockState getBlockState(BlockPos pos) {
		if (!checkCoords(pos))
			return Blocks.BEDROCK.getDefaultState();

		IBlockState state = blocks[encodeCoords(pos)];
		return state != null ? state : Blocks.AIR.getDefaultState();
	}

	@Override
	public IFluidState getFluidState(BlockPos pos) {
		return getBlockState(pos).getFluidState();
	}

	@Override
	public TileEntity getTileEntity(BlockPos pos) {
		return null; // You're a funny guy, I kill you last
	}

	@Override
	public int getCombinedLight(BlockPos pos, int lightValue) {
		int sky = this.getLightFromNeighborsFor(EnumLightType.SKY, pos);
		int block = this.getLightFromNeighborsFor(EnumLightType.BLOCK, pos);

		if (block < lightValue)
			block = lightValue;

		return sky << 20 | block << 4;
	}

	@Override
	public int getLightFor(EnumLightType type, BlockPos pos) {
		if (!checkCoords(pos))
			return (type != EnumLightType.SKY || !this.dimension.hasSkyLight()) && type != EnumLightType.BLOCK ? 0 : type.defaultLightValue;

		if (type == EnumLightType.SKY)
			return this.dimension.hasSkyLight() ? skylightmap[encodeCoords(pos)] : 0;
		else
			return type == EnumLightType.BLOCK ? blocklightmap[encodeCoords(pos)] : type.defaultLightValue;
	}

	public int getLightFromNeighborsFor(EnumLightType type, BlockPos pos) {
		if (!checkCoords(pos))
			return type.defaultLightValue;
		if ((type == EnumLightType.SKY && !this.dimension.hasSkyLight()))
			return 0;

		if (getBlockState(pos).useNeighborBrightness(this, pos)) {
			int light = 0;

			for (EnumFacing face : EnumFacing.values()) {
				int neighborLight = this.getLightFor(type, pos.offset(face));

				if (neighborLight > light)
					light = neighborLight;
				if (light >= 15)
					return light;
			}

			return light;
		} else {
			return this.getLightFor(type, pos);
		}
	}

	@Override
	public int getLightSubtracted(BlockPos pos, int amount) {
		if (!checkCoords(pos.getX(), 0, pos.getZ()))
			return 0;

		if (pos.getY() < 0) {
			return 0;
		} else if (pos.getY() >= 256) {
			int light = 15 - amount;
			if (light < 0)
				light = 0;
			return light;
		} else {
			int light = (this.dimension.hasSkyLight() ? skylightmap[encodeCoords(pos)] : 0) - amount;
			int blockLight = blocklightmap[encodeCoords(pos)];

			if (blockLight > light)
				light = blockLight;
			return light;
		}
	}

	@Override
	public boolean isChunkLoaded(int x, int z, boolean allowEmpty) {
		return checkCoords(new BlockPos(x * 16, 0, z * 16)); // Uh?
	}

	@Override
	public boolean canSeeSky(BlockPos pos) {
		return true; // ¯\_(ツ)_/¯
	}

	@Override
	public int getHeight(Heightmap.Type heightmapType, int x, int z) {
		return 0; // ¯\_(ツ)_/¯
	}

	@Nullable
	@Override
	public EntityPlayer getClosestPlayer(double x, double y, double z, double distance, Predicate<Entity> predicate) {
		return null; // Yeah right
	}

	@Override
	public int getSkylightSubtracted() {
		return 0; // idk this is just what RenderChunkCache does
	}

	@Override
	public WorldBorder getWorldBorder() {
		return new WorldBorder();
	}

	@Override
	public boolean checkNoEntityCollision(Entity entityIn, VoxelShape shape) {
		return false; // ???
	}

	@Override
	public boolean isAirBlock(BlockPos pos) {
		return this.getBlockState(pos).isAir();
	}

	@Override
	public Biome getBiome(BlockPos pos) {
		if (!checkCoords(pos))
			return Biomes.PLAINS;

		return biomemap[encodeCoords(pos.getX(), pos.getZ())];
	}

	@Override
	public int getStrongPower(BlockPos pos, EnumFacing direction) {
		return 0;
	}

	@Override
	public boolean isRemote() {
		return false;
	}

	@Override
	public int getSeaLevel() {
		return 63; // magic number
	}

	@Override
	public Dimension getDimension() {
		return this.dimension;
	}
}
