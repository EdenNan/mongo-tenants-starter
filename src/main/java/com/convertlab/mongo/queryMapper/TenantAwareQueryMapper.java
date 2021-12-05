package com.convertlab.mongo.queryMapper;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.convert.QueryMapper;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.lang.Nullable;

public class TenantAwareQueryMapper extends QueryMapper {
    /**
     * Creates a new {@link QueryMapper} with the given {@link MongoConverter}.
     *
     * @param converter must not be {@literal null}.
     */
    public TenantAwareQueryMapper(MongoConverter converter) {
        super(converter);
    }

    @Override
    public Document getMappedObject(Bson query, @Nullable MongoPersistentEntity<?> entity) {
        Document document = super.getMappedObject(query, entity);
        document.put("tenantId", "$tenantId");
        return document;
    }
}
