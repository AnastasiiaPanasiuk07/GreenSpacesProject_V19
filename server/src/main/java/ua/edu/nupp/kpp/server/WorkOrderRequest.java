package ua.edu.nupp.kpp.server;

public class WorkOrderRequest {
    private Long id;
    private long inspectionId;
    private WorkOrderRequestState state;
    private String description;

    public WorkOrderRequest(){
    }
    public WorkOrderRequest(Long id, long inspectionId, WorkOrderRequestState state, String description){
        this.id=id;
        this.inspectionId=inspectionId;
        this.state=state;
        this.description=description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getInspectionId() {
        return inspectionId;
    }

    public void setInspectionId(long inspectionId) {
        this.inspectionId = inspectionId;
    }

    public WorkOrderRequestState getState() {
        return state;
    }

    public void setState(WorkOrderRequestState state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
