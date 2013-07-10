package org.dpi.util.query;


public interface HqlQueryFilter {

	String[] getParamNames();

	Object[] getParamValues();

	String getHqlWhereClause();
}
