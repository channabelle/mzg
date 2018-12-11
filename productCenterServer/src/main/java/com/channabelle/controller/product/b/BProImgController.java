package com.channabelle.controller.product.b;

import com.channabelle.common.ControllerResult;
import com.channabelle.controller.BaseController;
import com.channabelle.model.shop.ShopManager;
import com.channabelle.service.impl.product.ProImgServiceImpl;
import com.channabelle.service.impl.shop.ShopManagerServiceImpl;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = {"/b/product/proImg"})
public class BProImgController extends BaseController {
    @Autowired
    ProImgServiceImpl<JSONObject> ProImgServiceImpl;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public @ResponseBody
    Object list() throws Exception {
        ShopManager sm = ShopManagerServiceImpl.getSession_ShopManager(this.request);

        List<JSONObject> list = ProImgServiceImpl.getImageList(sm.getUuid_shop());

        return ControllerResult.success(list);
    }
}
