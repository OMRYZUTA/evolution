package il.ac.mta.zuli.evolution.ui;

import il.ac.mta.zuli.evolution.engine.data.Descriptor;
import il.ac.mta.zuli.evolution.engine.xmlparser.XMLParser;

public class Program {
    public static void main(String[] args) {
        XMLParser xmlParser = new XMLParser();
        Descriptor d = xmlParser.unmarshall("engine/src/resources/EX1-small.xml");
        System.out.println(d);
    }
}
