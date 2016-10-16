/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package input;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author Terrell
 */
public class DialogReader {
    
    public static String getDialog(String filePath) {
        try {
            Scanner s = new Scanner(new File(filePath));
            String text = "";
            while(s.hasNextLine()) {
                text += s.nextLine();
                if(s.hasNextLine()) {
                    text += "\n";
                }
            }
            return text;
        } catch (FileNotFoundException ex) {
            return "";
        }
    }
}
