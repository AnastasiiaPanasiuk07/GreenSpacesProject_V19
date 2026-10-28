package ua.edu.nupp.kpp.server;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class InspectionService {
    private static final Logger LOG = Logger.getLogger(InspectionService.class.getName());
    private final InspectionDAO inspectionDAO = new InspectionDAO();
    private final TreeDAO treeDAO = new TreeDAO();

    public List<Inspection> getAll() throws SQLException {
        return inspectionDAO.getAll();
    }

    public Inspection getById(Long id) throws SQLException {
        return inspectionDAO.getById(id);
    }

    public void add(Inspection inspection) throws SQLException {
        validate(inspection);
        requireTree(inspection.getTreeId());

        double calculatedIndex = RiskCalculator.calculate(
                inspection.getF1(),
                inspection.getF2(),
                inspection.getF3());
        inspection.setRiskIndex(calculatedIndex);

        inspectionDAO.add(inspection);
        LOG.info("Успішно створено новий огляд. Розраховано індекс ризику: " + calculatedIndex);
    }

    public void update(Inspection inspection) throws SQLException {
        validate(inspection);
        requireTree(inspection.getTreeId());

        Inspection old = inspectionDAO.getById(inspection.getId());
        if (old == null) {
            throw new IllegalArgumentException("Огляд не знайдено");
        }

        double calculatedIndex = RiskCalculator.calculate(
                inspection.getF1(),
                inspection.getF2(),
                inspection.getF3());
        inspection.setRiskIndex(calculatedIndex);

        inspectionDAO.update(inspection);
        LOG.info("Оновлено огляд з ID: " + inspection.getId());
    }

    public void delete(Long id) throws SQLException {
        inspectionDAO.delete(id);
        LOG.info("Видалено огляд з ID: " + id);
    }

    private void validate(Inspection inspection) {
        if (inspection == null) {
            LOG.warning("Спроба передати нульовий об'єкт огляду");
            throw new IllegalArgumentException("Огляд не може бути null");
        }
        if (inspection.getTreeId() <= 0) {
            LOG.warning("Валідація не пройшла: некоректний treeId = " + inspection.getTreeId());
            throw new IllegalArgumentException("Некоректний treeId");
        }
        if (inspection.getF1() < 0 || inspection.getF1() > 100 ||
                inspection.getF2() < 0 || inspection.getF2() > 100 ||
                inspection.getF3() < 0 || inspection.getF3() > 100) {
            LOG.warning("Валідація не пройшла: фактори ризику поза діапазоном 0..100 (F1=" +
                    inspection.getF1() + ", F2=" + inspection.getF2() + ", F3=" + inspection.getF3() + ")");
            throw new IllegalArgumentException("Фактори мають бути в діапазоні 0..100");
        }
    }

    private void requireTree(long treeId) throws SQLException {
        Tree tree = treeDAO.getById(treeId);
        if (tree == null) {
            LOG.warning("Спроба посилатися на неіснуюче дерево");
            throw new IllegalArgumentException("Дерево не знайдено");
        }
    }
}
