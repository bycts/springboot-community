package com.byc.community.community.service;

import com.byc.community.community.dto.QuestionDTO;
import com.byc.community.community.mapper.QuestionMapper;
import com.byc.community.community.mapper.UserMapper;
import com.byc.community.community.model.Question;
import com.byc.community.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
/*组装user的同时组装qustion适用中间层去做这件事*/
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    public List<QuestionDTO> list() {
        List<Question> questions=questionMapper.list();
        List<QuestionDTO> questionDTOList=new ArrayList<>();
        for (Question question:questions){
           User user= userMapper.findById(question.getCreator());
           QuestionDTO questionDTO=new QuestionDTO();
         /*  questionDTO.setId();*/
            //把question的属性拷贝到questionDTO
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        return  questionDTOList;
    }
}
