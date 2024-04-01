package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

@Mapper
public interface UserMapper {

    /**
     * 根据openid查询用户
     *
     * @param openid
     * @return
     */
    @Select("SELECT * FROM user WHERE openid = #{openid}")
    User getByOpenid(String openid);

    /**
     * 插入数据
     *
     * @param user
     * @return
     */
    void insert(User user);

    /**
     * 根据id查询用户
     *
     * @param userId
     * @return
     */
    @Select("SELECT * FROM user WHERE id = #{userId}")
    User getById(Long userId);

    /**
     * 获取某区间的用户数
     *
     * @param dateStart
     * @param dateEnd
     * @return
     */
    Integer getUserByDate(LocalDateTime dateStart, LocalDateTime dateEnd);

}
