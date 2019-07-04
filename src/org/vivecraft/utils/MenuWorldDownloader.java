package org.vivecraft.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.zip.DataFormatException;

public class MenuWorldDownloader {
	private static final String baseUrl = "https://cache.techjargaming.com/vivecraft/114/";
	private static boolean init;
	private static int worldCount;
	private static Random rand;
	
	public static void init() {
		if (init) return;
		try {
			worldCount = Integer.parseInt(Utils.httpReadLine(baseUrl + "menuworldcount.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		rand = new Random();
		rand.nextInt();
		init = true;
	}

	public static void downloadWorld(String path) throws IOException, NoSuchAlgorithmException {
		File file = new File(path);
		file.getParentFile().mkdirs();
		if (file.exists()) {
			String localSha1 = Utils.getFileChecksum(file, "SHA-1");
			String remoteSha1 = Utils.httpReadLine(baseUrl + "checksum.php?file=" + path);
			if (localSha1.equals(remoteSha1)) {
				System.out.println("SHA-1 matches for " + path);
				return;
			}
		}
		System.out.println("Downloading world " + path);
		Utils.httpReadToFile(baseUrl + path, file, true);
	}
	
	public static InputStream getRandomWorld() throws IOException, NoSuchAlgorithmException {
		init();
		InputStream customWorld = getCustomWorld();
		if (customWorld != null) return customWorld;
		if (worldCount == 0) {
			return getRandomWorldFallback();
		}
		try {
			String path = "menuworlds/world" + rand.nextInt(worldCount) + ".mmw";
			downloadWorld(path);
			return new FileInputStream(path);
		} catch (IOException e) {
			e.printStackTrace();
			return getRandomWorldFallback();
		}
	}

	private static InputStream getCustomWorld() throws IOException {
		File dir = new File("menuworlds/custom");
		if (dir.exists()) {
			File file = getRandomFileInDirectory(dir);
			if (file != null) {
				System.out.println("Using custom world menuworlds/custom/" + file.getName());
				return new FileInputStream(file);
			}
		}
		File customFile = new File("menuworlds/worldcustom.mmw");
		if (customFile.exists()) {
			int version = MenuWorldExporter.readVersion(customFile);
			if (version >= MenuWorldExporter.MIN_VERSION && version <= MenuWorldExporter.VERSION) {
				System.out.println("Using custom world menuworlds/worldcustom.mmw");
				return new FileInputStream(customFile);
			}
		}
		return null;
	}
	
	private static InputStream getRandomWorldFallback() throws IOException {
		System.out.println("Couldn't download a world, trying random file from directory");
		File dir = new File("menuworlds");
		if (dir.exists()) {
			File file = getRandomFileInDirectory(dir);
			if (file != null) {
				System.out.println("Using world menuworlds/" + file.getName());
				return new FileInputStream(file);
			}
		}
		return null;
	}

	private static File getRandomFileInDirectory(File dir) throws IOException {
		List<File> files = Arrays.asList(dir.listFiles(file -> file.isFile() && file.getName().toLowerCase().endsWith(".mmw")));
		if (files.size() > 0) {
			Collections.shuffle(files, rand);
			for (File file : files) {
				int version = MenuWorldExporter.readVersion(file);
				if (version >= MenuWorldExporter.MIN_VERSION && version <= MenuWorldExporter.VERSION)
					return file;
			}
		}
		return null;
	}
}
