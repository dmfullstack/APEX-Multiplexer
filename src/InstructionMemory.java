
public class InstructionMemory {
	private Apex_Instruction inst_Details;
	private String inst_Address;
	public void setiDetails(Apex_Instruction iDetails) {
		this.inst_Details = iDetails;
	}
	public Apex_Instruction getiDetails() {
		return inst_Details;
	}
	
	
	public void setiAddress(String iAddress) {
		this.inst_Address = iAddress;
	}
	public String getiAddress() {
		return inst_Address;
	}
	
	
}
