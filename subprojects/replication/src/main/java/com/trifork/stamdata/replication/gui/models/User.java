// Stamdata - Copyright (C) 2011 National Board of e-Health (NSI)
// 
// All source code and information supplied as part of Stamdata is
// copyright to National Board of e-Health.
// 
// The source code has been released under a dual license - meaning you can
// use either licensed version of the library with your code.
// 
// It is released under the Common Public License 1.0, a copy of which can
// be found at the link below.
// http://www.opensource.org/licenses/cpl1.0.php
// 
// It is released under the LGPL (GNU Lesser General Public License), either
// version 2.1 of the License, or (at your option) any later version. A copy
// of which can be found at the link below.
// http://www.gnu.org/copyleft/lesser.html

package com.trifork.stamdata.replication.gui.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
public class User {

	@Id
	@GeneratedValue
	private String id;

	private String name;
	private String cvr;
	private String cpr;

	protected User() {

	}

	public User(String name, String cpr, String cvr) {

		this.name = name;
		this.cpr = cpr;
		this.cvr = cvr;
	}

	public String getId() {

		return id;
	}

	public String getName() {

		return name;
	}

	public String getCvr() {

		return cvr;
	}

	public String getCpr() {

		return cpr;
	}

	@Override
	public String toString() {

		return String.format("%s (cpr=%s, cvr=%s)", name, cpr, cvr);
	}
}