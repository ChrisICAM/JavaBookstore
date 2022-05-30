//Since the address has their own data
public class Address {
	private int houseNo;
	private String postcode;
	private String city;
	
	public Address(int houseNo, String postcode, String city) {
		this.houseNo = houseNo;
		this.postcode = postcode;
		this.city = city;
	}

	// getter methods for the attributes
	public String getCity() {
		return city;
	}

	public String getPostcode() {
		return postcode;
	}

	public int getHouseNo() {
		return houseNo;
	}
}
