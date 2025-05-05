package com.server.vulEnpoint;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;

@WebServlet("/upload-file")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class UploadFileServlet extends HttpServlet {

    // Xóa đệ quy thư mục
    private boolean deleteRecursive(File file) {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    deleteRecursive(child);
                }
            }
        }
        return file.delete();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Part filePart = req.getPart("file");
            String fileName = filePart.getSubmittedFileName();

            String uploadPath = getServletContext().getRealPath("") + File.separator + fileName;
            filePart.write(uploadPath);

            resp.getWriter().write("[+] File uploaded successfully: " + fileName + "\n");
            resp.getWriter().write("Access at: /server-vul-demo-1.0.0/" + fileName + "\n");
        } catch (Exception e) {
            resp.getWriter().write("[-] Error: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String deleteFile = req.getParameter("delete");

        try {
            if (deleteFile != null && !deleteFile.trim().isEmpty()) {
                // Xóa file cụ thể trong webapp
                String filePath = getServletContext().getRealPath("") + File.separator + deleteFile;
                File file = new File(filePath);

                if (file.exists() && file.delete()) {
                    resp.getWriter().write("[+] Deleted original: " + deleteFile + "\n");
                } else {
                    resp.getWriter().write("[-] Could not delete original: " + deleteFile + "\n");
                }
            }

            // Luôn xóa folder chứa file JSP đã compile
            File workDir = new File(System.getProperty("catalina.base"),
                    "work/Catalina/localhost/server-vul-demo-1.0.0/org/apache/jsp");

            int deletedCount = 0;

            if (workDir.exists()) {
                File[] compiledFiles = workDir.listFiles();
                if (compiledFiles != null) {
                    for (File f : compiledFiles) {
                        if (deleteRecursive(f)) {
                            deletedCount++;
                        } else {
                            resp.getWriter().write("[-] Failed to delete: " + f.getAbsolutePath() + "\n");
                        }
                    }
                }
            }

            resp.getWriter().write("[+] Deleted compiled JSP class files: " + deletedCount + "\n");

        } catch (Exception e) {
            resp.getWriter().write("[-] Error: " + e.getMessage());
        }
    }
}
