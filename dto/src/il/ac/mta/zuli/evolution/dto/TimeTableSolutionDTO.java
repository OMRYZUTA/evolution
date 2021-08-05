package il.ac.mta.zuli.evolution.dto;

import java.util.List;
import java.util.Map;

public class TimeTableSolutionDTO {
    private final List<QuintetDTO> solution;
    private final int solutionSize;
    private final double totalFitnessScore;
    private final Map<RuleDTO, Double> fitnessScorePerRule;
    TimeTableDTO timeTable;

    public TimeTableSolutionDTO(List<QuintetDTO> solution, int solutionSize,
                                double totalFitnessScore, Map<RuleDTO, Double> fitnessScorePerRule,
                                TimeTableDTO timeTable) {
        //TODO - copy quintet-list and rule map properly
        this.solution = solution;
        this.solutionSize = solutionSize;
        this.totalFitnessScore = totalFitnessScore;
        this.fitnessScorePerRule = fitnessScorePerRule;
        this.timeTable = timeTable;
    }

    public List<QuintetDTO> getSolution() {
        return solution;
    }

    public int getSolutionSize() {
        return solutionSize;
    }

    public double getTotalFitnessScore() {
        return totalFitnessScore;
    }

    public Map<RuleDTO, Double> getFitnessScorePerRule() {
        return fitnessScorePerRule;
    }

    public TimeTableDTO getTimeTable() {
        return timeTable;
    }

    @Override
    public String toString() {
        return "TimeTableSolutionDTO{" +
                "solution=" + solution +
                ", solutionSize=" + solutionSize +
                ", totalFitnessScore=" + totalFitnessScore +
                ", fitnessScorePerRule=" + fitnessScorePerRule +
                ", timeTable=" + timeTable +
                '}';
    }
}
