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
