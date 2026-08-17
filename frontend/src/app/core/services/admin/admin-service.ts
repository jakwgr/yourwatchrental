import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Service } from '@angular/core';

import { UserResponseDTO } from '../../models/profile/user-response.dto';
import { PageResponseDTO } from '../../models/page-response.dto';
import { addObjectToHttpParams } from '../../../shared/util/http-params.util';
import { UserFilterCriteriaRequestDTO } from '../../models/admin/users/user-filter-criteria-request.dto';
import { UserInformationUpdateRequestDTO } from '../../models/profile/put-patch/user-information-update-request.dto';
import { UserPasswordUpdateAdminRequestDTO } from '../../models/admin/users/user-password-update-admin-request.dto';
import { UserEmailUpdateAdminRequestDTO } from '../../models/admin/users/user-email-update-admin-request.dto';
import { UserStatusChangeRequestDTO } from '../../models/admin/users/user-status-change-admin-request.dto';
import { UserRoleChangeRequestDTO } from '../../models/admin/users/user-role-change-request.dto';
@Service()
export class AdminService {
    private http = inject(HttpClient);

    getUsersAdmin(page: number = 0, size: number = 10, filter?: UserFilterCriteriaRequestDTO)
    {
        let params = new HttpParams()
        .set('page', page)
        .set('size', size)

        if(filter) params = addObjectToHttpParams(params, filter);

        return this.http.get<PageResponseDTO<UserResponseDTO>>('/api/users',
            {params}
        );
    }

    updateUserInformation(id: string, request: UserInformationUpdateRequestDTO)
    {
        return this.http.put<UserResponseDTO>(`api/users/${id}`,
            request
        )
    }

    updateUserPassword(id: string, request: UserPasswordUpdateAdminRequestDTO)
    {
        return this.http.patch<UserResponseDTO>(`api/users/admin/${id}/password`,
            request
        )
    }

    updateUserEmail(id: string, request: UserEmailUpdateAdminRequestDTO)
    {
        return this.http.patch<UserResponseDTO>(`api/users/admin/${id}/email`,
            request
        )
    }

    updateUserStatus(id: string, request: UserStatusChangeRequestDTO)
    {
        return this.http.patch<UserResponseDTO>(`api/users/${id}/status`,
            request
        )
    }

    updateUserRole(id: string, request: UserRoleChangeRequestDTO)
    {
        return this.http.patch<UserResponseDTO>(`api/users/${id}/role`,
            request
        )
    }
}
