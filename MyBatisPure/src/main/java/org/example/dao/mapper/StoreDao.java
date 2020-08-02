package org.example.dao.mapper;

import org.example.dao.bean.Store;
import org.example.dao.bean.User;

public interface StoreDao {
    Store getById(long id);
    User getUser(long id);
}
