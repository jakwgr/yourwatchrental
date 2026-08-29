import {
  HttpClient,
  HttpParams
} from '@angular/common/http';

import {
  inject,
  Injectable
} from '@angular/core';

import { WatchCardResponseDTO } from '../../models/watches/watch-card-response.dto';

import { PageResponseDTO } from '../../models/page-response.dto';

import { WatchFilterRequestDTO } from '../../models/watches/watch-filter-request.dto';

import {
  addObjectToHttpParams
} from '../../../shared/util/http-params.util';

import { WatchFullInfoResponseDTO } from '../../models/watches/watch-full-info-response.dto';

import { WatchAvailabilityResponseDTO } from '../../models/watches/watch-availability-response.dto';

import { WatchUpdateRequestDTO } from '../../models/watches/watch-update-request.dto';

import { WatchStatusUpdateRequestDTO } from '../../models/watches/watch-status-update-request.dto';

import { WatchBranchUpdateRequestDTO } from '../../models/watches/watch-branch-update-request.dto';

import { WatchSerialNumberUpdateRequestDTO } from '../../models/watches/watch-serial-number-update-request.dto';

import { PhotoType } from '../../models/watches/photos/photo-type';

import { WatchPhotoResponseDTO } from '../../models/watches/photos/watch-photo-response.dto';

import { WatchRequestDTO } from '../../models/watches/watch-request.dto';


@Injectable({
  providedIn: 'root'
})
export class WatchesService {

  private http = inject(HttpClient);


  // =========================
  // POBIERANIE ZEGARKÓW
  // =========================

  getWatches(
    page: number = 0,
    size: number = 10,
    filter?: WatchFilterRequestDTO,
    sort: string = 'yearOfProduction,desc'
  ) {

    let params = new HttpParams()

      .set(
        'page',
        page
      )

      .set(
        'size',
        size
      )

      .set(
        'sort',
        sort
      );


    if (filter) {

      params =
        addObjectToHttpParams(
          params,
          filter
        );

    }


    return this.http.get<
      PageResponseDTO<WatchCardResponseDTO>
    >(
      '/api/watches',
      { params }
    );

  }


  // =========================
  // POJEDYNCZY ZEGAREK
  // =========================

  getWatch(
    id: string
  ) {

    return this.http.get<WatchFullInfoResponseDTO>(
      `/api/watches/${id}`
    );

  }


  // =========================
  // DOSTĘPNOŚĆ ZEGARKA
  // =========================

  getWatchAvailability(
    id: string,
    startDate: string,
    endDate: string
  ) {

    return this.http.get<WatchAvailabilityResponseDTO>(
      `/api/watches/${id}/availability`,
      {
        params: {
          startDate,
          endDate
        }
      }
    );

  }


  // =========================
  // UPDATE
  // =========================

  updateWatch(
    id: string,
    request: WatchUpdateRequestDTO
  ) {

    return this.http.put<WatchFullInfoResponseDTO>(
      `/api/watches/${id}`,
      request
    );

  }


  updateWatchStatus(
    id: string,
    request: WatchStatusUpdateRequestDTO
  ) {

    return this.http.patch<WatchFullInfoResponseDTO>(
      `/api/watches/${id}/status`,
      request
    );

  }


  updateWatchBranch(
    id: string,
    request: WatchBranchUpdateRequestDTO
  ) {

    return this.http.patch<WatchFullInfoResponseDTO>(
      `/api/watches/${id}/branch`,
      request
    );

  }


  updateWatchSerialNumber(
    id: string,
    request: WatchSerialNumberUpdateRequestDTO
  ) {

    return this.http.patch<WatchFullInfoResponseDTO>(
      `/api/watches/${id}/serial_number`,
      request
    );

  }


  // =========================
  // ZDJĘCIA
  // =========================

  uploadPhoto(
    watchId: string,
    file: File,
    photoType: PhotoType,
    description: string
  ) {

    const formData =
      new FormData();


    formData.append(
      'file',
      file
    );

    formData.append(
      'photoType',
      photoType
    );

    formData.append(
      'description',
      description
    );


    return this.http.post<WatchPhotoResponseDTO>(
      `/api/watches/photos/${watchId}`,
      formData
    );

  }


  getPhotos(
    watchId: string
  ) {

    return this.http.get<WatchPhotoResponseDTO[]>(
      `/api/watches/photos/${watchId}`
    );

  }


  deletePhoto(
    id: string
  ) {

    return this.http.delete(
      `/api/watches/photos/${id}`
    );

  }


  // =========================
  // TWORZENIE
  // =========================

  createWatch(
    request: WatchRequestDTO
  ) {

    return this.http.post<WatchFullInfoResponseDTO>(
      '/api/watches',
      request
    );

  }


  // =========================
  // MINIATURA
  // =========================

  getThumbnail(
    id: string
  ) {

    return this.http.get<WatchPhotoResponseDTO>(
      `/api/watches/photos/${id}/thumbnail`
    );

  }

}

