package com.trifork.sdm.replication.replication.models.dkma;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.*;
import javax.xml.bind.annotation.*;

import com.trifork.sdm.replication.replication.models.Record;
import com.trifork.sdm.replication.util.Namespace;

@Entity(name = "dkma/indholdsstoffer/v1")
@Table(name = "Indikation")
@XmlRootElement(namespace = Namespace.STAMDATA_3_0)
@XmlAccessorType(XmlAccessType.FIELD)
public class Indholdsstoffer extends Record
{	
	@Id
	@GeneratedValue
	@Column(name = "IndholdsstofferPID")
	@XmlTransient
	private BigInteger recordID;

	@Column(name = "CID")
	private String id;

	@Column(name = "DrugID")
	protected BigInteger drugId;

	@Column(name = "Varenummer")
	protected BigInteger varenummer;
	
	@Column(name = "Stofklasse")
	protected String stofklasse;
	
	@Column(name = "Substans")
	protected String substans;
	
	@Column(name = "Substansgruppe")
	protected String substansgruppe;
	
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
	public String getID()
	{
		return id;
	}


	@Override
	public Date getUpdated()
	{
		return modifiedDate;
	}


	@Override
	public BigInteger getRecordID()
	{
		return recordID;
	}
}