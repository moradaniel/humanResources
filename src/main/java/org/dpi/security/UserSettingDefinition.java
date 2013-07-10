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



/**
 * Provide definition of a user setting: tag, type, default value, and whether the user
 * is allowed to modify.
 */
public class UserSettingDefinition
{
	public static final int FIRST_SETTING_TYPE = 0;
	public static final int STRING = 1;
	public static final int INTEGER = 2;
	public static final int BOOLEAN = 3;
	public static final int STRING_LIST = 4;
	public static final int LAST_SETTING_TYPE = STRING_LIST;
	public static final int NUM_SETTING_TYPES = LAST_SETTING_TYPE - FIRST_SETTING_TYPE + 1;

	private String settingTag;
	private int settingType;
	private String defaultValue;
	private boolean userCanModify;	
	private UserSettingRole requiredUserSettingRole;
	private String description;
	
	/**
	 * Caller must guarantee that defaultValue is of appropriate type!
	 * 
	 * @param settingTag
	 * @param settingType
	 * @param defaultValue
	 * @param userCanModify
	 * @param requiredUserSettingRole
	 * @param description
	 */
	public UserSettingDefinition(String settingTag, int settingType, String defaultValue,
			boolean userCanModify, UserSettingRole requiredUserSettingRole, String description)
	{
		this.settingTag = settingTag;
		this.settingType = settingType;
		this.defaultValue = defaultValue;
		this.userCanModify = userCanModify;
		this.requiredUserSettingRole = requiredUserSettingRole;
		this.description = description;
	}

	private UserSettingDefinition() {}

	/**
	 * @return Returns the default_value.
	 */
	public String getDefaultValue()
	{
		return defaultValue;
	}
	/**
	 * @return Returns the setting_tag.
	 */
	public String getSettingTag()
	{
		return settingTag;
	}
	/**
	 * @return Returns the setting_type.
	 */
	public int getSettingType()
	{
		return settingType;
	}
	/**
	 * @return Returns the user_can_modify.
	 */
	public boolean getUserCanModify()
	{
		return userCanModify;
	}
	
	/**
	 * @return Returns the description.
	 */
	public String getDescription()
	{
		return description;
	}
	
	public UserSettingRole getRequiredUserSettingRole()
	{
		return requiredUserSettingRole;
	}
		
	public boolean getIsStringType()
	{
		return (settingType == STRING);
	}
	
	public boolean getIsBooleanType()
	{
		return (settingType == BOOLEAN);
	}
	
	public boolean getIsIntegerType()
	{
		return (settingType == INTEGER);
	}
	
	public boolean getIsStringListType()
	{
		return (settingType == STRING_LIST);
	}
}
