package org.hrodberaht.injection.extensions.junit.internal.embedded;

import java.io.File;

public class PersistenceResource {

    private String script;

    public PersistenceResource(String script) {
        this.script = script;
    }

    public boolean exists() {
        File aFile = new File(script);
        return aFile.exists();
    }

    public String getName() {
        return script;
    }
}
