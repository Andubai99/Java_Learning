package com.sky.common;

import java.util.List;

public record PageResult<T>(long total, List<T> records) {
}
