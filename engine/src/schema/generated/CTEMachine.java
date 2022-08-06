//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.08.06 at 02:07:36 PM IDT 
//


package schema.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ABC" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element ref="{}CTE-Rotors"/>
 *         &lt;element ref="{}CTE-Reflectors"/>
 *       &lt;/sequence>
 *       &lt;attribute name="rotors-count" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "abc",
    "cteRotors",
    "cteReflectors"
})
@XmlRootElement(name = "CTE-Machine")
public class CTEMachine {

    @XmlElement(name = "ABC", required = true)
    protected String abc;
    @XmlElement(name = "CTE-Rotors", required = true)
    protected CTERotors cteRotors;
    @XmlElement(name = "CTE-Reflectors", required = true)
    protected CTEReflectors cteReflectors;
    @XmlAttribute(name = "rotors-count", required = true)
    protected int rotorsCount;

    /**
     * Gets the value of the abc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getABC() {
        return abc;
    }

    /**
     * Sets the value of the abc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setABC(String value) {
        this.abc = value;
    }

    /**
     * Gets the value of the cteRotors property.
     * 
     * @return
     *     possible object is
     *     {@link CTERotors }
     *     
     */
    public CTERotors getCTERotors() {
        return cteRotors;
    }

    /**
     * Sets the value of the cteRotors property.
     * 
     * @param value
     *     allowed object is
     *     {@link CTERotors }
     *     
     */
    public void setCTERotors(CTERotors value) {
        this.cteRotors = value;
    }

    /**
     * Gets the value of the cteReflectors property.
     * 
     * @return
     *     possible object is
     *     {@link CTEReflectors }
     *     
     */
    public CTEReflectors getCTEReflectors() {
        return cteReflectors;
    }

    /**
     * Sets the value of the cteReflectors property.
     * 
     * @param value
     *     allowed object is
     *     {@link CTEReflectors }
     *     
     */
    public void setCTEReflectors(CTEReflectors value) {
        this.cteReflectors = value;
    }

    /**
     * Gets the value of the rotorsCount property.
     * 
     */
    public int getRotorsCount() {
        return rotorsCount;
    }

    /**
     * Sets the value of the rotorsCount property.
     * 
     */
    public void setRotorsCount(int value) {
        this.rotorsCount = value;
    }

}