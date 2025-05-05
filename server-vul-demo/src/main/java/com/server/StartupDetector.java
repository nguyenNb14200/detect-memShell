package  com.server;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.apache.catalina.Container;
import org.apache.catalina.Wrapper;
import org.apache.catalina.core.StandardContext;

import java.lang.reflect.Field;

public class StartupDetector implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Object appCtx = getField(sce.getServletContext(), "context");
            StandardContext stdCtx = (StandardContext) getField(appCtx, "context");

            for (Container container : stdCtx.findChildren()) {
                if (container instanceof Wrapper) {
                    Wrapper w = (Wrapper) container;
                    String servletClass = w.getServletClass();
                    if (servletClass != null && servletClass.contains("MemShell")) {
                        System.err.println("[DETECT] 🔥 Detected suspicious injected servlet: " + servletClass +
                                " at path /" + w.findMappings()[0]);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[DETECT] Error during startup detection: " + e);
        }
    }

    private Object getField(Object target, String fieldName) throws Exception {
        java.lang.reflect.Field f = target.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        return f.get(target);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
}
