const redux = require('@reduxjs/toolkit')
const reduxLogger = require('redux-logger')
const axios = require('axios')

const createStore = redux.createStore
const combineReducers = redux.combineReducers
const applyMiddleware = redux.applyMiddleware

const logger = reduxLogger.createLogger()
const thunkMiddleware = require('redux-thunk').default

const initialState = {
  loading: false,
  users: [],
  error: undefined
}

const FETCH_USERS_REQUEST = 'FETCH_USERS_REQUEST'
const FETCH_USERS_SUCCESS = 'FETCH_USER_SUCCESS'
const FETCH_USERS_ERROR = 'FETCH_USER_ERROR'

const fetchUsersRequest = () => {
  return {
    type: FETCH_USERS_REQUEST,
  }
}

const fetchUsersSuccess = (users) => {
  return {
    type: FETCH_USERS_SUCCESS,
    payload: users
  }
}

const fetchUsersError = (error) => {
  return { 
    type: FETCH_USERS_ERROR,
    payload: error
  }
}

const reducer = (state = initialState, action) => {
  switch (action.type) {
    case FETCH_USERS_REQUEST:
      return {
        ...state,
        loading: true
      }
    case FETCH_USERS_SUCCESS:
      return {
        ...state,
        loading: false,
        users: action.payload
      }
    case FETCH_USERS_ERROR:
      return {
        ...state,
        loading: false,
        error: action.payload
      }
  }
}

const fetchUsers = () => {
  return (dispatch) => {
    dispatch(fetchUsersRequest())

    axios.get('https://jsonplaceholder.typicode.com/users')
      .then(response => {
        dispatch(fetchUsersSuccess(response.data))
      }).catch(error => {
        dispatch(fetchUsersError(error))
      })
  }
}

const store = createStore(reducer, applyMiddleware(logger, thunkMiddleware))
store.dispatch(fetchUsers())
