package ch.unibas.dmi.dbis.cs108.util;

import static ch.unibas.dmi.dbis.cs108.util.Util.splitParameterAtFirstN;
import static ch.unibas.dmi.dbis.cs108.util.Util.removeIllegalCharacters;
import static ch.unibas.dmi.dbis.cs108.util.Util.isValidName;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class UtilTest {

    @Test
    public void testSplitParameterAtFirstNForNEquals2() {
        String delim = "|";
        String name = "Andrew";
        String message = "hi";
        String parameter = "|" + name + "|" + message;
        String[] strArray = splitParameterAtFirstN(parameter, delim, 2);
        assertTrue(strArray[0].equals(name));
        assertTrue(strArray[1].equals(message));
    }

    @Test
    public void testSplitParameterAtFirstNForNEquals3() {
        String delim = "|";
        String firstName = "Andrew";
        String secondName = "James";
        String message = "One | two || three ||";
        String parameter = "|" + firstName + "|" + secondName + "|" + message;
        String[] strArray = splitParameterAtFirstN(parameter, delim, 3);
        assertTrue(strArray[0].equals(firstName));
        assertTrue(strArray[1].equals(secondName));
        assertTrue(strArray[2].equals(message));
    }

    @Test
    public void testMethodRemoveIllegalCharacters() {
        String str = "| is an illegal character";
        String expectedStr = " is an illegal character";
        List<Character> list = new ArrayList<>();
        list.add('|');
        str = removeIllegalCharacters(str, list);
        assertTrue(str.equals(expectedStr));
    }

    @Test
    public void testMethodIsValidStringTrue() {
        String str = "Andrew";
        List<String> list = new ArrayList<>();
        list.add("All");
        boolean isValid = isValidName(str, list);
        assertTrue(isValid);
    }

    @Test
    public void testMethodIsValidStringFalse() {
        String str = "All";
        List<String> list = new ArrayList<>();
        list.add("All");
        boolean isValid = isValidName(str, list);
        assertFalse(isValid);
    }

}
