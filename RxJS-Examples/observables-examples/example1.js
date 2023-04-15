import { Observable } from 'rxjs';

const observable = new Observable((subscriber) => {
  subscriber.next(1);
  subscriber.next(2);
  subscriber.next(3);
  setTimeout(() => {
    subscriber.next(4);
    subscriber.complete();
  }, 1000);
});

console.log('just before subscribed');

observable.subscribe({
  next(x) {
    console.log('got value: ' + x);
  },
  error(x) {
    console.log('error: ' + x);
  },
  complete() {
    console.log('done');
  }
});

console.log('just after subscribe');
