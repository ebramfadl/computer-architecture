package engine;

public class JFormat extends Instruction{

    private String address;

    public JFormat(String opcode, String address) {
        super(opcode);
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
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
