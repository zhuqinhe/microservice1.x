package com.udcm.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Locale;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.udcm.constants.StatusCode;
import com.udcm.model.User;
import com.udcm.service.UserService;
import com.udcm.utils.SecurityUtils;
import com.udcm.utils.SessionManager;
import com.udcm.utils.UserSession;
import com.udcm.vo.LoginRequest;
import com.udcm.vo.LoginResponse;
import com.udcm.vo.Response;
import com.udcm.vo.VoResponse;


@RestController
@RequestMapping("/ui")
public class LoginController {
	
	static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
		
	
	
	@Resource
	private UserService userService;
    @Autowired
   	private Producer captchaProducer;


    
   	@RequestMapping(value = "/v1/generate/captcha")
   	public void getKaptchaImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
   		//用字节数组存储
   		byte[] captchaChallengeAsJpeg = null;
   		ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
   		ServletOutputStream responseOutputStream =
   				response.getOutputStream();
   		final HttpSession httpSession=request.getSession();
   		try {
   			//生产验证码字符串并保存到session中
   			String createText = captchaProducer.createText();
               //打印随机生成的字母和数字
   			logger.debug(createText);
   			httpSession.setAttribute(Constants.KAPTCHA_SESSION_KEY, createText);
   			//使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
   			BufferedImage challenge = captchaProducer.createImage(createText);
   			ImageIO.write(challenge, "jpg", jpegOutputStream);
   			captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
   			response.setHeader("Cache-Control", "no-store");
   			response.setHeader("Pragma", "no-cache");
   			response.setDateHeader("Expires", 0);
   			response.setContentType("image/jpeg");
               //定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
   			responseOutputStream.write(captchaChallengeAsJpeg);
               responseOutputStream.flush();
    		} catch (IllegalArgumentException e) {
   			response.sendError(HttpServletResponse.SC_NOT_FOUND);
   			return;
   		}finally {
   			responseOutputStream.close();
   		}
   		
   	}
  
    /**
     * 用户登录
     * @param login
     * @return
     */
    @RequestMapping(method=RequestMethod.POST,path="/v1/login", consumes={"application/json"},produces={"application/json"})
    public LoginResponse login(@RequestBody LoginRequest login,HttpServletRequest request){
    	LoginResponse resp = new LoginResponse();
    	logger.debug("captcha ---- > " + login.getCaptcha());
    	try{
    		String captchaId=(String)request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
    		String parameter = login.getCaptcha();
    		System.out.println("Session  vrifyCode "+captchaId+" form veritycode "+parameter);
    		if (!captchaId.equals(parameter)) {
    			resp.setResultCode(StatusCode.UI.UI_20003); //验证码不正确
    			return resp;
    		} 
    	}catch(Exception e){
    		logger.error(e.getMessage(),e);
    		resp.setResultCode(StatusCode.UI.UI_20003); //验证码不正确
    		return resp;
    	}
    	UserSession session = userService.authorize(login.getUserId(), login.getPassword());
    	if(null==session){
    		resp.setResultCode(StatusCode.UI.UI_20005); //认证不通过
    		return resp;
    	}else{
    		String token = SecurityUtils.createToken(login.getUserId(), login.getPassword(), session.getLastActiveTime().getTime());
    		SessionManager.addSessionUser(token, session);
    		User user = userService.getUserByUserId(session.getUserId());
    		//未启用用户
    		if(!user.getEnable()){
    			resp.setResultCode(StatusCode.UI.UI_20012); 
    			return resp;
    		}
    		//HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
    		String ip = request.getRemoteAddr();
    		logger.info("Login from ip address:{}",ip);
    		if(user.getEnableIPBinding()&&!isAllowVisit(user.getIpAddr(), ip)){
    			resp.setResultCode(StatusCode.UI.UI_20001); //IP限制
    		}else{
    			// 更新用户登录时间和次数
    			userService.updateUserLoginInfo(user);  
    			resp.setResultCode(StatusCode.UI.UI_0); 
    			resp.setToken(token);
    			resp.setPrivileges(session.getMenuPrivileges());
    			resp.setUserType(user.getUserType());
    		}    		
    	}    	

    	return resp;

    }
    
    /**
     * 用户Logout
     * @return
     */
    @RequestMapping(method=RequestMethod.DELETE,path="/v1/logout",produces={"application/json"})
    public Response logout(){    	
    	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
    	String token = request.getHeader("Token");    	
    	if(null!=token){    		
    		SessionManager.removeSessionUser(token);
    	}    	
    	Response resp = new Response();
    	resp.setResultCode(StatusCode.UI.UI_0);
    	return resp;
    }    

    
    /**
     * 校验登录IP是否在允许登录的白名单中
     * @param restrictIp
     * @param remoteIp
     * @return
     */
    private boolean isAllowVisit(String restrictIp,String remoteIp){
    	if(StringUtils.isEmpty(restrictIp)) {
            return true;
        }
    	if(StringUtils.isEmpty(remoteIp)) {
            return false;
        }
    	
    	String[] whiteList = restrictIp.split(",");
    	for(String item : whiteList){
    		if(remoteIp.trim().equals(item.trim())) {
                return true;
            }
    	}    	
    	return false;
    }
    
    
	
	@RequestMapping(method=RequestMethod.GET,path="/v1/security/check/default/password")
	public VoResponse<Boolean> checkDefaultPassword(HttpServletRequest request){
		VoResponse<Boolean> rsp = new VoResponse<Boolean>(StatusCode.UI.UI_0);
		String token = request.getHeader("Token");
		User user = userService.getCurrentUser(token);
		if("96e79218965eb72c92a549dd5a330112".equals(user.getPassword())){
			rsp.setVo(true);
		}else{
			rsp.setVo(false);
		}
/*		if(!sysConfigService.checkPassWordTimeOut(user.getPwdUpdateTime())){
			rsp.setResultCode(StatusCode.UI.UI_PWD_60001);//密码过期
		}*/
		return rsp;
	}
}
