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
import oio.sagdok._2_0.TilstandVirkningType;


/**
 * <p>Java class for CivilStatusType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CivilStatusType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oio:sagdok:person:1.0.0}CivilStatusKode"/>
 *         &lt;element ref="{urn:oio:sagdok:2.0.0}TilstandVirkning" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CivilStatusType", propOrder = {
    "civilStatusKode",
    "tilstandVirkning"
})
public class CivilStatusType {

    @XmlElement(name = "CivilStatusKode", required = true)
    protected CivilStatusKodeType civilStatusKode;
    @XmlElement(name = "TilstandVirkning", namespace = "urn:oio:sagdok:2.0.0")
    protected TilstandVirkningType tilstandVirkning;

    /**
     * Gets the value of the civilStatusKode property.
     * 
     * @return
     *     possible object is
     *     {@link CivilStatusKodeType }
     *     
     */
    public CivilStatusKodeType getCivilStatusKode() {
        return civilStatusKode;
    }

    /**
     * Sets the value of the civilStatusKode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CivilStatusKodeType }
     *     
     */
    public void setCivilStatusKode(CivilStatusKodeType value) {
        this.civilStatusKode = value;
    }

    /**
     * Gets the value of the tilstandVirkning property.
     * 
     * @return
     *     possible object is
     *     {@link TilstandVirkningType }
     *     
     */
    public TilstandVirkningType getTilstandVirkning() {
        return tilstandVirkning;
    }

    /**
     * Sets the value of the tilstandVirkning property.
     * 
     * @param value
     *     allowed object is
     *     {@link TilstandVirkningType }
     *     
     */
    public void setTilstandVirkning(TilstandVirkningType value) {
        this.tilstandVirkning = value;
    }

}