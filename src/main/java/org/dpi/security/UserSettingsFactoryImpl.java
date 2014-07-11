package org.dpi.security;

import java.util.HashMap;
import java.util.Map;

import org.dpi.security.AbstractUserSettings.SettingsGroup;
import org.janux.bus.security.Account;
import org.springframework.security.core.context.SecurityContextHolder;




/**
 *
 * Encapsulate a user's settings. Statically define all valid user setting definitions.
 * Settings may be of various types; clients must know this type when requesting a particular setting.
 * Lazily fetches settings from the backing store. Stores to the backing store when set.
 * 
 */
public class UserSettingsFactoryImpl implements UserSettingsFactory
{
	private Map<String,AccountSettings> accountSettingsMap = new HashMap<String,AccountSettings>();
	private static UserSettingsFactoryImpl instance = null;
	

	public synchronized AbstractUserSettings getSettingsForAccount(Account account)
	{
		AccountSettings settings = accountSettingsMap.get(account.getName());
		
		if (settings == null)
		{
			settings = new AccountSettings(account, ourSettingsGroups, settingDefinitionsMap);
			
			accountSettingsMap.put(account.getName(),settings);
		}
		
		return settings;
	}

	public synchronized void clearSettingsForAccount(Account account)
	{
		accountSettingsMap.remove(account.getName());
	}

	/**
	 * Convenience function for clients who just want to read the prefs in order to modify app behavior.
	 * @return
	 */
	public static AccountSettings getSettingsForPrincipal()
	{
		Object accountObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Account account = (Account) accountObj; // better be one of our Account objects!
		AccountSettings settings = (AccountSettings) instance.getSettingsForAccount(account);

		return settings;
	}


	public static final String INVENTORY_VIEW_DAYS    = "ui.inventory.viewDays";
	public static final String EXPAND_INVENTORY_ROOMS = "ui.inventory.showExpanded.roomTypes";
	public static final String EXPAND_INVENTORY_RATES = "ui.inventory.showExpanded.ratePlans";
	public static final String LAST_DEPARTMENT	      = "ui.misc.lastDepartment";
	public static final String ASSURE_HOTEL_SETUP	  = "ui.admin.assure.df.hotelSetup";


	//							settingTag						  			type			    default canModify  userSettingRole			description
	
	private static UserSettingDefinition inventorySettings[] =
	{
		new UserSettingDefinition(INVENTORY_VIEW_DAYS        ,UserSettingDefinition.INTEGER    , "5"    ,true ,UserSettingRoleImpl.BASIC ,"Default Days to View"),
//		new UserSettingDefinition(EXPAND_INVENTORY_ROOMS     ,UserSettingDefinition.BOOLEAN    ,"false" ,true ,UserSettingRole.BASIC ,"Expand Room Types by Default"),
//		new UserSettingDefinition(EXPAND_INVENTORY_RATES     ,UserSettingDefinition.BOOLEAN    ,"false" ,true ,UserSettingRole.BASIC ,"Expand Rate Plans by Default"),
	};
	
	
	private static UserSettingDefinition miscSettings[] =
	{
		new UserSettingDefinition(LAST_DEPARTMENT                 ,UserSettingDefinition.STRING     ,  null  ,true ,UserSettingRoleImpl.BASIC    ,"Last Department"),	
	};
	
	private static UserSettingDefinition adminSettings[] =
	{
		new UserSettingDefinition(ASSURE_HOTEL_SETUP         ,UserSettingDefinition.BOOLEAN     ,"false"  ,true ,UserSettingRoleImpl.ADMIN    ,"Assure Hotel Setup on Select"),	
	};
	
	private static UserSettingDefinition[] definitionGroups[] = 
	{
		inventorySettings,miscSettings,adminSettings
	};
	
	private static Map<String,UserSettingDefinition> settingDefinitionsMap = null;
	{
		settingDefinitionsMap = new HashMap<String,UserSettingDefinition> ();
		
		for (int i = 0; i < definitionGroups.length; i++)
		{
			for (int j = 0; j < definitionGroups[i].length; j++)
			{
				UserSettingDefinition setting = definitionGroups[i][j];
				
				settingDefinitionsMap.put(setting.getSettingTag(),setting);
			}
		}
	}

	private SettingsGroup[] ourSettingsGroups = 
	{
		new SettingsGroup("Inventory Management",inventorySettings),
		new SettingsGroup("General",miscSettings),
		new SettingsGroup("Admin",adminSettings),
	};

	public UserSettingsFactoryImpl()
	{
		if (instance != null)
		{
			throw new IllegalStateException("singleton!");
		}
		
		instance = this;
	}
	
}

