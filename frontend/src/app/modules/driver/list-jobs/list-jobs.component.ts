import { Component, Input } from '@angular/core';
import { ApiService } from '../../../services/api/api.service';

@Component({
  selector: 'app-list-jobs',
  templateUrl: './list-jobs.component.html',
  styleUrl: './list-jobs.component.css'
})
export class ListJobsComponent {
  @Input() driverId!: string;

  jobs: any[] = [];

  constructor(private api:ApiService) {}

  ngOnInit() {
    this.api.getTodayJobsByDriverId(this.driverId).subscribe((jobs) => {
      this.jobs = jobs.data;

      console.log(this.driverId);
      console.log(jobs);
      console.log(this.jobs);
    });
  }
}
