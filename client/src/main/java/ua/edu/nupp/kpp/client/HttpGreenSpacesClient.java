package ua.edu.nupp.kpp.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class HttpGreenSpacesClient {
    private static final String BASE = "http://localhost:8080/green-spaces-server";
    private final HttpClient http = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Tree> getTrees() {
        return Arrays.asList(read(get("/trees"), Tree[].class));
    }

    public void addTree(Tree t) {
        post("/trees", "action=add&species=" + enc(t.getSpecies()) + "&location=" + enc(t.getLocation()) +
                "&plantingDate=" + t.getPlantingDate() + "&conditionScore=" + t.getConditionScore());
    }

    public void updateTree(Tree t) {
        post("/trees", "action=update&id=" + t.getId() + "&species=" + enc(t.getSpecies()) +
                "&location=" + enc(t.getLocation()) + "&plantingDate=" + t.getPlantingDate() + "&conditionScore=" + t.getConditionScore());
    }

    public void deleteTree(long id) {
        post("/trees","action=delete&id=" + id);
    }

    public List<Inspection> getInspections() {
        return Arrays.asList(read(get("/inspections"), Inspection[].class));
    }

    public void addInspection(Inspection i) {
        post("/inspections", inspectionForm("add", i));
    }

    public void updateInspection(Inspection i) {
        post("/inspections", inspectionForm("update", i) + "&id=" + i.getId());
    }

    public void deleteInspection(long id) {
        post("/inspections","action=delete&id=" + id);
    }

    public List<WorkOrderRequest> getWorkOrders() {
        return Arrays.asList(read(get("/work-orders"), WorkOrderRequest[].class));
    }

    public void addWorkOrder(long inspectionId, String description) {
        // Додано обов'язковий стан CREATED при створенні
        post("/work-orders", "action=add&inspectionId=" + inspectionId + "&state=CREATED&description=" + enc(description));
    }

    public void updateWorkOrder(long id, long inspectionId, WorkOrderRequestState state, String description) {
        // Додано обов'язковий стан при оновленні
        String stateStr = (state != null) ? state.name() : "CREATED";
        post("/work-orders", "action=update&id=" + id + "&inspectionId=" + inspectionId + "&state=" + stateStr + "&description=" + enc(description));
    }

    public void changeWorkOrderState(long id, WorkOrderRequestState state) {
        post("/work-orders", "action=transition&id=" + id + "&state=" + state.name());
    }

    public void deleteWorkOrder(long id) {
        post("/work-orders","action=delete&id=" + id);
    }

    public List<CrewAssignment> getCrewAssignments() {
        return Arrays.asList(read(get("/crew-assignments"), CrewAssignment[].class));
    }

    public void addCrewAssignment(CrewAssignment c) {
        post("/crew-assignments", crewForm("add", c));
    }

    public void updateCrewAssignment(CrewAssignment c) {
        post("/crew-assignments", crewForm("update", c) + "&id=" + c.getId());
    }

    public void deleteCrewAssignment(long id) {
        post("/crew-assignments","action=delete&id=" + id);
    }

    private String inspectionForm(String action, Inspection i) {
        // Додано обов'язковий riskIndex у тіло запиту
        return "action=" + action + "&treeId=" + i.getTreeId() + "&inspectionDate=" + enc(i.getInspectionDate()) +
                "&f1=" + i.getF1() + "&f2=" + i.getF2() + "&f3=" + i.getF3() + "&riskIndex=" + i.getRiskIndex();
    }

    private String crewForm(String action, CrewAssignment c) {
        return "action=" + action + "&requestId=" + c.getRequestId() + "&crewName=" + enc(c.getCrewName()) +
                "&description=" + enc(c.getDescription());
    }

    private String get(String path) {
        HttpRequest req = HttpRequest.newBuilder(URI.create(BASE + path)).GET().build();
        return send(req);
    }

    private String post(String path, String form) {
        HttpRequest req = HttpRequest.newBuilder(URI.create(BASE + path))
                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(form)).build();
        return send(req);
    }

    private String send(HttpRequest req) {
        try {
            HttpResponse<String> r = http.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (r.statusCode() >= 400) throw new RuntimeException(r.body());
            return r.body();
        } catch (Exception e) {
            throw new RuntimeException("Помилка HTTP: " + e.getMessage(), e);
        }
    }

    private <T> T read(String json, Class<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (Exception e) {
            throw new RuntimeException("Помилка JSON", e);
        }
    }

    private String enc(String s) {
        return URLEncoder.encode(s == null ? "" : s, StandardCharsets.UTF_8);
    }
}