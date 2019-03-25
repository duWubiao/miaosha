package com.miaoshaproject.service;

import com.miaoshaproject.service.model.PromoModel;

public interface PromoService {

    //根据itemId 获取即将进行的或正在进行的秒杀活动
    PromoModel getPromoModelByItemId(Integer itemId);
}
