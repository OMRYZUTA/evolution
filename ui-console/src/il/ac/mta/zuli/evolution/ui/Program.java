package il.ac.mta.zuli.evolution.ui;

import il.ac.mta.zuli.evolution.engine.data.Descriptor;
import il.ac.mta.zuli.evolution.engine.xmlparser.XMLParser;

public class Program {
    public static void main(String[] args) {
        XMLParser xmlParser = new XMLParser();
        Descriptor d = xmlParser.unmarshall();
        System.out.println(d);

//        Teacher teacher = xmlParser.createTeacher();
//        System.out.println(teacher);
    }
}
