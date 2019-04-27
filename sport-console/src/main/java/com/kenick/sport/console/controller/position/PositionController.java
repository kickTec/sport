package com.kenick.sport.console.controller.position;

import com.kenick.sport.console.controller.BaseController;
import com.kenick.sport.pojo.ad.Position;
import com.kenick.sport.service.product.AdService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/position")
public class PositionController extends BaseController {
    @Resource
    private AdService adService;

    @RequestMapping("/list.do")
    public String list(String root,Model model){
        Long parentId = null;
        if("source".equals(root)){
            parentId = 0L;
        }else{
            if(root != null){
                parentId = Long.parseLong(root);
            }
        }
        if(parentId != null){
            List<Position> positionList = adService.selectPositionListByParentId(parentId);
            model.addAttribute("positionList", positionList);
        }
        return "position/list";
    }

    @RequestMapping("/querySubNode.do")
    public @ResponseBody List<Position> querySubNode(String root){
        List<Position> positionList = new ArrayList<>();
        Long parentId = null;
        if("source".equals(root)){
            parentId = 0L;
        }else{
            if(root != null){
                parentId = Long.parseLong(root);
            }
        }
        if(parentId != null){
            List<Position> list = adService.selectPositionListByParentId(parentId);
            positionList.addAll(list);
        }
        return positionList;
    }
}
