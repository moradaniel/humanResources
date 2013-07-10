package org.dpi.security;

public class Constants {
	
	public enum ActiveContext {
		Inventory_Management,
		Source_Management,
		Property_Info,
		GDS_Translations,
		Rooms_Rates,
		Reports,
		Search_Reservations,
		Search_Hotels,
		Search_Error_Queue,
		Create_Reservation,
		Manage_Users,
		Manage_Properties,
		Call_Wrap,
		Export_Res_Data,
		TRAVEL_AGENCY
	};

	public enum Permission { READ, WRITE, UPDATE, DELETE };

}
