import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../../services/api/api.service';
import { AuthService } from '../../../services/auth/auth.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { WebcamImage, WebcamInitError, WebcamUtil } from 'ngx-webcam';
import { Subject, Observable } from 'rxjs';
import { saveAs } from 'file-saver';

@Component({
  selector: 'app-job',
  templateUrl: './job.component.html',
  styleUrl: './job.component.css',
})
export class JobComponent implements OnInit {
  driver!: any;
  driverId!: string;
  ownerId!: string;
  todayJobs: any[] = [];
  vehicles!: any[];
  startJobForm!: FormGroup;
  endJobForm!: FormGroup;

  jobId: string = 'idle';

  jobStarted: boolean = false;
  jobStartTime: Date | null = null;
  timerInterval: any;

  // Webcam trigger
  private trigger: Subject<void> = new Subject<void>();
  public triggerObservable: Observable<void> = this.trigger.asObservable();
  public webcamImage: WebcamImage | null = null;

  // webcam container
  public isWebcamOpen: boolean = false;
  public isPhotoCaptured: boolean = false;

  constructor(
    private api: ApiService,
    private authService: AuthService,
    private fb: FormBuilder
  ) {
    this.api
      .getDriverById(this.authService.currentUserId)
      .subscribe((driver) => {
        this.driver = driver.data;
        this.driverId = driver.data.id;
        this.ownerId = driver.data.ownerId;

        this.jobId = this.driver.currentJobId;
        if (this.jobId !== 'idle') {
          this.api.getJobById(this.jobId).subscribe((job) => {
            this.jobStarted = true;
            this.jobStartTime = new Date(job.data.startTime);
            this.startTimer();
          });
        } else {
          this.jobStarted = false;
          this.jobStartTime = null;
          clearInterval(this.timerInterval);
        }

        this.api.getVehiclesByOwnerId(this.ownerId).subscribe((vehicles) => {
          this.vehicles = vehicles.data;
        });
      });
  }

  ngOnInit() {
    this.startJobForm = this.fb.group({
      vehicleId: ['', Validators.required],
      driverId: [this.authService.currentUserId],
      ownerId: [''],
      customerName: ['', Validators.required],
      startImagePath: ['', Validators.required],
    });

    this.endJobForm = this.fb.group({
      endImagePath: ['', Validators.required],
      dieselUsed: ['', Validators.required],
    });
  }

  triggerSnapshot(): void {
    this.trigger.next();
  }

  handleImage(webcamImage: WebcamImage): void {
    this.webcamImage = webcamImage;
    this.isPhotoCaptured = true;
  }

  confirmPhoto() {
    if (this.webcamImage) {
      const imagePath = this.saveImageLocally(this.webcamImage.imageAsDataUrl);
      if (this.jobStarted) {
        this.endJobForm.patchValue({ endImagePath: imagePath });
      } else {
        this.startJobForm.patchValue({ startImagePath: imagePath });
      }
      this.closeWebcam();
    }
  }

  retakePhoto() {
    this.webcamImage = null;
    this.isPhotoCaptured = false;
  }

  saveImageLocally(imageDataUrl: string): string {
    const byteString = atob(imageDataUrl.split(',')[1]);
    const mimeString = imageDataUrl.split(',')[0].split(':')[1].split(';')[0];
    const ab = new ArrayBuffer(byteString.length);
    const ia = new Uint8Array(ab);
    for (let i = 0; i < byteString.length; i++) {
      ia[i] = byteString.charCodeAt(i);
    }
    const blob = new Blob([ab], { type: mimeString });

    const timestamp = new Date()
      .toLocaleString('en-IN', { timeZone: 'Asia/Kolkata', hour12: false })
      .replace(/:/g, '-')
      .replace(/, /g, 'T')
      .replace(/\//g, '-');

    const fileName = `image-${timestamp}.jpg`;
    const fileNameElement = document.getElementById('fileName');
    if (fileNameElement) {
      fileNameElement.textContent = fileName;
    }
    saveAs(blob, fileName);

    const filePath = `C:/Users/Harish S/Downloads/${fileName}`;
    return filePath;
  }

  startJob() {
    this.startJobForm.patchValue({ ownerId: this.ownerId });
    // save job in db
    this.api.postJob(this.startJobForm.getRawValue()).subscribe((job) => {
      this.jobId = job.data.id;
      this.jobStartTime = new Date(job.data.startTime);
      this.jobStarted = true;
      this.isPhotoCaptured = false;
      this.startJobForm.patchValue({ customerName: "" });
      this.retakePhoto();
      this.startTimer();
    });
  }

  updateTimer() {
    if (this.jobStartTime) {
      const now = new Date().getTime();
      const startTime = this.jobStartTime.getTime();
      const elapsed = now - startTime;

      const hours = Math.floor(elapsed / (1000 * 60 * 60));
      const minutes = Math.floor((elapsed % (1000 * 60 * 60)) / (1000 * 60));

      const timerElement = document.getElementById('jobTimer');
      if (timerElement) {
        timerElement.textContent = `${hours} hours ${minutes} minutes`;
      }
    }
  }

  startTimer() {
    this.timerInterval = setInterval(() => {
      this.updateTimer();
      console.log('Timer updated');
    }, 1000);
  }

  openWebcam() {
    this.isWebcamOpen = true;
  }

  closeWebcam() {
    this.isWebcamOpen = false;
  }

  endJob() {
    const jobData = this.endJobForm.getRawValue();
    this.api.endJob(this.jobId, jobData.endImagePath, jobData.dieselUsed).subscribe((job: any) => {
      this.jobId = 'idle';
      this.jobStarted = false;
      this.jobStartTime = null;
      this.isPhotoCaptured = false;
      this.retakePhoto()
      clearInterval(this.timerInterval);
      this.endJobForm.reset();
      console.log(job.data);
    });
  }

  ngOnDestroy() {
    clearInterval(this.timerInterval);
  }
}