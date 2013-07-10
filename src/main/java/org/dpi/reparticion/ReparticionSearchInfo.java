package org.dpi.reparticion;


public class ReparticionSearchInfo {

	Long reparticionId;
	/*String hotelCode;*/
	String reparticionName;
	/*String line1;
	String line2;
	String line3;
	String city;
	String state;
	String country;
	String postalCode;
	String phoneNumber;
	String hotelChannels[];
	HotelStatus hotelStatus;*/
	

	/**
	 * @return the hotelId
	 */
	public Long getReparticionId() {
		return reparticionId;
	}

	/**
	 * @param hotelId
	 *            the hotelId to set
	 */
	public void setReparticionId(Long hotelId) {
		this.reparticionId = hotelId;
	}



	/**
	 * @return the hotelName
	 */
	public String getReparticionName() {
		return reparticionName;
	}

	/**
	 * @param hotelName
	 *            the hotelName to set
	 */
	public void setReparticionName(String hotelName) {
		this.reparticionName = hotelName;
	}

}
