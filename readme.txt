Please explain your implementation approach here. Mention changes you've made to existing source, as well as new code that you've added to the project.

1. Create InventoryComponent to support inventory system in game
    a. Use addItem, dropItem, allInventories to modify inventory for player
    b. Use map to track amount of items in inventory system
2. Create Exit, Item, Player, Room, and Thing Class. They are all subclass of Entity.
    a. In base class Entity, we add IdentityComponent and DescriptionComponent by default.
    b. Depending on different sub class type, add LocationComponent/RoomComponent/InventoryComponent.
    c. Add abstract methods: get() and drop() to Entity.
3. Create world use singleton pattern. So we can access 'world' globally and don't have to pass world everywhere.
4. Refactor EntityFactory and create part in InputProcessor class.
