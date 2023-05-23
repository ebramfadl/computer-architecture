package engine;

public class Instruction {
    private String opcode;

    public Instruction(String opcode) {
        this.opcode = opcode;
    }

    public String getOpcode() {
        return opcode;
    }

    public void setOpcode(String opcode) {
        this.opcode = opcode;
    }

    public String toString(){
        return "";
    }

    public Integer execute(RegisterFile registerFile) throws ProgramException {
        return null;
    }

    public Integer accessMemory(int memoryLocation, Memory memory, Integer registerValue) throws ProgramException {
        return null;
    }

    public void registerWriteBack(int value, RegisterFile registerFile) throws ProgramException {
        return;
    }


}
