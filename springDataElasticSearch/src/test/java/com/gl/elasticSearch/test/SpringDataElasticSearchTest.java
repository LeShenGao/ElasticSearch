package com.gl.elasticSearch.test;

import com.gl.elasticsearch.entity.Article;
import com.gl.elasticsearch.service.ArticleService;
import org.elasticsearch.client.Client;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author gl
 * @create 2018-01-07-14:47
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:applicationContext.xml")
public class SpringDataElasticSearchTest {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private Client client;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Test
    public void test1() {
        elasticsearchTemplate.createIndex(Article.class);
        elasticsearchTemplate.putMapping(Article.class);
    }

    /**
     * 添加操作
     */
    @Test
    public void test2() {
        Article article = new Article();
        article.setId(10010);
        article.setTitle("基于Lucene的搜索服务器");
        article.setContent("设计用于云计算中，能够达到实时搜索，稳定，可靠，快速，安装使用方便。");
        articleService.save(article);
    }

    /**
     * 修改操作
     */
    @Test
    public void test3() {
        Article article = new Article();
        article.setId(10010);
        article.setTitle("基于Lucene的分布式搜索服务器");
        article.setContent("设计用于云计算中，能够达到实时搜索，稳定，可靠，快速，安装使用方便。");
        articleService.save(article);
    }

    /**
     * 查询操作
     */
    @Test
    public void test4() {
        System.out.println(articleService.findOne(10010));
    }

    /**
     * 删除操作
     */
    @Test
    public void test5() {
        articleService.delete(10010);
    }

    /**
     * 批量添加数据
     */
    @Test
    public void test7() {
        for (int i = 1; i <= 100; i++) {
            Article article = new Article();
            article.setId(i);
            article.setTitle(i + "基于Lucene的分布式搜索服务器");
            article.setContent(i + "设计用于云计算中，能够达到实时搜索，稳定，可靠，快速，安装使用方便。");
            articleService.save(article);
        }
    }

    /**
     * 查询分页数据
     */
    @Test
    public void test6() {
        Pageable pageable = new PageRequest(0, 10);
        Page<Article> pageData = articleService.findPageData(pageable);
        for (Article article : pageData.getContent()) {
            System.out.println(article);
        }
    }

    /**
     * 排序分页查询
     */
    @Test
    public void test8() {
        Iterable<Article> iterable = articleService.findAll();
        for (Article article : iterable) {
            System.out.println(article);
        }
    }

    /**
     * 条件分页查询
     */
    @Test
    public void test9() {
//        查询标题中含有 基于 的 1- 10条数据
        Pageable pageable = new PageRequest(0, 10);

        Page<Article> pageData = articleService.findByTitle("基于", pageable);

        System.out.println("总记录数据:" + pageData.getTotalElements());

//        遍历查询到的数据
        for (Article article : pageData.getContent()) {
            System.out.println(article);
        }
    }
}
