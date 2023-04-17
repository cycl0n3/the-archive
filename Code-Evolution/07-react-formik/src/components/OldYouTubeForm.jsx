import React from 'react'
import { useFormik } from 'formik'
import * as Yup from 'yup'

const initialValues = {
  name: '',
  email: '',
  channel: ''
}

const onSubmit = (data) => {
  console.log(data)
}

// const validate = (data) => {
//   let errors = {}

//   if (!data.name) {
//     errors.name = 'Please enter a name'
//   }

//   if (!data.email) {
//     errors.email = 'Please enter an email address'
//   } else if (!/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i.test(data.email)) {
//     errors.email = 'Invalid email format'
//   }

//   if (!data.channel) {
//     errors.channel = 'Please enter a channel'
//   }

//   return errors
// }

const validationSchema = Yup.object({
  name: Yup.string().required('Required !'),
  email: Yup.string().email('Invalid email format !!').required('Required !'),
  channel: Yup.string().required('Required !')
})

const OldYouTubeForm = () => {
  const formik = useFormik({
    initialValues,
    onSubmit,
    validationSchema
  })

  return (
    <div>
      <form onSubmit={formik.handleSubmit}>
        <div className='form-control'>
          <label htmlFor="name">Name</label>
          <input type="text" name="name" id="name"
            onChange={formik.handleChange} value={formik.values.name}
            onBlur={formik.handleBlur} />
          {formik.touched.name && formik.errors.name 
            ? <div className='error'>{formik.errors.name}</div> 
            : undefined}
        </div>
        <div className='form-control'>
          <label htmlFor="email">E-mail</label>
          <input type="text" name="email" id="email"
            onChange={formik.handleChange} value={formik.values.email} 
            onBlur={formik.handleBlur} />
          {formik.touched.email && formik.errors.email 
            ? <div className='error'>{formik.errors.email}</div> 
            : undefined}
        </div>
        <div className='form-control'>
          <label htmlFor="channel">Channel</label>
          <input type="text" name="channel" id="channel"
            onChange={formik.handleChange} value={formik.values.channel}
            onBlur={formik.handleBlur} />
          {formik.touched.channel && formik.errors.channel 
            ? <div className='error'>{formik.errors.channel}</div> 
            : undefined}
        </div>
        <p>
          <button type='submit'>Submit</button>
        </p>
      </form>
    </div>
  )
}

export default OldYouTubeForm
