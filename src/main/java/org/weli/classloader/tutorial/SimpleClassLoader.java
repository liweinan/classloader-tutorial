package org.weli.classloader.tutorial;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SimpleClassLoader extends ClassLoader {
    String[] dirs;

    public SimpleClassLoader(String path) {
        // we support multiple paths
        // for example: /usr:/tmp
        dirs = path.split(System.getProperty("path.separator"));
        String[] _dirs = dirs.clone();
        for (String _dir : _dirs) {
            extendClasspath(_dir);
        }
    }

    public void extendClasspath(String path) {
        String[] segments = path.split("/");
        String[] subDirs = new String[segments.length];
        for (int i = 0; i < (segments.length); i++) {
            subDirs[i] = combineSegments(segments, i);
        }

        String[] newDirs = new String[dirs.length + subDirs.length];
        System.arraycopy(dirs, 0, newDirs, 0, dirs.length);
        System.arraycopy(subDirs, 0, newDirs, dirs.length, subDirs.length);
        dirs = newDirs;
    }

    private String combineSegments(String[] pathSegments, int level) {
        StringBuffer path = new StringBuffer();
        for (int i = 0; i < level; i++) {
            path.append(pathSegments[i]).append("/");
        }
        return path.toString();
    }

    public String[] getDirs() {
        return dirs;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        try {
            Class clazz = findClass(name);
            return clazz;
        } catch (ClassNotFoundException e) {
            return super.loadClass(name);
        }
    }

    protected Class<?> findClass(String name) throws ClassNotFoundException {
        for (String dir : dirs) {
            byte[] buf = getClassData(dir, name);
            if (buf != null) {
                System.out.println("Loading class:" + name + " From: " + dir);
                return defineClass(name, buf, 0, buf.length);
            }
        }
        throw new ClassNotFoundException();
    }


    protected byte[] getClassData(String directory, String name) {
        String[] tokens = name.split("\\.");
        String classFile = directory + "/" + tokens[tokens.length - 1]
                + ".class";
        File f = (new File(classFile));
        int classSize = (new Long(f.length())).intValue();
        byte[] buf = new byte[classSize];
        try {
            FileInputStream filein = new FileInputStream(classFile);
            classSize = filein.read(buf);
            filein.close();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
        return buf;
    }
}