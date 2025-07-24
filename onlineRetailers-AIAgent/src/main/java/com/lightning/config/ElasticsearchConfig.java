package com.lightning.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;

@Configuration
public class ElasticsearchConfig {

    // 声明 RestClient 单例 Bean，跳过证书验证
    @Bean(destroyMethod = "close")
    public RestClient restClient() throws Exception {
        SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(null, TrustAllStrategy.INSTANCE) // 信任所有证书
                .build();

        //构造认证信息
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                AuthScope.ANY,
                new UsernamePasswordCredentials("elastic", "-xBu6Qc=dK*I08NiEr+w")
        );

        RestClientBuilder builder = RestClient.builder(new HttpHost("localhost", 9200, "https"))
                .setHttpClientConfigCallback(httpClientBuilder ->
                        httpClientBuilder
                                .setSSLContext(sslContext)                     // 设置 SSLContext
                                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE) // 跳过主机名验证
                                .setDefaultCredentialsProvider(credentialsProvider) // 添加认证
                );

        return builder.build();
    }

    @Bean
    public ElasticsearchTransport elasticsearchTransport(RestClient restClient) {
        return new RestClientTransport(restClient, new JacksonJsonpMapper());
    }

    @Bean
    public ElasticsearchClient elasticsearchClient(ElasticsearchTransport transport) {
        return new ElasticsearchClient(transport);
    }
}
