package com.miaoshaproject.controller;

import com.miaoshaproject.controller.viewobject.ItemVo;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.service.ItemService;
import com.miaoshaproject.service.model.ItemModel;
import com.miaoshaproject.utils.CommonReturnType;
import com.sun.org.apache.regexp.internal.RE;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/item")
public class ItemController extends BaseController{

    private Logger logger = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private ItemService itemService;

    //创建商品
    @PostMapping("/create")
    public CommonReturnType createItem(@RequestParam(name = "title") String title,
                                       @RequestParam(name = "description") String description,
                                       @RequestParam(name = "price") BigDecimal price,
                                       @RequestParam(name = "stock") Integer stock,
                                       @RequestParam(name = "imgUrl") String imgUrl) throws BusinessException {

        //封装service请求用来创建商品
        ItemModel itemModel = new ItemModel();
        itemModel.setTitle(title);
        itemModel.setDescription(description);
        itemModel.setPrice(price);
        itemModel.setStock(stock);
        itemModel.setImgUrl(imgUrl);

        ItemModel item = itemService.createItem(itemModel);

        ItemVo itemVo = convertVoFromModel(item);
        return CommonReturnType.createCommonReturnType(itemVo);
    }

    //商品详情浏览
    @GetMapping("/get")
    public CommonReturnType getItem(@RequestParam(name = "id") Integer id) {
        ItemModel itemModel = itemService.getItemById(id);
        ItemVo itemVo = convertVoFromModel(itemModel);
        return CommonReturnType.createCommonReturnType(itemVo);
    }

    //商品列表页面浏览
    @GetMapping("list")
    public CommonReturnType listItem() {

        List<ItemModel> itemModels = itemService.listItem();
        //使用stream api 将list内的itemModel转化itemVo
        List<ItemVo> itemVoList = itemModels.stream().map(itemModel -> {
            ItemVo itemVo = convertVoFromModel(itemModel);
            return itemVo;
        }).collect(Collectors.toList());
        return CommonReturnType.createCommonReturnType(itemVoList);
    }

    public ItemVo convertVoFromModel(ItemModel itemModel) {
        if(itemModel == null) {
            return null;
        }
        ItemVo itemVo = new ItemVo();
        BeanUtils.copyProperties(itemModel,itemVo);
        if(itemModel.getPromoModel() != null) {
            //有正在进行或即将进行的秒杀活动
            itemVo.setPromoStatus(itemModel.getPromoModel().getStatus());
            itemVo.setStartDate(itemModel.getPromoModel().getStartDate().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
            itemVo.setPromoPrice(itemModel.getPromoModel().getPromoItemPrice());
            itemVo.setPromoId(itemModel.getPromoModel().getId());
        }else {
            itemVo.setPromoStatus(0);
        }
        return itemVo;
    }

}
