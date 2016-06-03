public class Apex_Instruction {
	private String operation;
	
	private String s2;
	private int literal_value;
	private int instruction_Number;
	private boolean takeValue;
	private int result;
	private String stage;
	private String dest;
	private String CurrentInstruction;
	private String s1;
	public Apex_Instruction() {

		this.CurrentInstruction = "";
	}
	

	public boolean isTakeValue() {
		return takeValue;
	}


	public void setTakeValue(boolean takeValue) {
		this.takeValue = takeValue;
	}


	public int getInstrNumber() {
		return instruction_Number;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String opcode) {
		this.operation = opcode;
		this.CurrentInstruction += " " + this.operation;
	}
	public void setInstrNumber(int instrNumber) {
		this.instruction_Number = instrNumber;
		this.CurrentInstruction += "" + this.instruction_Number + "" + ":";
	}
	public void setDestination(String destinationReg) {
		this.dest = destinationReg;
		this.CurrentInstruction += " " + this.dest;
	}
	

	
	public void setSource1(String sourceReg1) {
		this.s1 = sourceReg1;
		if (!operation.equals("BNZ") && !operation.equals("BZ")&&!operation.equals("JUMP")&&!operation.equals("BAL")) {
		this.CurrentInstruction += " " + this.s1;
	}
	}
		
	public String getDestination() {
		return dest;
	}
	public String getSource1() {
		return s1;
	}

	

	public String getSource2() {
		return s2;
	}
	public int getLiteral() {
		return literal_value;
	}
	public void setSource2(String sourceReg2) {
		this.s2 = sourceReg2;
		this.CurrentInstruction += " " + this.s2;
	}
	
	

	public void setLiteral(int literal) {
		this.literal_value = literal;
		this.CurrentInstruction += " " + this.literal_value;
	}

	

	public void setResult(int tempResult) {
		this.result = tempResult;
	}
	public void setStage(String stage) {
		this.stage = stage;
	}
	public String getStage() {
		return stage;
	}
	public int getResult() {
		return result;
	}
	

	@Override
	public String toString() {

		// System.out.println(this.thisInstruction);
		return this.CurrentInstruction;
	}

}
