package com.example.showmori2.Controller;

import com.example.showmori2.Dto.*;
import com.example.showmori2.Service.DonationService;
import com.example.showmori2.Service.PostService;
import com.example.showmori2.Service.UserService;
import com.example.showmori2.domain.User_info;
import com.example.showmori2.domain.User_info_repository;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
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

    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private DonationService donationService;
//    private HttpSession session, userInfoSession;

    //CORS 허가
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
    
    //request 처리해서 string 으로 반환
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
            
            jsonObject = (JSONObject) parser.parse(body);
            jsonString = jsonObject.toString();

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

    ///////////////////////////////////////////////////User
    @CrossOrigin(origins = "*") // 로그인
    @RequestMapping(value = "/api/users/login", method = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST})
    public UserResponseDTO doLogin(@RequestHeader(value = "Access-Control-Request-Method", required = false) String requestMethods,
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

        return server_response;
    }

    @CrossOrigin(origins = "*") // 회원가입
    @RequestMapping(value = "/api/users/register", method = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
    public CheckDTO doRegister(@RequestHeader(value = "Access-Control-Request-Method", required = false) String requestMethods,
                               @RequestHeader(value = "Access-Control-Request-Headers", required = false) String requestHeaders,
                               HttpServletRequest request,
                               HttpServletResponse response) {

        confirmCORS(requestMethods, requestHeaders, request, response);


        String jsonString = inputFromJson(request);
        UserDTO client_request = new Gson().fromJson(jsonString, UserDTO.class);

        String user_id = client_request.getUser_id();
        String password = client_request.getPassword();
        String user_name = client_request.getUser_name();
        String user_phone = client_request.getUser_phone();

        CheckDTO server_response = new CheckDTO();
        if (userService.checkId(user_id)) { // id 중복 확인
            userService.saveUserinfo(user_id, password, user_name, user_phone); // db 저장

            server_response.setSuccess(true);
            server_response.setValidate(true);
        } else {
            server_response.setValidate(false);
            server_response.setSuccess(false);
        }

        return server_response;
    }

    @CrossOrigin(origins = "*") // 유저 정보 수정 전
    @RequestMapping(value = "/api/users/{user_id}/my-info", method = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST})
    public UserResponseDTO getUserInfo(@PathVariable("user_id")String user_id,
									    @RequestHeader(value = "Access-Control-Request-Method", required = false) String requestMethods,
									    @RequestHeader(value = "Access-Control-Request-Headers", required = false) String requestHeaders,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {

        confirmCORS(requestMethods, requestHeaders, request, response);
        
        User_info user_info = userService.findMyInfoById(user_id);
        UserResponseDTO server_response = new UserResponseDTO();

        server_response.setUser_id(user_id);
        server_response.setUser_name(user_info.getUser_name());
        server_response.setUser_phone(user_info.getUser_phone());
        server_response.setSuccess(true);

        return server_response;
    }

    @CrossOrigin(origins = "*") // 유저 정보 수정 후
    @RequestMapping(value = "/api/users/{user_id}/modify-profile", method = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
    public UserResponseDTO updateUserInfo(@PathVariable("user_id") String user_id,
                                          @RequestHeader(value = "Access-Control-Request-Method", required = false) String requestMethods,
                                          @RequestHeader(value = "Access-Control-Request-Headers", required = false) String requestHeaders,
                                          HttpServletRequest request,
                                          HttpServletResponse response) {

        confirmCORS(requestMethods, requestHeaders, request, response);


        String jsonString = inputFromJson(request);
        UserDTO client_request = new Gson().fromJson(jsonString, UserDTO.class);

        userService.updateUserInfo(user_id, //client_request.getPassword(),
                client_request.getUser_name(), client_request.getUser_phone());
        UserResponseDTO server_response = new UserResponseDTO();

        server_response.setUser_id(user_id);
        server_response.setSuccess(true);

        return server_response;
    }

    @CrossOrigin(origins = "*") // 회원 탈퇴
    @RequestMapping(value = "/api/users/{user_id}/unregister", method = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE})
    public CheckDTO deleteUserInfo(@PathVariable("user_id") String user_id,
                                          @RequestHeader(value = "Access-Control-Request-Method", required = false) String requestMethods,
                                          @RequestHeader(value = "Access-Control-Request-Headers", required = false) String requestHeaders,
                                          HttpServletRequest request,
                                          HttpServletResponse response) {

        confirmCORS(requestMethods, requestHeaders, request, response);

        
        if(userService.checkId(user_id)) return new CheckDTO(user_id, false, false); // user_id 가 애초에 존재 안 할 떄는 false

        userService.deleteUserInfo(user_id);
        CheckDTO server_response = new CheckDTO(user_id, userService.checkId(user_id), userService.checkId(user_id)); // 삭제 한 후 user 테이블에 user_id 유무에 따라 true/false

        return server_response;
    }

    ///////////////////////////////////////////////////funding
    @CrossOrigin(origins = "*") // DB에 저장되어있는 post_id들 프론트로
    @RequestMapping(value = "/api/funding/all-post-id", method = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST})
    public List<Long> getPostId(@RequestHeader(value = "Access-Control-Request-Method", required = false) String requestMethods,
                                @RequestHeader(value = "Access-Control-Request-Headers", required = false) String requestHeaders,
                                HttpServletRequest request,
                                HttpServletResponse response) {

        confirmCORS(requestMethods, requestHeaders, request, response);
        List<Long> server_response = new ArrayList<>();

        postService.deletePostToDate();
        
        for (PostResponseDTO postResponseDTO : postService.getPostId())
            server_response.add(postResponseDTO.getPost_id());

        return server_response;
    }

    @CrossOrigin(origins = "*") // 전체 게시글
    @RequestMapping(value = "/api/funding/all", method = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST})
    public List<PostResponseDTO> getPostInfoList(@RequestHeader(value = "Access-Control-Request-Method", required = false) String requestMethods,
                                                 @RequestHeader(value = "Access-Control-Request-Headers", required = false) String requestHeaders,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response) {

        confirmCORS(requestMethods, requestHeaders, request, response);

        postService.deletePostToDate();

        List<PostResponseDTO> server_response = postService.getPostInfoList();
        return server_response;
    }

    @CrossOrigin(origins = "*") // 검색
    @RequestMapping(value = "/api/discover/{title}", method = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST})
    public List<PostResponseDTO> searchPostInfo(@PathVariable("title") String title,
                                                @RequestHeader(value = "Access-Control-Request-Method", required = false) String requestMethods,
                                                @RequestHeader(value = "Access-Control-Request-Headers", required = false) String requestHeaders,
                                                HttpServletRequest request,
                                                HttpServletResponse response) {

        confirmCORS(requestMethods, requestHeaders, request, response);

        List<PostResponseDTO> server_response = postService.searchPost(title);
        return server_response;
    }

    @CrossOrigin(origins = "*") // 공연 게시
    @RequestMapping(value = "/api/funding/create-funding", method = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
    public CheckDTO insertPostInfo(@RequestHeader(value = "Access-Control-Request-Method", required = false) String requestMethods,
                                   @RequestHeader(value = "Access-Control-Request-Headers", required = false) String requestHeaders,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {

        confirmCORS(requestMethods, requestHeaders, request, response);

        String jsonString = inputFromJson(request);

        PostDTO postDTO = new Gson().fromJson(jsonString, PostDTO.class);
//        List<Reward_info> rewardDTO = client_request.getReward_list();

        postService.insertPost(postDTO); ////////오류나면 타이틀 체크....
        
    	return new CheckDTO(null, true, true);

    }
   
    @CrossOrigin(origins = "*") // 상세 정보
    @RequestMapping(value = "/api/funding/detail/{post_id}", method = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST})
    public PostResponseDTO getPostDetailInfo(@PathVariable("post_id") Long post_id,
								            @RequestHeader(value = "Access-Control-Request-Method", required = false) String requestMethods,
								            @RequestHeader(value = "Access-Control-Request-Headers", required = false) String requestHeaders,
								            HttpServletRequest request,
								            HttpServletResponse response) {

		confirmCORS(requestMethods, requestHeaders, request, response);
//		String jsonString = inputFromJson(request);
//		PostDTO postDTO = new Gson().fromJson(jsonString, PostDTO.class);
		
		PostResponseDTO server_response = postService.getPostDetail(post_id);
		
		return server_response;
    }

    
    @CrossOrigin(origins = "*") // 수정 전 전체 정보 뿌려주기
    @RequestMapping(value = "/api/funding/my-funding-info/{post_id}", method = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST})
    public PostResponseDTO getPostFundingInfo(@PathVariable("post_id") Long post_id,
                                             @RequestHeader(value = "Access-Control-Request-Method", required = false) String requestMethods,
                                             @RequestHeader(value = "Access-Control-Request-Headers", required = false) String requestHeaders,
                                             HttpServletRequest request,
                                             HttpServletResponse response) {

        confirmCORS(requestMethods, requestHeaders, request, response);

//        userid
//        post_id
//        -> title
//        poster
//        contents
//        goalsum
//        deadline
//        startday
//        lastday
//        rewardlist{}


//        String jsonString = inputFromJson(request);
//        System.out.println(jsonString);
//        PostDTO postDTO = new Gson().fromJson(jsonString, PostDTO.class);

        PostResponseDTO server_response = postService.getPostFundingInfo(post_id);//, postDTO.getUser_info().getUser_id());//, client_request.getUser_info().getUser_id());//, client_request.getTitle());
        return server_response;
    }

    @CrossOrigin(origins = "*") // 공연 수정 후
    @RequestMapping(value = "/api/funding/modify-funding/{post_id}", method = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
    public CheckDTO updatePostInfo(@PathVariable("post_id") Long post_id,
                                   @RequestHeader(value = "Access-Control-Request-Method", required = false) String requestMethods,
                                   @RequestHeader(value = "Access-Control-Request-Headers", required = false) String requestHeaders,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {

        confirmCORS(requestMethods, requestHeaders, request, response);

        String jsonString = inputFromJson(request);
        System.out.println(jsonString);
        PostDTO client_request = new Gson().fromJson(jsonString, PostDTO.class);

        
        client_request.setPost_id(post_id);
        postService.updatePost(client_request); // reward 포함

        CheckDTO server_response = new CheckDTO(null, true, true);

        return server_response;
    }

    @CrossOrigin(origins = "*") // 게시글 삭제 == 날짜 다 된것
    @RequestMapping(value = "/api/funding/remove/{post_id}", method = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE})
    public CheckDTO deletePost(@PathVariable("post_id") Long post_id,
                               @RequestHeader(value = "Access-Control-Request-Method", required = false) String requestMethods,
                               @RequestHeader(value = "Access-Control-Request-Headers", required = false) String requestHeaders,
                               HttpServletRequest request,
                               HttpServletResponse response) {

        confirmCORS(requestMethods, requestHeaders, request, response);

        postService.deletePost(post_id); //new CheckDTO(null, true, true);

        return new CheckDTO(null, true, true);
    }


    ///////////////////////////////////////////////////donation
    @CrossOrigin(origins = "*") //후원 하는 페이지
    @RequestMapping(value = "/api/funding/create-donation/{post_id}", method = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE})
    public CheckDTO insertDonation(@PathVariable("post_id") Long post_id,
                                   @RequestHeader(value = "Access-Control-Request-Method", required = false) String requestMethods,
                                   @RequestHeader(value = "Access-Control-Request-Headers", required = false) String requestHeaders,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {

//        User_id
//        post_id
//        donation_money
//        selected_date
//        -> success

        confirmCORS(requestMethods, requestHeaders, request, response);
        String jsonString = inputFromJson(request);
        DonationDTO donationDTO = new Gson().fromJson(jsonString, DonationDTO.class);

        donationService.insertDonationInfo(donationDTO, post_id);
        return new CheckDTO(donationDTO.getUser_info().getUser_id(), true, true);
    }



    @CrossOrigin(origins = "*") //게시글 올린 사람이 누가 후원했는지 확인하는
    @RequestMapping(value = "/api/users/funding-backer-list/{user_id}", method = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE})
    public List<PostResponseDTO> getDonationByFundingUser(@PathVariable("user_id") String user_id,
                                                           @RequestHeader(value = "Access-Control-Request-Method", required = false) String requestMethods,
                                                           @RequestHeader(value = "Access-Control-Request-Headers", required = false) String requestHeaders,
                                                           HttpServletRequest request,
                                                           HttpServletResponse response) {

//        -> title
//        total_donation
//    	  image
//        goal_sum
//        dead_line
//        donation{
//              user_id
//              user_phone
//              donation_money
//              selected_date }
//         select * from user u, donation d, post p where p.user_id = "apple" and d.post_id = p.post_id and u.user_id = d.user_id;

        confirmCORS(requestMethods, requestHeaders, request, response);

        //user_id 가 게시한 포스트 리스트
        List<PostResponseDTO> postResponseDTOList = postService.findPostInfoByUserId(user_id);

        for(int i = 0; i < postResponseDTOList.size(); i ++){
            Long post_id = postResponseDTOList.get(i).getPost_id();
            List<DonationResponseDTO> donationResponseDTOList = donationService.getDonation_infoByPostID(post_id);
            
            if(donationResponseDTOList.isEmpty()) {
                postResponseDTOList.remove(i --);
                continue;
            }
            postResponseDTOList.get(i).setDonation_list(donationResponseDTOList);
            for(int j = 0; j < donationResponseDTOList.size(); j ++){
            	int donation_money = donationResponseDTOList.get(j).getDonation_money();
            	
                List<UserResponseDTO> userResponseDTOList = userService.getUserPhoneByUserIdFromDonation(post_id, donation_money);
                donationResponseDTOList.get(j).setUser(userResponseDTOList);
            }
        }

        return postResponseDTOList;

    }

    @CrossOrigin(origins = "*") // 후원한 유저 자신의 후원 내역
    @RequestMapping(value = "/api/users/backup-list/{user_id}", method = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE})
    public List<PostResponseDTO> getDonationByDonationUser(@PathVariable("user_id") String user_id,
						                                   @RequestHeader(value = "Access-Control-Request-Method", required = false) String requestMethods,
						                                   @RequestHeader(value = "Access-Control-Request-Headers", required = false) String requestHeaders,
						                                   HttpServletRequest request,
						                                   HttpServletResponse response) {


        confirmCORS(requestMethods, requestHeaders, request, response);
//        Title
//        total_donation
//        goal_sum
//        donation_info{
//            donation_money
//        }
//        reward_list {
//        }

        List<Long> postIDList = donationService.getPostIdByUserId(user_id);
        Collections.sort(postIDList);
        
        List<PostResponseDTO> server_response = new ArrayList<>();

        for(int i = 0; i < postIDList.size(); i ++) {
        	if(i + 1 < postIDList.size() && postIDList.get(i) == postIDList.get(i + 1)) i ++;
        	
        	PostResponseDTO postResponseDTO = postService.getPostByPostId(postIDList.get(i));

        	List<DonationResponseDTO> donationResponseDTOList = donationService.getDonation_infoByPostID(postIDList.get(i), user_id);
        	postResponseDTO.setDonation_list(donationResponseDTOList);
        	
        	List<RewardResponseDTO> rewardResponseDTOList = postService.getRewardinfoByPostID(postIDList.get(i));
        	postResponseDTO.setReward_list(rewardResponseDTOList);
        	
        	server_response.add(postResponseDTO);
        }

        return server_response;

    }
    
    @CrossOrigin(origins = "*") // 후원 수정 전 
    @RequestMapping(value = "/api/users/donation-info/{post_id}", method = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE})
    public List<DonationResponseDTO>getMyDonation(@PathVariable("user_id") String user_id,
                                   @RequestHeader(value = "Access-Control-Request-Method", required = false) String requestMethods,
                                   @RequestHeader(value = "Access-Control-Request-Headers", required = false) String requestHeaders,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
//        user_id
//        Title
//        Poster
//        Contents
//        goal_sum
//        dead_line
//        start_day
//        last_day
//        reward_list {
//        }
//
//        Validate
//        Success
        confirmCORS(requestMethods, requestHeaders, request, response);
        
        List<DonationResponseDTO> server_response = donationService.getDonation_infoByUserID(user_id);
        
        return server_response;
        
    }

    @CrossOrigin(origins = "*") // 후원 수정 후
    @RequestMapping(value = "/api/users/modify-donation/{post_id}", method = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE})
    public CheckDTO updateDonation(@PathVariable("user_id") String user_id,
                                   @RequestHeader(value = "Access-Control-Request-Method", required = false) String requestMethods,
                                   @RequestHeader(value = "Access-Control-Request-Headers", required = false) String requestHeaders,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
//        user_id
//        Title
//        Poster
//        Contents
//        goal_sum
//        dead_line
//        start_day
//        last_day
//        reward_list {
//        }
//
//        Validate
//        Success
        confirmCORS(requestMethods, requestHeaders, request, response);
        String jsonString = inputFromJson(request);

        DonationDTO donationDTO = new Gson().fromJson(jsonString, DonationDTO.class);
        donationDTO.getUser_info().setUser_id(user_id);

        donationService.updateDonation(donationDTO);

        return new CheckDTO(user_id, true, true);
    }

    @CrossOrigin("*") // 후원 삭제
    @RequestMapping(value = "/api/users/{user_id}/remove/{title}", method = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST})
    public CheckDTO deleteDonation(@PathVariable("user_id") String user_id,
                               @PathVariable("title") String title,
                               @RequestHeader(value = "Access-Control-Request-Method", required = false) String requestMethods,
                               @RequestHeader(value = "Access-Control-Request-Headers", required = false) String requestHeaders,
                               HttpServletRequest request,
                               HttpServletResponse response) {
    	
        confirmCORS(requestMethods, requestHeaders, request, response);

        if(donationService.checkUserIdAndTitle(user_id, title)) return new CheckDTO(user_id, false, false); // title이 애초에 존재 안 할 떄는 false

        donationService.deleteDonation_infoByUserIdAndTitle(user_id, title);
        CheckDTO server_response = new CheckDTO(user_id, donationService.checkUserIdAndTitle(user_id, title), 
        		donationService.checkUserIdAndTitle(user_id, title)); // 삭제 한 후 post 테이블에 title 유무에 따라 true/false
        
        return server_response;
    }
}



//{
//"post":[
//        {
//            "title":"string",
//            "total_donation":"int",
//            "goal_sum":"int",
//            "deadline":"date",
//            "user_info...?" : [
//                {
//                    "user_id":"string(후원한 사람들)",
//                    "user_name" : "string",
//                    "user_phone":"string(후원한 사람들)",
//                    "donation" : [
//                        {
//                            "donation_money" : "int",
//                            "selected_day" : "string"
//                        },
//                         ...
//                    ]
//                },
//                 ...
//            ]
//        }
//    ]
//}

//[
//	{
//		"post_id":1,
//		"poster":"오페라의 유령.jpg",
//		"image":null,
//		"title":"오페라의 유령",
//		"contents":"오페라의 유령 시놉.jpg",
//		"goal_sum":400000,
//		"dead_line":"2020-11-07",
//		"total_donation":10000,
//		"donation_list":[
//			{
//			"donation_money":30000,
//			"selected_date":"2020-11-10 09:00:00.0",
//				"user":[
//					{
//					"user_id":"cherry",
//					"password":"cherry",
//					"user_name":"체리",
//					"user_phone":"010-6666-6666",
//					}
//				],
//			}
//		]
//	},
//]


//{
//    "user":{
//        "user_id" : "string",
//            "post" : [
//            {
//                "title":"string",
//                "total_donation":"int",
//                "goal_sum":"int",
//                "deadline":"date",
//                "donation" : [
//                    {
//                        "donation_money" : "int",
//                        "selected_day" : "string"
//                    },
//                    ...
//                ]
//            },
//             ...
//        ]
//    }
//}

//{
//    post : [{
//        title : string
//        total_donation : int
//        goal_sum : int
//        deadline : date
//        donation : [{
//            user_id : String
//            donation_money : int
//            selected_date : String(내가 고른 관람 날짜)
//          },
//          ...
//        ]
//      },
//      ...
//    ]
//}