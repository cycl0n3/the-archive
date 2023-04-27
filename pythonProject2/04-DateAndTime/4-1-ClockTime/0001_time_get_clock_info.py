import textwrap
import time


available_clocks = [
    #('perf_counter', time.perf_counter),
    ('monotonic', time.monotonic),
    ('perf_counter', time.perf_counter),
    ('process_time', time.process_time),
    ('time', time.time),
]

for clock_name, func in available_clocks:
    try:
        print(
            textwrap.dedent(
                '''
                {name}:
                    adjustable: {info.adjustable}
                    implementation: {info.implementation}
                    monotonic: {info.monotonic}
                    resolution: {info.resolution}
                    current: {current}
                '''.format(
                    name=clock_name,
                    info=time.get_clock_info(clock_name),
                    current=func()
                )
            )
        )
    except Exception as err:
        print(err)
