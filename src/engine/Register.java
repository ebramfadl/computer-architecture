package engine;

public class Register {

    private String name;
    private String bits;
    private int value;

    public Register(String name, int value){
        this.name = name;
        this.value = value;
        bits = String.format("%"+32+"s",Integer.toBinaryString(0)).replace(' ','0');
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getBits() {
        return bits;
    }

    public void setBits(String bits) {
        this.bits = bits;
    }

    public static String convertIntToBits(int number, int bits) throws ProgramException {

        int maxRange = (1 << bits-1) - 1; // max
        int minRange = -(1 << bits-1); // min

        if(number > maxRange || number < minRange){
            throw new ProgramException("Number is out of range for "+bits+" bits");
        }

        StringBuilder sb = new StringBuilder();

        if(number < 0){
            sb.append("1");
            number = ((1 << bits-1) -1) & number; // two's complement
        }
        else {
            sb.append("0");
        }

        String binary = Integer.toBinaryString(number);
        int leadingZeroes = bits - 1 -binary.length();

        for (int i = 0 ; i < leadingZeroes ; i++){
            sb.append("0");
        }
        sb.append(binary);
        return sb.toString();

    }

    public static int convertBitsToInt(String bitsString, int bits) throws ProgramException {

        if(bitsString.length() != bits){
            throw new ProgramException("Invalid bit string length, expected "+bits+" bits");
        }

        char signBit = bitsString.charAt(0);

        String magnitudeBits = bitsString.substring(1);
        int magnitude = Integer.parseInt(magnitudeBits,2);
        int converted ;

        if(signBit == '1'){
            converted = -((1 << bits - 1) - magnitude);
        }
        else {
            converted = magnitude;
        }

        return converted;
    }

    public String toString(){
        return name+" : "+bits.substring(0,8)+" "+bits.substring(8,16)+" "+bits.substring(16,24)+" "+bits.substring(24,32)+" "+value;
    }


    public static void main(String[] args) throws ProgramException {
//        System.out.println(convertIntToBits(7 , 10));
//        System.out.println(convertBitsToInt("0000000111" , 10));

    }
}
