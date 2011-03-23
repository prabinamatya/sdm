package com.trifork.stamdata.replication.replication.views.dkma;

import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.*;
import com.trifork.stamdata.replication.replication.views.View;
import com.trifork.stamdata.replication.util.Namespace;


@Entity(name = "dkma/dosisdispensering/v1")
@Table(name = "OplysningerOmDosisdispensering")
@XmlRootElement(namespace = Namespace.STAMDATA_3_0)
@XmlAccessorType(XmlAccessType.FIELD)
public class OplysningerOmDosisdispensering extends View {

	@Id
	@GeneratedValue
	@Column(name = "OplysningerOmDosisdispenseringPID")
	@XmlTransient
	private BigInteger recordID;

	@Column(name = "Varenummer")
	protected BigInteger id; // TODO: BigInt Why?

	@Column(name = "DrugID")
	protected BigInteger drugId; // TODO: BigInt Why?

	@Column(name = "LaegemidletsSubstitutionsgruppe")
	protected String substitutionsgruppe;

	@Column(name = "MindsteAIPPrEnhed")
	protected BigInteger mindsteAIPPrEnhed;

	@Column(name = "MindsteRegisterprisEnh")
	protected BigInteger mindsteRegisterprisEnh; // TODO: Bad name

	@Column(name = "TSPPrEnhed")
	protected BigInteger tspPrEnhed; // TODO: Bad name

	@Column(name = "BilligsteDrugid")
	protected BigInteger billigsteDrugid;

	@Column(name = "KodeForBilligsteDrugid")
	// TODO: Why VARCHAR(1)?
	protected String KodeForBilligsteDrugid;

	// Metadata

	@XmlTransient
	@Column(name = "ModifiedDate")
	private Date modifiedDate;

	@Column(name = "ValidFrom")
	@Temporal(TemporalType.TIMESTAMP)
	protected Date validFrom;

	@Column(name = "ValidTo")
	@Temporal(TemporalType.TIMESTAMP)
	protected Date validTo;

	@Override
	public String getId() {

		return id.toString();
	}

	@Override
	public Date getUpdated() {

		return modifiedDate;
	}

	@Override
	public BigInteger getRecordID() {

		return recordID;
	}
}