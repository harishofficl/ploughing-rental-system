import { Component } from '@angular/core';
import { ApiService } from '../../../services/api/api.service';
import { AuthService } from '../../../services/auth/auth.service';

@Component({
  selector: 'app-job',
  templateUrl: './job.component.html',
  styleUrl: './job.component.css',
})
export class JobComponent {
  driver!: any;
  ownerId!: string;
  todayJobs: any[] = [];

  constructor(private api: ApiService, private authService: AuthService) {
    this.api.getDriverById(this.authService.currentUserId).subscribe((driver) => {
      this.driver = driver.data;
      this.ownerId = this.driver.ownerId;
    });
  }
}
