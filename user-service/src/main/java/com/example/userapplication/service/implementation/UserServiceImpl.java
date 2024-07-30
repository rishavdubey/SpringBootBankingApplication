package com.example.userapplication.service.implementation;

import com.example.userapplication.exception.EmptyFields;
import com.example.userapplication.exception.ResourceConflictException;
import com.example.userapplication.exception.ResourceNotFound;
import com.example.userapplication.external.AccountService;
import com.example.userapplication.model.Status;
import com.example.userapplication.model.dto.*;
import com.example.userapplication.model.dto.response.Response;
import com.example.userapplication.model.entity.User;
import com.example.userapplication.model.entity.UserProfile;
import com.example.userapplication.model.external.Account;
import com.example.userapplication.model.mapper.UserMapper;
import com.example.userapplication.repository.UserRepository;
import com.example.userapplication.service.KeycloakService;
import com.example.userapplication.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    KeycloakService keycloakService;
    @Autowired
    AccountService accountService;

    private final UserMapper userMapper = new UserMapper();

    @Value("${spring.application.success}")
    private String responseCodeSuccess;

    @Value("${spring.application.not_found}")
    private String responseCodeNotFound;

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * Creates a new user.
     *
     * @param userDto The user data transfer object containing user information.
     * @return A response indicating the result of the user creation.
     * @throws ResourceConflictException If the emailId is already registered as a user.
     * @throws RuntimeException If the user with identification number is not found.
     */
    @Override
    public Response createUser(CreateUser userDto) {
        log.info("Inside Create User Line 68");

        List<UserRepresentation> userRepresentations = keycloakService.readUserByEmail(userDto.getEmailId());
        if(userRepresentations.size() > 0) {
            log.error("This emailId is already registered as a user");
            throw new ResourceConflictException("This emailId is already registered as a user");
        }

        log.info("Inside Create User Line 76");
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(userDto.getEmailId());
        userRepresentation.setFirstName(userDto.getFirstName());
        userRepresentation.setLastName(userDto.getLastName());
        userRepresentation.setEmailVerified(false);
        userRepresentation.setEnabled(false);
        userRepresentation.setEmail(userDto.getEmailId());

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setValue(userDto.getPassword());
        credentialRepresentation.setTemporary(false);
        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));

        Integer userCreationResponse = keycloakService.createUser(userRepresentation);

        log.info("Inside Create User Line 92");
        if (userCreationResponse.equals(201)) {

            List<UserRepresentation> representations = keycloakService.readUserByEmail(userDto.getEmailId());
            UserProfile userProfile = UserProfile.builder()
                    .firstName(userDto.getFirstName())
                    .lastName(userDto.getLastName()).build();

            User user = User.builder()
                    .emailId(userDto.getEmailId())
                    .contactNo(userDto.getContactNumber())
                    .status(Status.PENDING).userProfile(userProfile)
                    .authId(representations.get(0).getId())
                    .identificationNumber(UUID.randomUUID().toString()).build();

            log.info("Inside Create User Line 107");
            userRepository.save(user);
            return Response.builder()
                    .responseMessage("User created successfully")
                    .responseCode(responseCodeSuccess).build();
        }
        throw new RuntimeException("User with identification number not found");
    }

    /**
     * Retrieves all users and their corresponding details.
     *
     * @return a list of UserDto objects containing the user information
     */
    @Override
    public List<UserDto> readAllUsers() {

        List<User> users = userRepository.findAll();

        Map<String, UserRepresentation> userRepresentationMap = keycloakService.readUsers(users.stream().map(user -> user.getAuthId()).collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(UserRepresentation::getId, Function.identity()));

        return users.stream().map(user -> {
            UserDto userDto = userMapper.convertToDto(user);
            UserRepresentation userRepresentation = userRepresentationMap.get(user.getAuthId());
            userDto.setUserId(user.getUserId());
            userDto.setEmailId(userRepresentation.getEmail());
            userDto.setIdentificationNumber(user.getIdentificationNumber());
            return userDto;
        }).collect(Collectors.toList());
    }

    /**
     * Reads a user from the database using the provided authId.
     *
     * @param authId the authentication id of the user
     * @return the UserDto object representing the user
     * @throws ResourceNotFound if the user is not found on the server
     */
    @Override
    public UserDto readUser(String authId) {

        User user = userRepository.findUserByAuthId(authId).
                orElseThrow(() -> new ResourceNotFound("User not found on the server"));

        UserRepresentation userRepresentation = keycloakService.readUser(authId);
        UserDto userDto = userMapper.convertToDto(user);
        userDto.setEmailId(userRepresentation.getEmail());
        return userDto;
    }

    /**
     * Updates the status of a user.
     *
     * @param id The ID of the user.
     * @param userUpdate The updated user status.
     * @return The response indicating the success of the update.
     * @throws ResourceNotFound If the user is not found.
     * @throws EmptyFields If the user has empty fields.
     */
    @Override
    public Response updateUserStatus(Long id, UserUpdateStatus userUpdate) {

        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFound("User not found on the server"));

        if (org.training.user.service.utils.FieldChecker.hasEmptyFields(user)) {
            log.error("User is not updated completely");
            throw new EmptyFields("please updated the user", responseCodeNotFound);
        }

        if(userUpdate.getStatus().equals(Status.APPROVED)){
            UserRepresentation userRepresentation = keycloakService.readUser(user.getAuthId());
            userRepresentation.setEnabled(true);
            userRepresentation.setEmailVerified(true);
            keycloakService.updateUser(userRepresentation);
        }

        user.setStatus(userUpdate.getStatus());
        userRepository.save(user);

        return Response.builder()
                .responseMessage("User updated successfully")
                .responseCode(responseCodeSuccess).build();
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param userId the ID of the user to retrieve
     * @return the UserDto object representing the user
     * @throws ResourceNotFound if the user is not found
     */
    @Override
    public UserDto readUserById(Long userId) {

        return userRepository.findById(userId)
                .map(user -> userMapper.convertToDto(user))
                .orElseThrow(() -> new ResourceNotFound("User not found on the server"));
    }

    /**
     * Updates a user with the given ID.
     *
     * @param id The ID of the user to update.
     * @param userUpdate The updated information for the user.
     * @return The response indicating the success or failure of the update operation.
     * @throws ResourceNotFound if the user with the given ID is not found.
     */
    @Override
    public Response updateUser(Long id, UserUpdate userUpdate) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("User not found on the server"));

        user.setContactNo(userUpdate.getContactNo());
        BeanUtils.copyProperties(userUpdate, user.getUserProfile());
        userRepository.save(user);

        return Response.builder()
                .responseCode(responseCodeSuccess)
                .responseMessage("user updated successfully").build();
    }

    /**
     * Retrieves a UserDto by the given accountId.
     *
     * @param accountId The account ID of the user.
     * @return The UserDto object corresponding to the given accountId.
     * @throws ResourceNotFound If the account or user is not found on the server.
     */
    @Override
    public UserDto readUserByAccountId(String accountId) {

        ResponseEntity<Account> response = accountService.readByAccountNumber(accountId);
        if(Objects.isNull(response.getBody())){
            throw new ResourceNotFound("account not found on the server");
        }
        Long userId = response.getBody().getUserId();
        return userRepository.findById(userId)
                .map(user -> userMapper.convertToDto(user))
                .orElseThrow(() -> new ResourceNotFound("User not found on the server"));
    }

    @Override
    public Response singInUser(SingIn singIn) {
        List<UserRepresentation> userRepresentations = keycloakService.readUserByEmail(singIn.getEmail());
        if(userRepresentations==null || userRepresentations.isEmpty()) {
            log.error("This emailId is as doesn't have a user");
            throw new ResourceConflictException("This emailId is not registered as a user");
        }
        Optional<User> user=userRepository.findUserByEmailId(singIn.getEmail());
        String authId=null;
        if(user.isPresent()){
             authId=user.get().getAuthId();
             log.info("Auth Id :: {}",authId);
        }

        UserRepresentation userRepresent= keycloakService.readUser(authId);
        log.info("UserRepresent :: {}",userRepresent.toString());
         String password =userRepresent.getCredentials().get(0).getValue();
         log.info("Password :: {}",password);
        return Response.builder()
                .responseCode("205")
                .responseMessage("User LogIN successfully").build();
    }
}
