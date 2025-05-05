package com.server.vulEnpoint;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.Base64;
import java.util.stream.Collectors;

public class DeserializeEndpoint  extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            // Đọc toàn bộ nội dung request body (Base64)
            String base64Data = new BufferedReader(new InputStreamReader(req.getInputStream()))
                    .lines()
                    .collect(Collectors.joining());

            // Decode base64 thành mảng byte
            byte[] objectBytes = Base64.getDecoder().decode(base64Data);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(objectBytes);
            ObjectInputStream in = new ObjectInputStream(byteIn);
            Leo2907 leo2 = (Leo2907) in.readObject();
            in.close();

            resp.getWriter().write("Đã tạo đối tượng từ class: " + leo2.getName());
        } catch (Exception e) {
            resp.getWriter().write("[-] Error: " + e);
        }
    }
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String classBody = req.getParameter("body");
            resp.getWriter().write("Đã tạo đối tượng từ class: " + classBody);
            byte[] data = Base64.getDecoder().decode(classBody);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(data);
            ObjectInputStream in = new ObjectInputStream(byteIn);
            Leo2907 leo2 = (Leo2907) in.readObject();
            in.close();

            resp.getWriter().write("Đã tạo đối tượng từ class: " + leo2.getName());
        } catch (Exception e) {
            resp.getWriter().write("[-] Error: " + e);
        }
    }
}