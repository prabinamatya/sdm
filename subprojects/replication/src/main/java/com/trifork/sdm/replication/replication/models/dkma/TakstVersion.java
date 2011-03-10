package com.trifork.sdm.replication.replication.models.dkma;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.*;
import javax.xml.bind.annotation.*;

import com.trifork.sdm.replication.replication.models.Record;
import com.trifork.sdm.replication.util.Namespace;

@Entity(name = "dkma/takstversion/v1")
@Table(name = "TakstVersion")
@XmlType(namespace = Namespace.STAMDATA_3_0)
@XmlAccessorType(XmlAccessType.FIELD)
public class TakstVersion extends Record
{
	@Id
	@GeneratedValue
	@Column(name = "TakstVersionPID")
	@XmlTransient
	private BigInteger recordID;

	@Column(name = "TakstUge")
	protected String takstUge;
	
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
	public String getID()
	{
		return takstUge.toString();
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