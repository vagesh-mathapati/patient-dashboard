package com.opengov.dashboard.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class FilterObject {
    private Map<String, FilterCriteria> filters;
}