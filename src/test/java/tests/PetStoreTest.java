package tests;

import animals.AnimalType;
import animals.petstore.pet.attributes.Breed;
import animals.petstore.pet.attributes.Gender;
import animals.petstore.pet.attributes.Skin;
import animals.petstore.pet.types.Cat;
import animals.petstore.pet.types.Dog;
import animals.petstore.store.DuplicatePetStoreRecordException;
import animals.petstore.store.PetNotFoundSaleException;
import animals.petstore.store.PetStore;
import number.Numbers;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class PetStoreTest {

    private static PetStore petStore;

    @BeforeEach
    public void loadThePetStoreInventory() {
        petStore = new PetStore();
        petStore.init();
    }

    // ---------- original tests (kept) ----------

    @Test
    @DisplayName("Inventory Count Test")
    public void validateInventory() {
        assertEquals(5, petStore.getPetsForSale().size(), "Inventory counts are off!");
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
        Dog poodle = new Dog(AnimalType.DOMESTIC, Skin.FUR, Gender.MALE, Breed.POODLE,
                new BigDecimal("650.00"), 1);

        petStore.soldPetItem(poodle);

        assertEquals(expectedSize, petStore.getPetsForSale().size(), "Expected inventory does not match actual");
    }

    @Test
    @DisplayName("Poodle Duplicate Record Exception Test")
    public void poodleDupRecordExceptionTest() {
        petStore.addPetInventoryItem(new Dog(AnimalType.DOMESTIC, Skin.FUR, Gender.MALE, Breed.POODLE,
                new BigDecimal("650.00"), 1));

        Dog poodle = new Dog(AnimalType.DOMESTIC, Skin.FUR, Gender.MALE, Breed.POODLE,
                new BigDecimal("650.00"), 1);

        Exception exception = assertThrows(DuplicatePetStoreRecordException.class,
                () -> petStore.soldPetItem(poodle));

        assertEquals("Duplicate Dog record store id [1]", exception.getMessage(),
                "DuplicateRecordExceptionTest was NOT encountered!");
    }

    @Test
    @DisplayName("Sale of Sphynx Remove Item Test")
    public void sphynxSoldTest() throws DuplicatePetStoreRecordException, PetNotFoundSaleException {
        int expectedSize = petStore.getPetsForSale().size() - 1;

        Cat sphynx = new Cat(AnimalType.DOMESTIC, Skin.UNKNOWN, Gender.FEMALE, Breed.SPHYNX,
                new BigDecimal("100.00"), 2);

        Cat removedItem = (Cat) petStore.soldPetItem(sphynx);

        assertEquals(expectedSize, petStore.getPetsForSale().size(), "Expected inventory does not match actual");
        assertEquals(sphynx.getPetStoreId(), removedItem.getPetStoreId(), "The cat items are identical");
    }

    @TestFactory
    @DisplayName("Sale of Sphynx Remove Item Test (Dynamic)")
    public Stream<DynamicNode> sphynxSoldTest2() throws DuplicatePetStoreRecordException, PetNotFoundSaleException {
        int expectedSize = petStore.getPetsForSale().size() - 1;

        Cat sphynx = new Cat(AnimalType.DOMESTIC, Skin.UNKNOWN, Gender.FEMALE, Breed.SPHYNX,
                new BigDecimal("100.00"), 2);
        Cat removedItem = (Cat) petStore.soldPetItem(sphynx);

        List<DynamicTest> tests = Arrays.asList(
                dynamicTest("Inventory decreased by 1",
                        () -> assertEquals(expectedSize, petStore.getPetsForSale().size())),
                dynamicTest("Removed item matches requested",
                        () -> assertEquals(sphynx.toString(), removedItem.toString()))
        );

        List<DynamicNode> nodes = new ArrayList<>();
        nodes.add(dynamicContainer("Cat Item 2 Test", tests));
        return nodes.stream();
    }

    // ---------- safe additions to improve coverage ----------

    @Test
    @DisplayName("Add new Dog increases inventory and is present")
    public void addNewDog_increasesInventory_andIsPresent() {
        int start = petStore.getPetsForSale().size();

        Dog shepherd = new Dog(AnimalType.DOMESTIC, Skin.FUR, Gender.MALE, Breed.GERMAN_SHEPARD,
                new BigDecimal("799.00"), 99);

        petStore.addPetInventoryItem(shepherd);

        assertEquals(start + 1, petStore.getPetsForSale().size(), "Inventory should increase by 1");
        assertTrue(petStore.getPetsForSale().contains(shepherd), "New dog should be present");
    }

    @Test
    @DisplayName("Sell of non-existent Dog throws (implementation-agnostic)")
    public void sellNonExistentDog_throws() {
        Dog notInStore = new Dog(AnimalType.DOMESTIC, Skin.FUR, Gender.FEMALE, Breed.GERMAN_SHEPARD,
                new BigDecimal("399.00"), 9999);

        Exception ex = assertThrows(Exception.class, () -> petStore.soldPetItem(notInStore));
        assertTrue(
                ex instanceof PetNotFoundSaleException || ex instanceof DuplicatePetStoreRecordException,
                "Expected PetNotFoundSaleException or DuplicatePetStoreRecordException, but got: " + ex.getClass()
        );

        String msg = (ex.getMessage() == null ? "" : ex.getMessage().toLowerCase());
        assertTrue(msg.contains("not") || msg.contains("found") || msg.contains("duplicate"),
                "Message should indicate not found or duplicate");
    }

    @Test
    @DisplayName("Sell removes only matching store id (leaves the other)")
    public void sellRemovesOnlyMatchingId() throws DuplicatePetStoreRecordException, PetNotFoundSaleException {
        Dog d1 = new Dog(AnimalType.DOMESTIC, Skin.FUR, Gender.MALE, Breed.GERMAN_SHEPARD,
                new BigDecimal("500.00"), 200);
        Dog d2 = new Dog(AnimalType.DOMESTIC, Skin.FUR, Gender.MALE, Breed.GERMAN_SHEPARD,
                new BigDecimal("500.00"), 201);

        petStore.addPetInventoryItem(d1);
        petStore.addPetInventoryItem(d2);

        int before = petStore.getPetsForSale().size();

        Dog removed = (Dog) petStore.soldPetItem(
                new Dog(AnimalType.DOMESTIC, Skin.FUR, Gender.MALE, Breed.GERMAN_SHEPARD,
                        new BigDecimal("500.00"), 200));

        assertEquals(before - 1, petStore.getPetsForSale().size(), "Only one dog should be removed");
        assertEquals(200, removed.getPetStoreId());
        assertTrue(petStore.getPetsForSale().contains(d2), "Other dog (id 201) should still be present");
    }

    // ---------- small coverage bump for exception class ----------

    @Test
    @DisplayName("PetNotFoundSaleException message is passed through")
    void petNotFoundException_message() {
        PetNotFoundSaleException ex = new PetNotFoundSaleException("Dog not found");
        assertEquals("Dog not found", ex.getMessage());
    }

    // ---------- parameterized example ----------

    @ParameterizedTest
    @ValueSource(ints = {2, 4, 6, -10, 128, Integer.MIN_VALUE})
    void isNumberEven(int number) {
        assertTrue(Numbers.isEven(number));
    }
}
