package com.example.products.datasource.specification;

import com.course.graphql.generated.types.SortDirection;
import com.course.graphql.generated.types.SortInput;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

public abstract class BaseSpecification {

    protected static String contains(String keyword) {
        return MessageFormat.format("%{0}%", keyword);
    }

    public static List<Sort.Order> sortOrdersFrom(List<SortInput> sortInputList) {
        if (CollectionUtils.isEmpty(sortInputList)) {
            return Collections.emptyList();
        }

        return sortInputList.stream()
                .map(
                        sortInput -> sortInput.getDirection().equals(SortDirection.ASCENDING) ?
                                Sort.Order.asc(sortInput.getField()) : Sort.Order.desc(sortInput.getField())
                )
                .toList();
    }
}
