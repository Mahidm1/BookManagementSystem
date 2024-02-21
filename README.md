# Book Management System

## Overview
This project is a Java application for managing a bookstore's inventory with functionalities for tracking prize-winning books and maintaining a curated reading list.

## Current State of the Project
The application's foundation is set, with capabilities to parse book data and manage inventory categorization. The test suite is included to validate functionality but the UI and database integration are pending.

## Features
- Load book data from files and organize it.
- Categorize and store prize-winning books.
- Manage and test reading lists.

## Testing
The project includes a test suite in the `assignment2-test-classes` folder:
- `Assignment2Base`
- `Test_Q1` to `Test_Q4`
- `TestBase`

## Running Tests
To verify the application's functionality, a suite of unit tests is provided. Follow these steps to execute the tests:

- Compile the application and test classes with the following command:
  `javac BookStore.java PrizeWinnerStore.java ReadingListItemStore.java ReadingListItemStoreTest.java Assignment2Base.java Test_Q1.java Test_Q2.java Test_Q3.java Test_Q4.java TestBase.java`
- Execute the test suite by running:
  `java ReadingListItemStoreTest`

## Learning Outcomes
- Applying Java classes and objects to model real-world entities.
- Using lists and arrays to manage collections of objects.
- Implementing basic file I/O operations to read data.
- Writing test cases to validate the application logic.

