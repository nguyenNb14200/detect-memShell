package  com.server;

import com.example.MyAgent;
import com.example.RasMonitor;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/get-new-stat")
public class GetCurrentStat extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        try {
            out.println("\n--- Runtime Servlet Detection ---");
            out.println(RasMonitor.scheduleMonitoring(MyAgent.getInstrumentation()));
        } catch (Exception e) {
            out.println("[ERROR] " + e.getMessage());
        }
    }
}

