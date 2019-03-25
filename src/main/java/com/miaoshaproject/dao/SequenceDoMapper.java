package com.miaoshaproject.dao;

import com.miaoshaproject.dataObject.SequenceDo;

public interface SequenceDoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sequence_info
     *
     * @mbggenerated Tue Mar 05 17:22:21 CST 2019
     */
    int deleteByPrimaryKey(String name);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sequence_info
     *
     * @mbggenerated Tue Mar 05 17:22:21 CST 2019
     */
    int insert(SequenceDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sequence_info
     *
     * @mbggenerated Tue Mar 05 17:22:21 CST 2019
     */
    int insertSelective(SequenceDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sequence_info
     *
     * @mbggenerated Tue Mar 05 17:22:21 CST 2019
     */
    SequenceDo selectByPrimaryKey(String name);

    SequenceDo getSequenceByName(String name);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sequence_info
     *
     * @mbggenerated Tue Mar 05 17:22:21 CST 2019
     */
    int updateByPrimaryKeySelective(SequenceDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sequence_info
     *
     * @mbggenerated Tue Mar 05 17:22:21 CST 2019
     */
    int updateByPrimaryKey(SequenceDo record);
}