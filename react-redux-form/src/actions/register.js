import axios from "axios";

const API = "http://5b6066f8bde36b0014081203.mockapi.io/register";

export function dataRegisterSubmit(data) {
    return dispatch => {
        console.log("dataSubmit called");
        axios
            .post(API, data)
            .then(result => {
                dispatch(userRegister(true));
                console.log("result : ", result);
            })
            .catch(error => {
                console.log("error : ", error);
            });
    };
}

export const formReset = () => {
    return {
        type: "SUBMIT_FORM_DATA_CLEAR",
    };
};

export const userRegister = data => {
    return {
        type: "IS_REGISTER",
        data: data,
    };
};
