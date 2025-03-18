import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ContentLoadingComponent } from './content-loading.component';

describe('ContentLoadingComponent', () => {
  let component: ContentLoadingComponent;
  let fixture: ComponentFixture<ContentLoadingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ContentLoadingComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ContentLoadingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
