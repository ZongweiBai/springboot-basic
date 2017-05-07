package com.baymin.springboot.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.List;

/**
 * Created by Baymin on 2017/5/7.
 */
public interface IRedisService {

    boolean set(String key, String value);

    String get(String key);

    boolean expire(String key, long expire);

    <T> boolean setList(String key, List<T> list) throws JsonProcessingException;

    <T> List<T> getList(String key) throws IOException;

    long lpush(String key, Object obj) throws JsonProcessingException;

    long rpush(String key, Object obj) throws JsonProcessingException;

    String lpop(String key);

}
