package engine;


public class IFormat extends Instruction{
    private String R1;
    private  String R2;
    private int immediate;

    public IFormat(String opcode, String r1, String r2, int immediate) {
        super(opcode);
        R1 = r1;
        R2 = r2;
        this.immediate = immediate;
    }

    public String getR1() {
        return R1;
    }

    public void setR1(String r1) {
        R1 = r1;
    }

    public String getR2() {
        return R2;
    }

    public void setR2(String r2) {
        R2 = r2;
    }

    public int getImmediate() {
        return immediate;
    }

    public void setImmediate(int immediate) {
        this.immediate = immediate;
    }

    public Integer execute(RegisterFile registerFile) throws ProgramException {

        int r1 = Register.convertBitsToInt(R1,5);
        int r2 = Register.convertBitsToInt(R2,5);

        int r1Value = registerFile.getAllRegisters()[r1].getValue();
        int r2Value = registerFile.getAllRegisters()[r2].getValue();

        if (getOpcode().equals("0011")){
            return immediate;
        }
        else if(getOpcode().equals("0100")){
            if(r1Value == r2Value)
                return registerFile.getPC().getValue()+1+immediate;
            else
                return null;

        }
        else if(getOpcode().equals("0110")){
            return r2Value ^ immediate;
        }
        else if(getOpcode().equals("1010")){
            return r2Value + immediate;
        }
        else if(getOpcode().equals("1011")){
            return r2Value + immediate;
        }
        return null;
    }

    public Integer accessMemory(int memoryLocation, Memory memory, Integer registerValue) throws ProgramException {

        if(getOpcode().equals("1010")){
            int data = Register.convertBitsToInt(memory.getContent()[memoryLocation].getValue(),32);
            System.out.println("Loading from memory["+memoryLocation+"] : "+data);
            return data;
        }
        else if(getOpcode().equals("1011")){

            String registerBits = Register.convertIntToBits(registerValue,32);
            System.out.println("Storing in memory["+memoryLocation+"]"+ " : "+registerValue);
            memory.getContent()[memoryLocation].setValue(registerBits);
            return null;
        }
        return memoryLocation;

    }

    public void registerWriteBack(Integer value, RegisterFile registerFile) throws ProgramException {
        if (getOpcode().equals("0011") || getOpcode().equals("0110") || getOpcode().equals("1010")){
            int destinationRegister = Register.convertBitsToInt(this.getR1(),5);
            registerFile.getAllRegisters()[destinationRegister].setValue(value);
            System.out.println("Updating register R"+destinationRegister+ " to be = "+value);

        }

        else if(getOpcode().equals("0100")){
            System.out.println("Updating PC to be "+value);
            registerFile.getPC().setValue(value);
        }

        return;
    }

    @Override
    public String toString(){
        String str = "=========================== I Instruction ================================"+ "\n";
        str = str + "Opcode : "+getOpcode()+"\n";
        str = str + "R1 : "+R1+"\n";
        str = str + "R2 : "+R2+"\n";
        str = str + "Immediate : "+immediate+"\n";
        str = str + "=========================================================================="+ "\n";
        return str;
    }
}
