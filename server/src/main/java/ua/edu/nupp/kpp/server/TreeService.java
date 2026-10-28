package ua.edu.nupp.kpp.server;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class TreeService {
    private static final Logger LOG = Logger.getLogger(TreeService.class.getName());
    private final TreeDAO treeDAO = new TreeDAO();

    public List<Tree> getAll() throws SQLException {
        return treeDAO.getAll();
    }

    public Tree getById(Long id) throws SQLException {
        return treeDAO.getById(id);
    }

    public void add(Tree tree) throws SQLException {
        validate(tree);
        treeDAO.add(tree);
        LOG.info("Успішно додано нове дерево до бази даних");
    }

    public void update(Tree tree) throws SQLException {
        validate(tree);

        Tree old = treeDAO.getById(tree.getId());
        if (old == null) {
            LOG.warning("Спроба оновити неіснуюче дерево з ID: " + tree.getId());
            throw new IllegalArgumentException("Дерево не знайдено");
        }
        treeDAO.update(tree);
        LOG.info("Оновлено інформацію про дерево з ID: " + tree.getId());
    }

    public void delete(Long id) throws SQLException {
        treeDAO.delete(id);
        LOG.info("Видалено дерево з ID: " + id);
    }

    private void validate(Tree tree) {
        if (tree == null) {
            LOG.warning("Спроба передати нульовий об'єкт дерева");
            throw new IllegalArgumentException("Дерево не може бути null");
        }
        if (blank(tree.getSpecies())) {
            LOG.warning("Валідація не пройшла: відсутня або порожня назва виду дерева.");
            throw new IllegalArgumentException("Вид дерева є обов'язковим");
        }
        if (tree.getConditionScore() < 0 || tree.getConditionScore() > 100) {
            LOG.warning("Валідація не пройшла: оцінка стану поза діапазоном 0..100 (conditionScore = " + tree.getConditionScore() + ")");
            throw new IllegalArgumentException("Оцінка стану має бути в діапазоні від 0 до 100");
        }
    }

    private boolean blank(String v) {
        return v == null || v.isBlank();
    }
}
