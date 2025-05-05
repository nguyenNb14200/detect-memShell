package com.example;

import java.lang.instrument.Instrumentation;
public class MyAgent {
    private static volatile Instrumentation instrumentation;

    public static void premain(String agentArgs, Instrumentation inst) {
        try {
            System.out.println("Java Agent is starting with instrumentation: " + inst);
            instrumentation = inst;
        } catch (Exception e) {
            System.out.println("Java Agent is starting with instrumentation: " + e.getMessage());
        }
    }

    // Phương thức để truy cập Instrumentation từ các phần khác của ứng dụng
    public static Instrumentation getInstrumentation() {
        if (instrumentation == null) {
            throw new IllegalStateException("Instrumentation not initialized");
        }
        return instrumentation;
    }
}
