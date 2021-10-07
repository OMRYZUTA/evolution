package il.ac.mta.zuli.evolution.dto;

import il.ac.mta.zuli.evolution.engine.Quintet;
import il.ac.mta.zuli.evolution.engine.TimetableSolution;
import il.ac.mta.zuli.evolution.engine.rules.Rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimetableSolutionDTO {
    private final List<QuintetDTO> solutionQuintets;
    private final Map<RuleDTO, Double> scorePerRule;
    private final int solutionSize;
    private final double score;

    public TimetableSolutionDTO(TimetableSolution timetableSolution) {
        this.solutionSize = timetableSolution.getSolutionSize();
        this.score = timetableSolution.getFitnessScore();
        this.solutionQuintets = generateQuintetDTOs(timetableSolution.getSolutionQuintets());
        this.scorePerRule = generateRuleDTOScoreMap(timetableSolution.getFitnessScorePerRule());
    }

    private List<QuintetDTO> generateQuintetDTOs(List<Quintet> quintets) {
        List<QuintetDTO> quintetDTOList = new ArrayList<>();

        for (Quintet quintet : quintets) {
            quintetDTOList.add(new QuintetDTO(quintet));
        }

        return quintetDTOList;
    }

    private Map<RuleDTO, Double> generateRuleDTOScoreMap(Map<Rule, Double> scorePerRule) {
        Map<RuleDTO, Double> scorePerRuleDTO = new HashMap<>();

        for (Map.Entry<Rule, Double> entry : scorePerRule.entrySet()) {
            scorePerRuleDTO.put(new RuleDTO(entry.getKey()), entry.getValue());
        }

        return scorePerRuleDTO;
    }

    public List<QuintetDTO> getSolutionQuintets() {
        return solutionQuintets;
    }

    public int getSolutionSize() {
        return solutionSize;
    }


    public double getHardRulesAvg() {
        double hardRuleSum = 0;
        int numOfHardRules = 0;

        for (Map.Entry<RuleDTO, Double> entry : scorePerRule.entrySet()) {
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

        for (Map.Entry<RuleDTO, Double> entry : scorePerRule.entrySet()) {
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
        return "TimeTableSolutionDTO" +
                "solution=" + solutionQuintets +
                ", solutionSize=" + solutionSize +
                ", totalFitnessScore=" + score +
                ", fitnessScorePerRule=" + scorePerRule;
    }
}
