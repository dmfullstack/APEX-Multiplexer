import java.io.*;
import java.util.*;

public class ApexSimulationMain {

	public static String FILENAME = "Input 3.txt";
	private ArrayList<Apex_Instruction> instructions;
	private ArrayList<MemoryRegisterState> regstate = new ArrayList<MemoryRegisterState>();
	private Map<String, Apex_Instruction> Pipeline_Status= new HashMap<String, Apex_Instruction>();
	//private Map<String, RegisterState> archiRegisters = new HashMap<String, RegisterState>();
	int PC;
	int cycles;
	int Halt_Flag=0;
	int X;
	
	public static void main(String args[]) {
		
		
		ApexSimulationMain asm = new ApexSimulationMain();
		//System.out.println("H");
		Scanner scanner=new Scanner(System.in);
		//String input = scanner.next();
		System.out.println("Enter 1: Initialize 2:Simulate 3:Display");
		while (true) {
			
			            String command = scanner.next();
			
			            
			            switch (command) {
			            case "1":
			                asm.initialize();
			                System.out.println("Simulator Init.");
			                break;
			            case "2":
			                asm.readInstructionFromFile(Integer.parseInt(scanner.next()));
			                System.out.println("Simulation Completed.");
			                break;
			            case "3":
			                asm.printState();
			                asm.printArchState();
			                break;
			            }
			            if (command.equals("Close")) {
			                break;
			            }
			        }
			        scanner.close();
			
	}
	
	public ApexSimulationMain(){
		Pipeline_Status.put("Decode", getEmpty());
		Pipeline_Status.put("Fetch", getEmpty());
		Pipeline_Status.put("WriteBack",getEmpty() );
		Pipeline_Status.put("Execute", getEmpty());
		Pipeline_Status.put("Memory", getEmpty());
	}
	// Method to get instructions from the file
	public void initialize() {
		//resetStages();
		PC = 0;
		resetArchReg();
		cycles = 1;
	}
	
	
	public void resetArchReg(){
		for(int i=0;i<8;i++){
			regstate.add(new MemoryRegisterState("R"+i,0,false));
			//archiRegisters.put("R"+i, new RegisterState(0, false));
		}for(int i=8;i<10000;i++){
			regstate.add(new MemoryRegisterState("R"+i,0,false));
			//archiRegisters.put("R"+i, new RegisterState(0, false));
		}
			regstate.add(new MemoryRegisterState("X", 0, false));
	}
	
	public Apex_Instruction getEmpty(){
		
		return null;
	}
	
	private boolean allAreExecuted() {

		if((Pipeline_Status.get("Fetch")==null)&&(Pipeline_Status.get("Decode")==null)&&(Pipeline_Status.get("Execute")==null)&&(Pipeline_Status.get("Memory")==null)&&(Pipeline_Status.get("WriteBack")==null)){
			return true;
		}
		return false;
		
	}

	private void readInstructionFromFile(int executionCycle) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(FILENAME));
			
			
			instructions = new ArrayList<>();
			int instrNum = 0;
			String newLine;
			
			for (int i=0; (newLine = reader.readLine()) != null;i++) {
				String[] instParts = newLine.split(" ");
				Apex_Instruction An_Inst = new Apex_Instruction();
				instrNum++;
				An_Inst.setInstrNumber(instrNum);
				instructions.add(An_Inst);
				An_Inst.setOperation(instParts[0]); 
				An_Inst.setStage("Read");
				if (instParts.length == 1)
					continue;
				 try {
	                    Integer.parseInt(instParts[1]);
	                    An_Inst.setLiteral(Integer.parseInt(instParts[1])); 
	                } catch (NumberFormatException e) {
	                    An_Inst.setDestination(instParts[1]);
	                } catch (NullPointerException e) {
	                    An_Inst.setDestination(instParts[1]);
	                }
					
				if (instParts.length == 2) 
					continue;
				try {
                    Integer.parseInt(instParts[2]);
                    An_Inst.setLiteral(Integer.parseInt(instParts[2])); 
                } catch (NumberFormatException e) {
                	An_Inst.setSource1(instParts[2]);
                } catch (NullPointerException e) {
                	An_Inst.setSource1(instParts[2]);
                }
				
				if (instParts.length == 3) 
					continue;
				try {
                    Integer.parseInt(instParts[3]);
                    An_Inst.setLiteral(Integer.parseInt(instParts[3])); 
                } catch (NumberFormatException e) {
                	An_Inst.setSource2(instParts[3]);
                } catch (NullPointerException e) {
                	An_Inst.setSource2(instParts[3]);
                }
					
			}
			
			reader.close();

			do {
				pushInstructionForward();
				if (Pipeline_Status.get("WriteBack") != null) {
					
					Apex_Instruction instr=Pipeline_Status.get("WriteBack");
					
					
					String Dest_Reg = instr.getDestination();
					MemoryRegisterState registerState = getArchReg(Dest_Reg);
					
					if(registerState !=null){
						registerState.setUnderOperation(false);
					}
					if(instr.isTakeValue()){
						
						registerState.setValue(instr.getResult());
					}
					
					
					
				}
				
				if (Pipeline_Status.get("Memory") != null) {
					//MemoryInstruction(Apex_Stage_Pipeline.get("Memory"));
					Apex_Instruction instruction=Pipeline_Status.get("Memory");
					if(instruction.getDestination()!=null){
						MemoryRegisterState destination = getArchReg(instruction.getDestination());
						destination.setUnderOperation(true);
						switch(instruction.getOperation()){
						case "LOAD":{
							
							int src1 = regAdd(instruction.getSource1());
							int dest = regAdd(instruction.getDestination());
							int lit = instruction.getLiteral();
							int address = src1+lit;
							MemoryRegisterState src_add = regstate.get(address);
							MemoryRegisterState dest_add = regstate.get(dest);
							dest_add.setValue(src_add.getValue());
							
							
							
							
							break;
						}
						case "STORE":{
							
							int src1 = regAdd(instruction.getDestination());
							int dest1 = regAdd(instruction.getSource1());
							int lit = instruction.getLiteral();
							int address = dest1+lit;
							MemoryRegisterState src_add = regstate.get(src1);
							MemoryRegisterState dest_add = regstate.get(address);
							dest_add.setValue(src_add.getValue());
							System.out.println(dest_add.getValue());
							break;
						}
						}
					}
					
				}
				// Execute
				if (Pipeline_Status.get("Execute") != null) {
					//ExecuteInstruction(Apex_Stage_Pipeline.get("Execute"));
					Apex_Instruction instruct =Pipeline_Status.get("Execute");
					if(!instruct.getOperation().equals("STORE")){
						
					
					if (instruct.getDestination() != null) {
						MemoryRegisterState destination = getArchReg(instruct.getDestination());
						destination.setUnderOperation(true);
					}
					switch (instruct.getOperation()) {
					case "ADD": {
						MemoryRegisterState source1 = getArchReg(instruct.getSource1());
						MemoryRegisterState source2 = getArchReg(instruct.getSource2());
						instruct.setResult(source1.getValue() + source2.getValue());
						instruct.setTakeValue(true);
						break;
					}
					case "MUL": {
						MemoryRegisterState source1 = getArchReg(instruct.getSource1());
						MemoryRegisterState source2 = getArchReg(instruct.getSource2());
						instruct.setResult(source1.getValue() * source2.getValue());
						instruct.setTakeValue(true);
						break;
					}
					
					case "SUB": {
						MemoryRegisterState source1 = getArchReg(instruct.getSource1());
						MemoryRegisterState source2 = getArchReg(instruct.getSource2());
						instruct.setResult(source1.getValue() - source2.getValue());
						instruct.setTakeValue(true);
						break;
					}
					
					case "MOVC": {
						instruct.setResult(instruct.getLiteral());
						instruct.setTakeValue(true);
						break;
					}
					case "MOV":{
						MemoryRegisterState source1 = getArchReg(instruct.getSource1());
						MemoryRegisterState dest = getArchReg(instruct.getDestination());
						instruct.setResult(source1.getValue());
						instruct.setTakeValue(true);
						break;
					}
					case "LOAD": {
						break;
					}
					case "STORE": {
						break;
					}
					case "AND": {
						MemoryRegisterState source1 = getArchReg(instruct.getSource1());
						MemoryRegisterState source2 = getArchReg(instruct.getSource2());
						instruct.setResult((source1.getValue())&(source2.getValue()));
						instruct.setTakeValue(true);
						break;
					}
					case "OR": {
						MemoryRegisterState source1 = getArchReg(instruct.getSource1());
						MemoryRegisterState source2 = getArchReg(instruct.getSource2());
						instruct.setResult((source1.getValue())|(source2.getValue()));
						instruct.setTakeValue(true);
						break;
					}
					case "EX-OR": {
						
						MemoryRegisterState source1 = getArchReg(instruct.getSource1());
						MemoryRegisterState source2 = getArchReg(instruct.getSource2());
						instruct.setResult((source1.getValue())^(source2.getValue()));
						instruct.setTakeValue(true);
						break;
					}
					case "BZ": {
						int a= instruct.getLiteral();
						MemoryRegisterState dest = getArchReg(instruct.getSource1());
						if(dest.getValue()==0){
							
							PC= a+(PC-3);
							//System.out.println("Working:"+PC);
							Pipeline_Status.put("Decode", getEmpty());
							Pipeline_Status.put("Fetch", getEmpty());
						}
						
						break;
					}
					case "BNZ": {
						int a= instruct.getLiteral();
						MemoryRegisterState dest = getArchReg(instruct.getSource1());
						if(dest.getValue()!=0){
							
							PC= a+(PC-3);
							System.out.println("Working:"+PC);
							Pipeline_Status.put("Decode", getEmpty());
							Pipeline_Status.put("Fetch", getEmpty());
						}
						
						break;
					}
					case "JUMP": {
						MemoryRegisterState source1;
					
						if(instruct.getDestination().equals("X")){
							source1 =new MemoryRegisterState("X", X,false);
						}else{
						 source1 = getArchReg(instruct.getDestination());
						}
						//int i = source1.getValue();
						//System.out.println(i);
						int lit = instruct.getLiteral();
						PC = (source1.getValue()+lit)-20000;
						 //System.out.println(PC);
						 //System.out.println(jmp);
						 Pipeline_Status.put("Decode", getEmpty());
						 Pipeline_Status.put("Fetch", getEmpty());
						break;
					}
					case "BAL": {
						X=20000+PC-2;
						//System.out.println(X);
						MemoryRegisterState source1 = getArchReg(instruct.getDestination());
						int lit = instruct.getLiteral();
						 PC = (source1.getValue()+lit)-20000;
						 //System.out.println(PC);
						 //System.out.println(jmp);
						 Pipeline_Status.put("Decode", getEmpty());
						 Pipeline_Status.put("Fetch", getEmpty());
						//RegisterState src1 = regstate.get(regAdd(instruct.getSource1()));
						//PC=src1+a;
						
						
						break;
					}
					case "HALT": {
						Halt_Flag=1;
						Pipeline_Status.put("Decode", getEmpty());
						Pipeline_Status.put("Fetch", getEmpty());
						break;
					}
					}
					}
				}
				// Decode
				if (Pipeline_Status.get("Decode") != null) {
					//DecodeInstruction(Apex_Stage_Pipeline.get("Decode"));
					Apex_Instruction inst=Pipeline_Status.get("Decode");
					
					String Source_One = inst.getSource1();
					if(inst.getOperation().equals("STORE")){
						Source_One=inst.getDestination();
					}
					
					if (Source_One != null) {
							if (!getArchReg(Source_One).isUnderOperation()) {

							String Source_Two = inst.getSource2();
							if (Source_Two != null) {
								if (!getArchReg(Source_Two).isUnderOperation()) {

									inst.setStage("ready");
								} else {

									inst.setStage("stalled");
								}

								
							} else {
								inst.setStage("ready");
							}
						} 
							else {inst.setStage("stalled");}

						
					} else {
						inst.setStage("ready");
					}
				}
				printState();
				cycles++;
			} while (executionCycle!=this.cycles);
			printArchState();

		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
private void printArchState(){
	for(int i=0; i<8;i++){
		System.out.println("R"+i+"="+regstate.get(i).getValue());
	
	}	System.out.println("X="+X);
	System.out.println("Memory Address");
	for(int g=8; g<108;g++){
		System.out.println("memory Address"+g+"="+regstate.get(g).getValue());
	
	}
}	
private int regAdd(String source){
	int s=0;
	switch(source.toString()){
	case "R0":{
		s=0;
		break;
	}
	case "R1":{
		s=1;
		break;
	}
	case "R2":{
		s=2;
		break;
	}
	case "R3":{
		s=3;
		break;
	}
	case "R4":{
		s=4;
		break;
	}
	case "R5":{
		s=5;
		break;
	}
	case "R6":{
		s=6;
		break;
	}
	case "R7":{
		s=7;
		break;
			}
	case "X":{
		s=10000;
		break;
			}
		
	}
	return s;
	
	}
	private void printState() {
		System.out.println("Cycle No: " + this.cycles);
		Apex_Instruction instrInFetch = this.Pipeline_Status.get("Fetch");
		System.out.println((instrInFetch == null ? "Empty": instrInFetch)+"     Fetch-Stage: "+(instrInFetch == null ? "No Operation" : instrInFetch.getStage()));
		
		Apex_Instruction instrInDecode = this.Pipeline_Status.get("Decode");
		System.out.println((instrInDecode == null ? "Empty": instrInDecode)+"      Decode-Stage: "+(instrInDecode == null ? "No Operation" : instrInDecode.getStage()));
		
		Apex_Instruction instrInExecute = this.Pipeline_Status.get("Execute");
		System.out.println((instrInExecute == null ? "Empty": instrInExecute)+"     Execution-Stage: "+(instrInExecute == null ? "No Operation" : instrInExecute.getStage()));
		
		Apex_Instruction instrInMemory = this.Pipeline_Status.get("Memory");
		System.out.println((instrInMemory == null ? "Empty": instrInMemory)+"     Memory-Stage: "+(instrInMemory == null ? "No Operation" : instrInMemory.getStage()));
		
		Apex_Instruction instrInWriteBack = this.Pipeline_Status.get("WriteBack");
		System.out.println((instrInWriteBack == null ? "Empty" : instrInWriteBack)+"     Writeback-Stage: "+(instrInWriteBack == null ? "No Operation" : instrInWriteBack.getStage()));
		System.out.println();
		
	}
	private MemoryRegisterState getArchReg(String destRegister){
		
		for(int i =0; i<regstate.size();i++){
			if(regstate.get(i).getRegName().equals(destRegister)){
				return regstate.get(i);
			}
		}
		return null;
	}
	
	
	public void pushWriteBack(){
			if (Pipeline_Status.get("WriteBack") != null) { // Retire
			Apex_Instruction anInstruction = Pipeline_Status.get("WriteBack");
			Pipeline_Status.put("WriteBack", null);
			anInstruction.setStage("completed");
			
			}
			if (Pipeline_Status.get("WriteBack") == null) {
			Apex_Instruction anInstruction = Pipeline_Status.get("Memory");
			if (anInstruction != null) {
			Pipeline_Status.put("Memory", null);
			anInstruction.setStage("WriteBack");
			Pipeline_Status.put("WriteBack", anInstruction);
			}
		}
	}
	public void pushMemory(){
		if (Pipeline_Status.get("Memory") == null) {

			Apex_Instruction anInstruction = Pipeline_Status.get("Execute");
			if (anInstruction != null) {
				Pipeline_Status.put("Execute", null);
				Pipeline_Status.put("Memory", anInstruction);
				anInstruction.setStage("Memory");
				
			}
		}
	}
	public void pushExecute(){
		if (Pipeline_Status.get("Execute") == null) {

			Apex_Instruction anInstruction = Pipeline_Status.get("Decode");
			if (anInstruction != null) {

				if ( "ready"==anInstruction.getStage()) {
					Pipeline_Status.put("Decode", null);
					anInstruction.setStage("Execute");
					Pipeline_Status.put("Execute", anInstruction);
					
				}
			}

		}
	}
	public void pushDecode(){
		if ( null ==Pipeline_Status.get("Decode")) {

			Apex_Instruction anInstruction = Pipeline_Status.get("Fetch");
			if (anInstruction != null) {
				Pipeline_Status.put("Fetch", null);
				anInstruction.setStage("Decode");
				Pipeline_Status.put("Decode", anInstruction);
				
			}
		}
	}
	public void pushFetch(){
		if ( null==Pipeline_Status.get("Fetch")&& Halt_Flag==0) {

			if (this.PC < instructions.size()) {
				
				Apex_Instruction inst1 = instructions.get(this.PC);
				Pipeline_Status.put("Fetch", inst1);
				
				inst1.setStage("Fetch");
				
				if(inst1.getOperation().equals("BZ")){
					
					Apex_Instruction inst2=instructions.get(this.PC-1);
					inst1.setSource1(inst2.getDestination());
					
					
					
				}
				if(inst1.getOperation().equals("JUMP")){
					inst1.setSource1(inst1.getDestination());
				}
				if(inst1.getOperation().equals("BNZ")){
					
					Apex_Instruction inst2=instructions.get(this.PC-1);
					inst1.setSource1(inst2.getDestination());
					
					
					
				}
				if(inst1.getOperation().equals("BAL")){
					inst1.setSource1(inst1.getDestination());
				}
				this.PC++;
				//this.PC++;
			}

		}
	}
	private void pushInstructionForward() {

		pushWriteBack();
		pushMemory();
		pushExecute();
		pushDecode();
		pushFetch();
		
		
	}


}
