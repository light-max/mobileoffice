package com.lfq.mobileoffice.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 断言异常类，继承运行时异常{@link RuntimeException}
 */
@AllArgsConstructor
@Getter
public class AssertException extends RuntimeException {
    private final String message;
}
