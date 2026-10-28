package ua.edu.nupp.kpp.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TreeDAO {
    public void add(Tree t) throws SQLException {
        String sql = "INSERT INTO tree(species, location, planting_date, condition_score) VALUES(?,?,?,?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, t.getSpecies());
            p.setString(2, t.getLocation());
            p.setInt(3, t.getPlantingDate());
            p.setInt(4, t.getConditionScore());
            p.executeUpdate();
        }
    }

    public List<Tree> getAll() throws SQLException {
        List<Tree> list = new ArrayList<>();
        String sql = "SELECT id, species, location, planting_date, condition_score FROM tree ORDER BY id";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {
            while (r.next()) {
                list.add(new Tree(
                        r.getLong("id"),
                        r.getString("species"),
                        r.getString("location"),
                        r.getInt("planting_date"),
                        r.getInt("condition_score")
                ));
            }
        }
        return list;
    }

    public Tree getById(long id) throws SQLException {
        String sql = "SELECT id, species, location, planting_date, condition_score FROM tree WHERE id=?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            try (ResultSet r = p.executeQuery()) {
                return r.next() ? new Tree(
                        r.getLong("id"),
                        r.getString("species"),
                        r.getString("location"),
                        r.getInt("planting_date"),
                        r.getInt("condition_score")
                ) : null;
            }
        }
    }

    public void update(Tree t) throws SQLException {
        String sql = "UPDATE tree SET species=?, location=?, planting_date=?, condition_score=? WHERE id=?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, t.getSpecies());
            p.setString(2, t.getLocation());
            p.setInt(3, t.getPlantingDate());
            p.setInt(4, t.getConditionScore());
            p.setLong(5, t.getId());
            p.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement("DELETE FROM tree WHERE id=?")) {
            p.setLong(1, id);
            p.executeUpdate();
        }
    }
}
