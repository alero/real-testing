package org.hrodberaht.injection.plugin.junit.service.sample;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class ArticlesStore {

    private Map<String,Article> articles = new HashMap<>(10000000);

    public void save(Article article) {
        articles.put(article.getId(),article);
    }

    public Collection<Article> getAll() {
        return articles.values();
    }

    public Article get(String id) {
        return articles.get(id);
    }

    public long getSize() {
        return articles.size();
    }

    public void delete(String key) {
        articles.remove(key);
    }

    public void clean() {
        articles.clear();
    }
}
