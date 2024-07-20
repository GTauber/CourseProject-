package com.ead.authuser.models.query.filter;

import com.ead.authuser.models.query.Filterable;
import com.ead.authuser.models.query.annotation.FilterableField;
import lombok.Data;
import lombok.Getter;

@Getter(onMethod_ = {@FilterableField})
@Data
public class UserFilterable implements Filterable {
    private String userType;
    private String userStatus;
    private String email;
}
