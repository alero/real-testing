package org.hrodberaht.injection.plugin.junit.solr;

import org.apache.commons.io.FileUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.common.SolrException;
import org.apache.solr.core.CoreContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class SolrTestRunner {

    private static final PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
    private static final Logger LOG = LoggerFactory.getLogger(SolrTestRunner.class);
    private static Map<String,SolrRunnerHolder> CORE_CACHE = new ConcurrentHashMap<>();

    public static final String DEAFULT_HOME = "target/solr";

    private String home;
    private String coreName;


    private class SolrRunnerHolder{
        private final CoreContainer coreContainer;
        private final EmbeddedSolrServer solr;

        public SolrRunnerHolder(CoreContainer coreContainer, EmbeddedSolrServer solr) {
            this.coreContainer = coreContainer;
            this.solr = solr;
        }
    }

    public void setup() throws Exception {
        setup(DEAFULT_HOME);
    }

    public void setup(String solrHome) throws Exception {
        setup(solrHome, "collection1");

    }

    public void setup(String solrHome, String coreName) throws Exception {
        this.home = solrHome;
        this.coreName = coreName;
        setupSolr();
    }

    public SolrAssertions solrAssertions(){
        return new SolrAssertions(getServer());
    }

    public SolrClient getClient() {
        return getServer();
    }

    private void setupSolr() throws IOException {
        System.out.println(" ----------- SolrTestRunner setup --- STARTING! ");

        perpareSolrHomeAndStart();

    }

    private static void copyFile(final String copyToDir, final String resourceStream, final String fileName) throws IOException {
        final InputStream inputStream = patternResolver.getResource("classpath:" + resourceStream).getInputStream();
        new File(copyToDir).mkdirs();
        Files.copy(inputStream, new File(copyToDir, fileName).toPath(), REPLACE_EXISTING);
    }

    private boolean hasCoreConfigDir() {
        return new File(home, coreName).exists();
    }

    private void perpareSolrHomeAndStart() throws IOException {
        String runnerName = runnerName();
        CORE_CACHE.computeIfAbsent(runnerName, s -> {
            try {
                tearDown();
                moveConfigFiles();
                LOG.info("Loading Solr container {}", runnerName);
                return createSolrContainer(s);
            }catch (IOException e){
                LOG.error("Bad container", e);
                throw new RuntimeException(e);
            }
        });
    }

    private EmbeddedSolrServer getServer(){
        String runnerName = runnerName();
        return CORE_CACHE.get(runnerName).solr;
    }

    private SolrRunnerHolder createSolrContainer(String runnerName) {
        // TODO: figure out if its possible to create a reusable core/collection service cache
        CoreContainer coreContainer = new CoreContainer(home);
        coreContainer.load();
        LOG.info("Loading embedded container {}", runnerName);
        EmbeddedSolrServer solr = new EmbeddedSolrServer(coreContainer, coreName);
        LOG.info("Loading done {}", runnerName);
        return new SolrRunnerHolder(
                coreContainer, solr
        );
    }

    private String runnerName() {
        return home + "/" + coreName;
    }

    private boolean hasSolrConfigDir() {
        return new File(home, "solr.xml").exists();
    }

    public void cleanDataFromCollection() throws SolrServerException, IOException {
        try {
            cleanSolrInstance();
        } catch (SolrException e) {
            // might get "no such core" in the multi-core config
        }
    }

    public void cleanSolrInstance() throws SolrServerException, IOException {
        EmbeddedSolrServer solr = getServer();
        solr.deleteByQuery("*:*");
        solr.commit(true, true, false);
    }

    private void moveConfigFiles() throws IOException {

        moveSolrConfigFile(home, "./solr/solr.xml", "solr.xml");
        moveFiles(home, coreName);
        moveFiles(home, coreName + "/conf");

        try {
            moveFiles(home, coreName + "/conf/lang");
        } catch (UnsupportedOperationException e) {

        }
    }

    private void moveSolrConfigFile(String solrHome, String resourceStream, String fileName) throws IOException {
        copyFile(solrHome, resourceStream, fileName);
    }

    private void moveFiles(String solrHome, String path) throws IOException {

        for (String fileName : getResourceListing(SolrTestRunner.class, "solr/" + path)) {

            copyFile(solrHome + "/" + path, "./solr/" + path + "/" + fileName, fileName);

        }
    }


    private List<String> getResourceListing(Class<?> clazz, String path) throws IOException {
        List<String> foundFiles = new ArrayList<>();
        URL dirURL = clazz.getClassLoader().getResource(path);
        if (findFilesFromFilessystem(dirURL, foundFiles)) {
            LOG.debug("Found files on filesystem : "+foundFiles.size());
            return foundFiles;
        }

        if (dirURL == null) { // its probably in a jar file
            String me = clazz.getName().replace(".", "/") + ".class";
            dirURL = clazz.getClassLoader().getResource(me);
        }
        if (dirURL == null) {
            throw new UnsupportedOperationException("Cannot list files for URL " + dirURL);
        }

        if ("jar".equals(dirURL.getProtocol())) {
            /* A JAR path */
            String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); //strip out only the JAR file

            if(findJarFilesInPath(path, foundFiles, jarPath)){
                LOG.debug("Found files in jar-resource : "+foundFiles.size());
                return foundFiles;
            }
        }

        throw new UnsupportedOperationException("Found fo files using filesystem or jar parts for URL " + dirURL);

    }

    private boolean findFilesFromFilessystem(URL dirURL, List<String> foundFiles) throws IOException {
        if (dirURL != null && dirURL.getProtocol().equals("file")) {
            try {
                Stream.of(new File(dirURL.toURI()).listFiles()).forEach(file -> {
                    if (!file.isDirectory()) {
                        foundFiles.add(file.getName());
                    }
                });
                return true;
            } catch (URISyntaxException e) {
                throw new IOException(e);
            }
        }
        return false;
    }

    private boolean findJarFilesInPath(String path, List<String> foundFiles, String jarPath) throws IOException {
        try (JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"))) {
            Enumeration<JarEntry> entries = jar.entries();

            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                String name = jarEntry.getName();
                if (name.startsWith(path) && !jarEntry.isDirectory()) {
                    String entry = name.substring(path.length() + 1);
                    if (!entry.contains("/")) { // only add files on the same level
                        foundFiles.add(entry);

                    }
                }
            }
            return !foundFiles.isEmpty();
        }
    }


    private void tearDown() throws IOException {
        File coreDir = new File(home, coreName);
        if(coreDir.exists()) {
            LOG.info("cleaning : " + coreDir.getAbsolutePath());
            FileUtils.cleanDirectory(coreDir);
        }
    }


}
