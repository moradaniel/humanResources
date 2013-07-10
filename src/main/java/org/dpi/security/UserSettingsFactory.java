package org.dpi.security;

import org.janux.bus.security.Account;

public interface UserSettingsFactory
{
	AbstractUserSettings getSettingsForAccount(Account account);

	void clearSettingsForAccount(Account account);

	/**
	 * Convenience function for clients who just want to read the prefs in order to modify app behavior.
	 * @return
	 */
//	AbstractUserSettings getSettingsForPrincipal();
}