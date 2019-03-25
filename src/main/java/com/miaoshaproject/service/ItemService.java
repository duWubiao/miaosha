package com.miaoshaproject.service;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.service.model.ItemModel;

import java.util.List;

public interface ItemService {

    //创建商品
    ItemModel createItem(ItemModel item) throws BusinessException;

    //查看商品列表
    List<ItemModel> listItem();

    //商品详情浏览
    ItemModel getItemById(Integer id);

    boolean decressStock(Integer itemId,Integer amount);

    void increaseSales(Integer itemId, Integer amount);
}
