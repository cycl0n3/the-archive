import { interval, merge, take } from 'rxjs';

const numbers = interval(1000 /* number of milliseconds */);

const takeNumbers = numbers.pipe(take(4));

takeNumbers.subscribe(n => console.log(`Next: ${n}`));
