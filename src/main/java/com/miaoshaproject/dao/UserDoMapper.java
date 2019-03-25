package com.miaoshaproject.dao;

import com.miaoshaproject.dataObject.UserDo;
import org.springframework.transaction.annotation.Transactional;

public interface UserDoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbggenerated Wed Feb 27 20:57:08 CST 2019
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbggenerated Wed Feb 27 20:57:08 CST 2019
     */
    int insert(UserDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbggenerated Wed Feb 27 20:57:08 CST 2019
     */
    int insertSelective(UserDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbggenerated Wed Feb 27 20:57:08 CST 2019
     */
    UserDo selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbggenerated Wed Feb 27 20:57:08 CST 2019
     */
    int updateByPrimaryKeySelective(UserDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbggenerated Wed Feb 27 20:57:08 CST 2019
     */
    int updateByPrimaryKey(UserDo record);


    UserDo selectByTelphone(String telphone);
}