package org.dpi.web.reporting;

import java.util.ArrayList;
import java.util.List;

public class CanGenerateReportResult{
	
	public enum ReasonCodes {negativeBalance,closedCreditsPeriod};
			
	private boolean hasPermissions = false;
	
	private List<String> reasonCodes = new ArrayList<String>();
	
	public boolean canGenerateReport(){
		return hasPermissions && reasonCodes.isEmpty();
	}

	public void addReasonCode(String reasonCode) {
		reasonCodes.add(reasonCode);
	}
	
	public List<String> getReasonCodes() {
		return reasonCodes;
	}
	
	public void setReasonCodes(List<String> reasonCodes) {
		this.reasonCodes = reasonCodes;
	}
	
	public boolean hasPermissions() {
		return hasPermissions;
	}

	public void setHasPermissions(boolean hasPermissions) {
		this.hasPermissions = hasPermissions;
	}

}