package org.vivecraft.tweaker;

import cpw.mods.modlauncher.api.IEnvironment;
import cpw.mods.modlauncher.api.ITransformationService;
import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.IncompatibleEnvironmentException;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class VivecraftForgeTransformationService implements ITransformationService
{
//    private static final Logger LOGGER = LogManager.getLogger();
//    private static URL ofZipFileUrl;
//    private static ZipFile ofZipFile;
//    private static VivecraftForgeTransformer transformer;
//
//    public String name()
//    {
//        return "vivecraft";
//    }
//
//    public void initialize(IEnvironment environment)
//    {
//        LOGGER.info("OptiFineTransformationService.initialize");
//    }
//
//    public void beginScanning(IEnvironment environment)
//    {
//    }
//
//    public void onLoad(IEnvironment env, Set<String> otherServices) throws IncompatibleEnvironmentException
//    {
//        LOGGER.info("OptiFineTransformationService.onLoad");
//        ofZipFileUrl = VivecraftForgeTransformer.class.getProtectionDomain().getCodeSource().getLocation();
//
//        try
//        {
//            URI uri = ofZipFileUrl.toURI();
//            File file1 = new File(uri);
//            ofZipFile = new ZipFile(file1);
//            LOGGER.info("OptiFine ZIP file: " + file1);
//            transformer = new VivecraftForgeTransformer(ofZipFile);
//            OptiFineResourceLocator.setResourceLocator(transformer);
//        }
//        catch (Exception exception)
//        {
//            LOGGER.error("Error loading OptiFine ZIP file: " + ofZipFileUrl, (Throwable)exception);
//            throw new IncompatibleEnvironmentException("Error loading OptiFine ZIP file: " + ofZipFileUrl);
//        }
//    }
//
//    public Entry<Set<String>, Supplier<Function<String, Optional<URL>>>> additionalResourcesLocator()
//    {
//        return ITransformationService.super.additionalResourcesLocator();
//    }
//
//    public Entry<Set<String>, Supplier<Function<String, Optional<URL>>>> additionalClassesLocator()
//    {
//        Set<String> set = new HashSet<>();
//        set.add("net.optifine.");
//        set.add("optifine.");
//        Supplier<Function<String, Optional<URL>>> supplier = () ->
//        {
//            return this::getResourceUrl;
//        };
//        Entry<Set<String>, Supplier<Function<String, Optional<URL>>>> entry = new SimpleEntry<>(set, supplier);
//        LOGGER.info("additionalClassesLocator: " + set);
//        return entry;
//    }
//
//    public Optional<URL> getResourceUrl(String name)
//    {
//        if (name.endsWith(".class") && !name.startsWith("optifine/"))
//        {
//            name = "srg/" + name;
//        }
//
//        if (transformer == null)
//        {
//            return Optional.empty();
//        }
//        else
//        {
//            ZipEntry zipentry = ofZipFile.getEntry(name);
//
//            if (zipentry == null)
//            {
//                return Optional.empty();
//            }
//            else
//            {
//                try
//                {
//                    String s = ofZipFileUrl.toExternalForm();
//                    URL url = new URL("jar:" + s + "!/" + name);
//                    return Optional.of(url);
//                }
//                catch (IOException ioexception)
//                {
//                    LOGGER.error(ioexception);
//                    return Optional.empty();
//                }
//            }
//        }
//    }
//
//    public List<ITransformer> transformers()
//    {
//        LOGGER.info("OptiFineTransformationService.transformers");
//        List<ITransformer> list = new ArrayList<>();
//
//        if (transformer != null)
//        {
//            list.add(transformer);
//        }
//
//        return list;
//    }
//
//    public static VivecraftForgeTransformer getTransformer()
//    {
//        return transformer;
//    }
}
