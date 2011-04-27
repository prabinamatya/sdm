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

package com.trifork.stamdata.replication.db;

import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * Wraps requests in a database transaction.
 *
 * Responsibilities:
 *
 * <ol>
 * 	<li>Handle transactions.
 *  <li>Close hibernate sessions as needed.
 * </ol>
 *
 * @author Thomas Børlum (thb@trifork.com)
 */
@Singleton
public class PersistenceFilter implements Filter {

	private static final Logger logger = getLogger(PersistenceFilter.class);
	private final Provider<Session> sessions;
	private final Provider<StatelessSession> statelessSessions;

	@Inject
	PersistenceFilter(Provider<Session> sessions, Provider<StatelessSession> statelessSessions) {

		this.sessions = sessions;
		this.statelessSessions = statelessSessions;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		Transaction transaction = sessions.get().getTransaction();

		try {
			transaction.begin();
			chain.doFilter(request, response);
			transaction.commit();
		}
		catch (Exception e) {
			logger.error("An unexpected error occured.", e);
			transaction.rollback();
		}
		finally {			
			sessions.get().close();

			// TODO: This next call will actually open
			// a new session if one has not been created.
			// This does not impact performance much since
			// state-less sessions are only not opened for
			// requests to the administration GUI.

			statelessSessions.get().close();
		}
	}

	@Override
	public void destroy() {

	}
}