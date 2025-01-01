package com.quocanh.doan.dto.request;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserUpdate {
    @NotNull(message = "Experience level name must be provided.")
    private String experiencelevel;

    @NotNull(message = "Name user must be provided.")
    private String name;

    @NotNull(message = "Number phone must be provided.")
    private String phone;

    @NotNull(message = "Name's university must be provided.")
    private String university;
}
