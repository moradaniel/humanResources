package org.dpi.persistence;

import org.dpi.creditsPeriod.Status;

public class CreditsPeriodStatusEnumUserType extends EnumUserType<Status> { 
    public CreditsPeriodStatusEnumUserType() { 
        super(Status.class); 
    } 
}