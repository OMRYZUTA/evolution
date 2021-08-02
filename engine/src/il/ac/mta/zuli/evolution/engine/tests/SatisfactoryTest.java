package il.ac.mta.zuli.evolution.engine.tests;

import il.ac.mta.zuli.evolution.engine.Descriptor;
import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.rules.Satisfactory;
import il.ac.mta.zuli.evolution.engine.xmlparser.XMLParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SatisfactoryTest {
    Satisfactory satisfactoryRule;
    XMLParser xmlParser = new XMLParser();
    Descriptor descriptor;
    @BeforeEach
    void setUp() {
        try {
             descriptor = xmlParser.unmarshall("src/resources/EX1-small.xml");
            satisfactoryRule = new Satisfactory("soft", descriptor.getTimeTable().getSchoolClasses());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Test
    void fitnessEvaluation() {
        TimeTableSolution timeTableSolution = new TimeTableSolution(descriptor.getTimeTable());
        satisfactoryRule.fitnessEvaluation(timeTableSolution);

    }
}