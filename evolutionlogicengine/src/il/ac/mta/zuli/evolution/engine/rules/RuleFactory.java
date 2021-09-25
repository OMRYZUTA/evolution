package il.ac.mta.zuli.evolution.engine.rules;

import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;
import il.ac.mta.zuli.evolution.engine.timetable.SchoolClass;
import il.ac.mta.zuli.evolution.engine.timetable.Teacher;
import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ex3.ETTRule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class RuleFactory {
    public static Rule createRule(ETTRule rule, int days, List<Teacher> teachers, List<SchoolClass> classes) {
        Map<String, Supplier<Rule>> factory = new HashMap();
        factory.put("teacherishuman",() -> new TeacherIsHuman(rule.getType()));
        factory.put("singularity",() -> new Singularity(rule.getType()));
        return factory.get(rule.getETTRuleId().toLowerCase()).get();

//        Rule newRule; //todo refactor
//        switch (rule.getETTRuleId().toLowerCase()) {
//            case "teacherishuman":
//                newRule = new TeacherIsHuman(rule.getType());
//                break;
//            case "singularity":
//                newRule = new Singularity(rule.getType());
//                break;
//            case "knowledgeable":
//                newRule = new Knowledgeable(rule.getType());
//                break;
//            case "satisfactory":
//                newRule = new Satisfactory(rule.getType(), classes);
//                break;
//            case "sequentiality":
//                newRule = new Sequentiality(rule.getType(),
//                        fetchTotalHours(rule.getETTConfiguration()),
//                        classes);
//                break;
//            case "dayoffteacher":
//                newRule = new DayOffTeacher(rule.getType(), days, teachers);
//                break;
//            case "dayoffclass":
//                newRule = new DayOffClass(rule.getType(), days,classes);
//                break;
//            case "workinghourspreference":
//                newRule = new WorkingHoursPreference(rule.getType(), teachers);
//                break;
//            default:
//                throw new ValidationException("Invalid rule for ex.3: " + rule.getETTRuleId());
//        }
//        return newRule;
    }
    private static int fetchTotalHours(String configuration) {

        if (configuration.length() == 0) {
            throw new ValidationException("Empty configuration");
        }

        int index = configuration.indexOf('=');

        if (index == -1) {
            throw new ValidationException("Invalid configuration format, missing '=' ");
        }

        int hours;

        try {
            hours = Integer.parseInt(configuration.substring(index + 1), 10);
        } catch (Throwable e) {
            throw new ValidationException("Invalid configuration format, total hours value must be number");
        }

        return hours;
    }

}
