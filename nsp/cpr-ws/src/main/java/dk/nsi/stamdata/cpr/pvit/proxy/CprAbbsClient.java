package dk.nsi.stamdata.cpr.pvit.proxy;

import dk.nsi.stamdata.cpr.jaxws.SealNamespaceResolver;
import dk.nsi.stamdata.cpr.ws.*;
import org.joda.time.DateTime;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.ws.Holder;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class CprAbbsClient {
	public static final String ENDPOINT_PROPERTY_NAME = "cprabbs.service.endpoint.url";
	private final URL wsdlLocation;
	private static final QName SERVICE_QNAME = new QName("http://nsi.dk/cprabbs/2011/10", "CprAbbsFacadeService");

	@Inject
	public CprAbbsClient(@Named(ENDPOINT_PROPERTY_NAME) String cprabbsServiceUrl) throws MalformedURLException {
		this.wsdlLocation = new URL(cprabbsServiceUrl + "?wsdl");
	}

	public List<String> getChangedCprs(Security wsseHeader, Header medcomHeader, DateTime since) throws CprAbbsException {
		CprAbbsFacadeService serviceCatalog = new CprAbbsFacadeService(wsdlLocation, SERVICE_QNAME);

		serviceCatalog.setHandlerResolver(new SealNamespaceResolver());

		CprAbbsFacade client = serviceCatalog.getCprAbbsSoapBinding();

		Holder<Security> securityHolder = new Holder<Security>(wsseHeader);
		Holder<Header> medcomHolder = new Holder<Header>(medcomHeader);

		CprAbbsRequest request = new CprAbbsRequest();

		if (since != null) {
			XMLGregorianCalendar sinceAsXml = newXMLGregorianCalendar(since);
			request.setSince(sinceAsXml);
		}

		CprAbbsResponse response;
		try {
			response = client.getChangedCprs(securityHolder, medcomHolder, request);
		} catch (DGWSFault dgwsFault) {
			throw new CprAbbsException(dgwsFault);
		}

		return response.getChangedCprs();
	}

	private XMLGregorianCalendar newXMLGregorianCalendar(DateTime since) {
		DatatypeFactory factory = null;
		try {
			factory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException(e);
		}
		return factory.newXMLGregorianCalendar(since.toGregorianCalendar());
	}

}
