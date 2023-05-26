package engine;

public class Word {
    private String value;
    private Integer instructionNumber;

    public Word(){
        this.value = String.format("%32s",Integer.toBinaryString(0)).replace(' ','0');
        instructionNumber = null;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getInstructionNumber() {
        return instructionNumber;
    }

    public void setInstructionNumber(Integer instructionNumber) {
        this.instructionNumber = instructionNumber;
    }

    public String toString(){
        try {
            return value.substring(0,8)+" "+value.substring(8,16)+" "+value.substring(16,24)+" "+value.substring(24) + " = " +Register.convertBitsToInt(value,32);
        } catch (ProgramException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) {
        String binaryString = String.format("%32s",Integer.toBinaryString(16)).replace(' ','0');
        Word w = new Word();
        w.setValue(binaryString);
        System.out.println(w);

    }
}
