package engine;

public class RFormat extends Instruction{
    private String R1;
    private String R2;
    private String R3;
    private String shamt;

    public RFormat(String opcode, String r1, String r2, String r3, String shamt) {
        super(opcode);
        R1 = r1;
        R2 = r2;
        R3 = r3;
        this.shamt = shamt;
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

    public String getR3() {
        return R3;
    }

    public void setR3(String r3) {
        R3 = r3;
    }

    public String getShamt() {
        return shamt;
    }

    public void setShamt(String shamt) {
        this.shamt = shamt;
    }

    @Override
    public String toString(){
        String str = "=========================== R Instruction ================================"+ "\n";
        str = str + "Opcode : "+getOpcode()+"\n";
        str = str + "R1 : "+R1+"\n";
        str = str + "R2 : "+R2+"\n";
        str = str + "R3 : "+R3+"\n";
        str = str + "Shift amount : "+shamt+"\n";
        str = str + "=========================================================================="+ "\n";
        return str;
    }
}
