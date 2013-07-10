package org.dpi.security;

public class UserSettingRoleImpl implements UserSettingRole
{
	public static final UserSettingRoleImpl BASIC = new UserSettingRoleImpl("Basic");
	public static final UserSettingRoleImpl ADVANCED = new UserSettingRoleImpl("Advanced");
	public static final UserSettingRoleImpl ADMIN = new UserSettingRoleImpl("Admin");
	public static final UserSettingRoleImpl DEVELOPER = new UserSettingRoleImpl("Developer");

	public String toString() { return name; }
		
	private UserSettingRoleImpl(String name)
	{
		this.name = name;
	}
	
	private final String name;
}
