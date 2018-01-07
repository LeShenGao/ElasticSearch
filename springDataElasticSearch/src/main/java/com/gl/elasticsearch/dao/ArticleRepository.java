package com.gl.elasticsearch.dao;

import com.gl.elasticsearch.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author gl
 * @create 2018-01-07-12:46
 */
public interface ArticleRepository extends ElasticsearchRepository<Article, Integer> {

    /**
     * 根据标题条件,进行条件分页查询
     *
     * @param title
     * @param pageable
     * @return
     */
    Page<Article> findByTitle(String title, Pageable pageable);
}
