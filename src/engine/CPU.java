package engine;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class CPU {

    private Memory memory;
    private RegisterFile registerFile;

    private Hashtable<Integer, String> allInstructions;
    private Hashtable<Integer, Word> fetchResult;
    private Hashtable<Integer, Instruction> decodeResult;
    private Hashtable<Integer, Integer> executeResult;
    private Hashtable<Integer, Integer> memoryResult;
    private int numberOfInstructions;
    private int completedInstructions;
    private int numberOfFetchedInstructions;

    private int cycleNumber;

    public CPU() throws ProgramException {
        memory = new Memory();
        registerFile = new RegisterFile();
        fetchResult = new Hashtable<>();
        decodeResult = new Hashtable<>();
        executeResult = new Hashtable<>();
        memoryResult = new Hashtable<>();
        allInstructions = new Hashtable<>();

        cycleNumber = 1;
        numberOfInstructions = 0;
        completedInstructions = 0;
        numberOfFetchedInstructions = 0;
        populateInstructions();
    }

    public static int convertBinaryToInt(String bits){
        return Integer.parseInt(bits,2);
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

            numberOfInstructions++;
            allInstructions.put(numberOfInstructions,instructions.get(i).toString());
            memory.getContent()[i].setValue(result);
            memory.getContent()[i].setInstructionNumber(numberOfInstructions);

        }
    }

    public Word fetch() throws ProgramException {
        Word word = (Word)memory.getContent()[registerFile.getPC().getValue()];
        registerFile.getPC().setValue(registerFile.getPC().getValue()+1);
        return word;
    }

    public Instruction decode(Word word) throws ProgramException {

        String opcode = word.getValue().substring(0,4);
        String R1 = word.getValue().substring(4,9);
        String R2 = word.getValue().substring(9,14);
        String R3 = word.getValue().substring(14,19);
        String shamt = word.getValue().substring(20);
        String immediate = word.getValue().substring(14);
        String address = word.getValue().substring(4);

        int shamtInt = convertBinaryToInt(shamt);
        int immediateInt = Register.convertBitsToInt(immediate,18);
        int addressInt = convertBinaryToInt(address);

        Instruction instruction = null;

        switch (opcode){
            case "0000" : case "0001" : case "0010" : case "0101" : case "1000" : case "1001" :
                instruction = new RFormat(opcode,R1,R2,R3,shamtInt);break;
            case "0011" : case "0100" : case "0110" : case "1010" : case "1011" :
                instruction = new IFormat(opcode,R1,R2,immediateInt);break;
            case "0111" :
                instruction = new JFormat(opcode,addressInt);break;
        }

        return instruction;

    }

    public Integer execute(Instruction instruction) throws ProgramException {
        int result = instruction.execute(registerFile);
//        memoryAccess(instruction, result);
        return result;
    }

    public Integer memoryAccess(Instruction instruction, int executeResult) throws ProgramException {
        int registerIndex = -1;
        int registerValue = 0;
        if(instruction instanceof RFormat || instruction instanceof IFormat) {
            if(instruction instanceof RFormat)
                registerIndex = convertBinaryToInt(((RFormat) instruction).getR1());
            else
                registerIndex = convertBinaryToInt(((IFormat) instruction).getR1());

            registerValue = registerFile.getAllRegisters()[registerIndex].getValue();
        }
        Integer result = instruction.accessMemory(executeResult,memory,registerValue);
//        if(result == null){
//            writeBack(instruction,executeResult);
//        }
//        else
//            writeBack(instruction,result);
        return result;

    }

    public void writeBack(Instruction instruction, Integer value) throws ProgramException {
        instruction.registerWriteBack(value,registerFile);
//        registerFile.getPC().setValue(registerFile.getPC().getValue()+1);
        return;
    }


    public void startProgram() throws ProgramException {
        ArrayList<Integer> fetchedInstructions = new ArrayList<>();
        ArrayList<Integer> decodedInstructions = new ArrayList<>();
        ArrayList<Integer> executedInstructions = new ArrayList<>();
        ArrayList<Integer> memoryInstructions = new ArrayList<>();

        while (completedInstructions < numberOfInstructions){
            run(fetchedInstructions,decodedInstructions,executedInstructions,memoryInstructions);
        }
    }

    public void run(ArrayList<Integer> fetchedInstructions, ArrayList<Integer> decodedInstructions, ArrayList<Integer> executedInstructions, ArrayList<Integer> memoryInstructions ) throws ProgramException{
        System.out.println("Cycle number "+cycleNumber);
        Integer decodingInstructionNumber = null;
        Integer executingInstructionNumber = null;
        Integer memoryInstructionNumber = null;

        if (cycleNumber%2 == 1 && numberOfFetchedInstructions < this.numberOfInstructions){
            Word word =fetch();
            fetchResult.put(word.getInstructionNumber(),word);
            fetchedInstructions.add(word.getInstructionNumber());
            System.out.println("Fetching instruction "+word.getInstructionNumber());
            if (fetchedInstructions.size() > 1){
                decodingInstructionNumber = fetchedInstructions.get(fetchedInstructions.size()-2);
            }
            numberOfFetchedInstructions++;
        }

        if(cycleNumber % 2 == 0 ){
            decodingInstructionNumber = fetchedInstructions.get(fetchedInstructions.size()-1);

        }
        if(cycleNumber % 2 == 1 && cycleNumber == numberOfInstructions+numberOfInstructions+1 ){
            decodingInstructionNumber = fetchedInstructions.get(fetchedInstructions.size()-1);
        }

        if(decodingInstructionNumber != null && cycleNumber <= numberOfInstructions+numberOfInstructions+1) {
            Word toBeDecoded = fetchResult.get(decodingInstructionNumber);
            System.out.println("Decoding instruction " + decodingInstructionNumber);
            Instruction decodeOutputInstruction = decode(toBeDecoded);
            if(! decodedInstructions.contains(toBeDecoded.getInstructionNumber()))
                decodedInstructions.add(toBeDecoded.getInstructionNumber());
            decodeResult.put(toBeDecoded.getInstructionNumber(), decodeOutputInstruction);
        }


        if( decodedInstructions.size() > 1 ){
            if(cycleNumber > numberOfInstructions+numberOfInstructions+1) {
                executingInstructionNumber = decodedInstructions.get(decodedInstructions.size() - 1);
            }
            else
                executingInstructionNumber = decodedInstructions.get(decodedInstructions.size() - 2);

        }

        if(executingInstructionNumber != null && cycleNumber >= 4 && cycleNumber <= numberOfInstructions+numberOfInstructions+3) {
            Instruction toBeExecuted = decodeResult.get(executingInstructionNumber);
            System.out.println(toBeExecuted);
            System.out.println("Executing instruction " + executingInstructionNumber);
            Integer executeOutput = execute(toBeExecuted);
            System.out.println("Result inst "+executingInstructionNumber+" = "+executeOutput);
            if(! executedInstructions.contains(executingInstructionNumber))
                executedInstructions.add(executingInstructionNumber);
            if(executeOutput == null)
                executeResult.put(executingInstructionNumber, -1);
            else
                executeResult.put(executingInstructionNumber, executeOutput);
        }

        if(executedInstructions.size() >1 ){
            if(cycleNumber > numberOfInstructions+numberOfInstructions+3)
                memoryInstructionNumber = executedInstructions.get(executedInstructions.size()-1);
            else
                memoryInstructionNumber = executedInstructions.get(executedInstructions.size()-2);
        }

        if (cycleNumber % 2 == 0 && cycleNumber >= 6 && cycleNumber <= (numberOfInstructions*2)+4){

            if(memoryInstructionNumber != null) {
                Integer resultBeInMemory = executeResult.get(memoryInstructionNumber);
                Instruction instructionToBeInMemory = decodeResult.get(memoryInstructionNumber);
                System.out.println("Accessing memory from instruction "+memoryInstructionNumber);
                Integer memoryOutput = memoryAccess(instructionToBeInMemory,resultBeInMemory);
//                System.out.println("memory output "+memoryOutput);
                if(!memoryInstructions.contains(memoryInstructionNumber))
                    memoryInstructions.add(memoryInstructionNumber);
                if(memoryOutput == null)
                    memoryResult.put(memoryInstructionNumber,-1);
                else
                    memoryResult.put(memoryInstructionNumber,memoryOutput);
            }
        }

        if(cycleNumber % 2 == 1 && cycleNumber >= 7 && cycleNumber <= (numberOfInstructions*2)+5){
            if(completedInstructions >= numberOfInstructions)
                return;
            Integer registerInstructionNumber = memoryInstructions.get(memoryResult.size()-1);
            if(registerInstructionNumber != null) {
                Integer resultFromMemory = memoryResult.get(registerInstructionNumber);
                Instruction instructionToBeInMemory = decodeResult.get(registerInstructionNumber);

                System.out.println("Write back from instruction " + registerInstructionNumber);
                System.out.println("Result mem "+resultFromMemory);
                writeBack(instructionToBeInMemory, resultFromMemory);

                completedInstructions++;
            }
        }
        cycleNumber++;
    }
    public static void main(String[] args) throws ProgramException {
        CPU cpu = new CPU();
//        for (int i = 0 ; i < 3 ; i++){
//            cpu.fetch();
//        }
        cpu.startProgram();
//        System.out.println(cpu.memory.toString());
        System.out.println(cpu.registerFile);
    }
}
