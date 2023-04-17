import React from 'react'

import { ToastContainer, toast } from 'react-toastify'

import 'react-toastify/dist/ReactToastify.css'



const ToastExample = () => {
  const notify = () => {
    toast("Default Notification !");

      toast.success("Success Notification !", {
        position: toast.POSITION.TOP_CENTER,
        autoClose: false,
        theme: 'dark'
      })

      toast.error("Error Notification !", {
        position: toast.POSITION.TOP_LEFT
      })

      toast.warn("Warning Notification !", {
        position: toast.POSITION.BOTTOM_LEFT
      })

      toast.info("Info Notification !", {
        position: toast.POSITION.BOTTOM_CENTER
      })

      const CustomToast = ({ closeToast }) => {
        return <div>
          Something went wrong
          <button onClick={closeToast}>Close</button>
        </div>
      }

      toast(<CustomToast />, {
        position: toast.POSITION.BOTTOM_RIGHT,
      })
  }

  return (
    <div>
      <button onClick={() => notify()}>Notify</button>
      <ToastContainer />
    </div>
  )
}

export default ToastExample
