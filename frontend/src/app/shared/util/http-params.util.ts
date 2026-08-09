import { HttpParams } from "@angular/common/http";

export function addObjectToHttpParams<T>(
    params: HttpParams,
    object: T
): HttpParams {
    Object.entries(object as object).forEach(([key, value]) => {
        if (value !== undefined && value !== null && value !== '') {
            params = params.set(key, String(value));
        }
    });

    return params;
}