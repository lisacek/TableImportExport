package com.quant.utils;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.ParameterizedType;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectionUtils {

    public static List<Class<?>> getClassesInPackage(String packageName) {
        var classes = new ArrayList<Class<?>>();

        try {
            var urls = Thread.currentThread().getContextClassLoader().getResources(packageName.replace(".", "/"));

            while (urls.hasMoreElements()) {
                var url = urls.nextElement();
                var protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    var filePath = URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8);
                    findClassesInPackage(packageName, filePath, classes);
                } else if ("jar".equals(protocol)) {
                    try (var jar = ((JarURLConnection) url.openConnection()).getJarFile()) {
                        jar.stream().forEach(entry -> {
                            var name = entry.getName();
                            if (name.startsWith(packageName.replace(".", "/")) && name.endsWith(".class")) {
                                var className = name.replace("/", ".").substring(0, name.length() - 6);
                                try {
                                    classes.add(Thread.currentThread().getContextClassLoader().loadClass(className));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return classes;
    }

    private static void findClassesInPackage(String packageName, String packagePath, List<Class<?>> classes) {
        var dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }

        var files = dir.listFiles(file -> (file.isDirectory()) || (file.getName().endsWith(".class")));
        assert files != null : "Files not found";
        for (var file : files) {
            if (file.isDirectory()) {
                findClassesInPackage(packageName + "." + file.getName(), file.getAbsolutePath(), classes);
                continue;
            }

            var className = file.getName().substring(0, file.getName().length() - 6);
            try {
                classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + "." + className));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Class<?> getGenericType(Class<?> clazz, int index) {
        var genericInterfaces = clazz.getGenericInterfaces();
        return (Class<?>) ((ParameterizedType) genericInterfaces[0]).getActualTypeArguments()[index];
    }

    public static Object normalizeValue(Object value, Class<?> type) {
        if (type == String.class) {
            return value.toString();
        }

        if (type == Integer.class || type == int.class) {
            return Integer.parseInt(value.toString());
        }

        if (type == Double.class || type == double.class) {
            return Double.parseDouble(value.toString());
        }

        if (type == Float.class || type == float.class) {
            return Float.parseFloat(value.toString());
        }

        if (type == Boolean.class || type == boolean.class) {
            return Boolean.parseBoolean(value.toString());
        }

        if(type == Long.class || type == long.class) {
            return Long.parseLong(value.toString());
        }

        return value;
    }
}
