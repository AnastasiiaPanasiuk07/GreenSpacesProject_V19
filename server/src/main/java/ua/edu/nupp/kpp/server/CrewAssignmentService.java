package ua.edu.nupp.kpp.server;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class CrewAssignmentService {
    private static final Logger LOG = Logger.getLogger(CrewAssignmentService.class.getName());
    private final CrewAssignmentDAO assignmentDAO = new CrewAssignmentDAO();
    private final WorkOrderRequestDAO requestDAO = new WorkOrderRequestDAO();

    public List<CrewAssignment> getAll() throws SQLException {
        return assignmentDAO.getAll();
    }

    public CrewAssignment getById(Long id) throws SQLException {
        return assignmentDAO.getById(id);
    }

    public void add(CrewAssignment assignment) throws SQLException {
        validate(assignment);
        requireRequest(assignment.getRequestId());
        assignmentDAO.add(assignment);
        LOG.info("Успішно створено призначення бригади на виконання робіт");
    }

    public void update(CrewAssignment assignment) throws SQLException {
        validate(assignment);
        requireRequest(assignment.getRequestId());

        CrewAssignment old = assignmentDAO.getById(assignment.getId());
        if (old == null) {
            LOG.warning("Спроба оновити неіснуюче призначення бригади з ID: " + assignment.getId());
            throw new IllegalArgumentException("Призначення бригади не знайдено");
        }

        assignmentDAO.update(assignment);
        LOG.info("Оновлено призначення бригади з ID: " + assignment.getId());
    }

    public void delete(Long id) throws SQLException {
        assignmentDAO.delete(id);
        LOG.info("Видалено призначення бригади з ID: " + id);
    }

    private void validate(CrewAssignment assignment) {
        if (assignment == null) {
            LOG.warning("Спроба передати нульовий об'єкт призначення бригади");
            throw new IllegalArgumentException("Наряд бригади не може бути null");
        }
        if (assignment.getRequestId() <= 0) {
            LOG.warning("Валідація не пройшла: некоректний requestId = " + assignment.getRequestId());
            throw new IllegalArgumentException("Некоректний requestId");
        }
        if (assignment.getCrewName() == null || assignment.getCrewName().isBlank()) {
            LOG.warning("Валідація не пройшла: назва або номер бригади (crewName) є обов'язковою.");
            throw new IllegalArgumentException("Назва бригади є обов'язковою");
        }
    }

    private void requireRequest(long requestId) throws SQLException {
        WorkOrderRequest request = requestDAO.getById(requestId);
        if (request == null) {
            LOG.warning("Спроба призначити бригаду на неіснуючу заявку з ID: " + requestId);
            throw new IllegalArgumentException("Заявку не знайдено");
        }
    }
}