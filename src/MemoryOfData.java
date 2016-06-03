
public class MemoryOfData {
	private String dest_Address;
	private int value;
	
	public int getData() {
		return value;
	}
	public String getdAddress() {
		return dest_Address;
	}
	public void setData(int data) {
		this.value = data;
	} 
	public void setdAddress(String dAddress) {
		this.dest_Address = dAddress;
	}
}
