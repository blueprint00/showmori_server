package com.example.showmori2.Controller;

import com.example.showmori2.Dto.LoginDTO;
import com.example.showmori2.Dto.LoginResponseDTO;
import com.example.showmori2.Dto.RegisterDTO;
import com.example.showmori2.Dto.RegisterResponseDTO;
import com.example.showmori2.Service.LoginService;
import com.example.showmori2.Service.RegisterService;
import com.example.showmori2.domain.User_info;
import com.example.showmori2.domain.User_info_repository;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

//@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
@RestController //@ResponseBody 포함
//@javax.servlet.annotation.WebServlet("/")
public class ShowmoriController {

    private static final String ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
    private static final String ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";
    private static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
    private static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    private static final String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";
    private static final Integer DAY_IN_SECONDS = 24 * 60 * 60;

    private User_info_repository user_info_repository;

    @Autowired
    private RegisterService registerService;
    @Autowired
    private LoginService loginService;

    private HttpSession session, userInfoSession;

    @GetMapping("/")
    public String basic() {
    	return "plz";
    }
    @GetMapping("/hello")
    public String hello(){
        return "HEllo";
    }

    @RequestMapping("/test1")
    public LoginDTO test(){
        LoginDTO loginDTO = new LoginDTO();

        loginDTO.setUser_id("?");
        loginDTO.setPassword("??");

        return loginDTO;
    }


    @CrossOrigin(origins="*")
    @GetMapping(value = "/api/users/login")//, method = {RequestMethod.OPTIONS, RequestMethod.POST, RequestMethod.GET})
    public LoginResponseDTO doLogin(//@RequestHeader(value="Access-Control-Request-Method", required=false) String requestMethods,
                                    //@RequestHeader(value="Access-Control-Request-Headers", required=false) String requestHeaders,
                                    HttpServletRequest request,
                                    HttpServletResponse response){

//       try {
//            if (StringUtils.hasLength(requestMethods)) {
//                response.setHeader(ACCESS_CONTROL_ALLOW_METHODS, requestMethods);
//            }
//            if (StringUtils.hasLength(requestHeaders)) {
//                response.setHeader(ACCESS_CONTROL_ALLOW_HEADERS, requestHeaders);
//            }
//            response.setHeader("Access-Control-Allow-Origin", "*");
//
//            request.setCharacterEncoding("UTF-8");
//            response.setCharacterEncoding("UTF-8");
//        } catch (Exception e) {
//            System.out.println("cors?");
//            e.printStackTrace();
//        }
//        response.setHeader(ACCESS_CONTROL_MAX_AGE, DAY_IN_SECONDS.toString());

        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        String body = null;

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        Gson gson = new Gson();

        LoginDTO client_request = null;
        LoginResponseDTO server_response = new LoginResponseDTO();

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
            body = stringBuilder.toString();

//            body = "{" +
//                    "\"user_id\" : \"user_id1\"" +
//                    "\"password\" : \"password\"" +
//                    "\"user_name\" : \"user_name\"" +
//                    "\"user_phone\" : \"user_phone\"" +
//                    "}";

            jsonObject = (JSONObject) parser.parse(body);
            client_request = gson.fromJson(jsonObject.toString(), LoginDTO.class);

            String user_id = client_request.getUser_id();
            String password = client_request.getPassword();

            if(user_id.equals("") || password.equals("")){
                server_response.setSuccess(false);
            }

            String password_DB = loginService.findPasswordById(user_id);

//            List<User_info> list = user_info_repository.getList();
//            System.out.println(list.get(0).getUser_id());
//            System.out.println("1");
//            System.out.println("2");
//            System.out.println(list.size() + " "
//                    + password_DB + " "
//                    + count);

            if (password.equals(password_DB)) {
//                userInfoSession = request.getSession(true);
//                userInfoSession.setAttribute("loginUserInfo", user_info);

                session = request.getSession(true); // 생성된 세션 없을 시 새로 생성해서 반환
                session.setAttribute("user_id", user_id);

                server_response.setUser_id(user_id);
                server_response.setSuccess(true);
            } else {
                server_response.setSuccess(false);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return server_response;
    }

    @CrossOrigin(origins="*")
    @GetMapping(value = "/api/users/register")
    public RegisterResponseDTO doRegister(HttpServletRequest request){//, HttpServletResponse response){

        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        String body = null;

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        Gson gson = new Gson();

        RegisterDTO client_request = null;
        RegisterResponseDTO server_response = new RegisterResponseDTO();

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
            body = stringBuilder.toString();
//            body = "{" +
//                    "\"user_id\" : \"user_id1\"" +
//                    "\"password\" : \"password\"" +
//                    "\"user_name\" : \"user_name\"" +
//                    "\"user_phone\" : \"user_phone\"" +
//                    "}";

            jsonObject = (JSONObject) parser.parse(body);
            client_request = gson.fromJson(jsonObject.toString(), RegisterDTO.class);

            String user_id = client_request.getUser_id();
            String password = client_request.getPassword();
            String user_name = client_request.getUser_name();
            String user_phone = client_request.getUser_phone();

            server_response.setUser_id(user_id);
            User_info user_info = user_info_repository.getOne(user_id);

            System.out.println("register user_info : " + user_info.getUser_id() + " " + user_info.getPassword());
            if (registerService.checkRegisterId(user_id) == 0) { // id 중복 확인
               session = request.getSession(true);
                session.setAttribute("user_info", user_info);

                registerService.saveUserinfo(user_id, password, user_name, user_phone); // db 저장

                server_response.setValidate(true);
                server_response.setSuccess(true);
            } else {
                server_response.setValidate(false);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        System.out.println(server_response.getSuccess());
        return server_response;
    }

    @DeleteMapping("/users/logout")
    public void logOut(){
        session.removeAttribute("user_id");
    }

//    @GetMapping("/{id}")
//    public Account retrieve(@PathVariable Long id) {
//        // ...
//    }

}
