package model;

import java.sql.Date;

public class Exam {
    private String examCode;
    private String examType; // "Theoretical", "Practical", "Medical"
    private Date examDate;
    private String result; // "Passed", "Failed"
    private String examinerName;
    private String entityCode; // Foreign key
    private String driverId; // Foreign key
	private String  vehicleCategory;
	

	public String getVehicleCategory() {
		return vehicleCategory;
	}
	public void setVehicleCategory(String vehicleCategory) {
		this.vehicleCategory = vehicleCategory;
	}
	public String getExamCode() {
		return examCode;
	}
	public void setExamCode(String examCode) {
		this.examCode = examCode;
	}
	public String getExamType() {
		return examType;
	}
	public void setExamType(String examType) {
		this.examType = examType;
	}
	public Date getExamDate() {
		return examDate;
	}
	public void setExamDate(Date examDate) {
		this.examDate = examDate;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getExaminerName() {
		return examinerName;
	}
	public void setExaminerName(String examinerName) {
		this.examinerName = examinerName;
	}
	public String getEntityCode() {
		return entityCode;
	}
	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}
	public String getDriverId() {
		return driverId;
	}
	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}
}