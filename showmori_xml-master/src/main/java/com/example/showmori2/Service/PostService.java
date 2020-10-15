package com.example.showmori2.Service;

import com.example.showmori2.Dto.*;
import com.example.showmori2.domain.*;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.google.gson.Gson;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private Post_info_repository post_info_repository;
    @Autowired
    private Reward_info_repository reward_info_repository;

    public String returnImage(String posterName){

        String path = "/Users/kay/00project/code/showmori_server-master/showmori_server/showmori_xml-master/src/main/resources/images/";
        File file = new File(path + posterName);
        byte[] imageByte = null;

        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", baos);
            bufferedImage.flush();

            imageByte = baos.toByteArray();
            baos.close();

        } catch (IOException e){
            e.printStackTrace();
        }
//        ByteArrayInputStream inputStream = new ByteArrayInputStream(imageByte);
//        BufferedImage bufferedImage1 = ImageIO.read(inputStream);

//        ImageIO.write(bufferedImage1, "jpg", new File("/Users/kay/00project/code/showmori_server-master/showmori_server/showmori_xml-master/src/main/resources/images/" + "ㅔㅣㅋ입니다 제발444.jpg"));

        return Base64.encode(imageByte);
    }

    public void saveImage(String posterName, String imageString) {
        String path = "/Users/kay/"; //"/Users/kay/00project/code/showmori_server-master/showmori_server/showmori_xml-master/src/main/resources/images/";
//        File file = new File(path + imageName);
//        BufferedImage bufferedImage = ImageIO.read(file);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ImageIO.write(bufferedImage, "jpg", baos);
//        bufferedImage.flush();
//
//        byte[] imageByte = baos.toByteArray();
//        baos.close();

        System.out.println(posterName + "\n" + imageString);
        String[] base = imageString.split(",");

        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.decode(base[1]));
            BufferedImage bufferedImage1 = ImageIO.read(inputStream);
            ImageIO.write(bufferedImage1, "jpg", new File(path + posterName + ".jpg"));
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    @Transactional // 메이 페이지 == 전체 공연
    public List<PostResponseDTO> getPostInfoList(){
        List<PostResponseDTO> postResponseDTOList = post_info_repository.getPostList()
                        .map(PostResponseDTO::new)
                        .collect(Collectors.toList());

        for (int i = 0; i < postResponseDTOList.size(); i++) {
//            postResponseDTOList.get(i).setImage(returnImage(postResponseDTOList.get(i).getPoster()));
            PostResponseDTO dto = new PostResponseDTO();
            dto = postResponseDTOList.get(i);
            dto.setImage(returnImage(postResponseDTOList.get(i).getPoster()));

            postResponseDTOList.set(i, dto);
        }

        return postResponseDTOList;
    }

    @Transactional // 검색
    public List<PostResponseDTO> findPostInfoByTitle(String title){
        List<PostResponseDTO> postResponseDTOList = post_info_repository.findPostInfoByTitle(title)
                .map(PostResponseDTO::new)
                .collect(Collectors.toList());

        PostResponseDTO postResponseDTO = new PostResponseDTO();
        for (int i = 0; i < postResponseDTOList.size(); i++) {
            postResponseDTOList.get(i).setImage(returnImage(postResponseDTOList.get(i).getPoster()));
        }

//        List<PostResponseDTO> response = new ArrayList<>();
//
//        for(int i = 0; i < postList.size(); i ++){
//            List<RewardResponseDTO> rewardList = reward_info_repository.getRewardinfoByPostId(postList.get(i).getPost_id())
//                                                .map(RewardResponseDTO::new)
//                                                .collect(Collectors.toList());
//            User_info user_info = post_info_repository.getUserInfoById(postList.get(i).getPost_id());
//
//            postList.get(i).setReward_list(rewardList);
//            postList.get(i).setUser_info(user_info);
//        }

        return postResponseDTOList;
    }

    @Transactional // 공연 게시
    public void insertPost(PostDTO postDTO) {
       post_info_repository.insertPost(postDTO.getPoster(),
                postDTO.getTitle(), postDTO.getContents(),
                postDTO.getTotal_donation(),
                postDTO.getGoal_sum(),
                Date.valueOf(postDTO.getDead_line()),
                Date.valueOf(postDTO.getStart_day()),
                Date.valueOf(postDTO.getLast_day()),
                postDTO.getUser_info().getUser_id());
//        post_info_repository.save(postDTO.toEntity());

        String image = postDTO.getImage();
        saveImage(postDTO.getPoster(), image);
        Long post_id = post_info_repository.getPostIdByTitle(postDTO.getTitle());

        for (Reward_info reward_info : postDTO.getReward_list()) {
            reward_info_repository.updateReward(post_id, reward_info.getReward_money(), reward_info.getReward());
        }
    }

    @Transactional //검색...
    public PostResponseDTO getPostInfoByPostId(Long post_id){
            PostResponseDTO postResponseDTO = post_info_repository.getPostInfoByPostId(post_id);
            return postResponseDTO;
    }


    @Transactional // 게시물 수정 - > post_id로 무슨 포스트인지
    public PostResponseDTO getPostInfoById(Long post_id, String user_id){//}, String title){

//        String post_info = post_info_repository.getPostInfoByPostId(post_id).toString();
//
//        JSONParser parser = new JSONParser();
//        try {
//            JSONObject jsonObject = (JSONObject) parser.parse(post_info);
//            postResponseDTO = new Gson().fromJson(jsonObject.toString(), PostResponseDTO.class);
//            postResponseDTO.setReward_list(reward_info_repository.getRewardinfoByPostId(post_id)
//                                .map(RewardResponseDTO::new).collect(Collectors.toList()));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        PostResponseDTO postResponseDTO = post_info_repository.getPostInfoByPostId(post_id);

        // 게시한 유저와 수정하려는 유저가 같은지
        if(postResponseDTO.getUser_info().getUser_id() == user_id) {
            postResponseDTO.setReward_list(reward_info_repository.getRewardinfoByPostId(post_id)
                    .map(RewardResponseDTO::new).collect(Collectors.toList()));
        } else postResponseDTO = null;

        return postResponseDTO;
    }

    @Autowired
    private PostFileManager fileManager;
//    @Value("${ImageFilePath}")
//    private String filepath;

    @Transactional // 수정
    public void updatePost(PostDTO postDTO){//, MultipartFile file){
//    public void updatePost(MultipartFile file){
//        postDTO.setPoster(StringUtils.cleanPath(file.getOriginalFilename()));

        post_info_repository.updatePost(postDTO.getPoster(), postDTO.getTitle(), postDTO.getContents(), postDTO.getTotal_donation(), postDTO.getGoal_sum(),
                                    Date.valueOf(postDTO.getDead_line()),
                                    Date.valueOf(postDTO.getStart_day()),
                                    Date.valueOf(postDTO.getLast_day()),
                                    postDTO.getUser_info().getUser_id());

        System.out.println(postDTO.getPoster());

        Base64.decode(postDTO.getImage());

        for(Reward_info reward_info : postDTO.getReward_list()) {
            reward_info_repository.updateReward(postDTO.getPost_id(), reward_info.getReward_money(), reward_info.getReward());
        }

        saveImg(postDTO.getPoster());

//        try {
//            String originalName = file.getOriginalFilename();
//            System.out.println("origin  : " + originalName);
//            fileManager.fileUpload(file, originalName);
//        } catch (Exception e){
//            e.printStackTrace();
//        }
    }

    public void saveImg(String fileName) {
//        String imgUrl = "https://postfiles.pstatic.net/MjAxOTExMTZfMjQ2/MDAxNTczODQxNjg3NTYz.pXFjEQ3P8_uFgacA25iqWl0GnjCMQlpmbOvq4DS38AQg.6DIhw7fyF2fGYnWq-Qhx6qAqa98K4XWsAHsYg-9vrMIg.PNG.halowd/JSP.png?type=w966";
//        String path = "/Users/kay/00project/code/showmori_server-master/showmori_server/showmori_xml-master/src/main/resources/images/";

        PostSaveImg postSaveImg = new PostSaveImg();

        try {
            int result = postSaveImg.saveImgFromUrl(fileName); // 성공 시 1 리턴, 오류 시 -1 리턴
            if (result == 1) {
                System.out.println("저장된경로 : " + postSaveImg.getPath());
                System.out.println("저장된파일이름 : " + postSaveImg.getSavedFileName());
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Transactional // 삭제
    public void deletePost(long post_id){
        post_info_repository.deletePost(post_id);
        reward_info_repository.deleteRewardByPostID(post_id);
    }

    @Transactional
    public Long getPostIdByTitle(String title){
        return post_info_repository.getPostIdByTitle(title);
    }


    @Transactional
    public List<RewardResponseDTO> getRewardinfoByPostId(Long post_id){
        return reward_info_repository.getRewardinfoByPostId(post_id)
                .map(RewardResponseDTO::new)
                .collect(Collectors.toList());
    }

}


