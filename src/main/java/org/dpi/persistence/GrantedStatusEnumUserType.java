package org.dpi.persistence;

import org.dpi.movimientoCreditos.MovimientoCreditos.GrantedStatus;

public class GrantedStatusEnumUserType extends EnumUserType<GrantedStatus>{

    public GrantedStatusEnumUserType() { 
        super(GrantedStatus.class); 
    } 
}