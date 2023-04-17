import React from 'react'
import CountUp, {
  useCountUp
} from 'react-countup'

const CountUpEx = () => {
  // const { getCountUp } = useCountUp({
  //   duration: 5,
  //   end: 1000
  // })

  return (
    <div>
      <h2>Count Up</h2>
      <h3>
        {0}
      </h3>
      <h3>
        <CountUp end={200} />
      </h3>
      <h3>
        <CountUp end={200} duration={5} />
      </h3>
      <h3>
        <CountUp start={500} end={1000} duration={5} prefix='$' decimals={2} />
      </h3>
      <h3>
        <CountUp start={500} end={1000} duration={5} suffix='USD' decimals={2} />
      </h3>
    </div>
  )
}

export default CountUpEx
