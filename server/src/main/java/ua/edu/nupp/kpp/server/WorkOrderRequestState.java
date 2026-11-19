package ua.edu.nupp.kpp.server;

public enum WorkOrderRequestState {
    CREATED,
    CHECKED,
    PLANNED,
    DONE;

    public static boolean isAllowedTransition(String current, String next) {
        try {
            WorkOrderRequestState curr = WorkOrderRequestState.valueOf(current);
            WorkOrderRequestState nxt = WorkOrderRequestState.valueOf(next);

            if (curr == CREATED && nxt == CHECKED) return true;
            if (curr == CHECKED && nxt == PLANNED) return true;
            if (curr == PLANNED && nxt == DONE) return true;

            return false;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
