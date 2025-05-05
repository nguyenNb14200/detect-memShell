package  com.server;

import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.Container;
import org.apache.catalina.Wrapper;
import org.apache.catalina.core.ApplicationFilterConfig;
import org.apache.catalina.core.StandardContext;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

@WebServlet("/detect-now")
public class DetectNowServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        try {
            ServletContext context = req.getServletContext();

            Field f1 = context.getClass().getDeclaredField("context");
            f1.setAccessible(true);
            Object appCtx = f1.get(context);

            Field f2 = appCtx.getClass().getDeclaredField("context");
            f2.setAccessible(true);
            StandardContext stdCtx = (StandardContext) f2.get(appCtx);

            out.println("--- Runtime Filter Detection ---");
            Field f3 = stdCtx.getClass().getDeclaredField("filterConfigs");
            f3.setAccessible(true);
            Map<String, ApplicationFilterConfig> filterConfigs = (Map<String, ApplicationFilterConfig>) f3.get(stdCtx);
            for (Map.Entry<String, ApplicationFilterConfig> entry : filterConfigs.entrySet()) {
                try {
                    Method getFilter = ApplicationFilterConfig.class.getDeclaredMethod("getFilter");
                    getFilter.setAccessible(true);
                    Object filterObj = getFilter.invoke(entry.getValue());
                    String className = filterObj.getClass().getName();
                    out.println("[Filter] " + entry.getKey() + " => " + className);
                    if (className.contains("$") || className.contains("MemShell")|| className.toLowerCase().contains("memfilter".toLowerCase())) {
                        out.println("[ALERT] Suspicious filter: " + className);
                    }
                } catch (Exception e) {
                    out.println("[Filter] " + entry.getKey() + " => (error accessing filter class)");
                }
            }

            out.println("\n--- Runtime Servlet Detection ---");
            for (org.apache.catalina.Container child : stdCtx.findChildren()) {
                if (child instanceof org.apache.catalina.Wrapper) {
                    org.apache.catalina.Wrapper w = (org.apache.catalina.Wrapper) child;
                    String servletClass = w.getServletClass();
                    out.println("[Servlet] " + w.getName() + " => " + servletClass);
                    if (servletClass != null && (servletClass.contains("$") || servletClass.toLowerCase().contains("MemShell".toLowerCase())|| servletClass.contains("memshell"))) {
                        out.println("[ALERT] Suspicious servlet: " + servletClass);
                    }
                }
            }
        } catch (Exception e) {
            out.println("[ERROR] " + e.getMessage());
        }
    }
}

