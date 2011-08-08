// The contents of this file are subject to the Mozilla Public
// License Version 1.1 (the "License"); you may not use this file
// except in compliance with the License. You may obtain a copy of
// the License at http://www.mozilla.org/MPL/
//
// Software distributed under the License is distributed on an "AS
// IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
// implied. See the License for the specific language governing
// rights and limitations under the License.
//
// Contributor(s): Contributors are attributed in the source code
// where applicable.
//
// The Original Code is "Stamdata".
//
// The Initial Developer of the Original Code is Trifork Public A/S.
//
// Portions created for the Original Code are Copyright 2011,
// Lægemiddelstyrelsen. All Rights Reserved.
//
// Portions created for the FMKi Project are Copyright 2011,
// National Board of e-Health (NSI). All Rights Reserved.

package com.trifork.stamdata.importer.jobs.cpr;

import static com.trifork.stamdata.importer.util.DateUtils.yyyy_MM_dd;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.trifork.stamdata.importer.config.MySQLConnectionManager;
import com.trifork.stamdata.importer.persistence.AuditingPersister;


public class CPRIntegrationTest
{
	private Connection con;

	@Before
	public void cleanDatabase() throws Exception
	{
		con = MySQLConnectionManager.getConnection();

		Statement statement = con.createStatement();
		statement.execute("truncate table Person");
		statement.execute("truncate table BarnRelation");
		statement.execute("truncate table ForaeldreMyndighedRelation");
		statement.execute("truncate table UmyndiggoerelseVaergeRelation");
		statement.execute("truncate table PersonIkraft");
		statement.execute("truncate table Udrejseoplysninger");
		statement.execute("truncate table Statsborgerskab");
		statement.execute("truncate table Foedselsregistreringsoplysninger");
		statement.execute("truncate table KommunaleForhold");
		statement.execute("truncate table AktuelCivilstand");
	}

	@After
	public void tearDown() throws Exception
	{
		con.rollback();
		con.close();
	}

	@Test
	public void canEstablishData() throws Exception
	{
		importFile("data/cpr/testEtablering/D100313.L431102");

		// When running a full load (file doesn't ends on 01) of CPR no
		// LatestIkraft should be written to the db.

		Date latestIkraft = CPRImporter.getLatestVersion(con);
		assertNull(latestIkraft);
	}

	@Test
	public void canImportAnUpdate() throws Exception
	{
		importFile("data/cpr/D100315.L431101");

		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT Fornavn, validFrom, validTo from Person WHERE cpr='1312095098'");
		assertTrue(rs.next());
		assertEquals("Hjalte", rs.getString("Fornavn"));
		assertEquals("2010-03-15 00:00:00.0", rs.getString("validFrom"));
		assertEquals("2999-12-31 00:00:00.0", rs.getString("validTo"));
		assertFalse(rs.next());

		importFile("data/cpr/D100317.L431101");

		rs = stmt.executeQuery("SELECT Fornavn, validFrom, validTo from Person WHERE cpr='1312095098' ORDER BY validFrom");
		assertTrue(rs.next());
		assertEquals("Hjalte", rs.getString("Fornavn"));
		assertEquals("2010-03-15 00:00:00.0", rs.getString("validFrom"));
		assertEquals("2010-03-17 00:00:00.0", rs.getString("validTo"));
		assertTrue(rs.next());
		assertEquals("Hjalts", rs.getString("Fornavn"));
		assertEquals("2010-03-17 00:00:00.0", rs.getString("validFrom"));
		assertEquals("2999-12-31 00:00:00.0", rs.getString("validTo"));
		assertFalse(rs.next());
	}

	@Test(expected = Exception.class)
	public void failsWhenDatesAreNotInSequence() throws Exception
	{
		importFile("data/cpr/testSequence1/D100314.L431101");

		Date latestIkraft = CPRImporter.getLatestVersion(con);
		assertEquals(yyyy_MM_dd.parse("2001-11-16"), latestIkraft);

		importFile("data/cpr/testSequence2/D100314.L431101");

		latestIkraft = CPRImporter.getLatestVersion(con);
		assertEquals(yyyy_MM_dd.parse("2001-11-19"), latestIkraft);

		importFile("data/cpr/testOutOfSequence/D100314.L431101");
	}

	@Test(expected = Exception.class)
	public void failsWhenEndRecordAppearsBeforeEndOfFile() throws Exception
	{
		importFile("data/cpr/endRecords/D100314.L431101");
	}

	@Test(expected = Exception.class)
	public void failsWhenNoEndRecordExists() throws Exception
	{
		importFile("data/cpr/endRecords/D100315.L431101");
	}

	@Test
	public void canImportPersonNavnebeskyttelse() throws Exception
	{
		importFile("data/cpr/testCPR1/D100314.L431101");

		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM Person WHERE CPR = '0101965058'");
		rs.next();

		assertEquals("K", rs.getString("Koen"));
		assertEquals("Ude Ulrike", rs.getString("Fornavn"));
		assertEquals("", rs.getString("Mellemnavn"));
		assertEquals("Udtzen", rs.getString("Efternavn"));
		assertEquals("", rs.getString("CoNavn"));
		assertEquals("", rs.getString("Lokalitet"));
		assertEquals("Søgade", rs.getString("Vejnavn"));
		assertEquals("", rs.getString("Bygningsnummer"));
		assertEquals("16", rs.getString("Husnummer"));
		assertEquals("1", rs.getString("Etage"));
		assertEquals("", rs.getString("SideDoerNummer"));
		assertEquals("Vodskov", rs.getString("Bynavn"));
		assertEquals("9000", rs.getString("Postnummer"));
		assertEquals("Aalborg", rs.getString("PostDistrikt"));
		assertEquals("01", rs.getString("Status"));
		assertEquals(yyyy_MM_dd.parse("1997-09-09"), rs.getDate("NavneBeskyttelseStartDato"));
		assertEquals(yyyy_MM_dd.parse("2001-02-20"), rs.getDate("NavneBeskyttelseSletteDato"));
		assertEquals("", rs.getString("GaeldendeCPR"));
		assertEquals(yyyy_MM_dd.parse("1896-01-01"), rs.getDate("Foedselsdato"));
		assertEquals("Pensionist", rs.getString("Stilling"));
		assertEquals("8511", rs.getString("VejKode"));
		assertEquals("851", rs.getString("KommuneKode"));
		assertEquals(yyyy_MM_dd.parse("2001-11-16"), rs.getDate("ValidFrom"));
		assertEquals(yyyy_MM_dd.parse("2999-12-31"), rs.getDate("ValidTo"));
		assertTrue(rs.last());
	}

	@Test
	public void canImportForaeldreMyndighedBarn() throws Exception
	{
		importFile("data/cpr/testForaeldremyndighed/D100314.L431101");

		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("Select * from Person where CPR='3112970028'");

		rs.next();

		assertTrue(rs.last());

		rs = stmt.executeQuery("SELECT * FROM BarnRelation WHERE BarnCPR='3112970028'");

		rs.next();

		assertEquals("0702614082", rs.getString("CPR"));
		assertTrue(rs.last());

		rs = stmt.executeQuery("SELECT * FROM ForaeldreMyndighedRelation WHERE CPR='3112970028' ORDER BY TypeKode");

		rs.next();

		assertEquals("0003", rs.getString("TypeKode"));
		assertEquals("Mor", rs.getString("TypeTekst"));
		assertEquals("", rs.getString("RelationCpr"));
		assertEquals(yyyy_MM_dd.parse("2008-01-01"), rs.getDate("ValidFrom"));
		assertEquals(yyyy_MM_dd.parse("2999-12-31"), rs.getDate("ValidTo"));

		rs.next();

		assertEquals("0004", rs.getString("TypeKode"));
		assertEquals("Far", rs.getString("TypeTekst"));
		assertEquals("", rs.getString("RelationCpr"));
		assertEquals(yyyy_MM_dd.parse("2008-01-01"), rs.getDate("ValidFrom"));
		assertEquals(yyyy_MM_dd.parse("2999-12-31"), rs.getDate("ValidTo"));

		assertTrue(rs.last());
	}

	@Test
	public void canImportUmyndighedVaerge() throws Exception
	{
		importFile("data/cpr/testUmyndigVaerge/D100314.L431101");

		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM UmyndiggoerelseVaergeRelation WHERE CPR='0709614126'");

		rs.next();

		assertEquals("0001", rs.getString("TypeKode"));
		assertEquals("Værges CPR findes", rs.getString("TypeTekst"));
		assertEquals("0904414131", rs.getString("RelationCpr"));
		assertEquals(yyyy_MM_dd.parse("2000-02-28"), rs.getDate("RelationCprStartDato"));
		assertEquals("", rs.getString("VaergesNavn"));
		assertEquals(null, rs.getDate("VaergesNavnStartDato"));
		assertEquals("", rs.getString("RelationsTekst1"));
		assertEquals("", rs.getString("RelationsTekst2"));
		assertEquals("", rs.getString("RelationsTekst3"));
		assertEquals("", rs.getString("RelationsTekst4"));
		assertEquals("", rs.getString("RelationsTekst5"));
		assertEquals(yyyy_MM_dd.parse("2001-11-19"), rs.getDate("ValidFrom"));
		assertEquals(yyyy_MM_dd.parse("2999-12-31"), rs.getDate("ValidTo"));
		assertTrue(rs.last());
	}

	@Test
	public void canImportFolkekirkeoplysninger() throws Exception
	{
		importFile("data/cpr/folkekirkeoplysninger/D100314.L431101");

		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM Folkekirkeoplysninger WHERE CPR='0709614126'");

		rs.next();

		assertEquals("0709614126", rs.getString("CPR"));
		assertEquals("F", rs.getString("Forholdskode"));
		assertEquals(yyyy_MM_dd.parse("1961-09-07"), rs.getDate("validFrom"));
		assertTrue(rs.last());
	}

	@Test
	public void canImportCivilstand() throws Exception
	{
		importFile("data/cpr/civilstand/D100314.L431101");

		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM AktuelCivilstand WHERE CPR='0905414143'");
		rs.next();
		assertEquals("0905414143", rs.getString("CPR"));
		assertEquals("E", rs.getString("Civilstandskode"));
		assertEquals("0901414084", rs.getString("Aegtefaellepersonnummer"));
		assertNull(rs.getDate("Aegtefaellefoedselsdato"));
		assertEquals("", rs.getString("Aegtefaellenavn"));
		assertEquals(yyyy_MM_dd.parse("1961-03-13"), rs.getDate("validFrom"));
		assertNull(rs.getDate("Separation"));
		assertTrue(rs.last());
	}

	@Test
	public void canImportKommunaleForhold() throws Exception
	{
		importFile("data/cpr/kommunaleForhold/D100314.L431101");

		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("Select * from KommunaleForhold where CPR='2802363039'");

		rs.next();

		assertEquals("2802363039", rs.getString("CPR"));
		assertEquals("3", rs.getString("Kommunalforholdstypekode"));
		assertEquals("N", rs.getString("Kommunalforholdskode"));
		assertEquals(yyyy_MM_dd.parse("2000-06-30"), rs.getDate("validFrom"));
		assertEquals("Tekst til komforh3/pension", rs.getString("Bemaerkninger"));
		assertTrue(rs.last());
	}

	@Test
	public void canImportValgoplysninger() throws Exception
	{
		importFile("data/cpr/valgoplysninger/D100314.L431101");

		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("Select * from Valgoplysninger where CPR='0708614335'");

		rs.next();

		assertEquals("0708614335", rs.getString("CPR"));
		assertEquals("1", rs.getString("Valgkode"));
		assertEquals(yyyy_MM_dd.parse("1999-03-10"), rs.getDate("Valgretsdato"));
		assertEquals(yyyy_MM_dd.parse("1999-02-01"), rs.getDate("validFrom"));
		assertEquals(yyyy_MM_dd.parse("2001-03-10"), rs.getDate("validTo"));
		assertTrue(rs.last());
	}

	@Test
	public void canImportHaendelser() throws Exception
	{
		importFile("data/cpr/haendelse/D100314.L431101");

		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM Haendelse WHERE CPR = '0905414143'");

		rs.next();

		assertEquals("0905414143", rs.getString("CPR"));
		assertEquals(yyyy_MM_dd.parse("2001-11-15"), rs.getDate("Ajourfoeringsdato"));
		assertEquals("P10", rs.getString("Haendelseskode"));
		assertEquals("", rs.getString("AfledtMarkering"));
		assertEquals("", rs.getString("Noeglekonstant"));
		assertTrue(rs.last());
	}

	@Test
	public void ImportU12160Test() throws Exception
	{
		importFile("data/cpr/D100312.L431101");

		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("Select COUNT(*) from Person");
		rs.next();
		assertEquals(100, rs.getInt(1));

		rs = stmt.executeQuery("Select COUNT(*) from BarnRelation");
		rs.next();
		assertEquals(30, rs.getInt(1));

		rs = stmt.executeQuery("Select COUNT(*) from ForaeldreMyndighedRelation");
		rs.next();
		assertEquals(4, rs.getInt(1));

		rs = stmt.executeQuery("Select COUNT(*) from UmyndiggoerelseVaergeRelation");
		rs.next();
		assertEquals(1, rs.getInt(1));

		// Check Address protection

		rs = stmt.executeQuery("SELECT COUNT(*) FROM Person WHERE NavneBeskyttelseStartDato < NOW() AND NavneBeskyttelseSletteDato > NOW()");
		rs.next();
		assertEquals(1, rs.getInt(1));
	}

	@Test
	public void ImportU12170Test() throws Exception
	{
		importFile("data/cpr/D100313.L431101");

		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("Select COUNT(*) from Person");
		rs.next();
		assertEquals(80, rs.getInt(1));

		rs = stmt.executeQuery("SELECT COUNT(*) FROM BarnRelation");
		rs.next();
		assertEquals(29, rs.getInt(1));

		rs = stmt.executeQuery("SELECT COUNT(*) FROM ForaeldreMyndighedRelation");
		rs.next();
		assertEquals(5, rs.getInt(1));

		rs = stmt.executeQuery("SELECT COUNT(*) FROM UmyndiggoerelseVaergeRelation");
		rs.next();
		assertEquals(2, rs.getInt(1));

		// Check Address protection
		rs = stmt.executeQuery("SELECT COUNT(*) FROM Person WHERE NavneBeskyttelseStartDato < NOW() AND NavneBeskyttelseSletteDato > NOW()");
		rs.next();
		assertEquals(1, rs.getInt(1));
	}

	private void importFile(String fileName) throws Exception
	{
		File file = FileUtils.toFile(getClass().getClassLoader().getResource(fileName));
		new CPRImporter().importFiles(new File[] {file}, new AuditingPersister(con));
	}
}
