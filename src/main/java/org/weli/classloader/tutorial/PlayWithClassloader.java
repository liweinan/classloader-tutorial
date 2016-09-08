package org.weli.classloader.tutorial;

import org.weli.classloader.tutorial.data.Product;
import org.weli.classloader.tutorial.data.color.Red;
import org.weli.classloader.tutorial.data.impl.ProductImpl;

import java.lang.reflect.Method;
import java.nio.file.Paths;

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

        {
            String pwd = Paths.get("").toAbsolutePath().toString();
            ClassLoader cl = new SimpleClassLoader(pwd + "/build/classes/main/org/weli/classloader/tutorial/data/impl");
            Class clazz = cl.loadClass("org.weli.classloader.tutorial.data.impl.ProductImpl");
            System.out.println(clazz);
        }

        {
            String pwd = Paths.get("").toAbsolutePath().toString();
            ClassLoader loader1 = new SimpleClassLoader(pwd + "/build/classes/main/org/weli/classloader/tutorial/data/impl");
            Class clazz1 = loader1.loadClass("org.weli.classloader.tutorial.data.impl.ProductImpl");

            ClassLoader loader2 = new SimpleClassLoader(pwd + "/build/classes/main/org/weli/classloader/tutorial/data/impl");
            Class clazz2 = loader2.loadClass("org.weli.classloader.tutorial.data.impl.ProductImpl");

            // 查看各自己使用的ClassLoader
            System.out.println(clazz1.getClassLoader());
            System.out.println(clazz2.getClassLoader());

            // 看看JVM是否认为myClass1和myClass2是同一个Class
            System.out.println(clazz1 == clazz2);

            System.out.println("clazz1: " + clazz1);
            try {
                ProductImpl instance = (ProductImpl) clazz1.newInstance();
            } catch (ClassCastException e) {
                System.out.println("EXPECTED: ");
                System.out.println(e);
            }

        }

        {
            String pwd = Paths.get("").toAbsolutePath().toString();
            ClassLoader loader = new SimpleClassLoader(pwd + "/build/classes/main/org/weli/classloader/tutorial/data/color");
            Class clazz = loader.loadClass("org.weli.classloader.tutorial.data.color.Red");
            Method m = clazz.getMethod("show");
            m.invoke(clazz.newInstance());
        }
    }
}
