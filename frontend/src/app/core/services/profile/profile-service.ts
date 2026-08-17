import { inject, Service } from '@angular/core';
import { UserResponseDTO } from '../../models/profile/user-response.dto';
import { HttpClient, HttpParams } from '@angular/common/http';

import { UserEmailUpdateRequestDTO } from '../../models/profile/put-patch/user-email-update-request.dto';
import { UserPasswordUpdateRequestDTO } from '../../models/profile/put-patch/user-password-update-request.dto';
import { UserInformationUpdateRequestDTO } from '../../models/profile/put-patch/user-information-update-request.dto';
import { UserSoftDeleteRequestDTO } from '../../models/profile/put-patch/user-soft-delete-request.dto';


@Service()
export class ProfileService {
    private http = inject(HttpClient);


    getMyProfile()
    {
        return this.http.get<UserResponseDTO>("/api/users/me");
    }

    updateMyEmail(request: UserEmailUpdateRequestDTO)
    {
        return this.http.patch("/api/users/me/email", 
            request
        )
    }

    updateMyPassword(request: UserPasswordUpdateRequestDTO)
    {
        return this.http.patch("/api/users/me/password",
        request
        )
    }

    updateMyInformation(request: UserInformationUpdateRequestDTO)
    {
        return this.http.put("/api/users/me",
            request
        )
    }

    softDeleteMyAccount(request: UserSoftDeleteRequestDTO)
    {
        return this.http.delete("/api/users/me",
            {body: request}
        )
    }
}
