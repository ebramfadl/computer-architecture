package engine;

public class RFormat extends Instruction{
    private String R1;
    private String R2;
    private String R3;
    private int shamt;

    public RFormat(String opcode, String r1, String r2, String r3, int shamt) {
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

    public int getShamt() {
        return shamt;
    }

    public void setShamt(int shamt) {
        this.shamt = shamt;
    }

    public Integer execute(RegisterFile registerFile) throws ProgramException {

        int r1 = Register.convertBitsToInt(R1,5);
        int r2 = Register.convertBitsToInt(R2,5);
        int r3 = Register.convertBitsToInt(R3,5);
        int r2Value = registerFile.getAllRegisters()[r2].getValue();
        int r3Value = registerFile.getAllRegisters()[r3].getValue();

        Integer result = null;

        if(this.getOpcode().equals("0000")){

            result = r2Value + r3Value;
        }
        else if(this.getOpcode().equals("0001")){

            result = r2Value - r3Value;
        }
        else if(this.getOpcode().equals("0010")){

            result = r2Value * r3Value;
        }
        else if(this.getOpcode().equals("0101")){

            result = r2Value & r3Value;
        }
        else if(this.getOpcode().equals("1000")){
            result = r2Value << shamt ;
        }
        else if(this.getOpcode().equals("1001")){
            result = r2Value >>> shamt;
        }
//        System.out.println("exec "+r2Value+" "+r3Value+" "+getOpcode()+" = "+result);
        return result;
    }


    public Integer accessMemory(int dataLocation,Memory memory,Integer registerValue) throws ProgramException{
        return dataLocation;
    }

    public void registerWriteBack(Integer value, RegisterFile registerFile) throws ProgramException {
        int destinationRegister = Register.convertBitsToInt(this.getR1(),5);
        registerFile.getAllRegisters()[destinationRegister].setValue(value);
        System.out.println("Updating register R"+destinationRegister+ " to be = "+value);
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
