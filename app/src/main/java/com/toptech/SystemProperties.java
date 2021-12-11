package com.toptech;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class SystemProperties {
    private static Class aClass;

    static {
        try {
            aClass = Class.forName("android.os.SystemProperties");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean getBoolean(String s, boolean b) {
        try {
            Method method = aClass.getDeclaredMethod("getBoolean", String.class, Boolean.TYPE);
            method.setAccessible(true);
            Object invoke = method.invoke(aClass, s, b);
            return (boolean) invoke;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String get(String s) {
        try {
            Method method = aClass.getDeclaredMethod("get", String.class);
            method.setAccessible(true);
            Object invoke = method.invoke(aClass, s);
            return (String) invoke;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String get(String s, String aFalse) {
        try {
            Method method = aClass.getDeclaredMethod("get", String.class, String.class);
            method.setAccessible(true);
            return (String) method.invoke(aClass, s, aFalse);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return aFalse;
    }

    public static void set(String s, String aFalse) {
        try {
            Method method = aClass.getDeclaredMethod("set", String.class, String.class);
            method.setAccessible(true);
            method.invoke(aClass, s, aFalse);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
