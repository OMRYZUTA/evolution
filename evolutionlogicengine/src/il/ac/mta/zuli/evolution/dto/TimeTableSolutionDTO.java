package il.ac.mta.zuli.evolution.dto;

import java.util.List;
import java.util.Map;

public class TimeTableSolutionDTO {
    private final List<QuintetDTO> solution;
    private final int solutionSize;
    private final double totalFitnessScore;
    private final Map<RuleDTO, Double> fitnessScorePerRule;
    private final TimeTableDTO timeTable;

    public TimeTableSolutionDTO(List<QuintetDTO> solution, int solutionSize,
                                double totalFitnessScore, Map<RuleDTO, Double> fitnessScorePerRule,
                                TimeTableDTO timeTable) {
        this.solution = solution;
        this.solutionSize = solutionSize;
        this.totalFitnessScore = totalFitnessScore;
        this.fitnessScorePerRule = fitnessScorePerRule;
        this.timeTable = timeTable;
    }

    public List<QuintetDTO> getSolutionQuintets() {
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

    public double getHardRulesAvg() {
        double hardRuleSum = 0;
        int numOfHardRules = 0;

        for (Map.Entry<RuleDTO, Double> entry : fitnessScorePerRule.entrySet()) {
            if (entry.getKey().isHardRule()) {
                numOfHardRules++;
                hardRuleSum += entry.getValue();
            }
        }

        double hardRuleAvg = 0;

        if (numOfHardRules != 0) {
            hardRuleAvg = hardRuleSum / numOfHardRules;
        }

        return hardRuleAvg;
    }

    public double getSoftRulesAvg() {
        double softRuleSum = 0;
        int numOfSoftRules = 0;

        for (Map.Entry<RuleDTO, Double> entry : fitnessScorePerRule.entrySet()) {
            if (!entry.getKey().isHardRule()) {
                numOfSoftRules++;
                softRuleSum += entry.getValue();
            }
        }

        double softRuleAvg = 0;

        if (numOfSoftRules != 0) {
            softRuleAvg = softRuleSum / numOfSoftRules;
        }

        return softRuleAvg;
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
