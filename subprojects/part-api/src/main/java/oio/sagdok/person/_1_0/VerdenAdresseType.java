//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.3u2-hudson-jaxb-ri-2.2.3-4- 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.05.25 at 09:38:22 AM CEST 
//


package oio.sagdok.person._1_0;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import dk.oio.rep.cpr_dk.xml.schemas._2008._05._01.ForeignAddressStructureType;


/**
 * <p>Java class for VerdenAdresseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VerdenAdresseType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:oio:sagdok:person:1.0.0}AdresseBaseType">
 *       &lt;sequence>
 *         &lt;element ref="{http://rep.oio.dk/cpr.dk/xml/schemas/2008/05/01/}ForeignAddressStructure"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VerdenAdresseType", propOrder = {
    "foreignAddressStructure"
})
public class VerdenAdresseType
    extends AdresseBaseType
{

    @XmlElement(name = "ForeignAddressStructure", namespace = "http://rep.oio.dk/cpr.dk/xml/schemas/2008/05/01/", required = true)
    protected ForeignAddressStructureType foreignAddressStructure;

    /**
     * Gets the value of the foreignAddressStructure property.
     * 
     * @return
     *     possible object is
     *     {@link ForeignAddressStructureType }
     *     
     */
    public ForeignAddressStructureType getForeignAddressStructure() {
        return foreignAddressStructure;
    }

    /**
     * Sets the value of the foreignAddressStructure property.
     * 
     * @param value
     *     allowed object is
     *     {@link ForeignAddressStructureType }
     *     
     */
    public void setForeignAddressStructure(ForeignAddressStructureType value) {
        this.foreignAddressStructure = value;
    }

}