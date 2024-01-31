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


### TODO
* before java platform
  * refactor agents
  * clear maven dependencies
* prepare testing for webapps - run webapps and send http queries
* clear spring-mvc
* clear spring web security
* clear sql
* go through all description files
* projects which use hsql database should create files in tmp
  directory, not in project
* hibernate search
  * organize resources
  * HibernateUtils - I think it should be removed
  * describe
* hsql
  * refactor isolationLevels example
  * describe
* fix testListenerForEntitiesStoredDueToCascadeIsCalledFirst - when run more tests it fails
* go again through hibernate search tutorial and update examples
* more examples for string format
* java - nested classes
* remove @Ignore on tests

## TODO
* what will happen when you persist persisted entity?
