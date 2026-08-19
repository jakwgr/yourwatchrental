import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable, Service } from '@angular/core';
import { WatchCardResponseDTO } from '../../models/watches/watch-card-response.dto';
import { PageResponseDTO } from '../../models/page-response.dto';
import { WatchFilterRequestDTO } from '../../models/watches/watch-filter-request.dto';
import { addObjectToHttpParams } from '../../../shared/util/http-params.util';
import { WatchFullInfoResponseDTO } from '../../models/watches/watch-full-info-response.dto';
import { WatchAvailabilityResponseDTO } from '../../models/watches/watch-availability-response.dto';
@Service()

export class WatchesService {
    private http = inject(HttpClient);

    getWatches(page: number = 0, size: number = 10, filter?: WatchFilterRequestDTO)
    {
        let params = new HttpParams()
        .set('page', page)
        .set('size', size)

        if(filter) params = addObjectToHttpParams(params, filter);

        return this.http.get<PageResponseDTO<WatchCardResponseDTO>>('/api/watches',
            {params}
        );
    }

    getWatch(id:string)
    {
        return this.http.get<WatchFullInfoResponseDTO>(`/api/watches/${id}`)
    }

    getWatchAvailability(
        id: string,
        startDate: string,
        endDate: string,
    )
    {
        return this.http.get<WatchAvailabilityResponseDTO>(`/api/watches/${id}/availability`,
            {
                params: {
                    startDate,
                    endDate
                }
            }
        )
    }
}
