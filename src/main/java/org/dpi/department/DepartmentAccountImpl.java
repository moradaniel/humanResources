package org.dpi.department;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.dpi.domain.PersistentAbstract;
import org.janux.bus.security.Account;
import org.janux.util.JanuxToStringStyle;



public class DepartmentAccountImpl  extends PersistentAbstract implements DepartmentAccount,Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    Department department;
    Account account;

    @Override
    public Account getAccount() {
        return this.account;
    }

    @Override
    public void setAccount(Account account) {
        this.account=account;

    }

    @Override
    public Department getDepartment() {
        return this.department;
    }

    @Override
    public void setDepartment(Department department) {
        this.department=department;

    }

    @Override
    public String toString() 
    {
        ToStringBuilder sb = new ToStringBuilder(this, JanuxToStringStyle.COMPACT);

        sb.append(super.toString());

        sb.append("department", department.toString());

        sb.append("account", account.toString());

        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((account == null) ? 0 : account.hashCode());
        result = prime * result
                + ((department == null) ? 0 : department.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DepartmentAccountImpl other = (DepartmentAccountImpl) obj;
        if (account == null) {
            if (other.account != null)
                return false;
        } else if (!account.equals(other.account))
            return false;
        if (department == null) {
            if (other.department != null)
                return false;
        } else if (!department.equals(other.department))
            return false;
        return true;
    }

}
