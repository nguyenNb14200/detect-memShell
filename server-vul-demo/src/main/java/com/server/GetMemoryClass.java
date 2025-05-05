package com.server;

import com.example.MyAgent;
import com.example.RasMonitor;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sun.security.mscapi.CKeyPairGenerator;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/get-memory-class")
public class GetMemoryClass extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        try {
            out.println("\n--- Runtime Class Detection ---");
            out.println(RasMonitor.getRuntimeClass(MyAgent.getInstrumentation()));
        } catch (Exception e) {
            out.println("[ERROR] " + e.getMessage());
        }
    }
}
