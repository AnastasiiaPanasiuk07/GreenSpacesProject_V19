package ua.edu.nupp.kpp.server;

public class CrewAssignment {
    private Long id;
    private long requestId;
    private String crewName;
    private String description;

    public CrewAssignment(){
    }
    public CrewAssignment(Long id, long requestId, String crewName, String description){
        this.id=id;
        this.requestId=requestId;
        this.crewName=crewName;
        this.description=description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public String getCrewName() {
        return crewName;
    }

    public void setCrewName(String crewName) {
        this.crewName = crewName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
