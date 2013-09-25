package org.dpi.logging;

import org.apache.commons.lang.builder.StandardToStringStyle;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 ***************************************************************************************************
 * Abstract Class used to hold public static constants used for logging, such as the keys for 
 * Mapped Diagnostic Contexts, and a standard ToStringStyle for audit logs
 * 
 ***************************************************************************************************
 */
public abstract class LoggingConstants
{
	/** key for the 'native' hotel code by which the hotel is known in the system: the key value is "Hotel" */
	public static final String KEY_HOTEL    = "Hotel";

	/** key for a Channel through which a Reservation may be coming the key value is "Channel" */
	public static final String KEY_CHANNEL = "Channel";

	/** key for a 'Remote' Hotel Code by which the hotel may be identified in a third-party system or channel: the key value is "RemoteHotelCode" */
	public static final String KEY_HOTEL_REMOTE = "RemoteHotelCode";

	public static final String KEY_DURATION = "Duration";
	public static final String KEY_ACCOUNT  = "Account";
	public static final String KEY_METHOD   = "Method";
	public static final String KEY_DETAILS  = "Details";
	public static final String KEY_SESSION  = "Session";

	public static final ToStringStyle STYLE_AUDIT = new StandardToStringStyle();

	/** used to populate a hotel code when the hotel code is not set in a request */
	public static final String HOTEL_NOT_SET = "Not Set";

	static {
		((StandardToStringStyle)STYLE_AUDIT).setUseFieldNames(true);
		((StandardToStringStyle)STYLE_AUDIT).setUseClassName(false);
		((StandardToStringStyle)STYLE_AUDIT).setUseIdentityHashCode(false);
	}
	

} // end class


