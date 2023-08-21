package com.spring.community.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.community.DTO.ReplyCreateRequestDTO;
import com.spring.community.entity.Post;
import com.spring.community.entity.Reply;
import com.spring.community.repository.ReplyRepository;
import com.spring.community.service.ReplyService;
import com.spring.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class ReplyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReplyService replyService;



    @Test
    @Transactional
    public void getAllRepliesByPostIdTest() throws Exception {
        // given
        String url = "/reply/102/all";

        // when
        ResultActions resultActions = mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @Transactional
    public void getReplyByReplyIdTest() throws Exception {
        // given
        String url = "/reply/1";

        // when
        ResultActions resultActions = mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("This is a reply to the first post."));
    }

    @Test
    @Transactional
    public void createReplyTest() throws Exception {
        // given
        long postId = 101;
        String content = "create reply test";
        long userId = 1;

        String url = "/reply";

        ReplyCreateRequestDTO replyCreateRequestDTO = new ReplyCreateRequestDTO();
        replyCreateRequestDTO.setPostId(postId);
        replyCreateRequestDTO.setContent(content);

        // 데이터 직렬화
        final String requestBody = objectMapper.writeValueAsString(replyCreateRequestDTO);

        // when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string("댓글이 등록되었습니다."));
    }

    @Test
    @Transactional
    @DisplayName("댓글번호 3번 삭제시 글번호 102의 댓글수 1개, 단일댓글 조회시 null")
    public void deleteReplyTest() throws Exception {
        // given
        long replyId = 3;
        long postId = 102;
        String url = "/reply/3";

        // when
        ResultActions resultActions =  mockMvc.perform(delete(url)
                .accept(MediaType.TEXT_PLAIN));

        // then
        resultActions
                .andExpect(status().isNoContent());
    }

    @Test
    @Transactional
    public void updateReplyTest() throws Exception {
        // given
        long replyId = 2;
        String content = "댓글 수정";

        String url = "/reply/2";

        Reply updatedReply = new Reply();
        updatedReply.setContent(content);

        // 데이터 직렬화
        final String requestBody = objectMapper.writeValueAsString(updatedReply);

        // when
        ResultActions resultActions = mockMvc.perform(patch(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string("댓글이 수정되었습니다."));

        // 댓글 수정 후 조회하여 수정되었는지 확인
        mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(content));



    }
}
