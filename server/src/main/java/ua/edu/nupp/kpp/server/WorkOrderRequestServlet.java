package ua.edu.nupp.kpp.server;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Map;

@WebServlet("/work-orders")
public class WorkOrderRequestServlet extends HttpServlet {
    private final WorkOrderRequestService service = new WorkOrderRequestService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            writeJson(resp, service.getAll());
        } catch (Exception e) {
            writeError(resp, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            String action = req.getParameter("action");
            if ("delete".equals(action)) {
                service.delete(id(req));
            } else if ("transition".equals(action)) {
                // Додана обробка переходу станів
                service.changeState(id(req), WorkOrderRequestState.valueOf(req.getParameter("state")));
            } else if ("add".equals(action) || "update".equals(action)) {
                WorkOrderRequest value = new WorkOrderRequest(
                        "update".equals(action) ? id(req) : null,
                        Long.parseLong(req.getParameter("inspectionId")),
                        WorkOrderRequestState.valueOf(req.getParameter("state")),
                        req.getParameter("description")
                );
                if ("add".equals(action)) {
                    service.add(value);
                } else {
                    service.update(value);
                }
            } else {
                throw new IllegalArgumentException("Невідома операція");
            }
            writeJson(resp, service.getAll());
        } catch (Exception e) {
            writeError(resp, e);
        }
    }

    private void writeJson(HttpServletResponse resp, Object data) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        mapper.writeValue(resp.getWriter(), data);
    }

    private void writeError(HttpServletResponse resp, Exception e) throws IOException {
        boolean clientError = e instanceof IllegalArgumentException;
        int status = clientError ? HttpServletResponse.SC_BAD_REQUEST : HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        String message = clientError ? (e.getMessage() == null ? "Некоректний запит" : e.getMessage()) : "Внутрішня помилка сервера";
        resp.setStatus(status);
        writeJson(resp, Map.of("error", message));
    }

    private long id(HttpServletRequest req) {
        return Long.parseLong(req.getParameter("id"));
    }
}
