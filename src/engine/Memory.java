package engine;

public class Memory {

    private Word[] content = new Word[2048];

    public Memory(){
        for (int i=0 ; i< 2048 ; i++){
            content[i] = new Word();
        }
    }

    public Word[] getContent() {
        return content;
    }

    public String toString(){
        String str = "Instructions ==================================================================="+"\n"+"\n";
        for (int i=0 ; i<= 1023 ; i++){
            str = str + i +" : "+content[i].toString()+"\n";
        }
        str = str + "\n"+"\n"+" Data ========================================================================="+"\n"+"\n";
        for (int i=1024 ; i<2048 ; i++){
            str = str + i +" : "+content[i].toString()+"\n";
        }
        return str;
    }

    public static void main(String[] args) {
        Memory memory = new Memory();
        System.out.println(memory);
    }
}
