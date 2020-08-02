package org.example.dao.bean;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ItemGeneral implements Serializable {
    private long item_general_id;
    private String item_name;
    private long item_id;
    private long item_variation_id;
    private long create_user_id;
    private long last_modify_user_id;
    private int is_deleted;
}
