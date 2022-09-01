package ch.unibas.dmi.dbis.cs108.rank;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class rankTest {

    @Test
    public void testCompareTo() {
        RankEntry rankEntry1 = new RankEntry("James", 2.0, "00:00");
        RankEntry rankEntry2 = new RankEntry("Andrew", 2.5, "01:10");
        RankEntry rankEntry3 = new RankEntry("Rémi", 1.0, "00:00");
        RankEntry rankEntry4 = new RankEntry("Jury", 1.5, "00:00");
        RankEntry rankEntry5 = new RankEntry("Igor", 2.5, "01:00");
        List<RankEntry> list = new ArrayList<>();
        list.add(rankEntry1);
        list.add(rankEntry2);
        list.add(rankEntry3);
        list.add(rankEntry4);
        list.add(rankEntry5);
        Collections.sort(list);
        assertTrue(list.get(0).getPlayerName().equals("Andrew"));
        assertTrue(list.get(1).getPlayerName().equals("Igor"));
        assertTrue(list.get(2).getPlayerName().equals("James"));
        assertTrue(list.get(3).getPlayerName().equals("Jury"));
        assertTrue(list.get(4).getPlayerName().equals("Rémi"));
    }

}
