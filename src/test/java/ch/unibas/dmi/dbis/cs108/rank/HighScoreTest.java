package ch.unibas.dmi.dbis.cs108.rank;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class HighScoreTest {

    @Test
    public void testMethodVerifyFormattingOfSize() {
        String correctDecimalNumber = "3.14";
        String returnCorrectDecimalNumber = HighScore.verifyFormattingOfSize(correctDecimalNumber);
        assertTrue(returnCorrectDecimalNumber.equals(correctDecimalNumber));

        String incorrectDecimalNumber = "notANumber";
        String returnDefaultDecimalNumber = HighScore.verifyFormattingOfSize(incorrectDecimalNumber);
        assertTrue(returnDefaultDecimalNumber.equals("0.0"));
    }

}
