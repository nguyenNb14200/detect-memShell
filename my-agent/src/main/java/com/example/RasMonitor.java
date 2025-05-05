package com.example;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import jakarta.servlet.Filter;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRequestListener;
import org.apache.catalina.Container;
import org.apache.catalina.Wrapper;
import org.apache.catalina.core.StandardContext;
import org.apache.tomcat.util.descriptor.web.FilterDef;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class RasMonitor {
    private static Set<String> initialLoadedClasses = new HashSet<>();
    private static Set<String> initialFilters = new HashSet<>();
    private static Set<String> initialServlets = new HashSet<>();

    private static Set<String> initialListeners = new HashSet<>();

    private static Set<String> classRegisLocalInit = new HashSet<>();

    public static String captureInitialState(Instrumentation instrumentation) {
        // Load classes regisLocal
        try (ScanResult scanResult = new ClassGraph()
                .enableAllInfo()
                .scan()) {

            scanResult.getAllClasses().forEach(classInfo -> {
                classRegisLocalInit.add(classInfo.getName());
            });
        } catch(Exception e){
            // Bỏ qua lỗi nếu có
        }
        try {
            // Load initial classes and something we focus on
            for (Class<?> cls : instrumentation.getAllLoadedClasses()) {
                initialLoadedClasses.add(cls.getName());
                if (Servlet.class.isAssignableFrom(cls)) {
                    initialServlets.add(cls.getName());
                }
                if (ServletRequestListener.class.isAssignableFrom(cls)) {
                    initialListeners.add(cls.getName());
                }
                if (Filter.class.isAssignableFrom(cls)) {
                    initialFilters.add(cls.getName());
                }
            }
            return "Initial class: " + initialLoadedClasses + "\n \n \n Initial filters: " + initialFilters + "\n \n \n Initial Servlet: " + initialServlets;
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return sw.toString();
        }
    }

    public static Set<String>  getStaticClass() {
        try {
            return classRegisLocalInit;
        } catch (Exception e) {
            Set<String> err = new HashSet<>();
            err.add("[ERROR] " + e.getMessage());
            return err;
        }
    }

    public static String getRuntimeClass(Instrumentation instrumentation){
        StringBuilder result = new StringBuilder();
        Set<String> currentClasses = new HashSet<>();
        try {
            for (Class<?> cls : instrumentation.getAllLoadedClasses()) {
                currentClasses.add(cls.getName());
            }
            currentClasses.removeAll(getStaticClass());
            result.append("Memory classes:\n").append(String.join("\n", currentClasses)).append("\n");
            return !result.isEmpty() ? result.toString() : "No new components detected";
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return sw.toString();
        }
    }

    public static String getInitStat() {
        try {
            return "Filters:\n" + String.join("\n", initialFilters) + "\nServlets:\n" + String.join("\n", initialServlets);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public static String scheduleMonitoring(Instrumentation instrumentation) {
        StringBuilder result = new StringBuilder();
        Set<String> currentServlets = new HashSet<>();
        Set<String> currentFilters = new HashSet<>();
        Set<String> currentClasses = new HashSet<>();
        Set<String> listenerClasses = new HashSet<>();

        try {
            for (Class<?> cls : instrumentation.getAllLoadedClasses()) {
                currentClasses.add(cls.getName());
                if (ServletRequestListener.class.isAssignableFrom(cls)) {
                    listenerClasses.add(cls.getName());
                }
                if (Servlet.class.isAssignableFrom(cls)) {
                    currentServlets.add(cls.getName());
                }
                if (Filter.class.isAssignableFrom(cls)) {
                    currentFilters.add(cls.getName());
                }
            }

            // diff and print
            Set<String> newClasses = new HashSet<>(currentClasses);
            newClasses.removeAll(initialLoadedClasses);
            result.append("New classes:\n").append(String.join("\n", newClasses)).append("\n");

            Set<String> newListener = new HashSet<>(listenerClasses);
            newListener.removeAll(initialListeners);
            result.append("\n \n \nNew Listener:\n").append(String.join("\n", newListener)).append("\n");

            Set<String> newFilters = new HashSet<>(currentFilters);
            newFilters.removeAll(initialFilters);
            result.append("\n \n \nNew filters:\n").append(String.join("\n", newFilters)).append("\n");

            Set<String> newServlets = new HashSet<>(currentServlets);
            newServlets.removeAll(initialServlets);
            result.append("\n \n \nNew servlets:\n").append(String.join("\n", newServlets)).append("\n");

            newClasses.removeAll(getStaticClass());
            result.append("\n \n \nClass not regis local:\n").append(String.join("\n", newClasses)).append("\n");

            return !result.isEmpty() ? result.toString() : "No new components detected";
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return sw.toString();
        }
    }
}