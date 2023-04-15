import { combineReducers } from "redux";
import LoginReducer from "./LoginReducer";
import { reducer as formReducer } from "redux-form";

export default combineReducers({
    LoginReducer,
    form: formReducer,
});
