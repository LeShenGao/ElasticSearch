package com.gl.elasticsearch.service.impl;

import com.gl.elasticsearch.dao.ArticleRepository;
import com.gl.elasticsearch.entity.Article;
import com.gl.elasticsearch.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gl
 * @create 2018-01-07-13:51
 */
@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;


    public void save(Article article) {
        articleRepository.save(article);
    }

    public Article findOne(Integer id) {
        return articleRepository.findOne(id);
    }

    public void delete(Integer id) {
        articleRepository.delete(id);
    }

    public Page<Article> findPageData(Pageable pageable) {
        return articleRepository.findAll(pageable);
    }

    public Iterable<Article> findAll() {
//        对id进行升序排序
        return articleRepository.findAll(new Sort(new Sort.Order(Sort.Direction.ASC, "id")));
    }

    public Page<Article> findByTitle(String title, Pageable pageable) {
        return articleRepository.findByTitle(title,pageable);
    }
}
