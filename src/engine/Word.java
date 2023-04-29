package engine;

public class Word {
    private String value;

    public Word(){
        this.value = String.format("%32s",Integer.toBinaryString(0)).replace(' ','0');

    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString(){
        return value.substring(0,8)+" "+value.substring(8,16)+" "+value.substring(16,24)+" "+value.substring(24,32);
    }

    public static void main(String[] args) {
        String binaryString = String.format("%32s",Integer.toBinaryString(16)).replace(' ','0');
        Word w = new Word();
        w.setValue(binaryString);
        System.out.println(w);

    }
}
