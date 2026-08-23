import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import { RentalRequestDTO } from '../../models/rentals/rental-request.dto';
import { RentalResponseDTO } from '../../models/rentals/rental-response.dto';
import { PageResponseDTO } from '../../models/page-response.dto';
import { RentalFilterRequestDTO } from '../../models/rentals/rental-filter-request.dto';
import { addObjectToHttpParams } from '../../../shared/util/http-params.util';
import { PaymentStatus } from '../../models/rentals/payment-status';

@Service()
export class RentalsService {
    private http = inject(HttpClient)

    createRental(request: RentalRequestDTO)
    {
        return this.http.post<RentalResponseDTO>('/api/rentals' , request);
    }

    getMyRentals(page: number = 0, size: number = 5, filter?: RentalFilterRequestDTO)
    {
        let params = new HttpParams()
        .set('page', page)
        .set('size', size)

        if(filter) params = addObjectToHttpParams(params, filter);

        return this.http.get<PageResponseDTO<RentalResponseDTO>>('/api/rentals/my', 
            {params}
        );
    }

    getRentalsAdmin(page: number = 0, size: number = 5, filter?: RentalFilterRequestDTO)
    {
        let params = new HttpParams()
        .set('page', page)
        .set('size', size)

        if(filter) params = addObjectToHttpParams(params, filter);

        return this.http.get<PageResponseDTO<RentalResponseDTO>>('/api/rentals', 
            {params}
        );
    }

    changePaymentStatus(id:string, status: PaymentStatus)
    {
        return this.http.patch<RentalResponseDTO>(`/api/rentals/${id}/payment`,
            {
                paymentStatus: status
            }
        );
    }

    cancelRental(id:string)
    {
        return this.http.patch<RentalResponseDTO>(`/api/rentals/${id}/cancel`, null);
    }
}
