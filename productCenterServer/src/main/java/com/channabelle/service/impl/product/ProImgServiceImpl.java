package com.channabelle.service.impl.product;

import com.channabelle.common.exception.ServiceException;
import com.channabelle.common.utils.tencent.TencentCOS;
import com.channabelle.model.shop.ShopConfig;
import com.channabelle.service.impl.BaseServiceImpl;
import com.channabelle.service.impl.shop.ShopConfigServiceImpl;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProImgServiceImpl<T> extends BaseServiceImpl<T> {
    Logger log = Logger.getLogger(ProImgServiceImpl.class);

    @Autowired
    ShopConfigServiceImpl<ShopConfig> cService_UserConfig;

    public ProImgServiceImpl() {
        log.info("UserServiceImpl");
    }

    public List<JSONObject> getImageList(String uid) throws ServiceException {
        String SecretId = cService_UserConfig.getValueByUuidShopAndConfigName(uid, ShopConfig.CONFIG_TECENT_SecretId[0]);
        String SecretKey = cService_UserConfig.getValueByUuidShopAndConfigName(uid, ShopConfig.CONFIG_TECENT_SecretKey[0]);
        String region = cService_UserConfig.getValueByUuidShopAndConfigName(uid, ShopConfig.CONFIG_COS_REGION[0]);
        TencentCOS cos = new TencentCOS(SecretId, SecretKey, region);

        String bucketName = cService_UserConfig.getValueByUuidShopAndConfigName(uid, ShopConfig.CONFIG_COS_NAME[0]);
        String img_root = cService_UserConfig.getValueByUuidShopAndConfigName(uid, ShopConfig.CONFIG_PRO_IMG_ROOT[0]);
        List<JSONObject> list = cos.listAll(bucketName, img_root);
        cos.closeClient();

        return list;
    }
}