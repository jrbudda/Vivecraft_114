package org.vivecraft.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import com.google.common.io.Files;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.world.EnumLightType;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;

public class MenuWorldExporter {
	public static final int VERSION = 1;

	public static byte[] saveArea(World world, int xMin, int zMin, int xSize, int zSize, int ground) throws IOException {
		int ySize = 256;
		int[] blocks = new int[xSize * ySize * zSize];
		byte[] skylightmap = new byte[xSize * ySize * zSize];
		byte[] blocklightmap = new byte[xSize * ySize * zSize];
		int[] biomemap = new int[xSize * zSize];
		for (int x = xMin; x < xMin + xSize; x++) {
			int xl = x - xMin;
			for (int z = zMin; z < zMin + zSize; z++) {
				int zl = z - zMin;
				int index2 = zl * xSize + xl;
				BlockPos pos2 = new BlockPos(x, 0, z);
				biomemap[index2] = (byte)IRegistry.BIOME.getIDForObjectRaw(world.getBiome(pos2));
				for (int y = 0; y < ySize; y++) {
					int index3 = (y * zSize + zl) * xSize + xl;
					BlockPos pos3 = new BlockPos(x, y, z);
					IBlockState state = world.getBlockState(pos3);
					blocks[index3] = Block.getStateId(state);
					skylightmap[index3] = (byte)world.getLightFor(EnumLightType.SKY, pos3);
					blocklightmap[index3] = (byte)world.getLightFor(EnumLightType.BLOCK, pos3);
				}
			}
		}
		
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(data);
		dos.writeInt(VERSION);
		dos.writeInt(xSize);
		dos.writeInt(ySize);
		dos.writeInt(zSize);
		dos.writeInt(ground);
		dos.writeInt(world.dimension.getType().getId());
		dos.writeUTF(world.getWorldInfo().getTerrainType().getName());
		dos.writeBoolean(world.dimension.hasSkyLight()); // because we can't init it later
		for (int i = 0; i < blocks.length; i++) {
			dos.writeInt(blocks[i]);
		}
		for (int i = 0; i < skylightmap.length; i++) {
			dos.writeByte(skylightmap[i] | (blocklightmap[i] << 4));
		}
		for (int i = 0; i < biomemap.length; i++) {
			dos.writeInt(biomemap[i]);
		}
		
		Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);
		deflater.setInput(data.toByteArray());
		deflater.finish();
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[1048576];
		while (!deflater.finished()) {
			int len = deflater.deflate(buffer);
			output.write(buffer, 0, len);
		}
		
		return output.toByteArray();
	}
	
	public static void saveAreaToFile(World world, int xMin, int zMin, int xSize, int zSize, int ground, File file) throws IOException {
		byte[] bytes = saveArea(world, xMin, zMin, xSize, zSize, ground);
		Files.write(bytes, file);
	}
	
	public static FakeBlockAccess loadWorld(byte[] data) throws IOException, DataFormatException {
		Inflater inflater = new Inflater();
		inflater.setInput(data);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[1048576];
		while (!inflater.finished()) {
			int len = inflater.inflate(buffer);
			output.write(buffer, 0, len);
		}
		
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(output.toByteArray()));
		int version = dis.readInt(); // for future use
		int xSize = dis.readInt();
		int ySize = dis.readInt();
		int zSize = dis.readInt();
		int ground = dis.readInt();
		DimensionType dimensionType = DimensionType.getById(dis.readInt());
		if (dimensionType == null)
			dimensionType = DimensionType.OVERWORLD;
		WorldType worldType = WorldType.byName(dis.readUTF());
		if (worldType == null)
			worldType = WorldType.DEFAULT;
		boolean hasSkyLight = dis.readBoolean();
		IBlockState[] blocks = new IBlockState[xSize * ySize * zSize];
		for (int i = 0; i < blocks.length; i++) {
			blocks[i] = Block.getStateById(dis.readInt());
		}
		byte[] skylightmap = new byte[xSize * ySize * zSize];
		byte[] blocklightmap = new byte[xSize * ySize * zSize];
		for (int i = 0; i < skylightmap.length; i++) {
			int b = dis.readByte() & 0xFF;
			skylightmap[i] = (byte)(b & 15);
			blocklightmap[i] = (byte)(b >> 4);
		}
		Biome[] biomemap = new Biome[xSize * zSize];
		for (int i = 0; i < biomemap.length; i++) {
			biomemap[i] = Biome.getBiome(dis.readInt(), Biomes.PLAINS);
		}
		
		return new FakeBlockAccess(blocks, skylightmap, blocklightmap, biomemap, xSize, ySize, zSize, ground, dimensionType, worldType, hasSkyLight);
	}
	
	public static FakeBlockAccess loadWorld(InputStream is) throws IOException, DataFormatException {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		byte[] buffer = new byte[1048576];
		int count;
		while ((count = is.read(buffer)) != -1) {
			data.write(buffer, 0, count);
		}
		return loadWorld(data.toByteArray());
	}
}
