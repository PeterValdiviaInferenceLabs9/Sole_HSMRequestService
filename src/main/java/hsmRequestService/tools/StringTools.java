/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hsmRequestService.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Luis D
 */
public class StringTools {

    public static int getNumberOfRepeatedWords(String filter, String fullText) {
        Pattern pattern = Pattern.compile(filter);
        Matcher matcher = pattern.matcher(fullText);
        long counts = matcher.results().count();;
        String temp = fullText;

        return (int) counts;
    }
}
