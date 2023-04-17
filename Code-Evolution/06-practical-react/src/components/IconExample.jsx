import React from 'react'

import { IconContext } from 'react-icons/lib'

import { FaReact } from "react-icons/fa"
import { MdAlarm } from "react-icons/md"


const IconExample = () => {
  return (
    <div>
      <IconContext.Provider value={{color: 'red', size: '150px'}} >
        <FaReact />
        <MdAlarm />
      </IconContext.Provider>
    </div>
  )
}

export default IconExample
