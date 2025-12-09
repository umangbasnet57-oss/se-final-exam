FinalExam

Part 1 (coverage increase):
Added new tests to PetStoreTest to increase method/line/branch coverage:
addNewDog_increasesInventory_andIsPresent()
sellNonExistentDog_throws() (exception path)
sellRemovesOnlyMatchingId() (ID match behavior)
petNotFoundException_message() (message pass-through)
parameterized isNumberEven(int) example

Part 2 (new Pet + tests):
Created new class animals.petstore.pet.types.Snake and comprehensive SnakeTest covering:
construction defaults and full-ctor values
speak() for domestic vs wild snakes
hypoallergenic logic and skin/legs/storeId defaults
Note on branches
I kept the feature branch feature/snake after merging so the commit history of the new class (Snake) and the PetStoreTest coverage additions remains easy to review. The default/gradable branch is initial code; the PR above shows the exact diff merged into it.
<img width="468" height="450" alt="image" src="https://github.com/user-attachments/assets/32c6ffb6-82c5-4886-8c04-27d41699cbe5" />
