package com.miaoshaproject.service.impl;

import com.miaoshaproject.controller.viewobject.ItemVo;
import com.miaoshaproject.dao.ItemDoMapper;
import com.miaoshaproject.dao.ItemStockDoMapper;
import com.miaoshaproject.dataObject.ItemDo;
import com.miaoshaproject.dataObject.ItemStockDo;
import com.miaoshaproject.dataObject.UserDo;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.service.ItemService;
import com.miaoshaproject.service.PromoService;
import com.miaoshaproject.service.model.ItemModel;
import com.miaoshaproject.service.model.PromoModel;
import com.miaoshaproject.validator.ValidationResult;
import com.miaoshaproject.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private ItemDoMapper itemDoMapper;

    @Autowired
    private ItemStockDoMapper itemStockDoMapper;

    @Autowired
    private PromoService promoService;

    @Override
    public ItemModel createItem(ItemModel item) throws BusinessException {
        //校验入参
        ValidationResult result = validator.validate(item);
        if(result.isHasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrorMsg());
        }
        //转化itemModel -> dataobject
        ItemDo itemDo = convertItemDoFromItemModel(item);
        itemDoMapper.insertSelective(itemDo);
        item.setId(itemDo.getId());

        ItemStockDo itemStockDo = convertItemStockDoFromItemModel(item);
        itemStockDoMapper.insertSelective(itemStockDo);
        return getItemById(item.getId());
    }

    public ItemDo convertItemDoFromItemModel(ItemModel itemModel) {
        if(itemModel == null) {
            return null;
        }
        ItemDo itemDo = new ItemDo();
        BeanUtils.copyProperties(itemModel,itemDo);
        return itemDo;
    }

    public ItemStockDo convertItemStockDoFromItemModel(ItemModel itemModel) {
        if(itemModel == null) {
            return null;
        }
        ItemStockDo itemStockDo = new ItemStockDo();
        itemStockDo.setItemId(itemModel.getId());
        itemStockDo.setStock(itemModel.getStock());
        return itemStockDo;
    }

    @Override
    public List<ItemModel> listItem() {
        List<ItemDo> itemDoList = itemDoMapper.listItem();
        List<ItemModel> itemModelList = itemDoList.stream().map(itemDo -> {
            ItemStockDo itemStockDo = itemStockDoMapper.selectByItemId(itemDo.getId());
            ItemModel itemModel = convertItemModelFromDataObject(itemDo, itemStockDo);
            return itemModel;
        }).collect(Collectors.toList());
        return itemModelList;
    }

    @Override
    public ItemModel getItemById(Integer id) {
        ItemDo itemDo = itemDoMapper.selectByPrimaryKey(id);
        if(itemDo == null) {
            return null;
        }
        ItemStockDo itemStockDo = itemStockDoMapper.selectByItemId(itemDo.getId());

        //将dataobject -> itemModel
        ItemModel itemModel = convertItemModelFromDataObject(itemDo,itemStockDo);

        PromoModel promoModel = promoService.getPromoModelByItemId(itemModel.getId());
        if(promoModel != null && promoModel.getStatus() != 3) {
            itemModel.setPromoModel(promoModel);
        }
        return itemModel;
    }

    @Override
    @Transactional //事务一致性
    public boolean decressStock(Integer itemId, Integer amount) {
        int affectedRow = itemStockDoMapper.decressStock(itemId,amount);
        if(affectedRow > 0) {
            //更新库存成功
            return true;
        }else {
            //更新库存失败
            return false;
        }
    }

    @Override
    @Transactional
    public void increaseSales(Integer itemId, Integer amount) {
        itemDoMapper.increaseSales(itemId,amount);
    }

    private ItemModel convertItemModelFromDataObject(ItemDo itemDo, ItemStockDo itemStockDo) {
        if(itemDo == null || itemStockDo == null) {
            return null;
        }
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDo,itemModel);
        itemModel.setStock(itemStockDo.getStock());
        return itemModel;
    }
}
