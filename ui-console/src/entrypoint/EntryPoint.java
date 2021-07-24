package entrypoint;

import il.ac.mta.zuli.evolution.engine.data.Teacher;
import il.ac.mta.zuli.evolution.engine.xmlparser.XMLParser;

public class EntryPoint {
    public static void main(String[] args) {
        XMLParser xmlParser = new XMLParser();
        xmlParser.marshall();
        Teacher teacher = xmlParser.createTeacher();
        System.out.println(teacher);
    }
}
