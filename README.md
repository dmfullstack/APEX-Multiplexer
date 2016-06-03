APEX Simulator
=======================

Design Document Of Apex 5 Stage Pipelining

Run Code using this command:
make

Five Stages of Pieplining:
Fetch
Decode
Execute
Memory
WriteBack

Number of Architectural Registers:R0,R1,R2,R3,R4,R5,R6,R7 which is in linear array from location of 0 to 7
Rest of all as Memory Address from 8 to 9999
Reister "X"

Use of Datastructure:
Map
ArrayList
getter Setter Methods of Java

How it Works?
First Stage: Fetch: In this stage instruction get Opcode, Destination and Source registers line by line. And it will check either next stage Decode is empty or not. If empty then set as "Ready" and it will push the instruction to the next stage decode.
If the Decode stage is on "Stalled" than pushForward() will not call.

Decode: It will check that if there is any dependency of previous instruction or not using InstructionDependency() method.If there is dependency than stage will be stalled. Other wise "Ready" and go into the execution stage.

Execute: Operation Perform correspondingly according to Opcode of instruction and pushForwar() call will send instruction to the next Memory stage.

WriteBack: Operation Will set Architectural Result to the corresponding register.
