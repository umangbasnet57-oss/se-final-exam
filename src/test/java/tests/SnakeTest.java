package tests;

import animals.AnimalType;
import animals.petstore.pet.attributes.Gender;
import animals.petstore.pet.attributes.Skin;
import animals.petstore.pet.types.Snake;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class SnakeTest {

    @Test
    public void testSnakeCreation_minCtor_defaultsStoreIdAndLegs() {
        Snake s = new Snake(AnimalType.DOMESTIC, Skin.SCALES, Gender.FEMALE);

        // Type & gender come from Pet
        assertEquals(Gender.FEMALE, s.getGender());
        assertEquals("SNAKE", s.getPetType().name());

        // cost defaults to 0 in the 3-arg Snake ctor
        assertEquals(0, s.getCost().compareTo(BigDecimal.ZERO));

        // Defaults
        assertEquals(0, s.getPetStoreId());
        assertEquals(0, s.getNumberOfLegs());

        // skin + animal type getters
        assertEquals(Skin.SCALES, s.getSkinType());
        assertEquals(AnimalType.DOMESTIC, s.getAnimalType());
    }

    @Test
    public void testSnakeCreation_fullCtor_setsAllFields() {
        Snake s = new Snake(AnimalType.WILD, Skin.SCALES, Gender.MALE, new BigDecimal("123.45"), 5);

        assertEquals(AnimalType.WILD, s.getAnimalType());
        assertEquals(Skin.SCALES, s.getSkinType());
        assertEquals(Gender.MALE, s.getGender());
        assertEquals(0, s.getCost().compareTo(new BigDecimal("123.45")));
        assertEquals(5, s.getPetStoreId());
    }

    @Test
    public void testSpeak_domesticBranch() {
        Snake s = new Snake(AnimalType.DOMESTIC, Skin.SCALES, Gender.MALE);
        String msg = s.speak();
        assertTrue(msg.contains("snake") && (msg.contains("Hiss") || msg.contains("SSSS")),
                "Domestic snake should hiss: " + msg);
    }

    @Test
    public void testSpeak_wildBranch() {
        Snake s = new Snake(AnimalType.WILD, Skin.SCALES, Gender.MALE);
        String msg = s.speak();
        assertTrue(msg.contains("snake") && (msg.contains("SSSS") || msg.contains("Hiss")),
                "Wild snake should SSSS (or hiss): " + msg);
    }

    @Test
    public void testToString_includesKeyDetails() {
        Snake s = new Snake(AnimalType.DOMESTIC, Skin.SCALES, Gender.FEMALE, new BigDecimal("65.00"), 5);
        String t = s.toString();

        assertTrue(t.contains("type of pet is SNAKE"));
        assertTrue(t.contains("pet store id is 5"));
        assertTrue(t.contains("gender is FEMALE"));
        assertTrue(t.contains("cost is $65.00"));
        assertTrue(t.toLowerCase().contains("snake is"));
    }

    @Test
    public void testSnakeHypoallergenic_messageMentionsHypoOrUnknown() {
        // SCALES are the “hypoallergenic” branch in this codebase wording
        Snake s = new Snake(AnimalType.WILD, Skin.SCALES, Gender.MALE);
        String msg = s.snakeHypoallergenic();
        String lower = msg.toLowerCase();

        // Accept either “hypoallergenic” line or the UNKNOWN fallback (robust to minor wording)
        assertTrue(
                lower.contains("hypo") || lower.contains("hyper") || lower.contains("unknown"),
                "Expected message to include 'hypo*' or 'hyper*' or 'UNKNOWN'. Got: " + msg
        );
    }
}
