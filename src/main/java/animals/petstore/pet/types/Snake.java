package animals.petstore.pet.types;

import animals.AnimalType;
import animals.petstore.pet.Pet;
import animals.petstore.pet.attributes.Breed;
import animals.petstore.pet.attributes.Gender;
import animals.petstore.pet.attributes.PetType;
import animals.petstore.pet.attributes.Skin;

import java.math.BigDecimal;

public class Snake extends Pet implements PetImpl {

    private int numberOfLegs;

    public Snake(AnimalType animalType, Skin skinType, Gender gender) {
        this(animalType, skinType, gender, BigDecimal.ZERO);
    }

    public Snake(AnimalType animalType, Skin skinType, Gender gender, BigDecimal cost) {
        this(animalType, skinType, gender, cost, 0);
    }

    public Snake(AnimalType animalType, Skin skinType, Gender gender, BigDecimal cost, int petStoreId) {
        super(PetType.SNAKE, cost, gender, petStoreId);
        super.animalType = animalType;
        super.skinType = skinType;
        this.numberOfLegs = 0;
        this.hasLegs = false;
        this.isMammal = false;
    }

    // ======= getters your tests use =======

    /** Expose the skin for assertions in tests. */
    public Skin getSkinType() {
        return this.skinType;
    }

    /** Expose the animal type (DOMESTIC/WILD/UNKNOWN). */
    public AnimalType getAnimalType() {
        return this.animalType;
    }

    /** Snakes have 0 legs, but keep as field for consistency/tests. */
    public int getNumberOfLegs() {
        return numberOfLegs;
    }

    // ======= PetImpl requirement =======
    @Override
    public Breed getBreed() {
        return Breed.UNKNOWN;
    }

    // ======= behavior helpers =======

    /** Make the base hypoallergenic message snake-specific. */
    public String snakeHypoallergenic() {
        // base message uses word "pet" in AbstractPet; keep its original spelling, just swap the noun
        return super.petHypoallergenic(this.skinType).replace("pet", "snake");
    }

    /** Simple “speak” that varies by animal type. */
    public String speak() {
        switch (this.animalType) {
            case DOMESTIC:
                return "The snake goes Hiss! Hiss!";
            case WILD:
                return "The snake goes SSSS! SSSS!";
            default:
                return "The snake goes " + super.getPetType().speak + "! " + super.getPetType().speak + "!";
        }
    }

    private String numberOfLegsAsString() {
        return "Snakes have " + numberOfLegs + " legs!";
    }

    @Override
    public String toString() {
        // Start with Pet’s string (type/gender/cost and maybe store id), then append snake specifics
        return super.toString()
                + "The snake is " + this.animalType + "!\n"
                + this.snakeHypoallergenic() + "!\n"
                + this.speak() + "\n"
                + this.numberOfLegsAsString();
    }
}
