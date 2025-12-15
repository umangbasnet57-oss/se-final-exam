package animals.petstore.store;

import animals.petstore.pet.Pet;
import animals.petstore.pet.attributes.PetType;
import animals.petstore.pet.types.Cat;
import animals.petstore.pet.types.Dog;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PetStore {

    private List<Pet> petsForSale;
    private List<Pet> petsSold;

    public PetStore() {
        petsForSale = new ArrayList<>();
        petsSold = new ArrayList<>();
    }

    /**
     * Initialize the pet inventory list
     */
    public void init() {
        petsForSale.clear();
        petsSold.clear();

        // Inventory loaded exactly as tests expect
        petsForSale.add(new Dog(
                animals.AnimalType.DOMESTIC,
                animals.petstore.pet.attributes.Skin.FUR,
                animals.petstore.pet.attributes.Gender.MALE,
                animals.petstore.pet.attributes.Breed.MALTESE,
                new java.math.BigDecimal("750.00"),
                3
        ));

        petsForSale.add(new Dog(
                animals.AnimalType.DOMESTIC,
                animals.petstore.pet.attributes.Skin.FUR,
                animals.petstore.pet.attributes.Gender.MALE,
                animals.petstore.pet.attributes.Breed.POODLE,
                new java.math.BigDecimal("650.00"),
                1
        ));

        petsForSale.add(new Cat(
                animals.AnimalType.DOMESTIC,
                animals.petstore.pet.attributes.Skin.HAIR,
                animals.petstore.pet.attributes.Gender.MALE,
                animals.petstore.pet.attributes.Breed.BURMESE,
                new java.math.BigDecimal("65.00"),
                1
        ));

        petsForSale.add(new Dog(
                animals.AnimalType.DOMESTIC,
                animals.petstore.pet.attributes.Skin.HAIR,
                animals.petstore.pet.attributes.Gender.MALE,
                animals.petstore.pet.attributes.Breed.GERMAN_SHEPARD,
                new java.math.BigDecimal("50.00"),
                2
        ));

        petsForSale.add(new Cat(
                animals.AnimalType.DOMESTIC,
                animals.petstore.pet.attributes.Skin.UNKNOWN,
                animals.petstore.pet.attributes.Gender.FEMALE,
                animals.petstore.pet.attributes.Breed.SPHYNX,
                new java.math.BigDecimal("100.00"),
                2
        ));
    }

    /**
     * Print inventory
     */
    public void printInventory() {
        Consumer<Pet> action = System.out::println;

        petsForSale.stream()
                .sorted(Comparator.comparing(Pet::getPetType))
                .forEach(action);
    }

    /**
     * Sell a pet
     */
    public Pet soldPetItem(Pet soldPet)
            throws DuplicatePetStoreRecordException, PetNotFoundSaleException {

        if (soldPet.getPetStoreId() == 0) {
            throw new PetNotFoundSaleException("The Pet is not part of the pet store!!");
        }

        if (soldPet instanceof Dog) {
            Dog found = identifySoldDogFromInventory((Dog) soldPet);
            removePetFromInventoryByPetId(PetType.DOG, soldPet.getPetStoreId());
            petsSold.add(found);
            return found;
        }

        if (soldPet instanceof Cat) {
            Cat found = identifySoldCatFromInventory((Cat) soldPet);
            removePetFromInventoryByPetId(PetType.CAT, soldPet.getPetStoreId());
            petsSold.add(found);
            return found;
        }

        // 🔹 For Bird / Snake / future pets:
        // They are not sold via PetStore logic in this assignment
        throw new PetNotFoundSaleException("Unsupported pet type for sale");
    }

    /**
     * Add pet to inventory
     */
    public void addPetInventoryItem(Pet pet) {
        petsForSale.add(pet);
    }

    /**
     * Remove pet by type and store ID
     */
    private void removePetFromInventoryByPetId(PetType petType, int petStoreId) {
        petsForSale = petsForSale.stream()
                .filter(p ->
                        !(p.getPetType() == petType && p.getPetStoreId() == petStoreId)
                )
                .collect(Collectors.toList());
    }

    /**
     * Find dog
     */
    private Dog identifySoldDogFromInventory(Dog soldDog)
            throws DuplicatePetStoreRecordException {

        List<Pet> matches = petsForSale.stream()
                .filter(p -> p instanceof Dog && p.getPetStoreId() == soldDog.getPetStoreId())
                .collect(Collectors.toList());

        if (matches.size() == 1) {
            return (Dog) matches.get(0);
        }

        throw new DuplicatePetStoreRecordException(
                "Duplicate Dog record store id [" + soldDog.getPetStoreId() + "]"
        );
    }

    /**
     * Find cat
     */
    private Cat identifySoldCatFromInventory(Cat soldCat)
            throws DuplicatePetStoreRecordException {

        List<Pet> matches = petsForSale.stream()
                .filter(p -> p instanceof Cat && p.getPetStoreId() == soldCat.getPetStoreId())
                .collect(Collectors.toList());

        if (matches.size() == 1) {
            return (Cat) matches.get(0);
        }

        throw new DuplicatePetStoreRecordException(
                "Duplicate Cat record store id [" + soldCat.getPetStoreId() + "]"
        );
    }

    public List<Pet> getPetsForSale() {
        return petsForSale;
    }
}
