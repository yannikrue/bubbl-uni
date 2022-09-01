package ch.unibas.dmi.dbis.cs108.util;

import ch.unibas.dmi.dbis.cs108.rank.RankEntry;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

/**
 * The Util class provides a number of utility functions to be used in multiple classes.
 */
public class Util {

    private static final String DEFAULT_DELIM = "|";

    /**
     * Splits the @param input into the first four Characters corresponding to the Command (e.g. SMBT for SUBMIT) and
     * the remainder corresponding to the Parameter. Returns a String-array of length two with Command being the first
     * and Parameter being the second value.
     */
    public static String[] splitCommandParameter(String input) {
        String[] strArray = new String[2];
        strArray[0] = input.substring(0, 4);    // endIndex is excluded
        strArray[1] = input.substring(4, input.length());
        return strArray;
    }

    /**
     * Splits the @param parameter at every occurrence of the DEFAULT_DELIMITER "|" and @return s the resulting
     * substrings as an array list.
     */
    public static ArrayList<String> splitParameter(String parameter) {
        StringTokenizer tokenizer = new StringTokenizer(parameter, DEFAULT_DELIM);
        ArrayList<String> list = new ArrayList<>();
        while (tokenizer.hasMoreTokens()) {
            list.add(tokenizer.nextToken());
        }
        return list;
    }

    /**
     * Splits the @param parameter at the first (non-trivial) occurrence of the DEFAULT_DELIMITER "|" and @return s
     * the two resulting substrings as an array string.
     */
    public static String[] splitParameterAtFirstDelim(String parameter) {
        String[] strArray = new String[2];
        int delimPos = parameter.indexOf(DEFAULT_DELIM, 1);
        strArray[0] = parameter.substring(1, delimPos);
        strArray[1] = parameter.substring(delimPos + 1, parameter.length());
        return strArray;
    }

    /**
     * Splits the @param parameter at the first (non-trivial) @param nbOfOccurrences of the delimiter @delim. For example,
     * "|Andrew|James|How are you?" with the delimiter "|" is split into "Andrew", "James" and "How are you?".
     * The substrings are @return ed as a string-array.
     */
    public static String[] splitParameterAtFirstN(String parameter, String delim, int nbOfOccurrences) {
        if (parameter.indexOf(delim) == 0) {    // if delim is the first character, then remove it
            parameter = parameter.substring(1);
        }
        // https://stackoverflow.com/questions/10796160/splitting-a-java-string-by-the-pipe-symbol-using-split
        if (delim.equals("|")) {
            delim = "\\|";
        }
        String[] strArray = parameter.split(delim, nbOfOccurrences);
        return strArray;
    }

    /**
     * Splits the @param parameter at the first (non-trivial) @param nbOfOccurrences of the default delimiter "|".
     * The substrings are @return ed as a string-array.
     */
    public static String[] splitParameterAtFirstN(String parameter, int nbOfOccurrences) {
        return splitParameterAtFirstN(parameter, DEFAULT_DELIM, nbOfOccurrences);
    }

    /**
     * Splits the @param parameter of the form |playerName1|size1|playerName2|size| ... into an array list of
     * RankEntries which each consist of a player name and the size of the corresponding player character at the end
     * of the game.
     */
    public static ArrayList<RankEntry> splitParameterIntoRankEntry(String parameter) {
        StringTokenizer tokenizer = new StringTokenizer(parameter, DEFAULT_DELIM);
        ArrayList<RankEntry> list = new ArrayList<>();
        while (tokenizer.hasMoreTokens()) {
            RankEntry rankEntry = new RankEntry(tokenizer.nextToken(), Double.parseDouble(tokenizer.nextToken()), tokenizer.nextToken());
            list.add(rankEntry);
        }
        return list;
    }

    /**
     * Remove the illegal characters provided in the @param list from @param str.
     */
    public static String removeIllegalCharacters(String str, List<Character> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (!list.contains(str.charAt(i))) {
                sb.append(str.charAt(i));
            }
        }
        return sb.toString();
    }

    /**
     * Tests is the @param str is valid, which is the case if it is NOT contained in the @param list.
     */
    public static boolean isValidName(String str, List<String> list) {
        if (list.contains(str)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Parse a string into a {@link Vector2D}
     * @param text the input text to parse
     * @return the parsed Vector or {@link Vector2D#ZERO} when the parse failed
     */
    public static Vector2D parseVector2D(String text){
        var split = text.split(",");
        if(split.length == 2){
            var x = Double.parseDouble(split[0]);
            var y = Double.parseDouble(split[1]);
            return new Vector2D(x, y);
        }
        return Vector2D.ZERO;
    }
    
    public static ArrayList<Vector2D> parseVectorList(String text){
        var split = text.split(",");
        var list = new ArrayList<Vector2D>();

        if(text.isEmpty()){
            return list;
        }

        for (int i = 0; i < split.length - 1; i+=2) {
            list.add(parseVector2D(split[i] + "," + split[i+1]));
        }
        return list;
    }

    public static ArrayList<Float> parseFloatList(String text){
        var split = text.split(",");
        var list = new ArrayList<Float>();

        for (int i = 0; i < split.length; i++) {
            list.add(Float.parseFloat(split[i]));
        }
        return list;
    }

    public static String vectorListToString(Iterable<Vector2D> list){
        var sb = new StringBuilder();
        for (Vector2D position : list) {
            sb.append(String.format("%.2f,%.2f,", position.getX(), position.getY()));
        }

        if(sb.length() == 0){
            return "";
        }

        // remove the last comma
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static String floatListToString(Iterable<Float> list){
        var sb = new StringBuilder();
        for (Float f : list) {
            sb.append(String.format("%.2f,", f));
        }

        if(sb.length() == 0){
            return "";
        }

        // remove the last comma
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static Map<String, Double> sortByValue(Map<String, Double> unsortMap, final boolean order) {
        // https://stackoverflow.com/questions/8119366/sorting-hashmap-by-values
        List<Map.Entry<String, Double>> list = new LinkedList<>(unsortMap.entrySet());

        // Sorting the list based on values
        list.sort((o1, o2) -> order ? o1.getValue().compareTo(o2.getValue()) == 0
                ? o1.getKey().compareTo(o2.getKey())
                : o1.getValue().compareTo(o2.getValue()) : o2.getValue().compareTo(o1.getValue()) == 0
                ? o2.getKey().compareTo(o1.getKey())
                : o2.getValue().compareTo(o1.getValue()));
        return list.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, LinkedHashMap::new));

    }
}
