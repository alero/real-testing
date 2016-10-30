# Real testing using CDI-Light and JUnit

The test below will create a reusable Datasource that can be injected as @Resources in any regular CDI class.
This datasource will share a HSQL database (default database version) during the execution of the tests

The following row is the one that actually registers the resource and makes it available to the JUnit test.
*addResource(dataSourceName, dataSource);*  

The configuration also registers two different CDI beans. 
The default instance handling for the CDI version is instance(new) so the singleton needs to be defined when adding beans. (can be annotated as well)

 > Example class for a CDI configuration (can be found in the test suite)
  
    public class CDIContainerConfigExample extends JUnitStreamCDIContainerConfig {
   
       public CDIContainerConfigExample() {
           String dataSourceName = "MyDataSource";
           DataSource dataSource = createDataSource(dataSourceName);          
           addResource(dataSourceName, dataSource);           
       }
   
       @Override
       protected void registerStream(CDIInjectionRegistryStream stream) {
           stream
               .register(registrations -> {
                   registrations.register(CDIServiceInterface.class).with(CDIServiceInterfaceImpl.class);
                   registrations.register(ConstantClassLoadedPostContainer.class).scopeAs(ScopeContainer.Scope.SINGLETON);
               });
       }
    }
   
   
The actual test it self needs to enabled the real testing framework by adding a @Runwith and a @ContainerContext.

The @RunWith extends the JUnit framework and enhances the beans to enabled Injection.

The @ContainerContext points to a configuration like the one above. 
There can be only one configuration for a runner, if there is a need to vary the configuration there are ways to do this in the container itself and use normal Java inheritence in the config creation. 
This limitation is intentional to avoid complex configurations to spread into each test class. 

The ContainerLifeCycleTestUtil is a special tool that is enabled when using the @ContainerContext.
This tool can be used to replace and get components in the IoC Container for each test. 
All manipluations done using the tool is limited to the test that executes, all other tests are protected from the manipluation of the components that can be done.

The sample test below showcases a few of the features the tool can do.
1. using a basic get to verify that singleton lifecycle is fulfilled.
2. replacing a service and using it with different results (enabled deep mocking without changes in the codebase, this is core in the real-testing idea)

> Example Unit test using the @RunWith and a configuration via @ContainerContext, enabling the use of @Inject on the test

    @ContainerContext(CDIContainerConfigExample.class)
    @RunWith(JUnitRunner.class)
    public class TestCDIServiceContext {
    
    
        @Inject
        private CDIServiceInterface anInterface;
    
        @Inject
        private ContainerLifeCycleTestUtil containerLifeCycleTestUtil;
    
        @Test
        public void testWiring() {
            String something = anInterface.findSomething(12L);
            assertEquals("Something", something);
    
            String somethingDeep = anInterface.findSomethingDeep(12L);
            assertEquals("initialized", somethingDeep);
    
            assertTrue(
                    containerLifeCycleTestUtil.getService(CDIServiceInterface.class)
                    ==
                    containerLifeCycleTestUtil.getService(CDIServiceInterface.class));

            containerLifeCycleTestUtil.registerServiceInstance(CDIServiceInterface.class, new CDIServiceInterface(){
    
                @Override
                public String findSomething(long l) {
                    return "Mocking";
                }
    
                @Override
                public String findSomethingDeep(long l) {
                    return "DeepMocking";
                }
            });
    
            assertEquals("Mocking", containerLifeCycleTestUtil.getService(CDIServiceInterface.class)
                    .findSomething(14L));
    
            assertEquals("DeepMocking", containerLifeCycleTestUtil.getService(CDIServiceInterface.class)
                    .findSomethingDeep(14L));
    
        }
     }