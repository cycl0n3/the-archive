import axios from "axios";

const API = "http://5b3ded9195bf8d0014a1d806.mockapi.io/reactapi";

export function dataSubmit(data) {
    return dispatch => {
        console.log("dataSubmit called");
        axios
            .post(API, data)
            .then(result => {
                dispatch(userLogin(result.data.lemail));
                // console.log("result : ", result);
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

export const userLogin = data => {
    return {
        type: "IS_LOGIN",
        data: data,
    };
};
