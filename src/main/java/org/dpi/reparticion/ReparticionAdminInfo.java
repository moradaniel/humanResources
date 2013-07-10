package org.dpi.reparticion;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.dpi.logging.LoggingConstants;


public class ReparticionAdminInfo implements Serializable
{
	private static final long serialVersionUID = 6837102037351198611L;

	private Long id = null;
	/*private String chainCode = null;*/
	private String nombre = null;
	//private HotelStatus status = null;
	private Set<String> users = null;
	/*private String groupCode = null; // just supporting one, for now! TODO: support multiple
	private String portfolioManagerName = null;
	private Map<String, String> hotelAttributes = new HashMap<String, String>();
	private List<RatePlan> activeRatePlans = new ArrayList<RatePlan>();
	private List<RoomType> roomTypes = new ArrayList<RoomType>();*/
	
	public ReparticionAdminInfo()
	{}
	
	public ReparticionAdminInfo(Reparticion reparticion)
	{
		this.id = reparticion.getId();
		this.nombre = reparticion.getNombre();//.getShort(); // TODO: Really should make sure this exists!
		/*this.status = reparticion.getHotelStatus();	// TODO: hotel should have this?!
		this.chainCode = reparticion.getChainCode();
		Set<HotelGroup> groups = reparticion.getHotelGroups();
		
		if ((groups != null) && !groups.isEmpty())
		{
			groupCode = groups.iterator().next().getCode();
		}
		
		if(reparticion.getPortfolioManager()!=null){
			this.portfolioManagerName=reparticion.getPortfolioManager().getName();
		}
		
		this.copyHotelAttributes(reparticion);
		
		if(reparticion.getActiveRatePlans()!=null){
			setActiveRatePlans(reparticion.getActiveRatePlans());	
		}
		
		if(reparticion.getRoomTypes()!=null){
			setRoomTypes(new ArrayList<RoomType>(reparticion.getRoomTypes().values()));	
		}
		
		if(reparticion.getPortfolioManager()!=null){
			this.portfolioManagerName=reparticion.getPortfolioManager().getName();
		}*/
		
	}

	/*
	private void copyHotelAttributes(Hotel hotel) {

		this.hotelAttributes.put(HotelAttributeConstants.Attributes.SHOW_CALENDAR.name(), hotel.getAttribute(HotelAttributeConstants.Attributes.SHOW_CALENDAR.name()));
		this.hotelAttributes.put(HotelAttributeConstants.Attributes.SHOW_PROPERTY_LEVEL_CLOSEOUT.name(), hotel.getAttribute(HotelAttributeConstants.Attributes.SHOW_PROPERTY_LEVEL_CLOSEOUT.name()));
		this.hotelAttributes.put(HotelAttributeConstants.Attributes.SHOW_MAXIMUM_RESTRICTION.name(), hotel.getAttribute(HotelAttributeConstants.Attributes.SHOW_MAXIMUM_RESTRICTION.name()));
		this.hotelAttributes.put(HotelAttributeConstants.Attributes.SHOW_MINIMUM_RESTRICTION.name(), hotel.getAttribute(HotelAttributeConstants.Attributes.SHOW_MINIMUM_RESTRICTION.name()));
		this.hotelAttributes.put(HotelAttributeConstants.Attributes.SHOW_NO_ARRIVALS_RESTRICTION.name(), hotel.getAttribute(HotelAttributeConstants.Attributes.SHOW_NO_ARRIVALS_RESTRICTION.name()));
		this.hotelAttributes.put(HotelAttributeConstants.Attributes.SHOW_NO_DEPARTURE_RESTRICTION.name(), hotel.getAttribute(HotelAttributeConstants.Attributes.SHOW_NO_DEPARTURE_RESTRICTION.name()));
		
		this.hotelAttributes.put(HotelAttributeConstants.Attributes.SHOW_RATES_ON_CALENDAR.name(), hotel.getAttribute(HotelAttributeConstants.Attributes.SHOW_RATES_ON_CALENDAR.name()));
		this.hotelAttributes.put(HotelAttributeConstants.Attributes.SHOW_RATES_ON_CALENDAR_DEFAULT_RATE_PLAN.name(), hotel.getAttribute(HotelAttributeConstants.Attributes.SHOW_RATES_ON_CALENDAR_DEFAULT_RATE_PLAN.name()));
		this.hotelAttributes.put(HotelAttributeConstants.Attributes.SHOW_RATES_ON_CALENDAR_DEFAULT_ROOM_TYPE.name(), hotel.getAttribute(HotelAttributeConstants.Attributes.SHOW_RATES_ON_CALENDAR_DEFAULT_ROOM_TYPE.name()));
		this.hotelAttributes.put(HotelAttributeConstants.Attributes.SHOW_RATES_ON_CALENDAR_DEFAULT_LENGTH_OF_STAY.name(), hotel.getAttribute(HotelAttributeConstants.Attributes.SHOW_RATES_ON_CALENDAR_DEFAULT_LENGTH_OF_STAY.name()));
		this.hotelAttributes.put(HotelAttributeConstants.Attributes.USE_INTERNATIONAL_CALENDAR.name(), hotel.getAttribute(HotelAttributeConstants.Attributes.USE_INTERNATIONAL_CALENDAR.name()));
		this.hotelAttributes.put(HotelAttributeConstants.Attributes.USE_INTERNATIONAL_DATE_FORMAT.name(), hotel.getAttribute(HotelAttributeConstants.Attributes.USE_INTERNATIONAL_DATE_FORMAT.name()));

		this.hotelAttributes.put(Hotel.Attributes.HOTEL_AVAIL_MAX_LOS.name(), hotel.getAttribute(Hotel.Attributes.HOTEL_AVAIL_MAX_LOS.name()));
		this.hotelAttributes.put(Hotel.Attributes.HOTEL_AVAIL_MAX_PRODUCTS.name(), hotel.getAttribute(Hotel.Attributes.HOTEL_AVAIL_MAX_PRODUCTS.name()));
		this.hotelAttributes.put(Hotel.Attributes.HOTEL_AVAIL_SORT_ORDER.name(), hotel.getAttribute(Hotel.Attributes.HOTEL_AVAIL_SORT_ORDER.name()));
		this.hotelAttributes.put(HotelAttributeConstants.Attributes.MAXIMUM_NUMBER_OF_ROOMS_TO_SELL_IN_SINGLE_TRANSACTION.name(), hotel.getAttribute(HotelAttributeConstants.Attributes.MAXIMUM_NUMBER_OF_ROOMS_TO_SELL_IN_SINGLE_TRANSACTION.name()));
		this.hotelAttributes.put(Hotel.Attributes.HOTEL_AVAIL_CORP_INCL_PUBLIC_RATES.name(), hotel.getAttribute(Hotel.Attributes.HOTEL_AVAIL_CORP_INCL_PUBLIC_RATES.name()));
		this.hotelAttributes.put(Hotel.Attributes.HOTEL_AVAIL_GROUP_INCL_PUBLIC_RATES.name(), hotel.getAttribute(Hotel.Attributes.HOTEL_AVAIL_GROUP_INCL_PUBLIC_RATES.name()));
		this.hotelAttributes.put(Hotel.Attributes.HOTEL_AVAIL_PROMO_INCL_PUBLIC_RATES.name(), hotel.getAttribute(Hotel.Attributes.HOTEL_AVAIL_PROMO_INCL_PUBLIC_RATES.name()));
		this.hotelAttributes.put(Hotel.Attributes.HOTEL_AVAIL_REQUESTED_INCL_PUBLIC_RATES.name(), hotel.getAttribute(Hotel.Attributes.HOTEL_AVAIL_REQUESTED_INCL_PUBLIC_RATES.name()));
		this.hotelAttributes.put(Hotel.Attributes.HOTEL_AVAIL_REDEMPTION_INCL_PUBLIC_RATES.name(), hotel.getAttribute(Hotel.Attributes.HOTEL_AVAIL_REDEMPTION_INCL_PUBLIC_RATES.name()));
		this.hotelAttributes.put(Hotel.Attributes.HOTEL_RATE_RULE_INCLUDE_PUBLIC_RATES_WHEN_CORPORATE_RATE_CODE_IS_INVALID.name(), hotel.getAttribute(Hotel.Attributes.HOTEL_RATE_RULE_INCLUDE_PUBLIC_RATES_WHEN_CORPORATE_RATE_CODE_IS_INVALID.name()));
		this.hotelAttributes.put(Hotel.Attributes.HOTEL_RATE_RULE_INCLUDE_PUBLIC_RATES_WHEN_GROUP_RATE_CODE_IS_INVALID.name(), hotel.getAttribute(Hotel.Attributes.HOTEL_RATE_RULE_INCLUDE_PUBLIC_RATES_WHEN_GROUP_RATE_CODE_IS_INVALID.name()));
		this.hotelAttributes.put(Hotel.Attributes.HOTEL_RATE_RULE_INCLUDE_PUBLIC_RATES_WHEN_PROMOTIONAL_RATE_CODE_IS_INVALID.name(), hotel.getAttribute(Hotel.Attributes.HOTEL_RATE_RULE_INCLUDE_PUBLIC_RATES_WHEN_PROMOTIONAL_RATE_CODE_IS_INVALID.name()));
		this.hotelAttributes.put(Hotel.Attributes.HOTEL_AVAIL_DISPLAY_RATE_AS.name(), hotel.getAttribute(Hotel.Attributes.HOTEL_AVAIL_DISPLAY_RATE_AS.name()));
	}*/

	public ReparticionAdminInfo(Long id,String name)
	{
		this.id = id;
		this.nombre = name;
		//this.status = HotelStatus.ACTIVE;
	}

	/**
	 * @param obj
	 * @return
	 */
	public boolean equals(Object obj)
	{
		if (!(obj instanceof ReparticionAdminInfo)) 
		{
		   return false;
		}
		  
		if (this == obj) 
		{
		     return true;
		}
		   
		final ReparticionAdminInfo thatInfo = (ReparticionAdminInfo) obj;
		   
		return new EqualsBuilder()
		                // .append(this.getCode(), thatInfo.getCode())
		                 .append(this.getNombre(), thatInfo.getNombre())
		                 .isEquals();
	}
	
	
	public int hashCode()
	{
	     return new HashCodeBuilder(17, 37).
	       append(this.getId()).
	       append(this.getNombre()).
	       toHashCode();
	}

	public static class NameComparator implements Comparator<ReparticionAdminInfo>, Serializable
	{
		private static final long serialVersionUID = -1203575935957588349L;

		public int compare(ReparticionAdminInfo info1, ReparticionAdminInfo info2)
		{
			int comp = 0;
			
			/*if (!info1.code.equals(info2.code))
			{*/
				comp = info1.nombre.compareTo(info2.nombre);
				
				/*if (comp == 0)
				{
					comp = info1.code.compareTo(info2.code);
				}
			}*/

			return comp;
		}
		
	}

	/*public static class CodeComparator implements Comparator<ReparticionAdminInfo>, Serializable
	{
		private static final long serialVersionUID = 8069477633087743248L;

		public int compare(ReparticionAdminInfo info1, ReparticionAdminInfo info2)
		{
			return info1.code.compareTo(info2.code); // assumes no two should have same code
		}
	}*/

	public static NameComparator nameComparator = new NameComparator();
	//public static CodeComparator codeComparator = new CodeComparator();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String name) {
		this.nombre = name;
	}

	/*
	public boolean isActive() // TODO: is this appropriate for Admin purposes?
	{
		return (status == HotelStatus.ACTIVE) || (status == HotelStatus.SUSPENDED);
	}*/

	public Set<String> getUsers() {
		return users;
	}

	public void setUsers(Set<String> hotelUsers) {
		this.users = hotelUsers;
	}
/*
	public HotelStatus getStatus() {
		return status;
	}

	public void setStatus(HotelStatus status) {
		this.status = status;
	}

	public String getChainCode() {
		return chainCode;
	}

	public void setChainCode(String chainCode) {
		this.chainCode = chainCode;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	*/
	public String toString() 
	{
		ToStringBuilder sb = new ToStringBuilder(this, LoggingConstants.STYLE_AUDIT);

		if (this.getId() != null) 
			sb.append("id", this.getId());
		
		/*if (this.getChainCode() != null) 
			sb.append("chainCode", this.getChainCode());*/
		
		if (this.getNombre() != null) 
			sb.append("name", this.getNombre());
		/*
		if (this.getStatus() != null) 
			sb.append("status", this.getStatus());
		 */
		if (this.getUsers() != null) 
			sb.append("users", this.getUsers());
		/*
		if (this.getGroupCode() != null) 
			sb.append("groupCode", this.getGroupCode());*/
		
		return sb.toString();
	}

	/*
	public Map<String, String> getHotelAttributes() {
		return hotelAttributes;
	}

	public void setHotelAttributes(Map<String, String> hotelAttributes) {
		this.hotelAttributes = hotelAttributes;
	}
	
	public String getPortfolioManagerName() {
		return portfolioManagerName;
	}

	public void setPortfolioManagerName(String portfolioManagerName) {
		this.portfolioManagerName = portfolioManagerName;
	}*/
	
	/*
	public List<RatePlan> getActiveRatePlans() {
		return activeRatePlans;
	}

	public void setActiveRatePlans(List<RatePlan> activeRatePlans) {
		this.activeRatePlans = activeRatePlans;
	}
	
	public List<RoomType> getRoomTypes() {
		return roomTypes;
	}

	public void setRoomTypes(List<RoomType> roomTypes) {
		this.roomTypes = roomTypes;
	}

*/
}
