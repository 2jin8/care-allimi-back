package kr.ac.kumoh.allimi.dto.invitation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SendInvitationDto {//facility_id, phone_num, userRole
    private Long user_id;
    private Long facility_id;
    private String user_role;
}
