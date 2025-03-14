import { NgClass } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-loader',
  imports: [NgClass],
  templateUrl: './loader.component.html',
})
export class LoaderComponent {
  @Input() size: number = 12;
}
