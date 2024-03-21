package com.fzq.userservice.service;

import com.fzq.userservice.mapper.UserMapper;
import com.fzq.userservice.model.domain.User;
import com.fzq.userservice.model.domain.request.UserUpdateRequest;
import com.fzq.userservice.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;



import static com.fzq.userservice.constant.UserConstant.USER_LOGIN_STATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    public UserServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test user login
     */
    @Test
    public void testUserLogin() {
        // Arrange
        String userAccount = "userAccount";
        String userPassword = "userPassword";
        User expectedUser = new User();

        expectedUser.setUserAccount(userAccount);
        expectedUser.setUserPassword(null); // User will be desensitized

        when(userMapper.selectOne(any())).thenReturn(expectedUser);
        when(request.getSession()).thenReturn(session);


        // Act
        User actualUser = userService.userLogin(userAccount, userPassword, request);

        // Assert
        assertEquals(expectedUser, actualUser);
    }

    /**
     * Test user logout
     */
    @Test
    public void testUserLogout() {
        // Arrange
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(new User());

        // Act
        int result = userService.userLogout(request);

        // Assert
        assertEquals(1, result);
    }

    /**
     * Test user register
     */
    @Test
    public void testUserRegister() {
        // Arrange
        String userAccount = "userAccount";
        String userPassword = "userPassword";
        String checkPassword = "userPassword";
        String email = "email";
        String phone = "phone";
        String username = "username";
        Integer userRole = 0;
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(userPassword);
        user.setUsername(username);
        user.setEmail(email);
        user.setPhone(phone);
        user.setUserRole(userRole);
        when(userMapper.selectCount(any())).thenReturn(0L);
        doAnswer(invocation -> {
            User user1 = invocation.getArgument(0);
            user1.setId(1L);
            return 1;
        }).when(userMapper).insert(any());

        // Act
        long result = userService.userRegister(userAccount, username, userPassword, checkPassword, phone, email, userRole);

        // Assert
        assertEquals(1, result);
    }

    /**
     * Test get safety user
     */
    @Test
    public void testGetSafetyUser() {
        // Arrange
        User user = new User();
        user.setUserAccount("userAccount");
        user.setUserPassword("userPassword");
        user.setUsername("username");
        user.setEmail("email");
        user.setPhone("phone");

        // Act
        User safetyUser = userService.getSafetyUser(user);

        // Assert
        assertEquals("userAccount", safetyUser.getUserAccount());
        assertNull(safetyUser.getUserPassword());
        assertEquals("username", safetyUser.getUsername());
        assertEquals("email", safetyUser.getEmail());
        assertEquals("phone", safetyUser.getPhone());
    }

    /**
     * Test get current user
     */
    @Test
    public void testGetCurrentUser() {
        // Arrange
        User user = new User();
        user.setUserAccount("userAccount");
        user.setUserPassword("userPassword");
        user.setUsername("username");
        user.setEmail("email");
        user.setPhone("phone");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(USER_LOGIN_STATE)).thenReturn(user);

        // Act
        User currentUser = userService.getCurrentUser(request);

        // Assert
        assertEquals("userAccount", currentUser.getUserAccount());
        assertEquals("userPassword", currentUser.getUserPassword());
        assertEquals("username", currentUser.getUsername());
        assertEquals("email", currentUser.getEmail());
        assertEquals("phone", currentUser.getPhone());
    }

    @Test
    public void testUpdateUser() {
        // Arrange
        UserUpdateRequest user = new UserUpdateRequest();
        user.setId(1L);
        user.setUsername("newUsername");
        user.setAvatarUrl("newAvatarUrl");
        user.setGender("Male");
        user.setProfile("newProfile");
        user.setEmail("newEmail");
        user.setPhone("newPhone");
        user.setBirthday(null);
        user.setCity("Nanjing");

        User currentUser = new User();
        currentUser.setId(1L);
        currentUser.setUserAccount("currentUserAccount");
        currentUser.setUserPassword("currentUserPassword");
        currentUser.setUsername("currentUsername");
        currentUser.setUserRole(1);
        currentUser.setEmail("currentEmail");
        currentUser.setPhone("currentPhone");

        User oldUser = new User();
        oldUser.setId(user.getId());
        oldUser.setUsername(user.getUsername());
        oldUser.setAvatarUrl(user.getAvatarUrl());
        oldUser.setGender(user.getGender());
        oldUser.setProfile(user.getProfile());
        oldUser.setEmail(user.getEmail());
        oldUser.setPhone(user.getPhone());
        oldUser.setBirthday(user.getBirthday());
        oldUser.setCity(user.getCity());

        when(userMapper.selectById(user.getId())).thenReturn(oldUser);
        when(userMapper.updateById(oldUser)).thenReturn(1);

        // Act
        int result = userService.updateUser(user, currentUser);

        // Assert
        assertEquals(1, result);
    }
}
