package com.baymin.springboot.store.enumconstant.convert;

import java.util.Objects;

public interface IBaseDbEnum {

    /**
     * 用于显示的枚举名
     *
     * @return
     */
    String getName();

    /**
     * 存储到数据库的枚举值
     *
     * @return
     */
    int getIndex();

    /**
     * 按枚举的value获取枚举实例
     */
    static <T extends IBaseDbEnum> T fromValue(Class<T> enumType, int value) {
        for (T object : enumType.getEnumConstants()) {
            if (Objects.equals(value, object.getIndex())) {
                return object;
            }
        }
        throw new IllegalArgumentException("No enum value " + value + " of " + enumType.getCanonicalName());
    }

}
