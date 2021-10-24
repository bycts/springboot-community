package com.byc.community.community.controller;

import com.byc.community.community.dto.AccessTokenDTO;
import com.byc.community.community.dto.GithubUser;
import com.byc.community.community.mapper.UserMapper;
import com.byc.community.community.model.User;
import com.byc.community.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

//使用github的授权登录
@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @Value("${github.client.id}")
    private  String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String  redirectUri;

    @Autowired
    private UserMapper userMapper;


    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response){

        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if(githubUser!=null&&githubUser.getId()!=null){
            User user = new User();
            //存入数据库
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setAvatarUrl(githubUser.getAvatarUrl());
            userMapper.insert(user);
             /*response写入cookie*/
            response.addCookie(new Cookie("token",token));
            //token信息已写入，取出来与数据库进行对比 验证登录是否成功

            //request.getSession().setAttribute("githubUser",githubUser);
            //登录成功，返回cookie session
            return  "redirect:/";

        }else{
            //登录失败 重新登陆
            return  "redirect:/";

        }
       // System.out.println(githubUser.getName());
       // return "index";
    }

}
