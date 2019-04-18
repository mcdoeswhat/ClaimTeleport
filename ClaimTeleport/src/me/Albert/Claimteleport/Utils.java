package me.Albert.Claimteleport;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
	public static boolean isNumeric(String input){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(input);
        if( !isNum.matches() ){
            return false;
        }
        return true;
	}
}
