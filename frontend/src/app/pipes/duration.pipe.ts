import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'duration'
})
export class DurationPipe implements PipeTransform {
  transform(value: number): string {
    const hours = Math.max(0, Math.floor(value / 3600));
    const minutes = Math.max(0, Math.floor((value % 3600) / 60));
    if (hours === 0) {
      return `${minutes} minutes`;
    }
    return `${hours} hours ${minutes} minutes`;
  }
}