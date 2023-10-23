/*
 * Copyright (c) 2017 org.hrodberaht
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hrodberaht.injection.plugin.junit.jersey.jakarta;

import org.glassfish.jersey.client.JerseyClient;

public class JerseyBuilder {

    private final String url;
    private final JerseyClient jerseyClient;

    public JerseyBuilder(String url, JerseyClient jerseyClient) {
        this.url = url;
        this.jerseyClient = jerseyClient;
    }

    public String getUrl() {
        return url;
    }

    public JerseyClientRestassuredResponse get(String path) {
        return new JerseyClientRestassuredRequest(jerseyClient, url).get(path);
    }

    public JerseyClientRestassuredResponse get(String path, Object... values) {
        return new JerseyClientRestassuredRequest(jerseyClient, url).get(path, values);
    }

    public JerseyClientRestassuredRequest given() {
        return new JerseyClientRestassuredRequest(jerseyClient, url);
    }


}
