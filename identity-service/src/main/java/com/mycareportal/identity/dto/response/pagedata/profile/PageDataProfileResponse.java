package com.mycareportal.identity.dto.response.pagedata.profile;

import java.util.List;

import com.mycareportal.identity.dto.response.pagedata.PageDataResponse;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageDataProfileResponse {

    List<ProfileResponse> profileResponse;
    PageDataResponse pageData;
}
