package com.baymin.springboot.store.enumconstant.convert;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.DynamicParameterizedType;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;
import java.util.Properties;

public class DbEnumType implements UserType, DynamicParameterizedType {

    private Class enumClass;
    private static final int[] SQL_TYPES = new int[]{Types.INTEGER};

    @Override
    public void setParameterValues(Properties parameters) {
        final ParameterType reader = (ParameterType) parameters.get(PARAMETER_TYPE);
        if (reader != null) {
            enumClass = reader.getReturnedClass().asSubclass(Enum.class);
        }
    }

    /**
     * 枚举存储int值
     * @return
     */
    @Override
    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    @Override
    public Class returnedClass() {
        return enumClass;
    }

    /**
     * 是否相等，不相等会触发JPA update操作
     * @param x
     * @param y
     * @return
     * @throws HibernateException
     */
    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        if (x == null && y == null) {
            return true;
        }
        if ((x == null && y != null) || (x != null && y == null)) {
            return false;
        }
        return x.equals(y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x == null ? 0 : x.hashCode();
    }

    /**
     * 返回枚举
     *
     * @param resultSet
     * @param names
     * @param sharedSessionContractImplementor
     * @param o
     * @return
     * @throws HibernateException
     * @throws SQLException
     */
    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] names, SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException, SQLException {
        String value = resultSet.getString(names[0]);
        if (value == null) {
            return null;
        }
        for (Object object : enumClass.getEnumConstants()) {
            if (Objects.equals(Integer.parseInt(value), ((IBaseDbEnum) object).getIndex())) {
                return object;
            }
        }
        throw new RuntimeException(String.format("Unknown name value [%s] for enum class [%s]", value, enumClass.getName()));
    }

    /**
     * 保存枚举值
     *
     * @param preparedStatement
     * @param value
     * @param index
     * @param sharedSessionContractImplementor
     * @throws HibernateException
     * @throws SQLException
     */
    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index, SharedSessionContractImplementor sharedSessionContractImplementor) throws HibernateException, SQLException {
        if (value == null) {
            preparedStatement.setNull(index, SQL_TYPES[0]);
        } else if (value instanceof Integer) {
            preparedStatement.setInt(index, (Integer) value);
        } else {
            preparedStatement.setInt(index, ((IBaseDbEnum) value).getIndex());
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

}
