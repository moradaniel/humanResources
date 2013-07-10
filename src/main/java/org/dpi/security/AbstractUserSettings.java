/*
 * Copyright 2006 Kevin Irlen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dpi.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * @author kirlen
 *
 * Encapsulate a user's settings. Statically define all valid user setting definitions.
 * Settings may be of various types; clients must know this type when requesting a particular setting.
 * Lazily fetches settings from the backing store. Stores to the backing store when set.
 * 
 */
public abstract class AbstractUserSettings
{
	protected Map<String,UserSetting> settingsMap = null;
	protected SettingsGroup[] settingsGroups = new SettingsGroup[0]; // subclass must set
	private boolean allSettingsLoaded = false;
	
	protected AbstractUserSettings()
	{
		this.settingsMap = new TreeMap<String,UserSetting>();
	}

	public static class SettingsGroup
	{
		String groupName;
		UserSettingDefinition[] settingDefs;
		
		public SettingsGroup(String groupName, UserSettingDefinition[] settingDefs)
		{
			this.groupName = groupName;
			this.settingDefs = settingDefs;
		}

		public String getGroupName()
		{
			return groupName;
		}

		public UserSettingDefinition[] getSettingDefs()
		{
			return settingDefs;
		}

//		public List<UserSetting> getSettings()
//		{
//			List<UserSetting> availableSettings = new ArrayList<UserSetting>();
//			
//			for (int i = 0; i < settings.length; i++)
//			{
//				String tag = settings[i].getSettingTag();
//				UserSetting setting = AbstractUserSettings.this.getSetting(tag);
//				
//				if (setting.displayable)
//				{
//					availableSettings.add(setting);
//				}
//			}
//			
//			return availableSettings;
//		}
		
	}

	

	public String getStringSetting(String key)
	{
		UserSetting setting = this.getSetting(key);
		
		return setting.getStringValue();
	}

	public void setStringSetting(String key,String value)
	{
		UserSetting setting = this.getSetting(key);
		
		setting.setStringValue(value);

		this.saveSetting(setting);
	}

	public boolean getBooleanSetting(String key)
	{
		UserSetting setting = this.getSetting(key);
		
		return setting.getBooleanValue();
	}

	public void setBooleanSetting(String key,boolean value)
	{
		UserSetting setting = this.getSetting(key);
		
		setting.setBooleanValue(value);
		
		this.saveSetting(setting);
	}

	public String[] getStringListSetting(String key)
	{
		UserSetting setting = this.getSetting(key);
		
		return setting.getStringListValue();
	}

	public void setStringListSetting(String key,String[] values)
	{
		UserSetting setting = this.getSetting(key);
		
		setting.setStringListValue(values);

		this.saveSetting(setting);
	}
	
	public int getIntegerSetting(String key)
	{
		UserSetting setting = this.getSetting(key);
		
		return setting.getIntegerValue();
	}

	public void setIntegerSetting(String key, int value)
	{
		UserSetting setting = this.getSetting(key);
		
		setting.setIntegerValue(value);
		
		this.saveSetting(setting);
	}
	
	protected abstract void saveSetting(UserSetting setting);
	
	protected abstract UserSetting getSetting(String key);
	
	public SettingsGroup[] getGroups()
	{
		this.loadAllSettings();
		
		return settingsGroups;
	}

	public Collection<UserSetting> getAllSettings()
	{
		this.loadAllSettings();
		
		return settingsMap.values();
	}

	public List<UserSetting> getGroupSettings(SettingsGroup group)
	{
		List<UserSetting> availableSettings = new ArrayList<UserSetting>();
		
		for (UserSettingDefinition def : group.getSettingDefs())
		{
			UserSetting setting = this.getSetting(def.getSettingTag());
			
			if (setting.displayable)
			{
				availableSettings.add(setting);
			}
		}
		
		return availableSettings;
	}
	

	protected abstract boolean isDisplayable(UserSettingDefinition definition);
	
	// TODO: push to AdminSession to speed this up
	private void loadAllSettings()
	{
		if (allSettingsLoaded)
		{
			return;
		}
		
		// first load the settings
		for (int i = 0; i < settingsGroups.length; i++)
		{
			UserSettingDefinition[] definitions = settingsGroups[i].settingDefs;
			
			for (int j = 0; j < definitions.length; j++)
			{
				this.getSetting(definitions[j].getSettingTag());
			}
		}
		
		// Now set their displayable attribute (since that's dependent on some settings)
		for (int i = 0; i < settingsGroups.length; i++)
		{
			UserSettingDefinition[] definitions = settingsGroups[i].settingDefs;
			
			for (int j = 0; j < definitions.length; j++)
			{
				UserSettingDefinition definition = definitions[j];
				boolean displayable =  this.isDisplayable(definition);
				UserSetting setting = this.getSetting(definition.getSettingTag());
				
				setting.setDisplayable(displayable);
			}
		}
		
		allSettingsLoaded = true;
	}
}

