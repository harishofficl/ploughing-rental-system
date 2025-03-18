import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { LoadingService } from '../../services/loading/loading.service';

@Component({
  selector: 'app-content-loading',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './content-loading.component.html',
  styleUrl: './content-loading.component.css',
})
export class ContentLoadingComponent {
  constructor(public loadingService: LoadingService) {}
}
