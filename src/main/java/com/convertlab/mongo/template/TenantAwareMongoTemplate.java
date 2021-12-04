package com.convertlab.mongo.template;

import com.convertlab.mongo.converter.TenantAwareMappingMongoConverter;
import com.convertlab.mongo.queryMapper.TenantAwareQueryMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

public class TenantAwareMongoTemplate extends MongoTemplate implements InitializingBean {
    private TenantAwareQueryMapper tenantAwareQueryMapper;

    public TenantAwareMongoTemplate(MongoDatabaseFactory mongoDbFactory, TenantAwareMappingMongoConverter tenantAwareMappingMongoConverter,
                                    TenantAwareQueryMapper tenantAwareQueryMapper) {

        super(mongoDbFactory, tenantAwareMappingMongoConverter);
        this.tenantAwareQueryMapper = tenantAwareQueryMapper;
    }

    @Override
    public void afterPropertiesSet(){
        super.setTenantAwareQueryOperations(tenantAwareQueryMapper);
    }
}
