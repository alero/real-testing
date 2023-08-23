# Real testing
The basic usage scenario of the project is to support a Test driven JVM environment for the developer. 
Enabling the developer by combining best of breed products for a productive and effective way of testing 

It all starts with @RunWith and a configuration via @ContainerContext
The IoC container that manages the lifecycle in the JUnit tests is created with the purpose of supporting tests(JUnit) and resets all its states between all tests (Singletons are not shared over tests). The basic idea is that all TDD rules are automatically implemented via the framework, like cleanup and no accidental shared statefulness.

The ContainerContext configurations can in its turn append different resources and thirdparty IoC Containers to support a variation of normal development environments in Java.
These are called "JUnit Plugins" and can be found in the plugin projects referenced below. 
A plugin is similar to a JUnit @Rule but is more flexible and adds more customization support as well as the possibility to combine the plugins (you can feed the result of one plugin as @Resources or @Inject/@Autowired resocoures to the injection framework and their extensions)

If the IoC Container used by the JUnit Runner is used standalone its very fast (1 million injections/second can normally be resolved). 
It has the same object creation speed as Google Guice but can also clone and reset itself in a few milliseconds between tests.
It was built to be used in tests and to be as fast as possible.

It is built to be extendable so that the developers can use the frameworks they are used to:
The main focus has been on CDI, Guice and Spring supported development.

## CI integration (Travis)
[Travis CI link](https://travis-ci.org/alero/real-testing)

[Sonar Report link](https://sonarcloud.io/dashboard?id=alero)

## JUnit support
The support for JUnit has been extended to include JUnit5 (jupiter) as well as JUnit4
JUnit 5.0.1 and JUnit 4.12 are used to verify the compatibility

## Plugins
Plugins have three different scenarions they support to make it easy to adapt to underlying JUnit framework.

**Lifecycle management**
The lifecycle is managed using before/after for four types: test, test_class, test_config, test_suite

The lifecycles are explained in order of shortest living "life", shortest first
* Lifecycle.test, means around each test. For example the datasource plugin managed transactions around each test lifecycle
* Lifecyclke.test_class, means around each testclass that is defined in the testsuite.  

**Runner Plugin** 
The runner plugin is what connects the plugin to the before/after of the lifecycle management
  
## JUnit config annotation
The config is a design style of this JUnitRunner and enforces a good packaging of the test variations, these configurations are then used as the base for alot of Plugins as the default lifecycle
All configurations for tests are managed by the runner (JUnit4Runner & JUnit5Runner) and can be safely reused between tests.


## IoC Container Extension or Injection support
The difference between Extension & Injection is that an extension uses the actual underlying framework as a extension to perform its IoC while the Injected means that the hrodberaht inject container reacts to the annotations in the code.
The Extended way of doing this is always the recommended way, but sometimes this is not good enough. (TODO: add samples in the details of these plugins README.md files)

**IoC Container Extension support**
* Spring support (Use the @Autowired or @Inject) to leave the hrodberaht Testing Container and jump over to spring
-- This means a Spring Container is started and attached to the lifecycle handling using the same principles as spring normally does.
Uses the plugin called SpringPlugin to activate this support.
[SpringExtensionPlugin](plugin/inject-spring-plugin/README.md)
* Guice support (@Inject)
-- This means a Guice Container is started and attached to the lifecycle handling using the same principles as guice normally does.
Uses the plugin called GuicePlugin to activate this support.
[GuiceExtensionPlugin](plugin/inject-guice-plugin/README.md)

All the Injection Container plugins support a "with" method that can take in typed and/or named "POJO" objects as javax.inject.Provider that will be registered to the underlying IoC framework.

**IoC Container Injection support**
* CDI light, the application can depend on most common CDI configurations but does not support CDI Config annotations. Uses the regular non glued @RunWith(JUnitRunner.class) JUnit runner
* EJB3 light, the application can depend on EJB3 annotations. Uses the regular non glued @RunWith(JUnitRunner.class) JUnit runner
[CDIInjectedPlugin](plugin/inject-cdi-plugin/README.md)

**Planned Container Implemented support**
* Spring Light, as spring contains alot of variations and code the Glue version was created. But this makes it a bit slow!

## Resources support
[ContextResourcePlugin](plugin/inject-plugin-core/README.md)
* Create managed JNDI objects

[DataSourcePlugin](plugin/inject-plugin-core/README.md)
* Create managed datasources for an in-memory database (hsql)
* Loading SQL schema files to create tables(*.sql)
* Loading SQL data files to insert base data for all tests to share (*.sql)
* Supports multithreading tests (reuses connection to enabled shared state within a test)
* Supports transaction isolaiton on the test level
* Sopports loading a "base" of data to share for all tests (uses into the IoC container with extensions)

[LiquibasePlugin](plugin/inject-liquibase-plugin/README.md)
* loading luquibase schema manager against registered datasources from datasource plugin
* Snapshot / restore function to get good throughput on large test suites (thousands of tests)

[JPAPlugin](plugin/inject-jpa-plugin/README.md) 
* Create Entity Managers (manages the lifecycle of JPA creation, easy to combine with Datasource)
* Utility to help with "flush/clear cache" testing problems to actaully ensure testing is done on the underlying SQL store
 
[SolrPlugin](plugin/inject-solr-plugin/README.md) 
* Creates a SolrJPlugin that starts and collects a solr server with cores against the Lifecycle management
* Uses a test "safe" copy of the solr config files depending on the lifecycle selected, starting cores against them
* Multi collection contribution by @anderslindsgard  

[JerseyPlugin](plugin/inject-jersey-plugin/README.md)
* Creates a Jersey (grizzly) in-memory connected test base and connects it to the Lifecycle management
* start/stop of the underlying server will honor the selected lifecycle, recommended usage is test_config lifecycle


## RELEASE NOTES

3.0.0 Reworked the entire framework to become plugin based
3.1.0 Upgraded all dependencies against Seal
3.2.0 Upgraded to JDK11 and Solr9

## RELEASE intentions

The intended release if 3.0.0 will go through a few more RCs

- RC1, mainly bugs from the beta releases 
- RC2, probably did stuff
- RC3, added kafka plugin support and noticed that activemq is designed strangely
- RC4, added solr multi collection support
- RC5, Jersey assertions now also works for non-embedded usecases
- RC6, build issues (travis and stuff changed)
- RC7, activemq assertions util added
- RC8, media-type supported in the call method 

Work needed before release
  - More glue code between plugins (activemq can be used with guice and spring and so on)
  - Improve documentation (read-me files)
  
Moved to next release  
  - Solve the bug with fork-join executor not working for default setup
  - Add kafka cluster testing support (starting many servers that works as a cluster)
  - Look at making JUnit 5 the default with better connectino to phases

### Release quick guide
###### Internal release

mvn -B versions:set -DnewVersion=$RELEASE_VERSION -DgenerateBackupPoms=false
mvn clean install
mvn deploy -Prelease-seal-nexus -Dmaven.test.skip=true


###### External release

* Install software to enable signing and publishing
Mac: 
```bash
brew install pgp
export GPG_TTY=$(tty)
```
Create a maven settings file with the following:
```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                          https://maven.apache.org/xsd/settings-1.0.0.xsd">
    <servers>
        <server>
            <id>ossrh</id>
            <username>${USERNAME}</username>
            <password>${PASSWORD}</password>
        </server>
    </servers>
</settings>

```



## Planned Resource Plugins, to come in a future not so far away
* Zookeeper ?
* h2sql fully supported (within datasource plugin)
* Guice support moved to "fully supported by all plugins"

## Planned development for lifecycle management (Planned for next patch release 3.1.x)
* Extend with more fine-grained control using JUnit5 as it has a lot more pre/post extensions built in.
- Pre/Post test (method)
- Pre/Post test class 
- Pre/Post config class
- Pre/Post test suite (If a suite is used)
- Pre/Post test runner (the complete execution of all tests)

