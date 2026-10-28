package ua.edu.nupp.kpp.server;

public class Tree {
    private Long id;
    private String species;
    private String location;
    private int plantingDate;
    private int conditionScore;

    public Tree(){
    }
    public Tree(Long id, String species, String location, int plantingDate, int conditionScore){
        this.id=id;
        this.species=species;
        this.location=location;
        this.plantingDate=plantingDate;
        this.conditionScore=conditionScore;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getPlantingDate() {
        return plantingDate;
    }

    public void setPlantingDate(int plantingDate) {
        this.plantingDate = plantingDate;
    }

    public int getConditionScore() {
        return conditionScore;
    }

    public void setConditionScore(int conditionScore) {
        this.conditionScore = conditionScore;
    }
}
