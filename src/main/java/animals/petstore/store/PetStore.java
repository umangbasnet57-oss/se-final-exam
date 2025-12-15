package animals.petstore.store;

import animals.AnimalType;
import animals.petstore.pet.Pet;
import animals.petstore.pet.attributes.Breed;
import animals.petstore.pet.attributes.Gender;
import animals.petstore.pet.attributes.PetType;
import animals.petstore.pet.attributes.Skin;
import animals.petstore.pet.types.Bird;
import animals.petstore.pet.types.Cat;
import animals.petstore.pet.types.Dog;

import java.math.BigDecimal;
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

    public void init() {
        this.addPetInventoryItem(new Dog(AnimalType.DOMESTIC, Skin.FUR, Gender.MALE, Breed.MALTESE,
                new BigDecimal("750.00"), 3));
        this.addPetInventoryItem(new Dog(AnimalType.DOMESTIC, Skin.FUR, Gender.MALE, Breed.POODLE,
                new BigDecimal("650.00"), 1));
        this.addPetInventoryItem(new Cat(AnimalType.DOMESTIC, Skin.HAIR, Gender.MALE, Breed.BURMESE,
                new BigDecimal("65.00"), 1));
        this.addPetInventoryItem(new Dog(AnimalType.DOMESTIC, Skin.HAIR, Gender.MALE, Breed.GERMAN_SHEPARD,
                new BigDecimal("50.00"), 2));
        this.addPetInventoryItem(new Cat(AnimalType.DOMESTIC, Skin.UNKNOWN, Gender.FEMALE, Breed.SPHYNX,
                new BigDecimal("100.00"), 2));
    }

    public void initAddDuplicateItem(Pet addPet) {
        this.init();
        this.addPetInventoryItem(addPet);
    }

    public void printInventory() {
        Consumer<Pet> action = System.out::println;
        List<Pet> sortedPets = this.petsForSale.stream()
                .sorted(Comparator.comparing(Pet::getPetType))
                .collect(Collectors.toList());
        sortedPets.forEach(action);
    }

    public Pet soldPetItem(Pet soldPet) throws DuplicatePetStoreRecordException, PetNotFoundSaleException {
        if (soldPet.getPetStoreId() == 0) {
            throw new PetNotFoundSaleException("The Pet is not part of the pet store!!");
        }

        if (soldPet instanceof Dog) {
            Dog foundDog = identifySoldDogFromInventory((Dog) soldPet);
            removePetFromInventoryByPetId(PetType.DOG, soldPet.getPetStoreId());
            return foundDog;
        }

        if (soldPet instanceof Cat) {
            Cat foundCat = identifySoldCatFromInventory((Cat) soldPet);
            removePetFromInventoryByPetId(PetType.CAT, soldPet.getPetStoreId());
            return foundCat;
        }

        if (soldPet instanceof Bird) {
            Bird foundBird = identifySoldBirdFromInventory((Bird) soldPet);
            removePetFromInventoryByPetId(PetType.BIRD, soldPet.getPetStoreId());
            return foundBird;
        }

        throw new PetNotFoundSaleException("The Pet is not part of the pet store!!");
    }

    public void addPetInventoryItem(Pet pet) {
        this.petsForSale.add(pet);
    }

    private void removePetFromInventoryByPetId(PetType petType, int petStoreId) {
        this.petsForSale = this.petsForSale.stream()
                .filter(p -> !(p.getPetType() == petType && p.getPetStoreId() == petStoreId))
                .collect(Collectors.toList());
    }

    private Dog identifySoldDogFromInventory(Dog soldDog)
            throws DuplicatePetStoreRecordException, PetNotFoundSaleException {

        List<Pet> dogPets = this.petsForSale.stream()
                .filter(p -> (p instanceof Dog) && (p.getPetStoreId() == soldDog.getPetStoreId()))
                .collect(Collectors.toList());

        if (dogPets.size() == 1) return (Dog) dogPets.get(0);
        if (dogPets.size() == 0) throw new PetNotFoundSaleException("The Pet is not part of the pet store!!");

        throw new DuplicatePetStoreRecordException("Duplicate Dog record store id [" + soldDog.getPetStoreId() + "]");
    }

    private Cat identifySoldCatFromInventory(Cat soldCat)
            throws DuplicatePetStoreRecordException, PetNotFoundSaleException {

        List<Pet> catPets = this.petsForSale.stream()
                .filter(p -> (p instanceof Cat) && (p.getPetStoreId() == soldCat.getPetStoreId()))
                .collect(Collectors.toList());

        if (catPets.size() == 1) return (Cat) catPets.get(0);
        if (catPets.size() == 0) throw new PetNotFoundSaleException("The Pet is not part of the pet store!!");

        throw new DuplicatePetStoreRecordException("Duplicate Cat record store id [" + soldCat.getPetStoreId() + "]");
    }

    private Bird identifySoldBirdFromInventory(Bird soldBird)
            throws DuplicatePetStoreRecordException, PetNotFoundSaleException {

        List<Pet> birdPets = this.petsForSale.stream()
                .filter(p -> (p instanceof Bird) && (p.getPetStoreId() == soldBird.getPetStoreId()))
                .collect(Collectors.toList());

        if (birdPets.size() == 1) return (Bird) birdPets.get(0);
        if (birdPets.size() == 0) throw new PetNotFoundSaleException("The Pet is not part of the pet store!!");

        throw new DuplicatePetStoreRecordException("Duplicate Bird record store id [" + soldBird.getPetStoreId() + "]");
    }

    public List<Pet> getPetsForSale() {
        return petsForSale;
    }
}

