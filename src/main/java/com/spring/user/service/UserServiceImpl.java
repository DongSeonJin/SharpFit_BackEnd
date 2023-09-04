package com.spring.user.service;

import com.spring.user.DTO.AddUserRequestDTO;
import com.spring.user.DTO.UserUpdateDTO;
import com.spring.user.entity.User;
import com.spring.user.exception.UserIdNotFoundException;
import com.spring.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{


     private final UserRepository userRepository;
     private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public void save(AddUserRequestDTO dto) { // 회원 email, password를 저장하고 password는 암호화

         userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .build()
         );
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    @Transactional
    public void updateUser(UserUpdateDTO userUpdateDTO) {

        userUpdateDTO.toString();

        User user = userRepository.findById(userUpdateDTO.getUserId())
                .orElseThrow(() -> new UserIdNotFoundException("해당되는 글을 찾을 수 없습니다 : " + userUpdateDTO.getUserId()));

        User updatingUser = User.builder()
                .userId(userUpdateDTO.getUserId())
                .nickname(userUpdateDTO.getNickname())
                .password(userUpdateDTO.getPassword())
                .imageUrl(userUpdateDTO.getImageUrl())
                .build();

        userRepository.save(updatingUser);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

}