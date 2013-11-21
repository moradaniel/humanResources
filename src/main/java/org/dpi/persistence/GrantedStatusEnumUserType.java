package org.dpi.persistence;

import org.dpi.creditsEntry.CreditsEntry.GrantedStatus;

public class GrantedStatusEnumUserType extends EnumUserType<GrantedStatus>{

    public GrantedStatusEnumUserType() { 
        super(GrantedStatus.class); 
    } 
}