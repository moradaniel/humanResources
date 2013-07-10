package org.dpi.security;

import java.util.Map;
import java.util.Set;

import org.janux.bus.security.Account;
import org.janux.bus.security.AccountSetting;
import org.janux.bus.security.AccountSettingImpl;

public abstract class AccountBasedUserSettings extends AbstractUserSettings
{
 	protected Account userAccount = null;
 	protected Map<String,UserSettingDefinition> settingDefinitionsMap = null;

 	protected AccountBasedUserSettings(Account userBean,SettingsGroup[] settingsGroups,Map<String,UserSettingDefinition> settingDefinitionsMap)
 	{
		this.settingsGroups = settingsGroups;
		this.userAccount = userBean;
		this.settingDefinitionsMap = settingDefinitionsMap;
 	}
	
	protected void saveSetting(UserSetting setting)
	{
		boolean replaced = false;
		String key = setting.getSettingDefinition().getSettingTag();
		Set<AccountSetting> settings = userAccount.getSettings();
		for (AccountSetting accountSetting : settings)
		{
			if (accountSetting.getTag().equals(key))
			{
				accountSetting.setValue(setting.getValue());
				replaced = true;
				break;
			}
		}

		if (!replaced)
		{
			AccountSetting settingToSave = new AccountSettingImpl(key,setting.getValue());
			
			settings.add(settingToSave);
		}
	}

	protected UserSetting getSetting(String key)
	{
		UserSetting setting = settingsMap.get(key);
		
		if (setting == null) // don't have it; time to create it and add to our map
		{
			UserSettingDefinition definition = (UserSettingDefinition) settingDefinitionsMap.get(key);
			Set<AccountSetting> settings = userAccount.getSettings();
			String settingValue = null;

			// try to load from backing store
			for (AccountSetting accountSetting : settings)
			{
				if (accountSetting.getTag().equals(key))
				{
					settingValue = accountSetting.getValue();
					break;
				}
			}
			
			setting = new UserSetting(definition,settingValue);
			
			settingsMap.put(key,setting);
		}
		
		return setting;
	}

}
