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

import java.util.Map;

import org.dpi.security.Constants.ActiveContext;
import org.dpi.security.Constants.Permission;
import org.janux.bus.security.Account;


/**
 * @author kirlen
 *
 * Encapsulate a user's settings. Statically define all valid user setting definitions.
 * Settings may be of various types; clients must know this type when requesting a particular setting.
 * Lazily fetches settings from the backing store. Stores to the backing store when set.
 * 
 */
public class AccountSettings extends AccountBasedUserSettings
{
	protected AccountSettings(Account account,SettingsGroup[] settingsGroups,Map<String,UserSettingDefinition> settingDefinitionsMap)
	{
		super(account, settingsGroups, settingDefinitionsMap);
	}
	
	public int getInventoryViewDays()
	{
		return this.getIntegerSetting(UserSettingsFactoryImpl.INVENTORY_VIEW_DAYS);
	}
	
	public boolean getExpandInventoryRoomTypes()
	{
		return this.getBooleanSetting(UserSettingsFactoryImpl.EXPAND_INVENTORY_ROOMS);
	}
	
	public boolean getExpandInventoryRatePlans()
	{
		return this.getBooleanSetting(UserSettingsFactoryImpl.EXPAND_INVENTORY_RATES);
	}
	
	public String getLastHotel()
	{
		return this.getStringSetting(UserSettingsFactoryImpl.LAST_HOTEL);
	}
	
	public void setLastHotel(String hotelCode)
	{
		this.setStringSetting(UserSettingsFactoryImpl.LAST_HOTEL,hotelCode);
	}
	
	public boolean getAssureHotelSetup()
	{
		return this.getBooleanSetting(UserSettingsFactoryImpl.ASSURE_HOTEL_SETUP);
	}

	@Override
	protected boolean isDisplayable(UserSettingDefinition definition)
	{
		UserSettingRole requiredRole = definition.getRequiredUserSettingRole();
		boolean isAdmin = userAccount.hasPermissions(ActiveContext.Manage_Properties.name(), Permission.READ.name());
		boolean displayable =  (
				(requiredRole == UserSettingRoleImpl.BASIC) ||
				(requiredRole == UserSettingRoleImpl.ADMIN && isAdmin)
				);
		
		return displayable;
	}
}

