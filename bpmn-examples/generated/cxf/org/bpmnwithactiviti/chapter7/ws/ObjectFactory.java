
package org.bpmnwithactiviti.chapter7.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.bpmnwithactiviti.chapter7.ws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _FindCustomer_QNAME = new QName("http://ws.chapter7.bpmnwithactiviti.org/", "findCustomer");
    private final static QName _FindCustomerById_QNAME = new QName("http://ws.chapter7.bpmnwithactiviti.org/", "findCustomerById");
    private final static QName _FindCustomerResponse_QNAME = new QName("http://ws.chapter7.bpmnwithactiviti.org/", "findCustomerResponse");
    private final static QName _FindCustomerByIdResponse_QNAME = new QName("http://ws.chapter7.bpmnwithactiviti.org/", "findCustomerByIdResponse");
    private final static QName _StoreSalesOpportunity_QNAME = new QName("http://ws.chapter7.bpmnwithactiviti.org/", "storeSalesOpportunity");
    private final static QName _StoreSalesOpportunityResponse_QNAME = new QName("http://ws.chapter7.bpmnwithactiviti.org/", "storeSalesOpportunityResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.bpmnwithactiviti.chapter7.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link StoreSalesOpportunity }
     * 
     */
    public StoreSalesOpportunity createStoreSalesOpportunity() {
        return new StoreSalesOpportunity();
    }

    /**
     * Create an instance of {@link FindCustomerResponse }
     * 
     */
    public FindCustomerResponse createFindCustomerResponse() {
        return new FindCustomerResponse();
    }

    /**
     * Create an instance of {@link FindCustomer }
     * 
     */
    public FindCustomer createFindCustomer() {
        return new FindCustomer();
    }

    /**
     * Create an instance of {@link SalesOpportunity }
     * 
     */
    public SalesOpportunity createSalesOpportunity() {
        return new SalesOpportunity();
    }

    /**
     * Create an instance of {@link StoreSalesOpportunityResponse }
     * 
     */
    public StoreSalesOpportunityResponse createStoreSalesOpportunityResponse() {
        return new StoreSalesOpportunityResponse();
    }

    /**
     * Create an instance of {@link FindCustomerByIdResponse }
     * 
     */
    public FindCustomerByIdResponse createFindCustomerByIdResponse() {
        return new FindCustomerByIdResponse();
    }

    /**
     * Create an instance of {@link Customer }
     * 
     */
    public Customer createCustomer() {
        return new Customer();
    }

    /**
     * Create an instance of {@link FindCustomerById }
     * 
     */
    public FindCustomerById createFindCustomerById() {
        return new FindCustomerById();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindCustomer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.chapter7.bpmnwithactiviti.org/", name = "findCustomer")
    public JAXBElement<FindCustomer> createFindCustomer(FindCustomer value) {
        return new JAXBElement<FindCustomer>(_FindCustomer_QNAME, FindCustomer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindCustomerById }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.chapter7.bpmnwithactiviti.org/", name = "findCustomerById")
    public JAXBElement<FindCustomerById> createFindCustomerById(FindCustomerById value) {
        return new JAXBElement<FindCustomerById>(_FindCustomerById_QNAME, FindCustomerById.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindCustomerResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.chapter7.bpmnwithactiviti.org/", name = "findCustomerResponse")
    public JAXBElement<FindCustomerResponse> createFindCustomerResponse(FindCustomerResponse value) {
        return new JAXBElement<FindCustomerResponse>(_FindCustomerResponse_QNAME, FindCustomerResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindCustomerByIdResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.chapter7.bpmnwithactiviti.org/", name = "findCustomerByIdResponse")
    public JAXBElement<FindCustomerByIdResponse> createFindCustomerByIdResponse(FindCustomerByIdResponse value) {
        return new JAXBElement<FindCustomerByIdResponse>(_FindCustomerByIdResponse_QNAME, FindCustomerByIdResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StoreSalesOpportunity }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.chapter7.bpmnwithactiviti.org/", name = "storeSalesOpportunity")
    public JAXBElement<StoreSalesOpportunity> createStoreSalesOpportunity(StoreSalesOpportunity value) {
        return new JAXBElement<StoreSalesOpportunity>(_StoreSalesOpportunity_QNAME, StoreSalesOpportunity.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StoreSalesOpportunityResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.chapter7.bpmnwithactiviti.org/", name = "storeSalesOpportunityResponse")
    public JAXBElement<StoreSalesOpportunityResponse> createStoreSalesOpportunityResponse(StoreSalesOpportunityResponse value) {
        return new JAXBElement<StoreSalesOpportunityResponse>(_StoreSalesOpportunityResponse_QNAME, StoreSalesOpportunityResponse.class, null, value);
    }

}
