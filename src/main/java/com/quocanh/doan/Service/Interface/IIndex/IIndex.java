package com.quocanh.doan.Service.Interface.IIndex;

public interface IIndex {
    void rebuildIndex() throws InterruptedException;
    <T> void rebuildSpecificIndex(Class<T> entityClass, boolean purgeFirst);
    <T> void updateIndex(T entity);
}
