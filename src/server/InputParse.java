package server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by marva on 9.12.15.
 */
public class InputParse {

    public static String getResponse(String input) {
        CharSequence cs = " :";
        String[] s;

        if (input.contains(cs)) {
            s = input.split(" \\:");
        }else {
            s = input.split(" ");
        }
        return s[1];
    }

    public static String getChannelName(String input) {
        String regex = "#\\w+";
        return parse(regex,input);
    }

    public static String getCommand(String input) {
        String regex = "^\\w+";
        return parse(regex,input);
    }

     private static String parse(String regex, String input) {

       Pattern p = Pattern.compile(regex);
       Matcher m = p.matcher(input);

        if(m.find()) {
            return m.group(0);
        }
        return null;
    }
}
