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
}
