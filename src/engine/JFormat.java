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

    @Override
    public String toString(){
        String str = "=========================== J Instruction ================================"+ "\n";
        str = str + "Opcode : "+getOpcode()+"\n";
        str = str + "Address : "+address+"\n";
        str = str + "=========================================================================="+ "\n";
        return str;
    }
}
