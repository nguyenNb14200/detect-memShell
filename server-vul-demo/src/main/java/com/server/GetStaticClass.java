package  com.server;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/get-all-static-class")
public class GetStaticClass extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        try {
            out.println("--- All Class Tree default ---");
            try (ScanResult scanResult = new ClassGraph()
                    .enableAllInfo()
                    .scan()) {

                scanResult.getAllClasses().forEach(classInfo -> {
                    out.println(classInfo.getName());
                });
            }
            out.println("\n--- Runtime Servlet Detection ---");
        } catch (Exception e) {
            out.println("[ERROR] " + e.getMessage());
        }
    }
}
