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
 * An actual user setting:  definition plus value.  Enforces type safety.
 * 
 */
public class UserSetting
{
	private UserSettingDefinition definition = null;
	private String value = null;
	boolean displayable = true;
	
	private UserSetting() {} // no default constructor
	
	/**
	 * 
	 */
	public UserSetting(UserSettingDefinition definition,String value)
	{
		this.definition = definition;
		this.setFromString(value);
	}

	/**
	 * Returns the definition for this UserSetting.
	 */
	public UserSettingDefinition getSettingDefinition()
	{
		return definition;
	}

	/**
	 * Returns the value for this UserSetting.
	 */
	public String getValue()
	{
		return value;
	}
	
	/**
	 * If value is not appropriate for setting type, default will be used.
	 * 
	 * @param value
	 */
	protected void setFromString(String value)
	{
		boolean okToSet = false;

		switch (definition.getSettingType())
		{
			case UserSettingDefinition.STRING:
			case UserSettingDefinition.STRING_LIST:
				okToSet = true;
			break;
			
			case UserSettingDefinition.INTEGER:
				try
				{
					Integer.parseInt(value);
					okToSet = true;
				}
				catch(NumberFormatException ex)
				{
				}
			break;
			
			case UserSettingDefinition.BOOLEAN:
				okToSet = ("true".equals(value) || "false".equals(value));
			break;
								
			default:
		}
		
		this.value = okToSet ? value : definition.getDefaultValue();
	}
	
	protected String getStringValue()
	{
		if (definition.getSettingType() != UserSettingDefinition.STRING)
		{
			throw new IllegalAccessError();
		}

		return this.value;
	}
	
	protected void setStringValue(String value)
	{
		if (definition.getSettingType() != UserSettingDefinition.STRING)
		{
			throw new IllegalAccessError();
		}

		this.value = value;
	}
	
	protected void setBooleanValue(boolean value)
	{
		if (definition.getSettingType() != UserSettingDefinition.BOOLEAN)
		{
			throw new IllegalAccessError();
		}

		this.value = value ? "true" : "false";
	}

	protected boolean getBooleanValue()
	{
		if (definition.getSettingType() != UserSettingDefinition.BOOLEAN)
		{
			throw new IllegalAccessError();
		}

		return "true".equals(value);
	}
	
	int getIntegerValue()
	{
		if (definition.getSettingType() != UserSettingDefinition.INTEGER)
		{
			throw new IllegalAccessError();
		}

		int result = 0;
		
		try
		{
			result = Integer.parseInt(value);
		}
		catch(NumberFormatException ex)
		{
			throw new IllegalStateException("integer setting had non-integer value!");
		}
		
		return result;
	}
	
	void setIntegerValue(int value)
	{
		if (definition.getSettingType() != UserSettingDefinition.INTEGER)
		{
			throw new IllegalAccessError();
		}

		this.value = Integer.toString(value);
	}
	
	private static final String STRINGLIST_SPLITEXP = "__,__";
	
	protected String[] getStringListValue()
	{
		if (definition.getSettingType() != UserSettingDefinition.STRING_LIST)
		{
			throw new IllegalAccessError();
		}

	    String[] list = value != null ? value.split(STRINGLIST_SPLITEXP) : null;
		
		return list;
	}
	
	protected void setStringListValue(String[] stringList)
	{
		if (definition.getSettingType() != UserSettingDefinition.STRING_LIST)
		{
			throw new IllegalAccessError();
		}

		if (stringList != null)
		{
			StringBuffer list = new StringBuffer();
		
			for (int i = 0; i < stringList.length; i++)
			{
				list.append(stringList[i]);
				list.append(STRINGLIST_SPLITEXP);
			}
		
			this.value = list.toString();
		}
		else
		{
			this.value = null;
		}
		
	}

	public boolean isDisplayable()
	{
		return displayable;
	}

	public void setDisplayable(boolean displayable)
	{
		this.displayable = displayable;
	}

}

