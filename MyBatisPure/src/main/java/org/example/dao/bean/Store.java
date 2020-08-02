package org.example.dao.bean;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 店铺信息
 */
@Builder
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Store implements Serializable {
    private Long storeId;

    /**
     * 所属用户ID
     */
    private Long userId;

    /**
     * 关联的用户信息
     */
    private User user;

    /**
     * 店铺类型
     */
    private int type;

    private int status;

    private Date createTime;

    private Long createUserId;

    private Date lastModifyTime;

    private Long lastModifyUserId;

    private Long isDeleted;

    private static final long serialVersionUID = 1L;
}