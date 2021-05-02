package org.vivecraft.tweaker;

import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.ITransformerVotingContext;
import cpw.mods.modlauncher.api.TransformerVoteResult;
import cpw.mods.modlauncher.api.ITransformer.Target;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

public abstract class VivecraftForgeTransformer implements ITransformer<ClassNode>
{
//    private static final Logger LOGGER = LogManager.getLogger();
//    private ZipFile ofZipFile;
//    private Map<String, String> patchMap = null;
//    private Pattern[] patterns = null;
//    public static final String PREFIX_SRG = "srg/";
//    public static final String SUFFIX_CLASS = ".class";
//    public static final String PREFIX_PATCH_SRG = "patch/srg/";
//    public static final String SUFFIX_CLASS_XDELTA = ".class.xdelta";
//    public static final String PREFIX_OPTIFINE = "optifine/";
//
//    public VivecraftForgeTransformer(ZipFile ofZipFile)
//    {
//        this.ofZipFile = ofZipFile;
//
//        try
//        {
//            this.patchMap = Patcher.getConfigurationMap(ofZipFile);
//            this.patterns = Patcher.getConfigurationPatterns(this.patchMap);
//        }
//        catch (IOException ioexception)
//        {
//            ioexception.printStackTrace();
//        }
//    }
//
//    public TransformerVoteResult castVote(ITransformerVotingContext context)
//    {
//        return TransformerVoteResult.YES;
//    }
//
//    public Set<Target> targets()
//    {
//        Set<Target> set = new HashSet<>();
//        String[] astring = this.getResourceNames("srg/", ".class");
//        String[] astring1 = this.getResourceNames("patch/srg/", ".class.xdelta");
//        astring = (String[])Utils.addObjectsToArray(astring, astring1);
//
//        for (int i = 0; i < astring.length; ++i)
//        {
//            String s = astring[i];
//            s = Utils.removePrefix(s, new String[] {"srg/", "patch/srg/"});
//            s = Utils.removeSuffix(s, new String[] {".class", ".class.xdelta"});
//
//            if (!s.startsWith("net/optifine/"))
//            {
//                Target target = Target.targetClass(s);
//                set.add(target);
//            }
//        }
//
//        LOGGER.info("Targets: " + set.size());
//        return set;
//    }
//
//    public ClassNode transform(ClassNode input, ITransformerVotingContext context)
//    {
//        ClassNode classnode = input;
//        String s = context.getClassName();
//        String s1 = s.replace('.', '/');
//        byte[] abyte = this.getOptiFineResource("srg/" + s1 + ".class");
//
//        if (abyte != null)
//        {
//            InputStream inputstream = new ByteArrayInputStream(abyte);
//            ClassNode classnode1 = this.loadClass(inputstream);
//
//            if (classnode1 != null)
//            {
//                this.debugClass(classnode1);
//                AccessFixer.fixMemberAccess(input, classnode1);
//                classnode = classnode1;
//            }
//        }
//
//        return classnode;
//    }
//
//    private void debugClass(ClassNode classNode)
//    {
//    }
//
//    private ClassNode loadClass(InputStream in)
//    {
//        try
//        {
//            ClassReader classreader = new ClassReader(in);
//            ClassNode classnode = new ClassNode(393216);
//            classreader.accept(classnode, 0);
//            return classnode;
//        }
//        catch (IOException ioexception)
//        {
//            ioexception.printStackTrace();
//            return null;
//        }
//    }
//
//    private String[] getResourceNames(String prefix, String suffix)
//    {
//        List<String> list = new ArrayList<>();
//        Enumeration <? extends ZipEntry > enumeration = this.ofZipFile.entries();
//
//        while (enumeration.hasMoreElements())
//        {
//            ZipEntry zipentry = enumeration.nextElement();
//            String s = zipentry.getName();
//
//            if (s.startsWith(prefix) && s.endsWith(suffix))
//            {
//                list.add(s);
//            }
//        }
//
//        String[] astring = list.toArray(new String[list.size()]);
//        return astring;
//    }
//
//    private byte[] getOptiFineResource(String name)
//    {
//        try
//        {
//            InputStream inputstream = this.getOptiFineResourceStream(name);
//
//            if (inputstream == null)
//            {
//                return null;
//            }
//            else
//            {
//                byte[] abyte = Utils.readAll(inputstream);
//                inputstream.close();
//                return abyte;
//            }
//        }
//        catch (IOException ioexception)
//        {
//            ioexception.printStackTrace();
//            return null;
//        }
//    }
//
//    public synchronized InputStream getOptiFineResourceStream(String name)
//    {
//        name = Utils.removePrefix(name, "/");
//        InputStream inputstream = this.getOptiFineResourceStreamZip(name);
//
//        if (inputstream != null)
//        {
//            return inputstream;
//        }
//        else
//        {
//            inputstream = this.getOptiFineResourceStreamPatched(name);
//            return inputstream != null ? inputstream : null;
//        }
//    }
//
//    public InputStream getResourceStream(String path)
//    {
//        path = Utils.ensurePrefix(path, "/");
//        return this.getClass().getResourceAsStream(path);
//    }
//
//    public synchronized InputStream getOptiFineResourceStreamZip(String name)
//    {
//        if (this.ofZipFile == null)
//        {
//            return null;
//        }
//        else
//        {
//            name = Utils.removePrefix(name, "/");
//            ZipEntry zipentry = this.ofZipFile.getEntry(name);
//
//            if (zipentry == null)
//            {
//                return null;
//            }
//            else
//            {
//                try
//                {
//                    InputStream inputstream = this.ofZipFile.getInputStream(zipentry);
//                    return inputstream;
//                }
//                catch (IOException ioexception)
//                {
//                    ioexception.printStackTrace();
//                    return null;
//                }
//            }
//        }
//    }
//
//    public synchronized byte[] getOptiFineResourceZip(String name)
//    {
//        InputStream inputstream = this.getOptiFineResourceStreamZip(name);
//
//        if (inputstream == null)
//        {
//            return null;
//        }
//        else
//        {
//            try
//            {
//                byte[] abyte = Utils.readAll(inputstream);
//                return abyte;
//            }
//            catch (IOException var4)
//            {
//                return null;
//            }
//        }
//    }
//
//    public synchronized InputStream getOptiFineResourceStreamPatched(String name)
//    {
//        byte[] abyte = this.getOptiFineResourcePatched(name);
//        return abyte == null ? null : new ByteArrayInputStream(abyte);
//    }
//
//    public synchronized byte[] getOptiFineResourcePatched(String name)
//    {
//        if (this.patterns != null && this.patchMap != null)
//        {
//            name = Utils.removePrefix(name, "/");
//            String s = "patch/" + name + ".xdelta";
//            byte[] abyte = this.getOptiFineResourceZip(s);
//
//            if (abyte == null)
//            {
//                return null;
//            }
//            else
//            {
//                try
//                {
//                    byte[] abyte1 = Patcher.applyPatch(name, abyte, this.patterns, this.patchMap, this);
//                    String s1 = "patch/" + name + ".md5";
//                    byte[] abyte2 = this.getOptiFineResourceZip(s1);
//
//                    if (abyte2 != null)
//                    {
//                        String s2 = new String(abyte2, "ASCII");
//                        byte[] abyte3 = HashUtils.getHashMd5(abyte1);
//                        String s3 = HashUtils.toHexString(abyte3);
//
//                        if (!s2.equals(s3))
//                        {
//                            throw new IOException("MD5 not matching, name: " + name + ", saved: " + s2 + ", patched: " + s3);
//                        }
//                    }
//
//                    return abyte1;
//                }
//                catch (Exception exception)
//                {
//                    exception.printStackTrace();
//                    return null;
//                }
//            }
//        }
//        else
//        {
//            return null;
//        }
//    }
}
