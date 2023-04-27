import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';

import {of as observableOf, Observable, merge, startWith, switchMap, map, tap} from 'rxjs';
import { HttpClient } from '@angular/common/http';

import {MatPaginator, PageEvent} from '@angular/material/paginator';

export interface Book {
  bookId: number,
  name: string,
  authors: Author[],
  publisher: Publisher,
  year: string,
  language: string
}

export interface Publisher {
  publisherId: number,
  name: string
}

export interface Author {
  authorId: number,
  name: string
}

export interface BookResponse {
  books: Book[],
  currentPage: number,
  totalItems: number,
  totalPages: number
}

@Component({
  selector: 'app-book-list',
  templateUrl: './book-list.component.html',
  styleUrls: ['./book-list.component.scss']
})
export class BookListComponent implements AfterViewInit {

  database!: BookDatabase;
  data: Book[] = [];
  displayedColumns: string[] = ['bookId', 'title', 'authors', 'publisher', 'year', 'language'];

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  // @ViewChild(MatSort) sort!: MatSort;

  resultsLength = 0;
  isLoadingResults = true;

  searchQuery: string = '';
  searchQuerySaved: string = '';

  constructor(
    private http: HttpClient
  ) {
    this.database = new BookDatabase(this.http);
  }

  onSubmit(event: any) {
    let e = new PageEvent();

    this.searchQuerySaved = this.searchQuery;
    this.paginator.page.emit(e);
  }

  ngAfterViewInit(): void {

    this.paginator.page.pipe(
      startWith({}),
      switchMap(() => {
        this.isLoadingResults = true;

        return this.database.getBooks(
          this.paginator.pageIndex,
          this.paginator.pageSize,
          this.searchQuerySaved
        );
      }),
      map(data => {
        if(data.books === null) {
          return [];
        }

        this.isLoadingResults = false;
        this.resultsLength = data.totalItems;

        window.scrollTo(0, 0);

        return data.books;
      })
    ).subscribe(data => (this.data = data));
  }
}


class BookDatabase {

  constructor(
    private http: HttpClient
  ) {
  }
  
  getBooks(page: number, pageSize: number, searchQuery: string): Observable<BookResponse> {
    const requestUrl = new URL('http://localhost:8080/api/books');

    requestUrl.searchParams.append('page', `${page}`);
    requestUrl.searchParams.append('size', `${pageSize}`);
    requestUrl.searchParams.append('searchQuery', `${searchQuery}`);

    return this.http.get<BookResponse>(requestUrl.toString());
  }
}
