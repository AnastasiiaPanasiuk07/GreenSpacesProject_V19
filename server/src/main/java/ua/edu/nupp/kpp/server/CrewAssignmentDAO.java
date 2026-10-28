package ua.edu.nupp.kpp.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CrewAssignmentDAO {

    public void add(CrewAssignment ca) throws SQLException {
        String sql = "INSERT INTO crew_assignment(request_id, crew_name, description) VALUES(?,?,?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, ca.getRequestId()); // camelCase метод
            p.setString(2, ca.getCrewName()); // camelCase метод
            p.setString(3, ca.getDescription());
            p.executeUpdate();
        }
    }

    public List<CrewAssignment> getAll() throws SQLException {
        List<CrewAssignment> list = new ArrayList<>();
        String sql = "SELECT id, request_id, crew_name, description FROM crew_assignment ORDER BY id";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {
            while (r.next()) {
                list.add(new CrewAssignment(
                        r.getLong("id"),
                        r.getLong("request_id"),  // Столібець з БД з підкресленням[cite: 1]
                        r.getString("crew_name"), // Столібець з БД з підкресленням[cite: 1]
                        r.getString("description")
                ));
            }
        }
        return list;
    }

    public CrewAssignment getById(long id) throws SQLException {
        String sql = "SELECT id, request_id, crew_name, description FROM crew_assignment WHERE id=?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            try (ResultSet r = p.executeQuery()) {
                return r.next() ? new CrewAssignment(
                        r.getLong("id"),
                        r.getLong("request_id"),
                        r.getString("crew_name"),
                        r.getString("description")
                ) : null;
            }
        }
    }

    public void update(CrewAssignment ca) throws SQLException {
        String sql = "UPDATE crew_assignment SET request_id=?, crew_name=?, description=? WHERE id=?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, ca.getRequestId());
            p.setString(2, ca.getCrewName());
            p.setString(3, ca.getDescription());
            p.setLong(4, ca.getId());
            p.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement("DELETE FROM crew_assignment WHERE id=?")) {
            p.setLong(1, id);
            p.executeUpdate();
        }
    }
}
