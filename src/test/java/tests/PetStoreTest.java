package tests;

import animals.AnimalType;
import animals.petstore.pet.Pet;
import animals.petstore.pet.attributes.Breed;
import animals.petstore.pet.attributes.Gender;
import animals.petstore.pet.attributes.PetType;
import animals.petstore.pet.attributes.Skin;
import animals.petstore.pet.types.Cat;
import animals.petstore.pet.types.Dog;
import animals.petstore.store.DuplicatePetStoreRecordException;
import animals.petstore.store.PetNotFoundSaleException;
import animals.petstore.store.PetStore;

import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class PetStoreTest {

    private static PetStore petStore;

    @BeforeEach
    public void loadThePetStoreInventory() {
        petStore = new PetStore();
        petStore.init();
    }

    // ======================================================
    // ORIGINAL TESTS (UNCHANGED)
    // ======================================================

    @Test
    @DisplayName("Inventory Count Test")
    public void validateInventory() {
        assertEquals(5, petStore.getPetsForSale().size(),
                "Inventory counts are off!");
    }

    @Test
    @DisplayName("Print Inventory Test")
    public void printInventoryTest() {
        petStore.printInventory();
    }

    @Test
    @DisplayName("Sale of Poodle Remove Item Test")
    public void poodleSoldTest() throws DuplicatePetStoreRecordException, PetNotFoundSaleException {
        int expectedSize = petStore.getPetsForSale().size() - 1;

        Dog poodle = new Dog(
                AnimalType.DOMESTIC,
                Skin.FUR,
                Gender.MALE,
                Breed.POODLE,
                new BigDecimal("650.00"),
                1
        );

        petStore.soldPetItem(poodle);

        assertEquals(expectedSize, petStore.getPetsForSale().size(),
                "Expected inventory does not match actual");
    }

    @Test
    @DisplayName("Poodle Duplicate Record Exception Test")
    public void poodleDupRecordExceptionTest() {
        petStore.addPetInventoryItem(new Dog(
                AnimalType.DOMESTIC,
                Skin.FUR,
                Gender.MALE,
                Breed.POODLE,
                new BigDecimal("650.00"),
                1
        ));

        Dog poodle = new Dog(
                AnimalType.DOMESTIC,
                Skin.FUR,
                Gender.MALE,
                Breed.POODLE,
                new BigDecimal("650.00"),
                1
        );

        assertThrows(DuplicatePetStoreRecordException.class,
                () -> petStore.soldPetItem(poodle));
    }

    @Test
    @DisplayName("Sale of Sphynx Remove Item Test")
    public void sphynxSoldTest() throws DuplicatePetStoreRecordException, PetNotFoundSaleException {
        int expectedSize = petStore.getPetsForSale().size() - 1;

        Cat sphynx = new Cat(
                AnimalType.DOMESTIC,
                Skin.UNKNOWN,
                Gender.FEMALE,
                Breed.SPHYNX,
                new BigDecimal("100.00"),
                2
        );

        Pet removedItem = petStore.soldPetItem(sphynx);

        assertEquals(expectedSize, petStore.getPetsForSale().size(),
                "Expected inventory does not match actual");
        assertEquals(PetType.CAT, removedItem.getPetType(),
                "The cat items are identical");
    }

    // ======================================================
    // ADDED TESTS FOR PART 1 (ONLY ADDITIONS)
    // ======================================================

    @Test
    @DisplayName("Selling pet with store ID 0 throws PetNotFoundSaleException")
    void sellingPetWithZeroIdThrowsException() {
        Dog invalidDog = new Dog(
                AnimalType.DOMESTIC,
                Skin.FUR,
                Gender.MALE,
                Breed.MALTESE,
                new BigDecimal("300.00"),
                0
        );

        assertThrows(PetNotFoundSaleException.class,
                () -> petStore.soldPetItem(invalidDog));
    }

    @Test
    @DisplayName("Adding pet increases inventory size")
    void addPetIncreasesInventory() {
        int before = petStore.getPetsForSale().size();

        Dog newDog = new Dog(
                AnimalType.DOMESTIC,
                Skin.FUR,
                Gender.MALE,
                Breed.GERMAN_SHEPARD,
                new BigDecimal("800.00"),
                99
        );

        petStore.addPetInventoryItem(newDog);

        assertEquals(before + 1, petStore.getPetsForSale().size());
    }

    @Test
    @DisplayName("Selling Cat removes correct Cat")
    void sellingCatRemovesCorrectCat() throws DuplicatePetStoreRecordException, PetNotFoundSaleException {
        int before = petStore.getPetsForSale().size();

        Cat cat = new Cat(
                AnimalType.DOMESTIC,
                Skin.UNKNOWN,
                Gender.FEMALE,
                Breed.BURMESE,
                new BigDecimal("65.00"),
                1
        );

        Pet sold = petStore.soldPetItem(cat);

        assertEquals(before - 1, petStore.getPetsForSale().size());
        assertEquals(PetType.CAT, sold.getPetType());
    }

    @Test
    @DisplayName("Duplicate Cat store ID throws DuplicatePetStoreRecordException")
    void duplicateCatStoreIdThrowsException() {
        Cat duplicateCat = new Cat(
                AnimalType.DOMESTIC,
                Skin.UNKNOWN,
                Gender.FEMALE,
                Breed.SPHYNX,
                new BigDecimal("100.00"),
                2
        );

        petStore.addPetInventoryItem(duplicateCat);

        assertThrows(DuplicatePetStoreRecordException.class,
                () -> petStore.soldPetItem(duplicateCat));
    }
}
