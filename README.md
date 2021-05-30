# SnackMachine
It's a basic version of a snack vending machine. You can manage different snack categories, control the price and amount of available items in each category, handle purchases(there is a bug :)), and provide related statistics(there is a bug :)).

## What you need for the correct operation of the program
* You need to connect to MySQL (I used the DB Navigator plugin)
* Everything else will be installed by maven
* Just run the code. Creating tables is spelled out in the code :)

## What did I use in the project?
* Java 8
* Maven
* MySQL
* JUnit(I tried to write unit tests, but I didn't succeed :))

## List of commands
* ***addCategory “Chocolate bar”  35.75 12***  — register a snack category in the system.
Command accepts the following parameters:
  * _“Chocolate bar”_ — the name of the snack category to be served by the vending machine
  * _35.75_ — the price of items in the category
  * _12 (optional, default 0_ — the number of purchasable snack items in the category
* ***addItem “Chocolate bar” 25*** — register provided amount of snack items to sell.
Command accepts the following parameters:
  * _“Chocolate bar”_ — the name of the snack category added items belong to
  * _25_ — the number of added snack items for sell
* ***purchase “Chocolate bar” 2021-04-13*** — purchase a single snack item.
Command accepts the following parameters:
  * _“Chocolate bar”_ — the name of the snack category an item is sold
  * _2021-04-13_ — the purchase date
* ***list*** — show list of served categories with amount of items available for sale sorted by amount
* ***clear*** — stop serving all snack categories that don’t have items for sale (items can not be purchased)
* ***report 2021-04*** — show earnings by category in specified month
Command accepts the following parameters:
  * _2021-04_ — the month for which the report is requested
* ***report 2021-04-21*** — show earnings by category gained since provided date till now sorted by category name.
Command accepts the following parameters:
  * _2021-04-21_ — the start date of the period for wich the report is requested

#### Additional information to the commands:
* Write the name of the category in " "
* The purchase and report commands do not work correctly

## Bugs
* There is a bug in the purchase method(In line 207. The same snack can be recorded 2 times in the table as different in a certain order of purchase of products). I think the problem is in some cheks
* There is a bug in the report method(In line 441. Incorrectly displays the report for a specific day; does not show a report for a specific month). I think the problem is in some cheks and retrieval of data from the table
