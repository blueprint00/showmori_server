package com.example.showmori2.Service;

import com.example.showmori2.Dto.*;
import com.example.showmori2.domain.Post_info_repository;
import com.example.showmori2.domain.Reward_info_repository;

import org.apache.commons.codec.binary.Base64;
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

import java.util.Calendar;
import java.util.GregorianCalendar;

@Service
public class PostService {

    @Autowired
    private Post_info_repository post_info_repository;
    @Autowired
    private Reward_info_repository reward_info_repository;

    //프론트에 이미지 돌려주기
    public String returnImage(String posterName){

        String path = "/Users/kay/00project/code/showmori_server-master/showmori_eclipse/src/main/resources/images/";
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

        return Base64.encodeBase64String(imageByte);
    }

    // 받은 이미지 로컬에 저장
    public void saveImage(String posterName, String imageString) {
        String path = "/Users/kay/00project/code/showmori_server-master/showmori_eclipse/src/main/resources/images/";

        System.out.println(posterName + "\n" + imageString);
        String[] base = imageString.split(",");

        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.decodeBase64(base[1]));
            BufferedImage bufferedImage1 = ImageIO.read(inputStream);
            ImageIO.write(bufferedImage1, "jpg", new File(path + posterName));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    // 로컬에서 이미지 삭제
    public void deleteImage(String posterName){
        String path = "/Users/kay/00project/code/showmori_server-master/showmori_eclipse/src/main/resources/images/";
        File file = new File(path + posterName);

        System.out.println(path + posterName);
        if(file.exists()){
            file.delete();
        }
    }
    
    //날짜 지난 포스트삭제
    @Transactional
    public void deletePostToDate() {
    	List<PostResponseDTO> postResponseDTOList = post_info_repository.getPostList()
                .map(PostResponseDTO::new)
                .collect(Collectors.toList());
    	
    	Calendar cal = new GregorianCalendar();
    	Date curDate = new Date(cal.getTimeInMillis());

    	for(PostResponseDTO dto : postResponseDTOList) {
    		if(curDate.compareTo(dto.getDead_line()) >= 0) { // deadline 넘어갔을경우
    			post_info_repository.deletePostByPostID(dto.getPost_id());
    			deleteImage(dto.getPoster());
    			deleteImage(dto.getContents());
    		}
    	}	
    }

    @Transactional // post_id 만 보내주기
    public List<PostResponseDTO> getPostId(){
        return post_info_repository.getPostList()
                .map(PostResponseDTO::new)
                .collect(Collectors.toList());
    }


    @Transactional // 메이 페이지 == 전체 공연
    public List<PostResponseDTO> getPostInfoList(){
        List<PostResponseDTO> postResponseDTOList = post_info_repository.getPostList()
                        .map(PostResponseDTO::new)
                        .collect(Collectors.toList());

        for (int i = 0; i < postResponseDTOList.size(); i++) {
            postResponseDTOList.get(i).setPoster_image(returnImage(postResponseDTOList.get(i).getPoster()));
        }
                
        return postResponseDTOList;
    }

    // user_id가 올린 게시글 리스트
//    @Transactional
//    public List<PostResponseDTO> findPostInfoByUserId(String user_id){
//        return post_info_repository.findPostInfoByUserId(user_id)
//                .map(PostResponseDTO::new)
//                .collect(Collectors.toList());
//    }


    @Transactional // 검색
    public List<PostResponseDTO> searchPost(String title){
        List<PostResponseDTO> postResponseDTOList = post_info_repository.findPostInfoByTitle(title)
                .map(PostResponseDTO::new)
                .collect(Collectors.toList());

        for (int i = 0; i < postResponseDTOList.size(); i++) {
            postResponseDTOList.get(i).setPoster_image(returnImage(postResponseDTOList.get(i).getPoster()));
            postResponseDTOList.get(i).setContents_image(returnImage(postResponseDTOList.get(i).getContents_image()));
        }

        return postResponseDTOList;
    }

    @Transactional // 공연 게시
    public void insertPost(PostDTO postDTO) {
//        List<PostResponseDTO> postResponseDTOList= post_info_repository.getPostList()
//                .map(PostResponseDTO::new)
//                .collect(Collectors.toList());
//
//        for(PostResponseDTO dto : postResponseDTOList){
//            if(postDTO.getTitle().equals(dto.getTitle()))
//                return new CheckDTO(postDTO.getUser_info().getUser_id(), false, false);
//        }

        ////////가능?
        if(post_info_repository.checkTitle(postDTO.getTitle()) == 0) { //null이면 없는거니까....
            post_info_repository.insertPost(postDTO.getPoster(),
                    postDTO.getTitle(), postDTO.getContents(),
                    postDTO.getTotal_donation(),
                    postDTO.getGoal_sum(),
                    Date.valueOf(postDTO.getDead_line()),
                    Date.valueOf(postDTO.getStart_day()),
                    Date.valueOf(postDTO.getLast_day()),
                    postDTO.getUser_info().getUser_id());
    //        post_info_repository.save(postDTO.toEntity());

            String poster_image = postDTO.getPoster_image();
            saveImage(postDTO.getPoster(), poster_image);
            String content_image = postDTO.getContents_image();
            saveImage(postDTO.getContents(), content_image);

            Long post_id = post_info_repository.getPostIdByTitle(postDTO.getTitle());

            for (RewardDTO dto : postDTO.getReward_list()) {
                reward_info_repository.insertReward(post_id, dto.getReward_money(), dto.getReward());
            }
        }
    }

    // 게시물 상세 
    @Transactional
    public PostResponseDTO getPostDetail(Long post_id) {
    	 PostResponseDTO postResponseDTO = post_info_repository.getPostInfoByPostID(post_id)
					.map(PostResponseDTO::new)
					.collect(Collectors.toList())
					.get(0);

    	 postResponseDTO.setPoster_image(returnImage(postResponseDTO.getPoster()));
    	 postResponseDTO.setContents_image(returnImage(postResponseDTO.getContents()));
    	 List<RewardResponseDTO> rewardResponseDTOList = reward_info_repository.getRewardinfoByPostID(post_id)
                 .map(RewardResponseDTO::new).collect(Collectors.toList());

    	 postResponseDTO.setReward_list(rewardResponseDTOList);
    	
    	 return postResponseDTO;
    }
    
    @Transactional // 게시물 수정 하기 전에- > post_id로 무슨 포스트인지 ????
    public PostResponseDTO getPostFundingInfo(Long post_id){//, String user_id){//}, String title){

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

        PostResponseDTO postResponseDTO = post_info_repository.getPostInfoByPostID(post_id)
        													.map(PostResponseDTO::new)
        													.collect(Collectors.toList())
        													.get(0);
        // 오류 나면 Date 때문임...???

        postResponseDTO.setPoster_image(returnImage(postResponseDTO.getPoster()));
        postResponseDTO.setContents_image(returnImage(postResponseDTO.getContents()));
        
        List<RewardResponseDTO> rewardResponseDTOList = reward_info_repository.getRewardinfoByPostID(post_id)
                .map(RewardResponseDTO::new).collect(Collectors.toList());

        // 게시한 유저와 수정하려는 유저가 같은지
//        if(user_id.equals(post_info_repository.getUserInfoByPostId(post_id).getUser_id())) {
            postResponseDTO.setReward_list(rewardResponseDTOList);
//        } else postResponseDTO = null;

        return postResponseDTO;
    }

    @Transactional // 수정후 저장
    public void updatePost(PostDTO postDTO){

        post_info_repository.updatePost(postDTO.getPost_id(),postDTO.getPoster(), postDTO.getTitle(), postDTO.getContents(),
                                    postDTO.getTotal_donation(), postDTO.getGoal_sum(),
                                    Date.valueOf(postDTO.getDead_line()),
                                    Date.valueOf(postDTO.getStart_day()),
                                    Date.valueOf(postDTO.getLast_day()));

        System.out.println(postDTO.getPoster());

        for(RewardDTO dto : postDTO.getReward_list()) {
            reward_info_repository.updateReward(postDTO.getPost_id(), dto.getReward_money(), dto.getReward());
        }
        saveImage(postDTO.getPoster(), postDTO.getPoster_image());
        saveImage(postDTO.getContents(), postDTO.getContents_image());
    }


    @Transactional // 삭제
    public void deletePost(long post_id){

//        donation_info_repository.deleteDonation_infoByPostID(post_id);
//        reward_info_repository.deleteRewardByPostID(post_id);
        post_info_repository.deletePostByPostID(post_id);
        
        String poster = post_info_repository.getPostInfoByPostID(post_id)
            	.map(PostResponseDTO::new)
            	.collect(Collectors.toList()).get(0).getPoster();
        String content = post_info_repository.getPostInfoByPostID(post_id)
            	.map(PostResponseDTO::new)
            	.collect(Collectors.toList()).get(0).getContents();
        deleteImage(poster);
        deleteImage(content);
    }

    // 게시물 올린 사람이 누가 후원했나 알아보는
    @Transactional
    public List<PostResponseDTO> findPostInfoByUserId(String user_id) {
        return post_info_repository.findPostInfoByUserId(user_id)
                .map(PostResponseDTO::new)
                .collect(Collectors.toList());
    }

    // 후원한 유저 자신의 후원 내역 > 후원한 포스트에 맞는 리워드 리스트
    @Transactional
    public List<RewardResponseDTO> getRewardinfoByPostID(Long post_id) {
        return reward_info_repository.getRewardinfoByPostID(post_id)
        		.map(RewardResponseDTO::new)
        		.collect(Collectors.toList());
    }

    // 후원한 유저 자신의 후원 내역 > 후원한 포스트 리스트
    @Transactional
    public PostResponseDTO getPostByPostId(Long post_id) {
        return post_info_repository.getPostInfoByPostID(post_id)
        	.map(PostResponseDTO::new)
        	.collect(Collectors.toList())
        	.get(0);
    }
    
    //title 중복 체크
    @Transactional
    public boolean checkTitle(String title) {
    	if(post_info_repository.checkTitle(title) == null) return true;
    	return false;    
    }

}


