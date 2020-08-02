package org.example.dao.bean;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Builder
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserWithStore implements Serializable {
    private long user_id;
    private int type;
    private int status;
    private Date create_time;
    private int create_user_id;
    private Date last_modify_time;
    private int last_modify_user_id;
    private int is_deleted;
    private List<Store> storeList;
}
