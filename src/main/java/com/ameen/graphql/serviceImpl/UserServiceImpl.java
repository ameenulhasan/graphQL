package com.ameen.graphql.serviceImpl;

import com.ameen.graphql.constant.Constant;
import com.ameen.graphql.dto.BookDto;
import com.ameen.graphql.dto.BookIdDto;
import com.ameen.graphql.dto.LoginDto;
import com.ameen.graphql.dto.OrderListDto;
import com.ameen.graphql.dto.UserDto;
import com.ameen.graphql.dto.UserListDto;
import com.ameen.graphql.exception.CustomGraphQLException;
import com.ameen.graphql.model.Book;
import com.ameen.graphql.model.Order;
import com.ameen.graphql.model.Role;
import com.ameen.graphql.model.User;
import com.ameen.graphql.model.UserRoleMapping;
import com.ameen.graphql.repository.RoleRepository;
import com.ameen.graphql.repository.UserRepository;
import com.ameen.graphql.repository.UserRoleMappingRepository;
import com.ameen.graphql.response.PageResponse;
import com.ameen.graphql.response.SuccessResponse;
import com.ameen.graphql.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository  roleRepository;
    private final UserRoleMappingRepository userRoleMappingRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           UserRoleMappingRepository userRoleMappingRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleMappingRepository = userRoleMappingRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public SuccessResponse<Object> createUser(UserDto userDto) {
        SuccessResponse<Object> successResponse = new SuccessResponse<>();
        User user;
        user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new CustomGraphQLException(Constant.USER_NOT_FOUND));
        Optional<User> userOptional = userRepository.findByEmail(userDto.getEmail());
        if (userOptional.isPresent() && !userOptional.get().getId().equals(userDto.getId())) {
            throw new CustomGraphQLException(Constant.USER_EMAIL_ALREADY_EXIST);
        }
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhoneNumber());
        user.setAddress(userDto.getAddress());user.setGender(userDto.getGender());
        if (userDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        user.setIsActive(true);
        user.setDeletedFlag(false);
        User savedUser = userRepository.save(user);
        Optional<Role> roleOptional = roleRepository.findByRoleUserIsActive("User");
        roleOptional.ifPresent(role -> {
            UserRoleMapping userRoleMapping = new UserRoleMapping();
            userRoleMapping.setUser(savedUser);
            userRoleMapping.setRole(role);
            userRoleMapping.setIsActive(true);
            userRoleMapping.setDeletedFlag(false);
            userRoleMappingRepository.save(userRoleMapping);
        });
        return successResponse;
    }
    public User login(LoginDto loginDto) {
        User user;
        Optional<User> userOptional=userRepository.findByEmail(loginDto.getEmail());
        if (userOptional.isPresent()){
            if (passwordEncoder.matches(loginDto.getPassword(), userOptional.get().getPassword())){
                user=userOptional.get();
            }else {
                throw new CustomGraphQLException(Constant.INVALID_PASSWORD);
            }
        }else {
            throw new CustomGraphQLException(Constant.INVALID_EMAIL);
        }
        return user;
    }

    @Override
    public SuccessResponse<Object> getAllUsersWithOrders() {
        SuccessResponse<Object> response = new SuccessResponse<>();
        List<User> users = userRepository.findAllActiveUsersWithOrdersAndBooks();
        Map<Long, UserListDto> userMap = new LinkedHashMap<>();
        for (User user : users) {
            UserListDto userDto = userMap.computeIfAbsent(user.getId(), id -> {
                UserListDto dto = new UserListDto();
                dto.setId(user.getId());
                dto.setName(user.getName());
                dto.setOrders(new ArrayList<>());
                return dto;
            });
            for (Order order : user.getOrders()) {
                OrderListDto orderDto = new OrderListDto();
                orderDto.setId(order.getId());
                orderDto.setQuantity(order.getQuantity());
                orderDto.setTotalPrice(order.getTotalPrice());
                Book book = order.getBook();
                if (book != null) {
                    BookDto bookDto = new BookDto();
                    bookDto.setId(book.getId());
                    bookDto.setTitle(book.getTitle());
                    bookDto.setAuthor(book.getAuthor());
                    bookDto.setPrice(book.getPrice());
                    orderDto.setBook(bookDto);
                }
                userDto.getOrders().add(orderDto);
            }
        }
        response.setStatusCode(200);
        response.setStatusMessage("Fetched all users with orders");
        response.setData(new ArrayList<>(userMap.values()));
        return response;
    }

    @Override
    public SuccessResponse<Object> getByIdUser(Long id) {
        SuccessResponse<Object> response = new SuccessResponse<>();
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    System.out.println("User not found with ID: " + id);
                    return new CustomGraphQLException("User not found");
                });
        UserListDto userListDto = new UserListDto();
        userListDto.setId(user.getId());
        userListDto.setName(user.getName());
        List<OrderListDto> orderListDtos = user.getOrders() != null ?
                user.getOrders().stream()
                        .map(order -> {
                            OrderListDto orderListDto = new OrderListDto();
                            orderListDto.setId(order.getId());
                            orderListDto.setQuantity(order.getQuantity());
                            orderListDto.setTotalPrice(order.getTotalPrice());
                            BookDto bookDto = new BookDto();
                            bookDto.setId(order.getBook().getId());
                            bookDto.setTitle(order.getBook().getTitle());
                            bookDto.setAuthor(order.getBook().getAuthor());
                            bookDto.setPrice(order.getBook().getPrice());
                            orderListDto.setBook(bookDto);
                            return orderListDto;
                        })
                        .toList(): Collections.emptyList();
        userListDto.setOrders(orderListDtos);
        response.setStatusMessage("User ID successfully retrieved");
        response.setData(userListDto);
        return response;
    }

    @Override
    public PageResponse<List<UserListDto>> userPagesSearch(String search, int pageNo, int pageSize) {
        PageResponse<List<UserListDto>> pageResponse = new PageResponse<>();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<User> userPage;
        if (search != null && !search.isBlank()) {
            userPage = userRepository.findByNameAndIsActiveTrue(search, pageable);
        } else {
            userPage = userRepository.findByIsActiveTrue(pageable);
        }
        List<UserListDto> userList = getUserListDtos(userPage);
        pageResponse.setData(userList);
        pageResponse.setTotalRecordCount(userPage.getTotalElements());
        pageResponse.setTotalPageCount(userPage.getTotalPages());
        pageResponse.setHasNext(userPage.hasNext());
        pageResponse.setHasPrevious(userPage.hasPrevious());
        return pageResponse;
    }
    @NotNull
    private static List<UserListDto> getUserListDtos(Page<User> userPage) {
        List<UserListDto> userList = new ArrayList<>();
        for (User user : userPage.getContent()) {
            UserListDto userDto = new UserListDto();
            userDto.setId(user.getId());
            userDto.setName(user.getName());
            List<OrderListDto> orderList = new ArrayList<>();
            for (Order order : user.getOrders()) {
                OrderListDto orderDto = new OrderListDto();
                orderDto.setId(order.getId());
                orderDto.setQuantity(order.getQuantity());
                orderDto.setTotalPrice(order.getTotalPrice());
                Book book = order.getBook();
                if (book != null) {
                    BookIdDto bookDto = new BookIdDto();
                    bookDto.setTitle(book.getTitle());
                    bookDto.setAuthor(book.getAuthor());
                    bookDto.setPrice(book.getPrice());
                    orderDto.setBookIdDto(bookDto);
                }
                orderList.add(orderDto);
            }
            userDto.setOrders(orderList);
            userList.add(userDto);
        }
        return userList;
    }

}
