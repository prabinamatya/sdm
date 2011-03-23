package com.trifork.stamdata.replication.gui;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.servlet.RequestScoped;
import com.trifork.rid2cpr.CachingRID2CPRFacadeImpl;
import com.trifork.rid2cpr.RID2CPRFacade;
import com.trifork.stamdata.replication.gui.annotations.Whitelist;
import com.trifork.stamdata.replication.gui.controllers.LogController;
import com.trifork.stamdata.replication.gui.controllers.ClientController;
import com.trifork.stamdata.replication.gui.controllers.UserController;
import com.trifork.stamdata.replication.gui.models.User;
import com.trifork.stamdata.replication.gui.security.LoginFilter;
import com.trifork.stamdata.replication.util.ConfiguredModule;
import com.trifork.xmlquery.Namespaces;
import dk.itst.oiosaml.sp.service.SPFilter;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;


public class GuiModule extends ConfiguredModule {

	@Override
	protected void configureServlets() {

		// HTML TEMPLATE ENGINE
		//
		// We use Freemaker for HTML templates.
		// The template files can be found in the 'webapp' directory
		// and all have the extension .ftl.

		Configuration config = new Configuration();

		String TEMPLATE_DIR = "views";
		URL TEMPLATE_DIR_URL = getClass().getClassLoader().getResource(TEMPLATE_DIR);

		try {
			File templateDir = new File(TEMPLATE_DIR_URL.toURI());

			// Specify the data source where the template files come from.
			// Here I set a file directory for it:

			config.setDirectoryForTemplateLoading(templateDir);

			// Specify how templates will see the data-model.
			// We just use the default:

			config.setObjectWrapper(new DefaultObjectWrapper());

			bind(Configuration.class).toInstance(config);
		}
		catch (Exception e) {
			addError("Invalid template directory.", e);
		}

		// WHITELIST CVR NUMBERS
		//
		// These CVR numbers that can be used when creating new administrators.
		// The list in maintained in the config.properties file.

		String[] cvrNumbers = getConfig().getStringArray("whitelist");
		String[] orgNames = getConfig().getStringArray("whitelistNames");

		Map<String, String> whitelist = new HashMap<String, String>(cvrNumbers.length);

		for (int i = 0; i < cvrNumbers.length; i++) {
			whitelist.put(orgNames[i], cvrNumbers[i]);
		}

		if (whitelist.size() > 0) {
			bind(new TypeLiteral<Map<String, String>>() {}).annotatedWith(Whitelist.class).toInstance(whitelist);
		}
		else {
			addError("No cvr-numbers have been white-listed. Change the configuration file.");
		}

		// FILTER ACCESS THROUGH SAML
		//
		// The LoginFilter requires that trafic has passed through
		// the SPFilter first.

		// TODO: bind(SPFilter.class).in(Singleton.class);
		// TODO: filter("/admin", "/admin/*").through(SPFilter.class);
		filter("/admin", "/admin/*").through(LoginFilter.class);
		bind(User.class).toProvider(LoginFilter.class).in(RequestScoped.class);

		// SERVE THE ADMIN GUI

		serve("/admin/users", "/admin/users/*").with(UserController.class);
		serve("/admin/log", "/admin/log/*").with(LogController.class);
		serve("/admin", "/admin/clients", "/admin/clients/*").with(ClientController.class);
	}

	@Provides
	@Singleton
	public RID2CPRFacade provideRIDHelper() {

		// Bind the RID 2 CPR helper to get the users' CPR
		// from a remote service.

		CachingRID2CPRFacadeImpl ridService = new CachingRID2CPRFacadeImpl();
		ridService.setEndpoint(getConfig().getString("rid2cpr.endpoint"));

		String keystore = getConfig().getString("rid2cpr.keystore");
		keystore = getClass().getClassLoader().getResource(keystore).toExternalForm();

		ridService.setKeystore(keystore);
		ridService.setKeystorePassword(getConfig().getString("rid2cpr.keystorePassword"));
		ridService.setReadTimeout(getConfig().getInt("rid2cpr.callTimeout"));

		// TODO: Setting the default namespaces is deprecated.
		// We should set our own. (The ones that we actually need.)

		ridService.setNamespaces(Namespaces.getOIONamespaces());
		ridService.init();

		return ridService;
	}
}