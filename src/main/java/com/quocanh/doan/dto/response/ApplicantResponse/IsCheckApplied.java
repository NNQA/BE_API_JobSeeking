package com.quocanh.doan.dto.response.ApplicantResponse;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IsCheckApplied {
    private boolean isCheckApplied;
}
