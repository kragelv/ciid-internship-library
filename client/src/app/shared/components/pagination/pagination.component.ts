import { NgClass, NgFor, NgIf } from '@angular/common';
import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { range } from '../../utils/math.utils';

@Component({
  selector: 'app-pagination',
  imports: [NgClass, NgIf, NgFor],
  templateUrl: './pagination.component.html',
})
export class PaginationComponent implements OnInit {
  @Output() pageChange: EventEmitter<number> = new EventEmitter<number>();

  start: number = 1;
  end: number = 1;
  needFirst: boolean = false;
  needLast: boolean = false;

  readonly SIDE_COUNT = 3;
  readonly MAX_CELLS = 2 * this.SIDE_COUNT + 1;
  readonly FISRT_PAGE = 1;

  private _currentPage: number = 1;
  private _totalPages: number = 1;

  @Input()
  set currentPage(value: number) {
    this._currentPage = value;
    this.updatePagination();
  }

  get currentPage(): number {
    return this._currentPage;
  }

  @Input()
  set totalPages(value: number) {
    this._totalPages = value;
    this.updatePagination();
  }

  get totalPages(): number {
    return this._totalPages;
  }

  ngOnInit(): void {
    this.updatePagination();
  }

  private updatePagination(): void {
    const rightDiff = this._totalPages - this._currentPage;
    const leftDiff = this._currentPage - 1;
    const leftCount = Math.max(
      this.MAX_CELLS - 2 - rightDiff,
      Math.min(leftDiff, this.SIDE_COUNT - 1)
    );
    const rightCount = Math.max(
      this.MAX_CELLS - 1 - this._currentPage,
      Math.min(rightDiff, this.SIDE_COUNT - 1)
    );
    const newStart = Math.max(this._currentPage - leftCount, 1);
    const widthCount = leftCount + rightCount + 1;
    const newEnd = Math.min(newStart + widthCount, this._totalPages);
    this.start = newStart;
    this.end = newEnd;
    this.needFirst = this.start > 1;
    this.needLast = this.end <= this._totalPages;
  }

  handlePrev(): void {
    if (this._currentPage > 1) {
      this.pageChange.emit(this._currentPage - 1);
    }
  }

  handleNext(): void {
    if (this._currentPage < this._totalPages) {
      this.pageChange.emit(this._currentPage + 1);
    }
  }

  handlePageClick(page: number): void {
    this.pageChange.emit(page);
  }

  pageNumbersRange(start: number, end: number): number[] {
    return range(start, end);
  }
}
