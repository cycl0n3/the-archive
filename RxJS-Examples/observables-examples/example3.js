import { Observable } from 'rxjs';

const foo = new Observable((subscriber) => {
  console.log('Hello');

  subscriber.next(42);
  subscriber.next(100); // "return" another value
  subscriber.next(200); // "return" yet another

  setTimeout(() => {
    subscriber.next(300); // happens asynchronously
  }, 1000);
});

console.log('before');

foo.subscribe((x) => {
  console.log(x);
});

console.log('after');
