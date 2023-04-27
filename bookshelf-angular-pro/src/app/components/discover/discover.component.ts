import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { retry, tap } from 'rxjs';


interface Response {
  'new-books': Book[],
  'existing-books': Book[]
};

interface Book {
  id: number,
  title: string
}

@Component({
  selector: 'app-discover',
  templateUrl: './discover.component.html',
  styleUrls: ['./discover.component.scss']
})
export class DiscoverComponent {

  searchQuery: string = '';
  isSearched = false;
  isLoadingResults = false;

  newBooks: Book[] = [];
  existingBooks: Book[] = [];

  constructor(
    private http: HttpClient
  ) {
  }

  onSubmit(event: any) {
    if(this.searchQuery.trim().length !== 0) {
      this.isLoadingResults = true;

      const requestUrl = new URL('http://localhost:8080/api/discover');
      requestUrl.searchParams.append('searchQuery', `${this.searchQuery}`);

      this.http.get<Response>(requestUrl.toString()).subscribe({
        next: (data) => {
          this.isSearched = true;
          this.isLoadingResults = false;

          this.newBooks = data['new-books'];
          this.existingBooks = data['existing-books'];
        },
        error: (e) => {
          console.log(e);
        },
        complete: () => {
          this.isSearched = true;
          this.isLoadingResults = false;
        }
      })
    }
  }
}
