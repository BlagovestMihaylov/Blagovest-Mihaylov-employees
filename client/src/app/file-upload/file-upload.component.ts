import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { FileUploadService } from '../file-upload.service';
import {TimeWorked} from './time-worked.model';
import {MatCard, MatCardContent, MatCardHeader, MatCardTitle} from '@angular/material/card';

@Component({
  selector: 'app-file-upload',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatCardContent, MatCardTitle, MatCardHeader, MatCard],
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.css'],
})
export class FileUploadComponent {
  selectedFile: File | null = null;
  timeWorkedList: TimeWorked[] = [];
  displayedColumns: string[] = ['firstEmployeeId', 'secondEmployeeId', 'projectId', 'daysWorkedTogether'];

  constructor(private fileUploadService: FileUploadService) {}

  onFileSelected(event: Event): void {
    const target = event.target as HTMLInputElement;
    if (target.files) {
      this.selectedFile = target.files[0];
    }
  }

  uploadFile(): void {
    if (this.selectedFile) {
      this.fileUploadService.uploadFile(this.selectedFile)
        .subscribe({
          next: (data: TimeWorked[]) => {
            this.timeWorkedList = data;
          },
          error: (err: any) => {
            console.error('Error uploading file', err);
          }
        });
    }
  }
}
