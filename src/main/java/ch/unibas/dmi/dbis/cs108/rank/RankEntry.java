package ch.unibas.dmi.dbis.cs108.rank;

/**
 * The class RankEntry is used to display the ranking after a game.
 */
public class RankEntry implements Comparable<RankEntry> {

    String playerName;
    double size;
    String time;

    public RankEntry(String playerName, double size, String time) {
        this.playerName = playerName;
        this.size = size;
        this.time = time;
    }

    /**
     * Two RankEntry elements are compared to each other first based on the time (a higher time is better since it
     * corresponds to a faster win) and second, if the time is the same, based on size (larger size is better);
     * ended.
     */
    @Override
    public int compareTo(RankEntry otherRankEntry) {
        int timeInt = convertTimeToInt(this.time);
        int timeIntOther = convertTimeToInt(otherRankEntry.getTime());
        if (timeInt > timeIntOther) {
            return -1;
        } else if (timeInt < timeIntOther) {
            return 1;
        } else {    // timeInt == timeIntOther
            if (this.getSize() > otherRankEntry.getSize()) {
                return -1;
            } else if (this.getSize() < otherRankEntry.getSize()) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    private int convertTimeToInt(String time) {
        int minutes = Integer.parseInt(time.substring(0, time.indexOf(":")));
        int seconds = Integer.parseInt(time.substring(time.indexOf(":") + 1, time.length()));
        return minutes * 60 + seconds;
    }

    /**
     * @return s the player name. This is used for the table view to access the player name.
     */
    public String getPlayerName() {
        return this.playerName;
    }

    /**
     * @return s the size. This is used for the table view to access the size.
     */
    public double getSize() {
        return this.size;
    }

    /**
     * @return s this time. This is used for table view to access the time.
     */
    public String getTime() {
        return this.time;
    }

    /**
     * @return s a formatted string concatenating the player name and size. This is used for testing purposes.
     */
    @Override
    public String toString() {
        return "Player name: " + this.playerName + ", size: " + this.size + ", time; " + this.time;
    }

    /**
     * Overwrite the equals methode to be used in contains.
     */
    @Override
    public boolean equals(Object o) {
        RankEntry otherRankEntry = (RankEntry) o;
        return this.playerName.equals(otherRankEntry.getPlayerName());
    }
}
