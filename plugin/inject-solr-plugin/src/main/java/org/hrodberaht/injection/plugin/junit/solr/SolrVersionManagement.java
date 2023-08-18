package org.hrodberaht.injection.plugin.junit.solr;

import org.apache.solr.core.CoreContainer;

import java.nio.file.Paths;
import java.util.Properties;

public class SolrVersionManagement {

    public static CoreContainer createContainer(String home){
        return new CoreContainer(Paths.get(home).toAbsolutePath(), new Properties());
    }

}
