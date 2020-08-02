package org.example.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.example.dao.bean.ItemGeneral;

public interface ItemDao {
    ItemGeneral getByIdOrName(@Param("id") long id, @Param("name") String name);
}
