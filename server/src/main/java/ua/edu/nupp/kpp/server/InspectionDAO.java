package ua.edu.nupp.kpp.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InspectionDAO {

    public void add(Inspection i) throws SQLException {
        String sql = "INSERT INTO inspection(tree_id, inspection_date, f1, f2, f3, risk_index) VALUES(?,?,?,?,?,?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, i.getTreeId());
            p.setString(2, i.getInspectionDate());
            p.setDouble(3, i.getF1());
            p.setDouble(4, i.getF2());
            p.setDouble(5, i.getF3());
            p.setDouble(6, i.getRiskIndex());
            p.executeUpdate();
        }
    }

    public List<Inspection> getAll() throws SQLException {
        List<Inspection> list = new ArrayList<>();
        String sql = "SELECT id, tree_id, inspection_date, f1, f2, f3, risk_index FROM inspection ORDER BY id";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {
            while (r.next()) {
                list.add(new Inspection(
                        r.getLong("id"),
                        r.getLong("tree_id"),
                        r.getString("inspection_date"),
                        r.getDouble("f1"),
                        r.getDouble("f2"),
                        r.getDouble("f3"),
                        r.getDouble("risk_index")
                ));
            }
        }
        return list;
    }

    public Inspection getById(long id) throws SQLException {
        String sql = "SELECT id, tree_id, inspection_date, f1, f2, f3, risk_index FROM inspection WHERE id=?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            try (ResultSet r = p.executeQuery()) {
                return r.next() ? new Inspection(
                        r.getLong("id"),
                        r.getLong("tree_id"),
                        r.getString("inspection_date"),
                        r.getDouble("f1"),
                        r.getDouble("f2"),
                        r.getDouble("f3"),
                        r.getDouble("risk_index")
                ) : null;
            }
        }
    }

    public void update(Inspection i) throws SQLException {
        String sql = "UPDATE inspection SET tree_id=?, inspection_date=?, f1=?, f2=?, f3=?, risk_index=? WHERE id=?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, i.getTreeId());
            p.setString(2, i.getInspectionDate());
            p.setDouble(3, i.getF1());
            p.setDouble(4, i.getF2());
            p.setDouble(5, i.getF3());
            p.setDouble(6, i.getRiskIndex());
            p.setLong(7, i.getId());
            p.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement("DELETE FROM inspection WHERE id=?")) {
            p.setLong(1, id);
            p.executeUpdate();
        }
    }
}
