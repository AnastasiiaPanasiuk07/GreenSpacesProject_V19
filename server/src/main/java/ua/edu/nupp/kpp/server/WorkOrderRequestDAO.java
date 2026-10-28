package ua.edu.nupp.kpp.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkOrderRequestDAO {
    public void add(WorkOrderRequest req) throws SQLException {
        String sql = "INSERT INTO work_order_request(inspection_id, state, description) VALUES(?,?,?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, req.getInspectionId()); // camelCase метод
            p.setString(2, req.getState().name());
            p.setString(3, req.getDescription());
            p.executeUpdate();
        }
    }

    public List<WorkOrderRequest> getAll() throws SQLException {
        List<WorkOrderRequest> list = new ArrayList<>();
        String sql = "SELECT id, inspection_id, state, description FROM work_order_request ORDER BY id";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {
            while (r.next()) {
                list.add(new WorkOrderRequest(
                        r.getLong("id"),
                        r.getLong("inspection_id"),
                        WorkOrderRequestState.valueOf(r.getString("state")),
                        r.getString("description")));
            }
        }
        return list;
    }

    public WorkOrderRequest getById(long id) throws SQLException {
        String sql = "SELECT id, inspection_id, state, description FROM work_order_request WHERE id=?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            try (ResultSet r = p.executeQuery()) {
                return r.next() ? new WorkOrderRequest(
                        r.getLong("id"),
                        r.getLong("inspection_id"),
                        WorkOrderRequestState.valueOf(r.getString("state")),
                        r.getString("description")
                ) : null;
            }
        }
    }

    public void update(WorkOrderRequest req) throws SQLException {
        String sql = "UPDATE work_order_request SET inspection_id=?, state=?, description=? WHERE id=?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, req.getInspectionId());
            p.setString(2, req.getState().name());
            p.setString(3, req.getDescription());
            p.setLong(4, req.getId());
            p.executeUpdate();
        }
    }

    public void updateState(long id, WorkOrderRequestState state) throws SQLException {
        String sql = "UPDATE work_order_request SET state=? WHERE id=?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, state.name());
            p.setLong(2, id);
            p.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement("DELETE FROM work_order_request WHERE id=?")) {
            p.setLong(1, id);
            p.executeUpdate();
        }
    }
}
