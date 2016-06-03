
public class DetailsOfCycle {
	private int cycle_number;
	private String instNames;
	private Apex_Instruction instrObjOfCD;
	public String getInstructionNames() {
		return instNames;
	}
	
	public Apex_Instruction getCdInstrObj() {
		return instrObjOfCD;
	}
	public void setCycleNumber(int cycleNumber) {
		this.cycle_number = cycleNumber;
	}
	
	public void setCdInstrObj(Apex_Instruction cdInstrObj) {
		this.instrObjOfCD = cdInstrObj;
	}
	public void setInstructionNames(String instructionNames) {
		this.instNames = instructionNames;
	}
	public int getCycleNumber() {
		return cycle_number;
	}
	
	
	
	
}
