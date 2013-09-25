package org.dpi.security;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.dpi.reparticion.ReparticionAdminInfo;
import org.janux.bus.security.Account;


public interface UserAccessService
{
	
	/**
	 * This method retrieves all the hotels that are linked to a specific account
	 * @param aAccountName the account to lookup links for
	 * @return a set of hotel codes that the account has access to
	 */
	Set<String> getHotelCodes(final String aAccountName);

	Set<String> getAccountsForReparticion(final Long reparticionId);
	
	/**
	 * This method retrieves a list of all the hotels that are linked to a specific account
	 * @param aAccountName the account to lookup links for
	 * @param comp optional Comparator
	 * @return a set of ReparticionAdminInfo objects
	 */
	Set<ReparticionAdminInfo> getReparticionListForAccount(final String aAccountName,Comparator<ReparticionAdminInfo> comp);

	/**
	 * This method retrieves a list of all the hotels
	 * @param comp optional Comparator
	 * @return a set of HotelMiniInfo objects
	 */
	Set<ReparticionAdminInfo> getGlobalHotelList(Comparator<ReparticionAdminInfo> comp,boolean getAccounts);

	/**
	 * This method clears all the user/hotel links for a specific account
	 * @param aAccountName identifies the account to clear links for
	 */
	void clearAccountLinks(final String aAccountName);

	
	/**
	 * This method clears all the user/hotel links for a specific hotel
	 * @param aHotelCode identifies the hotel to clear links for
	 */
	void clearHotelLinks(final String aHotelCode);
	
	/**
	 * This method translates the given hotel chain code from the GDS into our native code
	 * @param aGdsCode identifies the GDS
	 * @param aRemoteHotelChainCode the hotel chain code referenced by the GDS
	 * @return the native hotel chain code as referenced internally in the hotel chain table
	 */
	void setAccountHotelLinks(final String aAccountName, final Set<String> aHotelCodes);

	void setHotelAccountLinks(final String hotelCode, final String[] accountNames);
	
	/**
	 * Accounts that can be assigned to hotels when creating/updating a hotel.
	 * Accounts with VIEW_ALL_HOTELS permission are not assignable since they have access
	 * to all hotels by default
	 * @return a list of assignable accounts
	 */
	public List<Account>findAssignableAccountsToHotels();
	
	/**
	 * Check if a logged user has access to a given hotel
	 *
	 * @param aAccountName
	 * @param aHotelCode
	 * @return whether the user has access to the given hotel or not
	 * @throws IllegalArgumentException if the parameters are null
	 */
	public boolean hasAccessToReparticion(final String aAccountName,final Long aReparticionId);
	
	/**
	 * Check if a logged user has access to a given hotel; assumes that the Account's Role and
	 * Permissions have been fully instantiated; use this method if the account has already been
	 * instantiated so that it does not have be loaded again
	 *
	 * @param anAccount 
	 * @param aHotel
	 * @return whether the user has access to the given hotel or not
	 * @throws IllegalArgumentException if the parameters are null
	 */
	public boolean hasAccessToReparticion(final Account anAccount, final Long aHotelCode);
	
	public List<Account>findPortfolioManagers();
}
