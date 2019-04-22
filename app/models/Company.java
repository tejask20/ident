package models;

import java.math.BigDecimal;

public class Company {
    private int id;
    private String name;
    private int slaTime;
    private BigDecimal slaPercentage;
    private BigDecimal currentSlaPercentage;

    public Company() {
    }

    public Company(int id, String name, int slaTime, BigDecimal slaPercentage, BigDecimal currentSlaPercentage) {
        this.id = id;
        this.name = name;
        this.slaTime = slaTime;
        this.slaPercentage = slaPercentage;
        this.currentSlaPercentage = currentSlaPercentage;
    }

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

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", slaTime=" + slaTime +
                ", slaPercentage=" + slaPercentage +
                ", currentSlaPercentage=" + currentSlaPercentage +
                '}';
    }
}
