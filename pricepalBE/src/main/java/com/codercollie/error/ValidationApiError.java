package com.codercollie.error;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record ValidationApiError(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, List<String>> fieldErrors
) {
    public ValidationApiError(ApiError apiError, Map<String, List<String>> fieldErrors) {
        this(
                apiError.timestamp(),
                apiError.status(),
                apiError.error(),
                apiError.message(),
                apiError.path(),
                fieldErrors
        );
    }
}
