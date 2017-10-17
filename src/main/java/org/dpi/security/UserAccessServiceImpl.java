package org.dpi.security;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dpi.department.Department;
import org.dpi.department.DepartmentAdminInfo;
import org.dpi.department.DepartmentSearchInfo;
import org.dpi.department.DepartmentService;
import org.dpi.util.tree.GenericTreeNode;
import org.janux.bus.security.Account;
import org.janux.bus.security.AccountService;
import org.janux.bus.security.Role;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.CollectionUtils;


public class UserAccessServiceImpl implements UserAccessService
{
    private Log log = LogFactory.getLog(this.getClass());

    /** the datasource */
    private DataSource datasource;

    private DepartmentService departmentService;

    private AccountService accountService;


    /**
     * @return Returns the datasource
     */
    public DataSource getDataSource()
    {
        return datasource;
    }


    /**
     * @param aDataSource The datasource to set.
     */
    public void setDataSource(final DataSource aDataSource)
    {
        this.datasource = aDataSource;
    }

    @SuppressWarnings("unchecked")
    private Set<DepartmentAdminInfo> getDepartmentList(List queryResults,Comparator<DepartmentAdminInfo> comp,boolean getAccounts)
    {
        Set<DepartmentAdminInfo> departmentList = (comp != null) ? new TreeSet<DepartmentAdminInfo>(comp) : new HashSet<DepartmentAdminInfo>();

        for (Object rowObj : queryResults)
        {
            final Map<String, Object> rowMap = (Map<String, Object>) rowObj;
            final BigDecimal id = (BigDecimal) rowMap.get("id");
            final String name = (String) rowMap.get("name");
            final String code = (String ) rowMap.get("code");

            DepartmentAdminInfo info = new DepartmentAdminInfo(new Long(id.longValue()),name,code);

            /*if (StringUtils.hasText(status))
			{
				//info.setStatus(HotelStatus.valueOf(status));
			}*/

            if (getAccounts)
            {
                Set<String> departmentUsers = this.getAccountsForDepartment(id.longValue());
                info.setUsers(departmentUsers);
            }

            departmentList.add(info);
        }

        return departmentList;
    }

    @SuppressWarnings("unchecked")
    private Set<DepartmentAdminInfo> getDepartmentListAsAdminInfo(Set<GenericTreeNode<Department>> departments,Comparator<DepartmentAdminInfo> comp,boolean getAccounts)
    {
        Set<DepartmentAdminInfo> departmentList = (comp != null) ? new TreeSet<DepartmentAdminInfo>(comp) : new HashSet<DepartmentAdminInfo>();

        for (GenericTreeNode<Department> node : departments)
        {
            //final Map<String, Object> rowMap = (Map<String, Object>) department;
            /*final long id = (BigDecimal) rowMap.get("id");
	            final String name = (String) rowMap.get("name");
	            final String code = (String ) rowMap.get("code");*/

            DepartmentAdminInfo info = new DepartmentAdminInfo(node.getData().getId(),node.getData().getName(),node.getData().getCode());

            /*if (StringUtils.hasText(status))
	            {
	                //info.setStatus(HotelStatus.valueOf(status));
	            }*/

            if (getAccounts)
            {
                Set<String> departmentUsers = this.getAccountsForDepartment(node.getData().getId());
                info.setUsers(departmentUsers);
            }

            departmentList.add(info);
        }

        return departmentList;
    }

    @SuppressWarnings("unchecked")
    public Set<DepartmentAdminInfo> getGlobalHotelList(Comparator<DepartmentAdminInfo> comp,boolean getAccounts)
    {
        final JdbcTemplate template = new JdbcTemplate(datasource);

        // this is the query to lookup all the hotels
        final String sHotelQuery = " select code, shortName, longName, status from hotel_hotel ";

        final List results = template.queryForList(sHotelQuery);

        return this.getDepartmentList(results,comp,getAccounts);
    }

    @SuppressWarnings("unchecked")
    public Set<DepartmentAdminInfo> getDepartmentListForAccount(final String aAccountName,Comparator<DepartmentAdminInfo> comp)
    {
        final JdbcTemplate template = new JdbcTemplate(datasource);

        Set<DepartmentAdminInfo> departmentList = new HashSet();

        String sdepartmentQuery = "  " ;

        Account account = accountService.loadAccountByName(aAccountName);

        for(Role role : account.getRoles())
        {
            if(role.getName().equals("DEPARTMENT_RESPONSIBLE") ||
               role.getName().equals("TEMP_READONLY_DEPARTMENT_RESPONSIBLE") ||
               role.getName().equals("HR_MANAGER") ){
                sdepartmentQuery = " select h.id, h.name, h.code " +
                        " from DEPARTMENT_ACCOUNT ha " +
                        " inner join DEPARTMENT h on h.id = ha.DEPARTMENTID " +
                        " inner join sec_account a on a.id = ha.accountId " +
                        " where a.name = ? ";

                final int[] types = { Types.VARCHAR };
                final Object[] args = { aAccountName };
                final List results = template.queryForList(sdepartmentQuery, args, types);

                departmentList = this.getDepartmentList(results,comp,false);

            }else

            if(role.getName().equals("SUBTREE_SUPERVISOR") || role.getName().equals("GOVERNOR")){

                List<Department> userDepartments = departmentService.findUserDepartments(accountService.findAccountByName(aAccountName));

                GenericTreeNode<Department> rootNode = departmentService.getSubTree(userDepartments.get(0).getId());

                departmentList = this.getDepartmentListAsAdminInfo(rootNode.returnAllNodes(rootNode),comp,false);

                /*
                sdepartmentQuery = "Select h.id, h.name , h.code " +
                        " from department h " +
                        " where REGEXP_LIKE (h.code, (select reverse(cast((cast(reverse(code) as number)) as varchar2(30))) as patron from DEPARTMENT_ACCOUNT ha " +
                        " inner join DEPARTMENT r on r.id = ha.DEPARTMENTID " +
                        " inner join sec_account a on a.id = ha.accountId " +
                        " where a.name = ?))" ;
                final int[] types = { Types.VARCHAR };
                final Object[] args = { aAccountName };
                final List results = template.queryForList(sdepartmentQuery, args, types);


                departmentList = this.getDepartmentList(results,comp,false);*/

            }else

            if(role.getName().equals("DEPARTMENTS_SUPERVISOR")){
                
                List<DepartmentSearchInfo> departmentSearchInfos = new ArrayList<DepartmentSearchInfo>();

                departmentSearchInfos = departmentService.findAllDepartments();
                for(DepartmentSearchInfo departmentSearchInfo: departmentSearchInfos)
                {
                    DepartmentAdminInfo info = new DepartmentAdminInfo(departmentSearchInfo.getDepartmentId(),departmentSearchInfo.getDepartmentName(),departmentSearchInfo.getDepartmentCode());

                    departmentList.add(info);
                }
            }

        }

        return departmentList;
    }

    // FIXME: add a test
    public boolean hasAccessToDepartment(final Account anAccount, final Long departmentId) 
    {
        if (anAccount == null) {
            String msg = "Cannot check access of account to department with null account";
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        if (departmentId == null) {
            String msg = "Cannot check access account to department with null department id";
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        if (log.isDebugEnabled()) {
            log.debug("Checking if user: '" + anAccount.getName() + " has access to department: '" + departmentId + "' ...");
        }

        if (anAccount.hasPermissions("VIEW_ALL_DEPARTMENTS", "READ") ) {
            return true;
        }

        final JdbcTemplate template = new JdbcTemplate(datasource);

        final String sDepartmentQuery = " select r.id " +
                " from department_account ra " +
                " inner join department r on r.id = ra.departmentId " +
                " inner join sec_account a on a.id = ra.accountId " +
                " where a.name = ? and r.id = ?";

        final Object[] args = new Object[2];
        args[0] = anAccount.getName();
        args[1] = departmentId;

        final int[] argType = new int[2];  
        argType[0] = java.sql.Types.VARCHAR;
        argType[1] = java.sql.Types.DECIMAL;

        final List results = template.queryForList(sDepartmentQuery, args, argType);

        if(results!=null && results.size()>0){
            return true;
        }

        return false;
    }


    // FIXME add a test!!!
    @SuppressWarnings("unchecked")
    public boolean hasAccessToDepartment(final String aAccountName,final Long aDepartmentId)
    {
        if (aAccountName == null) {
            String msg = "Cannot check access of account to department with null account name";
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        if (aDepartmentId == null) {
            String msg = "Cannot check access account to department with null department code";
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        Account account = accountService.loadAccountByName(aAccountName);

        if (account.hasPermissions("VIEW_ALL_HOTELS", "READ")){
            return true;
        }

        return this.hasAccessToDepartment(account, aDepartmentId);
    }


    @SuppressWarnings("unchecked")
    public Set<String> getAccountsForDepartment(final Long departmentId)
    {
        final JdbcTemplate template = new JdbcTemplate(datasource);

        // this is the query to lookup the list of associated accounts for the given department
        final String sHotelQuery =  " select a.name" +
                " from department_account ha " +
                " inner join sec_account a on a.id = ha.accountId " +
                " inner join hotel_hotel h on h.id = ha.hotelId " +
                " where h.code = ? " +
                " UNION" +
                " SELECT distinct sec_account.name " +
                " FROM sec_permission_bit "+
                " Inner Join sec_permission_context ON sec_permission_context.id = sec_permission_bit.contextId "+
                " Inner Join sec_permission_granted ON sec_permission_bit.contextId = sec_permission_granted.contextId "+
                " Inner Join sec_role ON sec_permission_granted.roleId = sec_role.id "+
                " Inner Join sec_account_role ON sec_role.id = sec_account_role.roleId "+
                " Inner Join sec_account ON sec_account_role.accountId = sec_account.id "+
                " WHERE "+
                " sec_permission_context.name = 'VIEW_ALL_DEPARTMENTS' AND "+
                " sec_permission_bit.name = 'READ' ";

        final int[] types = { Types.VARCHAR };
        final Object[] args = { departmentId };
        final List results = template.queryForList(sHotelQuery, args, types);

        Set<String> accountNames = new HashSet<String>();

        for (Object rowObj : results)
        {
            final Map rowMap = (Map) rowObj;
            final String accountId = (String) rowMap.get("name");

            accountNames.add(accountId);
        }

        return accountNames;
    }

    /**
     * This method retrieves all the hotels that are linked to a specific account
     * @param aAccountName the account to lookup links for
     * @return a set of hotel codes that the account has access to
     */
    @SuppressWarnings("unchecked")
    public Set<String> getHotelCodes(final String aAccountName)
    {
        // this is the query to lookup the list of associated hotels
        final String sHotelQuery = " select h.code" +
                " from hotel_account ha " +
                " inner join hotel_hotel h on h.id = ha.hotelId " +
                " inner join sec_account a on a.id = ha.accountId " +
                " where a.name = ? ";


        // parameter settings
        final int[] types = new int[1];
        types[0] = Types.VARCHAR;

        final Object[] args = new Object[1];
        args[0] = aAccountName;


        final Set<String> hotelCodes = new HashSet<String>();

        // run the query
        final JdbcTemplate jt = new JdbcTemplate(datasource);
        final List list = jt.queryForList(sHotelQuery, args, types);
        final Iterator it = list.iterator();
        while (it.hasNext())
        {
            final Map<String, Object> values = (Map<String, Object> )it.next();
            final String sCode      = (String )values.get("code");
            hotelCodes.add(sCode);
        }

        return (hotelCodes);
    }

    /**
     * This method clears all the user/hotel links for a specific account
     * @param aAccountName identifies the account to clear links for
     */
    public void clearAccountLinks(final String aAccountName)
    {
        // this is the query to delete the links
        final String sNativeCodeQuery = " delete from hotel_account " +
                " where accountId = (select id " +
                " from sec_account " +
                " where name = ?) ";


        // parameter settings
        final int[] types = new int[1];
        types[0] = Types.VARCHAR;

        final Object[] args = new Object[1];
        args[0] = aAccountName;

        // run the query
        final JdbcTemplate jt = new JdbcTemplate(datasource);
        jt.update(sNativeCodeQuery, args, types);
    }


    /**
     * This method clears all the user/hotel links for a specific hotel
     * @param aHotelCode identifies the hotel to clear links for
     */
    public void clearHotelLinks(final String aHotelCode)
    {
        if (log.isDebugEnabled()){
            log.debug("Attempting to delete accounts assigned to hotel: '"+aHotelCode +"' ...");
        }

        // this is the query to delete the links
        final String sNativeCodeQuery = " delete from hotel_account " +
                " where hotelId = (select id " +
                " from hotel_hotel " +
                " where code = ?) ";


        // parameter settings
        final int[] types = new int[1];
        types[0] = Types.VARCHAR;

        final Object[] args = new Object[1];
        args[0] = aHotelCode;

        // run the query
        final JdbcTemplate jt = new JdbcTemplate(datasource);
        int numberOfDeletedassociations = jt.update(sNativeCodeQuery, args, types);

        if (log.isInfoEnabled()){
            log.info("Deleted "+ numberOfDeletedassociations +" relation between accounts and hotel: '"+aHotelCode+"' in table hotel_account");
        }
    }


    /**
     * 
     */
    public void setAccountHotelLinks(final String aAccountName, final Set<String> aHotelCodes)
    {
        int numberOfHotelsToAssign = aHotelCodes==null?0:aHotelCodes.size();

        if (log.isDebugEnabled()) {
            log.debug("Attempting to assign " + numberOfHotelsToAssign + " hotels to account '" + aAccountName + "'");
        }

        // remove the current links
        clearAccountLinks(aAccountName);

        // if account has permissions to "VIEW_ALL_HOTELS" and the account has hotels selected
        // then save the hotels in the hotel_account table
        // if not, then do nothing since the account has access to all hotels by default
        Account account = accountService.loadAccountByName(aAccountName);

        // pp-20101028: the following check is frail, as there is no guarantee that the control dropdown
        // will always contain all hotels in the system, for example, at some point we may want to limit
        // them to only 'active' hotels; also, loading all hotels just to count them is inneficcient.
        // This should be replaced with a screen display indicating that the account can view all hotels
        // and that the combo box will actually restrict the list
        if (account.hasPermissions("VIEW_ALL_HOTELS", "READ")) 
        {
            List<DepartmentSearchInfo> hotelSearchInfos = new ArrayList<DepartmentSearchInfo>();

            hotelSearchInfos = departmentService.findAllDepartments();

            if(CollectionUtils.isEmpty(aHotelCodes) || aHotelCodes.size()==hotelSearchInfos.size()){
                //If CollectionUtils.isEmpty(aHotelCodes) then the user cleaned up his selected hotels subset, so he will see all hotels
                //aHotelCodes.size()==hotelSearchInfos.size()then user did not selected any subset of
                //hotels so he wants to continue to see all hotels, so do nothing and return
                return;
            }
        }

        if ((aHotelCodes == null) || (aAccountName == null))
        {
            return;
        }

        final String[] sHotelCodes = (String[] )aHotelCodes.toArray(new String[0]);

        // this is the query to insert the account/hotel link
        final String sInsertQuery = 
                " insert into hotel_account (hotelId, accountId) " +
                        " (select h.id, a.id " +
                        " from hotel_hotel h, sec_account a " +
                        " where h.code = ? " +
                        " and a.name = ?) ";


        // this will process this resultset
        final BatchPreparedStatementSetter pss = new BatchPreparedStatementSetter ()
        {
            public int getBatchSize()
            {
                return sHotelCodes.length;
            }

            public void setValues(PreparedStatement ps, int i) throws SQLException
            {
                ps.setString(1, sHotelCodes[i]);
                ps.setString(2, aAccountName);
            }
        };

        // run the queries to insert the links
        final JdbcTemplate jt = new JdbcTemplate(datasource);
        jt.batchUpdate(sInsertQuery, pss);

        if (log.isInfoEnabled()) {
            log.info("Assigned " + sHotelCodes.length + " to account: '" + aAccountName + "'");
        }
    }

    /**
     * Deletes all accounts assigned to the hotels and assigns the new list of accounts
     */
    public void setHotelAccountLinks(final String hotelCode, final String[] accountNames)
    {	

        if (hotelCode == null)
        {
            if (log.isWarnEnabled()){
                log.warn("Assigning accounts to hotel - hotelCode cannot be null");
            }
            return;
        }

        if ((accountNames == null) || (accountNames.length == 0))
        {
            if (log.isWarnEnabled()){
                log.warn("Assigning accounts to hotel - accountNames cannot be empty or null");
            }

            return;
        }

        if (log.isDebugEnabled()){
            log.debug("Attempting to assign "+accountNames.length +" accounts to hotel: '"+hotelCode+"' ...");
        }


        // remove the current links
        this.clearHotelLinks(hotelCode);


        // this is the query to insert the account/hotel link
        final String insertStatement = " insert into hotel_account (accountId, hotelId) " +
                " (select a.id, h.id " +
                " from sec_account a, hotel_hotel h " +
                " where a.name = ? " +
                " and h.code = ?) ";


        // this will process this resultset
        final BatchPreparedStatementSetter statementSetter = new BatchPreparedStatementSetter()
        {
            public int getBatchSize()
            {
                return accountNames.length;
            }

            public void setValues(PreparedStatement ps, int i) throws SQLException
            {
                ps.setString(1, accountNames[i]);
                ps.setString(2, hotelCode);
            }
        };


        // run the queries to insert the links
        final JdbcTemplate template = new JdbcTemplate(datasource);

        template.batchUpdate(insertStatement, statementSetter);

        if (log.isInfoEnabled()){
            log.info("Assigned "+accountNames.length +" accounts to hotel: '"+hotelCode+"'");
        }

    }

    /**
     * Accounts that can be assigned to hotels when creating/updating a hotel.
     * Accounts with VIEW_ALL_HOTELS permission are not assignable since they have access
     * to all hotels by default
     * @return a list of assignable accounts
     */
    public List<Account>findAssignableAccountsToHotels() 
    {
        if (log.isDebugEnabled()) {
            log.debug("Searching for accounts that are assignable to hotels...");
        }

        SortedSet<Account> accounts = accountService.loadAllAccounts(true);
        List<Account> assignableAccounts = new ArrayList<Account>();

        for(Account account : accounts){
            if(!account.hasPermissions("VIEW_ALL_HOTELS", "READ")){
                assignableAccounts.add(account);	
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("Found "+assignableAccounts.size()+" assignable accounts");
        }

        return assignableAccounts;
    }

    public List<Account>findPortfolioManagers() 
    {
        if (log.isDebugEnabled()) {
            log.debug("Searching for portfolio manager accounts...");
        }

        SortedSet<Account> accounts = accountService.loadAllAccounts(true);
        List<Account> portfolioManagerAccounts = new ArrayList<Account>();

        for(Account account : accounts){
            for(Role role : account.getRoles()){
                if(role.getName().equals("PORTFOLIO_MANAGER")){
                    portfolioManagerAccounts.add(account);
                    break;
                }				
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("Found "+portfolioManagerAccounts.size()+" portfolio manager accounts");
        }

        return portfolioManagerAccounts;
    }

    public DepartmentService getDepartmentService() {
        return departmentService;
    }

    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    public AccountService getAccountService() {
        return accountService;
    }


    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }


}
