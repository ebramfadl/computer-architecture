package engine;

import java.util.ArrayList;

public class CPU {

    private Memory memory;
    private RegisterFile registerFile;

    public CPU() throws ProgramException {
        memory = new Memory();
        registerFile = new RegisterFile();
        populateInstructions();
    }

    public static String convertIntToBinary(int number, int bits){
        return String.format("%"+bits+"s",Integer.toBinaryString(number)).replace(' ','0');
    }

    public void populateInstructions() throws ProgramException {
        ArrayList<String[]> instructions = AssemblyReader.readAssemblyFile("program.txt");

        for (int i = 0 ; i < instructions.size() ; i++){
            String[] str = instructions.get(i);
            String result = "";

            if(str[0].equals("ADD") || str[0].equals("SUB") || str[0].equals("MUL") || str[0].equals("AND")){

                switch (str[0]){
                    case "ADD" : result += "0000";break;
                    case "SUB" : result += "0001";break;
                    case "MUL" : result += "0010";break;
                    case "AND" : result += "0101";break;
                }

                result = result + convertIntToBinary(Integer.parseInt(str[1].substring(1)) ,5);
                result = result +convertIntToBinary(Integer.parseInt(str[2].substring(1)) ,5);
                result = result + convertIntToBinary(Integer.parseInt(str[3].substring(1)) ,5);
                result = result + "0000000000000";
            }
            else if(str[0].equals("LSL") || str[0].equals("LSR")){
                switch (str[0]){
                    case "LSL" : result += "1000";break;
                    case  "LSR" : result += "1001";break;
                }

                result += convertIntToBinary(Integer.parseInt(str[1].substring(1)) ,5) + convertIntToBinary(Integer.parseInt(str[2].substring(1)) ,5) + "00000" +Register.convertIntToBits(Integer.parseInt(str[3]),13);
            }

            else if(str[0].equals("MOVI") || str[0].equals("JEQ") || str[0].equals("XORI") || str[0].equals("MOVR") || str[0].equals("MOVM")){
                switch (str[0]){
                    case "MOVI" : result += "0011";break;
                    case "JEQ" : result += "0100";break;
                    case "XORI" : result += "0110";break;
                    case  "MOVR" : result += "1010";break;
                    case  "MOVM" : result += "1011";break;
                }
                if(str[0].equals("MOVI")){
                    result += convertIntToBinary(Integer.parseInt(str[1].substring(1)) ,5) + "00000" + Register.convertIntToBits(Integer.parseInt(str[2]),18);
                }
                else {
                    result += convertIntToBinary(Integer.parseInt(str[1].substring(1)) ,5) + convertIntToBinary(Integer.parseInt(str[2].substring(1)) ,5) + Register.convertIntToBits(Integer.parseInt(str[3]),18);
                }
            }
            else if(str[0].equals("JMP")){
                result += "0111";
                result += Register.convertIntToBits(Integer.parseInt(str[1]),28);
            }

            memory.getContent()[i].setValue(result);

        }
    }

    public void fetch() throws ProgramException {
        Word word = (Word)memory.getContent()[registerFile.getPC().getValue()];
        decode(word);
    }

    public void decode(Word word) throws ProgramException {

        String opcode = word.getValue().substring(0,4);
        String R1 = word.getValue().substring(4,9);
        String R2 = word.getValue().substring(10,15);
        String R3 = word.getValue().substring(16,21);
        String shamt = word.getValue().substring(22);
        String immediate = word.getValue().substring(16);
        String address = word.getValue().substring(4);



        int shamtInt = Register.convertBitsToInt(shamt,13);
        int immediateInt = Register.convertBitsToInt(immediate,18);
        int addressInt = Register.convertBitsToInt(address,28);

        Instruction instruction = null;

        switch (opcode){
            case "0000" : case "0001" : case "0010" : case "0101" : case "1000" : case "1001" :
                instruction = new RFormat(opcode,R1,R2,R3,shamtInt);break;
            case "0011" : case "0100" : case "0110" : case "1010" : case "1011" :
                instruction = new IFormat(opcode,R1,R2,immediateInt);break;
            case "0111" :
                instruction = new JFormat(opcode,addressInt);break;
        }

        execute(instruction);

    }

    public void execute(Instruction instruction) throws ProgramException {
        int result = instruction.execute(registerFile);
        memoryAccess(instruction, result);
    }

    public void memoryAccess(Instruction instruction, int executeResult) throws ProgramException {
        Integer result = instruction.accessMemory(executeResult,memory,1);
        if(result == null){
            writeBack(instruction,executeResult);
        }
        else
            writeBack(instruction,result);

    }

    public void writeBack(Instruction instruction, Integer value) throws ProgramException {
        instruction.registerWriteBack(value,registerFile);
        return;
    }

    public static void main(String[] args) throws ProgramException {
        CPU cpu = new CPU();
        System.out.println(cpu.memory.toString());
    }
}
