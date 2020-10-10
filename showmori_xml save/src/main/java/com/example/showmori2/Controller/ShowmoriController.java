package com.example.showmori2.Controller;

import com.example.showmori2.Dto.*;
import com.example.showmori2.Service.PostService;
import com.example.showmori2.Service.UserService;
import com.example.showmori2.domain.*;

import com.example.showmori2.tmp.LoginDTO;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

//@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
@RestController //@ResponseBody 포함
public class ShowmoriController {

    private static final String ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
    private static final String ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";
    private static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
    private static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    private static final String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";
    private static final Integer DAY_IN_SECONDS = 24 * 60 * 60;

    private User_info_repository user_info_repository;
    private Post_info_repository post_info_repository;

//    private RegisterService registerService;
//    private LoginService loginService;

    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;

//    private HttpSession session, userInfoSession;

    public void confirmCORS(@RequestHeader(value = "Access-Control-Request-Method", required = false) String requestMethods,
                            @RequestHeader(value = "Access-Control-Request-Headers", required = false) String requestHeaders,
                            HttpServletRequest request,
                            HttpServletResponse response) {

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
//        response.setHeader(ACCESS_CONTROL_MAX_AGE, DAY_IN_SECONDS.toString());
    }

    public String inputFromJson(HttpServletRequest request) {

        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        String body = null;

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        String jsonString = null;
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
//                    "\"user_id\" : \"apple\"" +
//                    "\"password\" : \"apple\"" +
//                    "\"user_name\" : \"user_name\"" +
//                    "\"user_phone\" : \"user_phone\"" +
//                    "}";

            jsonObject = (JSONObject) parser.parse(body);
            jsonString = jsonObject.toString();
            System.out.println(jsonString);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    @GetMapping("/")
    public String basic() {
        return "plz";
    }

    @RequestMapping("/test1")
    public LoginDTO test() {
        LoginDTO loginDTO = new LoginDTO();

        loginDTO.setUser_id("?");
        loginDTO.setPassword("??");

        return loginDTO;
    }


    @CrossOrigin(origins = "*") // 로그인
    @RequestMapping(value = "/api/users/login", method = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST})
    public UserResponseDTO doLogin(@RequestHeader(value = "Access-Control-Request-Method", required = false) String requestMethods,
                                   @RequestHeader(value = "Access-Control-Request-Headers", required = false) String requestHeaders,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {

        confirmCORS(requestMethods, requestHeaders, request, response);

//        Gson gson = new Gson();
        UserDTO client_request = null;
        UserResponseDTO server_response = new UserResponseDTO();

        String jsonString = inputFromJson(request);

//        client_request = gson.fromJson(jsonObject.toString(), LoginDTO.class);
        client_request = new Gson().fromJson(jsonString, UserDTO.class);

        String user_id = client_request.getUser_id();
        System.out.println(user_id);
        String password = client_request.getPassword();

        if (user_id.equals("") || password.equals("")) {
            server_response.setSuccess(false);
        }

        String password_DB = userService.findPasswordById(user_id);

        if (password.equals(password_DB)) {
//            session = request.getSession(true); // 생성된 세션 없을 시 새로 생성해서 반환
//            session.setAttribute("user_id", user_id);

            server_response.setUser_id(user_id);
            server_response.setSuccess(true);
        } else {
            server_response.setSuccess(false);
        }

        System.out.println();
        return server_response;
    }

    @CrossOrigin(origins = "*") // 회원가입
    @RequestMapping(value = "/api/users/register", method = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST})
    public UserResponseDTO doRegister(@RequestHeader(value = "Access-Control-Request-Method", required = false) String requestMethods,
                                      @RequestHeader(value = "Access-Control-Request-Headers", required = false) String requestHeaders,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {

        confirmCORS(requestMethods, requestHeaders, request, response);

        UserDTO client_request = null;
        UserResponseDTO server_response = new UserResponseDTO();

        String jsonString = inputFromJson(request);
        client_request = new Gson().fromJson(jsonString, UserDTO.class);

        String user_id = client_request.getUser_id();
        String password = client_request.getPassword();
        String user_name = client_request.getUser_name();
        String user_phone = client_request.getUser_phone();

        server_response.setUser_id(user_id);
        //????????????????????????????????????????????
        User_info user_info = user_info_repository.getOne(user_id);
        //????????????????????????????????????????????

//        System.out.println("register user_info : " + user_info.getUser_id() + " " + user_info.getPassword());
        if (userService.checkRegisterId(user_id) == 0) { // id 중복 확인
//                session = request.getSession(true);
//                session.setAttribute("user_info", user_info);

            userService.saveUserinfo(user_id, password, user_name, user_phone); // db 저장

            server_response.setValidate(true);
            server_response.setSuccess(true);
        } else {
            server_response.setValidate(false);
            server_response.setSuccess(false);
        }

        System.out.println(server_response.getSuccess());
        return server_response;
    }

    @CrossOrigin(origins = "*") // 유저 정보 수정 전
    @RequestMapping(value = "/api/users/{user_id}/beforeModification", method = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST})
    public UserResponseDTO getUserInfo(@PathVariable("user_id") String user_id,
                                       @RequestHeader(value = "Access-Control-Request-Method", required = false) String requestMethods,
                                       @RequestHeader(value = "Access-Control-Request-Headers", required = false) String requestHeaders,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {

        confirmCORS(requestMethods, requestHeaders, request, response);

        UserResponseDTO server_response = new UserResponseDTO();
        User_info user_info = userService.findUserInfoById(user_id);

        server_response.setUser_id(user_id);
        server_response.setUser_name(user_info.getUser_name());
        server_response.setUser_phone(user_info.getUser_phone());
        server_response.setSuccess(true);

        return server_response;
    }

    @CrossOrigin(origins = "*") // 유저 정보 수정 후
    @RequestMapping(value = "/api/users/{user_id}/afterModification", method = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
    public UserResponseDTO updateUserInfo(@PathVariable("user_id") String user_id,
                                          @RequestHeader(value = "Access-Control-Request-Method", required = false) String requestMethods,
                                          @RequestHeader(value = "Access-Control-Request-Headers", required = false) String requestHeaders,
                                          HttpServletRequest request,
                                          HttpServletResponse response) {

        confirmCORS(requestMethods, requestHeaders, request, response);

        UserDTO client_request = new UserDTO();
        UserResponseDTO server_response = new UserResponseDTO();

        String jsonString = inputFromJson(request);
        client_request = new Gson().fromJson(jsonString, UserDTO.class);

        userService.updateUserInfo(user_id, //client_request.getPassword(),
                client_request.getUser_name(), client_request.getUser_phone());

        server_response.setUser_id(user_id);
        server_response.setSuccess(true);

        return server_response;
    }

    @CrossOrigin(origins = "*") // 회원 탈퇴
    @RequestMapping(value = "/api/users/{user_id}/unregister", method = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE})
    public UserResponseDTO deleteUserInfo(@PathVariable("user_id") String user_id,
                                          @RequestHeader(value = "Access-Control-Request-Method", required = false) String requestMethods,
                                          @RequestHeader(value = "Access-Control-Request-Headers", required = false) String requestHeaders,
                                          HttpServletRequest request,
                                          HttpServletResponse response) {

        confirmCORS(requestMethods, requestHeaders, request, response);

        userService.deleteUserInfo(user_id);

        UserResponseDTO server_response = new UserResponseDTO(user_id, null, true);

        return server_response;
    }

    @CrossOrigin(origins = "*") // 전체 게시글
    @RequestMapping(value = "/api/fundings", method = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST})
    public ResponseDTO getPostInfoList(@RequestHeader(value = "Access-Control-Request-Method", required = false) String requestMethods,
                                       @RequestHeader(value = "Access-Control-Request-Headers", required = false) String requestHeaders,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {

        confirmCORS(requestMethods, requestHeaders, request, response);

        ResponseDTO server_response = new ResponseDTO();

        server_response.setPost(postService.getPostInfoList());
        return server_response;
    }

    @CrossOrigin(origins = "*") // 검색
    @RequestMapping(value = "/api/fundings/{title}", method = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST})
    public ResponseDTO searchPostInfo(@PathVariable("title") String title,
                                                @RequestHeader(value = "Access-Control-Request-Method", required = false) String requestMethods,
                                                @RequestHeader(value = "Access-Control-Request-Headers", required = false) String requestHeaders,
                                                HttpServletRequest request,
                                                HttpServletResponse response) {

        confirmCORS(requestMethods, requestHeaders, request, response);

        ResponseDTO server_response = new ResponseDTO();

        server_response.setPost(postService.findPostInfoByTitle(title));
        return server_response;
    }

    @CrossOrigin(origins = "*") // 공연 게시
    @RequestMapping(value = "/api/fundings/posting", method = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST})
    public CheckDTO insertPostInfo(@RequestHeader(value = "Access-Control-Request-Method", required = false) String requestMethods,
                                   @RequestHeader(value = "Access-Control-Request-Headers", required = false) String requestHeaders,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {

        confirmCORS(requestMethods, requestHeaders, request, response);

        String jsonString = inputFromJson(request);
        PostDTO client_request = new Gson().fromJson(jsonString, PostDTO.class);
        List<Reward_info> rewardDTO = client_request.getRewardList();

        postService.insertPost(client_request);
        CheckDTO server_response = new CheckDTO(null, true, true);

        return server_response;
    }

//    @CrossOrigin(origins = "*") // 공연 상세페이지
//    @RequestMapping(value = "/api/fundings/{post_id}", method = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST})
//    public Post_info getPostInfo(@PathVariable("post_id") Long post_id,
//                                 @RequestHeader(value = "Access-Control-Request-Method", required = false) String requestMethods,
//                                 @RequestHeader(value = "Access-Control-Request-Headers", required = false) String requestHeaders,
//                                 HttpServletRequest request,
//                                 HttpServletResponse response) {
//
//        confirmCORS(requestMethods, requestHeaders, request, response);
//
//        String jsonString = inputFromJson(request);
//
//        //????????????????????????????????????????????
//        Post_info server_response = postService.getPostInfoByPostId(post_id);
//        //????????????????????????????????????????????
//
//        return server_response;
//    }

    @CrossOrigin(origins = "*") // 공연 수정 전
    @RequestMapping(value = "/api/fundings/beforeModification", method = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST})
    public Post_info getPostInfo(@RequestHeader(value = "Access-Control-Request-Method", required = false) String requestMethods,
                                 @RequestHeader(value = "Access-Control-Request-Headers", required = false) String requestHeaders,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {

        confirmCORS(requestMethods, requestHeaders, request, response);

        String jsonString = inputFromJson(request);
        PostDTO client_request = new Gson().fromJson(jsonString, PostDTO.class);

        //????????????????????????????????????????????
        Post_info server_response = postService.getPostInfoById(client_request.getUser_info().getUser_id(), client_request.getTitle());
        //????????????????????????????????????????????

        return server_response;
    }

    @CrossOrigin(origins = "*") // 공연 수정 후
    @RequestMapping(value = "/api/fundings/afterModification", method = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
    public CheckDTO updatePostInfo(@RequestHeader(value = "Access-Control-Request-Method", required = false) String requestMethods,
                                   @RequestHeader(value = "Access-Control-Request-Headers", required = false) String requestHeaders,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {

        confirmCORS(requestMethods, requestHeaders, request, response);

        String jsonString = inputFromJson(request);
        PostDTO client_request = new Gson().fromJson(jsonString, PostDTO.class);
//        List<Reward_info> rewardDTO = client_request.getRewardList();

        postService.updatePost(client_request);

        CheckDTO server_response = new CheckDTO(null, true, true);

        return server_response;
    }

    @CrossOrigin(origins = "*") // 게시글 삭제
    @RequestMapping(value = "/api/fundings/deletePost", method = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE})
    public CheckDTO deletePost(@RequestHeader(value = "Access-Control-Request-Method", required = false) String requestMethods,
                           @RequestHeader(value = "Access-Control-Request-Headers", required = false) String requestHeaders,
                           HttpServletRequest request,
                           HttpServletResponse response) {

        confirmCORS(requestMethods, requestHeaders, request, response);
        String jsonString = inputFromJson(request);
        PostDTO client_request = new Gson().fromJson(jsonString, PostDTO.class);

        postService.deletePost(client_request.getTitle());
        CheckDTO server_response = new CheckDTO(null, true, true);

        return server_response;
    }




}