package com.convertlab.mongo.converter;

import com.mongodb.DBObject;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.LazyLoadingProxy;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.ClassUtils;

import java.util.Collection;
import java.util.Map;

public class TenantAwareMappingMongoConverter extends MappingMongoConverter {
    public TenantAwareMappingMongoConverter(DbRefResolver dbRefResolver, MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext) {
        super(dbRefResolver, mappingContext);
    }

    @Override
    public void write(Object obj, Bson bson) {

        if (null == obj) {
            return;
        }

        Class<?> entityType = ClassUtils.getUserClass(obj.getClass());
        TypeInformation<? extends Object> type = ClassTypeInformation.from(entityType);

        Object target = obj instanceof LazyLoadingProxy ? ((LazyLoadingProxy) obj).getTarget() : obj;

        writeInternal(target, bson, type);
        if (asMap(bson).containsKey("_id") && asMap(bson).get("_id") == null) {
            removeFromMap(bson, "_id");
        }

        addTenantIdToMap(bson);
        if (requiresTypeHint(entityType)) {
            typeMapper.writeType(type, bson);
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> asMap(Bson bson) {

        if (bson instanceof Document) {
            return (Document) bson;
        }

        if (bson instanceof DBObject) {
            return ((DBObject) bson).toMap();
        }

        throw new IllegalArgumentException(
                String.format("Cannot read %s. as map. Given Bson must be a Document or DBObject!", bson.getClass()));
    }

    private boolean requiresTypeHint(Class<?> type) {
        return !conversions.isSimpleType(type) && !ClassUtils.isAssignable(Collection.class, type)
                && !conversions.hasCustomWriteTarget(type, Document.class);
    }

    private static void removeFromMap(Bson bson, String key) {

        if (bson instanceof Document) {
            ((Document) bson).remove(key);
            return;
        }

        if (bson instanceof DBObject) {
            ((DBObject) bson).removeField(key);
            return;
        }

        throw new IllegalArgumentException(
                String.format("Cannot remove from %s. Given Bson must be a Document or DBObject.", bson.getClass()));
    }

    private static void addTenantIdToMap(Bson bson) {

        if (bson instanceof Document) {
            ((Document) bson).put("tenantId", "$tenantId");
            return;
        }

        if (bson instanceof DBObject) {
            ((DBObject) bson).put("tenantId","$tenantId");
            return;
        }

        throw new IllegalArgumentException(
                String.format("Cannot remove from %s. Given Bson must be a Document or DBObject.", bson.getClass()));
    }
}
