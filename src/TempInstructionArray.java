
public class TempInstructionArray {
	private int inst_number;
	
	

	
	public int getInstructionNumber() {
		return inst_number;
	}
	private int inst_stage_value;
	public void setInstructionNumber(int instructionNumber) {
		this.inst_number = instructionNumber;
	}

	public int getInstructionStageNumber() {
		return inst_stage_value;
	}
	private Apex_Instruction inst_OBject_Id;
	public void setInstructionStageNumber(int instructionStageNumber) {
		this.inst_stage_value = instructionStageNumber;
	}

	public Apex_Instruction getItaIDObj() {
		return inst_OBject_Id;
	}

	public void setItaIDObj(Apex_Instruction itaIDObj) {
		this.inst_OBject_Id = itaIDObj;
	}
	
}
