package com.gl.elasticSearch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gl.elasticSearch.domain.Article;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.highlight.HighlightField;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * elasticSearch入门案例
 *
 * @author gl
 * @create 2018-01-04-21:49
 */
public class ElasticSearchHelloWorldTest {
    /**
     * 创建索引,文档,并将文档保存到索引中
     *
     * @throws IOException
     */
    @Test
    public void test() throws IOException {
//        创建集群连接
        Client client = TransportClient.builder().build().addTransportAddresses(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
//        创建添加的对象
//        创建对象要添加对象的JSON格式的字符串
        XContentBuilder xContentBuilder = XContentFactory
                .jsonBuilder()
                .startObject()
                .field("id", 1)
                .field("title", "ElasticSearch是一个基于lucene的分布式搜索引擎")
                .field("content", "Apache Lucene将所有信息写到一个称为倒排索引（inverted index）的结构中。不同于关系型\n" +
                        "数据库中表的处理方式，倒排索引建立索引中词和文档之间的映射。你可以把倒排索引看成这样\n" +
                        "一种数据结构，其中的数据是面向词而不是面向文档的。")
                .endObject();
//        建立文档读写            索引          文档类型        文档个数
        client.prepareIndex("blog1", "article", "1").setSource(xContentBuilder).get();

//        关闭连接
        client.close();
    }

    /**
     * 查询文档对象
     *
     * @throws UnknownHostException
     */
    @Test
    public void test1() throws UnknownHostException {
//        创建集群连接
        Client client = TransportClient.builder().build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

//        搜索数据
        SearchResponse searchResponse = client.prepareSearch("blog1")
                .setTypes("article").setQuery(QueryBuilders.matchAllQuery())
                .get();// get() == execute().actionGet()

        printSearchResponse(searchResponse);

//        关闭资源
        client.close();
    }

    @Test
    public void test2() throws UnknownHostException {
        Client client = TransportClient.builder().build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

        SearchResponse searchResponse = client.prepareSearch("blog1")
                .setTypes("article").setQuery(QueryBuilders.queryStringQuery("全水")).get();

        printSearchResponse(searchResponse);

        client.close();
    }

    @Test
    public void test3() throws UnknownHostException {
        Client client = TransportClient.builder().build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

        SearchResponse searchResponse = client.prepareSearch("blog1")
                .setTypes("article").setQuery(QueryBuilders.termQuery("content", "面向")).get();

        printSearchResponse(searchResponse);

        client.close();
    }

    /**
     * 模糊查询
     *
     * @throws UnknownHostException
     */
    @Test
    public void test4() throws UnknownHostException {
        Client client = TransportClient.builder().build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

        SearchResponse searchResponse = client.prepareSearch("blog1")
                .setTypes("article").setQuery(QueryBuilders.wildcardQuery("content", "*信息*")).get();

        printSearchResponse(searchResponse);

        client.close();
    }

    /**
     * 对索引的操作
     *
     * @throws UnknownHostException
     */
    @Test
    public void test5() throws UnknownHostException {
        Client client = TransportClient.builder().build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

//        添加索引
//        client.admin().indices().prepareCreate("blog3").get();

//        删除索引
        client.admin().indices().prepareDelete("blog3").get();

        client.close();
    }

    /**
     * 给索引添加映射配置
     *
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void test6() throws IOException, ExecutionException, InterruptedException {
        Client client = TransportClient.builder().build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder().startObject()
                .startObject("article").startObject("properties")
                .startObject("id").field("type", "integer")
                .field("store", "yes").endObject()
                .startObject("title").field("type", "string").field("store", "yes")
                .field("analyzer", "ik").endObject().startObject("content")
                .field("type", "string").field("store", "yes")
                .field("analyzer", "ik").endObject().endObject().endObject().endObject();

        PutMappingRequest mapping = Requests.putMappingRequest("blog3").type("article").source(xContentBuilder);

        client.admin().indices().putMapping(mapping).get();

        client.close();
    }

    /**
     * 创建文档数据
     */
    @Test
    public void test7() throws UnknownHostException, JsonProcessingException {
//        创建与服务器的连接
        Client client = TransportClient.builder().build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

//        创建文档对象
        Article article = new Article();
        article.setId(2);
        article.setTitle("搜索工作的创建是非常困难的。");
        article.setContent("我们希望搜索解决方案要运行速度快，我们希望能有一个零配置和一个完全免费的搜索模式，我们希望能够简单地使用JSON通过HTTP来索引数据，我们希望我们的搜索服务器始终可用，我们希望能够从一台开始并扩展到数百台，我们要实时搜索，我们要简单的多租户，我们希望建立一个云的解决方案。因此我们利用Elasticsearch来解决所有这些问题以及可能出现的更多其它问题。");

//        创建 jackson对象
        ObjectMapper objectMapper = new ObjectMapper();

//        建立文档
        client.prepareIndex("blog2", "article", article.getId().toString())
                .setSource(objectMapper.writeValueAsString(article)).get();

        client.close();

    }

    /**
     * 修改文档数据
     */
    @Test
    public void test8() throws UnknownHostException, JsonProcessingException, ExecutionException, InterruptedException {
        Client client = TransportClient.builder().build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

        Article article = new Article();
        article.setId(2);
        article.setTitle("搜索工作的创建是非常快乐的。");
        article.setContent("我们希望搜索解决方案要运行速度快，我们希望能有一个零配置和一个完全免费的搜索模式，我们希望能够简单地使用JSON通过HTTP来索引数据，我们希望我们的搜索服务器始终可用，我们希望能够从一台开始并扩展到数百台，我们要实时搜索，我们要简单的多租户，我们希望建立一个云的解决方案。因此我们利用Elasticsearch来解决所有这些问题以及可能出现的更多其它问题。");

        ObjectMapper objectMapper = new ObjectMapper();

//        修改方式二
        client.update(new UpdateRequest("blog2", "article", article.getId().toString()).doc(objectMapper.writeValueAsString(article))).get();

        client.close();

    }

    /**
     * 删除文档操作
     */
    @Test
    public void test9() throws UnknownHostException, ExecutionException, InterruptedException, JsonProcessingException {
        Client client = TransportClient.builder().build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

        Article article = new Article();
        article.setId(2);
        article.setTitle("搜索工作的创建是非常快乐的。");
        article.setContent("我们希望搜索解决方案要运行速度快，我们希望能有一个零配置和一个完全免费的搜索模式，我们希望能够简单地使用JSON通过HTTP来索引数据，我们希望我们的搜索服务器始终可用，我们希望能够从一台开始并扩展到数百台，我们要实时搜索，我们要简单的多租户，我们希望建立一个云的解决方案。因此我们利用Elasticsearch来解决所有这些问题以及可能出现的更多其它问题。");

        ObjectMapper objectMapper = new ObjectMapper();

//        删除文档
//        client.prepareDelete("blog2", "article", article.getId().toString()).get();

        client.delete(new DeleteRequest("blog2", "article", article.getId().toString())).get();
        client.close();
    }

    /**
     * 批量添加文档数据
     */
    @Test
    public void test10() throws UnknownHostException, JsonProcessingException {
        Client client = TransportClient.builder().build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

        ObjectMapper objectMapper = new ObjectMapper();

        for (int i = 1; i <= 100; i++) {
            Article article = new Article();
            article.setId(i);
            article.setTitle(i + "搜索工作的创建是非常快乐的。");
            article.setContent(i + "我们希望搜索解决方案要运行速度快，我们希望能有一个零配置和一个完全免费的搜索模式，我们希望能够简单地使用JSON通过HTTP来索引数据，我们希望我们的搜索服务器始终可用，我们希望能够从一台开始并扩展到数百台，我们要实时搜索，我们要简单的多租户，我们希望建立一个云的解决方案。因此我们利用Elasticsearch来解决所有这些问题以及可能出现的更多其它问题。");

            client.prepareIndex("blog2", "article", article.getId().toString()).setSource(objectMapper.writeValueAsString(article)).get();
        }
        client.close();
    }

    /**
     * 文档数据的查询
     */
    @Test
    public void test11() throws UnknownHostException {
        Client client = TransportClient.builder().build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("blog2").setTypes("article").setQuery(QueryBuilders.matchAllQuery());

//        查询设置分页标准
        searchRequestBuilder.setFrom(20).setSize(20);

//        执行搜索,得到相应结果
        SearchResponse searchResponse = searchRequestBuilder.get();

//        打印响应结果信息
        printSearchResponse(searchResponse);

        client.close();
    }

    /**
     * 指定查询到的数据 高亮显示
     */
    @Test
    public void test12() throws IOException {
        Client client = TransportClient.builder().build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

//        创建 jackson对象:将对象转换为JSON数据
        ObjectMapper objectMapper = new ObjectMapper();

//        搜索数据
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("blog2").setTypes("article").setQuery(QueryBuilders.termQuery("title", "搜索"));

//        高亮定义
        searchRequestBuilder.addHighlightedField("title");//对title字段进行高亮显示
        searchRequestBuilder.setHighlighterPreTags("<em>");//前置元素
        searchRequestBuilder.setHighlighterPostTags("</em>");//后置元素

//        获取查询后,响应的结果数据
        SearchResponse searchResponse = searchRequestBuilder.get();

        SearchHits hits = searchResponse.getHits();
        System.out.println("总记录数:" + hits.getTotalHits());
        Iterator<SearchHit> iterator = hits.iterator();
        while (iterator.hasNext()) {
//            每个查询对象
            SearchHit searchHit = iterator.next();

//            将高亮处理后内容,替换原有内容,(原有内容,可能会出现显示不全)
            Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();

            HighlightField titleField = highlightFields.get("title");

//            获取到原有内容中,每个高亮显示集中位置 fragment :就是高亮片段
            Text[] fragments = titleField.fragments();
            String title = "";
            for (Text text : fragments) {
                title += text;
            }

//            将查询结果转换为对象
            Article article = objectMapper.readValue(searchHit.getSourceAsString(), Article.class);

//            用高亮的内容,替换原有的内容
            article.setTitle(title);

            System.out.println(article);
        }

        client.close();
    }

    /**
     * 打印搜索后的响应结果
     *
     * @param searchResponse
     */
    private void printSearchResponse(SearchResponse searchResponse) {
        SearchHits hits = searchResponse.getHits();
        System.out.println("总记录数:" + hits.getTotalHits());
        Iterator<SearchHit> iterator = hits.iterator();
        while (iterator.hasNext()) {
//            每个查询对象
            SearchHit searchHit = iterator.next();
//            获取字符串格式数据打印
            System.out.println(searchHit.getSourceAsString());
//            获取对应字段数据
            System.out.println("title:" + searchHit.getSource().get("title"));
        }
    }
}
