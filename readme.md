### Structure
My snippets have this structure:

    java-snippets                                  1
    +- project1                                    2
    |  +- src                                      3
    |     +- main                                  4
    |        +- java                               5
    |        |  +- cz.kul.snippets.project1        6
    |        |     +- commons                      7
    |        |     +- example01_nameOfExample      8
    |        |        +- TestNameOfExample.java    9
    |        +- resources                          10
    |           +- example01                       11
    |              +- resource.xml                 12
    +- description.md                              13
    
1. name of main maven project
2. name of project. Usually it is name of a technology. For 
   example Java, SQL, spring, ...
3. standard maven directory
4. standard maven directory
5. standard maven directory
6. last package is the same as project name
7. package for common project stuffs (optional)
8. project consists of N examples
9. test start with Test prefix following with name of th example.
   It should extends TestSnippets
   Or class with main method if it is difficult to create test
   for the example. Then the name is MainNameOfExample.
10. standar maven directory
11. each example has its own directory in resources folder
12. resource related to given example
13. description of the project/technology. It contains terms, basic principles, ...
