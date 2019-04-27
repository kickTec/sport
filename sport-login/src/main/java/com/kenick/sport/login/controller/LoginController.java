package com.kenick.sport.login.controller;

import com.kenick.sport.common.utils.LoginUtil;
import com.kenick.sport.common.utils.Md5Util;
import com.kenick.sport.pojo.user.Buyer;
import com.kenick.sport.service.buyer.BuyerService;
import com.kenick.sport.service.login.SessionProviderService;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

@Controller
public class LoginController {
    @Resource
    private BuyerService buyerService;

    @Resource
    private SessionProviderService sessionProviderService;

    @RequestMapping(value = "/login.aspx",method = {RequestMethod.GET})
    public String login(String returnUrl,String error,Model model){
        model.addAttribute("returnUrl",returnUrl);
        if(error !=null && !"".equals(error)){
            error = new String(error.getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);
        }
        model.addAttribute("error",error);
        return "login";
    }

    @RequestMapping(value = "/login.aspx",method = {RequestMethod.POST})
    public String login(String returnUrl, String username, String password,
                        HttpServletRequest request, HttpServletResponse response, Model model){
        if(username != null && !"".equals(username)){
            Buyer buyer = buyerService.selectBuyerByUsername(username);
            if(buyer != null){
                if(password != null && !"".equals(password)){
                    String passwordEncode = Md5Util.md5Encode(password);
                    if(buyer.getPassword().equals(passwordEncode)){
                        String csessionid = LoginUtil.getCSESSIONID(request, response);
                        sessionProviderService.setAttributeForUsername(csessionid,username);
                        return "redirect:"+returnUrl;
                    }else{
                        model.addAttribute("error","密码不正确");
                    }
                }else{
                    model.addAttribute("error","密码不能为空");
                }
            }else{
                model.addAttribute("error","用户名不正确");
            }
        }else{
            model.addAttribute("error","用户名不能为空");
        }
        model.addAttribute("returnUrl",returnUrl);
        return "redirect:/login.aspx";
    }

    @RequestMapping("/isLogin.aspx")
    public @ResponseBody MappingJacksonValue isLogin(String callback,HttpServletRequest request, HttpServletResponse response){
        String username = sessionProviderService.getAttributeForUsername(LoginUtil.getCSESSIONID(request, response));
        String flag = "0";
        if(username !=null && !"".equals(username)){
            flag = "1";
        }
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(flag);
        mappingJacksonValue.setJsonpFunction(callback);
        return mappingJacksonValue;
    }
}
