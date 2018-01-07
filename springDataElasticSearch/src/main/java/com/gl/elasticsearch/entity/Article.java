package com.gl.elasticsearch.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author gl
 * @create 2018-01-07-12:45
 */
@Document(indexName = "blog3", type = "article")
public class Article {
    @Id
    @Field(index = FieldIndex.not_analyzed, store = true, type = FieldType.Integer)
    private Integer id;
    @Field(index = FieldIndex.analyzed, analyzer = "ik", store = true, searchAnalyzer = "ik", type = FieldType.String)
    private String title;
    @Field(index = FieldIndex.analyzed, analyzer = "ik", store = true, searchAnalyzer = "ik", type = FieldType.String)
    private String content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
