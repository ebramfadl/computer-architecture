package engine;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class AssemblyReader {

    public static ArrayList<String[]> readAssemblyFile(String path){
        ArrayList<String[]> rows = new ArrayList<String[]>();
        File file = new File(path);
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()){
                String line = scanner.nextLine();
                String[] arr = line.split(" ");
                rows.add(arr);
            }
            scanner.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return rows;
    }

    public static void displayAllInstructions(ArrayList<String[]> allInstructions){
        for (String[] arr : allInstructions){
            for (int i = 0; i<arr.length ; i++){
                if( i == arr.length-1){
                    System.out.print(arr[i]);
                }
                else {
                    System.out.print(arr[i]+" , ");
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        ArrayList<String[]> instructions =  readAssemblyFile("program.txt");
        displayAllInstructions(instructions);
    }
}
