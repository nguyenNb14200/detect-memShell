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

@WebServlet("/set-init-stat")
public class GetInitStat extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        try {
            out.println("\n--- Runtime Servlet Detection ---");
            ServletContext servletContext = req.getSession().getServletContext();
            out.println(RasMonitor.captureInitialState(MyAgent.getInstrumentation()));
        } catch (Exception e) {
            out.println("[ERROR] " + e.getMessage());
        }
    }
}

