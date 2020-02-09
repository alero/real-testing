package demo;

import org.apache.solr.client.solrj.SolrClient;
import org.hrodberaht.injection.core.stream.InjectionRegistryBuilder;
import org.hrodberaht.injection.plugin.junit.ContainerContext;
import org.hrodberaht.injection.plugin.junit.ContainerContextConfigBase;
import org.hrodberaht.injection.plugin.junit.JUnit4Runner;
import org.hrodberaht.injection.plugin.junit.api.resource.ResourceProviderBuilder;
import org.hrodberaht.injection.plugin.junit.plugins.DataSourcePlugin;
import org.hrodberaht.injection.plugin.junit.plugins.GuiceExtensionPlugin;
import org.hrodberaht.injection.plugin.junit.plugins.SolrJPlugin;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.sql.DataSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@ContainerContext(GuiceUserServiceTest.LocalConfig.class)
@RunWith(JUnit4Runner.class)
public class GuiceUserServiceTest {

    @Inject
    private UserService userService;

    @Test
    public void testForCreateUser() {
        assertNull(userService.getName("testing"));
        assertFalse(userService.existsInIndex("testing"));
        userService.createUser("testing", "password");

        assertNotNull(userService.getName("testing"));

        assertEquals(Integer.valueOf(0), userService.getLoginCount("testing"));

        userService.login("testing", "badpassword");

        assertEquals(Integer.valueOf(1), userService.getLoginCount("testing"));


        assertTrue(userService.existsInIndex("testing"));

    }


    public static class LocalConfig extends ContainerContextConfigBase {
        @Override
        public void register(InjectionRegistryBuilder registryBuilder) {

            DataSourcePlugin dataSourcePlugin = activatePlugin(DataSourcePlugin.class);
            DataSource dataSource = dataSourcePlugin.createDataSource();

            dataSourcePlugin.loadSchema(dataSource, "sql");

            SolrJPlugin solrJPlugin = activatePlugin(SolrJPlugin.class)
                    .solrHome(SolrJPlugin.DEFAULT_HOME)
                    .coreName("collection1");

            activatePlugin(GuiceExtensionPlugin.class)
                    .with(dataSourcePlugin)
                    .withResources(ResourceProviderBuilder.of(SolrClient.class, solrJPlugin::getClient))
                    .guiceModules(new GuiceConfig());


        }
    }
}