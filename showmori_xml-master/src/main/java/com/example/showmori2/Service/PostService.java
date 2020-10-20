package com.example.showmori2.Service;

import com.example.showmori2.Dto.*;
import com.example.showmori2.domain.*;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private Post_info_repository post_info_repository;
    @Autowired
    private Reward_info_repository reward_info_repository;
    @Autowired
    private Donation_info_repository donation_info_repository;

    public String returnImage(String posterName){

        System.out.println("posterName " + posterName + " + " + posterName);

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
        String path = "/Users/kay/00project/code/showmori_server-master/showmori_server/showmori_xml-master/src/main/resources/images/";

        System.out.println(posterName + "\n" + imageString);
        String[] base = imageString.split(",");

        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.decode(base[1]));
            BufferedImage bufferedImage1 = ImageIO.read(inputStream);
            ImageIO.write(bufferedImage1, "jpg", new File(path + posterName));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void deleteImage(String posterName){
        String path = "/Users/kay/00project/code/showmori_server-master/showmori_server/showmori_xml-master/src/main/resources/images/";
        File file = new File(path + posterName);

        System.out.println(path + posterName);
        if(file.exists()){
            file.delete();
        }
    }

    @Transactional // 메이 페이지 == 전체 공연
    public List<PostResponseDTO> getPostInfoList(){
        List<PostResponseDTO> postResponseDTOList = post_info_repository.getPostList()
                        .map(PostResponseDTO::new)
                        .collect(Collectors.toList());

        for (int i = 0; i < postResponseDTOList.size(); i++) {
            postResponseDTOList.get(i).setImage(returnImage(postResponseDTOList.get(i).getPoster()));
        }

        return postResponseDTOList;
    }

    @Transactional
    public List<PostResponseDTO> getPostId(){

        System.out.println(post_info_repository.getPostList()
                .map(PostResponseDTO::new)
                .collect(Collectors.toList()).get(0).getPost_id());

        return post_info_repository.getPostList()
                .map(PostResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional // 검색
    public List<PostResponseDTO> searchPost(String title){
        List<PostResponseDTO> postResponseDTOList = post_info_repository.findPostInfoByTitle(title)
                .map(PostResponseDTO::new)
                .collect(Collectors.toList());

        for (int i = 0; i < postResponseDTOList.size(); i++) {
            postResponseDTOList.get(i).setImage(returnImage(postResponseDTOList.get(i).getPoster()));
        }

        return postResponseDTOList;
    }

    @Transactional // 공연 게시
    public CheckDTO insertPost(PostDTO postDTO) {
        List<PostResponseDTO> postResponseDTOList= post_info_repository.getPostList()
                .map(PostResponseDTO::new)
                .collect(Collectors.toList());

        for(PostResponseDTO dto : postResponseDTOList){
            if(postDTO.getTitle().equals(dto.getTitle()))
                return new CheckDTO(postDTO.getUser_info().getUser_id(), false, false);
        }

        post_info_repository.insertPost(postDTO.getPoster(),
                postDTO.getTitle(), postDTO.getContents(),
                postDTO.getTotal_donation(),
                postDTO.getGoal_sum(),
                Date.valueOf(postDTO.getDead_line()),
                Date.valueOf(postDTO.getStart_day()),
                Date.valueOf(postDTO.getLast_day()),
                postDTO.getUser_info().getUser_id());
//        post_info_repository.save(postDTO.toEntity());

        try {
            String image = postDTO.getImage();
            saveImage(postDTO.getPoster(), image);
            Long post_id = post_info_repository.getPostIdByTitle(postDTO.getTitle());

            for (Reward_info reward_info : postDTO.getReward_list()) {
                reward_info_repository.insertReward(post_id, reward_info.getReward_money(), reward_info.getReward());
            }
        } catch (Exception e){
            return new CheckDTO(postDTO.getUser_info().getUser_id(), false, false);
        }
        return new CheckDTO(postDTO.getUser_info().getUser_id(),true, true);
    }

    @Transactional // 게시물 수정 - > post_id로 무슨 포스트인지
    public PostResponseDTO getPostDetailInfo(Long post_id, String user_id){//}, String title){

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

        PostResponseDTO postResponseDTO = post_info_repository.getPostInfoByPostID(post_id);

        // 게시한 유저와 수정하려는 유저가 같은지
        if(postResponseDTO.getUser_info().getUser_id() == user_id) {
            postResponseDTO.setReward_list(reward_info_repository.getRewardinfoByPostID(post_id)
                    .map(RewardResponseDTO::new).collect(Collectors.toList()));
        } else postResponseDTO = null;

        return postResponseDTO;
    }

//    @Autowired
//    private PostFileManager fileManager;

    @Transactional // 수정후 저장
    public CheckDTO updatePost(PostDTO postDTO){//, MultipartFile file){
//    public void updatePost(MultipartFile file){
//        postDTO.setPoster(StringUtils.cleanPath(file.getOriginalFilename()));

        post_info_repository.updatePost(postDTO.getPost_id(),postDTO.getPoster(), postDTO.getTitle(), postDTO.getContents(),
                                    postDTO.getTotal_donation(), postDTO.getGoal_sum(),
                                    Date.valueOf(postDTO.getDead_line()),
                                    Date.valueOf(postDTO.getStart_day()),
                                    Date.valueOf(postDTO.getLast_day()),
                                    postDTO.getUser_info().getUser_id());

        System.out.println(postDTO.getPoster());

        for(Reward_info reward_info : postDTO.getReward_list()) {
            reward_info_repository.updateReward(postDTO.getPost_id(), reward_info.getReward_money(), reward_info.getReward());
        }
        saveImage(postDTO.getPoster(), postDTO.getImage());

        return new CheckDTO(true);

//        try {
//            String originalName = file.getOriginalFilename();
//            System.out.println("origin  : " + originalName);
//            fileManager.fileUpload(file, originalName);
//        } catch (Exception e){
//            e.printStackTrace();
//        }
    }

//    public void saveImg(String fileName) {
////        String imgUrl = "https://postfiles.pstatic.net/MjAxOTExMTZfMjQ2/MDAxNTczODQxNjg3NTYz.pXFjEQ3P8_uFgacA25iqWl0GnjCMQlpmbOvq4DS38AQg.6DIhw7fyF2fGYnWq-Qhx6qAqa98K4XWsAHsYg-9vrMIg.PNG.halowd/JSP.png?type=w966";
////        String path = "/Users/kay/00project/code/showmori_server-master/showmori_server/showmori_xml-master/src/main/resources/images/";
//
//        PostSaveImg postSaveImg = new PostSaveImg();
//
//        try {
//            int result = postSaveImg.saveImgFromUrl(fileName); // 성공 시 1 리턴, 오류 시 -1 리턴
//            if (result == 1) {
//                System.out.println("저장된경로 : " + postSaveImg.getPath());
//                System.out.println("저장된파일이름 : " + postSaveImg.getSavedFileName());
//            }
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }

    @Transactional // 삭제
    public CheckDTO deletePost(long post_id){
        try {
            deleteImage(post_info_repository.getPosterByPostID(post_id));

            donation_info_repository.deleteDonation_infoByPostID(post_id);
            reward_info_repository.deleteRewardByPostID(post_id);
            post_info_repository.deletePostByPostID(post_id);

            } catch (Exception e){
            return new CheckDTO(false);
        }
        return new CheckDTO(String.valueOf(post_id), true, false);
    }

}


