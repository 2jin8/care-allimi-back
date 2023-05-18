package kr.ac.kumoh.allimi.dto.invitation;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ApproveInviteDto {//facility_id, phone_num, userRole
    @NotNull(message = "user_id가 널이어서는 안됩니다")
    private Long user_id;

    @NotNull(message = "invite_id가 널이어서는 안됩니다")
    private Long invite_id;
}
