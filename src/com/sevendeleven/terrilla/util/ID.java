package com.sevendeleven.terrilla.util;

public class ID {
	private int intId;
	private String uniqueName;
	public ID(int intId, String uniqueName) {
		this.intId = intId;
		this.uniqueName = uniqueName;
	}
	public Integer getId() {
		return intId;
	}
	public String getUniqueName() {
		return uniqueName;
	}
	@Override
	public boolean equals(Object o) {	//True if either the intId or uniqueName match (this is because the two should always match in either case)
		if (o == this || (o instanceof ID && (((ID)o).intId == this.intId || ((ID)o).uniqueName.equals(uniqueName)))) {
			return true;
		}
		return false;
	}
}
