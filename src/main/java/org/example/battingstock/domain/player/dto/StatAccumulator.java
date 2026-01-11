package org.example.battingstock.domain.player.dto;

import lombok.Getter;

@Getter
public class StatAccumulator {
    private String name;
    private String team;
    private String position;
    private int lastYear = 0;

    // 합산할 지표들
    private double totalWar = 0;
    private double totalAvg = 0;
    private double totalOps = 0;
    private int totalHr = 0;
    private int count = 0;

    public StatAccumulator(String name, String team, String position, int year) {
        this.name = name;
        this.team = team;
        this.position = position;
        this.lastYear = year;
    }

    public void addStats(double war, double avg, double ops, int hr, int year, String currentTeam) {
        if (year > this.lastYear) {
            this.lastYear = year;
            this.team = currentTeam;
        }

        this.totalWar += war;
        this.totalAvg += avg;
        this.totalOps += ops;
        this.totalHr += hr;
        this.count++;
    }

    public double getAverageWar() {
        if (count == 0) return 0.0;
        double avgWar = totalWar / count;
        return Math.round(avgWar * 1000.0) / 1000.0;
    }

    public double getAverageAvg() {
        if (count == 0) return 0.0;
        double avgAvg = totalAvg / count;
        return Math.round(avgAvg * 1000.0) / 1000.0;
    }

    public double getAverageOps() {
        if (count == 0) return 0.0;
        double avgOps = totalOps / count;
        return Math.round(avgOps * 1000.0) / 1000.0;
    }

    public Integer getAverageHr() {
        if (count == 0) return 0;
        return (int) Math.round((double) totalHr / count);
    }
}