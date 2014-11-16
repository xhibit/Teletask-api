package be.xhibit.teletask.webapp.rest;

import be.xhibit.teletask.webapp.ClientHolder;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

public class TeletaskHttpServletDispatcher extends HttpServletDispatcher {
    private static final long serialVersionUID = 2478299348723634565L;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);

        ClientHolder.getClient();
    }

    @Override
    public void destroy() {
        super.destroy();

        ClientHolder.getClient().stop();
    }

}
