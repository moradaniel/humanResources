package org.dpi.department;

import org.janux.bus.security.Account;

public interface DepartmentAccount {

    Account getAccount();

    void setAccount(Account account);

    Department getDepartment();

    void setDepartment(Department department);

}
