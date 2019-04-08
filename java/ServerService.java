package com.orbis.coreserver.api.services.developer;

import com.orbis.coreserver.base.Toolbox;
import com.orbis.coreserver.base.interfaces.repositories.ILogRepository;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path(ServerService.WEB_CONTEXT_PATH)
public final class ServerService {

    public static final String WEB_CONTEXT_PATH = "server";
    private final ILogRepository logRepository;

    @Inject
    public ServerService(ILogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @GET
    @Path("index.html")
    @Produces(MediaType.TEXT_HTML)
    public final String index() {
        return "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<title>Core-Server</title>"
                + "</head>"
                + "<body>"
                + "Version: 0.001"
                + "<br>"
                + "Time: " + Toolbox.dateTimeNow()
                + "</body>"
                + "</html>";
    }

    @GET
    @Path("logs.html")
    @Produces(MediaType.TEXT_HTML)
    public final String appLogs() {
        return logs(logRepository.infoLogs());
    }

    private String logs(final List<String> logs) {
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html><html><title>Core-Server</title><body>");
        html.append("<table style=\"text-align:left;\">");

        html.append("<tr>");
        html.append("<th style=\"border:1px solid black\">Logs:</th>");
        html.append("</tr>");

        logs.stream().map(l -> "<tr><td style=\"border:1px solid black;text-align:left;\">" + l + "</td></tr>").forEach(html::append);

        html.append("</table></body></html>");

        return html.toString();
    }
}
