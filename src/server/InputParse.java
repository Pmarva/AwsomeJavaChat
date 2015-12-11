package server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by marva on 9.12.15.
 * Class for parsing IRC client protocol.
 */
public class InputParse {

    /**
     * Get response from client response.
     * @param input A string containing user input. // NICK Mike
     * @return response  //Mike
     */
    public static String getResponse(String input) {
        CharSequence cs = " :";
        String[] s;

        if (input.contains(cs)) {
            s = input.split(" \\:");
        } else {
            s = input.split(" ");
        }
        return s[1];
    }

    /**
     * Get channel name from client response.
     * @param input - is String //PRIVMSG #marva :Hello people
     * @return String //#marva
     */
    public static String getChannelName(String input) {
        String regex = "#\\w+";
        return parse(regex, input);
    }

    /**
     * Get command from client response.
     * @param input is Strin // PRIVMSG #marva :Hello people
     * @return String PRIVMSG
     */
    public static String getCommand(String input) {
        String regex = "^\\w+";
        return parse(regex, input);
    }

    /**
     * Get command from client response.
     * @param input is Strin // PRIVMSG #marva :Hello people
     * @return String PRIVMSG
     */
    private static String parse(String regex, String input) {

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(input);

        if (m.find()) {
            return m.group(0);
        }
        return null;
    }
}
