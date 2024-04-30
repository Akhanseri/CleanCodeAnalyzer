# Code Analyzer

The Code Analyzer project is a tool for analyzing Java code, which allows you to determine the following characteristics:

- The percentage of methods following the CamelCase convention, using typical suffixes and endings.
- The top 3 methods with the highest nesting level in each class.

## Usage

1. Make sure that the `BASE_DIRECTORY` variable in the `CodeAnalyzer` class specifies the path to the directory containing the Java files to be analyzed.
2. Run the application by invoking the `main` method in the `CodeAnalyzer` class.
3. After the application finishes execution, you will see a report containing the overall percentage of methods following the CamelCase convention, as well as the top 3 methods with the highest nesting level in each class.

## How it Works

1. Directory traversal process:
    - The program starts traversing the specified directory and analyzes each Java file.
    - For each Java file, the program analyzes classes and methods.
2. Method analysis:
    - Each method in a class is analyzed for compliance with the CamelCase convention, using typical suffixes and endings, as well as its nesting level.
    - The nesting level is determined by the number of opening and closing braces in the method.
3. Report:
    - Upon completion of the analysis, the program outputs the overall percentage of methods following the CamelCase convention.
    - Additionally, it displays the top 3 methods with the highest nesting level in each class.

## Additional Features

- It is possible to expand the functionality of the program by adding new metrics for Java code analysis.
- The program can be configured to analyze other programming languages by modifying regular expressions and analysis rules.

## TODO
- Ability to check for the number of lines in a method; a good number of lines is considered to be 20.
- Check for the number of arguments: if there are no more than 3, it's acceptable, but if there are more, the code is considered dirty, as a significant amount of time is lost on testing.

## Requirements

- Java Runtime Environment (JRE) version 8 or higher.
- Access to the file system to read Java files.


