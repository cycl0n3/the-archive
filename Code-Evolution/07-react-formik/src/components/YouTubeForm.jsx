import React from 'react'

import {
  Formik,
  Form,
  Field,
  ErrorMessage,
  FieldArray
} from 'formik'

import * as Yup from 'yup'

const initialValues = {
  name: '',
  email: '',
  channel: '',
  comments: '',
  address: '',
  social: {
    twitter: '',
    facebook: '',
  },
  phoneNumbers: ['', ''],
  phNumbers: [''],
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
  channel: Yup.string().required('Required !'),
  address: Yup.string().required('Required !'),
  comments: Yup.string().required(),
})

const validateComments = (data) => {
  let error

  if (!data) {
    error = 'Required ?'
  }

  console.log('validateComments')

  return error
}

const TextError = (props) => {
  return (
    <div className='error'>
      {props.children}
    </div>
  )
}

const YouTubeForm = () => {

  return (
    <Formik
      initialValues={initialValues}
      validationSchema={validationSchema}
      onSubmit={onSubmit}>

      {formik => {
        return (
          <Form>
            <div className='form-control'>
              <label htmlFor="name">Name</label>
              <Field type='text' name="name" id="name" />
              <ErrorMessage name='name' component={TextError} />
            </div>

            <div className='form-control'>
              <label htmlFor="email">E-mail</label>
              <Field type='text' name="email" id="email" />
              <ErrorMessage name='email'>
                {(error) => {
                  return <div className='error'>{error}</div>
                }}
              </ErrorMessage>
            </div>

            <div className='form-control'>
              <label htmlFor="channel">Channel</label>
              <Field type='text' name="channel" id="channel" />
              <ErrorMessage name='channel' component={TextError} />
            </div>

            <div className='form-control'>
              <label htmlFor="comments">Comments</label>
              <Field as="textarea" name="comments" id="comments" validate={validateComments} />
              <ErrorMessage name='comments' component={TextError} />
            </div>

            <div className='form-control'>
              <label htmlFor="address">Address</label>
              <Field name='address' id='address'>
                {(props) => {
                  const { field, form, meta } = props
                  return (<>
                    <input type="text" name="address" id='address' {...field} />
                    {meta.touched && meta.error ? <span className='error'>{meta.error}</span> : undefined}
                  </>
                  )
                }}
              </Field>
            </div>

            <div className='form-control'>
              <label htmlFor="facebook">Facebook profile</label>
              <Field type='text' name='social.facebook' id='facebook' />
            </div>

            <div className='form-control'>
              <label htmlFor="twitter">Twitter profile</label>
              <Field type='text' name='social.twitter' id='twitter' />
            </div>

            <div className='form-control'>
              <label htmlFor="primaryPh">Primary phone</label>
              <Field type='text' name='phoneNumbers[0]' id='primaryPh' />
            </div>

            <div className='form-control'>
              <label htmlFor="secondaryPh">Secondary phone</label>
              <Field type='text' name='phoneNumbers[1]' id='secondaryPh' />
            </div>

            <div className='form-control'>
              <label htmlFor="phNumbers">Phone Numbers</label>
              <FieldArray name='phNumbers'>
                {(props) => {
                  const { push, remove, form } = props
                  const { values } = form
                  const { phNumbers } = values

                  return (
                    <div>
                      {phNumbers.map((phNumber, idx) => {
                        return <div key={idx}>
                          <Field name={`phNumbers[${idx}]`} />
                          <button type='button' onClick={() => remove(idx)}
                            disabled={phNumbers.length === 1}>-</button>
                          <button type='button' onClick={() => push('')}>+</button>
                        </div>
                      })}
                    </div>
                  )
                }}
              </FieldArray>
            </div>
            <p>
              <button type='submit'>Submit</button>
            </p>
          </Form>
        )
      }}
    </Formik>
  )
}

export default YouTubeForm
