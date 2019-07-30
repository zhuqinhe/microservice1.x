package com.udcm.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.udcm.constants.StatusCode;
import com.udcm.model.User;
import com.udcm.service.UserService;
import com.udcm.utils.PasswordEncode;
import com.udcm.utils.StringUtils;
import com.udcm.vo.ListResponse;
import com.udcm.vo.Response;
import com.udcm.vo.VoResponse;
/**
 * @Description 
 * @author hoob
 * @date 2019年5月29日下午5:03:15
 */
@RestController
@RequestMapping("/ui")
public class UserController {
	static Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
	
	@Resource
	private UserService userService;

	
	@RequestMapping(method=RequestMethod.POST,path="/v1/security/user/add", consumes={"application/json"},produces={"application/json"})
    public Response add(@RequestBody User uservo, HttpServletRequest request){
		log.debug("add user ");
		Response response = new Response();
		try {
			String token = request.getHeader("Token");
			if (token != null) {
				String[] parts = token.split(":");
            	String parentUser = parts[0];
				if(uservo.getUserId() == null) {
					response.setResultCode(-1);
					response.setDescription("name is null");
				} else {
					uservo.setParentUserId(parentUser);
					userService.addUser(uservo);
					response.setDescription("Succuss");
				}
			} else {
				uservo.setParentUserId("sysadmin");
				userService.addUser(uservo);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResultCode(-1);
			response.setDescription("Fail");
		}
    	return response;
    }
	
	@RequestMapping(method=RequestMethod.PUT,path="/v1/security/user/edit", consumes={"application/json"},produces={"application/json"})
    public Response update(@RequestBody User uservo){
		log.debug("update user ");
		Response response = new Response();
		try {
			if(uservo.getUserId() == null) {
				response.setResultCode(-1);
				response.setDescription("userId is null");
			} else {
				userService.editUser(uservo);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResultCode(-1);
			response.setDescription("Fail");
		}
    	return response;
    }
	
	@RequestMapping(method=RequestMethod.GET,path="/v1/security/user/detail")
    public VoResponse<User> get(@RequestParam("id") String id, HttpServletRequest request){
		log.debug("get user ");
		VoResponse<User> response = new VoResponse<User>(0);
		User uservo = null;
        try {
			 id = StringUtils.handleStrParam(id);
        	if(id != null) {
				uservo = userService.getUserByUserId(id);
        	}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResultCode(StatusCode.UI.UI_1);
		}
        response.setVo(uservo);
        return response;
    }
	
	@RequestMapping(method=RequestMethod.GET,path="/v1/security/user/list")
    public ListResponse<User> getList(
    		                   @RequestParam(value="userId",required=false) String userId,
    		                  @RequestParam(value="name",required=false) String name,
							  @RequestParam(value="enable",required=false) String enable,
							  @RequestParam(value = "first",required=false) String first,
							  @RequestParam(value = "max",required=false) String max,
    		HttpServletRequest request){
		log.debug("get user list ");
		ListResponse<User> response = new ListResponse<User>(0);
		name = StringUtils.handleStrParam(name);
		userId = StringUtils.handleStrParam(userId);
		enable = StringUtils.handleStrParam(enable);
		Integer firstInt = 0;
		Integer maxInt = -1;
		firstInt = StringUtils.handleIntParam(first)==null ? 0 : StringUtils.handleIntParam(first);
		maxInt = StringUtils.handleIntParam(max)== null ? -1 : StringUtils.handleIntParam(max);
		//long total = 0;
		try {
			String token = request.getHeader("Token");
			if (token != null) {
				User currentUser = userService.getCurrentUser(token);
				List<User> users = userService.getUser(userId,name, enable,currentUser.getUserId(),firstInt, maxInt);
				int total = userService.counts(userId, name, enable, currentUser.getUserId());
				response.setList(users);
				response.setTotal(total);
				
			}else{
				log.error("token is null ");
				response.setResultCode(StatusCode.UI.UI_1);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResultCode(StatusCode.UI.UI_1);
		}

		return response;
    }
	
	@RequestMapping(method=RequestMethod.POST,path="/v1/security/user/remove", consumes={"application/json"},produces={"application/json"})
	public Response delete(@RequestBody List<String> ids){
		log.debug("remove user ");
		Response response = new Response(0);
		try {

			if(ids==null||ids.isEmpty()){
				response.setResultCode(StatusCode.UI.UI_1);
				return response;
			}	
			userService.remove(ids);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResultCode(StatusCode.UI.UI_1);
		}
		return response;
	}
	
	@RequestMapping(method=RequestMethod.PUT,path="/v1/security/user/enable")
    public Response enable(@RequestParam("id") String id, HttpServletRequest request){
		log.debug("enable user ");
		Response response = new Response(0);
		//boolean enable = true;
		try {
			id=StringUtils.handleStrParam(id);
			if(id != null) {
				User user = userService.getUserByUserId(id);
				if(user != null) {
					userService.enableUser(user);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new Response(StatusCode.UI.UI_1);
		}
		//response.setVo(enable);
    	return response;
    }

	@RequestMapping(method=RequestMethod.PUT,path="/v1/security/user/password/reset")
	public VoResponse reset(@RequestParam("id") String id, HttpServletRequest request){
		log.debug("reset user password");
		VoResponse response = new VoResponse(StatusCode.UI.UI_0);
		try {
			id=StringUtils.handleStrParam(id);
			if(id != null) {
				User user = userService.getUserByUserId(id);
				if(user != null) {
					userService.resetPassword(user);
					System.out.println(user.getPassword());
					response.setVo(user.getPassword());
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new VoResponse(StatusCode.UI.UI_1);
		}
		return response;
	}

	@RequestMapping(method=RequestMethod.PUT,path="/v1/security/user/password/edit")
	public Response editPws(@RequestBody Map<String,String> body, HttpServletRequest request){
		log.debug("reset user password");
		Response response = new Response(0);
		try {
			String token = request.getHeader("Token");
			if (token != null) {
				//String currentUser = SessionManager.getCurrentUser(token).getUserId();
                //User user = userService.getUserByName(currentUser);
//				String[] parts = token.split(":");
//				String userName = parts[0];
				User currentUser = userService.getCurrentUser(token);
				if (currentUser != null) {
//					currentUser = userService.getUser(currentUser.getId());
					//UserVO userVO = userService.getUserVoByName(currentUser);
					String oldPw = body.get("oldPw");
					String newPw = body.get("newPw");
					oldPw=encodePw(oldPw);
					if (oldPw.equals(currentUser.getPassword())) {
						currentUser.setPassword(encodePw(newPw));
						currentUser.setPwdUpdateTime(new Date());//修改密码时更新密码修改时间
						userService.updatePassword(currentUser);
					} else {
						response.setResultCode(StatusCode.UI.UI_OLD_PASWORD_ERROR);
						response.setDescription("the oldPw is error! ");
					}
				}
			} else {
				response.setResultCode(-1);
				response.setDescription("the token is null! ");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new Response(StatusCode.UI.UI_0);
		}
		return response;
	}

	@RequestMapping(method=RequestMethod.GET,path="/v1/security/user/check/unique")
	public VoResponse<Boolean> judgeUnique(@RequestParam("name") String name, HttpServletRequest request){
		log.debug("judge Unique userName ");
		VoResponse<Boolean> response = new VoResponse<Boolean>(0);
		User uservo = null;
		boolean unique = false;
		try {
			name = StringUtils.handleStrParam(name);
			if(name != null) {
				uservo = userService.getUserByUserId(name);
				unique = uservo == null? true:false;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new VoResponse<Boolean>(-1);
		}

		response.setVo(unique);
		return response;
	}

	/**
	 * 密码加密
	 * @param oPassword
	 * @return
	 */
	public String encodePw(String oPassword) {
		return PasswordEncode.encodePassword(oPassword);
	}
}
