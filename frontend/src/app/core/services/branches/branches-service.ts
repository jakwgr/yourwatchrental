import { inject, Service } from '@angular/core';
import { BranchFilterCriteriaRequest } from '../../models/branch/branch-filter-criteria-request.dto';
import { HttpClient, HttpParams } from '@angular/common/http';
import { BranchResponseDTO } from '../../models/branch/branch-response.dto';
import { body } from '@primeuix/themes/aura/card';

@Service()
export class BranchesService {
    private http = inject(HttpClient);

    getBranches(criteria? : BranchFilterCriteriaRequest) {
    let params = new HttpParams();

    if (criteria?.city) {
        params = params.set('city', criteria?.city);
    }

    if (criteria?.name) {
        params = params.set('name', criteria?.name);
    }

    if (criteria?.phoneNumber) {
        params = params.set('phoneNumber', criteria?.phoneNumber);
    }

    if (criteria?.address) {
        params = params.set('address', criteria?.address);
    }

    if (criteria?.email) {
        params = params.set('email', criteria?.email);
    }

    if (criteria?.status) {
        params = params.set('status', criteria?.status);
    }

    return this.http.get<BranchResponseDTO[]>(
    'api/branches',
    { params }
);
}
}
