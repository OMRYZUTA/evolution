//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.08.08 at 10:02:00 PM IDT 
//


package il.ac.mta.zuli.evolution.engine.xmlparser.generated.ex2;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="subject-id" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "ETT-Teaches")
public class ETTTeaches {

    @XmlAttribute(name = "subject-id", required = true)
    protected int subjectId;

    /**
     * Gets the value of the subjectId property.
     * 
     */
    public int getSubjectId() {
        return subjectId;
    }

    /**
     * Sets the value of the subjectId property.
     * 
     */
    public void setSubjectId(int value) {
        this.subjectId = value;
    }

}