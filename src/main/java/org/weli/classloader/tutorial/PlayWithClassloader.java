package org.weli.classloader.tutorial;

/**
 * Created by weinanli on 9/8/16.
 */
public class PlayWithClassloader {
    public static void main(String[] args) throws Exception {


        {
            System.out.println(ClassLoader.getSystemClassLoader());
        }

        {
            Class builderClazz = ClassLoader.getSystemClassLoader().loadClass("java.lang.StringBuilder");
            System.out.println(builderClazz);
            StringBuilder builder = (StringBuilder) builderClazz.newInstance();
            builder.append("CAFE");
            System.out.println(builder.toString());
        }

        {
            SimpleClassLoader cl = new SimpleClassLoader("/usr/share/java");
            for (String dir : cl.getDirs()) {
                System.out.println(dir);
            }
        }

    }
}
