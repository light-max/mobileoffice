package com.lfq.mobileoffice.constant;

/**
 * 自定义断言类
 *
 * @author 李凤强
 */
public interface Assert {
    default void newException() {
        throw new AssertException(getMessage());
    }

    String getMessage();

    default void isTrue(boolean b) {
        if (!b) {
            newException();
        }
    }

    default void isFalse(boolean b) {
        if (b) {
            newException();
        }
    }

    default void notNull(Object object) {
        if (object == null) {
            newException();
        }
    }

    default void isNull(Object object) {
        if (object != null) {
            newException();
        }
    }
}
