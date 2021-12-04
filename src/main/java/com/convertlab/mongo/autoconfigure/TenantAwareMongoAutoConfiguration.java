package com.convertlab.mongo.autoconfigure;

import com.convertlab.library.condition.MultiTenancyEnableCondition;
import com.convertlab.mongo.converter.TenantAwareMappingMongoConverter;
import com.convertlab.mongo.queryMapper.TenantAwareQueryMapper;
import com.convertlab.mongo.template.TenantAwareMongoTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@Configuration(proxyBeanMethods = false)
@Conditional(MultiTenancyEnableCondition.class)
public class TenantAwareMongoAutoConfiguration {

    @Bean
    TenantAwareMappingMongoConverter tenantAwareMappingMongoConverter(
            MongoDatabaseFactory factory, MongoMappingContext context, MongoCustomConversions conversions) {

        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        TenantAwareMappingMongoConverter tenantAwareMappingMongoConverter = new TenantAwareMappingMongoConverter(dbRefResolver, context);
        tenantAwareMappingMongoConverter.setCustomConversions(conversions);
        return tenantAwareMappingMongoConverter;
    }

    @Bean
    TenantAwareQueryMapper tenantAwareQueryMapper(TenantAwareMappingMongoConverter tenantAwareMappingMongoConverter) {
        return new TenantAwareQueryMapper(tenantAwareMappingMongoConverter);
    }

    @Bean
    TenantAwareMongoTemplate tenantAwareMongoTemplate(
            MongoDatabaseFactory factory, TenantAwareMappingMongoConverter tenantAwareMappingMongoConverter,TenantAwareQueryMapper tenantAwareQueryMapper) {

        return new TenantAwareMongoTemplate(factory, tenantAwareMappingMongoConverter,tenantAwareQueryMapper);
    }

}
