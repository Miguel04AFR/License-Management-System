package model;

public class Center {
    private String centerCode;
    private String centerName;
    private String postalAddress;
    private String phoneNumber;
    private String generalDirector;
    private String hrManager;
    private String accountingManager;
    private String unionSecretary;
    private byte[] logo;
    private String contactEmail;
	public String getCenterCode() {
		return centerCode;
	}
	public void setCenterCode(String centerCode) {
		this.centerCode = centerCode;
	}
	public String getCenterName() {
		return centerName;
	}
	public void setCenterName(String centerName) {
		this.centerName = centerName;
	}
	public String getPostalAddress() {
		return postalAddress;
	}
	public void setPostalAddress(String postalAddress) {
		this.postalAddress = postalAddress;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getGeneralDirector() {
		return generalDirector;
	}
	public void setGeneralDirector(String generalDirector) {
		this.generalDirector = generalDirector;
	}
	public String getHrManager() {
		return hrManager;
	}
	public void setHrManager(String hrManager) {
		this.hrManager = hrManager;
	}
	public String getAccountingManager() {
		return accountingManager;
	}
	public void setAccountingManager(String accountingManager) {
		this.accountingManager = accountingManager;
	}
	public String getUnionSecretary() {
		return unionSecretary;
	}
	public void setUnionSecretary(String unionSecretary) {
		this.unionSecretary = unionSecretary;
	}
	public byte[] getLogo() {
		return logo;
	}
	public void setLogo(byte[] logo) {
		this.logo = logo;
	}
	public String getContactEmail() {
		return contactEmail;
	}
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
}