package com.ead.authuser.repository;

import static com.ead.authuser.utils.MethodUtils.getAccessorMethod;
import static com.ead.authuser.utils.MethodUtils.toSnakeCase;

import com.ead.authuser.models.entity.UserModel;
import com.ead.authuser.models.exceptions.ApplicationException;
import com.ead.authuser.models.query.Filterable;
import com.ead.authuser.models.query.annotation.FilterableField;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GenericFilteringRepository {

    private final R2dbcEntityTemplate databaseClient;

    public <T> Mono<Page<T>> findAllWithFilter(Filterable filterable, Pageable pageable, Class<T> entityClass) {
        var criteria = buildCriteria(filterable);

        return databaseClient
            .select(Query.query(criteria).with(pageable), entityClass)
            .collectList()
            .zipWith(count(filterable))
            .map(tuple -> new PageImpl<>(tuple.getT1(), pageable, tuple.getT2()));
    }

    private Criteria buildCriteria(Filterable filterable) {
        return Stream.of(filterable.getClass().getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(FilterableField.class))
            .map(field -> buildCriteriaPart(field, getAccessorMethod(field), filterable))
            .filter(Objects::nonNull)
            .reduce(Criteria::and)
            .orElse(Criteria.empty());
    }

    private Criteria buildCriteriaPart(Field field, Method method, Filterable filterable) {
        try {
            var value = method.invoke(filterable);
            if (value == null) {
                return null;
            }
            var fieldName = toSnakeCase(field.getName());
            if (method.getReturnType() == String.class) {
                return buildStringCriteria(fieldName, (String) value);
            }
            return Criteria.where(fieldName).is(value);
        } catch (Exception e) {
            throw new ApplicationException();
        }
    }

    private Criteria buildStringCriteria(String fieldName, String value) {
        BiFunction<String, String, Criteria> likeFunc = (path, val) -> Criteria.where(path).like("%" + val + "%");
        return likeFunc.apply(fieldName, value);
    }

    private Mono<Long> count(Filterable filterable) {
        var criteria = buildCriteria(filterable);
        return databaseClient.count(Query.query(criteria), UserModel.class);
    }

}
