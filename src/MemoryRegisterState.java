
public class MemoryRegisterState {

	private int value;
	private boolean inOperation;
	private String registerName;
	
	public MemoryRegisterState(String regName,int value, boolean underOperation){
		this.registerName=regName;
		this.inOperation = underOperation;
		this.value = value;
		
	}
	
	public String getRegName() {
		return registerName;
	}
	public int getValue() {
		return value;
	}
	public void setRegName(String regName) {
		this.registerName = regName;
	}

	public boolean isUnderOperation() {
		return inOperation;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public void setUnderOperation(boolean underOperation) {
		this.inOperation = underOperation;
	}
	
}
