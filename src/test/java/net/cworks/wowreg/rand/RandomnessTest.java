package net.cworks.wowreg.rand;

import org.junit.Assert;
import org.junit.Test;

public class RandomnessTest {

    @Test
    public void testRandomWord() {
        String word1 = Randomness.getWord();
        String word2 = Randomness.getWord();
        String word3 = Randomness.getWord();
        Assert.assertNotEquals(word1, word2);
        Assert.assertNotEquals(word1, word3);
        Assert.assertNotEquals(word2, word3);
        System.out.println(word1 + "-" + word2 + "-" + word3);
    }
}
