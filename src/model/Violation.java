package model;

import java.sql.Date;

public class Violation {
    private String violationCode;
    private String violationType; // "Minor", "Major", "Critical"
    private Date date;
    private String location;
    private String description;
    private int deductedPoints;
    private boolean isPaid;
    private String licenseCode; // Foreign key
	public String getViolationCode() {
		return violationCode;
	}
	public void setViolationCode(String violationCode) {
		this.violationCode = violationCode;
	}
	public String getViolationType() {
		return violationType;
	}
	public void setViolationType(String violationType) {
		this.violationType = violationType;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getDeductedPoints() {
		return deductedPoints;
	}
	public void setDeductedPoints(int deductedPoints) {
		this.deductedPoints = deductedPoints;
	}
	public boolean isPaid() {
		return isPaid;
	}
	public void setPaid(boolean isPaid) {
		this.isPaid = isPaid;
	}
	public String getLicenseCode() {
		return licenseCode;
	}
	public void setLicenseCode(String licenseCode) {
		this.licenseCode = licenseCode;
	}
}