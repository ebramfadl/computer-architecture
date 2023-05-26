package engine;

public class JFormat extends Instruction{

    private int address;

    public JFormat(String opcode, int address) {
        super(opcode);
        this.address = address;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public Integer execute(RegisterFile registerFile) throws ProgramException {
        Register pc = registerFile.getPC();
        String pcBits = pc.getBits().substring(0,4);
        String addressBits = Register.convertIntToBits(address,28);
        int result = Register.convertBitsToInt(pcBits+addressBits,32);

        return result;

    }


    public Integer accessMemory(int memoryLocation,Memory memory, Integer registerValue) throws ProgramException {

        return memoryLocation;
    }

    public void registerWriteBack(Integer value, RegisterFile registerFile) throws ProgramException {
        registerFile.getPC().setValue(value);
        return;
    }

    @Override
    public String toString(){
        String str = "=========================== J Instruction ================================"+ "\n";
        str = str + "Opcode : "+getOpcode()+"\n";
        str = str + "Address : "+address+"\n";
        str = str + "=========================================================================="+ "\n";
        return str;
    }
}
