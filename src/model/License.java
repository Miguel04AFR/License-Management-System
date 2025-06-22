  package model;

import java.sql.Date;

public class License {
    private String licenseCode;
    private String licenseType; // 'A', 'B', 'C', 'D', 'E'
    private Date issueDate;
    private Date expirationDate;
    private String vehicleCategory; // "Motorcycle", "Car", "Truck", "Bus"
    private String restrictions;
    private boolean isRenewed;
    private String driverId; // Foreign key
	public String getLicenseCode() {
		return licenseCode;
	}
	public void setLicenseCode(String licenseCode) {
		this.licenseCode = licenseCode;
	}
	public String getLicenseType() {
		return licenseType;
	}
	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}
	public Date getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}
	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	public String getVehicleCategory() {
		return vehicleCategory;
	}
	public void setVehicleCategory(String vehicleCategory) {
		this.vehicleCategory = vehicleCategory;
	}
	public String getRestrictions() {
		return restrictions;
	}
	public void setRestrictions(String restrictions) {
		this.restrictions = restrictions;
	}
	public boolean isRenewed() {
		return isRenewed;
	}
	public void setRenewed(boolean isRenewed) {
		this.isRenewed = isRenewed;
	}
	public String getDriverId() {
		return driverId;
	}
	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}
}