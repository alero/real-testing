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

package demo;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;
import java.io.IOException;

@Singleton
public class UserService {


    private final DataSource dataSource;
    private final SolrClient solrClient;
    private final JdbcTemplate jdbcTemplate;

    @Inject
    public UserService(DataSource dataSource, SolrClient solrClient) {
        this.dataSource = dataSource;
        this.solrClient = solrClient;
        jdbcTemplate = new JdbcTemplate(dataSource);
        init();
    }

    public void init() {
        synchronized (UserService.class){
            if( getName("root") == null){
                createUser("root", "pwd999");
            }
        }
    }

    public String getName() {
        return "SpringBeanName";
    }

    public String getName(String name) {
        try {
            return jdbcTemplate.queryForObject("select username from theUser where username=?", String.class, name);
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public Integer getLoginCount(String name) {
        try {
            return jdbcTemplate.queryForObject("select loginTries from theUser where username=?", Integer.class, name);
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public boolean checkPassword(String username, String password) {
        try {
            jdbcTemplate.queryForObject("select username from theUser where username=? and password=?", String.class, username, password);
            return true;
        }catch (EmptyResultDataAccessException e){
            return false;
        }
    }

    public boolean existsInIndex(String username) {
        SolrQuery solrQuery = new SolrQuery().setQuery("name:"+username) ;
        try {
            return 1 == solrClient.query(solrQuery).getResults().getNumFound();
        } catch (SolrServerException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createUser(String username, String password) {
        jdbcTemplate.update("insert into theUser (username, password, loginTries) values (?, ?, ?)", username, password, 0);
        SolrInputDocument document = new SolrInputDocument();
        document.addField("name", username);
        document.addField("password", password);
        try {
            solrClient.add(document);
            solrClient.commit(false, false, true);
        } catch (SolrServerException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean login(String username, String password) {
        jdbcTemplate.update("update theUser set loginTries = loginTries + 1");
        if(checkPassword(username, password)){
            jdbcTemplate.update("update theUser set loginTries = 0");
            return true;
        }
        return false;
    }

}
