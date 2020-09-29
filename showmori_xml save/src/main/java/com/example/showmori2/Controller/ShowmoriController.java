package com.example.showmori2.Controller;

import com.example.showmori2.Dto.LoginDTO;
import com.example.showmori2.Dto.LoginResponseDTO;
import com.example.showmori2.Dto.RegisterResponseDTO;
import com.example.showmori2.Service.RegisterService;
import com.example.showmori2.domain.User_info;
import com.example.showmori2.domain.User_info_repository;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
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
    private RegisterService registerService;
    private HttpSession session, userInfoSession;

    @CrossOrigin(origins="*")
    @RequestMapping(value = "/api/users/login", method = {RequestMethod.OPTIONS, RequestMethod.POST, RequestMethod.GET})
    public LoginResponseDTO handleOptionsRequest(@RequestHeader(value="Access-Control-Request-Method", required=false) String requestMethods,
                                     @RequestHeader(value="Access-Control-Request-Headers", required=false) String requestHeaders,
                                     HttpServletRequest request,
                                     HttpServletResponse response) {
        //        response.setHeader("Access-Control-Allow-Headers", "*");

//        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
//        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
//        response.setHeader("Access-Control-Allow-Origin", "*");

        try {
            if (StringUtils.hasLength(requestMethods)) {
                response.setHeader(ACCESS_CONTROL_ALLOW_METHODS, requestMethods);
            }
            if (StringUtils.hasLength(requestHeaders)) {
                response.setHeader(ACCESS_CONTROL_ALLOW_HEADERS, requestHeaders);
            }
            response.setHeader("Access-Control-Allow-Origin", "*");

            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
        } catch (Exception e) {
            System.out.println("cors?");
            e.printStackTrace();
        }

        JSONObject jsonObject = null;

        LoginDTO client_request = null;
        LoginResponseDTO server_response = null;
//            Gson gson = new Gson();
        String client_json = null;

        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        String body = null;
        // String command = null;
        JSONParser parser = new JSONParser();
        PrintWriter out = null;

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
//                    "\"id\" : \"id\"," +
//                    "\"password\" : \"password\"" +
//                    "}";
//            client_json = ((JSONObject) parser.parse(body)).toString();
//            client_request = gson.fromJson(client_json, LoginDTO.class);

            jsonObject = (JSONObject) parser.parse(body);
            System.out.println("id " + jsonObject.get("id").toString());
//            System.out.println(client_request.getPassword());


        } catch (Exception e) {
            e.printStackTrace();
        }

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setUser_id(jsonObject.get("id").toString());
        loginResponseDTO.setCheck("success");

        System.out.println(request.toString());
        return loginResponseDTO;
    }

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

//    @GetMapping("/api/users/login")
//    public String login() {//@RequestBody LoginDTO loginDTO){
////        return loginDTO.getUser_id();
//    	return "login..";
//    }

    @GetMapping("/users/login")
    public LoginResponseDTO doLogin(){//@RequestHeader(value="Access-Control-Request-Method", required=false) String requestMethods,
//                                    @RequestHeader(value="Access-Control-Request-Headers", required=false) String requestHeaders,
//                                    HttpServletResponse response){
        //@RequestBody LoginDTO loginDTO){
        //String user_id, String password, HttpServletRequest request){

//        response.setHeader("Access-Control-Allow-Headers", "*");

//        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
//        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
//
//        response.setHeader("Access-Control-Allow-Origin", "*");
//
//        if(StringUtils.hasLength(requestMethods)){
//            response.setHeader(ACCESS_CONTROL_ALLOW_METHODS, requestMethods);
//        }
//        if(StringUtils.hasLength(requestHeaders)){
//            response.setHeader(ACCESS_CONTROL_ALLOW_HEADERS, requestHeaders);
//        }
//
//        response.setHeader(ACCESS_CONTROL_MAX_AGE, DAY_IN_SECONDS.toString());

        String user_id = "apple";
        String password = "apple";

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();

        if(user_id.equals("") || password.equals("")){
            loginResponseDTO.setCheck("login_fail");
        }

        try {
            String password_DB = user_info_repository.findPassword(user_id);

//            Optional<User_info> user_info = user_info_repository.findById(user_id);
            System.out.println("1");
//            System.out.println(user_info.get().getPassword());
            System.out.println("2");

            List<User_info> list = user_info_repository.getList();
//            System.out.println(list.get(0).getUser_id());

            if (password_DB.equals(password)) {
//                userInfoSession = request.getSession(true);
//                userInfoSession.setAttribute("loginUserInfo", user_info);

//         ¡       session = request.getSession(true);
                session.setAttribute("loginUserId", user_id);

                loginResponseDTO.setUser_id(user_id);
                loginResponseDTO.setCheck("login_success");

            }
        }catch(Exception e){
            e.printStackTrace();
        }

        loginResponseDTO.setUser_id(user_id);
        loginResponseDTO.setCheck("login_check");
        return loginResponseDTO;
//        return "string";
    }

    @GetMapping("/users/register")
    public RegisterResponseDTO doRegister(){
        //@RequestBody RegisterDTO registerDTO){
        // String user_id, String password, String user_name, String user_phone){

        String user_id = "last123";
        String password = "kim00";
        String user_name = "kim";
        String user_phone = "0000";

//        Object UserInfo = userInfoSession.getAttribute("loginUserInfo");
//        User_info curUserInfo = (User_info) UserInfo;
//        List<User_info> userInfo = user_info_repository.getList();
        RegisterResponseDTO registerResponseDTO = new RegisterResponseDTO();

//        Long count = user_info_repository.countRegisterID(user_id);
//        Optional<User_info> user_info = user_info_repository.findById(user_id);
//
//        if(count == 0) {
            registerService.saveUserinfo(user_id, password, user_name, user_phone); // db 저장
            registerResponseDTO.setId(user_id);
            registerResponseDTO.setCheck("register_success");
//        }
//        else registerResponseDTO.setCheck("register_fail");

//        registerResponseDTO.setCheck("check");
        System.out.println(registerResponseDTO.getCheck());
        return registerResponseDTO;
    }

    @DeleteMapping("/users/logout")
    public void logOut(HttpSession session){
        session.removeAttribute("loginUserid");
    }

//    @GetMapping("/{id}")
//    public Account retrieve(@PathVariable Long id) {
//        // ...
//    }

}
