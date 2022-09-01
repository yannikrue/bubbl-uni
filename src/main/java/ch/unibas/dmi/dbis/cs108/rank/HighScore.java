package ch.unibas.dmi.dbis.cs108.rank;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HighScore {

    private static final List<RankEntry> rankList = new ArrayList<>();
    private static final String LIST_NAME = "highScore.txt";

    /**
     * Read the high score list. It uses UTF 8 character encoding.
     */
    public static void readList() {
        String tempPlayerName;
        String tempSize;
        String tempTime;

        synchronized(rankList) {
            try {
                BufferedReader rankReader = new BufferedReader(new InputStreamReader(new FileInputStream(LIST_NAME), StandardCharsets.UTF_8));   // throws FileNotFoundException
                tempPlayerName = rankReader.readLine();
                while (tempPlayerName != null && tempPlayerName.length() > 0) {
                    tempSize = verifyFormattingOfSize(rankReader.readLine());
                    tempTime = verifyFormattingOfTime(rankReader.readLine());
                    RankEntry rankEntry = new RankEntry(tempPlayerName, Double.parseDouble(tempSize), tempTime);
                    rankList.add(rankEntry);
                    tempPlayerName = rankReader.readLine();
                }
                rankReader.close();
                Collections.sort(rankList); // sort the list of rank entries base on compareTo

            } catch (FileNotFoundException fnfe) {
                // do nothing for now.
            } catch (IOException ioe) {
                // do nothing for now.
            }
        }
    }

    /**
     * Writes the high score list as a text file to the root directory using the LIST_NAME.
     * It uses UTF 8 character encoding.
     */
    public static void writeList() {
        synchronized(rankList) {
            try {
                BufferedWriter rankWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(LIST_NAME), StandardCharsets.UTF_8));
                Collections.sort(rankList); // sort the list of rank entries base on compareTo
                for (int i = 0; i < 10 && i < rankList.size(); i++) {
                    rankWriter.write(rankList.get(i).getPlayerName());
                    rankWriter.write("\n");
                    rankWriter.write(String.valueOf(rankList.get(i).getSize()));
                    rankWriter.write("\n");
                    rankWriter.write(rankList.get(i).getTime());
                    rankWriter.write("\n");
                }
                rankWriter.close();
            } catch (IOException ioe) {
                // do nothing for now.
            }
        }
    }

    /**
     * The @param size must be a (decimal) number.
     */
    public static String verifyFormattingOfSize(String size) {
        try {
            double sizeDouble = Double.parseDouble(size);
        } catch (NumberFormatException nfe) {
            return "0.0";
        }
        return size;
    }

    /**
     * The @param time must have the format "mm:ss", where 99 <= mm, ss <= 00.
     */
    private static String verifyFormattingOfTime(String time) {
        String defaultTime = "00:00";
        if (time.length() != 5) {
            return defaultTime;
        }
        if (!Character.isDigit(time.charAt(0)) ||
                !Character.isDigit(time.charAt(1)) ||
                time.charAt(2) != ':' ||
                !Character.isDigit(time.charAt(3)) ||
                !Character.isDigit(time.charAt(4))) {
            return defaultTime;

        }
        return time;
    }

    /**
     * If the player name of the @param winner is not included in the rankList, it is added as a new entry and the
     * rankList is then sorted. If it is already included, then the existing values (time and/or size) are overwritten
     * if the new values are better (larger).
     */
    public static void add(RankEntry winner) {
        synchronized(rankList) {
            int i = rankList.indexOf(winner);
            if (i >= 0) {
                int comp = winner.compareTo(rankList.get(i));
                if (comp < 0) {
                    rankList.set(i, winner);
                }
            } else {
                rankList.add(winner);
            }
            Collections.sort(rankList); // sort the list of rank entries base on compareTo
        }
    }

    /**
     * Return a string of all entries in the high score list.
     */
    public static String getHighScoreAsString() {
        StringBuilder sb = new StringBuilder();
        synchronized (rankList) {
            for (RankEntry rankEntry : rankList) {
                sb.append("|");
                sb.append(rankEntry.getPlayerName());
                sb.append("|");
                sb.append(rankEntry.getSize());
                sb.append("|");
                sb.append(rankEntry.getTime());
            }
        }
        return sb.toString();
    }

}
