package com.gl.elasticsearch.service;

import com.gl.elasticsearch.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleService {

    /**
     * 保存
     *
     * @param article
     */
    public void save(Article article);

    /**
     * 根据id查询文档数据
     *
     * @param id
     * @return
     */
    Article findOne(Integer id);

    /**
     * 根据 id 进行删除操作
     *
     * @param id
     */
    void delete(Integer id);

    /**
     * 查询分页数据
     *
     * @param pageable
     * @return
     */
    Page<Article> findPageData(Pageable pageable);

    /**
     * 排序分页查询
     *
     * @return
     */
    Iterable<Article> findAll();

    /**
     * 条件分页查询,根据标题
     *
     * @param title
     * @param pageable
     * @return
     */
    Page<Article> findByTitle(String title, Pageable pageable);
}
