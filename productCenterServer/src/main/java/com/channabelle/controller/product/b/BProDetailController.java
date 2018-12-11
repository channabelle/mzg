package com.channabelle.controller.product.b;

import com.channabelle.common.ControllerResult;
import com.channabelle.common.ServiceResult;
import com.channabelle.common.ServiceResult.Status;
import com.channabelle.controller.BaseController;
import com.channabelle.model.product.ProDetail;
import com.channabelle.service.impl.product.ProDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = {"/b/product/proDetail"})
public class BProDetailController extends BaseController {
    @Autowired
    ProDetailServiceImpl<ProDetail> proDetailService;

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public @ResponseBody
    Object query(@RequestParam(value = "proInfoId", required = true) String proInfoId) throws Exception {

        ProDetail pDetail = proDetailService.getProDetailByProInfoId(proInfoId);

        return ControllerResult.success(pDetail);
    }

    @RequestMapping(value = "/createOrUpdate", method = RequestMethod.POST)
    public @ResponseBody
    Object createOrUpdate(@RequestBody ProDetail proDetail) throws Exception {
        ServiceResult sRes = null;

        if(null != proDetail.getP_uuid_pro_detail()) {
            ProDetail old = proDetailService.hfindOne(ProDetail.class, proDetail.getP_uuid_pro_detail());
            if(null == old) {
                return ControllerResult.setServiceResult(new ServiceResult(Status.Error, "产品详情编号", "不存在"), null);
            } else if(false == old.getUuid_pro_info().equals(proDetail.getUuid_pro_info())) {
                return ControllerResult.setServiceResult(new ServiceResult(Status.Error, "产品详情所属产品编号", "不能变更"), null);
            }

            sRes = proDetailService.hPatchUpdateOne(proDetail, ProDetail.class, proDetail.getP_uuid_pro_detail());
        } else {
            sRes = proDetailService.hAddOne(proDetail);
        }

        return ControllerResult.setServiceResult(sRes, proDetail);
    }
}
