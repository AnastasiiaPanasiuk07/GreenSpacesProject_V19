package ua.edu.nupp.kpp.server;

public class Inspection {
    private Long id;
    private long treeId;
    private String inspectionDate;
    private double f1;
    private double f2;
    private double f3;
    private double riskIndex;

    public Inspection(){
    }
    public Inspection(Long id, long treeId, String inspectionDate, double f1, double f2, double f3, double riskIndex){
        this.id=id;
        this.treeId=treeId;
        this.inspectionDate=inspectionDate;
        this.f1=f1;
        this.f2=f2;
        this.f3=f3;
        this.riskIndex=riskIndex;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getTreeId() {
        return treeId;
    }

    public void setTreeId(long treeId) {
        this.treeId = treeId;
    }

    public String getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(String inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public double getF1() {
        return f1;
    }

    public void setF1(double f1) {
        this.f1 = f1;
    }

    public double getF2() {
        return f2;
    }

    public void setF2(double f2) {
        this.f2 = f2;
    }

    public double getF3() {
        return f3;
    }

    public void setF3(double f3) {
        this.f3 = f3;
    }

    public double getRiskIndex() {
        return riskIndex;
    }

    public void setRiskIndex(double riskIndex) {
        this.riskIndex = riskIndex;
    }
}
