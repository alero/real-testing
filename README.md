# Real testing
The basic need of the project is to cover a Test driven environment for the developer. 
Doing this by combining best of breed products to enable a productive and accurate way of testing 

It all starts with @RunWith and a configuration via @ContainerContext
The IoC container that manages the lifecycle in the JUnit tests is a bit special and resets all its states between all tests (Singletons are not shared over tests). The basic idea is that all TDD rules are automatically implemented via the framework, like cleanup and no accidental shared statefulness.

The ContainerContext can in its turn append alot of different resources and thirdparty IoC Containers to support a variation of normal development environments in Java.

If the IoC Container used by the JUnit Runner is used standalone its very fast. 
It has the same object creation speed as Google Guice but can also clone and reset itself in a few milliseconds between tests.
It was built to be used in tests and beeing slow is not an option.

It is built to be extendable so that the developers can use the frameworks they are used to:
The main focus has been on CDI and Spring supported development.

**Container Glue support**
* Spring support (Use the @Autowired) to leave the Testing Container and jump over to spring
-- This means a Spring Container is started and attached to the lifecycle handling using the same principles as spring normally does.
Uses a special @RunWith(SpringJUnitRunner.class) to do this

**Container Implemented support**
* CDI light, the application can depend on most common CDI configurations but does not support CDI Config annotations. Uses the regular non glued @RunWith(JUnitRunner.class) JUnit runner
* EJB3 light, the application can depend on EJB3 annotations. Uses the regular non glued @RunWith(JUnitRunner.class) JUnit runner

**Planned Container Implemented support**
* Google Guice, as the IoC Container inside the tests was built using Guice as blueprint it should be fairly esasy to support it fully.
* Spring Light, as spring contains alot of variations and code the Glue version was created. But this makes it a bit slow!

**Resources support**
* Loading SQL schema files (*.sql)
* Loading SQL data files (*.sql)
* Loading Liquibase schema files
* Datasources (java.sql.Datasource)
* JPA Entity Managers (manages the lifecycle and makes it possible to combine with Datasource)

**Single config placeholder**
All configurations for tests are centralized into a "application type config" and can be safely reused between tests.
