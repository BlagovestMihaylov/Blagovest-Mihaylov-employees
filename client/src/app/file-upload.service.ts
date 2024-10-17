import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {TimeWorked} from './file-upload/time-worked.model';

@Injectable({
  providedIn: 'root'
})
export class FileUploadService {

  private uploadUrl = 'http://localhost:8080/upload';

  constructor(private http: HttpClient) {}

  uploadFile(file: File): Observable<TimeWorked[]> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<TimeWorked[]>(this.uploadUrl, formData);
  }
}
