package ua.edu.nupp.kpp.server;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class WorkOrderRequestService {
    private static final Logger LOG = Logger.getLogger(WorkOrderRequestService.class.getName());

    private final WorkOrderRequestDAO requestDAO = new WorkOrderRequestDAO();
    private final InspectionDAO inspectionDAO = new InspectionDAO();

    public List<WorkOrderRequest> getAll() throws SQLException {
        return requestDAO.getAll();
    }

    public WorkOrderRequest getById(Long id) throws SQLException {
        return requestDAO.getById(id);
    }

    public void add(WorkOrderRequest request) throws SQLException {
        validate(request);
        requireInspection(request.getInspectionId());

        if (request.getState() == null) {
            request.setState(WorkOrderRequestState.CREATED);
        }

        requestDAO.add(request);
        LOG.info("Успішно створено нову заявку на роботи");
    }

    public void update(WorkOrderRequest request) throws SQLException {
        validate(request);
        requireInspection(request.getInspectionId());

        WorkOrderRequest old = requestDAO.getById(request.getId());
        if (old == null) {
            LOG.warning("Спроба оновити неіснуючу заявку з ID: " + request.getId());
            throw new IllegalArgumentException("Заявку на роботи не знайдено");
        }

        requestDAO.update(request);
        LOG.info("Оновлено заявку на роботи з ID: " + request.getId());
    }

    public void delete(Long id) throws SQLException {
        requestDAO.delete(id);
        LOG.info("Видалено заявку на роботи з ID: " + id);
    }

    public static boolean isAllowedTransition(WorkOrderRequestState from, WorkOrderRequestState to) {
        if (from == null || to == null) {
            return false;
        }
        return (from == WorkOrderRequestState.CREATED && to == WorkOrderRequestState.CHECKED)
                || (from == WorkOrderRequestState.CHECKED && to == WorkOrderRequestState.PLANNED)
                || (from == WorkOrderRequestState.PLANNED && to == WorkOrderRequestState.DONE);
    }

    public void changeState(long id, WorkOrderRequestState next) throws SQLException {
        WorkOrderRequest current = requestDAO.getById(id);
        if (current == null) {
            LOG.warning("Спроба змінити стан для неіснуючої заявки з ID: " + id);
            throw new IllegalArgumentException("Заявку на роботи не знайдено");
        }
        if (!isAllowedTransition(current.getState(), next)) {
            LOG.warning("Спроба виконати недопустимий перехід стану: " + current.getState() + " -> " + next);
            throw new IllegalArgumentException("Недопустимий перехід стану");
        }
        requestDAO.updateState(id, next);
        LOG.info("Успішно змінено стан заявки (ID: " + id + "): " + current.getState() + " -> " + next);
    }

    private void validate(WorkOrderRequest request) {
        if (request == null) {
            LOG.warning("Спроба передати нульовий об'єкт заявки");
            throw new IllegalArgumentException("Заявка не може бути null");
        }
        if (request.getInspectionId() <= 0) {
            LOG.warning("Валідація не пройшла: некоректний inspectionId = " + request.getInspectionId());
            throw new IllegalArgumentException("Некоректний inspectionId");
        }
        if (request.getDescription() == null || request.getDescription().isBlank()) {
            LOG.warning("Валідація не пройшла: відсутній обов'язковий опис заявки.");
            throw new IllegalArgumentException("Опис заявки є обов'язковим");
        }
    }

    private void requireInspection(long inspectionId) throws SQLException {
        Inspection inspection = inspectionDAO.getById(inspectionId);
        if (inspection == null) {
            LOG.warning("Спроба посилатися на неіснуючий огляд з ID: " + inspectionId);
            throw new IllegalArgumentException("Огляд не знайдено");
        }
    }
}
