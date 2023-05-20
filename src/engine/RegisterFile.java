package engine;

public class RegisterFile {
    private Register[] allRegisters;
    private Register R0 ;
    private Register PC;

    public RegisterFile(){
        allRegisters = new Register[32];
        R0 = new Register("R0",0);
        allRegisters[0] = R0;
        for (int i = 1 ; i<32 ; i++){
            allRegisters[i] = new Register("R"+i,0);
        }
        PC = new Register("PC",0);
    }

    public Register[] getAllRegisters() {
        return allRegisters;
    }

    public Register getR0() {
        return R0;
    }

    public Register getPC() {
        return PC;
    }

    public String toString(){
        String str  = "";
        for (Register r : allRegisters){
            str = str + r.toString()+"\n";
        }
        str = str + "\n" + PC.toString();
        return str;
    }

    public static void main(String[] args) {
        RegisterFile file = new RegisterFile();
        System.out.println(file);
    }


}
