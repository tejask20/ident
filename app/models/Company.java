package models;

import java.math.BigDecimal;

public class Company {
    private int id;
    private String name;
    private int slaTime;
    private BigDecimal slaPercentage;
    private BigDecimal currentSlaPercentage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSlaTime() {
        return slaTime;
    }

    public void setSlaTime(int slaTime) {
        this.slaTime = slaTime;
    }

    public BigDecimal getSlaPercentage() {
        return slaPercentage;
    }

    public void setSlaPercentage(BigDecimal slaPercentage) {
        this.slaPercentage = slaPercentage;
    }

    public BigDecimal getCurrentSlaPercentage() {
        return currentSlaPercentage;
    }

    public void setCurrentSlaPercentage(BigDecimal currentSlaPercentage) {
        this.currentSlaPercentage = currentSlaPercentage;
    }
}
