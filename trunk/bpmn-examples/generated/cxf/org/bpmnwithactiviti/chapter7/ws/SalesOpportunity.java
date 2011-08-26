
package org.bpmnwithactiviti.chapter7.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for salesOpportunity complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="salesOpportunity">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="expectedQuantity" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="opportunityId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="product" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "salesOpportunity", propOrder = {
    "description",
    "expectedQuantity",
    "opportunityId",
    "product"
})
public class SalesOpportunity {

    protected String description;
    protected long expectedQuantity;
    protected long opportunityId;
    protected String product;

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the expectedQuantity property.
     * 
     */
    public long getExpectedQuantity() {
        return expectedQuantity;
    }

    /**
     * Sets the value of the expectedQuantity property.
     * 
     */
    public void setExpectedQuantity(long value) {
        this.expectedQuantity = value;
    }

    /**
     * Gets the value of the opportunityId property.
     * 
     */
    public long getOpportunityId() {
        return opportunityId;
    }

    /**
     * Sets the value of the opportunityId property.
     * 
     */
    public void setOpportunityId(long value) {
        this.opportunityId = value;
    }

    /**
     * Gets the value of the product property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProduct() {
        return product;
    }

    /**
     * Sets the value of the product property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProduct(String value) {
        this.product = value;
    }

}
