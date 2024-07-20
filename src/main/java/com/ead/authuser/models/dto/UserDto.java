package com.ead.authuser.models.dto;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.validation.UsernameConstraint;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDto(
    Long id,

    @NotBlank(message = "Username is required", groups = UserView.RegistrationPost.class)
    @Size(min = 4, max = 20, message = "Username should have between 4 and 20 characters",
        groups = UserView.RegistrationPost.class)
    @JsonView(UserView.RegistrationPost.class)
    @UsernameConstraint(groups = UserView.RegistrationPost.class)
    String username,

    @NotBlank(message = "Email should be valid", groups = UserView.RegistrationPost.class)
    @Email(groups = UserView.RegistrationPost.class)
    @JsonView(UserView.RegistrationPost.class)
    String email,

    @NotBlank(message = "Password is required", groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class})
    @Size(min = 6, max = 20, message = "Password should have between 6 and 20 characters", groups =
        {UserView.RegistrationPost.class, UserView.PasswordPut.class})
    @JsonView({UserView.RegistrationPost.class, UserView.PasswordPut.class})
    @JsonProperty(access = WRITE_ONLY)
    String password,

    @NotBlank(message = "Old password is required", groups = UserView.PasswordPut.class)
    @Size(min = 6, max = 20, message = "Old password should have between 6 and 20 characters",
        groups = UserView.PasswordPut.class)
    @JsonView(UserView.PasswordPut.class)
    String oldPassword,

    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    String fullName,

    @JsonProperty(access = READ_ONLY)
    UserStatus userStatus,

    @JsonProperty(access = READ_ONLY)
    UserType userType,

    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    String phoneNumber,

    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    String cpf,

    @NotBlank(message = "Image URL is required", groups = UserView.ImagePut.class)
    @JsonView(UserView.ImagePut.class)
    String imageUrl
) {

    public interface UserView {

        interface RegistrationPost {

        }

        interface UserPut {

        }

        interface PasswordPut {

        }

        interface ImagePut {

        }
    }
}
